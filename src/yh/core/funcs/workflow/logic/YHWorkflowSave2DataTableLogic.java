package yh.core.funcs.workflow.logic;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHWorkflowSave2DataTableLogic {
  private static Logger log = Logger
  .getLogger("yh.core.funcs.workflow.logic.YHWorkflowSave2DataTableLogic");
  public static String FORM_DATA_TABLE_PRE = "FORM_DATA_";
  public void createTable(Connection dbConn, String tableName, String formSeqId) throws Exception {
    // TODO Auto-generated method stub
    String fields = this.getFields(dbConn , formSeqId);
    try {
      this.isExistTable(dbConn, tableName);
    } catch (SQLException ex) {
      String type = YHSysProps.getProp("db.jdbc.dbms");
      String sql = "";
      if ("oracle".equals(type)) {
        sql = this.getOracleSql(fields ,  tableName);
        this.exSql(dbConn, sql);
        this.exeSeq(dbConn, tableName);
      } else if ("sqlserver".endsWith(type)) {
        sql = this.getMssqlSql(fields ,  tableName);
        this.exSql(dbConn, sql);
      } else {
        sql = this.getMysqlSql(fields ,  tableName);
        this.exSql(dbConn, sql);
      }
      
    }
  }
  public void createTableAny(Connection dbConn, String tableName, String formSeqId ) throws Exception {
    // TODO Auto-generated method stub
    String fields = this.getFields(dbConn , formSeqId);
    try {
      this.isExistTable(dbConn, tableName);
      dropTable(dbConn , tableName);
    } catch (SQLException ex) {
    }
    String type = YHSysProps.getProp("db.jdbc.dbms");
    String sql = "";
    if ("oracle".equals(type)) {
      sql = this.getOracleSql(fields ,  tableName);
      this.exSql(dbConn, sql);
      this.exeSeq(dbConn, tableName);
    } else if ("sqlserver".endsWith(type)) {
      sql = this.getMssqlSql(fields ,  tableName);
      this.exSql(dbConn, sql);
    } else {
      sql = this.getMysqlSql(fields ,  tableName);
      this.exSql(dbConn, sql);
    }
  }
  public void exeSeq(Connection dbConn , String tableName) throws Exception {
    CallableStatement cs = null;
    try {
      cs = dbConn.prepareCall("{call pr_createidentitycolumn(?,?)}");
      cs.setString(1, tableName);
      cs.setString(2, "SEQ_ID");
      cs.execute();
    } catch(Exception ex) {
      throw ex;
    } finally {
      try {
        if (cs != null) {
          cs.close();
        }
      }catch(Exception ex) {
      }
    }
  }
  private String getOracleSeq(String tableName) {
    // TODO Auto-generated method stub
    return "exec pr_createidentitycolumn('"+tableName+"','SEQ_ID');";
  }
  public void isExistTable(Connection dbConn, String tableName) throws Exception {
    // TODO Auto-generated method stub
    String sql = "select 1 from " + tableName;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = dbConn.createStatement();
      rs =  stm.executeQuery(sql);
      rs.next();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
  }
  public void exSql(Connection conn , String sql) throws Exception {
    Statement stm = null;
    try {
      stm = conn.createStatement();
       stm.executeUpdate(sql);
    } catch(Exception ex) {
      //System.out.print(sql);
      throw ex;
    } finally {
      YHDBUtility.close(stm, null, null); 
    }
  }
  public String getFields(Connection conn, String seqId) throws Exception {
    // TODO Auto-generated method stub
    Statement stm = null;
    ResultSet rs = null;
    String str = "";
    String sql = "select ITEM_ID from oa_fl_form_item WHERE FORM_ID=" + seqId;
    try {
      stm = conn.createStatement();
      rs =  stm.executeQuery(sql);
      while (rs.next()) {
        int i = rs.getInt("ITEM_ID");
        if (!YHWorkFlowUtility.findId(str, i + "")) {
          str += i + ",";
        }
      } 
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    str = yh.core.funcs.workflow.util.YHWorkFlowUtility.getOutOfTail(str);
    return str;
  }
  public String getFlowTypeByFormId(Connection conn, String seqId) throws Exception {
    // TODO Auto-generated method stub
    Statement stm = null;
    ResultSet rs = null;
    String str = "";
    String sql = "select SEQ_ID from oa_fl_type WHERE FORM_SEQ_ID=" + seqId;
    try {
      stm = conn.createStatement();
      rs =  stm.executeQuery(sql);
      while (rs.next()) {
        int i = rs.getInt("SEQ_ID");
        str += i + ",";
      } 
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    str = yh.core.funcs.workflow.util.YHWorkFlowUtility.getOutOfTail(str);
    return str;
  }
  public void updateFlowTypeByFormId(Connection conn, String  old , int newId) throws Exception {
    // TODO Auto-generated method stub
    String sql = "UPDATE  oa_fl_type SET FORM_SEQ_ID = '"+ newId +"' WHERE FORM_SEQ_ID=" + old;
    this.exSql(conn, sql);
  }
  public void updateFormTypeByFormId(Connection conn, String  old , int newId) throws Exception {
    // TODO Auto-generated method stub
    String sql = "UPDATE  oa_fl_form_type SET FORM_ID = '"+ newId +"' WHERE SEQ_ID=" + old + " OR FORM_ID =" + old;
    this.exSql(conn, sql);
  }
  public String getOracleSql( String fields , String tableName) {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer();
    sb.append("create table "+tableName+"(");
    sb.append("SEQ_ID NUMBER,");
    sb.append("RUN_ID NUMBER,");
    sb.append("RUN_NAME VARCHAR2(2000),");
    sb.append("BEGIN_USER NUMBER,");
    sb.append("BEGIN_TIME TIMESTAMP,");
    String[] ss  = fields.split(",");
    for (String m : ss) {
      String temp = "DATA_" + m + " " + "CLOB ";
      sb.append(temp);
      sb.append(",");
    }
    //if (fields.size() > 0) {
    sb.deleteCharAt(sb.length() - 1);
   // sb.deleteCharAt(sb.length() - 1);
   //   sb.append("");
    //} 
    sb.append(")");
    return sb.toString();
  }
  public String getMysqlSql( String fields , String tableName) {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer();
    sb.append("create table "+tableName+" (\r\n");
    sb.append("SEQ_ID  INTEGER UNSIGNED NOT NULL AUTO_INCREMENT primary key,\r\n");
    sb.append("RUN_ID INTEGER,\r\n");
    sb.append("RUN_NAME VARCHAR(500),\r\n");
    sb.append("BEGIN_USER INTEGER,\r\n");
    sb.append("BEGIN_TIME DATETIME,\r\n");
    String[] ss  = fields.split(",");
    for (String m : ss) {
      String  temp = "DATA_" + m + " " + "TEXT ";
      sb.append(temp);
      sb.append(",");
    }
    //if (fields.size() > 0) {
      sb.deleteCharAt(sb.length() - 1);
      sb.deleteCharAt(sb.length() - 1);
      sb.append("\r\n");
    //} 
    sb.append(")ENGINE = MyISAM;\r\n");/*ljs20130105 修改引擎从INNODB改为Myisam*/
    return sb.toString();
  }
  public String getMssqlSql(String fields , String tableName) {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer();
    sb.append("create table "+tableName+" (\r\n");
    sb.append("SEQ_ID  numeric(11) NOT NULL IDENTITY  primary key,\r\n");
    sb.append("RUN_ID numeric(11),\r\n");
    sb.append("RUN_NAME VARCHAR(500),\r\n");
    sb.append("BEGIN_USER numeric(11),\r\n");
    sb.append("BEGIN_TIME datetime,\r\n");
    String[] ss  = fields.split(",");
    for (String m : ss) {
      String  temp = "DATA_" + m + " " + "TEXT ";
      sb.append(temp);
      sb.append(",\r\n");
    }
    //if (fields.size() > 0) {
      sb.deleteCharAt(sb.length() - 2);
      sb.append("\r\n");
    //} 
    sb.append(");\r\n");
    return sb.toString();
  }
  public void dropTable(Connection dbConn, String tableName) {
    // TODO Auto-generated method stub
    String drop = " DROP TABLE " + tableName;
    try {
      this.exSql(dbConn, drop);
    } catch (Exception e) {
      // TODO Auto-generated catch block
    }
    String type = YHSysProps.getProp("db.jdbc.dbms");
    if ("oracle".equals(type)) {
      drop = "drop SEQUENCE  SEQ_" + tableName;
      try {
        this.exSql(dbConn, drop);
      } catch (Exception e) {
        // TODO Auto-generated catch block
      }
    }
    
  }
  public void emptyTable(Connection dbConn, String tableName) {
    // TODO Auto-generated method stub
    String drop = " delete from  " + tableName;
    try {
      this.exSql(dbConn, drop);
    } catch (Exception e) {
      // TODO Auto-generated catch block
    }
  }
  
  public void alterTable(Connection dbConn, String tableName, String seqStr) throws Exception {
    // TODO Auto-generated method stub
    String fields = this.getFields(dbConn , seqStr);
    String type = YHSysProps.getProp("db.jdbc.dbms");
    String metaData = this.getMetaData(dbConn , tableName);
    metaData = YHWorkFlowUtility.getOutOfTail(metaData);
    fields = this.needAddField(fields, metaData);
    if ("oracle".equals(type)) {
      this.alterOracleSql(dbConn,fields ,  tableName);
    } else if ("sqlserver".endsWith(type)) {
      this.alterMssqlSql(dbConn,fields ,  tableName);
    } else {
      this.alterMysqlSql(dbConn,fields ,  tableName);
    }
  }
  public String needAddField(String fields , String metaData) {
    String[] ss  = fields.split(",");
    String idStr = "";
    for (String m : ss) {
      String field = "DATA_" + m;
      if(!YHWorkFlowUtility.findId(metaData , field)){
        idStr += field + ",";
      }
    }
    idStr = YHWorkFlowUtility.getOutOfTail(idStr);
    return idStr;
  }
  private void alterMysqlSql(Connection dbConn, String fields, String tableName) throws Exception {
    String[] ss  = fields.split(",");
    for (String m : ss) {
      if (!YHUtility.isNullorEmpty(m)) {
        String sql = "ALTER TABLE "+ tableName +" ADD COLUMN "+ m +" TEXT";
        try {
          this.exSql(dbConn, sql);
        } catch (Exception ex) {
          log.error("为表"+ tableName + ",增加字段:"+ m +"时出错!");
        }
      }
    }
  }
  private void alterMssqlSql(Connection dbConn, String fields, String tableName) throws Exception {
    String[] ss  = fields.split(",");
    for (String m : ss) {
      if (!YHUtility.isNullorEmpty(m)) {
      String sql = "ALTER TABLE "+ tableName +" ADD "+ m +" TEXT";
      try {
        this.exSql(dbConn, sql);
      } catch (Exception ex) {
        log.error("为表"+ tableName + ",增加字段:"+ m +"时出错!");
      }
      }
    }
  }
  private void alterOracleSql(Connection dbConn, String fields, String tableName) throws Exception {
    String[] ss  = fields.split(",");
    for (String m : ss) {
      if (!YHUtility.isNullorEmpty(m)) {
        String sql = "ALTER TABLE "+ tableName +"  ADD ("+ m +" CLOB)";
        try {
          this.exSql(dbConn, sql);
        } catch (Exception ex) {
          log.error("为表"+ tableName + ",增加字段:"+ m +"时出错!");
        }
        
      }
    }
  }
  private String getMetaData(Connection conn, String tableName) throws Exception {
    // TODO Auto-generated method stub
    String query = "select * from " + tableName;
    Statement stm = null;
    ResultSet rs = null;
    String str = "";
    try {
      stm = conn.createStatement();
      rs =  stm.executeQuery(query);
      ResultSetMetaData rsm = rs.getMetaData();
      int count = rsm.getColumnCount();
      for (int i = 1 ;i <= count ; i++) {
        str += rsm.getColumnName(i) + ",";
      }
      
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return str;
  }
  /**
   * 为系统的所有表单创建新的存储方式
   * @return 
   * @throws Exception 
   */
  public void createSaveTable(Connection conn) throws Exception {
    this.updateFormItemMax(conn);
    String query = "select SEQ_ID , FORM_SEQ_ID from oa_fl_type" ;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs =  stm.executeQuery(query);
      while (rs.next()) {
        int formId = rs.getInt("FORM_SEQ_ID");
        String tableName =  "" +YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE + rs.getInt("SEQ_ID") + "_" + formId;
        this.createTable(conn, tableName, formId + "");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
  }
  /**
   * 为系统的所有表单创建新的存储方式
   * @return 
   * @throws Exception 
   */
  public void createSaveTableById(Connection conn , int id) throws Exception {
    this.updateFormItemMaxById(conn , id);
    String query = "select SEQ_ID , FORM_SEQ_ID from oa_fl_type where SEQ_ID=" + id ;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs =  stm.executeQuery(query);
      if (rs.next()) {
        int formId = rs.getInt("FORM_SEQ_ID");
        String tableName =  "" +YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE + rs.getInt("SEQ_ID") + "_" + formId;
        this.createTableAny(conn, tableName, formId + "");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
  }
  /**
   * 为系统的所有表单创建新的存储方式
   * @return 
   * @throws Exception 
   */
  public void createSaveTableSeq(Connection conn) throws Exception {
    this.updateFormItemMax(conn);
    String query = "select SEQ_ID , FORM_SEQ_ID from oa_fl_type" ;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs =  stm.executeQuery(query);
      while (rs.next()) {
        int formId = rs.getInt("FORM_SEQ_ID");
        String tableName =  "" +YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE + rs.getInt("SEQ_ID") + "_" + formId;
        this.createTable(conn, tableName, formId + "");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
  }
  public void updateFormItemMaxById(Connection conn , int formId) throws Exception {
    String query = "select SEQ_ID, ITEM_MAX from oa_fl_form_type where SEQ_ID =" + formId ;
    Statement stm = null;
    ResultSet rs = null;
    int max = 0;
    try {
      stm = conn.createStatement();
      rs =  stm.executeQuery(query);
      if (rs.next()) {
        max = rs.getInt(2) ;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    Statement stm2 = null;
    ResultSet rs2 = null;
    query = "select max(ITEM_ID) FROM oa_fl_form_item WHERE FORM_ID=" + formId;
    try {
      stm2 = conn.createStatement();
      rs2 =  stm2.executeQuery(query);
      if (rs2.next()) {
        int i = rs2.getInt(1);
        if (i > max) {
          String update = "update oa_fl_form_type SET ITEM_MAX = " + i + " where SEQ_ID =" + formId;
          this.exSql(conn, update);
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
  }
  public void updateFormItemMax(Connection conn) throws Exception {
    String query = "select SEQ_ID, ITEM_MAX from oa_fl_form_type" ;
    Statement stm = null;
    ResultSet rs = null;
    String formIds = "";
    String itemMaxs = "";
    try {
      stm = conn.createStatement();
      rs =  stm.executeQuery(query);
      while (rs.next()) {
        formIds += rs.getInt("SEQ_ID") + ",";
        itemMaxs += rs.getInt(2)+ ",";
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    String[] formIdss = formIds.split(",");
    String[] itemMaxss =  itemMaxs.split(",");
    for (int b = 0 ; b < formIdss.length ; b++) {
      String id = formIdss[b];
      String im = itemMaxss[b];
      if (!YHUtility.isNullorEmpty(id)) {
        Statement stm2 = null;
        ResultSet rs2 = null;
        query = "select max(ITEM_ID) FROM oa_fl_form_item WHERE FORM_ID=" + id;
        try {
          stm2 = conn.createStatement();
          rs2 =  stm2.executeQuery(query);
          if (rs2.next()) {
            int i = rs2.getInt(1);
            if (i > Integer.parseInt(im)) {
              String update = "update oa_fl_form_type SET ITEM_MAX = " + i + " where SEQ_ID =" + id;
              this.exSql(conn, update);
            }
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm2, rs2, null); 
        }
      }
    }
  }
  public void updateFlowFormTable(Connection dbConn, int seqId) throws Exception {
    if (YHWorkFlowUtility.isSave2DataTable()) {
      YHWorkflowSave2DataTableLogic logic = new YHWorkflowSave2DataTableLogic();
      String flowTypes = logic.getFlowTypeByFormId(dbConn, seqId + "");
      if (!YHUtility.isNullorEmpty(flowTypes)) {
        String[] flowType = flowTypes.split(",");
        for (String ss : flowType) {
          if (!YHUtility.isNullorEmpty(ss)) {
            String tableName = "" +YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE + ss + "_" + seqId;
            logic.alterTable(dbConn , tableName , seqId+"") ;
          }
        }
      }
    }
  }
  public void createFlowFormTable(Connection dbConn, int seqId) throws Exception {
    if (YHWorkFlowUtility.isSave2DataTable()) {
      YHWorkflowSave2DataTableLogic logic = new YHWorkflowSave2DataTableLogic();
      String flowTypes = logic.getFlowTypeByFormId(dbConn, seqId + "");
      if (!YHUtility.isNullorEmpty(flowTypes)) {
        String[] flowType = flowTypes.split(",");
        for (String ss : flowType) {
          if (!YHUtility.isNullorEmpty(ss)) {
            String tableName = "" +YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE + ss + "_" + seqId;
            this.createTable(dbConn, tableName, seqId + "");
          }
        }
      }
    }
  }
  public void createFlowFormTable(Connection dbConn, int seqId , String flowTypes) throws Exception {
    if (YHWorkFlowUtility.isSave2DataTable()) {
      if (!YHUtility.isNullorEmpty(flowTypes)) {
        String[] flowType = flowTypes.split(",");
        for (String ss : flowType) {
          if (!YHUtility.isNullorEmpty(ss)) {
            String tableName = "" +YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE + ss + "_" + seqId;
            this.createTable(dbConn, tableName, seqId + "");
          }
        }
      }
    }
  }
  public void updateItemMax(Connection conn , int id) throws Exception {
    Statement stm2 = null;
    ResultSet rs2 = null;
    String query = "select max(ITEM_ID) FROM oa_fl_form_item WHERE FORM_ID=" + id;
    try {
      stm2 = conn.createStatement();
      rs2 =  stm2.executeQuery(query);
      if (rs2.next()) {
        int i = rs2.getInt(1);
        String update = "update oa_fl_form_type SET ITEM_MAX = " + i + " where SEQ_ID =" + id;
        this.exSql(conn, update);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
  }
  public void convertData2Table(Connection conn) throws Exception {
    // TODO Auto-generated method stub
    String sql = "select RUN_ID , RUN_NAME , BEGIN_USER, BEGIN_TIME , FLOW_TYPE.SEQ_ID , FLOW_TYPE.FORM_SEQ_ID FROM oa_fl_run , oa_fl_type as FLOW_TYPE WHERE FLOW_ID = FLOW_TYPE.SEQ_ID";
    List<Map> list = new ArrayList();
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 =  stm2.executeQuery(sql);
      while (rs2.next()) {
        Map map = new HashMap();
        map.put("RUN_ID", rs2.getInt("RUN_ID"));
        map.put("FLOW_ID", rs2.getInt("SEQ_ID"));
        map.put("FORM_ID",  rs2.getInt("FORM_SEQ_ID"));
        map.put("RUN_NAME",  rs2.getString("RUN_NAME"));
        map.put("BEGIN_USER", rs2.getInt("BEGIN_USER"));
        map.put("BEGIN_TIME", rs2.getTimestamp("BEGIN_TIME"));
        list.add(map);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    for (Map m : list) {
      int runId = (Integer) m.get("RUN_ID");
      int flowId = (Integer)m.get("FLOW_ID");
      int formId = (Integer)m.get("FORM_ID");
      String runName = (String) m.get("RUN_NAME");
      int beginUser = (Integer)m.get("BEGIN_USER");
      Timestamp  beginTime = (Timestamp) m.get("BEGIN_TIME");
      String tableName = "" +YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE + flowId + "_" + formId;
    }
    
  }
  public void createSaveTableSeq(Connection dbConn, String tableName) throws Exception {
    // TODO Auto-generated method stub
    String type = YHSysProps.getProp("db.jdbc.dbms");
    String sql = "";
    if ("oracle".equals(type)) {
    }
  }
}
