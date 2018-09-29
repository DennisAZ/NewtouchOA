package yh.core.funcs.email.data;

public class YHWebmail {
  private int seqId;
  private String email;
  private int userId;
  private String popServer;
  private String smtpServer;
  private String loginType;
  private String smtpPass;
  private String emailUser;
  private String emailPass;
  private String pop3Port;
  private String smtpPort;
  private String isDefault = "0";
  private String pop3Ssl;
  private String smtpSsl;
  private String quotaLimit;
  private String emailUid;
  private String checkFlag;
  private String priority;
  private String recvDel = "0";
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
  public String getPopServer() {
    return popServer;
  }
  public void setPopServer(String popServer) {
    this.popServer = popServer;
  }
  public String getSmtpServer() {
    return smtpServer;
  }
  public void setSmtpServer(String smtpServer) {
    this.smtpServer = smtpServer;
  }
  public String getLoginType() {
    return loginType;
  }
  public void setLoginType(String loginType) {
    this.loginType = loginType;
  }
  public String getSmtpPass() {
    return smtpPass;
  }
  public void setSmtpPass(String smtpPass) {
    this.smtpPass = smtpPass;
  }
  public String getEmailUser() {
    return emailUser;
  }
  public void setEmailUser(String emailUser) {
    this.emailUser = emailUser;
  }
  public String getEmailPass() {
    return emailPass;
  }
  public void setEmailPass(String emailPass) {
    this.emailPass = emailPass;
  }
  public String getPop3Port() {
    return pop3Port;
  }
  public void setPop3Port(String pop3Port) {
    this.pop3Port = pop3Port;
  }
  public String getSmtpPort() {
    return smtpPort;
  }
  public void setSmtpPort(String smtpPort) {
    this.smtpPort = smtpPort;
  }
  public String getIsDefault() {
    return isDefault;
  }
  public void setIsDefault(String isDefault) {
    this.isDefault = isDefault;
  }
  public String getPop3Ssl() {
    return pop3Ssl;
  }
  public void setPop3Ssl(String pop3Ssl) {
    this.pop3Ssl = pop3Ssl;
  }
  public String getSmtpSsl() {
    return smtpSsl;
  }
  public void setSmtpSsl(String smtpSsl) {
    this.smtpSsl = smtpSsl;
  }
  public String getQuotaLimit() {
    return quotaLimit;
  }
  public void setQuotaLimit(String quotaLimit) {
    this.quotaLimit = quotaLimit;
  }
  public String getEmailUid() {
    return emailUid;
  }
  public void setEmailUid(String emailUid) {
    this.emailUid = emailUid;
  }
  public String getCheckFlag() {
    return checkFlag;
  }
  public void setCheckFlag(String checkFlag) {
    this.checkFlag = checkFlag;
  }
  public String getPriority() {
    return priority;
  }
  public void setPriority(String priority) {
    this.priority = priority;
  }
  public String getRecvDel() {
    return recvDel;
  }
  public void setRecvDel(String recvDel) {
    this.recvDel = recvDel;
  }
  @Override
  public String toString() {
    return "YHWebMail [checkFlag=" + checkFlag + ", email=" + email
        + ", emailPass=" + emailPass + ", emailUid=" + emailUid
        + ", emailUser=" + emailUser + ", isDefault=" + isDefault
        + ", loginType=" + loginType + ", pop3Port=" + pop3Port + ", pop3Ssl="
        + pop3Ssl + ", popServer=" + popServer + ", priority=" + priority
        + ", quotaLimit=" + quotaLimit + ", recvDel=" + recvDel + ", seqId="
        + seqId + ", smtpPass=" + smtpPass + ", smtpPort=" + smtpPort
        + ", smtpServer=" + smtpServer + ", smtpSsl=" + smtpSsl + ", userId="
        + userId + "]";
  }
  
}
