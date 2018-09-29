package yh.core.module.org_select.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import yh.core.global.YHSysProps;
import yh.core.util.db.YHDBUtility;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.data.YHUserGroup;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
public class YHOrgSelect2Logic {
	/**
	   * 判断是否在我的权限范围内
	   */
  public static void isMyRole(Connection conn , String moduleId , int userId , int deptId){
  }
  
  public static void isParentDept(){
  }
  
  public void getDeptUser(Connection conn, int deptId , int userId){
    
  }
  /**
   * 取得当前部门的所有用户

   * @param conn
   * @param deptId
   * @return
   * @throws Exception
   */
  public  ArrayList<YHPerson> getDeptUser(Connection conn, int deptId , boolean notLoginIn) throws Exception{
    String query = "select PERSON.SEQ_ID, USER_NAME, DEPT_ID, USER_PRIV, TEL_NO_DEPT, EMAIL, ICQ, MY_STATUS, USER_ID from PERSON , USER_PRIV where USER_PRIV.SEQ_ID = PERSON.USER_PRIV "; 
    if (!notLoginIn) {
      query += " AND NOT_LOGIN <> '1' " ;
    }
    query += " AND (DEPT_ID=" + deptId + " or " + YHDBUtility.findInSet(String.valueOf(deptId), "DEPT_ID_OTHER")+ ")  order by USER_PRIV.PRIV_NO , PERSON.USER_NO DESC  ,PERSON.SEQ_ID";
    ArrayList<YHPerson> persons = new ArrayList();
    Statement stm4 = null;
    ResultSet rs4 = null;
    Set set = new HashSet();
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        YHPerson person = new YHPerson();
        int seqId = rs4.getInt("SEQ_ID");
        if (!set.contains(seqId)) {
          person.setSeqId(seqId);
          person.setUserName(rs4.getString("USER_NAME"));
          person.setDeptId(rs4.getInt("DEPT_ID"));
          person.setUserPriv(rs4.getString("USER_PRIV"));
          person.setTelNoDept(rs4.getString("TEL_NO_DEPT"));
          person.setEmail(rs4.getString("EMAIL"));
          person.setIcq(rs4.getString("ICQ"));
          person.setMyStatus(rs4.getString("MY_STATUS"));
          person.setUserId(rs4.getString("USER_ID"));
          persons.add(person);
          set.add(seqId);
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return persons;
  }
  
  /**
   * 取得当前部门的所有用户(不对禁止登录的用户进行控制)

   * @param conn
   * @param deptId
   * @return
   * @throws Exception
   */
  
  public  ArrayList<YHPerson> getDeptUser2(Connection conn, int deptId) throws Exception{
    String query = "select PERSON.SEQ_ID, USER_NAME, DEPT_ID, USER_PRIV, TEL_NO_DEPT, EMAIL, ICQ, MY_STATUS, USER_ID from PERSON , USER_PRIV where USER_PRIV.SEQ_ID = PERSON.USER_PRIV AND  (DEPT_ID=" + deptId + " or " + YHDBUtility.findInSet(String.valueOf(deptId), "DEPT_ID_OTHER") + ") order by USER_PRIV.PRIV_NO , PERSON.USER_NO DESC ,PERSON.SEQ_ID";
    ArrayList<YHPerson> persons = new ArrayList();
    Statement stm4 = null;
    ResultSet rs4 = null;
    Set set = new HashSet();
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        YHPerson person = new YHPerson();
        int seqId = rs4.getInt("SEQ_ID");
        if (!set.contains(seqId)) {
          person.setSeqId(seqId);
          person.setUserName(rs4.getString("USER_NAME"));
          person.setDeptId(rs4.getInt("DEPT_ID"));
          person.setUserPriv(rs4.getString("USER_PRIV"));
          person.setTelNoDept(rs4.getString("TEL_NO_DEPT"));
          person.setEmail(rs4.getString("EMAIL"));
          person.setIcq(rs4.getString("ICQ"));
          person.setMyStatus(rs4.getString("MY_STATUS"));
          person.setUserId(rs4.getString("USER_ID"));
          persons.add(person);
          set.add(seqId);
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return persons;
  }
  /**
   * 取得当前部门的所有子部门
   * @param conn
   * @param parentDeptId
   * @return
   * @throws Exception
   */
  public ArrayList<YHDepartment> getChildDept(Connection conn , int parentDeptId) throws Exception{
    String query = "select SEQ_ID , DEPT_NAME from oa_department where DEPT_PARENT = " + parentDeptId;
    ArrayList<YHDepartment> depts = new ArrayList();
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        YHDepartment dept  = new YHDepartment();
        dept.setSeqId(rs4.getInt("SEQ_ID"));
        dept.setDeptName(rs4.getString("DEPT_NAME"));
        depts.add(dept);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return depts;
  }
  /**
   * 组装成json数据（包含当前部门）
   * @param conn
   * @param deptId
   * @param deptName
   * @return
   * @throws Exception
   */
  public StringBuffer deptUser2Json(Connection conn, int deptId , String deptName,int childNum,YHMyPriv mp,YHPerson loginPerson , boolean isModule , boolean notLoginIn) throws Exception{
    StringBuffer sb = new StringBuffer();
    StringBuffer users = new StringBuffer();
    ArrayList<YHPerson> persons = getDeptUser(conn, deptId , notLoginIn);
    sb.append("{")
    .append("deptName:\"").append(deptNameRender(deptName, childNum)).append("\"").append(",user:[");
    for (int i = 0; i < persons.size(); i++) {
      YHPerson person = persons.get(i);
      if(isModule && !YHPrivUtil.isUserPriv(conn, person.getSeqId(), mp,  loginPerson.getPostPriv(), loginPerson.getPostDept(), loginPerson.getSeqId(), loginPerson.getDeptId())){
        continue;
      }
      int isOnline = isUserOnline(conn, person.getSeqId());
      String userNameRender = YHUtility.encodeSpecial(person.getUserName()) ;
      if(!"".equals(users.toString())){
        users.append(",");
      }
      users.append("{userId:\"").append(person.getSeqId()).append("\"")
        .append(",userName:\"").append(userNameRender).append("\"")
        .append(",isOnline:\"").append(isOnline).append("\"")
        .append("}");
    }
    sb.append(users).append("]}");
    ArrayList<YHDepartment> depts = getChildDept(conn, deptId); 
    for (int i = 0; i < depts.size(); i++) {
      YHDepartment dept = depts.get(i);
      int childDeptId = dept.getSeqId();
      String childDeptName = dept.getDeptName();
      StringBuffer childSb = deptUser2Json(conn, childDeptId,childDeptName,childNum + 1 ,mp,loginPerson , isModule , notLoginIn);
      if(!"".equals(sb.toString()) && !"".equals(childSb.toString())){
        sb.append(",");
      }
      sb.append(childSb);
    }
    return sb;
  }
  
  /**
   * 组装成json数据（包含当前部门），deptId=0的情况下
   * add by jzk
 * @param conn
 * @param deptName
 * @param childNum
 * @param mp
 * @param loginPerson
 * @param isModule
 * @param notLoginIn
 * @return
 * @throws Exception
 */
public StringBuffer deptUser2Json(Connection conn, String deptName,int childNum,YHMyPriv mp,YHPerson loginPerson , boolean isModule , boolean notLoginIn) throws Exception{
	  StringBuffer sb = new StringBuffer();
	  ArrayList<YHDepartment> depts = getChildDept(conn, 0); 
	    for (int i = 0; i < depts.size(); i++) 
	    {
	      YHDepartment dept = depts.get(i);
	      int childDeptId = dept.getSeqId();
	      String childDeptName = dept.getDeptName();
	      StringBuffer childSb = deptUser2JsonAll(conn, childDeptId,childDeptName,childNum + 1 ,mp,loginPerson , isModule , notLoginIn);
	      if(!"".equals(sb.toString()) && !"".equals(childSb.toString())){
	        sb.append(";").append(childSb);
	      }
	      if("".equals(sb.toString()) && !"".equals(childSb.toString()))
	      {
	    	  sb.append(childSb);
	      }
	    }
	    
	  //去掉sb中相同的项�
	    Set<String> set = new HashSet<String>();
	    String[] strArr = sb.toString().split(";");
	    int length = strArr.length;
	    for(int i=0;i<length;i++){
	    	set.add(strArr[i].trim());
	    }
	    sb = new StringBuffer();
	    Iterator<String> it = set.iterator();
	    while(it.hasNext()){
	    	String str = it.next();
	    	if(!"".equals(sb.toString())){
	    		sb.append(",");
	    	}
	    	sb.append(str);
	    }
	  return sb;
  }

/**
 *  组装成json数据（包含当前部门），deptId=0的情况下
 * add by jzk
 * @param conn
 * @param deptId
 * @param deptName
 * @param childNum
 * @param mp
 * @param loginPerson
 * @param isModule
 * @param notLoginIn
 * @return
 * @throws Exception
 */
public StringBuffer deptUser2JsonAll(Connection conn, int deptId , String deptName,int childNum,YHMyPriv mp,YHPerson loginPerson , boolean isModule , boolean notLoginIn) throws Exception{
    StringBuffer sb = new StringBuffer();
    StringBuffer users = new StringBuffer();
    ArrayList<YHPerson> persons = getDeptUser(conn, deptId , notLoginIn);
    for (int i = 0; i < persons.size(); i++) {
      YHPerson person = persons.get(i);
      if(isModule && !YHPrivUtil.isUserPriv(conn, person.getSeqId(), mp,  loginPerson.getPostPriv(), loginPerson.getPostDept(), loginPerson.getSeqId(), loginPerson.getDeptId())){
        continue;
      }
      int isOnline = isUserOnline(conn, person.getSeqId());
      String userNameRender = YHUtility.encodeSpecial(person.getUserName()) ;
      if(!"".equals(users.toString())){
          users.append(";");
        }
      users.append("{userId:\"").append(person.getSeqId()).append("\"")
        .append(",userName:\"").append(userNameRender).append("\"")
        .append(",isOnline:\"").append(isOnline).append("\"")
        .append("}");
    }
    sb.append(users);
    ArrayList<YHDepartment> depts = getChildDept(conn, deptId); 
    for (int i = 0; i < depts.size(); i++) {
      YHDepartment dept = depts.get(i);
      int childDeptId = dept.getSeqId();
      String childDeptName = dept.getDeptName();
      StringBuffer childSb = deptUser2JsonAll(conn, childDeptId,childDeptName,childNum + 1 ,mp,loginPerson , isModule , notLoginIn);
      if(!"".equals(sb.toString()) && !"".equals(childSb.toString())){
        sb.append(";");
      }
      sb.append(childSb);
    }
    return sb;
  }


/**
 * 组装成json数据（包含当前部门）
 * @param conn
 * @param deptId
 * @return
 * @throws Exception
 */
  public StringBuffer deptUser2Json(Connection conn, int deptId ,YHMyPriv mp,YHPerson person , boolean isModule , boolean notLoginIn) throws Exception{
    String deptName = "";
    StringBuffer result = new StringBuffer();
    if(deptId == 0){
      deptName = "鍏ㄤ綋閮ㄩ棬";
      result.append("[{deptName:\"鍏ㄤ綋閮ㄩ棬\",user:[").append(deptUser2Json(conn,deptName,0,mp,person , isModule , notLoginIn)).append("]}]");
    }else{
      deptName =  getDeptNameById(conn, deptId);
      result.append("[").append(deptUser2Json(conn, deptId,deptName,0,mp,person , isModule , notLoginIn)).append("]");
    }
    return result;
  }
  public String getDeptNameById(Connection conn, int deptId ) throws Exception{
    String query = "select DEPT_NAME from oa_department where SEQ_ID=" + deptId;
    Statement stm4 = null;
    ResultSet rs4 = null;
    String deptName = "";
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      if (rs4.next()) {
        deptName = rs4.getString("DEPT_NAME");
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return deptName;
  }
  /**
   * 判断当前用户是否在线
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public int  isUserOnline(Connection conn, int userId) throws Exception{
    String query = "select 1 from oa_online where USER_ID = " + userId;
    Statement stm4 = null;
    ResultSet rs4 = null;
    boolean flag = false;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      if (rs4.next()) {
        flag = true;
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    if(flag){
     return 1; 
    }else{
      return 0;
    }
  }
  /**
   * 组织部门名称
   * @param deptName
   * @param childNum
   * @return
   */
  public String deptNameRender(String deptName ,int childNum){
    String result = "";
    for(int i = 0 ; i < childNum ; i ++){
      result += "&nbsp;";
    }
    if(childNum > 0){
      result += "├";
    }
    result += YHUtility.encodeSpecial(deptName);
    return result;
  }
  /**
   * 通过名称得到用户
   * @param conn
   * @param userName
   * @param notLoginIn 
   * @return
   * @throws Exception
   */
  public ArrayList<YHPerson> getUserByName(Connection conn , String userName, boolean notLoginIn) throws Exception{
    
    String query = "select SEQ_ID , USER_NAME , DEPT_ID from PERSON where 1=1 " ;
    if (!notLoginIn)
      query+= " AND NOT_LOGIN <> '1' ";
      
      query += " AND  "
      +" (USER_NAME LIKE '%" + YHUtility.encodeLike(userName)  + "%' " + YHDBUtility.escapeLike()
      +" or USER_ID like '%" + YHUtility.encodeLike(userName)  + "%' "+ YHDBUtility.escapeLike() +")";
    ArrayList<YHPerson> persons = new ArrayList();
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        YHPerson person = new YHPerson();
        person.setSeqId(rs4.getInt("SEQ_ID"));
        person.setUserName(rs4.getString("USER_NAME"));
        person.setDeptId(rs4.getInt("DEPT_ID"));
        persons.add(person);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return persons;
  }
  /**
   * 查询得到用户
   * @param conn
   * @param queryName
   * @param notLoginIn 
   * @return
   * @throws Exception
   */
  public StringBuffer getQueryUser2Json(Connection conn , String queryName, YHPerson loginUser, boolean hasModule  , YHMyPriv mp, boolean notLoginIn) throws Exception{
    StringBuffer user = new StringBuffer();
    StringBuffer sb = new StringBuffer();
    ArrayList<YHPerson> persons = getUserByName(conn, queryName ,notLoginIn);
    for (int i = 0; i < persons.size(); i++) {
      YHPerson person = persons.get(i);
      String userName = person.getUserName();
      int userId = person.getSeqId();
      if(hasModule && !YHPrivUtil.isUserPriv(conn
          , userId
          , mp
          , loginUser.getPostPriv()
          , loginUser.getPostDept()
          , loginUser.getSeqId()
          , loginUser.getDeptId())){
        continue;
      }
      int deptId = person.getDeptId();
      if (deptId != 0) {
        String deptName = getDeptNameById(conn, deptId);
        if(!"".equals(sb.toString())){
          sb.append(",");
        }
        sb.append(user2Json(conn, userId, deptId, deptName, userName,true));
      }
    }
    user.append("[").append(sb).append("]");
    return user;
  }
  /**
   * 得到在线用户
   * @param conn
   * @param queryName
   * @return
   * @throws Exception
   */
  public StringBuffer getOnlineUser2Json(Connection conn, YHPerson user1, boolean hasModule , YHMyPriv mp ) throws Exception{
    StringBuffer user = new StringBuffer();
    StringBuffer sb = new StringBuffer();
    ArrayList<YHPerson> persons = getOnlineUser(conn ,  user1,  hasModule ,  mp );
    for (int i = 0; i < persons.size(); i++) {
      YHPerson person = persons.get(i);
      String userName = person.getUserName();
      int userId = person.getSeqId();
      int deptId = person.getDeptId();
      String deptName = getDeptNameById(conn, deptId);
      if(!"".equals(sb.toString())){
        sb.append(",");
      }
      sb.append(user2Json(conn, userId, deptId, deptName, userName,false));
    }
    user.append("[").append(sb).append("]");
    return user;
  }
  /**
   * 
   * @param conn
   * @return
   * @throws Exception
   */
  public ArrayList<YHPerson> getOnlineUser(Connection conn , YHPerson user, boolean hasModule , YHMyPriv mp ) throws Exception{
    ArrayList<YHPerson> persons = new ArrayList<YHPerson>(); 
    List onlines = new ArrayList();
    String query = "select distinct(USER_ID) from oa_online where USER_STATE = '1' or USER_STATE = '2' OR USER_STATE = '3'";
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        int userId = rs4.getInt("USER_ID");
        if(hasModule && !YHPrivUtil.isUserPriv(conn
            , userId
            , mp
            , user.getPostPriv()
            , user.getPostDept()
            , user.getSeqId()
            , user.getDeptId())){
          continue;
        }
        onlines.add(userId);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    for (int i = 0; i < onlines.size(); i++) {
      int seqId = (Integer) onlines.get(i);
      String query1 = "select SEQ_ID , USER_NAME , DEPT_ID from PERSON where  SEQ_ID =" + seqId;
      Statement stm5 = null;
      ResultSet rs5 = null;
      try {
        stm5 = conn.createStatement();
        rs5 = stm5.executeQuery(query1);
        while (rs5.next()) {
          YHPerson person = new YHPerson();
          person.setSeqId(rs5.getInt("SEQ_ID"));
          person.setUserName(rs5.getString("USER_NAME"));
          person.setDeptId(rs5.getInt("DEPT_ID"));
          persons.add(person);
        }
      }catch(Exception ex) {
        throw ex;
      }finally {
        YHDBUtility.close(stm5 , rs5 , null);
      }
    }
    return persons;
  }
  /**
   * 组装JSON数据
   * @param conn
   * @param userId
   * @param deptId
   * @param deptName
   * @param userName
   * @return
   * @throws Exception
   */
  public StringBuffer user2Json(Connection conn ,int userId , int deptId , String deptName , String userName,boolean isOnline) throws Exception{
    StringBuffer sb = new StringBuffer();
    int online = 0;
  //需要叛断是否在线的
    if(isOnline){
      online = isUserOnline(conn, userId);
    }
    sb.append("{deptName:\"").append(YHUtility.encodeSpecial(deptName)).append("\"")
    .append(",userId:\"").append(userId).append("\"")
    .append(",userName:\"").append(YHUtility.encodeSpecial(userName)).append("\"");
    if (isOnline) {
      sb.append(",isOnline:\"").append(online).append("\"");
    }
    sb.append("}");
    return sb;
  }
  /**
   * 角色JSON数据组织
   * @param conn
   * @param userId
   * @param userName
   * @param isOnline
   * @return
   * @throws Exception
   */
  public StringBuffer role2Json(Connection conn ,int userId , String userName,boolean isOnline) throws Exception{
    StringBuffer sb = new StringBuffer();
    int online = 0;
  //需要叛断是否在线的
    if(isOnline){
      online = isUserOnline(conn, userId);
    }
    sb.append("{userId:\"").append(userId).append("\"")
    .append(",userName:\"").append(YHUtility.encodeSpecial(userName)).append("\"");
    if (isOnline) {
      sb.append(",isOnline:\"").append(online).append("\"");
    }
    sb.append("}");
    return sb;
  }
  /**
   * 取得主角色用户

   * @param conn
   * @param roleId
   * @param notLoginIn 
   * @return
   * @throws Exception
   */
  public StringBuffer getPrincipalRoleUser(Connection conn, int roleId  , YHPerson user, boolean hasModule , YHMyPriv mp, boolean notLoginIn ) throws Exception{
    StringBuffer sb = new StringBuffer();
    StringBuffer result = new StringBuffer();
    
    String sql  = "select SEQ_ID , USER_NAME FROM PERSON WHERE   USER_PRIV='" + roleId + "' ";
    if (!notLoginIn) {
      sql +=  " AND NOT_LOGIN <> '1'";
    }
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        int userId = rs.getInt(1);
        if(hasModule && !YHPrivUtil.isUserPriv(conn
            , userId
            , mp
            , user.getPostPriv()
            , user.getPostDept()
            , user.getSeqId()
            , user.getDeptId())){
          continue;
        }
        String userName = rs.getString(2);
        if(!"".equals(sb.toString())){
          sb.append(",");
         }
         sb.append(role2Json(conn, userId, userName, true));
      }
      result.append("[").append(sb).append("]");
      return result;
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  /**
   * 取得辅助角色用户
   * @param conn
   * @param roleId
   * @param notLoginIn 
   * @return
   * @throws Exception
   */
  public StringBuffer getSupplementRoleUser(Connection conn , int roleId, YHPerson user, boolean hasModule  , YHMyPriv mp, boolean notLoginIn) throws Exception{
    StringBuffer sb = new StringBuffer();
    StringBuffer result = new StringBuffer();
    String sql  = "select SEQ_ID , USER_NAME , USER_PRIV_OTHER FROM PERSON WHERE 1=1  " ;
     if (!notLoginIn) {
       sql += " AND NOT_LOGIN <> '1' ";
     }
     sql+= " AND  USER_PRIV_OTHER LIKE '%" + YHUtility.encodeLike(String.valueOf(roleId)) + "%' " + YHDBUtility.escapeLike();
    
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
         String userPrivOther = rs.getString(3);
         if(userPrivOther != null 
             && findId(userPrivOther,roleId,",")){
           int userId = rs.getInt(1);
           if(hasModule && !YHPrivUtil.isUserPriv(conn, userId, mp, user.getPostPriv(), user.getPostDept(), user.getSeqId(), user.getDeptId())){
             continue;
           }
           String userName = rs.getString(2);
           if(!"".equals(sb.toString())){
             sb.append(",");
           }
           sb.append(role2Json(conn, userId, userName, false));
         }
      }
      result.append("[").append(sb).append("]");
      return result;
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  /**
   * 按角色选取人员
   * @param conn
   * @param roleId
   * @param notLoginIn 
   * @return
   * @throws Exception
   */
  public StringBuffer getRoleUser(Connection conn, int roleId , YHPerson user, boolean hasModule  , YHMyPriv mp, boolean notLoginIn) throws Exception{
    StringBuffer result = new StringBuffer("{");
    StringBuffer principalRole = getPrincipalRoleUser(conn, roleId , user , hasModule , mp , notLoginIn);
    StringBuffer supplementRole = getSupplementRoleUser(conn, roleId, user , hasModule , mp , notLoginIn);
    result.append("principalRole:").append(principalRole)
      .append(",supplementRole:").append(supplementRole)
      .append("}");
    return result;
  }
  /**
   * 查询ID  
   * @param str
   * @param id
   * @param reg
   * @return
   */
  public boolean findId(String str , int id,String reg){
    String[] strs = str.split(reg);
    for (int i = 0; i < strs.length; i++) {
      if (YHUtility.isInteger(strs[i])) {
        int tempId = Integer.parseInt(strs[i]);
        if(tempId == id){
          return true;
        }
      }
    }
    return false;
  }
  
  /**
   * 得到分组用户
   * @param conn
   * @param notLoginIn 
   * @return
   * @throws Exception
   */
  public ArrayList<YHPerson> getGorupUser(Connection conn, int groupId, boolean notLoginIn) throws Exception{
    YHORM orm = new YHORM();
    ArrayList<YHPerson> persons = new ArrayList<YHPerson>(); 
    YHUserGroup group = (YHUserGroup) orm.loadObjSingle(conn, YHUserGroup.class, groupId);
    if (group != null) {
      String userIdStrs = group.getUserStr();
      if(userIdStrs != null){
        String[] userIds = userIdStrs.split(",");
        for (int i = 0; i < userIds.length; i++) {
          if(YHUtility.isInteger(userIds[i])) {
            int userId = Integer.parseInt(userIds[i]);
            YHPerson person = this.getPersonById(conn, userId ,notLoginIn);
            if (person != null) {
              persons.add(person);
            }
          }
        }
      }
    }
    return persons;
  }
  public YHPerson getPersonById(Connection conn , int seqId ,boolean notLoginIn) throws Exception {
    String query = "select SEQ_ID , USER_NAME , DEPT_ID , USER_PRIV  from PERSON where  seq_id = " + seqId ;
    if (!notLoginIn) {
      query += " and NOT_LOGIN <> '1' ";
    }
    
    Statement stm4 = null;
    ResultSet rs4 = null;
    YHPerson per = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      if (rs4.next()) {
        per = new YHPerson();
        int deptId1 = rs4.getInt("DEPT_ID");
        String userPriv = rs4.getString("USER_PRIV");
        String userName = rs4.getString("USER_NAME");
        
        per.setSeqId(seqId);
        per.setUserName(userName);
        per.setDeptId(deptId1);
        per.setUserPriv(userPriv);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return per ;
  }
  /**
   * 得到在线用户
   * @param conn
   * @param notLoginIn 
   * @param queryName
   * @return
   * @throws Exception
   */
  public StringBuffer getGorupUser2Json(Connection conn , int groupId, YHPerson loginUser, boolean hasModule  , YHMyPriv mp, boolean notLoginIn) throws Exception{
    StringBuffer user = new StringBuffer();
    StringBuffer sb = new StringBuffer();
    ArrayList<YHPerson> persons = getGorupUser(conn, groupId , notLoginIn);
    for (int i = 0; i < persons.size(); i++) {
      YHPerson person = persons.get(i);
      String userName = person.getUserName();
      int userId = person.getSeqId();
      if(hasModule && !YHPrivUtil.isUserPriv(conn, userId, mp, loginUser.getPostPriv(), loginUser.getPostDept(), loginUser.getSeqId(), loginUser.getDeptId())){
        continue;
      }
      int deptId = person.getDeptId();
      String deptName = getDeptNameById(conn, deptId);
      if(!"".equals(sb.toString())){
        sb.append(",");
      }
      sb.append(user2Json(conn, userId, deptId, deptName, userName,true));
    }
    user.append("[").append(sb).append("]");
    return user;
  }
  public String getUserState(String id , Connection conn) throws Exception {
    String c = "0" ;
    String query = "select USER_STATE from oa_online where USER_ID = " + id;
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      if (rs4.next()) {
        c = rs4.getString("USER_STATE");
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return c ;
  }
  public String getPersons(String ids , Connection conn) throws Exception  {
    if (YHUtility.isNullorEmpty(ids)) {
      return "";
    }
    if (ids.endsWith(",")) {
      ids = ids.substring(0, ids.length() - 1);
    }
    String query = "select PERSON.SEQ_ID , USER_NAME , USER_STATE from PERSON LEFT OUTER JOIN oa_online as USER_ONLINE ON USER_ONLINE.USER_ID = PERSON.SEQ_ID where PERSON.SEQ_ID IN  (" + ids + ")";
    Statement stm4 = null;
    ResultSet rs4 = null;
    StringBuffer sb = new StringBuffer();
    int count = 0 ;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        int userId = rs4.getInt("SEQ_ID");
        String userName = rs4.getString("USER_NAME");
        String userState = rs4.getString("USER_STATE");
        if (YHUtility.isNullorEmpty(userState)) {
          userState = "0";
        }
        sb.append("{userId:\"").append(userId).append("\",userName:\"").append(YHUtility.encodeSpecial(userName)).append("\",isOnline:").append(userState).append("},");
        count++;
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  public String getStates(String ids , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    if (ids == null || "".equals(ids)) {
      return "";
    }
    String[] aId = ids.split(",");
    String userState = "";
    for (String id : aId) {
      if (YHUtility.isInteger(id)) {
        userState += this.getUserState(id, conn) + ",";
      }
    }
    return userState;
  }
  
  /**
   * 根据部门Id取得人员列表
   * @param dbConn
   * @param deptId
   * @return
   * @throws Exception
   */
  public List<YHPerson> getPersonsByDept(Connection conn, int deptId  , boolean notLoginIn) throws Exception{
    List<YHPerson> list = new ArrayList();
    String query = "select PERSON.SEQ_ID , USER_NAME , DEPT_ID  , USER_PRIV  from PERSON , USER_PRIV where USER_PRIV.SEQ_ID = PERSON.USER_PRIV " ;
    if (!notLoginIn) {
      query += " AND NOT_LOGIN <> '1' ";
    }
    //判断项目运行是那种数据库，选择用户排序的SQL的语句不一样 shenrm 2012-12-19
    String type = YHSysProps.getProp("db.jdbc.dbms"); //获取相应的数据库类型
    if("oracle".equals(type)) //oracle 数据库  NLSSORT(PERSON.USER_NAME,'NLS_SORT=SCHINESE_PINYIN_M') ASC
    {
    	query += " AND (DEPT_ID = " + deptId + "  or " + YHDBUtility.findInSet(String.valueOf(deptId), "DEPT_ID_OTHER")+ ")  order by USER_PRIV.PRIV_NO ,NLSSORT(PERSON.USER_NAME,'NLS_SORT=SCHINESE_PINYIN_M') ASC,PERSON.USER_NO DESC ,PERSON.SEQ_ID";
    }else if("mysql".equals(type)) //mysql 数据库 CONVERT(PERSON.USER_NAME USING GB2312) ASC
    {
    	query += " AND (DEPT_ID = " + deptId + "  or " + YHDBUtility.findInSet(String.valueOf(deptId), "DEPT_ID_OTHER")+ ")  order by USER_PRIV.PRIV_NO ,CONVERT(PERSON.USER_NAME USING GB2312) ASC,PERSON.USER_NO DESC ,PERSON.SEQ_ID";
    }    
    //System.out.print(query);
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
    return  list;
  }
  
  public String getDeptName(Connection dbConn, int deptId){
    YHOrgSelectLogic deptNameLogic = new YHOrgSelectLogic();
    String deptNameStr = "";
    try {
      deptNameStr = deptNameLogic.getDeptNameLogic(dbConn, deptId);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return deptNameStr;
    
  }
  
  public String getRoleName(Connection dbConn, int roleId){
    YHOrgSelectLogic deptNameLogic = new YHOrgSelectLogic();
    String roleNameStr = "";
    try {
      roleNameStr = deptNameLogic.getRoleNameLogic(dbConn, roleId);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return roleNameStr;
    
  }
  
}
