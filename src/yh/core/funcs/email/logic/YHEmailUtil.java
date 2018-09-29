package yh.core.funcs.email.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

import yh.core.funcs.email.data.YHEmail;
import yh.core.funcs.email.data.YHEmailBody;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.util.db.YHORM;

/**
 * 邮件外部接口
 * @author Think
 *
 */
public class YHEmailUtil {
  /**
   * 邮件提醒接口
   * @param conn 数据库连接
   * @param fromId 邮件发送人
   * @param toId  邮件接收人
   * @param subject 邮件标题
   * @param content 邮件正文
   * @param sentTime 邮件发送时间
   * @param important 邮件重要度[1,2,3]
   * @throws Exception
   */
  public static void emailNotifier(Connection conn , int fromId 
      , String toId , String subject  , String content ,Date sentTime,String important,String contextPath) throws Exception{
    emailNotifier(conn, fromId, toId, subject, content, sentTime, important, contextPath,false);
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
  public static void emailNotifier(Connection conn , int fromId 
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
      important = "1";
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
  }
}

