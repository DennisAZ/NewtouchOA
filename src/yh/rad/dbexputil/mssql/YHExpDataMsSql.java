package yh.rad.dbexputil.mssql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.junit.Test;

import com.sun.jmx.snmp.Timestamp;

import yh.core.data.YHDsType;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHConfigLoader;
import yh.core.util.TestDbUtil;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;


public class YHExpDataMsSql {
  private static final String increment = "SEQ_ID";
  private static final String isNull = "YES";
  private static String selfConfigFile = "D:\\project\\yh\\webroot\\yh\\WEB-INF\\config\\selfconfig.properties";
  private static String sysConfigFile = "D:\\project\\yh\\webroot\\yh\\WEB-INF\\config\\sysconfig.properties";
  
  static {
    YHSysProps.setProps(YHConfigLoader.loadSysProps(sysConfigFile));
    if(new File(selfConfigFile).exists()){
      YHSysProps.addProps(YHConfigLoader.loadSysProps(selfConfigFile));
    }
  }
  public static Connection getDbConn() throws Exception{
    return TestDbUtil.getConnection(false, "TEST");
  }
  public static Connection getDbConn(String dbType) throws Exception{
    if("mysql".equals(dbType)){
      return TestDbUtil.getConnection(false, "yh");
    }else {
      return TestDbUtil.getConnection(false, "TEST");
    }
  }
  
  public static Connection getDbConn(String dbType,String name) throws Exception{
    if("mysql".equals(dbType)){
      YHDBUtility dUtility = new YHDBUtility("mysql");
      return dUtility.getConnection(false, name);
    }else if("sqlserver".equals(dbType)) {
      YHDBUtility dUtility = new YHDBUtility("sqlserver");
      return dUtility.getConnection(false, name);
    }else {
      YHDBUtility dUtility = new YHDBUtility("oracle");
      return dUtility.getConnection(false, name);
    }
  }
  /**
   * 取得所有的数据列表
   * @param conn
   * @param schemaPattern
   * @return
   * @throws Exception
   */
  public static ArrayList<String> getTables(Connection conn,String  schemaPattern,boolean debug) throws Exception{
   return getTables(conn, schemaPattern, null,debug);
  }
  /**
   * 取得所有的数据列表
   * @param conn
   * @param schemaPattern
   * @return
   * @throws Exception
   */
  public static ArrayList<String> getTables(Connection conn,String  schemaPattern,String tablePattern,boolean debug) throws Exception{
    if(debug){
      if(tablePattern == null){
        System.out.println("正在取得 " + schemaPattern + "用户 所有的数据库表名称...");
      }else{
        System.out.println("正在取得 " + schemaPattern + "用户 符合条件<" + tablePattern + ">的数据库表名称...");
      }
    }
    ArrayList<String> result = new ArrayList<String>();
    DatabaseMetaData dbmd = conn.getMetaData();
    ResultSet rs = null;
    try {
      rs = dbmd.getTables(null, schemaPattern, tablePattern,new String[]{"TABLE"});
      while (rs.next()) {
        String tableName = rs.getString("TABLE_NAME");
        if(notActivTable().contains(tableName)){
          continue;
        }
        result.add(tableName);
      }
    } catch (Exception e) {
      throw e;
    }
    if(debug){
      System.out.println("共取得 " + result.size() + " 张表的表名称.");
    }
   return result;
  }
  /**
   * 得到所有的建表语句
   * @param conn
   * @param schemaPattern
   * @param tables
   * @param debug
   * @return
   * @throws Exception
   */
  public static void createTableSQL(Connection conn,String  schemaPattern,ArrayList<String> tables,FileOutputStream fio,boolean debug) throws Exception{
    String createSql = "";
    for (int i = 0; i < tables.size(); i++) {
      String tableNamePattern = tables.get(i);
      createSql = createTableSQL(conn, schemaPattern, tableNamePattern, debug);
      byte[] createBytes = createSql.getBytes(YHConst.DEFAULT_CODE);
      fio.write(createBytes);
    }
    fio.flush();
  }
  /**
   * 创建mysql数据库
   * @param conn2
   * @param dbName
   * @param debug
   * @throws Exception
   */
  public static void createDataBase(Connection conn2,String dbName,boolean debug) throws Exception{
    Statement smt = null;
    String sql = "create database " + dbName + ";";
    try {
      smt = conn2.createStatement();
      smt.executeUpdate(sql);
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(smt, null, null);
    }
  }
  /**
   * 建表语句
   * @param conn
   * @param conn2
   * @param schemaPattern
   * @param tables
   * @param debug
   * @throws Exception
   */
  public static void createTableSQL(Connection conn,Connection conn2,String  schemaPattern,String schemaPattern2, ArrayList<String> tables,boolean debug) throws Exception{
    String createSql = "";
    Statement smt = null;
    
    try {
      smt = conn2.createStatement();
      smt.execute("USE [" + schemaPattern2 + "];");
      for (int i = 0; i < tables.size(); i++) {
        String tableNamePattern = tables.get(i);
       // String header = " drop table if exists " +  tableNamePattern + "";
        String header =  " if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[" + tableNamePattern + "]') and OBJECTPROPERTY(id, N'IsUserTable') = 1) " +
        " drop table [dbo].[" + tableNamePattern + "] ;";
        smt.execute(header);
        conn2.commit();
        createSql = createTableSQL(conn, schemaPattern, tableNamePattern, debug);
        System.out.println("正在生成 " + tableNamePattern + "表结构!");
        System.out.println(createSql);
        smt.executeUpdate(createSql);
        conn2.commit();
      }
      //smt.executeBatch();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(smt, null, null);
    }
  }
  /**
   * 生成表结构
   * @param conn
   * @param tableNamePattern
   * @return
   * @throws Exception drop table if exists tablename;
   */
  public static String createTableSQL(Connection conn,String  schemaPattern,String tableNamePattern,boolean debug) throws Exception{
    if(debug){
      System.out.println("开始生成 " + tableNamePattern + "数据结构...");
    }
    String header = " create table [" + tableNamePattern + "] (";
    String columns = "";
    String last = "  )  ON [PRIMARY];";
    DatabaseMetaData dbmd = null; 
    ResultSet rs = null;
    try {
      dbmd = conn.getMetaData();
      rs = dbmd.getColumns(null, schemaPattern, tableNamePattern, null);
      String primaryKey = getPrimaryKey(conn, schemaPattern, tableNamePattern,debug);
      while (rs.next()){
        String columnName = rs.getString("COLUMN_NAME");
        String defaultValue = null;
        try{
         defaultValue = rs.getString("COLUMN_DEF");
        }catch(Exception e){
          
        }
        int dataType = rs.getInt("DATA_TYPE");
        String nullEnable = rs.getString("IS_NULLABLE");
        int columnSize = rs.getInt("COLUMN_SIZE");
        int dec = rs.getInt("DECIMAL_DIGITS");
        
        String columnSql = getColumnSQL(columnName, dataType, nullEnable, columnSize, primaryKey,dec,defaultValue, debug);
        if(!"".equals(columns)){
          columns += ",";
        }
        columns += columnSql;
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(null, rs, null);
    }
    return header + columns + last;
  }
  /**
   * 取得主键
   * @param conn
   * @param schemaPattern
   * @param tableNamePattern
   * @param debug
   * @return
   * @throws Exception
   */
  public static String getPrimaryKey(Connection conn,String  schemaPattern,String tableNamePattern,boolean debug) throws Exception{
    DatabaseMetaData dbmd = null; 
    ResultSet rs = null;
    String primaryKey = "";
    try {
      dbmd = conn.getMetaData();
      rs  = dbmd.getPrimaryKeys(null, schemaPattern, tableNamePattern);
      if(rs.next()){
        primaryKey = rs.getString("COLUMN_NAME");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(null, rs, null);
    }
    return primaryKey;
  }
  /**
   * 取得数据类型MYSQL
   * @param typeInt
   * @return
   */
  public static String getMySqlTypes(int typeInt){
    String result = "";
    if (typeInt == Types.BIT) {
      return "BIT";
    } else if (typeInt == Types.TINYINT) {
      return "TINYINT";
    } else if (typeInt == Types.SMALLINT) {
      return "SMALLINT";
    } else if (typeInt == Types.INTEGER) {
      return "INT";
    } else if (typeInt == Types.BIGINT) {
      return "BIGINT";
    } else if (typeInt == Types.FLOAT) {
      return "FLOAT";
    } else if (typeInt == Types.REAL) {
      return "REAL";
    } else if (typeInt == Types.DOUBLE) {
      return "DOUBLE";
    } else if (typeInt == Types.NUMERIC) {
      return "NUMERIC";
    } else if (typeInt == Types.DECIMAL) {
      return "DECIMAL";
    } else if (typeInt == Types.CHAR) {
      return "CHAR";
    } else if (typeInt == Types.VARCHAR) {
      return "VARCHAR";
    } else if (typeInt == Types.LONGVARCHAR) {
      return "LONGVARCHAR";
    } else if (typeInt == Types.DATE) {
      return "TIMESTAMP";
    } else if (typeInt == Types.TIME) {
      return "TIME";
    } else if (typeInt == Types.TIMESTAMP) {
      return "DATETIME";
    } else if (typeInt == Types.BINARY) {
      return "BINARY";
    } else if (typeInt == Types.VARBINARY) {
      return "VARBINARY";
    } else if (typeInt == Types.LONGVARBINARY) {
      return "LONGVARBINARY";
    } else if (typeInt == Types.NULL) {
      return "NULL";
    } else if (typeInt == Types.OTHER) {
      return "VARCHAR";
    } else if (typeInt == Types.JAVA_OBJECT) {
      return "JAVA_OBJECT";
    } else if (typeInt == Types.DISTINCT) {
      return "DISTINCT";
    } else if (typeInt == Types.STRUCT) {
      return "STRUCT";
    } else if (typeInt == Types.ARRAY) {
      return "ARRAY";
    } else if (typeInt == Types.BLOB) {
      return "BLOB";
    } else if (typeInt == Types.CLOB) {
      return "TEXT";
    } else if (typeInt == Types.REF) {
      return "REF";
    } else if (typeInt == Types.DATALINK) {
      return "DATALINK";
    } else if (typeInt == Types.BOOLEAN) {
      return "BOOLEAN";
    }
    return result;
  }
  /**
   * 构建表结构的字段语句
   * @param columnName
   * @param typeInt
   * @param nullEnable
   * @param columnSize
   * @return
   */
  public static String getColumnSQL(String columnName,int typeInt,String nullEnable,int columnSize,String primaryKey,int dec,String defaultValue,boolean debug){
    String result = "";
    if(primaryKey == null || "".equals(primaryKey)){
      primaryKey = increment;
    }
    String typeName = getMySqlTypes(typeInt);
    boolean isPrimaryKey = columnName.equals(primaryKey);
    boolean isIncrement = columnName.equals(increment);
    boolean isNullEnable = isNull.equals(nullEnable);
    result += "    [" + columnName.trim() + "] ";
    if(typeInt == Types.BLOB){
      System.out.println("1");
    }
    if(isIncrement){
      result += " int";
    }else if(YHDsType.isDecimalType(typeInt)){
      if(dec > 0){
        result += " " + typeName + "(" + columnSize + "," + dec + ")";
      }else{
        result += " int";
      }
    }else if(YHDsType.isCharType(typeInt)){
      //if(columnSize >= 1000){
        //result += " text ";
        //defaultValue = null;
     // }else{
        result += " " + typeName + "(" + columnSize + ")";
    //  }
    }else if(typeInt == Types.BLOB){
      result += " Image " ;
    }else if(!YHDsType.isClobType(typeInt)
        && !YHDsType.isDateType(typeInt)){
      result += " " + typeName + "(" + columnSize + ")";
    }else{
      result += " " + typeName ;
    }
    //if(isPrimaryKey){
      //result += " primary key ";
   // }
    if(!isNullEnable){
      result += " not null ";
    }else{
      if(Types.TIMESTAMP == typeInt && defaultValue == null){
        result += " null default null ";
      }
//      if(Types.CLOB == typeInt && defaultValue == null){
//        result += " default ' ' ";
//      }
    }
    if(defaultValue != null){
        result += " default " + defaultValue;
    }
    if(isIncrement){
      result += " unique  IDENTITY (1, 1) ";
    }
    return result;
  }
  /***
   * 
   * @param conn
   * @param schemaPattern
   * @param tableNamePattern
   * @param debug
   * @return
   * @throws Exception
   */
  public static ArrayList<String> getColumns(Connection conn,String  schemaPattern,String tableNamePattern,boolean debug) throws Exception{
    ArrayList<String> result = new ArrayList<String>();
    DatabaseMetaData dbmd = null; 
    ResultSet rs = null;
    try {
      dbmd = conn.getMetaData();
      rs = dbmd.getColumns(null, schemaPattern, tableNamePattern, null);
      while (rs.next()){
        String columnName = rs.getString("COLUMN_NAME");
        result.add(columnName);
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(null, rs, null);
    }
    return result;
  }
  /**
   * 
   * @param conn
   * @param schemaPattern
   * @param debug
   * @return
   * @throws Exception
   */
  public static void createInsertSql(Connection conn,String  schemaPattern, ArrayList<String> tables,FileOutputStream fio,boolean debug) throws Exception{
    for (int i = 0; i < tables.size(); i++) {
      String tableNamePattern = tables.get(i);
      ArrayList<String> columns = getColumns(conn, schemaPattern, tableNamePattern, debug);
      if(debug){
        System.out.println("开始导出  " + tableNamePattern + " 数据...");
      }
      ArrayList<String> temp = toExpData(conn, tableNamePattern, columns,schemaPattern, debug) ;
      for (int j = 0; j < temp.size(); j++) {
        byte[] createBytes = temp.get(j).getBytes(YHConst.DEFAULT_CODE);
        fio.write(createBytes);
      }
      fio.flush();
      //insertSqls.addAll(temp);
    }
  }
  
  /**
   * 
   * @param conn
   * @param schemaPattern
   * @param debug
   * @return
   * @throws Exception
   */
  public static void createInsertSql(Connection conn,Connection conn2,String  schemaPattern, String  schemaPattern2, ArrayList<String> tables,boolean debug) throws Exception{
    try {
      ArrayList<String> erroinfo = new ArrayList<String>();
      for (int i = 0; i < tables.size(); i++) {
        String tableNamePattern = tables.get(i);
        ArrayList<String> columns = getColumns(conn, schemaPattern, tableNamePattern, debug);
        if(debug){
          System.out.println("开始导出  " + tableNamePattern + " 数据...");
        }
        toExpData2(conn,conn2, tableNamePattern, columns,schemaPattern, debug);
        //erroinfo.addAll(toExpData2(conn,conn2, tableNamePattern, columns,schemaPattern, debug));
      }
      System.out.println(erroinfo);
    } catch (Exception e) {
      throw e;
    } 
  }
  /**
   * 
   * @param conn
   * @param tableNamePattern
   * @param columns
   * @param debug
   * @return
   * @throws Exception
   */
  public static ArrayList<String> toExpData(Connection conn,String tableNamePattern,ArrayList<String> columns,String sp,boolean debug) throws Exception{
    //String[] columnsArray = (String[]) columns.toArray();
    ArrayList<String> result = new ArrayList<String>();
    String sql = "select ";
    String field = "";
    for (int i = 0; i < columns.size(); i++) {
      if(!"".equals(field)){
        field += ",";
      }
      field += columns.get(i);
    }
    sql += field + " from " + sp + "." +tableNamePattern.trim() + " where 1=1 ";
    
    PreparedStatement st = null;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
    //System.out.println(sql);
    try {
      st = conn.prepareStatement(sql);
      rs = st.executeQuery();
      rsmd = rs.getMetaData();
      int columnCount = rsmd.getColumnCount();
      int i = 0;
      while( rs.next() ){
        String valuesField = "";
        String inserField = "";
        for (int j = 1; j <= columnCount; j++) {
          int typeInt = rsmd.getColumnType(j);
          String value = null ;
          if(Types.TIMESTAMP == typeInt){
            Date date = rs.getTimestamp(j);
            String dateTime = (date ==  null) ?null : YHUtility.getDateTimeStr(date);
            value = dateTime.length() >= 19 ?  dateTime.substring(0,19) : dateTime;
          }else if(Types.DATE == typeInt){
            value = rs.getDate(j) == null ? null : rs.getString(j);
          }else{
            value = rs.getString(j);
          }
          String valuestr = "";
          if(value == null ){
            continue;
          }
          if(YHDsType.isIntType(typeInt)
              || YHDsType.isLongType(typeInt)){
            valuestr = value.toString(); 
          }else{
            valuestr = "'" + toChar(value.toString()) + "'";
          }
          if(!"".equals(valuesField)){
            valuesField += ",";
          }
          valuesField += valuestr;
          if(!"".equals(inserField)){
            inserField += ",";
          }
          inserField += rsmd.getColumnName(j);
        }
        String inserSql = "insert into " + tableNamePattern + " (";

        inserSql += inserField + ") values(" + valuesField + ");\r\n"; 
        result.add(inserSql);
        
        if(debug && (i%500 == 0)){
          System.out.println("正在导出  " + tableNamePattern + " 表数据                                                                  " + i + " 行.");
        }
        i++;
      }
      if(debug){
        System.out.println("已经导出  " + tableNamePattern + " 表数据                                                                  " + result.size() + " 行.");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(st, rs, null);
    }
    return result;
  }
  /**
   * 
   * @param conn
   * @param tableNamePattern
   * @param columns
   * @param sp
   * @param debug
   * @return
   * @throws Exception
   */
  public static ArrayList<String> toExpData2(Connection conn,Connection conn2,String tableNamePattern,ArrayList<String> columns,String sp,boolean debug) throws Exception{
    //String[] columnsArray = (String[]) columns.toArray();
    ArrayList<String> erroinfo = new ArrayList<String>();
    String sql = "select ";
    String field = "";
    for (int i = 0; i < columns.size(); i++) {
      if(!"".equals(field)){
        field += ",";
      }
      field += columns.get(i);
    }
    sql += field + " from " +tableNamePattern.trim() + " where 1=1 ";
    
    PreparedStatement st = null;
    Statement smt = null;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
    PreparedStatement ps = null;
    String inserSql = "insert into " + tableNamePattern + " (";
    try {
      st = conn.prepareStatement(sql);
      
      rs = st.executeQuery();
      rsmd = rs.getMetaData();
      int columnCount = rsmd.getColumnCount();
      int i = 0;
      smt = conn2.createStatement();
      try {
        smt.execute("set IDENTITY_INSERT [" + tableNamePattern + "] ON");
      } catch (Exception e) {
        e.printStackTrace();
      }
     

      String valuesField = "";
      String inserField = "";
      for (int j = 1; j <= columnCount; j++) {
        if(!"".equals(inserField)){
          inserField += ",";
        }
        inserField += "[" + rsmd.getColumnName(j) + "]";
        if(!"".equals(valuesField)){
          valuesField += ",";
        }
        valuesField += "?";
      }
      String execInserSql =  inserSql + inserField + ") values (" + valuesField + ")";
      ps = conn2.prepareStatement(execInserSql);
      while( rs.next() ){
        Object[] params = new Object[columnCount]; 
        for (int j = 1; j <= columnCount; j++) {
          int typeInt = rsmd.getColumnType(j);
          params[j -1] = getObject(typeInt ,rs ,j);
        }
          try {
            ps = javaParam2SQLParam(params, ps);
            ps.executeUpdate();
            conn2.commit();
          } catch (Exception e) {
            e.printStackTrace();
            String info = tableNamePattern + "表 插入错误 ： " + e.getMessage();
            erroinfo.add(info);
          }
          if(debug && i%500 == 0){
            System.out.println("正在导出  " + tableNamePattern + " 表数据                                                                  " + i + " 行.");
          }
          i++;
        }
       
//      }
      if(debug){
        System.out.println("已经导出  " + tableNamePattern + " 表数据                                                                  " + i + " 行.");
      }
      try {
        smt.execute("set IDENTITY_INSERT [" + tableNamePattern + "] OFF");
      } catch (Exception e) {
        e.printStackTrace();
      }
      
      conn2.commit();

    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(st, rs, null);
      YHDBUtility.close(smt, null, null);
      YHDBUtility.close(ps, null, null);
    }
    return erroinfo;
  }
  
  public static Object getObject(int dbType,ResultSet rs,int i) throws Exception{
    Object o = null;
    if (dbType == Types.TINYINT) {
      o = rs.getByte(i);
    } else if (dbType == Types.SMALLINT) {
       o =rs.getShort(i);
    } else if (dbType == Types.INTEGER || dbType == Types.NUMERIC) {
      try{
         o =rs.getInt(i);
      }catch(Exception e){
         o =rs.getDouble(i);
      }
    } else if (dbType == Types.BIGINT) {
       o =rs.getLong(i);
    } else if (dbType == Types.FLOAT || dbType == Types.REAL) {
       o =rs.getFloat(i);
    } else if (dbType == Types.DOUBLE) {
       o =rs.getDouble(i);
    } else if (dbType == Types.DECIMAL) {
       o =rs.getDouble(i);
    } else if (dbType == Types.BIT) {
       o =rs.getBoolean(i);
    } else if (dbType == Types.CHAR || dbType == Types.VARCHAR
        || dbType == Types.LONGVARCHAR ) {
      o =rs.getString(i);
//      if(o == null){
//        o = " ";
//      }
       
    } else if(dbType == Types.CLOB){
       o = rs.getString(i);
//       if(o == null){
//          o = " ";
//       }
      
    } else if (dbType == Types.DATE) { // 继承于 java.util.Date 类

      java.util.Date date = rs.getDate(i);
       o =date;
    } else if (dbType == Types.TIME) { // 继承于 java.util.Date 类

       o =rs.getTime(i);
    } else if (dbType == Types.TIMESTAMP) { // 继承于 java.util.Date 类

       o =rs.getTimestamp(i);
    } else if (dbType == Types.BINARY || dbType == Types.VARBINARY
        || dbType == Types.LONGVARBINARY || dbType == Types.BLOB) {
       o =rs.getBytes(i);
    } else {
      o =rs.getObject(i);
    }
    return o;
  }
  public static PreparedStatement javaParam2SQLParam(Object[] param,
      PreparedStatement ps) throws Exception {
    //System.out.println("开始 javaParam2SQLParam 方法");
    // 使用Class.isInstance()方法判定指定的 Object
    // 是否与此 Class 所表示的对象赋值兼容。

    // 此方法是 Java 语言 instanceof 运算符的动态等效方法。

    // 如果当前SQL命令包含参数则进行Java数据类型与SQL命令参数的人工映射

    if (param != null && param.length != 0) {
      try {
        for (int i = 0; i < param.length; i++) {
          Object pa = param[i];
          if (pa == null) {
            ps.setString(i + 1, "");
            continue;
          }
          // 将第i个参数的Java数据类型通过其对应的包装器类人工映射并设定SQL命令参数
          if (Boolean.class.isInstance(pa)) { // 映射boolean类型
            ps.setBoolean(i + 1, Boolean.parseBoolean(pa.toString()));
          } else if (Byte.class.isInstance(pa)) { // 映射byte类型
            ps.setByte(i + 1, Byte.parseByte(pa.toString()));
          } else if (byte[].class.isInstance(pa)) {
            ps.setBytes(i + 1, (byte[])pa); // 映射byte[]类型
          } else if (Character.class.isInstance(pa)
              || String.class.isInstance(pa)) { // 映射char和String类型
            String st = String.valueOf(pa);
            ps.setString(i + 1, st);
          } else if (Short.class.isInstance(pa)) { // 映射short类型
            ps.setShort(i + 1, Short.parseShort(pa.toString()));
          } else if (Integer.class.isInstance(pa)) { // 映射int类型
            ps.setInt(i + 1, Integer.parseInt(pa.toString()));
          } else if (Long.class.isInstance(pa)) { // 映射long类型
            ps.setLong(i + 1, Long.parseLong(pa.toString()));
          } else if (Float.class.isInstance(pa)) { // 映射float类型
            ps.setFloat(i + 1, Float.parseFloat(pa.toString()));
          } else if (Double.class.isInstance(pa)) { // 映射double类型
            ps.setDouble(i + 1, Double.parseDouble(pa.toString()));
          } else if (BigDecimal.class.isInstance(pa)) { // 映射BigDecimal类型
            ps.setBigDecimal(i + 1, new BigDecimal(pa.toString()));
          } else if (Date.class.isInstance(pa)
              || java.util.Date.class.isInstance(pa)) { // 映射Date类型
            try {
              ps.setDate(i + 1,  (java.sql.Date)pa);
            } catch (Exception e) {
              java.sql.Timestamp sqlDate = YHUtility.parseTimeStamp(((java.util.Date)pa).getTime());
              ps.setTimestamp(i + 1, sqlDate);
            }
          } else if (Time.class.isInstance(pa)) { // 映射Time类型
            ps.setTime(i + 1, Time.valueOf(pa.toString()));
          } else if (Timestamp.class.isInstance(pa)) { // 映射Timestamp类型
            ps.setTimestamp(i + 1, java.sql.Timestamp.valueOf(pa.toString()));
          } else {
            ps.setObject(i + 1, pa);
          }
        }
      } catch (Exception ex) {
        System.err.println("异常信息：特定数据库访问及其他相关错误！\r\n" + ex.getMessage());
        ex.printStackTrace();
        throw ex;
      }
    }
    
    return ps;
  }
  /**
   * mysql数据库的特殊字符处理
   * @param str
   * @return
   */
  public static String toChar(String str){
    
    return str.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"");
  }
  
  public static void main(String[] args) throws Exception {
   /* Connection conn = getDbConn();
    String scm = "TD_OA4";
    ArrayList<String> tables = getTables(conn, scm, true);
    File file = new File("d:/test12.sql");
    FileOutputStream fio = new FileOutputStream(file);
    expMySqlData("yh", tables, fio, true);*/
    long start = System.currentTimeMillis();
    expOracleData();
    long end = System.currentTimeMillis();
    long time = (end - start)/(60*1000);
    System.out.println("花费时间:" + time + " 分钟.");
  }
  
  public static  void expMySqlData(String dataBase,ArrayList<String> tables,FileOutputStream fio,boolean debug) throws Exception{
    //Connection conn = getDbConn("mysql");
    //createInsertSql(conn, dataBase, tables, fio, debug);
    expOracleData();
  }
  @Test
  public void test(){
    Connection conn = null;
    Connection conn2 = null;
    try {
      String scm = YHSysProps.getProp("db.jdbc.olddbms.dbname");;
      String schemaPattern2 = YHSysProps.getProp("db.jdbc.newdbms.dbname");
      String olddbms = YHSysProps.getProp("db.jdbc.olddbms");;
      String newdbms = YHSysProps.getProp("db.jdbc.newdbms");
      //createmysqlDb(newdbms, schemaPattern2);
      conn = getDbConn(olddbms,scm);
      conn2 = getDbConn(newdbms,schemaPattern2);
     // ArrayList<String> tables = getTables(conn, scm,"FLOW%", true);
      //ArrayList<String> tables = getTables(conn, scm, true);
      ArrayList<String> tables2 = new ArrayList<String>();
      tables2.add("PERSON");
  //   ArrayList<String> tables = new ArrayList<String>();
     // tables.add("VERSION");
      //tables.add("DEPARTMENT");
    //  tables.add("USER_PRIV");*/
      //for (int i = 0; i < tables.size(); i++) {
       // File file = new File("d:/test14.sql");
      //  FileOutputStream fio = new FileOutputStream(file);
        System.out.println("开始生产数据结构...");
        createTableSQL(conn, conn2, scm, schemaPattern2, tables2, true);
        System.out.println("开始导出数据....");
        //tables.remove("CALENDAR");
        createInsertSql(conn, conn2, scm, schemaPattern2, tables2, true);

        //createTableSQL(conn, conn2, scm, schemaPattern2, tables2, true);
        //createInsertSql(conn, conn2, scm, schemaPattern2, tables2, true);
        System.out.println("成功导出数据.");
       // fio.flush();
     // }
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      YHDBUtility.closeDbConn(conn, null);
      YHDBUtility.closeDbConn(conn2, null);
    }
  }
  public static void expOracleData(){
    Connection conn = null;
    Connection conn2 = null;
    try {
      String scm = YHSysProps.getProp("db.jdbc.olddbms.dbname");;
      String schemaPattern2 = YHSysProps.getProp("db.jdbc.newdbms.dbname");
      String olddbms = YHSysProps.getProp("db.jdbc.olddbms");;
      String newdbms = YHSysProps.getProp("db.jdbc.newdbms");
      createmysqlDb(newdbms, schemaPattern2);
      conn = getDbConn(olddbms,scm);
      conn2 = getDbConn(newdbms,schemaPattern2);
     // ArrayList<String> tables = getTables(conn, scm,"FLOW%", true);
      ArrayList<String> tables = getTables(conn, scm, true);
     // ArrayList<String> tables2 = new ArrayList<String>();
     // tables2.add("CALENDAR");
  //   ArrayList<String> tables = new ArrayList<String>();
     // tables.add("VERSION");
      //tables.add("DEPARTMENT");
    //  tables.add("USER_PRIV");*/
      //for (int i = 0; i < tables.size(); i++) {
       // File file = new File("d:/test14.sql");
      //  FileOutputStream fio = new FileOutputStream(file);
        System.out.println("开始生产数据结构...");
        createTableSQL(conn, conn2, scm, schemaPattern2, tables, true);
        System.out.println("开始导出数据....");
        tables.remove("CALENDAR");
        createInsertSql(conn, conn2, scm, schemaPattern2, tables, true);

        //createTableSQL(conn, conn2, scm, schemaPattern2, tables2, true);
        //createInsertSql(conn, conn2, scm, schemaPattern2, tables2, true);
        System.out.println("成功导出数据.");
       // fio.flush();
     // }
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      YHDBUtility.closeDbConn(conn, null);
      YHDBUtility.closeDbConn(conn2, null);
    }
  }
  /**
   * 
   * @param newdbms
   * @param schemaPattern2
   * @throws Exception
   */
  public static void createmysqlDb(String newdbms,String schemaPattern2) throws Exception{
    Connection conn = null;
    Statement smt = null;
    try {
      conn = getDbConn(newdbms,"master");
      System.out.println("初始化新数据库....");
      smt = conn.createStatement();
      String drop =  " IF EXISTS (SELECT name FROM master.dbo.sysdatabases WHERE name = N'" + schemaPattern2 + "') " +
      " DROP DATABASE [" + schemaPattern2 + "];";
      String sql = "create database [" + schemaPattern2 + "];";
      smt.executeUpdate(drop);
      conn.commit();
      smt.executeUpdate(sql);
      conn.commit();
      System.out.println("初始化完毕.");
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(smt, null, null);
      YHDBUtility.closeDbConn(conn, null);
    }
  }
  public static ArrayList<String> notActivTable(){
    ArrayList<String> result = new ArrayList<String>();
    result.add("INDEX_ARTICLE_KEYWORD");
    return result;
  }
  
  public static void main1(String[] args) throws Exception{
    String scm = YHSysProps.getProp("db.jdbc.olddbms.dbname");;
    String schemaPattern2 = YHSysProps.getProp("db.jdbc.newdbms.dbname");
    String olddbms = YHSysProps.getProp("db.jdbc.olddbms");;
    String newdbms = YHSysProps.getProp("db.jdbc.newdbms");
    try {
      Connection conn = getDbConn(newdbms,schemaPattern2);
      Statement st = conn.createStatement();
      //SET IDENTITY_INSERT [TABLE][ON|OFF]
      
      //st.execute("create table dbo.test2 (t int IDENTITY (1, 1) ) ON [PRIMARY]");
      System.out.println(conn);
      st.execute("set IDENTITY_INSERT [test2] ON");
     // Statement st = conn.createStatement();
      st.executeUpdate("insert into test2 (t) values(1)");
     // st.execute("set IDENTITY_INSERT test2 on ");
      st.execute("set IDENTITY_INSERT [test2] OFF");
      conn.commit();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
