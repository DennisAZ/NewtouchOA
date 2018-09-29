package yh.core.funcs.system.sealmanage.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.sealmanage.data.YHSeal;
import yh.core.funcs.system.sealmanage.data.YHSealLog;
import yh.core.funcs.system.sealmanage.logic.YHSealLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHSealAct {
  private static Logger log = Logger.getLogger(YHSealAct.class);

  /**
   * 印章权限管理列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getSealList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String sealId = request.getParameter("sealId");
      String sealName = request.getParameter("sealName");
      sealName = YHDBUtility.escapeLike(sealName);
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
      String data = "";
      YHSealLogic sl = new YHSealLogic();
      if(!YHUtility.isNullorEmpty(sealId) || !YHUtility.isNullorEmpty(sealName) || !YHUtility.isNullorEmpty(beginTime) || !YHUtility.isNullorEmpty(endTime)){
        data = sl.getSearchList(dbConn,request.getParameterMap(), sealId, sealName, beginTime, endTime);
      }else{
        data = sl.getSealList(dbConn,request.getParameterMap());
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
  
  public String getSearchList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String sealId = request.getParameter("sealId");
      String sealName = request.getParameter("sealName");
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
      YHSealLogic sl = new YHSealLogic();
      String data = sl.getSearchList(dbConn,request.getParameterMap(), sealId, sealName, beginTime, endTime);
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
  
  public String addPerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    int num = 0;
    Map map = new HashMap();
    String userIdOld = "";
    String userId = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      
        String userName = request.getParameter("userName");
        String userPriv = request.getParameter("userPriv");
        String userPrivOther = request.getParameter("role");
        
        map.put("userId" , userId);
        map.put("userName" , userName);
        map.put("userPriv" , userPriv);
       
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn , "sealLog" , map);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功添加人员");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateSealPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map map = new HashMap();
    Map mapSealLog = new HashMap();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int loginSeqId = person.getSeqId();
      String loginSeqIds = String.valueOf(loginSeqId);
      YHSealLogic sl = new YHSealLogic();
      YHSealLog slLog = new YHSealLog();
        String seqId = request.getParameter("seqId");
        String userStr = request.getParameter("userStr");
        String userName = request.getParameter("userName");
        
        map.put("seqId" , seqId);
        map.put("userStr" , userStr);
       
      YHORM orm = new YHORM();
      orm.updateSingle(dbConn, "seal", map);
      
      Calendar cal = Calendar.getInstance();
      java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        
      String logTime = sdf.format(cal.getTime());
      
      String sealId = sl.getSealId(dbConn, Integer.parseInt(seqId));
//      map.put("sealId" , sealId);
//      map.put("logType" , "setseal");
//      map.put("logTime" , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(logTime));
//      map.put("result" , "授权给:"+userName);
//      map.put("ipAdd" , request.getRemoteAddr());
//      map.put("userId" , loginSeqId);
      
      slLog.setsId(sealId);
      slLog.setLogType("setseal");
      slLog.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(logTime));
      slLog.setResult("授权给:"+userName);
      slLog.setIpAdd(request.getRemoteAddr());
      slLog.setUserId(loginSeqIds);
      //orm.saveSingle(dbConn , "sealLog" , map);
      orm.saveSingle(dbConn, slLog);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功添加人员");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取用户名称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getUserName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = "";
      String userIdStr = request.getParameter("userId");
      YHSealLogic dl = new YHSealLogic();
      if(!YHUtility.isNullorEmpty(userIdStr)){
        data = dl.getUserNameLogic(dbConn, userIdStr);
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getSealPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String seqIds = request.getParameter("seqId");
      int seqId = Integer.parseInt(seqIds);
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"SEQ_ID="+seqId};
      List funcList = new ArrayList();
      funcList.add("seal");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("SEAL"));
      if(list.size() > 1){
        for(Map ms : list){
          sb.append("{");
          sb.append("seqId:\"" + ms.get("seqId") + "\"");
          sb.append(",userStr:\"" + (ms.get("userStr") == null ? "" : ms.get("userStr")) + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map ms : list){
          sb.append("{");
          sb.append("seqId:\"" + ms.get("seqId") + "\"");
          sb.append(",userStr:\"" + (ms.get("userStr") == null ? "" : ms.get("userStr")) + "\"");
          sb.append("}");
        }
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
   * 删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteSeal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      String sumStrs = request.getParameter("sumStrs");
      YHSealLogic pl = new YHSealLogic();
      pl.deleteSeal(dbConn, sumStrs);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取印章后四位ID
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getCounter(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = "";
      String SEAL_ID = request.getParameter("SEAL_ID");
      YHSealLogic dl = new YHSealLogic();
      if(!YHUtility.isNullorEmpty(SEAL_ID)){
        data = dl.getCounterLogic(dbConn, SEAL_ID);
      }
      //System.out.println(data+"NMNMNMNMNMN");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String addSeal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map map = new HashMap();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      YHSeal sls = new YHSeal();
      String sealId = request.getParameter("sealId");
      String sealName = request.getParameter("sealName");
      //String certStr = request.getParameter("cetrStr");
      //sealName = YHUtility.encodeSpecial(sealName);
//      if(!YHUtility.isNullorEmpty(sealName)){
//        sealName = sealName.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
//      }
      String sealData = request.getParameter("sealData");
      //System.out.println(sealData);
      String deptId = request.getParameter("deptId");
      Calendar cal = Calendar.getInstance();        
      java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        
      String createTime = sdf.format(cal.getTime());    
   
      sls.setSealId(sealId);
      sls.setSealName(sealName);
      //sls.setCertStr(certStr);
      sls.setSealData(sealData);
      sls.setDeptId(deptId);
      sls.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime));
       
      YHORM orm = new YHORM();
      //orm.saveSingle(dbConn , "seal" , map);
      orm.saveSingle(dbConn , sls);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功添加人员");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 根据用户的管理权限得到所有部门（考勤统计）
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectDeptToAttendance(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      //int deptId = Integer.parseInt(request.getParameter("deptId"));
      int userId = user.getSeqId();
      String userPriv = user.getUserPriv();// 角色
      String postpriv = user.getPostPriv();// 管理范围
      String postDept = user.getPostDept();// 管理范围指定部门
      int userDeptId = user.getDeptId();
      YHDeptLogic deptLogic = new YHDeptLogic();
      boolean isAdminRole = user.isAdminRole();
      boolean isAdmin = user.isAdmin();
      String userDeptName = deptLogic.getNameByIdStr(
          String.valueOf(userDeptId), dbConn);
      String data = "";
      if (YHUtility.isNullorEmpty(postpriv)) {
        String[] postDeptArray = {String.valueOf(userDeptId)};
        data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
            + "]";
      } else {
        if (postpriv.equals("0")) {
          // data = "[{text:\"" + userDeptName + "\",value:" + userDeptId + "}]";
            String[] postDeptArray = {String.valueOf(userDeptId)};
            data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
                + "]";
         }
         if (postpriv.equals("1")) {
           data = deptLogic.getDeptTreeJson(0, dbConn);
         }
         if (postpriv.equals("2")) {
           if (postDept == null || postDept.equals("")) {
             data = "[]";
           } else {
             String[] postDeptArray = postDept.split(",");
             data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
                 + "]";

           }
         }
      }
      //System.out.println(data+"KJKJKJ");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, String.valueOf(userDeptId)
          + "," + postpriv);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 查询并拼装产品类别树   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectProductTypeTree(HttpServletRequest request,
		  HttpServletResponse response) throws Exception {
	  Connection dbConn = null;
	  try {
		  YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		  .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  dbConn = requestDbConn.getSysDbConn();
		  YHDeptLogic deptLogic = new YHDeptLogic();
		  String data = "";
		  data = deptLogic.getProductTypeTreeJson("-1", dbConn);
		  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		  request.setAttribute(YHActionKeys.RET_DATA, data);
	  } catch (Exception ex) {
		  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		  request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		  throw ex;
	  }
	  return "/core/inc/rtjson.jsp";
  }
  
  public String getShowInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map map = new HashMap();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("seqId");
      int seqId = Integer.parseInt(seqIdStr);
      String data = "";
      YHSealLogic sl = new YHSealLogic();
      data = sl.getShowInfo(dbConn, seqId);
      //System.out.println(data+"JKJKLK");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
