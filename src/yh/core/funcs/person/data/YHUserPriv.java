package yh.core.funcs.person.data;

public class YHUserPriv {
  private int seqId;
  private String privName;
  private int privNo;
  private String funcIdStr;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getPrivName() {
    return privName;
  }
  public void setPrivName(String privName) {
    this.privName = privName;
  }
  public int getPrivNo() {
    return privNo;
  }
  public void setPrivNo(int privNo) {
    this.privNo = privNo;
  }
  public String getFuncIdStr() {
    return funcIdStr;
  }
  public void setFuncIdStr(String funcIdStr) {
    this.funcIdStr = funcIdStr;
  }
  public String getJsonSimple(){
    String up = "{";
    up += "privNo:" + this.seqId + ",";  
    up += "privName:'" + this.privName + "'";  
    up += "},";
    return up; 
  }
  public String toString(){
    return "{"
       + "seqId:" + this.seqId + ","
       + "privNo:" + this.privNo + ","
       +"privName:'" + this.privName + "'"
       +"}";
  }
}
