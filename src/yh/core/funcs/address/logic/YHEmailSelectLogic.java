package yh.core.funcs.address.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.data.YHUserGroup;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
public class YHEmailSelectLogic {
  /**
   * 取得当前部门的所有用户   * @param conn
   * @param deptId
   * @return
   * @throws Exception
   */
  public  ArrayList<YHPerson> getDeptUser(Connection conn, int deptId , boolean notLoginIn) throws Exception{
    String query = "select PERSON.SEQ_ID, USER_NAME, DEPT_ID, USER_PRIV, TEL_NO_DEPT, EMAIL, ICQ, MY_STATUS, USER_ID from person as PERSON , USER_PRIV where USER_PRIV.SEQ_ID = PERSON.USER_PRIV "; 
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
      if(!"".equals(sb.toString()) && !"".equals(childSb)){
        sb.append(",");
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
    if(deptId == 0){
      deptName = "全体部门";
    }else{
      deptName =  getDeptNameById(conn, deptId);
    }
    StringBuffer result = new StringBuffer();
    result.append("[").append(deptUser2Json(conn, deptId,deptName,0,mp,person , isModule , notLoginIn)).append("]");
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
    
    String query = "select SEQ_ID , USER_NAME , DEPT_ID , EMAIL from PERSON where 1=1 " ;
    if (!notLoginIn)
      query+= " AND NOT_LOGIN <> '1' ";
      
      query += " AND  "
      +" (USER_NAME LIKE '%" + YHUtility.encodeLike(userName)  + "%' " + YHDBUtility.escapeLike()
      + " OR EMAIL LIKE '%" + YHUtility.encodeLike(userName)  + "%' " + YHDBUtility.escapeLike()
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
        person.setEmail(rs4.getString("EMAIL"));
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
        //sb.append(user2Json(conn, userId, deptId, deptName, userName,true));
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
  public StringBuffer getOnlineUserEmail2Json(Connection conn, YHPerson user1) throws Exception{
    StringBuffer user = new StringBuffer();
    StringBuffer sb = new StringBuffer();
    ArrayList<YHPerson> persons = getOnlineUser(conn ,  user1 );
    Set set = new HashSet();
    for (int i = 0; i < persons.size(); i++) {
      YHPerson person = persons.get(i);
      String userName = person.getUserName();
      int userId = person.getSeqId();
      int deptId = person.getDeptId();
      String deptName = getDeptNameById(conn, deptId);
      String email = person.getEmail();
      if (!YHUtility.isNullorEmpty(email)
          && !set.contains(email)) {
        if(!"".equals(sb.toString())){
          sb.append(",");
        }
        sb.append(user2Json(conn, userId, userName, person.getEmail()));
        set.add(email);
      }
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
  public ArrayList<YHPerson> getOnlineUser(Connection conn , YHPerson user ) throws Exception{
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
        onlines.add(userId);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    for (int i = 0; i < onlines.size(); i++) {
      int seqId = (Integer) onlines.get(i);
      String query1 = "select SEQ_ID , USER_NAME , DEPT_ID , EMAIL from PERSON where  SEQ_ID =" + seqId;
      Statement stm5 = null;
      ResultSet rs5 = null;
      try {
        stm5 = conn.createStatement();
        rs5 = stm5.executeQuery(query1);
        while (rs5.next()) {
          YHPerson person = new YHPerson();
          person.setSeqId(rs5.getInt("SEQ_ID"));
          person.setUserName(rs5.getString("USER_NAME"));
          person.setEmail(rs5.getString("EMAIL"));
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
   * 角色JSON数据组织
   * @param conn
   * @param userId
   * @param userName
   * @param isOnline
   * @return
   * @throws Exception
   */
  public StringBuffer user2Json(Connection conn ,int userId , String userName , String email) throws Exception{
    StringBuffer sb = new StringBuffer();
    sb.append("{userId:\"").append(userId).append("\"")
    .append(",userName:\"").append(YHUtility.encodeSpecial(userName)).append("\"").append(",email:\"").append(YHUtility.encodeSpecial(email)).append("\"");
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
  public StringBuffer getPrincipalRoleEmail(Connection conn, int roleId  , YHPerson user ) throws Exception{
    StringBuffer sb = new StringBuffer();
    StringBuffer result = new StringBuffer();
    
    String sql  = "select SEQ_ID , USER_NAME , EMAIL FROM PERSON WHERE   USER_PRIV='" + roleId + "' ";
      sql +=  " AND NOT_LOGIN <> '1'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    Set set = new HashSet();
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        int userId = rs.getInt(1);
        String userName = rs.getString(2);
       
        String email = rs.getString(3);
        if (!YHUtility.isNullorEmpty(email)
            && !set.contains(email)) {
          if(!"".equals(sb.toString())){
            sb.append(",");
           }
           sb.append(user2Json(conn, userId, userName, email));
           set.add(email);
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
   * 取得辅助角色用户
   * @param conn
   * @param roleId
   * @param notLoginIn 
   * @return
   * @throws Exception
   */
  public StringBuffer getSupplementRoleEmail(Connection conn , int roleId, YHPerson user) throws Exception{
    StringBuffer sb = new StringBuffer();
    StringBuffer result = new StringBuffer();
    String sql  = "select SEQ_ID , USER_NAME , USER_PRIV_OTHER,EMAIL FROM PERSON WHERE 1=1  " ;
     sql+= " AND  USER_PRIV_OTHER LIKE '%" + YHUtility.encodeLike(String.valueOf(roleId)) + "%' " + YHDBUtility.escapeLike();
     sql +=  " AND NOT_LOGIN <> '1'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    Set set = new HashSet();
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
         String userPrivOther = rs.getString(3);
         if(userPrivOther != null 
             && findId(userPrivOther,roleId,",")){
           int userId = rs.getInt(1);
           String userName = rs.getString(2);
           
          String email =  rs.getString(4);
           if (!YHUtility.isNullorEmpty(email)
               && !set.contains(email)) {
             if(!"".equals(sb.toString())){
               sb.append(",");
              }
             sb.append(user2Json(conn, userId, userName,email));
             set.add(email);
           }
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
  public ArrayList<YHPerson> getGorupUser(Connection conn, int groupId) throws Exception{
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
            YHPerson person = this.getPersonById(conn, userId );
            if (person != null) {
              persons.add(person);
            }
          }
        }
      }
    }
    return persons;
  }
  public YHPerson getPersonById(Connection conn , int seqId) throws Exception {
    String query = "select SEQ_ID , USER_NAME , DEPT_ID , USER_PRIV , EMAIL  from PERSON where  seq_id = " + seqId ;
    query += " and NOT_LOGIN <> '1' ";
    
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
        String email = rs4.getString("EMAIL");
        
        
        per.setSeqId(seqId);
        per.setUserName(userName);
        per.setDeptId(deptId1);
        per.setUserPriv(userPriv);
        per.setEmail(email);
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
  public StringBuffer getGorupEmail2Json(Connection conn , int groupId, YHPerson loginUser) throws Exception{
    StringBuffer user = new StringBuffer();
    StringBuffer sb = new StringBuffer();
    ArrayList<YHPerson> persons = getGorupUser(conn, groupId);
    Set set = new HashSet();
    for (int i = 0; i < persons.size(); i++) {
      YHPerson person = persons.get(i);
      String userName = person.getUserName();
      int userId = person.getSeqId();
      String email = person.getEmail();
      if (!YHUtility.isNullorEmpty(email)
          && !set.contains(email)) {
        if(!"".equals(sb.toString())){
          sb.append(",");
         }
        sb.append(user2Json(conn, userId, userName, person.getEmail()));
        set.add(email);
      }
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
  public List<YHPerson> getEmailsByDept(Connection conn, int deptId  , boolean notLoginIn) throws Exception{
    List<YHPerson> list = new ArrayList();
    String query = "select  PERSON.SEQ_ID , USER_NAME , EMAIL  from PERSON , USER_PRIV where USER_PRIV.SEQ_ID = PERSON.USER_PRIV " ;
    if (!notLoginIn) {
      query += " AND NOT_LOGIN <> '1' ";
    }
    query += " AND (DEPT_ID = " + deptId + "  or " + YHDBUtility.findInSet(String.valueOf(deptId), "DEPT_ID_OTHER")+ ")  order by USER_PRIV.PRIV_NO ,  PERSON.USER_NO DESC ,PERSON.SEQ_ID";
    Statement stm4 = null;
    ResultSet rs4 = null;
    Set set = new HashSet();
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        YHPerson per = new YHPerson();
        int seqId = rs4.getInt("SEQ_ID");
        String email = rs4.getString("EMAIL");
        if (!YHUtility.isNullorEmpty(email)
            && !set.contains(email)) {
          String userName = rs4.getString("USER_NAME");
          per.setSeqId(seqId);
          per.setUserName(userName);
          per.setEmail(email);
          list.add(per);
          set.add(email);
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return  list;
  }
  
  public String getDeptName(Connection dbConn, int deptId) throws Exception{
    YHOrgSelectLogic deptNameLogic = new YHOrgSelectLogic();
    String deptNameStr = "";
    try {
      deptNameStr = deptNameLogic.getDeptNameLogic(dbConn, deptId);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      throw e;
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

  public StringBuffer getRoleEmail(Connection conn, int roleId, YHPerson user) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer result = new StringBuffer("{");
    StringBuffer principalRole = getPrincipalRoleEmail(conn, roleId , user );
    StringBuffer supplementRole = getSupplementRoleEmail(conn, roleId, user );
    result.append("principalRole:").append(principalRole)
      .append(",supplementRole:").append(supplementRole)
      .append("}");
    return result;
  }

  public StringBuffer getQueryEmail2Json(Connection dbConn, String userName) throws Exception {
    StringBuffer user = new StringBuffer();
    StringBuffer sb = new StringBuffer();
    ArrayList<YHPerson> persons = getUserByName(dbConn, userName, false);
    Set set = new HashSet();
    for (int i = 0; i < persons.size(); i++) {
      YHPerson person = persons.get(i);
      String userName2 = person.getUserName();
      int userId = person.getSeqId();
      String email = person.getEmail();
      if (!YHUtility.isNullorEmpty(email)
          && !set.contains(email)) {
        if(!"".equals(sb.toString())){
          sb.append(",");
         }
         sb.append(user2Json(dbConn, userId, userName2, email));
         set.add(email);
      }
    }
    user.append("[").append(sb).append("]");
    return user;
  }
}
