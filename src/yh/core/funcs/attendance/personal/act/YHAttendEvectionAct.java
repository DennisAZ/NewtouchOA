package yh.core.funcs.attendance.personal.act;

import java.net.InetAddress;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.attendance.personal.data.YHAttendEvection;
import yh.core.funcs.attendance.personal.logic.YHAttendEvectionLogic;
import yh.core.funcs.attendance.personal.logic.YHAttendLeaveLogic;
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
import yh.subsys.oa.fillRegister.logic.YHAttendFillLogic;

public class YHAttendEvectionAct {
  private YHAttendEvectionLogic logic = new YHAttendEvectionLogic();
  /**
   * 添加出差申请
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addEvection(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendEvection evection = new YHAttendEvection();
      YHAttendEvectionLogic yhael = new YHAttendEvectionLogic();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String evectionDate1 = request.getParameter("evectionDate1");
      String evectionDate2 = request.getParameter("evectionDate2");
      String smsRemind = request.getParameter("smsRemind");
      String userSeqId = request.getParameter("user");
      if(userSeqId!=null&&!userSeqId.equals("")){
        userId = Integer.parseInt(userSeqId);
      }
      //得到客户端的IP地址
      YHAttendDutyAct dutyAct = new YHAttendDutyAct();
      String registerIp = dutyAct.getIpAddr(request);
      InetAddress inet = InetAddress.getLocalHost();
      String localIp = inet.getHostAddress();
      if(registerIp!=null&&registerIp.equals("127.0.0.1")){
        registerIp = localIp;
      }
      String reason = request.getParameter("reason");
      reason = reason.replaceAll("\\\n","");
      reason = reason.replaceAll("\\\r","");
      String evectionDest = request.getParameter("evectionDest");
      YHFOM fom = new YHFOM();
      evection = (YHAttendEvection) fom.build(request.getParameterMap());
      evection.setReason(reason);
      evection.setEvectionDest(evectionDest);
      evection.setRegisterIp(registerIp);
      evection.setAllow("0");
      evection.setStatus("1");
      evection.setUserId(String.valueOf(userId));
      evection.setEvectionDate1(YHUtility.parseDate("yyyy-MM-dd",evectionDate1));
      evection.setEvectionDate2(YHUtility.parseDate("yyyy-MM-dd",evectionDate2));
      YHAttendLeaveLogic all = new YHAttendLeaveLogic();
      double hour = all.getHourDiff(evectionDate1, evectionDate2, "yyyy-MM-dd");
      evection.setHour(hour);
      yhael.addEvection(dbConn, evection);
      
      YHFlowHookUtility ut = new YHFlowHookUtility();
      int attendEvectionId = ut.getMax(dbConn, "select max(SEQ_ID) FROM oa_attendance_trip");
      Map dataArray = new HashMap();
      dataArray.put("KEY", attendEvectionId + "");
      dataArray.put("FIELD", "EVECTION_ID");
      dataArray.put("USER_ID", evection.getUserId()+"");
      YHPersonLogic p = new YHPersonLogic();
      String userName = p.getUserNameLogic(dbConn, Integer.parseInt(evection.getUserId()));
      dataArray.put("USER_NAME", userName);
      String leaderName= p.getUserNameLogic(dbConn, Integer.parseInt(evection.getLeaderId()));
      dataArray.put("LEADER_ID",leaderName);
      dataArray.put("REASON", reason);
      dataArray.put("EVECTION_DEST", evectionDest);
      dataArray.put("EVECTION_DATE1",evectionDate1);
      dataArray.put("EVECTION_DATE2",evectionDate2);
      String url = ut.runHook(dbConn, user, dataArray, "attend_evection");
      if (!"".equals(url)) {
        String path = request.getContextPath();
        response.sendRedirect(path+ url);
        return null;
      }
      
      
      //短信smsType, content, remindUrl, toId, fromId
      if(smsRemind!=null){
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("提交出差申请，请批示！");
        sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp");
        sb.setToId(evection.getLeaderId());
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      if(moblieSmsRemind!=null){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,evection.getLeaderId(), userId, "提交出差申请，请批示:" + reason, new Date());
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
    response.sendRedirect(path+ "/core/funcs/attendance/personal/evection.jsp");
    return "";
  }
  /*
   * 
   *查询所有出差记录
   */
  public String selectHistroyEvection(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHAttendEvection evection = new YHAttendEvection();
      YHAttendEvectionLogic yhael = new YHAttendEvectionLogic();
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      String whereStr = "";
      String ymd = year + "-" + month + "-" + "01";
      if(!YHUtility.isNullorEmpty(year) || !YHUtility.isNullorEmpty(month)){
        whereStr += " and " + YHDBUtility.getMonthFilter("EVECTION_DATE1", YHUtility.parseDate(ymd));
      }
      String data = "[";
      Map map = new HashMap();
      map.put("USER_ID", userId);
      map.put("STATUS", "2 order by EVECTION_DATE1 desc");
      String[] str = {"USER_ID='"+userId +"' and STATUS='2'" + whereStr + " order by EVECTION_DATE1 desc"};
      List<YHAttendEvection> evectionList = yhael.selectEvection(dbConn, str);
      YHFlowHookUtility fu = new YHFlowHookUtility();
      YHFlowRunUtility ru = new YHFlowRunUtility();
      for (int i = 0; i < evectionList.size(); i++) {
        YHPersonLogic tpl = new YHPersonLogic();
        evection = evectionList.get(i);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(evection.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
          int runId = fu.isRunHook(dbConn, "EVECTION_ID", evection.getSeqId() + "");
          int flowId = 0;
          if (runId != 0) {
            flowId = ru.getFlowId(dbConn, runId);
        
          
          
        }
        data = data + YHFOM.toJson(evectionList.get(i)).toString().substring(0, YHFOM.toJson(evectionList.get(i)).toString().length()-1 ) + ",isHookRun:\""+runId+"\",flowId:\""+flowId+"\",leaderName:\"" + leaderName + "\"},";
      }
      if(evectionList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
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
   *查询历史请假记录
   */
  public String selectEvection(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHAttendEvection evection = new YHAttendEvection();
      YHAttendEvectionLogic yhael = new YHAttendEvectionLogic();
      String data = "[";
      Map map = new HashMap();
      map.put("USER_ID", userId);
      map.put("STATUS", "1");
      YHFlowHookUtility fu = new YHFlowHookUtility();
      YHFlowRunUtility ru = new YHFlowRunUtility();
      List<YHAttendEvection> evectionList = yhael.selectEvection(dbConn, map);
      for (int i = 0; i < evectionList.size(); i++) {
        YHPersonLogic tpl = new YHPersonLogic();
        evection = evectionList.get(i);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(evection.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        
        int runId = fu.isRunHook(dbConn, "EVECTION_ID", evection.getSeqId() + "");
        int flowId = 0;
        if (runId != 0) {
          flowId = ru.getFlowId(dbConn, runId);
        }
        
        data = data + YHFOM.toJson(evectionList.get(i)).toString().substring(0, YHFOM.toJson(evectionList.get(i)).toString().length()-1 ) + ","+"isHookRun:\""+runId+"\",flowId:\""+ flowId +"\",leaderName:\"" + leaderName + "\"},";
      }
      if(evectionList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
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
   *查询一条请假记录ById
   *   */
  public String selectEvectionById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHAttendEvection evection = new YHAttendEvection();
      YHAttendEvectionLogic yhael = new YHAttendEvectionLogic();
      evection = yhael.selectEvectionById(dbConn, seqId);
      String data = "";
      if(!"".equals(evection.getLeaderId())){
        YHPersonLogic tpl = new YHPersonLogic();
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(evection.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName.substring(0, leaderName.length()-1));
        }
        data = data + YHFOM.toJson(evection).toString().substring(0, YHFOM.toJson(evection).toString().length()-1 ) + ",leaderName:\"" + leaderName + "\"}";
      }
      if(data.equals("")){
        data = data + "{}";
      }
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
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
   * 删除出差记录ById
   */
  public String deleteEvectionById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHAttendEvection evection = new YHAttendEvection();
      YHAttendEvectionLogic yhael = new YHAttendEvectionLogic();
      yhael.deleteEvectionById(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 更新出差申请
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateEvection(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendEvection evection = new YHAttendEvection();
      YHAttendEvectionLogic yhael = new YHAttendEvectionLogic();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String evectionDate1 = request.getParameter("evectionDate1");
      String evectionDate2 = request.getParameter("evectionDate2");
      //得到客户端的IP地址
      YHAttendDutyAct dutyAct = new YHAttendDutyAct();
      String registerIp = dutyAct.getIpAddr(request);
      InetAddress inet = InetAddress.getLocalHost();
      String localIp = inet.getHostAddress();
      if(registerIp!=null&&registerIp.equals("127.0.0.1")){
        registerIp = localIp;
      }
      String reason = request.getParameter("reason");
      reason = reason.replaceAll("\\\n","");
      reason = reason.replaceAll("\\\r","");
      String evectionDest = request.getParameter("evectionDest"); 
      YHFOM fom = new YHFOM();
      evection = (YHAttendEvection) fom.build(request.getParameterMap());
    //  reason = new String(reason.getBytes("iso-8859-1"), "utf-8"); 
      evection.setReason(reason);
      evection.setEvectionDest(evectionDest);
      evection.setRegisterIp(registerIp);
      evection.setAllow("0");
      evection.setStatus("1");
      evection.setUserId(String.valueOf(userId));
      evection.setEvectionDate1(YHUtility.parseDate("yyyy-MM-dd",evectionDate1));
      evection.setEvectionDate2(YHUtility.parseDate("yyyy-MM-dd",evectionDate2));
      YHAttendLeaveLogic all = new YHAttendLeaveLogic();
      double hour = all.getHourDiff(evectionDate1, evectionDate2, "yyyy-MM-dd");
      evection.setHour(hour);
      yhael.updateEvection(dbConn, evection);
      String smsRemind = request.getParameter("smsRemind");
      //短信smsType, content, remindUrl, toId, fromId
      if(smsRemind!=null){
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("提交出差申请，请批示！");
        sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp");
        sb.setToId(evection.getLeaderId());
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      if(moblieSmsRemind!=null){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,evection.getLeaderId(), userId, "提交出差申请，请批示:" + reason, new Date());
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
    response.sendRedirect(path+ "/core/funcs/attendance/personal/evection.jsp");
    return "";
  }
  /**
   * 更改出差状态/外出归来
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
      YHAttendEvection evection = new YHAttendEvection();
      YHAttendEvectionLogic yhael = new YHAttendEvectionLogic();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String evectionDate1 = request.getParameter("evectionDate1");
      String evectionDate2 = request.getParameter("evectionDate2");
      String smsRemind = request.getParameter("smsRemind");
      String registerIp = request.getLocalAddr();;
      String reason = request.getParameter("reason");
      reason = reason.replaceAll("\\\n","");
      reason = reason.replaceAll("\\\r","");
      String evectionDest = request.getParameter("evectionDest");
      String seqId = request.getParameter("seqId");
      if(YHUtility.isInteger(seqId)){
        evection = yhael.selectEvectionById(dbConn, seqId);
        evection.setEvectionDate1(YHUtility.parseDate(evectionDate1));
        evection.setEvectionDate2(YHUtility.parseDate(evectionDate2));
        evection.setEvectionDest(evectionDest);
        evection.setReason(reason);
        evection.setStatus("2");
        yhael.updateEvection(dbConn, evection);
        //配合陈晨出差自动补登记
        YHAttendFillLogic fillLogic = new YHAttendFillLogic();
        fillLogic.addAttendScoreAbord(dbConn, user, evectionDate1, evectionDate2, userId+"",evection.getLeaderId());
        
        //短信smsType, content, remindUrl, toId, fromId
        if(smsRemind!=null){
          YHSmsBack sb = new YHSmsBack();
          sb.setSmsType("6");
          sb.setContent("出差归来，请查看！！");
          sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp");
          sb.setToId(evection.getLeaderId());
          sb.setFromId(userId);
          YHSmsUtil.smsBack(dbConn, sb);
        }
      }
     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 展示自动补登记、不需要审核日期--cc 20101126
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String showMonth(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String data = "";
      data = this.logic.showTimeStr(dbConn, beginDate, endDate);
      request.setAttribute(YHActionKeys.RET_DATA, data);
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
   * 出差总时长
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAttendEvectionHour(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userId = String.valueOf(person.getSeqId());
      String userIdStr = request.getParameter("userIdStr");
      if(!YHUtility.isNullorEmpty(userIdStr)){
        userId = userIdStr;
      }
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      double data = this.logic.getAttendEvectionHourLogic(dbConn, year, month, userId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

}
