package yh.core.esb.common.data;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import yh.core.esb.common.YHEsbTaskInfo;

public class YHTaskInfo {
  private BitSet bitset = new BitSet();
  synchronized public void done(int index) {
    bitset.set(index, true);
  }
  public String hasDone() {
    String ss = "";
    for(int i=bitset.nextSetBit(0); i>=0; i=bitset.nextSetBit(i+1)) { 
      ss += i + ",";
    }
    if (ss.endsWith(","))
      ss = ss.substring(0 , ss.length() - 1);
    return ss;
  }
  
  public boolean isDone(int index) {
    return bitset.get(index);
  }
  private File file;
  private String md5;
  private long fileLength;
  private String toId;
  private int fromId;
  private String guid;
  private String content;
  private String type;
  private Date startTime = new Date();
  private String fromCode;
  private ByteBuffer bytes = null;
  private Map dateCache = new HashMap();
  
  public Map getDateCache() {
    return dateCache;
  }
  public void setDateCache(Map dateCache) {
    this.dateCache = dateCache;
  }
  public ByteBuffer getBytes() {
    return bytes;
  }
  public void setBytes(ByteBuffer bytes) {
    this.bytes = bytes;
  }
  public String getFromCode() {
    return fromCode;
  }
  public void setFromCode(String fromCode) {
    this.fromCode = fromCode;
  }
  public Date getStartTime() {
    return startTime;
  }
  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public File getFile() {
    return file;
  }
  public void setFile(File file) {
    this.file = file;
  }
  public String getMd5() {
    return md5;
  }
  public void setMd5(String md5) {
    this.md5 = md5;
  }
  public long getFileLength() {
    return fileLength;
  }
  public void setFileLength(long fileLength) {
    this.fileLength = fileLength;
  }
  public String getToId() {
    return toId;
  }
  public void setToId(String toId) {
    this.toId = toId;
  }
  public String getGuid() {
    return guid;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }
  public int getFromId() {
    return fromId;
  }
  public void setFromId(int fromId) {
    this.fromId = fromId;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
}
