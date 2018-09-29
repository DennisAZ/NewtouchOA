//

package yh.core.funcs.workflow.data;

import java.util.Date;

public class YHFlowRun {
  private int runId;
  private int flowId;
  private int beginUser;
  private String runName;
  private Date beginTime;
  private Date endTime;
  private String attachmentId;
  private String attachmentName;
  private String delFlag;
  private String focusUser;
  private int parentRun;
  private String preSet;
  private String aipFiles;
  private int seqId;
  private int formVersion;
  public int getFormVersion() {
    return formVersion;
  }
  public void setFormVersion(int formVersion) {
    this.formVersion = formVersion;
  }
  private String extend;
  
  public String getExtend() {
    return extend;
  }
  public void setExtend(String extend) {
    this.extend = extend;
  }
  public int getRunId() {
    return this.runId;
  }
  public void setRunId(int runId) {
    this.runId = runId;
  }
  public int getFlowId() {
    return this.flowId;
  }
  public void setFlowId(int flowId) {
    this.flowId = flowId;
  }

  public int getBeginUser() {
    return beginUser;
  }
  public void setBeginUser(int beginUser) {
    this.beginUser = beginUser;
  }
  public String getRunName() {
    return this.runName;
  }
  public void setRunName(String runName) {
    this.runName = runName;
  }
  public Date getBeginTime() {
    return this.beginTime;
  }
  public void setBeginTime(Date beginTime) {
    this.beginTime = beginTime;
  }
  public Date getEndTime() {
    return this.endTime;
  }
  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
  public String getAttachmentId() {
    if ( this.attachmentId != null) {
      this.attachmentId = this.attachmentId.trim();
    }
    return this.attachmentId;
  }
  public void setAttachmentId(String attachmentId) {
    this.attachmentId = attachmentId;
  }
  public String getAttachmentName() {
    if (this.attachmentName != null) {
      this.attachmentName = this.attachmentName.trim();
    }
    return this.attachmentName;
  }
  public void setAttachmentName(String attachmentName) {
    this.attachmentName = attachmentName;
  }
  public String getDelFlag() {
    return this.delFlag;
  }
  public void setDelFlag(String delFlag) {
    this.delFlag = delFlag;
  }
  public String getFocusUser() {
    return this.focusUser;
  }
  public void setFocusUser(String focusUser) {
    this.focusUser = focusUser;
  }
  public int getParentRun() {
    return this.parentRun;
  }
  public void setParentRun(int parentRun) {
    this.parentRun = parentRun;
  }
  public String getPreSet() {
    return this.preSet;
  }
  public void setPreSet(String preSet) {
    this.preSet = preSet;
  }
  public String getAipFiles() {
    return this.aipFiles;
  }
  public void setAipFiles(String aipFiles) {
    this.aipFiles = aipFiles;
  }
  public int getSeqId() {
    return this.seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
}
