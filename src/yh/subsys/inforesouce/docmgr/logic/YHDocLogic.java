package yh.subsys.inforesouce.docmgr.logic;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowFormItem;
import yh.core.funcs.doc.data.YHDocFlowProcess;
import yh.core.funcs.doc.data.YHDocRun;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.logic.YHAttachmentLogic;
import yh.core.funcs.doc.logic.YHFeedbackLogic;
import yh.core.funcs.doc.logic.YHFlowRunLogic;
import yh.core.funcs.doc.logic.YHFlowTypeLogic;
import yh.core.funcs.doc.logic.YHMyWorkLogic;
import yh.core.funcs.doc.util.YHFlowRunUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;
public class YHDocLogic {
  public static String contentStylePath = "subsys/inforesource/docmgr/ntko/docContent";
  public static String docStylePath = "subsys/inforesource/docmgr/ntko/word";
  public static String COPYPATH = "\\subsys\\inforesource\\docmgr\\ntko";
  public String getDocNum(Connection conn , int runId) throws Exception {
    String str = "";
    String query = "select DW_NAME,oa_officialdoc_word.SEQ_ID,DOC_YEAR,oa_officialdoc_word.INDEX_STYLE, oa_doc_type.documents_font  from oa_officialdoc_fl_run,oa_officialdoc_word,oa_doc_type where run_id=" + runId 
      + " AND oa_officialdoc_word.SEQ_ID = oa_officialdoc_fl_run.DOC_WORD"
      + " AND oa_doc_type.SEQ_ID = oa_officialdoc_fl_run.DOC_TYPE";
    Statement stm = null;
    ResultSet rs = null;
    String dwName = "";
    int docWord =  0 ;
    String docYear = "";
    String indexStyle = "";
    String docTypes = "";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
         dwName = YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("DW_NAME")));
         docWord = rs.getInt("SEQ_ID") ;
         indexStyle = YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("INDEX_STYLE")));
         docYear = rs.getString("DOC_YEAR");
         docTypes = rs.getString("documents_font");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    int num = this.getNum(conn, docYear, docWord);
    String docWords = this.getDocWords(conn, docTypes);
    str = "{indexStyle:\""+ indexStyle +"\",docWord:\""+dwName+"\",docWordSeqId:"+docWord+",docYear:'"+docYear+"',docNum:"+num+",docWords:"+ docWords +"}";
    return str;
  }
  public String getDocWords(Connection conn , String docTypes) throws Exception {
    StringBuffer sb = new StringBuffer();
    if (YHUtility.isNullorEmpty(docTypes)) {
      return "[]";
    }
    docTypes = YHWorkFlowUtility.getOutOfTail(docTypes);
    String query = "select SEQ_ID ,DW_NAME  from oa_officialdoc_word where SEQ_ID IN ("+docTypes+")";
    Statement stm = null;
    ResultSet rs = null;
    sb.append("[");
    int count  = 0 ;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
         String name = YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("DW_NAME"))) ;
         int seqId = rs.getInt("SEQ_ID");
         sb.append("{").append("name:\"").append(name).append("\",seqId:\"").append(seqId).append("\"},");
         count++;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    if (count > 0 ) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public int getNum(Connection conn , String year , int docWord) throws Exception {
    String query = "select NUM from oa_officialdoc_num where DOC_YEAR='" + year  + "' AND DOC_WORD = '" + docWord + "'";
    Statement stm = null;
    ResultSet rs = null;
    int num = 1 ;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        num = rs.getInt("NUM");
        num++;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return num;
  }
  public int getFeedback(Connection conn , int runId , int flowId , int prcsId , int userId) throws Exception {
    String query = "select MAX(SEQ_ID) from oa_fl_run_feedback where RUN_ID='" + runId  + "' AND PRCS_ID = '" + prcsId + "' AND USER_ID='" + userId + "'";
    Statement stm = null;
    ResultSet rs = null;
    int max = 0 ;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        max = rs.getInt(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return max;
  }
  
  /**
   * 新建文档
   * @param runId
   * @param newType
   * @param newName
   * @throws Exception 
   */
  public String createAttachment(int runId, String fileName , Connection conn , String webrootPath) throws Exception {
    // TODO Auto-generated method stub
    Calendar cld = Calendar.getInstance();
    int year =  cld.get(Calendar.YEAR)%100;
    int month = cld.get(Calendar.MONTH) + 1;
    String mon = month >= 10?month+"":"0"+month;
    String hard = year + mon ;
    String attachmentId = YHGuid.getRawGuid(); 
    String fileName2 = attachmentId + "_" + fileName;
    String tmp = YHAttachmentLogic.filePath + "\\" + hard + "\\" + fileName2;
    File catalog = new File(YHAttachmentLogic.filePath + "\\" + hard);
    if(!catalog.exists()){
      catalog.mkdirs();
    }
    boolean success = false;
    File file = new File(tmp);
    String srcFile = webrootPath + this.COPYPATH  + "\\母版.doc";
    YHFileUtility.copyFile(srcFile, tmp);
    success = true;
    if (success) {
      attachmentId = hard + "_" + attachmentId;
      String update =  "update oa_officialdoc_fl_run SET DOC_NAME = '"+ fileName +"',DOC_ID='"+attachmentId+"' , DOC_STYLE='' WHERE RUN_ID='"+ runId +"'";
      //保存公文 
      YHWorkFlowUtility.updateTableBySql(update, conn);
      return "'"+ attachmentId +"'";
    } else {
      return "''";
    }
  }
  /**
   * 删除公文
   * @param runId
   * @throws Exception
   */
  public void delDoc(int runId  , Connection conn) throws Exception{
    //处理数据库
    String query = "select * from oa_officialdoc_fl_run where run_id=" + runId;
    Statement stm = null;
    ResultSet rs = null;
    String attachmentId = "";
    String attachmentName = "";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        attachmentId = rs.getString("DOC_ID");
        attachmentName = rs.getString("DOC_NAME");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    
  //处理数据库    String query2 = "delete from oa_officialdoc_fl_run where run_id=" + runId;
    Statement stm1 = null;
    try {
      stm1 = conn.createStatement();
      int result = stm1.executeUpdate(query2);
      if (result > 0) {
        YHAttachmentLogic.deleteAttachement(attachmentId, attachmentName);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm1, null, null);
    }
  }
  /**
   * 删除公文
   * @param runId
   * @throws Exception
   */
  public void delDoc1(int runId  , Connection conn) throws Exception{
    //处理数据库

    String query = "select * from oa_officialdoc_fl_run where run_id=" + runId;
    Statement stm = null;
    ResultSet rs = null;
    String attachmentId = "";
    String attachmentName = "";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        attachmentId = rs.getString("DOC_ID");
        attachmentName = rs.getString("DOC_NAME");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    
  //处理数据库
    String query2 = "update oa_officialdoc_fl_run set DOC_ID='' , DOC_NAME=''  where run_id=" + runId;
    Statement stm1 = null;
    try {
      stm1 = conn.createStatement();
      int result = stm1.executeUpdate(query2);
      if (result > 0) {
        YHAttachmentLogic.deleteAttachement(attachmentId, attachmentName);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm1, null, null);
    }
  }
  public String getDoc(int runId,int flowPrcs , int flowId, Connection conn) throws Exception {
    // TODO Auto-generated method stub
    String res = this.getDoc(runId, conn , false);
    boolean isPigeonhole = this.enforcePigeonhole(flowPrcs, flowId, conn);
    boolean hasPigeonhole = this.hasPigeonhole(runId, conn);
    boolean isGiveNum = this.isGiveNum(flowPrcs, flowId, conn);
    int num = this.hasGiveNum(runId, conn);
    YHDocFlowProcess fp =  this.getDocPriv(flowPrcs, flowId, conn);
    res += ",isPigeonhole:" + isPigeonhole + ",hasPigeonhole:" + hasPigeonhole + ",isGiveNum:" + isGiveNum + ",num:" + num + ",docPriv:\""+ fp.getDocAttachPriv() + "\",docCreate:\"" + fp.getDocCreate() + "\"";
    return res;
  }
  /**
   * 已经归档没有
   * @return
   * @throws Exception 
   */
  public String getDocType(YHPerson person, Connection conn) throws Exception {
    String query = "select * from oa_doc_type";
    Statement stm = null;
    ResultSet rs = null;
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    int count = 0 ;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        sb.append("{");
        sb.append("seqId:" + rs.getString("seq_id"));
        sb.append(",name:\"" + rs.getString("documents_name") + "\"");
        sb.append("},");
        count++ ;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    if (count > 0 ) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  /**
   * 已经归档没有
   * @return
   * @throws Exception 
   */
  public String getDocWordByType(YHPerson person, Connection conn , String type) throws Exception {
    String query = "select flow_type , documents_font from oa_doc_type where seq_id = '" + type + "'"; 
    Statement stm = null;
    ResultSet rs = null;
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    int flowType = 0;
    String documentsFont = "";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
         flowType = rs.getInt("flow_type");
        documentsFont = YHUtility.null2Empty(rs.getString("documents_font"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    sb.append("flowType:'" + flowType + "'");
    sb.append(",docWords:[");
    
    documentsFont = YHWorkFlowUtility.getOutOfTail(documentsFont);
    if (!"".equals(documentsFont)) {
      int count = 0 ;
      String query1 = "SELECT SEQ_ID, DW_NAME, INDEX_STYLE, DEPART_PRIV, ROLE_PRIV, USER_PRIV  FROM oa_officialdoc_word WHERE SEQ_ID IN (" + documentsFont +")";
      Statement stm1 = null;
      ResultSet rs1 = null;
      try {
        stm1 = conn.createStatement();
        rs1 = stm1.executeQuery(query1);
        while (rs1.next()) {
          String departPriv = rs1.getString("DEPART_PRIV");
          String rolePriv = rs1.getString("ROLE_PRIV");
          String userPriv = rs1.getString("USER_PRIV");
          if (checkPriv(userPriv, departPriv, rolePriv , person)) {
            sb.append("{");
            sb.append("seqId:" + rs1.getString("SEQ_ID"));
            sb.append(",name:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(rs1.getString("DW_NAME"))) + "\"");
            sb.append(",indexStyle:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(rs1.getString("INDEX_STYLE"))) + "\"");
            sb.append("},");
            count++ ;
          }
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm1, rs1, null);
      }
      if (count > 0 ) {
        sb.deleteCharAt(sb.length() - 1);
      }
    }
    sb.append("]}");
    return sb.toString();
  }
  public boolean checkPriv(String prcsUser, String prcsDept , String prcsPriv , YHPerson user) {
    if ("0".equals(prcsDept) || "ALL_DEPT".equals(prcsDept)) {
      return true;
    }
    String userPrivOther = user.getUserPrivOther();
    String userDeptIdOther = user.getDeptIdOther();
    if(YHWorkFlowUtility.findId(prcsUser , String.valueOf(user.getSeqId()))){
      return true;
    }
    if(YHWorkFlowUtility.findId(prcsDept , String.valueOf(user.getDeptId()))){
      return true;
    }
    if(YHWorkFlowUtility.findId(prcsPriv,user.getUserPriv())){
      return true;
    }
    if(userPrivOther != null && !YHWorkFlowUtility.checkId(prcsPriv , userPrivOther , true).equals("")){
      return true;
    }
    if(userDeptIdOther != null && !YHWorkFlowUtility.checkId(prcsDept , userDeptIdOther , true).equals("")){
      return true;
    }
    return false;
  }
  /**
   * 已经归档没有
   * @return
   * @throws Exception 
   */
  public boolean hasPigeonhole(int runId , Connection conn) throws Exception {
    boolean result = false;
    String query = "select " 
      + " extend  " 
      + " from oa_fl_run where  " 
      + " run_id=" + runId;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        String extend = rs.getString("extend");
        if (extend != null) {
          result = true;
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return result;
  }
  public int hasGiveNum(int runId , Connection conn) throws Exception {
    boolean result = false;
    String query = "select " 
      + " DOC_NUM  " 
      + " from oa_officialdoc_fl_run where  " 
      + " run_id=" + runId;
    Statement stm = null;
    int extend = 0;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
         extend = rs.getInt("DOC_NUM");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return   extend ;
  }
  public String getDoc(int runId, Connection conn , boolean isRestore) throws Exception {
    // TODO Auto-generated method stub
    String query = "select * from oa_officialdoc_fl_run where run_id=" + runId;
    Statement stm = null;
    ResultSet rs = null;
    String attachmentId = "";
    String attachmentName = "";
    String docStyle = "";
    String res = "''";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        attachmentId = YHUtility.null2Empty(rs.getString("DOC_ID"));
        attachmentName = YHUtility.null2Empty(rs.getString("DOC_NAME"));
        
        if (isRestore) {
          attachmentId = this.restoreFile(attachmentId, attachmentName);
        }
        res =  "{docId:'" + attachmentId + "',docName:'" + attachmentName + "',docStyle:'"+docStyle+"'}";
      } 
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return res;
  }
  public String restoreFile(String aId , String aName) throws Exception {
    int index = aId.indexOf("_");
    String hard = "";
    String str = "";
    if (index > 0) {
      hard = aId.substring(0, index);
      str = aId.substring(index + 1);
    } else {
      hard = "all";
      str = aId;
    }
    String path = YHAttachmentLogic.filePath + "\\"  + hard + "\\" + str + "_" + aName;
    File file = new File(path);
    String docId = "";
    if (file.exists()) {
      InputStream in = new FileInputStream(file);
      docId = this.storeFileToRoll(aName, in);
    }
    return docId;
  }
  /**
   * 是否强制归档  
   * @param runId
   * @param conn
   * @return
   * @throws Exception
   */
  public boolean enforcePigeonhole(int flowPrcs , int flowId, Connection conn) throws Exception {
    boolean result = false;
    String query = "select EXTEND from oa_fl_process where PRCS_ID = " + flowPrcs + " and FLOW_SEQ_ID = " + flowId;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        String extend = rs.getString("EXTEND");
        if ("1".equals(extend)) {
          result = true;
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return result;
  }
  /**
   * 是否  
   * @param runId
   * @param conn
   * @return
   * @throws Exception
   */
  public boolean isGiveNum(int flowPrcs , int flowId, Connection conn) throws Exception {
    boolean result = false;
    String query = "select EXTEND1 from oa_fl_process where PRCS_ID = " + flowPrcs + " and FLOW_SEQ_ID = " + flowId;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        String extend = rs.getString("EXTEND1");
        if ("1".equals(extend)) {
          result = true;
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return result;
  }
  public YHDocFlowProcess getDocPriv(int flowPrcs , int flowId, Connection conn) throws Exception {
    String query = "select DOC_ATTACH_PRIV,DOC_CREATE from oa_fl_process where PRCS_ID = " + flowPrcs + " and FLOW_SEQ_ID = " + flowId;
    Statement stm = null;
    ResultSet rs = null;
    YHDocFlowProcess fp = new YHDocFlowProcess();
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        String docAttachPriv = rs.getString("DOC_ATTACH_PRIV");
        String docCreate = rs.getString("DOC_CREATE");
        fp.setDocAttachPriv(docAttachPriv);
        //if ("1".equals(docCreate)) {
          fp.setDocCreate(YHUtility.null2Empty(docCreate));
        //}
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return fp;
  }
  public String getBookmark(int runId, Connection conn) throws Exception {
    // TODO Auto-generated method stub
    String query = "select " 
      + " form_seq_Id   " 
      + " from oa_fl_type as flow_type , oa_fl_run as flow_run where  " 
      + " flow_id = flow_type.seq_id  " 
      + " and run_id=" + runId;
    Statement stm = null;
    ResultSet rs = null;
    int formId = 0 ;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        formId = rs.getInt("form_seq_Id");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    Map queryItem = new HashMap();
    queryItem.put("FORM_ID", formId);
    YHORM orm = new YHORM();
    List<YHDocFlowFormItem> list = orm.loadListSingle(conn, YHDocFlowFormItem.class, queryItem);
    int itemId = 0;
    for (YHDocFlowFormItem item : list) {
      if ("设定书签".equals(item.getTitle())) {
        itemId = item.getItemId();
      }
    }
    YHFlowRunUtility runUtility = new YHFlowRunUtility();
    //String value = runUtility.getData(runId, itemId, conn);
    //String[] vals = value.split(",");
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    int count = 0;
      int itemId2 = 0;
      for (YHDocFlowFormItem item : list) {
        itemId2 = item.getItemId();
        String value = runUtility.getData(runId, itemId2, conn);
        sb.append("[").append("\"").append(item.getTitle()).append("\",\"").append(YHUtility.encodeSpecial(value)).append("\"],");
        count++;
      }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    Timestamp createTime = this.getWrittenTime(runId, conn);
    if (createTime != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String ss = sdf.format(createTime);
      if (count > 0) {
        sb.append(",");
      }
      sb.append("[\"成文日期\",\""+ ss +"\"]");
    }
    sb.append("]");
    return sb.toString();
  }
  public Timestamp getWrittenTime(int runId , Connection  conn) throws Exception {
    String query = "SELECT WRITTEN_TIME FROM oa_officialdoc_fl_run WHERE RUN_ID = '" + runId +"'";
    Statement stm = null;
    ResultSet rs = null;
    Timestamp time = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        time = rs.getTimestamp("WRITTEN_TIME");
      } 
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return time;
  }
  public String getDocStyle(String webrootPath ) {
    String docPath = webrootPath + docStylePath;
    File docFile = new File(docPath);
    String[] names = docFile.list();
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    int count = 0 ;
    for (String n : names) {
      if (n.endsWith("doc")) {
        sb.append( "\""+ YHUtility.encodeSpecial(n)+ "\"").append(",");
        count++;
      }
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public String getDocStyle1(String webrootPath ,int runId , Connection conn) throws Exception {
    String docPath = webrootPath + docStylePath;
    String query = "SELECT documents_word_model FROM oa_doc_type,oa_officialdoc_fl_run "
      + " where "
      + " oa_doc_type.SEQ_ID =oa_officialdoc_fl_run.DOC_TYPE "
      + " AND oa_officialdoc_fl_run.RUN_ID = '" + runId + "'";
    
    String doc = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        doc = rs.getString("documents_word_model");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    
    File docFile = new File(docPath);
    String[] names = docFile.list();
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    int count = 0 ;
    for (String n : names) {
      if (n.endsWith("doc") 
          && YHWorkFlowUtility.findId(doc, n)) {
        sb.append( "\""+ YHUtility.encodeSpecial(n)+ "\"").append(",");
        count++;
      }
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public String getDocStyle1(String webrootPath , String seqId  , Connection conn ) throws Exception {
    String docMod = "";
    String sql = "select doc_mod from oa_officialdoc_type where SEQ_ID = " + seqId;
    Statement stm = null;
    ResultSet rs = null;
    
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(sql);
      if (rs.next()) {
        docMod = YHUtility.null2Empty(rs.getString("doc_mod"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    
    String docPath = webrootPath + docStylePath;
    File docFile = new File(docPath);
    String[] names = docFile.list();
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    int count = 0 ;
    for (String n : names) {
      if (n.endsWith("doc") && YHWorkFlowUtility.findId(docMod, n)) {
        sb.append( "'"+ n+ "'").append(",");
        count++;
      }
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public String getContentStyle(String webrootPath) {
    String docPath = webrootPath + contentStylePath;
    File docFile = new File(docPath);
    String[] names = docFile.list();
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    for (String n : names) {
      if (n.endsWith("doc")) {
        sb.append( "'"+ n+ "'").append(",");
      }
    }
    if (names.length > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public void saveDocStyle(int runId, String docStyle, Connection conn) throws Exception {
    // TODO Auto-generated method stub
    //处理数据库    String query2 = "update oa_officialdoc_fl_run set doc_style=? where run_id=" + runId;
    PreparedStatement stm1 = null;
    try {
      stm1 = conn.prepareStatement(query2);
      stm1.setString(1, docStyle);
      stm1.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm1, null, null);
    }
  }
  public void saveCreateTime(int feedback, Connection conn , int runId) throws Exception {
    // TODO Auto-generated method stub
    String query = "SELECT EDIT_TIME FROM oa_fl_run_feedback WHERE SEQ_ID = '" + feedback +"'";
    Statement stm = null;
    ResultSet rs = null;
    Timestamp time = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        time = rs.getTimestamp("EDIT_TIME");
      } 
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    String update = "update oa_officialdoc_fl_run set WRITTEN_TIME=? where RUN_ID=" + runId;
    PreparedStatement stm1 = null;
    try {
      stm1 = conn.prepareStatement(update);
      stm1.setTimestamp(1,time);
      stm1.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm1, null, null);
    }
  }
  public String getStyle(int runId, Connection conn) throws Exception {
    String query = "select DOC_STYLE from oa_officialdoc_fl_run where run_id=" + runId;
    Statement stm = null;
    ResultSet rs = null;
    String docStyle = "";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        docStyle = rs.getString("DOC_STYLE");
        if (docStyle == null) {
          docStyle = "";
        }
        return "'"+docStyle+"'";
      } else {
        return "''";
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
  }
  /**
   * 取得归档数据
   * @param runId
   * @param conn
   * @return
   */
  public String getPigeonholeData(int runId, Connection conn) throws Exception {
    // TODO Auto-generated method stub
    String query = "select " 
      + " form_seq_Id   " 
      + " from oa_fl_type as flow_type , oa_fl_run as flow_run where  " 
      + " flow_id = flow_type.seq_id  " 
      + " and run_id=" + runId;
    Statement stm = null;
    ResultSet rs = null;
    int formId = 0 ;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        formId = rs.getInt("form_seq_Id");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    Map queryItem = new HashMap();
    queryItem.put("FORM_ID", formId);
    YHORM orm = new YHORM();
    List<YHDocFlowFormItem> list = orm.loadListSingle(conn, YHDocFlowFormItem.class, queryItem);
    YHFlowRunUtility runUtility = new YHFlowRunUtility();
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    int count = 0;
    int itemId2 = 0;
    for (YHDocFlowFormItem item : list) {
        itemId2 = item.getItemId();
        String v = item.getTitle();
        String value = runUtility.getData(runId, itemId2, conn);
        sb.append("[").append("'").append(v).append("','").append(value).append("'],");
        count++;
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  /**
   * 处理归档相关附件
   * @param runId
   * @param imgPath 
   * @param loginUser 
   * @param dbConn
   * @return
   * @throws Exception 
   */
  public String pigeonholeAttachment(int runId, Connection conn, YHPerson user, String imgPath) throws Exception {
    // TODO Auto-generated method stub
    String attachId = "";
    String attachName = "";
    
    YHFlowTypeLogic ftl = new YHFlowTypeLogic();
    YHFlowRunLogic frl = new YHFlowRunLogic();
    YHAttachmentLogic attachLogic = new YHAttachmentLogic();
    YHFeedbackLogic feedbackLogic = new YHFeedbackLogic();
    YHMyWorkLogic workLogic = new YHMyWorkLogic();
    
    YHDocRun flowRun = frl.getFlowRunByRunId(runId , conn);
    String runName = flowRun.getRunName();
    String aIds = flowRun.getAttachmentId();
    String aNames = flowRun.getAttachmentName();
    YHDocFlowType ft = ftl.getFlowTypeById(flowRun.getFlowId(),conn);
    
    StringBuffer sb = new StringBuffer();
    Map result = frl.getPrintForm(user, flowRun, ft ,true, conn , imgPath) ;
    String  form = (String)result.get("form");
    sb.append("<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><head><title></title></head><body><div id=\"form\" style=\"margin-top:5px;margic-bottom:5px\">");
    form = form.replaceAll("\\\\\"", "\"");
    sb.append(form).append("</div><div id=\"attachment\"><table width='100%'><tr class=TableHeader><td colspan=3>公共附件</td></tr><tbody id=\"attachmentsList\">");
    String attachment = attachLogic.getAttachmentsHtml(user, flowRun.getRunId() , conn);
    sb.append(attachment).append("</tbody></table></div><div id=\"feedBack\"><table width='100%'><tr class=TableHeader><td colspan=3>会签与点评</td></tr><tbody id=\"feedbackList\">");
    String feedbacks = feedbackLogic.getFeedbacksHtml(user, flowRun.getFlowId() , flowRun.getRunId() ,conn);
    sb.append(feedbacks).append("</tbody></table></div><div id=\"prcss\"><table width='100%'><tr class=TableHeader><td colspan=3>流程图</td></tr><tbody id=\"listTbody\">");
    String prcs =  workLogic.getPrcsHtml(flowRun.getRunId(), ft , conn );
    sb.append(prcs);
    sb.append("</tbody></table></div></body></html>");
    InputStream isb = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
    runName = YHWorkFlowUtility.getFileName(runName);
    String fileName =   runName + ".html";
    attachName += fileName + "*";
    attachId += this.storeFileToRoll(fileName , isb) + ",";
    List<File> list = workLogic.getAttachement(aIds, aNames) ;
    for (int i = 0; i < list.size(); i++) {
      File file = list.get(i);
      InputStream in = new FileInputStream(file);
      String tmp = file.getName();
      int index = tmp.indexOf("_") + 1;
      tmp = tmp.substring(index);
      attachName += tmp + "*";
      attachId += this.storeFileToRoll(tmp , in) + ",";
    }
    String res = "{attachmentName:\"" + YHWorkFlowUtility.getOutOfTail(attachName) + "\",attachmentId:\"" + YHWorkFlowUtility.getOutOfTail(attachId) + "\"}";
    return res;
  }
  public String storeFileToRoll(String fileName , InputStream in) throws Exception {
    String attachId  = "";
    Date date = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyMM");
    String currDate = format.format(date);
    String separator = File.separator;
    String filePath = YHSysProps.getAttachPath() + separator + "roll_manage" + separator + currDate;
    String rand =  YHGuid.getRawGuid(); ;
    attachId = currDate + "_" + rand ;
    fileName = filePath + "\\" + rand + "_" + fileName;
    YHFileUtility.storeFileFromStream(in, fileName);
    return  attachId;
  }
  public void updateFlowRun(int runId , Connection conn) throws Exception {
    String query = "update oa_fl_run set extend='1' where RUN_ID=" + runId;
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate(query);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, null, null);
    }
  }
  public boolean getState(int runId , int flowPrcs , int flowId , Connection conn) throws Exception {
    String query = "select extend from oa_fl_process where flow_seq_id=" + flowId + " and prcs_id=" + flowPrcs;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        String extend = rs.getString("extend");
        if ("1".equals(extend)) {
          String query2 = "select extend from oa_fl_run where run_id=" + runId;
          Statement stm2 = null;
          ResultSet rs2 = null;
          try {
            stm2 = conn.createStatement();
            rs2 = stm2.executeQuery(query2);
            if (rs2.next()) {
              String extend2 = rs2.getString("extend");
              if ("1".equals(extend2)) {
                return false;
              } else {
                return true;
              }
            }
          } catch (Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(stm2, rs2, null);
          }
        } else {
          return false;
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return false;
  }
  public String getHandlerTime(int runId, int prcsId, Connection conn) throws Exception {
    // TODO Auto-generated method stub
    long timeUsed = 0 ;
    long beginDate = 0;
    String query = "select begin_time from oa_fl_run where run_id = " + runId;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        Timestamp beginTime = rs.getTimestamp("begin_time");
        beginDate = beginTime.getTime();
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    timeUsed = new Date().getTime() - beginDate;
    String timeStr = "";
    long day=timeUsed/(24*60*60*1000); 
    long hour=(timeUsed/(60*60*1000)-day*24); 
    long min=((timeUsed/(60*1000))-day*24*60-hour*60); 
    long s=(timeUsed/1000-day*24*60*60-hour*60*60-min*60);
    
    if ( day > 0 ) {
      timeStr = day + "天";
    }
    if ( hour>0){
      timeStr +=hour + "时";
    }
    if(min>0){
      timeStr +=min + "分";
    }
    if(s>0){
      timeStr +=s + "秒";
    }
    return timeStr;
  }
  public int sendNum(Connection dbConn, String year, int docWord, String doc,
      int runId, int docNum) throws Exception {
    // TODO Auto-generated method stub
    String update = "update oa_officialdoc_fl_run SET DOC_YEAR='" + year + "' , DOC='" + doc + "' , DOC_NUM= '"+ docNum +"' where RUN_ID=" + runId ;
    YHWorkFlowUtility.updateTableBySql(update, dbConn);
    YHFlowRunLogic logic = new YHFlowRunLogic();
    logic.updateRunName(doc, runId, dbConn);
    String query = "select * from oa_officialdoc_num where DOC_WORD=" + docWord + " AND DOC_YEAR='" + year + "' " ;
    Statement stm = null;
    ResultSet rs = null;
    String update2 = "update oa_officialdoc_num SET NUM= '"+ docNum +"' where DOC_WORD=" + docWord + " AND DOC_YEAR='" + year + "' " ;
    try {
      stm = dbConn.createStatement();
      rs = stm.executeQuery(query);
      if (!rs.next()) {
        update2 = "insert into oa_officialdoc_num (NUM,DOC_WORD,DOC_YEAR) VALUES ('"+ docNum +"','" + docWord + "','" + year + "')";
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    YHWorkFlowUtility.updateTableBySql(update2, dbConn);
    YHFlowRunLogic logic1= new YHFlowRunLogic();
    logic1.updateRunName(doc, runId,dbConn );
    return 0;
  }
}
