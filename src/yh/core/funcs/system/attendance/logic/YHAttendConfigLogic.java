package yh.core.funcs.system.attendance.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.system.attendance.data.YHAttendConfig;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHAttendConfigLogic {
  private static Logger log = Logger.getLogger(YHAttendConfigLogic.class);
  public void addConfig(Connection dbConn, YHAttendConfig config) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, config);  
  }
  public List<YHAttendConfig> selectConfig(Connection dbConn,Map map) throws Exception {
    List<YHAttendConfig> configList = new ArrayList<YHAttendConfig>();
    YHORM orm = new YHORM();
    configList = orm.loadListSingle(dbConn, YHAttendConfig.class, map);
    return configList;
  }
  public YHAttendConfig selectConfigById(Connection dbConn,String seqIds) throws Exception {
    YHORM orm = new YHORM();
    YHAttendConfig config = new YHAttendConfig ();
    int seqId = 0;
    if(!seqIds.equals("")){
      seqId = Integer.parseInt(seqIds);
    }
    config = (YHAttendConfig) orm.loadObjSingle(dbConn, YHAttendConfig.class, seqId);
    return config;
  }
  public void updateConfig(Connection dbConn, YHAttendConfig config) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, config);
  }
  public void deleteConfig(Connection dbConn, String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHAttendConfig.class, Integer.parseInt(seqId));
  }
  public void updateConfigGenaralById(Connection dbConn,String seqId,  String general) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "update oa_attendance_conf set GENERAL = '" + general + "' where SEQ_ID = " + seqId;
    //System.out.println(sql);
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
}
