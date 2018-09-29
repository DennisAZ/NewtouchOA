package yh.rad.dsdef.logic;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import yh.core.data.YHDbRecord;
import yh.core.data.YHDsField;
import yh.core.data.YHDsTable;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.db.YHStringFormat;
import yh.core.util.form.YHFOM;
import yh.rad.dsdef.logic.parser.YHMysqlColumnParser;
import yh.rad.dsdef.logic.parser.YHOracleColumnParser;
import yh.rad.dsdef.logic.praserI.YHColumnPraserI;

public class YHDsDefLogic2Db {

  /**
   * 创建物理表
   * @param dbConn
   * @param tableNo
   * @throws Exception
   */
  public void createPhyics(Connection dbConn,String tableNo) throws Exception{
    //1.获得数据库处理类型
    String dbExecType = getDbExecType(dbConn);
    //1.取得数据字典对象
    YHORM orm = new YHORM();
    Map filters = new HashMap();
    filters.put("TABLE_NO", tableNo);
    YHDsTable dsTable = (YHDsTable) orm.loadObjSingle(dbConn, YHDsTable.class, filters);
    ArrayList<YHDsField> dsFields = (ArrayList<YHDsField>) orm.loadListSingle(dbConn, YHDsField.class, filters);
    String tableName = dsTable.getTableName();
    if(isExist(dbConn, tableName)){
      throw new Exception( tableName + "表已存在!");
    }
    YHColumnPraserI columnParser = parserFactory(dbConn, tableName, dbExecType);
    columnParser.execPhyicsSql(dbConn, tableName, dsFields);
  }
  /**
   * 得到数据库处理类型
   * @param conn
   * @return
   */
  public  String getDbExecType(Connection conn){
    return YHSysProps.getProp("db.jdbc.dbms");
  }
  /**
   * 
   * @param dbConn
   * @param tableName
   * @param dbExecType
   */
  public YHColumnPraserI parserFactory(Connection dbConn,String tableName,String dbExecType){
    YHColumnPraserI columnParser = null;
    if("oracle".equalsIgnoreCase(dbExecType)){
      columnParser = new YHOracleColumnParser();
    }else if("mysql".equalsIgnoreCase(dbExecType)){
      columnParser = new YHMysqlColumnParser();
    }
    return columnParser;
  }
  /**
   * 判断表是否存在
   * @param conn
   * @param tableName
   * @return
   * @throws SQLException
   */
  public boolean isExist(Connection conn,String tableName) throws Exception{
    boolean isExist = true;
    Statement stmt =  null;
    try {
      stmt = conn.createStatement(); 
      stmt.executeQuery( "select count(*) from " + tableName ); 
    } catch (Exception e) {
      isExist = false;
    } finally {
      YHDBUtility.close(stmt, null, null);
    }
    return isExist;
  }
  /**
   * 判断表是否存在
   * @param conn
   * @param tableName
   * @return
   * @throws SQLException
   */
  public void dropTabLogic(Connection conn,String tableName) throws Exception{
    Statement stmt =  null;
    String dbExecType = getDbExecType(conn);
    try {
      stmt = conn.createStatement(); 
      stmt.execute( "drop table " + tableName ); 
      if("oracle".equalsIgnoreCase(dbExecType)){
        stmt.execute( "drop sequence SEQ_" + tableName ); 
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, null);
    }
  }
  
  /**
   * 取得物理结构
   * @param conn
   * @param tableName
   * @return
   * @throws Exception 
   */
  public StringBuffer getPhysicsDbInfo(Connection conn , String tableName) throws Exception{
    StringBuffer result = new StringBuffer();
    ArrayList<YHDsField> fieldList = getDsFields(conn,  tableName);
    result = getlistField2Json(fieldList);
    return result;
  }
  
  /**
   * 取得物理结构
   * @param conn
   * @param tableName
   * @return
   * @throws Exception 
   */
  public String getPhysicsDbInfo2(Connection conn , String tableName) throws Exception{
    ArrayList<YHDsField> fieldList = getDsFields(conn,  tableName);
    String result = getlistField2Json2(fieldList);
    return result;
  }
  
  /**
   * 
   * @param conn
   * @param tableNo
   * @param tableName
   * @return
   * @throws Exception 
   */
  public ArrayList<YHDsField> getDsFields(Connection conn , String tableName) throws Exception{
    Statement sysStmt = null;
    ResultSet rs = null;
    String sql = "select * from " + tableName + " where 1=0";
    ArrayList<YHDsField> fieldList = new ArrayList<YHDsField>();
    try {
      sysStmt = conn.createStatement();
      rs = sysStmt.executeQuery(sql);
      ResultSetMetaData rsMeta = rs.getMetaData();
      int colCnt = rsMeta.getColumnCount();
      String primaryKey = getPrimaryKey(conn, null, tableName, false);
      for (int i = 0; i < colCnt; i++) {
        YHDsField field = new YHDsField();
        String colName = rsMeta.getColumnName(i + 1);
        field.setFieldName(colName);
        field.setPropName(YHStringFormat.unformat(colName));
        field.setFieldPrecision(rsMeta.getPrecision(i + 1));
        field.setFieldScale(rsMeta.getScale(i + 1));
        field.setDataType(rsMeta.getColumnType(i + 1));
        field.setFormatMode(field.genFormatMode());
        field.setIsIdentity(rsMeta.isAutoIncrement(i + 1) ? "1" : "0");
        if(primaryKey.equalsIgnoreCase(colName)){
          field.setMustFill("1");
          field.setIsPrimaryKey("1");
        }else{
          field.setMustFill(rsMeta.isNullable(i + 1)
              == ResultSetMetaData.columnNullable ? "1" : "0");
          field.setIsPrimaryKey("0");
        }
        fieldList.add(field);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(sysStmt, rs, null);
    }
    return fieldList;
  }
  
  /**
   * 
   * @param fieldList
   * @return
   * @throws Exception
   */
   public StringBuffer getlistField2Json(ArrayList<YHDsField> fieldList) throws Exception{
     StringBuffer sb = new StringBuffer();
     StringBuffer temp = new StringBuffer();
     for (YHDsField yhDsField : fieldList) {
       if(yhDsField == null ){
         continue;
       }
       if(!"".equals(temp.toString())){
         temp.append(",");
       }
       temp.append(YHFOM.toJson(yhDsField));
     }
     sb.append("[").append(temp).append("]");
     return sb;
   }
   
   /**
    * 
    * @param fieldList
    * @return
    * @throws Exception
    */
    public String getlistField2Json2(ArrayList<YHDsField> fieldList) throws Exception{
      StringBuffer sb = new StringBuffer();
      sb.append("{\"records\":");
      sb.append(fieldList.size());
      sb.append(",\"page\":");
      sb.append(1);
      sb.append(",\"total\":");
      sb.append(1);
      sb.append(",\"rows\":[");
      for(int i = 0; i < fieldList.size(); i++){
        YHDsField yhDsField = fieldList.get(i);
        sb.append("{\"id\":");
        sb.append(i);
        sb.append(",\"cell\":[");
        
        sb.append(YHFOM.toJson2(yhDsField));
        sb.append("]}");
        if (i < fieldList.size() - 1) {
          sb.append(",");
        }
      }
      sb.append("]");
      sb.append("}");
      return sb.toString();
    }
  
  /**
   * 
   * @param conn
   * @param schemaPattern
   * @param tableNamePattern
   * @param debug
   * @return
   * @throws Exception
   */
  public  String getPrimaryKey(Connection conn,String  schemaPattern,String tableNamePattern,boolean debug) throws Exception{
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
  
  public String isExistForTab(Connection conn,String tableName) throws Exception{
    String sql = "select count(*) from oa_table_dicts WHERE TABLE_NAME='" + tableName + "'";
    Statement st = null;
    ResultSet rs = null;
    try {
      st = conn.createStatement() ;
      rs = st.executeQuery(sql);
      if(rs.next()){
        int temp = rs.getInt(1);
        if(temp > 0){
          return "1";
        }else{
          return "0";
        }
      }else{
        return "0";
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(st, rs, null);
    }
  }
}
