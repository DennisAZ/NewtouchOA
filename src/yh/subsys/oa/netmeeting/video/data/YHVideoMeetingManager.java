package yh.subsys.oa.netmeeting.video.data;

public class YHVideoMeetingManager {
	private int seqId;
	private int userId; 
	private String userName;
	private String redUsername; 
  private String redPassword; 
  
	public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getRedUsername() {
    return redUsername;
  }
  public void setRedUsername(String redUsername) {
    this.redUsername = redUsername;
  }
  public String getRedPassword() {
    return redPassword;
  }
  public void setRedPassword(String redPassword) {
    this.redPassword = redPassword;
  }

}
