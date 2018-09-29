package yh.core.funcs.attendance.personal.act;

import java.net.InetAddress;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.attendance.personal.data.YHAttendOut;
import yh.core.funcs.attendance.personal.logic.YHAttendOutLogic;
import yh.core.funcs.calendar.info.logic.YHInfoLogic;
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

public class YHAttendOutAct {
  /**
   * 
   * 添加外出记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addOut(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHAttendOut out = new YHAttendOut();
      YHAttendOutLogic yhaol = new YHAttendOutLogic();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      //系统 管理员指定的人
      String userSeqId = request.getParameter("user");
      if(userSeqId!=null&&!userSeqId.equals("")){
        userId = Integer.parseInt(userSeqId);
      }
      String outDate = request.getParameter("outDate");
      String outTime1 = request.getParameter("outTime1");
      String submitDateStr = outDate + " " + outTime1 ; 
      String outType = request.getParameter("outType");
      String smsRemind = request.getParameter("smsRemind");
      //得到客户端的IP地址
      YHAttendDutyAct dutyAct = new YHAttendDutyAct();
      String registerIp = dutyAct.getIpAddr(request);
      InetAddress inet = InetAddress.getLocalHost();
      String localIp = inet.getHostAddress();
      if(registerIp!=null&&registerIp.equals("127.0.0.1")){
        registerIp = localIp;
      }
/*      String ip = YHSysLogLogic.getIpAddr( request);
      String ipd = request.getRemoteAddr();
      //System.out.println(ip);*/
      YHFOM fom = new YHFOM();
      out = (YHAttendOut) fom.build(request.getParameterMap());
      //System.out.println(outType);
      //outType = new String(outType.getBytes("iso-8859-1"), "utf-8"); 
      outType = outType.replaceAll("\\\n", "");
      outType = outType.replaceAll("\\\r", "");
      //System.out.println(outType);
      out.setOutType(outType);
      out.setRegisterIp(registerIp);
      out.setAllow("0");
      out.setStatus("0");
      out.setUserId(String.valueOf(userId));
      out.setCreateDate(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",dateFormat.format(new Date())));
      out.setSubmitTime(YHUtility.parseDate("yyyy-MM-dd HH:mm", submitDateStr));
      yhaol.addOut(dbConn, out);
      
      YHFlowHookUtility ut = new YHFlowHookUtility();
      int attendOutId = ut.getMax(dbConn, "select max(SEQ_ID) FROM oa_attendance_out");
      Map dataArray = new HashMap();
      dataArray.put("KEY", attendOutId + "");
      dataArray.put("FIELD", "OUT_ID");
      dataArray.put("USER_ID", out.getUserId()+"");
      YHPersonLogic p = new YHPersonLogic();
      String userName = p.getUserNameLogic(dbConn, Integer.parseInt(out.getUserId()));
      dataArray.put("USER_NAME", userName);
     // String leaderName= p.getUserNameLogic(dbConn, Integer.parseInt(out.getLeaderId()));
      dataArray.put("LEADER_ID",out.getLeaderId());
      dataArray.put("REASON", out.getReason());
      dataArray.put("OUT_TYPE", out.getOutType());
      dataArray.put("OUT_DATE",outDate );
      dataArray.put("OUT_TIME1",outDate+" "+out.getOutTime1());
      dataArray.put("OUT_TIME2",outDate+" "+out.getOutTime2());
      String url = ut.runHook(dbConn, user, dataArray, "OA_ATTENDANCE_OUT");
      if (!"".equals(url)) {
        String path = request.getContextPath();
        response.sendRedirect(path+ url);
        return null;
      }
      
      
      //短信smsType, content, remindUrl, toId, fromId
      if(smsRemind!=null){
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("提交外出申请，请批示！");
        sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp");
        sb.setToId(out.getLeaderId());
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      if(moblieSmsRemind!=null){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,out.getLeaderId(), userId, "提交外出申请，请批示:" + outType, new Date());
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
    response.sendRedirect(path+ "/core/funcs/attendance/personal/out.jsp");
    return "";
  }
  /**
   * 
   * 查询所有外出记录根据自己的ID
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectOut(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendOut out = new YHAttendOut();
      String data = "[";
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      Map map = new HashMap();
      map.put("USER_ID", userId);
      map.put("STATUS", "0");
      String[] str = {"USER_ID='" + userId + "'" , "STATUS = '0' order by SUBMIT_TIME" };
      YHAttendOutLogic yhaol = new YHAttendOutLogic();
      YHFlowHookUtility fu = new YHFlowHookUtility();
      YHFlowRunUtility ru = new YHFlowRunUtility();
      List<YHAttendOut> outList = yhaol.selectOut(dbConn, str);
      for (int i = 0; i < outList.size(); i++) {
        YHPersonLogic tpl = new YHPersonLogic();
        out = outList.get(i);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(out.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        
        int runId = fu.isRunHook(dbConn, "OUT_ID", out.getSeqId() + "");
        int flowId = 0;
        if (runId != 0) {
          flowId = ru.getFlowId(dbConn, runId);
        }
        
        
        
        data = data + YHFOM.toJson(outList.get(i)).toString().substring(0, YHFOM.toJson(outList.get(i)).toString().length()-1 ) + ",isHookRun:\""+runId+"\",flowId:\""+flowId+"\",leaderName:\"" + leaderName + "\"},";
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
 
  /**
   * 
   * 查询一条记录根据自己的ID and ById
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectOutById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHAttendOut out = new YHAttendOut();
      String data = "";
      YHAttendOutLogic yhaol = new YHAttendOutLogic();
      out = yhaol.selectOutById(dbConn, seqId);
      String userName = YHInfoLogic.getUserName(out.getUserId(), dbConn);
      if(userName!=null&&!userName.equals("")){
        userName = userName.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
      }
      data = data + YHFOM.toJson(out).toString().substring(0, YHFOM.toJson(out).toString().length()-1)+",userName:\"" + userName + "\"}" ;
 
      if(data.equals("")){
        data = "{}";
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
   * 删除一条记录ById
   */
  public String deleteOutById(HttpServletRequest request,
  HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
     // System.out.println(seqId);
      YHAttendOut out = new YHAttendOut();
      YHAttendOutLogic yhaol = new YHAttendOutLogic();
      yhaol.deleteOutById(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功！");
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
      int userId = user.getSeqId();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String seqId = request.getParameter("seqId");
      //System.out.println(seqId);
      String outDate = request.getParameter("outDate");
      String outTime1 = request.getParameter("outTime1");
      String outTime2 = request.getParameter("outTime2");
      String outType = request.getParameter("outType");
      String smsRemind = request.getParameter("smsRemind");
      String type = request.getParameter("type");
      outType = outType.replaceAll("\\\n", "");
      outType = outType.replaceAll("\\\r", "");
      String submitDateStr = outDate + " " + outTime1 + ":00"; 
      //System.out.println(submitDateStr);
      YHAttendOut out = new YHAttendOut();
      YHAttendOutLogic yhaol = new YHAttendOutLogic();
      if(seqId!=null&&!seqId.equals("")){
        out = yhaol.selectOutById(dbConn, seqId);
        out.setOutType(outType);
        out.setOutTime1(outTime1);
        out.setOutTime2(outTime2);
        out.setSubmitTime(YHUtility.parseDate(submitDateStr));
        out.setStatus("1");
        yhaol.updateOut(dbConn, out);
       
        //短信smsType, content, remindUrl, toId, fromId
        if(smsRemind!=null){
          YHSmsBack sb = new YHSmsBack();
          
          if(type!=null&&type.equals("1")){//审批人点击外出归来保存后
            
          }else{
            sb.setSmsType("6");
            sb.setContent("外出归来，请查看！！");
            //sb.setRemindUrl("/core/funcs/attendance/manage/manage.jsp");
            sb.setRemindUrl("/core/funcs/attendance/personal/readerEditOut.jsp?seqId="+seqId+"&openFlag=1&openWidth=600&openHeight=300");
            sb.setToId(out.getLeaderId());
            sb.setFromId(userId);
            YHSmsUtil.smsBack(dbConn, sb);
          }

        }
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
  /**
   * 
   * 更新外出记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateOut(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHAttendOut out = new YHAttendOut();
      YHAttendOutLogic yhaol = new YHAttendOutLogic();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String outDate = request.getParameter("outDate");
      String outTime1 = request.getParameter("outTime1");
      String submitDateStr = outDate + " " + outTime1+":00" ; 
      String outType = request.getParameter("outType");
      String smsRemind = request.getParameter("smsRemind");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      //System.out.println(submitDateStr);
      //得到客户端的IP地址
      YHAttendDutyAct dutyAct = new YHAttendDutyAct();
      String registerIp = dutyAct.getIpAddr(request);
      InetAddress inet = InetAddress.getLocalHost();
      String localIp = inet.getHostAddress();
      if(registerIp!=null&&registerIp.equals("127.0.0.1")){
        registerIp = localIp;
      }
      YHFOM fom = new YHFOM();
      out = (YHAttendOut) fom.build(request.getParameterMap());
     // outType = new String(outType.getBytes("iso-8859-1"), "utf-8"); 
      outType = outType.replaceAll("\\\n", "");
      outType = outType.replaceAll("\\\r", "");
      out.setOutType(outType);
      out.setRegisterIp(registerIp);
      out.setAllow("0");
      out.setStatus("0");
      out.setUserId(String.valueOf(userId));
      out.setReason("");
      out.setCreateDate(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",dateFormat.format(new Date())));
      out.setSubmitTime(YHUtility.parseDate(submitDateStr));
      yhaol.updateOut(dbConn, out);
      //短信smsType, content, remindUrl, toId, fromId
      if(smsRemind!=null){
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("提交外出申请，请批示！");
        sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp");
        sb.setToId(out.getLeaderId());
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      if(moblieSmsRemind!=null){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,out.getLeaderId(), userId, "提交外出申请，请批示:" + outType, new Date());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    String path = request.getContextPath();
    response.sendRedirect(path+ "/core/funcs/attendance/personal/out.jsp");
    return "";
  }
  /**
   * 
   * 外出历史记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectHistoryOut(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendOut out = new YHAttendOut();
      YHAttendOutLogic yhaol = new YHAttendOutLogic();
      Date date = new Date();
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      String whereStr = "";
      String ymd = year + "-" + month + "-" + "01";
      if(!YHUtility.isNullorEmpty(year) || !YHUtility.isNullorEmpty(month)){
        whereStr += " and " + YHDBUtility.getMonthFilter("SUBMIT_TIME", YHUtility.parseDate(ymd));
      }
      Map map = new HashMap();
      YHFOM fom = new YHFOM();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      map.put("USER_ID", userId);
      map.put("ALLOW", "1");
      map.put("status", "1");
      String[] str = {"USER_ID='"+userId + "'and ALLOW='1' " + whereStr + " and STATUS = '1' order by SUBMIT_TIME desc"};
      String data = "[";
      List<YHAttendOut> outList = yhaol.selectHistoryOut(dbConn, str);
      YHFlowHookUtility fu = new YHFlowHookUtility();
      YHFlowRunUtility ru = new YHFlowRunUtility();
      for (int i = 0; i < outList.size(); i++) {
        YHPersonLogic tpl = new YHPersonLogic();
        out = outList.get(i);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(out.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        int runId = fu.isRunHook(dbConn, "OUT_ID", out.getSeqId() + "");
        int flowId = 0;
        if (runId != 0) {
          flowId = ru.getFlowId(dbConn, runId);
        }
        data = data + YHFOM.toJson(outList.get(i)).toString().substring(0, YHFOM.toJson(outList.get(i)).toString().length()-1 ) + ",isHookRun:\""+runId+"\",flowId:\""+flowId+"\",leaderName:\"" + leaderName + "\"},";
      }
      if(outList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String selectOutToDisk(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      YHAttendOut out = new YHAttendOut();
      YHAttendOutLogic yhaol = new YHAttendOutLogic();
      Map map = new HashMap();
      YHFOM fom = new YHFOM();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      map.put("USER_ID", userId);
      map.put("ALLOW", "1");
      map.put("status", "1");
      String TEMP1 = YHDBUtility.getDateFilter("SUBMIT_TIME", format.format(new Date()), ">=");
      String TEMP2 = YHDBUtility.getDateFilter("SUBMIT_TIME", format.format(new Date()) + " 23:59:59", "<=");
      String[] str = {TEMP1,TEMP2,"ALLOW='1'","STATUS='0' order by SUBMIT_TIME desc"};
      String data = "[";
      List<YHAttendOut> outList = yhaol.selectOut(dbConn, str);
      for (int i = 0; i < outList.size(); i++) {
        YHPersonLogic tpl = new YHPersonLogic();
        out = outList.get(i);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(out.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        String userName = tpl.getNameBySeqIdStr(out.getUserId(), dbConn);
        if(userName!=null&&!userName.equals("")){
          userName = YHUtility.encodeSpecial(userName);
        }
        data = data + YHFOM.toJson(outList.get(i)).toString().substring(0, YHFOM.toJson(outList.get(i)).toString().length()-1 ) + ",userName:\""+userName+"\",leaderName:\"" + leaderName + "\"},";
      }
      if(outList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getAttendOutCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
      YHAttendOutLogic adl = new YHAttendOutLogic();
      int data = adl.getAttendOutCountLogic(dbConn, year, month, userId);
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
