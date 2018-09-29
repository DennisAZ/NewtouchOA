package yh.subsys.oa.hr.manage.titleEvaluation.data;

import java.util.Date;

public class YHHrStaffTitleEvaluation {

  private int seqId;//   流水号
  private String createUserId;//     系统登录人seqid
  private int createDeptId;//   系统登录人部门id
  private String postName;//   获取职称
  private String getMethod;//    获取方式
  private Date reportTime;//  申报时间
  private Date receiveTime;//    获取时间
  private String approvePerson;//   批准人
  private String approveNext;//    下次申报职称
  private Date approveNextTime;//   下次申报日期
  private String remark;//    备注
  private String employPost;//   聘用职务
  private Date startDate;//    申请日期
  private Date endDate;//   工资恢复日期
  private String employCompany;//   聘用单位
  private String byEvaluStaffs;//   评定对象
  private Date addTime;//    系统当前时间
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getCreateUserId() {
    return createUserId;
  }
  public void setCreateUserId(String createUserId) {
    this.createUserId = createUserId;
  }
  public int getCreateDeptId() {
    return createDeptId;
  }
  public void setCreateDeptId(int createDeptId) {
    this.createDeptId = createDeptId;
  }
  public String getPostName() {
    return postName;
  }
  public void setPostName(String postName) {
    this.postName = postName;
  }
  public String getGetMethod() {
    return getMethod;
  }
  public void setGetMethod(String getMethod) {
    this.getMethod = getMethod;
  }
  public Date getReportTime() {
    return reportTime;
  }
  public void setReportTime(Date reportTime) {
    this.reportTime = reportTime;
  }
  public Date getReceiveTime() {
    return receiveTime;
  }
  public void setReceiveTime(Date receiveTime) {
    this.receiveTime = receiveTime;
  }
  public String getApprovePerson() {
    return approvePerson;
  }
  public void setApprovePerson(String approvePerson) {
    this.approvePerson = approvePerson;
  }
  public String getApproveNext() {
    return approveNext;
  }
  public void setApproveNext(String approveNext) {
    this.approveNext = approveNext;
  }
  public Date getApproveNextTime() {
    return approveNextTime;
  }
  public void setApproveNextTime(Date approveNextTime) {
    this.approveNextTime = approveNextTime;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  public String getEmployPost() {
    return employPost;
  }
  public void setEmployPost(String employPost) {
    this.employPost = employPost;
  }
  public Date getStartDate() {
    return startDate;
  }
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  public Date getEndDate() {
    return endDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  public String getEmployCompany() {
    return employCompany;
  }
  public void setEmployCompany(String employCompany) {
    this.employCompany = employCompany;
  }
  public String getByEvaluStaffs() {
    return byEvaluStaffs;
  }
  public void setByEvaluStaffs(String byEvaluStaffs) {
    this.byEvaluStaffs = byEvaluStaffs;
  }
  public Date getAddTime() {
    return addTime;
  }
  public void setAddTime(Date addTime) {
    this.addTime = addTime;
  }

}
