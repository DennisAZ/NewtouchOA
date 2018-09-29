package yh.core.funcs.system.wordmoudel.act;

import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.wordmoudel.data.YHWordModel;
import yh.core.funcs.system.wordmoudel.data.YHWordModelCont;
import yh.core.funcs.system.wordmoudel.logic.YHWordModelLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

public class YHWordModelAct {
  
  private static Logger log = Logger.getLogger(YHWordModelAct.class);
/**
 * 保存模板文件
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
  public String saveWordModel(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      YHWordModelLogic wml = new YHWordModelLogic();
      wml.saveLogic(conn, fileForm,person.getSeqId(),YHWordModelCont.ATTA_PATH);
     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, "");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/system/wordmodel/success.jsp";
  }
  /**
   * 更新套红模板
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateWordModel(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection conn = null;
    String seqId = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      YHWordModelLogic wml = new YHWordModelLogic();
      wml.updateLogic(conn, fileForm,person.getSeqId(),YHWordModelCont.ATTA_PATH);
      seqId = fileForm.getParameter("seqId");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, "");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/system/wordmodel/success.jsp?seqId=" + seqId;
  }
  
  /**
   * 分页列出所有的套红模板
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listAllModel(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection conn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHWordModelLogic wml = new YHWordModelLogic();
      String data =  wml.listWordModelSearch(conn, request.getParameterMap());
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 查询套红模板--暂未启用
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listModelSearch(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection conn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHWordModelLogic wml = new YHWordModelLogic();
      String data =  wml.listWordModelSearch(conn, request.getParameterMap());
      PrintWriter pw = response.getWriter();
      pw.println(data);
      //System.out.println(data);
      pw.flush();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 删除套红模块
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteModel(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection conn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      String idStr = request.getParameter("seqId");
      int id = Integer.valueOf(idStr);
      YHWordModelLogic wml = new YHWordModelLogic();
      wml.doDelete(conn, id,YHWordModelCont.ATTA_PATH);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功!");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 显示单个套红模板
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String showModel(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection conn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      String idStr = request.getParameter("seqId");
      int id = Integer.valueOf(idStr);
      YHORM orm = new YHORM();
      YHWordModel wm = (YHWordModel) orm.loadObjSingle(conn, YHWordModel.class, id);
      StringBuffer data = YHFOM.toJson(wm);
      //System.out.println(data.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 显示单个套红模板
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAllDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection conn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      String data = YHOrgSelectLogic.getAlldept(conn);
      //System.out.println(data.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
