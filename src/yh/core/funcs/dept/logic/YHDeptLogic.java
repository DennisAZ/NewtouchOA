package yh.core.funcs.dept.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHDsTable;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.org.data.YHOrganization;
import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHDeptLogic {
  private static Logger log = Logger.getLogger(YHDeptLogic.class);
  private int userIdStrs = 0;
  public void saveDept(Connection dbConn,String deptName , int deptParentNo , String deptNo ,String telNo , String faxNo , String deptFunc ) throws Exception {
    String query = "insert into oa_department (DEPT_NAME , DEPT_PARENT ,DEPT_NO , TEL_NO, FAX_NO, DEPT_FUNC ) VALUES (?,?,?,?,?,?)";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.prepareStatement(query);
      stmt.setString(1, deptName);
      stmt.setInt(2, deptParentNo);
      stmt.setString(3, deptNo);
      stmt.setString(4, telNo);
      stmt.setString(5, faxNo);
      stmt.setString(6, deptFunc);
      stmt.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  /**
   * 级联查出该部门下的所有子部门的seqId（不包含跟节点）
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public List deleteDeptMul(Connection dbConn, int seqId) throws Exception {
    List list = new ArrayList();
    YHDepartment de = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT * FROM oa_department WHERE DEPT_PARENT = '" + seqId + "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        de = new YHDepartment();
        de.setSeqId(rs.getInt("SEQ_ID"));
        de.setDeptName(rs.getString("DEPT_NAME"));
        de.setDeptNo(rs.getString("DEPT_NO"));
        list.add(de);
      }
      List tmpList = new ArrayList();
      tmpList.addAll(list);
      for(Iterator it = tmpList.iterator(); it.hasNext();){
//        for(int x = 0; x < list.size(); x++){
//          YHDepartment 
//          
//        }
        YHDepartment der = (YHDepartment)(it.next());
        List srclist = deleteDeptMul(dbConn,der.getSeqId());
        list.addAll(srclist);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  
  public boolean existsTableNo(Connection dbConn, String deptNo)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) FROM oa_department WHERE DEPT_NO = '" + deptNo
          + "'";
      rs = stmt.executeQuery(sql);
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

  public boolean existsDeptPosition(Connection dbConn, String positionNo)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) FROM oa_dept_role WHERE POSITION_NO = '"
          + positionNo + "'";
      rs = stmt.executeQuery(sql);
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

  public List selectDept(Connection dbConn, String total) throws Exception {
    List list = new ArrayList();
    YHDsTable dt = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT * FROM oa_table_dicts";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        dt = new YHDsTable();
        dt.setSeqId(rs.getInt("SEQ_ID"));
        dt.setTableNo(rs.getString("table_No"));
        dt.setTableName(rs.getString("table_Name"));
        dt.setTableDesc(rs.getString("table_Desc"));
        dt.setClassName(rs.getString("class_Name"));
        dt.setCategoryNo(rs.getString("category_No"));
        dt.setDbNo(rs.getString("db_No"));
        list.add(dt);
      }

    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  public List getDeptList(Connection conn) throws Exception{
    YHORM orm = new YHORM();
    List list = new ArrayList();
    Map filters = new HashMap();
    list  = orm.loadListSingle(conn ,YHDepartment.class , filters);
    return list;
  }
  
  public YHDepartment getDepartmentById(int deptId , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    YHDepartment department = new YHDepartment();
    department = (YHDepartment) orm.loadObjSingle(conn, YHDepartment.class, deptId);
    return department;
  }
  public YHDepartment getDepartmentByUser(String userId , Connection conn) throws Exception{
    Map map = new HashMap();
    YHORM orm = new YHORM();
    map.put("USER_ID", userId);
    YHPerson user = (YHPerson) orm.loadObjSingle(conn, YHPerson.class, map);
    return this.getDepartmentById(user.getDeptId() , conn);
  }
  
  //根据id串取得名字串
  public String getNameByIdStr(String ids  , Connection conn) throws Exception, Exception{
    String names = "";
    if (ids == null || "".equals(ids.trim())) {
      return names ;
    }
    if ("0".equals(ids) || "ALL_DEPT".equals(ids)) {
      names = "全体部门";
      return names;
    }
    
    for (String id : ids.split(",")) {
      String name = null;
      if ("0".equals(id) || "ALL_DEPT".equalsIgnoreCase(id)) {
        name = "全体部门";
      }
      else if (YHUtility.isNumber(id)) {
        name = this.getDeptNameById(conn, Integer.parseInt(id));
      }
      if (name != null) {
        names += name + ",";
      }
    }
    
    if (names.endsWith(",")) {
      names = names.substring(0, names.length() - 1);
    }
    return names;
  }
  
  public String getDeptNameById(Connection dbConn, int id) throws Exception, Exception {
    Statement stm = null;
    ResultSet rs = null;
    String name = null;
    try {
      String sql = "select DEPT_NAME from oa_department where seq_id = " + id;
      stm  = dbConn.createStatement();
      rs = stm.executeQuery(sql);
      if (rs.next()) {
        name = rs.getString("DEPT_NAME");
      }
    } catch (Exception ex ) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return name;
  }
  
  /**
   * 取得部门下级级次树.
   *String str =  logic.getDeptTreeJson(0,conn);
   * @param deptId
   * @param conn
   * @return String [{value :'dd', text:'dds'},{value:'dd', text:'dd'}]
   * @throws Exception
   */
  public String getDeptTreeJson(int deptId , Connection conn) throws Exception{
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    this.getDeptTree(deptId, sb, 0 , conn);
    if(sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    
    return sb.toString();
  }
  
  /**
   * 取得产品类别下级级次树.
   *String str =  logic.getDeptTreeJson(0,conn);
   * @param deptId
   * @param conn
   * @return String [{value :'dd', text:'dds'},{value:'dd', text:'dd'}]
   * @throws Exception
   */
  public String getProductTypeTreeJson(String productTypeId , Connection conn) throws Exception{
	  StringBuffer sb = new StringBuffer();
	  sb.append("[");
	  this.getProductTypeTree(productTypeId, sb, 0 , conn);
	  if(sb.charAt(sb.length() - 1) == ','){
		  sb.deleteCharAt(sb.length() - 1);
	  }
	  sb.append("]");
	  
	  return sb.toString();
  }
  /**
   * 取得部门下级级次树.包括本部门在里面
   *String str =  logic.getDeptTreeJson(0,conn);
   * @param deptId
   * @param conn
   * @return String [{value :'dd', text:'dds'},{value:'dd', text:'dd'}]
   * @throws Exception
   */
  public String getDeptTreeJson(YHPerson u , Connection conn) throws Exception{
    StringBuffer sb = new StringBuffer();
    int deptId = 0 ;
    if (!u.isAdminRole()) {
      deptId = u.getDeptId();
    }
    sb.append("[");
    //本部门    int level = 0 ;
    if (deptId != 0) {
      sb.append("{");
      sb.append("text:'&nbsp;└"  + this.getNameById(deptId, conn) + "',");
      sb.append("value:" + deptId );
      sb.append("},");
      level = 1;
    }
    this.getDeptTree(deptId, sb, level , conn);
    if(sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    
    return sb.toString();
  }
  public void getDeptTree(int deptId , StringBuffer sb , int level , Connection conn) throws Exception{
    //首选分级，然后记录级数，是否为最后一个。。。    List<Map> list = new ArrayList();
    String query = "select SEQ_ID , DEPT_NAME from oa_department where DEPT_PARENT=" + deptId + " order by DEPT_NO, DEPT_NAME asc";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String deptName = rs.getString("DEPT_NAME");
        int seqId = rs.getInt("SEQ_ID");
        Map map = new HashMap();
        map.put("deptName", deptName);
        map.put("seqId", seqId);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    for(int i = 0; i < list.size(); i++){
      String flag = "&nbsp;├";
      if(i == list.size() - 1 ){
        flag = "&nbsp;└";
      }
      String tmp = "";
      for(int j = 0 ;j < level ; j++){
        tmp += "&nbsp;│";
      }
      flag = tmp + flag;
      
      Map dp = list.get(i);
      int seqId = (Integer)dp.get("seqId");
      String deptName = (String)dp.get("deptName");
      sb.append("{");
      sb.append("text:'" + flag + YHUtility.encodeSpecial(deptName) + "',");
      sb.append("value:" + seqId );
      sb.append("},");
    
      this.getDeptTree(seqId, sb, level + 1 , conn);
    }
   
  }
  
  /**
   * 取得产品类别下级级次树.
   * @param productTypeId
   * @param conn
   * @return String [{value :'dd', text:'dds'},{value:'dd', text:'dd'}]
   * @throws Exception
   */
  public void getProductTypeTree(String productTypeId , StringBuffer sb , int level , Connection conn) throws Exception{
	  //首选分级，然后记录级数，是否为最后一个。。。	  List<Map> list = new ArrayList();
	  String query = "select id , name from erp_product_type where parent_id='" + productTypeId + "' and is_del='0' order by id,name asc";
	  Statement stmt = null;
	  ResultSet rs = null;
	  try {
		  stmt = conn.createStatement();
		  rs = stmt.executeQuery(query);
		  while (rs.next()) {
			  String seqId = rs.getString("id");
			  String productTypeName = rs.getString("name");
			  Map map = new HashMap();
			  map.put("seqId", seqId);
			  map.put("productTypeName", productTypeName);
			  list.add(map);
		  }
	  } catch (Exception ex) {
		  throw ex;
	  } finally {
		  YHDBUtility.close(stmt, rs, log);
	  }
	  for(int i = 0; i < list.size(); i++){
		  String flag = "&nbsp;├";
		  if(i == list.size() - 1 ){
			  flag = "&nbsp;└";
		  }
		  String tmp = "";
		  for(int j = 0 ;j < level ; j++){
			  tmp += "&nbsp;│";
		  }
		  flag = tmp + flag;
		  
		  Map dp = list.get(i);
		  String seqId = (String)dp.get("seqId");
		  String productTypeName = (String)dp.get("productTypeName");
		  sb.append("{");
		  sb.append("text:'" + flag + YHUtility.encodeSpecial(productTypeName) + "',");
		  sb.append("value:'" + seqId +"'");
		  sb.append("},");
		  
		  this.getProductTypeTree(seqId, sb, level + 1 , conn);
	  }
	  
  }
  
  //syl
  public String getDeptTreeJson(int deptId , Connection conn,String[] postDeptArray) throws Exception{
    StringBuffer sb = new StringBuffer();
    Set childDeptId = new HashSet();
    for (int i = 0; i < postDeptArray.length; i++) {
      //childDeptId.add(postDeptArray[i]);
      getChildDept(conn,Integer.parseInt(postDeptArray[i]),childDeptId); 
    }
    this.getDeptTreeByPostDept(deptId, sb, 0 , conn,childDeptId);
    if(sb.length()>0&&sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  
  public Set getChildDept(Connection conn,int postDept,Set childDeptId) throws Exception{
 
    List<Map> list = new ArrayList();
    String query = "select SEQ_ID , DEPT_NAME from oa_department where DEPT_PARENT=" + postDept +" order by DEPT_NO, DEPT_NAME asc";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String deptName = rs.getString("DEPT_NAME");
        int seqId = rs.getInt("SEQ_ID");
        Map map = new HashMap();
        map.put("deptName", deptName);
        map.put("seqId", seqId);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    childDeptId.add(postDept);
    for(int i = 0; i < list.size(); i++){
      Map dp = list.get(i);
      int seqId = (Integer)dp.get("seqId");
      childDeptId.add(seqId);
      getChildDept(conn,seqId,childDeptId);
    }
    return childDeptId;
  }
  
  public void getDeptTreeByPostDept(int deptId , StringBuffer sb , int level , Connection conn,Set childDeptId) throws Exception{
    //首选分级，然后记录级数，是否为最后一个。。。    List<Map> list = new ArrayList();
    String query = "select SEQ_ID , DEPT_NAME from oa_department where DEPT_PARENT=" + deptId +" order by DEPT_NO ASC, DEPT_NAME asc";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String deptName = rs.getString("DEPT_NAME");
        int seqId = rs.getInt("SEQ_ID");
        Map map = new HashMap();
        map.put("deptName", deptName);
        map.put("seqId", seqId);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    
    for(int i = 0; i < list.size(); i++){
      String flag = " ├";
      if(i == list.size() - 1 ){
        flag = " └";
      }
      String tmp = "";
      for(int j = 0 ;j < level ; j++){
        tmp += " │";
      }
      flag = tmp + flag;
      Map dp = list.get(i);
      int seqId = (Integer)dp.get("seqId");
      if(childDeptId.contains(seqId)){
        String deptName = (String)dp.get("deptName");
        sb.append("{");
        sb.append("text:'" + flag + YHUtility.encodeSpecial(deptName) + "',");
        sb.append("value:" + seqId );
        sb.append("},");
      }
      this.getDeptTreeByPostDept(seqId, sb, level + 1 , conn,childDeptId);
    }
   
  }
  
  //cc
  public String getDeptTreeJson(int deptId , Connection conn,String[] postDeptArray,int userDeptId) throws Exception{
    StringBuffer sb = new StringBuffer();
    String tmp = "";
    for (int i = 0; i < postDeptArray.length; i++) {
      if(postDeptArray[i].equals(tmp)){
        continue;
      }
      this.getDeptTreeByPostDept(deptId, sb, 0 , conn, postDeptArray[i], userDeptId);
      tmp = postDeptArray[i];
    }
    if(sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    
    return sb.toString();
  }
  
  public void getDeptTreeByPostDept(int deptId, StringBuffer sb, int level, Connection conn, String postDeptId, int userDeptId) throws Exception{
    //首选分级，然后记录级数，是否为最后一个。。。
    List<Map> list = new ArrayList();
    String query = "select SEQ_ID , DEPT_NAME,DEPT_PARENT from oa_department where DEPT_PARENT=" + deptId + " order by DEPT_NO, DEPT_NAME asc";
    Statement stmt = null;
    ResultSet rs = null;
    int deptParent =  getParentDept(conn,userDeptId);
    int deptParentTemp = 0;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String deptName = rs.getString("DEPT_NAME");
        int seqId = rs.getInt("SEQ_ID");
        deptParentTemp = rs.getInt("DEPT_PARENT");
        Map map = new HashMap();
        map.put("deptName", deptName);
        map.put("seqId", seqId);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    
    for(int i = 0 ;i < list.size() ;i ++){
      String flag = " ├";
      if(i == list.size() - 1 ){
        flag = " └";
      }
      String tmp = "";
      for(int j = 0 ;j < level ; j++){
        tmp += " │";
      }
      flag = tmp + flag;
      Map dp = list.get(i);
      int seqId = (Integer)dp.get("seqId");
      if(seqId != userDeptId){
        if(postDeptId.equals(String.valueOf(seqId))){
          String deptName = (String)dp.get("deptName");
          sb.append("{");
          sb.append("text:'" + flag + deptName + "',");
          sb.append("value:" + seqId );
          sb.append("},");
        }
        this.getDeptTreeByPostDept(seqId, sb, level + 1, conn, postDeptId, userDeptId);
      }
    }
  }
  
  public int getParentDept(Connection conn,int seqId)throws Exception{
    List<YHDepartment> list = new ArrayList<YHDepartment>();
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "select SEQ_ID, DEPT_NAME, DEPT_PARENT from oa_department where SEQ_ID =" + seqId;
    int deptParent = 0;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        deptParent = rs.getInt("DEPT_PARENT");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return deptParent;
  }
  
  public List<YHDepartment> getDeptByParentId(int deptId ,Connection conn) throws Exception {
    // TODO Auto-generated method stub
    YHORM orm = new YHORM();
    List<YHDepartment> list = new ArrayList();
    Map filters = new HashMap();
    filters.put("DEPT_PARENT", deptId);
    list  = orm.loadListSingle(conn ,YHDepartment.class , filters);
    return list;
  }
  
  public String getNameById(int deptId , Connection conn) throws Exception{
    String query = "select DEPT_NAME from oa_department where SEQ_ID=" + deptId;
    Statement stmt = null;
    ResultSet rs = null;
    String deptName = "";
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      if (rs.next()) {
        deptName = rs.getString("DEPT_NAME");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return deptName;
  }
 
 // ddkfkd/dsafkds/
  public void getDeptNameLong(Connection dbConn, int seqId ,StringBuffer sb) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    int parentId = 0 ;
    String sql = "SELECT DEPT_NAME,SEQ_ID,DEPT_PARENT FROM oa_department WHERE SEQ_ID = '" + seqId + "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        parentId = rs.getInt("DEPT_PARENT");
        sb.insert(0,  rs.getString("DEPT_NAME") + "/"); 
      }
      if(parentId != 0){
        getDeptNameLong(dbConn,parentId , sb);
      }
    } catch (Exception ex ) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 取得部门名称（本部门的）
   * @param conn
   * @param deptId
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
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 取得部门名称
   * @param conn
   * @param deptId
   * @return
   * @throws Exception
   */
  
  public String getDeptNameLogic(Connection conn , int deptId) throws Exception{
    String result = "成功添加部门：";
    String sql = " select DEPT_NAME, DEPT_PARENT from oa_department where SEQ_ID = " + deptId;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String deptName = rs.getString(1);
        int deptParent = rs.getInt(2);
        if(deptName != null){
          result += deptName + "," + "DEPT_ID=" + String.valueOf(deptId) + "," + "DEPT_PARENT=" + String.valueOf(deptParent);
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public int getDeptIdLogic(Connection conn , String deptName) throws Exception{
    int result = 0;
    String sql = " select SEQ_ID from oa_department where DEPT_NAME = '" + deptName + "'";
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
  
  public String getDeptAddSeqLogic(Connection conn) throws Exception{
    String result = "";
    String sql = " select MAX(SEQ_ID) from oa_department" ;
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
  
  public String deleteDeptMul2(Connection dbConn, int seqIds) throws Exception {
    List<Map> list = new ArrayList();
    int seqID = 0;
    YHDepartment de = null;
    Statement stmt = null;
    ResultSet rs = null;
    String seqIdStr = null;
    String sql = "SELECT SEQ_ID FROM oa_department WHERE DEPT_PARENT = '" + seqIds + "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        de = new YHDepartment();
        int seqId = rs.getInt("SEQ_ID");
        Map map = new HashMap();
        map.put("seqId", seqId);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    if(list.size() > 0){
      for(int i = 0; i < list.size(); i++){
        Map dp = list.get(i);
        int seq = (Integer)dp.get("seqId");
        String seqs = String.valueOf(dp.get("seqId"));
        seqIdStr += seqs + ",";
        this.deleteDeptMul2(dbConn, seq);
      }
    }
    return seqIdStr;
  }
  //cc
  
  /**
   * 取得部门下级级次树. 对选择上级部门有限制
   *String str =  logic.getDeptTreeJson(0,conn);
   * @param deptId
   * @param conn
   * @return String [{value :'dd', text:'dds'},{value:'dd', text:'dd'}]
   * @throws Exception
   */
  
  public String getDeptTreeJson1(int deptId , Connection conn ,int userDeptIdStr) throws Exception{
    userIdStrs = userDeptIdStr;
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    this.getDeptTree1(deptId, sb, 0 , conn);
    if(sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    
    return sb.toString();
  }
  
  public void getDeptTree1(int deptId , StringBuffer sb , int level , Connection conn) throws Exception{
    //首选分级，然后记录级数，是否为最后一个。。。
    List<Map> list = new ArrayList();
    String query = "select SEQ_ID , DEPT_NAME from oa_department where DEPT_PARENT=" + deptId +  " order by DEPT_NO, DEPT_NAME asc";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String deptName = rs.getString("DEPT_NAME");
        int seqId = rs.getInt("SEQ_ID");
        Map map = new HashMap();
        map.put("deptName", deptName);
        map.put("seqId", seqId);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    for(int i = 0; i < list.size(); i++){
      String flag = "&nbsp;├";
      if(i == list.size() - 1 ){
        flag = "&nbsp;└";
      }
      String tmp = "";
      for(int j = 0 ;j < level ; j++){
        tmp += "&nbsp;│";
      }
      flag = tmp + flag;
      Map dp = list.get(i);
      int seqId = (Integer)dp.get("seqId");
      String deptName = (String)dp.get("deptName");
      if(seqId != this.userIdStrs){
        sb.append("{");
        sb.append("text:'" + flag + YHUtility.encodeSpecial(deptName) + "',");
        sb.append("value:" + seqId );
        sb.append("},");
        this.getDeptTree1(seqId, sb, level + 1 , conn);
      }
    }
  }
  
  public String getDeptTreeJson0(int deptId , Connection conn,String[] postDeptArray,int userDeptId) throws Exception{
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < postDeptArray.length; i++) {
      this.getDeptTreeByPostDept0(deptId, sb, 0 , conn, postDeptArray[i], userDeptId);
    }
    if(sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    
    return sb.toString();
  }
  
  public void getDeptTreeByPostDept0(int deptId, StringBuffer sb, int level, Connection conn, String postDeptId, int userDeptId) throws Exception{
    //首选分级，然后记录级数，是否为最后一个。。。
    List<Map> list = new ArrayList();
    String query = "select SEQ_ID, DEPT_NAME,DEPT_PARENT from oa_department where DEPT_PARENT=" + deptId + " order by DEPT_NO, DEPT_NAME asc";
    Statement stmt = null;
    ResultSet rs = null;
    int deptParent =  getParentDept(conn,userDeptId);
    int deptParentTemp = 0;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String deptName = rs.getString("DEPT_NAME");
        int seqId = rs.getInt("SEQ_ID");
        deptParentTemp = rs.getInt("DEPT_PARENT");
        Map map = new HashMap();
        map.put("deptName", deptName);
        map.put("seqId", seqId);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    
    for(int i = 0; i < list.size(); i++){
      String flag = " ├";
      if(i == list.size() - 1 ){
        flag = " └";
      }
      String tmp = "";
      for(int j = 0 ;j < level ; j++){
        tmp += " │";
      }
      flag = tmp + flag;
      Map dp = list.get(i);
      int seqId = (Integer)dp.get("seqId");
      if(seqId != userDeptId){
        if(postDeptId.equals(String.valueOf(seqId))){
          String deptName = (String)dp.get("deptName");
          sb.append("{");
          sb.append("text:'" + flag + deptName + "',");
          sb.append("value:" + seqId );
          sb.append("},");
        }
        this.getDeptTreeByPostDept0(seqId, sb, level + 1, conn, postDeptId, userDeptId);
      }
    }
  }
  
  /**
   * 获得上级部门select框（本部门，不出现当前部门的下级部门）
   * @param deptId
   * @param conn
   * @param postDeptArray
   * @param userDeptIdFunc
   * @return
   * @throws Exception
   */
  
  public String getDeptTreeJsonSelf(int deptId , Connection conn,String[] postDeptArray, int userDeptIdFunc) throws Exception{
    StringBuffer sb = new StringBuffer();
    Set childDeptId = new HashSet();
    for (int i = 0; i < postDeptArray.length; i++) {
      getChildDept(conn,Integer.parseInt(postDeptArray[i]),childDeptId); 
    }
    this.getDeptTreeByPostDeptSelf(deptId, sb, 0 , conn,childDeptId ,userDeptIdFunc);
    if (sb.length() > 1) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
 
  public void getDeptTreeByPostDeptSelf(int deptId , StringBuffer sb , int level , Connection conn,Set childDeptId, int userDeptIdFunc) throws Exception{
    //首选分级，然后记录级数，是否为最后一个。。。
    List<Map> list = new ArrayList();
    String query = "select SEQ_ID , DEPT_NAME from oa_department where DEPT_PARENT=" + deptId + " order by DEPT_NO, DEPT_NAME asc";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String deptName = rs.getString("DEPT_NAME");
        int seqId = rs.getInt("SEQ_ID");
        Map map = new HashMap();
        map.put("deptName", deptName);
        map.put("seqId", seqId);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    
    for(int i = 0; i < list.size(); i++){
      String flag = " ├";
      if(i == list.size() - 1 ){
        flag = " └";
      }
      String tmp = "";
      for(int j = 0; j < level; j++){
        tmp += " │";
      }
      flag = tmp + flag;
      Map dp = list.get(i);
      int seqId = (Integer)dp.get("seqId");
      if(seqId != userDeptIdFunc){
        if(childDeptId.contains(seqId)){
          String deptName = (String)dp.get("deptName");
          sb.append("{");
          sb.append("text:'" + flag + YHUtility.encodeSpecial(deptName) + "',");
          sb.append("value:" + seqId );
          sb.append("},");
        }
      this.getDeptTreeByPostDeptSelf(seqId, sb, level + 1 , conn,childDeptId, userDeptIdFunc);
      }
    }
  }
  
  public ArrayList<YHDbRecord> toExportDeptData(Connection conn) throws Exception{
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
    String sql = "SELECT "
          + " DEPT_NAME "
          + ",DEPT_NO "
          + ",DEPT_PARENT "
          + ",TEL_NO "
          + ",FAX_NO"
          + ",DEPT_FUNC"
          + " from oa_department";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery() ;
      while (rs.next()) {
        YHDbRecord record = new YHDbRecord();
        String deptName = rs.getString(1);
        String deptNo = rs.getString(2);
        int deptParent = rs.getInt(3);
        String telNo = rs.getString(4);
        String faxNo = rs.getString(5);
        String deptFunc = rs.getString(6);
        
        record.addField("部门名称", deptName);
        record.addField("部门排序号", deptNo);
        record.addField("上级部门",  getDeptParentName(conn, deptParent).toString());
        record.addField("部门电话", telNo);
        record.addField("部门传真", faxNo);
        record.addField("部门职能",deptFunc);
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
   * 取得上级部门名称

   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  
  public String getDeptParentName(Connection conn , int deptParent) throws Exception{
    String result = "";
    String sql = " select DEPT_NAME from oa_department where SEQ_ID = " + deptParent ;
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
  
  public boolean existsDeptName(Connection dbConn, String deptName)
  throws Exception {
    long count = 0;
    String sql = "SELECT count(*) FROM oa_department WHERE DEPT_NAME = '" + deptName + "'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
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
  
  public boolean existsDeptNameIsMultiple(Connection dbConn, String deptName)
  throws Exception {
    long count = 0;
    String sql = "SELECT count(*) FROM oa_department WHERE DEPT_NAME = '" + deptName + "'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count > 1) {
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
  
  public ArrayList<YHOrganization> getOrganization(Connection conn) throws Exception{
    YHOrganization org = null;
    ArrayList<YHOrganization> result = new ArrayList<YHOrganization>();
    String queryStr = "select SEQ_ID, UNIT_NAME from oa_organization";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(queryStr);
      rs = ps.executeQuery() ;
      while (rs.next()) {
        org = new YHOrganization();
        org.setSeqId(Integer.parseInt(rs.getString(1)));
        org.setUnitName(rs.getString(2));
        result.add(org);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  
  /**
   * 删除部门时级联删除部门中的人员，系统管理员不删除
   * @param conn
   * @param seqId
   * @throws Exception
   */
  public void deleteDepPerson(Connection conn, int seqId) throws Exception {
    String sql = "DELETE FROM PERSON WHERE DEPT_ID =" + seqId + "";
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
   * 删除部门
   * @param conn
   * @param seqId
   * @throws Exception
   */
  public void deleteDept(Connection conn, int seqId) throws Exception {
    String sql = "DELETE FROM oa_department WHERE SEQ_ID =" + seqId + "";
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
  
  
}
