/**
 * 
 */
package oa.core.funcs.bbs.act;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.notify.logic.YHNotifyManageLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

/**
 * @author lanjinsheng
 *
 */
public class DBManager {

	//用户名

	private String user = "root";

	//密码

	private String password = "myoa888";

	//主机

	private String host = "10.1.100.52:3396";

	//数据库名字

	private String database = "yh";

	



	/*

	private String url="jdbc:mysql://"+host+"/"+"useUnicode=true&characterEncoding=GB2312";

	*/

	private String url ="";

	private Connection con = null;
	Statement stmt=null;

	public DBManager(HttpServletRequest request) throws Exception {
		//显示中文
           try{
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      con = requestDbConn.getSysDbConn();
		  	stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,

		  			ResultSet.CONCUR_READ_ONLY);
		     
		    } catch (Exception ex) {
		    	ex.printStackTrace();
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		      throw ex;
		    }
		

		}


	
	/**

	* 返回取得的连接

	*/

	public Connection getCon() {

	return con;

	}

	/**

	* 执行一条简单的查询语句

	* 返回取得的结果集

	*/

	public ResultSet executeQuery(String sql) {

	ResultSet rs = null;

	try {

	rs = stmt.executeQuery(sql); 

	}

	catch (SQLException e) {

	e.printStackTrace();

	}

	return rs;

	}

	/**

	* 执行一条简单的更新语句

	* 执行成功则返回true
	 * @throws SQLException 

	*/

	public boolean executeUpdate(String sql) throws SQLException {

	boolean v = false;



	v = stmt.executeUpdate(sql) > 0 ? true : false;
    this.con.commit();



    return v;



	}
	public void closeAll(ResultSet rs){
		try {
			if(rs==null)
				return;
			rs.close();
			rs=null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				rs=null;
				

		}
	}
	public void closeAll() throws SQLException{
		try {
			
			if(stmt!=null){
				stmt.close();
				stmt=null;
			}
			if(con!=null)
			{
				con.close();
				con=null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				if(stmt!=null){
					stmt.close();
					stmt=null;
				}
				if(con!=null)
				{
					con.close();
					con=null;
				}

		}
	}
	
	public void executePrepared(String sql,List array) throws SQLException{
		PreparedStatement ps = null;
//		Connection conn = this.getConn();
//		ds.first();
		try {
			ps = this.con.prepareStatement(sql);
			Iterator it=array.iterator();
			int i=1;
			while(it.hasNext()){
				Object obj=it.next();
				ps.setObject(i, obj);
				i++;
			}
			ps.executeUpdate();
			con.commit();
			ps.close();
		    ps = null;
	} catch (Exception ex) {
		ex.printStackTrace();
		
	} finally {
		if (ps != null) {
			ps.close();
			ps = null;
		}
	}
	
	}
	public int executePreparedReturnKey(String sql,List array,String keySql) throws SQLException{
		PreparedStatement ps = null;
//		Connection conn = this.getConn();
//		ds.first();
		int rt=0;
		try {
			
			
			if (!keySql.equals("")) {
				ps = con.prepareStatement(keySql);
				//ptmt = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();	
//				System.out.println("插入表成功！！");

				if(rs.next()){
				rt = rs.getInt(1);
				
//				System.out.println(rt);
				}	
				
				
				ps = this.con.prepareStatement(sql);
				if (!keySql.equals("")) {
					 ps.setObject(1, rt);		
				}
				Iterator it=array.iterator();
				int i=1;
				while(it.hasNext()){
					Object obj=it.next();
				
				    	System.out.println(i+1);
				    	if (Date.class.isInstance(obj)
				                || java.util.Date.class.isInstance(obj)) { // 映射Date类型
				              try {
				                ps.setDate(i + 1,  (Date)obj);
				              } catch (Exception e) {
				                Timestamp sqlDate = YHUtility.parseTimeStamp(((java.util.Date)obj).getTime());
				                ps.setTimestamp(i + 1, sqlDate);
				              }
				            } 
				    	else{
				    	ps.setObject(i+1, obj);
				    	}
				 
					i++;
				}
				if(ps.executeUpdate()>0){
					
				}
			}else{
			ps = this.con.prepareStatement(sql);
			Iterator it=array.iterator();
			int i=1;
			while(it.hasNext()){
				Object obj=it.next();
				ps.setObject(i, obj);
				i++;
			}
			if(ps.executeUpdate()>0){
				ps = con.prepareStatement("Select @@identity as keyid");
				//ptmt = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();	
//				System.out.println("插入表成功！！");

				if(rs.next()){
				rt = rs.getInt("keyid");
//				System.out.println(rt);
				}	
			}
			
			}
			
			
		this.con.commit();
			ps.close();
		ps = null;
	} catch (Exception ex) {
		ex.printStackTrace();
		
	} finally {
		if (ps != null) {
			ps.close();
			ps = null;
		}
	}
	return rt;
	
	}
}
