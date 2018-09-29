package yh.subsys.jtgwjh.task.data;

import java.util.Date;

import yh.core.util.YHUtility;

public class YHJhTaskLog {
  private int seqId;
 // private int taskId;
  private  String userId;
private String userName;
  private String type;//1:外部组织机构数;2:公章3：公告
  private String guid;//任务GUId
  private String status;
  private String toDept;//接收单位
  private String toDeptName;
  private String fromDept;//发送单位
  private String fromDeptName;
  private Date optTime;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
 /* public int getTaskId() {
	return taskId;
}
public void setTaskId(int taskId) {
	this.taskId = taskId;
}*/
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getGuid() {
    return guid;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public String getToDept() {
    return toDept;
  }
  public void setToDept(String toDept) {
    this.toDept = toDept;
  }
  public String getToDeptName() {
    return toDeptName;
  }
  public void setToDeptName(String toDeptName) {
    this.toDeptName = toDeptName;
  }
  public String getFromDept() {
    return fromDept;
  }
  public void setFromDept(String fromDept) {
    this.fromDept = fromDept;
  }
  public String getFromDeptName() {
    return fromDeptName;
  }
  public void setFromDeptName(String fromDeptName) {
    this.fromDeptName = fromDeptName;
  }
  public Date getOptTime() {
    return optTime;
  }
  public void setOptTime(Date optTime) {
    this.optTime = optTime;
  }
public String toXML() {
	 String str = "<seqId>"+seqId+"</seqId>"
			// + "<taskId>"+taskId+"</taskId>"
             + "<userId>"+userId+"</userId>"
             + "<userName>"+userName+"</userName>"
             + "<type>"+type+"</type>"
             + "<toDept>"+toDept+"</toDept>"
             + "<toDeptName>"+toDeptName+"</toDeptName>"
             + "<fromDept>"+fromDept+"</fromDept>"
             + "<fromDeptName>"+fromDeptName+"</fromDeptName>"
             + "<status>"+status+"</status>"
             + "<optTime> "+optTime +"</optTime>"
             + "<guid>"+YHUtility.null2Empty(guid)+"</guid>";
	return str;
}
  
}
