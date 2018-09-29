package yh.subsys.jtgwjh.setting.data;

public class YHJhSysPara {
  private int seqId;//SEQ_ID  int 流水号 Y   自增     
  private String paraName;//  PARA_NAME Varchar(40) 参数名称，BENBN_USER,NANYIN_USER          
  private String paraValue;//  PARA_VALUE   CLOB 参数值        
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getParaName() {
    return paraName;
  }
  public void setParaName(String paraName) {
    this.paraName = paraName;
  }
  public String getParaValue() {
    return paraValue;
  }
  public void setParaValue(String paraValue) {
    this.paraValue = paraValue;
  }
}
