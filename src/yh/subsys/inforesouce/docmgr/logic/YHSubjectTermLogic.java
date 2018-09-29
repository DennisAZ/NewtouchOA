package yh.subsys.inforesouce.docmgr.logic;

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
import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.inforesouce.docmgr.data.YHSubjectTerm;

public class YHSubjectTermLogic {
  String typeFlagStr="";
  private static Logger log = Logger.getLogger(YHSubjectTermLogic.class);
  private int userIdStrs = 0;
  
  public String getWordTreeJson(int wordId , Connection conn) throws Exception{
  	StringBuffer sb = new StringBuffer();
  	sb.append("[");
  	this.getWordTree(wordId, sb, 0 , conn);
  	if(sb.charAt(sb.length() - 1) == ','){
  	  sb.deleteCharAt(sb.length() - 1);
  	}
  	sb.append("]");
  	return sb.toString();
  }	
  public void getWordTree(int wordId , StringBuffer sb , int level , Connection conn) throws Exception{
  	List<Map> list = new ArrayList();
  	String query = "select SEQ_ID , WORD from oa_topic_term where TYPE_FLAG=0 and PARENT_ID=" + wordId + " order by SORT_NO,WORD desc";
  	Statement stmt = null;
  	ResultSet rs = null;
  	try {
      stmt = conn.createStatement();
	    rs = stmt.executeQuery(query);
	    while (rs.next()) {
        String word = rs.getString("WORD");
        int seqId = rs.getInt("SEQ_ID");
        Map map = new HashMap();
        map.put("word", word);
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
  	  String word = (String)dp.get("word");
  	  sb.append("{");
  	  sb.append("text:'" + flag + YHUtility.encodeSpecial(word) + "',");
  	  sb.append("value:" + seqId);
  	  sb.append("},");
  	  this.getWordTree(seqId, sb, level + 1 , conn);
    }  
  }
		  
  public String getWordTreeJson(int wordId , Connection conn,String[] postDeptArray) throws Exception{
  	StringBuffer sb = new StringBuffer();
  	Set childWordId = new HashSet();
  	for (int i = 0; i < postDeptArray.length; i++) {
  	  getChildWord(conn,Integer.parseInt(postDeptArray[i]),childWordId); 
  	 }
  	this.getWordTreeByPostDept(wordId, sb, 0 , conn,childWordId);
  	if(sb.length()>0&&sb.charAt(sb.length() - 1) == ','){
  	  sb.deleteCharAt(sb.length() - 1);
  	}
  	return sb.toString();
  }	
  
  public Set getChildWord(Connection conn,int postDept,Set childWordId) throws Exception{
  	List<Map> list = new ArrayList();
  	String query = "select SEQ_ID , WORD from oa_topic_term where TYPE_FLAG=0 and PARENT_ID=" + postDept +" order by SORT_NO,WORD asc";
  	Statement stmt = null;
  	ResultSet rs = null;
  	try {
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(query);
	    while (rs.next()) {
        String word = rs.getString("WORD");
        int seqId = rs.getInt("SEQ_ID");
        Map map = new HashMap();
        map.put("word", word);
        map.put("seqId", seqId);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    childWordId.add(postDept);
    for(int i = 0; i < list.size(); i++){
      Map dp = list.get(i);
      int seqId = (Integer)dp.get("seqId");
      childWordId.add(seqId);
      getChildWord(conn,seqId,childWordId);
    }
    return childWordId;
  }
	  
  public void getWordTreeByPostDept(int wordId , StringBuffer sb , int level , Connection conn,Set childDeptId) throws Exception{
    List<Map> list = new ArrayList();
    String query = "select SEQ_ID , WORD from oa_topic_term where TYPE_FLAG=0 and PARENT_ID=" + wordId +" order by SORT_NO, WORD asc";
    Statement stmt = null;
    ResultSet rs = null;
    try {
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(query);
	    while (rs.next()) {
	      String word = rs.getString("WORD");
	      int seqId = rs.getInt("SEQ_ID");
	      Map map = new HashMap();
	      map.put("word", word);
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
        String word = (String)dp.get("word");
        sb.append("{");
        sb.append("text:'" + flag + YHUtility.encodeSpecial(word) + "',");
        sb.append("value:" + seqId );
        sb.append("},");
      }
      this.getWordTreeByPostDept(seqId, sb, level + 1 , conn,childDeptId);
    }
  }  
  
  public static String getChildWordId(Connection conn, int parentDeptId) throws Exception {
    String result = "";
    String subResult = "";
    String sql = "select " + "  SEQ_ID ,WORD" + " from " + " oa_topic_term "
	        + " where " + "TYPE_FLAG=0 and PARENT_ID = " + parentDeptId;
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
    subResult = getChildWordId(conn, result);
    if(!"".equals(subResult)){
      if(!"".equals(result) && !result.endsWith(",")){
        result += ",";
      }
      result += subResult;
    }
    return result;
  }
  
  public static String getChildWordId(Connection conn, String parentDeptIds)
      throws Exception {
    String result = "";
    if (parentDeptIds == null || "".equals(parentDeptIds)) {
      return "";
    } else {
      String[] parentDeptArray = parentDeptIds.split(",");
      for (String pid : parentDeptArray) {
        if ("".equals(pid.trim())) {
          continue;
        }
        if (!"".equals(result) && !result.endsWith(",")) {
          result += ",";
        }
        result += getChildWordId(conn, Integer.parseInt(pid));
      }
    }
    return result;
  }
  
  public String getWordTreeJsonSelf(int deptId, Connection conn,
      String[] postDeptArray, int userDeptIdFunc) throws Exception {
    StringBuffer sb = new StringBuffer();
    Set childDeptId = new HashSet();
    for (int i = 0; i < postDeptArray.length; i++) {
      getChildWord(conn, Integer.parseInt(postDeptArray[i]), childDeptId);
    }
    this.getWordTreeByPostWordSelf(deptId, sb, 0, conn, childDeptId,
        userDeptIdFunc);
    if (sb.length() > 1) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  
  public void getWordTreeByPostWordSelf(int deptId, StringBuffer sb, int level,
      Connection conn, Set childDeptId, int userDeptIdFunc) throws Exception {
    List<Map> list = new ArrayList();
    String query = "select SEQ_ID , WORD from oa_topic_term where TYPE_FLAG=0 and PARENT_ID="
        + deptId + " order by SORT_NO,WORD asc";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String word = rs.getString("WORD");
        int seqId = rs.getInt("SEQ_ID");
        Map map = new HashMap();
        map.put("word", word);
        map.put("seqId", seqId);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    for (int i = 0; i < list.size(); i++) {
      String flag = " ├";
      if (i == list.size() - 1) {
        flag = " └";
      }
      String tmp = "";
      for (int j = 0; j < level; j++) {
        tmp += " │";
      }
      flag = tmp + flag;
      Map dp = list.get(i);
      int seqId = (Integer) dp.get("seqId");
      if (seqId != userDeptIdFunc) {
        if (childDeptId.contains(seqId)) {
          String word = (String) dp.get("word");
          sb.append("{");
          sb.append("text:'" + flag + YHUtility.encodeSpecial(word) + "',");
          sb.append("value:" + seqId);
          sb.append("},");
        }
        this.getWordTreeByPostWordSelf(seqId, sb, level + 1, conn, childDeptId,
            userDeptIdFunc);
      }
    }
  }
  
  public String getWordTreeJson1(int deptId , Connection conn ,int userDeptIdStr) throws Exception{
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
  	List<Map> list = new ArrayList();
  	String query = "select SEQ_ID , WORD from oa_topic_term where TYPE_FLAG=0 and PARENT_ID=" + deptId +  " order by SORT_NO,WORD asc";
  	Statement stmt = null;
  	ResultSet rs = null;
  	try {
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(query);
	    while (rs.next()) {
	      String word = rs.getString("WORD");
	      int seqId = rs.getInt("SEQ_ID");
	      Map map = new HashMap();
	      map.put("word", word);
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
  	  String word = (String)dp.get("word");
  	  if(seqId != this.userIdStrs){
  	    sb.append("{");
  	    sb.append("text:'" + flag + YHUtility.encodeSpecial(word) + "',");
  	    sb.append("value:" + seqId );
  	    sb.append("},");
  	    this.getDeptTree1(seqId, sb, level + 1 , conn);
  	  }
  	}
  }
	  
  public void updateWordInfoLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception{
	  YHORM orm = new YHORM();
	  String seqIdStr = fileForm.getParameter("treeId");
	  String word=fileForm.getParameter("word");
	  int parentId=Integer.parseInt(fileForm.getParameter("parentId"));
	  int sortNo=Integer.parseInt(fileForm.getParameter("sortNo"));
	  int typeFlag=Integer.parseInt(fileForm.getParameter("typeFlag"));
	  int seqId = 0;
	  if (!YHUtility.isNullorEmpty(seqIdStr)) {
	    seqId = Integer.parseInt(seqIdStr);
	  }
	  try{
      YHSubjectTerm st = (YHSubjectTerm) orm.loadObjSingle(dbConn, YHSubjectTerm.class, seqId);
      st.setParentId(parentId);
      st.setSortNo(sortNo);
      st.setTypeFlag(typeFlag);
      st.setWord(word);
      orm.updateSingle(dbConn,st);
    }catch (Exception e) {
      throw e;
    }
  }
  
  public List deleteWordMul(Connection dbConn, int seqId) throws Exception {
    List list = new ArrayList();
	  YHSubjectTerm de = null;
    Statement stmt = null;
  	ResultSet rs = null;
  	String sql = "SELECT SEQ_ID FROM oa_topic_term WHERE PARENT_ID = '" + seqId + "'";
  	try {
	    stmt = dbConn.createStatement();
	    rs = stmt.executeQuery(sql);
	    while (rs.next()) {
	      de = new YHSubjectTerm();
	      de.setSeqId(rs.getInt("SEQ_ID"));
	      list.add(de);
	    }
	    List tmpList = new ArrayList();
	    tmpList.addAll(list);
	    for(Iterator it = tmpList.iterator(); it.hasNext();){
	      YHSubjectTerm der = (YHSubjectTerm)(it.next());
	      List srclist = deleteWordMul(dbConn,der.getSeqId());
	      list.addAll(srclist);
	    }
  	} catch (Exception ex) {
	    throw ex;
  	} finally {
	    YHDBUtility.close(stmt, rs, log);
  	}
  	return list;
  }
  
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
  
  public void deleteWord(Connection conn, int seqId) throws Exception {
  	String sql = "DELETE FROM oa_topic_term WHERE SEQ_ID =" + seqId + "";
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
  
  public YHSubjectTerm getMaxSeqId(Connection dbConn) {
    String sql = "select SEQ_ID,WORD,TYPE_FLAG from oa_topic_term where SEQ_ID=(select MAX(SEQ_ID) from oa_topic_term ) ";
    YHSubjectTerm st = null;
  	int seqId = 0;
  	String word = "";
  	int typeFlag=0;
  	PreparedStatement ps = null;
  	ResultSet rs = null;
  	try {
  		ps = dbConn.prepareStatement(sql);
  		rs = ps.executeQuery();
  		while (rs.next()) {
  		  st = new YHSubjectTerm();
  		  seqId = rs.getInt("SEQ_ID");
  		  word = rs.getString("WORD");
  		  typeFlag = rs.getInt("TYPE_FLAG");
  		  st.setSeqId(seqId);
  		  st.setWord(word);
  		  st.setTypeFlag(typeFlag);
  		}
  	} catch (Exception e) {
  	  e.printStackTrace();
  	} finally {
  	  YHDBUtility.close(ps, rs, log);
  	}
	  return st;
  }
  
  public ArrayList<YHDbRecord> toExportWordData(Connection conn) throws Exception{
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
    String sql = "SELECT "
          + " SEQ_ID"
          + ",WORD "
          + ",PARENT_ID "
          + ",SORT_NO "
          + ",TYPE_FLAG "
          + " from oa_topic_term";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery() ;
      while (rs.next()) {
        YHDbRecord record = new YHDbRecord();
        String word = rs.getString(2);
        int parentId = rs.getInt(3);
        int sortNo = rs.getInt(4);
        int typeFlag = rs.getInt(5);
        if(typeFlag==1){
          typeFlagStr="主题词";
        } 
        if(typeFlag==0){
          typeFlagStr="类别";
        }       
        record.addField("主题词", word);
        record.addField("类别",  getWordParentName(conn, parentId).toString());
        record.addField("排序号", sortNo);
        record.addField("类型", typeFlagStr);
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
   *主题词是否存在*
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public boolean existsWordName(Connection dbConn, String wordName)throws Exception {
    long count = 0;
    String sql = "SELECT count(*) FROM oa_topic_term WHERE WORD = '" + wordName + "'";
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
  
  public int getWordIdLogic(Connection conn , String wordName) throws Exception{
    int result = 0;
    String sql = " select SEQ_ID from oa_topic_term where WORD = '" + wordName + "'";
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
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  
  /**
   * 取得存入导入的数据

   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public void saveWord(Connection conn,String word, int parentid,String sortNo,int typeFlag )throws Exception{ 
    String sql="insert into oa_topic_term(WORD,PARENT_ID,SORT_NO,TYPE_FLAG)value(?,?,?,?)";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      ps.setString(1, word);
      ps.setInt(2, parentid);
      ps.setString(3,sortNo);
      ps.setInt(4,typeFlag);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  
  /**
   * 取得上级主题词名称

   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  
  public String getWordParentName(Connection conn , int wordParent) throws Exception{
    String result = "";
    String sql = " select WORD from oa_topic_term where SEQ_ID = " + wordParent ;
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
  
  public String getAjaxCheckLogic(Connection dbConn,String parentId,String typeFlag,String word)throws Exception{
    String result=null;
    String sql="SELECT COUNT(*) FROM oa_topic_term WHERE PARENT_ID='"+parentId+"' and type_Flag='"+typeFlag+"' and word='"+word+"'";
    PreparedStatement ps=null;
    ResultSet rs=null;
    try{
      ps=dbConn.prepareStatement(sql);
      rs=ps.executeQuery();
      while(rs.next()){
        result=rs.getString(1);
      }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
}
