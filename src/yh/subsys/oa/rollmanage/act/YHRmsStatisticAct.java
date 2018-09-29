package yh.subsys.oa.rollmanage.act;


import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.rollmanage.logic.YHRmsStatisticLogic;
public class YHRmsStatisticAct {
  private YHRmsStatisticLogic logic = new YHRmsStatisticLogic();
  private static Logger log = Logger.getLogger(YHRmsStatisticAct.class);

  /**
   * 取得文件列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRmsStatisticJosn(HttpServletRequest request, HttpServletResponse response) throws Exception {

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getRmsFileJosn(dbConn, request.getParameterMap(), person);

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
   * 获取借阅次数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRmsLendCount(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String data = "";
      data = String.valueOf(this.logic.getRmsLendCount(dbConn, seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取文件个数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRmsRollCount(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String data = "";
      data = String.valueOf(this.logic.getRmsRollCount(dbConn, seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取卷库select下拉框数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRmsStatisticSelect(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      
      String[] filters2 = null;
      List funcList = new ArrayList();
      funcList.add("rmsRollRoom");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters2);
      list.addAll((List<Map>) map.get("OA_ARCHIVES_VOLUME_ROOM"));
      int flag = 0;
      for(Map ms : list){
        String roomName = (String) ms.get("roomName");
        if(!YHUtility.isNullorEmpty(roomName)){
          roomName = YHUtility.encodeSpecial(roomName);
        }
        String roomCode = (String) ms.get("roomCode");
        if(!YHUtility.isNullorEmpty(roomCode)){
          roomCode = YHUtility.encodeSpecial(roomCode);
        }
        sb.append("{");
        sb.append("seqId:\"" + ms.get("seqId") + "\"");
        sb.append(",roomCode:\"" + (ms.get("roomCode") == null ? "" : roomCode) + "\"");
        sb.append(",rommName:\"" + (ms.get("roomName") == null ? "" : roomName) + "\"");
        sb.append("},");
      }
      if(sb.length() > 1){
        sb.deleteCharAt(sb.length() - 1); 
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  
  /**
   * 获取卷库管理列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRmsStatisticRollJosn(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getRmsStatisticJson(dbConn,request.getParameterMap(), seqId);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
}
