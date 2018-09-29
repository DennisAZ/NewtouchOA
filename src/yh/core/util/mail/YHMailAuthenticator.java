package yh.core.util.mail;

import javax.mail.*;

/**
 * 发送邮件身份验证信息类
 * @author jpt
 *
 */
public class YHMailAuthenticator extends Authenticator {
  String userName = null;
  String password = null;

  public YHMailAuthenticator() {
  }

  public YHMailAuthenticator(String username, String password) {
    this.userName = username;
    this.password = password;
  }

  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(userName, password);
  }
}
