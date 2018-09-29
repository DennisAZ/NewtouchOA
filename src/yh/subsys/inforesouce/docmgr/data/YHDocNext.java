package yh.subsys.inforesouce.docmgr.data;

import yh.core.util.YHUtility;

public class YHDocNext{
  private String runId;
  private String flowId;
  private String flowPrcs;
  private String prcsName;
  public String getRunId(){
    if(runId == null || runId=="" || runId =="null"){
      return "";
    }
    return runId;
  }
  public void setRunId(String runId){
    this.runId = runId;
  }
  public String getFlowId(){
    if(flowId == null || flowId=="" || flowId =="null"){
      return "";
    }
    return flowId;
  }
  public void setFlowId(String flowId){
    this.flowId = flowId;
  }
  public String getFlowPrcs(){
    if(flowPrcs == null || flowPrcs=="" || flowPrcs =="null"){
      return "";
    }
    return flowPrcs;
  }
  public void setFlowPrcs(String flowPrcs){
    this.flowPrcs = flowPrcs;
  }
  public String getPrcsName(){
    if(prcsName == null || prcsName=="" || prcsName =="null"){
      return "";
    }
    return prcsName;
  }
  public void setPrcsName(String prcsName){
    this.prcsName = prcsName;
  }
 
  public String toJson(){
    StringBuffer sb = new StringBuffer();
    sb.append("{");
      sb.append("runId:").append("'").append(YHUtility.encodeSpecial(this.getRunId())).append("',");
      sb.append("flowId:").append("'").append(YHUtility.encodeSpecial(this.getFlowId())).append("',");
      sb.append("flowPrcs:").append("'").append(YHUtility.encodeSpecial(this.getFlowPrcs())).append("',");
      sb.append("prcsName:").append("'").append(YHUtility.encodeSpecial(this.getPrcsName())).append("'");
    sb.append("}");
    return sb.toString();
  }
}
