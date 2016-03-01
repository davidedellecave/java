package ddc.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ddc.util.Field;
import ddc.util.Fields;

public abstract class BaseSqlTransformer  {
	private static Logger logger = Logger.getLogger(BaseSqlTransformer.class);
	// cached value
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private Fields fields = null;
	private String sqlQuery = null;
	// private ResultSetMetaData metaData = null;
	private int lineNumber = 0;
	// configuration
	private boolean autocloseConnection = false;
	private boolean isExecuteBatchEnabled = true;
	private int itemForBatchExecution = 20000;

	public BaseSqlTransformer(boolean autocloseConnection) {
		this.autocloseConnection = autocloseConnection;
	}

	public abstract Fields createSqlFieldsOneShot();
	public abstract void updateSqlFields(Fields fields, ColInfo[] metadata, int lineNumber, String source) throws Exception;
	public abstract JdbcConnectionFactory getJdbcConnection();
	public abstract String buildSql_InsertInto(Fields fields);
	public abstract String buildSql_SelectFirstRow();
	public abstract String getTable();

	private static DataSource datasource = null;
	private static ColInfo[] metadata = null;

	private void openDataSource() {
		if (datasource == null) {
			PooledDatasourceFactory factory = new PooledDatasourceFactory();
			datasource = factory.createDataSource(getJdbcConnection());
		}
	}

	private void openMetadata() throws SQLException {
		if (metadata == null) {
			String sql = buildSql_SelectFirstRow();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			metadata = new ColInfo[meta.getColumnCount()];
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				metadata[i - 1] = new ColInfo();
				metadata[i - 1].name = meta.getColumnName(i);
				metadata[i - 1].size = meta.getColumnDisplaySize(i);
				metadata[i - 1].type = meta.getColumnType(i);
			}
		}
	}

	private void closeConnection() {
		JdbcConnectionFactory.close(preparedStatement);
		JdbcConnectionFactory.close(connection);
	}

	private Fields getSqlFields() {
		if (fields == null) {
			fields = createSqlFieldsOneShot();
		}
		return fields;
	}

	private String getSqlQuery() {
		if (sqlQuery == null) {
			sqlQuery = buildSql_InsertInto(getSqlFields());
		}
		return sqlQuery;
	}

	
	public void open() throws Exception {
		lineNumber = 0;
		openDataSource();
		connection = datasource.getConnection();
		String sql = getSqlQuery();
		logger.info("Sql template:[" + sql + "]");
		preparedStatement = connection.prepareStatement(sql);
		openMetadata();
		getSqlFields();
	}

	public void close() throws Exception {
		if (isExecuteBatchEnabled) {
			if (preparedStatement != null) {
				logger.debug("executeBatch at close lineNumber:[" + lineNumber + "]");
				preparedStatement.executeBatch();
			}
		}
		if (autocloseConnection) {
			closeConnection();
		}
	}

	public Object insert(Object item) throws Exception {
		try {
			lineNumber++;
			if (!(item instanceof String))
				return item;
			String line = (String) item;
			fields.setAllValuesTo(null);
			updateSqlFields(fields, metadata, lineNumber, line);		
			preparedStatement.clearParameters();
			int index = 1;
			for (Field f : fields) {
				if (f.isEnabled()) {
					preparedStatement.setObject(index, f.getValue(), f.getType());
					index++;
				}
			}
			executeStatement();
		} catch (Exception e) {
			logger.error("DB Writing - exception:[" + e.getMessage() + "]  line:[" + lineNumber + "] values:[" + fields.toString() + "] source:[" + item + "]");
			throw e;
		}
		//return the input item
		return item;
	}

	private void executeStatement() throws SQLException {
		if (isExecuteBatchEnabled) {
			preparedStatement.addBatch();
			if (lineNumber % itemForBatchExecution == 0) {
				logger.debug("executeBatch at line:[" + lineNumber + "]");
				preparedStatement.executeBatch();
			}
		} else {
			logger.debug("executeUpdate at line:[" + lineNumber + "]");
			preparedStatement.executeUpdate();
		}
	}

	public class ColInfo {
		public String name;
		public int type;
		public int size;
	}
}
