package yh.subsys.oa.fillRegister.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.attendance.personal.data.YHAttendDuty;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.attendance.data.YHAttendConfig;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.fillRegister.data.YHAttendScore;
import yh.subsys.oa.fillRegister.data.YHAttendTime;
import yh.subsys.oa.training.data.YHHrTrainingPlan;

public class YHAttendScoreLogic {
  private static Logger log = Logger.getLogger(YHAttendScoreLogic.class);

  public void add(Connection dbConn, YHAttendScore record) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, record);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  public void updateRecord(Connection conn, YHAttendScore record) throws Exception {
    try {
          YHORM orm = new YHORM();
          orm.updateSingle(conn, record);
        } catch (Exception ex) {
          throw ex;
        } finally {
      }
    }
  
  /**
   * 判断是否存在数据
   * @param dbConn
   * @param registerType
   * @param createTime
   * @return
   * @throws Exception
   */
  public boolean getAttendScoreFlag(Connection dbConn, String registerType, java.util.Date createTime)
  throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) from oa_attendance_integral where REGISTER_TYPE = '" + registerType + "' and "+YHDBUtility.getDayFilter("CREATE_TIME", createTime)+"";
      rs = stmt.executeQuery(sql);
      long count = 0;
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if(count > 0){
        return true;
      }else{
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 获取存在数据的SEQ_ID
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public String getAttendScoreSeqId(Connection conn, String registerType, java.util.Date createTime) throws Exception {
    String result = "";
    String sql = "SELECT SEQ_ID from oa_attendance_integral where REGISTER_TYPE = '" + registerType + "' and "+YHDBUtility.getDayFilter("CREATE_TIME", createTime)+"";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public void addRegister(Connection dbConn, YHAttendScore record) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, record);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  /**
   * 获取分值
   * @param dbConn
   * @param score
   * @return
   * @throws Exception
   */
  public double getAttendTimeScoreLogic(Connection dbConn, long lateTime, int dutyId) throws Exception {
    YHORM orm = new YHORM();
    double returnScore = 0;
    double score = 0;
    try {
      String[] filters = {" DUTY_ID=" + dutyId};
      List<YHAttendTime> attendTimes = (List<YHAttendTime>) orm.loadListSingle(dbConn, YHAttendTime.class, filters);
      if (attendTimes != null || attendTimes.size() != 0) {
        for (YHAttendTime attendTime : attendTimes) {
          long minLateTime = (long)attendTime.getMinLateTime();
          long maxLateTime = (long)attendTime.getMaxLateTime();
          if ((minLateTime <= lateTime) && (lateTime <= maxLateTime)) {
            returnScore = attendTime.getScore();
            break;
          }
        }
      }
      if(returnScore == 0){
        score = Double.parseDouble(getMaxScore(dbConn, dutyId));
      }else{
        score = returnScore;
      }
    } catch (Exception e) {
      throw e;
    }
    return score;
  }
  
  /**
   * 获取需要扣除的分数
   * @param dbConn
   * @param time
   * @param date
   * @param dutyType
   * @return
   * @throws Exception
   */
  public double getUpScore(Connection dbConn, java.util.Date time, java.util.Date date, String dutyType, String dutyFlag, String registerType, int dutyId) throws Exception {
    double scores = 0;
    try {
      long curTime = Math.abs(time.getTime() - date.getTime());
      long day = curTime/(24*60*60*1000);  
      long hour = (curTime/(60*60*1000) - day*24);  
      long min = ((curTime/(60*1000)) - day*24*60 - hour*60); 
      long lateTime = hour*60 + min;
      if("1".equals(dutyType)){
        if(registerType.equals(dutyFlag)){
          int timeNum = time.compareTo(date);
          if(timeNum < 0){
            scores = 0;
          }else{
            scores = getAttendTimeScoreLogic(dbConn, lateTime, dutyId);
          }
        }else{
          //scores = getAttendTimeScoreLogic(dbConn, lateTime);
          scores = Double.parseDouble(getMaxScore(dbConn, dutyId));
        }
      }else{
        if(registerType.equals(dutyFlag)){
          int timeNum = time.compareTo(date);
          if(timeNum > 0){
            scores = 0;
          }else{
            scores = getAttendTimeScoreLogic(dbConn, lateTime, dutyId);
          }
        }else{
          //scores = getAttendTimeScoreLogic(dbConn, lateTime);
          scores = Double.parseDouble(getMaxScore(dbConn, dutyId));
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return scores;
  }
  
  /**
   * 修改ATTEND_SCORE表(考勤登记扣分表)
   * @param dbConn
   * @param record
   * @param person
   * @param time
   * @param dutyType  考勤类型(上班1，下班2)
   * @param score     所扣的分数
   * @param assessingStatus  补登记是否审核通过（0，1，2）
   * @param attendFlag    是否是补登记
   * @param seqId
   * @throws Exception
   */
  public void getUpdateRegisterFunc(Connection dbConn, YHAttendScore record, YHPerson person, java.util.Date time, String dutyType, double score, String assessingStatus, String attendFlag, int seqId) throws Exception{
    record.setSeqId(seqId);
    record.setCreateTime(time);
    record.setDutyType(dutyType);
    record.setScore(score);
    record.setUserId(String.valueOf(person.getSeqId()));
    record.setAssessingStatus(assessingStatus);
    record.setAttendFlag(attendFlag);
    updateRecord(dbConn, record);
  }  
  
  public void getAddRegisterFunc(Connection dbConn, YHAttendScore record, YHPerson person, java.util.Date time, String dutyType, double scores, String assessingStatus, String attendFlag, String selfType) throws Exception{
    record.setCreateTime(time);
    record.setUserId(String.valueOf(person.getSeqId()));
    record.setDutyType(dutyType);
    record.setScore(scores);
    record.setAssessingStatus(assessingStatus);
    record.setAttendFlag(attendFlag);
    record.setRegisterType(selfType);
    addRegister(dbConn, record);
  }  
  
  /**
   * 更新ATTEND_SCORE表(考勤登记扣分表) 所扣的分数
   * @param dbConn
   * @param record
   * @param person
   * @param time
   * @param dutyType  考勤类型(上班1，下班2)
   * @param scores    所扣的分数
   * @param selfType  第几次登记
   * @param registerType 登记类型(1,2,3,4,5,6)
   * @throws Exception
   */
  public void getUpdateRegister(Connection dbConn, YHAttendScore record, YHPerson person, java.util.Date time, String dutyType, double scores, String selfType, String registerType) throws Exception{
    if(selfType.equals(registerType)){
      double numScore = 0;
      String maxScore = getMaxScore(dbConn, person.getDutyType());
      if(scores <= Double.parseDouble(maxScore)){
        numScore = scores;
      }else{
        numScore = Integer.parseInt(maxScore);
      }
      int seqId = 0;
      if(!YHUtility.isNullorEmpty(getAttendScoreSeqId(dbConn, selfType, time))){
        seqId = Integer.parseInt(getAttendScoreSeqId(dbConn, selfType, time));
      }
      getUpdateRegisterFunc(dbConn, record, person, time, dutyType, numScore, "1", "0", seqId);
    }
  }
  
  /**
   * 获取最大扣除的分数
   * @param conn
   * @return
   * @throws Exception
   */
  public String getMaxScore(Connection conn, int dutyId) throws Exception {
    String result = "0";
    String sql = " select MAX(SCORE) from oa_attendance_time where DUTY_ID = " + dutyId + "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public String getScore(Connection conn, int userId) throws Exception {
    String result = "";
    String sql = " select SCORE from oa_attendance_integral where SEQ_ID = " + userId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public List<YHAttendDuty> getAttendDutyJson(Connection dbConn, Map request, YHPerson person, String beginDate, String endDate) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "select " 
        + "SEQ_ID" 
        + ", DUTY_TIME1" 
        + ", DUTY_TIME2" 
        + ", DUTY_TIME3" 
        + ", DUTY_TIME4" 
        + ", DUTY_TIME5" 
        + ", DUTY_TIME6" 
        + ", DUTY_TYPE1"  
        + ", DUTY_TYPE2" 
        + ", DUTY_TYPE3" 
        + ", DUTY_TYPE4" 
        + ", DUTY_TYPE5" 
        + ", DUTY_TYPE6" 
        + " from oa_attendance_conf SEQ_ID = '"+ person.getDutyType() +"'";
    if (!YHUtility.isNullorEmpty(beginDate)) {
      sql = sql + " and " + YHDBUtility.getDateFilter("REGISTER_TIME", beginDate, ">=");
    }
    if (!YHUtility.isNullorEmpty(endDate)) {
      sql = sql + " and " + YHDBUtility.getDateFilter("REGISTER_TIME", endDate, ">=");
    }
    sql = sql + " ORDER BY SEQ_ID desc";
    List<YHAttendDuty> attend = new ArrayList<YHAttendDuty>();
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();  
      int cont = 0;
      while(rs.next() && ++cont < 50){
        YHAttendDuty attendDuty = new YHAttendDuty();
        attendDuty.setSeqId(rs.getInt("SEQ_ID"));
        attendDuty.setUserId(rs.getString("USER_ID"));
        attendDuty.setRegisterType(rs.getString("REGISTER_TYPE"));
        attendDuty.setRegisterTime(rs.getDate("REGISTER_TIME"));
        attend.add(attendDuty);
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return attend;
  }
  
  
  public YHAttendConfig selectConfigById(Connection dbConn, String seqIds)
  throws Exception {
    YHORM orm = new YHORM();
    YHAttendConfig config = new YHAttendConfig();
    int seqId = 0;
    if (!seqIds.equals("")) {
      seqId = Integer.parseInt(seqIds);
      config = (YHAttendConfig) orm.loadObjSingle(dbConn, YHAttendConfig.class,
      seqId);
    }
    return config;
  }
  
  
  public List<YHHrTrainingPlan> getAttendDutyJson0(Connection dbConn, YHPerson user, String condition) throws SQLException, Exception{     

    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "SELECT SEQ_ID,T_PLAN_NO,T_PLAN_NAME, T_INSTITUTION_NAME from oa_pm_trainplan where 1=1" ;
    if(!YHUtility.isNullorEmpty(condition)){
       sql += " and T_PLAN_NAME like '%" + YHDBUtility.escapeLike(condition) +"%'"; 
    }
    sql += " order by T_PLAN_NO";
    List<YHHrTrainingPlan> hrTrainingPlan = new ArrayList<YHHrTrainingPlan>();
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();  
      int cont = 0;
      while(rs.next() && ++cont < 50){
        YHHrTrainingPlan trainingPlan = new YHHrTrainingPlan();
        trainingPlan.setSeqId(rs.getInt("SEQ_ID"));
        trainingPlan.setTPlanNo(rs.getString("T_PLAN_NO"));
        trainingPlan.setTPlanName(rs.getString("T_PLAN_NAME"));
        trainingPlan.setTInstitutionName(rs.getString("T_INSTITUTION_NAME"));
        hrTrainingPlan.add(trainingPlan);
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return hrTrainingPlan;
  }
  
}
