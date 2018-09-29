package yh.cms.column.act;

import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import yh.cms.column.data.YHCmsColumn;
import yh.cms.column.logic.YHColumnLogic;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;

public class YHColumnAct {

  /**
   * 得到模板的所有类型

   * 根据seqId（codeClass） 得到所有的codeItem
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String stationId = request.getParameter("stationId");
    if (YHUtility.isNullorEmpty(stationId)) {
      stationId = "0";
    }
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHColumnLogic logic = new YHColumnLogic();
      String data = logic.getTemplateArticle(dbConn, stationId);
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
   * 获取详情
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getInfomation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String stationId = request.getParameter("stationId");
    String parentId = request.getParameter("parentId");
    if (YHUtility.isNullorEmpty(stationId) || "null".equals(stationId)) {
      stationId = "0";
    }
    if (YHUtility.isNullorEmpty(parentId) || "null".equals(parentId)) {
      parentId = "0";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHColumnLogic logic = new YHColumnLogic();
      StringBuffer data = logic.getInfomation(dbConn, Integer.parseInt(stationId), Integer.parseInt(parentId));
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
  * CMS栏目 添加
  * 
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
  public String addColumn(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      YHCmsColumn column = (YHCmsColumn)YHFOM.build(request.getParameterMap());
      YHColumnLogic logic = new YHColumnLogic();
      int seqId = logic.addColumn(dbConn, column, person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
      request.setAttribute(YHActionKeys.RET_DATA, "{maxSeqId:\"" + seqId + "\"}");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 得到模板的所有类型

   * 根据seqId（codeClass） 得到所有的codeItem
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getColumnTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String id = request.getParameter("id");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      YHColumnLogic logic = new YHColumnLogic();
      String data = "";
      if (!YHUtility.isNullorEmpty(id) && !id.equals("0")) {
        String idArry[] = id.split(",");
        if (idArry != null && idArry.length > 0) {
          data = logic.getColumnTree(dbConn, idArry[0], idArry[1], person);
        }
      } else {
        data = logic.getStationTree(dbConn, person);
      }
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
   * 获取详情
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getColumnDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHColumnLogic logic = new YHColumnLogic();
      StringBuffer data = logic.getColumnDetailLogic(dbConn, Integer.parseInt(seqId));
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
   * CMS栏目 修改
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateColumn(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHCmsColumn column  = (YHCmsColumn) YHFOM.build(request.getParameterMap()); 
      YHColumnLogic logic = new YHColumnLogic();
      logic.updateColumn(dbConn, column);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功修改数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * CMS下级栏目 通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getColumnList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    int seqId = 0;
    String seqIdStr = (String)request.getParameter("seqId");
    if(!YHUtility.isNullorEmpty(seqIdStr)){
      seqId = Integer.parseInt(seqIdStr);
    }
    String flag = (String)request.getParameter("flag");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHColumnLogic logic = new YHColumnLogic();
      String data = logic.getColumnList(dbConn, request.getParameterMap(), person, seqId, flag);
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
   * 删除栏目
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteColumn(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHColumnLogic logic = new YHColumnLogic();
      int data = logic.deleteColumn(dbConn, seqIdStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\""+data+"\"");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 发布
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String toRelease(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHColumnLogic logic = new YHColumnLogic();
      int data = logic.toRelease(dbConn, Integer.parseInt(seqId), true);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      request.setAttribute(YHActionKeys.RET_DATA, "\""+data+"\"");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "发布失败！");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 调序
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String toSort(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    String toSeqId = request.getParameter("toSeqId");
    String flag = request.getParameter("flag");
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    if (YHUtility.isNullorEmpty(toSeqId)) {
      toSeqId = "0";
    }
    if (YHUtility.isNullorEmpty(flag)) {
      flag = "0";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHColumnLogic logic = new YHColumnLogic();
      logic.toSort(dbConn, Integer.parseInt(seqId), Integer.parseInt(toSeqId), Integer.parseInt(flag));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 验证路径是否存在
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String checkPath(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String stationId = request.getParameter("stationId");
    String parentId = request.getParameter("parentId");
    String seqId = request.getParameter("seqId");
    String columnPath = request.getParameter("columnPath");
    if (YHUtility.isNullorEmpty(stationId) || "null".equals(stationId)) {
      stationId = "0";
    }
    if (YHUtility.isNullorEmpty(parentId) || "null".equals(parentId)) {
      parentId = "0";
    }
    if (YHUtility.isNullorEmpty(seqId) || "null".equals(seqId)) {
      seqId = "0";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHColumnLogic logic = new YHColumnLogic();
      int data = logic.checkPath(dbConn, Integer.parseInt(stationId), Integer.parseInt(parentId), Integer.parseInt(seqId), columnPath);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      request.setAttribute(YHActionKeys.RET_DATA, "\""+data+"\"");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "发布失败！");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 站点预览
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String toSee(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    if (YHUtility.isNullorEmpty(seqId) || "null".equals(seqId)) {
      seqId = "0";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHColumnLogic logic = new YHColumnLogic();
      String data = logic.getPath(dbConn, Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      request.setAttribute(YHActionKeys.RET_DATA, "\""+data+"\"");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "预览失败！");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
}
