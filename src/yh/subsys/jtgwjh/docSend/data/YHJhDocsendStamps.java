package yh.subsys.jtgwjh.docSend.data;

import java.util.Date;

public class YHJhDocsendStamps {

  private int seqId;//自增，索引
  private int docSendInfoId;//集团公文oa(run_id)
  private String stampType;//盖章类型。0：普通盖章（主办盖章）1：协办盖章
  private int dept;//盖章部门id
  private String deptName;//盖章部门名称
  private int user;//盖章人id
  private String userName;//盖章人名字
  private String stampStatus;//盖章状态：0：待盖章，1：已盖章
  private Date stampTime;//盖章时间
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getDocSendInfoId() {
    return docSendInfoId;
  }
  public void setDocSendInfoId(int docSendInfoId) {
    this.docSendInfoId = docSendInfoId;
  }
  public String getStampType() {
    return stampType;
  }
  public void setStampType(String stampType) {
    this.stampType = stampType;
  }
  public String getDeptName() {
    return deptName;
  }
  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getStampStatus() {
    return stampStatus;
  }
  public void setStampStatus(String stampStatus) {
    this.stampStatus = stampStatus;
  }
  public Date getStampTime() {
    return stampTime;
  }
  public void setStampTime(Date stampTime) {
    this.stampTime = stampTime;
  }
  public int getDept() {
    return dept;
  }
  public void setDept(int dept) {
    this.dept = dept;
  }
  public int getUser() {
    return user;
  }
  public void setUser(int user) {
    this.user = user;
  }

}
