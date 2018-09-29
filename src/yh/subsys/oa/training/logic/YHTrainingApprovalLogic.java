package yh.subsys.oa.training.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.training.data.YHHrTrainingPlan;

public class YHTrainingApprovalLogic {
	private static Logger log = Logger.getLogger(YHTrainingApprovalLogic.class);

	 /**
   * 培训计划名称--计划编号--模糊查找 最多显示50条--cc

   * 1.查找用户的部门id
   * 2.查找图书的借阅范围包含部门id
   * @param dbConn
   * @param condition 查询条件
   * @param user 用户
   * @return
   * @throws Exception 
   * @throws SQLException 
   */
  public List<YHHrTrainingPlan> findTrainingPlanNo(Connection dbConn, YHPerson user, String condition) throws SQLException, Exception{     

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
  
  /**
   * 获取用户信息--cc
   * @param dbConn
   * @param user
   * @param condition
   * @return
   * @throws SQLException
   * @throws Exception
   */
  public List<YHPerson> findTrainingUserSelect(Connection dbConn, YHPerson user, String condition) throws SQLException, Exception{     

    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "SELECT SEQ_ID,USER_ID,USER_NAME from PERSON where 1=1" ;
    if(!YHUtility.isNullorEmpty(condition)){
       sql += " and USER_NAME like '%" + YHDBUtility.escapeLike(condition) +"%'"; 
    }
    sql += " order by USER_ID";
    List<YHPerson> result = new ArrayList<YHPerson>();
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();  
      int cont = 0;
      while(rs.next() && ++cont < 50){
        YHPerson per = new YHPerson();
        per.setSeqId(rs.getInt("SEQ_ID"));
        per.setUserId(rs.getString("USER_ID"));
        per.setUserName(rs.getString("USER_NAME"));
        result.add(per);
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  
  /**
   * 会议管理通用列表--cc
   * 
   * @param dbConn
   * @param request
   * @param mStatus
   * @param person
   * @return
   * @throws Exception
   */
  public String getTrainingApprovalListJson(Connection dbConn, Map request, String assessingStatus, YHPerson person) throws Exception {
    String sql = "";
    if(person.isAdminRole()){
      sql = "select " 
        + "  SEQ_ID" 
        + ", ASSESSING_STATUS" 
        + ", T_PLAN_NO" 
        + ", T_PLAN_NAME" 
        + ", T_CHANNEL" 
        + ", T_COURSE_TYPES" 
        + ", T_ADDRESS" 
        + " from oa_pm_trainplan where ASSESSING_OFFICER = '" + person.getSeqId() + "' and ASSESSING_STATUS = '" + assessingStatus + "' ORDER BY SEQ_ID desc";
    }else{
      sql = "select " 
        + "  SEQ_ID" 
        + ", ASSESSING_STATUS" 
        + ", T_PLAN_NO" 
        + ", T_PLAN_NAME" 
        + ", T_CHANNEL" 
        + ", T_COURSE_TYPES" 
        + ", T_ADDRESS" 
        + " from oa_pm_trainplan where CREATE_USER_ID = '" + person.getSeqId() + "' and ASSESSING_OFFICER = '" + person.getSeqId() + "' and ASSESSING_STATUS = '" + assessingStatus + "' ORDER BY SEQ_ID desc";
    }
     

    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
    return pageDataList.toJson();
  }
  
  /**
   * 获取培训计划详细信息--cc
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHHrTrainingPlan getPlanDetail(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHHrTrainingPlan) orm.loadObjSingle(conn, YHHrTrainingPlan.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  /**
   * 获取审批人名称--cc
   * 
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */

  public String getUserNameLogic(Connection conn, int userId) throws Exception {
    String result = "";
    String sql = " select USER_NAME from PERSON where SEQ_ID = " + userId;
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
  
  /**
   * 短信提醒(带时间)--cc
   * @param conn
   * @param content
   * @param fromId
   * @param toId
   * @param type
   * @param remindUrl
   * @param sendDate
   * @throws Exception
   */
  public static void doSmsBackTime(Connection conn, String content, int fromId, String toId, String type, String remindUrl, Date sendDate)
      throws Exception {
    YHSmsBack sb = new YHSmsBack();
    sb.setContent(content);
    sb.setFromId(fromId);
    sb.setToId(toId);
    sb.setSmsType(type);
    sb.setRemindUrl(remindUrl);
    sb.setSendDate(sendDate);
    YHSmsUtil.smsBack(conn, sb);
  }
  
  /**
   * 培训计划(审批)查询--cc
   * @param dbConn
   * @param request
   * @param person
   * @param tPlanName
   * @param tChannel
   * @param assessingOfficer
   * @param assessingStatus
   * @param beginDate
   * @param endDate
   * @return
   * @throws Exception
   */
  public String getTrainingApprovalSearchList(Connection dbConn, Map request, YHPerson person, String tPlanName, String tChannel, String assessingOfficer,
      String assessingStatus, String beginDate, String endDate) throws Exception {
    String sql = "";
    if(person.isAdminRole()){
      sql = "select " 
        + "SEQ_ID" 
        + ", T_PLAN_NO" 
        + ", T_PLAN_NAME" 
        + ", T_CHANNEL" 
        + ", T_COURSE_TYPES" 
        + ", T_ADDRESS" 
        + ", ASSESSING_STATUS" 
        + " from oa_pm_trainplan where 1=1 ";
    }else{
      sql = "select " 
        + "SEQ_ID" 
        + ", T_PLAN_NO" 
        + ", T_PLAN_NAME" 
        + ", T_CHANNEL" 
        + ", T_COURSE_TYPES" 
        + ", T_ADDRESS" 
        + ", ASSESSING_STATUS" 
        + " from oa_pm_trainplan where CREATE_USER_ID = '" + person.getSeqId() + "'";
    }

    if (!YHUtility.isNullorEmpty(tPlanName)) {
      sql = sql + " and T_PLAN_NAME = '" + tPlanName + "'";
    }
    if (!YHUtility.isNullorEmpty(tChannel)) {
      sql = sql + " and T_CHANNEL = '" + tChannel + "'";
    }
    if (!YHUtility.isNullorEmpty(beginDate)) {
      sql = sql + " and " + YHDBUtility.getDateFilter("ASSESSING_TIME", beginDate, ">=");
    }

    if (!YHUtility.isNullorEmpty(endDate)) {
      sql = sql + " and " + YHDBUtility.getDateFilter("ASSESSING_TIME", endDate, "<=");
    }

    if (!YHUtility.isNullorEmpty(assessingOfficer)) {
      sql = sql + " and ASSESSING_OFFICER = '" + assessingOfficer + "'";
    }

    if (!YHUtility.isNullorEmpty(assessingStatus)) {
      sql = sql + " and ASSESSING_STATUS = '" + assessingStatus + "'";
    }
    sql = sql + " ORDER BY SEQ_ID desc";

    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);

    return pageDataList.toJson();
  }
}
