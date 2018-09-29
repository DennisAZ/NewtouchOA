package yh.subsys.inforesouce.data;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import yh.core.util.YHUtility;

/**
 * 
 * @author jpt
 *
 */
public class YHSignFile {
  //流水号
  private int seqId = 0;
  //模块编码
  private String moduleNo = null;
  //业务数据ID
  private String recordId = null;
  //文件GUID
  private String fileId = null;
  //文件路径
  private String filePath = null;
  //文件创建时间
  private Date createTime = null;
  //文件更新时间
  private Date updateTime = null;
  //标引标记 1=已经标记，其他=不能标记
  private String signFlag = null;
  //最后一次标引时间
  private Date lastSignTime = null;
  //标引类别 0=自动标引；1=手工标引
  private String signType = null;
  //全文检索标记 1=已经标记，其他=不能标记
  private String fullTextFlag = null;
  //最后一次全文检索时间
  private Date fullTextTime = null;
  //文件名称
  private String fileName = null;
  //文件大小
  private long fileSize = 0;
  //元数据哈希表{M1:"", M2:"", MEX101:"", MEX102:""}
  private Map<String, String> metaMap = new HashMap<String, String>();
  //摘要
  private String abstracts = null;
  
  //页面显示不带后缀文件名
  private String title = null;
  
  //文件大小从库中读取
  private long file_Sizes = 0;
  
  public long getFile_Sizes(){
    return file_Sizes;
  }

  public void setFile_Sizes(long fileSizes){
    file_Sizes = fileSizes;
  }

  public String getTitle(){
    return title;
  }

  public void setTitle(String title){
    this.title = title;
  }

  public String getAbstracts(){
    return abstracts;
  }

  public void setAbstracts(String abstracts){
    this.abstracts = abstracts;
  } 
  
  public String toJson() throws Exception {
    StringBuffer rtBuf = new StringBuffer("{");
    rtBuf.append("seqId:");
    rtBuf.append(seqId);
    rtBuf.append(",");
    rtBuf.append("moduleNo:");
    rtBuf.append("\"" + (this.moduleNo == null ? "" : this.moduleNo) + "\"");
    rtBuf.append(",");
    rtBuf.append("title:");
    rtBuf.append("\"" + (this.title == null ? "" : YHUtility.encodeSpecial(this.title)) + "\"");
    rtBuf.append(",");
    rtBuf.append("recordId:");
    rtBuf.append("\"" + (this.recordId == null ? "" : this.recordId) + "\"");
    rtBuf.append(",");
    rtBuf.append("fileId:");
    rtBuf.append("\"" + (this.fileId == null ? "" : this.fileId) + "\"");
    rtBuf.append(",");
    rtBuf.append("filePath:");
    rtBuf.append("\"" + (this.filePath == null ? "" : this.filePath) + "\"");
    rtBuf.append(",");
    rtBuf.append("createTime:");
    rtBuf.append("\"" + (this.createTime == null ? "" : YHUtility.getDateTimeStr(this.createTime)) + "\"");
    rtBuf.append(",");
    rtBuf.append("updateTime:");
    rtBuf.append("\"" + (this.updateTime == null ? "" : YHUtility.getDateTimeStr(this.updateTime)) + "\"");
    rtBuf.append(",");
    rtBuf.append("signFlag:");
    rtBuf.append("\"" + (this.signFlag == null ? "" : this.signFlag) + "\"");
    rtBuf.append(",");
    rtBuf.append("lastSignTime:");
    rtBuf.append("\"" + (this.lastSignTime == null ? "" : YHUtility.getDateTimeStr(this.lastSignTime)) + "\"");
    rtBuf.append(",");
    rtBuf.append("signType:");
    rtBuf.append("\"" + (this.signType == null ? "" : this.signType) + "\"");
    rtBuf.append(",");
    rtBuf.append("fullTextFlag:");
    rtBuf.append("\"" + (this.fullTextFlag == null ? "" : this.fullTextFlag) + "\"");
    rtBuf.append(",");
    rtBuf.append("fullTextTime:");
    rtBuf.append("\"" + (this.fullTextTime == null ? "" : YHUtility.getDateTimeStr(this.fullTextTime)) + "\"");
    rtBuf.append(",");
    rtBuf.append("fileName:");
    rtBuf.append("\"" + (this.fileName == null ? "" : YHUtility.encodeSpecial(this.fileName)) + "\"");
    rtBuf.append(",");
    rtBuf.append("abstract:");
    rtBuf.append("\"" + (this.abstracts == null ? "" : YHUtility.encodeSpecial(this.abstracts)) + "\"");
    rtBuf.append(",");
    rtBuf.append("file_Sizes:");
    rtBuf.append(this.file_Sizes);
    rtBuf.append(",");
    rtBuf.append("fileSize:");
    rtBuf.append(this.fileSize);
    rtBuf.append(",");
    rtBuf.append("metAttrs:");
    rtBuf.append(toJsonAtts());
    rtBuf.append("}");
    return rtBuf.toString();
  }
  
  /**
   * 获取属性Json
   * @return
   */
  private StringBuffer toJsonAtts() {
    if (this.metaMap == null) {
      return new StringBuffer("{}");
    }
    StringBuffer rtBuf = new StringBuffer("{");
    Iterator<String> iKeys = this.metaMap.keySet().iterator();
    while (iKeys.hasNext()) {
      String key = iKeys.next();
      String value = this.metaMap.get(key);
      if (value == null) {
        value = "";
      }
      String typeNo = null;
      int intNo = 0;
      if (key.startsWith("ATTR_")) {
        intNo = Integer.parseInt(key.substring(5));
      }
      if (intNo < 1) {
        continue;
      }
      if (intNo < 101) {
        typeNo = "M" + intNo;
      }else if (intNo < 201) {
        typeNo = "MEX" + intNo;
      }
      rtBuf.append(typeNo + ":");
      rtBuf.append("\"" + YHUtility.encodeSpecial(value) + "\",");      
    }
    if (rtBuf.length() > 1) {
      rtBuf.delete(rtBuf.length() - 1, rtBuf.length());
    }
    rtBuf.append("}");
    return rtBuf;
  }
  
  /**
   * 迭代属性名称
   * @return
   */
  public Iterator<String> itAttrNames() {
    return metaMap.keySet().iterator();
  }
  /**
   * 设置属性
   * @param attrName
   * @param attrValue
   */
  public void putAttr(Map metas) {
    if (metas == null) {
      return;
    }
    YHUtility.copyMap(metas, metaMap);
  }
  /**
   * 设置属性
   * @param attrName
   * @param attrValue
   */
  public void putAttr(String attrName, String attrValue) {
    metaMap.put(attrName, attrValue);
  }
  /**
   * 取得属性值
   * @param attrName
   * @return
   */
  public String getAttr(String attrName) {
    return metaMap.get(attrName);
  }

  public int getSeqId() {
    return seqId;
  }

  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }

  public String getModuleNo() {
    return moduleNo;
  }

  public void setModuleNo(String moduleNo) {
    this.moduleNo = moduleNo;
  }

  public String getRecordId() {
    return recordId;
  }

  public void setRecordId(String recordId) {
    this.recordId = recordId;
  }

  public String getFileId() {
    return fileId;
  }

  public void setFileId(String fileId) {
    this.fileId = fileId;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
    if (!YHUtility.isNullorEmpty(filePath)) {
      File file = new File(filePath);
      if (file.exists()) {
        this.fileName = file.getName();
        this.fileSize = file.length();
      }
    }
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getSignFlag() {
    return signFlag;
  }

  public void setSignFlag(String signFlag) {
    this.signFlag = signFlag;
  }

  public Date getLastSignTime() {
    return lastSignTime;
  }

  public void setLastSignTime(Date lastSignTime) {
    this.lastSignTime = lastSignTime;
  }

  public String getSignType() {
    return signType;
  }

  public void setSignType(String signType) {
    this.signType = signType;
  }

  public String getFullTextFlag() {
    return fullTextFlag;
  }

  public void setFullTextFlag(String fullTextFlag) {
    this.fullTextFlag = fullTextFlag;
  }

  public Date getFullTextTime() {
    return fullTextTime;
  }

  public void setFullTextTime(Date fullTextTime) {
    this.fullTextTime = fullTextTime;
  }
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public long getFileSize() {
    return fileSize;
  }
  public void setFileSize(long fileSize) {
    this.fileSize = fileSize;
  }
}
