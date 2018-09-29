package yh.subsys.oa.officeProduct.report.act;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
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
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.subsys.oa.officeProduct.report.logic.YHReportAnalysisLogic;

public class YHReportAnalysisAct {
  private YHReportAnalysisLogic logic = new YHReportAnalysisLogic();
  public static final String attachmentFolder = "hr";


  /**
   *办公用品报表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public String getAnalysis(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String careDate1 = request.getParameter("careDate1");
      String careDate2 = request.getParameter("careDate2");
      String mapType = request.getParameter("mapType");
      String officeDepository = request.getParameter("officeDepository");
      String officeProtype = request.getParameter("officeProtype");
      String product = request.getParameter("product");
      String module = request.getParameter("module");
      String deptId = request.getParameter("deptId");
      
      Map map = new HashMap();
      map.put("careDate1", careDate1);
      map.put("careDate2", careDate2);
      map.put("officeDepository", officeDepository);
      map.put("officeProtype", officeProtype);
      map.put("product", product);
      map.put("module", module);
      map.put("mapType", mapType);
      map.put("deptId", deptId);
      
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getAnalysis(dbConn, map, request.getParameterMap(), person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询");
      
      if(YHUtility.isNullorEmpty(mapType)){
        if(module.equals("OFFICE_LYWP")){
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG,"成功查询");
          request.setAttribute(YHActionKeys.RET_DATA, data);
          return "/core/inc/rtjson.jsp";
        }
        PrintWriter pw = response.getWriter();
        pw.println(data);
        pw.flush();
        return null;
      }
      else{
        data = "{\"data\":\""+data+"\"}";
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功查询");
        request.setAttribute(YHActionKeys.RET_DATA, data);
        return "/core/inc/rtjson.jsp";
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
  }
  
  
  /**
   *导出cvs
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public String printExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    OutputStream ops = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String careDate1 = request.getParameter("careDate1");
      String careDate2 = request.getParameter("careDate2");
      String mapType = request.getParameter("mapType");
      String officeDepository = request.getParameter("officeDepository");
      String officeProtype = request.getParameter("officeProtype");
      String product = request.getParameter("product");
      String module = request.getParameter("module");
      String deptId = request.getParameter("deptId");
      String title = request.getParameter("title");
      
      Map map = new HashMap();
      map.put("careDate1", careDate1);
      map.put("careDate2", careDate2);
      map.put("officeDepository", officeDepository);
      map.put("officeProtype", officeProtype);
      map.put("product", product);
      map.put("module", module);
      map.put("mapType", mapType);
      map.put("deptId", deptId);
      map.put("title", title);
      
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      ArrayList<YHDbRecord> dbl  = this.logic.printExcel(dbConn, map, request.getParameterMap(), person);
      
      String fileName = URLEncoder.encode("报表.xls", "UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
      
      ops = response.getOutputStream();
      YHJExcelUtil.writeExc(ops, dbl);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }finally{
      ops.close();
    }
    return null;
  }

  
  /**
   * 所属部门下拉框
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getOfficeDepository(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

      String data = "";
      data = this.logic.getOfficeDepository(dbConn);
      if(YHUtility.isNullorEmpty(data)){
        data = "[]";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

}
