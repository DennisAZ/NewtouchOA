package oa.spring.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.CannotGetJdbcConnectionException;

/*
 * 
 * author: lanjinsheng
 * 
 * */
public interface DBInfoInf {
 public List getDBInfo()throws CannotGetJdbcConnectionException, SQLException ;
 public ResultSet getSchemas();
 public List getTables(Connection conn) throws SQLException;
 public List getColumnsByTableName(String tableName) throws CannotGetJdbcConnectionException, SQLException ;
}
