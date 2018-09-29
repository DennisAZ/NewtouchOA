package yh.subsys.oa.book.data;

import yh.core.util.YHUtility;


  public class YHBookType {
    
    private int seqId;//图书类型Id
    private String typeName=""; //图书名称
  
    public int getSeqId() {
      return seqId;
    }
    public void setSeqId(int seqId) {
      this.seqId = seqId;
    }
    public String getTypeName() {
      if(this.typeName == null || "null".equalsIgnoreCase(typeName)){
        this.typeName ="";
      }
      return typeName;
    }
    public void setTypeName(String typeName) {
      this.typeName = typeName;
    }
    
    public String toJson(){
      StringBuffer sb = new StringBuffer();
      sb.append("{");
        sb.append("seqId:").append(seqId).append(",");
        sb.append("typeName:").append("'").append(YHUtility.encodeSpecial(typeName).trim()).append("'");
      sb.append("}");
      return sb.toString();
    }
  
  }
