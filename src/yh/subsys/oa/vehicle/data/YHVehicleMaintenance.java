package yh.subsys.oa.vehicle.data;

import java.util.Date;

public class YHVehicleMaintenance {
/*  create table VC_MAINTENANCE(
      SEQ_ID INT PRIMARY KEY;
      VC_ID INT ;(VEHICLE  ID 的外健) 
      VM_REQUEST_DATE DATE;(维护日期)
      VM_TYPE VARCHAR2(40);(维护类型)
      VM_REASON VARCHAR2(20); (维护原因)
      VM_FEE decimal(10,2); (维护费用)
      VM_PERSON VARCAHR2(40)；(经办人)
VM_REMARK VARCHAR2(1000)；(维护备注)
      forergn key (VC_ID) REFERENCES VEHICLE(SEQ_ID)
)*/
  private int seqId;
  private int vId;
  private Date vmRequestDate;
  private String vmType;
  private String vmReason;
  //private float vmFee;
  private double vmFee;
  private String vmPerson;
  private String vmRemark;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getVId() {
    return vId;
  }
  public void setVId(int vId) {
    this.vId = vId;
  }
  public Date getVmRequestDate() {
    return vmRequestDate;
  }
  public void setVmRequestDate(Date vmRequestDate) {
    this.vmRequestDate = vmRequestDate;
  }
  public String getVmType() {
    return vmType;
  }
  public void setVmType(String vmType) {
    this.vmType = vmType;
  }
  public String getVmReason() {
    return vmReason;
  }
  public void setVmReason(String vmReason) {
    this.vmReason = vmReason;
  }
  public double getVmFee() {
    return vmFee;
  }
  public void setVmFee(double vmFee) {
    this.vmFee = vmFee;
  }
  public String getVmPerson() {
    return vmPerson;
  }
  public void setVmPerson(String vmPerson) {
    this.vmPerson = vmPerson;
  }
  public String getVmRemark() {
    return vmRemark;
  }
  public void setVmRemark(String vmRemark) {
    this.vmRemark = vmRemark;
  }
 
}
