package yh.core.funcs.doc.act;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowSort;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.logic.YHFlowSortLogic;
import yh.core.funcs.doc.logic.YHFlowTypeLogic;
import yh.core.funcs.doc.logic.YHFlowWorkAdSearchLogic;
import yh.core.funcs.doc.logic.YHFlowWorkSearchLogic;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHWorkQueryAct {
  private static Logger log = Logger
      .getLogger("yh.core.funcs.doc.act.YHWorkQueryAct");
  public String getWorkList1(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String sSortId =  request.getParameter("sortId");
      YHFlowWorkSearchLogic myWorkLogic = new YHFlowWorkSearchLogic();
      StringBuffer result = myWorkLogic.getWorkList2(dbConn,request.getParameterMap(), loginUser , sSortId , request.getRealPath("/"));
      PrintWriter pw = response.getWriter();
      pw.println( result.toString());
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public String getManagerRole(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      
      YHPerson loginUser = null;
      Connection dbConn = null;
      try {
        String runId = request.getParameter("runId");
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
        
        YHFlowWorkSearchLogic myWorkLogic = new YHFlowWorkSearchLogic();
        StringBuffer result = myWorkLogic.getManagerRoleLogic(dbConn, loginUser, runId);
        request.setAttribute(YHActionKeys.RET_DATA, result.toString());
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      } catch (Exception ex) {
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
      String sSortId = request.getParameter("sortId");
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        
        StringBuffer sb = new StringBuffer("[");
        YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
        if (!YHUtility.isNullorEmpty(sSortId)) {
          typeList = flowTypeLogic.getFlowTypeList(sSortId, dbConn);
        } else {
          typeList = flowTypeLogic.getFlowTypeList(dbConn);
        }
        int count = 0 ;
        YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
        
        YHPrcsRoleUtility prcsRole = new YHPrcsRoleUtility();
        for(int i = 0; i < typeList.size(); i++) {
          flowType = typeList.get(i);
          boolean canShow = true;
          if ("1".equals(flowType.getFlowType())) {
            String seqId = String.valueOf(loginUser.getSeqId());
            if (!(loginUser.isAdminRole() 
                || YHWorkFlowUtility.findId(flowType.getManageUser(), seqId)
                || YHWorkFlowUtility.findId(flowType.getQueryUser() , seqId)
                || prcsRole.prcsRoleByQuery(flowType, loginUser, dbConn))) {
              canShow = false;
            }
          }
          if (canShow) {
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
    public String getFlowType(HttpServletRequest request,
        HttpServletResponse response) throws Exception{
      Connection dbConn = null;
      String sSortId = request.getParameter("sortId");
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
        StringBuffer sb2 = new StringBuffer("[");
        YHFlowTypeLogic ft = new YHFlowTypeLogic();
        YHDocFlowType flowType = null;
        YHPrcsRoleUtility prcsRole = new YHPrcsRoleUtility();
        List<YHDocFlowType> typeList = ft.getFlowTypeList(sSortId , dbConn);
        int flowTypeCount = 0;
        YHFlowWorkSearchLogic myWorkLogic = new YHFlowWorkSearchLogic();
        for(int i = 0; i < typeList.size(); i++) {
          flowType = typeList.get(i);
          boolean canShow = true;
          if ("1".equals(flowType.getFlowType())) {
            String seqId = String.valueOf(loginUser.getSeqId());
            if (!(loginUser.isAdminRole() 
                || YHWorkFlowUtility.findId(flowType.getManageUser(), seqId)
                || YHWorkFlowUtility.findId(flowType.getQueryUser() , seqId)
                || prcsRole.prcsRoleByQuery(flowType, loginUser, dbConn))) {
              canShow = false;
            }
          }
          if (canShow) {
            int flowId = flowType.getSeqId();
            sb2.append("{");
            sb2.append("flowId:\"" + flowId  + "\"");
            sb2.append(",flowName:\"" + flowType.getFlowName() + "\"");
            Map map = myWorkLogic.getWorkCount(flowId,loginUser.getSeqId() , dbConn);
            int endWorkCount = (Integer)map.get("endCount");
            int handlerWorkCount =  (Integer)map.get("handlerCount");
            int newCount = (Integer)map.get("newCount");
            int dealCount = (Integer)map.get("dealCount");
            int overCount = (Integer)map.get("overCount");
            
            sb2.append(",newCount:" + newCount);
            sb2.append(",dealCount:" + dealCount);
            sb2.append(",overCount:" + overCount);
            sb2.append(",endWorkCount:" + endWorkCount);
            sb2.append(",handlerWorkCount:" + handlerWorkCount);
            String flowDoc = flowType.getFlowDoc();
            if (flowDoc == null) {
              flowDoc = "1";
            }
            sb2.append(",flowDoc:" + flowDoc );
            sb2.append(",flowType:" + flowType.getFlowType() );
            sb2.append("},"); 
            flowTypeCount ++;
          }
        }
        if (flowTypeCount > 0) {
          sb2.deleteCharAt(sb2.length() - 1);
        }
        sb2.append("]");
        request.setAttribute(YHActionKeys.RET_DATA, sb2.toString());
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, sSortId);
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
    public String getFlowList(HttpServletRequest request,
        HttpServletResponse response) throws Exception{
      Connection dbConn = null;
      YHDocFlowType flowType = null;
      String sSortId = request.getParameter("sortId");
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
        YHPrcsRoleUtility prcsRole = new YHPrcsRoleUtility();
        StringBuffer sb = new StringBuffer("[");
        List<YHDocFlowSort> list  = new ArrayList();
        YHFlowSortLogic fs = new YHFlowSortLogic();
        if (sSortId == null || "".equals(sSortId)) {
          list = fs.getFlowSort(dbConn);
        } else {
          sSortId = YHWorkFlowUtility.getInStr(sSortId);
          list =  fs.getFlowSortByIds(dbConn , sSortId);
        }
        YHFlowTypeLogic ft = new YHFlowTypeLogic();
        YHFlowWorkSearchLogic myWorkLogic = new YHFlowWorkSearchLogic();
        int count = 0;
        for(YHDocFlowSort tmp : list){
          StringBuffer sb2 = new StringBuffer();
          sb2.append("[");
//          if (count  <= 1 ) {
//            List<YHFlowType> typeList = ft.getFlowTypeList(String.valueOf(tmp.getSeqId()) , dbConn);
//            int flowTypeCount = 0 ;
//            for(int i = 0; i < typeList.size(); i++) {
//              flowType = typeList.get(i);
//              boolean canShow = true;
//              if ("1".equals(flowType.getFlowType())) {
//                String seqId = String.valueOf(loginUser.getSeqId());
//                if (!(loginUser.isAdminRole() 
//                    || YHWorkFlowUtility.findId(flowType.getManageUser(), seqId)
//                    || YHWorkFlowUtility.findId(flowType.getQueryUser() , seqId)
//                    || prcsRole.prcsRoleByQuery(flowType, loginUser, dbConn))) {
//                  canShow = false;
//                }
//              }
//              if (canShow) {
//                int flowId = flowType.getSeqId();
//                sb2.append("{");
//                sb2.append("flowId:\"" + flowId  + "\"");
//                sb2.append(",flowName:\"" + flowType.getFlowName() + "\"");
//                Map map = myWorkLogic.getWorkCount(flowId,loginUser.getSeqId() , dbConn);
//                int endWorkCount = (Integer)map.get("endCount");
//                int handlerWorkCount =  (Integer)map.get("handlerCount");
//                int newCount = (Integer)map.get("newCount");
//                int dealCount = (Integer)map.get("dealCount");
//                int overCount = (Integer)map.get("overCount");
//                
//                sb2.append(",newCount:" + newCount);
//                sb2.append(",dealCount:" + dealCount);
//                sb2.append(",overCount:" + overCount);
//                sb2.append(",endWorkCount:" + endWorkCount);
//                sb2.append(",handlerWorkCount:" + handlerWorkCount);
//                String flowDoc = flowType.getFlowDoc();
//                if (flowDoc == null) {
//                  flowDoc = "1";
//                }
//                sb2.append(",flowDoc:" + flowDoc );
//                sb2.append(",flowType:" + flowType.getFlowType() );
//                sb2.append("},"); 
//                flowTypeCount ++;
//              }
//            }
//            if (flowTypeCount > 0) {
//              sb2.deleteCharAt(sb2.length() - 1);
//            }
//          }
          sb2.append("]");
          sb.append("{");
          sb.append("seqId:" + tmp.getSeqId());
          sb.append(",sortName:'" + tmp.getSortName() + "'");
          sb.append(",flows:" + sb2.toString());
          sb.append("},");
          count++;
        }
        if (list.size() > 0 ) {
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
    public String getQueryItem(HttpServletRequest request,
        HttpServletResponse response) throws Exception{
      String sFlowId = request.getParameter("flowId");
      int flowId = 0 ;
      if (YHUtility.isInteger(sFlowId)) {
        flowId = Integer.parseInt(sFlowId);
      }
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
        YHFlowWorkSearchLogic myWorkLogic = new YHFlowWorkSearchLogic();
        String result = myWorkLogic.getFormItem(flowId ,loginUser, dbConn);
        request.setAttribute(YHActionKeys.RET_DATA, result);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "全部取出流程分类数据！");
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
    public String doQuery(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      String returnUrl = YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrun/query/doQuery.jsp";
      String sSortId =  request.getParameter("sortId");
      if (sSortId == null) {
        sSortId = "";
      }
      String sFlowId = request.getParameter("flowId");
      String condFormula = request.getParameter("condFormula");
      YHPerson loginUser = null;
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        loginUser = (YHPerson) request.getSession().getAttribute(
            YHConst.LOGIN_USER);
        YHFlowWorkAdSearchLogic myWorkLogic = new YHFlowWorkAdSearchLogic();
        String skin = request.getParameter("skin");
        if (skin == null) {
          skin = "";
        }
        //--- 检查表单字段条件公式的正确性 ---
        String msg = myWorkLogic.checkFormula(condFormula, sFlowId, sSortId , skin, request.getContextPath());
        if (msg != null) {
          request.setAttribute("error", msg);
          return returnUrl;
        }
        List<Map> result = myWorkLogic.doQuery(Integer.parseInt(sFlowId) , request ,  dbConn, loginUser );
        request.setAttribute("result", result);
      } catch (Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return returnUrl;
    }
    public String getExcelData(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      String runIds = request.getParameter("runIds");
      String sFlowId = request.getParameter("flowId");
      int flowId = 0 ;
      if (YHUtility.isInteger(sFlowId)) {
        flowId = Integer.parseInt(sFlowId);
      }
      YHPerson loginUser = null;
      OutputStream ops = null;
      InputStream is = null;
      Connection conn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        conn = requestDbConn.getSysDbConn();
        loginUser = (YHPerson) request.getSession().getAttribute(
            YHConst.LOGIN_USER);
        YHFlowTypeLogic logic = new YHFlowTypeLogic();
        String flowName = logic.getFlowTypeName(flowId, conn);
        String fileName =  URLEncoder.encode("流程：" + flowName + "_-_数据报表.xls","UTF-8");
        fileName = fileName.replaceAll("\\+", "%20");
        response.setHeader("Cache-control","private");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Accept-Ranges","bytes");
        response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
        ops = response.getOutputStream();
        YHFlowWorkAdSearchLogic myWorkLogic = new YHFlowWorkAdSearchLogic();
        ArrayList<YHDbRecord > dbL = myWorkLogic.getExcelData(flowId, runIds, conn,  request.getContextPath(),loginUser);
        YHJExcelUtil.writeExc(ops, dbL);
      } catch (Exception ex) {
        throw ex;
      } finally {
        ops.close();
      }
      return null;
    }
    public String flowReport(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      String returnUrl = YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrun/query/flowReport.jsp";
      String sSortId =  request.getParameter("sortId");
      if (sSortId == null) {
        sSortId = "";
      }
      String sFlowId = request.getParameter("flowId");
      String condFormula = request.getParameter("condFormula");
      String listFldsStr = request.getParameter("listFldsStr");
      
      if (listFldsStr == null) {
        listFldsStr = "";
      }
      YHPerson loginUser = null;
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        loginUser = (YHPerson) request.getSession().getAttribute(
            YHConst.LOGIN_USER);
        YHFlowWorkAdSearchLogic myWorkLogic = new YHFlowWorkAdSearchLogic();
        //--- 检查表单字段条件公式的正确性 ---
        String skin = request.getParameter("skin");
        if (skin == null) {
          skin = "";
        }
        String msg = myWorkLogic.checkFormula(condFormula, sFlowId, sSortId , skin, request.getContextPath());
        if (msg != null) {
          request.setAttribute("error", msg);
          return returnUrl;
        }
        Map result = myWorkLogic.flowReport(Integer.parseInt(sFlowId) , request ,  dbConn, loginUser  ,listFldsStr);
        Map title = (Map)result.get("title");
        List<Map> re = (List<Map>)result.get("result");
        request.setAttribute("result", re);
        request.setAttribute("title", title);
      } catch (Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return returnUrl;
    }
}
