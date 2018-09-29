package yh.subsys.oa.profsys.data;

import java.util.Date;

public class YHProjectFile {
  private int seqId;//SEQ_ID  int 流水号
  private int projId;//PROJ_ID Int 来访项目ID
  private String projCreator;//PROJ_CREATOR  VARCHAR(20) 用户ID
  private Date projDate;//PROJ_DATA CLOB  数据
  private String fileNum;//FILE_NUM  VARCHAR(20) 文档编号
  private String fileName;//FILE_NAME VARCHAR(50) 文档名称
  private String fileType;//FILE_TYPE VARCHAR(20) 文档类型
  private String fileCreator;//FILE_CREATOR  varchar(20) 创建人员
  private String fileTitle;//FILE_TITLE  CLOB  文档主题词
  private String fileContent;//FILE_CONTENT  CLOB  内容
  private String fileNote;//FILE_NOTE CLOB  备注
  private String attachmentId;//ATTACHMENT_ID CLOB  附件ID
  private String attachmentName;//ATTACHMENT_NAME varchar(200)  附件名称
  private String projFileType;//FILE_TYPE VARCHAR(2)  项目文件类别0：来访项目1：出访项目2：大型活动项目
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getProjId() {
    return projId;
  }
  public void setProjId(int projId) {
    this.projId = projId;
  }
  public String getProjCreator() {
    return projCreator;
  }
  public void setProjCreator(String projCreator) {
    this.projCreator = projCreator;
  }
  public Date getProjDate() {
    return projDate;
  }
  public void setProjDate(Date projDate) {
    this.projDate = projDate;
  }
  public String getFileNum() {
    return fileNum;
  }
  public void setFileNum(String fileNum) {
    this.fileNum = fileNum;
  }
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public String getFileType() {
    return fileType;
  }
  public void setFileType(String fileType) {
    this.fileType = fileType;
  }
  public String getFileCreator() {
    return fileCreator;
  }
  public void setFileCreator(String fileCreator) {
    this.fileCreator = fileCreator;
  }
  public String getFileTitle() {
    return fileTitle;
  }
  public void setFileTitle(String fileTitle) {
    this.fileTitle = fileTitle;
  }
  public String getFileContent() {
    return fileContent;
  }
  public void setFileContent(String fileContent) {
    this.fileContent = fileContent;
  }
  public String getFileNote() {
    return fileNote;
  }
  public void setFileNote(String fileNote) {
    this.fileNote = fileNote;
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
  public String getProjFileType() {
    return projFileType;
  }
  public void setProjFileType(String projFileType) {
    this.projFileType = projFileType;
  }


}
