package yh.core.funcs.calendar.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.calendar.data.YHTask;
import yh.core.funcs.calendar.logic.YHTaskLogic;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHTaskAct {
  /**
   * 新建任务
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addTask(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
      int userId = user.getSeqId();
      YHTask task = new YHTask();
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String remindTime = request.getParameter("remindTime");
      String finishTime = request.getParameter("finishTime");
      String content = request.getParameter("content");
      String taskNo = request.getParameter("taskNo");
      String totalTime = request.getParameter("totalTime");
      String useTime = request.getParameter("useTime");
      String rate = request.getParameter("rate");
      String color = request.getParameter("color");
      String important = request.getParameter("important");
      String taskType = request.getParameter("taskType");
      String taskStatus = request.getParameter("taskStatus");
      String subject = request.getParameter("subject");
      String smsRemind = request.getParameter("smsRemind");
      content = content.replaceAll("\\\n", "");
      content = content.replaceAll("\\\r", "");
      if (rate.equals("")) {
        rate = "0";
      }
      if (!beginDate.equals("")) {
        task.setBeginDate(dateFormat1.parse(beginDate));
      }
      if (!endDate.equals("")) {
        task.setEndDate(dateFormat1.parse(endDate));
      }
      if (!finishTime.equals("")) {
        task.setFinishTime(dateFormat.parse(finishTime));
      }
      task.setUserId(String.valueOf(userId));
      task.setColor(color);
      task.setImportant(important);
      task.setRate(rate);
      task.setContent(content);
      task.setTaskStatus(taskStatus);
      task.setTaskType(taskType);
      task.setSubject(subject);
      if (taskNo.equals("")) {
        taskNo = "0";
      }
      task.setTaskNo(Integer.parseInt(taskNo));
      if (totalTime.equals("")) {
        totalTime = "0";
      }
      task.setTotalTime(totalTime);
      if (useTime.equals("")) {
        useTime = "0";
      }
      task.setUseTime(useTime);
      YHTaskLogic ttl = new YHTaskLogic();
      int maxSeqId = ttl.addTask(dbConn, task);
      // 短信smsType, content, remindUrl, toId, fromId
      if (smsRemind != null && !YHUtility.isNullorEmpty(remindTime)) {
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("5");
        sb.setSendDate(YHUtility.parseDate(remindTime));
        sb.setContent("请查看我的任务！标题：" + subject);
        sb.setRemindUrl("/core/funcs/calendar/tasknote.jsp?seqId=" + maxSeqId
            + "&openFlag=1&openWidth=300&openHeight=250");
        sb.setToId(String.valueOf(userId));
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      if (moblieSmsRemind != null && !YHUtility.isNullorEmpty(remindTime)) {
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn, String.valueOf(userId), userId,
            "我的任务：" + content, new Date());
      }
      // request.setAttribute(YHActionKeys.RET_DATA, "data");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    String path = request.getContextPath();
    response.sendRedirect(path + "/core/funcs/calendar/task.jsp");
    // return "/core/funcs/calendar/task.jsp";
    return null;

  }

  /*
   * 修改任务
   */
  public String updateTask(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
      int userId = user.getSeqId();
      YHTask task = new YHTask();
      String seqId = request.getParameter("seqId");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String remindTime = request.getParameter("remindTime");
      String finishTime = request.getParameter("finishTime");
      String content = request.getParameter("content");
      String taskNo = request.getParameter("taskNo");
      String totalTime = request.getParameter("totalTime");
      String useTime = request.getParameter("useTime");
      String rate = request.getParameter("rate");
      String color = request.getParameter("color");
      String important = request.getParameter("important");
      String taskType = request.getParameter("taskType");
      String taskStatus = request.getParameter("taskStatus");
      String subject = request.getParameter("subject");
      String smsRemind = request.getParameter("smsRemind");
      content = content.replaceAll("\\\n", "");
      content = content.replaceAll("\\\r", "");
      if (rate.equals("")) {
        rate = "0";
      }
      if (!beginDate.equals("")) {
        task.setBeginDate(dateFormat1.parse(beginDate));
      }
      if (!endDate.equals("")) {
        task.setEndDate(dateFormat1.parse(endDate));
      }
      if (!finishTime.equals("")) {
        task.setFinishTime(dateFormat.parse(finishTime));
      }
      task.setSeqId(Integer.parseInt(seqId));
      task.setUserId(String.valueOf(userId));
      task.setColor(color);
      task.setImportant(important);
      task.setRate(rate);
      task.setContent(content);
      task.setTaskStatus(taskStatus);
      task.setTaskType(taskType);
      task.setSubject(subject);
      if (taskNo.equals("")) {
        taskNo = "0";
      }
      task.setTaskNo(Integer.parseInt(taskNo));
      if (totalTime.equals("")) {
        totalTime = "0";
      }
      task.setTotalTime(totalTime);
      if (useTime.equals("")) {
        useTime = "0";
      }
      task.setUseTime(useTime);
      YHTaskLogic ttl = new YHTaskLogic();
      ttl.updateTask(dbConn, task);
      // 短信smsType, content, remindUrl, toId, fromId
      if (smsRemind != null && !YHUtility.isNullorEmpty(remindTime)) {
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("5");
        sb.setSendDate(YHUtility.parseDate(remindTime));
        sb.setContent("请查看我的任务！标题：" + subject);
        sb.setRemindUrl("/core/funcs/calendar/tasknote.jsp?seqId=" + seqId
            + "&openFlag=1&openWidth=300&openHeight=250");
        sb.setToId(String.valueOf(userId));
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      if (moblieSmsRemind != null && !YHUtility.isNullorEmpty(remindTime)) {
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn, String.valueOf(userId), userId,
            "我的任务：" + content, new Date());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      // request.setAttribute(YHActionKeys.RET_DATA, "data");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 修改任务 日程安排查询
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateTaskByUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
      int userSeqId = user.getSeqId();
      YHTask task = new YHTask();
      String seqId = request.getParameter("seqId");
      String userId = request.getParameter("userId");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String remindTime = request.getParameter("remindTime");
      String finishTime = request.getParameter("finishTime");
      String content = request.getParameter("content");
      String taskNo = request.getParameter("taskNo");
      String totalTime = request.getParameter("totalTime");
      String useTime = request.getParameter("useTime");
      String rate = request.getParameter("rate");
      String color = request.getParameter("color");
      String important = request.getParameter("important");
      String taskType = request.getParameter("taskType");
      String taskStatus = request.getParameter("taskStatus");
      String subject = request.getParameter("subject");
      String smsRemind = request.getParameter("smsRemind");
      content = content.replaceAll("\\\n", "");
      content = content.replaceAll("\\\r", "");
      if (seqId != null && !seqId.equals("")) {
        if (rate.equals("")) {
          rate = "0";
        }
        if (!beginDate.equals("")) {
          task.setBeginDate(dateFormat1.parse(beginDate));
        }
        if (!endDate.equals("")) {
          task.setEndDate(dateFormat1.parse(endDate));
        }
        if (!finishTime.equals("")) {
          task.setFinishTime(dateFormat.parse(finishTime));
        }
        YHTaskLogic ttl = new YHTaskLogic();
        task.setSeqId(Integer.parseInt(seqId));
        task.setUserId(userId);
        task.setColor(color);
        task.setImportant(important);
        task.setRate(rate);
        task.setContent(content);
        task.setTaskStatus(taskStatus);
        task.setTaskType(taskType);
        task.setSubject(subject);
        task.setManagerId(String.valueOf(userSeqId));
        if (taskNo.equals("")) {
          taskNo = "0";
        }
        task.setTaskNo(Integer.parseInt(taskNo));
        if (totalTime.equals("")) {
          totalTime = "0";
        }
        task.setTotalTime(totalTime);
        if (useTime.equals("")) {
          useTime = "0";
        }
        task.setUseTime(useTime);

        ttl.updateTask(dbConn, task);
        if (smsRemind != null && !YHUtility.isNullorEmpty(remindTime)) {
          // 短信smsType, content, remindUrl, toId, fromId
          YHSmsBack sb = new YHSmsBack();
          sb.setSmsType("5");
          sb.setSendDate(YHUtility.parseDate(remindTime));
          sb.setContent("请查看" + user.getUserName() + "安排的任务！标题：" + subject);
          sb.setRemindUrl("/core/funcs/calendar/tasknote.jsp?seqId=" + seqId
              + "&openFlag=1&openWidth=300&openHeight=250");
          sb.setToId(userId);
          sb.setFromId(userSeqId);
          YHSmsUtil.smsBack(dbConn, sb);
        }
        String moblieSmsRemind = request.getParameter("moblieSmsRemind");
        if (moblieSmsRemind != null) {
          YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
          sms2Logic.remindByMobileSms(dbConn, userId, userSeqId, "任务安排："
              + content, new Date());
        }
      }

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      // request.setAttribute(YHActionKeys.RET_DATA, "data");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /*
   * 查询所有任务
   */
  public String selectTask(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHTaskLogic ttl = new YHTaskLogic();
      List<Map<String, String>> taskList = ttl.selectTask(dbConn, userId);
      StringBuffer buffer = new StringBuffer("[");
      for (Map<String, String> equipmentsMap : taskList) {
        buffer.append("{");
        Set<String> keySet = equipmentsMap.keySet();
        for (String mapStr : keySet) {
          // System.out.println(mapStr + ":>>>>>>>>>>>>" +
          // equipmentsMap.get(mapStr));
          String name = equipmentsMap.get(mapStr);
          if (name != null) {
            name = name.replace("\\", "\\\\").replace("\"", "\\\"").replace(
                "\r", "").replace("\n", "");
          }
          buffer.append(mapStr + ":\"" + (name == null ? "" : name) + "\",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append("},");
      }
      buffer.deleteCharAt(buffer.length() - 1);
      if (taskList.size() > 0) {
        buffer.append("]");
      } else {
        buffer.append("[]");
      }
      String data = buffer.toString();
      // System.out.println(data);

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
   * 任务分页查询
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectTaskByPage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHTaskLogic dl = new YHTaskLogic();
      String data = dl.toSearchData(dbConn, request.getParameterMap(), userId);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }

  /*
   * 查询任务ById
   */
  public String selectTaskById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHTaskLogic ttl = new YHTaskLogic();
      String data = "";
      YHPersonLogic personLogic = new YHPersonLogic();
      if (seqId != null && !seqId.equals("")) {
        Map<String, String> task = ttl.selectTaskById(dbConn, seqId);
        if (task != null) {
          StringBuffer buffer = new StringBuffer("{");
          Set<String> keySet = task.keySet();
          String managerName = "";
          for (String mapStr : keySet) {
            // System.out.println(mapStr + ":>>>>>>>>>>>>" + task.get(mapStr));
            String name = task.get(mapStr);
            if (name != null) {
              name = name.replace("\\", "\\\\").replace("\"", "\\\"").replace(
                  "\r", "").replace("\n", "");
            }

            if (mapStr != null && mapStr.equals("managerId")) {
              if (name != null && !name.trim().equals("")) {
                managerName = personLogic.getNameBySeqIdStr(name, dbConn);
                if (managerName != null && !managerName.equals("")) {
                  managerName = YHUtility.encodeSpecial(managerName);
                }
              }
            }
            buffer.append(mapStr + ":\"" + (name == null ? "" : name) + "\",");
          }
          buffer.append("managerName:\"" + managerName + "\",");
          buffer.deleteCharAt(buffer.length() - 1);
          buffer.append("}");
          data = buffer.toString();
        }

      }
      if (data.equals("")) {
        data = "{}";
      }
      // System.out.println(data);
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

  /*
   * 按条件查询所有任务
   */
  public String selectTaskByTerm(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String minDate = request.getParameter("minDate");
      String maxDate = request.getParameter("maxDate");
      String taskType = request.getParameter("taskType");
      String taskStatus = request.getParameter("taskStatus");
      String content = request.getParameter("content");
      String important = request.getParameter("important");
      YHTaskLogic ttl = new YHTaskLogic();
      List<Map<String, String>> taskList = ttl.selectTaskByTerm(dbConn, userId,
          minDate, maxDate, taskType, taskStatus, content, important);
      request.setAttribute("taskList", taskList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      // request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/calendar/querytask.jsp";
  }

  /*
   * 删除任务ById
   */
  public String deleteTaskById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      // System.out.println(seqId);
      YHTaskLogic ttl = new YHTaskLogic();
      ttl.deleteTaskById(dbConn, seqId);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   *           删除多条任务
   */
  public String deleteTask(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIds = request.getParameter("seqIds");
      YHTaskLogic ttl = new YHTaskLogic();
      ttl.deleteTask(dbConn, seqIds);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/calendar/task.jsp";
  }
}
