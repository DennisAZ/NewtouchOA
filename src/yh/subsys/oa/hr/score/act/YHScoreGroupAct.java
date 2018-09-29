package yh.subsys.oa.hr.score.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.score.data.YHScoreGroup;
import yh.subsys.oa.hr.score.logic.YHScoreGroupLogic;

public class YHScoreGroupAct {
  public static final String attachmentFolder = "scoreGroup";
  private YHScoreGroupLogic logic = new YHScoreGroupLogic();
  
  /**
   * 新建考核指标集--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addScoreGroup(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
     
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map<String,String[]> map = request.getParameterMap();
      String diaryId = request.getParameter("diaryId");
      String calendarId = request.getParameter("calendarId");
      String userPriv = request.getParameter("role");
      String groupRefer = "";
      YHScoreGroup scoreGroup = (YHScoreGroup) YHFOM.build(map, YHScoreGroup.class, "");
      if(!YHUtility.isNullorEmpty(diaryId)){
        groupRefer = diaryId + ",";
      }
      if(!YHUtility.isNullorEmpty(calendarId)){
        groupRefer += calendarId + ",";
      }
      
      scoreGroup.setGroupRefer(groupRefer);
      scoreGroup.setUserPriv(userPriv);
      this.logic.addScoreGroup(dbConn, scoreGroup);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 考核指标集管理列表--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getScoreGroupList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = this.logic.getScoreGroupList(dbConn, request.getParameterMap());
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
   * 删除一条考核指标集记录--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteSingle(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      this.logic.deleteSingle(dbConn, Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取考核指标集管理一条记录 --cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getScoreGroupDetail(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      if(YHUtility.isNullorEmpty(seqId)){
        seqId = "0";
      }
      YHScoreGroup paper = (YHScoreGroup)this.logic.getScoreGroupDetail(dbConn, Integer.parseInt(seqId));
      if (paper == null){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
        request.setAttribute(YHActionKeys.RET_MSRG, "该考核指标集不存在");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data = YHFOM.toJson(paper);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功"); 
      request.setAttribute(YHActionKeys.RET_DATA, data.toString()); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 编辑考核指标集管理 --cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateScoreGroup(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map<String,String[]> map = request.getParameterMap();
      String diaryId = request.getParameter("diaryId");
      String calendarId = request.getParameter("calendarId");
      String userPriv = request.getParameter("role");
      String groupRefer = "";
      if(!YHUtility.isNullorEmpty(diaryId)){
        groupRefer = diaryId + ",";
      }
      if(!YHUtility.isNullorEmpty(calendarId)){
        if(!YHUtility.isNullorEmpty(groupRefer)){
          groupRefer += calendarId + ",";
        }
      }
      YHScoreGroup scoreGroup = (YHScoreGroup) YHFOM.build(map, YHScoreGroup.class, "");
      scoreGroup.setGroupRefer(groupRefer);
      scoreGroup.setUserPriv(userPriv);
      this.logic.updateScoreGroup(dbConn, scoreGroup);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }

}
