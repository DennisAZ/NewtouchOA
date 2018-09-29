package yh.subsys.oa.hr.recruit.requirements.act;

import java.io.File;
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
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.notify.data.YHNotify;
import yh.core.funcs.notify.logic.YHNotifyManageLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.recruit.requirements.data.YHHrRecruitRequirements;
import yh.subsys.oa.hr.recruit.requirements.logic.YHHrRecruitRequirementsLogic;

public class YHHrRecruitRequirementsAct {
  private YHHrRecruitRequirementsLogic logic = new YHHrRecruitRequirementsLogic();
  public static final String attachmentFolder = "hr";
  
  /**
   * 新建招聘需求
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addRecruitRequirementsInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    String contexPath = request.getContextPath();
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.setNewRecruitRequirementsValueLogic(dbConn, fileForm, person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    response.sendRedirect(contexPath + "/subsys/oa/hr/recruit/requirements/newRemind.jsp");
    return null;
  }
  
  /**
   * 招聘需求  通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRecruitRequirementsListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getRecruitRequirementsJsonLogic(dbConn, request.getParameterMap(), person);
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
   * 获取部门名称
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDeptName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptIdStr = request.getParameter("deptIdStr");
      String data = this.logic.getDeptName(dbConn, deptIdStr);
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
   * 获取详情
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRecruitRequirementsDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHHrRecruitRequirements recruitRequirements = (YHHrRecruitRequirements) this.logic.getRecruitRequirementsDetailLogic(dbConn, Integer.parseInt(seqId));
      if (recruitRequirements == null) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data = YHFOM.toJson(recruitRequirements);
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
   * 编辑招聘需求
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateRecruitRequirementsInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    String contexPath = request.getContextPath();
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      this.logic.updateRecruitRequirementsInfoLogic(dbConn,fileForm,person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    response.sendRedirect(contexPath + "/subsys/oa/hr/recruit/requirements/manage.jsp");
    return null;
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
   * 招聘需求查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryRecruitRequirementsListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
   
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      Map<Object, Object> map = new HashMap<Object, Object>();
      map.put("requNo", YHDBUtility.escapeLike(request.getParameter("requNo")));
      map.put("requNum", YHDBUtility.escapeLike(request.getParameter("requNum")));
      map.put("requJob", YHDBUtility.escapeLike(request.getParameter("requJob")));
      map.put("requDeptId", YHDBUtility.escapeLike(request.getParameter("requDeptId")));
      map.put("startTime", YHDBUtility.escapeLike(request.getParameter("startTime")));
      map.put("endTime", YHDBUtility.escapeLike(request.getParameter("endTime")));
    
      String data = "";
      data = this.logic.queryRecruitRequirementsJsonLogic(dbConn, request.getParameterMap(), map, person);
     
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
   * 导出到excel
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String toExcel(HttpServletRequest request, HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    OutputStream ops = null;
    try{
      Map<Object, Object> map = new HashMap<Object, Object>();
      map.put("seqId", YHDBUtility.escapeLike(request.getParameter("seqId")));
      map.put("requNo", YHDBUtility.escapeLike(request.getParameter("requNo")));
      map.put("requNum", YHDBUtility.escapeLike(request.getParameter("requNum")));
      map.put("requJob", YHDBUtility.escapeLike(request.getParameter("requJob")));
      map.put("requDeptId", YHDBUtility.escapeLike(request.getParameter("requDeptId")));
      map.put("startTime", YHDBUtility.escapeLike(request.getParameter("startTime")));
      map.put("endTime", YHDBUtility.escapeLike(request.getParameter("endTime")));
      
     // YHHrRecruitRequirements recruitrequirements = (YHHrRecruitRequirements)YHFOM.build(request.getParameterMap());    
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      List<Map<String, String>> recruits = this.logic.toExcel(dbConn,person,map);
      String fileName = URLEncoder.encode("招聘需求.xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream(); 
      ArrayList<YHDbRecord > dbL = this.logic.convertList(recruits);
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception e){
      e.printStackTrace();
      throw e;
    }finally{
      ops.close();
    }
    return null;
  }
  /**
   * 查询导出到excel
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryToExcel(HttpServletRequest request, HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    OutputStream ops = null;
    try{
      Map<Object, Object> map = new HashMap<Object, Object>();
      //map.put("seqId", YHDBUtility.escapeLike(request.getParameter("seqId")));
      map.put("requNo", YHDBUtility.escapeLike(request.getParameter("requNo")));
      map.put("requNum", YHDBUtility.escapeLike(request.getParameter("requNum")));
      map.put("requJob", YHDBUtility.escapeLike(request.getParameter("requJob")));
      map.put("requDeptId", YHDBUtility.escapeLike(request.getParameter("requDeptId")));
      map.put("startTime", YHDBUtility.escapeLike(request.getParameter("startTime")));
      map.put("endTime", YHDBUtility.escapeLike(request.getParameter("endTime")));
      
     // YHHrRecruitRequirements recruitrequirements = (YHHrRecruitRequirements)YHFOM.build(request.getParameterMap());    
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      List<Map<String, String>> recruits = this.logic.toExcel(dbConn,person,map);
      String fileName = URLEncoder.encode("招聘需求.xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream(); 
      ArrayList<YHDbRecord > dbL = this.logic.convertList(recruits);
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception e){
      e.printStackTrace();
      throw e;
    }finally{
      ops.close();
    }
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
}
