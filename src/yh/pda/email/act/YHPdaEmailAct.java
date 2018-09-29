package yh.pda.email.act;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.data.YHEmail;
import yh.core.funcs.email.data.YHEmailBody;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.pda.email.data.YHPdaEmail;

public class YHPdaEmailAct {

  public void search(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      int pageSize = Integer.parseInt(request.getParameter("pageSize") == "" || request.getParameter("pageSize") == null ? "5" : request.getParameter("pageSize"));
      int thisPage = Integer.parseInt(request.getParameter("thisPage") == "" || request.getParameter("thisPage") == null ? "1" : request.getParameter("thisPage"));
      //int totalPage = Integer.parseInt(request.getParameter("totalPage") == "" || request.getParameter("totalPage") == null ? "1" : request.getParameter("totalPage"));
      
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String sql = " select oa_email.SEQ_ID, FROM_ID, SUBJECT, SEND_TIME, IMPORTANT, "
      		       + " ATTACHMENT_ID, ATTACHMENT_NAME, person.USER_NAME, CONTENT "
                 + " from oa_email,oa_email_body "
                 + " left join person on oa_email_body.FROM_ID=person.SEQ_ID "
                 + " where oa_email_body.SEQ_ID=oa_email.BODY_ID "
                 + " and BOX_ID=0 "
                 + " and oa_email.TO_ID='"+person.getSeqId()+"' "
                 + " and SEND_FLAG='1' "
                 + " and (DELETE_FLAG='' or DELETE_FLAG='0' or DELETE_FLAG='2') "
                 + " and IS_WEBMAIL is null "
                 + " order by SEND_TIME desc ";
      
      List<YHPdaEmail> list = new ArrayList<YHPdaEmail>();
      ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      rs.last();
      int totalSize = rs.getRow();
      if (totalSize == 0) {
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("thisPage", 0);
        request.setAttribute("totalPage", 0);
        request.setAttribute("emails", list);
        request.getRequestDispatcher("/pda/email/index.jsp").forward(request, response);
        return;
      }
      rs.absolute((thisPage-1) * pageSize + 1);
      int count = 0;
      while(!rs.isAfterLast()) {
        if(count >= pageSize)
          break;
        YHPdaEmail email = new YHPdaEmail();
        email.setSeqId(rs.getInt("SEQ_ID"));
        email.setFromId(rs.getInt("FROM_ID"));
        email.setSubject(rs.getString("SUBJECT"));
        email.setSendTime(rs.getTimestamp("SEND_TIME"));
        email.setImportant(rs.getString("IMPORTANT"));
        email.setAttachmentId(rs.getString("ATTACHMENT_ID"));
        email.setAttachmentName(rs.getString("ATTACHMENT_NAME"));
        email.setUserName(rs.getString("USER_NAME"));
        String content = rs.getString("CONTENT");
        content = content == null ? "" : YHUtility.cutHtml(content);
        email.setContent(content);
        list.add(email);
        rs.next();
        count++;
      }
      request.setAttribute("pageSize", pageSize);
      request.setAttribute("thisPage", thisPage);
      request.setAttribute("totalPage", totalSize/pageSize + (totalSize%pageSize == 0 ? 0 : 1));
      request.setAttribute("emails", list);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    request.getRequestDispatcher("/pda/email/index.jsp").forward(request, response);
    return;
  }
  
  public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    try{
      String emailId = (String)request.getParameter("emailId");
      
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String sql = " update oa_email set DELETE_FLAG='3' where TO_ID='"+person.getSeqId()+"' and (DELETE_FLAG='' or (DELETE_FLAG='' or DELETE_FLAG='0')) and SEQ_ID="+emailId;
      ps = dbConn.prepareStatement(sql);
      ps.executeUpdate();
      
      sql = " update oa_email set DELETE_FLAG='4' where TO_ID='"+person.getSeqId()+"' and DELETE_FLAG='2' and SEQ_ID="+emailId;
      ps = dbConn.prepareStatement(sql);
      ps.executeUpdate();
      request.setAttribute("flag", 1);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, null, null);
    }
    request.getRequestDispatcher("/pda/email/delete.jsp").forward(request, response);
    return;
  }
  
  public void sendMail(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      String toName1 = (String)request.getParameter("toName1");
      String toName2 = (String)request.getParameter("toName2");
      String subject = (String)request.getParameter("subject");
      String content = (String)request.getParameter("content");
      int bodyId = 0;
      
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String toId = "0";
      String sql = " select SEQ_ID from person where USER_NAME = '"+toName1+"'";
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        toId = rs.getString("SEQ_ID");
      }
      if(!"0".equals(toId)){
        bodyId = emailNotifier(dbConn, person.getSeqId(), toId, subject, content, new Date(), "", request.getContextPath(), true);
        request.setAttribute("flag", 1);
      }
//      if(YHUtility.isNullorEmpty(toName2)){
//        YHEmailBody eb = new YHEmailBody();
//        eb.setFromId(person.getSeqId());
//        eb.setAttachmentId("");
//        eb.setAttachmentName("");
//        eb.setSendFlag("1");
//        eb.setCompressContent(YHDiaryUtil.cutHtml(eb.getContent()));
//        eb.setSendTime(new Date());
//        eb.setToWebmail(toName2);
//        YHWebmailLogic wbml = new YHWebmailLogic();
//        wbml.sendWebMail(dbConn, eb, bodyId);
//      }
      else{
        request.setAttribute("flag", 0);
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    request.getRequestDispatcher("/pda/email/submit.jsp").forward(request, response);
    return;
  }
  
  /**
   * 邮件提醒接口
   * @param conn 数据库连接


   * @param fromId 邮件发送人
   * @param toId  邮件接收人


   * @param subject 邮件标题
   * @param content 邮件正文
   * @param sentTime 邮件发送时间


   * @param important 邮件重要度[1,2,3]
   * @param contextPath 系统上下文地址
   * @throws Exception
   */
  public static int emailNotifier(Connection conn , int fromId 
      , String toId , String subject  , String content ,Date sentTime,String important,String contextPath,boolean isSmsRemind) throws Exception{
    YHORM orm = new YHORM();
    YHEmailBody emailBody = new YHEmailBody();
    YHEmail email = new YHEmail();
    long size = 0l;
    content = content.replaceAll("[\r\n]", "<br>");
    emailBody.setEnsize(size);
    emailBody.setFromId(fromId);
    emailBody.setToId(toId);
    emailBody.setContent(content);
    if(subject == null || "".equals(subject)){
      subject = "[无主题]";
    }
    emailBody.setSubject(subject);
    if(sentTime == null){
      sentTime = new Date();
    }
    emailBody.setSendTime(sentTime);
    if(important == null || "".equals(important)){
      important = "3";
    }
    emailBody.setImportant(important);
    emailBody.setSendFlag("1");
    orm.saveSingle(conn, emailBody);
    YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
    int bId = emul.getBodyId(conn);
    ArrayList<String> ids = new ArrayList<String>();
    if(toId != null && !"".equals(toId)){
      ids = emul.addArray(ids, toId.split(","));
    }
    if(isSmsRemind){
      subject = " 请查收我的邮件！主题：" + subject;
      String remindUrl = "/core/funcs/email/inbox/read_email/index.jsp?seqId=" + bId ;
      YHSmsBack sb = new YHSmsBack();
      sb.setFromId(fromId);
      sb.setContent(subject);
      sb.setSmsType("2");
      sb.setRemindUrl(remindUrl);
      sb.setToId(toId);
      YHSmsUtil.smsBack(conn, sb);
    }
    for(int i = 0 ; ids != null && i < ids.size(); i++){
      String id = ids.get(i);
      if("".equals(id)){
        continue;
      }
      email.setBodyId(bId);
      email.setToId(id);
      email.setReadFlag("0");
      email.setDeleteFlag("0");
      email.setBoxId(0);
      orm.saveSingle(conn, email);
    }  
    return bId;
  }
}
