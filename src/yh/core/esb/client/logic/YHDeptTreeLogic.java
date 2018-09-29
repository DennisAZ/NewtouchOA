package yh.core.esb.client.logic;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHExtDept;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.user.api.core.db.YHDbconnWrap;

public class YHDeptTreeLogic {
  public String getDeptTreeJson(String deptId , String seqId, Connection conn) throws Exception{
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    this.getDeptTree(deptId, sb, 0 , seqId , conn);
    if(sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    
    return sb.toString();
  }
  public String getDeptName(Connection conn , String deptId) throws Exception{
    String result = "";
    String sql = " select DEPT_NAME from oa_dept_ext where DEPT_ID = '" + deptId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        result = rs.getString(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  public String getDeptGuidByName(Connection conn , String orgName) throws Exception{
	    String result = "";
	    String sql = " select DEPT_ID  from oa_dept_ext where DEPT_NAME = '" + orgName + "'";
	    PreparedStatement ps = null;
	    ResultSet rs = null ;
	    try {
	      ps = conn.prepareStatement(sql);
	      rs = ps.executeQuery();
	      if(rs.next()){
	        result = rs.getString(1);
	      }
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      YHDBUtility.close(ps, rs, null);
	    }
	    return result;
	  }
  public YHExtDept getDeptByEsbUser(Connection conn , String esbUser) throws Exception {
    YHExtDept dept = null;
    String query = "select * from oa_dept_ext WHERE ESB_USER = '" + esbUser + "'";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      if (rs.next()) {
        String deptNo = rs.getString("DEPT_NO");
        String deptName = rs.getString("DEPT_NAME"); 
         String deptParent = rs.getString("DEPT_PARENT");
         String deptDesc = rs.getString("DEPT_DESC");
         String deptId = rs.getString("DEPT_ID");
         String deptStatue = rs.getString("DEPT_STATUE");
         String deptTelLine = rs.getString("DEPT_TEL_LINE");
         String deptCode = rs.getString("DEPT_CODE");
         String deptFullName = rs.getString("DEPT_FULL_NAME");
         String deptPasscard = rs.getString("DEPT_PASSCARD");
         String deptGroup = rs.getString("DEPT_GROUP");
         String deptOfficialName = rs.getString("DEPT_OFFICIAL_NAME");
         dept = new YHExtDept( deptNo ,  deptName,  esbUser,
              deptParent,  deptDesc, deptStatue, deptTelLine, deptCode, deptFullName, deptPasscard, deptGroup,deptOfficialName);
         dept.setDeptId(deptId);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return dept; 
  }
  public void getDeptTree(String deptId , StringBuffer sb , int level , String getOutId, Connection conn) throws Exception{
    //首选分级，然后记录级数，是否为最后一个。。。
    List<Map> list = new ArrayList();
    String query = "select DEPT_ID, DEPT_NAME from oa_dept_ext where DEPT_PARENT='" + deptId + "' order by DEPT_NO, DEPT_NAME asc";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String deptName = rs.getString("DEPT_NAME");
        String seqId = rs.getString("DEPT_ID");
        if (!YHUtility.isNullorEmpty(getOutId) 
            && getOutId.equals(seqId)) {
          continue;
        }
        Map map = new HashMap();
        map.put("deptName", deptName);
        map.put("seqId", seqId);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
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
      String deptName = (String)dp.get("deptName");
      sb.append("{");
      sb.append("text:\"" + flag + YHUtility.encodeSpecial(deptName) + "\",");
      sb.append("value:'" + seqId + "'" );
      sb.append("},");
      this.getDeptTree(seqId, sb, level + 1 , getOutId , conn);
    }
   
  }
  public void getDeptsByDeptParent(Connection conn , String deptParent , int level , StringBuffer sb, String deptPermisssions ) throws Exception {
    
    String query = "select DEPT_ID , ESB_USER, DEPT_NAME, DEPT_TEL_LINE from oa_dept_ext where DEPT_ID = '"+deptParent+"' and DEPT_STATUE = 1 and ESB_USER !='' and DEPT_ID in ("+deptPermisssions+") order by DEPT_NO ASC, DEPT_NAME asc";
    Statement stmt = null;
    ResultSet rs = null;
    String nbsp = "├";
    for(int i = 0 ;i < level;i++){
      nbsp = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + nbsp;
    }
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      if (rs.next()) {
        String deptName = YHUtility.encodeSpecial(rs.getString("DEPT_NAME"));
        String deptId =  YHUtility.encodeSpecial(rs.getString("DEPT_ID"));
        String esbUser =  YHUtility.encodeSpecial(rs.getString("ESB_USER"));
        String deptTelLine =  YHUtility.encodeSpecial(rs.getString("DEPT_TEL_LINE"));
        sb.append("{");
        sb.append("deptName:\"").append(nbsp).append(deptName).append("\"");
        sb.append(",deptId:\"").append(deptId).append("\"");
        sb.append(",esbUser:\"").append(esbUser).append("\"");
        sb.append(",deptTelLine:\"").append(deptTelLine).append("\"");
        sb.append("},");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
     query = "select DEPT_ID from oa_dept_ext where DEPT_PARENT = '"+deptParent+"' and DEPT_STATUE = 1 and DEPT_ID in ("+deptPermisssions+") order by DEPT_NO ASC, DEPT_NAME asc";
    List<String> list = new ArrayList();
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String deptId =  YHUtility.encodeSpecial(rs.getString("DEPT_ID"));
        list.add(deptId);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    for (String ss : list) {
      this.getDeptsByDeptParent(conn, ss, level + 1, sb, deptPermisssions);
    }
  }
  
  public void getDeptsByDeptParent2(Connection conn , String deptParent , int level , StringBuffer sb ) throws Exception {
    String query = "select DEPT_ID , ESB_USER, DEPT_NAME from oa_dept_ext where DEPT_ID = '"+deptParent+"' and DEPT_STATUE = 1 and ESB_USER !='' order by DEPT_NO ASC, DEPT_NAME asc";
    Statement stmt = null;
    ResultSet rs = null;
    String nbsp = "├";
    for(int i = 0 ;i < level;i++){
      nbsp = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + nbsp;
    }
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      if (rs.next()) {
        String deptName = YHUtility.encodeSpecial(rs.getString("DEPT_NAME"));
        String deptId =  YHUtility.encodeSpecial(rs.getString("DEPT_ID"));
        String esbUser =  YHUtility.encodeSpecial(rs.getString("ESB_USER"));
        sb.append("{");
        sb.append("deptName:\"").append(nbsp).append(deptName).append("\"");
        sb.append(",deptId:\"").append(deptId).append("\"");
        sb.append(",esbUser:\"").append(esbUser).append("\"");
        sb.append("},");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
     query = "select DEPT_ID from oa_dept_ext where DEPT_PARENT = '"+deptParent+"' order by DEPT_NO ASC, DEPT_NAME asc";
    List<String> list = new ArrayList();
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String deptId =  YHUtility.encodeSpecial(rs.getString("DEPT_ID"));
        list.add(deptId);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    for (String ss : list) {
      this.getDeptsByDeptParent2(conn, ss, level + 1, sb);
    }
  }
  
  public String getDepts2(Connection conn) throws Exception {
    String query = "select DEPT_ID,DEPT_NO , DEPT_DESC , ESB_USER,DEPT_PARENT , DEPT_NAME, DEPT_FLAG, DEPT_PERMISSIONS, DEPT_PERMISSIONS_DESC, DEPT_STATUE, DEPT_TEL_LINE, DEPT_CODE, DEPT_FULL_NAME, DEPT_PASSCARD, DEPT_GROUP from oa_dept_ext ";
    Statement stmt = null;
    ResultSet rs = null;
    int count = 0 ;
    StringBuffer sb = new StringBuffer();
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String deptName = rs.getString("DEPT_NAME");
        String deptId = rs.getString("DEPT_ID");
        String deptDesc  =  rs.getString("DEPT_DESC");
        String deptNo = rs.getString("DEPT_NO");
        String deptParent = rs.getString("DEPT_PARENT");
        String esbUser = rs.getString("ESB_USER");
        String deptFlag = rs.getString("DEPT_FLAG");
        String deptPermissions = rs.getString("DEPT_PERMISSIONS");
        String deptPermissionsDesc = rs.getString("DEPT_PERMISSIONS_DESC");
        String deptStatue = rs.getString("DEPT_STATUE");
        String deptTelLine = rs.getString("DEPT_TEL_LINE");
        String deptCode = rs.getString("DEPT_CODE");
        String deptFullName = rs.getString("DEPT_FULL_NAME");
        String deptPasscard = rs.getString("DEPT_PASSCARD");
        String deptGroup = rs.getString("DEPT_GROUP");
        YHExtDept ed = new YHExtDept( deptNo ,  deptName,  esbUser,
             deptParent,  deptDesc, deptFlag, deptPermissions, deptPermissionsDesc, deptStatue, deptTelLine, deptCode, deptFullName, deptPasscard, deptGroup);
        ed.setDeptId(deptId);
        sb.append(YHObjectUtility.writeObject(ed)).append(",");
        count++;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    if (count>0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  public List<YHExtDept> getList(String xml) throws Exception {
    SAXReader saxReader = new SAXReader(); 
    StringReader rs = new StringReader(xml);
    Document document = saxReader.read(rs);
    Element root = document.getRootElement(); 
    
    return null;
  }
  public YHExtDept getDept(Connection conn, String deptId) throws Exception {
    String query = "select DEPT_ID,DEPT_NO , DEPT_DESC , ESB_USER,DEPT_PARENT , DEPT_NAME, DEPT_FLAG, DEPT_PERMISSIONS"
    		         + ", DEPT_PERMISSIONS_DESC, DEPT_STATUE, DEPT_TEL_LINE, DEPT_CODE, DEPT_FULL_NAME, DEPT_PASSCARD"
    		         + ", DEPT_GROUP from oa_dept_ext where DEPT_ID='" + deptId + "'";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      if (rs.next()) {
        String deptName = rs.getString("DEPT_NAME");
        YHExtDept dept = new YHExtDept();
        dept.setDeptDesc(rs.getString("DEPT_DESC"));
        dept.setDeptName(deptName);
        dept.setDeptId(rs.getString("DEPT_ID"));
        dept.setDeptNo(rs.getString("DEPT_NO"));
        dept.setDeptParent(rs.getString("DEPT_PARENT"));
        dept.setEsbUser(rs.getString("ESB_USER"));
        dept.setDeptFlag(rs.getString("DEPT_FLAG"));
        dept.setDeptPermissions(rs.getString("DEPT_PERMISSIONS"));
        dept.setDeptPermissionsDesc(rs.getString("DEPT_PERMISSIONS_DESC"));
        dept.setDeptStatue(rs.getString("DEPT_STATUE"));
        dept.setDeptTelLine(rs.getString("DEPT_TEL_LINE"));
        dept.setDeptCode(rs.getString("DEPT_CODE"));
        dept.setDeptFullName(rs.getString("DEPT_FULL_NAME"));
        dept.setDeptPasscard(rs.getString("DEPT_PASSCARD"));
        dept.setDeptGroup(rs.getString("DEPT_GROUP"));
        return dept;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return null;
  }
  public String saveDept(Connection conn, YHExtDept de, String seqId) throws Exception {

    String sql = "";
    if (!YHUtility.isNullorEmpty(seqId)) {
      sql = " select count(1) count from oa_dept_ext where ESB_USER='" + de.getEsbUser() + "' and DEPT_ID != '" + seqId + "' and ESB_USER !='' ";
    }
    else{
      sql = " select count(1) count from oa_dept_ext where ESB_USER='" + de.getEsbUser() + "' and ESB_USER !=''";
    }
    Statement stmt0 = null;
    ResultSet rs = null;
    int count = 0;
    try {
      stmt0 = conn.createStatement();
      rs = stmt0.executeQuery(sql);
      if (rs.next()) {
        count = rs.getInt("count");
      }
    }
    catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt0, rs, null);
    }
    if(count > 0){
      return "0";//有重复的esb用户配置
    }
    
    String query = "";
    String seqIdStr = seqId;
    if (!YHUtility.isNullorEmpty(seqId)) {
      query = "update  oa_dept_ext set DEPT_NO = ? , DEPT_NAME = ? , DEPT_DESC = ? , DEPT_PARENT = ?  , ESB_USER = ? , DEPT_STATUE = ? , DEPT_TEL_LINE = ?  , DEPT_CODE = ?, DEPT_FULL_NAME = ?, DEPT_Passcard = ?, DEPT_GROUP = ? where DEPT_ID=?";
    } else {
      query = "insert into  oa_dept_ext ( DEPT_NO , DEPT_NAME , DEPT_DESC , DEPT_PARENT , ESB_USER , DEPT_STATUE , DEPT_TEL_LINE , DEPT_CODE, DEPT_FULL_NAME, DEPT_PASSCARD, DEPT_GROUP, DEPT_ID , DEPT_PERMISSIONS) values (? , ? ,? ,? , ? ,? ,? ,? ,? ,? ,? ,? ,? ) ";
    }
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement(query);
      stmt.setString(1, de.getDeptNo());
      stmt.setString(2, de.getDeptName());
      stmt.setString(3, de.getDeptDesc());
      stmt.setString(4, de.getDeptParent());
      stmt.setString(5, de.getEsbUser());
      stmt.setString(6, de.getDeptStatue());
      stmt.setString(7, de.getDeptTelLine());
      stmt.setString(8, de.getDeptCode());
      stmt.setString(9, de.getDeptFullName());
      stmt.setString(10, de.getDeptPasscard());
      stmt.setString(11, de.getDeptGroup());
      
      if (YHUtility.isNullorEmpty(seqId)) {
        seqIdStr = de.getDeptCode();
      } 
      stmt.setInt(12, Integer.parseInt(seqIdStr));
      if(YHUtility.isNullorEmpty(seqId)){
        stmt.setString(13, "a");
      }
      stmt.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, null, null);
    }
    return "1";
  }
  public void saveDept(Connection conn, YHExtDept de) throws Exception {
    // TODO Auto-generated method stub
    String query = "insert into  oa_dept_ext ( DEPT_NO , DEPT_NAME , DEPT_DESC , DEPT_PARENT , ESB_USER , DEPT_ID, DEPT_FLAG" +
    		", DEPT_PERMISSIONS, DEPT_PERMISSIONS_DESC, DEPT_STATUE, DEPT_TEL_LINE, DEPT_CODE, DEPT_FULL_NAME, DEPT_PASSCARD, " +
    		"DEPT_GROUP ) values (? , ? ,? ,? , ? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,?  ) ";
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement(query);
      stmt.setString(1, de.getDeptNo());
      stmt.setString(2, de.getDeptName());
      stmt.setString(3, de.getDeptDesc());
      stmt.setString(4, de.getDeptParent());
      stmt.setString(5, de.getEsbUser());
      stmt.setString(6, de.getDeptId());
      
      stmt.setString(7, de.getDeptFlag());
      stmt.setString(8, de.getDeptPermissions());
      stmt.setString(9, de.getDeptPermissionsDesc());
      stmt.setString(10, de.getDeptStatue());
      stmt.setString(11, de.getDeptTelLine());
      
      stmt.setString(12, de.getDeptCode());
      stmt.setString(13, de.getDeptFullName());
      stmt.setString(14, de.getDeptPasscard());
      stmt.setString(15, de.getDeptGroup());
      
      stmt.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, null, null);
    }
  }
  public void selectDept(List<String> list , Connection conn , String deptId) throws Exception {
    String sql = "select dept_id from oa_dept_ext where DEPT_PARENT = '" + deptId + "'";
    //list.add(deptId);
    Statement stmt = null;
    ResultSet rs = null;
    List<String> list2 = new ArrayList();
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        list.add(rs.getString("dept_id"));
        list2.add(rs.getString("dept_id"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    for (String ss : list2) {
      if (!YHUtility.isNullorEmpty(ss)) {
        this.selectDept(list, conn, ss);
      }
    }
  }
  public void delDept(Connection conn , String deptId) throws Exception {
    List<String> list = new ArrayList();
    this.selectDept(list, conn, deptId);
    list.add(deptId);
    String sss = "";
    for (String ss : list){
      if (!YHUtility.isNullorEmpty(ss)) {
        sss += "'" + ss + "',";
      }
    }
    if (sss.endsWith(",")) {
      sss = sss.substring(0, sss.length() - 1);
    }
    String sql = "delete from oa_dept_ext where DEPT_ID IN (" + sss + ")";
    this.exeSql(sql, conn);
  }
  /**
   * 同步
   * @param depts
   */
  public void updateDept(String depts) {
    String[] objects = depts.split(",");
    YHDbconnWrap dbUtil = new YHDbconnWrap();
    Connection dbConn = null;
    try {      
      dbConn = dbUtil.getSysDbConn();
      String sql = "delete from oa_dept_ext";
      this.exeSql(sql, dbConn);
      for (String ss : objects) {
        if (!YHUtility.isNullorEmpty(ss)) {
          YHExtDept ext = (YHExtDept) YHObjectUtility.readObject(ss);
          this.saveDept(dbConn, ext);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      dbUtil.closeAllDbConns();
    }
  }
  public void exeSql(String sql , Connection conn) throws Exception {
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate(sql);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, null, null); 
    }
  }
  public  String getEsbUsers(Connection conn , String deptId) throws Exception {
    String deptIn = YHWorkFlowUtility.getInStr(deptId);
    String query = "select ESB_USER FROM oa_dept_ext WHERE DEPT_ID IN (" + deptIn + ")";
    Statement stmt = null;
    ResultSet rs = null;
    
    String esbUser = "";
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        esbUser += rs.getString("ESB_USER") + ",";
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    if (esbUser.endsWith(","))
      esbUser = esbUser.substring(0, esbUser.length() - 1);
    return esbUser;
  }
  
  
  public  static String[] getEsbUsers2(Connection conn , String deptId) throws Exception {
    String deptIn = YHWorkFlowUtility.getInStr(deptId);
    String query = "select DEPT_NAME,DEPT_ID FROM oa_dept_ext WHERE DEPT_ID IN (" + deptIn + ")";
    Statement stmt = null;
    ResultSet rs = null;
    String[] esb = new String[2];
    String esbUser = "";
    String esbDeptId = "";
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        esbUser += rs.getString("DEPT_NAME") + ",";
        esbDeptId += rs.getString("DEPT_ID") + ",";
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    if (esbUser.endsWith(","))
      esbUser = esbUser.substring(0, esbUser.length() - 1);
    
    if (esbDeptId.endsWith(","))
      esbDeptId = esbDeptId.substring(0, esbDeptId.length() - 1);
    
    esb[0] = esbDeptId;
    esb[1] = esbUser;
    return esb;
  }
  public static String getInStr(String str) {
    if (str == null || "".equals(str)) {
      return "";
    }
    String[] strs = str.split(",");
    String newStr = "";
    for (String tmp : strs) {
      
      if (tmp != null && !"".equals(tmp)) {
        if (tmp.startsWith("'") && tmp.endsWith("'")) {
          newStr += "" + tmp + ",";
        } else {
          newStr += "'" + tmp + "',";
        }
      } 
    }
    if (newStr.endsWith(",") ) {
      newStr = newStr.substring(0, newStr.length() - 1);
    }
    return newStr;
  }
  
  
  /**
   * 设置外发单位权限 -- yyb
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public void setPermissions(Connection dbConn, String dept, String permissions, String sendDept, String sendDeptDesc) throws Exception {
    YHORM orm = new YHORM();
    
    String deptArray[] = dept.split(",");
    for(int i = 0; i < deptArray.length; i++){
      Map<String, String> filters = new HashMap<String, String>();
      filters.put("DEPT_ID", deptArray[i]);
      YHExtDept extDept = (YHExtDept)orm.loadObjSingle(dbConn, YHExtDept.class, filters);
      if("0".equals(permissions)){
        extDept.setDeptPermissions("a");
        extDept.setDeptPermissionsDesc("");
      }
      else{
        extDept.setDeptPermissions(sendDept);
        extDept.setDeptPermissionsDesc(sendDeptDesc);
      }
      orm.updateSingle(dbConn, extDept);
    }
  }
  
  public String getPermissions(Connection dbConn) throws Exception{
    
    //获取esb本地配置单位
    YHEsbClientConfig config = YHEsbClientConfig.builder(YHSysProps.getWebPath() + YHEsbConst.CONFIG_PATH) ;
    
    YHORM orm = new YHORM();
    Map<String, String> filters = new HashMap<String, String>();
    filters.put("ESB_USER", config.getUserId());
    YHExtDept extDept = (YHExtDept) orm.loadObjSingle(dbConn, YHExtDept.class, filters);
    String deptPermissions = "";
    
    Map<String, String> filters1 = new HashMap<String, String>();
    List<YHExtDept> extDeptAllList = orm.loadListSingle(dbConn, YHExtDept.class, filters1);
    for(YHExtDept list1 : extDeptAllList){
      filters1.put(list1.getDeptId()+"", list1.getDeptParent());
    }
    
    if(extDept != null){
      deptPermissions = extDept.getDeptPermissions();
      if("a".equals(deptPermissions)){
        List<String> list = new ArrayList<String>();
        getParentDept(extDept.getDeptParent(), list, filters1);
        deptPermissions = list.toString();
        deptPermissions = deptPermissions.substring(1, deptPermissions.length() - 1);
        deptPermissions += "," + extDept.getDeptId() + ",";
        deptPermissions += getChildDept(dbConn, extDept.getDeptId()+"");
        deptPermissions = deptPermissions.substring(0, deptPermissions.length() - 1);
      }
      else{
        List<String> list = new ArrayList<String>();
        String deptPermissionsArray[] = deptPermissions.split(",");
        
        for(int i = 0; i < deptPermissionsArray.length; i++){
          getParentDept(deptPermissionsArray[i], list, filters1);
        }
        deptPermissions = list.toString();
        deptPermissions = deptPermissions.substring(1, deptPermissions.length() - 1);
      }
    }
    if(YHUtility.isNullorEmpty(deptPermissions)){
      deptPermissions = "0";
    }
    return deptPermissions;
  }
  
  public String getPermissions3(Connection dbConn, String deptPermissions) throws Exception{
    YHORM orm = new YHORM();
    
    Map<String, String> filters1 = new HashMap<String, String>();
    List<YHExtDept> extDeptAllList = orm.loadListSingle(dbConn, YHExtDept.class, filters1);
    for(YHExtDept list1 : extDeptAllList){
      filters1.put(list1.getDeptId()+"", list1.getDeptParent());
    }
    
    List<String> list = new ArrayList<String>();
    String deptPermissionsArray[] = deptPermissions.split(",");
    
    for(int i = 0; i < deptPermissionsArray.length; i++){
      getParentDept(deptPermissionsArray[i], list, filters1);
    }
    deptPermissions = list.toString();
    deptPermissions = deptPermissions.substring(1, deptPermissions.length() - 1);
    if(YHUtility.isNullorEmpty(deptPermissions)){
      deptPermissions = "0";
    }
    return deptPermissions;
  }
  
//  public String permissions2inStr(String deptPermissions){
//    String deptPermissionsArray[] = deptPermissions.split(",");
//    String returnStr = "";
//    for(int i = 0; i < deptPermissionsArray.length; i++){
//      returnStr += "'" + (deptPermissionsArray[i]).trim() + "',";
//    }
//    returnStr = returnStr.substring(0, returnStr.length() - 1);
//    return returnStr;
//  }
  
  public String getChildDept(Connection dbConn, String deptId) throws Exception{
    YHORM orm = new YHORM();
    String childDeptStr = "";
    Map<String, String> filters = new HashMap<String, String>();
    filters.put("DEPT_PARENT", deptId);
    List<YHExtDept> extDeptList = orm.loadListSingle(dbConn, YHExtDept.class, filters);
    
    for(YHExtDept extDept : extDeptList){
      childDeptStr += extDept.getDeptId() + ",";
      childDeptStr += getChildDept(dbConn, extDept.getDeptId()+"");
    }
    return childDeptStr;
  }
  
  public void getParentDept(String DeptId, List<String> list, Map<String, String>filters1) throws Exception{
    if(filters1.get(DeptId) != null ){
      if(list.contains(DeptId)){
        return;
      }
      list.add(DeptId);
      getParentDept(filters1.get(DeptId), list, filters1);
    }
  }
  
  /**
   * 条件查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public static List<YHExtDept> select(Connection dbConn, String[] str) throws Exception {
    YHORM orm = new YHORM();
    List<YHExtDept> list = orm.loadListSingle(dbConn, YHExtDept.class, str);
    return list;
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
  
  public void getDeptTree(int deptId , StringBuffer sb , int level , Connection conn) throws Exception{
    //首选分级，然后记录级数，是否为最后一个。。。
    List<Map> list = new ArrayList();
    String query = "select SEQ_ID,DEPT_ID , DEPT_NAME from oa_dept_ext where DEPT_PARENT=" + deptId + " order by DEPT_NO, DEPT_NAME asc";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String deptName = rs.getString("DEPT_NAME");
        int seqId = rs.getInt("SEQ_ID");
        String deptIdStr = rs.getString("DEPT_ID");
        Map map = new HashMap();
        map.put("deptName", deptName);
        map.put("seqId", seqId);
        map.put("deptId", deptIdStr);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
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
      String deptIdStr = (String)dp.get("deptId");
      sb.append("{");
      sb.append("text:'" + flag + YHUtility.encodeSpecial(deptName) + "',");
      sb.append("value:'" + deptIdStr + "'" );
      sb.append("},");
      //5分组
     if(deptIdStr.equals("1")){
       seqId = 1;
     }else if(deptIdStr.equals("2")){
       seqId = 2;
     }else if(deptIdStr.equals("3")){
       seqId = 3;
     }else if(deptIdStr.equals("4")){
       seqId = 4;
     }else if(deptIdStr.equals("5")){
       seqId = 5;
     }
      this.getDeptTree(seqId, sb, level + 1 , conn);
    }
   
  }
  
  
  /**
   * 新建
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public static void addDept(Connection dbConn,YHExtDept dept) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, dept);
  }
  
  
  /**
   * 设置外发单位权限 -- yyb
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getGroupDept(Connection dbConn, String flag) throws Exception {
    
    List<String> deptNameList = new ArrayList<String>();
    List<String> deptIdList = new ArrayList<String>();
    List<String> deptTelFlagList = new ArrayList<String>();
    YHORM orm = new YHORM();
    String filters[] = {"DEPT_GROUP="+flag+" order by DEPT_NO asc"};
    List<YHExtDept> extDeptList = orm.loadListSingle(dbConn, YHExtDept.class, filters);
    for(YHExtDept extDept: extDeptList){
      deptNameList.add(extDept.getDeptName());
      deptIdList.add(extDept.getDeptId()+"");
      if("1".equals(extDept.getDeptTelLine())){
        deptTelFlagList.add(extDept.getDeptId()+"");
      }
      else{
        deptTelFlagList.add(extDept.getDeptId()+"");
      }
    }
    
    StringBuffer sb = new StringBuffer();
    sb.append("{\"deptName\":\"" + (deptNameList.toString().substring(1,deptNameList.toString().length() - 1)).replaceAll(", ",",") + "\"");
    sb.append(",\"deptId\":\"" + (deptIdList.toString().substring(1,deptIdList.toString().length() - 1)).replaceAll(", ",",") + "\"");
    sb.append(",\"telFlag\":\"" + (deptTelFlagList.toString().substring(1,deptTelFlagList.toString().length() - 1)).replaceAll(", ",",") + "\"}");
    
    return sb.toString();
  }
}
