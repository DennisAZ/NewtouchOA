package yh.core.funcs.workplan.data;
import java.io.Serializable;
public class YHPlanType implements Serializable{
  private int seqId;//类型ID
  private String typeName;//类型名称
  private int typeNO;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getTypeName() {
    return typeName;
  }
  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }
  public int getTypeNO() {
    return typeNO;
  }
  public void setTypeNO(int typeNO) {
    this.typeNO = typeNO;
  }
 
}
