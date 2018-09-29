package yh.subsys.oa.vmeet.data;

import java.util.Date;

public class YHVMeet {

  private int seqId ;
  private String beginUser ;
  private String content ;
  private String inviteUsers ;
  private String  addTime ;
  private String vmeet;
  private String vt;
  private String vck;
  
  
  public String getVmeet() {
    return vmeet;
  }
  public void setVmeet(String vmeet) {
    this.vmeet = vmeet;
  }
  public String getVt() {
    return vt;
  }
  public void setVt(String vt) {
    this.vt = vt;
  }
  public String getVck() {
    return vck;
  }
  public void setVck(String vck) {
    this.vck = vck;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getBeginUser() {
    return beginUser;
  }
  public void setBeginUser(String beginUser) {
    this.beginUser = beginUser;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getInviteUsers() {
    return inviteUsers;
  }
  public void setInviteUsers(String inviteUsers) {
    this.inviteUsers = inviteUsers;
  }
  public String getAddTime() {
    return addTime;
  }
  public void setAddTime(String addTime) {
    this.addTime = addTime;
  }
  
  
}
