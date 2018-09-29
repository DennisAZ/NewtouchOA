/**
 * 
 */
package oa.spring.base;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oa.spring.db.dataSet.DataSet;
import oa.spring.db.dataSet.DataSetFactory;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author Lan
 * 
 */
public abstract class AbstractJdbcSpringDAO extends JdbcDaoSupport implements
		IDAO {
	private static Logger logger = Logger
			.getLogger(AbstractJdbcSpringDAO.class);

	public Connection getConn() {
		return this.getConnection();
	}
	public JdbcTemplate getSpringJdbcTemplate(){
		return this.getJdbcTemplate();
	}
	public int updateSql(String sql) {
		return this.getJdbcTemplate().update(sql);
	}

	public ResultSet query(String sql) throws SQLException {
		Statement osmt = getConn().createStatement();
		return osmt.executeQuery(sql);
	}

	public int queryCount(String sql) throws SQLException {
		Statement osmt = getConn().createStatement();
		ResultSet rs = osmt.executeQuery(sql);
		
		int n=0;
		if (rs.next()) {
			 n=rs.getInt(1);
			rs.close();
			rs=null;
		}
		osmt.close();
		osmt=null;
		return n;
	}
	public DataSet getTableValueByTimes(String sql,int times,int records) throws SQLException{
		DataSet ds=this.preparedQuery(sql,times,records);
		return ds;
		
	}
	public void deleteDO(IDO o) throws SystemException {
		// TODO Auto-generated method stub

	}

	public void deleteDO(Serializable key) throws SystemException {
		// TODO Auto-generated method stub

	}

	public Class getReferenceClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object insertDO(IDO o) throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertOrUpdateObject(IDO o) throws SystemException {
		// TODO Auto-generated method stub

	}

	public List queryAll() throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	public IDO queryDO(Serializable key) throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	public List queryDOs(String queryString) throws SystemException {
		// TODO Auto-generated method stub
		return this.getJdbcTemplate().queryForList(queryString);
	}

	public List queryDOs(String queryString, Object[] values)
			throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	public void refreshObject(IDO o) throws SystemException {
		// TODO Auto-generated method stub

	}

	public void updateDO(IDO o) throws SystemException {
		// TODO Auto-generated method stub

	}

	public List pageList(String sql, long allCount, long fromSize, long pageSize)
			throws SystemException {
		// TODO Auto-generated method stub

		return null;

	}

	/**
	 * ִ�в�ѯSQL�����ؽ�� <p/> //
	 * 
	 * @param string
	 * 
	 * @return List
	 */
	public DataSet queryDataSet(String sql) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = this.getConn().createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sql);

			DataSet ds = DataSetFactory.getDataSet(rs);

			return ds;

		} finally {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		}
	}

	public DataSet preparedQuery(String sql, int curPage, int pageSize)
			throws SQLException {

		if (curPage < 1) {
			curPage = 1;
		}
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = this.getConn().createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			stmt.setMaxRows(curPage * pageSize);

			rs = stmt.executeQuery(sql);
			long beginRow = (curPage - 1) * pageSize;
			for (int i = 0; i < beginRow; i++) {
				rs.next();
			}
			DataSet ds = DataSetFactory.getDataSet(rs);

			return ds;

		} finally {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		}

	}

	public DataSet callingProcess(String status) throws SQLException {

		ResultSet rs = null;
		CallableStatement proc = null;
		List list = new ArrayList();
		try {
			proc = this.getConn().prepareCall("{ call ord_query(?,?) }");
			proc.setString(1, status);
			proc.registerOutParameter(2, 1);
			// proc.setString(3, "MEMBERCODE");
			proc.execute();
			rs = (ResultSet) proc.getObject(2);
			DataSet ds = DataSetFactory.getDataSet(rs);
			rs.close();
			rs = null;
			proc.close();
			proc = null;
			return ds;
		} finally {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (proc != null) {
				proc.close();
				proc = null;
			}
		}
	}

	/**
	 * ִ���޸����
	 */
	public void execute(String sql) throws Exception {
		logger.debug("execute begin()");
		this.getJdbcTemplate().execute(sql);
	}
	
	public void update(String sql) throws Exception {
		logger.debug("execute begin()");
		this.getJdbcTemplate().update(sql);
	}
	public void delete(String sql) throws Exception {
		logger.debug("execute begin()");
		this.getJdbcTemplate().execute(sql);
	}
	

	/**
	 * ���Զ���sql���ִ�г���Ƿ��ܽ��лع�
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	public void execute(List<String> sqls) throws SQLException {

		logger.debug("execute begin()");
		
			for (int i = 0; i < sqls.size(); i++) {
				logger.error("sql:" + sqls.get(i));

				this.getJdbcTemplate().execute(sqls.get(i));
				
			}
			
	}

	public void preparedExecute(String sql,final DataSet ds) throws SQLException {
		
		int[] count = this.getJdbcTemplate().batchUpdate(sql,
                new BatchPreparedStatementSetter() {

                    @Override
                     //执行次数
                     public int getBatchSize() {
                        return ds.getRowCount();
                     }

                     @Override
                    //执行参数
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                    	 
             			while(ds.next()){
            				
          			      for(int j=0;i<ds.getColCount();i++)
          			      {
          			    	  Object obj = ds.get(i);
          						if (obj != null) {
          							ps.setObject(i + 1, obj);
          						} else {
          						
          							ps.setNull(i + 1, java.sql.Types.VARCHAR);
          						}
          			    	 
          		         // System.out.println("fileName="+ds.getName(i)+"   value="+ds.get(i));
          		          
          			     }

          			}
                     }

                 });	
		
	}
	
	
//	Oracle                                java.sql.Types 
//	blob                                     blob 
//	char                                     char 
//	clob                                     clob 
//	date                                    date 
//	number                               decimal 
//	long                                     varbinary 
//	nclob,nvarchar2                   other 
//	smallint                                smallint 
//	timestamp                            timstamp 
//	raw                                      varbinary 
//	varchar2                               varchar 
	
	public int getJavaSQLType(String typeName,Object obj){
		if(typeName.equalsIgnoreCase("blob")){
			return java.sql.Types.BLOB;
		}else if(typeName.equalsIgnoreCase("char")){
			return java.sql.Types.CHAR;
		}
//		else if(typeName.equalsIgnoreCase("clob")){
//			return java.sql.Types.CLOB;
//		}
//		else if(typeName.equalsIgnoreCase("date")){
//
//			java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd HH:mm:ss
//
//			String strDateTime =obj.toString();
//
//			try {
//	
//				java.util.Date date = format.parse(strDateTime);
//				  java.sql.Date b = new java.sql.Date(date.getTime());
//				System.out.println("----------------"+b);
//				obj=b;
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}  
//			return java.sql.Types.DATE;
//		}
//		else if(typeName.equalsIgnoreCase("number")){
//			if(obj.toString().equalsIgnoreCase(""))
//				obj=null;
//				else
//			obj=new BigDecimal(obj);
//			  
//			 return java.sql.Types.DECIMAL;
//		}
		else if(typeName.equalsIgnoreCase("long")){
			return java.sql.Types.VARBINARY;
		}
//		else if(typeName.equalsIgnoreCase("nvarchar2")){
//			return java.sql.Types.OTHER;
//		}
		else if(typeName.equalsIgnoreCase("nclob")){
			return java.sql.Types.OTHER;
		}else if(typeName.equalsIgnoreCase("smallint")){
			return java.sql.Types.SMALLINT;
		}
//		else if(typeName.equalsIgnoreCase("timestamp")){
//			return java.sql.Types.TIMESTAMP;
//		}
		else if(typeName.equalsIgnoreCase("raw")){
			return java.sql.Types.VARBINARY;
		}else if(typeName.equalsIgnoreCase("varchar2")){
			return java.sql.Types.VARCHAR;
		}
		
		 return -888;
	}
	
	public int getJavaOraSQLType(String typeName){
		if(typeName.equalsIgnoreCase("blob")){
			return java.sql.Types.BLOB;
		}else if(typeName.equalsIgnoreCase("char")){
			return java.sql.Types.CHAR;
		}
		else if(typeName.equalsIgnoreCase("clob")){
			return java.sql.Types.CLOB;
		}
		else if(typeName.equalsIgnoreCase("date")){
//
			
			return java.sql.Types.DATE;
		}
		else if(typeName.equalsIgnoreCase("number")){
			
			  
			 return java.sql.Types.DECIMAL;
		}
		else if(typeName.equalsIgnoreCase("long")){
			return java.sql.Types.VARBINARY;
		}
		else if(typeName.equalsIgnoreCase("nvarchar2")){
			return java.sql.Types.OTHER;
		}
		else if(typeName.equalsIgnoreCase("nclob")){
			return java.sql.Types.OTHER;
		}else if(typeName.equalsIgnoreCase("smallint")){
			return java.sql.Types.SMALLINT;
		}
		else if(typeName.equalsIgnoreCase("timestamp")){
			return java.sql.Types.TIMESTAMP;
		}
		else if(typeName.equalsIgnoreCase("raw")){
			return java.sql.Types.VARBINARY;
		}else if(typeName.equalsIgnoreCase("varchar2")){
			return java.sql.Types.VARCHAR;
		}
		
		 return -888;
	}
	public void updateClob(Map clobM,String id,String keyName,String TB) throws SQLException, IOException{
		if(clobM.isEmpty())
			return;
	 Statement st = this.getConn().createStatement();       
	     //插入一个空对象empty_clob()       
//	 	    st.executeUpdate("insert into "+TB+ (ID, NAME, CLOBATTR) values (1, "thename", empty_clob())");       
	 	    //锁定数据行进行更新，注意“for update”语句 
	        StringBuffer sbsql=new StringBuffer();
	        
	        Set fields= clobM.keySet();
	        Iterator it=fields.iterator();
	        int i=0;
	        List names=new ArrayList();
	        while(it.hasNext()){
	        	String name=it.next().toString();
	        	if(i>0)
	        		sbsql.append(",");
	        	sbsql.append(name);
	        	names.add(name);
	        	i++;
	        }
	 
	        String updateSql="select "+sbsql.toString()+" from "+TB +" where "+keyName+"="+id +" for update";
//	        System.out.println(updateSql);
	     ResultSet rs = st.executeQuery(updateSql);  
	     
	 	if (rs.next())       
 	    {       
 	        //得到java.sql.Clob对象后强制转换为oracle.sql.CLOB  
	 		for(int j=0;j<names.size();j++){
 	        oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(names.get(j).toString());   
 	     
// 	        if(clob==null)
// 	        	return;
	 	        Writer outStream = clob.getCharacterOutputStream();       
	  	        //data是传入的字符串，定义：String data       
	  	        char[] c = clobM.get(names.get(j).toString()).toString().toCharArray();       
	  	        outStream.write(c, 0, c.length);  
		 	    outStream.flush();       
		  	    outStream.close(); 
	 		}
	 	    }  
	 	rs.close();
	 	st.close();
	 	rs=null;
	 	st=null;
	 	clobM.clear();
//	 	con.commit();       
//	    con.close();

	}
	
	public static String readTxtFile(String tbn,String fileName,int seq) throws UnsupportedEncodingException{
		String filename="D:\\ALLBLOB\\"+tbn+"\\"+seq+"\\"+fileName;
		   
		String read;
        FileReader fileread=null;
        StringBuffer readStr=new StringBuffer();
        
        try {
//            fileread = new FileReader(filename);
            FileInputStream fis = new FileInputStream(filename);
            StringBuffer content = new StringBuffer();
            DataInputStream in = new DataInputStream(fis);
            if(in==null)
            	return null;
            BufferedReader bufread = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//            BufferedReader  bufread = new BufferedReader(fileread,"utf-8");
            try {
                while ((read = bufread.readLine()) != null) {
                    readStr.append(read+ "\r\n");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//        System.out.println("文件内容是:"+ "\r\n" + readStr);\
    
		return readStr.toString();
    }
	
	private String readtxt3(String tbn,String fileName,int seq) throws IOException{
		String filename="D:\\ALLBLOB\\"+tbn+"\\"+seq+"\\"+fileName;
		StringBuffer sb=new StringBuffer();
		try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(filename),"gbk"));
            String Line = br.readLine();
  
            while (Line != null) {
                       sb.append(Line);
                       Line = br.readLine();
            }
            br.close();
              
        } catch (IOException e) {
             // TODO Auto-generated catch block
            e.printStackTrace();
    }  
        return sb.toString();
    }

	
	public void updateBlob(Map blobM,String id,String keyName,String TB) throws SQLException, IOException{
//	1.	//获得数据库连接       
//	2.	    Connection con = ConnectionFactory.getConnection();       
//	3.	    con.setAutoCommit(false);       
//	4.	    Statement st = con.createStatement();       
//	5.	    //插入一个空对象empty_blob()       
//	6.	    st.executeUpdate("insert into TESTBLOB (ID, NAME, BLOBATTR) values (1, "thename", empty_blob())");       
//	7.	    //锁定数据行进行更新，注意“for update”语句       
//	8.	    ResultSet rs = st.executeQuery("select BLOBATTR from TESTBLOB where ID=1 for update");       
//	9.	    if (rs.next())       
//	10.	    {       
//	11.	        //得到java.sql.Blob对象后强制转换为oracle.sql.BLOB       
//	12.	        oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob("BLOBATTR");       
//	13.	        OutputStream outStream = blob.getBinaryOutputStream();       
//	14.	        //data是传入的byte数组，定义：byte[] data       
//	15.	        outStream.write(data, 0, data.length);       
//	16.	    }       
//	17.	    outStream.flush();       
//	18.	    outStream.close();       
//	19.	    con.commit();       
//	20.	    con.close();    
		if(blobM.isEmpty())
			return;
	 Statement st = this.getConn().createStatement();   
	 StringBuffer sbsql=new StringBuffer();
     
     Set fields= blobM.keySet();
     Iterator it=fields.iterator();
     int i=0;
     List names=new ArrayList();
     while(it.hasNext()){
     	String name=it.next().toString();
     	if(i>0)
     		sbsql.append(",");
     	sbsql.append(name);
     	names.add(name);
     	i++;
     }

  String updateSql="select "+sbsql.toString()+" from "+TB +" where "+keyName+"="+id +" for update";
//  System.out.println(updateSql);
  ResultSet rs = st.executeQuery(updateSql);  
  
	if (rs.next())       
  {       
      //得到java.sql.Clob对象后强制转换为oracle.sql.CLOB  
		for(int j=0;j<names.size();j++){
      oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob(names.get(j).toString());       
      OutputStream outStream = blob.getBinaryOutputStream();       
        //data是传入的byte数组，定义：byte[] data 
      int seq=Integer.valueOf(id)%100;

      byte[] data=this.readTxtFile(TB.toUpperCase(), names.get(j)+"_"+id+".txt",seq).getBytes("utf-8"); 

      outStream.write(data, 0, data.length);   
   
	 	    outStream.flush();       
	  	    outStream.close(); 
		}
   }  
	rs.close();
	st.close();
	rs=null;
	st=null;
	blobM.clear();
	}
	
	
	public List readClob(String sql,String field) throws SQLException, IOException{
	
        List returnList=new ArrayList();
	     Connection con = this.getConn();       
//	     con.setAutoCommit(false);       
	 	    Statement st = con.createStatement();       
	     //不需要“for update”       
	     ResultSet rs = st.executeQuery(sql);       
	     while (rs.next())       
	 	    {       
	         java.sql.Clob clob = rs.getClob(field);       
	 	        Reader inStream = clob.getCharacterStream();       
	 	        char[] c = new char[(int) clob.length()];       
	 	        inStream.read(c);       
	 	        //data是读出并需要返回的数据，类型是String       
	 	       returnList.add( new String(c));       
	         inStream.close();       
	 	    }       
	 	    
//	 	    con.commit();       
//	 	    con.close();   
	 	    return returnList;
	}
	
	public List readBlob(String sql,String field) throws SQLException, IOException{
		
        List returnList=new ArrayList();
	     Connection con = this.getConn();       
//	     con.setAutoCommit(false);       
	 	    Statement st = con.createStatement();       
	     //不需要“for update”       
	     ResultSet rs = st.executeQuery(sql);  
	     int READ_BUFFER_SIZE=1024;
	     String outfile="";
	     while (rs.next())       
	 	    {       
	         java.sql.Blob blob = rs.getBlob(field);     
	         BufferedInputStream bi = new BufferedInputStream(blob.getBinaryStream());
	         byte []data= new byte[READ_BUFFER_SIZE];
	         for (int len = 0; (len = bi.read(data)) != -1;) { 
	         outfile+=new String(data,"UTF-8");
	         data= new byte[READ_BUFFER_SIZE];

	         }   
	 	        
	 	        //data是读出并需要返回的数据，类型是String       
	 	       returnList.add( outfile);       
//	         inStream.close();       
	 	    }       
	 	    
//	 	    con.commit();       
//	 	    con.close();   
	 	    return returnList;
	}
	
//	public List readBlob(String sql,String field){
//	{
//		2.	    Connection con = ConnectionFactory.getConnection();       
//		3.	    con.setAutoCommit(false);       
//		4.	    Statement st = con.createStatement();       
//		5.	    //不需要“for update”       
//		6.	    ResultSet rs = st.executeQuery("select BLOBATTR from TESTBLOB where ID=1");       
//		7.	    if (rs.next())       
//		8.	    {       
//		9.	        java.sql.Blob blob = rs.getBlob("BLOBATTR");       
//		10.	        InputStream inStream = blob.getBinaryStream();       
//		11.	        //data是读出并需要返回的数据，类型是byte[]       
//		12.	        data = new byte[input.available()];       
//		13.	        inStream.read(data);       
//		14.	        inStream.close();       
//		15.	    }       
//		16.	    inStream.close(); 
//		 List returnList=new ArrayList();
//	     Connection con = this.getConn();       
////	     con.setAutoCommit(false);       
//	 	    Statement st = con.createStatement();       
//	     //不需要“for update”       
//	     ResultSet rs = st.executeQuery(sql);       
//	     while (rs.next())       
//	 	    {       
//	    	 java.sql.Blob blob =  rs.getBlob(field);       
//	    	 InputStream inStream = blob.getBinaryStream();       
//	 	        byte[] c =new byte[b.available()];       
//	 	        inStream.read(c);       
//	 	        //data是读出并需要返回的数据，类型是String       
//	 	       returnList.add( new String(c));       
//	         inStream.close();       
//	 	    }       
//	 	    
//	 	    con.commit();       
//	 	    con.close();   
//	 	    return returnList;

//	}
	
	public void preparedExecuteAll(String sql, DataSet ds,List fns,String tbn) throws SQLException {}
	
	
}
