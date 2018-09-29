package yh.core.funcs.doc.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowFormType;
import yh.core.funcs.doc.data.YHDocFlowProcess;
import yh.core.funcs.doc.data.YHDocFlowSort;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.logic.YHFlowFormLogic;
import yh.core.funcs.doc.logic.YHFlowProcessLogic;
import yh.core.funcs.doc.logic.YHFlowSortLogic;
import yh.core.funcs.doc.logic.YHFlowTypeLogic;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;

public class YHFlowTypeAct {
  private static Logger log = Logger.getLogger(YHFlowTypeAct.class);
  public String getFlowSort(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    ArrayList<YHDocFlowSort> sortList = null;
    YHDocFlowSort flowSort = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer("[");
      String data = "";
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      
      sortList = flowTypeLogic.getFlowSortList(dbConn);
      if(sortList.size() > 0) {
        for(int i = 0; i < sortList.size(); i++) {
          flowSort = sortList.get(i);
            sb.append("{");
            sb.append("seqId:\"" + flowSort.getSeqId() + "\"");
            sb.append(",sortName:\"" + flowSort.getSortName() + "\"");
            sb.append(",sortParent:\"" + flowSort.getSortParent() + "\"");
            sb.append("},");                   
        }
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      data = sb.toString();
      
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出流程分类数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String addFlowType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String flowType = request.getParameter("flowType");
    String flowName = request.getParameter("flowName");
    String formSeqId = request.getParameter("formSeqId");
    String flowDoc = request.getParameter("flowDoc");
    String manageUser = request.getParameter("manageUser");
    String flowNo = request.getParameter("flowNo");
    String flowSort = request.getParameter("flowSort");
    String autoName = request.getParameter("autoName");
    String autoNum = request.getParameter("autoNum");
    String autoLen = request.getParameter("autoLen");
    String queryUser = request.getParameter("queryUser");
    String flowDesc = request.getParameter("flowDesc");
    String autoEdit = request.getParameter("autoEdit");
    String newUser = request.getParameter("newUser");
    String queryItem = request.getParameter("queryItem");
    String commentPriv = request.getParameter("commentPriv");
    String deptId = request.getParameter("deptId");
    String freePreset = request.getParameter("freePreset");
    String freeOther = request.getParameter("freeOther");
    String queryUserDept = request.getParameter("queryUserDept");
    String manageUserDept = request.getParameter("manageUserDept");
    String editPriv = request.getParameter("editPriv");
    String listFldsStr = request.getParameter("listFldsStr");
    String allowPreSet = request.getParameter("allowPreSet");
    String modelId = request.getParameter("modelId");
    String modelName = request.getParameter("modelName");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      
      Map map = new HashMap();
      
      map.put("flowName", flowName);
      map.put("formSeqId", formSeqId);
      map.put("flowDoc", flowDoc);
      map.put("flowType", flowType);
      map.put("manageUser", manageUser);
      map.put("flowNo", flowNo);
      map.put("flowSort", flowSort);
      map.put("autoName", autoName);
      map.put("autoNum", autoNum);
      map.put("autoLen", autoLen);
      map.put("queryUser", queryUser);
      map.put("flowDesc", flowDesc);
      map.put("autoEdit", autoEdit);
      map.put("newUser", newUser);
      map.put("queryItem", queryItem);
      map.put("commentPriv", commentPriv);
      map.put("deptId", deptId);
      map.put("freePreset", freePreset);
      map.put("freeOther", freeOther);
      map.put("queryUserDept", queryUserDept);
      map.put("manageUserDept", manageUserDept);
      map.put("editPriv", editPriv);
      map.put("listFldsStr", listFldsStr);
      map.put("allowPreSet", allowPreSet);
      map.put("modelId", modelId);
      map.put("modelName", modelName);
      
      flowTypeLogic.saveFlowType(dbConn, map);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功添加人员");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getFlowType(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    ArrayList<YHDocFlowFormType> typeList = null;
    YHDocFlowFormType formType = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      StringBuffer sb = new StringBuffer("[");
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      typeList = flowTypeLogic.getFlowFormTypeList(dbConn);
      if(typeList.size() > 0) {
        for(int i = 0; i < typeList.size(); i++) {
            formType = typeList.get(i);
            sb.append("{");
            sb.append("seqId:\"" + formType.getSeqId() + "\"");
            sb.append(",formName:\"" + formType.getFormName() + "\"");
            sb.append("},");                   
        }
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出流程分类数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getFlowTypeJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    List<YHDocFlowType> typeList = new ArrayList();
    YHDocFlowType flowType = null;
    String checkTypeStr = request.getParameter("checkType");
    int checkType = 0;
    String sSortId = request.getParameter("sortId");
    try {
      if(checkTypeStr != null && !"".equals(checkTypeStr)){
        try {
          checkType = Integer.parseInt(checkTypeStr);
        } catch (Exception e) {
          checkType = 0;
        }
      }
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      StringBuffer sb = new StringBuffer("[");
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      if (YHUtility.isNullorEmpty(sSortId)) {
        typeList = flowTypeLogic.getFlowTypeList(dbConn);
      } else {
        typeList = flowTypeLogic.getFlowTypeList(sSortId , dbConn);
      }
      int count = 0 ;
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      YHFlowProcessLogic fpl = new YHFlowProcessLogic();
      for(int i = 0; i < typeList.size(); i++) {
        flowType = typeList.get(i);
        List<YHDocFlowProcess> list = fpl.getFlowPrrocessByFlowId1(flowType.getSeqId() , dbConn);
        if(roleUtility.prcsRole(flowType, list , checkType, loginUser, dbConn)){
          sb.append("{");
          sb.append("seqId:\"" + flowType.getSeqId() + "\"");
          sb.append(",flowName:\"" + flowType.getFlowName() + "\"");
          sb.append("},"); 
          count ++;
        }
      }
      if(count > 0){  
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出流程分类数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 取得管理权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getFlowTypeJsonByManager(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    List<YHDocFlowType> typeList = new ArrayList();
    YHDocFlowType flowType = null;
    String sSortId = request.getParameter("sortId");
    String checkTypeStr = request.getParameter("checkType");
    int checkType = 0;
    try {
      if(checkTypeStr != null && !"".equals(checkTypeStr)){
        try {
          checkType = Integer.parseInt(checkTypeStr);
        } catch (Exception e) {
          checkType = 0;
        }
      }
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      StringBuffer sb = new StringBuffer("[");
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      if (YHUtility.isNullorEmpty(sSortId)) {
        typeList = flowTypeLogic.getFlowTypeList(dbConn);
      } else {
        typeList = flowTypeLogic.getFlowTypeList(sSortId , dbConn);
      }
      int count = 0 ;
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      YHFlowProcessLogic fpl = new YHFlowProcessLogic();
      for(int i = 0; i < typeList.size(); i++) {
        flowType = typeList.get(i);
        List<YHDocFlowProcess> list = fpl.getFlowPrrocessByFlowId1(flowType.getSeqId() , dbConn);
        if(roleUtility.prcsRoleByManager(flowType, list , checkType, loginUser)){
          sb.append("{");
          sb.append("seqId:\"" + flowType.getSeqId() + "\"");
          sb.append(",flowName:\"" + flowType.getFlowName() + "\"");
          sb.append("},"); 
          count ++;
        }
      }
      if(count > 0){  
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出流程分类数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 主要是用于定义规则页面
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getFlowTypeJson1(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    List<YHDocFlowType> typeList = new ArrayList();
    YHDocFlowType flowType = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sSortId = request.getParameter("sortId");
      StringBuffer sb = new StringBuffer("[");
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      if (YHUtility.isNullorEmpty(sSortId)) {
        Map m = new HashMap();
        m.put("FREE_OTHER", "2");
        YHORM orm = new YHORM();
        typeList = orm.loadListSingle(dbConn, YHDocFlowType.class, m);
      } else {
        typeList = flowTypeLogic.getFlowTypeList(sSortId , dbConn ,"2");
      }
      
      int count = 0 ;
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      YHFlowProcessLogic fpl = new YHFlowProcessLogic();
      for(int i = 0; i < typeList.size(); i++) {
        flowType = typeList.get(i);
        List<YHDocFlowProcess> list = fpl.getFlowPrrocessByFlowId1(flowType.getSeqId() , dbConn);
        if(roleUtility.prcsRole(flowType, list , 0, loginUser, dbConn)){
          sb.append("{");
          sb.append("seqId:\"" + flowType.getSeqId() + "\"");
          sb.append(",flowName:\"" + flowType.getFlowName() + "\"");
          sb.append("},"); 
          count ++;
        }
      }
      if(count > 0){  
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出流程分类数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getAddMessage(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHDeptLogic deptLogic = new YHDeptLogic();
      YHPerson u = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      StringBuffer sb = new StringBuffer("{sortList:[");
      YHFlowSortLogic flowSortLogic = new YHFlowSortLogic();
      YHFlowFormLogic ffLogic = new YHFlowFormLogic();
     
      List<YHDocFlowSort> sortList = flowSortLogic.getFlowSort(dbConn);
     
      if(sortList.size() > 0) {
        YHWorkFlowUtility w = new YHWorkFlowUtility();
        for(YHDocFlowSort tmp : sortList) {
          if (!w.isHaveSortRight(tmp.getDeptId(), u, dbConn)) {
            continue;
          }
          sb.append("{");
          sb.append("value:" + tmp.getSeqId() + ",");
          sb.append("text:'├" + tmp.getSortName() + "'");
          sb.append("},");
        }
        if(sb.charAt(sb.length() - 1) == ','){
          sb.deleteCharAt(sb.length() - 1);
        }
      }
      sb.append("],formList:");
      sb.append(ffLogic.getFlowFormTypeOption(dbConn, u));
      String deptList  = deptLogic.getDeptTreeJson(u , dbConn);
      sb.append(",deptList:" + deptList);
      sb.append("}");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getEditMessage(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String sFlowId = request.getParameter("flowId");
    try {
     
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer("{flowType:");
      YHFlowSortLogic flowSortLogic = new YHFlowSortLogic();
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      YHPerson u = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDocFlowType ft = flowTypeLogic.getFlowTypeById(Integer.parseInt(sFlowId) , dbConn);
     
      YHDeptLogic deptLogic = new YHDeptLogic();
      sb.append(ft.toJson());
      YHFlowFormLogic ffLogic = new YHFlowFormLogic();
      List<YHDocFlowSort> sortList = flowSortLogic.getFlowSort(dbConn);
      int formId = ft.getFormSeqId();
      
      String formList = YHUtility.encodeSpecial(ffLogic.getTitle(dbConn, formId));
      
      sb.append(",sortList:[");
      if(sortList.size() > 0) {
        YHWorkFlowUtility w = new YHWorkFlowUtility();
        for(YHDocFlowSort tmp : sortList) {
          if (!w.isHaveSortRight(tmp.getDeptId(), u, dbConn)) {
            continue;
          }
          sb.append("{");
          sb.append("value:" + tmp.getSeqId() + ",");
          sb.append("text:'├" + tmp.getSortName() + "'");
          sb.append("},");
        }
        if(sb.charAt(sb.length() - 1) == ','){
          sb.deleteCharAt(sb.length() - 1);
        }
      }
      sb.append("],formList:");
      
     
      sb.append(ffLogic.getFlowFormTypeOption(dbConn , u));
      
      int workCount = flowTypeLogic.getWorkCountByFlowId(ft.getSeqId() , dbConn);
      int delCount = flowTypeLogic.getDelWorkCountByFlowId(ft.getSeqId() , dbConn);
      
      String deptList = deptLogic.getDeptTreeJson(u , dbConn);
      sb.append(",deptList:"  + deptList);
      sb.append(",workCount:" + workCount + ",delCount:" + delCount + ",formItem:\""+ formList +"\"}");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String saveFlowType(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHDocFlowType ft = new YHDocFlowType();
      this.setFlowType(request, ft, false);
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      flowTypeLogic.saveFlowType(ft , dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String updateFlowType(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String sFlowId = request.getParameter("flowId");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      YHDocFlowType ft = flowTypeLogic.getFlowTypeById(Integer.parseInt(sFlowId) , dbConn);
      this.setFlowType(request, ft, true);
      flowTypeLogic.updateFlowType(ft , dbConn);
      //flowTypeLogic.
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delFlowType(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String sFlowId = request.getParameter("flowId");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      YHDocFlowType ft = flowTypeLogic.getFlowTypeById(Integer.parseInt(sFlowId) , dbConn);
      flowTypeLogic.delFlowType(ft , dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public void setFlowType(HttpServletRequest request,
      YHDocFlowType ft , boolean isUpdate) {
    String flowSort = request.getParameter("flowSort");
    String sFlowNo = request.getParameter("flowNo");
    int flowNo = 0;
    if(!"".equals(sFlowNo)){
      flowNo = Integer.parseInt(sFlowNo);
    }
    String flowName = request.getParameter("flowName");
    String flowType = request.getParameter("flowType");
    String formSeqId = request.getParameter("formId");
    String freePreset = request.getParameter("freePreset");
    String freeOther = request.getParameter("freeOther");   
    String flowDoc = request.getParameter("flowDoc");
    String flowDesc = request.getParameter("flowDesc");
    String autoName = request.getParameter("autoName");
    String sAutoNum = request.getParameter("autoNum");
    String sDeptId = request.getParameter("deptId");
    String  listFldsStr = request.getParameter("listFldsStr");
    int autoNum = 0;
    if(!"".equals(sAutoNum)){
      autoNum = Integer.parseInt(sAutoNum);
    }
    String sAutoLen = request.getParameter("autoLen");
    int autoLen = 0;
    if(!"".equals(sAutoLen)){
      autoLen = Integer.parseInt(sAutoLen);
    }
    String autoEdit = request.getParameter("autoEdit");
    ft.setDeptId(Integer.parseInt(sDeptId));
    ft.setFlowSort(Integer.parseInt(flowSort));
    ft.setFlowNo(flowNo);
    ft.setFlowName(flowName);
    if(flowType != null){
      ft.setFlowType(flowType);
    }
    if(formSeqId != null){
      ft.setFormSeqId(Integer.parseInt(formSeqId));
    }
    ft.setFreePreset(freePreset);
    ft.setFreeOther(freeOther);
    ft.setFlowDoc(flowDoc);
    ft.setFlowDesc(flowDesc);
    ft.setAutoNum(autoNum);
    ft.setAutoName(autoName);
    ft.setAutoLen(autoLen);
    ft.setAutoEdit(autoEdit);
    if(isUpdate){
      ft.setListFldsStr(listFldsStr);
    }
  }
  public String getFlowTypeBySort(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String sSortId = request.getParameter("sortId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      StringBuffer sb = new StringBuffer("[");
      String data = "";
      StringBuffer workCounts = new StringBuffer("[");
      StringBuffer formName = new StringBuffer("[");
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      List<YHDocFlowType> typeList = flowTypeLogic.getFlowTypeList(Integer.parseInt(sSortId) , dbConn);
      YHPerson u = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int count = 0 ;
      YHWorkFlowUtility w = new YHWorkFlowUtility();
      for(int i = 0; i < typeList.size(); i++) {
        YHDocFlowType flowType = typeList.get(i);
        if (!w.isHaveRight(flowType.getDeptId(), u, dbConn)) {
          continue;
        }
        sb.append(flowType.toJson() + ",");
        //取得工作数量
        int workCount = flowTypeLogic.getWorkCountByFlowId(flowType.getSeqId() , dbConn);
        int delCount = flowTypeLogic.getDelWorkCountByFlowId(flowType.getSeqId() , dbConn);
        workCounts.append("{workCount:" + workCount);
        workCounts.append(",delCount:" + delCount + "},");
        formName.append("'" + flowTypeLogic.getFormName(flowType.getFormSeqId(), dbConn) + "',");
        count++;
      }
      if (count >0 ){
        sb.deleteCharAt(sb.length() - 1);
        workCounts.deleteCharAt(workCounts.length() - 1);
        formName.deleteCharAt(formName.length() - 1);
      }
        
     
      workCounts.append("]");
      sb.append("]");
      formName.append("]");
      data = "{flowList:" + sb.toString() + ",workCounts:" + workCounts.toString() + ",formName:"+ formName.toString() +"}";
      request.setAttribute(YHActionKeys.RET_DATA, data );
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "取出数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String empty(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String sFlowId = request.getParameter("flowId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int flowId = Integer.parseInt(sFlowId);
      YHFlowTypeLogic logic = new YHFlowTypeLogic();
      logic.empty(flowId, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功清空数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
