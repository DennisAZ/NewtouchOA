package yh.subsys.jtgwjh.docSend.data;


public class YHJhDocsendFiles{

  private int seqId;
  private int docsendInfoId;//发文登记表
  private String fileId;//附件id
  private String fileName;//附件名称
  private String reciveDept;//接收单位
  private String reciveDeptDesc;//接收单位名称
  private int isMainDoc;//是否为正文
  private String fileSize;//附件大小
  
  public String toXML(){
    String str = "<seqId>"+seqId+"</seqId>"
               + "<docsendInfoId>"+docsendInfoId+"</docsendInfoId>"
               + "<fileId>"+fileId+"</fileId>"
               + "<fileName>"+fileName+"</fileName>"
               + "<reciveDept>"+reciveDept+"</reciveDept>"
               + "<reciveDeptDesc>"+reciveDeptDesc+"</reciveDeptDesc>"
               + "<isMainDoc>"+isMainDoc+"</isMainDoc>"
               + "<fileSize>"+fileSize+"</fileSize>";
    return str;
  }
  
  public String getFileSize() {
    return fileSize;
  }
  public void setFileSize(String fileSize) {
    this.fileSize = fileSize;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getFileId() {
    return fileId;
  }
  public void setFileId(String fileId) {
    this.fileId = fileId;
  }
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public String getReciveDept() {
    return reciveDept;
  }
  public void setReciveDept(String reciveDept) {
    this.reciveDept = reciveDept;
  }
  public String getReciveDeptDesc() {
    return reciveDeptDesc;
  }
  public void setReciveDeptDesc(String reciveDeptDesc) {
    this.reciveDeptDesc = reciveDeptDesc;
  }
  public int getDocsendInfoId() {
    return docsendInfoId;
  }
  public void setDocsendInfoId(int docsendInfoId) {
    this.docsendInfoId = docsendInfoId;
  }
  public int getIsMainDoc() {
    return isMainDoc;
  }
  public void setIsMainDoc(int isMainDoc) {
    this.isMainDoc = isMainDoc;
  }
  
}
