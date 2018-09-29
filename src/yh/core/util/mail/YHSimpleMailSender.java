package yh.core.util.mail;

import java.io.File;
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

import yh.core.util.file.YHFileUtility;

/**
 * 简单邮件（不带附件的邮件）发送器
 */
public class YHSimpleMailSender {
  /**
   * 以文本格式发送邮件
   * @param mailInfo 待发送的邮件的信息
   */
  public void sendTextMail(YHMailSenderInfo mailInfo) throws Exception {
    // 根据session创建一个邮件消息
    Message mailMessage = buildMessage(mailInfo);
    mailMessage.setText(mailInfo.getContent());
    // 发送邮件
    Transport.send(mailMessage);
  }

  /**
   * 以HTML格式发送邮件
   * @param mailInfo 待发送的邮件信息
   */
  public void sendHtmlAndAttachMail(YHMailSenderInfo mailInfo) throws Exception {
    // 根据session创建一个邮件消息
    Message mailMessage = buildMessage(mailInfo);
    Multipart mainPart = buildMailPart(mailInfo.getContent(), mailInfo.getAttachFileNames());
    // 将MiniMultipart对象设置为邮件内容
    mailMessage.setContent(mainPart);
    // 发送邮件
    Transport.send(mailMessage);
  }
  /**
   * 构造发送对象
   * @param bodyStr
   * @param attachs
   * @return
   * @throws Exception
   */
  private Multipart buildMailPart(String bodyStr, String[] attachs) throws Exception {
    // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
    Multipart mainPart = new MimeMultipart();
    // 创建一个包含HTML内容的MimeBodyPart
    BodyPart html = new MimeBodyPart();
    // 设置HTML内容
    html.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + bodyStr, "text/html; charset=utf-8");
    mainPart.addBodyPart(html);
    
    if (attachs == null) {
      return mainPart;
    }
    for (int i = 0; i < attachs.length; i++) {
      BodyPart filePart = buildAttachPart(attachs[i]);
      if (filePart != null) {
        mainPart.addBodyPart(filePart);
      }
    }
    
    return mainPart;
  }
  
  private BodyPart buildAttachPart(String filePath) throws Exception {
    File attachFile = new File(filePath);
    if (!attachFile.isFile() || !attachFile.exists()) {
      return null;
    }
    BodyPart bp = new MimeBodyPart();
    FileDataSource fileds = new FileDataSource(attachFile);
    bp.setDataHandler(new DataHandler(fileds));
    sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
    String fileName = YHFileUtility.getFileName(filePath);
    bp.setFileName("=?UTF-8?B?"+enc.encode(fileName.getBytes("UTF-8"))+"?=");
    
    return bp;
  }
  /**
   * 构建Message对象
   * @param mailInfo
   * @return
   */
  private Message buildMessage(YHMailSenderInfo mailInfo) throws Exception {
    // 判断是否需要身份认证
    YHMailAuthenticator authenticator = null;
    Properties pro = mailInfo.getProperties();
    // 如果需要身份认证，则创建一个密码验证器
    if (mailInfo.isValidate()) {
      authenticator = new YHMailAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
    }
    // 根据邮件会话属性和密码验证器构造一个发送邮件的session
    Session sendMailSession = Session.getInstance(pro, authenticator);
    // 根据session创建一个邮件消息
    Message mailMessage = new MimeMessage(sendMailSession);
    // 创建邮件发送者地址
    Address from = new InternetAddress(mailInfo.getFromAddress());
    // 设置邮件消息的发送者
    mailMessage.setFrom(from);
    // 创建邮件的接收者地址，并设置到邮件消息中
    Address to = new InternetAddress(mailInfo.getToAddress());
    // Message.RecipientType.TO属性表示接收者的类型为TO
    mailMessage.setRecipient(Message.RecipientType.TO, to);
    // 设置邮件消息的主题
    mailMessage.setSubject(mailInfo.getSubject());
    // 设置邮件消息发送的时间
    mailMessage.setSentDate(new Date());
    return mailMessage;
  }
}
