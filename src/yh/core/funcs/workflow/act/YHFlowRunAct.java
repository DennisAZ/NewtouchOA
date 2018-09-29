package yh.core.funcs.workflow.act;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowProcess;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.logic.YHFlowProcessLogic;
import yh.core.funcs.workflow.logic.YHFlowRunAssistLogic;
import yh.core.funcs.workflow.logic.YHFlowRunLogic;
import yh.core.funcs.workflow.logic.YHFlowTypeLogic;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHFlowRunAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.workflow.act.YHFlowRunAct");
  /**
   * 取得工作流新建信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getNewMsg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowProcessLogic fpl = new YHFlowProcessLogic();
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
      String sFlowId = request.getParameter("flowId");
      int flowId = Integer.parseInt(sFlowId);
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      List<YHFlowProcess> list = fpl.getFlowProcessByFlowId(flowId , dbConn);
      YHFlowType flowType = flowTypeLogic.getFlowTypeById(flowId , dbConn);
      //如果第一步为空，以及检查出没有权限则提示          
      boolean flag = YHWorkFlowUtility.checkPriv(flowType, list, loginUser  , dbConn);
      if ( flag) {
        String message = YHWorkFlowUtility.Message("没有该流程新建权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        //取得文号,步骤列表
        //返加的json数据格式为rtData:{formId:23,flowName:'ddd',runName:'请假申请(2010-01-20 14:45:45)',prcsList:[{prcsNo:'1',prcsName:'请假申请',prcsTo:'2,'},{prcsNo:'1',prcsName:'请假申请',prcsTo:'2,'}]}
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "get Success");
          synchronized(loc) {
            request.setAttribute(YHActionKeys.RET_DATA, flowRunLogic.getNewMsg(flowType, loginUser, list , dbConn));
            dbConn.commit();
          }
        }
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  /**
   * 取得最近的工作列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRunList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      if(loginUser == null){
        String message = YHWorkFlowUtility.Message("用户未登录，请<a href='" + request.getContextPath() +"'>重新登录!</a>",2);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        String sSortId = request.getParameter("sortId");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "get Success");
        request.setAttribute(YHActionKeys.RET_DATA, "[" + flowRunLogic.getRecentlyFlowRun(loginUser , dbConn , sSortId) + "]");
      }
      
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  public final static byte[] loc = new byte[1];
  /**
   * 新建一个工作
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String createNewWork(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    String runName = null;
    if(request.getParameter("runName") != null){
      runName =  request.getParameter("runName");
    }
    String leftName = request.getParameter("runNameLeft");
    if (leftName != null && !"".equals(leftName)) {
      runName = leftName + runName;
    }
    String rightName = request.getParameter("runNameRight");
    if (rightName != null && !"".equals(rightName)) {
      runName = runName + rightName;
    }  
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowProcessLogic fpl = new YHFlowProcessLogic();
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      String sFlowId = request.getParameter("flowId");
      int flowId = Integer.parseInt(sFlowId);
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      List<YHFlowProcess> list = fpl.getFlowProcessByFlowId(flowId , dbConn);
      YHFlowType flowType = flowTypeLogic.getFlowTypeById(flowId , dbConn);
      //取得第一步      synchronized(loc) {
        boolean flag = YHWorkFlowUtility.checkPriv(flowType, list, loginUser , dbConn);
      //如果第一步为空，以及检查出没有权限则提示        if (flag) {
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程新建权限，请与OA管理员联系");
        }else{
        //查询是否为重名的
          YHFlowRunLogic frl = new YHFlowRunLogic();
          //如果没有指定runName
          if(runName == null){
            runName = frl.getRunName(flowType, loginUser , dbConn , false) ;
          }
          //重名
          if(frl.isExist(runName, flowId , dbConn)){ 
            request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
            request.setAttribute(YHActionKeys.RET_MSRG, "输入的工作名称/文号与之前的工作重复，请重新设置.");
          }else{
            int runId  = frl.createNewWork(loginUser, flowType, runName , dbConn);
            request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
            request.setAttribute(YHActionKeys.RET_MSRG, "新建成功!");
            request.setAttribute(YHActionKeys.RET_DATA, runId + "");
          }
        }
        dbConn.commit();
      }
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 新建一个工作

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String createNewDoc(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    String runName = null;
    if(request.getParameter("runName") != null){
      runName =  request.getParameter("runName");
    }
    String leftName = request.getParameter("runNameLeft");
    if (leftName != null && !"".equals(leftName)) {
      runName = leftName + runName;
    }
    String rightName = request.getParameter("runNameRight");
    if (rightName != null && !"".equals(rightName)) {
      runName = runName + rightName;
    }  
   Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowProcessLogic fpl = new YHFlowProcessLogic();
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      String sFlowId = request.getParameter("flowId");
      int flowId = Integer.parseInt(sFlowId);
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      List<YHFlowProcess> list = fpl.getFlowProcessByFlowId(flowId , dbConn);
      YHFlowType flowType = flowTypeLogic.getFlowTypeById(flowId , dbConn);
      //取得第一步

      boolean flag = YHWorkFlowUtility.checkPriv(flowType, list, loginUser , dbConn);
      //如果第一步为空，以及检查出没有权限则提示

      if ( flag ) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程新建权限，请与OA管理员联系");
      }else{
        //查询是否为重名的
        YHFlowRunLogic frl = new YHFlowRunLogic();
        //如果没有指定runName
        if(runName == null){
          runName = frl.getRunName(flowType, loginUser , dbConn , false) ;
        }
        //重名
        if(frl.isExist(runName, flowId , dbConn)){ 
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "输入的工作名称/文号与之前的工作重复，请重新设置.");
        }else{
          YHFlowRunUtility util = new YHFlowRunUtility();
          int runId = util.createNewWork(dbConn, flowId, loginUser, request.getParameterMap());
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "新建成功!");
          request.setAttribute(YHActionKeys.RET_DATA, runId + "");
        }
      }
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 新建一个工作
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String createWork(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
   String flowName = request.getParameter("flowName");
   Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowProcessLogic fpl = new YHFlowProcessLogic();
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      YHFlowRunUtility fru = new YHFlowRunUtility();
      int flowId = fru.getFlowId(dbConn, flowName);
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      List<YHFlowProcess> list = fpl.getFlowProcessByFlowId(flowId , dbConn);
      YHFlowType flowType = flowTypeLogic.getFlowTypeById(flowId , dbConn);
      //取得第一步
      boolean flag = YHWorkFlowUtility.checkPriv(flowType, list, loginUser , dbConn);
      //如果第一步为空，以及检查出没有权限则提示
      if ( flag ) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程新建权限，请与OA管理员联系");
      }else{
        //查询是否为重名的
        YHFlowRunLogic frl = new YHFlowRunLogic();
        String runName = frl.getRunName(flowType, loginUser , dbConn , false) ;
        //重名
        if(frl.isExist(runName, flowId , dbConn)){ 
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "输入的工作名称/文号与之前的工作重复，请重新设置.");
        }else{
          int runId = fru.createNewWork(dbConn, flowId, loginUser, request.getParameterMap());
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "新建成功!");
          request.setAttribute(YHActionKeys.RET_DATA, "{runId:" + runId + ",flowId:" + flowId + "}");
        }
      }
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 取得流水号
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getFlowId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
   String flowName = request.getParameter("flowName");
   Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowRunUtility fru = new YHFlowRunUtility();
      int flowId = fru.getFlowId(dbConn, flowName);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "新建成功!");
      request.setAttribute(YHActionKeys.RET_DATA, flowId + "");
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 修改流程名称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateRunName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String runName =  request.getParameter("runName");
    String sRunId = request.getParameter("runId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int runId = Integer.parseInt(sRunId);
      YHFlowRunLogic logic = new YHFlowRunLogic();
      logic.updateRunName(runName, runId, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功!");
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 添加经办人
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    String prcsIdStr = request.getParameter("prcsId");
    String flowPrcsStr = request.getParameter("flowPrcs");
    String toIdStr = request.getParameter("user");
    String sortId = request.getParameter("sortId");
    if (sortId == null) {
      sortId = "";
    }
    String skin = request.getParameter("skin");
    if (skin == null) {
      skin = "";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int runId = Integer.parseInt(runIdStr);
      int prcsId = Integer.parseInt(prcsIdStr);
      int flowId = Integer.parseInt(flowIdStr);
      int flowPrcs = 0;
      if (YHUtility.isInteger(flowPrcsStr)) {
        flowPrcs = Integer.parseInt(flowPrcsStr);
      }
      int toId = 0 ;
      if (YHUtility.isInteger(toIdStr)) {
        toId = Integer.parseInt(toIdStr);
      }
      
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser, dbConn);
      
      if (!YHWorkFlowUtility.findId(roleStr, "2")
          && !YHWorkFlowUtility.findId(roleStr, "3")
          && !loginUser.isAdminRole()) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "此用户没有此工作的经办权限");
        return "/core/inc/rtjson.jsp";
      }
      YHFlowRunUtility fru = new YHFlowRunUtility();
      boolean isExistUser = fru.isExistUser(dbConn, runId, prcsId, flowPrcs, toId);
      if (isExistUser) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "此用户已经为本步骤经办人！");
        return "/core/inc/rtjson.jsp";
      }
      YHFlowRunAssistLogic logic = new YHFlowRunAssistLogic();
      logic.addPrcsUser(dbConn, prcsId, runId, flowId, flowPrcs, toId, sortId , skin, loginUser.getSeqId(), request.getContextPath());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功!");
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
