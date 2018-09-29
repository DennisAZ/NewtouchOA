package yh.custom.attendance.act;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
import yh.custom.attendance.data.YHDuty;
import yh.custom.attendance.logic.YHDutyLogic;
import yh.custom.attendance.logic.YHOvertimeRecordLogic;

public class YHDutyAct {

  public static final String attachmentFolder = "duty";
  private YHDutyLogic logic = new YHDutyLogic();
  
  /**
   * 添加值班申请
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addDuty (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String dutyDesc = request.getParameter("dutyDesc");
      String dutyTime = request.getParameter("dutyTime");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String endTime = request.getParameter("endTime");
      String leaderId = request.getParameter("leaderId");
      String dutyType = request.getParameter("dutyType");
      String hour = request.getParameter("hour");
      String dutyMoney = request.getParameter("dutyMoney");
      String normalAdd = request.getParameter("normalAdd");
      String festivalAdd = request.getParameter("festivalAdd");
      String weekAdd = request.getParameter("weekAdd");
      YHDuty duty = new YHDuty();
      duty.setLeaderId(leaderId);
      duty.setDutyDesc(dutyDesc);
      duty.setDutyTime(new Date());
      if(!YHUtility.isNullorEmpty("beginTime")){
        duty.setDutyTime(format.parse(dutyTime));
      }
      duty.setUserId(String.valueOf(userId));
      duty.setStatus("0");
      duty.setBeginDate(beginDate);
      duty.setEndDate(endDate);
      duty.setHour(hour);
      if(YHUtility.isNullorEmpty(normalAdd)){
        normalAdd = "0";
      }
      if(YHUtility.isNullorEmpty(weekAdd)){
        weekAdd = "0";
      }
      if(YHUtility.isNullorEmpty(dutyMoney)){
        dutyMoney = "0";
      }
      if(YHUtility.isNullorEmpty(festivalAdd)){
        festivalAdd = "0";
      }
      duty.setNormalAdd(Double.valueOf(normalAdd));
      duty.setWeekAdd(Double.valueOf(weekAdd));
      duty.setDutyMoney(Double.valueOf(dutyMoney));
      duty.setDutyType(dutyType);
      duty.setFestivalAdd(Double.valueOf(festivalAdd));
      this.logic.addDuty(dbConn, duty);
      String smsRemind = request.getParameter("smsRemind");
      //短信smsType, content, remindUrl, toId, fromId
      if(!YHUtility.isNullorEmpty(smsRemind)){
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("提交值班申请，请批示！");
        sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp");
        sb.setToId(leaderId);
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      //手机短信 提醒 
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      if(!YHUtility.isNullorEmpty(moblieSmsRemind)){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,leaderId, userId, "提交值班申请，请批示:" + dutyDesc, new Date());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    String path = request.getContextPath();
    response.sendRedirect(path+ "/custom/attendance/personal/duty/index.jsp");
    return "";
  }
  
  /**
   * 查询加班申请（待批和未批的）
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDutyJsonList (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
      List<YHDuty> overList = new ArrayList<YHDuty>();
      YHPersonLogic tpl = new YHPersonLogic();
      String[] str = {"USER_ID = '" + userId + "'" , "(STATUS = '0' or STATUS is null or STATUS = '2') order by DUTY_TIME desc" };
      overList = this.logic.getDutyList(dbConn, str);
      String data = "[";
      for (int i = 0; i < overList.size(); i++) {
        YHDuty overtime = overList.get(i);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(overtime.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        data = data + YHFOM.toJson(overtime).toString().substring(0, YHFOM.toJson(overtime).toString().length()-1 ) + ",leaderName:\"" + leaderName+ "\"},";
      }
      if(overList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteSingle(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      this.logic.deleteSingle(dbConn, Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查询加班申请ById
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDuty (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHPersonLogic tpl = new YHPersonLogic();
      String data = "";
      if(!YHUtility.isNullorEmpty(seqId)){
        YHDuty overtime  = this.logic.getDutyDetail(dbConn, Integer.parseInt(seqId));
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(overtime.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        data = data + YHFOM.toJson(overtime).toString().substring(0, YHFOM.toJson(overtime).toString().length()-1 ) + ",leaderName:\"" + leaderName+ "\"}";
      }
      if(data.equals("")){
        data = "{}";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateDuty(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String dutyDesc = request.getParameter("dutyDesc");
      Map<String,String[]> map = request.getParameterMap();
      String leaderId = request.getParameter("leaderId");
      YHDuty record = (YHDuty) YHFOM.build(map, YHDuty.class, "");
      record.setStatus("0");
      this.logic.updateDuty(dbConn, record);
      String smsRemind = request.getParameter("smsRemind");
      //短信smsType, content, remindUrl, toId, fromId
      if(smsRemind!=null){
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("提交值班申请，请批示！");
        sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp");
        sb.setToId(leaderId);
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      if(moblieSmsRemind!=null){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,leaderId, userId, "提交值班申请，请批示:" + dutyDesc, new Date());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查询加班申请（已批的）

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getHistoryDuty (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
//      String year = request.getParameter("year");
//      String month = request.getParameter("month");
//      String ymd = year + "-" +month + "-01";
      List<YHDuty> dutyList = new ArrayList<YHDuty>();
      YHPersonLogic tpl = new YHPersonLogic();
      String[] str = {"USER_ID = '" + userId + "' and STATUS = '1'"};
      dutyList = this.logic.getDutyList(dbConn, str);
      String data = "[";
      for (int i = 0; i < dutyList.size(); i++) {
        YHDuty overtime = dutyList.get(i);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(overtime.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        data = data + YHFOM.toJson(overtime).toString().substring(0, YHFOM.toJson(overtime).toString().length()-1 ) + ",leaderName:\"" + leaderName+ "\"},";
      }
      if(dutyList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateDutyStatus(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String status = request.getParameter("allow");
      String userId = request.getParameter("userId");
      String checkOut = request.getParameter("checkOut");
      String reason = request.getParameter("reason");
      String content = "";
      YHDuty duty = new YHDuty();
      duty.setSeqId(Integer.parseInt(seqId));
      duty.setStatus(status);
      duty.setReason(reason);
//          Map map = new HashMap();
//          map.put("seqId", seqId);
//          map.put("status", status);
//          this.logic.updateStatus(dbConn, map);
      this.logic.updateDuty(dbConn, duty);
      //短信smsType, content, remindUrl, toId, fromId
      if("1".equals(status)){
        content = "您的值班申请已被批准！";
      }else{
        content = "您的值班申请未被批准！";
      }
      YHSmsBack sb = new YHSmsBack();
      sb.setSmsType("6");
      sb.setContent(content);
      sb.setRemindUrl("/core/funcs/attendance/personal/index.jsp");
      sb.setToId(userId);
      sb.setFromId(userSeqId);
      YHSmsUtil.smsBack(dbConn, sb);
      if(checkOut!=null&&checkOut.equals("1")){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,userId, userSeqId, content, new Date());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
