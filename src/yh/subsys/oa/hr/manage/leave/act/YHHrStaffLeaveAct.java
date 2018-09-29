package yh.subsys.oa.hr.manage.leave.act;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.manage.leave.data.YHHrStaffLeave;
import yh.subsys.oa.hr.manage.leave.logic.YHHrStaffLeaveLogic;

public class YHHrStaffLeaveAct {
  private YHHrStaffLeaveLogic logic = new YHHrStaffLeaveLogic();
  public static final String attachmentFolder = "hr";

  /**
   * 新建员工离职
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addStaffLeaveInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    String contexPath = request.getContextPath();
    String hrDeptId = YHUtility.null2Empty(fileForm.getParameter("hrDeptId"));
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.setNewStaffLeaveValueLogic(dbConn, fileForm, person);
      this.logic.setPersonDeptLogic(dbConn, fileForm, person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    response.sendRedirect(contexPath + "/subsys/oa/hr/manage/staffLeave/newRemind.jsp?hrDeptId=" + hrDeptId);
    return null;
  }
  
  /**
   * 新建员工离职(人事档案页面调用“离职”页面)
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addStaffLeaveInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    String contexPath = request.getContextPath();
    String hrDeptId = YHUtility.null2Empty(fileForm.getParameter("hrDeptId"));
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.addStaffLeaveInfoByIdLogic(dbConn, fileForm, person);

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    response.sendRedirect(contexPath + "/subsys/oa/hr/manage/staffLeave/newRemind.jsp?hrDeptId=" + hrDeptId);
    return null;
  }

  /**
   *员工离职 通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getStaffLeaveListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getStaffLeaveJsonLogic(dbConn, request.getParameterMap(), person);
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
   * 获取单位员工用户名称
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userIdStr = request.getParameter("userIdStr");
      String data = this.logic.getUserNameLogic(dbConn, userIdStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 删除文件--wyw
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String filePath = YHSysProps.getAttachPath() + File.separator + this.attachmentFolder + File.separator;
      this.logic.deleteFileLogic(dbConn, seqIdStr, filePath);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 获取详情
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getLeaveDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHHrStaffLeave leave = (YHHrStaffLeave) this.logic.getLeaveDetailLogic(dbConn, Integer.parseInt(seqId));
      if (leave == null) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data = YHFOM.toJson(leave);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 员工离职查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryLeaveListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      Map<Object, Object> map = new HashMap<Object, Object>();
      map.put("leavePerson", YHDBUtility.escapeLike(request.getParameter("leavePerson")));
      map.put("quitType", YHDBUtility.escapeLike(request.getParameter("quitType")));
      map.put("quitTimePlan1", YHDBUtility.escapeLike(request.getParameter("quitTimePlan1")));
      map.put("quitTimePlan2", YHDBUtility.escapeLike(request.getParameter("quitTimePlan2")));
      map.put("quitTimeFact1", YHDBUtility.escapeLike(request.getParameter("quitTimeFact1")));
      map.put("quitTimeFact2", YHDBUtility.escapeLike(request.getParameter("quitTimeFact2")));
      map.put("quitReason", YHDBUtility.escapeLike(request.getParameter("quitReason")));
      map.put("materialsCondition", YHDBUtility.escapeLike(request.getParameter("materialsCondition")));
      String data = "";
      data = this.logic.queryLeaveListJsonLogic(dbConn, request.getParameterMap(), map, person);
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
   * 编辑员工离职
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateLeaveInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    String contexPath = request.getContextPath();
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      this.logic.updateLeaveInfoLogic(dbConn,fileForm,person);
      this.logic.setPersonDeptLogic(dbConn, fileForm, person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    response.sendRedirect(contexPath + "/subsys/oa/hr/manage/staffLeave/manage.jsp");
    return null;
  }
  /**
   * 浮动菜单文件删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delFloatFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    String attachId = request.getParameter("delAttachId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      boolean updateFlag = this.logic.updateFloadFile(dbConn, seqIdStr, YHUtility.null2Empty(attachId));
      int returnFlag = 0;
      if (updateFlag) {
        returnFlag = 1;
      }
      String data = "{updateFlag:\"" + returnFlag + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   *查看领用物品
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSelectGoods(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String SeqId = request.getParameter("SeqId");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getSelectGoods(dbConn, request.getParameterMap(), SeqId);
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
}
