package yh.core.funcs.system.ispirit.n12.file.data;

import java.util.Date;

public class YHImOfflineFile {
private int id;
private Date  time;
private int  srcUid;
private String  destUid;
private String  fileName;
private long  file_size;
private int  flag;


public String getDestUid() {
  return destUid;
}
public void setDestUid(String destUid) {
  this.destUid = destUid;
}
public int getId() {
  return id;
}
public void setId(int id) {
  this.id = id;
}
public Date getTime() {
  return time;
}
public void setTime(Date time) {
  this.time = time;
}
public int getSrcUid() {
  return srcUid;
}
public void setSrcUid(int srcUid) {
  this.srcUid = srcUid;
}

public String getFileName() {
  return fileName;
}
public void setFileName(String fileName) {
  this.fileName = fileName;
}
public long getFile_size() {
  return file_size;
}
public void setFile_size(long file_size) {
  this.file_size = file_size;
}
public int getFlag() {
  return flag;
}
public void setFlag(int flag) {
  this.flag = flag;
}


}
