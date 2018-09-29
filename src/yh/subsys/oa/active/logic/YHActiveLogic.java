package yh.subsys.oa.active.logic;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.subsys.oa.active.data.YHActive;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHActiveLogic {
  private static Logger log = Logger.getLogger(YHActiveLogic.class);
  public int addActive(Connection dbConn,YHActive active) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, active);
    return 1;//YHCalendarLogic.getMaSeqId(dbConn, "TASK");
  }
  public void updateActive(Connection dbConn,YHActive active) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, active);
  }
  public YHActive selectActiveById(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    YHActive active =  (YHActive) orm.loadObjSingle(dbConn, YHActive.class, seqId);
    return active;
  }
  public List<YHActive>  selectActiveByWeek(Connection dbConn,String beginDate,String endDate,String userId) throws Exception {
    YHORM orm = new YHORM();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<YHActive> activeList = new ArrayList<YHActive>();
    Statement stmt = null;
    ResultSet rs = null;
    String dateStr1  = "";
    String dateStr2 = "";
    if(!beginDate.equals("")){
       dateStr1    = YHDBUtility.getDateFilter("ACTIVE_TIME", beginDate, ">=");
    }
    if(!endDate.equals("")){
      dateStr2 = YHDBUtility.getDateFilter("ACTIVE_TIME", endDate + " 24:59:59", "<=");
    }
    
    String sql = "select * from  ACTIVE where " + dateStr1 + " and " + dateStr2;
    sql = sql  + " order by ACTIVE_TIME";
    try {
      stmt = (Statement) dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        YHActive active = new YHActive();
        active.setSeqId(rs.getInt("SEQ_ID"));
        active.setActiveContent(rs.getString("ACTIVE_CONTENT"));
        active.setActiveUser(rs.getString("ACTIVE_USER"));
        active.setActiveTimeRang(rs.getString("ACTIVE_TIME_RANG"));
        active.setAttachmentId(rs.getString("ATTACHMENT_ID"));
        active.setAttachmentName(rs.getString("ATTACHMENT_ID"));
        active.setOpUserId(rs.getString("OP_USER_ID"));
        active.setOverStatus(rs.getString("OVER_STATUS"));
        if(rs.getString("ACTIVE_TIME")!=null){
          active.setActiveTime(df.parse(rs.getString("ACTIVE_TIME")));
        }else{
          active.setActiveTime(null);
        }
        if(rs.getString("OP_DATETIME")!=null){
          active.setOpDatetime(df.parse(rs.getString("OP_DATETIME")));
        }else{
          active.setOpDatetime(null);
        }
        if(!userId.equals("")){
          if(rs.getString("ACTIVE_USER")!=null&&!rs.getString("ACTIVE_USER").trim().equals("")){
            String userIdsa = rs.getString("ACTIVE_USER");
            String[] userIdArray = userIdsa.split(",");
            for (int i = 0; i < userIdArray.length; i++) {
              if(userIdArray[i].equals(userId)){
                activeList.add(active);
                break;
              }
            }
          }
        }else{
          activeList.add(active);
        }
      }  
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    //YHActive active =  (YHActive) orm.loadObjSingle(dbConn, YHActive.class, seqId);
    return activeList;
  }
  public void delActiveById(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHActive.class, seqId);
  }
}
