package yh.core.funcs.diary.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.modulepriv.data.YHModulePriv;
import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
/**
 * 权限控制
 * @author Think
 *
 */
public class YHPrivUtil {
  private static Logger log = Logger
      .getLogger("yh.core.funcs.diary.logic.YHPrivUtil");

  /**
   * 得到当前用户（按模块分配权限）的权限
   * 
   * @param conn
   * @param userId
   *          用户Id
   * @param moduleId
   *          模块Id
   * @return YHModulePriv
   * @throws Exception
   */
  public static YHModulePriv getMyPrivByModel(Connection conn, int userId,
      String moduleId) throws Exception {
    YHModulePriv mp = null;
    if(moduleId == null || "".equals(moduleId)){//如果模块号不存在则返回空
      return null;
    }
    String sql = "select " + " DEPT_PRIV " + ",ROLE_PRIV" + ",SEQ_ID"
        + ",DEPT_ID" + ",PRIV_ID" + ",USER_SEQ_ID" + ",USER_ID" + ",MODULE_ID"
        + " from " + "oa_function_priv" + " where " + "USER_SEQ_ID=?" + " and "
        + "MODULE_ID=?";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.setInt(1, userId);
      ps.setString(2, moduleId);
      rs = ps.executeQuery();
      if (rs.next()) {
        mp =  new YHModulePriv();
        mp.setSeqId(rs.getInt("SEQ_ID"));
        mp.setDeptId(rs.getString("DEPT_ID"));
        mp.setDeptPriv(rs.getString("DEPT_PRIV"));
        mp.setModuleId(rs.getInt("MODULE_ID"));
        mp.setPrivId(rs.getString("PRIV_ID"));
        mp.setRolePriv(rs.getString("ROLE_PRIV"));
        mp.setUserId(rs.getString("USER_ID"));
        mp.setUserSeqId(rs.getInt("USER_SEQ_ID"));
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return mp;
  }

  /**
   * 取得指定用户的角色排序号
   * 
   * @param conn
   * @param userId
   *          用户的ID
   * @return 用户的角色排序号
   * @throws Exception
   */
  public static int getMyUserPrivNo(Connection conn, int userId)
      throws Exception {
    int result = -1;
    String sql = "select " + " PRIV_NO " + " from " + " USER_PRIV "
        + " ,PERSON " + " where " + " PERSON.SEQ_ID = " + userId
        + "  AND USER_PRIV.SEQ_ID = PERSON.USER_PRIV";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  public static Map<String, Integer> getMyUserPriv(Connection conn, int userId)
    throws Exception {
    Map<String, Integer>  result = null;
    String sql = "select " 
        + " DEPT_ID " 
        + " ,PERSON.USER_PRIV " 
        + " ,PRIV_NO " 
        + " from " 
        + " USER_PRIV "
        + " ,PERSON "
        + " where " 
        + " PERSON.SEQ_ID = " + userId
        + "  AND USER_PRIV.SEQ_ID = PERSON.USER_PRIV";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        result = new HashMap<String, Integer>();
        result.put("deptId", rs.getInt(1));
        int userPriv = Integer.valueOf(rs.getString(2));
        result.put("userPriv", userPriv);
        result.put("privNo", rs.getInt(3));
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  /**
   * 判断parentDeptId是否是childDeptId的上级部门
   * 
   * @param parentDeptId
   * @param childDeptId
   * @return
   * @throws Exception
   */
  public static boolean isParentDept(Connection conn, int parentDeptId,
      int childDeptId) throws Exception {
    boolean result = false;
    String sql = "select " + " DEPT_PARENT " + " from " + " oa_department "
        + " where " + " SEQ_ID = " + parentDeptId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        int dept = rs.getInt(1);
        if (dept == childDeptId) {
          result = true;
        } else {
          result = isParentDept(conn, dept, childDeptId);
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  /**
   * 取得当前部门的所有下级部门
   * @param parentDeptId
   * @param childDeptId
   * @return
   * @throws Exception
   */
  public static String getChildDeptId(Connection conn, int parentDeptId) throws Exception {
    String result = "";
    String subResult = "";
    String sql = "select " + "  SEQ_ID ,DEPT_NAME" + " from " + " oa_department "
        + " where " + " DEPT_PARENT = " + parentDeptId;
    if(parentDeptId == 0){
      return "";
    }
    PreparedStatement ps = null;
    ResultSet rs = null;
    int dept = -1;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        dept = rs.getInt(1);
        if(!"".equals(result) && !result.endsWith(",")){
          result += ",";
        }
        result += dept;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    subResult = getChildDeptId(conn, result);
    if(!"".equals(subResult)){
      if(!"".equals(result) && !result.endsWith(",")){
        result += ",";
      }
      result += subResult;
   }
    return result;
  }
  /**
   * 取得当前部门的所有下级部门
   * @param parentDeptId
   * @param childDeptId
   * @return
   * @throws Exception
   */
  public static String getChildDeptId(Connection conn, String parentDeptIds) throws Exception {
    String result = "";
    if(parentDeptIds == null || "".equals(parentDeptIds)){
      return "";
    } else {
      String[] parentDeptArray = parentDeptIds.split(",");
      for (String pid : parentDeptArray) {
        if("".equals(pid.trim())){
          continue;
        }
        if(!"".equals(result) && !result.endsWith(",")){
          result += ",";
        }
        result += getChildDeptId(conn, Integer.parseInt(pid));
      }
    }
    return result;
  }
  
  /**
   * 判断当前部门是否在合法权限管理范围内
   * @param deptId
   * @param postPriv
   * @param postDept
   * @param LoginUserId
   * @param loginUserDept
   * @return
   * @throws Exception 
   */
  public static boolean isDeptPriv(Connection conn,int deptId,String postPriv,String postDept,int LoginUserId,int loginUserDept) throws Exception{
    boolean result = false;
    try {
      if(postPriv == null || "".equals(postPriv)){
        //如果postPriv为空则查询当前用户的权限
      }
      //人员范围/管理范围”为本部门（0） 并且 deptId是当前用户所在的部门或者其子部门
      if("0".equals(postPriv) 
          && (deptId == loginUserDept || isParentDept(conn, deptId, loginUserDept))) {
        result = true;
      } else if("2".equals(postPriv)){//“人员范围/管理范围”为指定部门（2）
        //循环检查$DEPT_ID是否为指定的部门之一，或者指定部门中某个部门的子部门
        String[] postDeptIds = postDept.split(",");
        for (int i = 0; i < postDeptIds.length; i++) {
          if("".equals(postDeptIds[i].trim())){
            continue;
          }
          int postDeptId = Integer.valueOf(postDeptIds[i]);
          if(deptId == postDeptId || isParentDept(conn, deptId, postDeptId)){//指定部门和指定部门的子部门
            result = true;
            break;
          }
        }
      } else if("1".equals(postPriv)){//全体部门
        result = true;
      } else if("3".equals(postPriv) || "4".equals(postPriv)){//指定人员或者本人 
        result = true;
      } 
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  
  /**
   * 判断当前部门是否在合法权限管理范围内
   * @param deptId
   * @param postPriv
   * @param postDept
   * @param LoginUserId
   * @param loginUserDept
   * @return
   * @throws Exception 
   */
  public static boolean isDeptPriv(Connection conn,int deptId,String postPriv,String postDept,int LoginUserId,int loginUserDept,YHMyPriv mp) throws Exception{
    boolean result = false;
    if("0".equals(mp.getDeptPriv()) //人员范围：0-本部门
        && !isDeptPriv(conn, deptId, postPriv, postDept, LoginUserId, loginUserDept) //$DEPT_ID不属于我的管理范围，注意：is_dept_priv函数包含了子部门的处理，即$DEPT_ID是我所在部门的子部门，也属于我的管理范围（仅当管理范围是“本部门”时考虑子部门的情况）
    || 
    "2".equals(mp.getDeptPriv()) //人员范围：2-指定部门
         && !findId(mp.getDeptId(),deptId))//$DEPT_ID不在指定部门范围之内，即便它是$MY_PRIV["DEPT_ID_STR"]中某个部门的子部门也不行，这里要求严格指定，仅明确指定的部门才算管理范围之内
    {
      result = true;
    }
    return result;
  }
  /**
   * 取得当前用户权限范围内的所有用户ID
   * @param conn
   * @param userId
   * @return
   * @throws Exception 
   */
  public static  ArrayList<Integer> getLawfulUserId(Connection conn,YHPerson person,String moduleId,int privNoFlag) throws Exception{
    ArrayList<Integer> result = new ArrayList<Integer>();
    int loginUserId = person.getSeqId();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      YHMyPriv mp = getMyPriv(conn, person, moduleId, privNoFlag);
      String sql = "SELECT SEQ_ID FROM PERSON WHERE SEQ_ID != " + loginUserId;
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        int userId = rs.getInt(1);
        if(YHPrivUtil.isUserPriv(conn, userId, mp, person.getPostPriv(), person.getPostDept(), loginUserId, person.getDeptId())){
          result.add(userId);
        }
      }
    } catch (Exception e) {
      throw e ;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  public static String getDeptManager(Connection conn,  YHModulePriv priv ,YHPerson person,String moduleId,int privNoFlag ) throws Exception {
    String manager = "";
    if (priv != null && "2".equals(priv.getDeptPriv()) 
        && !YHUtility.isNullorEmpty(priv.getDeptId())) {
      String deptStr = priv.getDeptId();
      String[] depts = deptStr.split(",");
      for (String de : depts) {
        if (!"".equals(de)) {
          if ("".equals(manager) || manager.endsWith(",")) {
            manager += getDeptManagerByDept(conn ,Integer.parseInt(de));
          } else {
            manager += "," + getDeptManagerByDept(conn ,Integer.parseInt(de));
          }
        }
      }
    }
    if ("".equals(manager) || manager.endsWith(",")) {
      manager += getDeptManagerByDept(conn , person.getDeptId());
    } else {
      manager += "," + getDeptManagerByDept(conn , person.getDeptId());
    }
    return manager;
  }
  public static String getDeptManagerByDept(Connection conn ,  int dept) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    String manager = "";
    int deptParent = 0 ;
    try{
      String sql = "SELECT MANAGER,DEPT_PARENT FROM oa_department WHERE SEQ_ID = " + dept;
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()){
        manager = rs.getString("MANAGER");
        deptParent = rs.getInt("DEPT_PARENT"); 
        if (manager == null) {
          manager = "";
        } 
      }
    } catch (Exception e) {
      throw e ;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    if (deptParent != 0) {
      PreparedStatement ps2 = null;
      ResultSet rs2 = null;
      try{
        String sql = "SELECT MANAGER,DEPT_PARENT FROM oa_department WHERE SEQ_ID = " + deptParent;
        ps2 = conn.prepareStatement(sql);
        rs2 = ps2.executeQuery();
        if (rs2.next()){
          String tmp = rs2.getString("MANAGER");
          deptParent = rs2.getInt("DEPT_PARENT"); 
          if (tmp != null) {
            if ("".equals(manager) || manager.endsWith(",")) {
              manager += tmp;
            } else {
              manager +=  ","  + tmp;
            }
          }
        }
      } catch (Exception e) {
        throw e ;
      } finally{
        YHDBUtility.close(ps2, rs2, log);
      }
    }
    if (deptParent != 0) {
      PreparedStatement ps2 = null;
      ResultSet rs2 = null;
      try{
        String sql = "SELECT MANAGER,DEPT_PARENT FROM oa_department WHERE SEQ_ID = " + deptParent;
        ps2 = conn.prepareStatement(sql);
        rs2 = ps2.executeQuery();
        if (rs2.next()){
          String tmp = rs2.getString("MANAGER");
          deptParent = rs2.getInt("DEPT_PARENT"); 
          if ("".equals(manager) || manager.endsWith(",")) {
            manager += tmp;
          } else {
            manager +=  ","  + tmp;
          }
        }
      } catch (Exception e) {
        throw e ;
      } finally{
        YHDBUtility.close(ps2, rs2, log);
      }
    }
    return manager;
  }
/**
 * 取得当前用户的权限信息 * @param conn
 * @param userId 当前用户的ID
 * @param postPriv 当前用户的管理范围 * @param postDept 管理范围--指定部门
 * @param moduleId 模块Id
 * @param loginUserPriv 登录用户的角色权限 * @param privNoFlag 权限设定
 * @return
 * @throws Exception
 */
  public static YHMyPriv getMyPriv(Connection conn,YHPerson person,String moduleId,int privNoFlag) throws Exception{
    int userId = person.getSeqId();
    String postPriv =  person.getPostPriv();
    String postDept = person.getPostDept();
    YHMyPriv mpriv = new YHMyPriv();
    String deptPriv = "0";
    String rolePriv = "0";
    String privId = "";
    String deptId = "";
    int privNo = -1;
    String userIsStr = "";
    try {
      YHModulePriv mp = getMyPrivByModel(conn, userId, moduleId);
      if(moduleId != null && !"".equals(moduleId)){//细粒度控制        if(mp != null){
          //非模块分配权限          //则按管理范围，角色号比自己高的          deptPriv = mp.getDeptPriv();//人员范围：0-本部门；1-全体；2-指定部门；3-指定人员；4-本人
          rolePriv = mp.getRolePriv();//人员角色：0-低角色的用户；1-同角色和低角色的用户；2-所有角色的用户；3-指定角色的用户          deptId = mp.getDeptId();//指定部门
          privId = mp.getPrivId();//指定角色
          userIsStr = mp.getUserId();//指定人员
        } else if("2".equals(moduleId)){//没有给当前用户按模块设置管理范围，并且当前模块是“全部人员模块”          deptPriv = "1";//人员范围：全体          rolePriv = "2";//人员角色：所有角色的用户
        }else{
          moduleId = "";
        }
      } 
   // privNoFlag = 0; //全体部门、全体角色
   // privNoFlag = 1; //自己的管理范围、全体角色
   // privNoFlag = 2; //自己的管理范围、低角色
   // privNoFlag = 3; //自己的管理范围、低角色，OA管理员除外的用户
      if(moduleId == null || "".equals(moduleId)){//模块名称为全部人员模块&不按 模块选择权限
         /// if(person.isAdminRole() || privNoFlag == 0){//判断当前用户是否管理员
        if(person.isAdminRole(conn)|| person.isAdmin() || privNoFlag == 0){
            deptPriv = "1";//人员范围：全体            rolePriv = "2";//人员角色：所有角色的用户
          } else{//不是管理员则按非模块权限处理
             deptPriv = postPriv;//部门管理范围
             deptId = postDept;//指定部门
             rolePriv = "0";//低角色用户            if(privNoFlag == 1){////自己的管理范围、全体角色 ？ 值是穿过来的？               rolePriv = "2";
             }
          }
      }
//  rolePriv = 0-低角色的用户
//  rolePriv = 1-同角色和低角色的用户
//  rolePriv =  2-所有角色的用户
//  rolePriv =  3-指定角色的用户
      if("0".equals(rolePriv) || "1".equals(rolePriv)){
         //取出当前用户自己的角色排序号
        privNo = getMyUserPrivNo(conn, userId);
      }
      mpriv.setDeptId(deptId);
      mpriv.setDeptPriv(deptPriv);
      mpriv.setPrivId(privId);
      mpriv.setPrivNo(privNo);
      mpriv.setRolePriv(rolePriv);
      mpriv.setUserId(userIsStr);
    } catch (Exception e) {
      throw e ;
    }
    return mpriv;
  }
  /**
   * 判断此用户是否合法   * @param conn
   * @param userId
   * @param mp
   * @param postPriv
   * @param postDept
   * @param LoginUserId
   * @param loginUserDept
   * @return
   * @throws Exception
   */
  public static boolean isUserPriv(Connection conn,int userId,YHMyPriv mp
      ,String postPriv,String postDept,int LoginUserId,int loginUserDept) throws Exception{
     //本部门,指定部门
 /*   if("1".equals(mp.getDeptPriv()) || "2".equals(mp.getDeptPriv()) ){
      return true;
    }*/
   Map<String, Integer>  map = getMyUserPriv(conn, userId);
   if(map == null || map.size() <= 0){
     return false;
   }
   //取出待判断用户（$USER_ID）的部门ID、角色ID、角色排序号
   int deptId = map.get("deptId");
   int userPriv = map.get("userPriv");
   int privNo = map.get("privNo");
   //判断哪些情况不是自己的管理范围
   boolean isRole = !isRoleRule(mp, privNo, userPriv);//判断是否在当前用户的角色管理范围
   
   if(("1".equals(mp.getDeptPriv()) && isRole)
     ||
     ("0".equals(mp.getDeptPriv()) //人员范围：0-本部门         && isDeptPriv(conn, deptId, mp.getDeptPriv(), mp.getDeptId(), LoginUserId, loginUserDept) && isRole) //DEPT_ID不属于我的管理范围，注意：is_dept_priv函数包含了子部门的处理，即$DEPT_ID是我所在部门的子部门，也属于我的管理范围（仅当管理范围是“本部门”时考虑子部门的情况）     || 
     ("2".equals(mp.getDeptPriv()) //人员范围：2-指定部门
          && findId(mp.getDeptId(),deptId) && isRole)//DEPT_ID不在指定部门范围之内，即便它是$MY_PRIV["DEPT_ID_STR"]中某个部门的子部门也不行，这里要求严格指定，仅明确指定的部门才算管理范围之内
     ||
     ("3".equals(mp.getDeptPriv())//人员范围：3-指定人员
          && findId(mp.getUserId(), userId) && isRole) )//USER_ID不在指定的人员范围之内       return true;
     return false;
  }
  
  public static boolean isRoleRule(YHMyPriv mp, int privNo,int userPriv){
    if("2".equals(mp.getRolePriv())){//人员角色：2-全体角色
      return false;
    }
    if(("0".equals(mp.getRolePriv())//人员角色：0-低角色的用户
        && mp.getPrivNo() >= privNo) //PRIV_NO比当前用户的角色级别高或相等
        || 
        ("1".equals(mp.getRolePriv())//人员角色：1-同角色和低角色的用户
             && mp.getPrivNo() > privNo) //PRIV_NO比当前用户的角色级别高
        ||
        ("3".equals(mp.getRolePriv()) //人员角色：3-指定角色的用户
             && ! findId(mp.getPrivId(),userPriv))){
      return true;
    }else{
      return false;
    }
  }
  
  /**
   * 判断此用户是否合法
   * @param conn
   * @param userId
   * @param mp
   * @param postPriv
   * @param postDept
   * @param LoginUserId
   * @param loginUserDept
   * @return
   * @throws Exception
   */
  public static boolean isUserPriv(Connection conn,int userId,YHMyPriv mp
      ,YHPerson person) throws Exception{
     String postPriv = person.getPostPriv();
     String postDept = person.getPostDept();
     int LoginUserId = person.getSeqId();
     int loginUserDept = person.getDeptId();
     return isUserPriv(conn, userId, mp, postPriv, postDept, LoginUserId, loginUserDept);
  }
  /**
   * @param conn
   * @param userId
   * @param mp
   * @param postPriv
   * @param postDept
   * @param LoginUserId
   * @param loginUserDept
   * @return
   * @throws Exception
   */
  public static boolean isDeptPriv(Connection conn,int deptId,YHMyPriv mp
      ,String postPriv,String postDept,int LoginUserId,int loginUserDept) throws Exception{

    if("1".equals(mp.getDeptPriv())){
      return true;
    }
   //判断哪些情况不是自己的管理范围

   if(("0".equals(mp.getDeptPriv()) //人员范围：0-本部门
         && !isDeptPriv(conn, deptId, postPriv, postDept, LoginUserId, loginUserDept) //$DEPT_ID不属于我的管理范围，注意：is_dept_priv函数包含了子部门的处理，即$DEPT_ID是我所在部门的子部门，也属于我的管理范围（仅当管理范围是“本部门”时考虑子部门的情况）
     )|| 
     ("2".equals(mp.getDeptPriv()) //人员范围：2-指定部门
          && !findId(mp.getDeptId(),deptId))//$DEPT_ID不在指定部门范围之内，即便它是$MY_PRIV["DEPT_ID_STR"]中某个部门的子部门也不行，这里要求严格指定，仅明确指定的部门才算管理范围之内
    )//$USER_PRIV不在指定的角色范围之内
       return false;
     return true;
  }
  
  public static boolean isDeptPriv(Connection conn,int deptId,YHMyPriv mp
      ,YHPerson person) throws Exception{
     String postPriv = person.getPostPriv();
     String postDept = person.getPostDept();
     int LoginUserId = person.getSeqId();
     int loginUserDept = person.getDeptId();
     return isDeptPriv(conn, deptId, mp, postPriv, postDept, LoginUserId, loginUserDept);
  }
  
  public static boolean isManageAreaPriv(Connection conn,int deptId,YHMyPriv mp
		  ,YHPerson person) throws Exception{
	  String postPriv = person.getPostPriv();
	  String postDept = person.getPostDept();
	  int LoginUserId = person.getSeqId();
	  int loginUserDept = person.getDeptId();
	  return isDeptPriv(conn, deptId, mp, postPriv, postDept, LoginUserId, loginUserDept);
  }
  /**
   * 查找id串中是否存在此ID
   * @param ids
   * @param id
   * @return
   */
  private static boolean findId(String ids,int id){
    String[] idarray = ids.split(",");
    for (int i = 0; i < idarray.length; i++) {
      if("".equals(idarray[i])){
        continue;
      }
      int preId = Integer.valueOf(idarray[i].trim());
      if(id == preId){
        return true;
      }
    }
    return false;
  }
  /**
   * 取得当前角色的最顶级部门
   * @param conn
   * @param dept
   * @return
   * @throws Exception
   */
  public static YHDepartment getGrandParentDept(Connection conn,int dept) throws Exception{
    YHDepartment result = null ;
    String sql = "select SEQ_ID,DEPT_PARENT,DEPT_NAME from oa_department where SEQ_ID =" + dept;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        int parentId = rs.getInt("DEPT_PARENT");
        if (parentId == 0) {
          result = new YHDepartment();
          result.setSeqId(rs.getInt("SEQ_ID"));
          result.setDeptName(rs.getString("DEPT_NAME"));
          result.setDeptParent(parentId);
        } else {
          result =  getGrandParentDept(conn, parentId);
        }
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  /**
   * 判断是否为hr管理员
   * @param conn
   * @param dept
   * @return
   * @throws Exception
   */
  public static boolean isHrManageDept(Connection conn, int dept, YHPerson person) throws Exception{
    String sql = "select 1 from oa_pm_manager where DEPT_ID =" + dept + " and " + YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "DEPT_HR_MANAGER");
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return false;
  }
}
