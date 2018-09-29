package yh.core.funcs.workplan.data;
import java.io.Serializable;
import java.sql.Date;

public class YHWorkPerson implements Serializable {
  private int seqId;// 自增ID
  private int planId;//对应WORK_PLAN表的PLAN_ID[SEQ_ID]
  private String puserId;// 计划任务指定人员 即：谁的任务
  private Date pbegeiDate;//开始时间  注：单词拼写有误
  private Date pendDate;//结束时间
  private String pplanContent;//计划任务任务内容的文字描述
  private String puseResource; // 相关资源,相关资源的文字描述
  private String attachmentId;//逗号分隔的随机数串
  private String attachmentName;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getPlanId() {
    return planId;
  }
  public void setPlanId(int planId) {
    this.planId = planId;
  }
  public String getPuserId() {
    return puserId;
  }
  public void setPuserId(String puserId) {
    this.puserId = puserId;
  }
  public Date getPbegeiDate() {
    return pbegeiDate;
  }
  public void setPbegeiDate(Date pbegeiDate) {
    this.pbegeiDate = pbegeiDate;
  }
  public Date getPendDate() {
    return pendDate;
  }
  public void setPendDate(Date pendDate) {
    this.pendDate = pendDate;
  }
  public String getPplanContent() {
    return pplanContent;
  }
  public void setPplanContent(String pplanContent) {
    this.pplanContent = pplanContent;
  }
  public String getPuseResource() {
    return puseResource;
  }
  public void setPuseResource(String puseResource) {
    this.puseResource = puseResource;
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
