package yh.subsys.oa.hr.salary.welfare_manager.data;

import java.util.Date;

public class YHHrWelfareManage {
  private int  seqId;//流水号
  private String createUserId;//创建者Id
  private int createDeptId;//创建者部门编号
  private String staffName;//员工姓名
  private Date paymentDate; //分发日期
  private String welfareMonth;//工资月份
  private String welfareItem;//福利项目
  private String freeGift;//发放物品
  private double welfarePayment;//发放金额
  private String taxAffares;//是否纳税
  private Date addTime;//登记日期
  private String remark;//备注
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
  public String getStaffName() {
    return staffName;
  }
  public void setStaffName(String staffName) {
    this.staffName = staffName;
  }
  public Date getPaymentDate() {
    return paymentDate;
  }
  public void setPaymentDate(Date paymentDate) {
    this.paymentDate = paymentDate;
  }
  public String getWelfareMonth() {
    return welfareMonth;
  }
  public void setWelfareMonth(String welfareMonth) {
    this.welfareMonth = welfareMonth;
  }
  public String getWelfareItem() {
    return welfareItem;
  }
  public void setWelfareItem(String welfareItem) {
    this.welfareItem = welfareItem;
  }
  public String getFreeGift() {
    return freeGift;
  }
  public void setFreeGift(String freeGift) {
    this.freeGift = freeGift;
  }
  public double getWelfarePayment() {
    return welfarePayment;
  }
  public void setWelfarePayment(double welfarePayment) {
    this.welfarePayment = welfarePayment;
  }
  public String getTaxAffares() {
    return taxAffares;
  }
  public void setTaxAffares(String taxAffares) {
    this.taxAffares = taxAffares;
  }
  public Date getAddTime() {
    return addTime;
  }
  public void setAddTime(Date addTime) {
    this.addTime = addTime;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  
  
}
