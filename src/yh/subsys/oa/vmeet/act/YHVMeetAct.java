package yh.subsys.oa.vmeet.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.vmeet.logic.YHVMeetLogic;

public class YHVMeetAct {
 private YHVMeetLogic logic=new YHVMeetLogic();
  
 /**
  * 新建视频会议
  * 
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String addVMeetInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

   String contexPath = request.getContextPath();
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
     dbConn = requestDbConn.getSysDbConn();
     String inviteUsers=request.getParameter("toId");
     String content=request.getParameter("content");
     this.logic.addVMeetInfoLogic(dbConn, person,inviteUsers,content);
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
   } catch (Exception ex) {
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
  
   return "/core/inc/rtjson.jsp";              
 } 
 
 /**
  * 新建视频会议
  * 
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String editUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
     dbConn = requestDbConn.getSysDbConn();
     String inviteUsers=request.getParameter("toId");
     String seqId=request.getParameter("seqId");
     String content=request.getParameter("content");
     this.logic.editUsersLogic(dbConn, person,inviteUsers,content,seqId);
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
   } catch (Exception ex) {
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
  
   return "/core/inc/rtjson.jsp";              
 } 
 
 
 /**
  * 新建视频会议
  * 
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String setVMeetPriv(HttpServletRequest request, HttpServletResponse response) throws Exception {

   String contexPath = request.getContextPath();
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
     String toIds=request.getParameter("toId");
   
     this.logic.setVMeetPrivLogic(dbConn,toIds);
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
   } catch (Exception ex) {
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
  
   return "/core/inc/rtjson.jsp";              
 } 
 
 
 
 
 
 /**
  * 获取视频会议的权限
  * 
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String getVMeetPriv(HttpServletRequest request, HttpServletResponse response) throws Exception {

   String contexPath = request.getContextPath();
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
     dbConn = requestDbConn.getSysDbConn();
     String inviteUsers=request.getParameter("inviteusers");
     String content=request.getParameter("content");
     String data=this.logic.getVMeetPriv(dbConn,person);
  
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
     request.setAttribute(YHActionKeys.RET_DATA,data);
   } catch (Exception ex) {
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
   return "/core/inc/rtjson.jsp";                             
 } 
 
 public String getLastBeginMeetAct(HttpServletRequest request, HttpServletResponse response) throws Exception {

   String contexPath = request.getContextPath();
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
     dbConn = requestDbConn.getSysDbConn();
     String data=this.logic.getLastBeginMeet(dbConn, person);
     data="["+data+"]";
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
     request.setAttribute(YHActionKeys.RET_DATA,data);
   } catch (Exception ex) {
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
   return "/core/inc/rtjson.jsp";                             
 } 
 
 public String deleteVMeetAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
     String seqId=request.getParameter("seqId");
     this.logic.deleteVMeet(dbConn, seqId);
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
   } catch (Exception ex) {
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
   return "/core/inc/rtjson.jsp";                             
 } 
 
 
 
 
 public String getLastInvitedMeetAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
     dbConn = requestDbConn.getSysDbConn();
     String data=this.logic.getLastInvitedMeet(dbConn, person);
     data="["+data+"]";
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
     request.setAttribute(YHActionKeys.RET_DATA,data);
   } catch (Exception ex) {
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
   return "/core/inc/rtjson.jsp";                             
 } 
 
 public String getVMeetByIdAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
     dbConn = requestDbConn.getSysDbConn();
     String seqId=request.getParameter("seqId");
     String data=this.logic.getVMeetByIdLogic(dbConn, person,seqId);
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
     request.setAttribute(YHActionKeys.RET_DATA,data);
   } catch (Exception ex) {
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
   return "/core/inc/rtjson.jsp";                             
 } 
 
}
