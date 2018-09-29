package yh.core.funcs.address.data;

import java.util.Date;


public class YHAddress {
  private int seqId;
  private String userId;
  private int groupId;
  private String psnName;
  private String sex;
  private String nickName;
  private Date birthday;
  private String ministration;
  private String mate;
  private String child;
  private String deptName;
  private String addDept;
  private String postNoDept;
  private String telNoDept;
  private String faxNoDept;
  private String addHome;
  private String postNoHome;
  private String telNoHome;
  private String mobilNo;
  private String bpNo;
  private String email;
  private String oicqNo;
  private String icqNo;
  private String notes;
  private int psnNo;
  private String smsFlag;
  
  public int getSeqId() {
    return this.seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getUserId() {
    return this.userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public int getGroupId() {
    return this.groupId;
  }
  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }
  public String getPsnName() {
    return this.psnName;
  }
  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }
  public String getSex() {
    return this.sex;
  }
  public void setSex(String sex) {
    this.sex = sex;
  }
  public String getNickName() {
    return this.nickName;
  }
  public void setNickName(String nickName) {
    this.nickName = nickName;
  }
  public Date getBirthday() {
//    try{
//      System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(this.birthday));
//      birthday = YHUtility.parseDate(new SimpleDateFormat("yyyy-MM-dd").format(this.birthday));
//      System.out.println(birthday+"ssdDDDSDSD");
//    } catch (ParseException e){
//      e.printStackTrace();
//    }
    return this.birthday;
  }
  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }
  public String getMinistration() {
    return this.ministration;
  }
  public void setMinistration(String ministration) {
    this.ministration = ministration;
  }
  public String getMate() {
    return this.mate;
  }
  public void setMate(String mate) {
    this.mate = mate;
  }
  public String getChild() {
    return this.child;
  }
  public void setChild(String child) {
    this.child = child;
  }
  public String getDeptName() {
    return this.deptName;
  }
  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }
  public String getAddDept() {
    return this.addDept;
  }
  public void setAddDept(String addDept) {
    this.addDept = addDept;
  }
  public String getPostNoDept() {
    return this.postNoDept;
  }
  public void setPostNoDept(String postNoDept) {
    this.postNoDept = postNoDept;
  }
  public String getTelNoDept() {
    return this.telNoDept;
  }
  public void setTelNoDept(String telNoDept) {
    this.telNoDept = telNoDept;
  }
  public String getFaxNoDept() {
    return this.faxNoDept;
  }
  public void setFaxNoDept(String faxNoDept) {
    this.faxNoDept = faxNoDept;
  }
  public String getAddHome() {
    return this.addHome;
  }
  public void setAddHome(String addHome) {
    this.addHome = addHome;
  }
  public String getPostNoHome() {
    return this.postNoHome;
  }
  public void setPostNoHome(String postNoHome) {
    this.postNoHome = postNoHome;
  }
  public String getTelNoHome() {
    return this.telNoHome;
  }
  public void setTelNoHome(String telNoHome) {
    this.telNoHome = telNoHome;
  }
  public String getMobilNo() {
    return this.mobilNo;
  }
  public void setMobilNo(String mobilNo) {
    this.mobilNo = mobilNo;
  }
  public String getBpNo() {
    return this.bpNo;
  }
  public void setBpNo(String bpNo) {
    this.bpNo = bpNo;
  }
  public String getEmail() {
    return this.email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getOicqNo() {
    return this.oicqNo;
  }
  public void setOicqNo(String oicqNo) {
    this.oicqNo = oicqNo;
  }
  public String getIcqNo() {
    return this.icqNo;
  }
  public void setIcqNo(String icqNo) {
    this.icqNo = icqNo;
  }
  public String getNotes() {
    return notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
  public int getPsnNo() {
    return this.psnNo;
  }
  public void setPsnNo(int psnNo) {
    this.psnNo = psnNo;
  }
  public String getSmsFlag() {
    return this.smsFlag;
  }
  public void setSmsFlag(String smsFlag) {
    this.smsFlag = smsFlag;
  }
}

