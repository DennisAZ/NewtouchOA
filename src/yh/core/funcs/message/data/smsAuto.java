package yh.core.funcs.message.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import yh.core.autorun.YHAutoRun;
import yh.core.funcs.system.ispirit.n12.org.logic.YHIsPiritLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;


public class smsAuto extends YHAutoRun{
  private static final Logger log = Logger.getLogger("yh.core.funcs.message.data.smsAuto");

  //定时发送提醒  以及 微讯
  @Override
 
  public void doTask() throws Exception {
    Connection conn =getRequestDbConn().getSysDbConn();
    isRemindSms(conn);  //检查提醒事务
    isRemindMessage(conn);  //检查微讯
  }
  
  /**
   * 是否有为提醒的
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public void isRemindSms(Connection conn) throws Exception{
    int result = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String dateFiler = YHDBUtility.getDateFilter("T0.SEND_TIME", YHUtility.getDateTimeStr(new Date()), "<=");
    String dbDateFremind = YHDBUtility.getDateFilter("T1.REMIND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String sql = "SELECT to_id FROM SMS T1 ,oa_msg_body T0 WHERE  REMIND_FLAG = '1' AND  T1.BODY_SEQ_ID= T0.SEQ_ID  " +
        "and DELETE_FLAG in (0, 2) " +
        "AND " + dateFiler +
        " AND (T1.REMIND_TIME IS NULL OR " + dbDateFremind + ")";

    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        String uid = rs.getString(1);
        if(uid.compareToIgnoreCase("1")>=0){
          
           YHIsPiritLogic.setUserSmsRemind(uid);
        }
        //log.info("***"+uid);
          
      }
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
   
  }

  
  /**
   * 是否有微讯
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public void isRemindMessage(Connection conn) throws Exception{
    int result = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String dateFiler = YHDBUtility.getDateFilter("T0.SEND_TIME", YHUtility.getDateTimeStr(new Date()), "<=");
    String dbDateFremind = YHDBUtility.getDateFilter("T1.REMIND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
   try{
   String sql = "SELECT to_id FROM oa_message T1 ,oa_message_body T0 WHERE  REMIND_FLAG = '1' AND  T1.BODY_SEQ_ID= T0.SEQ_ID  " +
      "and DELETE_FLAG in (0, 2) " +
      "AND " + dateFiler +
      " AND (T1.REMIND_TIME IS NULL OR " + dbDateFremind + ")";
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        String uid = rs.getString(1);
        if(uid.compareToIgnoreCase("1")>=0){
           YHIsPiritLogic.setUserMessageRemind(uid);
        }
   //   System.out.println(uid);
        
      }
      
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
   
  }

 
}
