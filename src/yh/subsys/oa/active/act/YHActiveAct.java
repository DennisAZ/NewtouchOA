package yh.subsys.oa.active.act;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.subsys.oa.active.data.YHActive;
import yh.subsys.oa.active.logic.YHActiveLogic;
import yh.core.funcs.calendar.act.YHCalendarAct;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;

public class YHActiveAct {
  
  /*
  * 新建周活动安排
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String addActive(HttpServletRequest request,
     HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
     int userId = user.getSeqId();
     Date curDate = new Date();
     String activeUser = request.getParameter("activeUser");
     String activeContent = request.getParameter("activeContent");
     String date = request.getParameter("activeTime");
     String hour = request.getParameter("hour");
     String min = request.getParameter("min");
     YHActive active = new YHActive();
     active.setActiveUser(activeUser);
     active.setActiveTimeRang("0");
     active.setOverStatus("0");
     active.setActiveContent(activeContent);
     active.setOpUserId(String.valueOf(userId));
     active.setOpDatetime(curDate);
     String activeTimeStr = date + " " + hour + ":" + min + ":00";
     Date activeTime = dateFormat.parse(activeTimeStr);
     active.setActiveTime(activeTime);
     YHActiveLogic activeLogic = new YHActiveLogic();
     activeLogic.addActive(dbConn, active);

   /*    //短信smsType, content, remindUrl, toId, fromId
       YHSmsBack sb = new YHSmsBack();
       sb.setSmsType("5");
       sb.setContent("请查看日程安排！内容："+content);
       sb.setRemindUrl("/yh/core/funcs/calendar/mynote.jsp?seqId="+maxSeqId+"&openFlag=1&openWidth=300&openHeight=250");
       sb.setToId(String.valueOf(userId));
       sb.setFromId(userId);
       YHSmsUtil.smsBack(dbConn, sb);*/
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
 /*
  * 更新周活动安排
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String updateActive(HttpServletRequest request,
     HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
     int userId = user.getSeqId();
     String seqId = request.getParameter("seqId");
     Date curDate = new Date();
     String activeUser = request.getParameter("activeUser");
     String activeContent = request.getParameter("activeContent");
     String date = request.getParameter("activeTime");
     String hour = request.getParameter("hour");
     String min = request.getParameter("min");
     YHActiveLogic activeLogic = new YHActiveLogic();
     if(seqId!=null&&!seqId.equals("")){
       YHActive active = new YHActive();
       active.setSeqId(Integer.parseInt(seqId));
       active.setActiveUser(activeUser);
       active.setActiveTimeRang("0");
       active.setOverStatus("0");
       active.setActiveContent(activeContent);
       active.setOpUserId(String.valueOf(userId));
       active.setOpDatetime(curDate);
       String activeTimeStr = date + " " + hour + ":" + min + ":00";
       Date activeTime = dateFormat.parse(activeTimeStr);
       active.setActiveTime(activeTime);
       activeLogic.updateActive(dbConn, active);
     }
    

   /*    //短信smsType, content, remindUrl, toId, fromId
       YHSmsBack sb = new YHSmsBack();
       sb.setSmsType("5");
       sb.setContent("请查看日程安排！内容："+content);
       sb.setRemindUrl("/yh/core/funcs/calendar/mynote.jsp?seqId="+maxSeqId+"&openFlag=1&openWidth=300&openHeight=250");
       sb.setToId(String.valueOf(userId));
       sb.setFromId(userId);
       YHSmsUtil.smsBack(dbConn, sb);*/
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
 /*
  * 查询周活动安排（在指定一周之内）
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String selectActiveByWeek(HttpServletRequest request,
     HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
     int userId = user.getSeqId();
     String beginDate = request.getParameter("beginDate");
     String endDate = request.getParameter("endDate");
     YHActiveLogic activeLogic = new YHActiveLogic();
     List<YHActive> activeList = activeLogic.selectActiveByWeek(dbConn, beginDate, endDate,"");
     YHPersonLogic personLogic = new YHPersonLogic();
     String data = "[";
     for (int i = 0; i < activeList.size(); i++) {
      YHActive active = activeList.get(i);
      String opUserIdName = "";
      String activeUserName = "";
      if(active.getOpUserId()!=null&&!active.getOpUserId().trim().equals("")){
        opUserIdName = personLogic.getNameBySeqIdStr(active.getOpUserId(), dbConn);
      }
      if(active.getActiveUser()!=null&&!active.getActiveUser().trim().equals("")){
        activeUserName = personLogic.getNameBySeqIdStr(active.getActiveUser(), dbConn);
      }
      data = data + YHFOM.toJson(active).toString().substring(0, YHFOM.toJson(active).toString().length()-1)+",opUserIdName:\""+opUserIdName+"\",activeUserName:\""+ activeUserName+ "\"},";
     }
     if(activeList.size()>0){
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
 /*
  * 查询周活动安排（ById）
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String selectActiveById(HttpServletRequest request,
     HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
     int userId = user.getSeqId();
     String seqId = request.getParameter("seqId");
     YHActiveLogic activeLogic = new YHActiveLogic();
     YHPersonLogic personLogic = new YHPersonLogic();
     String data = "";
     if(seqId!=null&&!seqId.equals("")){
       YHActive active = activeLogic.selectActiveById(dbConn, Integer.parseInt(seqId));
       if(active!=null){
         String opUserIdName = "";
         String activeUserName = "";
         if(active.getOpUserId()!=null&&!active.getOpUserId().trim().equals("")){
           opUserIdName = personLogic.getNameBySeqIdStr(active.getOpUserId(), dbConn);
         }
         if(active.getActiveUser()!=null&&!active.getActiveUser().trim().equals("")){
           activeUserName = personLogic.getNameBySeqIdStr(active.getActiveUser(), dbConn);
         }
         data = data + YHFOM.toJson(active).toString().substring(0, YHFOM.toJson(active).toString().length()-1) +",opUserIdName:\""+opUserIdName+"\",activeUserName:\""+ activeUserName+ "\"}";
       }
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
 /*
  * 删除周活动安排（ById）
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String delActiveById(HttpServletRequest request,
     HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn =
       (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
     int userId = user.getSeqId();
     String seqId = request.getParameter("seqId");
     YHActiveLogic activeLogic = new YHActiveLogic();
     String data = "";
     if(seqId!=null&&!seqId.equals("")){
         activeLogic.delActiveById(dbConn, Integer.parseInt(seqId));
     }
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
     //request.setAttribute(YHActionKeys.RET_DATA, data);
   }catch(Exception ex) {
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
   return "/core/inc/rtjson.jsp";
 }
 /*
  * 查询周活动安排（在今日||本周之内的）
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String selectActive(HttpServletRequest request,
     HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
     Date curDate = new Date();
     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
     YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
     int userId = user.getSeqId();
     String index = request.getParameter("index");
     String data = "[";
     List<YHActive> activeList = new ArrayList<YHActive>();
     YHActiveLogic activeLogic = new YHActiveLogic();
     if(index!=null&&!index.equals("")){
       if(index.equals("1")){
         String beginDate = dateFormat.format(curDate);
         activeList = activeLogic.selectActiveByWeek(dbConn, beginDate, beginDate,String.valueOf(userId));
         
       }
       if(index.equals("2")){
         YHCalendarAct  calendarAct = new YHCalendarAct();
         Calendar[] darr = calendarAct.getStartEnd();
         String beginDate = calendarAct.getFullTimeStr(darr[0]);
         String endDate = calendarAct.getFullTimeStr(darr[1]) ;
         activeList = activeLogic.selectActiveByWeek(dbConn, beginDate, endDate,String.valueOf(userId));
       }
     }
     YHPersonLogic personLogic = new YHPersonLogic();
     for (int i = 0; i < activeList.size(); i++) {
      YHActive active = activeList.get(i);
      String opUserIdName = "";
      String activeUserName = "";
      if(active.getOpUserId()!=null&&!active.getOpUserId().trim().equals("")){
        opUserIdName = personLogic.getNameBySeqIdStr(active.getOpUserId(), dbConn);
      }
      if(active.getActiveUser()!=null&&!active.getActiveUser().trim().equals("")){
        activeUserName = personLogic.getNameBySeqIdStr(active.getActiveUser(), dbConn);
      }
      data = data + YHFOM.toJson(active).toString().substring(0, YHFOM.toJson(active).toString().length()-1)+",opUserIdName:\""+opUserIdName+"\",activeUserName:\""+ activeUserName+ "\"},";
     }
     if(activeList.size()>0){
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
}
