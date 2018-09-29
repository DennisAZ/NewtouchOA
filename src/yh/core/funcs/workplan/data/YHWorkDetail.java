package yh.core.funcs.workplan.data;

import java.io.Serializable;
import java.sql.Date;

public class YHWorkDetail implements Serializable{
  private int seqId;//自增ID
  private String planId; //计划ID对应WORK_PLAN表的PLAN_ID[SEQ_ID]
  private Date writeTime;//日志撰写时间 或批注撰写时间
  private String progress;//进度详情文字描述
  private int percent;//完成百分比
  private String typeFlag;//日志类型0-进度日志1-领导批注
  private String writer;//撰写人
  private String attachmentId;//附件ID串逗号分隔的随机数串
  private String attachmentName;//附件名
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getPlanId() {
    return planId;
  }
  public void setPlanId(String planId) {
    this.planId = planId;
  }
  public Date getWriteTime() {
    return writeTime;
  }
  public void setWriteTime(Date writeTime) {
    this.writeTime = writeTime;
  }
  public String getProgress() {
    return progress;
  }
  public void setProgress(String progress) {
    this.progress = progress;
  }
  public int getPercent() {
    return percent;
  }
  public void setPercent(int percent) {
    this.percent = percent;
  }
  public String getTypeFlag() {
    return typeFlag;
  }
  public void setTypeFlag(String typeFlag) {
    this.typeFlag = typeFlag;
  }
  public String getWriter() {
    return writer;
  }
  public void setWriter(String writer) {
    this.writer = writer;
  }
  public String getAttachmentId() {
    return attachmentId;
  }
  public void setAttachmentId(String attachmentId) {
    this.attachmentId = attachmentId;
  }
  public String getAttachmentName() {
    return attachmentName;
  }
  public void setAttachmentName(String attachmentName) {
    this.attachmentName = attachmentName;
  }
}
