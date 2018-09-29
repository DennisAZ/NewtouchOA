//package org.springframework.jdbc.support;
package yh.subsys.oa.vmeet.act;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.mysql.jdbc.Driver;

/**
 * JDBC 处理的常用类,来自Spring
 * Generic utility methods for working with JDBC. Mainly for internal use within
 * the framework, but also useful for custom JDBC access code.
 * 
 * @author Thomas Risberg
 * @author Juergen Hoeller
 */
public class JdbcUtils
{	
	private static final Log logger = LogFactory.getLog(JdbcUtils.class);
	
	/**
	 * Close the given JDBC Connection and ignore any thrown exception. This is
	 * useful for typical finally blocks in manual JDBC code.
	 * 
	 * @param con the JDBC Connection to close (may be <code>null</code>)
	 */
	public static void closeConnection(Connection con)
	{
		if (con != null)
		{
			try
			{
				con.close();
			}
			catch (SQLException ex)
			{
				logger.debug("Could not close JDBC Connection", ex);
			}
			catch (Throwable ex)
			{
				// We don't trust the JDBC driver: It might throw
				// RuntimeException or Error.
				logger.debug("Unexpected exception on closing JDBC Connection",
						ex);
			}
		}
	}
	
	/**
	 * Close the given JDBC Statement and ignore any thrown exception. This is
	 * useful for typical finally blocks in manual JDBC code.
	 * 
	 * @param stmt the JDBC Statement to close (may be <code>null</code>)
	 */
	public static void closeStatement(Statement stmt)
	{
		if (stmt != null)
		{
			try
			{
				stmt.close();
			}
			catch (SQLException ex)
			{
				logger.debug("Could not close JDBC Statement", ex);
			}
			catch (Throwable ex)
			{
				// We don't trust the JDBC driver: It might throw
				// RuntimeException or Error.
				logger.debug("Unexpected exception on closing JDBC Statement",
						ex);
			}
		}
	}
	
	/**
	 * Close the given JDBC ResultSet and ignore any thrown exception. This is
	 * useful for typical finally blocks in manual JDBC code.
	 * 
	 * @param rs the JDBC ResultSet to close (may be <code>null</code>)
	 */
	public static void closeResultSet(ResultSet rs)
	{
		if (rs != null)
		{
			try
			{
				rs.close();
			}
			catch (SQLException ex)
			{
				logger.debug("Could not close JDBC ResultSet", ex);
			}
			catch (Throwable ex)
			{
				// We don't trust the JDBC driver: It might throw
				// RuntimeException or Error.
				logger.debug("Unexpected exception on closing JDBC ResultSet",
						ex);
			}
		}
	}
	
	public static ResultSet query(String sql, Connection conn)
	{
		if (conn != null && sql != null)
		{
			try
			{
				logger.info(sql);
				Statement stmt = conn.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery(sql);
				return rs;
			}
			catch (Exception ex)
			{
				logger.error("", ex);
				return null;
			}
		}
		return null;
	}
	
	public static int update(String sql, Connection conn)
	{
		if (conn != null && sql != null)
		{
			try
			{
				logger.info(sql);
				Statement stmt = conn.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				return stmt.executeUpdate(sql);
			}
			catch (Exception ex)
			{
				logger.error("", ex);
				return 0;
			}
		}
		return 0;
	}
	
	/**
	 * 获得Access数据库的连接
	 * @param accessDbPath
	 * @return
	 */
	public static Connection getConnAccess(String accessDbPath)
	{
		Connection con = null;
		String accessDriver = "oracle.jdbc.driver.OracleDriver";
		//数据库用户名
		String userName="TD_OA";
	    //密码
	    String userPasswd="test";
	    //数据库名
//	    String dbName="zlchat";
	   
	    
//	    Class.forName("oracle.jdbc.driver.OracleDriver");
//	    String url="oracle:jdbc:thin:@<数据库IP>:<端口号（默认1521）>:<数据库名>";
//	    String userName="xxx";
//	    String passwd="xxxxxx";
//	    Connection con=DriverManager.getConnection(url,userNme,passwd);//连接数据库了。

	    //数据库连接字符串
	    	String url="jdbc:oracle:thin:@localhost:1521:orcl";
	    
	    try         
		{
			Class.forName(accessDriver).newInstance();
			con = DriverManager.getConnection(url,userName,userPasswd);
			return con;
		}
		catch (Exception ex)
		{
			logger.error("", ex);
			return null;
		}
	}
	
}
