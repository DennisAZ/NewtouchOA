package yh.core.funcs.email.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import sun.misc.BASE64Decoder;
import yh.core.funcs.email.logic.YHInnerEMailLogic;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;

/**
 * 解析邮件正文的工具类
 * 
 * @author Think
 * 
 */
public class YHMimeMessageUtil {
  /**
   * 判断邮件中是否含有附件
   * 
   * @param msg
   * @return
   */

  public static boolean hasAttachment(Message msg) {
    Part part = (Part) msg;
    try {
      if (part.isMimeType("multipart/*")) {
        Multipart mp = (Multipart) part.getContent();
        int count = mp.getCount();
        for (int i = 0; i < count; i++) {
          Part tmp = mp.getBodyPart(i);
          if (isAttachment(tmp)) {
            return true;
          }
        }
      }
    } catch (Exception e) {
      return false;
    }
    return false;
  }

  /**
   * 判断是否是附件
   * 
   * @param part
   * @return
   */
  public static boolean isAttachment(Part part) {
    try {
      String disposition = part.getDisposition();
      if ((disposition != null)
          && ((disposition.equals(Part.ATTACHMENT)) || (disposition
              .equals(Part.INLINE))))
        return true;
    } catch (Exception e) {
      /**
       * 上面的方法只是适合于附件不是中文，或者中文名较短的情况,<br>
       * 附件中文名过长时就不能用了，因为javamail Api中的part.getDisposition()根本就不能得到正确的数据了<br>
       * 捕获异常后,分别再详细判断处理
       */
      String contentType = "";
      try {
        contentType = part.getContentType();
      } catch (MessagingException e1) {
        return false;
      }
      if (contentType.startsWith("application/octet-stream"))
        return true;
      return false;
    }
    return false;
  }

  /**
   * 解析邮件的正文
   * 
   * @param msg
   * @param userName
   * @return
   */
  public static String getBody(Message msg, String userName) {
    return getBody((Part) msg, userName);
  }

  public static boolean isHtml(Part part) throws Exception {
    boolean result = false;
    if (part.isMimeType("text/html")) {
      result = true;
    }
    return result;
  }

  /**
   * 解析邮件的正文
   * 
   * @param part
   * @param userName
   * @return
   */
  public static String getBody(Part part, String userName) {

    StringBuffer sb = new StringBuffer();
    sb.append(new String(""));
    try {
      /**
       * 纯文本或者html格式的,可以直接解析掉
       */
      if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
        sb.append(part.getContent());
      } else if (part.isMimeType("multipart/*")) {
        /**
         * 可供选择的,一般情况下第一个是plain,第二个是html格式的
         */
        if (part.isMimeType("multipart/alternative")) {
          Multipart mp = (Multipart) part.getContent();
          int index = 0;// 兼容不正确的格式,返回第一个部分
          if (mp.getCount() > 1)
            index = 1;// 第2个部分为html格式的
          /**
           * 已经根据情况进行了判断,就算不符合标准格式也不怕了.
           */
          Part tmp = mp.getBodyPart(index);
          String body = getBody(tmp , userName);
          sb.append(body);
        } else if (part.isMimeType("multipart/related")) {
          /**
           * related格式的,那么第一个部分包含了body,里面含有内嵌的内容的链接.
           */
          Multipart mp = (Multipart) part.getContent();
          Part tmp = mp.getBodyPart(0);
          String body = getBody(tmp, userName);
          int count = mp.getCount();
          /**
           * 要把那些可能的内嵌对象都先读出来放在服务器上,然后在替换相对地址为绝对地址
           */
          for (int k = 1; count > 1 && k < count; k++) {
            Part att = mp.getBodyPart(k);
            String attname = att.getFileName();
            String attid = "";
            if (attname != null) {
              attname = MimeUtility.decodeText(attname);
              try {
                YHWorkFlowUtility util = new YHWorkFlowUtility();
                String[] sss = util.getNewAttachPath(attname, "email");
                File attFile = new File(sss[1]);
                attid = sss[0];
                FileOutputStream fileoutput = new FileOutputStream(attFile);
                InputStream is = att.getInputStream();
                BufferedOutputStream outs = new BufferedOutputStream(fileoutput);
                byte b[] = new byte[att.getSize()];
                is.read(b);
                outs.write(b);
                outs.close();
              } catch (Exception e) {
              }
              String Content_ID[] = att.getHeader("Content-ID");
              if (Content_ID != null && Content_ID.length > 0) {
                String cid_name = Content_ID[0].replace("<", "").replace(">", "");
                String path = "/yh/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?attachmentName="+ attname+ "&attachmentId="+ attid +"&module=email";
                body = body.replace("cid:" + cid_name, path);
              }
            }
          }
          sb.append(body);
          return sb.toString();
        } else {
          /**
           * 其他multipart/*格式的如mixed格式,那么第一个部分包含了body,用递归解析第一个部分就可以了
           */
          Multipart mp = (Multipart) part.getContent();
          Part tmp = mp.getBodyPart(0);
          return getBody(tmp, userName);
        }
      } else if (part.isMimeType("message/rfc822")) {
        return getBody((Message) part.getContent(), userName);
      } else {
        /**
         * 否则的话,死马当成活马医,直接解析第一部分
         */
        Object obj = part.getContent();
        if (obj instanceof String) {
          sb.append(obj);
        } else {
          Multipart mp = (Multipart) obj;
          Part tmp = mp.getBodyPart(0);
          return getBody(tmp, userName);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "解析正文错误!";
    }
    return sb.toString();
  }

  /**
   * 　 *　获得发件人的地址和姓名
   * 
   * @throws Exception
   */
  public static String getFrom(Message mimeMessage) throws Exception {
    InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
    String from = address[0].getAddress();
    if (from == null) {
      from = "";
    }
    String personal = address[0].getPersonal();
    if (personal == null) {
      personal = "";
    }
    String fromaddr = personal + "<" + from + ">";
    return fromaddr;
  }

  /**
   * 获得邮件的收件人，抄送，和密送的地址和姓名 根据所传递的参数的不同 "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址
   */
  public static String getMailAddress(Message mimeMessage, String type)
      throws Exception {
    String mailaddr = "";
    String addtype = type.toUpperCase();
    InternetAddress[] address = null;
    if (addtype.equals("TO") || addtype.equals("CC") || addtype.equals("BCC")) {
      if (addtype.equals("TO")) {
        address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);
      } else if (addtype.equals("CC")) {
        address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);
      } else {
        address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);
      }
      if (address != null) {
        for (int i = 0; i < address.length; i++) {
          String email = address[i].getAddress();
          email = (email == null) ? "" : MimeUtility.decodeText(email);
          String personal = address[i].getPersonal();
          personal = (personal == null) ? "" : MimeUtility.decodeText(personal);
          String compositeto = personal + "<" + email + ">";
          mailaddr += "," + compositeto;
        }
        if (mailaddr.length() > 1) {
          mailaddr = mailaddr.substring(1);
        }
      }
    } else {
      throw new Exception("Error emailaddr type!");
    }
    return mailaddr;
  }

  /**
   * 获得邮件主题
   */
  public static String getSubject(Message mimeMessage)
      throws MessagingException {
    String subject = "";
    try {
      subject = mimeMessage.getSubject();
      if ( mimeMessage.isMimeType("text/plain")|| mimeMessage.isMimeType("text/html")) {
        String subjectType = mimeMessage.getHeader("Subject")[0].toLowerCase();
        if(!subjectType.startsWith("=?")){
          subject = new String(subject.getBytes("iso8859-1"),"gbk"); //解决标题不规则出现乱码的问题
        }else{
          subject = MimeUtility.decodeText(subject);
        }
      }else{
        subject = MimeUtility.decodeText(subject);
      }
      
      String temp = subject.toLowerCase();
      if (temp.startsWith("=?gbk?b?") && temp.endsWith("?=")) {
        subject = getFromBASE64(subject.substring("=?gbk?b?".length(),subject.indexOf("=?=") ),"gbk");
      }
      if (temp.startsWith("=?gb2312?b?") && temp.endsWith("=?=")) {
        subject = getFromBASE64(subject.substring("=?gb2312?b?".length(),subject.indexOf("?=") ),"gbk");
      }
      if (temp.startsWith("=?utf-8?b?") && temp.endsWith("=?=")) {
        subject = getFromBASE64(subject.substring("=?utf-8?b?".length(),subject.indexOf("?=")),"utf-8");
      }
      //System.out.println(subject);
      if (subject == null)
        subject = "";
    } catch (Exception exce) {
    }
    return subject;
  }

  /**
   * 获得邮件发送日期
   */
  public static Date getSentDate(Message mimeMessage) throws Exception {
    Date sentdate = mimeMessage.getSentDate();
    return sentdate;
  }

  /**
   * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"
   */
  public static boolean getReplySign(Message mimeMessage)
      throws MessagingException {
    boolean replysign = false;
    String needreply[] = mimeMessage.getHeader("Disposition-Notification-To");
    if (needreply != null) {
      replysign = true;
    }
    return replysign;
  }
  /**
   * 保存附件
   */
  public static Map<String,  String[]> saveAttachMent(Part part, String attachPath )
      throws Exception {
    String fileName = "";
    Map<String, String[]> attachMap = new HashMap<String, String[]>();
    if (part.isMimeType("multipart/*")) {
      Multipart mp = (Multipart) part.getContent();
      
      int count = mp.getCount();
      for (int i = 0; i < count; i++) {
        BodyPart mpart = mp.getBodyPart(i);
        String disposition = mpart.getDisposition();
        String Content_ID[] = mpart.getHeader("Content-ID");
        
        String cid_name = "";
        if (Content_ID != null && Content_ID.length > 0) {
           cid_name = Content_ID[0].replace("<", "").replace(">", "");
           String attname = mpart.getFileName();
           String attid = "";
           if (attname != null) {
             attname = MimeUtility.decodeText(attname);
             try {
               YHWorkFlowUtility util = new YHWorkFlowUtility();
               String[] sss = util.getNewAttachPath(attname, "email");
               File attFile = new File(sss[1]);
               attid = sss[0];
               FileOutputStream fileoutput = new FileOutputStream(attFile);
               InputStream is = mpart.getInputStream();
               BufferedOutputStream outs = new BufferedOutputStream(fileoutput);
               byte b[] = new byte[mpart.getSize()];
               is.read(b);
               outs.write(b);
               outs.close();
             } catch (Exception e) {
             }
             String[] re = new String[2];
             re[0] = attid;
             re[1] = attname;
             attachMap.put(cid_name, re);
             continue;
           }
        }
        
        if ((disposition != null)
            && ((disposition.equals(Part.ATTACHMENT)) || (disposition
                .equals(Part.INLINE)))) {
          fileName = mpart.getFileName();
          /*lanjinsheng 修改 at t9.core.autorun.T9AutoRun.run(T9AutoRun.java:101) java.lang.NullPointerException*/
          if(fileName!=null){
          if (fileName.toLowerCase().startsWith("=?gbk?b?") && fileName.endsWith("?=")) {
            fileName = getFromBASE64(fileName.substring("=?gbk?b?".length(),fileName.indexOf("?=")),"gbk");
          }
          if (fileName.toLowerCase().startsWith("=?gb2312?b?") && fileName.endsWith("?=")) {
            fileName = getFromBASE64(fileName.substring("=?gb2312?b?".length(),fileName.indexOf("?=")),"gbk");
          }
          if (fileName.toLowerCase().startsWith("=?gb18030?b?") && fileName.endsWith("?=")) {
            fileName = getFromBASE64(fileName.substring("=?gb18030?b?".length(),fileName.indexOf("?=")),"gbk");
          }
          if (fileName.toLowerCase().startsWith("=?utf-8?b?") && fileName.endsWith("?=")) {
            fileName = getFromBASE64(fileName.substring("=?utf-8?b?".length(),fileName.indexOf("?=")),"utf-8");
          }
          if (fileName.toLowerCase().indexOf("gb2312") != -1) {
            fileName = MimeUtility.decodeText(fileName);
          }
          if (fileName.toLowerCase().indexOf("gbk") != -1) {
            fileName = MimeUtility.decodeText(fileName);
          }
          String[] re = saveFile(fileName, mpart.getInputStream(),
              attachPath);
          attachMap.put(re[0], re);
          }
        } else if (mpart.isMimeType("multipart/*")) {
          saveAttachMent(mpart, attachPath);
        } else {
          fileName = mpart.getFileName();
          if ((fileName != null)
              && (fileName.toLowerCase().indexOf("GB2312") != -1)) {
            fileName = MimeUtility.decodeText(fileName);
            String[] re = saveFile(fileName, mpart.getInputStream(),
                attachPath);
            //if (YHUtility.isNullorEmpty(cid_name)) {
              //cid_name = re[0];
            //}
            attachMap.put(re[0], re);
          }
        }
      }
    } else if (part.isMimeType("message/rfc822")) {
      saveAttachMent((Part) part.getContent(), attachPath);
    }
    return attachMap;
  }

  /**
   * 取得打邮件的附件
   * 
   * @param part
   * @return
   * @throws Exception
   */
  public static ArrayList<String> getLargerAttachmentName(Part part)
      throws Exception {
    String fileName = "";
    ArrayList<String> attachList = new ArrayList<String>();
    if (part.isMimeType("multipart/*")) {
      Multipart mp = (Multipart) part.getContent();
      for (int i = 0; i < mp.getCount(); i++) {
        BodyPart mpart = mp.getBodyPart(i);
        String disposition = mpart.getDisposition();
        if ((disposition != null)
            && ((disposition.equals(Part.ATTACHMENT)) || (disposition
                .equals(Part.INLINE)))) {
          fileName = mpart.getFileName();
          if (fileName.toLowerCase().indexOf("gb2312") != -1) {
            fileName = MimeUtility.decodeText(fileName);
          }
          if (fileName.toLowerCase().indexOf("gbk") != -1) {
            fileName = MimeUtility.decodeText(fileName);
          }
          attachList.add(fileName);
        } else if (mpart.isMimeType("multipart/*")) {
          getLargerAttachmentName(mpart);
        } else {
          fileName = mpart.getFileName();
          if ((fileName != null)
              && (fileName.toLowerCase().indexOf("GB2312") != -1)) {
            fileName = MimeUtility.decodeText(fileName);
            attachList.add(fileName);
          }
        }
      }
    } else if (part.isMimeType("message/rfc822")) {
      getLargerAttachmentName((Part) part.getContent());
    }
    return attachList;
  }

  /**
   * 
   * @param fileName
   * @param in
   * @return
   * @throws Exception
   */
  public static String[] saveFile(String fileName, InputStream in,
      String attachPath) throws Exception {
    // 保存附件到email模块
    YHInnerEMailLogic iemu = new YHInnerEMailLogic();
    return iemu.fileUploadLogic2(fileName, in, attachPath);
  }

  /**
   * 得到解码后的BASE64所有字符串
   * @param s
   * @param charset
   * @return
   */
  public static String getFromBASE64(String s,String charset) {
    if (s == null)
      return null;
    BASE64Decoder decoder = new BASE64Decoder();
    try {
      s = s.replaceAll(" ", "");//去掉base64编码后的空格字符串
     // System.out.println(s);
      byte[] b = decoder.decodeBuffer(s.trim());
      return new String(b,charset);
    } catch (Exception e) {
      return null;
    }
  }
 public static void main(String[] args) {
   String uf = "xcvOos6ioaqhqrj2yMu88sD6LmRvYw=";
 // String bas = "1Ma8xsvjtcTB+bTzudi8/M7KzOIs1tC5+tTGvMbL47X3sumxqLjmILa81Nogtdq2/r3s1tC5+tTG vMbL47Tzu+E=";
//   System.out.println(getFromBASE64(uf, "gbk"));
/*   try {
    //System.out.println(new String(uf.getBytes("iso8859-1"),"gbk"));
  } catch (UnsupportedEncodingException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }*/
 }
}
