package yh.core.funcs.workflow.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHDocWord;
import yh.core.funcs.workflow.data.YHDocumentsType;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.recruit.filter.data.YHHrRecruitFilter;

public class YHDocumentsTypeLogic {

  public void addDocumentsTypeInfo(Connection dbConn, Map map, YHPerson person) throws Exception{
    
    YHDocumentsType documentsType = new YHDocumentsType();
    documentsType.setDocumentsName((String)map.get("documentsName"));
    documentsType.setFlowType(Integer.parseInt((String)map.get("flowType")));
    documentsType.setDocumentsFont((String)map.get("documentsFont"));
    documentsType.setDocumentsWordModel((String)map.get("documentsWordModel"));
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, documentsType);
  }
  
  /**
   * 文件类型 通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getDocumentsTypeList(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      String sql = " SELECT SEQ_ID, documents_name, flow_type, documents_font, documents_word_model FROM oa_doc_type ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 文件类型
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHDocumentsType getDocumentsTypeDetail(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHDocumentsType) orm.loadObjSingle(conn, YHDocumentsType.class, seqId);
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 文件类型信息
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void updateDocumentsTypeInfo(Connection dbConn, Map map, YHPerson person) throws Exception {
    YHDocumentsType documentsType = new YHDocumentsType();
    documentsType.setSeqId(Integer.parseInt((String)map.get("seqId")));
    documentsType.setDocumentsName((String)map.get("documentsName"));
    documentsType.setFlowType(Integer.parseInt((String)map.get("flowType")));
    documentsType.setDocumentsFont((String)map.get("documentsFont"));
    documentsType.setDocumentsWordModel((String)map.get("documentsWordModel"));
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, documentsType); 
  }
  
  /**
   * 删除文件类型
   * 
   * @param dbConn
   * @param seqIdStr
   * @throws Exception
   */
  public void deleteFileLogic(Connection dbConn, String seqIdStr) throws Exception {
    YHORM orm = new YHORM();
    if (YHUtility.isNullorEmpty(seqIdStr)) {
      seqIdStr = "";
    }
    try {
      String seqIdArry[] = seqIdStr.split(",");
      if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
        for (String seqId : seqIdArry) {
          YHDocumentsType documentsType = (YHDocumentsType) orm.loadObjSingle(dbConn, YHDocumentsType.class, Integer.parseInt(seqId));
          
          // 删除数据库信息
          orm.deleteSingle(dbConn, documentsType);
        }
      }
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 员工关怀查询
   * 
   * @param dbConn
   * @param request
   * @param map
   * @param person
   * @return
   * @throws Exception
   */
  public String queryDocumentsTypeList(Connection dbConn, Map request, Map map, YHPerson person) throws Exception {
    String documentsName = (String) map.get("documentsName");
    String flowType = (String) map.get("flowType");
    String documentsFont = (String) map.get("documentsFont");
    String documentsWordModel = (String) map.get("documentsWordModel");
    String conditionStr = "";
    String sql = "";
    try {
      if (!YHUtility.isNullorEmpty(documentsName)) {
        conditionStr = " and documents_name like '%" + YHDBUtility.escapeLike(documentsName) + "%'";
      }
      if (!YHUtility.isNullorEmpty(flowType)) {
        conditionStr = " and flow_type ='" + flowType + "'";
      }
      if (!YHUtility.isNullorEmpty(documentsFont)) {
        conditionStr += " and " + YHDBUtility.findInSet(documentsFont, "documents_font");
      }
      if (!YHUtility.isNullorEmpty(documentsWordModel)) {
        conditionStr = " and documents_word_model like '%" + YHDBUtility.escapeLike(documentsWordModel) + "%'";
      }
      sql = " SELECT oa_doc_type.SEQ_ID, documents_name, flow_name, documents_font, documents_word_model FROM oa_doc_type ,oa_fl_type as  flow_type "
          + " where documents_type.flow_type = flow_type.seq_id " + conditionStr + " ORDER BY SEQ_ID desc";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 查询ById
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static String getDocumentsTypeById(Connection dbConn, String seqIdStr) throws Exception {
    
    String docWordStr = "";
    YHORM orm = new YHORM();
    if (YHUtility.isNullorEmpty(seqIdStr)) {
      seqIdStr = "";
    }
    try {
      String seqIdArry[] = seqIdStr.split(",");
      if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
        for (String seqId : seqIdArry) {
          YHDocWord docWord = (YHDocWord) orm.loadObjSingle(dbConn, YHDocWord.class, Integer.parseInt(seqId));
          if (docWord != null) {
            docWordStr = docWordStr + docWord.getDwName() + ",";
          }
        }
      }
    } catch (Exception e) {
      throw e;
    }
    if(docWordStr.endsWith(",")){
      return docWordStr.substring(0, docWordStr.length()-1);
    }
    else{
      return docWordStr;
    }
  }
  
  public List<YHDocWord> getDocumentsTypeListSelect(Connection dbConn, YHPerson person, String condition) throws Exception{
    List<YHDocWord> list = new ArrayList<YHDocWord>();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = " SELECT SEQ_ID, DW_NAME, INDEX_STYLE FROM oa_officialdoc_word ";
    if(!YHUtility.isNullorEmpty(condition) && !condition.equals("undefined")){
      sql = sql + " where DW_NAME like '%" + condition + "%' ";
    }
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      int counter = 0;
      while(rs.next() && ++counter<50){
        YHDocWord docWord = new YHDocWord();
        docWord.setSeqId(rs.getInt("SEQ_ID"));
        docWord.setDwName(rs.getString("DW_NAME"));
        list.add(docWord);
      }
    } catch (Exception e) {
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    return list;
  }
}
