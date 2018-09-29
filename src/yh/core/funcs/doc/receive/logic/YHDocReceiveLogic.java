package yh.core.funcs.doc.receive.logic;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.doc.receive.data.YHDocConst;
import yh.core.funcs.doc.receive.data.YHDocNext;
import yh.core.funcs.doc.receive.data.YHDocReceive;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;

/**
 * 收文管理
 * @author Administrator
 *
 */
public class YHDocReceiveLogic{
  /**
   * 我的未(已)发收文
   * @param conn
   * @param doc
   * @param user
   * @throws Exception 
   */
  public List<YHDocReceive> faDocReceive(Connection conn, YHPerson user, int sendStatus, String column, String asc) throws Exception{
    String sql = " select dr.SEQ_ID, dr.DOC_NO, dr.RES_DATE, dr.FROMUNITS, dr.OPPDOC_NO, dr.TITLE, dr.COPIES, dr.CONF_LEVEL, dr.INSTRUCT, dr.SPONSOR, " +
    		                 " dr.RECE_USER_ID, dr.DOC_TYPE,dr.STATUS, dr.SEND_STATUS, dr.CREATE_USER_ID,dr.ATTACHNAME,dr.ATTACHID,dr.RUN_ID , dr.REC_DOC_ID , dr.REC_DOC_NAME from oa_officialdoc_recv dr   " +
    		                 " where dr.RECE_USER_ID = "+ user.getSeqId() +" and dr.SEND_STATUS = " + sendStatus;
    if(YHUtility.isNullorEmpty(column)){
      sql+= " order by dr.SEQ_ID desc";
    }else{
      sql += "order by dr."+column +" "+ asc;
    }
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHDocReceive> dosc = new ArrayList<YHDocReceive>();
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHDocReceive doc = new YHDocReceive();
        doc.setSeq_id(rs.getInt("SEQ_ID"));
        doc.setDocNo(rs.getString("DOC_NO"));
        doc.setResDate(rs.getDate("RES_DATE"));
        doc.setFromUnits(rs.getString("FROMUNITS"));
        doc.setOppdocNo(rs.getString("OPPDOC_NO"));
        doc.setTitle(rs.getString("TITLE"));
        doc.setCopies(rs.getInt("COPIES"));
        doc.setConfLevel(rs.getInt("CONF_LEVEL"));
        doc.setInstruct(rs.getString("INSTRUCT"));
        doc.setSponsor(rs.getString("SPONSOR"));
        int deptId = 0;
        try {
          deptId = Integer.parseInt(doc.getSponsor());
        }catch(Exception ex) {          
        }
        doc.setSponsorName(getDeptName(conn, deptId));
        doc.setRecipient(rs.getString("RECE_USER_ID"));
        doc.setToUserName(getUserName(conn, rs.getInt("RECE_USER_ID")));
        doc.setDocType(rs.getInt("DOC_TYPE"));
        doc.setStatus(rs.getInt("STATUS"));
        doc.setSendStauts(rs.getInt("SEND_STATUS"));
        doc.setUserId(rs.getInt("CREATE_USER_ID"));
        doc.setAttachNames(rs.getString("ATTACHNAME"));
        doc.setAttachIds(rs.getString("ATTACHID"));
        doc.setRunId(rs.getString("RUN_ID"));
        doc.setRecDocId(rs.getString("REC_DOC_ID"));
        doc.setRecDocName(rs.getString("REC_DOC_NAME"));
        doc.setNext(returnNext(conn, rs.getString("RUN_ID")));
        dosc.add(doc);
      }
    } catch (SQLException e){
      throw e;
    }
    return dosc;
  }
  /**
   * 我的未(已)发收文

   * @param conn
   * @param doc
   * @param user
   * @throws Exception 
   */
  public List<YHDocReceive> faDocReceive1(Connection conn, YHPerson user, int sendStatus, String column, String asc) throws Exception{
    String tmp = " dr.SPONSOR = '"+ user.getDeptId() +"' ";
    //支持辅助部门
    if (!YHUtility.isNullorEmpty(user.getDeptIdOther())) {
      tmp += " OR dr.SPONSOR IN ( " + YHWorkFlowUtility.getInStr(user.getDeptIdOther()) + " ))";
      tmp = "( " + tmp;
    }
    String sql = " select dr.SEQ_ID, dr.DOC_NO, dr.RES_DATE, dr.FROMUNITS, dr.OPPDOC_NO, dr.TITLE, dr.COPIES, dr.CONF_LEVEL, dr.INSTRUCT, dr.SPONSOR, " +
                         " dr.RECE_USER_ID, dr.DOC_TYPE,dr.STATUS, dr.SEND_STATUS, dr.CREATE_USER_ID,dr.ATTACHNAME,dr.ATTACHID,dr.RUN_ID,dr.REC_DOC_ID, dr.REC_DOC_NAME from oa_officialdoc_recv dr   " +
                         " where  "
                         + tmp + " and dr.SEND_STATUS = " + sendStatus;
    
    if(YHUtility.isNullorEmpty(column)){
      sql+= " order by dr.SEQ_ID desc";
    }else{
      sql += "order by dr."+column +" "+ asc;
    }
    
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHDocReceive> dosc = new ArrayList<YHDocReceive>();
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHDocReceive doc = new YHDocReceive();
        doc.setSeq_id(rs.getInt("SEQ_ID"));
        doc.setDocNo(rs.getString("DOC_NO"));
        doc.setResDate(rs.getDate("RES_DATE"));
        doc.setFromUnits(rs.getString("FROMUNITS"));
        doc.setOppdocNo(rs.getString("OPPDOC_NO"));
        doc.setTitle(rs.getString("TITLE"));
        doc.setCopies(rs.getInt("COPIES"));
        doc.setConfLevel(rs.getInt("CONF_LEVEL"));
        doc.setInstruct(rs.getString("INSTRUCT"));
        doc.setSponsor(rs.getString("SPONSOR"));
        int deptId = 0;
        try {
          deptId = Integer.parseInt(doc.getSponsor());
        }catch(Exception ex) {          
        }
        doc.setSponsorName(getDeptName(conn, deptId));
        doc.setRecipient(rs.getString("RECE_USER_ID"));
        doc.setToUserName(getUserName(conn, rs.getInt("RECE_USER_ID")));
        doc.setDocType(rs.getInt("DOC_TYPE"));
        doc.setStatus(rs.getInt("STATUS"));
        doc.setSendStauts(rs.getInt("SEND_STATUS"));
        doc.setUserId(rs.getInt("CREATE_USER_ID"));
        doc.setAttachNames(rs.getString("ATTACHNAME"));
        doc.setAttachIds(rs.getString("ATTACHID"));
        doc.setRunId(rs.getString("RUN_ID"));
        doc.setRecDocId(rs.getString("REC_DOC_ID"));
        doc.setRecDocName(rs.getString("REC_DOC_NAME"));
        doc.setNext(returnNext(conn, rs.getString("RUN_ID")));
        dosc.add(doc);
      }
    } catch (SQLException e){
      throw e;
    }
    return dosc;
  }
  
  public String getUserName(Connection dbConn, int seqId) throws Exception{
    String sql = "select  USER_NAME from person dr where dr.SEQ_ID=" + seqId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
         return rs.getString("USER_NAME");
      }
    } catch (Exception e){      
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return null;
  }
  
  public String getDeptName(Connection dbConn, int seqId) throws Exception{
    String sql = "select DEPT_NAME from oa_department dr where dr.SEQ_ID=" + seqId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
         return rs.getString("DEPT_NAME");
      }
    } catch (Exception e){      
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return "";
  }
  
  /**
   * 查找一个收文
   * @param conn
   * @param doc
   * @param user
   * @throws Exception 
   */
  public YHDocReceive faDoc(Connection conn,  int seqId) throws Exception{
    String sql = " select dr.SEQ_ID, dr.DOC_NO, dr.RES_DATE, dr.FROMUNITS, dr.OPPDOC_NO, dr.TITLE, dr.COPIES, dr.CONF_LEVEL, dr.INSTRUCT, dr.SPONSOR, " +
                         " dr.RECE_USER_ID, dr.DOC_TYPE,dr.STATUS, dr.SEND_STATUS, dr.CREATE_USER_ID,dr.ATTACHNAME,dr.ATTACHID,dr.RUN_ID from oa_officialdoc_recv dr  " +
                         "where  dr.SEQ_ID ="+ seqId +" order by dr.SEQ_ID desc";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        YHDocReceive doc = new YHDocReceive();
        doc.setSeq_id(rs.getInt("SEQ_ID"));
        doc.setDocNo(rs.getString("DOC_NO"));
        doc.setResDate(rs.getDate("RES_DATE"));
        doc.setFromUnits(rs.getString("FROMUNITS"));
        doc.setOppdocNo(rs.getString("OPPDOC_NO"));
        doc.setTitle(rs.getString("TITLE"));
        doc.setCopies(rs.getInt("COPIES"));
        doc.setConfLevel(rs.getInt("CONF_LEVEL"));
        doc.setInstruct(rs.getString("INSTRUCT"));
        doc.setSponsor(rs.getString("SPONSOR"));
        doc.setRecipient(rs.getString("RECE_USER_ID"));
        doc.setDocType(rs.getInt("DOC_TYPE"));
        doc.setStatus(rs.getInt("STATUS"));
        doc.setSendStauts(rs.getInt("SEND_STATUS"));
        doc.setUserId(rs.getInt("CREATE_USER_ID"));
        //doc.setToUserName(rs.getString("USER_NAME"));
        doc.setAttachNames(rs.getString("ATTACHNAME"));
        doc.setAttachIds(rs.getString("ATTACHID"));
        doc.setRunId(rs.getString("RUN_ID"));
        doc.setFromUserName(getUserName(conn, rs.getInt("CREATE_USER_ID")));
        int deptId = 0;
        try {
          deptId = Integer.parseInt(doc.getSponsor());
        }catch(Exception ex) {          
        }
        doc.setSponsorName(getDeptName(conn, deptId));
        return doc;
      }
    } catch (SQLException e){
      throw e;
    }
    return null;
  }
  
  /**
   * 新建收文
   * @param conn
   * @param doc
   * @throws Exception
   */
  public void insertBeanChYHDocReceive(Connection conn, YHDocReceive doc) throws Exception{
    String sql = " insert into oa_officialdoc_recv(DOC_NO, RES_DATE, FROMUNITS, OPPDOC_NO, TITLE, COPIES, CONF_LEVEL, INSTRUCT, SPONSOR, RECE_USER_ID, DOC_TYPE, STATUS, SEND_STATUS, CREATE_USER_ID,ATTACHNAME,ATTACHID) ";
           sql += " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement ps = null;
    try {
      conn.setAutoCommit(false);
      ps = conn.prepareStatement(sql);
      String reUserId = doc.getRecipient();
      int cnt = 0 ;
      String[] cont = null;
      if(YHUtility.isNullorEmpty(reUserId)){
        cnt = 1;
      }else{
        cont = doc.getRecipient().split(",");
        cnt = cont.length;
      }
      for(int i=0; i<cnt; i++){
        ps.setString(1, doc.getDocNo());
        ps.setDate(2, new Date(new java.util.Date().getTime()));
        ps.setString(3, doc.getFromUnits());
        ps.setString(4, doc.getOppdocNo());
        ps.setString(5, doc.getTitle());
        ps.setInt(6, doc.getCopies());
        ps.setInt(7, doc.getConfLevel());
        ps.setString(8, doc.getInstruct());
        ps.setString(9, doc.getSponsor());
        if(YHUtility.isNullorEmpty(reUserId)){
          ps.setInt(10, 0);
        }else{
          ps.setInt(10, Integer.parseInt(cont[i]));
        }
        ps.setInt(11, doc.getDocType());
        ps.setInt(12, 0);
        ps.setInt(13, doc.getSendStauts());
        ps.setInt(14, doc.getUserId());
        ps.setString(15, doc.getAttachNames());
        ps.setString(16, doc.getAttachIds());
        ps.addBatch();
      }
      ps.executeBatch();
      conn.commit();
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, null);
    }
  }
  
  /**
   * 分发收文
   * @param dbConn
   * @param seqId
   * @param userId 签收人id
   * @return
   * @throws SQLException
   */
  public int updateDocReceive(Connection dbConn, int seqId, int userId) throws SQLException{
    String sql = "update oa_officialdoc_recv set SEND_STATUS = 1,  RECE_USER_ID="+ userId +" where SEQ_ID=" + seqId;
    PreparedStatement ps = null;
    int k =0;
    try{
      ps = dbConn.prepareStatement(sql);
      k = ps.executeUpdate();
    } catch (SQLException e){      
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return k;
  }
  
  /**
   * 分发收文
   * @param dbConn
   * @param seqId
   * @return
   * @throws SQLException
   */
  public int updateDocReceive(Connection dbConn, int seqId, String runId, int status) throws SQLException{
    String sql = "update oa_officialdoc_recv set RUN_ID = "+ runId +",STATUS="+ status +"  where SEQ_ID=" + seqId;
    PreparedStatement ps = null;
    int k =0;
    try{
      ps = dbConn.prepareStatement(sql);
      k = ps.executeUpdate();
    } catch (SQLException e){      
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return k;
  }
  /**
   * 我的未(已)签收文

   * @param conn
   * @param doc
   * @param user
   * @throws Exception 
   */
  public List<YHDocReceive> myReadDocReceive1(Connection conn, YHPerson user, String status) throws Exception{
    String sql = " select  dr.SEQ_ID, dr.DOC_NO, dr.RES_DATE, dr.FROMUNITS, dr.OPPDOC_NO, dr.TITLE, dr.COPIES, dr.CONF_LEVEL, dr.INSTRUCT, dr.SPONSOR, " +
      " dr.RECE_USER_ID, dr.DOC_TYPE, dr.STATUS, dr.SEND_STATUS, dr.CREATE_USER_ID,P.USER_NAME ,dr.ATTACHNAME,dr.ATTACHID, dr.RUN_ID  , dr.REC_DOC_ID ,dr.REC_DOC_NAME " +
      " from oa_officialdoc_recv dr, PERSON P" ;
    if ("1".equals(status)) {
      sql += " where dr.CREATE_USER_ID = P.SEQ_ID  and dr.RECE_USER_ID = "+ user.getSeqId();
      sql += " and (dr.RUN_ID is null or dr.RUN_ID = '')";
    } else if ("2".equals(status)) {
      sql += " , oa_officialdoc_run  where dr.CREATE_USER_ID = P.SEQ_ID  and dr.RECE_USER_ID = "+ user.getSeqId() + " AND oa_officialdoc_run.RUN_ID = dr.RUN_ID" ;
      sql += " and (dr.RUN_ID is not null and dr.RUN_ID != '')  AND oa_officialdoc_run.END_TIME  is null";
    } else {
      sql += " , oa_officialdoc_run  where dr.CREATE_USER_ID = P.SEQ_ID  and dr.RECE_USER_ID = "+ user.getSeqId() + " AND oa_officialdoc_run.RUN_ID = dr.RUN_ID" ;
      sql += " and (dr.RUN_ID is not null and dr.RUN_ID != '')  AND oa_officialdoc_run.END_TIME  is not null";
    }
    sql += " order by dr.SEQ_ID desc"; //" and dr.SEND_STATUS = 1 and dr.STATUS =" + status;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHDocReceive> dosc = new ArrayList<YHDocReceive>();
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHDocReceive doc = new YHDocReceive();
        doc.setSeq_id(rs.getInt("SEQ_ID"));
        doc.setDocNo(rs.getString("DOC_NO"));
        doc.setResDate(rs.getDate("RES_DATE"));
        doc.setFromUnits(rs.getString("FROMUNITS"));
        doc.setOppdocNo(rs.getString("OPPDOC_NO"));
        doc.setTitle(rs.getString("TITLE"));
        doc.setCopies(rs.getInt("COPIES"));
        doc.setConfLevel(rs.getInt("CONF_LEVEL"));
        doc.setInstruct(rs.getString("INSTRUCT"));
        doc.setSponsor(rs.getString("SPONSOR"));
        doc.setRecipient(rs.getString("RECE_USER_ID"));
        doc.setDocType(rs.getInt("DOC_TYPE"));
        doc.setStatus(rs.getInt("STATUS"));
        doc.setSendStauts(rs.getInt("SEND_STATUS"));
        doc.setUserId(rs.getInt("CREATE_USER_ID"));
        doc.setFromUserName(rs.getString("USER_NAME"));
        doc.setAttachNames(rs.getString("ATTACHNAME"));
        doc.setAttachIds(rs.getString("ATTACHID"));
        doc.setRunId(rs.getString("RUN_ID"));
        doc.setNext(returnNext(conn, rs.getString("RUN_ID")));
        doc.setRecDocId(rs.getString("REC_DOC_ID"));
        doc.setRecDocName(rs.getString("REC_DOC_NAME"));
        dosc.add(doc);
      }
    } catch (SQLException e){
      throw e;
    }
    return dosc;
  }
  /**
   * 我的未(已)签收文
   * @param conn
   * @param doc
   * @param user
   * @throws Exception 
   */
  public List<YHDocReceive> myReadDocReceive(Connection conn, YHPerson user, int status ) throws Exception{
    String sql = " select  dr.SEQ_ID, dr.DOC_NO, dr.RES_DATE, dr.FROMUNITS, dr.OPPDOC_NO, dr.TITLE, dr.COPIES, dr.CONF_LEVEL, dr.INSTRUCT, dr.SPONSOR, " +
                         " dr.RECE_USER_ID, dr.DOC_TYPE, dr.STATUS, dr.SEND_STATUS, dr.CREATE_USER_ID,P.USER_NAME ,dr.ATTACHNAME,dr.ATTACHID, dr.RUN_ID " +
                         " from oa_officialdoc_recv dr, PERSON P  " +
                         "where dr.CREATE_USER_ID = P.SEQ_ID  and dr.RECE_USER_ID = "+ user.getSeqId() ;
    sql += " order by dr.SEQ_ID desc"; //" and dr.SEND_STATUS = 1 and dr.STATUS =" + status;
   
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHDocReceive> dosc = new ArrayList<YHDocReceive>();
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHDocReceive doc = new YHDocReceive();
        doc.setSeq_id(rs.getInt("SEQ_ID"));
        doc.setDocNo(rs.getString("DOC_NO"));
        doc.setResDate(rs.getDate("RES_DATE"));
        doc.setFromUnits(rs.getString("FROMUNITS"));
        doc.setOppdocNo(rs.getString("OPPDOC_NO"));
        doc.setTitle(rs.getString("TITLE"));
        doc.setCopies(rs.getInt("COPIES"));
        doc.setConfLevel(rs.getInt("CONF_LEVEL"));
        doc.setInstruct(rs.getString("INSTRUCT"));
        doc.setSponsor(rs.getString("SPONSOR"));
        doc.setRecipient(rs.getString("RECE_USER_ID"));
        doc.setDocType(rs.getInt("DOC_TYPE"));
        doc.setStatus(rs.getInt("STATUS"));
        doc.setSendStauts(rs.getInt("SEND_STATUS"));
        doc.setUserId(rs.getInt("CREATE_USER_ID"));
        doc.setFromUserName(rs.getString("USER_NAME"));
        doc.setAttachNames(rs.getString("ATTACHNAME"));
        doc.setAttachIds(rs.getString("ATTACHID"));
        doc.setRunId(rs.getString("RUN_ID"));
        doc.setNext(returnNext(conn, rs.getString("RUN_ID")));
        dosc.add(doc);
      }
    } catch (SQLException e){
      throw e;
    }
    return dosc;
  }
  
  /**
   * 分发收文
   * @param dbConn
   * @param seqId
   * @return
   * @throws SQLException
   */
  public int updateReadDocReceive(Connection dbConn, int seqId) throws SQLException{
    String sql = "update oa_officialdoc_recv set STATUS = 1 where SEQ_ID=" + seqId;
    PreparedStatement ps = null;
    int k =0;
    try{
      ps = dbConn.prepareStatement(sql);
      k = ps.executeUpdate();
    } catch (SQLException e){      
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return k;
  }

  /**
   * 
   * @param dbConn
   * @param user
   * @param seqId
   * @throws SQLException 
   */
  public void beanChConfirm(Connection dbConn,  String[] seqId, String[] userId) throws SQLException{
 /*   String sql = "update oa_officialdoc_recv set SEND_STATUS = 1, RECE_USER_ID=? where SEQ_ID=?";
    PreparedStatement ps = null;
    try{
      dbConn.setAutoCommit(false);
      for(int i=0; i<seqId.length; i++){
        ps = dbConn.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(userId[i]));
        ps.setInt(2, Integer.parseInt(seqId[i]));
        ps.addBatch();
      }
      ps.executeBatch();
      dbConn.commit();
    } catch (SQLException e){      
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }*/
    for(int i=0; i<seqId.length; i++){
      beanChConfirm(dbConn, seqId[i], userId[i]);
    }
  }
  
  public void beanChConfirm(Connection dbConn,  String seqId, String userId) throws SQLException{
    String sql = "update oa_officialdoc_recv set SEND_STATUS = 1, RECE_USER_ID=? where SEQ_ID=?";
    PreparedStatement ps = null;
    try{
        ps = dbConn.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(userId));
        ps.setInt(2, Integer.parseInt(seqId));
        int k = ps.executeUpdate();
    } catch (SQLException e){      
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    
  }

  /**
   * 查找一个doc
   * @param dbConn
   * @param i
   * @return
   * @throws Exception
   */
  public YHDocReceive beanChAlarm(Connection dbConn, int i) throws Exception{
    String sql = "select  dr.SEQ_ID, dr.DOC_NO, dr.RES_DATE, dr.RECE_USER_ID from oa_officialdoc_recv dr where dr.SEQ_ID=" + i;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
         YHDocReceive dos = new YHDocReceive();
         dos.setSeq_id(rs.getInt("SEQ_ID"));
         dos.setDocNo(rs.getString("DOC_NO"));
         dos.setRecipient(rs.getString("RECE_USER_ID"));
         return dos;
      }
    } catch (Exception e){      
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return null;
  }
  
  /**
   * 批量查找doc
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   * @throws Exception
   */
  public List<YHDocReceive> beanChAlarm(Connection dbConn, String[] seqId) throws Exception, Exception{
    List<YHDocReceive> docs = new ArrayList<YHDocReceive>();
    if(seqId.length > 0){
       for(int i=0; i<seqId.length; i++){
         YHDocReceive doc =  beanChAlarm(dbConn, Integer.parseInt(seqId[i]));
         docs.add(doc);
       }
       return docs;
    }
    return null;
  }
  
  /**
   * 处理上传附件，返回附件id，附件名称


  //点击单文件上传时调用的方法

   * @param request  HttpServletRequest
   * @param 
   * @return Map<String, String> ==> {id = 文件名}
   * @throws Exception 
   */
  public Map<String, String> fileUploadLogic(YHFileUploadForm fileForm,
      String pathPx) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    String filePath = pathPx;
    try {
      Calendar cld = Calendar.getInstance();
      int year = cld.get(Calendar.YEAR) % 100;
      int month = cld.get(Calendar.MONTH) + 1;
      String mon = month >= 10 ? month + "" : "0" + month;
      String hard = year + mon;
      Iterator<String> iKeys = fileForm.iterateFileFields();
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName).replaceAll("\\'", "");
        String fileNameV = fileName;
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        String rand = YHDiaryUtil.getRondom();
        fileName = rand + "_" + fileName;
        
        while (YHDiaryUtil.getExist(filePath + File.separator+ hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + fileName;
        }
        result.put(hard + "_" + rand, fileNameV);
        fileForm.saveFile(fieldName, filePath + File.separator+ YHWorkFlowConst.MODULE  + File.separator+ hard  + File.separator+ fileName);
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  
//多文件上传时用到的----
  /**
   * 浮动菜单文件删除
   * 
   * @param dbConn
   * @param attId
   * @param attName
   * @param contentId
   * @throws Exception
   */
  
  public boolean delFloatFile(Connection dbConn, String attId, String attName, int seqId) throws Exception {
    boolean updateFlag = false;
    if (seqId != 0) {
      YHORM orm = new YHORM();
      YHDocReceive news = faDoc(dbConn,  seqId);
      String[] attIdArray = {};
      String[] attNameArray = {};
      String attachmentId = news.getAttachIds();
      String attachmentName = news.getAttachNames();
      //YHOut.println("attachmentId"+attachmentId+"--------attachmentName"+attachmentName);
      if (!"".equals(attachmentId.trim()) && attachmentId != null && attachmentName != null) {
        attIdArray = attachmentId.trim().split(",");
        attNameArray = attachmentName.trim().split("\\*");
      }
      String attaId = "";
      String attaName = "";
  
      for (int i = 0; i < attIdArray.length; i++) {
        if (attId.equals(attIdArray[i])) {
          continue;
        }
        attaId += attIdArray[i] + ",";
        attaName += attNameArray[i] + "*";
      }
      //YHOut.println("attaId=="+attaId+"--------attaName=="+attaName);
      news.setAttachIds(attaId.trim());
      news.setAttachNames(attaName.trim());
     // orm.updateSingle(dbConn, news);
      updateSingle(dbConn, news);
      
    }
  //处理文件
    String[] tmp = attId.split("_");
    String path = YHDocConst.filePath  + File.separator+ tmp[0]  + File.separator+ tmp[1] + "_" + attName;
    File file = new File(path);
    if(file.exists()){
      file.delete();
    } else {
      //兼容老的数据
      String path2 = YHDocConst.filePath  + File.separator+ tmp[0]  + File.separator+tmp[1] + "." + attName;
      File file2 = new File(path2);
      if(file2.exists()){
        file2.delete();
      }
    }
    updateFlag=true;
    return updateFlag;
  }
  public void updateSingle(Connection dbConn, YHDocReceive news) throws SQLException{
    String sql = "update oa_officialdoc_recv set ATTACHID ='"+ news.getAttachIds() +"',ATTACHNAME='"+ news.getAttachNames() +"' where SEQ_ID="+ news.getSeq_id();
    PreparedStatement ps = null;
    try{
        ps = dbConn.prepareStatement(sql);
        ps.executeUpdate();
    } catch (SQLException e){      
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  /**
   * 工作流
   * @param conn
   * @param doc
   * @param user
   * @throws Exception 
   */
  public String myDocReceiveJson(Connection conn, YHPerson user, int seqId   , String webroot) throws Exception{
    String sql = " select  dr.SEQ_ID, dr.DOC_NO, dr.RES_DATE, dr.FROMUNITS, dr.OPPDOC_NO, dr.TITLE, dr.COPIES, dr.CONF_LEVEL, dr.INSTRUCT, dr.SPONSOR, " +
                         " dr.RECE_USER_ID, dr.DOC_TYPE, dr.STATUS, dr.SEND_STATUS, dr.CREATE_USER_ID,P.USER_NAME ,dr.ATTACHNAME,dr.ATTACHID, dr.RUN_ID " +
                         " from oa_officialdoc_recv dr, PERSON P  " +
                         " where dr.CREATE_USER_ID = P.SEQ_ID  and dr.SEQ_ID="+ seqId; //" and dr.SEND_STATUS = 1 and dr.STATUS =" + status;
   
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        YHDocReceive doc = new YHDocReceive();
        doc.setSeq_id(rs.getInt("SEQ_ID"));
        doc.setDocNo(rs.getString("DOC_NO"));
        doc.setResDate(rs.getDate("RES_DATE"));
        doc.setFromUnits(rs.getString("FROMUNITS"));
        doc.setOppdocNo(rs.getString("OPPDOC_NO"));
        doc.setTitle(rs.getString("TITLE"));
        doc.setCopies(rs.getInt("COPIES"));
        doc.setConfLevel(rs.getInt("CONF_LEVEL"));
        doc.setInstruct(rs.getString("INSTRUCT"));
        doc.setSponsor(rs.getString("SPONSOR"));
        doc.setRecipient(rs.getString("RECE_USER_ID"));
        doc.setDocType(rs.getInt("DOC_TYPE"));
        doc.setStatus(rs.getInt("STATUS"));
        doc.setSendStauts(rs.getInt("SEND_STATUS"));
        doc.setUserId(rs.getInt("CREATE_USER_ID"));
        doc.setFromUserName(rs.getString("USER_NAME"));
        doc.setAttachNames(rs.getString("ATTACHNAME"));
        doc.setAttachIds(rs.getString("ATTACHID"));
        doc.setToUserName(user.getUserName());
        doc.setRunId(rs.getString("RUN_ID"));
        doc.setNext(returnNext(conn, rs.getString("RUN_ID")));
        return doc.toJson(webroot);
      }
    } catch (SQLException e){
      throw e;
    }
    return null;
  }
  
  /**
   * 
   * @param conn
   * @param runId
   * @return
   * @throws Exception
   */
  public String getFlowId(Connection conn, String runId) throws Exception{
    String sql = "select FLOW_ID, RUN_ID, END_TIME from oa_officialdoc_run where run_id="+ runId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getString("flow_id");
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return null;
  }
  
  public boolean getEndTime(Connection conn, String runId) throws Exception{
    String sql = "select FLOW_ID, RUN_ID, END_TIME from oa_officialdoc_run where run_id="+ runId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next() && rs.getDate("END_TIME") != null){
        return true;
      } 
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return false;
  }
  
  public String getMaxProcsId(Connection conn, String runId)throws Exception{
    String sql = "select max(PRCS_ID) as prcdId from oa_officialdoc_fl_run_prcs where run_id="+ runId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getString("prcdId");
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return null;
  }
  
  public String getFlowPrcs(Connection conn, String prcsId) throws Exception{
    String sql = "select FLOW_PRCS from oa_officialdoc_fl_run_prcs where PRCS_ID="+ prcsId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getString("FLOW_PRCS");
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return null;
  }
  
  public String getPrcsName(Connection conn, String flowPrcs, String flowId)throws Exception{
    String sql = "select PRCS_NAME from oa_officialdoc_fl_process where PRCS_ID = "+ flowPrcs +" and FLOW_SEQ_ID ="+ flowId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getString("PRCS_NAME");
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return null;
  }
  
  public YHDocNext returnNext(Connection conn, String runId)throws Exception{
    try{
      YHDocNext next = new YHDocNext();
      if(YHUtility.isNullorEmpty(runId)){
        next.setPrcsName("未办理");
        return next;
      }
      String flowId = getFlowId( conn,  runId);
      boolean endtime = getEndTime(conn, runId);
      String maxprcsId = getMaxProcsId(conn, runId);
      String flowPrcs = getFlowPrcs(conn, maxprcsId);
      String getPrcsName = getPrcsName(conn, flowPrcs, flowId);
      next.setFlowId(flowId);
      next.setRunId(runId);
      next.setPrcsName(getPrcsName);
      next.setFlowPrcs(flowPrcs);
      if(endtime){
        next.setPrcsName("已办结");
        return next;
      }
      return next;
    } catch (Exception e){
      throw e;
    }  
  }
  /**
   * 获得最大的收文编号
   * @param dbConn
   * @param typeId
   * @return
   * @throws Exception
   */
  public List<Integer> getMaxOrderNo(Connection dbConn,  int typeId) throws Exception{
    String sql = "select  dr.DOC_NO from oa_officialdoc_recv dr where dr.DOC_TYPE=" + typeId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<Integer> ints = new ArrayList<Integer>();
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        ints.add(rs.getInt("DOC_NO"));
      }
    } catch (Exception e){      
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return ints;
  }
  
  public int getMaxOrderNo(List<Integer> ints){
    if(ints == null || ints.size() == 0){
      return 0;
    }
    int max = Collections.max(ints);
    return max;
  }
  
  /**
   * 修改收文时调用
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHDocReceive getAdocById(Connection dbConn,  int seqId) throws Exception{
    return faDoc(dbConn, seqId);
  }
  
  /**
   *更新数据 
   * @param dbConn
   * @param doc
   * @throws Exception 
   */
  public void update(Connection conn,  YHDocReceive doc) throws Exception{
    String sql = "update oa_officialdoc_recv " 
    		             +" set DOC_NO = ?,"
                     +" FROMUNITS = ?,"
                     +" OPPDOC_NO = ?,"
                     +" TITLE = ?,"
                     +" COPIES = ?,"
                     +" CONF_LEVEL = ?,"
                     +" INSTRUCT = ?,"
                     +" SPONSOR = ?,"
                     +" DOC_TYPE = ?,"
                     +" SEND_STATUS = ?,"
                     +" ATTACHNAME = ?,"
                     +" ATTACHID = ?"
                    +" where seq_id = ? ";
      PreparedStatement ps = null;
      try{
        ps = conn.prepareStatement(sql);
        ps.setString(1, doc.getDocNo());
        ps.setString(2, doc.getFromUnits());
        ps.setString(3, doc.getOppdocNo());
        ps.setString(4, doc.getTitle());
        ps.setInt(5, doc.getCopies());
        ps.setInt(6, doc.getConfLevel());
        ps.setString(7, doc.getInstruct());
        ps.setString(8, doc.getSponsor());
        ps.setInt(9, doc.getDocType());
        ps.setInt(10, doc.getSendStauts());
        ps.setString(12, doc.getAttachIds());
        ps.setString(11, doc.getAttachNames());
        ps.setInt(13, doc.getSeq_id());
        int k = ps.executeUpdate();
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, null, null);
      }
  }
  
  /**
   *更新数据 
   * @param dbConn
   * @param doc
   * @throws Exception 
   */
  public void updateStatus(Connection conn,  int  seqId) throws Exception{
    String sql = "update oa_officialdoc_recv " 
                     +" set "
                     +" RECE_USER_ID = ?,"
                     +" SEND_STATUS = ?,"
                     +" RUN_ID = ?,"
                     +" STATUS = ?"
                    +" where seq_id = ? ";
      PreparedStatement ps = null;
      try{
        ps = conn.prepareStatement(sql);
        ps.setInt(1, 0);
        ps.setInt(2, 0);
        ps.setString(3, null);
        ps.setInt(4, 0);
        ps.setInt(5, seqId);
        int k = ps.executeUpdate();
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, null, null);
      }
  }
  /**
   * 打印单个
   * @return
   * @throws Exception 
   */
  public YHDocReceive siglePrint(Connection conn,  int seqId) throws Exception{
    return this.faDoc(conn, seqId);
  }
  
  /**
   * 批量打印
   * @param conn
   * @param ids
   * @return
   * @throws Exception
   */
  public List<YHDocReceive> printDocs(Connection conn,  String ids) throws Exception{
    String sql = " select dr.SEQ_ID, dr.DOC_NO, dr.RES_DATE, dr.FROMUNITS, dr.OPPDOC_NO, dr.TITLE, dr.COPIES, dr.CONF_LEVEL, dr.INSTRUCT, dr.SPONSOR, " +
                         " dr.RECE_USER_ID, dr.DOC_TYPE,dr.STATUS, dr.SEND_STATUS, dr.CREATE_USER_ID,dr.ATTACHNAME,dr.ATTACHID,dr.RUN_ID from oa_officialdoc_recv dr   " +
                 " where  dr.SEQ_ID in ("+ ids +") order by dr.SEQ_ID desc";
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHDocReceive> dosc = new ArrayList<YHDocReceive>();
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHDocReceive doc = new YHDocReceive();
        doc.setSeq_id(rs.getInt("SEQ_ID"));
        doc.setDocNo(rs.getString("DOC_NO"));
        doc.setResDate(rs.getDate("RES_DATE"));
        doc.setFromUnits(rs.getString("FROMUNITS"));
        doc.setOppdocNo(rs.getString("OPPDOC_NO"));
        doc.setTitle(rs.getString("TITLE"));
        doc.setCopies(rs.getInt("COPIES"));
        doc.setConfLevel(rs.getInt("CONF_LEVEL"));
        doc.setInstruct(rs.getString("INSTRUCT"));
        doc.setSponsor(rs.getString("SPONSOR"));
        int deptId = 0;
        try {
          deptId = Integer.parseInt(doc.getSponsor());
        }catch(Exception ex) {          
        }
        doc.setSponsorName(getDeptName(conn, deptId));
        doc.setRecipient(rs.getString("RECE_USER_ID"));
        doc.setToUserName(getUserName(conn, rs.getInt("RECE_USER_ID")));
        doc.setDocType(rs.getInt("DOC_TYPE"));
        doc.setStatus(rs.getInt("STATUS"));
        doc.setSendStauts(rs.getInt("SEND_STATUS"));
        doc.setUserId(rs.getInt("CREATE_USER_ID"));
        doc.setAttachNames(rs.getString("ATTACHNAME"));
        doc.setAttachIds(rs.getString("ATTACHID"));
        doc.setRunId(rs.getString("RUN_ID"));
        dosc.add(doc);
      }
    } catch (SQLException e){
      throw e;
    }
    return dosc;
  }
  
  /**
   * 获得需要催办的人员的id
   * @param conn
   * @param seqId
   * @param prcsId
   * @param runId
   * @return
   * @throws Exception
   */
  public List<String> cuiBanUser(Connection conn,  int seqId, int prcsId, int runId) throws Exception{
    try{
      String sql = "select  USER_ID from oa_fl_run_prcs where PRCS_ID = "+ prcsId +" and RUN_ID = "+ runId +" and (PRCS_FLAG = 1 or PRCS_FLAG = 2)";
      PreparedStatement ps = null;
      ResultSet rs = null;
      List<String> useIds = new ArrayList<String>();
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        useIds.add(rs.getString("USER_ID"));
      }
      return useIds;
    } catch (Exception e){
      throw e;
    }
  }
  
  public List<String> cuiBanUser(Connection conn,  int seqId,  int runId) throws Exception{
    String prcsId = getMaxProcsId(conn, String.valueOf(runId));
    return cuiBanUser(conn, seqId, Integer.parseInt(prcsId), runId);
  }
  
  public static void main(String[] args){
    List<Integer> list = new ArrayList<Integer>();
    list.add(1);
    list.add(3);
    list.add(2);
    list.add(100);
    list.add(39);
    int k = Collections.max(list);
    System.out.print(k);
   
  }

  
}
