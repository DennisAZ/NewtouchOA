package yh.subsys.inforesouce.docmgr.logic;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
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

import org.apache.tools.ant.types.FileList.FileName;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowFormItem;
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
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;
import yh.subsys.inforesouce.docmgr.data.YHDocFlowRun;
public class YHDoc2Logic {
  public static String contentStylePath = "subsys/inforesource/docmgr/ntko/docContent";
  public static String docStylePath = "subsys/inforesource/docmgr/ntko/word";
  public static String COPYPATH = "\\subsys\\inforesource\\docmgr\\ntko";
  public void saveDoc(Connection conn , int userId  , String  runId , String docContent , String prcsId , String flowPrcs)  throws Exception {
    String query = "insert into DOC_CONTENT (EDIT_TIME , USER_ID , RUN_ID , PRCS_ID , FLOW_PRCS , DOC_CONTENT) "
      + " values "
      + "  (? , '"+ userId +"' ,  '"+ runId+"'  , '"+ prcsId+"' , '"+ flowPrcs+"' , ?) ";
    PreparedStatement stm5 = null;
    Timestamp time =  new  Timestamp(new Date().getTime());
    try {
      stm5 = conn.prepareStatement(query);
      stm5.setTimestamp(1, time);
      stm5.setString(2, docContent);
      stm5.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm5, null, null); 
    }
  }
  public int getFlowType(Connection conn , String runId) throws Exception {
    int flowType = 0 ;
    String q = "select FLOW_ID FROM oa_fl_run WHERE RUN_ID = '" + runId + "'";
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(q);
      if (rs2.next()){
        flowType = rs2.getInt("FLOW_ID");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    return flowType ;
  }
  public String getContent(Connection conn , int runId) throws Exception {
    String flowType = "" ;
    String q = "select DOC_CONTENT FROM DOC_CONTENT WHERE RUN_ID = '" + runId + "' order by EDIT_TIME DESC";
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(q);
      if (rs2.next()){
        flowType = YHUtility.null2Empty(rs2.getString("DOC_CONTENT"));
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    flowType = flowType.replaceAll("\n", "<br>");
    flowType = YHUtility.encodeSpecial(flowType);
    return flowType ;
  }
  public String getDocHistory(Connection conn   , String  runId , int userId) throws Exception {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    YHFlowTypeLogic logic = new YHFlowTypeLogic();
    int flowType = this.getFlowType(conn, runId);
    String query = "select EDIT_TIME , DOC_CONTENT.USER_ID , RUN_ID , PRCS_ID , FLOW_PRCS , DOC_CONTENT,DOC_CONTENT.SEQ_ID,USER_NAME from  DOC_CONTENT,PERSON where RUN_ID = '" + runId + "' AND PERSON.SEQ_ID = DOC_CONTENT.USER_ID ORDER BY EDIT_TIME desc";
    Statement stm2 = null;
    ResultSet rs2 = null;
    int count = 0 ;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query);
      while (rs2.next()){
        int sqlId = rs2.getInt("SEQ_ID");
        Date editTime = rs2.getTimestamp("EDIT_TIME");
        String userName = rs2.getString("USER_NAME");
        int prcsId = rs2.getInt("PRCS_ID");
        int flowPrcs = rs2.getInt("FLOW_PRCS");
        int userId2  = rs2.getInt("USER_ID");
        String content = YHUtility.null2Empty(rs2.getString("DOC_CONTENT"));
        boolean del = false;
        if ( userId2 == userId) {
          del = true;
        }
        String query2 = "select PRCS_NAME " 
          + " FROM oa_fl_process WHERE  " 
          + " Flow_SEQ_ID=" + flowType 
          + " and PRCS_ID=" + flowPrcs;
        String prcsName = "";
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(query2);
          if (rs.next()) {
            prcsName = rs.getString("PRCS_NAME") ;
          }
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null);
        }
        sb.append("{seqId:" + sqlId);
        sb.append(",prcsId:" + prcsId);
        sb.append(",prcsName:\"" + prcsName + "\"");
        sb.append(",userName:'" + userName + "'");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append(",editTime:'" + sdf.format(editTime) + "'");
        sb.append(",del:" + del);
        content = content.replaceAll("\n", "<br>");
        content = YHUtility.encodeSpecial(content);
        String contentStr = content;
        if (content.length() > 50) {
          contentStr = content.substring(0, 50) + "...";
        } 
        
        sb.append(",contentStr:\"" + contentStr + "\"");
        sb.append(",content:\"" + content + "\"},");
        count++ ;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public void delDocHistory(Connection dbConn, String seqId) throws Exception {
    // TODO Auto-generated method stub
    String query = "delete from DOC_CONTENT where SEQ_ID = '" + seqId + "'";
    YHWorkFlowUtility.updateTableBySql(query, dbConn);
  }
  /**
   * 新建文档
   * @param runId
   * @param newType
   * @param newName
   * @throws Exception 
   */
  public String createAttachment(int runId, String fileName , Connection conn , String webrootPath , String docStyle) throws Exception {
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
      YHDocFlowRun doc = new YHDocFlowRun();
      doc.setRunId(runId);
      doc.setDocName(fileName);
      doc.setDocId(attachmentId);
      doc.setDocStyle(docStyle);
      YHORM orm = new YHORM();
      orm.saveSingle(conn, doc);
      //保存公文 
      return "'"+ attachmentId +"'";
    } else {
      return "''";
    }
  }
  public String getDoc(int runId,int flowPrcs , int flowId, Connection conn) throws Exception {
    // TODO Auto-generated method stub
    YHDocLogic logic = new YHDocLogic();
    String res = logic.getDoc(runId, conn , false);
    String query = "select EXTEND,EXTEND2,EXTEND3 from oa_fl_process where PRCS_ID = " + flowPrcs + " and FLOW_SEQ_ID = " + flowId;
    Statement stm = null;
    ResultSet rs = null;
    boolean isPigeonhole = false;
    boolean docShowPriv = true;
    boolean docCreatePriv = true;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        String extend = rs.getString("EXTEND");
        String extend2 = rs.getString("EXTEND2");
        String extend3 = rs.getString("EXTEND3");
        
        if ("1".equals(extend)) {
          isPigeonhole = true;
        }
        if ("1".equals(extend2)) {
          docShowPriv = false;
        }
        if ("1".equals(extend3)) {
          docCreatePriv = false;
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    boolean hasPigeonhole = logic.hasPigeonhole(runId, conn);
    res += ",isPigeonhole:" + isPigeonhole + ",hasPigeonhole:" + hasPigeonhole + ",docShowPriv:" + docShowPriv + ",docCreatePriv:" + docCreatePriv;
    return res;
  }
}
