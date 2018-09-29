package yh.core.funcs.person.data;
import java.util.Date;

public class YHUserOnline {
  private int seqId;
  private int userId;
  private String sessionToken;
  private Date loginTime;
  private String userState;
  public int getSeqId() {
    return this.seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getUserId() {
    return this.userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
  public String getSessionToken() {
    return this.sessionToken;
  }
  public void setSessionToken(String sessionToken) {
    this.sessionToken = sessionToken;
  }
  public Date getLoginTime() {
    return this.loginTime;
  }
  public void setLoginTime(Date loginTime) {
    this.loginTime = loginTime;
  }
  public String getUserState() {
    return this.userState;
  }
  public void setUserState(String userState) {
    this.userState = userState;
  }
}
