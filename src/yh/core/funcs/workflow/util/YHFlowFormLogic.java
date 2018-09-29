package yh.core.funcs.workflow.util;

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

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowFormItem;
import yh.core.funcs.workflow.data.YHFlowFormReglex;
import yh.core.funcs.workflow.data.YHFlowFormType;
import yh.core.funcs.workflow.praser.YHFormPraser;
import yh.core.funcs.workflow.util.YHFlowFormUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHFlowFormLogic {
  private static Logger log = Logger.getLogger(YHFlowFormLogic.class);
  public Map selectFlowForm(Connection dbConn, int seqId) throws Exception {
    Map map = new HashMap();
    //String[] nameStr = str;
    YHFlowFormType form = null;
    Statement stmt = null;
    ResultSet rs = null;
    String print = "";
    String sql = "SELECT PRINT_MODEL , ITEM_MAX , FORM_ID "
                + " FROM oa_fl_form_type WHERE SEQ_ID = " 
                + seqId;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        print = rs.getString("PRINT_MODEL");
        map.put("PRINT_MODEL", print);
        int i  = rs.getInt("ITEM_MAX");
        map.put("ITEM_MAX", i);
        int b  = rs.getInt("FORM_ID");
        map.put("FORM_ID", b);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return map;
  }
  public int getFlowFormMax(Connection dbConn, int seqId) throws Exception {
    Map map = new HashMap();
    Statement stmt = null;
    ResultSet rs = null;
    int max = 0 ;
    String sql = "SELECT ITEM_MAX FROM oa_fl_form_type WHERE SEQ_ID = " 
                + seqId;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        max = rs.getInt(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return max;
  }
  
  public Map selectFlowForm(Connection dbConn, int seqId, String[] str) throws Exception {
    Map map = new HashMap();
    //String[] nameStr = str;
    YHFlowFormType form = null;
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
                + " FROM oa_fl_form_type WHERE SEQ_ID = " 
                + seqId;
    //System.out.println(sql);
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        //form = new YHFlowFormType();
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
    String sql = "UPDATE oa_fl_form_type SET " 
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
  public List<YHFlowFormItem> formToMap(Connection dbConn, int seqId) throws Exception{
    List<YHFlowFormItem> list = new ArrayList();
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "select TITLE , ITEM_ID ,TAG, CLAZZ, TYPE , NAME , CONTENT , VALUE , RADIO_CHECK , RADIO_FIELD from oa_fl_form_item where FORM_ID =" + seqId + " order by ITEM_ID";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        YHFlowFormItem item = new YHFlowFormItem();
        String title = rs.getString("TITLE");
        item.setTitle(title);
        String clazz = rs.getString("CLAZZ");
        item.setClazz(clazz);
        int itemId = rs.getInt("ITEM_ID");
        item.setItemId(itemId);
        String name = rs.getString("NAME");
        item.setName(name);
        String tag = rs.getString("TAG");
        item.setTag(tag);
        String type = rs.getString("TYPE");
        item.setType(type);
        String content = rs.getString("CONTENT");
        item.setContent(content);
        String value = rs.getString("VALUE");
        item.setValue(value);
        String radioCheck = rs.getString("RADIO_CHECK");
        item.setRadioCheck(radioCheck);
        String radioField = rs.getString("RADIO_FIELD");
        item.setRadioField(radioField);
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
    List<YHFlowFormItem> hs = formToMap(dbConn, seqId);
    String result = "";
    for (YHFlowFormItem item : hs) {
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
  public String getTitle2(Connection dbConn, int seqId) throws Exception{
    List<YHFlowFormItem> hs = formToMap(dbConn, seqId);
    StringBuffer sb =new StringBuffer();
    int count = 0 ;
    for (YHFlowFormItem item : hs) {
      String clazz = item.getClazz();
      if ("DATE".equals(clazz) || "USER".equals(clazz)) {
        continue;
      }
      String value = item.getValue();
      if ("{宏控件}".equals(value)) {
        value = "";
      }
      String type = "text";
      String content = item.getContent();
      if (content == null){
        content = "";
      }
      if ("SELECT".equals(item.getTag()) && !"AUTO".equals(clazz) ) {
        type = "select";
        int index = content.indexOf(">");
        content = content.substring(index + 1);
        int endIndex = content.lastIndexOf("<");
        content = content.substring(0 , endIndex);
        content = content.replace("\\", "");
      }
      if ("checkbox".equals(item.getType()) || "CHECKBOX".equals(item.getType())) {
        type = "checkbox";
        if (content != null && (content.indexOf("CHECKED") >= 0 || content.indexOf("checked")>= 0)) {
          value = "on";
        } else {
          value = "";
        }
      }
      if ("RADIO".equals(clazz)) {
        type = "radio";
        String radioField = YHUtility.null2Empty(item.getRadioField());
        String radioCheck = YHUtility.null2Empty(item.getRadioCheck());
        String[] radioArray = radioField.split("`");
        content = "";
        for (String s : radioArray) {
          String checked = "";
          if (s.equals(radioCheck)) {
            checked = "selected";
          }
          content += "<option value=\"" + s + "\" "+checked+">" + s + "</option>";
        }
      }
      sb.append("{");
      sb.append("title:\"" + YHWorkFlowUtility.encodeSpecial(item.getTitle()) + "\"");
      sb.append(",value:\"" + YHWorkFlowUtility.encodeSpecial(value) + "\"");
      sb.append(",content:\"" +  YHWorkFlowUtility.encodeSpecial(content) + "\"");
      sb.append(",type:\"" + type + "\"");
      sb.append(",radioCheck:\"" + item.getRadioCheck() + "\"");
      sb.append(",radioField:\"" + item.getRadioField() + "\"");
      sb.append("},");
      count++;
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  public String getFormJson(Connection dbConn, int seqId) throws Exception{
    List<YHFlowFormItem> hs = formToMap(dbConn, seqId);
    
    StringBuffer sb = new StringBuffer("[");
    if (hs != null) {
      int count = 0;
      for (YHFlowFormItem item : hs) {
        String val = item.getTitle();
        String clazz = item.getClazz();
        if ("DATE".equals(clazz) || "USER".equals(clazz) || "SIGN".equals(clazz)) {
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
      + " from oa_fl_form_type where FORM_ID = '0'";
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
    String query = "select title , metadata_name from oa_fl_form_item where form_Id =" + formId + " and metadata_name is not null";
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
  public List<YHFlowFormType> getFlowFormType(Connection dbConn,String seqIds) throws Exception{
    List<YHFlowFormType> list = null;
    YHORM orm = new YHORM();
    if("".equals(seqIds)){
      return new ArrayList<YHFlowFormType>();
    }
    String[] filters = new String[]{" SEQ_ID IN (" + seqIds + ")"};
    list =  orm.loadListSingle(dbConn, YHFlowFormType.class, filters);
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
      sql =  "SELECT SEQ_ID FROM oa_fl_form_type WHERE SEQ_ID NOT IN ( SELECT FORM_SEQ_ID FROM oa_fl_type ) AND FORM_ID = 0 ";
    }else{
      sql = "SELECT SEQ_ID FROM oa_fl_form_type WHERE SEQ_ID IN ( SELECT FORM_SEQ_ID FROM oa_fl_type WHERE FLOW_SORT = " + sortId + ")  AND FORM_ID = 0 ";
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
  /**
   * 表单管理得到表单Id
   * @param dbConn
   * @param seqIds
   * @return
   * @throws Exception
   */
  public String getIdByForm(Connection dbConn,int seqId) throws Exception{
    String sql = "";
    sql =  "SELECT SEQ_ID FROM oa_fl_form_type WHERE FORM_ID = " +  seqId;
    String result = seqId + ",";
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try{
      pstmt = dbConn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        int fsi = rs.getInt(1);
        result +=  fsi + ",";
      }
    }catch(Exception e){
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
    ArrayList<YHFlowFormType> list = (ArrayList<YHFlowFormType>) getFlowFormType(dbConn, seqIds);
    YHWorkFlowUtility w = new YHWorkFlowUtility();
    for (YHFlowFormType fft : list){
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
  public StringBuffer flowFormType2JsonByType(Connection dbConn ,int seqId , YHPerson u) throws Exception{
    StringBuffer sb = new StringBuffer("[");
    StringBuffer field = new StringBuffer();

    String seqIds = getIdByForm(dbConn, seqId);
    seqIds = YHWorkFlowUtility.getOutOfTail(seqIds);
    ArrayList<YHFlowFormType> list = (ArrayList<YHFlowFormType>) getFlowFormType(dbConn, seqIds);
    YHWorkFlowUtility w = new YHWorkFlowUtility();
    for (YHFlowFormType fft : list){
      int deptId = fft.getDeptId();
      if (!w.isHaveRight(deptId, u, dbConn)) {
        continue;
      }
      if(!"".equals(field.toString())){
        field.append(",");
      }
      YHFormVersionLogic l = new YHFormVersionLogic();
      field.append("{\"seqId\":'").append(fft.getSeqId()).append("'")
           .append(",").append("\"versionTime\":'").append(YHUtility.getDateTimeStr(fft.getVersionTime()))
           .append("',")
           .append("\"versionNo\":'").append(fft.getVersionNo()).append("'")
           //.append(",noReplay:" + l.isExistRunFlowRun(fft.getSeqId(), dbConn))
           .append(",formId:" + fft.getFormId())
           .append("}");
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
    String query  = "SELECT 1 from oa_fl_type where FORM_SEQ_ID="+ formId;
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
  public int getSortId(Connection conn,int formId) throws Exception {
    String query  = "SELECT FLOW_SORT from oa_fl_type where FORM_SEQ_ID="+ formId;
    Statement pstmt = null;
    ResultSet rs = null;
    try{
      pstmt = conn.createStatement();
      rs = pstmt.executeQuery(query);
      if (rs.next()) {
        return rs.getInt("FLOW_SORT");
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(pstmt, rs, log);
    }
    return 0;
  }
  public StringBuffer search(Connection dbConn ,String searchKey , YHPerson u) throws Exception{
    String userPrivOther = u.getUserPrivOther();
    StringBuffer sb = new StringBuffer("[");
    StringBuffer field = new StringBuffer();
    YHORM orm = new YHORM();
    YHWorkFlowUtility w = new YHWorkFlowUtility();
    String query = " FORM_NAME LIKE '%" +  YHUtility.encodeLike(searchKey)  + "%' "  + YHDBUtility.escapeLike();
     String[] filters = new String[]{query , " FORM_ID = '0'"};
    ArrayList<YHFlowFormType> list =  (ArrayList<YHFlowFormType>) orm.loadListSingle(dbConn, YHFlowFormType.class, filters);
    for (YHFlowFormType fft : list){
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
           .append(",").append("\"formName\":'").append(name).append("'").append(",sortId:" + this.getSortId(dbConn, fft.getSeqId())).append("}");
    }
    sb.append(field).append("]");
    return sb;
  }
  public void deleteFormItems(String seqId , Connection conn) throws Exception {
    if (YHUtility.isNullorEmpty(seqId)) {
      return ;
    }
    if (seqId.endsWith(",")) {
      seqId = YHWorkFlowUtility.getOutOfTail(seqId);
    }
    String query = "delete from oa_fl_form_item where FORM_ID in (" + seqId + ")";
    Statement stm = null;
    try{
      stm = conn.createStatement();
      stm.executeUpdate(query);
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stm, null , log);
    }
  }
  public void deleteFormType(String seqId , Connection conn) throws Exception {
    if (YHUtility.isNullorEmpty(seqId)) {
      return ;
    }
    if (seqId.endsWith(",")) {
      seqId = YHWorkFlowUtility.getOutOfTail(seqId);
    }
    String query = "delete from oa_fl_form_type where SEQ_ID in (" + seqId + ")";
    Statement stm = null;
    try{
      stm = conn.createStatement();
      stm.executeUpdate(query);
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stm, null , log);
    }
  }
  /**
   * 删除表单
   * @param seqId
   * @param conn
   * @throws Exception
   */
  public void deleteForm (int seqId , Connection conn) throws Exception {
    String query = "select FORM_ID from oa_fl_form_type where SEQ_ID = " + seqId;
    Statement stm3 = null;
    ResultSet rs3 = null;
    int result = 0;
    try{
      stm3 = conn.createStatement();
      rs3 = stm3.executeQuery(query);
      if (rs3.next()) {
        result = rs3.getInt("FORM_ID");
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stm3, rs3 , log);
    }
    if (result == 0) {
      String query2 = "select SEQ_ID from oa_fl_form_type where FORM_ID = " + seqId;
      Statement stm4 = null;
      ResultSet rs4 = null;
      String ids = "";
      try{
        stm4 = conn.createStatement();
        rs4 = stm4.executeQuery(query2);
        while (rs4.next()) {
          ids  += rs4.getInt("SEQ_ID") + ",";
        }
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(stm4, rs4 , log);
      }
      ids += seqId + ",";
      this.deleteFormItems(ids, conn);
      this.deleteFormType(ids, conn);
    } else {
      this.deleteForm(result, conn);
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
  public void updateForm(Connection dbConn, int seqId, String printModel,
      String itemMax , boolean isUpdate) throws Exception {
    // TODO Auto-generated method stub
    String printModelNew = "";
    YHFlowFormLogic ffl = new YHFlowFormLogic();
    HashMap hm = (HashMap) YHFormPraser.praserHTML2Dom(printModel);
    Map<String, Map> m1 = YHFormPraser.praserHTML2Arr(hm);
    if(m1.get("isSame")!=null){
    	if("1".equals(m1.get("isSame").get("isSame"))){
    		throw new Exception("有元素的id或者name重复");
    	}
    }
    printModelNew = YHFormPraser.toShortString(m1, printModel, YHFlowFormReglex.CONTENT);
    ffl.updateFlowForm(dbConn, seqId, new String[]{"PRINT_MODEL","PRINT_MODEL_SHORT","ITEM_MAX"},  new String[]{printModel,printModelNew , itemMax});
    YHWorkflowSave2DataTableLogic logic = new YHWorkflowSave2DataTableLogic();
    if (isUpdate) {
      //更新表结构
      logic.updateFlowFormTable(dbConn, seqId);
    } else {
      //创建表结构
      logic.createFlowFormTable(dbConn, seqId);
    }
  }
}
