package yh.core.funcs.connswitch.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

import yh.core.global.YHSysProps;
import yh.core.load.YHConfigLoader;
import yh.core.util.form.YHFOM;

public class YHConnSwitchLogic {

  public String getConnectingDbms(){
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    return dbms;
  }
  
  public void getSwitchDbms(String dbms,String rootPath) throws Exception{
   Properties p = new Properties();
   String dbconfig=YHSysProps.getWebInfPath()+ File.separator + "config" + File.separator + "dbconfig.properties";
   String selfconfig=YHSysProps.getWebInfPath()+ File.separator + "config" + File.separator + "selfconfig.properties";
   p.load(new FileInputStream(new File(dbconfig)));
   String dbtype="";
   if("oracle".equals(dbms)){
     dbtype="db3";
   }else if("sqlserver".equals(dbms)){
     dbtype="db1";
   }else if("mysql".equals(dbms)){
     dbtype="db2";
   }
   String dbInfo = p.getProperty(dbtype);
   Map<String,String> map = YHFOM.json2Map(dbInfo);
    Properties selprops = new Properties(); 
    selprops.load(new FileInputStream(new File(selfconfig))); 
    selprops.setProperty("db.jdbc.dbms",map.get("dbmsName")); 
    selprops.setProperty("db.jdbc.datasource.sysDsName",map.get("dsName")); 
    selprops.setProperty("sysDatabaseName",map.get("dbName")); 
    if("oracle".equals(dbms)){
      String dbName=  YHSysProps.getProp("db.jdbc.userName.oracle");
      selprops.setProperty("sysDatabaseName",dbName); 
    }
    selprops.store(new FileOutputStream(new File(selfconfig)), "");
    YHConfigLoader.loadInit(rootPath);
    
  }
  
  public String getOraConnInfo()throws Exception{
    String data="";
    String selfconfig=YHSysProps.getWebInfPath()+ File.separator + "config" + File.separator + "selfconfig.properties";
    Properties selprops = new Properties(); 
    selprops.load(new FileInputStream(new File(selfconfig))); 
    String conurl=selprops.getProperty("db.jdbc.conurl.oracle");//jdbc\:oracle\:thin\:@localhost\:1521\:orcl
    String serverName=conurl.substring(conurl.lastIndexOf(":")+1);
    conurl=conurl.substring(conurl.indexOf("@")+1);
    String hostName=conurl.substring(0,conurl.indexOf(":"));
    conurl=conurl.substring(conurl.indexOf(":")+1);
    String socket=conurl.substring(0,conurl.indexOf(":"));
    
    String uid=selprops.getProperty("db.jdbc.userName.oracle");
    //String pwd=selprops.getProperty("db.jdbc.passward.oracle");
    data="{hostName:'"+hostName+"',socket:'"+socket+"',uid:'"+uid+"',serverName:'"+serverName+"'}";
    return data;
  }
  
  
  public String getSqlConnInfo()throws Exception{
    String data="";
    Properties p = new Properties();
    String dbconfig=YHSysProps.getWebInfPath()+ File.separator + "config" + File.separator + "dbconfig.properties";
    String selfconfig=YHSysProps.getWebInfPath()+ File.separator + "config" + File.separator + "selfconfig.properties";
    Properties selprops = new Properties(); 
    selprops.load(new FileInputStream(new File(selfconfig))); 
    String conurl=selprops.getProperty("db.jdbc.conurl.sqlserver");//jdbc\:jtds\:sqlserver\://localhost\:1433;
    String socket=conurl.substring(conurl.lastIndexOf(":")+1, conurl.length());
    conurl=conurl.substring(0,conurl.lastIndexOf(":"));
    String hostName=conurl.substring(conurl.lastIndexOf("//")+2,conurl.length());
   
    String uid=selprops.getProperty("db.jdbc.userName.sqlserver");
    //String pwd=selprops.getProperty("db.jdbc.passward.oracle");
    
    //连接的数据库
    p.load(new FileInputStream(new File(dbconfig)));
    String dbInfo = p.getProperty("db1");
    Map<String,String> map = YHFOM.json2Map(dbInfo);
    String dbname=map.get("dbName");
    data="{hostName:'"+hostName+"',socket:'"+socket+"',uid:'"+uid+"',dbase:'"+dbname+"'}";
    return data;
  }
  
  
  public String getMysqlConnInfo()throws Exception{
    String data="";
    Properties p = new Properties();
    String dbconfig=YHSysProps.getWebInfPath()+ File.separator + "config" + File.separator + "dbconfig.properties";
    String selfconfig=YHSysProps.getWebInfPath()+ File.separator + "config" + File.separator + "selfconfig.properties";
    Properties selprops = new Properties(); 
    selprops.load(new FileInputStream(new File(selfconfig))); 
    String conurl=selprops.getProperty("db.jdbc.conurl.mysql");//jdbc\:mysql\://localhost\:3396/
    String socket=conurl.substring(conurl.lastIndexOf(":")+1, conurl.length()-1);
    conurl=conurl.substring(0,conurl.lastIndexOf(":"));
    String hostName=conurl.substring(conurl.lastIndexOf("//")+2,conurl.length());
   
    String uid=selprops.getProperty("db.jdbc.userName.mysql");
    //String pwd=selprops.getProperty("db.jdbc.passward.oracle");
    
  //连接的数据库
    p.load(new FileInputStream(new File(dbconfig)));
    String dbInfo = p.getProperty("db2");
    Map<String,String> map = YHFOM.json2Map(dbInfo);
    String dbname=map.get("dbName");
    data="{hostName:'"+hostName+"',socket:'"+socket+"',uid:'"+uid+"',dbsae:'"+dbname+"',dbase:'"+dbname+"'}";

    return data;
  }
  
  public String testOraConnect(Map request)throws Exception{
    //jdbc:oracle:thin:@localhost:1521:orcl
    String hostName = request.get("hostName") == null ? null : ((String[]) request.get("hostName"))[0];
    String socket = request.get("socket") == null ? null : ((String[]) request.get("socket"))[0];
    String serverName = request.get("serverName") == null ? null : ((String[]) request.get("serverName"))[0];
    String user = request.get("uid") == null ? null : ((String[]) request.get("uid"))[0];
    String password = request.get("pwd") == null ? null : ((String[]) request.get("pwd"))[0];

    String data="0";
    Connection conn=null;
    String url="jdbc:oracle:thin:@"+hostName+":"+socket+":"+serverName;

   try{
    String driverName=  YHSysProps.getProp("db.jdbc.driver.oracle");
    Class.forName(driverName).newInstance();
    conn=DriverManager.getConnection(url,user,password);
    data="1";
    conn.close();
   }catch(Exception e){
    System.out.println(e.toString());
   }
   return data;
  }
  
  
  public String testSqlConnect(Map request)throws Exception{
    String data="";
    String hostName = request.get("hostName") == null ? null : ((String[]) request.get("hostName"))[0];
    String socket = request.get("socket") == null ? null : ((String[]) request.get("socket"))[0];
    String dbase = request.get("dbase") == null ? null : ((String[]) request.get("dbase"))[0];
    String user = request.get("uid") == null ? null : ((String[]) request.get("uid"))[0];
    String password = request.get("pwd") == null ? null : ((String[]) request.get("pwd"))[0];

    String url="jdbc:jtds:sqlserver://"+hostName+":"+socket+";DatabaseName="+dbase;

    Connection conn= null;
   try{
    String driverName=  YHSysProps.getProp("db.jdbc.driver.sqlserver");
    Class.forName(driverName).newInstance();
    conn=DriverManager.getConnection(url,user,password);
    data="1";
    conn.close();
   }catch(Exception e){
    System.out.println(e.toString());
   }
   return data;
  }
  
  
  public String testMysqlConnect(Map request)throws Exception{
    String data="";
    String hostName = request.get("hostName") == null ? null : ((String[]) request.get("hostName"))[0];
    String socket = request.get("socket") == null ? null : ((String[]) request.get("socket"))[0];
    String dbase = request.get("dbase") == null ? null : ((String[]) request.get("dbase"))[0];
    String user = request.get("uid") == null ? null : ((String[]) request.get("uid"))[0];
    String password = request.get("pwd") == null ? null : ((String[]) request.get("pwd"))[0];

    String url="jdbc:mysql://"+hostName+":"+socket+"/"+dbase;

    Connection conn= null;
   try{
    String driverName=  YHSysProps.getProp("db.jdbc.driver.mysql");
    Class.forName(driverName).newInstance();
    conn=DriverManager.getConnection(url,user,password);
    data="1";
    conn.close();
   }catch(Exception e){
    System.out.println(e.toString());
   }
   return data;
  }
  
  
  

  public void saveOraConnect(Map request)throws Exception{
    //jdbc:oracle:thin:@localhost:1521:orcl
    String hostName = request.get("hostName") == null ? null : ((String[]) request.get("hostName"))[0];
    String socket = request.get("socket") == null ? null : ((String[]) request.get("socket"))[0];
    String serverName = request.get("serverName") == null ? null : ((String[]) request.get("serverName"))[0];
    String user = request.get("uid") == null ? null : ((String[]) request.get("uid"))[0];
    String password = request.get("pwd") == null ? null : ((String[]) request.get("pwd"))[0];
   
    String selfconfig=YHSysProps.getWebInfPath()+ File.separator + "config" + File.separator + "selfconfig.properties";
    
    Properties selprops = new Properties(); 
    selprops.load(new FileInputStream(new File(selfconfig))); 
    String url="jdbc:oracle:thin:@"+hostName+":"+socket+":"+serverName;
    selprops.setProperty("db.jdbc.conurl.oracle", url);
    selprops.setProperty("db.jdbc.userName.oracle", user);
    selprops.setProperty("db.jdbc.passward.oracle", password);
    selprops.store(new FileOutputStream(new File(selfconfig)), "");
  }
  
  
  public void saveSqlConnect(Map request)throws Exception{
    String data="";
    String hostName = request.get("hostName") == null ? null : ((String[]) request.get("hostName"))[0];
    String socket = request.get("socket") == null ? null : ((String[]) request.get("socket"))[0];
    String dbase = request.get("dbase") == null ? null : ((String[]) request.get("dbase"))[0];
    String user = request.get("uid") == null ? null : ((String[]) request.get("uid"))[0];
    String password = request.get("pwd") == null ? null : ((String[]) request.get("pwd"))[0];
 
    String selfconfig=YHSysProps.getWebInfPath()+ File.separator + "config" + File.separator + "selfconfig.properties";
    Properties selprops = new Properties(); 
    selprops.load(new FileInputStream(new File(selfconfig))); 
    String url="jdbc:jtds:sqlserver://"+hostName+":"+socket+";DatabaseName="+dbase;
    selprops.setProperty("db.jdbc.conurl.sqlserver", url);
    selprops.setProperty("db.jdbc.userName.sqlserver", user);
    selprops.setProperty("db.jdbc.passward.sqlserver", password);
    selprops.store(new FileOutputStream(new File(selfconfig)), "");
    
    String dbconfig=YHSysProps.getWebInfPath()+ File.separator + "config" + File.separator + "dbconfig.properties";
    Properties p = new Properties();
    p.load(new FileInputStream(new File(dbconfig)));
    String dbInfo = p.getProperty("db1");
    Map<String,String> map = YHFOM.json2Map(dbInfo);
    map.remove("dbName");
    map.put("dbName", dbase);
    StringBuffer dbConfigInfo=YHFOM.toJson(map);
    p.setProperty("db1", dbConfigInfo.toString());
    p.store(new FileOutputStream(new File(dbconfig)), "");
  }
  
  
  public void saveMysqlConnect(Map request)throws Exception{
    String data="";
    String hostName = request.get("hostName") == null ? null : ((String[]) request.get("hostName"))[0];
    String socket = request.get("socket") == null ? null : ((String[]) request.get("socket"))[0];
    String dbase = request.get("dbase") == null ? null : ((String[]) request.get("dbase"))[0];
    String user = request.get("uid") == null ? null : ((String[]) request.get("uid"))[0];
    String password = request.get("pwd") == null ? null : ((String[]) request.get("pwd"))[0];

    String url="jdbc:mysql://"+hostName+":"+socket+"/"+dbase;

    String selfconfig=YHSysProps.getWebInfPath()+ File.separator + "config" + File.separator + "selfconfig.properties";
    Properties selprops = new Properties(); 
    selprops.load(new FileInputStream(new File(selfconfig))); 
    selprops.setProperty("db.jdbc.conurl.mysql", url);
    selprops.setProperty("db.jdbc.userName.mysql", user);
    selprops.setProperty("db.jdbc.passward.mysql", password);
    selprops.store(new FileOutputStream(new File(selfconfig)), "");
    
    String dbconfig=YHSysProps.getWebInfPath()+ File.separator + "config" + File.separator + "dbconfig.properties";
    Properties p = new Properties();
    p.load(new FileInputStream(new File(dbconfig)));
    String dbInfo = p.getProperty("db2");
    Map<String,String> map = YHFOM.json2Map(dbInfo);
    map.remove("dbName");
    map.put("dbName", dbase);
    StringBuffer dbConfigInfo=YHFOM.toJson(map);
    p.setProperty("db2", dbConfigInfo.toString());
    p.store(new FileOutputStream(new File(dbconfig)), "");
  }
  
  
  
}
