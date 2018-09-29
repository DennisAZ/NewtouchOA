package yh.core.funcs.doc.data;
import java.util.Date;
public class YHDocFlowRule {
  private int flowId;
  private int userId ;
  private int toId;
  private Date beginDate;
  private Date endDate;
  private int status;
  private int seqId;
  public int getSeqId() {
    return this.seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public void setStatus(int status) {
    this.status = status;
  }
  public int getStatus() {
    return this.status;
  }
  
  public int getFlowId() {
    return this.flowId;
  }
  
  public void setFlowId(int id) {
    this.flowId = id;
  }
  public int getUserId() {
    return this.userId;
  }
  public void setUserId(int id) {
    this.userId = id;
  }
  public Date getBeginDate() {
    return this.beginDate;
  }
  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }
  public int getToId() {
    return this.toId;
  }
  public void setToId(int id) {
    this.toId = id;
  }
  public Date getEndDate() {
    return this.endDate;
  }
  public void setEndDate(Date beginDate) {
    this.endDate = beginDate;
  }
}
