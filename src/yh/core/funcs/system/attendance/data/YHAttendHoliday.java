package  yh.core.funcs.system.attendance.data;

import java.util.Date;
public class YHAttendHoliday {
  private int seqId;
  private Date beginDate;
  private Date endDate;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
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
}
