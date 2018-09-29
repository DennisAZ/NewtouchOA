package yh.subsys.inforesouce.data;

/**
 * 保存用户选择的树中选中的节点
 * @author qwx110
 *
 */
public class YHMateNode{
  private int seqId;
  private int userId;
  private String nodes;
  private String tagName;
  public int getSeqId(){
    return seqId;
  }
  public void setSeqId(int seqId){
    this.seqId = seqId;
  }
  public int getUserId(){
    return userId;
  }
  public void setUserId(int userId){
    this.userId = userId;
  }
  public String getNodes(){
    return nodes;
  }
  public void setNodes(String nodes){
    this.nodes = nodes;
  }
  public String getTagName(){
    return tagName;
  }
  public void setTagName(String tagName){
    this.tagName = tagName;
  }
  
  public String toString(){
    StringBuffer sb = new StringBuffer();
    sb.append("{");
       sb.append("seqId:").append("'").append(seqId).append("'").append(",");
       sb.append("userId:").append("'").append(userId).append("'").append(",");
       sb.append("nodes:").append("'").append(nodes).append("'").append(",");
       sb.append("tagName:").append("'").append(tagName).append("'");
    sb.append("}");
    return sb.toString();
  }
}
