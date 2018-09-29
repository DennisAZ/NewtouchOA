package yh.core.funcs.attendance.manage.act;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.attendance.manage.logic.YHManageOutLogic;
import yh.core.funcs.attendance.personal.data.YHAttendOut;
import yh.core.funcs.attendance.personal.logic.YHAttendOutLogic;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.workflow.util.YHFlowHookUtility;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHManageOutAct {
  /**
   * 
   * 查询所有外出记录根据自己的ID审批人
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public String selectOutManage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendOut out = new YHAttendOut();
      YHManageOutLogic yhaol = new YHManageOutLogic();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
      SimpleDateFormat formatter2 = new SimpleDateFormat("E"); 
      int userId = user.getSeqId();
      Map map = new HashMap();
      //System.out.println(userId);
      map.put("LEADER_ID", String.valueOf(userId));//审批人员
      map.put("STATUS", "0");
      map.put("ALLOW", "0");
      String[] str = {"LEADER_ID = '" + userId + "'","STATUS='0'","ALLOW='0' order by CREATE_DATE"};
      List<YHAttendOut> outList = yhaol.selectOutManage(dbConn, str);
      YHFlowHookUtility fu = new YHFlowHookUtility();
      YHFlowRunUtility ru = new YHFlowRunUtility();     
      String data = "[";
      for (int i = 0; i < outList.size(); i++) {
        YHPersonLogic tpl = new YHPersonLogic();
        out = outList.get(i);
        String submitTime = formatter1.format(out.getSubmitTime());
        String applyName = tpl.getNameBySeqIdStr(out.getUserId() , dbConn);
        if(applyName!=null&&!applyName.equals("")){
          applyName = YHUtility.encodeSpecial(applyName);
        }
        String week = formatter2.format(out.getSubmitTime());
        //System.out.println(week);
        //String mydate = formatter2.format(formatter1.parse(rs.getString("REGISTER_TIME")));
        String deptName = yhaol.selectByUserIdDept(dbConn, out.getUserId());
        
        int runId = fu.isRunHook(dbConn, "OUT_ID", out.getSeqId() + "");
        int flowId = 0;
        if (runId != 0) {
          flowId = ru.getFlowId(dbConn, runId);
        }
        
        data = data + YHFOM.toJson(outList.get(i)).toString().substring(0, YHFOM.toJson(outList.get(i)).toString().length()-1 ) + ",week:\"" + week+"\",isHookRun:\""+runId+"\",flowId:\""+flowId+"\",applyName:\"" +applyName +"\",deptName:\"" + deptName +"\"},";
      }
      if(outList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      //System.out.println(data);
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
  /*
   * 
   * updateStatus ById
   */
  public String updateStatus(HttpServletRequest request,
  HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String allow = request.getParameter("allow");
      String userId = request.getParameter("userId");
      String checkOut = request.getParameter("checkOut");
      //System.out.println(checkOut);
      //System.out.println(seqId+allow);
      Map map = new HashMap();
      map.put("seqId", seqId);
      map.put("allow", allow);
      YHAttendOut out = new YHAttendOut();
      YHAttendOutLogic yhaol = new YHAttendOutLogic();
      yhaol.updateStatus(dbConn, map);
      //短信smsType, content, remindUrl, toId, fromId
      YHSmsBack sb = new YHSmsBack();
      sb.setSmsType("6");
      sb.setContent("您的外出申请已被批准！");
      sb.setRemindUrl("/core/funcs/attendance/personal/index.jsp");
      sb.setToId(userId);
      sb.setFromId(userSeqId);
      YHSmsUtil.smsBack(dbConn, sb);
      if(checkOut!=null&&checkOut.equals("1")){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,userId, userSeqId, "您的外出申请已被批准！", new Date());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/attendance/manage/manage.jsp";    
  }
  /*
   * 
   * updateStatus ById
   */
  public String updateReason(HttpServletRequest request,
  HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String allow = request.getParameter("allow");
      String reason = request.getParameter("reason");
      String userId = request.getParameter("userId");
      String checkOut = request.getParameter("checkOut");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      reason = reason.replaceAll("\\\n", "");
      reason = reason.replaceAll("\\\r", "");
      //System.out.println(seqId+allow);
      YHAttendOut out = new YHAttendOut();
      Map map = new HashMap();
      map.put("seqId", seqId);
      map.put("allow", allow);
      map.put("reason", reason);
      YHAttendOutLogic yhaol = new YHAttendOutLogic();
      yhaol.updateStatus(dbConn, map);
      //短信smsType, content, remindUrl, toId, fromId
      YHSmsBack sb = new YHSmsBack();
      sb.setSmsType("6");
      sb.setContent("您的外出申请未被批准！");
      sb.setRemindUrl("/core/funcs/attendance/personal/index.jsp");
      sb.setToId(userId);
      sb.setFromId(userSeqId);
      YHSmsUtil.smsBack(dbConn, sb);
      
      if(checkOut!=null&&checkOut.equals("1")){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,userId, userSeqId, "您的外出申请未被批准！原因："+reason, new Date());
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
  /*
   * 得到开始日期和结束日期
   * 得到UserId
   */
  public String selectOut(HttpServletRequest request,
  HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
      String userId = request.getParameter("userId");
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
      beginTime = beginTime + " 00:00:00";
      endTime = endTime + " 23:59:59";
      //System.out.println(beginTime+endTime);
      List<YHAttendOut> outList = new ArrayList<YHAttendOut>();
    
      YHManageOutLogic yhaol = new YHManageOutLogic();
      if(!userId.equals("")){
        String newUserId = "";
        String[] userIdArray = userId.split(",");
        for (int i = 0; i < userIdArray.length; i++) {
          newUserId = newUserId + "'" + userIdArray[i] + "',";
        }
        newUserId = newUserId.substring(0, newUserId.length()-1);
        String str[] = {"USER_ID in(" + newUserId + ")",YHDBUtility.getDateFilter("SUBMIT_TIME", beginTime, ">="),YHDBUtility.getDateFilter("SUBMIT_TIME", endTime, "<=")+" order by SUBMIT_TIME"};
        outList = yhaol.selectOutManage(dbConn, str);
      }
      String data = "[";
      YHPersonLogic tpl = new YHPersonLogic();
      for (int i = 0; i < outList.size(); i++) {
        YHAttendOut out = outList.get(i);
        String userName = tpl.getNameBySeqIdStr(out.getUserId(), dbConn);
        if(userName!=null&&!userName.equals("")){
          userName = YHUtility.encodeSpecial(userName);
        }
        String leaderName = tpl.getNameBySeqIdStr(out.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        String deptName = yhaol.selectByUserIdDept(dbConn, out.getUserId());
        data = data + YHFOM.toJson(outList.get(i)).toString().substring(0, YHFOM.toJson(outList.get(i)).toString().length()-1 ) + ",userName:\""+userName+"\",leaderName:\"" +leaderName +"\",deptName:\"" + deptName +"\"},";
      }
      if(outList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      //System.out.println(data);
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
  /*
   * 根据ID更改外出的时间
   * 
   */
  public String updateOutById(HttpServletRequest request,
  HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String outDate = request.getParameter("outDate");
      String outTime1 = request.getParameter("outTime1");
      String outTime2 = request.getParameter("outTime2");
      String outType = request.getParameter("outType");
      outType = outType.replaceAll("\\\n", "");
      outType = outType.replaceAll("\\\r", "");
      YHAttendOut  out = new YHAttendOut();
      YHAttendOutLogic outLogic = new YHAttendOutLogic();
      out = outLogic.selectOutById(dbConn, seqId);
      out.setOutTime1(outTime1);
      out.setOutTime2(outTime2);
      out.setOutType(outType);
      out.setSubmitTime(YHUtility.parseDate(outDate + " " + outTime1 + ":00"));
      outLogic.updateOut(dbConn, out);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "{}");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
