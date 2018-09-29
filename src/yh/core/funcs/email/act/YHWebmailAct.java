package yh.core.funcs.email.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.data.YHWebmailBody;
import yh.core.funcs.email.logic.YHWebmailLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

/**
 * 
 * @author tulaike
 *
 */
public class YHWebmailAct {
	private Logger log = Logger.getLogger(YHWebmailAct.class);
	
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setWebmailInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHWebmailLogic wbl = new YHWebmailLogic();
      String seqId = wbl.setWebmail(dbConn, request.getParameterMap(), person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\""+seqId+"\"");
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateWebmailInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    try{
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHWebmailLogic wbl = new YHWebmailLogic();
      wbl.updateWebmail(dbConn, request.getParameterMap(),person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String listWebmailInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    try{
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHWebmailLogic wbl = new YHWebmailLogic();
      String data = wbl.listWebmail(dbConn, person.getSeqId());
      log.debug(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String loadWebmailInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    String seqId = request.getParameter("seqId");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHWebmailLogic wbl = new YHWebmailLogic();
      String data = wbl.getWebmail(dbConn, Integer.valueOf(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 删除邮箱配置
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deletWebmailInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    String seqId = request.getParameter("seqId");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHWebmailLogic wbl = new YHWebmailLogic();
      wbl.deletWebmail(dbConn, Integer.valueOf(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"邮箱配置删除成功!");
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String hasLagerAttachment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    String bodyId = request.getParameter("bodyId");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHWebmailLogic wbl = new YHWebmailLogic();
      String data = wbl.hasLagerAttachment(dbConn, Integer.valueOf(bodyId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String refreshLagerAttachment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    String bodyId = request.getParameter("bodyId");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHWebmailLogic wbl = new YHWebmailLogic();
      YHWebmailBody wmb =  null;
      
      String attachmentId = "";
      String attachmentName = "";
      String refreshState = "0";
      try {
        wmb =  wbl.refreshLagerAttachmentMail(dbConn, Integer.valueOf(bodyId));
        attachmentId = wmb.getAttachmentId();
        attachmentName = wmb.getAttachmentName();
      } catch (Exception e) {
        refreshState = "1";
      }
      
      String data = "{bodyId:\"" + bodyId + "\",refreshState:\"" + refreshState + "\",attachmentId:\"" + attachmentId + "\",attachmentName:\"" + attachmentName + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
}
