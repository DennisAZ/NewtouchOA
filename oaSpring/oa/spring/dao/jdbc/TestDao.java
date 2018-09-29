package oa.spring.dao.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import oa.spring.base.AbstractJdbcSpringDAO;
import oa.spring.base.TableFieldVO;
import oa.spring.db.dataSet.DataSet;

import org.springframework.jdbc.CannotGetJdbcConnectionException;



public class TestDao extends AbstractJdbcSpringDAO{
	public List getColumnsByTableName(String tableName) throws CannotGetJdbcConnectionException, SQLException {
		List list=new ArrayList();
		  Statement   stmt=this.getConnection().createStatement();   
          String   sql="select   *   from   "+tableName +" limit 1";   
          ResultSet   rs=stmt.executeQuery(sql);   
          ResultSetMetaData   rsmd   =   rs.getMetaData();   
          int    colCount   =   rsmd.getColumnCount();  
          for(int i=1;i<colCount+1;i++){
        	  TableFieldVO vo=new TableFieldVO(); 
        	  vo.setFieldName(rsmd.getColumnName(i));
        	  vo.setFieldType(rsmd.getColumnType(i));
        	  vo.setFieldType(rsmd.getColumnDisplaySize(i));
        	  list.add(vo);
          }
     
        return list;
	}

	public ResultSet getSchemas() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getTables(Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		List tables=new ArrayList();
		if(conn==null) conn=this.getConn();
		DatabaseMetaData meta = conn.getMetaData();
		   ResultSet rs = meta.getTables(null, null, null,
		     new String[] { "TABLE" });
		   
		   while (rs.next()) {
		     System.out.println(rs.getString(3));
		     tables.add(rs.getString(3));
		     System.out.println(rs.getString(2));
		     System.out.println("------------------------------");
		   }
		return tables;
	}
	public DataSet getTableValue(String sql) throws SQLException{
		DataSet ds=this.queryDataSet(sql);
		return ds;
		
	}
	public DataSet getTableValueByTimes(String sql,int times,int records) throws SQLException{
		DataSet ds=this.preparedQuery(sql,times,records);
		return ds;
		
	}
	
	public List getDBInfo() throws CannotGetJdbcConnectionException, SQLException {
		// TODO Auto-generated method stub
		
		List oList=new ArrayList();
		oList.add(this.getConnection().getCatalog());
		oList.add(this.getConn().getMetaData());
		oList.add(this.getConnection().getMetaData().getCatalogs());
		
		return oList;
	}
}
