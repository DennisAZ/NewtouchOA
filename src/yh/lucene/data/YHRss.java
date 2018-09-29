package yh.lucene.data;

public class YHRss {
  private int seqId;
  private String userId;
  private String rssUrl;
  private String name;
  
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getRssUrl() {
    return rssUrl;
  }
  public void setRssUrl(String rssUrl) {
    this.rssUrl = rssUrl;
  }
  
}
