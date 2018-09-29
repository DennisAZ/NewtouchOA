package yh.cms.template.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.cms.template.data.YHCmsTemplate;
import yh.cms.template.logic.YHTemplateLogic;
import yh.core.codeclass.data.YHCodeItem;
import yh.core.codeclass.logic.YHCodeClassLogic;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

public class YHTemplateAct {
  
  /**
   * 得到模板的所有类型

   * 根据seqId（codeClass） 得到所有的codeItem
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCodeItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHCodeClassLogic codeLogic = new YHCodeClassLogic();
      String data = "[";
      List<YHCodeItem> itemList = new ArrayList<YHCodeItem>();
      itemList = codeLogic.getCodeItem(dbConn, "TEMPLATE_TYPE");
      for (int i = 0; i < itemList.size(); i++) {
        YHCodeItem item = itemList.get(i);
        data = data + YHFOM.toJson(item) + ",";
      }
      if (itemList.size() > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
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
  * CMS模板 添加
  * 
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
  public String addTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    Connection dbConn = null;
    String contexPath = request.getContextPath();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      YHCmsTemplate template  = (YHCmsTemplate) YHFOM.build(fileForm.getParamMap(), YHCmsTemplate.class, null); 
      YHTemplateLogic logic = new YHTemplateLogic();
      logic.addTemplate(dbConn, template, person, fileForm);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    response.sendRedirect(contexPath + "/cms/template/manage.jsp");
    return null;
  }
  
  /**
   * CMS模板 通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getTemplateList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      String stationId = request.getParameter("stationId");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHTemplateLogic logic = new YHTemplateLogic();
      String data = logic.getTemplateList(dbConn, request.getParameterMap(), person, stationId);
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
  public String getTemplateDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    String flag = request.getParameter("flag");
    if (YHUtility.isNullorEmpty(flag)) {
      flag = "0";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHTemplateLogic logic = new YHTemplateLogic();
      StringBuffer data = logic.getTemplateDetailLogic(dbConn, Integer.parseInt(seqId), Integer.parseInt(flag));
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
   * CMS模板 修改
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    Connection dbConn = null;
    String contexPath = request.getContextPath();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHCmsTemplate template  = (YHCmsTemplate) YHFOM.build(fileForm.getParamMap(), YHCmsTemplate.class, null); 
      YHTemplateLogic logic = new YHTemplateLogic();
      logic.updateTemplate(dbConn, template, fileForm);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功修改数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    response.sendRedirect(contexPath + "/cms/template/manage.jsp");
    return null;
  }
  
  /**
   * 删除模板
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteTempalte(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHTemplateLogic logic = new YHTemplateLogic();
      logic.deleteTemplateLogic(dbConn, seqIdStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 模板查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      Map<Object, Object> map = new HashMap<Object, Object>();
      map.put("templateName", YHDBUtility.escapeLike(request.getParameter("templateName")));
      map.put("templateFileName", YHDBUtility.escapeLike(request.getParameter("templateFileName")));
      map.put("templateType", YHDBUtility.escapeLike(request.getParameter("templateType")));
      String data = "";
      YHTemplateLogic logic = new YHTemplateLogic();
      data = logic.queryTemplateLogic(dbConn, request.getParameterMap(), map, person);
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
   * 获取所有站点
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectStationName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHTemplateLogic logic = new YHTemplateLogic();
      String data = logic.getStationName(dbConn);
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
