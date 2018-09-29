package yh.pda.workflow.data;

public class YHPdaWorkflow {

  private int prcsId;
  private int runId;
  private String runName;
  private int flowId;
  private String flowName;
  private String flowType;
  private String prcsFlag;
  private int flowPrcs;
  private String opFlag;
  private String prcsName;
  private String feedback;
  public int getPrcsId() {
    return prcsId;
  }
  public void setPrcsId(int prcsId) {
    this.prcsId = prcsId;
  }
  public int getRunId() {
    return runId;
  }
  public void setRunId(int runId) {
    this.runId = runId;
  }
  public int getFlowId() {
    return flowId;
  }
  public void setFlowId(int flowId) {
    this.flowId = flowId;
  }
  public String getPrcsFlag() {
    return prcsFlag;
  }
  public void setPrcsFlag(String prcsFlag) {
    this.prcsFlag = prcsFlag;
  }
  public int getFlowPrcs() {
    return flowPrcs;
  }
  public void setFlowPrcs(int flowPrcs) {
    this.flowPrcs = flowPrcs;
  }
  public String getOpFlag() {
    return opFlag;
  }
  public void setOpFlag(String opFlag) {
    this.opFlag = opFlag;
  }
  public String getRunName() {
    return runName;
  }
  public void setRunName(String runName) {
    this.runName = runName;
  }
  public String getFlowName() {
    return flowName;
  }
  public void setFlowName(String flowName) {
    this.flowName = flowName;
  }
  public String getFlowType() {
    return flowType;
  }
  public void setFlowType(String flowType) {
    this.flowType = flowType;
  }
  public String getPrcsName() {
    return prcsName;
  }
  public void setPrcsName(String prcsName) {
    this.prcsName = prcsName;
  }
  public String getFeedback() {
    return feedback;
  }
  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }
}
