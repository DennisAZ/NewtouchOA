package yh.core.funcs.workflow.act;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.logic.YHFlowProcessLogic;
import yh.core.funcs.workflow.logic.YHFlowRunLogic;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHWorkTurnAct {
  private static Logger log = Logger
  .getLogger("yh.core.funcs.workflow.act.YHWorkTurnAct");
  private String PLUGINPACKAGE = "yh.plugins.workflow";
  public String getTurnData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    String prcsIdStr = request.getParameter("prcsId");
    String flowPrcsStr = request.getParameter("flowPrcs");
    String sIsManage = request.getParameter("isManage");
    boolean isManage = false;
    if (sIsManage != null || "".equals(sIsManage)) {
      isManage = Boolean.valueOf(sIsManage);
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
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser, dbConn);
      if("".equals(roleStr) && !isManage){//没有权限
        String message = YHWorkFlowUtility.Message("没有该流程办理权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
        //取转交相关数据        String msg = flowRunLogic.getTurnData(loginUser , runId , prcsId , flowPrcsStr ,dbConn , isManage);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "get success");
        request.setAttribute(YHActionKeys.RET_DATA, msg);
      }
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String turnNext(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
	  
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    String prcsIdStr = request.getParameter("prcsId");
    String flowPrcsStr = request.getParameter("flowPrcs");
    String prcsChoose = request.getParameter("prcsChoose");
    String remindContent = request.getParameter("smsContent");
    String sIsManage = request.getParameter("isManage");
    boolean isManage = false;
    /*ljs srm 修改sIsManage判断*/
    if (sIsManage != null || !"".equals(sIsManage)) {
      isManage = Boolean.valueOf(sIsManage);
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
      int flowPrcs = Integer.parseInt(flowPrcsStr);
      String sSmsRemindNext = request.getParameter("smsRemindNext");
      String sWebMailRemindNext = request.getParameter("webMailRemindNext");
      String sSmsRemindStart = request.getParameter("smsRemindStart");
      String sWebMailRemindStart = request
          .getParameter("webMailRemindStart");
      String sSmsRemindAll = request.getParameter("smsRemindAll");
      String sWebMailRemindAll = request.getParameter("webMailRemindAll");
      String sSms2RemindAll = request.getParameter("sms2RemindAll");
      String sSms2RemindNext = request.getParameter("sms2RemindNext");
      String sSms2RemindStart = request.getParameter("sms2RemindStart");
      int remindFlag = YHWorkFlowUtility.getRemindFlag( sSmsRemindNext ,  sSms2RemindNext ,  sWebMailRemindNext
          ,  sSmsRemindStart,  sSms2RemindStart,  sWebMailRemindStart 
          ,  sSmsRemindAll ,  sSms2RemindAll ,  sWebMailRemindAll);
      //YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      //String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
      YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
      YHFlowProcessLogic fp = new YHFlowProcessLogic();
      //固定流程
      String pluginName =  fp.getPluginStr(flowPrcs, flowId, dbConn);
      YHIWFPlugin  pluginObj = null;
      if (pluginName != null
          && !"".equals(pluginName)) {
        String className = PLUGINPACKAGE + "." + pluginName;
        try{
          pluginObj = (YHIWFPlugin) Class.forName(className).newInstance();
        }catch(ClassNotFoundException ex){
        }
      }
      if (pluginObj != null) {
        String str = pluginObj.before(request, response);
        if (str != null) {
          request.setAttribute(YHActionKeys.RET_STATE, "3");
          request.setAttribute(YHActionKeys.RET_MSRG, str);
          return "/core/inc/rtjson.jsp";
        }
      }
      String sortId = request.getParameter("sortId");
      if (sortId == null) {
        sortId = "";
      }
      String skin = request.getParameter("skin");
      if (skin == null) {
        skin = "";
      }
      //结束流程
      //发送短信提醒      flowRunLogic.remindAllAndSend(dbConn, remindFlag, remindContent, request.getContextPath(), runId, loginUser.getSeqId() , flowId);
      String imgPath = YHWorkFlowUtility.getImgPath(request);
      flowRunLogic.remaindEmail(flowId, prcsId, runId, dbConn, loginUser , imgPath , request.getContextPath());
      if(prcsChoose== null || "".equals(prcsChoose) || "0".equals(prcsChoose)|| "0,".equals(prcsChoose)){
        String prcsUser = request.getParameter("prcsUser_0");
        String prcsOpUser = request.getParameter("prcsOpUser_0");
        String topFlag = request.getParameter("topFlag_0");
        String prcsBack = request.getParameter("prcsBack");
        if (prcsBack == null) {
          prcsBack = "";
        }
        flowRunLogic.turnEnd(loginUser, runId, flowId, prcsId, flowPrcs, prcsUser, prcsOpUser, topFlag  , request.getRemoteAddr() , dbConn , prcsBack);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "办理完毕!");
      }else{
        Map opUserMap = new HashMap();
        String[] aStr = prcsChoose.split(",");
        for(int i = 0 ;i < aStr.length ; i++){
          String prcsUser = request.getParameter("prcsUser_" + aStr[i]);
          String prcsOpUser = request.getParameter("prcsOpUser_" + aStr[i]);
          String topFlag = request.getParameter("topFlag_" + aStr[i]);
          opUserMap.put("prcsOpUser_" + aStr[i], prcsOpUser);
          opUserMap.put("prcsUser_" + aStr[i], prcsUser);
          opUserMap.put("topFlag_" + aStr[i], topFlag);
        }
        flowRunLogic.turnNext(loginUser  , runId , flowId , prcsId   , flowPrcs  , prcsChoose 
            , opUserMap   , request.getRemoteAddr() , dbConn);
        String[] ss = prcsChoose.split(",");
        for (int i = 0 ;i < ss.length ;i++) {
          String s = ss[i];
          if (!"".equals(s) && YHUtility.isInteger(s)) {
            int nextFlowPrcs = Integer.parseInt(s);
            //短信提醒下一步经办人
            //为什么不用上面的prcsUser?因为下面这个有可能是委托之后的用户．．．
            String prcsUser2 = (String)opUserMap.get("prcsUser_" + s);
            if ((remindFlag&0x100)>0) {
              String childFlow = (String)opUserMap.get("nextFlow_" + s);
              if (childFlow == null) {
                flowRunLogic.remindNext(dbConn,  runId ,  flowId ,  prcsId + 1,  nextFlowPrcs, remindContent, request.getContextPath(), prcsUser2 , loginUser.getSeqId() , sortId , skin);
              } else {
                int runIdNew = (Integer)opUserMap.get("nextRun_" + s);
                flowRunLogic.remindNext(dbConn,  runIdNew ,  Integer.parseInt(childFlow) ,  1,  1 , remindContent, request.getContextPath(), prcsUser2 , loginUser.getSeqId() , sortId , skin);
              }
              
            }
            if ((remindFlag&0x40)>0 ) {
              
            }
            if ((remindFlag&0x80)>0) {
              YHMobileSms2Logic ms2l = new YHMobileSms2Logic(); 
              ms2l.remindByMobileSms(dbConn, prcsUser2 , loginUser.getSeqId(), remindContent, null);
            }
          }
        }
        if (isManage) {
          flowRunLogic.manageTurnNext(dbConn, runId, prcsId, flowPrcs);
        }
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功转交!");
      }
      if (pluginObj != null) {
        pluginObj.after(request, response);
      }
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String backTo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    String prcsIdStr = request.getParameter("prcsId");
    String flowPrcsStr = request.getParameter("flowPrcs");
    String prcsIdPre = request.getParameter("prcsIdPre");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int runId = Integer.parseInt(runIdStr);
      int prcsId = Integer.parseInt(prcsIdStr);
      int flowId = Integer.parseInt(flowIdStr);
      int flowPrcs = Integer.parseInt(flowPrcsStr);
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      //String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
      YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
      //取转交相关数据
      String sortId = request.getParameter("sortId");
      if (sortId == null) {
        sortId = "";
      }
      String skin = request.getParameter("skin");
      if (skin == null) {
        skin = "";
      }
      String content = request.getParameter("content");
      if (!YHUtility.isNullorEmpty(content)) {
        Timestamp time =  new  Timestamp(new Date().getTime());
        String insert = "INSERT INTO oa_fl_run_feedback (RUN_ID,PRCS_ID,FLOW_PRCS,USER_ID,CONTENT,ATTACHMENT_ID,ATTACHMENT_NAME,EDIT_TIME,SIGN_DATA) VALUES ('"+runId+"','"+prcsId+"','"+flowPrcs+"','"+loginUser.getSeqId()+"',?,'','',?,'')";
        PreparedStatement stm5 = null;
        try {
          stm5 = dbConn.prepareStatement(insert);
          stm5.setString(1, content);
          stm5.setTimestamp(2, time);
          stm5.executeUpdate();
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm5, null, null); 
        }
      }
      flowRunLogic.backTo(loginUser , runId , flowId, prcsId , flowPrcs , prcsIdPre, request.getRemoteAddr(), request.getContextPath() ,sortId , skin , dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "回退成功!");
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
