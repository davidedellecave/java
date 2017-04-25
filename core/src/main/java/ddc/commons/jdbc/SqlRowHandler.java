package ddc.commons.jdbc;

import java.sql.ResultSet;

public interface SqlRowHandler {
	public void handle(long counter, ResultSet rs)  throws Exception;
}
