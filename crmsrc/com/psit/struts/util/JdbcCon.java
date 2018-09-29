package com.psit.struts.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Jdbc连接数据库 <br>
 * create_date: Aug 20, 2010,4:52:01 PM<br>
 * @author zjr
 */
public class JdbcCon {
	protected Connection conn = null;
	protected Statement stmt = null;
	protected CallableStatement proc = null;
	protected ResultSet rs = null;
	//MS-SQL
	/*
	private static final String REMOTE_URL="jdbc:sqlserver://221.231.140.176:1433;DatabaseName=beone";
	private static final String REMOTE_USER="beone";
	private static final String REMOTE_PW="4515440";
	private static final String REMOTE_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	*/
	//MYSQL
//	private static final String REMOTE_URL="jdbc:mysql://frcrm.gicp.net:3306/frsoft";
//	private static final String REMOTE_USER="frsoftReg";
//	private static final String REMOTE_PW="4515440";
	private static final String REMOTE_DRIVER="com.mysql.jdbc.Driver";
	
	private static final String R_TAB="reg_company";
	private static final String R_USER_NAME="user_name";
	private static final String R_USER_PWD="user_pass_word";
	private static final String R_USER_SIZE="user_size";
	private static final String R_S_DATE="user_start_date";
	private static final String R_E_DATE="user_end_date";
	private static final String R_TYPE="user_type";
	private static final String R_STATE="user_state";
	
	String driverName = "";
	String sqlUrl = "";
	String sqlUser = "";
	String sqlPw = "";
	
	public JdbcCon(){
		driverName = GetAppXml.getInstance().getDriverName();
		sqlUrl = GetAppXml.getInstance().getDbUrl();
		sqlUser = GetAppXml.getInstance().getDbUser();
		sqlPw = GetAppXml.getInstance().getDbPassword();
	}
	
	/**
	 * 打开一个连接 <br>
	 * create_date: Aug 20, 2010,4:52:38 PM <br> void<br>
	 */
	protected void openConn() {
		Connection aConn = null;
		try {
			Class.forName(driverName);
			//端口，数据库名，用户名，密码
			aConn = DriverManager.getConnection(sqlUrl, sqlUser, sqlPw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.conn = aConn;
	}
	/**
	 * 
	 * 获取连接 <br>
	 * create_date: Aug 20, 2010,4:54:33 PM <br>
	 * @return Connection<br>
	 */
	public Connection getConn()
	{   
		this.openConn();
		return conn;
	}

	private void getRomoteConn() throws ClassNotFoundException,SQLException {
//		Connection aConn = null;
//		Class.forName(REMOTE_DRIVER);
//		aConn = DriverManager.getConnection(REMOTE_URL, REMOTE_USER, REMOTE_PW);
//		this.conn = aConn;
	}
	/**
	 * 
	 * 执行查询sql <br>
	 * create_date: Aug 20, 2010,4:57:34 PM <br>
	 * @param sql 查询语句
	 * @return ResultSet 返回结果集<br>
	 */
	public ResultSet querySql(String sql) {
		this.openConn();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE );
			rs = stmt.executeQuery(sql);
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return rs;
	}
	/**
	 * 
	 * 执行查询sql（远程） <br>
	 * create_date: Aug 20, 2010,4:57:34 PM <br>
	 * @param sql 查询语句
	 * @return ResultSet 返回结果集<br>
	 */
	private ResultSet getResultSet(String sql) throws ClassNotFoundException,SQLException {
		this.getRomoteConn();
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		return rs;
	}
	/**
	 * 
	 * 获取远程表company信息 <br>
	 * create_date: Aug 20, 2010,5:01:08 PM <br>
	 * @param userName 企业名称
	 * @param passWord 密码
	 * @return String[] 返回company信息数组<br>
	 */
	public String[] getInitInfor(String userName,String passWord) throws ClassNotFoundException,SQLException
	{
		//ResultSet rs=getResultSet("select user_num from company where user_name='"+userName+"'");
		ResultSet rs=getResultSet("select "+R_USER_SIZE+","+R_S_DATE+","+R_E_DATE+","+R_TYPE
					+" from "+R_TAB+" where "+R_STATE+"='1' and "+R_USER_NAME+"='"+userName+"'"
					+" and "+R_USER_PWD+"='"+passWord+"'");
		String initInfo[]=new String[4];
		try {
			while(rs.next())
			{
				initInfo[0]=rs.getString(R_USER_SIZE);
				initInfo[1]=rs.getString(R_E_DATE);
				initInfo[2]=rs.getString(R_S_DATE);
				initInfo[3]=rs.getString(R_TYPE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return initInfo;
	}
	/**
	 * 
	 * 判断名称和账号 <br>
	 * create_date: Aug 20, 2010,5:04:13 PM <br>
	 * @param userName 企业名称
	 * @param passWord 密码
	 * @return boolean 判断成功返回true，否则返回false<br>
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean check(String userName,String passWord) throws ClassNotFoundException, SQLException
	{
        ResultSet rs=getResultSet("select "+R_USER_PWD+" from "+R_TAB
        			+" where "+R_STATE+"='1' and "+R_USER_NAME+"='"+userName+"'");
    	try {
			if(rs.next())
			{			
				try {
					    if(passWord.equals(rs.getString(R_USER_PWD)))
					    {					    	
					    	return true;
					    }						
					 } 
				  catch (Exception e) 
				    {						
						e.printStackTrace();
					}
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		   
	 return false;
	}
	/**
	 * 
	 * 更新表company <br>
	 * create_date: Aug 20, 2010,5:09:36 PM <br>
	 * @param userName 企业名称
	 * @param passWord 密码<br>
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void updUserState(String userName,String passWord) throws ClassNotFoundException, SQLException
	{
		this.getRomoteConn();
		try {
			//String newPassWord=passWord+"#み@ち$%そぬ";
			String sql="update "+R_TAB+" set "+R_STATE+"='0' "
					+"where "+R_USER_NAME+"='"+userName+"'and "+R_USER_PWD+"='"+passWord+"'";
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * 
	 *  执行更新sql <br>
	 * create_date: Aug 20, 2010,5:12:52 PM <br>
	 * @param sql sql语句
	 * @return int 返回执行状态值<br>
	 */
	public int updateSql(String sql) {
		int count=0;
		this.openConn();
		try {
			stmt = conn.createStatement();
			count = stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return count;
	}
	/**
	 * 
	 * 执行存储过程 <br>
	 * create_date: Aug 20, 2010,5:15:28 PM <br>
	 * @param procString 存储过程名称
	 * @param params 存储过程所需参数
	 * @return 返回存储过程结果集
	 * @throws Exception ResultSet<br>
	 */
	public ResultSet callProcedure(String procString,String params) throws Exception {
	    this.openConn();
	    try {
	    	proc = conn.prepareCall(procString,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	    	if(params!=null && !params.equals("")){
	    		proc.setObject(1, params);
	    	}
	    	proc.execute(); 
	        ResultSet rs = proc.getResultSet();
	        return rs;
	    } catch (SQLException e) {   
	        e.printStackTrace();
	        proc.close();
	        throw new Exception("调用存储过程的时候发生错误[sql = " + procString + "]", e);   
	    }
	}
	/**
	 * 
	 * 关闭所有链接 <br>
	 * create_date: Aug 20, 2010,5:43:23 PM <br> void<br>
	 */
	public void closeAll() {

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmt = null;
		}
        if(proc!=null)
        {
        	try {
				proc.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}
	
	/*public static void main(String[]args){
		new JdbcCon().getRomoteConn();//测试远程服务器
	}*/
}
