package yh.core.funcs.address.act;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.address.data.YHAddressGroup;
import yh.core.funcs.address.logic.YHEmailSelectLogic;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.module.org_select.logic.YHOrgSelect2Logic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHEmailSelectAct {
  /**
   * 按部门选择email
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserByDept(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptStr = request.getParameter("deptId");
      String moduleId = request.getParameter("moduleId");
      String privNoFlagStr = request.getParameter("privNoFlag");
      int privNoFlag = 0;
      if (!YHUtility.isNullorEmpty(privNoFlagStr)) {
        privNoFlag = Integer.parseInt(privNoFlagStr);
      }
      String notLoginInStr = request.getParameter("notLoginIn");
      boolean notLoginIn = false;
      if (!YHUtility.isNullorEmpty(notLoginInStr) ) {
        notLoginIn = Boolean.parseBoolean(notLoginInStr);
      }
      long date1 = System.currentTimeMillis();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int deptId = Integer.parseInt(deptStr);
      YHOrgSelect2Logic osl = new YHOrgSelect2Logic();
      YHMyPriv mp = YHPrivUtil.getMyPriv(dbConn, person, moduleId, privNoFlag);
      boolean hasModule = false;
      if (moduleId != null && !"".equals(moduleId)) {
        hasModule = true;
      }
      StringBuffer data = osl.deptUser2Json(dbConn, deptId,mp,person , hasModule , notLoginIn);
      long date2 = System.currentTimeMillis();
      long date3 = date2 - date1;
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 按角色选择人员（包括辅助角色）
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getEmailByRole(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String roleIdStr = request.getParameter("roleId");
      int roleId = Integer.parseInt(roleIdStr);
      YHEmailSelectLogic onlinelogic = new YHEmailSelectLogic();
      YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      StringBuffer data = onlinelogic.getRoleEmail(dbConn, roleId,  user);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 选择在线用户
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getEmailByOnline(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHEmailSelectLogic onlinelogic = new YHEmailSelectLogic();
      YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      StringBuffer data = onlinelogic.getOnlineUserEmail2Json(dbConn,  user);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 根据查询选择人员
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserBySearch(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String moduleId = request.getParameter("moduleId");
    String privNoFlagStr = request.getParameter("privNoFlag");
    int privNoFlag = 0;
    if (!YHUtility.isNullorEmpty(privNoFlagStr)) {
      privNoFlag = Integer.parseInt(privNoFlagStr);
    }
    String notLoginInStr = request.getParameter("notLoginIn");
    boolean notLoginIn = false;
    if (!YHUtility.isNullorEmpty(notLoginInStr) ) {
      notLoginIn = Boolean.parseBoolean(notLoginInStr);
    }
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userName = request.getParameter("userName");
      YHOrgSelect2Logic osl = new YHOrgSelect2Logic();
      
      YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHMyPriv mp = YHPrivUtil.getMyPriv(dbConn, user, moduleId, privNoFlag);
      boolean hasModule = false;
      if (moduleId != null && !"".equals(moduleId)) {
        hasModule = true;
      }
      StringBuffer data = osl.getQueryUser2Json(dbConn, userName , user,  hasModule ,  mp , notLoginIn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 根据分组（自定义分组/公共分组）选择人员
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getEmailByGroup(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String groupIdStr = request.getParameter("groupId");
      int groupId = Integer.parseInt(groupIdStr);
      YHEmailSelectLogic onlinelogic = new YHEmailSelectLogic();
      YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      StringBuffer data = onlinelogic.getGorupEmail2Json(dbConn, groupId, user);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 由部门Id选择email
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getEmailsByDept(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String deptId = request.getParameter("deptId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHEmailSelectLogic onlinelogic = new YHEmailSelectLogic();
      List<YHPerson> list = onlinelogic.getEmailsByDept(dbConn, Integer.parseInt(deptId) ,true);
      YHDeptLogic deptLogic = new YHDeptLogic();
      String deptName = deptLogic.getNameById(Integer.parseInt(deptId), dbConn);
      
      StringBuffer data = new StringBuffer("[");
      StringBuffer sb = new StringBuffer();
      
      for (YHPerson p : list) {
        if(!"".equals(sb.toString())){
          sb.append(",");
        }
        String userName = p.getUserName();
        sb.append("{");
        sb.append("email:\"" + YHUtility.encodeSpecial(p.getEmail()) + "\",");
        sb.append("userName:\"" + YHUtility.encodeSpecial(userName) + "\"");
        sb.append(",isOnline:\"").append(onlinelogic.isUserOnline(dbConn, p.getSeqId())).append("\"");
        sb.append("}");
      }
      data.append(sb).append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, deptName);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 公共自定义组列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getUserGroup(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      String isPublic = request.getParameter("isPublic");
      String query = "1=1 and (user_Id is null or user_Id ='') order by ORDER_NO";
      if (isPublic == null 
          || "".equals(isPublic)) {
        query = "1=1 and user_Id  ='"+ userId +"' order by ORDER_NO";
      }
      YHORM orm = new YHORM();
      List<Map> list = new ArrayList();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{query};
      List funcList = new ArrayList();
      funcList.add("userGroup");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_PERSON_GROUP"));
      for(Map ms : list){
        String groupName = ms.get("groupName") == null ? "" : (String)ms.get("groupName");
        String groupNo = ms.get("orderNo") == null ? "" : (String)ms.get("orderNo");
        groupName = groupName.replaceAll("\"", "\\\\\"");
        groupNo = groupNo.replaceAll("\"", "\\\\\"");
        sb.append("{");
        sb.append("seqId:\"" + ms.get("seqId") + "\"");
        sb.append(",groupName:\"" + YHUtility.encodeSpecial(groupName) + "\"");
        sb.append(",orderNo:\"" + groupNo + "\"");
        sb.append("},");
      }
      if (list.size() > 0) {
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
   * 公共自定义组列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getUserState(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String ids = request.getParameter("ids");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHOrgSelect2Logic logic = new YHOrgSelect2Logic();
      String state = logic.getStates(ids, dbConn);
      if (state.endsWith(",")) {
        state = state.substring(0, state.length() - 1);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + state + "\"");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 根据查询选择人员
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDefaultEmail(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      String name = "请选择部门";
      StringBuffer data = new StringBuffer("[");
      YHEmailSelectLogic onlinelogic = new YHEmailSelectLogic();
      List<YHPerson> list = onlinelogic.getEmailsByDept(dbConn,person.getDeptId()  ,true);
      YHDeptLogic deptLogic = new YHDeptLogic();
      name = deptLogic.getNameById(person.getDeptId(), dbConn);
      
      StringBuffer sb = new StringBuffer();
      for (YHPerson p : list) {
        if(!"".equals(sb.toString())){
          sb.append(",");
        }
        String userName = p.getUserName();
        sb.append("{");
        sb.append("email:\"" + YHUtility.encodeSpecial(p.getEmail()) + "\",");
        sb.append("userName:\"" + YHUtility.encodeSpecial(userName) + "\"");
        sb.append(",isOnline:\"").append(onlinelogic.isUserOnline(dbConn, p.getSeqId())).append("\"");
        sb.append("}");
      }
      data.append(sb.toString());
      data.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, name);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 根据查询选择人员
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getEmailBySearch(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userName = request.getParameter("userName");
      YHEmailSelectLogic onlinelogic = new YHEmailSelectLogic();
      
      StringBuffer data = onlinelogic.getQueryEmail2Json(dbConn, userName );
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getContactPersonGroup(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int loginSeqId = person.getSeqId();
      int loginDeptId = person.getDeptId();
      int loginUserPriv = Integer.parseInt(person.getUserPriv());
      String loginUserId = person.getUserId(); 
      
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      ArrayList<YHAddressGroup> addressGroup = null;
      String[] filters = new String[]{"USER_ID = '" 
          + loginSeqId + "' or (USER_ID is null and ("
          + YHDBUtility.findInSet(String.valueOf(loginSeqId),"PRIV_USER")
          + " or "+ YHDBUtility.findInSet("0","PRIV_DEPT")
          + " or "+ YHDBUtility.findInSet("ALL_DEPT","PRIV_DEPT")
          + " or "+ YHDBUtility.findInSet(String.valueOf(loginDeptId),"PRIV_DEPT")
          + " or "+ YHDBUtility.findInSet(String.valueOf(loginUserPriv),"PRIV_ROLE")
          + ")) order by USER_ID asc, ORDER_NO asc, GROUP_NAME asc"};
      List funcList = new ArrayList();
      funcList.add("addressGroup");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_ADDRESS_TEAM"));
      for(Map ms : list){
        //if(ms.get("userId") == null && (findId(String.valueOf(ms.get("privDept")), String.valueOf(loginDeptId)) || findId(String.valueOf(ms.get("privRole")), String.valueOf(loginUserPriv)) || findId(String.valueOf(ms.get("privUser")), String.valueOf(loginSeqId)))){
        String groupName = (String) ms.get("groupName");
        if(!YHUtility.isNullorEmpty(groupName)){
          groupName = groupName.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
        }
        sb.append("{");
        sb.append("seqId:\"" + ms.get("seqId") + "\"");
        sb.append(",userId:\"" + (ms.get("userId") == null ? "" : ms.get("userId")) + "\"");
        sb.append(",groupName:\"" + (ms.get("groupName") == null ? "" : groupName) + "\"");
        sb.append("},");
        //}
      }
      sb.deleteCharAt(sb.length() - 1); 
      if(list.size() == 0){
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb+"NNNNM");
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
  public String getPublicContactPersonGroup(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int loginSeqId = person.getSeqId();
      int loginDeptId = person.getDeptId();
      String loginSeqIdStr = String.valueOf(person.getDeptId());
      String loginDeptIdStr = String.valueOf(person.getDeptId());
      int loginUserPriv = Integer.parseInt(person.getUserPriv());
      String loginUserId = person.getUserId(); 
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      
      String[] filters = new String[]{"(USER_ID is null and ("
          + findInSet(String.valueOf(loginSeqId),"PRIV_USER")
          + " or "+ findInSet(String.valueOf(loginDeptId),"PRIV_DEPT")
          + " or "+ findInSet(String.valueOf(loginUserPriv),"PRIV_ROLE")
           + " or "+ findInSet("0","PRIV_DEPT")
            + " or "+ findInSet("ALL_DEPT","PRIV_DEPT")
          + ")) order by USER_ID asc, ORDER_NO asc, GROUP_NAME asc"};
      
      //and (" + YHDBUtility.findInSet(loginDeptIdStr, "SUPPORT_DEPT") +" or (SUPPORT_DEPT like 0 or SUPPORT_DEPT like 'ALL_DEPT') or " + YHDBUtility.findInSet(loginSeqIdStr, "SUPPORT_USER") +" )
      String[] filters2 = new String[]{"USER_ID is null order by ORDER_NO asc, GROUP_NAME asc"};
      List funcList = new ArrayList();
      funcList.add("addressGroup");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters2);
      list.addAll((List<Map>) map.get("OA_ADDRESS_TEAM"));
      int flag = 0;
      for(Map ms : list){
        if(!"0".equals(String.valueOf(ms.get("privDept")))){
          if(!findId(String.valueOf(ms.get("privDept")), String.valueOf(loginDeptId)) && !findId(String.valueOf(ms.get("privRole")), String.valueOf(loginUserPriv)) && !findId(String.valueOf(ms.get("privUser")), String.valueOf(loginSeqId))){
            flag++;
            continue;
          }
        }
        
        String groupName = (String) ms.get("groupName");
        if(!YHUtility.isNullorEmpty(groupName)){
          groupName = groupName.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
        }
        sb.append("{");
        sb.append("seqId:\"" + ms.get("seqId") + "\"");
        sb.append(",userId:\"" + (ms.get("userId") == null ? "" : ms.get("userId")) + "\"");
        sb.append(",groupName:\"" + (ms.get("groupName") == null ? "" : groupName) + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1); 
      
      if(list.size() == 0 || flag == list.size()){
        sb.append("[");
      }
      sb.append("]");
      //System.out.println(sb+"NNNNMssss");
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
   * mysql findInSet 处理
   * @param str
   * @param dbFieldName
   * @return
   * @throws SQLException
   */
  public String findInSet(String str,String dbFieldName) throws SQLException{
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    String result = "";
    if (dbms.equals("sqlserver")) {
      result = "find_in_set('" +str+ "'," + dbFieldName + ")";
    }else if (dbms.equals("mysql")) {
      result = "find_in_set('" +str+ "'," + dbFieldName + ")";
    }else if (dbms.equals("oracle")) {
      result = "instr(" + dbFieldName + ",'" +str+ "') > 0";
    }else {
      throw new SQLException("not accepted dbms");
    }
    
    return result;
  }
  /** 
   * 判段id是不是在str里面 
   * @param str 
   * @param id 
   * @return 
   */ 
   public static boolean findId(String str, String id) {
     if (str == null || id == null || "".equals(str) || "".equals(id)) {
       return false;
     }
     String[] aStr = str.split(",");
     for (String tmp : aStr) {
       if (tmp.equals(id)) {
         return true;
       }
     }
     return false;
   }
}
