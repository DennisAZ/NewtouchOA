package yh.subsys.oa.hr.manage.staff_contract.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.news.data.YHNews;
import yh.core.funcs.news.data.YHNewsCont;
import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;

import yh.subsys.oa.hr.manage.staff_contract.data.YHDocConst;
import yh.subsys.oa.hr.manage.staff_contract.data.YHDocReceive;

/**
 * 收文管理
 * @author Administrator
 *
 */
public class YHPublicFileUploadLogic{
  /**
   * 我的未(已)发收文
   * @param conn
   * @param doc
   * @param user
   * @throws SQLException 
   */
  public List<YHDocReceive> faDocReceive(Connection conn, YHPerson user, int sendStatus) throws SQLException{
    String sql = " select dr.SEQ_ID, dr.DOC_NO, dr.RES_DATE, dr.FROMUNITS, dr.OPPDOC_NO, dr.TITLE, dr.COPIES, dr.CONF_LEVEL, dr.INSTRUCT, dr.SPONSOR, " +
    		                 " dr.RECE_USER_ID, dr.DOC_TYPE,dr.STATUS, dr.SEND_STATUS, dr.CREATE_USER_ID,dr.ATTACHNAME,dr.ATTACHID,  P.USER_NAME from oa_officialdoc_recv dr, Person p  " +
    		                 "where dr.RECE_USER_ID = P.SEQ_ID and dr.CREATE_USER_ID = "+ user.getSeqId() +" and dr.SEND_STATUS = " + sendStatus;
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
        doc.setOppDocNo(rs.getString("OPPDOC_NO"));
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
        doc.setToUserName(rs.getString("USER_NAME"));
        doc.setAttachNames(rs.getString("ATTACHNAME"));
        doc.setAttachIds(rs.getString("ATTACHID"));
        dosc.add(doc);
      }
    } catch (SQLException e){
      throw e;
    }
    return dosc;
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
      String[] cont = doc.getRecipient().split(",");
      int cnt = cont.length;
      for(int i=0; i<cnt; i++){
        ps.setString(1, doc.getDocNo());
        ps.setDate(2, new Date(doc.getResDate().getTime()));
        ps.setString(3, doc.getFromUnits());
        ps.setString(4, doc.getOppDocNo());
        ps.setString(5, doc.getTitle());
        ps.setInt(6, doc.getCopies());
        ps.setInt(7, doc.getConfLevel());
        ps.setString(8, doc.getInstruct());
        ps.setString(9, doc.getSponsor());
        ps.setInt(10, Integer.parseInt(cont[i]));
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
   * @return
   * @throws SQLException
   */
  public int updateDocReceive(Connection dbConn, int seqId) throws SQLException{
    String sql = "update oa_officialdoc_recv set SEND_STATUS = 1 where SEQ_ID=" + seqId;
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
   * @throws SQLException 
   */
  public List<YHDocReceive> myReadDocReceive(Connection conn, YHPerson user, int status) throws SQLException{
    String sql = " select  dr.SEQ_ID, dr.DOC_NO, dr.RES_DATE, dr.FROMUNITS, dr.OPPDOC_NO, dr.TITLE, dr.COPIES, dr.CONF_LEVEL, dr.INSTRUCT, dr.SPONSOR, " +
                         " dr.RECE_USER_ID, dr.DOC_TYPE, dr.STATUS, dr.SEND_STATUS, dr.CREATE_USER_ID,P.USER_NAME ,dr.ATTACHNAME,dr.ATTACHID " +
                         " from oa_officialdoc_recv dr, PERSON P  " +
                         "where dr.CREATE_USER_ID = P.SEQ_ID and dr.RECE_USER_ID = "+ user.getSeqId(); //" and dr.SEND_STATUS = 1 and dr.STATUS =" + status;
   
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
        doc.setOppDocNo(rs.getString("OPPDOC_NO"));
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
  public void beanChConfirm(Connection dbConn, YHPerson user, String[] seqId) throws SQLException{
    String sql = "update oa_officialdoc_recv set SEND_STATUS = 1 where SEQ_ID=?";
    PreparedStatement ps = null;
    try{
      dbConn.setAutoCommit(false);
      for(int i=0; i<seqId.length; i++){
        ps = dbConn.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(seqId[i]));
        ps.addBatch();
      }
      ps.executeBatch();
      dbConn.commit();
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
        
        while (YHDiaryUtil.getExist(filePath + File.separator + hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + fileName;
        }
        result.put(hard + "_" + rand, fileNameV);
        fileForm.saveFile(fieldName, filePath + File.separator + YHDocConst.MODULE + File.separator + hard + File.separator + fileName);
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
      YHNews news = (YHNews)orm.loadObjSingle(dbConn, YHNews.class, seqId);
      String[] attIdArray = {};
      String[] attNameArray = {};
      String attachmentId = news.getAttachmentId();
      String attachmentName = news.getAttachmentName();
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
      news.setAttachmentId(attaId.trim());
      news.setAttachmentName(attaName.trim());
      orm.updateSingle(dbConn, news);
    }
  //处理文件
    String[] tmp = attId.split("_");
    String path = YHDocConst.filePath + File.separator  + tmp[0] + File.separator + tmp[1] + "_" + attName;
    File file = new File(path);
    if(file.exists()){
      file.delete();
    } else {
      //兼容老的数据
      String path2 = YHDocConst.filePath + File.separator  + tmp[0] + File.separator + tmp[1] + "." + attName;
      File file2 = new File(path2);
      if(file2.exists()){
        file2.delete();
      }
    }
    updateFlag=true;
    return updateFlag;
  }
}
