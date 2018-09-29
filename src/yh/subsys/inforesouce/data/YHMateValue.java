package yh.subsys.inforesouce.data;



/**
 * 值域表
 * @author qwx110
 *
 */
public class YHMateValue{
  private int seqId;        //主键
 // private int valueId;      //编号
  private String typeNumber;
  private String valueName; //名称
  public int getSeqId(){
    return seqId;
  }
  public void setSeqId(int seqId){
    this.seqId = seqId;
  }
 
  public String getValueName(){
    return valueName;
  }
  public void setValueName(String valueName){
    this.valueName = valueName;
  }
  
  public String toString(){
    StringBuffer sb = new StringBuffer();
    sb.append("{");
      sb.append("seqId:").append(seqId).append(",");
      sb.append("typeNumber:").append(typeNumber).append(",");
      sb.append("valueName:").append("'").append(valueName).append("'");
    sb.append("}");
    return sb.toString();
  }
  public String getTypeNumber() {
    return typeNumber;
  }
  public void setTypeNumber(String typeNumber) {
    this.typeNumber = typeNumber;
  }
  //测试一下toString
  public static void main(String[] args){
    YHMateValue mv = new  YHMateValue();
    mv.setSeqId(1);
  //  mv.typeNumber(123);
    mv.setValueName("aaaaaaaaaaaa");
    //System.out.println(mv.toString());
  }
}
