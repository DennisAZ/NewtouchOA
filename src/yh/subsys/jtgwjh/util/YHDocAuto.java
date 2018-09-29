package yh.subsys.jtgwjh.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import yh.core.autorun.YHAutoRun;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHDocAuto extends YHAutoRun {
  public void doTask() {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String dateStr = sdf.format(date.getTime());
      dateStr = dateStr.substring(0,7).replaceAll("-", "");
      String month = dateStr.substring(4, 6);
      if("03".equals(month) || "06".equals(month) || "09".equals(month) || "12".equals(month)){
        
        YHORM orm = new YHORM();
        Connection conn = getRequestDbConn().getSysDbConn();
        
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("TYPE", "sys_log");
        filters.put("TABLE_NAME", "sys_log_"+dateStr);
        YHBackupInfo backupInfo = (YHBackupInfo)orm.loadObjSingle(conn, YHBackupInfo.class, filters);
        
        if(backupInfo == null){
          for(int i = 0; i < 2; i++){
            String tableName = "";
            if(i == 0){
              tableName = "oa_sys_log";
            }
            else if(i == 1){
              tableName = "seclog";
            }
            
            String sql = " ALTER TABLE "+tableName+" RENAME TO "+tableName+"_" + dateStr;
            
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            
            if(i == 0){
              sql = " CREATE TABLE "+tableName+"( "
                  + " SEQ_ID int(20) unsigned NOT NULL auto_increment, "
                  + " USER_ID int(11) default NULL, "
                  + " TIME datetime default NULL, "
                  + " IP varchar(200) default NULL, "
                  + " TYPE varchar(30) default '1', "
                  + " REMARK text, "
                  + " USER_NAME text, "
                  + " PRIMARY KEY  (SEQ_ID), "
                  + " UNIQUE KEY SEQ_ID (SEQ_ID) "
                  + " ) ENGINE=MyISAM DEFAULT CHARSET=utf8 ";
            }
            else if(i == 1){
              sql = " CREATE TABLE seclog ( "
                  + " SEQ_ID int(10) unsigned NOT NULL auto_increment, "
                  + " USER_SEQ_ID varchar(200) default NULL, "
                  + " OP_TIME datetime default NULL, "
                  + " CLIENT_IP varchar(20) default NULL, "
                  + " OP_TYPE varchar(10) default NULL, "
                  + " OP_OBJECT text, "
                  + " OP_DESC text, "
                  + " user_name varchar(200) default NULL, "
                  + " op_result varchar(45) default NULL, "
                  + " PRIMARY KEY  (SEQ_ID) "
                  + " ) ENGINE=MyISAM DEFAULT CHARSET=utf8 ";
            }
            
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            
            YHBackupInfo backupInfoNew = new YHBackupInfo();
            backupInfoNew.setType(tableName);
            backupInfoNew.setDatetime(YHUtility.parseTimeStamp());
            backupInfoNew.setTableName(tableName+"_"+dateStr);
            orm.saveSingle(conn, backupInfoNew);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
  }
}
