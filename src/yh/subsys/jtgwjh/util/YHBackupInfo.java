package yh.subsys.jtgwjh.util;

import java.util.Date;

public class YHBackupInfo {

  private int seqId;//自增，索引
  private String type;
  private Date datetime;
  private String tableName;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public Date getDatetime() {
    return datetime;
  }
  public void setDatetime(Date datetime) {
    this.datetime = datetime;
  }
  public String getTableName() {
    return tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

}
