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
import yh.core.funcs.attendance.manage.logic.YHManageEvectionLogic;
import yh.core.funcs.attendance.manage.logic.YHManageOutLogic;
import yh.core.funcs.attendance.personal.data.YHAttendEvection;
import yh.core.funcs.attendance.personal.logic.YHAttendEvectionLogic;
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

public class YHManageEvectionAct {
  /**
   * 
   * 查询所有出差记录根据自己的ID审批人
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public String selectEvectionManage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendEvection evection = new YHAttendEvection();
      YHManageEvectionLogic yhael = new YHManageEvectionLogic();
      YHManageOutLogic yhaol = new YHManageOutLogic();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
      SimpleDateFormat formatter2 = new SimpleDateFormat("E"); 
      int userId = user.getSeqId();
      Map map = new HashMap();
      map.put("STATUS", "1");
      map.put("ALLOW", "0");
      List<YHAttendEvection> evectionList = yhael.selectEvectionManage(dbConn, map);
      YHFlowHookUtility fu = new YHFlowHookUtility();
      YHFlowRunUtility ru = new YHFlowRunUtility();
      String data = "[";
      for (int i = 0; i < evectionList.size(); i++) {
        YHPersonLogic tpl = new YHPersonLogic();
        evection = evectionList.get(i);
        String applyName = tpl.getNameBySeqIdStr(evection.getUserId() , dbConn);
        if(applyName!=null&&!applyName.equals("")){
          applyName = YHUtility.encodeSpecial(applyName);
        }
        String week1 = formatter2.format(evection.getEvectionDate1());
        String week2 = formatter2.format(evection.getEvectionDate2());
        int runId = fu.isRunHook(dbConn, "EVECTION_ID", evection.getSeqId() + "");
        int flowId = 0;
        if (runId != 0) {
          flowId = ru.getFlowId(dbConn, runId);
        }
        
        String deptName = yhaol.selectByUserIdDept(dbConn, evection.getUserId());
        data = data + YHFOM.toJson(evectionList.get(i)).toString().substring(0, YHFOM.toJson(evectionList.get(i)).toString().length()-1 ) + ",isHookRun:\""+runId+"\",flowId:\""+ flowId +"\",week1:\"" + week1+ "\",week2:\"" + week2+ "\",applyName:\"" +applyName +"\",deptName:\"" + deptName +"\"},";
      }
      if(evectionList.size()>0){
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
  /**
   * 更改出差状态(批准)
   * @param request
   * @param response
   * @return
   * @throws Exception
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
      if(YHUtility.isInteger(seqId)){
        Map map = new HashMap();
        map.put("seqId", seqId);
        map.put("allow", allow);
        YHAttendEvectionLogic yhall = new YHAttendEvectionLogic();
        yhall.updateEvectionStatus(dbConn, map);
        //短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("您的出差申请已被批准！");
        sb.setRemindUrl("/core/funcs/attendance/personal/index.jsp");
        sb.setToId(userId);
        sb.setFromId(userSeqId);
        YHSmsUtil.smsBack(dbConn, sb);
        String checkEvection = request.getParameter("checkEvection");
        if(checkEvection!=null&&checkEvection.equals("1")){
          YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
          sms2Logic.remindByMobileSms(dbConn,userId, userSeqId, "您的出差申请已被批准", new Date());
        }
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
  /**
   * 外出不批准(说明理由)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateReason(HttpServletRequest request,
  HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String allow = request.getParameter("allow");
      String notReason = request.getParameter("notReason");
      String userId = request.getParameter("userId");
      notReason = notReason.replaceAll("\\\n","");
      notReason = notReason.replaceAll("\\\r","");
      Map map = new HashMap();
      map.put("seqId", seqId);
      map.put("allow", allow);
      map.put("notReason", notReason);
      YHAttendEvectionLogic yhall = new YHAttendEvectionLogic();
      yhall.updateEvectionStatus(dbConn, map);
      //短信smsType, content, remindUrl, toId, fromId
      YHSmsBack sb = new YHSmsBack();
      sb.setSmsType("6");
      sb.setContent("您的出差申请未被批准！");
      sb.setRemindUrl("/core/funcs/attendance/personal/index.jsp");
      sb.setToId(userId);
      sb.setFromId(userSeqId);
      YHSmsUtil.smsBack(dbConn, sb);
      
      String checkEvection = request.getParameter("checkEvection");
      if(checkEvection!=null&&checkEvection.equals("1")){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,userId, userSeqId, "您的出差申请未被批准！内容："+notReason, new Date());
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
  public String selectEvection(HttpServletRequest request,
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
          YHManageEvectionLogic evectionLogic = new YHManageEvectionLogic();
          YHManageOutLogic yhaol = new YHManageOutLogic();
          List<YHAttendEvection> evectionList = new ArrayList<YHAttendEvection>();
          if(!userId.equals("")){
            String newUserId = "";
            String[] userIdArray = userId.split(",");
            for (int i = 0; i < userIdArray.length; i++) {
              newUserId = newUserId + "'" + userIdArray[i] + "',";
            }
            newUserId = newUserId.substring(0, newUserId.length()-1);
            String str[] = {"USER_ID in(" + userId + ")",YHDBUtility.getDateFilter("EVECTION_DATE1", beginTime, ">="),YHDBUtility.getDateFilter("EVECTION_DATE2", endTime, "<="),"ALLOW='1' order by EVECTION_DATE1"};
            evectionList = evectionLogic.selectEvectionManage(dbConn, str);
          }
          String data = "[";
          YHPersonLogic tpl = new YHPersonLogic();
          for (int i = 0; i < evectionList.size(); i++) {
            YHAttendEvection evection = evectionList.get(i);
            String userName = tpl.getNameBySeqIdStr(evection.getUserId(), dbConn);
            if(userName!=null&&!userName.equals("")){
              userName = YHUtility.encodeSpecial(userName);
            }
            String leaderName = tpl.getNameBySeqIdStr(evection.getLeaderId() , dbConn);
            if(leaderName!=null&&!leaderName.equals("")){
              leaderName = YHUtility.encodeSpecial(leaderName);
            }
            String deptName = yhaol.selectByUserIdDept(dbConn, evection.getUserId());
            data = data + YHFOM.toJson(evectionList.get(i)).toString().substring(0, YHFOM.toJson(evectionList.get(i)).toString().length()-1 ) + ",userName:\"" + userName + "\",leaderName:\"" +leaderName +"\",deptName:\"" + deptName +"\"},";
          }
          if(evectionList.size()>0){
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
   * 更新ById
   */
  public String updateEvectionById(HttpServletRequest request,
  HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String evectionDate1 = request.getParameter("evectionDate1");
      String evectionDate2 = request.getParameter("evectionDate2");
      String reason = request.getParameter("reason");
      reason = reason.replaceAll("\\\n","");
      reason = reason.replaceAll("\\\r","");
      String evectionDest = request.getParameter("evectionDest");
      YHAttendEvection evection = new YHAttendEvection();
      YHAttendEvectionLogic evectionLogic = new YHAttendEvectionLogic();
      evection = evectionLogic.selectEvectionById(dbConn, seqId);
      evection.setReason(reason);
      if(!evectionDate1.equals("")){
        evection.setEvectionDate1(YHUtility.parseDate(evectionDate1));
      }else{
        evection.setEvectionDate1(null);
      }
      if(!evectionDate2.equals("")){
        evection.setEvectionDate2(YHUtility.parseDate(evectionDate2));
      }else{
        evection.setEvectionDate2(null);
      }
      evection.setEvectionDest(evectionDest);
      evectionLogic.updateEvection(dbConn, evection);
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
