package yh.core.funcs.mysqldb.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import yh.core.autorun.YHAutoRun;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHMysqlBackUpOutoSercive extends YHAutoRun {
  private static final Logger log = Logger.getLogger("yzq.yh.core.funcs.email.logic.YHWebmailAutoService");
  private static YHMySqlDBLogic msl = new YHMySqlDBLogic();
  /**
   * 抽取webEmail到邮件中心
   */
  public void doTask() {
    //System.out.println("YHWebmailAutoService doTask Run" + YHUtility.getCurDateTimeStr());
    Connection conn = null;
    try {
     // requestDbConn = new YHRequestDbConn(acsetDbNo);
       conn = getRequestDbConn().getSysDbConn();
      if(msl.checkDb() && canRun(conn)){
        msl.backUpauto(conn);
        msl.updateLastTime(conn);
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.debug(e.getMessage(),e);
    } finally {
      //if (conn != null) {
        YHDBUtility.closeDbConn(conn, null);
     // }
    }
    //System.out.println("YHWebmailAutoService doTask Run END " + YHUtility.getCurDateTimeStr());
  }
  
  public boolean canRun(Connection conn) throws Exception{
    String sql = "select " + " SEQ_ID" + ",TASK_TYPE" + ",`INTERVAL`"
    + ",EXEC_TIME" + ",LAST_EXEC" + ",TASK_NAME" + ",TASK_DESC"
    + ",USE_FLAG " + " from oa_office_task where TASK_CODE = ?";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.setString(1, "db_backup");
      rs = ps.executeQuery();
      if(rs.next()){
        String exctTimeTurn = "";
        String lastExec = rs.getString(5);
        String execTmie = rs.getString(4);
        String taskUse = rs.getString(8);
        int interval = rs.getInt(3);
        if(taskUse.equals("0")){
          return false;
        }
        if(interval == 0){
          lastExec = YHUtility.getDateTimeStr(new Date());
        }
        if(lastExec == null){
          lastExec = YHUtility.getDateTimeStr(new Date());
        }
        if(lastExec.length() >= 10){
          lastExec = (String) lastExec.subSequence(0, 10);
        }
        exctTimeTurn = lastExec + " " + execTmie.trim(); 
        Date exctTimeTurnDate = YHUtility.parseDate("yy-MM-dd HH:mm:ss",exctTimeTurn);
        //Date nowDate = new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(exctTimeTurnDate);
        
        
        
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        
        int day= (int) ((c2.getTimeInMillis() - c.getTimeInMillis())/(24*60*60*1000));    

        if(interval != 0 && day > interval  ){
          c.add(Calendar.DATE, day);
        }else{
          c.add(Calendar.DATE, interval);
        }
        long exctTimeTurnSeconds = c.getTimeInMillis();
        long currTimeSecondes = c2.getTimeInMillis();
        if(currTimeSecondes < exctTimeTurnSeconds){
          return false;
        }else{
          if((currTimeSecondes - exctTimeTurnSeconds) <=  (intervalSeconds*1000) ){
            return true;
          }else{
            return false;
          }
        }
      }else{
        return false;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
}
