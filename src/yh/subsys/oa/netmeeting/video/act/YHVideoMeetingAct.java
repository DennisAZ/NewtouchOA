package yh.subsys.oa.netmeeting.video.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.netmeeting.video.data.YHVideoMeetingManager;
import yh.subsys.oa.netmeeting.video.logic.YHVideoMeetingLogic;

public class YHVideoMeetingAct {
  private YHVideoMeetingLogic logic = new YHVideoMeetingLogic();
  public static final String attachmentFolder = "VideoMeeting";

  
  public String checkUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String contexPathAll = request.getSession().getServletContext().getRealPath("/");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.checkUser(dbConn, person, contexPathAll);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);    
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 视频会议管理列表    通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getVideoMeetingList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String contexPathAll = request.getSession().getServletContext().getRealPath("/");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getMeetingList(dbConn, request.getParameterMap(), person, contexPathAll);
      if(data.equals("empty")){
        data = "{totalRecord:\"empty\",pageData:[]}";
      }
      if(data.equals("error")){
        data = "{totalRecord:\"error\",pageData:[]}";
      }
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
   * 进行中的视频会议列表    通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getVideoMeetingListInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String contexPathAll = request.getSession().getServletContext().getRealPath("/");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getVideoMeetingListInfo(request.getParameterMap(), dbConn, person, contexPathAll);
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
   * 获取启动会议Url   
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String doStartVideoMeeting(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String contexPathAll = request.getSession().getServletContext().getRealPath("/");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String confKey = request.getParameter("confKey");
      String password = request.getParameter("password");
      String data = this.logic.doStartVideoMeeting(confKey, password, dbConn, person, contexPathAll);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
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
  @SuppressWarnings("unchecked")
  public String addVideoMeeting(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Map map = request.getParameterMap();
    String contexPath = request.getContextPath();
    String contexPathAll = request.getSession().getServletContext().getRealPath("/");
    Connection dbConn = null;
    String data = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      data = this.logic.setNewVideoMeetingValueLogic(dbConn, map, person, contexPathAll);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    if(data == null)
      response.sendRedirect(contexPath + "/subsys/oa/netmeeting/video/manage/newRemind.jsp");
    else if(data.equals("empty"))
      response.sendRedirect(contexPath + "/subsys/oa/netmeeting/video/manage/check.jsp");
    else if(data.equals("error"))
      response.sendRedirect(contexPath + "/subsys/oa/netmeeting/video/manage/error.jsp");
    return null;
  }
  
  /**
   * 取消会议（删除）
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteMeeting(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String confKey = request.getParameter("confKey");
    Connection dbConn = null;
    String contexPathAll = request.getSession().getServletContext().getRealPath("/");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.deleteMeetingLogic(confKey, dbConn, person, contexPathAll);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取详情
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getVideoMeeting(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String confKey = request.getParameter("confKey");
    Connection dbConn = null;
    String contexPathAll = request.getSession().getServletContext().getRealPath("/");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getVideoMeetingInfo(confKey, dbConn, person, contexPathAll);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 编辑人事调动
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateVideoMeetingInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Map map = request.getParameterMap();
    String contexPath = request.getContextPath();
    String contexPathAll = request.getSession().getServletContext().getRealPath("/");
    Connection dbConn = null;
    String data = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      data = this.logic.updateTransferInfoLogic(dbConn, map,person, contexPathAll);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功修改数据");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    if(data == null)
      response.sendRedirect(contexPath + "/subsys/oa/netmeeting/video/manage/updateRemind.jsp");
    else if(data.equals("empty"))
      response.sendRedirect(contexPath + "/subsys/oa/netmeeting/video/manage/check.jsp");
    else if(data.equals("error"))
      response.sendRedirect(contexPath + "/subsys/oa/netmeeting/video/manage/error.jsp");
    return null;
  }
  
  
  /**
   * 获取启动会议Url   
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String doJoinVideoMeeting(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String contexPathAll = request.getSession().getServletContext().getRealPath("/");
    try {
      String confKey = request.getParameter("confKey");
      String password = request.getParameter("password");
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.doJoinVideoMeeting(confKey, password, person, contexPathAll);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String editManager(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map map = request.getParameterMap();
      YHVideoMeetingManager manager = (YHVideoMeetingManager)YHFOM.build(map);
      if(YHUtility.isNullorEmpty(manager.getSeqId()+"") || manager.getSeqId() == 0){
        this.logic.addManager(dbConn,manager);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功新增");
      }
      else{
        this.logic.updateManager(dbConn,manager);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功修改");
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getPage(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
      
      String sql = " select SEQ_ID,USER_ID,USER_NAME,RED_USERNAME from oa_conference_manager ";
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap()); 
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      PrintWriter pw = response.getWriter(); 
      pw.println(pageDataList.toJson()); 
      pw.flush(); 
      return null; 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
  }
  
  public String deleteManager(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      this.logic.deleteManager(dbConn,seqId);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 视频会议管理列表    通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryVideoMeetingList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String contexPathAll = request.getSession().getServletContext().getRealPath("/");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.queryMeetingList(dbConn, request.getParameterMap(), person, contexPathAll);
      if(data.equals("empty")){
        data = "{totalRecord:\"empty\",pageData:[]}";
      }
      if(data.equals("error")){
        data = "{totalRecord:\"error\",pageData:[]}";
      }
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
  
  public String getParameters(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String contexPathAll = request.getSession().getServletContext().getRealPath("/");
      String data = this.logic.getParameters(contexPathAll);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功修改");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String setParameters(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String contexPathAll = request.getSession().getServletContext().getRealPath("/");
      String ip = request.getParameter("ip");
      String port = request.getParameter("port");
      this.logic.setParameters(ip, port, contexPathAll);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功修改");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
