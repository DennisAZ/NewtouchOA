package yh.subsys.oa.examManage.data;

import java.io.Serializable;

public class YHExamQuizSet implements Serializable{
  private int seqId;//  SEQ_ID  number  自增ID
  private String roomCode;//  ROOM_CODE VARCHAR(200)  题库编号
  private String roomName;//  ROOM_NAME VARCHAR(200)  题库名称
  private String roomDesc;//  ROOM_DESC CLOB  题库说明
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
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
  public String getRoomDesc() {
    return roomDesc;
  }
  public void setRoomDesc(String roomDesc) {
    this.roomDesc = roomDesc;
  }

}
