package yh.rad.dbexputil.transplant.logic.core.praser;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

import yh.core.util.db.YHDBUtility;
import yh.rad.dbexputil.transplant.logic.core.data.YHColumnInfo;
import yh.rad.dbexputil.transplant.logic.core.data.YHSchemaInfo;
import yh.rad.dbexputil.transplant.logic.core.data.YHTableInfo;
/**
 * 1.取得schema信息
 * 2.取得表信息
 * 3.取得字段信息
 * @author Think
 *
 */
public class YHDBExcute {
  
  /**
   * 取得schema信息
   * @param conn
   * @param schemaPattern schema名称
   * @return
   * @throws Exception 
   */
  public static YHSchemaInfo getSchemaInfo(Connection conn,int dbType,String schemaPattern) throws Exception{
     return getSchemaInfo(conn, dbType,schemaPattern,null);
  }
  /**
   * 取得schema信息
   * @param conn
   * @param schemaPattern schema名称
   * @return
   * @throws Exception 
   */
  public static YHSchemaInfo getSchemaInfo(Connection conn,int dbType,String schemaPattern,String tablePattern) throws Exception{
    return getSchemaInfo(conn, dbType,schemaPattern, tablePattern,  new String[]{"TABLE"});
  }
  
  /**
   * 取得schema信息
   * @param conn
   * @param schemaPattern schema名称
   * @return
   * @throws Exception 
   */
  public static YHSchemaInfo getSchemaInfo(Connection conn,int dbType,String schemaPattern,String tablePattern,String[] types) throws Exception{
    YHSchemaInfo tsf = null;
    try {
      tsf = new YHSchemaInfo();
      ArrayList<String> tableNames = getTableNames(conn, schemaPattern, tablePattern, types);
      tsf.setTableNames(tableNames);
      tsf.setDbType(dbType);
      tsf.setSchemaName(schemaPattern);
    } catch (Exception e) {
      throw e;
    } 
    return tsf;
  }
  
  /**
   * 取得表名称
   * @param conn
   * @param schemaPattern schema名称
   * @return
   * @throws Exception 
   */
  public static ArrayList<String> getTableNames(Connection conn,String schemaPattern) throws Exception{
     return getTableNames(conn, schemaPattern,null);
  }
  /**
   * 取得表名称
   * @param conn
   * @param schemaPattern schema名称
   * @return
   * @throws Exception 
   */
  public static ArrayList<String> getTableNames(Connection conn,String schemaPattern,String tablePattern) throws Exception{
    return getTableNames(conn, schemaPattern, tablePattern,  new String[]{"TABLE"});
  }
  
  /**
   * 取得表名称
   * @param conn
   * @param schemaPattern schema名称
   * @return
   * @throws Exception 
   */
  public static ArrayList<String> getTableNames(Connection conn,String schemaPattern,String tablePattern,String[] types) throws Exception{
    DatabaseMetaData dbmd = conn.getMetaData();
    ResultSet rs = null ;
    ArrayList<String> tableNames = new ArrayList<String>();
    try {
      rs =  dbmd.getTables(conn.getCatalog(), schemaPattern, tablePattern, types);
      while (rs.next()) {
        String tableName = rs.getString("TABLE_NAME");
        tableNames.add(tableName);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(null, rs, null);
    }
    return tableNames;
  }
  
  /**
   * 取得表信息
   * @param conn
   * @param schemaPattern schema名称
   * @return
   * @throws Exception 
   */
  public static ArrayList<YHTableInfo> getTableInfos(Connection conn,String schemaPattern) throws Exception{
     return getTableInfos(conn, schemaPattern,null);
  }
  /**
   * 取得表信息
   * @param conn
   * @param schemaPattern schema名称
   * @return
   * @throws Exception 
   */
  public static ArrayList<YHTableInfo> getTableInfos(Connection conn,String schemaPattern,String tablePattern) throws Exception{
    return getTableInfos(conn, schemaPattern, tablePattern,  new String[]{"TABLE"});
  }
  
  /**
   * 取得表信息
   * @param conn
   * @param schemaPattern schema名称
   * @return
   * @throws Exception 
   */
  public static ArrayList<YHTableInfo> getTableInfos(Connection conn,String schemaPattern,String tablePattern,String[] types) throws Exception{
    DatabaseMetaData dbmd = conn.getMetaData();
    ResultSet rs = null ;
    String catalog = conn.getCatalog();
    ArrayList<YHTableInfo> tableNames = new ArrayList<YHTableInfo>();
    try {
      rs =  dbmd.getTables(catalog, schemaPattern, tablePattern, types);
      while (rs.next()) {
        YHTableInfo tf = new YHTableInfo();
        String tableName = rs.getString("TABLE_NAME");
        tf.setTableName(tableName);
        tf = setFkRefers(catalog, schemaPattern, dbmd, tf);
        ArrayList<YHColumnInfo> cls = getColumnInfos(dbmd, catalog, schemaPattern, tableName, null);
        tf.setColumns(cls);
        tableNames.add(tf);
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(null, rs, null);
    }
    return tableNames;
  }
  /**
   * 设置表的外键关联选项
   * @param catalog
   * @param sechema
   * @param dbmd
   * @param tf
   * @return
   * @throws Exception
   */
  private static YHTableInfo setFkRefers(String catalog ,String sechema,DatabaseMetaData dbmd,YHTableInfo tf) throws Exception{
    ResultSet rs = null;
    String fkrefersNames = "";
    String fkrefersColumns = "";
    try {
      rs = dbmd.getExportedKeys(catalog,sechema , tf.getTableName());
      while (rs.next()) {
        String fkTableName = rs.getString("FKTABLE_NAME");
        String fkColumnName = rs.getString("FKCOLUMN_NAME");
        fkrefersNames += fkTableName + ",";
        fkrefersColumns += fkColumnName + ",";
      }
      tf.setFkrefersColumns(fkrefersColumns);
      tf.setFkrefersNames(fkrefersNames);
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(null, rs, null);
    }
    return tf;
  }
  /**
   * 得到表的字段信息
   * @param dbmd
   * @param catalog
   * @param schemaPattern
   * @param tableNamePattern
   * @param columnNamePattern
   * @return
   * @throws Exception
   */
  public static ArrayList<YHColumnInfo> getColumnInfos(
      DatabaseMetaData dbmd,
      String catalog,
      String schemaPattern,
      String tableNamePattern,
      String columnNamePattern)throws Exception{
    ResultSet rs = null;
    ArrayList<YHColumnInfo> cfs = new ArrayList<YHColumnInfo>();
    try {
      rs = dbmd.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
      while (rs.next()) {
        YHColumnInfo cf = new YHColumnInfo();
        String columnName = rs.getString("COLUMN_NAME");
        int dataType = rs.getInt("DATA_TYPE");
        String isNullableStr = rs.getString("IS_NULLABLE");
        boolean isNullable = "NO".equals(isNullableStr.trim()) ? false : true;
        String typeName = rs.getString("TYPE_NAME");
        cf.setColumnName(columnName);
        cf.setDataType(dataType);
        cf.setNullable(isNullable);
        cf.setDbTypeName(typeName);
        cfs.add(cf);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(null, rs, null);
    }
    return cfs;
  }
  
  /**
   * 得到表的字段信息
   * @param dbmd
   * @param catalog
   * @param schemaPattern
   * @param tableNamePattern
   * @param columnNamePattern
   * @return
   * @throws Exception
   */
  public static ArrayList<String> getColumnNames(
      DatabaseMetaData dbmd,
      String catalog,
      String schemaPattern,
      String tableNamePattern,
      String columnNamePattern)throws Exception{
    ResultSet rs = null;
    ArrayList<String> cfs = new ArrayList<String>();
    try {
      rs = dbmd.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
      while (rs.next()) {
        YHColumnInfo cf = new YHColumnInfo();
        String columnName = rs.getString("COLUMN_NAME");
        cfs.add(columnName);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(null, rs, null);
    }
    return cfs;
  }
  /**
   * 
   * @param dbmd
   * @param catalog
   * @param schemaPattern
   * @param tableNamePattern
   * @param columnNamePattern
   * @return
   * @throws Exception
   */
  public static YHColumnInfo getColumnInfoByName(
      DatabaseMetaData dbmd,
      String catalog,
      String schemaPattern,
      String tableNamePattern,
      String columnNamePattern)throws Exception{
    ResultSet rs = null;
    YHColumnInfo  cf = null;
    try {
      rs = dbmd.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
      if (rs.next()) {
        cf = new YHColumnInfo();
        String columnName = rs.getString("COLUMN_NAME");
        int dataType = rs.getInt("DATA_TYPE");
        String isNullableStr = rs.getString("IS_NULLABLE");
        boolean isNullable = "NO".equals(isNullableStr.trim()) ? false : true;
        String typeName = rs.getString("TYPE_NAME");
        cf.setColumnName(columnName);
        cf.setDataType(dataType);
        cf.setNullable(isNullable);
        cf.setDbTypeName(typeName);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(null, rs, null);
    }
    return cf;
  }
  /**
   * 得到外键关联
   * @param conn
   * @param tableName
   * @param sch
   * @return
   * @throws Exception
   */
  public static String getRefers(Connection conn , String tableName,String sch) throws Exception{
    DatabaseMetaData dbmd = null;
    ResultSet rs = null;
    String fkrefersNames = "";
    try {
      dbmd = conn.getMetaData();
      rs = dbmd.getExportedKeys(conn.getCatalog(),sch , tableName);
      while (rs.next()) {
        String fkTableName = rs.getString("FKTABLE_NAME");
        fkrefersNames += fkTableName + ",";
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(null, rs, null);
    }
    return fkrefersNames;
   
  }
}
