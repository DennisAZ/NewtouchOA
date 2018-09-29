package yh.core.funcs.workflow.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHDocWord;
import yh.core.funcs.workflow.data.YHDocumentsType;
import yh.core.funcs.workflow.logic.YHDocumentsTypeLogic;
import yh.core.funcs.workflow.logic.YHFlowTypeLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
public class YHDocumentsTypeAct {

  private YHDocumentsTypeLogic logic = new YHDocumentsTypeLogic();
  /**
   * 新建文件类型
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addDocumentsTypeInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String contexPath = request.getContextPath();
    YHFOM fom = new YHFOM();
    Map map = fom.buildMap(request.getParameterMap());
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.addDocumentsTypeInfo(dbConn, map, person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    response.sendRedirect(contexPath + "/core/funcs/workflow/flowrun/documentsType/newRemind.jsp");
    return null;
  }
  
  /**
   *文件类型 通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDocumentsTypeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getDocumentsTypeList(dbConn, request.getParameterMap(), person);
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
   * 获取详情
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDocumentsTypeDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDocumentsType documentsType = (YHDocumentsType) this.logic.getDocumentsTypeDetail(dbConn, Integer.parseInt(seqId));
      if (documentsType == null) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data = YHFOM.toJson(documentsType);
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
   * 编辑文件类型
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateDocumentsTypeInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String contexPath = request.getContextPath();
    YHFOM fom = new YHFOM();
    Map map = fom.buildMap(request.getParameterMap());
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      this.logic.updateDocumentsTypeInfo(dbConn, map, person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功修改数据");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    response.sendRedirect(contexPath + "/core/funcs/workflow/flowrun/documentsType/manage.jsp");
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
      this.logic.deleteFileLogic(dbConn, seqIdStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 员工关怀查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryDocumentsTypeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFOM fom = new YHFOM();
      Map map = fom.buildMap(request.getParameterMap());
      String data = "";
      data = this.logic.queryDocumentsTypeList(dbConn, request.getParameterMap(), map, person);
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
   * 得到ById
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getFlowTypeName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("seqId");
      YHFlowTypeLogic logic = new YHFlowTypeLogic();
      String docWordNameStr =  logic.getFlowTypeName(Integer.parseInt(seqIdStr), dbConn);
      request.setAttribute(YHActionKeys.RET_DATA,"\""+docWordNameStr+"\"");
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
   * 得到ById
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getDocWordById(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("seqId");
      String docWordNameStr = this.logic.getDocumentsTypeById(dbConn, seqIdStr);
      request.setAttribute(YHActionKeys.RET_DATA,"\""+docWordNameStr+"\"");
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
   * 文件字-模糊查找
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDocumentsTypeListSelect(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String condition = request.getParameter("condition");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer("[");
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      List<YHDocWord> docWords = this.logic.getDocumentsTypeListSelect(dbConn,person,condition);
      boolean isHave = false;
      if (docWords!=null && docWords.size()>0) {
        for(YHDocWord docWord:docWords){
          sb.append("{");
          sb.append("dwId:\"" +  docWord.getSeqId() + "\"");
          sb.append(",dwName:\"" + docWord.getDwName() + "\"");
          sb.append("},");
          isHave = true;
        }
        if (isHave) {
          sb.deleteCharAt(sb.length()-1);
        }
        sb.append("]");
        
      }else {
        sb.append("]");
      }
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
}
