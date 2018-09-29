package yh.core.funcs.doc.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.doc.logic.YHFlowProcessLogic;
import yh.core.funcs.doc.logic.YHFlowRunLogic;
import yh.core.funcs.doc.send.logic.YHDocSendLogic;
import yh.core.funcs.doc.util.YHIWFPlugin;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;

public class YHWorkTurnAct {
  private static Logger log = Logger
  .getLogger("yh.core.funcs.doc.act.YHWorkTurnAct");
  private String PLUGINPACKAGE = "yh.plugins.doc";
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
        request.setAttribute(YHActionKeys.RET_MSRG, "");
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
      int flowPrcs = Integer.parseInt(flowPrcsStr);
      YHDocSendLogic logic = new YHDocSendLogic();
      
      if (logic.hasSend(dbConn, runId, flowId, flowPrcs)) {
        request.setAttribute(YHActionKeys.RET_STATE, "3");
        request.setAttribute(YHActionKeys.RET_MSRG, "公文还未发送不能转交，请先发送公文！");
        return "/core/inc/rtjson.jsp";
      }
      if (logic.hasRoll(dbConn, runId, flowId, flowPrcs)) {
        request.setAttribute(YHActionKeys.RET_STATE, "3");
        request.setAttribute(YHActionKeys.RET_MSRG, "公文还未归档，请先归档！");
        return "/core/inc/rtjson.jsp";
      }
      
      
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
    String content = request.getParameter("content");
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
      //YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
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
  /**
   * 更新/保存文件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding(YHConst.DEFAULT_CODE);
    response.setCharacterEncoding(YHConst.DEFAULT_CODE);
    try {
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      String attachmentName = fileForm.getParameter("docName");
      Iterator<String> iKeys = fileForm.iterateFileFields();
      String[] tmp  = {"",""};
      if (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String module = fileForm.getParameter("module");
        YHWorkFlowUtility util = new YHWorkFlowUtility();
         tmp = util.getNewAttachPath(attachmentName, module);
        fileForm.saveFile(fieldName, tmp[1]);
      }
      response.setContentType("text/html");
      PrintWriter pw = response.getWriter();
      pw.print(tmp[0]);
    } catch (Exception ex) {
      throw ex;
    } finally {
      
    }
    return null;
  }
}
