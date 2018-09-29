package yh.subsys.infomgr.bilingual.data;

import java.util.Date;

public class YHBilingualCorrection {
  private int seqId;
  private String content;
	private String picture;
	private String type;
	private String location;
	private String address;
  private Date correctDate;
  private String correcter;
  private String workplace;
  private String tel;
  private String email;
  private String changes;
  
  private String flag;
  
  public String getFlag() {
    return flag;
  }
  public void setFlag(String flag) {
    this.flag = flag;
  }
	public String getChanges() {
    return changes;
  }
  public void setChanges(String changes) {
    this.changes = changes;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getPicture() {
    return picture;
  }
  public void setPicture(String picture) {
    this.picture = picture;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  public Date getCorrectDate() {
    return correctDate;
  }
  public void setCorrectDate(Date correctDate) {
    this.correctDate = correctDate;
  }
  public String getCorrecter() {
    return correcter;
  }
  public void setCorrecter(String correcter) {
    this.correcter = correcter;
  }
  public String getWorkplace() {
    return workplace;
  }
  public void setWorkplace(String workplace) {
    this.workplace = workplace;
  }
  public String getTel() {
    return tel;
  }
  public void setTel(String tel) {
    this.tel = tel;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
}
