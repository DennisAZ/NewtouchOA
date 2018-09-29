package yh.subsys.oa.hr.manage.titleEvaluation.act;

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
import yh.subsys.oa.hr.manage.titleEvaluation.data.YHHrStaffTitleEvaluation;
import yh.subsys.oa.hr.manage.titleEvaluation.logic.YHHrStaffTitleEvaluationLogic;

public class YHHrStaffTitleEvaluationAct {
  private YHHrStaffTitleEvaluationLogic logic = new YHHrStaffTitleEvaluationLogic();
  public static final String attachmentFolder = "hr";

  /**
   * 新建职称评定
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addStaffTitleEvaluationInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    String contexPath = request.getContextPath();
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.setNewStaffTitleEvaluationValueLogic(dbConn, fileForm, person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    response.sendRedirect(contexPath + "/subsys/oa/hr/manage/staffTitleEvaluation/newRemind.jsp");
    return null;
  }

  /**
   *职称评定 通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getStaffTitleEvaluationListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getStaffTitleEvaluationJsonLogic(dbConn, request.getParameterMap(), person);
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
  public String getTitleEvaluationDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHHrStaffTitleEvaluation titleEvaluation = (YHHrStaffTitleEvaluation) this.logic.getTitleEvaluationDetailLogic(dbConn, Integer.parseInt(seqId));
      if (titleEvaluation == null) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data = YHFOM.toJson(titleEvaluation);
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
   * 职称评定查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryTitleEvaluationListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      Map<Object, Object> map = new HashMap<Object, Object>();
      map.put("byEvaluStaffs", YHDBUtility.escapeLike(request.getParameter("byEvaluStaffs")));
      map.put("approvePerson", YHDBUtility.escapeLike(request.getParameter("approvePerson")));
      map.put("postName", YHDBUtility.escapeLike(request.getParameter("postName")));
      map.put("getMethod", YHDBUtility.escapeLike(request.getParameter("getMethod")));
      map.put("reportTime1", YHDBUtility.escapeLike(request.getParameter("reportTime1")));
      map.put("reportTime2", YHDBUtility.escapeLike(request.getParameter("reportTime2")));
      map.put("receiveTime1", YHDBUtility.escapeLike(request.getParameter("receiveTime1")));
      map.put("receiveTime2", YHDBUtility.escapeLike(request.getParameter("receiveTime2")));
      map.put("employPost", YHDBUtility.escapeLike(request.getParameter("employPost")));
      map.put("employCompany", YHDBUtility.escapeLike(request.getParameter("employCompany")));
      map.put("remark", YHDBUtility.escapeLike(request.getParameter("remark")));
      String data = "";
      data = this.logic.queryTitleEvaluationListJsonLogic(dbConn, request.getParameterMap(), map, person);
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
   * 编辑职称评定
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateTitleEvaluationInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    String contexPath = request.getContextPath();
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      this.logic.updateTitleEvaluationInfoLogic(dbConn,fileForm,person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    response.sendRedirect(contexPath + "/subsys/oa/hr/manage/staffTitleEvaluation/manage.jsp");
    return null;
  }
  /**
   * 浮动菜单文件删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
//  public String delFloatFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
//    String seqIdStr = request.getParameter("seqId");
//    String attachId = request.getParameter("delAttachId");
//    Connection dbConn = null;
//    try {
//      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
//      dbConn = requestDbConn.getSysDbConn();
//      boolean updateFlag = this.logic.updateFloadFile(dbConn, seqIdStr, YHUtility.null2Empty(attachId));
//      int returnFlag = 0;
//      if (updateFlag) {
//        returnFlag = 1;
//      }
//      String data = "{updateFlag:\"" + returnFlag + "\"}";
//      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
//      request.setAttribute(YHActionKeys.RET_DATA, data);
//    } catch (Exception e) {
//      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
//      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
//      throw e;
//    }
//    return "/core/inc/rtjson.jsp";
//  }
  
  public String getDeptId(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String titleEvaluationPerson = request.getParameter("titleEvaluationPerson");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptId = this.logic.getDeptId(dbConn , titleEvaluationPerson);
      String data = "{deptId:\"" + deptId + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
}
