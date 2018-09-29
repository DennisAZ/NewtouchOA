package yh.core.funcs.person.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHPassEncrypt;
import yh.core.util.auth.YHUsbKey;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHPersonLogic {
  private static Logger log = Logger.getLogger(YHPersonLogic.class);

  public int selectPerson(Connection conn, String userId)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      String queryStr = "select count(*) from PERSON where USER_ID='" + userId + "'"; 
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      int num = 0;
      if(rs.next()){      
        num = rs.getInt(1);
      }
      return num;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  /**
   * 获取可以登录的用户数
   * @param dbConn
   * @return
   * @throws Exception
   */
  public int getUserConut(Connection dbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    int num = -1;
    try {
      String queryStr = "select count(1) from PERSON where NOT_LOGIN = '0'"; 
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if (rs.next()) {      
        num = rs.getInt(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return num;
  }
  
  
  public int getNotLoginNum(Connection conn)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    int num = 0;
    try {
      String queryStr = "select count(*) from PERSON where NOT_LOGIN = '0'"; 
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if(rs.next()){      
        num = rs.getInt(1);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return num;
  }
  
  public YHPerson getPerson(Connection conn, String seqId)throws Exception{
    YHPerson  person = null;
    try {
      YHORM orm = new YHORM();
      person = (YHPerson)orm.loadObjSingle(conn , YHPerson.class , Integer.parseInt(seqId));
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(null , null , log);
    }
    return person;
  }
  
  public String getDeptNameByDeptId(Connection conn, int deptId)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String deptName = "";
    try {
      String queryStr = "select DEPT_NAME from oa_department where SEQ_ID=" + deptId; 
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if(rs.next()) {
        deptName = rs.getString("DEPT_NAME");
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(null , null , log);
    }
    return deptName;
  }
  
  public String getDeptTypeDescByDutyType(Connection conn, int dutyType)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String dutyTypeDesc = "";
    try {
      String queryStr = "select CODE_DESC from DUTY_TYPE where CODE_NO=" + dutyType; 
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if(rs.next()) {
        dutyTypeDesc = rs.getString("CODE_DESC");
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(null , null , log);
    }
    return dutyTypeDesc;
  }
  
  public String getUserPrivDescByUserPriv(Connection conn, String userPriv)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String userPrivDesc = "";
    try {
      String queryStr = "select PRIV_NAME from USER_PRIV where PRIV_NO=" + Integer.parseInt(userPriv); 
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if(rs.next()) {
        userPrivDesc = rs.getString("PRIV_NAME");
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(null , null , log);
    }
    return userPrivDesc;
  }
  
  public String getPostPrivDescByPostPriv(Connection conn, String postPriv)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String postPrivDesc = "";
    if(postPriv == null) {
      postPriv = "" + 0;
    }
    try {
      String queryStr = "select CODE_DESC from POST_PRIV where CODE_NO=" + Integer.parseInt(postPriv); 
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if(rs.next()) {
        postPrivDesc = rs.getString("CODE_DESC");
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(null , null , log);
    }
    return postPrivDesc;
  }
  
  public YHPerson getPersonById(String userId , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    YHPerson person = new YHPerson();
    Map query = new HashMap();
    query.put("USER_ID", userId);
    person = (YHPerson) orm.loadObjSingle(conn, YHPerson.class, query);;
    return person;
  }
  
  public YHPerson getPersonById(int seqId, Connection conn) throws Exception{
    YHORM orm = new YHORM();
    YHPerson person = (YHPerson) orm.loadObjSingle(conn, YHPerson.class, seqId);;
    return person;
  }
  /**
   * 根据userid串返回一个名字串
   * @param ids
   * @return
   * @throws Exception
   * @throws Exception
   */
  public String getNameByIdStr(String ids, Connection conn) throws Exception, Exception{
    String names = "";
    if(ids != null && !"".equals(ids)){
      String[] aId = ids.split(",");
      for(String tmp : aId){
        if(!"".equals(tmp)){
          YHPerson person = this.getPersonById(tmp , conn);
          if(person != null){
            names += person.getUserName() + ",";
          }
        }
      }
    }
    return names;
  }
  /**
   * 根据seqid串返回一个名字串,和id串,这个可以去掉id串里面的重复，这样出来的名字串和id串是对应的(即使id里的数据有问题)
   * @param ids
   * @return
   * @throws Exception
   * @throws Exception
   */
  public Map getMapBySeqIdStr(String ids , Connection conn) throws Exception, Exception{
    String names = ""; 
    if(ids != null && !"".equals(ids.trim())){ 
    if (ids.endsWith(",")){ 
    ids = ids.substring(0, ids.length() - 1); 
    } 
    String query = "select USER_NAME,SEQ_ID from PERSON where SEQ_ID in (" + ids + ")"; 
    Statement stm = null; 
    ResultSet rs = null;
     ids = "";
    try { 
    stm = conn.createStatement(); 
    rs = stm.executeQuery(query); 
    while (rs.next()){ 
      names += rs.getString("USER_NAME") + ","; 
      ids += rs.getInt("SEQ_ID") + ","; 
    } 
    } catch(Exception ex) { 
    throw ex; 
    } finally { 
    YHDBUtility.close(stm, rs, null); 
    } 
    } 
    if (names.endsWith(",")) { 
    names = names.substring(0, names.length() - 1); 
    }
    if (ids.endsWith(",")) { 
      ids = ids.substring(0, ids.length() - 1); 
      }
    Map map =new HashMap();
    map.put("name", names);
    map.put("id", ids);
    return map;
  } 
  /**
   * 根据seqid串返回一个名字串
   * @param ids
   * @return
   * @throws Exception
   * @throws Exception
   */
  public String getNameBySeqIdStr(String ids , Connection conn) throws Exception, Exception{
    String names = ""; 
    if(ids != null && !"".equals(ids.trim())){ 
    if (ids.endsWith(",")){ 
    ids = ids.substring(0, ids.length() - 1); 
    } 
    String query = "select USER_NAME from PERSON where SEQ_ID in (" + ids + ")"; 
    Statement stm = null; 
    ResultSet rs = null; 
    try { 
    stm = conn.createStatement(); 
    rs = stm.executeQuery(query); 
    while (rs.next()){ 
    names += rs.getString("USER_NAME") + ","; 
    } 
    } catch(Exception ex) { 
    throw ex; 
    } finally { 
    YHDBUtility.close(stm, rs, null); 
    } 
    } 
    if (names.endsWith(",")) { 
    names = names.substring(0, names.length() - 1); 
    } 
    return names;
  } 
  public List<YHPerson> getPersonByDept(int deptId  , Connection conn) throws Exception{
    Map query = new HashMap();
    query.put("DEPT_ID", deptId);
    YHORM orm = new YHORM();
    List<YHPerson> list = orm.loadListSingle(conn, YHPerson.class, query);
    return list;
  }
  public List<YHPerson> getPersonByRole(int roleId  , Connection conn) throws Exception{
    Map query = new HashMap();
    query.put("USER_PRIV", String.valueOf(roleId));
    YHORM orm = new YHORM();
    List<YHPerson> list = orm.loadListSingle(conn, YHPerson.class, query);
    return list;
  }
  /**
   * 根据list取得人员json对象
   * @param list
   * @return
   */
  public String getPersonSimpleJson(List<YHPerson> list){
    StringBuffer sb = new StringBuffer();
    for(YHPerson person : list){
      sb.append(person.toJsonSimple());
    }
    if(list.size() > 0){
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  /**
   * 根据userID串取得人员json对象
   * @param userIds
   * @return
   * @throws Exception
   */
  public String getPersonSimpleJson(String userIds ,Connection conn) throws Exception{
    StringBuffer sb = new StringBuffer();
    String[] aId = userIds.split(",");
    int count = 0 ;
    for(String tmp : aId){
      if(!"".equals(tmp)){
        YHPerson person = this.getPersonById(Integer.parseInt(tmp) , conn);
        sb.append(person.toJsonSimple());
        count++;
      }
    }
    if(count > 0 ){
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  
  
  public List<YHPerson> getDeptIdOther(Connection dbConn) throws Exception {
    YHORM orm = new YHORM();
    String[] filters = null;
    return orm.loadListSingle(dbConn, YHPerson.class, filters);
  }
 
  /**
   * 分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String getManagePersonList(Connection conn,Map request,String deptId, String deptIdOther, String loginUserPriv, boolean isOaAdmin, String deptIdLogin, String postDept, String postPriv) throws Exception{
    //List<YHPerson> personList = getDeptIdOther(conn);
    //String userIds = getFalwUserId(conn, loginPerson, moduleId, privNoFlag, deptId);
    String[] deptIdOtherStr = null;
    String sql = "";
    YHPageDataList pageDataList = null;
    YHPageDataList pageDataList1 = null;
    int privNo = Integer.parseInt(getPrivNoStr(conn, loginUserPriv));
//    for (int x = 0; x < personList.size(); x++) {
//      deptIdOtherStr = personList.get(x).getDeptIdOther().split(",");
//      for (int i = 0; i < deptIdOtherStr.length; i++) {
//        String a = deptIdOtherStr[i];
//        if(deptIdOtherStr[i].equals(deptId)){
//          String sql = "select SEQ_ID" 
//              + ",USER_ID" 
//              + ",USER_NAME" 
//              + ",DEPT_ID"
//              + ",DUTY_TYPE" 
//              + ",USER_PRIV"
//              + ",POST_PRIV from PERSON where DEPT_ID ="
//              + Integer.parseInt(deptId)+" or DEPT_ID IN ('" + deptIdOtherStr[i] + "')";
//          // or DEPT_ID_OTHER IN ('" + deptIdOtherStr[i] + "')
//          // or DEPT_ID_OTHER IN ('" + deptId + "')
//          // String query = " order by DIA_DATE desc,DIA_TIME DESC ";
//          // sql += query;
//          System.out.println(sql);
//          YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
//          pageDataList = YHPageLoader.loadPageList(conn, queryParam, sql);
//        }else{
//          String sql = "select SEQ_ID" 
//              + ",USER_ID" 
//              + ",USER_NAME" 
//              + ",DEPT_ID"
//              + ",DUTY_TYPE" 
//              + ",USER_PRIV"
//              + ",POST_PRIV from PERSON where DEPT_ID ="
//              + Integer.parseInt(deptId);
//          // or DEPT_ID_OTHER IN ('" + deptIdOtherStr[i] + "')
//          // or DEPT_ID_OTHER IN ('" + deptId + "')
//          // String query = " order by DIA_DATE desc,DIA_TIME DESC ";
//          // sql += query;
//          System.out.println(sql);
//          YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
//          pageDataList = YHPageLoader.loadPageList(conn, queryParam, sql);
//        }
//      }
//    }
//    String[] deptIdOtherStr = deptIdOther.split(",");
//    YHPageDataList pageDataList = null;
//    for(int i = 0; i < deptIdOtherStr.length; i++){
//      String a = deptIdOtherStr[i];
//    }
    if(isOaAdmin){
      sql =  "select PERSON.SEQ_ID" +
      ",PERSON.USER_ID" +
      ",PERSON.USER_NAME" +
      ",PERSON.DEPT_ID" +
      ",PERSON.DUTY_TYPE" +
      ",PERSON.USER_PRIV" +
      ",PERSON.NOT_LOGIN" +
      ",PERSON.PASSWORD" +
      ",PERSON.POST_PRIV from PERSON, USER_PRIV where (DEPT_ID ="+Integer.parseInt(deptId)+" or " + YHDBUtility.findInSet(deptId, "DEPT_ID_OTHER")+ ") and PERSON.USER_PRIV = USER_PRIV.SEQ_ID";
    }else{
      sql =  "select PERSON.SEQ_ID" +
      ",PERSON.USER_ID" +
      ",PERSON.USER_NAME" +
      ",PERSON.DEPT_ID" +
      ",PERSON.DUTY_TYPE" +
      ",PERSON.USER_PRIV" +
      ",PERSON.NOT_LOGIN" +
      ",PERSON.PASSWORD" +
      ",PERSON.POST_PRIV from PERSON, USER_PRIV where (DEPT_ID =("+ Integer.parseInt(deptId) +") or " + YHDBUtility.findInSet(deptIdLogin, "DEPT_ID_OTHER")+ ") and PERSON.USER_PRIV = USER_PRIV.SEQ_ID and USER_PRIV.PRIV_NO > " + privNo + " and not USER_PRIV.SEQ_ID = 1";
      //",PERSON.POST_PRIV from PERSON, USER_PRIV where PERSON.SEQ_ID IN ("+ userIds +")";
    }
    
    // or DEPT_ID_OTHER IN ('" + deptIdOtherStr[i] + "')
    // or DEPT_ID_OTHER IN ('" + deptId + "')
   // String query = " order by DIA_DATE desc,DIA_TIME DESC ";
    //sql += query;
    //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    int cnt = pageDataList.getRecordCnt();
    for (int i = 0; i < cnt; i++) {
      YHDbRecord record = pageDataList.getRecord(i);
      String pass = (String)record.getValueByName("password");
      if (YHPassEncrypt.isValidPas("", pass)) {
        record.addField("isEmptyPass", "1");
      }else {
        record.addField("isEmptyPass", "0");
      }
    }
    
    return pageDataList.toJson();
  }
  
  public String getManagePersonList(Connection conn,Map request,String deptId, YHPerson loginPerson) throws Exception{
    String whereStr = "";
    int userPriv = 1; //OA管理员 角色权限表 seqId=1
    String userPrivNo = getPrivNo(conn, Integer.parseInt(loginPerson.getUserPriv()));
    String adminPrivNo = getPrivNo(conn, userPriv);
    String moduleId = (String) request.get("MODULE_ID");
    String privNoFlagStr = (String) request.get("privNoFlag");
    int privNoFlag = 2;
    try {
      privNoFlag = Integer.valueOf(privNoFlagStr);
    } catch (Exception e) {
    }
    String userIds = getFalwUserId(conn, loginPerson, moduleId, privNoFlag, deptId);
    if("".equals(userIds)){
      userIds = "-1";
    }
    
    if((!loginPerson.isAdminRole() || !loginPerson.isAdmin()) && (Integer.parseInt(userPrivNo) < Integer.parseInt(adminPrivNo))){
      whereStr = " AND NOT USER_PRIV = '1'";
    }
    String sql = "";
    YHPageDataList pageDataList = null;
      sql =  "select PERSON.SEQ_ID" +
                   ",PERSON.USER_ID" +
                   ",PERSON.USER_NAME" +
                   ",PERSON.DEPT_ID" +
                   ",PERSON.DUTY_TYPE" +
                   ",PERSON.USER_PRIV" +
                   ",PERSON.NOT_LOGIN" +
                   ",PERSON.PASSWORD" +
                 //",PERSON.POST_PRIV from PERSON, USER_PRIV where (DEPT_ID =("+ Integer.parseInt(deptId) +") or " + YHDBUtility.findInSet(deptIdLogin, "DEPT_ID_OTHER")+ ") and PERSON.USER_PRIV = USER_PRIV.SEQ_ID and USER_PRIV.PRIV_NO > " + privNo + " and not USER_PRIV.SEQ_ID = 1";
                   ",PERSON.POST_PRIV from PERSON,USER_PRIV where PERSON.SEQ_ID IN ("+ userIds +") AND (USER_PRIV.SEQ_ID = PERSON.USER_PRIV or PERSON.USER_PRIV='0') " 
                   + whereStr + " order by USER_PRIV.PRIV_NO, PERSON.USER_NO DESC , PERSON.SEQ_ID";
    //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    Date date1=new Date();
    pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    Date date2=new Date();
   
    int cnt = pageDataList.getRecordCnt();
    for (int i = 0; i < cnt; i++) {
      YHDbRecord record = pageDataList.getRecord(i);
      String pass = (String)record.getValueByName("password");
      if (YHPassEncrypt.isValidPas("", pass)) {
        record.addField("isEmptyPass", "1");
      }else {
        record.addField("isEmptyPass", "0");
      }
    }
    Date date3=new Date();
  
    return pageDataList.toJson();
  }
  /**
   * 查询出登录用户的所有合法范围的用户ID
   * @param conn
   * @param loginPerson
   * @param moduleId
   * @param privNoFlag
   * @return
   * @throws Exception 
   */
  public String getFalwUserId(Connection conn,YHPerson loginPerson,String moduleId ,int privNoFlag,String deptId) throws Exception{
    String result = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      YHMyPriv mp = YHPrivUtil.getMyPriv(conn, loginPerson ,moduleId,  privNoFlag);
      String sql = "";
      if(loginPerson.isAdminRole(conn)){
     //   sql = "select PERSON.SEQ_ID FROM PERSON, USER_PRIV WHERE  USER_PRIV.SEQ_ID = PERSON.USER_PRIV AND DEPT_ID=" + deptId + " OR " + YHDBUtility.findInSet(deptId + "", "DEPT_ID_OTHER") + " order by USER_PRIV.PRIV_NO , PERSON.USER_NO DESC ,PERSON.SEQ_ID";
        sql = "select distinct(PERSON.SEQ_ID) FROM PERSON, USER_PRIV WHERE  (USER_PRIV.SEQ_ID = PERSON.USER_PRIV or PERSON.USER_PRIV='0') AND DEPT_ID=" + deptId + " OR " + YHDBUtility.findInSet(deptId + "", "DEPT_ID_OTHER") + " ";
        
      
      }else{
   //     sql = "select PERSON.SEQ_ID FROM PERSON, USER_PRIV WHERE USER_PRIV.SEQ_ID = PERSON.USER_PRIV AND NOT PERSON.SEQ_ID = " + loginPerson.getSeqId() + " and (DEPT_ID=" + deptId + " OR " + YHDBUtility.findInSet(deptId + "", "DEPT_ID_OTHER") + ") order by USER_PRIV.PRIV_NO , PERSON.USER_NO DESC ,PERSON.SEQ_ID";
        sql = "select distinct(PERSON.SEQ_ID) FROM PERSON, USER_PRIV WHERE (USER_PRIV.SEQ_ID = PERSON.USER_PRIV or PERSON.USER_PRIV='0') AND NOT PERSON.SEQ_ID = " + loginPerson.getSeqId() + " and (DEPT_ID=" + deptId + " OR " + YHDBUtility.findInSet(deptId + "", "DEPT_ID_OTHER") + ") ";

      
      }
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        int userId = rs.getInt(1);
        if(YHPrivUtil.isUserPriv(conn, userId, mp, loginPerson.getPostPriv(), loginPerson.getPostDept(), loginPerson.getSeqId(), loginPerson.getDeptId())){
          if(!"".equals(result)){
            result += ",";
          }
          result += userId;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
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
    if(str == null || id == null || "".equals(str) || "".equals(id)){
      return false;
    }
    String[] aStr = str.split(",");
    for(String tmp : aStr){
      if(tmp.equals(id)){
        return true;
      }
    }
    return false;
  }
  
  /**
   *获取未分配角色的id 
   * 
   **/
  public String getNoPriv(Connection dbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String seq="0";
    String sql = "select seq_id from user_priv where priv_name='未分配角色'";
    try {
      stmt = dbConn.createStatement();  
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
       seq=rs.getString(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return seq;
  }
  
  /**
   *获取角色的id 
   * 
   **/
  public String getPrivName(Connection dbConn,String privName) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String seq="0";
    String sql = "select seq_id from user_priv where priv_name='"+privName+"'";
    try {
      stmt = dbConn.createStatement();  
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
       seq=rs.getString(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return seq;
  }
  
  /**
   * 判断登录人所在部门是否和点击部门树的部门相同
   * @param deptIdLogin
   * @param deptId
   * @return
   */
  
  public static boolean exitDeptId(String deptIdLogin, String deptId) {
    if(deptIdLogin.equals(deptId)){
      return true;
    }else{
      return false;
    }
  }
  
  
  public boolean existsPersonPriv(Connection dbConn, String postPriv, String postDept, String deptId, String deptIdLogin)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try {
      stmt = dbConn.createStatement();
      if(postPriv.equals("2")){
        sql = "SELECT count(*) FROM PERSON WHERE DEPT_ID = " + Integer.parseInt(deptIdLogin);
      }
      rs = stmt.executeQuery(sql);
      //System.out.println(sql);
      long count = 0;
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count >= 1) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 取得部门名称
   * @param conn
   * @param deptId
   * @return
   * @throws Exception
   */
  public String getDeptNameLogic(Connection conn , int deptId) throws Exception{
    String result = "";
    String sql = " select DEPT_NAME from oa_department where SEQ_ID = " + deptId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
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
   * 取得用户名称
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public String getUserNamesLogic(Connection conn , String userIds) throws Exception{
    if(YHUtility.isNullorEmpty(userIds)){
      return null;
    }
    String data="";
    String usId[]=userIds.split(",");
    for(int i=0;i<usId.length;i++){
      String userId=usId[i];
      if(!YHUtility.isNullorEmpty(userId))
      data+=this.getUserNameLogic(conn, Integer.parseInt(userId)) + ",";
      
    }
    
    return data;
    
  }
  
  /**
   * 取得用户名称
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  
  public String getUserNameLogic(Connection conn , int userId) throws Exception{
    String result = "";
    String sql = " select USER_NAME from PERSON where SEQ_ID = '"+userId+"'" ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
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
   * 取得用户USER_ID
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  
  public String getUserIdLogic(Connection conn , int userId) throws Exception{
    String result = "";
    String sql = " select USER_ID from PERSON where SEQ_ID = " + userId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
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
   * 获取用户DEPT_ID
   * @param conn
   * @param deptId
   * @return
   * @throws Exception
   */
  
  public String getDeptIdLogic(Connection conn , int deptId) throws Exception{
    String result = "";
    String sql = " select DEPT_ID from PERSON where SEQ_ID = " + deptId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
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
   * 取得角色名称
   * @param conn
   * @param roleId
   * @return
   * @throws Exception
   */
  public String getRoleNameLogic(Connection conn , int roleId) throws Exception{
    String result = "";
    String sql = " select PRIV_NAME from USER_PRIV where SEQ_ID = " + roleId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
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
   * 获取管理范围
   * @param conn
   * @param dutyId
   * @return
   * @throws Exception
   */
  
  public String getDutyTypeLogic(Connection conn , int dutyId) throws Exception{
    String result = "";
    String sql = " select DUTY_NAME from oa_attendance_conf where SEQ_ID = " + dutyId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
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
   * 删除用户
   * @param conn
   * @param seqIds   PERSON表中的SEQ_ID串（以逗号为分隔）
   * @throws Exception
   */
  
  public void deleteAll(Connection conn, String seqIds, String loginUserId) throws Exception {
    String sql = "DELETE FROM PERSON WHERE NOT USER_ID = 'admin' and NOT USER_ID = '" + loginUserId + "' and SEQ_ID IN(" + seqIds + ")";
    YHORM orm = new YHORM();
    String seqIdStr[] = seqIds.split(",");
    for (String seqId : seqIdStr) {
      YHPerson person = (YHPerson) orm.loadObjSingle(conn, YHPerson.class, Integer.parseInt(seqId));
      String sql1 = "DELETE FROM oa_pm_employee_info WHERE USER_ID ='"+person.getUserId()+"'";
      PreparedStatement pstmt1 = null;
      try {
        pstmt1 = conn.prepareStatement(sql1);
        pstmt1.executeUpdate();
      } catch (Exception e) {
        throw e;
      } finally {
        YHDBUtility.close(pstmt1, null, null);
      }
    }
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
  
  /**
   * 清空密码
   * @param conn
   * @param seqIdStrs  SEQ_ID串（以逗号为分隔）
   * @throws Exception
   */
  
  public void clearPassword(Connection conn, String seqIdStrs) throws Exception {
    String strs = "";
    String sql = "UPDATE PERSON SET PASSWORD ='" + YHPassEncrypt.encryptPass(strs) + "' WHERE SEQ_ID IN (" + seqIdStrs + ")";
    //System.out.println(sql+"BBB");
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
  
  /**
   * 清空在线时长
   * @param conn
   * @param seqIdStrs
   * @throws Exception
   */
  
  public void clearVisitTime(Connection conn, String seqIdStrs) throws Exception {
    int strs = 0;
    String sql = "UPDATE PERSON SET ON_LINE = " + strs + " WHERE SEQ_ID IN (" + seqIdStrs + ")";
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
  
  /**
   * 判断用户是否有密码
   * @param conn
   * @param deptId
   * @return
   * @throws Exception
   */
  
  public String getPassword(Connection conn , int deptId) throws Exception{
    String result = "";
    String sql = " select PASSWORD from PERSON where SEQ_ID = " + deptId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
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
   * 获取用户信息中没有登录过的
   * @param conn
   * @param deptId
   * @return
   * @throws Exception
   */
  
  public String getNotLogin(Connection conn , int deptId) throws Exception{
    String result = "";
    String sql = " select NOT_LOGIN from PERSON where SEQ_ID = " + deptId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
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
   * 获取密码为空用户显示为红色，禁止登录用户显示为灰色 的判断条件
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  
  public YHPerson getMenuPara(Connection conn, int seqId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHPerson org = null;
    try {
      String queryStr = "select SEQ_ID, USER_ID, NOT_LOGIN, PASSWORD from PERSON WHERE SEQ_ID="+seqId;
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if (rs.next()) {
        org = new YHPerson();
        org.setSeqId(rs.getInt("SEQ_ID"));
        org.setUserId(rs.getString("USER_ID"));
        org.setPassword(rs.getString("PASSWORD"));
        org.setNotLogin(rs.getString("NOT_LOGIN"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return org;
  }
  
  /**
   * 用户管理查询
   * @param conn
   * @param request
   * @param userId
   * @param userName
   * @param byname
   * @param sex
   * @param deptId
   * @param userPriv
   * @param postPriv
   * @param notLogin
   * @param notViewUser
   * @param notViewTable
   * @param dutyType
   * @param canbroadcast
   * @param lastVisitTime
   * @param loginUserPriv
   * @param isAdminRole
   * @return
   * @throws Exception
   */
  
  public ArrayList<YHPerson> getSearchPersonList(Connection conn,Map request, String userId, String userName
      , String byname, String sex, String deptId, String userPriv, String postPriv, String notLogin
      , String notViewUser, String notViewTable, String dutyType, String lastVisitTime
      , String loginUserPriv, boolean isAdminRole) throws Exception{
    String whereStr = "";
    Statement stmt = null;
    ResultSet rs = null;
    YHPerson persons = null;
    ArrayList<YHPerson> personList = new ArrayList<YHPerson>();
    int privNo = Integer.parseInt(getPrivNoStr(conn, loginUserPriv));
    if(sex.trim().equals("0")){
      whereStr += " and SEX=" + sex;
    }else if(sex.equals("1")){
      whereStr += " and SEX=" + sex;
    }
    if(postPriv.equals("0")){
      whereStr += " and POST_PRIV=" + postPriv;
    }else if(postPriv.equals("1")){
      whereStr += " and POST_PRIV=" + postPriv;
    }else if(postPriv.equals("2")){
      whereStr += " and POST_PRIV=" + postPriv;
    }
    if(notLogin.equals("0")){
      whereStr += " and NOT_LOGIN='" + notLogin +"'";
    }else if(notLogin.equals("1")){
      whereStr += " and NOT_LOGIN='" + notLogin +"'";
    }
    if(notViewUser.equals("0")){
      whereStr += " and NOT_VIEW_USER='" + notViewUser +"'";
    }else if(notViewUser.equals("1")){
      whereStr += " and NOT_VIEW_USER='" + notViewUser +"'";
    }
    if(notViewTable.equals("0")){
      whereStr += " and NOT_VIEW_TABLE='" + notViewTable +"'";
    }else if(notViewTable.equals("1")){
      whereStr += " and NOT_VIEW_TABLE='" + notViewTable +"'";
    }
    if(!isAdminRole){
      whereStr += " and PERSON.USER_PRIV = USER_PRIV.SEQ_ID and USER_PRIV.PRIV_NO > " + privNo + " and not USER_PRIV.SEQ_ID = 1";
    }else{
      whereStr += " and PERSON.USER_PRIV = USER_PRIV.SEQ_ID";
    }
    try {
      stmt = conn.createStatement();
      String sql = "select PERSON.SEQ_ID" +
                   ",PERSON.DEPT_ID" +
                   ",PERSON.USER_ID" +
                   ",PERSON.USER_NAME" +
                   ",PERSON.USER_PRIV" +
                   ",PERSON.NOT_LOGIN" +
                   ",PERSON.PASSWORD" +
                   ",PERSON.POST_PRIV from PERSON, USER_PRIV where 1=1" + whereStr + "";
    //String query = " order by DIA_DATE desc,DIA_TIME DESC ";
    //sql += query;
    if(!YHUtility.isNullorEmpty(userId)){ 
      sql = sql + " and USER_ID like '%" + userId + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(userName)){ 
      sql = sql + " and USER_NAME like '%" + userName + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(byname)){ 
      sql = sql + " and BYNAME like '%" + byname + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(userPriv)){ 
      sql = sql + " and USER_PRIV = '" + userPriv + "'"; 
    } 
    if(!YHUtility.isNullorEmpty(deptId)){ 
      sql = sql + " and DEPT_ID = " + Integer.parseInt(deptId) + ""; 
    } 
    if(lastVisitTime.equals("desc")){
      sql = sql + " order by LAST_VISIT_TIME desc";
    }else if(lastVisitTime.equals("asc")){
      sql = sql + " order by LAST_VISIT_TIME asc";
    }
    rs = stmt.executeQuery(sql);
    while (rs.next()) {
      persons = new YHPerson();
      persons.setSeqId(rs.getInt("SEQ_ID"));
      persons.setDeptId(rs.getInt("DEPT_ID"));
      persons.setUserId(rs.getString("USER_ID"));
      persons.setUserName(rs.getString("USER_NAME"));
      persons.setUserPriv(rs.getString("USER_PRIV"));
      persons.setPostPriv(rs.getString("POST_PRIV"));
      persons.setPassword(rs.getString("PASSWORD"));
      persons.setNotLogin(rs.getString("NOT_LOGIN"));
      personList.add(persons);
    }
  } catch (Exception ex) {
    throw ex;
  } finally {
    YHDBUtility.close(stmt, rs, log);
  }
  return personList;
}
  
  /**
   * 获取person表中的POST_PRIV
   * @param conn
   * @param loginUserId
   * @return
   * @throws Exception
   */
  
  public String getPostPrivLogic(Connection conn , String loginUserId) throws Exception{
    String result = "";
    String sql = " select POST_PRIV from PERSON where USER_ID = '" + loginUserId +"'" ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String postPriv = rs.getString(1);
        if(postPriv != null){
          result = postPriv;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public String getPostPrivOtherLogic(Connection conn , int loginDeptId) throws Exception{
    String result = "";
    String sql = " select DEPT_NAME from oa_department where SEQ_ID = " + loginDeptId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String deptName = rs.getString(1);
        if(deptName != null){
          result = deptName;
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
   * 最近10次批量个性设置日志列表
   * @param conn
   * @param request
   * @param loginUserId
   * @return
   * @throws Exception
   */
  
  public String getSetLogList(Connection conn,Map request, int loginUserId) throws Exception{
    
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    String sql = "";
    if (dbms.equals("sqlserver")) {
      sql = "select top 10 SEQ_ID" +
      ",USER_ID" +
      ",TIME" +
      ",IP" +
      ",TYPE" +
      ",REMARK from oa_sys_log where USER_ID=" + loginUserId + " and TYPE='19' order by TIME desc";
    } else if (dbms.equals("mysql")) {
      sql = "select SEQ_ID" +
      ",USER_ID" +
      ",TIME" +
      ",IP" +
      ",TYPE" +
      ",REMARK from oa_sys_log where USER_ID=" + loginUserId + " and TYPE='19' order by TIME desc limit 10";
    } else if (dbms.equals("oracle")) {
      sql = "select SEQ_ID" +
      ",USER_ID" +
      ",TIME" +
      ",IP" +
      ",TYPE" +
      ",REMARK from oa_sys_log where USER_ID=" + loginUserId + " and TYPE='19' and rownum <= 10 order by TIME desc";
    } else {
      sql = "select SEQ_ID" +
      ",USER_ID" +
      ",TIME" +
      ",IP" +
      ",TYPE" +
      ",REMARK from oa_sys_log where USER_ID=" + loginUserId + " and TYPE='19' order by TIME desc";
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    
    return pageDataList.toJson();
  }
  
  /**
   * 批量个性设置日志逻辑
   */
  
  public String getPostPriv(Connection conn , String loginUserId) throws Exception{
    String postPriv = "";
    String sql = " select POST_PRIV from PERSON where USER_ID = '" + loginUserId +"'" ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String postPrivStr = rs.getString(1);
        if(postPrivStr != null){
          postPriv = postPrivStr;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return postPriv;
  }
  
  /**
   * 获取USER_PRIV表中的PRIV_NO
   * @param conn
   * @param loginUserPriv
   * @return
   * @throws Exception
   */
  
  public static String getPrivNoStr(Connection conn , String loginUserPriv) throws Exception{
    String privNo = "";
    String sql = " select PRIV_NO from USER_PRIV where SEQ_ID = '" + loginUserPriv +"'" ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String privNoStr = rs.getString(1);
        if(privNoStr != null){
          privNo = privNoStr;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return privNo;
  }
  
  public String getPrivNo(Connection conn , String loginUserPriv) throws Exception{
    String privNo = "";
    String sql = " select PRIV_NO from USER_PRIV where USER_ID = '" + loginUserPriv +"'" ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String privNoStr = rs.getString(1);
        if(privNoStr != null){
          privNo = privNoStr;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return privNo;
  }
  
  public String getPrivNo(Connection conn , int loginUserPriv) throws Exception{
    String privNo = "";
    String sql = " select PRIV_NO from USER_PRIV where SEQ_ID = " + loginUserPriv;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String privNoStr = rs.getString(1);
        if(privNoStr != null){
          privNo = privNoStr;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return privNo;
  }
  
  public String getSearchSetUserIdStr(Connection dbConn , String whereStr) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String whereStrs = "";
    String userIdStrs = "";
    try {
      String queryStr = "SELECT PERSON.SEQ_ID,PERSON.USER_ID,PERSON.DEPT_ID from PERSON, USER_PRIV where PERSON.USER_PRIV = USER_PRIV.SEQ_ID";
      if(!whereStr.equals("")){
        queryStr +=" and (" + whereStr + ")";
      }
      //System.out.println(queryStr);
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {
        userIdStrs += rs.getString("SEQ_ID")+",";
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return userIdStrs;
  }
  
  public void getSearchSet(Connection dbConn , String dept, String role, String user ,String setStr, String privId1) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String admins = "233";  //问姚
    String whereStr = "";
    String whereStrs = "";
    String serStrs = "";
    //String userIdStrs = "";
    if(!dept.equals("0")){
      if(!dept.equals("")){
        whereStr += "DEPT_ID in (" + dept + ") or ";
      }
      if(!role.equals("")){
        whereStr += "PERSON.USER_PRIV in (" + role + ") or ";
      }
      if(!user.equals("")){
        whereStr += "PERSON.SEQ_ID in (" + user + ") or ";
      }
    }
    if(!dept.equals("0")){
      whereStrs = whereStr.substring(0, whereStr.length() - 4);
    }
    //System.out.println(whereStrs+"OPOPOPOPO");
    String userIdStrs = getSearchSetUserIdStr(dbConn, whereStrs);  //获取person表中SEQ_ID串
    
    if(!"".equals(privId1) && !"".equals(userIdStrs)){
      //getSearchSet2(dbConn , privId1, userIdStrs);                    //通讯范围LOGIC逻辑
    }
    //System.out.println(setStr+"WEWEWEWE");
    if (!YHUtility.isNullorEmpty(userIdStrs)) {
    String userIds = userIdStrs.substring(0, userIdStrs.length() - 1);
      if (!"".equals(setStr)) {
        serStrs = setStr.substring(0, setStr.length() - 1);
        //System.out.println(setStr);
        try {
          String queryStr = "update PERSON set " + serStrs + " where not USER_ID = 'admin' and SEQ_ID in (" + userIds + ")";
          stmt = dbConn.createStatement();
          //System.out.println(queryStr);
          stmt.executeUpdate(queryStr);
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stmt, rs, log);
        }
      }
    }
  }
  
  /**
   * 通讯范围逻辑
   * @param dbConn
   * @param privId1     通讯范围角色ID
   * @param whereStrs   PERSON表中SEQ_ID串
   * @throws Exception
   */
  
  public void getSearchSet2(Connection dbConn , String privId1, String userIdStrs) throws Exception{
    String[] userIds = userIdStrs.split(",");
    String funcs = "";
    for(int i = 0; i < userIds.length; i++){
      funcs = userIds[i];
      if(funcs.equals("")){
        continue;
      }
      if(existsTableNo(dbConn, funcs)){
        setUpdateModulePriv(dbConn, privId1, funcs);
      }else{
        setInsertModulePriv(dbConn, privId1, funcs);
      }
    }
  }
  
  /**
   * 判断MODULE_PRIV表中 USER_ID = USER_ID ,MODULE_ID='0'是否存在
   * @param dbConn
   * @param deptNo
   * @return
   * @throws Exception
   */
  
  public boolean existsTableNo(Connection dbConn, String userIdStr)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) FROM oa_function_priv WHERE MODULE_ID='0' and USER_ID like '"+userIdStr+"'";
      rs = stmt.executeQuery(sql);
      //System.out.println(sql);
      long count = 0;
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count == 1) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  public void setUpdateModulePriv(Connection dbConn, String privId1, String funcs) throws Exception {

    String sql = "update oa_function_priv set PRIV_ID = '" + privId1 + "' WHERE USER_ID like '" + funcs + "' and MODULE_ID='0'";
    //System.out.println(sql);
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
      //System.out.println(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  public void setInsertModulePriv(Connection dbConn, String privId1, String funcs) throws Exception {
    
    String sql = "insert into oa_function_priv (USER_ID,MODULE_ID,DEPT_PRIV,ROLE_PRIV,PRIV_ID) values('"+funcs+"','0','1','2','"+privId1+"')";
    //System.out.println(sql);
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  public String getNoPassUserId(Connection dbConn) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String userIdStr = "";
    try {
      String queryStr = "SELECT SEQ_ID,USER_ID,USER_NAME,PASSWORD from PERSON";
      //System.out.println(queryStr);
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {
        String password = rs.getString("PASSWORD");
        if(!YHPassEncrypt.isValidPas("",password.trim())){
          continue;
        }
        userIdStr += String.valueOf(rs.getInt("SEQ_ID")) + ",";
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return userIdStr;
  }
  
  public String getNoPassUserName(Connection dbConn) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String userNameStr = "";
    try {
      String queryStr = "SELECT SEQ_ID,USER_ID,USER_NAME,PASSWORD from PERSON";
      //System.out.println(queryStr);
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {
        String password = rs.getString("PASSWORD");
        if(!YHPassEncrypt.isValidPas("",password.trim())){
          continue;
        }
        userNameStr += rs.getString("USER_NAME") + ",";
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return userNameStr;
  }
  
  /**
   * 获取UserKey
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  
  public String getUserKey(Connection dbConn ,int seqId) throws Exception{
    //SELECT  SEQ_ID FROM EMAIL_BODY WHERE SEQ_ID IN( SELECT BODY_ID FROM EMAIL WHERE BOX_ID = 0)
    String sql = " SELECT USEING_KEY FROM PERSON WHERE SEQ_ID=" + seqId;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String result = null;
    try{
      pstmt = dbConn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        result = rs.getString(1);
      }
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }
  
  /**
   * 获取用户信息
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  
  public String getUserInformation(Connection dbConn ,int seqId) throws Exception{
    //SELECT  SEQ_ID FROM EMAIL_BODY WHERE SEQ_ID IN( SELECT BODY_ID FROM EMAIL WHERE BOX_ID = 0)
    String sql = " SELECT USER_ID,PASSWORD FROM PERSON WHERE SEQ_ID=" + seqId;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String result = null;
    String userId = null;
    String passwod = null;
    try{
      pstmt = dbConn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        userId = rs.getString(1);
        passwod = rs.getString(2);
      }
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    if(YHUtility.isNullorEmpty(passwod)){
      passwod = "";
    }
    result = userId + "," + YHUsbKey.md5Hex(passwod);
    return result;
  }
  
  /**
   * 获取人员是否在线
   * @param dbConn
   * @param userId
   * @return
   * @throws Exception
   */
  
  public String getUserStateImg(Connection dbConn ,int userId) throws Exception{
    String sql = " SELECT USER_STATE FROM oa_online WHERE USER_ID=" + userId;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String result = null;
    try{
      pstmt = dbConn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        result = rs.getString(1);
      }
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }
  
  public List<YHPerson> getPersonByPriv(Connection dbConn, String[] str)
      throws Exception {
    List<YHPerson> personList = new ArrayList<YHPerson>();
    YHORM orm = new YHORM();
    personList = orm.loadListSingle(dbConn, YHPerson.class, str);
    return personList;
  }
  
  /**
   * 获取新添加用户的SEQ_ID
   * @param conn
   * @return
   * @throws Exception
   */
  
  public String getPersonAddSeqLogic(Connection conn) throws Exception{
    String result = "";
    String sql = " select MAX(SEQ_ID) from PERSON" ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        int seqId = rs.getInt(1);
        result = String.valueOf(seqId);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public String getPersonFuncLogic(Connection conn , int seqId) throws Exception{
    String result = "";
    String sql = " select USER_ID, DEPT_PARENT from PERSON where SEQ_ID = " + seqId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String deptName = rs.getString(1);
        int deptParent = rs.getInt(2);
        if(deptName != null){
          result = deptName + "," + "DEPT_ID=" +String.valueOf(seqId) + "," + "DEPT_PARENT="+String.valueOf(deptParent);
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
   * 获取排序号最大的
   * @param conn
   * @return
   * @throws Exception
   */
  
  public String getMaxPrivNoLogic(Connection conn) throws Exception{
    String result = "";
    String sql = " select MAX(PRIV_NO) from USER_PRIV";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public String getMaxUserPrivLogic(Connection conn, String privNo) throws Exception{
    String result = "";
    String sql = " select SEQ_ID from USER_PRIV WHERE PRIV_NO = '" + privNo + "'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
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
   * 选择角色
   * @param conn
   * @return
   * @throws Exception
   */
  
  public List getRoleList(Connection conn, int privNo, YHPerson person) throws Exception{
    YHORM orm = new YHORM();
    List list = new ArrayList();
    Map filters = new HashMap();
    if(person.isAdminRole()){
      list  = orm.loadListSingle(conn ,YHUserPriv.class , filters);
    }else{
      String[] filterStr = new String[]{" PRIV_NO >"+privNo};
      list  = orm.loadListSingle(conn ,YHUserPriv.class , filterStr);
    }
    return list;
  }
  
  public ArrayList<YHDbRecord> toExportPersonData(Connection conn,Map request, String userId, String userName
      , String byname, String sex, String deptId, String userPriv, String postPriv, String notLogin
      , String notViewUser, String notViewTable, String dutyType, String lastVisitTime
      , String loginUserPriv, boolean isAdminRole) throws Exception{
    String whereStr = "";
    Statement stmt = null;
    ResultSet rs = null;
    YHPerson persons = null;
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
    int privNo = Integer.parseInt(getPrivNoStr(conn, loginUserPriv));
    if(sex.trim().equals("0")){
      whereStr += " and SEX=" + sex;
    }else if(sex.equals("1")){
      whereStr += " and SEX=" + sex;
    }
    if(postPriv.equals("0")){
      whereStr += " and POST_PRIV=" + postPriv;
    }else if(postPriv.equals("1")){
      whereStr += " and POST_PRIV=" + postPriv;
    }else if(postPriv.equals("2")){
      whereStr += " and POST_PRIV=" + postPriv;
    }
    if(notLogin.equals("0")){
      whereStr += " and NOT_LOGIN='" + notLogin +"'";
    }else if(notLogin.equals("1")){
      whereStr += " and NOT_LOGIN='" + notLogin +"'";
    }
    if(notViewUser.equals("0")){
      whereStr += " and NOT_VIEW_USER='" + notViewUser +"'";
    }else if(notViewUser.equals("1")){
      whereStr += " and NOT_VIEW_USER='" + notViewUser +"'";
    }
    if(notViewTable.equals("0")){
      whereStr += " and NOT_VIEW_TABLE='" + notViewTable +"'";
    }else if(notViewTable.equals("1")){
      whereStr += " and NOT_VIEW_TABLE='" + notViewTable +"'";
    }
    if(!isAdminRole){
      whereStr += " and PERSON.USER_PRIV = USER_PRIV.SEQ_ID and USER_PRIV.PRIV_NO > " + privNo + " and not USER_PRIV.SEQ_ID = 1";
    }else{
      whereStr += " and PERSON.USER_PRIV = USER_PRIV.SEQ_ID";
    }
    try {
      stmt = conn.createStatement();
      String sql = "select PERSON.SEQ_ID" +
                   ",PERSON.USER_ID" +
                   ",PERSON.DEPT_ID" +
                   ",PERSON.USER_NAME" +
                   ",PERSON.SEX" +
                   ",PERSON.BIRTHDAY" +
                   ",PERSON.USER_PRIV" +
                   ",PERSON.BYNAME" +
                   ",PERSON.USER_NO" +
                   ",PERSON.POST_PRIV" +
                   ",PERSON.MOBIL_NO" +
                   ",PERSON.BIND_IP" +
                   ",PERSON.ADD_HOME" +
                   ",PERSON.FAX_NO_DEPT" +
                   ",PERSON.POST_NO_HOME" +
                   ",PERSON.TEL_NO_HOME" +
                   ",PERSON.EMAIL" +
                   ",PERSON.OICQ" +
                   ",PERSON.MSN from PERSON, USER_PRIV where 1=1" + whereStr + "";

    //String query = " order by DIA_DATE desc,DIA_TIME DESC ";
    //sql += query;
    if(!YHUtility.isNullorEmpty(userId)){ 
      sql = sql + " and USER_ID like '%" + userId + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(userName)){ 
      sql = sql + " and USER_NAME like '%" + userName + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(byname)){ 
      sql = sql + " and BYNAME like '%" + byname + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(userPriv)){ 
      sql = sql + " and USER_PRIV = '" + userPriv + "'"; 
    } 
    if(!YHUtility.isNullorEmpty(deptId)){ 
      sql = sql + " and DEPT_ID = " + Integer.parseInt(deptId) + ""; 
    } 
    if(lastVisitTime.equals("desc")){
      whereStr += " order by LAST_VISIT_TIME desc";
    }else if(lastVisitTime.equals("asc")){
      whereStr += " order by LAST_VISIT_TIME asc";
    }
    rs = stmt.executeQuery(sql);
    while (rs.next()) {
      YHDbRecord record = new YHDbRecord();
      int seqId = rs.getInt(1);
      userId = rs.getString(2);
      int deptIds = rs.getInt(3);
      userName = rs.getString(4);
      sex = rs.getString(5);
      Date birthday = rs.getTimestamp(6);
      userPriv = rs.getString(7);
      String byName = rs.getString(8);
      String userNo = rs.getString(9);
      postPriv = rs.getString(10);
      String mobileNo = rs.getString(11);
      String bindIp = rs.getString(12);
      String addHome = rs.getString(13);
      String faxNoDept = rs.getString(14);
      String postNoHome = rs.getString(15);
      String telNoHome = rs.getString(16);
      String email = rs.getString(17);
      String oicqNo = rs.getString(18);
      String msn = rs.getString(19);
      
      record.addField("ID", seqId);
      record.addField("用户名", userId);
      record.addField("部门", getDeptName(conn, deptIds).toString());
      record.addField("姓名", userName);
      record.addField("性别", getSexFunc(conn, sex).toString());
      record.addField("生日",YHUtility.getDateTimeStrCn(birthday));
      record.addField("角色",getRoleNameLogic(conn, Integer.parseInt(userPriv)).toString());
      record.addField("别名",byName);
      record.addField("用户排序号",userNo);
      record.addField("管理范围",getPostPrivFlag(conn, postPriv).toString());
      record.addField("手机",mobileNo);
      record.addField("IP",bindIp);
      record.addField("工作电话",addHome);
      record.addField("工作传真",faxNoDept);
      record.addField("家庭地址",postNoHome);
      record.addField("邮编",telNoHome);
      record.addField("家庭电话",telNoHome);
      record.addField("E-mail",email);
      record.addField("QQ",oicqNo);
      record.addField("MSN",msn);
      result.add(record);
    }
  } catch (Exception ex) {
    throw ex;
  } finally {
    YHDBUtility.close(stmt, rs, log);
  }
  return result;
}
  
  public ArrayList<YHDbRecord> toExportPersonData1(Connection conn) throws Exception{
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
    String sql = "SELECT SEQ_ID"
      + ",USER_ID "
      + ",DEPT_ID "
      + ",USER_NAME "
      + ",SEX "
      + ",BIRTHDAY "
      + ",USER_PRIV "
      + ",BYNAME "
      + ",USER_NO "
      + ",POST_PRIV "
      + ",MOBIL_NO "
      + ",BIND_IP "
      + ",ADD_HOME "
      + ",FAX_NO_DEPT "
      + ",POST_NO_HOME "
      + ",TEL_NO_HOME"
      + ",EMAIL"
      + ",OICQ"
      + ",MSN"
      + " from PERSON";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery() ;
      while (rs.next()) {
        YHDbRecord record = new YHDbRecord();
        int seqId = rs.getInt(1);
        String userId = rs.getString(2);
        int deptId = rs.getInt(3);
        String userName = rs.getString(4);
        String sex = rs.getString(5);
        Date birthday = rs.getTimestamp(6);
        String userPriv = rs.getString(7);
        String byName = rs.getString(8);
        String userNo = rs.getString(9);
        String postPriv = rs.getString(10);
        String mobileNo = rs.getString(11);
        String bindIp = rs.getString(12);
        String addHome = rs.getString(13);
        String faxNoDept = rs.getString(14);
        String postNoHome = rs.getString(15);
        String telNoHome = rs.getString(16);
        String email = rs.getString(17);
        String oicqNo = rs.getString(18);
        String msn = rs.getString(19);
        
        record.addField("ID", seqId);
        record.addField("用户名", userId);
        record.addField("部门", getDeptName(conn, deptId).toString());
        record.addField("姓名", userName);
        record.addField("性别", getSexFunc(conn, sex).toString());
        record.addField("生日",YHUtility.getDateTimeStrCn(birthday));
        record.addField("角色",getRoleNameLogic(conn, Integer.parseInt(userPriv)).toString());
        record.addField("别名",byName);
        record.addField("用户排序号",userNo);
        record.addField("管理范围",getPostPrivFlag(conn, postPriv).toString());
        record.addField("手机",mobileNo);
        record.addField("IP",bindIp);
        record.addField("工作电话",addHome);
        record.addField("工作传真",faxNoDept);
        record.addField("家庭地址",postNoHome);
        record.addField("邮编",telNoHome);
        record.addField("家庭电话",telNoHome);
        record.addField("E-mail",email);
        record.addField("QQ",oicqNo);
        record.addField("MSN",msn);
        result.add(record);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  
  /**
   * 取得部门名称

   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public String getDeptName(Connection conn , int deptId) throws Exception{
    String result = "";
    String sql = " select DEPT_NAME from oa_department where SEQ_ID = " + deptId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  
  /**
   * 取得管理范围

   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public String getPostPrivFlag(Connection conn , String postPriv) throws Exception{
    String result = "";
    if("0".equals(postPriv)){
      result = "本部门";
    }else if("1".equals(postPriv)){
      result = "全体";
    }else if("2".equals(postPriv)){
      result = "指定部门";
    }
    return result;
  }
  
  /**
   * 取得性别

   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public String getSexFunc(Connection conn , String sex) throws Exception{
    String result = "";
    if("0".equals(sex)){
      result = "男";
    }else if("1".equals(sex)){
      result = "女";
    }
    return result;
  }
  /**
   * 根据条件去seq_id
   * @param conn
   * @param name
   * @param str
   * @return
   * @throws Exception
   */
  public Set getPersonByTerm(Connection conn,String name,String str,Set set)throws Exception{
    String userIds = "";
    String sql = " select SEQ_ID from PERSON" ;
    if(str!=null&&!str.equals("")){
      if(name.equals("USER_PRIV")){
        String[] strArray = str.split(",");
        String newUserIds = "";
        for (int i = 0; i < strArray.length; i++) {
          newUserIds = newUserIds + "'" + strArray[i] + "',";
        }
        if(strArray.length>0){
          newUserIds = newUserIds.substring(0,newUserIds.length()-1);
        }
        sql = sql + " where " + name + " in(" + newUserIds + ")";
      }
      if(name.equals("DEPT_ID")){
        sql = sql + " where " + name + " in(" + str + ")";
      } 
    }
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        set.add(rs.getString("SEQ_ID"));
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    if(!userIds.equals("")){
      userIds = userIds.substring(0, userIds.length()-1);
    }
    return set;
  }
  
  public boolean existsDeptNameIsMultiple(Connection dbConn, String deptName)
  throws Exception {
    long count = 0;
    String sql = "SELECT count(*) FROM oa_department WHERE DEPT_NAME = '" + deptName + "'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      //System.out.println(sql);
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count >= 2) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  
  public boolean existsDepartment(Connection dbConn, String deptName)
  throws Exception {
    long count = 0;
    String sql = "SELECT count(*) FROM oa_department WHERE DEPT_NAME = '" + deptName + "'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      //System.out.println(sql);
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count <= 0) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  
  public boolean existsUserId(Connection dbConn, String userId, int seqId)
  throws Exception {
    long count = 0;
    String sql = "SELECT count(*) FROM PERSON WHERE NOT USER_ID = '" + userId + "' AND SEQ_ID =" + seqId;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      //System.out.println(sql);
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count >= 1) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  
  public boolean existsUserId2(Connection dbConn, String userId)
  throws Exception {
    long count = 0;
    String sql = "SELECT count(*) FROM PERSON WHERE USER_ID = '" + userId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      //System.out.println(sql);
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count >= 1) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  
  public boolean existsRole(Connection dbConn, String role)
  throws Exception {
    long count = 0;
    String sql = "SELECT count(*) FROM USER_PRIV WHERE PRIV_NAME = '" + role + "'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      //System.out.println(sql);
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count <= 0) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  
  public int getDeptIdLogic(Connection conn , String deptName) throws Exception{
    int result = 0;
    String sql = " select SEQ_ID from oa_department where DEPT_NAME = '" + deptName +"'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        int toId = rs.getInt(1);
        result = toId;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public int getUserPrivIdLogic(Connection conn , String privName) throws Exception{
    int result = 0;
    String sql = " select SEQ_ID from USER_PRIV where PRIV_NAME = '" + privName +"'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        int toId = rs.getInt(1);
        result = toId;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
}
