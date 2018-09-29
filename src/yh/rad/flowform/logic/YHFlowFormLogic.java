package yh.rad.flowform.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import yh.core.util.db.YHORM;
import yh.core.funcs.workflow.util.YHFlowFormUtility;
import yh.core.util.db.YHDBUtility;
import yh.rad.flowform.data.YHFlowFormType;
import yh.rad.flowform.praser.YHFormPraser;

public class YHFlowFormLogic {
  private static Logger log = Logger.getLogger(YHFlowFormLogic.class);
  
  public Map selectFlowForm(Connection dbConn, int seqId, String str) throws Exception {
    Map map = new HashMap();
    //String[] nameStr = str;
    YHFlowFormType form = null;
    Statement stmt = null;
    ResultSet rs = null;
    String print = "";
    String sql = "SELECT "
                + str 
                + " FROM oa_fl_form_type WHERE SEQ_ID = " 
                + seqId;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        //form = new YHFlowFormType();
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
  public HashMap<String, Map> formToMap(Connection dbConn, int seqId) throws Exception{
    Map m = selectFlowForm(dbConn, seqId, "PRINT_MODEL");
    String html = (String) m.get("PRINT_MODEL");
    HashMap<String, Map> hs = null;
    hs = YHFormPraser.toMap(html);
    return hs;
  }
  
  public String getTitle(Connection dbConn, int seqId) throws Exception{
    HashMap<String, Map> hs = formToMap(dbConn, seqId);
    String resualt = "";
    Set<String> keys = hs.keySet();
    for (String key : keys){
      Map m = hs.get(key);
      String val =  (String) m.get("TITLE");
//      if(key != null && (key.indexOf("OTHER") != -1)){
//        continue;
//      }
      String clazz = (String)m.get("CLASS");
      if ("DATE".equals(clazz) || "USER".equals(clazz)) {
        continue;
      }
      if(!"".equals(resualt)){
        resualt += ",";
      }
      resualt += val;
    }
    return resualt;
  }
  public String getFormJson(Connection dbConn, int seqId) throws Exception{
    HashMap<String, Map> hs = formToMap(dbConn, seqId);
    StringBuffer sb = new StringBuffer("[");
    if (hs != null) {
      Set<String> keys = hs.keySet();
      int count = 0;
      for (String key : keys){
        Map m = hs.get(key);
        String val =  (String) m.get("TITLE");
//        if(key != null && (key.indexOf("OTHER") != -1)){
//          continue;
//        }
        String clazz = (String)m.get("CLASS");
        if ("DATE".equals(clazz) || "USER".equals(clazz)) {
          continue;
        }
        count ++ ;
        sb.append("{");
        String id = key.substring("DATA_".length());
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
  public List<YHFlowFormType> getFlowFormType(Connection dbConn ) throws Exception{
    List<YHFlowFormType> list = new ArrayList();
    YHORM orm = new YHORM();
    Map m = null;
    list =  orm.loadListSingle(dbConn, YHFlowFormType.class, m);
    return list;
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
      sql =  "SELECT SEQ_ID FROM oa_fl_form_type WHERE SEQ_ID NOT IN( SELECT FORM_SEQ_ID FROM oa_fl_type )";
    }else{
      sql = "SELECT SEQ_ID FROM oa_fl_form_type WHERE SEQ_ID IN( SELECT FORM_SEQ_ID FROM oa_fl_type WHERE FLOW_SORT = " + sortId + ")";
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
  public StringBuffer flowFormType2Json(Connection dbConn ,int sortId) throws Exception{
    StringBuffer sb = new StringBuffer("[");
    StringBuffer field = new StringBuffer();

    String seqIds = getIdBySort(dbConn, sortId);
    ArrayList<YHFlowFormType> list = (ArrayList<YHFlowFormType>) getFlowFormType(dbConn, seqIds);
    
    for (YHFlowFormType fft : list){
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
   * 是否存在工作
   * @param formId
   * @param conn
   * @return
   * @throws Exception
   */
  public boolean isExistFlowRun(int formId , Connection conn) throws Exception{
    String query  = "SELECT 1 from oa_fl_type as FLOW_TYPE,oa_fl_run as FLOW_RUN where FLOW_TYPE.FORM_SEQ_ID="+ formId +" and FLOW_TYPE.SEQ_ID=FLOW_RUN.FLOW_ID";
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
  public StringBuffer search(Connection dbConn ,String searchKey) throws Exception{
    StringBuffer sb = new StringBuffer("[");
    StringBuffer field = new StringBuffer();
    YHORM orm = new YHORM();
    String[] filters = new String[]{" FORM_NAME LIKE '%" + searchKey + "%'"};
    ArrayList<YHFlowFormType> list =  (ArrayList<YHFlowFormType>) orm.loadListSingle(dbConn, YHFlowFormType.class, filters);
    for (YHFlowFormType fft : list){
      if(!"".equals(field.toString())){
        field.append(",");
      }
      field.append("{\"seqId\":'").append(fft.getSeqId()).append("'")
           .append(",").append("\"formName\":'").append(fft.getFormName()).append("'").append(",noDelete:" + this.isExistFlowRun(fft.getSeqId(), dbConn)).append("}");
    }
    sb.append(field).append("]");
    return sb;
  }
}
