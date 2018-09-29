package yh.core.funcs.doc.logic;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowFormItem;
import yh.core.funcs.doc.data.YHDocFlowFormType;
import yh.core.funcs.doc.util.YHFlowFormUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
public class YHFlowFormLogic {
  private static Logger log = Logger.getLogger(YHFlowFormLogic.class);
  
  public Map selectFlowForm(Connection dbConn, int seqId, String str) throws Exception {
    Map map = new HashMap();
    //String[] nameStr = str;
    YHDocFlowFormType form = null;
    Statement stmt = null;
    ResultSet rs = null;
    String print = "";
    String sql = "SELECT "
                + str 
                + " FROM "+ YHWorkFlowConst.FLOW_FORM_TYPE +" WHERE SEQ_ID = " 
                + seqId;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        //form = new YHDocFlowFormType();
        //form.setSeqId(rs.getInt("SEQ_ID"));
        //form.setFormName(rs.getString("PRINT_MODEL"));
        print = rs.getString(str.trim());
        map.put(str.trim(), print);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return map;
  }
  
  public Map selectFlowForm(Connection dbConn, int seqId, String[] str) throws Exception {
    Map map = new HashMap();
    //String[] nameStr = str;
    YHDocFlowFormType form = null;
    Statement stmt = null;
    ResultSet rs = null;
    String print = "";
    String fields = "";
    for (String string : str){
      if("".equals(fields)){
        fields +=  string;
      }else {
        fields += "," + string;

      }
    }
    String sql = "SELECT "
                + fields 
                + " FROM "+ YHWorkFlowConst.FLOW_FORM_TYPE +" WHERE SEQ_ID = " 
                + seqId;
    //System.out.println(sql);
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        //form = new YHDocFlowFormType();
        //form.setSeqId(rs.getInt("SEQ_ID"));
        //form.setFormName(rs.getString("PRINT_MODEL"));
        for (String string : str){
          print = rs.getString(string);
          map.put(string, print);
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    //System.out.println(map);
    return map;
  }
  
  public void updateFlowForm(Connection dbConn, int seqId, String[] names, String[] values) throws Exception{
    String buffer = "";
    for (String  nameStr : names){
      if(!"".equals(buffer)){
        buffer += ",";
      }
      buffer +=  nameStr  + "= ? ";
    }
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "UPDATE "+ YHWorkFlowConst.FLOW_FORM_TYPE +" SET " 
                + buffer
                + " WHERE SEQ_ID = ?";
    //System.out.println(sql+":sql llllllllllllllll");
    try{
      stmt = dbConn.prepareStatement(sql);
      for (int i = 0; i < values.length ; i++){
        stmt.setString(i + 1, values[i]);
      }
      stmt.setInt(values.length + 1, seqId);
      stmt.executeUpdate();
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(stmt, rs, log);
    }
    YHFlowFormUtility ffu = new YHFlowFormUtility();
    ffu.cacheForm(seqId, dbConn);
  }
  public List<YHDocFlowFormItem> formToMap(Connection dbConn, int seqId) throws Exception{
    List<YHDocFlowFormItem> list = new ArrayList();
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "select TITLE , ITEM_ID , CLAZZ , NAME from "+ YHWorkFlowConst.FLOW_FORM_ITEM +" where FORM_ID =" + seqId + " order by ITEM_ID";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        YHDocFlowFormItem item = new YHDocFlowFormItem();
        String title = rs.getString("TITLE");
        item.setTitle(title);
        String clazz = rs.getString("CLAZZ");
        item.setClazz(clazz);
        int itemId = rs.getInt("ITEM_ID");
        item.setItemId(itemId);
        String name = rs.getString("NAME");
        item.setName(name);
        list.add(item);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  public String getTitle(Connection dbConn, int seqId) throws Exception{
    List<YHDocFlowFormItem> hs = formToMap(dbConn, seqId);
    String result = "";
    for (YHDocFlowFormItem item : hs) {
      String val = item.getTitle();
      String clazz = item.getClazz();
      if ("DATE".equals(clazz) || "USER".equals(clazz)) {
        continue;
      }
      if(!"".equals(result)){
        result += ",";
      }
      result += val;
    }
    return result;
  }
  public String getFormJson(Connection dbConn, int seqId) throws Exception{
    List<YHDocFlowFormItem> hs = formToMap(dbConn, seqId);
    
    StringBuffer sb = new StringBuffer("[");
    if (hs != null) {
      int count = 0;
      for (YHDocFlowFormItem item : hs) {
        String val = item.getTitle();
        String clazz = item.getClazz();
        if ("DATE".equals(clazz) || "USER".equals(clazz)) {
          continue;
        }
        count ++ ;
        sb.append("{");
        int id = item.getItemId();
        sb.append("id:'" + id + "'");
        sb.append(",title:'" + val + "'");
        sb.append("},");
      }
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
    }
    sb.append("]");
    return sb.toString();
  }
  public  String getFlowFormTypeOption(Connection conn ,YHPerson u) throws Exception{
    String query = "select " 
      + " FORM_NAME"
      + " , SEQ_ID"
      + " , DEPT_ID "
      + " from "+ YHWorkFlowConst.FLOW_FORM_TYPE;
    StringBuffer sb = new StringBuffer("[");
    int count = 0 ;
    Statement stm = null;
    ResultSet rs = null;
    try{
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      YHWorkFlowUtility w = new YHWorkFlowUtility();
      while(rs.next()){
        int deptId = rs.getInt("DEPT_ID");
        if (!w.isHaveRight(deptId, u, conn)) {
          continue;
        }
        String formName = rs.getString("FORM_NAME");
        int seqId = rs.getInt("SEQ_ID");
        sb.append("{");
        sb.append("value:" + seqId + ",");
        sb.append("text:'" + formName + "'");
        sb.append("},");
        count++ ;
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stm, rs, log);
    }
    if (count > 0 ) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public String getMetaDataItem(int formId , Connection conn) throws Exception {
    StringBuffer sb = new StringBuffer();
    String query = "select title , metadata_name from "+ YHWorkFlowConst.FLOW_FORM_ITEM +" where form_Id =" + formId + " and metadata_name is not null";
    Statement stm = null;
    ResultSet rs = null;
    int count = 0 ;
    try{
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while(rs.next()){
        String title = rs.getString("title");
        String metadataName = rs.getString("metadata_name");
        
        sb.append("{");
        sb.append("value:'" + title + "',");
        sb.append("text:'" + title +"---"+ metadataName  + "'");
        sb.append("},");
        count++ ;
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stm, rs, log);
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  /**
   * 表单管理
   *  by cy
   * @param dbConn
   * @return
   * @throws Exception
   */
  public List<YHDocFlowFormType> getFlowFormType(Connection dbConn,String seqIds) throws Exception{
    List<YHDocFlowFormType> list = null;
    YHORM orm = new YHORM();
    if("".equals(seqIds)){
      return new ArrayList<YHDocFlowFormType>();
    }
    String[] filters = new String[]{" SEQ_ID IN (" + seqIds + ")"};
    list =  orm.loadListSingle(dbConn, YHDocFlowFormType.class, filters);
    return list;
  }
  /**
   * 表单管理得到表单Id
   *  by cy 
   * @param dbConn
   * @param seqIds
   * @return
   * @throws Exception
   */
  public String getIdBySort(Connection dbConn,int sortId) throws Exception{
    String sql = "";
    if(sortId == 0){
      sql =  "SELECT SEQ_ID FROM "+ YHWorkFlowConst.FLOW_FORM_TYPE +" WHERE SEQ_ID NOT IN( SELECT FORM_SEQ_ID FROM "+ YHWorkFlowConst.FLOW_TYPE +" )";
    }else{
      sql = "SELECT SEQ_ID FROM "+ YHWorkFlowConst.FLOW_FORM_TYPE +" WHERE SEQ_ID IN( SELECT FORM_SEQ_ID FROM "+ YHWorkFlowConst.FLOW_TYPE +" WHERE FLOW_SORT = " + sortId + ")";
    }
    String result = "";
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try{
      pstmt = dbConn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        if(!"".equals(result)){
          result += ",";
        }
        int fsi = rs.getInt(1);
        result += fsi;
      }
    }catch(Exception e){
      e.printStackTrace();
      throw e;
    }finally{
      YHDBUtility.close(pstmt, rs, log);
    }
    return result;
  }
  public StringBuffer flowFormType2Json(Connection dbConn ,int sortId , YHPerson u) throws Exception{
    StringBuffer sb = new StringBuffer("[");
    StringBuffer field = new StringBuffer();

    String seqIds = getIdBySort(dbConn, sortId);
    ArrayList<YHDocFlowFormType> list = (ArrayList<YHDocFlowFormType>) getFlowFormType(dbConn, seqIds);
    YHWorkFlowUtility w = new YHWorkFlowUtility();
    for (YHDocFlowFormType fft : list){
      int deptId = fft.getDeptId();
      if (!w.isHaveRight(deptId, u, dbConn)) {
        continue;
      }
      if(!"".equals(field.toString())){
        field.append(",");
      }
      field.append("{\"seqId\":'").append(fft.getSeqId()).append("'")
           .append(",").append("\"formName\":'").append(fft.getFormName()).append("'").append(",noDelete:" + this.isExistFlowRun(fft.getSeqId(), dbConn)).append("}");
    }
    sb.append(field).append("]");
    return sb;
  }
  
  /**
   * 是否存在流程
   * @param formId
   * @param conn
   * @return
   * @throws Exception
   */
  public boolean isExistFlowRun(int formId , Connection conn) throws Exception{
    String query  = "SELECT 1 from "+ YHWorkFlowConst.FLOW_TYPE +" where FORM_SEQ_ID="+ formId;
    Statement pstmt = null;
    ResultSet rs = null;
    try{
      pstmt = conn.createStatement();
      rs = pstmt.executeQuery(query);
      if (rs.next()) {
        return true;
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(pstmt, rs, log);
    }
    return false;
  }
  public StringBuffer search(Connection dbConn ,String searchKey , YHPerson u) throws Exception{
    String userPrivOther = u.getUserPrivOther();
    StringBuffer sb = new StringBuffer("[");
    StringBuffer field = new StringBuffer();
    YHORM orm = new YHORM();
    YHWorkFlowUtility w = new YHWorkFlowUtility();
    String query = " FORM_NAME LIKE '%" +  YHUtility.encodeLike(searchKey)  + "%' "  + YHDBUtility.escapeLike();
     String[] filters = new String[]{query};
    ArrayList<YHDocFlowFormType> list =  (ArrayList<YHDocFlowFormType>) orm.loadListSingle(dbConn, YHDocFlowFormType.class, filters);
    for (YHDocFlowFormType fft : list){
      int deptId = fft.getDeptId();
      if (!w.isHaveRight(deptId, u, dbConn)) {
        continue;
      }
      if(!"".equals(field.toString())){
        field.append(",");
      }
      String name = fft.getFormName();
      name = name.replaceAll("<", "&lt");
      name = name.replaceAll(">", "&gt");
      field.append("{\"seqId\":'").append(fft.getSeqId()).append("'")
           .append(",").append("\"formName\":'").append(name).append("'").append(",noDelete:" + this.isExistFlowRun(fft.getSeqId(), dbConn)).append("}");
    }
    sb.append(field).append("]");
    return sb;
  }
  /**
   * 删除表单
   * @param seqId
   * @param conn
   * @throws Exception
   */
  public void deleteForm (int seqId , Connection conn) throws Exception {
    String query1 = "delete from "+ YHWorkFlowConst.FLOW_FORM_TYPE +" where SEQ_ID = " + seqId;
    YHWorkFlowUtility.updateTableBySql(query1, conn);
    String query = "delete from "+ YHWorkFlowConst.FLOW_FORM_ITEM +" where FORM_ID = " + seqId;
    Statement stm = null;
    try{
      stm = conn.createStatement();
      stm.executeUpdate(query);
    }catch(Exception e){
      e.printStackTrace();
      throw e;
    }finally{
      YHDBUtility.close(stm, null , log);
    }
  }
  /**
   * 取出印章
   * @param seqId
   * @param conn
   * @throws Exception
   */
  public String getSeals (int userId , Connection conn) throws Exception {
    String query = "select SEQ_ID,SEAL_ID,SEAL_NAME,USER_STR from SEAL ORDER BY CREATE_TIME DESC ";
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    int count = 0 ;
    Statement stm = null;
    ResultSet rs = null;
    try{
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        String userStr = rs.getString("USER_STR");
        if (YHWorkFlowUtility.findId(userStr, String.valueOf(userId))) {
          count ++ ;
          int id = rs.getInt("SEQ_ID");
          String sealId = rs.getString("SEAL_ID");
          String sealName = rs.getString("SEAL_NAME");
          sb.append("{");
          sb.append("id:" + id);
          sb.append(",sealId:'" + sealId + "'");
          sb.append(",sealName:'" + sealName + "'");
          sb.append("},");
        }
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stm, rs , log);
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  /**
   * 取出印章数据
   * @param seqId
   * @param conn
   * @throws Exception
   */
  public byte[] getSealData (int id , Connection conn) throws Exception {
    String query = "select SEAL_DATA from SEAL WHERE SEQ_ID=" + id;
    String sealData = "";
    byte[] bt = null;
    Statement stm = null;
    ResultSet rs = null;
    try{
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        Clob str =  rs.getClob("SEAL_DATA");
        sealData = YHWorkFlowUtility.clob2String(str);
        if (sealData == null) {
          sealData =  "err";
          bt = new byte[1];
        } else {
          sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
          bt = decoder.decodeBuffer(sealData);
        }
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stm, rs , log);
    }
    return bt;
  }
 
}
