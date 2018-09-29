package yh.subsys.oa.giftProduct.outstock.data;

import java.util.Date;

public class YHGiftOutstock {
  private int seqId;
  private int giftId;
  private String transUser;
  private int transFlag;
  private int transQty;
  private Date transDate;
  private String operator;
  private String transMemo;
  private int runId;
  private String transUses;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getGiftId() {
    return giftId;
  }
  public void setGiftId(int giftId) {
    this.giftId = giftId;
  }
  public String getTransUser() {
    return transUser;
  }
  public void setTransUser(String transUser) {
    this.transUser = transUser;
  }
  public int getTransFlag() {
    return transFlag;
  }
  public void setTransFlag(int transFlag) {
    this.transFlag = transFlag;
  }
  public int getTransQty() {
    return transQty;
  }
  public void setTransQty(int transQty) {
    this.transQty = transQty;
  }
  public Date getTransDate() {
    return transDate;
  }
  public void setTransDate(Date transDate) {
    this.transDate = transDate;
  }
  public String getOperator() {
    return operator;
  }
  public void setOperator(String operator) {
    this.operator = operator;
  }
  public String getTransMemo() {
    return transMemo;
  }
  public void setTransMemo(String transMemo) {
    this.transMemo = transMemo;
  }
  public int getRunId() {
    return runId;
  }
  public void setRunId(int runId) {
    this.runId = runId;
  }
  public String getTransUses() {
    return transUses;
  }
  public void setTransUses(String transUses) {
    this.transUses = transUses;
  }
}
