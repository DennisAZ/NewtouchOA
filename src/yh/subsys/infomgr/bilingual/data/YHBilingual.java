package yh.subsys.infomgr.bilingual.data;

import java.util.Date;

public class YHBilingual {
  private int seqId;
  private String type;
	private String cnName;
	private String enName;
	private String soundFile;
	private int entryUser;
	private Date entryDate;
	private String enable;
	
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
  public String getCnName() {
    return cnName;
  }
  public void setCnName(String cnName) {
    this.cnName = cnName;
  }
  public String getEnName() {
    return enName;
  }
  public void setEnName(String enName) {
    this.enName = enName;
  }
  public String getSoundFile() {
    return soundFile;
  }
  public void setSoundFile(String soundFile) {
    this.soundFile = soundFile;
  }
  public int getEntryUser() {
    return entryUser;
  }
  public void setEntryUser(int entryUser) {
    this.entryUser = entryUser;
  }
  public Date getEntryDate() {
    return entryDate;
  }
  public void setEntryDate(Date entryDate) {
    this.entryDate = entryDate;
  }
  public String getEnable() {
    return enable;
  }
  public void setEnable(String enable) {
    this.enable = enable;
  }
}
