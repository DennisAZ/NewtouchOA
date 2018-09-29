package yh.subsys.oa.rollmanage.data;

import java.util.Date;

public class YHRmsRollRoom {

  private int seqId;     
  private int deptId;    
  private String roomCode;
  private String roomName;
  private String remark;
  private String addUser;
  private Date addTime;
  private String modUser;
  private Date modTime;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getDeptId() {
    return deptId;
  }
  public void setDeptId(int deptId) {
    this.deptId = deptId;
  }
  public String getRoomCode() {
    return roomCode;
  }
  public void setRoomCode(String roomCode) {
    this.roomCode = roomCode;
  }
  public String getRoomName() {
    return roomName;
  }
  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  public String getAddUser() {
    return addUser;
  }
  public void setAddUser(String addUser) {
    this.addUser = addUser;
  }
  public Date getAddTime() {
    return addTime;
  }
  public void setAddTime(Date addTime) {
    this.addTime = addTime;
  }
  public String getModUser() {
    return modUser;
  }
  public void setModUser(String modUser) {
    this.modUser = modUser;
  }
  public Date getModTime() {
    return modTime;
  }
  public void setModTime(Date modTime) {
    this.modTime = modTime;
  }
  
}
