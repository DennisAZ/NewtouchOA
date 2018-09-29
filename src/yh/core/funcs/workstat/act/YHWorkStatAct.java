package yh.core.funcs.workstat.act;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.funcs.workstat.logic.YHWorkStatLogic;

public class YHWorkStatAct {
  public YHWorkStatLogic logic = new YHWorkStatLogic();

  /**
   * 所属部门下拉框
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectDeptToAttendance(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);

      String data = "";
      data = this.logic.getDeptTreeJson(0, dbConn, user);
      if (YHUtility.isNullorEmpty(data)) {
        data = "[]";
      }
      // System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String getUserDeptPrivAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);

      String data = ""; // this.logic.getUserDeptPrivLogic(dbConn,userId);

      data = "{deptId:'" + user.getDeptId() + "'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String getIsStaticAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);

      int userId = user.getSeqId();

      String data = this.logic.getIsStaticLogic(dbConn, userId);

      data = "{deptPriv:'" + data + "'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 获取数据
   * 
   * @param request
   * @param response
   * */
  public String getDataAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String deptId = request.getParameter("deptId");
      String startDate = request.getParameter("startDate");
      String endDate = request.getParameter("endDate");
      String deptMore = request.getParameter("deptMore");
      String minNum=request.getParameter("minNum");
      String maxNum=request.getParameter("maxNum");
      
      //注视的是不是100%确定他的方法的问题，自己重新写的方法，自己进行调用
      //shenrm 2012-12-13
      //String data = this.logic.getDataLogic(dbConn, user, deptId, startDate,
      //    endDate, deptMore,minNum,maxNum);
      
      String data = this.logic.getDataLogic1(dbConn, user, deptId, startDate,
          endDate, deptMore,minNum,maxNum);
      
      data = "{startDate:'" + startDate + "',endDate:'" + endDate
          + "',userData:[" + data + "]}";
     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 获取数据
   * 
   * @param request
   * @param response
   * */
  public String getDataToExeclAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    OutputStream ops = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String deptId = request.getParameter("deptId");
      String startDate = request.getParameter("startDate");
      String endDate = request.getParameter("endDate");
      String deptMore = request.getParameter("deptMore");

      List<Map<String, String>> dataList = this.logic.getDataToExeclLogic(
          dbConn, user, deptId, startDate, endDate, deptMore);

      String name = "工作统计报表（" + startDate + " 至   " + endDate + "）.xls";
      String fileName = URLEncoder.encode(name, "UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Content-disposition", "attachment; filename=\""
          + fileName + "\"");
      ops = response.getOutputStream();
      ArrayList<YHDbRecord> dbL = this.logic.convertList(dataList);
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      ops.close();
    }
    return null;
  }

  /**
   * 日程安排 通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCalFinishAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map<String, String> map = new HashMap<String, String>();

    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      map.put("userId", request.getParameter("userId"));
      map.put("startDate", request.getParameter("startDate"));
      map.put("endDate", request.getParameter("endDate"));
      map.put("status", request.getParameter("status"));
      String data = this.logic.getCalFinishLogic(dbConn, map);
      data = "{calendar:[" + data + "]}";
           
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);

    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());

      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 查询日志
   * */
  public String searchDiarySelf(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userIdStr = request.getParameter("userId");
      int userId = Integer.parseInt(userIdStr);
      // YHPerson person =
      // (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      // int userId = person.getSeqId();
      // YHDiaryLogic dl = new YHDiaryLogic();
      String data = this.logic.toSearchData(dbConn, request.getParameterMap(),
          userId);
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

  /**
   * 工作流主办
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getWorkFlowAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map<String, String> map = new HashMap<String, String>();
    try {
      map.put("userId", request.getParameter("userId"));
      map.put("startDate", request.getParameter("startDate"));
      map.put("endDate", request.getParameter("endDate"));
      map.put("flag", request.getParameter("flag"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String data = this.logic.getWorkFlowLogic(dbConn, request
          .getParameterMap(), map);
      PrintWriter pw = response.getWriter();
      // System.out.println(data);
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }

  /**
   * 工作流名称
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getFlowNameAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {

      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String flowId = request.getParameter("flowId");
      String data = this.logic.getFlowNameLogic(dbConn, flowId);
      data = "{flowName:'" + data + "'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 工作流会签
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getFlowSignAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map<String, String> map = new HashMap<String, String>();
    try {
      map.put("userId", request.getParameter("userId"));
      map.put("startDate", request.getParameter("startDate"));
      map.put("endDate", request.getParameter("endDate"));
      map.put("flag", request.getParameter("flag"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String data = this.logic.getFlowSignLogic(dbConn, request
          .getParameterMap(), map);
      PrintWriter pw = response.getWriter();
      // System.out.println(data);
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }

}
