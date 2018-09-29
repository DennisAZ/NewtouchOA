package yh.cms.content.data;

import java.util.Date;

public class YHCmsContent{

  private int seqId;
  private String contentName;
  private String contentTitle;
  private String contentAbstract;
  private String keyword;
  private String contentSource;
  private String contentFileName;
  private String contentAuthor;
  private Date contentDate;
  private int stationId;
  private int columnId;
  private String content;
  private int createId;
  private Date createTime;
  private int contentType;
  private int contentStatus; //新稿、已编、已签、返工、已否、已撤、已发
  private int contentTop;
  private int contentIndex;
  private String url;
  private String attachmentId;//   附件id
  private String attachmentName;//   附件名称
  private String imageUrl;
  public String getImageUrl() {
    return imageUrl;
  }
  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getContentName() {
    return contentName;
  }
  public void setContentName(String contentName) {
    this.contentName = contentName;
  }
  public String getContentTitle() {
    return contentTitle;
  }
  public void setContentTitle(String contentTitle) {
    this.contentTitle = contentTitle;
  }
  public String getContentAbstract() {
    return contentAbstract;
  }
  public void setContentAbstract(String contentAbstract) {
    this.contentAbstract = contentAbstract;
  }
  public String getKeyword() {
    return keyword;
  }
  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }
  public String getContentSource() {
    return contentSource;
  }
  public void setContentSource(String contentSource) {
    this.contentSource = contentSource;
  }
  public String getContentFileName() {
    return contentFileName;
  }
  public void setContentFileName(String contentFileName) {
    this.contentFileName = contentFileName;
  }
  public String getContentAuthor() {
    return contentAuthor;
  }
  public void setContentAuthor(String contentAuthor) {
    this.contentAuthor = contentAuthor;
  }
  public Date getContentDate() {
    return contentDate;
  }
  public void setContentDate(Date contentDate) {
    this.contentDate = contentDate;
  }
  public int getStationId() {
    return stationId;
  }
  public void setStationId(int stationId) {
    this.stationId = stationId;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public int getCreateId() {
    return createId;
  }
  public void setCreateId(int createId) {
    this.createId = createId;
  }
  public Date getCreateTime() {
    return createTime;
  }
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  public int getColumnId() {
    return columnId;
  }
  public void setColumnId(int columnId) {
    this.columnId = columnId;
  }
  public int getContentStatus() {
    return contentStatus;
  }
  public void setContentStatus(int contentStatus) {
    this.contentStatus = contentStatus;
  }
  public int getContentType() {
    return contentType;
  }
  public void setContentType(int contentType) {
    this.contentType = contentType;
  }
  public int getContentTop() {
    return contentTop;
  }
  public void setContentTop(int contentTop) {
    this.contentTop = contentTop;
  }
  public int getContentIndex() {
    return contentIndex;
  }
  public void setContentIndex(int contentIndex) {
    this.contentIndex = contentIndex;
  }
  public String getAttachmentId() {
    return attachmentId;
  }
  public void setAttachmentId(String attachmentId) {
    this.attachmentId = attachmentId;
  }
  public String getAttachmentName() {
    return attachmentName;
  }
  public void setAttachmentName(String attachmentName) {
    this.attachmentName = attachmentName;
  }
}
