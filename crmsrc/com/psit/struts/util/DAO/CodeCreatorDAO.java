package com.psit.struts.util.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.psit.struts.util.JdbcCon;

/**
 * 
 * 编号生成器DAO实现类 <br>
 *
 * create_date: Aug 20, 2010,2:08:58 PM<br>
 * @author zjr
 */
public class CodeCreatorDAO {
	/**
	 * 
	 * 得到最大的编号 <br>
	 * create_date: Aug 20, 2010,2:10:03 PM <br>
	 * @param tableName 表名
	 * @param prefix 前缀
	 * @param lockTabName 锁表
	 * @return String 返回最大编号<br> 
	 */
	public String getMax(String tableName, String prefix ,String lockTabName) {
		String max = "";
		JdbcCon jdbcCon=new JdbcCon();
		Connection conn=jdbcCon.getConn();
		PreparedStatement pstm1 = null;   
        PreparedStatement pstm2 = null; 
        ResultSet rs = null;
		try {
//			String lockStr=" select table_max from "+ lockTabName +" (tablockx)"; //MS-SQL
			String lockStr="select table_max from "+ lockTabName +" where table_name='" + tableName + "' for update ";//mysql   
			conn.setAutoCommit(false);
			pstm1=conn.prepareStatement(lockStr);
			rs=pstm1.executeQuery();
			rs.next();
			Long value=rs.getLong("table_max")+1;
		    max = String.valueOf(value);
		    String sql ="update lock_table set table_max="+value+" where table_name='" + tableName + "'";
		    pstm2=conn.prepareStatement(sql);
		    pstm2.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);
			
			conn.close();
		} catch (SQLException ex) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ex.printStackTrace();
		}
		finally{
            try {   
                if (rs!= null)   
                    rs.close();  
                if (pstm1 != null)   
                    pstm1.close();   
                if (pstm2 != null)   
                    pstm2.close(); 
            	if (conn != null)
            		conn.close();
            } catch (SQLException e1) {   
            }   
        } 
		
		return max;
	}
}