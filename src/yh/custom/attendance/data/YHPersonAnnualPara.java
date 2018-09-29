package yh.custom.attendance.data;

import java.util.Date;

public class YHPersonAnnualPara {
  private int seqId;
  private String userId;
  private int annualDays;
  private Date changeDate;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public int getAnnualDays() {
    return annualDays;
  }
  public void setAnnualDays(int annualDays) {
    this.annualDays = annualDays;
  }
  public Date getChangeDate() {
    return changeDate;
  }
  public void setChangeDate(Date changeDate) {
    this.changeDate = changeDate;
  }

}
