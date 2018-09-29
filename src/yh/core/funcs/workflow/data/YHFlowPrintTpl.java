package yh.core.funcs.workflow.data;

public class YHFlowPrintTpl {

     private int seqId ;
     private int flowId;
     private String tType  ;
     private String tName ;
     private String content;
     private String flowPrcs ;
         
    public int getSeqId() {
      return seqId;
    }
    public void setSeqId(int seqId) {
      this.seqId = seqId;
    }
    public int getFlowId() {
      return flowId;
    }
    public void setFlowId(int flowId) {
      this.flowId = flowId;
    }
    public String getTType() {
      return tType;
    }
    public void setTType(String tType) {
      this.tType = tType;
    }
    public String getTName() {
      return tName;
    }
    public void setTName(String tName) {
      this.tName = tName;
    }
    public String getContent() {
      return content;
    }
    public void setContent(String content) {
      this.content = content;
    }
    public String getFlowPrcs() {
      return flowPrcs;
    }
    public void setFlowPrcs(String flowPrcs) {
      this.flowPrcs = flowPrcs;
    }
      
}
