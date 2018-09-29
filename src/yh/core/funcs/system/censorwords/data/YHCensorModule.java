package yh.core.funcs.system.censorwords.data;

import java.util.ArrayList;
import java.util.Iterator;

import yh.core.data.YHDsField;

public class YHCensorModule {
  private int seqId;
  private String moduleCode;
  private String useFlag;
  private String checkUser;
  private String smsRemind;
  private String sms2Remind;
  private String bannedHint;
  private String modHint;
  private String filterHint;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  
  public String getModuleCode() {
    return moduleCode;
  }
  public void setModuleCode(String moduleCode) {
    this.moduleCode = moduleCode;
  }
  public String getUseFlag() {
    return useFlag;
  }
  public void setUseFlag(String useFlag) {
    this.useFlag = useFlag;
  }
  public String getCheckUser() {
    return checkUser;
  }
  public void setCheckUser(String checkUser) {
    this.checkUser = checkUser;
  }
  public String getSmsRemind() {
    return smsRemind;
  }
  public void setSmsRemind(String smsRemind) {
    this.smsRemind = smsRemind;
  }
  public String getSms2Remind() {
    return sms2Remind;
  }
  public void setSms2Remind(String sms2Remind) {
    this.sms2Remind = sms2Remind;
  }
  public String getBannedHint() {
    return bannedHint;
  }
  public void setBannedHint(String bannedHint) {
    this.bannedHint = bannedHint;
  }
  public String getModHint() {
    return modHint;
  }
  public void setModHint(String modHint) {
    this.modHint = modHint;
  }
  public String getFilterHint() {
    return filterHint;
  }
  public void setFilterHint(String filterHint) {
    this.filterHint = filterHint;
  }
}
