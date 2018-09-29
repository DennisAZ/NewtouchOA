package yh.core.funcs.email.logic;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import sun.misc.BASE64Encoder;
import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.email.data.YHEmail;
import yh.core.funcs.email.data.YHEmailBody;
import yh.core.funcs.email.data.YHEmailBox;
import yh.core.funcs.email.data.YHEmailCont;
import yh.core.funcs.email.util.YHMailSmtpUtil;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.office.ntko.data.YHNtkoCont;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.censorcheck.logic.YHCensorCheckLogic;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.funcs.workflow.util.YHFlowUtil;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.oa.tools.StaticData;

public class YHInnerEMailLogic {

	private Logger log = Logger.getLogger(YHInnerEMailLogic.class);
  /**
   * 组织收件箱的数据
   * 
   * @return
   * @throws Exception
   *           pageStart=&seqId=&boxId=0&field=&ascDec=
   */
  private YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
  /**
   * 2.0版邮箱邮件列表
   * 
   * @param conn
   * @param request
   * @param userId
   * @return
   * @throws Exception
   */
  public StringBuffer toInBoxJson(Connection conn, Map request, int userId,String contextPath,Integer styleInSession)
      throws Exception {
    StringBuffer sub = new StringBuffer();
    String boxId = "";
    try {
      boxId = ((String[]) request.get("boxId"))[0];
    } catch (Exception e) {
      boxId = (String) request.get("boxId");
    }
    
    String type = null;
    String[] tm = (String[]) request.get("type");
    if (tm != null && tm.length > 0 ){
      type = tm[0];
    }

    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    String sql = null;
    if (dbms.equals("sqlserver")) {
      sql = "select " + "T0.SEQ_ID as EMAIL_BODY_ID, T1.SEQ_ID as EMAIL_ID," 
      + "isnull((select USER_NAME from PERSON where SEQ_ID = FROM_ID), FROM_WEBMAIL) as FROM_ID,"
      + "SUBJECT," + "ATTACHMENT_ID," + "ATTACHMENT_NAME," + "SEND_TIME,"
      + "ENSIZE ," + "IMPORTANT," + "IS_WEBMAIL, " + "FROM_WEBMAIL "
      + " from " + "oa_email_body T0," + "oa_email T1  " + " where "
      + " T1.TO_ID ='" + userId + "'  " + " AND T1.BOX_ID= " + boxId
      + " AND T1.DELETE_FLAG IN('0','2') " + " AND T0.SEND_FLAG='1'"
      + " AND T1.BODY_ID = T0.SEQ_ID ";
   
    }else if (dbms.equals("mysql")) {
      sql = "select " + "T0.SEQ_ID as EMAIL_BODY_ID," + "T1.SEQ_ID as EMAIL_ID," + "if(T0.FROM_ID < 0, FROM_WEBMAIL, (select USER_NAME from PERSON where SEQ_ID = FROM_ID)) as FROM_ID,"
      + "SUBJECT," + "ATTACHMENT_ID," + "ATTACHMENT_NAME," + "SEND_TIME,"
      + "ENSIZE ," + "IMPORTANT," + "IS_WEBMAIL, " + "FROM_WEBMAIL "
      + " from " + "oa_email_body T0," + "oa_email T1  " + " where "
      + " T1.TO_ID ='" + userId + "'  " + " AND T1.BOX_ID= " + boxId
      + " AND T1.DELETE_FLAG IN('0','2') " + " AND T0.SEND_FLAG='1'"
      + " AND T1.BODY_ID = T0.SEQ_ID ";
    }else if (dbms.equals("oracle")) {
      sql = "select " + "T0.SEQ_ID as EMAIL_BODY_ID, T1.SEQ_ID as EMAIL_ID," 
      + " (case when T0.FROM_ID < 0 then FROM_WEBMAIL else (select USER_NAME from PERSON where SEQ_ID = FROM_ID) end) as FROM_ID,"
      + "SUBJECT," + "ATTACHMENT_ID," + "ATTACHMENT_NAME," + "SEND_TIME,"
      + "ENSIZE ," + "IMPORTANT," + "IS_WEBMAIL, " + "FROM_WEBMAIL "
      + " from " + "oa_email_body T0," + "oa_email T1  " + " where "
      + " T1.TO_ID ='" + userId + "'  " + " AND T1.BOX_ID= " + boxId
      + " AND T1.DELETE_FLAG IN('0','2') " + " AND T0.SEND_FLAG='1'"
      + " AND T1.BODY_ID = T0.SEQ_ID ";
    
    }else {
      throw new SQLException("not accepted dbms");
    }
    
    
    if ("3".equals(type)) {
      sql  += " AND IS_WEBMAIL = '1' ";
      String email = ((String[]) request.get("email"))[0];
      if (!YHUtility.isNullorEmpty(email)) {
        sql  += " AND T0.FROM_WEBMAIL_ID = '" + email + "' " ;
      }
    } else if ("2".equals(type)) {
      sql  += " AND IS_WEBMAIL is null ";
    }
    String query = " order by T0.SEND_TIME desc";
    String queryType = ((String[]) request.get("queryType"))[0];
    boolean isQuery = false;
    if (queryType != null && !"".equals(queryType) && "1".equals(queryType)) {
      sql += toSearchWhere(request, 1, "T1", "T0");
      isQuery = true;
    }
    sql += query;
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request,
        YHPageQueryParam.class, null);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,
        sql);
    pageDataList = listBoxHandler(conn, pageDataList, userId,contextPath,styleInSession,isQuery, boxId);
    sub.append(pageDataList.toJson());
    return sub;
  }

  public YHPageDataList listBoxHandler(Connection dbConn, YHPageDataList pdl,
      int personId,String contextPath,Integer styleInSession,boolean isQuery,String boxId) throws Exception, Exception {
    int rcnt = pdl.getRecordCnt();
    for (int i = 0; i < rcnt; i++) {
      YHDbRecord record = pdl.getRecord(i);
      String emailBodyId = record.getValueByName("emailBodyId").toString();
      String emailId =  record.getValueByName("emailId").toString();
      Object o = record.getValueByName("important");
      String importValue = "";
      if (o != null) {
        importValue = o.toString();
      }
       
      String isWebmail = record.getValueByName("isWebmail") == null ? null : record.getValueByName("isWebmail").toString();
      String subject = (String) record.getValueByName("subject");
     // String subject = (String) record.getValueByName("subject");
      String importHtml = "";
      if ("1".equals(importValue)) {
        importHtml = "<font color=\"red\">重要</font>";
      } else if ("2".equals(importValue)) {
        importHtml = "<font color=\"red\">非常重要</font>";
      }
      String html = "";
      int statusType = getMailStatus(dbConn, Integer.valueOf(emailBodyId), Integer
          .valueOf(emailId), personId, 2);
      int styleIndex = 1;
      
      if (styleInSession != null) {
        styleIndex = styleInSession;
      }
      
      String stylePath = contextPath + "/core/styles/style" + styleIndex;
      String imgPath = stylePath + "/img";
      
      html = getStatus(statusType,imgPath);
      String url = contextPath + "/core/funcs/email/inbox/read_email/index.jsp";
      if ("1".equals(isWebmail)) {
        url = contextPath + "/core/funcs/email/webbox/read_email/index.jsp";
      }
      String queryHtml = "0";
      if(isQuery){
        queryHtml = "1";
      }
      subject =  "<a href=\"" + url + "?isQuery=" + queryHtml + "&mailId=" + emailId + "&seqId="
          + emailBodyId + "&total=" + pdl.getTotalRecord() + "&recordIndex="
          + i + "&boxId=" + boxId + "\">" + subject + "</a>&nbsp;&nbsp;"
          + importHtml + "&nbsp;" + html;
      record.updateField("subject", subject);
      
    }
    return pdl;
  }
  /**
   * ddd
   * @param dbConn
   * @param pdl
   * @param personId
   * @param contextPath
   * @param styleInSession
   * @return
   * @throws Exception
   * @throws Exception
   */
  public YHPageDataList listBoxHandlerByDesk(Connection dbConn, YHPageDataList pdl,
      int personId,String contextPath,Integer styleInSession) throws Exception, Exception {
    int rcnt = pdl.getRecordCnt();
    for (int i = 0; i < rcnt; i++) {
      YHDbRecord record = pdl.getRecord(i);
      String emailBodyId = record.getValueByName("emailBodyId").toString();
      String emailId =  record.getValueByName("emailId").toString();
      String subject = (String) record.getValueByName("subject");
      Integer fromId = (Integer) record.getValueByName("fromId");
      String isWebmail = record.getValueByName("isWebmail") == null ? null : record.getValueByName("isWebmail").toString();
     // String subject = (String) record.getValueByName("subject");
      String importHtml = "";
      String html = "";
      int statusType = getMailStatus(dbConn, Integer.valueOf(emailBodyId), Integer
          .valueOf(emailId), personId, 2);
      int styleIndex = 1;
      
      if (styleInSession != null) {
        styleIndex = styleInSession;
      }
      
      String stylePath = contextPath + "/core/styles/style" + styleIndex;
      String imgPath = stylePath + "/img";
      
      html = getStatus(statusType,imgPath);
/*      String url = contextPath + "/core/funcs/email/inbox/read_email/index.jsp";
      if ("1".equals(isWebmail)) {
        url = contextPath + "/core/funcs/email/webbox/read_email/index.jsp";
      }*/
    /*  subject =  "<a href=\"" + url + "?mailId=" + emailId + "&seqId="
          + emailBodyId + "&total=" + pdl.getTotalRecord() + "&recordIndex="
          + i + "&boxId=0\">" + subject + "</a>&nbsp;&nbsp;"
          + importHtml + "&nbsp;" + html;*/
      record.updateField("subject", subject);
      String fromName = getUserName(dbConn, fromId).toString();
      record.addField("fromName", fromName);
      record.updateField("stauts", html);
    }
    return pdl;
  }
  public String getStatus(int statusType, String imgPath) {
    String html = "";
    String img = "";
    String title = "";
    switch (statusType) {
    case 1:
      img = "mailDel";
      title = "收件人已删除";
      break;
    case 2:
      img = "mailClose";
      title = "收件人未读";
      break;
    case 3:
      img = "mailOpen";
      title = "收件人已读";
      break;
    // case "4" :
    // img = "mailDel";
    // title = "发件人已删除";
    // break;
    case 5:
      img = "mailNew";
      title = "新邮件";
      break;
    case 6:
      img = "mailOpen";
      title = "已读";
      break;
    case 7:
      img = "mailOpen";
      title = "外部邮件";
      break;
    default:
      img = "";
      title = "";
    }
    if (!"".equals(img)) {
      html = "<img  src=\"" + imgPath + "/cmp/email/" + img + ".gif\" title=\""
          + title + "\"align=\"absmiddle\">";
    }
    return html;
  }

  public YHPageDataList listSendBoxHandler(Connection dbConn,
      YHPageDataList pdl, int personId,String contextPath,Integer styleInSession) throws Exception, Exception {
    int rcnt = pdl.getRecordCnt();
    int styleIndex = 1;
    
    if (styleInSession != null) {
      styleIndex = styleInSession;
    }
    
    String stylePath = contextPath + "/core/styles/style" + styleIndex;
    String imgPath = stylePath + "/img";
    for (int i = 0; i < rcnt; i++) {
      YHDbRecord record = pdl.getRecord(i);
      String emailBodyId = record.getValueByName("emailBodyId")== null ? null :record.getValueByName("emailBodyId").toString();
      String toId = record.getValueByName("toId")== null ? null : record.getValueByName("toId").toString();
      String copyToId = record.getValueByName("copyToId")== null ? null : record.getValueByName("copyToId").toString();
      String secretToId = record.getValueByName("secretToId")== null ? null : record.getValueByName("copyToId").toString();
      String[] toIds = toId.split(",");
      String[] copyToIds = (copyToId == null)? null : copyToId.split(",") ;
      String[] secretToIds = (secretToId == null)? null : secretToId.split(",") ;
      int length = 0;
      for (int j = 0; toIds != null && j < toIds.length; j++) {
        if(!"".equals(toIds[j].trim())){
          length ++;
        }
      }
     for (int j = 0; copyToIds != null && j < copyToIds.length; j++) {
       if(!"".equals(copyToIds[j].trim())){
         length ++;
       }
      }
      for (int j = 0;secretToIds!=null && j < secretToIds.length; j++) {
        if(!"".equals(secretToIds[j].trim())){
          length ++;
        }
      }
      int statusType = getMailStatus(dbConn, Integer.valueOf(emailBodyId), 0, personId, 1);
      String html = "";
      if(length == 1){
        html = "<center>" + getStatus(statusType,imgPath) + "</center>";
      }else{
        html = "<center> <a href=\"javascript:readStatus(" + emailBodyId + ");\"><img  src=\"" + imgPath + "/cmp/email/node_dept.gif\" title=\"查看详情\"></a></center> ";
      }
      if (length == 0 ) {
        html ="<center>" +   getStatus(7,imgPath) + "</center>";
      }
      record.addField("status", html);
    }
    return pdl;
  }

  /**
   * 
   * @param conn
   * @param request
   * @param userId
   * @param pageIndex
   * @return
   * @throws Exception
   */
  public StringBuffer toInBoxPageJson(Connection conn, Map request, int userId,
      int pageIndex) throws Exception {
    StringBuffer sub = new StringBuffer();
    String boxId = "";
    try {
      boxId = ((String[]) request.get("boxId"))[0];
    } catch (Exception e) {
      boxId = (String) request.get("boxId");
    }
    String sql = "select " 
      + "T0.SEQ_ID" 
      + ",T1.SEQ_ID" 
      + ",T0.IS_WEBMAIL" 
      + " from "
        + "oa_email_body T0," + "oa_email T1  " + " where " + " T1.TO_ID ='" + userId
        + "'  " + " AND T1.BOX_ID= " + boxId + " AND T0.SEND_FLAG='1'"
        + " AND T1.DELETE_FLAG IN('0','2') " + " AND T1.BODY_ID = T0.SEQ_ID";

    String query = " order by T0.SEND_TIME desc";
    String queryType = request.get("queryType") == null ? null
        : ((String[]) request.get("queryType"))[0];
    if (queryType != null && !"".equals(queryType) && "1".equals(queryType)) {
      sql += toSearchWhere(request, 1, "T1", "T0");
    }
    sql += query;
    // System.out.println(sql);
    YHPageQueryParam queryParam = new YHPageQueryParam();
    String namestr = "bodyId,emialId,isWebmail";
    queryParam.setNameStr(namestr);
    queryParam.setPageIndex(pageIndex);
    queryParam.setPageSize(1);
    // YHPageQueryParam queryParam =
    // (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,
        sql);
    sub.append(pageDataList.toJson());
    return sub;
  }

  /**
   * 
   * @param conn
   * @param request
   * @param userId
   * @param pageIndex
   * @return
   * @throws Exception
   */
  public StringBuffer toDelBoxPageJson(Connection conn, Map request,
      int userId, int pageIndex) throws Exception {
    StringBuffer sub = new StringBuffer();
    String boxId = "0";
    String sql = "select " + "T0.SEQ_ID," + "IS_WEBMAIL" + " from "
        + "oa_email_body T0," + "oa_email T1  " + " where " + "T1.TO_ID ='" + userId
        + "'  " + " AND T0.SEND_FLAG='1'" + " AND T1.DELETE_FLAG IN('3','4') "
        + " AND T1.BODY_ID = T0.SEQ_ID";
    String query = " order by T0.SEND_TIME desc";
    String queryType = request.get("queryType") == null ? null
        : ((String[]) request.get("queryType"))[0];
    if (queryType != null && !"".equals(queryType) && "1".equals(queryType)) {
      sql += toSearchWhere(request, 1, "T1", "T0");
    }
    sql += query;
    YHPageQueryParam queryParam = new YHPageQueryParam();
    String namestr = "bodyId,isWebmail";
    queryParam.setNameStr(namestr);
    queryParam.setPageIndex(pageIndex);
    queryParam.setPageSize(1);
    // YHPageQueryParam queryParam =
    // (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,
        sql);
    sub.append(pageDataList.toJson());
    return sub;
  }

  /**
   * 
   * @param conn
   * @param request
   * @param userId
   * @param pageIndex
   * @return
   * @throws Exception
   */
  public StringBuffer toSendBoxPageJson(Connection conn, Map request,
      int userId, int pageIndex) throws Exception {
    StringBuffer sub = new StringBuffer();
    String sql = "select " + "T0.SEQ_ID " + " from " + " oa_email_body T0"
        + " where " + " FROM_ID = '" + userId + "'" + " AND T0.SEND_FLAG='1'";

    String query = " order by T0.SEND_TIME desc";
    String queryType = request.get("queryType") == null ? null
        : ((String[]) request.get("queryType"))[0];
    if (queryType != null && !"".equals(queryType) && "1".equals(queryType)) {
      String mailStatus = request.get("mailStatus") != null ? ((String[]) request
          .get("mailStatus"))[0]
          : null;
      if (mailStatus != null && !"".equals(mailStatus)) {
        sql += " AND T0.SEQ_ID in(select DISTINCT T1.BODY_ID FROM oa_email T1 WHERE NOT DELETE_FLAG in('2','4') AND T1.READ_FLAG='"
            + mailStatus + "')";
      } else {
        sql += " AND T0.SEQ_ID in(select DISTINCT T1.BODY_ID FROM oa_email T1 WHERE NOT DELETE_FLAG in('2','4')) ";
      }
      sql += toSearchWhere(request, 1, null, "T0");
    } else {
      sql += " AND T0.SEQ_ID in(select DISTINCT T1.BODY_ID FROM oa_email T1 WHERE NOT DELETE_FLAG in('2','4')) ";
    }
    sql += query;
    // System.out.println(sql);
    YHPageQueryParam queryParam = new YHPageQueryParam();
    String namestr = "bodyId";
    queryParam.setNameStr(namestr);
    queryParam.setPageIndex(pageIndex);
    queryParam.setPageSize(1);
    // YHPageQueryParam queryParam =
    // (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,
        sql);
    sub.append(pageDataList.toJson());
    return sub;
  }

  /**
   * 组织未读邮件箱的数据
   * 
   * @return
   * @throws Exception
   *           pageStart=&seqId=&boxId=0&field=&ascDec=
   */
  public StringBuffer toNewBoxJson(Connection conn, Map request, int userId,String contextPath,Integer styleInSession)
      throws Exception {
    // 主题，日期，附件，大小
    StringBuffer sub = new StringBuffer();
    String sql = null;
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    if (dbms.equals("sqlserver")) {
      sql = "select " + "T0.SEQ_ID as EMAIL_BODY_ID," + "T1.SEQ_ID as EMAIL_ID," 
      + "isnull((select USER_NAME from PERSON where SEQ_ID = FROM_ID), FROM_WEBMAIL) as FROM_ID,"
      + "SUBJECT," + "ATTACHMENT_ID," + "ATTACHMENT_NAME," + "SEND_TIME,"
      + "ENSIZE, " + "IMPORTANT," + "IS_WEBMAIL, " + "FROM_WEBMAIL "
      + " from " + "oa_email_body T0," + "oa_email T1  " + " where "
      + "T1.TO_ID ='" + userId + "' " + " AND T1.READ_FLAG= '0'"
      + " AND T0.SEND_FLAG='1'" + " AND T1.DELETE_FLAG IN('0','2') "
      + " AND T1.BODY_ID = T0.SEQ_ID";
   
    }else if (dbms.equals("mysql")) {
      sql = "select " + "T0.SEQ_ID as EMAIL_BODY_ID," + "T1.SEQ_ID as EMAIL_ID," + "if(T0.FROM_ID < 0, FROM_WEBMAIL, (select USER_NAME from PERSON where SEQ_ID = FROM_ID)) as FROM_ID,"
      + "SUBJECT," + "ATTACHMENT_ID," + "ATTACHMENT_NAME," + "SEND_TIME,"
      + "ENSIZE, " + "IMPORTANT," + "IS_WEBMAIL, " + "FROM_WEBMAIL "
      + " from " + "oa_email_body T0," + "oa_email T1  " + " where "
      + "T1.TO_ID ='" + userId + "' " + " AND T1.READ_FLAG= '0'"
      + " AND T0.SEND_FLAG='1'" + " AND T1.DELETE_FLAG IN('0','2') "
      + " AND T1.BODY_ID = T0.SEQ_ID";
    }else if (dbms.equals("oracle")) {
      sql = "select " + "T0.SEQ_ID as EMAIL_BODY_ID," + "T1.SEQ_ID as EMAIL_ID," 
      + "(case when T0.FROM_ID < 0 then FROM_WEBMAIL else (select USER_NAME from PERSON where SEQ_ID = FROM_ID) end ) as FROM_ID,"
      + "SUBJECT," + "ATTACHMENT_ID," + "ATTACHMENT_NAME," + "SEND_TIME,"
      + "ENSIZE, " + "IMPORTANT," + "IS_WEBMAIL, " + "FROM_WEBMAIL "
      + " from " + "oa_email_body T0," + "oa_email T1  " + " where "
      + "T1.TO_ID ='" + userId + "' " + " AND T1.READ_FLAG= '0'"
      + " AND T0.SEND_FLAG='1'" + " AND T1.DELETE_FLAG IN('0','2') "
      + " AND T1.BODY_ID = T0.SEQ_ID";
    
    }else {
      throw new SQLException("not accepted dbms");
    }
    String query = " order by T0.SEND_TIME  desc";
    sql += query;
    // System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request,
        YHPageQueryParam.class, null);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,
        sql);
    pageDataList = listBoxHandler(conn, pageDataList, userId, contextPath, styleInSession,false,"0");
    sub.append(pageDataList.toJson());
    return sub;
  }

  /**
   * 组织草稿箱的数据
   * 
   * @return
   * @throws Exception
   *           pageStart=&seqId=&boxId=0&field=&ascDec=
   */
  public StringBuffer toOutBoxJson(Connection conn, Map request, int userId)
      throws Exception {
    // 主题，日期，附件，大小    StringBuffer sub = new StringBuffer();
    String sql = "select " + "T0.SEQ_ID," + "SUBJECT," + "ATTACHMENT_ID,"
        + "ATTACHMENT_NAME," + "SEND_TIME," + "ENSIZE ," + "IMPORTANT "
        + " from " + " oa_email_body T0" + " where " + " FROM_ID ='" + userId
        + "'" + " AND SEND_FLAG ='1'"
        + " AND NOT (T0.SEQ_ID in (select DISTINCT T1.BODY_ID FROM oa_email T1) " +
        		" or T0.SEQ_ID in (select DISTINCT T2.BODY_ID FROM oa_internet_mailbody T2)) " ;
    String query = " order by T0.SEND_TIME  desc";
    sql += query;
    // System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request,
        YHPageQueryParam.class, null);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,
        sql);
    sub.append(pageDataList.toJson());
    return sub;
  }

  /**
   * 组织发件箱的数据
   * 
   * @return
   * @throws Exception
   *           pageStart=&seqId=&boxId=0&field=&ascDec=
   */
  public StringBuffer toSendBoxJson(Connection conn, Map request, int userId,String contextPath,Integer styleInSession)
      throws Exception {
    // 主题，日期，附件，大小
    StringBuffer sub = new StringBuffer();
    String sql = "select " + "T0.SEQ_ID," + "T0.TO_ID," + "T0.COPY_TO_ID,"
        + "T0.SECRET_TO_ID," + "SUBJECT," + "ATTACHMENT_ID,"
        + "ATTACHMENT_NAME," + "SEND_TIME," + "ENSIZE ," + "IMPORTANT "
        + " from " + " oa_email_body T0" + " where " + " FROM_ID = '" + userId
        + "'" + " AND T0.SEND_FLAG='1' ";

    String query = " order by T0.SEND_TIME  desc";
    String queryType = request.get("queryType") == null ? null
        : ((String[]) request.get("queryType"))[0];
    if (queryType != null && !"".equals(queryType) && "1".equals(queryType)) {
      String mailStatus = request.get("mailStatus") != null ? ((String[]) request
          .get("mailStatus"))[0]
          : null;
      if (mailStatus != null && !"".equals(mailStatus)) {
        sql += " AND T0.SEQ_ID in (select DISTINCT T1.BODY_ID FROM oa_email T1 WHERE NOT DELETE_FLAG in ('2','4') AND T1.READ_FLAG='"
            + mailStatus + "')";
        sql += toSearchWhere(request, 2, "T1", "T0");
      } else {
        sql += " AND (T0.SEQ_ID in (select DISTINCT T1.BODY_ID FROM oa_email T1 WHERE NOT DELETE_FLAG in ('2','4')) ";
        sql += " or T0.SEQ_ID in (select DISTINCT T2.BODY_ID FROM oa_internet_mailbody T2 where T2.DELETE_FLAG is NULL)) ";
        sql += toSearchWhere2(request, 2, "T1", "T0" , "T2");
      }
      
    } else {
      sql += " AND (T0.SEQ_ID IN (select DISTINCT T1.BODY_ID FROM oa_email T1 WHERE NOT T1.DELETE_FLAG in ('2','4')) ";
      sql += " or T0.SEQ_ID in (select DISTINCT T2.BODY_ID FROM oa_internet_mailbody T2 where T2.DELETE_FLAG is NULL))";
    }
    sql += query;
    // System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,
        sql);
    pageDataList = listSendBoxHandler(conn, pageDataList, userId, contextPath, styleInSession);
    sub.append(pageDataList.toJson());
    return sub;
  }

  /**
   * 已删除邮件
   * 
   * @param conn
   * @param userId
   * @param boxId
   * @param page
   * @param max
   * @return
   * @throws Exception
   */
  public StringBuffer toDelBoxJson(Connection conn, Map request, int userId)
      throws Exception {
    // 主题，日期，附件，大小
    StringBuffer sub = new StringBuffer();
    String sql = "select " + "T0.SEQ_ID," + "T1.SEQ_ID," + "T0.FROM_ID,"
        + "SUBJECT," + "ATTACHMENT_ID," + "ATTACHMENT_NAME," + "SEND_TIME,"
        + "ENSIZE, " + "IMPORTANT," + "IS_WEBMAIL, " + "FROM_WEBMAIL "
        + " from " + "oa_email_body T0 , oa_email T1 " + "  where "
        + " T1.TO_ID ='" + userId + "'  " + " AND T1.DELETE_FLAG IN ('3','4')  "
        + " AND T0.SEND_FLAG='1' " + " and  T1.BODY_ID = T0.SEQ_ID  ";
    String type = null;
    String[] tm = (String[]) request.get("type");
    if (tm != null && tm.length > 0 ){
      type = tm[0];
    }
    if ("3".equals(type)) {
      sql  += " AND IS_WEBMAIL = '1' ";
      String email = ((String[]) request.get("email"))[0];
      if (!YHUtility.isNullorEmpty(email)) {
        sql  += " AND T0.FROM_WEBMAIL_ID = '" + email + "' " ;
      }
    } else if ("2".equals(type)) {
      sql  += " AND IS_WEBMAIL is null ";
    }
    
    String query = " order by T0.SEND_TIME  desc";
    String queryType = request.get("queryType") == null ? null
        : ((String[]) request.get("queryType"))[0];
    if (queryType != null && !"".equals(queryType) && "1".equals(queryType)) {
      sql += toSearchWhere(request, 1, "T1", "T0");
    }
    sql += query;
    // System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request,
        YHPageQueryParam.class, null);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,
        sql);
    sub.append(pageDataList.toJson());
    return sub;
  }
  public StringBuffer toDelBoxJson2(Connection conn, Map request, int userId)
  throws Exception {
// 主题，日期，附件，大小
    
    StringBuffer sub = new StringBuffer();
    String sql = "select " + "T0.SEQ_ID," + "T1.SEQ_ID," + "T0.FROM_ID,"
        + "T0.SUBJECT," + "T0.ATTACHMENT_ID," + "T0.ATTACHMENT_NAME," + "SEND_TIME,"
        + "ENSIZE, " + "IMPORTANT," + "IS_WEBMAIL, " + "FROM_WEBMAIL "
        + " from " + "oa_email_body T0 left outer join oa_email T1 on T1.BODY_ID = T0.SEQ_ID  left outer join oa_internet_mailbody T2 on T2.BODY_ID = T0.SEQ_ID  " + "  where "
        + " ((T1.TO_ID ='" + userId + "'  " + " AND T1.DELETE_FLAG IN('3','4')) OR T2.DELETE_FLAG IS NOT NULL) "
        + " AND T0.SEND_FLAG='1' " + " ";
    
    String type = null;
    String[] tm = (String[]) request.get("type");
    if (tm != null && tm.length > 0 ){
      type = tm[0];
    }
    if ("3".equals(type)) {
      sql  += " AND IS_WEBMAIL = '1' ";
      String email = ((String[]) request.get("email"))[0];
      if (!YHUtility.isNullorEmpty(email)) {
        sql  += " AND T0.FROM_WEBMAIL_ID = '" + email + "' " ;
      }
    } else if ("2".equals(type)) {
      sql  += " AND IS_WEBMAIL is null ";
    }
    
    String query = " order by T0.SEND_TIME  desc";
    String queryType = request.get("queryType") == null ? null
        : ((String[]) request.get("queryType"))[0];
    if (queryType != null && !"".equals(queryType) && "1".equals(queryType)) {
      sql += toSearchWhere(request, 1, "T1", "T0");
    }
    sql += query;
    // System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request,
        YHPageQueryParam.class, null);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,
        sql);
    sub.append(pageDataList.toJson());
    return sub;
}
  /**
   * 发送邮件
   * 
   * 逻辑: 1.插入邮件正文 2.插入邮件标题（根据收件人id）
   * 
   * map YHEmailBody 表单内容
   * 
   * @throws Exception
   */
  public int sendMailLogic(Connection conn, YHFileUploadForm fileForm,
      int fromId, String pathPx, String loginUserName, String contextPath)
      throws Exception {
    YHORM orm = new YHORM();
    String toId = "";
    String attachmentId = "";
    String attachmentName = "";
    Map request = fileForm.getParamMap();
    String smsRemind = (String) request.get("smsRemind");
    String sms2Remind = (String) request.get("sms2Remind");
    String sendFlag = "1";
    String sendEdit = null;
    YHEmailBody eb = null;
    int bodyId = 0;
    int result = 0;
    try {
      try {
        sendEdit = request.get("sendEdit") == null ? null : ((String[]) request
            .get("sendEdit"))[0];
      } catch (Exception e) {
        sendEdit = request.get("sendEdit") == null ? null : (String) request
            .get("sendEdit");
        log.debug(e.getMessage());
      }
      try {
        sendFlag = (String[]) request.get("sendFlag") == null ? "1"
            : ((String[]) request.get("sendFlag"))[0];
      } catch (Exception e) {
        sendFlag = (String) request.get("sendFlag") == null ? "1"
            : (String) request.get("sendFlag");
        log.debug(e.getMessage());
      }
      if ("0".equals(sendFlag)) {
        return 1;
      }
      String repict = "1";
      try {
        repict = (String[]) request.get("receipt") == null ? "0"
            : ((String[]) request.get("receipt"))[0];
      } catch (Exception e) {
        repict = (String) request.get("receipt") == null ? "0" : (String) request
            .get("receipt");
        log.debug(e.getMessage());
      }

      Map<String, String> attr = fileUploadLogic(fileForm, pathPx);
      try {
        attachmentId = ((String[]) request.get("attachmentId"))[0];
        attachmentName = ((String[]) request.get("attachmentName"))[0];
      } catch (Exception e) {
        attachmentId = (String) request.get("attachmentId");
        attachmentName = (String) request.get("attachmentName");
        log.debug(e.getMessage());
      }
      if (attachmentId != null && !"".equals(attachmentId)) {
        if (!(attachmentId.trim()).endsWith(",")) {
          attachmentId += ",";
        }
        if (!(attachmentName.trim()).endsWith("*")) {
          attachmentName += "*";
        }
      } else {
        attachmentId = "";
        attachmentName = "";
      }
      try {
        toId = ((String[]) request.get("toId"))[0];
      } catch (Exception e) {
        toId = (String) request.get("toId");
        log.debug(e.getMessage());
      }
      String copyToId = "";
      try {
        copyToId = ((String[]) request.get("copyToId"))[0];

      } catch (Exception e) {
        copyToId = (String) request.get("copyToId");
        log.debug(e.getMessage());
      }
      String secretToId = "";
      try {
        secretToId = ((String[]) request.get("secretToId"))[0];

      } catch (Exception e) {
        secretToId = (String) request.get("secretToId");
        log.debug(e.getMessage());
      }
      Set<String> attrKeys = attr.keySet();
      long esize = 0l;
//      Iterator<String> iKeys = fileForm.iterateFileFields();
//      while (iKeys.hasNext()) {
//        String fieldName = iKeys.next();
//        esize += fileForm.getFileSize(fieldName);
//      }
      for (String key : attrKeys) {
        String fileName = attr.get(key);
        attachmentId += key + ",";
        attachmentName += fileName + "*";
      }
      eb = (YHEmailBody) YHFOM
          .build(request, YHEmailBody.class, null);
      esize = eb.getEnsize() + getSize(attr);
      eb.setEnsize(esize);
      eb.setFromId(fromId);
      eb.setAttachmentId(attachmentId);
      eb.setAttachmentName(attachmentName);
      eb.setSendFlag(sendFlag);
      eb.setCompressContent(YHDiaryUtil.cutHtml(eb.getContent()));
      eb.setSendTime(new Date());
      if (eb.getImportant() == null || "".equals(eb.getImportant())) {
        eb.setImportant("0");
      }
      if (eb.getSubject() == null || "".equals(eb.getSubject())) {
        String subject = "";
        if (attachmentName != null && !"".equals(attachmentName.trim())) {
          subject = attachmentName.split("\\*")[0];
          subject = subject.substring(subject.indexOf("_") + 1, subject
              .lastIndexOf("."));
        } else {
          subject = "[无主题]";
        }
        eb.setSubject(subject);
      }
      if ("0".equals(sendFlag) || "2".equals(sendFlag)) {
        eb.setSendFlag("0");
      } else {
        eb.setSendFlag("1");
      }
      int emailId = 0;
      if (eb.getSeqId() != 0 && (sendEdit == null || "".equals(sendEdit.trim()))) {
        orm.updateSingle(conn, eb);// 先保存正文

        bodyId = eb.getSeqId();
      } else {
        orm.saveSingle(conn, eb);// 先保存正文
        bodyId = emul.getBodyId(conn);
      }

      if ("2".equals(sendFlag)) {
        smsRemind = "";
        String jsonStr = "{BODY_ID:" + bodyId + ",FROM_ID:" + fromId
            + ", TO_ID:\"" + toId + "\",SUBJECT:\""
            + YHUtility.encodeSpecial(eb.getSubject()) + "\", CONTENT:\""
            + YHUtility.encodeSpecial(eb.getContent()) + "\", SEND_TIME:\""
            + YHUtility.getDateTimeStr(eb.getSendTime()) + "\", RECEIPT:\""
            + repict + "\"}";
        YHCensorCheckLogic.addJsonContent(conn, "0", jsonStr, fromId);
        result = 2;
      }
      ArrayList<String> ids = new ArrayList<String>();
      if (toId != null && !"".equals(toId)) {
        ids = emul.addArray(ids, toId.split(","));
      }
      if (secretToId != null && !"".equals(secretToId)) {
        ids = emul.addArray(ids, secretToId.split(","));
      }
      if (copyToId != null && !"".equals(copyToId)) {
        ids = emul.addArray(ids, copyToId.split(","));
      }
      for (int i = 0; ids != null && i < ids.size(); i++) {
        String id = ids.get(i);
        if ("".equals(id)) {
          continue;
        }
        YHEmail em = (YHEmail) YHFOM.build(request, YHEmail.class, null);
        em.setBodyId(bodyId);
        em.setToId(id);
        if (eb.getToId() == null || "".equals(eb.getToId())) {
          em.setToId("-1");
        }
        orm.saveSingle(conn, em);
        emailId = emul.getBodyId(conn, "OA_EMAIL");
        if ("1".equals(smsRemind)) {
          smsRmind(conn, request, bodyId, fromId, emailId, id);
        }
      }
      if ("1".equals(sms2Remind)) {
        String smsContent = "OA邮件,来自 " + loginUserName + ":" + eb.getSubject();
        YHMobileSms2Logic ms2l = new YHMobileSms2Logic();
        String sms2ToId = toId + "," + copyToId + "," + secretToId;
        ms2l.remindByMobileSms(conn, sms2ToId, fromId, smsContent, new Date());
      }
    } catch (Exception e) {
      log.debug(e.getMessage());
      throw new Exception("1");
    }
    /**
     * 发送外部邮件
     */
    try {
      if (!YHUtility.isNullorEmpty(eb.getToWebmail().trim())) {
        YHWebmailLogic wbml = new YHWebmailLogic();
        wbml.sendWebMail(conn, eb, bodyId);
      }
    } catch (Exception e) {
      log.debug(e.getMessage());
      throw new Exception("2");
    }
    return result;
  }

  /**
   * 存草稿
   * 
   * @param conn
   * @param request
   * @param fromId
   * @throws Exception
   */
  public int saveMailLogic(Connection conn, YHFileUploadForm fileForm,
      int fromId, String pathPx) throws Exception {
    YHORM orm = new YHORM();
    String seqIdStr = "";
    String attachmentId = "";
    String attachmentName = "";
    int bId = -1;
    Map request = fileForm.getParamMap();
    Map<String, String> attr = new HashMap<String, String>();
    try {
      attr = fileUploadLogic(fileForm, pathPx);
    } catch (Exception e) {
    }
    try {
    } catch (Exception e) {
    }
    try {
      seqIdStr = ((String[]) request.get("seqId"))[0];
    } catch (Exception e) {
      seqIdStr = (String) request.get("seqId");
    }
    try {
      attachmentId = ((String[]) request.get("attachmentId"))[0];
      attachmentName = ((String[]) request.get("attachmentName"))[0];
    } catch (Exception e) {
      attachmentId = (String) request.get("attachmentId");
      attachmentName = (String) request.get("attachmentName");
    }
    if (attachmentId != null && !"".equals(attachmentId)) {
      if (!(attachmentId.trim()).endsWith(",")) {
        attachmentId += ",";
      }
      if (!(attachmentName.trim()).endsWith("*")) {
        attachmentName += "*";
      }
    } else {
      attachmentId = "";
      attachmentName = "";
    }
    
    Set<String> attrKeys = attr.keySet();
    for (String key : attrKeys) {
      String fileName = attr.get(key);
      attachmentId += key + ",";
      attachmentName += fileName + "*";
    }
    try {
    } catch (Exception e) {
    }
    try {
    } catch (Exception e) {
    }
    YHEmailBody eb = (YHEmailBody) YHFOM
        .build(request, YHEmailBody.class, null);
    eb.setSendFlag("1");
    long size = eb.getEnsize() + getSize(attr);
    eb.setEnsize(size);
    if (eb.getSubject() == null || "".equals(eb.getSubject().trim())) {
      String subject = "";
      if (attachmentName != null && !"".equals(attachmentName)) {
        subject = attachmentName.split("\\*")[0];
        subject = subject.substring(subject.indexOf("_") + 1, subject
            .lastIndexOf("."));
      } else {
        subject = "[无主题]";
      }
      eb.setSubject(subject);
    }
    int seqId = eb.getSeqId();
    eb.setFromId(fromId);
    if (eb.getSendTime() == null) {
      eb.setSendTime(new Date());
    }
    if (eb.getImportant() == null || "".equals(eb.getImportant())) {
      eb.setImportant("1");
    }
    /*
     * if (eb.getToId() == null || "".equals(eb.getToId())) { eb.setToId("-1");
     * }
     */
    eb.setAttachmentId(attachmentId);
    eb.setAttachmentName(attachmentName);
    eb.setCompressContent(YHDiaryUtil.cutHtml(eb.getContent()));
    if (seqIdStr != null && !"".equals(seqIdStr)) {
      seqId = Integer.valueOf(seqIdStr.trim());
      eb.setSeqId(seqId);
      orm.updateSingle(conn, eb);// 先保存正文      bId = eb.getSeqId();
    } else {
      orm.saveSingle(conn, eb);
      bId = emul.getBodyId(conn);
    }
    if (eb.getFromWebmailId() != null && !"".equals(eb.getFromWebmailId())) {
      //YHWebmailLogic wbml = new YHWebmailLogic();
      //wbml.saveWebMailBodyByEb(conn, eb, bId);
    }
    return bId;
  }

  /**
   * 存草稿
   * 
   * 
   * @param conn
   * @param request
   * @param fromId
   * @throws Exception
   */
  public int saveMailByTimeLogic(Connection conn, Map request, int fromId,
      String pathPx) throws Exception {
    YHORM orm = new YHORM();
    String toId = "";
    String copyToId = "";
    String secretToId = "";
    String seqIdStr = "";
    String attachmentId = "";
    String attachmentName = "";
    int bId = -1;
    try {
      toId = ((String[]) request.get("toId"))[0];
    } catch (Exception e) {
      toId = (String) request.get("toId");
    }
    try {
      seqIdStr = ((String[]) request.get("seqId"))[0];
    } catch (Exception e) {
      seqIdStr = (String) request.get("seqId");
    }
    try {
      attachmentId = ((String[]) request.get("attachmentId"))[0];
      attachmentName = ((String[]) request.get("attachmentName"))[0];
    } catch (Exception e) {
      attachmentId = (String) request.get("attachmentId");
      attachmentName = (String) request.get("attachmentName");
    }
    if (attachmentId != null && !"".equals(attachmentId)) {
      if (!(attachmentId.trim()).endsWith(",")) {
        attachmentId += ",";
      }
      if (!(attachmentName.trim()).endsWith("*")) {
        attachmentName += "*";
      }
    } else {
      attachmentId = "";
      attachmentName = "";
    }
    try {
      copyToId = ((String[]) request.get("copyToId"))[0];

    } catch (Exception e) {
      copyToId = (String) request.get("copyToId");
    }
    try {
      secretToId = ((String[]) request.get("secretToId"))[0];

    } catch (Exception e) {
      secretToId = (String) request.get("secretToId");
    }
    YHEmailBody eb = (YHEmailBody) YHFOM
        .build(request, YHEmailBody.class, null);
    eb.setSendFlag("1");
    int seqId = eb.getSeqId();
    eb.setFromId(fromId);
    if (eb.getSendTime() == null) {
      eb.setSendTime(new Date());
    }
    if (eb.getImportant() == null || "".equals(eb.getImportant())) {
      eb.setImportant("1");
    }
    eb.setAttachmentId(attachmentId);
    eb.setAttachmentName(attachmentName);
    if (eb.getSubject() == null || "".equals(eb.getSubject().trim())) {
      eb.setSubject("[无主题]");
    }
    if (seqIdStr != null && !"".equals(seqIdStr)) {
      seqId = Integer.valueOf(seqIdStr.trim());
      eb.setSeqId(seqId);
      orm.updateSingle(conn, eb);// 先保存正文
      bId = eb.getSeqId();
    } else {
      orm.saveSingle(conn, eb);
      bId = emul.getBodyId(conn);
    }
    return bId;
  }

  /**
   * 存草稿
   * 
   * @param conn
   * @param request
   * @param fromId
   * @throws Exception
   */
  public void updateMailLogic(Connection conn, YHFileUploadForm fileForm,
      int fromId, String pathx) throws Exception {
    YHORM orm = new YHORM();
    String attachmentId = "";
    String attachmentName = "";
    Map request = fileForm.getParamMap();
    Map<String, String> attr = fileUploadLogic(fileForm, pathx);
    try {
      attachmentId = ((String[]) request.get("attachmentId"))[0];
      attachmentName = ((String[]) request.get("attachmentName"))[0];
    } catch (Exception e) {
      attachmentId = (String) request.get("attachmentId");
      attachmentName = (String) request.get("attachmentName");
    }
    if (attachmentId != null && !"".equals(attachmentId)) {
      if (!(attachmentId.trim()).endsWith(",")) {
        attachmentId += ",";
      }
      if (!(attachmentName.trim()).endsWith("*")) {
        attachmentName += "*";
      }
    } else {
      attachmentId = "";
      attachmentName = "";
    }
    Set<String> attrKeys = attr.keySet();
    for (String key : attrKeys) {
      String fileName = attr.get(key);
      attachmentId += key + ",";
      attachmentName += fileName + "*";
    }
    YHEmailBody eb = (YHEmailBody) YHFOM
        .build(request, YHEmailBody.class, null);
    long size = eb.getEnsize() + getSize(attr);
    eb.setEnsize(size);
    eb.setFromId(fromId);
    eb.setAttachmentId(attachmentId);
    eb.setAttachmentName(attachmentName);
    if (eb.getSendTime() == null) {
      eb.setSendTime(new Date());
    }
    if (eb.getImportant() == null || "".equals(eb.getImportant())) {
      eb.setImportant("1");
    }
    if (eb.getSendFlag() == null || "".equals(eb.getSendFlag())) {
      eb.setSendFlag("1");
    }
    if (eb.getToId() == null || "".equals(eb.getToId())) {
      eb.setToId("-1");
    }
    eb.setCompressContent(YHDiaryUtil.cutHtml(eb.getContent()));
    orm.updateSingle(conn, eb);// 先保存正文  }

  /**
   * 查看邮件是否处于可删除状态OA版
   * 
   * @param conn
   * @param seqId
   *          {seqId,seqId,seqId...}
   * @param deletFlag
   * @param readFlag
   * @param isFrom
   *          1收件人，3发件人，2永久删除
   * @throws SQLException
   */
  public String deleteMailForOa(String isFrom, String seqId, String deletFlag,
      String readFlag) throws Exception {
    int deletbit = "".equals(deletFlag.trim()) ? 0 : Integer.valueOf(deletFlag
        .trim());
    int readbit = "".equals(readFlag.trim()) ? 0 : Integer.valueOf(readFlag
        .trim());
    String result = "";
    boolean bool = false;
    // 1.收件人删除01/11
    if ("1".equals(isFrom)) {
      if (deletbit == 0) {
        deletbit = 3;
      } else if (deletbit == 2) {
        deletbit = 4;
      }
    } else if ("2".equals(isFrom)) {
      // 2.发件人删除10/11
      if (deletbit == 0 || deletbit == 3) {
        deletbit = 1;
      } else if (deletbit == 4 || deletbit == 2) {
        bool = true;
      }
    } else if ("3".equals(isFrom)) {
      if (readbit == 0) {
        bool = true;
      } else {
        if (deletbit == 0) {
          deletbit = 2;
        } else if (deletbit == 3) {
          deletbit = 4;
        } else if (deletbit == 1) {
          bool = true;
        }
      }
    } else if ("5".equals(isFrom)) {
      if (deletbit == 4) {
        deletbit = 2;
      } else if (deletbit == 3) {
        deletbit = 0;
      }
    }
    if (!bool) {
      result = emul.updateFlagStr("DELETE_FLAG", deletbit + "", seqId, "oa_email");
    }
    return result;
  }

  public String deleteMailForOa2(String isFrom, String seqId, String deletFlag,
      String readFlag) throws Exception {
    int deletbit = Integer.valueOf(deletFlag.trim());
    int readbit = Integer.valueOf(readFlag.trim());
    String result = "";
    boolean bool = false;
    // 1.收件人删除01/11
    if ("1".equals(isFrom)) {
      if (deletbit == 0) {
        deletbit = 3;
      } else if (deletbit == 2) {
        deletbit = 4;
      } else if (deletbit == 3) {
        deletbit = 1;
      } else if (deletbit == 4) {
        bool = true;
      }
    } else if ("2".equals(isFrom)) {
      // 2.发件人删除10/11
      if (deletbit == 0 || deletbit == 3) {
        deletbit = 1;
      } else if (deletbit == 4 || deletbit == 2) {
        bool = true;
      }
    } else if ("3".equals(isFrom)) {
      if (deletbit == 0) {
        deletbit = 1;
      } else if (deletbit == 3) {
        deletbit = 1;
      } else if (deletbit == 1) {
        bool = true;
      }
    } else if ("5".equals(isFrom)) {
      if (deletbit == 4) {
        deletbit = 2;
      } else if (deletbit == 3) {
        deletbit = 0;
      }
    }
    if (!bool) {
      result = emul.updateFlagStr("DELETE_FLAG", deletbit + "", seqId, "oa_email");
    }
    return result;
  }

  /**
   * 删除邮件
   * 
   * @param conn
   * @param bodyId
   * @param isFrom
   *          1收件人，3发件人，2永久删除,4表示草稿箱删除
   * 
   * @param userId
   * @throws Exception
   *           if("4".equals(isform)){ deleteAll(conn, bodyId + "",
   *           "EMAIL_BODY"); return; } List<YHEmail> melist =
   *           orm.loadListSingle(conn, YHEmail.class, filters); rs =
   *           emul.findFlag(conn, "EMAIL", fields, filters); while(rs.next()){
   *           int seqId = rs.getInt("SEQ_ID"); String deleteFlag =
   *           rs.getString("DELETE_FLAG"); String readFlag =
   *           rs.getString("READ_FLAG"); String toId = rs.getString("TO_ID");
   *           //如果 boolean b = false; if(userId.equals(toId) &&
   *           "1".equals(isform)){ b = deleteMailForOa(conn, isform, seqId +
   *           "", deleteFlag, readFlag); } else if("2".equals(isform) ||
   *           "3".equals(isform)|| "5".equals(isform)){ b =
   *           deleteMailForOa(conn, isform, seqId + "", deleteFlag, readFlag);
   *           } if(b){ deleteAll(conn, seqId + "", "EMAIL"); } isDe = isDe &&
   *           b; if(!"".equals(seqIdArr)){ seqIdArr += ","; } seqIdArr +=
   *           seqId; } if(isDe){ //删除所有邮件
   * 
   * 
   *           deleteAll(conn, bodyId + "", "EMAIL_BODY"); }
   */
  public void doDeleteMail(Connection conn, String bodyId, String isform,
      String userId) throws Exception {
    if (bodyId == null || "".equals(bodyId)) {
      return;
    }
    int delType = Integer.valueOf(isform);
    String[] filters = null;
    List<YHEmail> melist = null;
    YHORM orm = new YHORM();
    YHWebmailLogic wbml = new YHWebmailLogic();
    try {
      if (delType == 2) {
        wbml.deleteWebmailAll(conn, bodyId);
      }
      
      if (delType == 4) {
        wbml.deleteMailByBodyId(conn, bodyId);
        deleteAll(conn, bodyId, "oa_email_body");
//        wbml.deleteWebmail(conn, bodyId);
        return;
      } else if (delType == 1  || delType == 2) {
        filters = new String[] { " TO_ID='" + userId + "' AND  BODY_ID IN( "
            + bodyId + ")" };
      } else if ( delType == 3 || delType == 5) {
        filters = new String[] { " BODY_ID IN( " + bodyId + ")" };
      }
      melist = orm.loadListSingle(conn, YHEmail.class, filters);
      ArrayList<String> sqls = new ArrayList<String>();
      ArrayList<Integer> isCanDelBodyIds = new ArrayList<Integer>();
      String sql = "";
      String isCanDelSeqIds = "";
      for (YHEmail email : melist) {
        sql = deleteMailForOa(isform, email.getSeqId() + "", email
            .getDeleteFlag(), email.getReadFlag());
        if ("".equals(sql)) {
          if (!"".equals(isCanDelSeqIds)) {
            isCanDelSeqIds += ",";
          }
          isCanDelSeqIds += email.getSeqId();
          if (!isCanDelBodyIds.contains(email.getBodyId())) {
            isCanDelBodyIds.add(email.getBodyId());
          }
        } else {
          sqls.add(sql);
        }
      }
      emul.updateFlag(conn, sqls);
      if (delType == 3) {
         sql = "update  oa_internet_mailbody set DELETE_FLAG = '1'  WHERE BODY_ID IN (" + bodyId + ")";
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
      if (!"".equals(isCanDelSeqIds)) {
        deleteAll(conn, isCanDelSeqIds, "oa_email");
        String bodyIds = emul.checkDel(conn, isCanDelBodyIds);
        deleteAll(conn, bodyIds, "oa_email_body");
        wbml.deleteWebmail(conn, bodyIds);
      }
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * 批量删除邮件
   * 
   * @param conn
   * @param seqIds
   * @param tableName
   * @throws Exception
   */
  public void deleteAll(Connection conn, String seqIds, String tableName)
      throws Exception {
    if (seqIds == null || "".equals(seqIds)) {
      return;
    }
    String sql = "DELETE FROM " + tableName + " WHERE SEQ_ID IN(" + seqIds
        + ")";
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
   * 批量发送邮件（从草稿箱中）
   * 
   * @param conn
   * @param seqIds
   * @param tableName
   * @throws Exception
   */
  public void sendMailAll(Connection conn, String seqIds) throws Exception {
    YHORM orm = new YHORM();
    String[] filters = new String[] { "SEQ_ID IN ( " + seqIds + " )" };
    ArrayList<YHEmailBody> emls = (ArrayList<YHEmailBody>) orm.loadListSingle(
        conn, YHEmailBody.class, filters);
    for (YHEmailBody emb : emls) {
      /**
       * 发送外部邮件
       */
      if (!YHUtility.isNullorEmpty(emb.getFromWebmailId())) {
        YHWebmailLogic wbml = new YHWebmailLogic();
        wbml.sendWebMail(conn, emb, emb.getSeqId());
      }
      if ("-1".equals(emb.getToId()) || "".equals(emb.getToId())
          || emb.getToId() == null) {
        continue;
      }
      String toId = emb.getToId();
      String secretToId = emb.getSecretToId();
      String copyToId = emb.getCopyToId();

      ArrayList<String> ids = new ArrayList<String>();
      if (toId != null && !"".equals(toId)) {
        ids = emul.addArray(ids, toId.split(","));
      }
      if (secretToId != null && !"".equals(secretToId)) {
        ids = emul.addArray(ids, secretToId.split(","));
      }
      if (copyToId != null && !"".equals(copyToId)) {
        ids = emul.addArray(ids, copyToId.split(","));
      }
      for (int i = 0; ids != null && i < ids.size(); i++) {
        String id = ids.get(i);
        if ("".equals(id)) {
          continue;
        }
        YHEmail em = new YHEmail();
        em.setBodyId(emb.getSeqId());
        em.setBoxId(0);
        em.setBodyId(emb.getSeqId());
        em.setToId(id);
        if (emb.getToId() == null || "".equals(emb.getToId())) {
          em.setToId("-1");
        }
        orm.saveSingle(conn, em);
      }
    }
  }

  /**
   * 处理上传附件，返回附件id，附件名称
   * 
   * @param request
   *          HttpServletRequest
   * @param
   * @return Map<String, String> ==> {id = 文件名}
   * @throws Exception
   */
  public Map<String, String> fileUploadLogic(YHFileUploadForm fileForm,
      String pathPx) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    String filePath = pathPx;
    // fileForm.saveFileAll(filePath);
    Calendar cld = Calendar.getInstance();
    int year = cld.get(Calendar.YEAR) % 100;
    int month = cld.get(Calendar.MONTH) + 1;
    String mon = month >= 10 ? month + "" : "0" + month;
    String hard = year + mon;
    Iterator<String> iKeys = fileForm.iterateFileFields();
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      String fileName = fileForm.getFileName(fieldName);
      String trusFileName = "";
      if (YHUtility.isNullorEmpty(fileName)) {
        continue;
      }
      String rand = emul.getRandom();
      trusFileName = rand + "." + fileName;
      while (emul.getExist(filePath + File.separator + hard, trusFileName)) {
        rand = emul.getRandom();
        trusFileName = rand + "." + fileName;
      }
      result.put(hard + "_" + rand, fileName);
      fileForm.saveFile(fieldName, filePath + File.separator + YHEmailCont.MODULE + File.separator
          + hard + File.separator + trusFileName);
    }
    YHSelAttachUtil selA = new YHSelAttachUtil(fileForm, YHEmailCont.MODULE);
    result.putAll(selA.getAttachInFo());
    return result;
  }

  public Map<String, String> fileUploadLogic(String fileName, InputStream in,
      String pathPx) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    String module = YHEmailCont.MODULE;
    String filePath = pathPx + File.separator + module;
    // fileForm.saveFileAll(filePath);
    Calendar cld = Calendar.getInstance();
    int year = cld.get(Calendar.YEAR) % 100;
    int month = cld.get(Calendar.MONTH) + 1;
    String mon = month >= 10 ? month + "" : "0" + month;
    String hard = year + mon;
    String trusFileName = "";
    String rand = emul.getRandom();
    trusFileName = rand + "." + fileName;
    while (emul.getExist(filePath + File.separator + hard, trusFileName)) {
      rand = emul.getRandom();
      trusFileName = rand + "." + fileName;
    }
    BufferedOutputStream bos = null;
    BufferedInputStream bis = null;
    String trusPath = filePath + File.separator + hard + File.separator + trusFileName;
    File storeDir = new File(filePath + File.separator + hard);
    if (!storeDir.exists()) {
      storeDir.mkdirs();
    }
    File storefile = new File(trusPath);
    if (!storefile.exists()) {
      storefile.createNewFile();
    }

    try {
      bos = new BufferedOutputStream(new FileOutputStream(storefile));
      bis = new BufferedInputStream(in);
      int c;
      while ((c = bis.read()) != -1) {
        bos.write(c);
        bos.flush();
      }
    } finally {
      bos.close();
      bis.close();
    }
    result.put(hard + "_" + rand, fileName);
    return result;
  }
  public String[] fileUploadLogic2(String fileName, InputStream in,
      String pathPx) throws Exception {
    String module = YHEmailCont.MODULE;
    String filePath = pathPx + File.separator + module;
    // fileForm.saveFileAll(filePath);
    Calendar cld = Calendar.getInstance();
    int year = cld.get(Calendar.YEAR) % 100;
    int month = cld.get(Calendar.MONTH) + 1;
    String mon = month >= 10 ? month + "" : "0" + month;
    String hard = year + mon;
    String trusFileName = "";
    String rand = emul.getRandom();
    trusFileName = rand + "." + fileName;
    while (emul.getExist(filePath + File.separator + hard, trusFileName)) {
      rand = emul.getRandom();
      trusFileName = rand + "." + fileName;
    }
    BufferedOutputStream bos = null;
    BufferedInputStream bis = null;
    String trusPath = filePath + File.separator + hard + File.separator + trusFileName;
    File storeDir = new File(filePath + File.separator + hard);
    if (!storeDir.exists()) {
      storeDir.mkdirs();
    }
    File storefile = new File(trusPath);
    if (!storefile.exists()) {
      storefile.createNewFile();
    }

    try {
      bos = new BufferedOutputStream(new FileOutputStream(storefile));
      bis = new BufferedInputStream(in);
      int c;
      while ((c = bis.read()) != -1) {
        bos.write(c);
        bos.flush();
      }
    } finally {
      bos.close();
      bis.close();
    }
    String[] result = new String[2];
    result[0] = hard + "_" + rand;
    result[1] = fileName;
    return result;
  }
  /**
   * 取得文件的大小
   * 
   * @param fileForm
   * @return
   * @throws Exception
   */
  public long getSize(YHFileUploadForm fileForm) throws Exception {
    long result = 0l;
    Iterator<String> iKeys = fileForm.iterateFileFields();
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      String fileName = fileForm.getFileName(fieldName);
      if (YHUtility.isNullorEmpty(fileName)) {
        continue;
      }
      result += fileForm.getFileSize(fieldName);
    }
    return result;
  }

  /**
   * 
   * @param attr
   * @param pathPx
   * @return
   * @throws Exception
   */
  public long getSize(Map<String, String> attr) throws Exception {
    long result = 0l;
    Set<String> attrKeys = attr.keySet();
    String module = YHEmailCont.MODULE;
    String fileName = "";
    String path = "";
    for (String attachmentId : attrKeys) {
      String attachmentName = attr.get(attachmentId);
      if(attachmentId != null && !"".equals(attachmentId)){
        if(attachmentId.indexOf("_") > 0){
          String attIds[] = attachmentId.split("_");
          fileName = attIds[1] + "." + attachmentName;
          path = YHEmailCont.UPLOAD_HOME + File.separator + module + File.separator + attIds[0] + File.separator  + fileName;
        }else{
          fileName = attachmentId + "." + attachmentName;
          path = YHEmailCont.UPLOAD_HOME + File.separator + module + File.separator  + fileName;
        }
        
        File file = new File(path);
        if(!file.exists()){
          if(attachmentId.indexOf("_") > 0){
            String attIds[] = attachmentId.split("_");
            fileName = attIds[1] + "_" + attachmentName;
            path = YHNtkoCont.ATTA_PATH + File.separator + module + File.separator + attIds[0] + File.separator  + fileName;
          }else{
            fileName = attachmentId + "_" + attachmentName;
            path = YHNtkoCont.ATTA_PATH + File.separator + module + File.separator  + fileName;
          }
          file = new File(path);
        }
        if(!file.exists()){
          continue;
        }
        //this.fileName = fileName;
        result += file.length();
      }
    }
    return result;
  }
  /**
   * 
   * @param typeInt
   *          (0代表收件箱，1代表发件箱，2代表草稿箱，3代表废件箱，4代表外发邮件箱，5其他)
   * @param conn
   * @return
   * @throws Exception
   */
  public StringBuffer getCount2JsonAll(Connection conn, int userId)
      throws Exception {

    StringBuffer result = new StringBuffer();
    YHEmailBoxLogic ebl = new YHEmailBoxLogic();
    ArrayList<YHEmailBox> eblist = ebl.listBoxByUser(conn, userId, false);
    String field = "count(T0.SEQ_ID)";
    // 默认邮箱
    // 收件箱'EMAIL_IN':{'total':10,'newTotal':4}
    int intotal = emul.getCount(conn, " T1.TO_ID ='" + userId + "' "
        + " AND T1.BOX_ID= 0" + " AND T1.DELETE_FLAG IN('0','2') "
        + " AND T0.SEND_FLAG='1' " + " AND T1.BODY_ID = T0.SEQ_ID", 1, field);
    int innew = emul.getCount(conn, " T1.TO_ID ='" + userId + "' "
        + " AND T1.READ_FLAG= '0'" + " AND T1.BOX_ID= 0"
        + " AND T1.DELETE_FLAG IN('0','2') " + " AND T0.SEND_FLAG='1' "
        + " AND T1.BODY_ID = T0.SEQ_ID ", 1, field);
    // 已删除邮件箱
    int deltotal = emul.getCount(conn, " T1.TO_ID ='" + userId + "'  "
        + " AND T1.DELETE_FLAG IN('3','4') " + " AND T0.SEND_FLAG='1' "
        + " AND T1.BODY_ID = T0.SEQ_ID", 1, field);
    // 已发送邮件箱
    int sendtotal = emul
        .getCount(
            conn,
            " FROM_ID = '"
                + userId
                + "'"
                + " AND T0.SEND_FLAG='1' AND (T0.SEQ_ID in (select DISTINCT T1.BODY_ID FROM oa_email T1 WHERE NOT DELETE_FLAG in('2','4')) or T0.SEQ_ID in (select DISTINCT T2.BODY_ID FROM oa_internet_mailbody T2 WHERE  DELETE_FLAG is null))",
            2, field);
    // 草稿箱    int outtotal = emul.getCount(conn, " FROM_ID ='" + userId + "'"
        + " AND T0.SEND_FLAG= '1'"
        + " AND NOT T0.SEQ_ID in (select DISTINCT T1.BODY_ID FROM oa_email T1) AND NOT T0.SEQ_ID IN (select DISTINCT T2.BODY_ID FROM oa_internet_mailbody T2) ",
        3, field);
    result.append("{inbox0:").append(intotal).append(",inNew:").append(innew)
        .append(",delbox:").append(deltotal).append(",sendbox:").append(
            sendtotal).append(",outbox:").append(outtotal).append(",webbox:")
        .append(0).append("}");
    return result;
  }
  public StringBuffer getCountIn(Connection conn, int userId)
  throws Exception {
    StringBuffer result = new StringBuffer();
    YHEmailBoxLogic ebl = new YHEmailBoxLogic();
    //ArrayList<YHEmailBox> eblist = ebl.listBoxByUser(conn, userId, false);

    String sql=" select count(seq_id) from emailView where  TO_ID ='" + userId + "' and BOX_ID= 0 and DELETE_FLAG IN('0','2') and SEND_FLAG='1' ";
    // 默认邮箱
    // 收件箱'EMAIL_IN':{'total':10,'newTotal':4}
    int intotal = emul.getCountTwo(conn, sql);
    result.append("{inbox0:").append(intotal).append(",webbox:")
    .append(0).append("}");
    return result;
}
  
  public boolean createView(Connection conn)
  throws Exception {
    boolean result=true;
    Statement stmt=null;
    try{
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    String sql="";
    String sql2="";
    if(!"sqlserver".equals(dbms)){
    sql=" CREATE OR REPLACE VIEW EMAILVIEW  AS "
             + " SELECT  e.seq_id,b.seq_id as bid,e.to_id,e.DELETE_FLAG,b.SEND_FLAG,FROM_ID,e.READ_FLAG,e.BOX_ID "
             + " FROM oa_email e right join oa_email_body b on e.body_id=b.seq_id ";
    }else if("sqlserver".equals(dbms)){
      sql=" if exists(select   name   from   sysobjects   where   name= 'emailview'   and   type= 'v ' ) " +
      		" drop view emailview; " ;
      		
      sql2="  CREATE VIEW  emailview  " +
      		" as "+
      		" SELECT  e.seq_id,b.seq_id as bid,e.to_id,e.DELETE_FLAG,b.SEND_FLAG,FROM_ID,e.READ_FLAG,e.BOX_ID  " +
      		" FROM oa_email e right join oa_email_body b on e.body_id=b.seq_id  " ;
    }
     stmt=conn.createStatement();
     stmt.execute(sql);
     if (!"".equals(sql2)) {
       stmt.execute(sql2);
     }
    } catch (Exception e) {
      result=false;
      e.printStackTrace();
      throw e;
     
    }
    finally{
      YHDBUtility.close(stmt, null, null);
    }
    return result;
    
}
  
  public StringBuffer getCountSent(Connection conn, int userId)
  throws Exception {
    
    StringBuffer result = new StringBuffer();
    YHEmailBoxLogic ebl = new YHEmailBoxLogic();
    //ArrayList<YHEmailBox> eblist = ebl.listBoxByUser(conn, userId, false);
    String field = "count(T0.SEQ_ID)";
    String sql2 = "SELECT count(*) from oa_email_body,oa_email where EMAIL.BODY_ID=oa_email_body.SEQ_ID and FROM_ID='"+userId + "' and SEND_FLAG='1' and DELETE_FLAG!='2' and DELETE_FLAG!='4' group by EMAIL.BODY_ID";
    //String sql=" select count(seq_id) from emailView where FROM_ID = '"+userId + "' and SEND_FLAG='1' and  NOT DELETE_FLAG in('2','4')  ";
    // 已发送邮件箱
    int sendtotal = emul.getCountTwo2(conn, sql2);
    result.append("{sendbox:").append(sendtotal).append("}");
    return result;
}
  public StringBuffer getCountNew(Connection conn, int userId)
  throws Exception {
    
    StringBuffer result = new StringBuffer();
    YHEmailBoxLogic ebl = new YHEmailBoxLogic();
    //ArrayList<YHEmailBox> eblist = ebl.listBoxByUser(conn, userId, false);
    String field = "count(T0.SEQ_ID)";
    String sql=" select count(seq_id) from emailView where TO_ID ='" + userId + "' and READ_FLAG= '0' and BOX_ID= 0 and DELETE_FLAG IN('0','2') and SEND_FLAG='1'  ";
    // 新邮件
    int innew = emul.getCountTwo(conn, sql);
    result.append("{inNew:").append(innew).append("}");
    return result;
}
  public StringBuffer getCountDel(Connection conn, int userId)
  throws Exception {
    
    StringBuffer result = new StringBuffer();
    YHEmailBoxLogic ebl = new YHEmailBoxLogic();
    //ArrayList<YHEmailBox> eblist = ebl.listBoxByUser(conn, userId, false);
    String field = "";
    field=" select count(SEQ_ID) from emailView where TO_ID ='" + userId + "' and DELETE_FLAG IN('3','4') and SEND_FLAG='1' " ;
    // 删除
    int deltotal = emul.getCountTwo(conn, field);
    result.append("{delbox:").append(deltotal).append("}");
    return result;
}
  public StringBuffer getCountOut(Connection conn, int userId)
  throws Exception {
    
    StringBuffer result = new StringBuffer();
    YHEmailBoxLogic ebl = new YHEmailBoxLogic();
    //ArrayList<YHEmailBox> eblist = ebl.listBoxByUser(conn, userId, false);
    String field = "count(T0.SEQ_ID)";
    // 草稿箱
    String sql=" select count(bid) as seq_id from emailView  where FROM_ID ='" + userId + "' and SEND_FLAG= '1' AND  seq_id is null";
    int outtotal = emul.getCountTwo(conn, sql);
    result.append("{outbox:").append(outtotal).append("}");
    return result;
}
  /**
   * 
   * @param typeInt
   *          (0代表收件箱，1代表发件箱，2代表草稿箱，3代表废件箱，4代表外发邮件箱，5其他)
   * @param conn
   * @return
   * @throws Exception
   */
  public StringBuffer getSelfBoxLogic(Connection conn, int userId)
      throws Exception {

    StringBuffer result = new StringBuffer();
    // 默认邮箱
    // 收件箱'EMAIL_IN':{'total':10,'newTotal':4}
    String field = "count(T0.SEQ_ID)";
    YHORM orm = new YHORM();
    String[] filters = new String[] { "USER_ID='" + userId
        + "' AND NOT BOX_NO = 0" };
    ArrayList<YHEmailBox> eblist = (ArrayList<YHEmailBox>) orm.loadListSingle(
        conn, YHEmailBox.class, filters);
    for (int i = 0; i < eblist.size(); i++) {
      int intotal = emul.getCount(conn,
          " T1.TO_ID ='" + userId + "' " + " AND T1.BOX_ID= "
              + eblist.get(i).getSeqId() + " AND T1.DELETE_FLAG IN('0','2') "
              + " AND T1.BODY_ID = T0.SEQ_ID ", 1, field);

      if (!"".equals(result.toString())) {
        result.append(",");
      }
      result.append("{inbox:").append(intotal).append(",inboxName:\"").append(
          eblist.get(i).getBoxName()).append("\"").append(",inboxId:").append(
          eblist.get(i).getSeqId()).append("}");
    }
    return result;
  }

  /**
   * 快捷操作时查找邮件的ID
   * 
   * @param conn
   * @param type
   * @param userId
   * @return
   * @throws Exception
   *           收件箱： 删除所有已读邮件 1 已发送邮件： 删除所有收件人已删除邮件 2 删除所有收件人未读邮件 3 删除所有收件人已读邮件 4
   *           已删除 : 清空已删除邮件箱 5
   */
  public String getIdsForEx(Connection conn, int type, int userId)
      throws Exception {
    String result = "";
    if (type == 1) {
      emul.getMailIds(conn, " TO_ID=" + userId + " AND BOX_ID =0"
          + " AND DELETE_FLAG IN('0','2') AND READ_FLAG = 1 ");
    } else if (type == 2) {
      emul.getMailIds(conn, " FROM_ID=" + userId + " AND BOX_ID =0"
          + " AND DELETE_FLAG = 3 ");
    } else if (type == 3) {
      emul.getMailIds(conn, " FROM_ID=" + userId + " AND BOX_ID =0"
          + " AND DELETE_FLAG IN('0','3')  AND  READ_FLAG = 0 ");
    } else if (type == 4) {
      emul.getMailIds(conn, " FROM_ID=" + userId + " AND BOX_ID =0"
          + " AND DELETE_FLAG IN('0','3')  AND  READ_FLAG = 1 ");
    } else if (type == 5) {
      emul.getMailIds(conn, " FROM_ID=" + userId + " AND BOX_ID =0"
          + " AND DELETE_FLAG IN('3','4')");
    }
    return result;
  }

  /**
   * 附件批量上传页面处理
   * 
   * @return
   * @throws Exception
   */
  public StringBuffer uploadMsrg2Json(YHFileUploadForm fileForm, String pathP)
      throws Exception {
    StringBuffer sb = new StringBuffer();
    Map<String, String> attr = null;
    String attachmentId = "";
    String attachmentName = "";
    try {
      attr = fileUploadLogic(fileForm, pathP);
      Set<String> attrKeys = attr.keySet();
      for (String key : attrKeys) {
        String fileName = attr.get(key);
        attachmentId += key + ",";
        attachmentName += fileName + "*";
      }
      long size = getSize(attr);
      sb.append("{");
      sb.append("'attachmentId':").append("\"").append(attachmentId).append(
          "\",");
      sb.append("'attachmentName':").append("\"").append(attachmentName)
          .append("\",");
      sb.append("'size':").append("").append(size);
      sb.append("}");
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return sb;
  }

  /**
   * 群发邮件-查看详情
   * 
   * @param conn
   * @param emailBodyId
   * @return
   * @throws Exception
   */
  public List<YHEmail> toStatusObj(Connection conn, int emailBodyId)
      throws Exception {
    ArrayList<YHEmail> result = null;
    YHORM orm = new YHORM();
    String[] filters = new String[] { " BODY_ID=" + emailBodyId };

    try {
      result = (ArrayList<YHEmail>) orm.loadListSingle(conn, YHEmail.class,
          filters);
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  /**
   * 群发邮件-查看详情
   * 
   * @param conn
   * @param emailBodyId
   * @return
   * @throws Exception
   */
  public List<YHEmail> toStatusObj(Connection conn, int emailBodyId,YHEmailBody eb)
      throws Exception {
    ArrayList<YHEmail> result = null;
    YHORM orm = new YHORM();
    String[] filters = new String[] { " BODY_ID=" + emailBodyId };

    String[] toIds = eb.getToId() == null ? null :  eb.getToId().split(",");
    String[] copyToIds = eb.getCopyToId() == null ? null :  eb.getCopyToId().split(",");
    ArrayList<String> ids = new ArrayList<String>();
    ArrayList<String> temp = new ArrayList<String>();
    for (int i = 0; toIds != null && i < toIds.length; i++) {
      if("".equals(toIds[i].trim())){continue;}
      ids.add(toIds[i]);
    }
    for (int i = 0; copyToIds != null && i < copyToIds.length; i++) {
      if("".equals(copyToIds[i].trim())){continue;}
      ids.add(copyToIds[i]);
    }
    try {
      result = (ArrayList<YHEmail>) orm.loadListSingle(conn, YHEmail.class,
          filters);
    } catch (Exception e) {
      throw e;
    }
    for (int i = 0; i < result.size(); i++) {
      if(ids.contains(result.get(i).getToId())){
        ids.remove(result.get(i).getToId());
      }
    }
    for (int i = 0; i < ids.size(); i++) {
      YHEmail tempe = new YHEmail();
      tempe.setToId(ids.get(i));
      tempe.setDeleteFlag("1");
      result.add(tempe);
    }
    return result;
  }
  /**
   * 得到邮件的状态
   * 
   * @param conn
   * @param emailBodyId
   * @param userId
   * @param type
   *          [1发件箱|2收件箱和已删除邮件箱]
   * @return [1发件箱已删除|2发件箱未读|3发件箱已读|4收件件箱已删除|5收件箱未读new|6收件箱已读]
   * @throws Exception
   */
  public int getMailStatus(Connection conn, int emailBodyId, int emailId,
      int userId, int type) throws Exception {
    int result = -1;
    String sql = " select " + "T0.DELETE_FLAG" + ",T0.READ_FLAG" + " from "
        + "oa_email T0" + ",oa_email_body T1";
    String query = "";
    if (type == 1) {// 发件箱
      query = " where " + " T1.FROM_ID='" + userId + "'" + " AND T0.BODY_ID="
          + emailBodyId + " AND T0.BODY_ID = T1.SEQ_ID";
    } else if (type == 2) {// 收件箱和已删除邮件箱
      query = " where " + " T0.SEQ_ID=" + emailId
          + " AND T0.BODY_ID = T1.SEQ_ID";
    }
    sql += query;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String deleteFlag = rs.getString(1);
        String readFlag = rs.getString(2);
        if (type == 1) {// 发件箱
          if ("1".equals(deleteFlag)) {
            result = 1;// 发件箱已删除
          } else {
            if ("0".equals(readFlag)) {
              result = 2;// 发件箱未读
            } else {
              result = 3;// 发件箱已读
            }
          }
        } else if (type == 2) {// 收件箱和已删除
          if ("2".equals(deleteFlag) || "4".equals(deleteFlag)) {
            result = 4;// 收件件箱已删除          } else {
            if ("0".equals(readFlag)) {
              result = 5;// 收件箱未读new
            } else {
              result = 6;// 收件箱已读            }
          }
        }
      } else {
        result = 7;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }

  /**
   * 短信回复
   * 
   * @param conn
   * @param request
   * @param emailId
   * @param emailBodyId
   * @param userId
   * @return
   * @throws Exception
   */
  public boolean smsRmind(Connection conn, Map request, int emailBodyId,
      int userId, int emailId, String ids) throws Exception {
    String subject = (String) request.get("subject");
    subject = " 请查收我的邮件！主题：" + subject;
    String remindUrl = "/core/funcs/email/inbox/read_email/index.jsp?mailId="
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
   * 删除操作的快捷键
   *  delType: 1=收件人已删除邮箱,2=收件人收件箱,3=发件人已发送邮箱
   *  shortCutType：1
   * @param conn
   * @param userId
   * @param delType
   * @param shortCutType
   * @param boxId
   * @return
   * @throws Exception
   */
  public String delForShortCut(Connection conn, int userId, String delTypeStr,
      String shortCutTypeStr, String boxId) throws Exception {
    YHInnerEMailUtilLogic ieul = new YHInnerEMailUtilLogic();
    int delType = Integer.valueOf(delTypeStr);
    int shortCutType = Integer.valueOf(shortCutTypeStr);
    String result = "";
    String filters = "";
    try {
      if (delType == 1) { //收件箱
        if (shortCutType == 1) { //删除所有已读邮件 
          filters = " TO_ID='" + userId + "' AND READ_FLAG='1' AND BOX_ID="
              + boxId + " AND DELETE_FLAG IN('0','2')";
        }
        if (filters.length() > 0) {
          result = ieul.getMailIds(conn, filters);
        }
      } else if (delType == 3) { //发件箱
        switch (shortCutType) {
        case 1: //删除所有收件人已删除邮件（收件人永久删除）
          filters = " T1.FROM_ID='" + userId
              + "' AND T1.SEQ_ID=T0.BODY_ID AND T0.DELETE_FLAG ='1'";
          break;
        case 2: //删除所有收件人未读邮件
          filters = " T1.FROM_ID='"
              + userId
              + "' AND T1.SEQ_ID=T0.BODY_ID AND T0.READ_FLAG='0' AND T0.DELETE_FLAG IN('0','1','3') ";
          break;
        case 3: //删除所有收件人已读邮件
          filters = " T1.FROM_ID='"
              + userId
              + "' AND T1.SEQ_ID=T0.BODY_ID AND T0.READ_FLAG='1' AND T0.DELETE_FLAG IN('0','1','3')";
          break;
        }
        //result = ieul.getMailBodyIdFout(conn, filters);
        if (filters.length() > 0) {
          result = ieul.getMailBodyIdFout(conn, filters,delTypeStr,shortCutTypeStr);
        }
      } else if (delType == 2) { //已删除邮件箱
        if (shortCutType == 2) { //清空已删除邮件箱
          filters = " TO_ID='" + userId + "' AND DELETE_FLAG IN('3','4')";
        }
        if (filters.length() > 0) {
          result = ieul.getMailIds(conn, filters);
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }

  /**
   * 
   * @param conn
   * @param type
   *          [1收件箱新邮件,2已删除邮件箱新邮件,3发件箱已删除邮件]
   * @param userId
   * @param boxId
   * @return
   * @throws Exception
   */
  public int getNewMailCount(Connection conn, int type, int userId, int boxId , String queryType , String email)
      throws Exception {
    int result = 0;
    String field = "count(T0.SEQ_ID)";
    if (type == 1) {
      String tmp = "";
      if ("3".equals(queryType)) {
        tmp = " AND IS_WEBMAIL = '1' ";
      } else if ("2".equals(queryType)) {
        tmp = " AND IS_WEBMAIL is null ";
      }
      if (!YHUtility.isNullorEmpty(email)) {
        tmp  += " AND FROM_WEBMAIL_ID = '" + email + "' " ;
      }
      result = emul.getCount(conn, " T1.TO_ID ='" + userId + "' "
          + " AND T1.READ_FLAG= '0'" + " AND T1.BOX_ID=" + boxId
          + " AND T1.DELETE_FLAG IN('0','2') " + " AND T0.SEND_FLAG='1'"
          + " AND T1.BODY_ID = T0.SEQ_ID "
          + tmp
          , 1, field);
    } else if (type == 2) {
      String tmp = "";
      if ("3".equals(queryType)) {
        tmp = " AND IS_WEBMAIL = '1' ";
      } else if ("2".equals(queryType)) {
        tmp = " AND IS_WEBMAIL is null ";
      }
      if (!YHUtility.isNullorEmpty(email)) {
        tmp  += " AND FROM_WEBMAIL_ID = '" + email + "' " ;
      }
      result = emul.getCount(conn, " T1.TO_ID ='" + userId + "'  "
          + " AND T1.READ_FLAG= '0'" + " AND T1.BOX_ID=" + boxId
          + " AND T1.DELETE_FLAG IN('3','4') " + " AND T0.SEND_FLAG='1'"
          + " AND T1.BODY_ID = T0.SEQ_ID " 
          + tmp
          , 1, field);
    } else if (type == 3) {
      result = emul
          .getCount(
              conn,
              " FROM_ID = "
                  + userId
                  + " AND T0.SEND_FLAG='1'"
                  + " AND T0.SEQ_ID in(select DISTINCT T1.BODY_ID FROM oa_email T1 WHERE  DELETE_FLAG='1') ",
              2, field);

    }
    return result;
  }

  public void changeMailBoxLogic(Connection conn, int boxId, String ids,
      int userId) throws Exception {
    String sql = "update EMAIL SET BOX_ID = " + boxId + " WHERE  TO_ID = '"
        + userId + "' AND BODY_ID IN(" + ids + ")";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, null, null);
    }
  }
  /**
   * 组装查询条件
   * 
   * @param request
   * @param type
   *          [1|2] [收件|发件]
   * @param table1
   * @param table2
   *          [1|2] [收件|发件]
   * @return
   * @throws Exception
   */
  private String toSearchWhere2(Map request, int type, String emailTable,
      String emailBodyTable , String webmailBody) throws Exception {
    String whereStr = "";
    String startDateStr = request.get("startDate") != null ? ((String[]) request
        .get("startDate"))[0]
        : null;
    String endDateStr = request.get("endDate") != null ? ((String[]) request
        .get("endDate"))[0] : null;
    String userId = request.get("userId") != null ? ((String[]) request
        .get("userId"))[0] : null;
    String webUsers = request.get("toWebmail") != null ? ((String[]) request
        .get("toWebmail"))[0] : null;
            String toWebmailcc = request.get("toWebmailCC") != null ? ((String[]) request
                .get("toWebmailCC"))[0] : null;  
                String toWebmailbcc = request.get("toWebmailBCC") != null ? ((String[]) request
                    .get("toWebmailBCC"))[0] : null;
    String subject = request.get("subject") != null ? YHDBUtility
        .escapeLike(((String[]) request.get("subject"))[0]) : null;
    String key1 = request.get("key1") != null ? YHDBUtility
        .escapeLike(((String[]) request.get("key1"))[0]) : null;
    String key2 = request.get("key2") != null ? YHDBUtility
        .escapeLike(((String[]) request.get("key2"))[0]) : null;
    String key3 = request.get("key3") != null ? YHDBUtility
        .escapeLike(((String[]) request.get("key3"))[0]) : null;
    String attachmentName = request.get("attachmentName") != null ? YHDBUtility
        .escapeLike(((String[]) request.get("attachmentName"))[0]) : null;

    String mailStatus = request.get("mailStatus") != null ? ((String[]) request
        .get("mailStatus"))[0] : null;

    if (userId != null && !"".equals(userId)) {
      if (userId.trim().endsWith(",")) {
        userId = userId.trim().substring(0, userId.trim().length() - 1);
      }
      if (type == 1) {
        whereStr += " and " + emailBodyTable + ".FROM_ID in(" + userId + ")";
      } else if (type == 2) {
        whereStr += " and " + emailBodyTable
            + ".SEQ_ID IN (SELECT BODY_ID FROM oa_email T2 WHERE "
            + findInId(userId, "T2.TO_ID") + ")";
      }
    }
    // 加上开始日期、截止日期条件

    if (!YHUtility.isNullorEmpty(webUsers)) {
      String filter = " and ( ";
      String[] webs = webUsers.split(",");
      int count = 0 ;
      for (String w : webs) {
        filter +=  YHDBUtility.findInSet(w, "T0.TO_WEBMAIL") + " or ";
        count++;
      }
      if (count > 0) {
        filter = filter.substring(0, filter.length() - "or ".length());
      }
      filter += " ) ";
      whereStr += filter;
    }
    if (!YHUtility.isNullorEmpty(toWebmailcc)) {
      String filter = " and (";
      String[] webs = toWebmailcc.split(",");
      int count = 0 ;
      for (String w : webs) {
        filter +=  YHDBUtility.findInSet(w, "T0.TO_WEBMAIL_COPY") + " or ";
        count++;
      }
      if (count > 0) {
        filter = filter.substring(0, filter.length() - "or ".length());
      }
      filter += " ) ";
      whereStr += filter;
    }
    if (!YHUtility.isNullorEmpty(toWebmailbcc)) {
      String filter = " and (";
      String[] webs = toWebmailbcc.split(",");
      int count = 0 ;
      for (String w : webs) {
        filter +=  YHDBUtility.findInSet(w, "T0.TO_WEBMAIL_SECRET") + " or ";
        count++;
      }
      if (count > 0) {
        filter = filter.substring(0, filter.length() - "or ".length());
      }
      filter += " ) ";
      whereStr += filter;
    }
    
    if (startDateStr != null && !"".equals(startDateStr)) {
      String dbDateF = YHDBUtility.getDateFilter(emailBodyTable + ".SEND_TIME",
          startDateStr, " >= ");
      whereStr += " and " + dbDateF;
    }
    if (endDateStr != null && !"".equals(endDateStr)) {
      String dbDateF = YHDBUtility.getDateFilter(emailBodyTable + ".SEND_TIME",
          endDateStr, " <= ");
      whereStr += " and " + dbDateF;
    }
    // 加上日志类型条件
    if (mailStatus != null && !"".equals(mailStatus)) {
      if (type == 1) {
        whereStr += " and " + emailTable + ".READ_FLAG='" + mailStatus + "'";
      }
    }
    // 加上标题条件
    if (subject != null && !"".equals(subject)) {
      whereStr += " and " + emailBodyTable + ".SUBJECT like '%" + subject
          + "%'" + YHDBUtility.escapeLike();
    }
    if (attachmentName != null && !"".equals(attachmentName)) {
      whereStr += " and " + emailBodyTable + ".ATTACHMENT_NAME like '%"
          + attachmentName + "%'" + YHDBUtility.escapeLike();
    }
    // 加上三个关键词条件，关键词对应CONTENT字段（CONTENT应该是滤掉了html格式之后的文本内容）
    if ((key1 != null && !"".equals(key1))
        || (key2 != null && !"".equals(key2))
        || (key3 != null && !"".equals(key3))) {
      if (key1 == null || "".equals(key1)) {
        key1 = "!@#$%^&*()__)(*&^%$#@";
      }
      if (key2 == null || "".equals(key2)) {
        key2 = "!@#$%^&*()__)(*&^%$#@";
      }
      if (key3 == null || "".equals(key3)) {
        key3 = "!@#$%^&*()__)(*&^%$#@";
      }
      whereStr += " and (" + emailBodyTable + ".COMPRESS_CONTENT like '%"
          + key1 + "%' " + YHDBUtility.escapeLike() + " or " + emailBodyTable
          + ".COMPRESS_CONTENT like '%" + key2 + "%' "
          + YHDBUtility.escapeLike() + "  or " + emailBodyTable
          + ".COMPRESS_CONTENT like '%" + key3 + "%' "
          + YHDBUtility.escapeLike() + " )";
    }
    return whereStr;
  }
  /**
   * 组装查询条件
   * 
   * @param request
   * @param type
   *          [1|2] [收件|发件]
   * @param table1
   * @param table2
   *          [1|2] [收件|发件]
   * @return
   * @throws Exception
   */
  private String toSearchWhere(Map request, int type, String emailTable,
      String emailBodyTable ) throws Exception {
    String whereStr = "";
    String startDateStr = request.get("startDate") != null ? ((String[]) request
        .get("startDate"))[0]
        : null;
    String endDateStr = request.get("endDate") != null ? ((String[]) request
        .get("endDate"))[0] : null;
    String userId = request.get("userId") != null ? ((String[]) request
        .get("userId"))[0] : null;
    String webUsers = request.get("toWebmail") != null ? ((String[]) request
        .get("toWebmail"))[0] : null;
    String subject = request.get("subject") != null ? YHDBUtility
        .escapeLike(((String[]) request.get("subject"))[0]) : null;
    String key1 = request.get("key1") != null ? YHDBUtility
        .escapeLike(((String[]) request.get("key1"))[0]) : null;
    String key2 = request.get("key2") != null ? YHDBUtility
        .escapeLike(((String[]) request.get("key2"))[0]) : null;
    String key3 = request.get("key3") != null ? YHDBUtility
        .escapeLike(((String[]) request.get("key3"))[0]) : null;
    String attachmentName = request.get("attachmentName") != null ? YHDBUtility
        .escapeLike(((String[]) request.get("attachmentName"))[0]) : null;

    String mailStatus = request.get("mailStatus") != null ? ((String[]) request
        .get("mailStatus"))[0] : null;

    if (userId != null && !"".equals(userId)) {
      if (userId.trim().endsWith(",")) {
        userId = userId.trim().substring(0, userId.trim().length() - 1);
      }
      if (type == 1) {
        whereStr += " and " + emailBodyTable + ".FROM_ID in(" + userId + ")";
      } else if (type == 2) {
        whereStr += " and " + emailBodyTable
            + ".SEQ_ID IN (SELECT BODY_ID FROM oa_email T2 WHERE "
            + findInId(userId, "T2.TO_ID") + ")";
      }
    }
    // 加上开始日期、截止日期条件
    if (type == 1 && !YHUtility.isNullorEmpty(webUsers)) {
      String filter = " and (";
      for (String s : webUsers.split(",")) {
        if (YHUtility.isNullorEmpty(s)) {
          continue;
        }
        s = YHMailSmtpUtil.covertAddress(s);
        filter += " FROM_WEBMAIL like '%" + s + "%'" + " or ";
      }
      if (!" and (".equals(filter)) {
        filter = filter.substring(0, filter.length() - 4) + ")";
        whereStr += filter;
      }
    }
    if (startDateStr != null && !"".equals(startDateStr)) {
      String dbDateF = YHDBUtility.getDateFilter(emailBodyTable + ".SEND_TIME",
          startDateStr, " >= ");
      whereStr += " and " + dbDateF;
    }
    if (endDateStr != null && !"".equals(endDateStr)) {
      String dbDateF = YHDBUtility.getDateFilter(emailBodyTable + ".SEND_TIME",
          endDateStr, " <= ");
      whereStr += " and " + dbDateF;
    }
    // 加上日志类型条件
    if (mailStatus != null && !"".equals(mailStatus)) {
      if (type == 1) {
        whereStr += " and " + emailTable + ".READ_FLAG='" + mailStatus + "'";
      }
    }
    // 加上标题条件
    if (subject != null && !"".equals(subject)) {
      whereStr += " and " + emailBodyTable + ".SUBJECT like '%" + subject
          + "%'" + YHDBUtility.escapeLike();
    }
    if (attachmentName != null && !"".equals(attachmentName)) {
      whereStr += " and " + emailBodyTable + ".ATTACHMENT_NAME like '%"
          + attachmentName + "%'" + YHDBUtility.escapeLike();
    }
    // 加上三个关键词条件，关键词对应CONTENT字段（CONTENT应该是滤掉了html格式之后的文本内容）
    if ((key1 != null && !"".equals(key1))
        || (key2 != null && !"".equals(key2))
        || (key3 != null && !"".equals(key3))) {
      if (key1 == null || "".equals(key1)) {
        key1 = "!@#$%^&*()__)(*&^%$#@";
      }
      if (key2 == null || "".equals(key2)) {
        key2 = "!@#$%^&*()__)(*&^%$#@";
      }
      if (key3 == null || "".equals(key3)) {
        key3 = "!@#$%^&*()__)(*&^%$#@";
      }
      whereStr += " and (" + emailBodyTable + ".COMPRESS_CONTENT like '%"
          + key1 + "%' " + YHDBUtility.escapeLike() + " or " + emailBodyTable
          + ".COMPRESS_CONTENT like '%" + key2 + "%' "
          + YHDBUtility.escapeLike() + "  or " + emailBodyTable
          + ".COMPRESS_CONTENT like '%" + key3 + "%' "
          + YHDBUtility.escapeLike() + " )";
    }
    return whereStr;
  }

  /**
   * mysql findInSet 处理
   * 
   * @param str
   * @param dbFieldName
   * @return
   * @throws SQLException
   */
  public static String findInId(String str, String dbFieldName)
      throws SQLException {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    String result = "";
    if (dbms.equals("sqlserver")) {
      result = "dbo.find_in_set('" + str + "'," + dbFieldName + ")>0";
    } else if (dbms.equals("mysql")) {
      result = "find_in_set(" + dbFieldName + "," + str + ")>0";
    } else if (dbms.equals("oracle")) {
      result = "instr('," + str + ",',','||" + dbFieldName + "||',') > 0";
    } else {
      throw new SQLException("not accepted dbms");
    }

    return result;
  }

  public String getRepceipt(Connection conn, int userId, int bodyId)
      throws Exception {
    String sql = "select RECEIPT from oa_email where BODY_ID = " + bodyId
        + " and TO_ID = '" + userId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String receipt = "";
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        receipt = rs.getString(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return receipt;
  }

  /**
   * 阅读回执
   * 
   * @param conn
   * @param userId
   * @param bodyId
   * @param toId
   * @param subject
   * @throws Exception
   */
  public void repceipt(Connection conn, int userId, int bodyId, String toId,
      String subject, String contextPath) throws Exception {
    String repceipt = getRepceipt(conn, userId, bodyId);
    if ("1".equals(repceipt)) {
      YHSmsBack sb = new YHSmsBack();
      String userName = getUserName(conn, userId);
      String content = userName + " 于 " + YHUtility.getDateTimeStr(new Date())
          + " 阅读了您发的邮件。<br>标题：" + subject;
      String remindUrl = "/core/funcs/email/inbox/read_email/index.jsp?seqId="
          + bodyId;
      sb.setContent(content);
      sb.setFromId(userId);
      sb.setToId(toId);
      sb.setSmsType("2");
      sb.setRemindUrl(remindUrl);
      YHSmsUtil.smsBack(conn, sb);
      emul.updateFlag(conn, "RECEIPT", "2", String.valueOf(bodyId), "oa_email",
          userId);
    }
  }

  /**
   * 取得用户名
   * 
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public String getUserName(Connection conn, int userId) throws Exception {
    String result = "";
    String sql = " select USER_NAME from PERSON where SEQ_ID = " + userId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
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

  /**
   * 得到邮箱的总容量
   * 
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public int getCountSize(Connection conn, int userId) throws Exception {
    int result = 0;
    YHEmailBoxLogic ebl = new YHEmailBoxLogic();
    YHORM orm = new YHORM();
    String[] names = new String[] { "PAGESIZE_OUT", "PAGESIZE_DEL",
        "PAGESIZE_SENT", "PAGESIZE_WEB", "PAGESIZE_IN0" };
    for (String name : names) {
      result += ebl.getSizeByBoxNo(conn, userId, name);
    }
    String[] filters = new String[] { " USER_ID = " + userId
        + " AND NOT BOX_NO = 0 " };
    ArrayList<YHEmailBox> eboxList = (ArrayList<YHEmailBox>) orm
        .loadListSingle(conn, YHEmailBox.class, filters);
    for (YHEmailBox box : eboxList) {
      result += ebl.getSizeByBoxNo(conn, userId, box.getSeqId());
    }
    return result;
  }

  /**
   * 判断邮箱容量是否已满
   * 
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public StringBuffer isFull(Connection conn, int mailSize, int userId)
      throws Exception {
    StringBuffer sb = new StringBuffer();
    
    if (mailSize < 1) {
      sb.append("{isFull:false}");
      return sb;
    }
    boolean result = false;
    long size = 0;
    long sendBox = 0;
    long inBox = 0;
    YHEmailBoxLogic ebl = new YHEmailBoxLogic();
    String[] names = new String[] { "PAGESIZE_DEL", "PAGESIZE_IN0" };

    for (String name : names) {
      inBox += ebl.getSizeByBoxNo(conn, userId, name);
    }
    sendBox += ebl.getSizeByBoxNo(conn, userId, "PAGESIZE_SENT");
    ArrayList<YHEmailBox> eblist = ebl.listBoxByUser(conn, userId, false);
    for (YHEmailBox yhEmailBox : eblist) {
      inBox += ebl.getBoxSizeById(conn, yhEmailBox.getSeqId());
    }
    size += inBox + sendBox;
    long emailCa = mailSize * 1024 * 1024;
    if (mailSize > 0 && emailCa <= size) {
      result = true;
    }

    sb.append("{").append("sendBox:").append(sendBox).append(",inBox:").append(
        inBox).append(",mailSize:").append(emailCa).append(",isFull:").append(
        result).append("}");
    return sb;
  }

  /**
   * 
   * @param conn
   * @param mailSize
   * @param userId
   * @return
   * @throws Exception
   */
  public StringBuffer getSpareCapacity(Connection conn, int mailSize, int userId)
      throws Exception {
    StringBuffer sb = new StringBuffer();
    if (mailSize < 1) {
      sb.append("{spareCapacitySize:" + (YHSysProps.getInt(YHSysPropKeys.MAX_UPLOAD_FILE_SIZE) * YHConst.M) + "}");
      return sb;
    }
    
    long spareCapacitySize = 0l;
    long size = 0;
    long sendBox = 0;
    long inBox = 0;
    YHEmailBoxLogic ebl = new YHEmailBoxLogic();
    String[] names = new String[] { "PAGESIZE_DEL", "PAGESIZE_IN0" };

    for (String name : names) {
      inBox += ebl.getSizeByBoxNo(conn, userId, name);
    }
    sendBox += ebl.getSizeByBoxNo(conn, userId, "PAGESIZE_SENT");
    ArrayList<YHEmailBox> eblist = ebl.listBoxByUser(conn, userId, false);
    for (YHEmailBox yhEmailBox : eblist) {
      inBox += ebl.getBoxSizeById(conn, yhEmailBox.getSeqId());
    }
    size += inBox + sendBox;
    long emailCa = mailSize * 1024 * 1024;
    if (mailSize == 0) {
      spareCapacitySize = -1;
    } else if (emailCa >= size) {
      spareCapacitySize = emailCa - size;
    } else {
      spareCapacitySize = 0;
    }

    sb.append("{").append("spareCapacitySize:").append(spareCapacitySize)
        .append("}");
    return sb;
  }

  /**
   * 取得当前用户的桌面邮件数
   * 
   * @param conn
   * @param request
   * @param userId
   * @param type
   * @return
   * @throws Exception
   */
  public StringBuffer getDeskMoudel(Connection conn, Map request, int userId,
      int type,String contextPath,int styleInSession) throws Exception {
    StringBuffer sub = new StringBuffer();
    String readFlagField = " ";
    if (type == 1) {
      readFlagField = " AND T1.READ_FLAG= '0'";
    } else if (type == 2) {
      readFlagField = " AND T1.READ_FLAG= '1'";
    }
  //添加一个查询条件 READ_FLAG shenrm 2012-12-06
    String sql = "select " + "T0.SEQ_ID," + "T1.SEQ_ID," + "T0.FROM_ID,"
        + "SUBJECT,T0.IS_WEBMAIL" + ",T1.READ_FLAG" + " from " + "oa_email_body T0," + "oa_email T1  " + " where "
        + " T1.TO_ID ='" + userId + "'  " + readFlagField
        + " AND T1.DELETE_FLAG IN('0','2') " + " AND T1.BODY_ID = T0.SEQ_ID";
    String sql2 = "select " + "count(T0.SEQ_ID)" + " from " + "oa_email_body T0,"
        + "oa_email T1  " + " where " + " T1.TO_ID ='" + userId + "'  "
        + readFlagField + " AND T1.DELETE_FLAG IN('0','2') "
        + " AND T1.BODY_ID = T0.SEQ_ID";
    String query = " order by T0.SEND_TIME desc";
    sql += query;
    sql2 += query;
   // System.out.println(sql);
    String nameStr = "emailBodyId,emailId,fromId,subject,isWebmail,readflag"; //添加一个查询条件
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request,
        YHPageQueryParam.class, null);
    queryParam.setNameStr(nameStr);// 取得别名
    queryParam.setPageIndex(0);// 只取得第一页
    // int pageSize = getTotale(conn, sql2) ;
    queryParam.setPageSize(10);// 只取得第一页
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,
        sql);
    pageDataList = listBoxHandlerByDesk(conn, pageDataList, userId, contextPath, styleInSession);
    int totle = pageDataList.getTotalRecord();// 处理显示条数;
    if (totle > queryParam.getPageSize()) {
      pageDataList.setTotalRecord(queryParam.getPageSize());
    }
    sub.append(pageDataList.toJson());
    return sub;
  }

  public static int getTotale(Connection conn, String sql) throws Exception {
    int result = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }

  /**
   * 
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public String getLinkMan(Connection conn, int userId) throws Exception {
    String result = "";
    String sql = "select TO_ID,COPY_TO_ID,SECRET_TO_ID from oa_email_body WHERE  FROM_ID = " + userId
        + " order by SEND_TIME DESC";
    PreparedStatement ps = null;
    ResultSet rs = null;
    ArrayList<String> ids = new ArrayList<String>();
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      String manToIds = "";
      int manCount = 0;
      while (rs.next() && manCount < 5) {
        String allToId = "";
        String toId = rs.getString(1);
        String copyToId = rs.getString(2);
        String secretToId = rs.getString(3);
        if(toId != null && !"".equals(toId)){
          allToId += "," + toId;
        }
        if(copyToId != null && !"".equals(copyToId)){
          allToId += "," + copyToId;
        }
        if(secretToId != null && !"".equals(secretToId)){
          allToId += "," + secretToId;
        }
        String[] allToIdArray = allToId.split(",");
        for (int i = 0; i < allToIdArray.length && manCount < 5; i++) {
          if("".equals(allToIdArray[i].trim()) || String.valueOf(userId).equals(allToIdArray[i].trim()) 
              || "-1".equals(allToIdArray[i].trim()) ){
            continue;
          }
          if (ids.contains(allToIdArray[i].trim())) {
            continue;
          } else {
            ids.add(allToIdArray[i].trim());
            manCount ++;
          }
        }
      }
      
      if(ids.size() > 0){
        result = getLinkManName(conn, ids);
      }
    } catch (Exception e) {
      throw e;
    }
    return "[" + result + "]";
  }
  
  public String getLinkManName(Connection conn,ArrayList<String> ids) throws Exception{
    String idstr = "";
    for (int i = 0; i < ids.size(); i++) {
      if(!"".equals(ids.get(i))){
        if(!"".equals(idstr)){
          idstr += ",";
        }
        idstr += ids.get(i);
      }
    }
    String sql = "select SEQ_ID,USER_NAME from person where SEQ_ID IN(" + idstr + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String result = "";
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        int fromId = rs.getInt(1);
        String userName = rs.getString(2);
        if (!"".equals(result)) {
          result += ",";
        }
        result += "{userId:" + fromId + ",userName:\""
           + YHUtility.encodeSpecial(userName) + "\"}";
      }
      return result;
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, null);
    }
  }
  /**
   * 
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public String getLinkMan2(Connection conn, int userId) throws Exception {
    String result = "";
    String sql = "select FROM_ID,USER_NAME from oa_email_body,PERSON WHERE FROM_ID = PERSON.SEQ_ID and FROM_ID != "
        + userId
        + " AND "
        + YHDBUtility.findInSet(String.valueOf(userId), "TO_ID")
        + " order by SEND_TIME DESC";
    PreparedStatement ps = null;
    ResultSet rs = null;
    ArrayList<Integer> ids = new ArrayList<Integer>();
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      for (int i = 0; i < 5 && rs.next();) {
        int fromId = rs.getInt(1);
        if (ids.contains(fromId)) {
          continue;
        } else {
          ids.add(fromId);
          String userName = rs.getString(2);
          if (!"".equals(result)) {
            result += ",";
          }
          result += "{userId:" + fromId + ",userName:\""
              + YHUtility.encodeSpecial(userName) + "\"}";
          i++;
        }

      }
    } catch (Exception e) {
      throw e;
    }
    return "[" + result + "]";
  }
  /**
   * 
   * @param conn
   * @param emailBodyId
   * @return
   * @throws Exception
   */
  public boolean isHasEmail(Connection conn, int emailBodyId) throws Exception {
    boolean result = true;
    String sql = "SELECT count(SEQ_ID) FROM EMAIL WHERE BODY_ID=" + emailBodyId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        int count = rs.getInt(1);
        if (count > 0) {
          result = true;
        } else {
          result = false;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }

  /**
   * 标记为已读
   * 
   * @param conn
   * @param mailId
   * @param fieldValue
   * @throws Exception
   */
  public void readFlagLogic(Connection conn, String mailId, String fieldValue)
      throws Exception {
    if (!YHUtility.isNullorEmpty(mailId)) {
      String sql = "UPDATE oa_email SET READ_FLAG = '" + fieldValue + "'"
        + " WHERE SEQ_ID IN (" + mailId + ")";
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
        ps = conn.prepareStatement(sql);
        ps.executeUpdate();
      } catch (Exception e) {
        throw e;
      } finally {
        YHDBUtility.close(ps, rs, null);
      }
    }
   
  }

  /**
   * 
   * @param conn
   * @param bodyId
   * @throws Exception
   */
  public void sendMailByCoren(Connection conn, int bodyId) throws Exception {
    String sql = "UPDATE oa_email_BODY SET SEND_FLAG = '1' WHERE SEQ_ID IN( "
        + bodyId + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }

  public ArrayList<YHDbRecord> toExportMailData(Connection conn,
      String emailIds, int userId) throws Exception {
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
    String sql = "SELECT "
        + " FROM_ID "
        + ",oa_email.TO_ID "
        + ",oa_email_body.TO_ID "
        + ",oa_email_body.COPY_TO_ID "
        + ",oa_email_body.SUBJECT"
        + ",oa_email_body.SEND_TIME"
        + ",oa_email_body.ATTACHMENT_NAME"
        + ",oa_email_body.IMPORTANT"
        + ",oa_email_body.CONTENT"
        + ",oa_email_body.IS_WEBMAIL"
        + ",oa_email_body.FROM_WEBMAIL"
        + ",oa_email_body.WEBMAIL_CONTENT"
        + ",oa_email_body.TO_WEBMAIL"
        + ",oa_email_body.SEQ_ID "
        + " from oa_email,oa_email_body where (oa_email_body.FROM_ID = "
        + userId
        + " or oa_email.TO_ID = '"
        + userId
        + "' ) and EMAIL.BODY_ID = oa_email_body.SEQ_ID and oa_email_body.SEQ_ID in ("
        + emailIds + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      ArrayList<Integer> ids = new ArrayList<Integer>();
      while (rs.next()) {
        int seqId = rs.getInt(14);
        if(ids.contains(seqId)){
          continue;
        }
        ids.add(seqId);
        YHDbRecord record = new YHDbRecord();
        int fromId = rs.getInt(1);
        String toId = rs.getString(2);
        String toId2 = rs.getString(3);
        String coptToId = rs.getString(4);
        String subject = rs.getString(5);
        Date sendTime = rs.getTimestamp(6);
        String attachName = rs.getString(7);
        String important = rs.getString(8);
        String content = rs.getString(9);
        String isWebmail = rs.getString(10);
        String fromWebmail = rs.getString(11);
        String webmailContent = rs.getString(12);
        String toWebmail = rs.getString(13);
      
        String fromName = "";
        if ("1".equals(important)) {
          important = "重要邮件";
        } else if ("2".equals(important)) {
          important = "非常重要";
        } else {
          important = "";
        }

        if (isWebmail != null && "1".equals(isWebmail)) {
          content = webmailContent;
          fromName = fromWebmail;
          toWebmail = "";
        } else {
          fromName = getUserName(conn, fromId);
        }
        record.addField("发件人", fromName);
        record.addField("收件人", getUserName(conn, toId2).toString());
        record.addField("外部收件人", toWebmail);
        record.addField("抄送", getUserName(conn, coptToId).toString());
        record.addField("重要级别", important);
        record.addField("主题", subject);
        record.addField("时间", YHUtility.getDateTimeStr(sendTime));
        record.addField("内容", content);
        record.addField("附件名称", attachName);
        result.add(record);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }

  public ArrayList<YHDbRecord> toExportMailData2(Connection conn,
      String emailIds, int userId) throws Exception {
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
    String sql = "SELECT " + " FROM_ID " + ",oa_email_body.TO_ID "
        + ",oa_email_body.COPY_TO_ID " + ",oa_email_body.SUBJECT"
        + ",oa_email_body.SEND_TIME" + ",oa_email_body.ATTACHMENT_NAME"
        + ",oa_email_body.IMPORTANT" + ",oa_email_body.CONTENT"
        + " from oa_email_body where  oa_email_body.SEQ_ID in (" + emailIds + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        YHDbRecord record = new YHDbRecord();
        int fromId = rs.getInt(1);
        String toId2 = rs.getString(2);
        String coptToId = rs.getString(3);
        String subject = rs.getString(4);
        Date sendTime = rs.getTimestamp(5);
        String attachName = rs.getString(6);
        String important = rs.getString(7);
        String content = rs.getString(8);

        if ("1".equals(important)) {
          important = "重要邮件";
        } else if ("2".equals(important)) {
          important = "非常重要";
        } else {
          important = "";
        }

        record.addField("发件人", getUserName(conn, fromId));
        record.addField("收件人", getUserName(conn, toId2).toString());
        record.addField("抄送", getUserName(conn, coptToId).toString());
        record.addField("重要级别", important);
        record.addField("主题", subject);
        record.addField("时间", YHUtility.getDateTimeStr(sendTime));
        record.addField("内容", content);
        record.addField("附件名称", attachName);
        result.add(record);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }

  public StringBuffer getUserName(Connection conn, String userIds)
      throws Exception {
    StringBuffer result = new StringBuffer();

    if (userIds == null || "".equals(userIds.trim())) {
      return result;
    }
    if (userIds.trim().endsWith(",")) {
      userIds = userIds.trim().substring(0, userIds.trim().length() - 1);
    }
    String sql = "SELECT USER_NAME from PERSON where SEQ_ID IN (" + userIds
        + ")";
    // YHDBUtility.
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        String userName = rs.getString(1);
        if (!"".equals(result.toString())) {
          result.append(",");
        }
        result.append(userName);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }

  /**
   * zip打包
   * 
   * @param map
   * @return
   * @throws IOException
   */
  public OutputStream zip(Map<String, byte[]> map, OutputStream baos)
      throws IOException {
    ZipOutputStream zos = new ZipOutputStream(baos);
    zos.setEncoding("GBK");
    ZipEntry zipEntry = null;
    for (String key : map.keySet()) {
      zipEntry = new ZipEntry(key);
      zipEntry.setSize(map.get(key).length);
      zipEntry.setTime(System.currentTimeMillis());
      zos.putNextEntry(zipEntry);
      zos.write(map.get(key));
      zos.flush();
    }
    zos.close();
    return baos;
  }

  /**
   * 组成一串字节数组
   * 
   * @param conn
   * @param userId
   * @param bodyIds
   * @return
   * @throws Exception
   */
  public Map<String, byte[]> toEmlByteMap(Connection conn, int userId,
      String bodyIds) throws Exception {
    Map<String, byte[]> result = new HashMap<String, byte[]>();
    HashMap<String, Integer> filesName = new HashMap<String, Integer>();
    String sql = "SELECT "
        + " FROM_ID "
        + ",oa_email.TO_ID "
        + ",oa_email_body.TO_ID "
        + ",oa_email_body.COPY_TO_ID "
        + ",oa_email_body.SUBJECT"
        + ",oa_email_body.SEND_TIME"
        + ",oa_email_body.ATTACHMENT_NAME"
        + ",oa_email_body.IMPORTANT"
        + ",oa_email_body.CONTENT"
        + ",oa_email_body.ATTACHMENT_ID"
        + ",oa_email_body.IS_WEBMAIL"
        + ",oa_email_body.TO_WEBMAIL"
        + ",oa_email_body.FROM_WEBMAIL"
        + ",oa_email_body.WEBMAIL_CONTENT"
        + ",oa_email_body.SEQ_ID "
        + " from oa_email,oa_email_body where (oa_email_body.FROM_ID = "
        + userId
        + " or oa_email.TO_ID = '"
        + userId
        + "' ) and oa_email.BODY_ID = oa_email_body.SEQ_ID and oa_email_body.SEQ_ID in ("
        + bodyIds + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      ArrayList<Integer> ids = new ArrayList<Integer>();
      while (rs.next()) {
        int seqid = rs.getInt(15);
        if(ids.contains(seqid)){
          continue;
        }
        ids.add(seqid);
        byte[] emlBytes = null;
        int fromId = rs.getInt(1);
        String toId = rs.getString(2);
        String toId2 = rs.getString(3);
        String copyToId = rs.getString(4);
        String subject = rs.getString(5);
        Date sendTime = rs.getTimestamp(6);
        String attachName = rs.getString(7);
        String important = rs.getString(8);
        String content = rs.getString(9);
        String attachId = rs.getString(10);
        String isWebmail = rs.getString(11);
        String toWebmail = rs.getString(12);
        String fromWebmail = rs.getString(13);
        String webmailContent = rs.getString(14);

        if ("1".equals(important)) {
          important = "重要邮件";
        } else if ("2".equals(important)) {
          important = "非常重要";
        } else {
          important = "";
        }
        if (isWebmail != null && "1".equals(isWebmail)) {
          content = webmailContent;
        }
        emlBytes = toEmlByte(conn, String.valueOf(fromId), toId, copyToId,
            subject, sendTime, content, attachName, attachId, fromWebmail,
            toWebmail);
        String fileName = "";
        if (filesName.keySet().contains(subject.trim())) {
          int count = filesName.get(subject.trim());
          fileName = subject + "_" + count;
          filesName.put(subject.trim(), count + 1);
        } else {
          filesName.put(subject.trim(), 1);
          fileName = subject;
        }
        fileName += ".eml";
        result.put(fileName, emlBytes);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }

  /**
   * 组成一串字节数组
   * 
   * @param conn
   * @param userId
   * @param bodyIds
   * @return
   * @throws Exception
   */
  public Map<String, byte[]> toEmlByteMap2(Connection conn, int userId,
      String bodyIds) throws Exception {
    Map<String, byte[]> result = new HashMap<String, byte[]>();
    HashMap<String, Integer> filesName = new HashMap<String, Integer>();
    String sql = "SELECT " 
      + " FROM_ID " 
      + ",oa_email_body.TO_ID "
      + ",oa_email_body.COPY_TO_ID " 
      + ",oa_email_body.SUBJECT"
      + ",oa_email_body.SEND_TIME" 
      + ",oa_email_body.ATTACHMENT_NAME"
      + ",oa_email_body.IMPORTANT" 
      + ",oa_email_body.CONTENT"
      + ",oa_email_body.ATTACHMENT_ID" 
      + ",oa_email_body.IS_WEBMAIL"
      + ",oa_email_body.TO_WEBMAIL" 
      + ",oa_email_body.FROM_WEBMAIL"
      + ",oa_email_body.WEBMAIL_CONTENT"
      + " from oa_email_body where oa_email_body.SEQ_ID in (" + bodyIds + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        byte[] emlBytes = null;
        int fromId = rs.getInt(1);
        String toId2 = rs.getString(2) == null ? "" : rs.getString(2) ;
        String copyToId = rs.getString(3)  == null ? "" : rs.getString(3) ;
        String subject = rs.getString(4);
        Date sendTime = rs.getTimestamp(5);
        String attachName = rs.getString(6);
        String important = rs.getString(7);
        String content = rs.getString(8);
        String attachId = rs.getString(9);
        String isWebmail = rs.getString(10);
        String toWebmail = rs.getString(11);
        String fromWebmail = rs.getString(12);
        String webmailContent = rs.getString(13);
        if ("1".equals(important)) {
          important = "重要邮件";
        } else if ("2".equals(important)) {
          important = "非常重要";
        } else {
          important = "";
        }
        emlBytes = toEmlByte(conn, String.valueOf(fromId), toId2, copyToId,
            subject, sendTime, content, attachName, attachId, fromWebmail,
            toWebmail);
        String fileName = "";
        if (filesName.keySet().contains(subject.trim())) {
          int count = filesName.get(subject.trim());
          fileName = subject + "_" + count;
          filesName.put(subject.trim(), count + 1);
        } else {
          filesName.put(subject.trim(), 1);
          fileName = subject;
        }
        fileName += ".eml";
        result.put(fileName, emlBytes);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }

  /**
   * 组装单个eml文件
   * 
   * @return
   * @throws Exception
   */
  public byte[] toEmlByte(Connection conn, String fromId, String toId,
      String copyToId, String subject, Date sendTime, String content,
      String attachmentName, String attachmentId, String fromWebmail,
      String toWebmail) throws Exception {
    byte[] result = null;
    BASE64Encoder encoder = new BASE64Encoder();
    String fromName = getUserName(conn, fromId).toString();
    String toName = getUserName(conn, toId).toString();
    String copyToName = getUserName(conn, copyToId).toString();
    String fileContent = "";
    String from = fromName + " <" + fromId + ">";
    if (fromName == null || "".equals(fromName)) {
      from = "";
    }
    String to = toName + " <" + toId + ">";
    if (fromWebmail != null && !"".equals(fromWebmail)) {
      if (!"".equals(from)) {
        from += ",";
      }
      from += fromWebmail;
    }
    if (toWebmail != null && !"".equals(toWebmail)) {
      to += "," + toWebmail;
    }
    fileContent = "Date: " + YHUtility.getDateTimeStr(sendTime) + " \n";
    fileContent += "From: " + from + " \n";
    fileContent += "MIME-Version: 1.0\n";
    fileContent += "To: " + to + " \n";
    if (copyToId != null || "".equals(copyToId)) {
      fileContent += "Cc: " + copyToName + " <"
          + (copyToId == null ? "" : copyToId) + "> \n";
    }
    fileContent += "Subject: " + subject + "\n";
    fileContent += "Content-Type: multipart/mixed; boundary=\"=========="+StaticData.YIHENG+"==========\"\n\n";
    fileContent += "This is a multi-part message in MIME format.\n";
    fileContent += "--=========="+StaticData.YIHENG+"==========\n";
    fileContent += "Content-Type: text/html; charset=\"GBK\"\n";
    fileContent += "Content-Transfer-Encoding: base64\n\n";
    if (content == null) {
      content = "";
    }
    fileContent += encoder.encode(content.getBytes("GBK")) + " \n";
    attachmentName = attachmentName == null ? "" : attachmentName;
    attachmentId = attachmentId == null ? "" : attachmentId;
    String[] attachMentNameArray = attachmentName.split("\\*");
    String[] attachMentIdArray = attachmentId.split(",");
    for (int i = 0; i < attachMentIdArray.length; i++) {
      if ("".equals(attachMentNameArray[i].trim())
          || "".equals(attachMentIdArray[i].trim())) {
        continue;
      }
      File file = getAttachRealPath(attachMentNameArray[i],
          attachMentIdArray[i], YHEmailCont.MODULE);
      if (file == null || !file.exists()) {
        continue;
      }
      fileContent += "--=========="+StaticData.YIHENG+"==========\n";
      fileContent += "Content-Type: application/octet-stream; name=\""
          + attachMentNameArray[i] + "\"\n";
      fileContent += "Content-Transfer-Encoding: base64\n";
      fileContent += "Content-Disposition: attachment; filename=\""
          + attachMentNameArray[i] + "\"\n\n";

      FileInputStream in = new FileInputStream(file);
      byte[] readBuf = new byte[1140];
      int readLen = 0;
      int offset = 0;
      while ((readLen = in.read(readBuf)) > 0) {
        if (readLen < 1140) {
          byte[] tmpBuf = new byte[readLen];
          System.arraycopy(readBuf, 0, tmpBuf, 0, readLen);
          fileContent += encoder.encode(tmpBuf);
        }else {
          fileContent += encoder.encode(readBuf);
        }
        offset += readLen;
      }
      if (offset > 0) {
        fileContent += "\n\n";
      }
    }
    fileContent += "--=========="+StaticData.YIHENG+"==========--";
    result = fileContent.getBytes("GBK");
    return result;
  }

  private File getAttachRealPath(String attachmentName, String attachmentId,
      String module) {
    String fileName = "";
    String path = "";
    File file = null;
    if (attachmentId != null && !"".equals(attachmentId)) {
      if (attachmentId.indexOf("_") > 0) {
        String attIds[] = attachmentId.split("_");
        fileName = attIds[1] + "." + attachmentName;
        path = YHNtkoCont.ATTA_PATH + File.separator + module + File.separator + attIds[0] + File.separator
            + fileName;
      } else {
        fileName = attachmentId + "." + attachmentName;
        path = YHNtkoCont.ATTA_PATH + File.separator + module + File.separator + fileName;
      }
      file = new File(path);
      if (!file.exists()) {
        if (attachmentId.indexOf("_") > 0) {
          String attIds[] = attachmentId.split("_");
          fileName = attIds[1] + "_" + attachmentName;
          path = YHNtkoCont.ATTA_PATH + File.separator + module + File.separator + attIds[0] + File.separator
              + fileName;
        } else {
          fileName = attachmentId + "_" + attachmentName;
          path = YHNtkoCont.ATTA_PATH + File.separator + module + File.separator + fileName;
        }
        file = new File(path);
      }
      if (!file.exists()) {
        return null;
      }
    }
    return file;
  }

  public String getAttachInfoLogic(Connection conn, String emailIds)
      throws Exception {
    String result = "";
    String sql = "select ATTACHMENT_NAME,ATTACHMENT_ID FROM oa_email_body WHERE SEQ_ID in("
        + emailIds + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String attachmentName = "";
    String attachmentId = "";
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        if (isEmpty(rs.getString(1)) || isEmpty(rs.getString(2))) {
          continue;
        }
        if (!"".equals(attachmentName) && !attachmentName.endsWith("\\*")) {
          attachmentName += "*";
        }
        if (!"".equals(attachmentId) && !attachmentId.endsWith("\\*")) {
          attachmentId += ",";
        }
        attachmentName += rs.getString(1);
        attachmentId += rs.getString(2);
      }

      result += "{attachmentName:\"" + attachmentName + "\",attachmentId:\""
          + attachmentId + "\"}";
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }

  private boolean isEmpty(String str) {
    if (str == null || "".equals(str.trim())) {
      return true;
    } else {
      return false;
    }

  }

  
  
	/**
	 * add by zyy 张银友 工作流发送邮件
	 * 
	 * @param conn
	 * @param requestMap
	 * @param person
	 * @param imgPath 
	 * @return
	 * @throws Exception 
	 */
	public int sendMailLogic(Connection conn, Map<String, String[]> requestMap,
			YHPerson person, String imgPath) throws Exception {
		
		int fromId=person.getSeqId();
	    YHORM orm = new YHORM();
	    String toId = "";
	    
	      String runId="";
		  String flowId="";
		  Map request=new HashMap();
		  
		  for(String s:requestMap.keySet()){
			 String[] str = requestMap.get(s);
			 request.put(s, str[0]);
			 if("runId".equals(s)){
				 runId=str[0];
			 }else if("flowId".equals(s)){
				 flowId=str[0];
			 }
			 System.out.println(s+":"+str[0]);
		  }
		  
	    String smsRemind = (String) request.get("smsRemind");
	    String sms2Remind = (String) request.get("sms2Remind");
	    String sendFlag = "1";
	    String sendEdit = null;
	    YHEmailBody eb = null;
	    int bodyId = 0;
	    int result = 0;
	    try {
	      try {
	        sendEdit = request.get("sendEdit") == null ? null : ((String[]) request
	            .get("sendEdit"))[0];
	      } catch (Exception e) {
	        sendEdit = request.get("sendEdit") == null ? null : (String) request
	            .get("sendEdit");
	      }
	      try {
	        sendFlag = (String[]) request.get("sendFlag") == null ? "1"
	            : ((String[]) request.get("sendFlag"))[0];
	      } catch (Exception e) {
	        sendFlag = (String) request.get("sendFlag") == null ? "1"
	            : (String) request.get("sendFlag");
	      }
	      if ("0".equals(sendFlag)) {
	        return 1;
	      }
	      String repict = "1";
	      try {
	        repict = (String[]) request.get("receipt") == null ? "0"
	            : ((String[]) request.get("receipt"))[0];
	      } catch (Exception e) {
	        repict = (String) request.get("receipt") == null ? "0" : (String) request
	            .get("receipt");
	      }

	  
	      try {
	        toId = ((String[]) request.get("toId"))[0];
	      } catch (Exception e) {
	        toId = (String) request.get("toId");
	      }
	      String copyToId = "";
	      try {
	        copyToId = ((String[]) request.get("copyToId"))[0];

	      } catch (Exception e) {
	        copyToId = (String) request.get("copyToId");
	      }
	      String secretToId = "";
	      try {
	        secretToId = ((String[]) request.get("secretToId"))[0];

	      } catch (Exception e) {
	        secretToId = (String) request.get("secretToId");
	      }
	      long esize = 0l;
	      YHFlowUtil util=new YHFlowUtil();
//	      Iterator<String> iKeys = fileForm.iterateFileFields();
//	      while (iKeys.hasNext()) {
//	        String fieldName = iKeys.next();
//	        esize += fileForm.getFileSize(fieldName);
//	      }
	      eb = (YHEmailBody) YHFOM
	          .build(request, YHEmailBody.class, null);
//	      esize = eb.getEnsize() + getSize(attr);
//	      eb.setEnsize(esize);
	      
	      eb.setFromId(fromId);
	      eb.setSendFlag(sendFlag);
	      eb.setContent(util.getContentFromFlow(person, runId, flowId, conn, imgPath));
	      eb.setCompressContent(YHDiaryUtil.cutHtml(eb.getContent()));
	      eb.setSendTime(new Date());
	      if (eb.getImportant() == null || "".equals(eb.getImportant())) {
	        eb.setImportant("0");
	      }
	      if (eb.getSubject() == null || "".equals(eb.getSubject())) {
	        String subject ="[无主题]";
	        eb.setSubject(subject);
	      }
	      if ("0".equals(sendFlag) || "2".equals(sendFlag)) {
	        eb.setSendFlag("0");
	      } else {
	        eb.setSendFlag("1");
	      }
	      int emailId = 0;
	      if (eb.getSeqId() != 0 && (sendEdit == null || "".equals(sendEdit.trim()))) {
	        orm.updateSingle(conn, eb);// 先保存正文


	        bodyId = eb.getSeqId();
	      } else {
	        orm.saveSingle(conn, eb);// 先保存正文

	        bodyId = emul.getBodyId(conn);
	      }
	      //若有附件，把附件复制过去
		  if(!"".equals(eb.getAttachmentId())){
			  util.copyAttachFlowToWhere(eb.getAttachmentName(),eb.getAttachmentId(),"email");
		  }
	      if ("2".equals(sendFlag)) {
	        smsRemind = "";
	        String jsonStr = "{BODY_ID:" + bodyId + ",FROM_ID:" + fromId
	            + ", TO_ID:\"" + toId + "\",SUBJECT:\""
	            + YHUtility.encodeSpecial(eb.getSubject()) + "\", CONTENT:\""
	            + YHUtility.encodeSpecial(eb.getContent()) + "\", SEND_TIME:\""
	            + YHUtility.getDateTimeStr(eb.getSendTime()) + "\", RECEIPT:\""
	            + repict + "\"}";
	        YHCensorCheckLogic.addJsonContent(conn, "0", jsonStr, fromId);
	        result = 2;
	      }
	      ArrayList<String> ids = new ArrayList<String>();
	      if (toId != null && !"".equals(toId)) {
	        ids = emul.addArray(ids, toId.split(","));
	      }
	      if (secretToId != null && !"".equals(secretToId)) {
	        ids = emul.addArray(ids, secretToId.split(","));
	      }
	      if (copyToId != null && !"".equals(copyToId)) {
	        ids = emul.addArray(ids, copyToId.split(","));
	      }
	      for (int i = 0; ids != null && i < ids.size(); i++) {
	        String id = ids.get(i);
	        if ("".equals(id)) {
	          continue;
	        }
	        YHEmail em = (YHEmail) YHFOM.build(request, YHEmail.class, null);
	        em.setBodyId(bodyId);
	        em.setToId(id);
	        if (eb.getToId() == null || "".equals(eb.getToId())) {
	          em.setToId("-1");
	        }
	        orm.saveSingle(conn, em);
	        emailId = emul.getBodyId(conn, "oa_email");
	        if ("1".equals(smsRemind)) {
	          smsRmind(conn, request, bodyId, fromId, emailId, id);
	        }
	      }
	      if ("1".equals(sms2Remind)) {
	        String smsContent = "OA邮件,来自 " + person.getUserName() + ":" + eb.getSubject();
	        YHMobileSms2Logic ms2l = new YHMobileSms2Logic();
	        String sms2ToId = toId + "," + copyToId + "," + secretToId;
	        ms2l.remindByMobileSms(conn, sms2ToId, fromId, smsContent, new Date());
	      }
	    } catch (Exception e) {
	      throw new Exception("1");
	    }
	    
	   
	    /**
	     * 发送外部邮件

	     */
	    try {
	      if (!YHUtility.isNullorEmpty(eb.getToWebmail().trim())) {
	        YHWebmailLogic wbml = new YHWebmailLogic();
	        wbml.sendWebMail(conn, eb, bodyId);
	      }
	    } catch (Exception e) {
	      throw new Exception("2");
	    }
	    return result;
	}
}
