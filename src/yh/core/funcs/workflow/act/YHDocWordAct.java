package yh.core.funcs.workflow.act;

import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.exps.YHInvalidParamException;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.logic.YHDocWordLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHDocWordAct {
  YHDocWordLogic  logic=new YHDocWordLogic();
  /**
   * 添加文件字
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addDocWordAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      String dwName = request.getParameter("dwName");
      String indexStyle = request.getParameter("indexStyle");
      String deptPrivId= request.getParameter("deptPrivId");
      String rolePrivId= request.getParameter("rolePrivId");
      String userPrivId= request.getParameter("userPrivId");
       this.logic.addDocWordLogic(dbConn, dwName, indexStyle,deptPrivId, rolePrivId, userPrivId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件字保存成功");
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件字保存失败");
    }
    return "/core/funcs/workflow/flowrun/docword/newRemind.jsp";
  }
  
  /**
   *   通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDocWordListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getDocWordListLogic(dbConn, request.getParameterMap(), person);
      PrintWriter pw = response.getWriter();
      //System.out.println(data);
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
   *   通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryDocWordListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String dwName=request.getParameter("dwName");
      String indexS=request.getParameter("indexS");
      String data = this.logic.queryDocWordListLogic(dbConn,request.getParameterMap(), dwName, indexS);
      PrintWriter pw = response.getWriter();
      //System.out.println(data);
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
   * 获取部门
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDeptName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      String deptId= request.getParameter("deptId");
      String data= this.logic.getDeptName(dbConn, deptId);
      data="{deptName:'"+data+"'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取角色
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRoleName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      String roleId= request.getParameter("roleId");
      String data= this.logic.getRoleName(dbConn, roleId);
      data="{roleName:'"+data+"'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 删除单个文件字
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteDocWordAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      String seqId= request.getParameter("seqId");
      this.logic.deleteDocWordLogic(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);

    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);

    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 删除单个文件字
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteAllDocWordAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      String seqIds= request.getParameter("seqIds");
      this.logic.deleteAllDocWordLogic(dbConn, seqIds);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);

    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);

    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取用户
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      String userId= request.getParameter("userId");
      String data= this.logic.getUserName(dbConn, userId);
      data="{userName:'"+data+"'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);

      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);

    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  public String getDocWordAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      String seqId= request.getParameter("seqId");
      String data= this.logic.getDocWordLogic(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);

      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);

    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateDocWordAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      String seqId= request.getParameter("seqId");
      String dwName = request.getParameter("dwName");
      String indexStyle = request.getParameter("indexStyle");
      String deptPrivId= request.getParameter("deptPrivId");
      String rolePrivId= request.getParameter("rolePrivId");
      String userPrivId= request.getParameter("userPrivId");
      this.logic.updateDocWordLogic(dbConn, dwName, indexStyle, deptPrivId, rolePrivId, userPrivId, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);

    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);

    }
    return "/core/funcs/workflow/flowrun/docword/editRemind.jsp";
  }
  
}
