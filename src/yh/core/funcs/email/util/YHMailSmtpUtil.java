package yh.core.funcs.email.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import yh.core.funcs.email.data.YHEmailCont;
import yh.core.funcs.email.data.YHWebmail;
import yh.core.funcs.email.data.YHWebmailBody;
import yh.core.util.YHUtility;
import yh.core.util.mail.YHMailAuthenticator;

/**
 * 发送邮件
 * 
 * @author tulaike
 * 
 */
public class YHMailSmtpUtil {
  public static boolean sendWebMail(Properties p, String userName,
      String password, boolean isValidate, YHWebmailBody wmb,String attachPath) throws Exception {
    YHMailAuthenticator authenticator = null;
    Properties pro = p;
    p.put("mail.smtp.auth","true");
    // 如果需要身份认证，则创建一个密码验证器
    if (isValidate) {
      authenticator = new YHMailAuthenticator(userName, password);
    }
    // 根据邮件会话属性和密码验证器构造一个发送邮件的session
    Session sendMailSession = Session.getInstance(pro, authenticator);
    try {
      // 根据session创建一个邮件消息
      Message mailMessage = new MimeMessage(sendMailSession);
      // 创建邮件发送者地址
      Address from = new InternetAddress(wmb.getFromMail());
      // 设置邮件消息的发送者
      mailMessage.setFrom(from);
      // 创建邮件的接收者地址，并设置到邮件消息中
     // Address to = new InternetAddress(wmb.getToMail());
      // Message.RecipientType.TO属性表示接收者的类型为TO
      
      sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder(); 
      
      wmb.setToMailCopy(YHUtility.null2Empty(wmb.getToMailCopy()));
      wmb.setToMailSecret(YHUtility.null2Empty(wmb.getToMailSecret()));
      
      InternetAddress[] tomail = getIa(wmb.getToMail()) ;
      InternetAddress[] tomailCopy = getIa(wmb.getToMailCopy()) ;
      InternetAddress[] tomailSecret = getIa(wmb.getToMailSecret()) ;
      mailMessage.setRecipients(Message.RecipientType.TO, tomail );
      mailMessage.setRecipients(Message.RecipientType.CC, tomailCopy);
      mailMessage.setRecipients(Message.RecipientType.BCC, tomailSecret);
      
      mailMessage.setSubject("=?UTF-8?B?"+enc.encode(wmb.getSubject().getBytes("UTF-8"))+"?=");
     // mailMessage.setSubject(wmb.getSubject());
      // 设置邮件消息发送的时间
      mailMessage.setSentDate(new Date());
      // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
      Multipart mainPart = new MimeMultipart();
      // 创建一个包含HTML内容的MimeBodyPart
      BodyPart html = new MimeBodyPart();
      // 设置HTML内容
      html.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + wmb.getContentHtml(), "text/html; charset=utf-8");
      mainPart.addBodyPart(html);
      // 设置附件
      mainPart = addFileAffixs(mainPart, attachPath, wmb.getAttachmentName(), wmb.getAttachmentId(), YHEmailCont.MODULE);
      // 将MiniMultipart对象设置为邮件内容
      mailMessage.setContent(mainPart);
      // 发送邮件
      mailMessage.saveChanges();
      Transport transport = sendMailSession.getTransport("smtp");
      transport.connect((String)p.get("mail.smtp.host"),userName,password);
      try {
       // transport.sendMessage(mailMessage,mailMessage.getRecipients(Message.RecipientType.TO));
        transport.send(mailMessage);
      } catch (NullPointerException e) {
        //在草稿箱中的内部邮件,外部地址报空指针异常,直接屏蔽异常
      }
      //Transport.send(mailMessage);
      return true;
    } catch (Exception ex) {
      throw ex;
    }
  }
  public static InternetAddress[] getIa(String toMail) throws Exception {
    if (YHUtility.isNullorEmpty(toMail)) {
      return null;
    }
    String[] tos = toMail.split(",");
    InternetAddress[] ias = new InternetAddress[tos.length];
    for (int i = 0 ;i < tos.length ; i++) {
      String to = tos[i];
      if (!YHUtility.isNullorEmpty(to)) {
        String name = "";
        if (to.endsWith(">")){
          int b = to.indexOf("<");
          if (b >= 0 ) {
            if (b > 0) {
              name = to.substring(0 , b);
            }
            to = to.substring(b + 1, to.length() - 1);
          }
        }
        ias[i] = new InternetAddress(to , name , "UTF-8");
      }
    }
    return ias;
  }
  public static String covertAddress(String ss) {
    if (ss == null) 
      return "";
    String[] sss = ss.split(",");
    String newStr = "";
    for (String s : sss) {
      if (s.endsWith(">")){
        int b = s.indexOf("<");
        s = s.substring(b + 1, s.length() - 1);
      }
      newStr += s + ",";
    }
    if (newStr.endsWith(",")) {
      newStr = newStr.substring(0, newStr.length() - 1);
    }
    return newStr;
  }
/**
 * 发送邮件带附件的
 * @param wm
 * @param wmb
 * @return
 * @throws Exception 
 */
  public static boolean sendWebMail(YHWebmail wm, YHWebmailBody wmb,String attachPath) throws Exception {
    // 判断是否需要身份认证    Properties p = new Properties();
    String userName = wm.getEmail();
    String password = wm.getEmailPass();
    p.setProperty("mail.smtp.host", wm.getSmtpServer());
    p.put("mail.smtp.port",wm.getSmtpPort());
    if(wm.getSmtpSsl() != null && ("1".equals(wm.getSmtpSsl().trim()) || "yes".equalsIgnoreCase(wm.getSmtpSsl().trim()))){
      Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
      final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
      p.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
      p.setProperty("mail.smtp.socketFactory.fallback", "false");
    }
    boolean isValidate = false;//是否需要验证 
    String smtpPass = wm.getSmtpPass();
    if("yes".equalsIgnoreCase(smtpPass) || "1".equals(smtpPass)){
      isValidate = true;
    }
    return sendWebMail(p, userName, password, isValidate, wmb,attachPath);
  }
/**
 * 添加附件
 * @param mp
 * @param attachPath
 * @param attachNames
 * @param attachIds
 * @param module
 * @return
 * @throws Exception 
 */
  public static Multipart addFileAffixs(Multipart mp,String attachPath,String attachNames,String attachIds,String module) throws Exception{
    if(attachIds == null || attachNames == null || attachPath == null){
      return mp;
    }
    String[] attachIdArray = attachIds.split(",");
    String[] attachNameArray = attachNames.split("\\*");
    for (int i = 0; i < attachIdArray.length; i++) {
      String attachId = attachIdArray[i];
      String attachName = attachNameArray[i];
      if(!"".equals(attachId) && !"".equals(attachName)){
        mp = addFileAffix(mp, attachPath, attachName, attachId, module);
      }
    }
    return mp;
  }
  /**
   * 添加单个附件
   * @param name
   *          String
   * @param pass
   *          String
   * @throws Exception 
   */
  public static Multipart addFileAffix(Multipart mp,String attachPath,String attachName,String attachId,String module) throws Exception {

    //System.out.println("增加邮件附件：" + attachName);
    File attachFile = getAttachmentFile(attachPath, attachName, attachId, module);
    if(attachFile == null){
      return mp;
    }
    try {
      BodyPart bp = new MimeBodyPart();
      FileDataSource fileds = new FileDataSource(attachFile);
      bp.setDataHandler(new DataHandler(fileds));
      sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
      bp.setFileName("=?UTF-8?B?"+enc.encode(attachName.getBytes("UTF-8"))+"?=");
      mp.addBodyPart(bp);
      return mp;
    } catch (Exception e) {
      throw e;
    }
  }
  /**
   * 组装YH附件信息
   * @param attachPath
   * @param attachName
   * @param attachId
   * @param module
   * @return
   */
  public static File getAttachmentFile(String attachPath,String attachName,String attachId,String module){
    File attachFile = null;
    String path  = "";
    String fileName = "";
    if(attachId != null && !"".equals(attachId)){
      if(attachId.indexOf("_") > 0){
        String attIds[] = attachId.split("_");
        fileName = attIds[1] + "." + attachName;
        path = attachPath + File.separator + module + File.separator + attIds[0] + File.separator  + fileName;
      }else{
        fileName = attachId + "." + attachName;
        path = attachPath + File.separator + module + File.separator  + fileName;
      }
      
      attachFile = new File(path);
      if(!attachFile.exists()){
        if(attachId.indexOf("_") > 0){
          String attIds[] = attachId.split("_");
          fileName = attIds[1] + "_" + attachName;
          path = attachPath + File.separator + module + File.separator + attIds[0] + File.separator  + fileName;
        }else{
          fileName = attachId + "_" + attachName;
          path = attachPath + File.separator + module + File.separator  + fileName;
        }
        attachFile = new File(path);
      }
    }
    if(!attachFile.exists()){
      attachFile = null;
    }
    return attachFile;
  }
}
