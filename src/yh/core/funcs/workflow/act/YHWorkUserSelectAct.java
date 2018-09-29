package yh.core.funcs.workflow.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.logic.YHFlowUserSelectLogic;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHWorkUserSelectAct {
  private static Logger log = Logger
  .getLogger("yh.core.funcs.workflow.act.YHWorkUserSelectAct");
  public String getOpUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String prcsChooseStr = request.getParameter("prcsChoose");
    String userFilterStr = request.getParameter("userFilter");
    String deptIdStr = request.getParameter("deptId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int flowId = Integer.parseInt(flowIdStr);
      int userFilter = 0 ;
      if(userFilterStr != null && !"".equals(userFilterStr)){
        userFilter = Integer.parseInt(userFilterStr);
      }
      int deptId = 0 ;
      String msrg = "全部经办人";
      if(deptIdStr != null  && !"".equals(deptIdStr)){
        deptId = Integer.parseInt(deptIdStr);
        YHDeptLogic deptLogic = new YHDeptLogic();
        msrg = deptLogic.getNameById(deptId , dbConn);
      }
      int prcsChoose = Integer.parseInt(prcsChooseStr);
      YHFlowUserSelectLogic flowRunLogic = new YHFlowUserSelectLogic(userFilter);
      //取转交相关数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, msrg);
      request.setAttribute(YHActionKeys.RET_DATA, "[" + flowRunLogic.getOpUserJson(deptId , prcsChoose , flowId , loginUser , dbConn) + "]");
      
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String doSearch(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String prcsChooseStr = request.getParameter("prcsChoose");
    String userFilterStr = request.getParameter("userFilter");
    String userName = request.getParameter("userName");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int flowId = Integer.parseInt(flowIdStr);
      int userFilter = 0 ;
      if(userFilterStr != null && !"".equals(userFilterStr)){
        userFilter = Integer.parseInt(userFilterStr);
      }
      String msrg = "查询";
      int prcsChoose = Integer.parseInt(prcsChooseStr);
      YHFlowUserSelectLogic flowRunLogic = new YHFlowUserSelectLogic(userFilter);
      //取转交相关数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, msrg);
      request.setAttribute(YHActionKeys.RET_DATA, "[" + flowRunLogic.doSearch(userName , prcsChoose , flowId , loginUser , dbConn) + "]");
      
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getUserByRole(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String prcsChooseStr = request.getParameter("prcsChoose");
    String userFilterStr = request.getParameter("userFilter");
    String sRoleId = request.getParameter("roleId");
    int roleId = Integer.parseInt(sRoleId);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int flowId = Integer.parseInt(flowIdStr);
      int userFilter = 0 ;
      if(userFilterStr != null && !"".equals(userFilterStr)){
        userFilter = Integer.parseInt(userFilterStr);
      }
      String msrg = "查询";
      int prcsChoose = Integer.parseInt(prcsChooseStr);
      YHFlowUserSelectLogic flowRunLogic = new YHFlowUserSelectLogic(userFilter);
      //取转交相关数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, msrg);
      request.setAttribute(YHActionKeys.RET_DATA, "{principalRole:[" + flowRunLogic.getUserByRole(roleId, prcsChoose, flowId, user, dbConn) 
          + "],supplementRole:["
          + flowRunLogic.getUserBySupplementRole(roleId, prcsChoose, flowId, user, dbConn) + "]}");
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getGroupUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String prcsChooseStr = request.getParameter("prcsChoose");
    String userFilterStr = request.getParameter("userFilter");
    String sGroupId = request.getParameter("groupId");
    int groupId = Integer.parseInt(sGroupId);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int flowId = Integer.parseInt(flowIdStr);
      int userFilter = 0 ;
      if(userFilterStr != null && !"".equals(userFilterStr)){
        userFilter = Integer.parseInt(userFilterStr);
      }
      String msrg = "查询";
      int prcsChoose = Integer.parseInt(prcsChooseStr);
      YHFlowUserSelectLogic flowRunLogic = new YHFlowUserSelectLogic(userFilter);
      //取转交相关数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, msrg);
      request.setAttribute(YHActionKeys.RET_DATA, "[" + flowRunLogic.getGroupUser(groupId, prcsChoose, flowId, user, dbConn) + "]");
      
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 自由流程取得所有人
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUsers(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String flowIdStr = request.getParameter("flowId");
    String deptIdStr = request.getParameter("deptId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int flowId = Integer.parseInt(flowIdStr);
      
      int deptId = 0 ;
      String msrg = "全部经办人";
      if(deptIdStr != null  && !"".equals(deptIdStr)){
        deptId = Integer.parseInt(deptIdStr);
        YHDeptLogic deptLogic = new YHDeptLogic();
        msrg = deptLogic.getNameById(deptId , dbConn);
      }
      YHFlowUserSelectLogic flowRunLogic = new YHFlowUserSelectLogic();
      //取转交相关数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, msrg);
      request.setAttribute(YHActionKeys.RET_DATA, "[" + flowRunLogic.getUserJson(deptId , flowId, loginUser , dbConn) + "]");
      
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
