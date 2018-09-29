package yh.core.funcs.email.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;

import yh.core.data.YHDataSources;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.email.data.YHEmail;
import yh.core.funcs.email.data.YHEmailBody;
import yh.core.funcs.email.data.YHEmailCont;
import yh.core.funcs.email.data.YHWebmail;
import yh.core.funcs.email.data.YHWebmailBody;
import yh.core.funcs.email.util.YHMailPop3Util;
import yh.core.funcs.email.util.YHMailSmtpUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHSysProps;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

/**
 * 
 * @author Think
 * 
 */
public class YHWebmailLogic {
  private static Logger log = Logger.getLogger(YHWebmailLogic.class);
  /**
   * 根据用户名取得用户的外部邮件设置信息
   * 
   * @param personId
   * @return
   * @throws Exception
   */
  public List<YHWebmail> getWebMailByUserId(Connection conn, int personId)
      throws Exception {
    List<YHWebmail> webList = null;
    YHORM orm = new YHORM();
    webList = orm.loadListSingle(conn, YHWebmail.class,
        new String[] { "USER_ID=" + personId });
    return webList;
  }

  /**
   * 取得外部邮件
   * 
   * @param conn
   * @throws Exception
   */
  public void loadWebMail() throws Exception {
    loadWebMail(YHEmailCont.UPLOAD_HOME);
  }

  /**
   * 根据单个用户取得外部邮件
   * 
   * @param conn
   * @throws Exception
   */
  private void loadWebMail(Connection conn, int personId) throws Exception {
    loadWebMail(conn, YHEmailCont.UPLOAD_HOME, personId);
  }

  /**
   * 取得外部邮件
   * 
   * @param conn
   * @param attachPath
   * @throws Exception
   * @throws Exception
   */
  private void loadWebMail(String attachPath) throws Exception {
    List<YHWebmail> wms = null;
    YHORM orm = new YHORM();
    Map hash = null;
    Connection dbConn = null;
    try {
      dbConn = YHDataSources.getDataSource(YHSysProps.getSysDbDsName()).getConnection();
      wms = (List<YHWebmail>) orm.loadListSingle(dbConn, YHWebmail.class, hash);
    }catch(Exception ex) {
    }finally {
      YHDBUtility.closeDbConn(dbConn, log);
    }
    if(wms != null){
    	for (YHWebmail wm : wms) {
    		if(!"1".equals(wm.getCheckFlag())){
    			continue;
    		}
    		try {
    			loadWebMail(wm, attachPath, orm);
    		} catch (Exception e) {
    			e.printStackTrace();
    			log.debug(e.getMessage(), e);
    		}
    	}
    }
  }

  /**
   * 根据单个用户取得外部邮件
   * 
   * @param conn
   * @param attachPath
   * @param personId
   * @throws Exception
   */
  private void loadWebMail(Connection conn, String attachPath, int personId)
      throws Exception {
    List<YHWebmail> wms = null;
    YHORM orm = new YHORM();
    Map hash = null;
    wms = getWebMailByUserId(conn, personId);
    for (YHWebmail wm : wms) {
      try {
        loadWebMail(wm, attachPath, orm);
      } catch (Exception e) {
        log.debug(e.getMessage(), e);
      }
    }
  }

  /**
   * 取得外部邮件
   * 
   * @param conn
   * @param wm
   * @param attachPath
   * @throws Exception
   */
  public void loadWebMail(YHWebmail wm, String attachPath,
      YHORM orm) throws Exception {
    ArrayList<YHWebmailBody> webmailbodys = null;
    boolean flag = true;
    int loopCnt = 0;
    int maxLoopCnt = 5;
    while (flag && loopCnt < maxLoopCnt) {
      webmailbodys = null;
      try {
        webmailbodys = YHMailPop3Util.getWebMailBody(wm, attachPath, 10);
      }catch(Exception ex) {
        try {
          Thread.sleep(1000);
        }catch(Exception ex2) {
        }
        loopCnt++;
        continue;
      }
      Connection dbConn = null;
      try {
        dbConn = YHDataSources.getDataSource(YHSysProps.getSysDbDsName()).getConnection();
        for (int i = 0; i < webmailbodys.size(); i++) {
          try {
            YHWebmailBody wmb = webmailbodys.get(i);
            if (wmb == null) {
              continue;
            }
            saveWebMail2Local(dbConn, wmb, wm, orm);
            dbConn.commit();
          } catch (Exception e) {
            try {
              dbConn.rollback();
            }catch(Exception ex2) {          
            }
            log.debug(e.getMessage(), e);
          }
        }
      }catch(Exception ex) {
      }finally {
        YHDBUtility.closeDbConn(dbConn, log);
      }
      
      if (webmailbodys.size() < 1) {
        flag = false;
      } else {
        flag = true;
      }
      if (flag) {
        try {
          Thread.sleep(1000);
        }catch(Exception ex) {
        }
      }
      loopCnt++;
    }
  }

  /**
   * 
   * @param conn
   * @param subject
   * @param emailBodyId
   * @param userId
   * @param emailId
   * @param ids
   * @return
   * @throws Exception
   */
  public boolean smsRmind(Connection conn, String subject, int emailBodyId,
      int userId, int emailId, String ids) throws Exception {
    subject = " 您收到一份外部邮件！主题：" + subject;
    String remindUrl = "/core/funcs/email/webbox/read_email/index.jsp?mailId="
        + emailId + "&seqId=" + emailBodyId;
    YHSmsBack sb = new YHSmsBack();
    sb.setFromId(userId);
    sb.setContent(subject);
    sb.setSmsType("2");
    sb.setRemindUrl(remindUrl);
    sb.setToId(ids);
    YHSmsUtil.smsBack(conn, sb);
    return false;
  }

  /**
   * 删除已经保存的内部邮件
   * @param conn
   * @return
   * @throws SQLException
   */
  private void delInnerEmail(Connection conn, int bodyId) throws Exception {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String sql = "delete form EMAIL where BODY_ID=" + bodyId;
      stmt.executeUpdate(sql);
      sql = "delete form oa_email_body where SEQ_ID=" + bodyId;
      stmt.executeUpdate(sql);
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, null);
    }
  }
  /**
   * 保存外部邮件到本地
   * 
   * @param conn
   * @param wmb
   * @throws Exception
   */
  public void saveWebMail2Local(Connection conn, YHWebmailBody wmb,
      YHWebmail wm, YHORM orm) throws Exception {
    int bodyId = 0;
    //保存内部邮件到 EMAIL_BODY
    bodyId = saveWemailBodyByWebmail(conn, wmb, wm, orm);// 保存邮件正文
    wmb.setBodyId(bodyId);
    //保存内部邮件到 EMAIL
    YHEmail em = new YHEmail();
    em.setBodyId(bodyId);
    em.setBoxId(0);
    em.setToId(String.valueOf(wm.getUserId()));
    orm.saveSingle(conn, em);
    YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
    int emailId = emul.getBodyId(conn, "oa_email");
    //保存到WEBMAIL_BODY
    try {
      orm.saveSingle(conn, wmb);
      smsRmind(conn, wmb.getSubject(), bodyId, 1, emailId, String.valueOf(wm.getUserId()));
    }catch(Exception ex) {
      delInnerEmail(conn, bodyId);
    }
  }

  /**
   * 保存外部邮件到本地
   * 
   * @param conn
   * @param wmb
   * @throws Exception
   */
  public void updateWebMail2Local(Connection conn, YHWebmailBody wmb,
      YHWebmail wm, YHORM orm, YHEmailBody eb) throws Exception {
    int bodyId = 0;
    bodyId = updateWemailBodyByWebmail(conn, wmb, wm, orm, eb);// 保存邮件正文
    wmb.setBodyId(bodyId);
    orm.updateSingle(conn, wmb);
  }

  /**
   * @throws Exception
   * 
   */
  public int saveWemailBodyByWebmail(Connection conn, YHWebmailBody wmb,
      YHWebmail wm, YHORM orm) throws Exception {
    YHEmailBody eb = new YHEmailBody();
    String attachmentId = wmb.getAttachmentId();
    String attachmentName = wmb.getAttachmentName();
    String webmailContent = wmb.getContentHtml();
    String compressContent = YHDiaryUtil.cutHtml(webmailContent);
    String fromWebmail = wmb.getFromMail();
    
    eb.setAttachmentId(attachmentId);
    eb.setAttachmentName(attachmentName);
    eb.setCompressContent(compressContent);
    eb.setWebmailContent(webmailContent);
    eb.setSubject(wmb.getSubject());
    eb.setSendTime(wmb.getSendDate());
    eb.setSendFlag("1");
    eb.setFromId(-2);// 收件人为外部邮箱，所以不在yh系统中存在用户
    eb.setToId(String.valueOf(wm.getUserId()));
    eb.setFromWebmail(fromWebmail);
    eb.setFromWebmailId(String.valueOf(wm.getSeqId()));
    eb.setToWebmail(wmb.getToMail());
    eb.setIsWebmail("1");
    eb.setEnsize(0);

    orm.saveSingle(conn, eb);
    YHInnerEMailUtilLogic iem = new YHInnerEMailUtilLogic();
    int bodyId = iem.getBodyId(conn);
    return bodyId;
  }

  /**
   * update
   * 
   * @param conn
   * @param wmb
   * @param wm
   * @param orm
   * @return
   * @throws Exception
   */
  public int updateWemailBodyByWebmail(Connection conn, YHWebmailBody wmb,
      YHWebmail wm, YHORM orm, YHEmailBody eb) throws Exception {
    String attachmentId = wmb.getAttachmentId();
    String attachmentName = wmb.getAttachmentName();
    String webmailContent = wmb.getContentHtml();
    String compressContent = YHDiaryUtil.cutHtml(webmailContent);
    String fromWebmail = wmb.getFromMail();
    eb.setAttachmentId(attachmentId);
    eb.setAttachmentName(attachmentName);
    eb.setCompressContent(compressContent);
    eb.setWebmailContent(webmailContent);
    eb.setSubject(wmb.getSubject());
    eb.setSendTime(wmb.getSendDate());
    eb.setSendFlag("1");
    eb.setFromId(-2);// 收件人为外部邮箱，所以不在yh系统中存在用户
    eb.setToId(String.valueOf(wm.getUserId()));
    eb.setFromWebmail(fromWebmail);
    eb.setFromWebmailId(String.valueOf(wm.getSeqId()));
    eb.setToWebmail(wmb.getToMail());
    eb.setIsWebmail("1");
    eb.setEnsize(0);
    orm.updateSingle(conn, eb);
    return eb.getSeqId();
  }

  /**
   * 
   * @param conn
   * @param wmb
   * @param wm
   * @param orm
   * @param eb
   * @return
   * @throws Exception
   */
  public int updateWemailBodyByWebmailForAtt(Connection conn,
      YHWebmailBody wmb, YHWebmail wm, YHORM orm, YHEmailBody eb)
      throws Exception {
    String attachmentId = wmb.getAttachmentId();
    String attachmentName = wmb.getAttachmentName();
    eb.setAttachmentId(attachmentId);
    eb.setAttachmentName(attachmentName);
    orm.updateSingle(conn, eb);
    return eb.getSeqId();
  }

  /**
   * 
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public ArrayList<YHWebmail> getWebmailInfo(Connection conn, int userId)
      throws Exception {
    YHORM orm = new YHORM();
    ArrayList<YHWebmail> wms = (ArrayList<YHWebmail>) orm.loadListSingle(conn,
        YHWebmail.class, new String[] { "USER_ID=" + userId });
    return wms;
  }

  /**
   * 设置网络邮箱的信息
   * 
   * @param conn
   * @param request
   * @param person
   * @throws Exception
   */
  public String setWebmail(Connection conn, Map request, YHPerson person)
      throws Exception {
    YHWebmail wm = (YHWebmail) YHFOM.build(request, YHWebmail.class, null);
    String[] pop3Ssls = (String[])request.get("pop3Ssl");
    String pop3Ssl = "0" ;
    if (pop3Ssls != null && pop3Ssls.length > 0) {
      pop3Ssl = pop3Ssls[0];
    } 
    String[] smtpSsls = (String[])request.get("smtpSsl");
    String smtpSsl = "0" ;
    if (smtpSsls != null && smtpSsls.length > 0) {
      smtpSsl = smtpSsls[0];
    } 
    wm.setPop3Ssl(pop3Ssl);
    wm.setSmtpSsl(smtpSsl);
    wm.setUserId(person.getSeqId());
    YHORM orm = new YHORM();
    orm.saveSingle(conn, wm);
    
    
    Statement st = null;
    ResultSet rs = null;
    String seqId = "";
    try {
      String sql = " select max(SEQ_ID) SEQ_ID from oa_internet_mail ";
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      if(rs.next()){
        seqId = rs.getString("SEQ_ID");
      }
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      YHDBUtility.close(st, rs, null);
    }
    return seqId;
  }

  /**
   * 
   * @param conn
   * @param request
   * @param person
   * @throws Exception
   */
  public void updateWebmail(Connection conn, Map request, int userId)
      throws Exception {
    YHWebmail wm = (YHWebmail) YHFOM.build(request, YHWebmail.class, null);
    String[] pop3Ssls = (String[])request.get("pop3Ssl");
    String pop3Ssl = "0" ;
    if (pop3Ssls != null && pop3Ssls.length > 0) {
      pop3Ssl = pop3Ssls[0];
    } 
    String[] smtpSsls = (String[])request.get("smtpSsl");
    String smtpSsl = "0" ;
    if (smtpSsls != null && smtpSsls.length > 0) {
      smtpSsl = smtpSsls[0];
    } 
    wm.setPop3Ssl(pop3Ssl);
    wm.setSmtpSsl(smtpSsl);
    wm.setUserId(userId);
    YHORM orm = new YHORM();
    orm.updateSingle(conn, wm);
  }

  /**
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String listWebmail(Connection conn, int userId) throws Exception {
    String sql = "select seq_id,email, is_default from oa_internet_mail where USER_ID=" + userId;
    StringBuffer sb = new StringBuffer("[");
    StringBuffer temp = new StringBuffer();
    Statement st = null;
    ResultSet rs = null;
    try {
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      while (rs.next()) {
        int seqId = rs.getInt(1);
        String email = rs.getString(2);
        String isDefault = rs.getString(3) == null ? "0" : rs.getString(3)  ;
        if (!"".equals(temp.toString())) {
          temp.append(",");
        }
        temp.append("{seqId:").append(seqId).append(",")
         .append("isDefault:\"").append(isDefault).append("\",")
         .append("email:\"").append(email).append("\"}");
      }
    } catch (Exception e) {
    } finally {
      YHDBUtility.close(st, rs, null);
    }
    sb.append(temp).append("]");
    return sb.toString();
  }

  /**
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String getWebmail(Connection conn, int seqId) throws Exception {
    YHWebmail wm = null;
    YHORM orm = new YHORM();
    wm = (YHWebmail) orm.loadObjSingle(conn, YHWebmail.class, seqId);
    StringBuffer sb = YHFOM.toJson(wm);
    return sb.toString();
  }

  /**
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHWebmail getWebmailById(Connection conn, int seqId) throws Exception {
    YHWebmail wm = null;
    YHORM orm = new YHORM();
    wm = (YHWebmail) orm.loadObjSingle(conn, YHWebmail.class, seqId);
    return wm;
  }

  /**
   * 删除邮件信息
   * 
   * @param conn
   * @param seqId
   * @throws Exception
   */
  public void deletWebmail(Connection conn, int seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(conn, YHWebmail.class, seqId);
  }

  /**
   * 发送外部邮件
   * 
   * @param conn
   * @param eb
   * @param bodyId
   * @throws Exception
   * @throws NumberFormatException
   */
  public void sendWebMail(Connection conn, YHEmailBody eb, int bodyId)
      throws NumberFormatException, Exception {
    YHWebmailBody wmb = new YHWebmailBody();
    String fromWebmailId = eb.getFromWebmailId();
    YHORM orm = new YHORM();
    YHWebmail wm = (YHWebmail) orm.loadObjSingle(conn, YHWebmail.class, Integer
        .valueOf(fromWebmailId));
    wmb.setAttachmentId(eb.getAttachmentId());
    wmb.setAttachmentName(eb.getAttachmentName());
    wmb.setBodyId(bodyId);
    wmb.setContentHtml(eb.getContent());
    wmb.setFromMail(wm.getEmail());
    wmb.setIsHtml("1");
    wmb.setSendDate(new Date());
    wmb.setSubject(eb.getSubject());
    wmb.setToMail(eb.getToWebmail());
    String copy = eb.getToWebmailCopy();
    wmb.setToMailCopy(copy);
    wmb.setToMailSecret(eb.getToWebmailSecret());
    
    int seqId = getWebSeqId(conn, bodyId);
    if (seqId > 0) {
      wmb.setSeqId(seqId);
      orm.updateSingle(conn, wmb);
    } else {
      orm.saveSingle(conn, wmb);
    }

    YHMailSmtpUtil.sendWebMail(wm, wmb, YHEmailCont.UPLOAD_HOME);
  }

  /**
   * 
   * @param conn
   * @param eb
   * @param bodyId
   * @throws NumberFormatException
   * @throws Exception
   */
  public void saveWebMailBodyByEb(Connection conn, YHEmailBody eb, int bodyId)
      throws NumberFormatException, Exception {
    YHWebmailBody wmb = new YHWebmailBody();
    String fromWebmailId = eb.getFromWebmailId();
    YHORM orm = new YHORM();
    YHWebmail wm = (YHWebmail) orm.loadObjSingle(conn, YHWebmail.class, Integer
        .valueOf(fromWebmailId));
    wmb.setAttachmentId(eb.getAttachmentId());
    wmb.setAttachmentName(eb.getAttachmentName());
    wmb.setBodyId(bodyId);
    wmb.setContentHtml(eb.getContent());
    wmb.setFromMail(wm.getEmail());
    wmb.setIsHtml("1");
    wmb.setSendDate(new Date());
    wmb.setSubject(eb.getSubject());
    wmb.setToMail(eb.getToWebmail());
    int seqId = getWebSeqId(conn, bodyId);
    if (seqId > 0) {
      wmb.setSeqId(seqId);
      orm.updateSingle(conn, wmb);
    } else {
      orm.saveSingle(conn, wmb);
    }
  }

  /**
   * 
   * @param conn
   * @param bodyId
   * @return
   * @throws Exception
   */
  public int getWebSeqId(Connection conn, int bodyId) throws Exception {
    String sql = "select SEQ_ID FROM oa_internet_mailbody WHERE BODY_ID=" + bodyId;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    int seqId = -1;
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        seqId = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return seqId;
  }

  /**
   * 
   * @param conn
   * @param bodyIds
   * @throws Exception
   */
  public void deleteWebmail(Connection conn, String bodyIds) throws Exception {
    if(bodyIds == null || "".equals(bodyIds.trim())){
      return;
    }
    String sql = "DELETE FROM oa_internet_mailbody WHERE BODY_ID IN (" + bodyIds + ")";
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

  /**
   * 
   * @param conn
   * @param bodyIds
   * @throws Exception
   */
  public void deleteMailByBodyId(Connection conn, String bodyIds)
      throws Exception {
    if (bodyIds == null || "".equals(bodyIds) || ",".equals(bodyIds.trim())) {
      return;
    }
    if (bodyIds.endsWith(",")) {
      bodyIds = bodyIds.substring(0, bodyIds.length() - 1);
    }
    String sql = "DELETE FROM oa_email WHERE BODY_ID IN(" + bodyIds + ")";
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
  /**
   * 
   * @param conn
   * @param bodyIds
   * @throws Exception
   */
  public void deleteMailByBodyId(Connection conn, String bodyIds,String userId)
      throws Exception {
    if (bodyIds == null || "".equals(bodyIds) || ",".equals(bodyIds.trim())) {
      return;
    }
    if (bodyIds.endsWith(",")) {
      bodyIds = bodyIds.substring(0, bodyIds.length() - 1);
    }
    String sql = "DELETE FROM oa_email WHERE BODY_ID IN(" + bodyIds + ") and TO_ID = " + userId;
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

  /**
   * 
   * @param conn
   * @param bodyIds
   * @throws Exception
   */
  public void deleteMailBodyByBodyId(Connection conn, String bodyIds)
      throws Exception {
    String sql = "DELETE FROM oa_email_body WHERE SEQ_ID IN(" + bodyIds + ")";
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

  /**
   * 
   * @param conn
   * @param bodyIds
   * @throws Exception
   */
  public String deleteWebmailAll(Connection conn, String bodyIds)
      throws Exception {
    String[] bodyArrays = bodyIds.split(",");
    String result = "";
    String temp = "";
    for (String bodyId : bodyArrays) {
      if (isWebmail(conn, bodyId)) {
        if (!"".equals(temp)) {
          temp += ",";
        }
        temp += bodyId;
      } else {
        if (!"".equals(result)) {
          result += ",";
        }
        result += bodyId;
      }
    }
    if (temp != null && !"".equals(temp)) {
      deleteMailByBodyId(conn, temp);
      deleteMailBodyByBodyId(conn, temp);
//      deleteWebmail(conn, temp);
    }
    return result;
  }

  /**
   * 
   * @param conn
   * @param bodyId
   * @return
   * @throws Exception
   */
  public boolean isWebmail(Connection conn, String bodyId) throws Exception {
    boolean result = false;
    String sql = "select count(SEQ_ID) from oa_email_body where SEQ_ID=" + bodyId
        + " and is_webmail='1'";

    Statement st = null;
    ResultSet rs = null;

    try {
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      if (rs.next()) {

        int count = rs.getInt(1);
        if (count > 0) {
          result = true;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(st, rs, log);
    }
    return result;
  }

  /**
   * 
   * @param con
   * @param bodyId
   * @throws Exception
   */
  public YHWebmailBody refreshLagerAttachmentMail(Connection con, int bodyId)
      throws Exception {
    YHORM orm = new YHORM();
    Map filters = new HashMap();
    filters.put("BODY_ID", bodyId);
    YHEmailBody eb = (YHEmailBody) orm.loadObjSingle(con, YHEmailBody.class,
        bodyId);
    YHWebmailBody wmb = (YHWebmailBody) orm.loadObjSingle(con,
        YHWebmailBody.class, filters);
    YHWebmail wm = (YHWebmail) orm.loadObjSingle(con, YHWebmail.class, Integer
        .valueOf(eb.getFromWebmailId()));
    int wmseqId = wmb.getSeqId();
    wmb = YHMailPop3Util.getWebMailBodyByMesId(con, wm, YHEmailCont.UPLOAD_HOME, wmb.getWebmailUid());
    wmb.setSeqId(wmseqId);
    updateWebMail2Local(con, wmb, wm, orm, eb);
    return wmb;
  }

  /**
   * 
   * @return
   * @throws Exception
   */
  public String hasLagerAttachment(Connection con, int bodyId) throws Exception {
    String result = "0";
    String sql = "select LARGE_ATTACHMENT from oa_internet_mailbody where body_id="
        + bodyId;
    Statement st = null;
    ResultSet rs = null;
    try {
      st = con.createStatement();
      rs = st.executeQuery(sql);
      if (rs.next()) {
        result = rs.getString(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(st, rs, log);
    }
    return result;
  }
  
  /**
   * 取得外部邮件
   * 
   * @param conn
   * @param attachPath
   * @throws Exception
   * @throws Exception
   */
  public void deleteNoUsedAttaches() throws Exception {
    List<YHWebmail> wms = null;

    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      dbConn = YHDataSources.getDataSource(YHSysProps.getSysDbDsName()).getConnection();
      
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery("select ATTACHMENT_ID, ATTACHMENT_NAME from oa_email_body order by SEQ_ID");
      List<String> fileNameList = new ArrayList<String>();
      while (rs.next()) {
        String attachId = rs.getString(1);
        String attachName = rs.getString(2);
        if (attachId == null || attachName == null) {
          continue;
        }
        if ("".equals(attachId.trim()) || "".equals(attachName.trim())) {
          continue;
        }
      }
    }catch(Exception ex) {
    }finally {
      YHDBUtility.closeDbConn(dbConn, log);
    }
  }
}
