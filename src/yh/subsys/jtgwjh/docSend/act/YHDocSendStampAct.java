package yh.subsys.jtgwjh.docSend.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendInfo;
import yh.subsys.jtgwjh.docSend.logic.YHDocSendStampLogic;
import yh.subsys.jtgwjh.sealmanage.data.YHJhSeal;
import yh.subsys.jtgwjh.sealmanage.logic.YHJhSealLogLogic;
import yh.subsys.jtgwjh.sealmanage.logic.YHJhSealLogic;

public class YHDocSendStampAct {

  /**
   * 主办待盖章列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDocSendStampLogic logic = new YHDocSendStampLogic();
      String data = logic.getJsonLogic(dbConn, request.getParameterMap(), person);
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
   * 主办已盖章列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getListOverJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDocSendStampLogic logic = new YHDocSendStampLogic();
      String data = logic.getOverJsonLogic(dbConn, request.getParameterMap(), person);
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
   * 协办待盖章列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getListJsonOther(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDocSendStampLogic logic = new YHDocSendStampLogic();
      String data = logic.getJsonOtherLogic(dbConn, request.getParameterMap(), person);
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
   * 协办已盖章列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getListJsonOtherOver(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDocSendStampLogic logic = new YHDocSendStampLogic();
      String data = logic.getJsonOtherOverLogic(dbConn, request.getParameterMap(), person);
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
   * 主办、协办盖章
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String mainStamps(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    String sealIds = request.getParameter("sealIds") == null ? "" : request.getParameter("sealIds") ;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDocSendStampLogic logic = new YHDocSendStampLogic();
      logic.mainStamps(dbConn, seqId, person, request);
      
      //盖章日志 syl
      String[] sealIdArr = sealIds.split(",");
      for (int i = 0; i < sealIdArr.length; i++) {
        if(!YHUtility.isNullorEmpty(sealIdArr[i])){
          String[] str ={}; //{"SEAL_ID='" + sealIdArr[i]+ "'" };
          List<YHJhSeal> list = YHJhSealLogic.selectSeal( dbConn, str) ;
          if(list.size() > 0){
            YHJhSeal seal = list.get(0);
            YHJhSealLogLogic.addSealInfoLog(dbConn, person, seal, "addseal", request.getRemoteAddr(), "确认盖章成功！");
          }
          
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 添加协办盖章人员
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String otherStamps(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    String otherStamps = request.getParameter("otherStamps");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      YHDocSendStampLogic logic = new YHDocSendStampLogic();
      logic.otherStamps(dbConn, seqId, otherStamps, person, request.getRemoteAddr());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 激活盖章状态
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String startStamp(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDocSendStampLogic logic = new YHDocSendStampLogic();
      YHJhDocsendInfo docsendInfo = logic.startStamp(dbConn, seqId, person, request, true);
      
      //系统日志
      YHSysLogLogic.addSysLog(dbConn, "60", "将公文标题为‘"+docsendInfo.getDocTitle()+"’的公文激活盖章状态。" ,person.getSeqId(), request.getRemoteAddr());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
}
