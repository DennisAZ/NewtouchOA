package yh.core.funcs.doc.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.doc.data.YHDocFlowProcess;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHFlowUserSelectLogic {
  private int userFilter = 0 ;
  private List<Map> personList = null;
  public YHFlowUserSelectLogic ( int userFilter) {
    this.userFilter = userFilter;
  }
  public YHFlowUserSelectLogic () {
  }
  /**
   * 是否是上一级部门
   * @param conn
   * @param user
   * @param deptId
   * @return
   */
  public boolean isParentDept(Connection conn , int parentId , int deptId , String deptOther) {
    if (parentId == deptId) {
      return true;
    }
    if (YHWorkFlowUtility.findId(deptOther, String.valueOf(parentId))) {
      return true;
    }
    return false;
  }
  public int getParentDept(Connection conn , YHPerson user) throws Exception {
    String query = "select DEPT_PARENT FROM oa_department WHERE SEQ_ID = " + user.getDeptId();
    Statement stm = null;
    ResultSet rs = null;
    int parentId = 0 ;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        parentId = rs.getInt("DEPT_PARENT") ;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return parentId;
  }
  /**
   * 取得经办人的 json对象
   * @param deptId
   * @param prcsChoose
   * @param flowId
   * @param userFilter
   * @param user
   * @return
   * @throws Exception
   */
  public String getOpUserJson(int deptId, int prcsChoose, int flowId , YHPerson user , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    Map map = this.getPrivStr(flowId, prcsChoose, conn);
    String userStr =  (String)map.get("user");
    String roleStr = (String)map.get("role");
    String deptStr =  (String)map.get("dept");
    
    YHPersonLogic personLogic = new YHPersonLogic();
    int count = 0 ;
    int parentId = 0;
    StringBuffer sb = new StringBuffer();
    if (userFilter == 20) {
      parentId = this.getParentDept(conn, user);
      if (parentId == 0 ) {
        return "";
      }else {
        deptId = parentId;
      }
    }
    if(deptId == 0){
       //所有人员      List<Map> userList = this.getPersons(userStr, deptStr, roleStr, conn);
      for(Map tmp : userList ){
        String deptIdOther = (String) tmp.get("deptIdOther");
        int deptId2 = (Integer)tmp.get("deptId");
       
        int userDeptId = user.getDeptId();
        //只能选择本部门经办人
        if (userFilter == 1 
            && (deptId2 != userDeptId
                && !YHWorkFlowUtility.findId(deptIdOther, String.valueOf(userDeptId)))) {
          continue;
        }
        this.getMapToJson(sb, tmp);
        count ++;
      }
    }else{
      //首先判断这个部门是否在里面      String deptStr2 = "";
      if ("0".equals(deptStr)) {
        deptStr2 = YHOrgSelectLogic.changeDept(conn, deptStr); 
      } else {
        deptStr2 = deptStr;
      }
      
      if(YHWorkFlowUtility.findId(deptStr2, String.valueOf(deptId))){
        List<YHPerson> list = this.getPersonsByDept(conn, deptId);
        return personLogic.getPersonSimpleJson(list);
      }else{
        List<Map> userList = this.getPersons(userStr, deptStr, roleStr, conn);
        for(Map tmp : userList ){
          int deptId2 = (Integer)tmp.get("deptId");
          String deptIdOther = (String)tmp.get("deptIdOther");
          String userPrivOther = (String)tmp.get("userPrivOther");
          int userDeptId = user.getDeptId();
          //只能选择本部门经办人
          if ((userFilter == 1 
              && (deptId2 != userDeptId
                  && !YHWorkFlowUtility.findId(deptIdOther, String.valueOf(userDeptId))))
              || (deptId2 != deptId && !YHWorkFlowUtility.findId(deptIdOther, String.valueOf(deptId))) ) {
            continue;
          }
          this.getMapToJson(sb, tmp);
          count ++;
        }
      }
    }
    if(count > 0 ){
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  public void getMapToJson(StringBuffer sb , Map map) {
    String name = (String)map.get("userName");
    Integer seqId = (Integer) map.get("seqId");
    sb.append("{");
    sb.append("userId:'" + seqId + "',");
    sb.append("userName:'" +  name +"'");
    sb.append("},");
  }
  /**
   * 取得查询的经办人json对象
   * @param deptId
   * @param prcsChoose
   * @param flowId
   * @param userFilter
   * @param user
   * @return
   * @throws Exception
   */
  public String doSearch(String  searchName, int prcsChoose, int flowId  , YHPerson user , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    Map map = this.getPrivStr(flowId, prcsChoose, conn);
    String userStr = (String)map.get("user");
    String deptStr = (String)map.get("dept");
    String roleStr = (String)map.get("role");
    List<Map> userList = this.getPersons(userStr, deptStr, roleStr, conn);
    StringBuffer sb = new StringBuffer();
    int count = 0 ;
    int parentId = 0;
    if (userFilter == 20) {
      parentId = this.getParentDept(conn, user);
      if (parentId == 0 ) {
        return "";
      }
    }
    for(Map tmp : userList ){
      String deptIdOther = (String) tmp.get("deptIdOther");
      String userName = (String)tmp.get("userName");
      int deptId = (Integer)tmp.get("deptId");
      int userDeptId = user.getDeptId();
      String userId = (String)tmp.get("userId");
      //只能选择本部门经办人
      if (userFilter == 1 
          && (deptId != userDeptId
              && !YHWorkFlowUtility.findId(deptIdOther, String.valueOf(userDeptId)))) {
        continue;
      }
      if ((userFilter == 20 
          && (!this.isParentDept(conn, parentId, deptId, deptIdOther)))) {
        continue;
      }
      if((searchName != null 
          && (userName.contains(searchName) 
              || userId.contains(searchName)))){
        this.getMapToJson(sb, tmp);
        count ++;
      }
    }
    if(count > 0 ){
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  public Map getPrivStr(int flowId , int prcsId , Connection conn) throws Exception {
    Map map = new HashMap();
    String query = "select PRCS_USER , PRCS_PRIV , PRCS_DEPT from "+ YHWorkFlowConst.FLOW_PROCESS +" where flow_seq_id=" + flowId  + " and PRCS_ID=" + prcsId;
    String userStr =  "";
    String roleStr = "";
    String deptStr =  "";
    
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        userStr = rs.getString("PRCS_USER");
        roleStr = rs.getString("PRCS_PRIV");
        deptStr = rs.getString("PRCS_DEPT");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    userStr = userStr == null ? "" : userStr;
    roleStr = roleStr == null ? "" : roleStr;
   // deptStr = YHOrgSelectLogic.changeDept(conn, deptStr); 
    deptStr = deptStr == null ? "" : deptStr;
    map.put("user", userStr);
    map.put("dept", deptStr);
    map.put("role", roleStr);
    return map;
  }
  /**
   * 根据roleId取得经办人json对象
   * @param deptId
   * @param prcsChoose
   * @param flowId
   * @param userFilter
   * @param user
   * @return
   * @throws Exception
   */
  public String getUserByRole(int  roleId, int prcsChoose, int flowId  , YHPerson user , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    Map map = this.getPrivStr(flowId, prcsChoose, conn);
    String userStr =  (String)map.get("user");
    String roleStr = (String)map.get("role");
    String deptStr =  (String)map.get("dept");
    //首先判断这个角色是否在里面    int parentId = 0;
    if (userFilter == 20) {
      parentId = this.getParentDept(conn, user);
      if (parentId == 0 ) {
        return "";
      }
    }
    if(YHWorkFlowUtility.findId(roleStr, String.valueOf(roleId))){
      List<YHPerson> list = this.getPersonsByRole(conn, roleId , user , parentId);
      YHPersonLogic personLogic = new YHPersonLogic();
      return personLogic.getPersonSimpleJson(list);
    }else{
     
      List<Map> userList = this.getPersons(userStr, deptStr, roleStr, conn);
      
      this.personList = userList;
      int count = 0;
      StringBuffer sb = new StringBuffer();
      for(Map tmp : userList ){
        int deptId2 = (Integer)tmp.get("deptId");
        String deptIdOther = (String)tmp.get("deptIdOther");
        int userDeptId = user.getDeptId();
        
        String sUserPriv = (String)tmp.get("userPriv");
        int userPriv = 0 ;
        if (sUserPriv != null && !"".equals(sUserPriv)) {
          userPriv = Integer.parseInt(sUserPriv);
        }
        //只能选择本部门经办人
        if ((userFilter == 1 
            && (deptId2 != userDeptId
                && !YHWorkFlowUtility.findId(deptIdOther, String.valueOf(userDeptId))))
            || userPriv != roleId) {
          continue;
        }
      //只能选择上一级部门经办人
        if (userFilter == 20 
            && (!this.isParentDept(conn, parentId, deptId2, deptIdOther))) {
          continue;
        }
        this.getMapToJson(sb, tmp);
        count ++;
      }
      if(count > 0 ){
        sb.deleteCharAt(sb.length() - 1);
      }
      return sb.toString();
    }
  }
  /**
   * 主要用于流程定义中的选择默认人员根据roleId取得经办人json对象
   * @param deptId
   * @param prcsChoose
   * @param flowId
   * @param userFilter
   * @param user
   * @return
   * @throws Exception
   */
  public String getUserByRoleP(int  roleId, YHDocFlowProcess fp , Connection conn , YHPerson user) throws Exception {
    // TODO Auto-generated method stub
    //取得下一步骤
    String userStr =  fp.getPrcsUser() == null ? "" : fp.getPrcsUser();
    String roleStr = fp.getPrcsPriv()  == null ? "" : fp.getPrcsPriv();
   // String deptStr = YHOrgSelectLogic.changeDept(conn, fp.getPrcsDept()); 
    String deptStr = fp.getPrcsDept();
    deptStr = deptStr == null ? "" : deptStr;
  //首先判断这个角色是否在里面
    if(YHWorkFlowUtility.findId(roleStr, String.valueOf(roleId))){
      List<YHPerson> list = this.getPersonsByRole(conn, roleId , user , 0);
      YHPersonLogic personLogic = new YHPersonLogic();
      return personLogic.getPersonSimpleJson(list);
    }else{
      List<Map> userList = this.getPersons(userStr, deptStr, roleStr, conn);
      this.personList = userList;
      int count = 0;
      StringBuffer sb = new StringBuffer();
      for(Map tmp : userList ){
        int deptId2 = (Integer)tmp.get("deptId");
        String deptIdOther = (String)tmp.get("deptIdOther");
        
        String sUserPriv = (String)tmp.get("userPriv");
        int userPriv = 0 ;
        if (sUserPriv != null && !"".equals(sUserPriv)) {
          userPriv = Integer.parseInt(sUserPriv);
        }
        //只能选择本部门经办人
        if ( userPriv != roleId) {
          continue;
        }
        this.getMapToJson(sb, tmp);
        count ++;
      }
      if(count > 0 ){
        sb.deleteCharAt(sb.length() - 1);
      }
      return sb.toString();
    }
  }
  /**
   * 根据roleId取得经办人json对象
   * @param deptId
   * @param prcsChoose
   * @param flowId
   * @param userFilter
   * @param user
   * @return
   * @throws Exception
   */
  public String getUserBySupplementRoleP(int  roleId, YHDocFlowProcess fp , Connection conn , YHPerson user) throws Exception {
    // TODO Auto-generated method stub
    //取得下一步骤
    String userStr =  fp.getPrcsUser() == null ? "" : fp.getPrcsUser();
    String roleStr = fp.getPrcsPriv()  == null ? "" : fp.getPrcsPriv();
    //String deptStr = YHOrgSelectLogic.changeDept(conn, fp.getPrcsDept()); 
    String deptStr = fp.getPrcsDept();
    deptStr = deptStr == null ? "" : deptStr;
   //首先判断这个角色是否在里面
    if(YHWorkFlowUtility.findId(roleStr, String.valueOf(roleId))){
      List<YHPerson> list = this.getPersonsBySupplementRole(conn, roleId , user , 0);
      YHPersonLogic personLogic = new YHPersonLogic();
      return personLogic.getPersonSimpleJson(list);
    }else{
      List<Map> userList = null;
      if (this.personList == null) {
        userList = this.getPersons(userStr, deptStr, roleStr, conn);
      } else {
        userList = this.personList;
      }
      int count = 0;
      StringBuffer sb = new StringBuffer();
      for(Map tmp : userList ){
        int deptId2 = (Integer)tmp.get("deptId");
        String deptIdOther = (String)tmp.get("deptIdOther");
        String userPrivOther = (String)tmp.get("userPrivOther");
        
        //只能选择本部门经办人
        if (!YHWorkFlowUtility.findId(userPrivOther, String.valueOf(roleId))) {
          continue;
        }
        this.getMapToJson(sb, tmp);
        count ++;
      }
      if(count > 0 ){
        sb.deleteCharAt(sb.length() - 1);
      }
      return sb.toString();
    }
  }
  /**
   * 根据roleId取得辅助角色经办人json对象
   * @param deptId
   * @param prcsChoose
   * @param flowId
   * @param userFilter
   * @param user
   * @return
   * @throws Exception
   */
  public String getUserBySupplementRole(int  roleId, int prcsChoose, int flowId  , YHPerson user , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    Map map = this.getPrivStr(flowId, prcsChoose, conn);
    String userStr =  (String)map.get("user");
    String roleStr = (String)map.get("role");
    String deptStr =  (String)map.get("dept");
   //首先判断这个角色是否在里面    int parentId = 0;
    if (userFilter == 20) {
      parentId = this.getParentDept(conn, user);
      if (parentId == 0 ) {
        return "";
      }
    }
    if(roleStr != null && YHWorkFlowUtility.findId(roleStr, String.valueOf(roleId))){
      List<YHPerson> list = this.getPersonsBySupplementRole(conn, roleId , user , parentId);
      YHPersonLogic personLogic = new YHPersonLogic();
      return personLogic.getPersonSimpleJson(list);
    }else{
      List<Map> userList = null;
      if (this.personList == null) {
        userList = this.getPersons(userStr, deptStr, roleStr, conn);
      } else {
        userList = this.personList;
      }
      int count = 0;
      StringBuffer sb = new StringBuffer();
      for(Map tmp : userList ){
        int deptId2 = (Integer)tmp.get("deptId");
        String deptIdOther = (String)tmp.get("deptIdOther");
        String userPrivOther = (String)tmp.get("userPrivOther");
        int userDeptId = user.getDeptId();
        
        //只能选择本部门经办人
        if ((userFilter == 1 
            && (deptId2 != userDeptId
                && !YHWorkFlowUtility.findId(deptIdOther, String.valueOf(userDeptId))))
            || !YHWorkFlowUtility.findId(userPrivOther, String.valueOf(roleId))) {
          continue;
        }
      //只能选择上一级部门经办人
        if (userFilter == 20 
            && (!this.isParentDept(conn, parentId, deptId2, deptIdOther))) {
          continue;
        }
        this.getMapToJson(sb, tmp);
        count ++;
      }
      if(count > 0 ){
        sb.deleteCharAt(sb.length() - 1);
      }
      return sb.toString();
      //String data = getPersonSimpleJson(userStr , deptStr , roleStr , "OR" , "OR" ,null , roleId , conn , user) ;
    }
  }
  /**
   * 取得所有人的 json对象,主要是在自由流程选人的
   * @param deptId
   * @param prcsChoose
   * @param flowId
   * @param userFilter
   * @param user
   * @return
   * @throws Exception
   */
  public String getUserJson(int deptId, int flowId , YHPerson user , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    //取得下一步骤
    if(deptId == 0){
      return this.getAllPerson(conn) ;
    }else{
      return this.getPersonByDept(conn, deptId);
    }
  }
  public String getPersonByDept(Connection conn , int deptId) throws Exception {
    StringBuffer sb = new StringBuffer();
    String query = "select  PERSON.SEQ_ID,USER_NAME  from PERSON,USER_PRIV where  USER_PRIV.SEQ_ID = PERSON.USER_PRIV AND  NOT_LOGIN <> '1' and dept_ID=" + deptId + " ORDER BY USER_PRIV.PRIV_NO ,USER_NO DESC , PERSON.SEQ_ID";
    int count = 0 ;
    Statement stm = null;
    ResultSet rs = null;
    Set set = new HashSet();
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        if (!set.contains(seqId)) {
          sb.append("{");
          sb.append("userId:" + rs.getInt("SEQ_ID"));
          sb.append(",userName:'" + rs.getString("USER_NAME") + "'");
          sb.append("},");
          count ++ ;
          set.add(seqId);
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    if (count > 0 ) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  
  public String getAllPerson(Connection conn) throws Exception {
    StringBuffer sb = new StringBuffer();
    String query = "select PERSON.SEQ_ID ,USER_NAME  from PERSON,USER_PRIV where USER_PRIV.SEQ_ID = PERSON.USER_PRIV AND NOT_LOGIN <> '1' ORDER BY USER_PRIV.PRIV_NO ,USER_NO DESC,PERSON.SEQ_ID";
    int count = 0 ;
    Statement stm = null;
    ResultSet rs = null;
    Set set = new HashSet();
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        if (!set.contains(seqId)) {
          sb.append("{");
          sb.append("userId:" + rs.getInt("SEQ_ID"));
          sb.append(",userName:'" + rs.getString("USER_NAME") + "'");
          sb.append("},");
          count ++ ;
          set.add(seqId);
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    if (count > 0 ) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  
  /**
   * 通过给定的人员ID串，部门串，权限串。取得有员json对象，
   * @param userIds
   * @param deptIds
   * @param privIds
   * @param flagStr 查询条件连接符号
   * @return
   * @throws Exception 
   * @throws Exception
   */
  public String getPersonInDept(String userStr , String deptStr , String roleStr  , Connection conn , String sDept) throws Exception {
    StringBuffer sb = new StringBuffer();
    int count = 0 ;
    if(sDept == null || "organizationNodeId".equals(sDept)){
       //所有人员      List<Map> userList = this.getPersons(userStr, deptStr, roleStr, conn);
      for(Map tmp : userList ){
        String deptIdOther = (String) tmp.get("deptIdOther");
        int deptId2 = (Integer)tmp.get("deptId");
        this.getMapToJson(sb, tmp);
        count ++;
      }
    }else{
      int deptId = Integer.parseInt(sDept);
      if (deptStr != null 
          && !"".equals(deptStr) 
          && YHWorkFlowUtility.findId(deptStr, sDept)) {//首先判断部门在里面没有这个部门
        YHPersonLogic personLogic = new YHPersonLogic();
        List<YHPerson> list = this.getPersonsByDept(conn, deptId  );
        return personLogic.getPersonSimpleJson(list);
      }else{
        List<Map> userList = this.getPersons(userStr, deptStr, roleStr, conn);
        for(Map tmp : userList ){
          int deptId2 = (Integer)tmp.get("deptId");
          String deptIdOther = (String)tmp.get("deptIdOther");
          if (deptId2 != deptId && !YHWorkFlowUtility.findId(deptIdOther, String.valueOf(sDept))) {
            continue;
          }
          this.getMapToJson(sb, tmp);
          count ++;
        }
      }
    }
    if(count > 0 ){
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  public List<Map> getPersons(String userIds , String deptIds , String privIds  , Connection conn) throws Exception {
    String query1 = "";
    Set set = new HashSet();
    if ("0".equals(deptIds)) {
      query1 = "select PERSON.SEQ_ID  , USER_ID , USER_NAME , DEPT_ID , DEPT_ID_OTHER , USER_PRIV , USER_PRIV_OTHER from PERSON, USER_PRIV WHERE  NOT_LOGIN <> '1' AND USER_PRIV.SEQ_ID = PERSON.USER_PRIV  ORDER BY USER_PRIV.PRIV_NO ,USER_NO DESC ,PERSON.SEQ_ID ";
    } else {
      query1 = "select PERSON.SEQ_ID  , USER_ID , USER_NAME , DEPT_ID , DEPT_ID_OTHER , USER_PRIV , USER_PRIV_OTHER from PERSON , USER_PRIV WHERE USER_PRIV.SEQ_ID = PERSON.USER_PRIV AND (1 <> 1 ";
      if(userIds != null && !"".equals(userIds)){
        String[] aUserId = userIds.split(",");
        //如果只有一个的话
        if(aUserId.length == 1){
          query1 += " or PERSON.SEQ_ID = " + aUserId[0];
        } else{
          if (userIds.endsWith(",")) {
            userIds = userIds.substring(0, userIds.length() - 1);
          }
          query1 += " or PERSON.SEQ_ID IN (" + userIds + ")";
        }
      }
      if(privIds != null && !"".equals(privIds)){
        String[] aPrivId = privIds.split(",");
        privIds = "";
        for(int i = 0 ;i < aPrivId.length ; i++){
          privIds +=  "'" + aPrivId[i] + "'" + ",";
        }
        privIds = privIds.substring(0, privIds.length() - 1);
        query1 += " or  USER_PRIV IN (" + privIds + ")";
      }
      if(deptIds != null && !"".equals(deptIds)){
        String[] aDeptId = deptIds.split(",");
        if(deptIds.endsWith(",")){
          deptIds = deptIds.substring(0, deptIds.length() - 1);
        }
        if(aDeptId.length == 1){
          query1 += " or DEPT_ID = " + deptIds;
        } else {
          query1 += " or DEPT_ID IN (" + deptIds + ") ";
        }
      }
      query1 += ") and NOT_LOGIN <> '1' ORDER BY USER_PRIV.PRIV_NO , USER_NO DESC ,PERSON.SEQ_ID ";
    }
    List<Map> list = new ArrayList();
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query1);
      while (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        if (!set.contains(seqId)) {
          Map map = this.rsToMap(rs);
          list.add(map);
          set.add(seqId);
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    if (!"0".equals(deptIds)) {
      //处理辅助部门
      this.getOtherDept(list, deptIds, conn);
      //处理辅助角色
      this.getOtherPriv(list, privIds, conn);
    }
    return list;
  }
  public Map rsToMap(ResultSet rs) throws SQLException{
    Map map = new HashMap();
    map.put("seqId", rs.getInt("SEQ_ID"));
    map.put("userId", rs.getString("USER_ID"));
    map.put("userName", rs.getString("USER_NAME"));
    map.put("deptId", rs.getInt("DEPT_ID"));
    map.put("deptIdOther", rs.getString("DEPT_ID_OTHER"));
    map.put("userPriv", rs.getString("USER_PRIV"));
    map.put("userPrivOther", rs.getString("USER_PRIV_OTHER"));
    return map;
  }
  
  public void  getOtherDept(List<Map> list , String depts , Connection conn) throws Exception {
    if (depts == null || "".equals(depts)) {
      return ;
    }
    String[] aDept = depts.split(",");
    for (int i = 0 ;i < aDept.length ;i ++) {
      String tmp = aDept[i];
      if (tmp.startsWith("'")) {
        tmp = tmp.substring(1);
      }
      if (tmp.endsWith("'")) {
        tmp = tmp.substring(0 , tmp.length() - 1);
      }
      if (YHUtility.isInteger(tmp)) {
        this.getPersonsByDeptOther(list ,tmp, conn);
      }
    }
  }
  public void getOtherPriv(List<Map> list , String roles , Connection conn) throws Exception {
    if (roles == null || "".equals(roles)) {
      return ;
    }
    String[] aRole = roles.split(",");
    for (int i = 0 ;i < aRole.length ;i ++) {
      String tmp = aRole[i];
      if (tmp.startsWith("'")) {
        tmp = tmp.substring(1);
      }
      if (tmp.endsWith("'")) {
        tmp = tmp.substring(0 , tmp.length() - 1);
      }
      if (YHUtility.isInteger(tmp)) {
        this.getPersonsByRoleOther(list, tmp, conn);
      }
    }
  }
  public void getPersonsByRoleOther(List<Map> list, String roleId , Connection conn ) throws Exception {
    String query = "select SEQ_ID , USER_ID , USER_NAME ,USER_PRIV_OTHER , DEPT_ID , USER_PRIV , DEPT_ID_OTHER  from PERSON where  NOT_LOGIN <> '1' AND  " + YHDBUtility.findInSet(roleId, "USER_PRIV_OTHER") + "  order by USER_NO DESC , SEQ_ID ";
    Statement stm5 = null;
    ResultSet rs5 = null;
    try {
      stm5 = conn.createStatement();
      rs5 = stm5.executeQuery(query);
      while (rs5.next()) {
        int seqId = rs5.getInt("SEQ_ID");
        if (!this.hasPerson(list, seqId)) {
          Map per = this.rsToMap(rs5);
          list.add(per);
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm5 , rs5 , null);
    }
  }
  public void getPersonsByDeptOther(List<Map> list ,String deptId , Connection conn) throws Exception {
    String query = "select SEQ_ID , USER_ID , USER_NAME ,USER_PRIV_OTHER , DEPT_ID , USER_PRIV , DEPT_ID_OTHER  from PERSON where  NOT_LOGIN <> '1' AND  " + YHDBUtility.findInSet(deptId, "DEPT_ID_OTHER") + "  order by USER_NO DESC , SEQ_ID";
    Statement stm5 = null;
    ResultSet rs5 = null;
    try {
      stm5 = conn.createStatement();
      rs5 = stm5.executeQuery(query);
      while (rs5.next()) {
        int seqId = rs5.getInt("SEQ_ID");
        if (!this.hasPerson(list, seqId)) {
          Map per = this.rsToMap(rs5);
          list.add(per);
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm5 , rs5 , null);
    }
  }
  public boolean hasPerson(List<Map> list , int userId) {
    for (Map map : list) {
      int seqId = (Integer)map.get("seqId");
      if (userId == seqId) {
        return true;
      }
    }
    return false;
  }
  public String getGroupUser(int groupId, int prcsChoose, int flowId  , YHPerson user , Connection conn) throws Exception {
    YHFlowProcessLogic fpl = new YHFlowProcessLogic();
    //取得下一步骤
    YHDocFlowProcess fp = fpl.getFlowProcessById(flowId, String.valueOf(prcsChoose) , conn);
    String userStr =  fp.getPrcsUser() == null ? "" : fp.getPrcsUser();
    
    String roleStr = fp.getPrcsPriv()  == null ? "" : fp.getPrcsPriv();
   // String deptStr = YHOrgSelectLogic.changeDept(conn, fp.getPrcsDept()); 
    String deptStr = fp.getPrcsDept(); 
    deptStr = deptStr == null ? "" : deptStr;
    int parentId = 0;
    if (userFilter == 20) {
      parentId = this.getParentDept(conn, user);
      if (parentId == 0 ) {
        return "";
      }
    }
    String query = "select USER_STR from oa_person_group where seq_id = " + groupId ;
    String users = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if(rs.next()){
        users = rs.getString("USER_STR");
        if (users == null || "".equals(users)) {
          return "";
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    List<Map> list = this.getPersons(userStr, deptStr, roleStr, conn);
    StringBuffer sb = new StringBuffer();
    int count = 0;
    for(Map tmp : list ){
      String userName = (String)tmp.get("userName");
      int deptId = (Integer)tmp.get("deptId");
      int userDeptId = user.getDeptId();
      String deptIdOther = (String)tmp.get("deptIdOther");
      //只能选择本部门经办人
      if ((userFilter == 1 
          && (deptId != userDeptId
              && !YHWorkFlowUtility.findId(deptIdOther, String.valueOf(userDeptId))))) {
        continue;
      }
      if ((userFilter == 20 
          && (!this.isParentDept(conn, parentId, deptId, deptIdOther)))) {
        continue;
      }
      Integer seqId = (Integer) tmp.get("seqId");
      if (YHWorkFlowUtility.findId(users, String.valueOf(seqId))) {
        this.getMapToJson(sb, tmp);
        count ++;
      }
    }
    if(count > 0 ){
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  /**
   * 通过给定的人员ID串，部门串，权限串，其中这三个是通过AND连接的。取得有员json对象，

   * @param userIds
   * @param deptIds
   * @param privIds
   * @return
   * @throws Exception
   */
  //[{userId:'liuhan1',userName:'liuhan1'},{userId:'liuhan',userName:'liuhan2'},{userId:'liuhan3',userName:'liuhan3'},{userId:'liuhan5',userName:'liuhan7'}];
//  public String getPersonJsonUseAnd(String userIds , String deptIds , String privIds ,  Connection conn , YHPerson user) throws Exception{
//    return this.getPersonSimpleJson(userIds, deptIds, privIds, "AND"  , "OR" , null , -1 , conn , user);
//  }
  /**
   * 取得辅助角色用户
   * @param conn
   * @param roleId
   * @return
   * @throws Exception
   */
  public String getSupplementRoleUser(Connection conn , int roleId) throws Exception{
    StringBuffer sb = new StringBuffer();
    String sql  = "select SEQ_ID , USER_NAME , USER_PRIV_OTHER FROM PERSON WHERE   NOT_LOGIN <> '1'  and USER_PRIV_OTHER LIKE '%" + YHUtility.encodeLike(String.valueOf(roleId)) + "%' "  + YHDBUtility.escapeLike() + "  order by USER_NO DESC ,  SEQ_ID ";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
         String userPrivOther = rs.getString(3);
         if(userPrivOther != null && YHWorkFlowUtility.findId(userPrivOther,String.valueOf(roleId))){
           int userId = rs.getInt(1);
           String userName = rs.getString(2);
           if(!"".equals(sb.toString())){
             sb.append(",");
           }
           sb.append("{userId:\"").append(userId).append("\"")
           .append(",userName:\"").append(userName).append("\"");
           sb.append("}");
         }
      }
      return sb.toString();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  /**
   * 根据部门Id取得人员列表
   * @param dbConn
   * @param deptId
   * @return
   * @throws Exception
   */
  public List<YHPerson> getPersonsByDept(Connection conn, int deptId ) throws Exception{
    List<YHPerson> list = new ArrayList();
    String query = "select  PERSON.SEQ_ID , USER_NAME , DEPT_ID , USER_PRIV , DEPT_ID_OTHER  from PERSON , USER_PRIV where USER_PRIV.SEQ_ID = PERSON.USER_PRIV AND NOT_LOGIN <> '1' AND (DEPT_ID = " + deptId + " or  " + YHDBUtility.findInSet(deptId + "", "DEPT_ID_OTHER") + " )  ORDER BY USER_PRIV.PRIV_NO ,USER_NO DESC , PERSON.SEQ_ID ";
    Statement stm4 = null;
    ResultSet rs4 = null;
    Set set = new HashSet();
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        int seqId = rs4.getInt("SEQ_ID");
       
        if (!set.contains(seqId)) {
          YHPerson per = new YHPerson();
          int deptId1 = rs4.getInt("DEPT_ID");
          
          String userPriv = rs4.getString("USER_PRIV");
          String userName = rs4.getString("USER_NAME");
          
          per.setSeqId(seqId);
          per.setUserName(userName);
          per.setDeptId(deptId1);
          per.setUserPriv(userPriv);
          list.add(per);
          set.add(seqId);
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    
//    query = "select PERSON.SEQ_ID  , USER_NAME , DEPT_ID , USER_PRIV , DEPT_ID_OTHER  from PERSON  , USER_PRIV where  USER_PRIV.SEQ_ID = PERSON.USER_PRIV AND  NOT_LOGIN <> '1' AND  DEPT_ID_OTHER like '%," + YHUtility.encodeLike(String.valueOf(deptId)) + ",%' "+YHDBUtility.escapeLike()+" or DEPT_ID_OTHER like '" + YHUtility.encodeLike(String.valueOf(deptId))  + ",%' "+ YHDBUtility.escapeLike() +" or DEPT_ID_OTHER like '%," + YHUtility.encodeLike(String.valueOf(deptId)) + "' "+ YHDBUtility.escapeLike() + "  ORDER BY USER_PRIV.PRIV_NO ,USER_NO DESC,PERSON.SEQ_ID";
//    Statement stm5 = null;
//    ResultSet rs5 = null;
//    Set set2 = new HashSet();
//    try {
//      stm5 = conn.createStatement();
//      rs5 = stm5.executeQuery(query);
//      while (rs5.next()) {
//        String deptIdOther = rs5.getString("DEPT_ID_OTHER");
//        if (YHWorkFlowUtility.findId(deptIdOther, String.valueOf(deptId))) {
//          int seqId = rs5.getInt("SEQ_ID");
//          if (!set2.contains(seqId)) {
//            YHPerson per = new YHPerson();
//            int deptId1 = rs5.getInt("DEPT_ID");
//            String userPriv = rs5.getString("USER_PRIV");
//            String userName = rs5.getString("USER_NAME");
//            
//            per.setSeqId(seqId);
//            per.setUserName(userName);
//            per.setDeptId(deptId1);
//            per.setUserPriv(userPriv);
//            list.add(per);
//            set2.add(seqId);
//          }
//        }
//      }
//    }catch(Exception ex) {
//      throw ex;
//    }finally {
//      YHDBUtility.close(stm5 , rs5 , null);
//    }
    return  list;
  }
  /**
   * 根据辅助角色Id取得人员列表
   * @param dbConn
   * @param deptId
   * @return
   * @throws Exception
   */
  public List<YHPerson> getPersonsBySupplementRole(Connection conn, int roleId , YHPerson user ,int parentId) throws Exception{
    List<YHPerson> list = new ArrayList();
    String query = "select SEQ_ID " 
      + ", USER_NAME  " 
      + ", DEPT_ID  " 
      + ", USER_PRIV  " 
      + ", USER_PRIV_OTHER   " 
      + ", DEPT_ID_OTHER   " 
      + " from PERSON where   " 
      + " NOT_LOGIN <> '1'  " 
      + " AND  USER_PRIV_OTHER like '%," +YHUtility.encodeLike(String.valueOf(roleId))  + ",%' "  + YHDBUtility.escapeLike()
      + " or USER_PRIV_OTHER like '" + YHUtility.encodeLike(String.valueOf(roleId)) + ",%' "  + YHDBUtility.escapeLike()
      + " or USER_PRIV_OTHER like '%," + YHUtility.encodeLike(String.valueOf(roleId)) + "' "  + YHDBUtility.escapeLike()
      + "  order by USER_NO DESC , SEQ_ID";
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        int seqId = rs4.getInt("SEQ_ID");
        int deptId1 = rs4.getInt("DEPT_ID");
        String userPriv = rs4.getString("USER_PRIV");
        String userName = rs4.getString("USER_NAME");
        String userPrivOther = rs4.getString("USER_PRIV_OTHER");
        String deptIdOther = rs4.getString("DEPT_ID_OTHER");
        int userDeptId = user.getDeptId();
        //只能选择本部门经办人
        if (userFilter == 1 
            && (deptId1 != userDeptId
                && !YHWorkFlowUtility.findId(deptIdOther, String.valueOf(userDeptId)))) {
          continue;
        }
       //只能选择上一级部门经办人
        if (userFilter == 20 
            && (!this.isParentDept(conn, parentId, deptId1, deptIdOther))) {
          continue;
        }
        if (!YHWorkFlowUtility.findId(userPrivOther, String.valueOf(roleId))) {
          continue; 
        }
        YHPerson per = new YHPerson();
        per.setSeqId(seqId);
        per.setUserName(userName);
        per.setDeptId(deptId1);
        per.setUserPriv(userPriv);
        per.setDeptIdOther(deptIdOther);
        list.add(per);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return  list;
  }
  /**
   * 根据角色Id取得人员列表
   * @param dbConn
   * @param deptId
   * @return
   * @throws Exception
   */
  public List<YHPerson> getPersonsByRole(Connection conn, int roleId , YHPerson user , int parentId) throws Exception{
    List<YHPerson> list = new ArrayList();
    String query = "select SEQ_ID , USER_NAME , DEPT_ID , USER_PRIV  , DEPT_ID_OTHER from PERSON where NOT_LOGIN <> '1' AND USER_PRIV = '" + roleId + "' order by USER_NO DESC,SEQ_ID";
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        int seqId = rs4.getInt("SEQ_ID");
        int deptId1 = rs4.getInt("DEPT_ID");
        String userPriv = rs4.getString("USER_PRIV");
        String userName = rs4.getString("USER_NAME");
        String deptIdOther = rs4.getString("DEPT_ID_OTHER");
        int userDeptId = user.getDeptId();
        //只能选择本部门经办人
        if (userFilter == 1 
            && (deptId1 != userDeptId
                && !YHWorkFlowUtility.findId(deptIdOther, String.valueOf(userDeptId)))) {
          continue;
        }
      //只能选择上一级部门经办人
        if (userFilter ==20 
            && (!this.isParentDept(conn, parentId, deptId1, deptIdOther))) {
          continue;
        }
        YHPerson per = new YHPerson();
        per.setSeqId(seqId);
        per.setUserName(userName);
        per.setDeptId(deptId1);
        per.setUserPriv(userPriv);
        per.setDeptIdOther(deptIdOther);
        list.add(per);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return  list;
   
  }
  public static void main(String[] args) {
  }
}
