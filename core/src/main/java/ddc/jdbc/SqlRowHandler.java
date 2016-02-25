package ddc.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SqlRowHandler {
	public void handle(long counter, ResultSet rs)  throws Exception;
}
