package yh.core.esb.server.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import yh.core.autorun.YHAutoRun;
import yh.core.esb.server.logic.YHEsbServerLogic;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.db.YHDBUtility;

public class YHEsbServerTasksBack extends YHAutoRun {
  public static final String ESB_TRANSFER_TABLE_PRE = "ESB_TRANSFER";
  public static final String ESB_TRANSFER_STATUS_TABLE_PRE = "ESB_TRANSFER_STATUS";
  public static final String ESB_SYS_MSG = "ESB_SYS_MSG";
  private static final Logger log = Logger.getLogger("yh.core.esb.server.task.YHEsbServerTasksBack");
  public void doTask() {
    Connection conn = null;
    try {
      conn = getRequestDbConn().getSysDbConn();
      Date date = new Date();
      
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
      boolean flag = false;
      if (date.getMonth() == 2 || date.getMonth() == 5 || date.getMonth() == 8 || date.getMonth() == 11) {
        flag = true;
      }
      String time = sdf.format(date);
      if (flag && !hasBack(conn , time)) {
        //创建表复制数据
        this.createTable(conn);
        //改表名
        this.alertTableName(conn, time);
        this.alertTableName2(conn);
        
        this.saveInfo(conn, time, date);
      }
    } catch (Exception e) {
      log.debug(e.getMessage(),e);
      e.printStackTrace();
    } finally {
      YHDBUtility.closeDbConn(conn, null);
    }
  }
  public boolean hasBack(Connection conn , String time) throws Exception {
    String sql = "select 1 from backup_info where TABLE_NAME = '" + ESB_TRANSFER_TABLE_PRE + "_" + time + "'";
    Statement stm = null;
    ResultSet rs = null;
    int seqId = 0;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(sql);
      if (rs.next()) {
        return true;
      } else {
        return false;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
  }
  public void saveInfo (Connection conn , String time , Date date) throws Exception {
    this.saveBackupInof(conn, ESB_TRANSFER_TABLE_PRE, ESB_TRANSFER_TABLE_PRE + "_" + time, date);
    this.saveBackupInof(conn, ESB_TRANSFER_STATUS_TABLE_PRE, ESB_TRANSFER_STATUS_TABLE_PRE + "_" + time, date);
    this.saveBackupInof(conn, ESB_SYS_MSG, ESB_SYS_MSG + "_" + time, date);
  }
  public  void saveBackupInof(Connection conn , String table , String reTable , Date date) throws Exception {
    String sql = "insert into backup_info (TYPE , DATETIME , TABLE_NAME) VALUES ('"+table+"',? , '"+reTable+"')" ;
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      Timestamp ts = new Timestamp(date.getTime());
      ps.setTimestamp(1, ts);
      ps.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    } finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  public void alertTableName(Connection conn , String time) throws Exception {
    String table_1 = "alter table "+ ESB_TRANSFER_TABLE_PRE + " rename to " + ESB_TRANSFER_TABLE_PRE + "_" + time;
    String table_2 = "alter table "+ ESB_TRANSFER_STATUS_TABLE_PRE + " rename to " + ESB_TRANSFER_STATUS_TABLE_PRE + "_" + time;
    String table_3 = "alter table "+ ESB_SYS_MSG + " rename to " + ESB_SYS_MSG + "_" + time;
    
    YHWorkFlowUtility.updateTableBySql(table_1, conn);
    YHWorkFlowUtility.updateTableBySql(table_2, conn);
    YHWorkFlowUtility.updateTableBySql(table_3, conn);
  }
  public void alertTableName2(Connection conn) throws Exception {
    String table_1 = "alter table "+ ESB_TRANSFER_TABLE_PRE + "_tmp rename to " + ESB_TRANSFER_TABLE_PRE ;
    String table_2 = "alter table "+ ESB_TRANSFER_STATUS_TABLE_PRE + "_tmp rename to " + ESB_TRANSFER_STATUS_TABLE_PRE;
    String table_3 = "alter table "+ ESB_SYS_MSG + "_tmp rename to " + ESB_SYS_MSG ;
    
    YHWorkFlowUtility.updateTableBySql(table_1, conn);
    YHWorkFlowUtility.updateTableBySql(table_2, conn);
    YHWorkFlowUtility.updateTableBySql(table_3, conn);
  }
  
  public void createTable(Connection conn) throws Exception {
    try {
      String table_1_1 = "drop table " + ESB_TRANSFER_TABLE_PRE + "_tmp";
      String table_2_1 = "drop table " + ESB_TRANSFER_STATUS_TABLE_PRE + "_tmp";
      String table_3_1 = "drop table " + ESB_SYS_MSG + "_tmp";
      YHWorkFlowUtility.updateTableBySql(table_1_1, conn);
      YHWorkFlowUtility.updateTableBySql(table_2_1, conn);
      YHWorkFlowUtility.updateTableBySql(table_3_1, conn);
    } catch (Exception ex) {
      
    }
    String table_1 = "create table " + ESB_TRANSFER_TABLE_PRE + "_tmp as select * from " + ESB_TRANSFER_TABLE_PRE  + " where STATUS <> '" + YHEsbServerLogic.TRANSFER_STATUS_ALLCOMPLETE + "'";
    String table_2 = "create table "  + ESB_TRANSFER_STATUS_TABLE_PRE + "_tmp as select * from " + ESB_TRANSFER_STATUS_TABLE_PRE + " b  where b.TRANS_ID in (select a.GUID from " + ESB_TRANSFER_TABLE_PRE  + " a where  a.STATUS <>'" + YHEsbServerLogic.TRANSFER_STATUS_ALLCOMPLETE + "')";
    String table_3 = "create table " + ESB_SYS_MSG + "_tmp as select * from " + ESB_SYS_MSG + " where STATUS <>'1'";
    
    YHWorkFlowUtility.updateTableBySql(table_1, conn);
    YHWorkFlowUtility.updateTableBySql(table_2, conn);
    YHWorkFlowUtility.updateTableBySql(table_3, conn);
  }
}
