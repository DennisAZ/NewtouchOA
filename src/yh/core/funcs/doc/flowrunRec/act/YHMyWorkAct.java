package yh.core.funcs.doc.flowrunRec.act;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.doc.flowrunRec.logic.YHMyWorkLogic;
import yh.core.funcs.doc.logic.YHFlowProcessLogic;
import yh.core.funcs.doc.logic.YHFlowRunLogic;
import yh.core.funcs.doc.receive.data.YHDocConst;
import yh.core.funcs.doc.receive.logic.YHDocRegisterLogic;
import yh.core.funcs.doc.util.YHIWFPlugin;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHMyWorkAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.doc.flowrunRec.act.YHMyWorkAct");
  private String PLUGINPACKAGE = "yh.plugins.workflow";
  public String getMyWorkList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String showLenStr = request.getParameter("showLength");
    String flowIdStr = request.getParameter("flowId");
    String pageIndexStr = request.getParameter("pageIndex");
    String typeStr = request.getParameter("typeStr");
    String sSortId = request.getParameter("sortId");
    if (sSortId == null) {
      sSortId = "";
    }
    
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      if (YHUtility.isNullorEmpty(sSortId)) {
        String webroot = request.getRealPath("/");
        String sortName = YHDocConst.getProp(webroot  , YHDocConst.DOC_RECEIVE_FLOW_SORT) ;
        if (!YHUtility.isNullorEmpty(sortName)) {
          String sortNamesNew = "";
          String[] news = sortName.split(",");
          for (String tmp : news) {
            if (!YHUtility.isNullorEmpty(tmp)) {
              sortNamesNew += "'" + tmp + "',";
            }
          }
          if (sortNamesNew.endsWith(",")) {
            sortNamesNew = sortNamesNew.substring(0, sortNamesNew.length() - 1);
          }
          String sql = "select seq_id from "+ YHWorkFlowConst.FLOW_SORT +" where sort_name in (" + sortNamesNew + ")"; 
          Statement stm = null;
          ResultSet rs = null;
          try {
            stm = dbConn.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
              sSortId += rs.getInt("seq_id") + ",";
            }
          } catch (Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(stm , rs , null);
          }
        }
      }
      
      loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      String str =  "";
      YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
      
      String filedList = "";
      if (!YHUtility.isNullorEmpty(flowIdStr)) {
        //filedList = YHWorkFlowUtility.getOutOfTail(myWorkLogic.getFildList(dbConn, flowIdStr));
      }
      String realPath = request.getRealPath("/");
      boolean isDesk = false;
      if (!YHUtility.isNullorEmpty(request.getParameter("isDesk"))) {
        isDesk = true;
      }
      if("3".equals(typeStr)){
        String opFlag = request.getParameter("opFlag");
        str = myWorkLogic.getEndWorkList1( loginUser, Integer.parseInt(pageIndexStr),
        flowIdStr, Integer.parseInt(showLenStr),opFlag  , sSortId ,dbConn , filedList,realPath , isDesk);
      }else{
        str = myWorkLogic.getMyWorkList( loginUser, Integer.parseInt(pageIndexStr),
        flowIdStr, Integer.parseInt(showLenStr),typeStr , sSortId , dbConn , filedList,realPath , isDesk );
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,filedList);
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex) {
      if(loginUser == null){
        String contextPath = request.getContextPath();
        String message = YHWorkFlowUtility.Message("用户未登录，请重新<a href='"+contextPath+"/login.jsp'>登录</a>!",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delRun(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String runIdStr = request.getParameter("runId");
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
      myWorkLogic.delRun(Integer.parseInt(runIdStr), loginUser, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除数据!");
    } catch (Exception ex) {
      if(loginUser == null){
        String contextPath = request.getContextPath();
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "用户未登录，请重新<a href='"+contextPath+"/login.jsp'>登录</a>!");
      }else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      }
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 取消工作 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String cancelRun(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    String prcsIdStr = request.getParameter("prcsId");
    String flowPrcsStr = request.getParameter("flowPrcs");
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
      //验证是否有权限

      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
      if(!YHWorkFlowUtility.findId(roleStr, "2")){//没有权限
        String message = YHWorkFlowUtility.Message("没有该流程权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
        //取表单相关信息
        flowRunLogic.cancelRun( flowId, runId, prcsId, flowPrcs , dbConn);
        YHDocRegisterLogic logic = new YHDocRegisterLogic();
        logic.delRun(dbConn, runId);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "删除成功!");
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
      YHFlowRunLogic flowRunLogic1 = new YHFlowRunLogic();
      YHFlowProcessLogic fp = new YHFlowProcessLogic();
      YHMyWorkLogic mywork = new YHMyWorkLogic();
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
      //发送短信提醒
      mywork.remindAllAndSend(dbConn, remindFlag, remindContent, request.getContextPath(), runId, loginUser.getSeqId() , flowId);
      String imgPath = YHWorkFlowUtility.getImgPath(request);
      mywork.remaindEmail(flowId, prcsId, runId, dbConn, loginUser , imgPath , request.getContextPath());
      if(prcsChoose== null || "".equals(prcsChoose) || "0".equals(prcsChoose)|| "0,".equals(prcsChoose)){
        String prcsUser = request.getParameter("prcsUser_0");
        String prcsOpUser = request.getParameter("prcsOpUser_0");
        String topFlag = request.getParameter("topFlag_0");
        String prcsBack = request.getParameter("prcsBack");
        if (prcsBack == null) {
          prcsBack = "";
        }
        flowRunLogic1.turnEnd(loginUser, runId, flowId, prcsId, flowPrcs, prcsUser, prcsOpUser, topFlag  , request.getRemoteAddr() , dbConn , prcsBack);
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
        flowRunLogic1.turnNext(loginUser  , runId , flowId , prcsId   , flowPrcs  , prcsChoose 
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
                mywork.remindNext(dbConn,  runId ,  flowId ,  prcsId + 1,  nextFlowPrcs, remindContent, request.getContextPath(), prcsUser2 , loginUser.getSeqId() , sortId , skin);
              } else {
                int runIdNew = (Integer)opUserMap.get("nextRun_" + s);
                mywork.remindNext(dbConn,  runIdNew ,  Integer.parseInt(childFlow) ,  1,  1 , remindContent, request.getContextPath(), prcsUser2 , loginUser.getSeqId() , sortId , skin);
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
          //flowRunLogic.manageTurnNext(dbConn, runId, prcsId, flowPrcs);
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
      YHMyWorkLogic mywork = new YHMyWorkLogic();
      //取转交相关数据

      String sortId = request.getParameter("sortId");
      if (sortId == null) {
        sortId = "";
      }
      String skin = request.getParameter("skin");
      if (skin == null) {
        skin = "";
      }
      mywork.backTo(loginUser , runId , flowId, prcsId , flowPrcs , prcsIdPre, request.getRemoteAddr(), request.getContextPath() ,sortId , skin , dbConn);
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
