package yh.core.module.org_select.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.modulepriv.data.YHModulePriv;
import yh.core.funcs.orgselect.logic.YHPersonSelectLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.module.org_select.logic.YHOrgSelect2Logic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;

public class YHOrgSelect2Act {
  /**
   * 按部门选择人员
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
     //add by jzk, 为树添加一个公司的根---start
      int deptId = 0;
      if(!"organizationNodeId".equalsIgnoreCase(deptStr))
      {
    	  deptId = Integer.parseInt(deptStr);
      }
      //add by jzk, 为树添加一个公司的根---end
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
  public String getUserByRole(HttpServletRequest request, HttpServletResponse response) throws Exception{
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
      String roleIdStr = request.getParameter("roleId");
      int roleId = Integer.parseInt(roleIdStr);
      YHOrgSelect2Logic osl = new YHOrgSelect2Logic();
      YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHMyPriv mp = YHPrivUtil.getMyPriv(dbConn, user, moduleId, privNoFlag);
      boolean hasModule = false;
      if (moduleId != null && !"".equals(moduleId)) {
        hasModule = true;
      }
      StringBuffer data = osl.getRoleUser(dbConn, roleId,  user,  hasModule  ,  mp , notLoginIn);
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
  public String getUserByOnline(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String moduleId = request.getParameter("moduleId");
    String privNoFlagStr = request.getParameter("privNoFlag");
    int privNoFlag = 0;
    if (!YHUtility.isNullorEmpty(privNoFlagStr)) {
      privNoFlag = Integer.parseInt(privNoFlagStr);
    }
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHOrgSelect2Logic osl = new YHOrgSelect2Logic();
      YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHMyPriv mp = YHPrivUtil.getMyPriv(dbConn, user, moduleId, privNoFlag);
      boolean hasModule = false;
      if (moduleId != null && !"".equals(moduleId)) {
        hasModule = true;
      }
      StringBuffer data = osl.getOnlineUser2Json(dbConn,  user,  hasModule ,  mp);
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
  public String getUserByGroup(HttpServletRequest request, HttpServletResponse response) throws Exception{
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
      String groupIdStr = request.getParameter("groupId");
      int groupId = Integer.parseInt(groupIdStr);
      YHOrgSelect2Logic osl = new YHOrgSelect2Logic();
      YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHMyPriv mp = YHPrivUtil.getMyPriv(dbConn, user, moduleId, privNoFlag);
      boolean hasModule = false;
      if (moduleId != null && !"".equals(moduleId)) {
        hasModule = true;
      }
      StringBuffer data = osl.getGorupUser2Json(dbConn, groupId, user,  hasModule ,  mp , notLoginIn);
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
   * 由部门Id选择人员
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPersonsByDept(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String deptId = request.getParameter("deptId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String moduleId = request.getParameter("moduleId");
      String privNoFlagStr = request.getParameter("privNoFlag");
      String notLoginInStr = request.getParameter("notLoginIn");
      boolean notLoginIn = false;
      if (!YHUtility.isNullorEmpty(notLoginInStr) ) {
        notLoginIn = Boolean.parseBoolean(notLoginInStr);
      }
      int privNoFlag = 0;
      if (!YHUtility.isNullorEmpty(privNoFlagStr)) {
        privNoFlag = Integer.parseInt(privNoFlagStr);
      }
      long date1 = System.currentTimeMillis();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHOrgSelect2Logic onlinelogic = new YHOrgSelect2Logic();
      List<YHPerson> list = onlinelogic.getPersonsByDept(dbConn, Integer.parseInt(deptId) ,notLoginIn);
      long date2 = System.currentTimeMillis();
      long date3 = date2 - date1;
      YHDeptLogic deptLogic = new YHDeptLogic();
      String deptName = deptLogic.getNameById(Integer.parseInt(deptId), dbConn);
      
      StringBuffer data = new StringBuffer("[");
      StringBuffer sb = new StringBuffer();
      YHMyPriv mp = new YHMyPriv();
      
      boolean  hasModule = false;
      if (moduleId != null && !"".equals(moduleId)) {
        mp = YHPrivUtil.getMyPriv(dbConn, person, moduleId, privNoFlag);
        hasModule = true;
      }
      YHModulePriv priv = YHPrivUtil.getMyPrivByModel(dbConn,  person.getSeqId(), moduleId);
      String manager = YHPrivUtil.getDeptManager( dbConn ,priv, person, moduleId, privNoFlag) ;
      for (YHPerson p : list) {
        boolean b = !YHPrivUtil.isUserPriv(dbConn, p.getSeqId(), mp,  person.getPostPriv(), person.getPostDept(), person.getSeqId(), person.getDeptId());
        if(hasModule && b && !YHWorkFlowUtility.findId(manager, p.getSeqId() + "")){
          continue;
        }
        if(!"".equals(sb.toString())){
          sb.append(",");
        }
        String userId = String.valueOf(p.getSeqId());
        String userName = p.getUserName();
        sb.append("{");
        sb.append("userId:'" + userId + "',");
        sb.append("userName:\"" + YHUtility.encodeSpecial(userName) + "\"");
        sb.append(",isOnline:\"").append(onlinelogic.isUserOnline(dbConn, p.getSeqId())).append("\"");
        sb.append("}");
      }
      data.append(sb).append("]");
      //System.out.println(sb.toString());
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
   * 取得人员的状态与名字
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
      String str  =  "[" +  logic.getPersons(ids, dbConn) + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, str);
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
  public String getDefaultUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String moduleId = request.getParameter("moduleId");
    boolean hasModule = false;
    if (moduleId != null && !"".equals(moduleId)) {
      hasModule = true;
    }
    String notLoginInStr = request.getParameter("notLoginIn");
    boolean notLoginIn = false;
    if (!YHUtility.isNullorEmpty(notLoginInStr) ) {
      notLoginIn = Boolean.parseBoolean(notLoginInStr);
    }
    String privNoFlagStr = request.getParameter("privNoFlag");
    int privNoFlag = 0;
    if (!YHUtility.isNullorEmpty(privNoFlagStr)) {
      privNoFlag = Integer.parseInt(privNoFlagStr);
    }
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      String name = "请选择部门";
      YHMyPriv mp = new YHMyPriv();
      boolean isMyDept = true;
      if (hasModule) {
        mp = YHPrivUtil.getMyPriv(dbConn, person, moduleId, privNoFlag);
        String deptPriv = mp.getDeptPriv();
        if (!"1".equals(deptPriv) && !"0".equals(deptPriv)){
          isMyDept = false;
        }
      } 
      StringBuffer data = new StringBuffer("[");
      if (isMyDept) {
        YHOrgSelect2Logic onlinelogic = new YHOrgSelect2Logic();
        List<YHPerson> list = onlinelogic.getPersonsByDept(dbConn, person.getDeptId() ,notLoginIn);
        YHDeptLogic deptLogic = new YHDeptLogic();
        name = deptLogic.getNameById(person.getDeptId(), dbConn);
        
        StringBuffer sb = new StringBuffer();
        for (YHPerson p : list) {
          if(hasModule && !YHPrivUtil.isUserPriv(dbConn, p.getSeqId(), mp,  person.getPostPriv(), person.getPostDept(), person.getSeqId(), person.getDeptId())){
            continue;
          }
          if(!"".equals(sb.toString())){
            sb.append(",");
          }
          String userId = String.valueOf(p.getSeqId());
          String userName = p.getUserName();
          sb.append("{");
          sb.append("userId:'" + userId + "',");
          sb.append("userName:\"" + YHUtility.encodeSpecial(userName) + "\"");
          sb.append(",isOnline:\"").append(onlinelogic.isUserOnline(dbConn, p.getSeqId())).append("\"");
          sb.append("}");
        }
        data.append(sb.toString());
      }
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
}
