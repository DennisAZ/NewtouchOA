//
package yh.core.funcs.workflow.data;

import java.util.Date;

public class YHFlowRunLog {
  private int seqId;
  private int runId;
  private String runName;
  private int flowId;
  private int prcsId;
  private int flowPrcs;
  private int userId;
  private Date time;
  private String ip;
  private int type;
  private String content;
  public int getSeqId() {
    return this.seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getRunId() {
    return this.runId;
  }
  public void setRunId(int runId) {
    this.runId = runId;
  }
  public String getRunName() {
    return this.runName;
  }
  public void setRunName(String runName) {
    this.runName = runName;
  }
  public int getFlowId() {
    return this.flowId;
  }
  public void setFlowId(int flowId) {
    this.flowId = flowId;
  }
  public int getPrcsId() {
    return this.prcsId;
  }
  public void setPrcsId(int prcsId) {
    this.prcsId = prcsId;
  }
  public int getFlowPrcs() {
    return this.flowPrcs;
  }
  public void setFlowPrcs(int flowPrcs) {
    this.flowPrcs = flowPrcs;
  }
  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
  public Date getTime() {
    return this.time;
  }
  public void setTime(Date time) {
    this.time = time;
  }
  public String getIp() {
    return this.ip;
  }
  public void setIp(String ip) {
    this.ip = ip;
  }
  public int getType() {
    return this.type;
  }
  public void setType(int type) {
    this.type = type;
  }
  public String getContent() {
    return this.content;
  }
  public void setContent(String content) {
    this.content = content;
  }
}
