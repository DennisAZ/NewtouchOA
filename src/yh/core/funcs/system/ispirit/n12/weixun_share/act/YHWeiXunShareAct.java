package yh.core.funcs.system.ispirit.n12.weixun_share.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.ispirit.n12.file.logic.YHImOffLineLogic;
import yh.core.funcs.system.ispirit.n12.group.logic.YHImGroupLogic;
import yh.core.funcs.system.ispirit.n12.weixun_share.data.YHWeixunShare;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;

public class YHWeiXunShareAct {

  /**
   * 添加微讯分享
   * 
   **/
  public String addWeiXun(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
   try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String TopPic = request.getParameter("topic");
      String mentionedIds= request.getParameter("mentionedIds");
      String content = request.getParameter("content");
      String broadcastIds = request.getParameter("broadcastIds");

      YHWeixunShare ws = new  YHWeixunShare();
      ws.setUserId(person.getSeqId());
      ws.setTopics(YHUtility.null2Empty(TopPic));
      ws.setMentionedIds(YHUtility.null2Empty(mentionedIds));
      ws.setContent(YHUtility.null2Empty(content));
      ws.setBroadcastIds(YHUtility.null2Empty(broadcastIds));
      Date date = new Date();
      ws.setAddTime(YHUtility.getCurDateTimeStr());
      
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, ws);
      
      person.setMyStatus(content);
      orm.updateSingle(dbConn, person);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
  //    request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
   return "/core/inc/rtjson.jsp";
   }
  
  /*
   *设置用户状态
   **/
  public String setUserStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
   try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      String content = request.getParameter("content");
       
      YHORM orm =new YHORM();
      person.setOnStatus(content);
      orm.updateSingle(dbConn, person);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
  //    request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
   return "/core/inc/rtjson.jsp";
   }
  
  
  /*
   *设置用户状态
   **/
  public String getUserStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
   try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      String uId = request.getParameter("uId");
      String deptId = request.getParameter("deptId");
       
      YHORM orm =new YHORM();
      person = (YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, Integer.parseInt(uId)); 
      String returnStr=person.getOnStatus();
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/html;charset=UTF-8");
      response.setHeader("Cache-Control","private");
      //response.setHeader("Accept-Ranges","bytes");
      PrintWriter out = response.getWriter();

      out.print(returnStr);
      out.flush();
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
   return "/core/inc/rtjson.jsp";
   }
  
}
