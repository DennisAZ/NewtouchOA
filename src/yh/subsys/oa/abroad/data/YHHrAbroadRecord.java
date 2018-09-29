package yh.subsys.oa.abroad.data;

import java.util.Date;

public class YHHrAbroadRecord {
  private int seqId;
  private String createUserId;
  private int createDeptId;
  private String abroadUserId;
  private Date beginDate;
  private Date endDate;
  private String abroadName;
  private String remark;
  
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
  public String getAbroadUserId() {
    return abroadUserId;
  }
  public void setAbroadUserId(String abroadUserId) {
    this.abroadUserId = abroadUserId;
  }
  public Date getBeginDate() {
    return beginDate;
  }
  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }
  public Date getEndDate() {
    return endDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  public String getAbroadName() {
    return abroadName;
  }
  public void setAbroadName(String abroadName) {
    this.abroadName = abroadName;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }

}
