package yh.subsys.oa.profsys.act;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.data.YHProjectCalendar;
import yh.subsys.oa.profsys.logic.YHProjectCalendarLogic;
import yh.subsys.oa.profsys.logic.YHProjectLogic;


public class YHProjectCalendarAct {
  /**
   * 新建项目
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addProjectCalendar(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHProjectCalendar calendar = (YHProjectCalendar)YHFOM.build(request.getParameterMap());
      calendar.setOverStatus("0");
      //文件柜中上传附件
      String attachmentName = request.getParameter("attachmentName");
      String attachmentId = request.getParameter("attachmentId");
      YHSelAttachUtil sel = new YHSelAttachUtil(request,"profsys");
      String attachNewId = sel.getAttachIdToString(",");
      String attachNewName = sel.getAttachNameToString("*");
      if(!"".equals(attachNewId) && !"".equals(attachmentId) &&  !attachmentId.trim().endsWith(",")){
        attachmentId += ",";
      }
      attachmentId += attachNewId;
      if(!"".equals(attachNewName) && !"".equals(attachmentName)  && !attachmentName.trim().endsWith("*")){
        attachmentName += "*";
      }
      attachmentName += attachNewName;
      calendar.setAttachmentId(attachmentId);
      calendar.setAttachmentName(attachmentName);
      String seqId = YHProjectCalendarLogic.addProjectCalendar(dbConn,calendar);
      String data = "{seqId:" + seqId + "}";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 修改项目日程
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateProjectCalendar(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHProjectCalendar calendar = (YHProjectCalendar)YHFOM.build(request.getParameterMap());
      //文件柜中上传附件
      String attachmentName = request.getParameter("attachmentName");
      String attachmentId = request.getParameter("attachmentId");
      YHSelAttachUtil sel = new YHSelAttachUtil(request,"profsys");
      String attachNewId = sel.getAttachIdToString(",");
      String attachNewName = sel.getAttachNameToString("*");
      if(!"".equals(attachNewId) && !"".equals(attachmentId) &&  !attachmentId.trim().endsWith(",")){
        attachmentId += ",";
      }
      attachmentId += attachNewId;
      if(!"".equals(attachNewName) && !"".equals(attachmentName)  && !attachmentName.trim().endsWith("*")){
        attachmentName += "*";
      }
      attachmentName += attachNewName;
      calendar.setAttachmentId(attachmentId);
      calendar.setAttachmentName(attachmentName);
      YHProjectCalendarLogic.updateProjectCalendar(dbConn,calendar);
      String seqId = calendar.getSeqId() + "";
      String data = "{seqId:" + seqId + "}";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 按月显示
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectCalendarByMonth(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Date dateCur = new Date();
      long dateTime = dateCur.getTime();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      SimpleDateFormat dateFormatday = new SimpleDateFormat("yyyy-MM-dd");
      String dateCurStr = dateFormatday.format(dateCur);
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String projId = request.getParameter("projId");
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      String data = "[";
      if(!YHUtility.isNullorEmpty(projId)){
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, Integer.parseInt(year)); // year 为 int
        time.set(Calendar.MONTH, Integer.parseInt(month) - 1);// 注意,Calendar对象默认一月为0
        int maxDay = time.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
        if (String.valueOf(month).length() == 1) {
          month = "0" + month;
        }
        String dateStr1 = year + "-" + month + "-01 00:00:00";
        String dateStr2 = year + "-" + month + "-" + maxDay + " 23:59:59";
        // System.out.println(dateStr1);

        // System.out.println(calTime2+status1);
        YHProjectCalendarLogic tcl = new YHProjectCalendarLogic();
        String[] str = {"PROJ_ID = " + projId ,
            "((" + YHDBUtility.getDateFilter("START_TIME", dateStr1, ">=") + " and " + YHDBUtility.getDateFilter("START_TIME", dateStr2, "<=") + ")"
            + " or (" + YHDBUtility.getDateFilter("END_TIME", dateStr1, ">=") + " and " + YHDBUtility.getDateFilter("END_TIME", dateStr2, "<=") + ")"
            + " or (" + YHDBUtility.getDateFilter("START_TIME", dateStr1, "<=") + " and " + YHDBUtility.getDateFilter("END_TIME", dateStr2, ">=") + ")) order by START_TIME" };
        List<YHProjectCalendar> calendarList = tcl.selectProjCalendar(dbConn, str);

        for (int i = 0; i < calendarList.size(); i++) {
          YHProjectCalendar calendar = new YHProjectCalendar();
          calendar = calendarList.get(i);
          long begin = 0;
          long end = 0;
          int status = 0;// 进行中 判断判断状态

          // System.out.println(calendar.getCalTime());
          begin = calendar.getStartTime().getTime();
          end = calendar.getEndTime().getTime();
          if (dateTime < begin) {
            status = 1;// 未开始

          }
          if (dateTime > end) {
            status = 2;// 超时
          }
          calendar = calendarList.get(i);
          // 判断是否跨天,并且判断是哪种跨天

          int dayStatus = 0;// 没跨天

          // System.out.println(calendar.getSeqId());
          dateStr1 = dateStr1.substring(0, 10);
          dateStr2 = dateStr2.substring(0, 10);
          if (!dateFormatday.format(calendar.getStartTime()).equals(
              dateFormatday.format(calendar.getEndTime()))) {
            // System.out.println(dateStr1.compareTo(dateFormatday.format(calendar.getCalTime()))>0);
            if (dateStr1.compareTo(dateFormatday.format(calendar.getStartTime())) > 0
                && dateStr2
                    .compareTo(dateFormatday.format(calendar.getEndTime())) >= 0) {
              dayStatus = 1;// 过期跨月
            } else if (dateStr1.compareTo(dateFormatday.format(calendar
                .getStartTime())) <= 0
                && dateStr2
                    .compareTo(dateFormatday.format(calendar.getEndTime())) < 0) {
              dayStatus = 2;// 未过跨月
            } else if (dateStr1.compareTo(dateFormatday.format(calendar
                .getStartTime())) > 0
                && dateStr2
                    .compareTo(dateFormatday.format(calendar.getEndTime())) < 0) {
              dayStatus = 3;// 跨月
            } else {
              dayStatus = 4;// 本月跨天
            }
          }
       
          data = data
              + YHFOM.toJson(calendar).substring(0,
                  YHFOM.toJson(calendar).length() - 1) + ",dayStatus:" + dayStatus + ",status:" + status
              + "},";
        }
        if (calendarList.size() > 0) {
          data = data.substring(0, data.length() - 1);
        }
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 项目日程信息byId
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectCalendarById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String data = "";
      if (YHUtility.isInteger(seqId)) {
        Map<String,String> calendar = YHProjectCalendarLogic.getCalendarInfoById(dbConn,seqId);
        if(calendar != null){
          data = getJson(calendar); 
        }
      }
      if(data.equals("")){
        data = "{}";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 删除项目
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteCalendarById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      if (YHUtility.isInteger(seqId)) {
        YHProjectCalendarLogic.delProjCalendar(dbConn,Integer.parseInt(seqId));
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 更改项目日程的状态（OVER_STATUS）
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateStatusById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String status = request.getParameter("status");
      if (YHUtility.isInteger(seqId)) {
        YHProjectCalendarLogic.updateStatus(dbConn,status,seqId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 单文件附件上传

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String uploadFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    Map<String, String> attr = null;
    String attrId = (fileForm.getParameter("attachmentId")== null )? "":fileForm.getParameter("attachmentId");
    String attrName = (fileForm.getParameter("attachmentName")== null )? "":fileForm.getParameter("attachmentName");
    String data = "";
    try{
      YHProjectCalendarLogic projectLogic = new YHProjectCalendarLogic();
      attr = projectLogic.fileUploadLogic(fileForm);
      Set<String> keys = attr.keySet();
      for (String key : keys){
        String value = attr.get(key);
        if(attrId != null && !"".equals(attrId)){
          if(!(attrId.trim()).endsWith(",")){
            attrId += ",";
          }
          if(!(attrName.trim()).endsWith("*")){
            attrName += "*";
          }
        }
        attrId += key + ",";
        attrName += value + "*";
      }
      data = "{attrId:\"" + attrId + "\",attrName:\"" + attrName + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);

    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
      throw e;
    }
    return "/core/inc/rtuploadfile.jsp";
  }
  
  /***
   * 更新的附件ById
     删除一个附件
   * @return
   * @throws Exception 
   */
  public String deleteCalenderFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String attachId = request.getParameter("attachId");
      String attachName = request.getParameter("attachName");
      if(seqId==null){
        seqId = "";
      }
      if(attachId==null){
        attachId = "";
      }
      if(attachName==null){
        attachName = "";
      }
      YHProjectCalendarLogic calendarLogic = new YHProjectCalendarLogic();
      YHProjectLogic projectLogic = new YHProjectLogic();
      YHProjectCalendar projectCalendar  = null ;
      String updateFlag = "0";
      if(YHUtility.isInteger(seqId)){
        projectCalendar = calendarLogic.getCalendarById(dbConn, seqId)  ;
       if(projectCalendar!=null){
         String attachmentId = projectCalendar.getAttachmentId();
         String attachmentName = projectCalendar.getAttachmentName();
         if(attachmentId==null){
           attachmentId = "";
         }
         if(attachmentName==null){
           attachmentName = "";
         }
         String[] attachmentIdArray = attachmentId.split(",");
         String[] attachmentNameArray = attachmentName.split("\\*");
         String newAttachmentId = "";
         String newAttachmentName = "";
         for (int i = 0; i < attachmentIdArray.length; i++) {
           if(!attachmentIdArray[i].equals(attachId)){
             newAttachmentId = newAttachmentId +attachmentIdArray[i] + ",";
           }
         }
         for (int i = 0; i < attachmentNameArray.length; i++) {
           if(!attachmentNameArray[i].equals(attachName)){
             newAttachmentName = newAttachmentName +attachmentNameArray[i] + "*";
           }
         }
         
         projectLogic.updateFile(dbConn,"oa_project_calendar", newAttachmentId, newAttachmentName, seqId);
         updateFlag = "1";
       }
      }
      String data = "{updateFlag:"+updateFlag+"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public static String getJson(Map<String,String> map){
    StringBuffer buffer=new StringBuffer("{"); 
    Set<String>keySet=map.keySet(); 
    for(String mapStr:keySet){ 
      String name=map.get(mapStr); 
      if(name!=null){
        name = YHUtility.encodeSpecial(name);
      }
      /* if(mapStr!=null&&mapStr.equals("seqId")){

      }*/
      buffer.append( mapStr+":\"" + (name==null? "":name) + "\","); 
    } 
    buffer.deleteCharAt(buffer.length()-1); 
    buffer.append("}"); 
    String data = buffer.toString();
    return data;
  }
  
  

}
