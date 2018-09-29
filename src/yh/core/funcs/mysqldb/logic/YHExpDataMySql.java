package yh.core.funcs.mysqldb.logic;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.sun.jmx.snmp.Timestamp;

import yh.core.data.YHDsType;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHConfigLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
public class YHExpDataMySql {
  private static final String increment = "SEQ_ID";
  private static final String isNull = "YES";

  
  /**
   * 得到所有的建表语句
   * @param conn
   * @param schemaPattern
   * @param tables
   * @param debug
   * @return
   * @throws Exception
   */
  public static String createTableSQL(Connection conn,String  schemaPattern,ArrayList<String> tables,boolean debug) throws Exception{
    String createSql = "";
    for (int i = 0; i < tables.size(); i++) {
      String tableNamePattern = tables.get(i);
      createSql += createTableSQL(conn, schemaPattern, tableNamePattern, debug);
    }
    return createSql;
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
    String header = " drop table if exists `" + tableNamePattern + "` ;\r\n";
    header += " create table `" + tableNamePattern + "` (\r\n";
    String columns = "";
    /*ljs20130105 修改引擎从INNODB改为Myisam*/
    String last = " \r\n ) ENGINE=MyISAM DEFAULT CHARSET=utf8;\r\n";
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
          columns += ",\r\n";
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
    result += "    `" + columnName.trim() + "` ";
    if(isIncrement){
      result += " int(11)";
    }else if(YHDsType.isDecimalType(typeInt)){
      if(dec > 0){
        result += " " + typeName + "(" + columnSize + "," + dec + ")";
      }else{
        result += " int(11)";
      }
    }else if(YHDsType.isCharType(typeInt)){
      if(columnSize >= 1000){
        result += " text ";
        defaultValue = null;
      }else{
        result += " " + typeName + "(" + columnSize + ")";
      }
    }else if(!YHDsType.isClobType(typeInt)
        && !YHDsType.isDateType(typeInt)){
      result += " " + typeName + "(" + columnSize + ")";
    }else{
      result += " " + typeName ;
    }
    if(isPrimaryKey){
      result += " primary key ";
    }
    if(!isNullEnable){
      result += " not null ";
    }else{
      if(Types.TIMESTAMP == typeInt && defaultValue == null){
        result += " null default null ";
      }
    }
    if(defaultValue != null){
        result += " default " + defaultValue;
    }
    if(isIncrement){
      result += " unique  auto_increment ";
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
  public static ZipOutputStream createInsertSql(Connection conn,String  schemaPattern, ArrayList<String> tables,ZipOutputStream baos,boolean debug) throws Exception{
    for (int i = 0; i < tables.size(); i++) {
      String tableNamePattern = tables.get(i);
      ArrayList<String> columns = getColumns(conn, schemaPattern, tableNamePattern, debug);
      if(debug){
        System.out.println("开始导出  " + tableNamePattern + " 数据...");
      }
      StringBuffer sb = toExpData(conn, tableNamePattern, columns,schemaPattern, debug) ;
      byte[] createBytes = sb.toString().getBytes(YHConst.DEFAULT_CODE);
      //insertSqls.addAll(temp);
      zip("YH_Data_"+ tableNamePattern + YHUtility.getCurDateTimeStr().substring(0,10) + ".sql", createBytes, baos);
    }
    return baos;
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
  public static StringBuffer toExpData(Connection conn,String tableNamePattern,ArrayList<String> columns,String sp,boolean debug) throws Exception{
    //String[] columnsArray = (String[]) columns.toArray();
    StringBuffer result = new StringBuffer();
    String sql = "select ";
    String field = "";
    for (int i = 0; i < columns.size(); i++) {
      if(!"".equals(field)){
        field += ",";
      }
      field += "`" + columns.get(i) + "`";
    }
    sql += field + " from " + sp + "." +tableNamePattern.trim() + " where 1=1 ";
    
    PreparedStatement st = null;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
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
            if(dateTime != null){
              value = dateTime.length() >= 19 ?  dateTime.substring(0,19) : dateTime;
            }else{
              value = null;
            }
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
        result.append(inserSql);
        
        if(debug && (i%500 == 0)){
          System.out.println("正在导出  " + tableNamePattern + " 表数据                                                                  " + i + " 行.");
        }
        i++;
      }
      if(debug){
        System.out.println("已经导出  " + tableNamePattern + " 表数据                                                                  " + i + " 行.");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(st, rs, null);
    }
    return result;
  }
  
  /**
   * mysql数据库的特殊字符处理
   * @param str
   * @return
   */
  public static String toChar(String str){
    return str.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"");
  }
  
  public static void expData(Connection conn, ArrayList<String> tables,String schemaPattern,ZipOutputStream baos) throws Exception{
    String tablesql = createTableSQL(conn, schemaPattern, tables, false);
    baos = createInsertSql(conn, schemaPattern, tables, baos,false);
    zip("YH_TABLE_"+ YHUtility.getCurDateTimeStr().substring(0,10) + ".sql", tablesql.getBytes(YHConst.DEFAULT_CODE), baos);
  }
  
  /**
   * 
   * @param map
   * @param baos
   * @return
   * @throws IOException
   */
    public static ZipOutputStream zip(String name, byte[] value,ZipOutputStream zos)
    throws IOException {
    ZipEntry zipEntry = null;
    zipEntry = new ZipEntry(name);
    zipEntry.setSize(value.length);
    zipEntry.setTime(System.currentTimeMillis());
    zos.putNextEntry(zipEntry);
    zos.write(value);
    zos.flush();
    return zos;
  }
}
