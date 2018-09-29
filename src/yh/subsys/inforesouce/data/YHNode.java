package yh.subsys.inforesouce.data;

public class YHNode {
  private String id ;
  private boolean isRect;
  private String nodeName;
  private int len ;
  private String title;
  private String relation ;
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isRect() {
    return isRect;
  }

  public void setRect(boolean isRect) {
    this.isRect = isRect;
  }

  public YHNode(String id, boolean isRect, String nodeName, int len, String title,
      String relation) {
    this.id = id;
    this.isRect = isRect;
    this.nodeName = nodeName;
    this.len = len;
    this.title = title;
    this.relation = relation;
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public int getLen() {
    return len;
  }

  public void setLen(int len) {
    this.len = len;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getRelation() {
    return relation;
  }

  public void setRelation(String relation) {
    this.relation = relation;
  }

  public String toJson() {
    StringBuffer sb = new StringBuffer();
    sb.append("{id:'" + id + "'");
    sb.append(",isRect:" + isRect );
    sb.append(",nodeName:'" + nodeName + "'");
    sb.append(",len:" + len);
    sb.append(",title:'" + title + "'");
    sb.append(",relation:'" + relation + "'");
    sb.append("}");
    return sb.toString();
  }
}
