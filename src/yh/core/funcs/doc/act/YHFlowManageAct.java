package yh.core.funcs.doc.act;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.logic.YHFlowManageLogic;
import yh.core.funcs.doc.logic.YHFlowTypeLogic;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHFlowManageAct {
  private static Logger log = Logger
      .getLogger("yh.core.funcs.doc.act.YHFlowManageAct");


  public String setPriv(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String act = request.getParameter("action");
    int flowId = Integer.parseInt(request.getParameter("flowId"));
    Connection dbConn = null;
    try {
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowManageLogic flowManage = new YHFlowManageLogic();
      if ("COMMENT".equals(act)) {
        String commentPriv1 = request.getParameter("commentPriv1");
        String commentPriv2 = request.getParameter("commentPriv2");
        int priv = 0;
        if (commentPriv1 == null && commentPriv2 != null) {
          priv = 2;
        } else if (commentPriv1 != null && commentPriv2 == null) {
          priv = 1;
        } else if (commentPriv2 != null && commentPriv1 != null) {
          priv = 3;
        }
        flowManage.setCommentPriv(flowId, priv, dbConn);
      } else {
        String privUser = request.getParameter("privUser");
        if (privUser == null) {
          privUser = "";
        }
        String privDept = request.getParameter("privDept");
        if (privDept == null) {
          privDept = "";
        }
        String role = request.getParameter("role");
        if (role == null) {
          role = "";
        }
        String privStr = privUser + "|" + privDept + "|" + role;

        flowManage.setPriv(flowId, act, privStr, dbConn);
      }
      this.setRequestSuccess(request, "设置成功 ");
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String getPriv(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    int flowId = Integer.parseInt(request.getParameter("flowId"));
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowManageLogic flowManage = new YHFlowManageLogic();
      String data = flowManage.getPriv(flowId, dbConn);
      this.setRequestSuccess(request, "取得成功 ", data);
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 取得超时催办提醒页面的相关信息
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRemindInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String toId = request.getParameter("toId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPersonLogic logic = new YHPersonLogic();
      String data = "{user:[" + logic.getPersonSimpleJson(toId, dbConn) + "]";
      YHFlowManageLogic mLogic = new YHFlowManageLogic();
      String query = "select TYPE_PRIV from oa_msg2_priv";
      String typePriv = "";
      String sms2RemindPriv = "";
      Statement stm1 = null;
      ResultSet rs1 = null;
      try {
        stm1 = dbConn.createStatement();
        rs1 = stm1.executeQuery(query);
        if (rs1.next()) {
          typePriv = rs1.getString("TYPE_PRIV");
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm1, rs1, null);
      }
      // 检查该模块是否允许手机提醒
      boolean sms2Priv = false;
      if (YHWorkFlowUtility.findId(typePriv, "7")) {
        sms2Priv = true;
      }

      data += ",sms2Priv:" + sms2Priv;
      data += "}";
      this.setRequestSuccess(request, "取得成功 ", data);
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String remindUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String toId = request.getParameter("toId");
    String sms2Remind = request.getParameter("sms2Remind");
    String content = request.getParameter("content");
    String sortId = request.getParameter("sortId");
    if (sortId == null) {
      sortId = "";
    }
    String skin = request.getParameter("skin");
    if (skin == null) {
      skin = "";
    }
    String flag = request.getParameter("flag");
    if (flag == null) {
      flag = "";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHFlowManageLogic mLogic = new YHFlowManageLogic();
      mLogic.remindUser(dbConn, toId, content, request.getContextPath(),
          loginUser.getSeqId(), sortId, skin , flag);
      if ("on".equals(sms2Remind)) {
        YHMobileSms2Logic ms2l = new YHMobileSms2Logic();
        ms2l.remindByMobileSms(dbConn, toId, loginUser.getSeqId(), content,
            null);
      }
      this.setRequestSuccess(request, "催办超时流程短信已发送 ");
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 强制结束工作流
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String endWorkFlow(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String runIdStr = request.getParameter("runIdStr");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String s = "";
      String sIsManage = request.getParameter("isManage");
      boolean isManage = false;
      if (sIsManage != null || "".equals(sIsManage)) {
        isManage = Boolean.valueOf(sIsManage);
      }
      if (runIdStr != null) {
        YHPrcsRoleUtility ru = new YHPrcsRoleUtility();
        YHFlowManageLogic manage = new YHFlowManageLogic();
        String[] runIds = runIdStr.split(",");
        for (int i = 0; i < runIds.length; i++) {
          String tmp = runIds[i];

          if (!"".equals(tmp)) {
            int runId = Integer.parseInt(tmp);
            String runRole = ru.runRole(runId, loginUser, dbConn);
            if (!loginUser.isAdminRole()
                && !YHWorkFlowUtility.findId(runRole, "3")) {
              continue;
            } else {
              manage.endWorkFlow(runId, loginUser, dbConn);
              s += runId + ",";
            }
          }
        }
      }
      if (s.endsWith(",")) {
        s = s.substring(0, s.length() - 1);
      }
      this.setRequestSuccess(request, "结束流水号为[" + s + "]的工作,操作成功！");
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 删除工作流
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delWorkFlow(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String runIdStr = request.getParameter("runIdStr");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String s = "";
      if (runIdStr != null) {
        YHPrcsRoleUtility ru = new YHPrcsRoleUtility();
        YHFlowManageLogic manage = new YHFlowManageLogic();
        String[] runIds = runIdStr.split(",");
        for (String tmp : runIds) {
          if (!"".equals(tmp)) {
            int runId = Integer.parseInt(tmp);
            String runRole = ru.runRole(runId, 1, loginUser, dbConn);
            boolean flag = manage.getFlag(runId, dbConn);
            if (!(YHWorkFlowUtility.findId(runRole, "2") && flag)// 不是发起人（第一步的主办人）或者已经开始流转（不只有第一步）
                && !YHWorkFlowUtility.findId(runRole, "1") // 当前用户不是系统管理员
                && !YHWorkFlowUtility.findId(runRole, "3")) { // 当前用户不是管理与监控人员
              continue;
            } else {
              boolean result = manage.delWorkFlow(runId, loginUser.getSeqId(),
                  dbConn);
              if (result) {
                s += runId + ",";
              }
            }
          }
        }
      }
      if (s.endsWith(",")) {
        s = s.substring(0, s.length() - 1);
      }
      this.setRequestSuccess(request, "删除流水号为[" + s + "]的工作,操作成功！");
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 恢复执行
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String restore(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String runId = request.getParameter("runId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHFlowManageLogic manage = new YHFlowManageLogic();
      if (!loginUser.isAdminRole()) {
        this.setRequestSuccess(request, "没有此操作权限！");
        return "/core/inc/rtjson.jsp";
      }
      boolean reslut = manage.restore(Integer.parseInt(runId), loginUser
          .getSeqId(), dbConn);
      if (!reslut) {
        this.setRequestSuccess(request, "您的恢复执行操作没有成功!");
      } else {
        this.setRequestSuccess(request, "流水号为[" + runId + "]的工作已经恢复到执行状态!");
      }
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 取得评论信息
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCommentMsg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String sRunId = request.getParameter("runId");
    String sFlowId = request.getParameter("flowId");
    int flowId = Integer.parseInt(sFlowId);
    int runId = Integer.parseInt(sRunId);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHFlowManageLogic manage = new YHFlowManageLogic();
      StringBuffer sb = new StringBuffer();
      sb.append("{");
      sb.append(manage.getCommentPriv(flowId, loginUser, dbConn));
      sb.append(",prcsId:" + manage.getMaxPrcsId(runId, dbConn) + ",");
      sb.append(manage.getSmsRemind(loginUser.getSeqId(), dbConn));
      sb.append("}");
      this.setRequestSuccess(request, "取得成功！", sb.toString());
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String saveComment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String sRunId = request.getParameter("runId");
    String sFlowId = request.getParameter("flowId");
    String sPrcsId = request.getParameter("prcsId");
    String content = request.getParameter("comment");
    String smsRemind = request.getParameter("smsRemind");
    int prcsId = Integer.parseInt(sPrcsId);
    int runId = Integer.parseInt(sRunId);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson u = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHFlowManageLogic manage = new YHFlowManageLogic();
      manage.saveComment(runId, prcsId, u.getSeqId(), u.getUserName(), content,
          smsRemind, request.getContextPath(), dbConn);
      this.setRequestSuccess(request, "操作成功！");
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String focus(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String sRunId = request.getParameter("runId");
    int runId = Integer.parseInt(sRunId);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson u = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHFlowManageLogic manage = new YHFlowManageLogic();
      String focusUser = manage.getFocusUser(runId, dbConn);
      if (YHWorkFlowUtility.findId(focusUser, String.valueOf(u.getSeqId()))) {
        this.setRequestSuccess(request, "您已经关注了此工作！");
        return "/core/inc/rtjson.jsp";
      } else {
        manage.focus(u, focusUser, runId, request.getContextPath(), dbConn);
      }
      this.setRequestSuccess(request, "操作成功！");
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String calFocus(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String sRunId = request.getParameter("runId");
    int runId = Integer.parseInt(sRunId);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson u = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHFlowManageLogic manage = new YHFlowManageLogic();
      String focusUser = manage.getFocusUser(runId, dbConn);
      if (!YHWorkFlowUtility.findId(focusUser, String.valueOf(u.getSeqId()))) {
        this.setRequestSuccess(request, "您没有关注此工作！");
        return "/core/inc/rtjson.jsp";
      } else {
        manage.calFocus(focusUser, u.getSeqId(), runId, dbConn);
      }
      this.setRequestSuccess(request, "操作成功！");
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String getFlowTypeJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    List<YHDocFlowType> typeList = new ArrayList();
    YHDocFlowType flowType = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sSortId = request.getParameter("sortId");
      StringBuffer sb = new StringBuffer("[");
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      if (!YHUtility.isNullorEmpty(sSortId)) {
        typeList = flowTypeLogic.getFlowTypeList(sSortId, dbConn);
      } else {
        typeList = flowTypeLogic.getFlowTypeList(dbConn);
      }

      int count = 0;
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);

      for (int i = 0; i < typeList.size(); i++) {
        flowType = typeList.get(i);
        boolean canShow = true;

        YHPrcsRoleUtility pu = new YHPrcsRoleUtility();
        String manageUser = flowType.getManageUser();
        if (manageUser == null) {
          manageUser = "";
        }
        String manageUserDept = flowType.getManageUserDept();
        if (manageUserDept == null) {
          manageUserDept = "";
        }
        boolean mUserPriv = pu.checkPriv(loginUser, flowType.getManageUser());
        boolean mUserDeptPriv = pu.checkPriv(loginUser, manageUserDept);
        if (!(mUserPriv || mUserDeptPriv)) {
          canShow = false;
        }
        if (canShow) {
          sb.append("{");
          sb.append("seqId:\"" + flowType.getSeqId() + "\"");
          sb.append(",flowName:\"" + flowType.getFlowName() + "\"");
          sb.append("},");
          count++;
        }
      }
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出流程分类数据！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String getQueryItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowId = request.getParameter("flowId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      String str = flowTypeLogic.getQueryItem(flowId, dbConn);
      request.setAttribute(YHActionKeys.RET_DATA, str);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出流程分类数据！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String setQueryItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowId = request.getParameter("flowId");
    String queryItem = request.getParameter("fldStr");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      flowTypeLogic.setQueryItem(flowId, queryItem, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "更新成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 设置错误信息
   * 
   * @param request
   * @param message
   */
  public void setRequestError(HttpServletRequest request, String message) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
  }

  /**
   * 设置成功信息
   * 
   * @param request
   * @param message
   */
  public void setRequestSuccess(HttpServletRequest request, String message) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
  }

  /**
   * 设置成功信息
   * 
   * @param request
   * @param message
   * @param data
   */
  public void setRequestSuccess(HttpServletRequest request, String message,
      String data) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
    request.setAttribute(YHActionKeys.RET_DATA, data);
  }

}