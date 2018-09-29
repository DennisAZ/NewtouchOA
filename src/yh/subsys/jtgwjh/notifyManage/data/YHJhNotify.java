package yh.subsys.jtgwjh.notifyManage.data;

import java.util.Date;
import java.util.List;

import yh.core.util.YHUtility;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendTasks;
import yh.subsys.jtgwjh.task.data.YHJhTaskLog;

public class YHJhNotify {
	private int seqId;
	private String  userId;
	private String userName;
	private String sendDept;
	private String sendDeptName;
	private String guid;
	private String receiveDept;
	private String receiveDeptName;
	private String attachmentId;
	private String attachmentName;
	private Date sendDateTime;
	private String title;
	private String content;
	private String publish;
	private Date createDate;
	
	
	  public String toString(){
		    return "seqId：" + seqId + ",公告标题：" + title;
		  }
	
	public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
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
	public String getSendDept() {
		return sendDept;
	}
	public void setSendDept(String sendDept) {
		this.sendDept = sendDept;
	}
	public String getSendDeptName() {
		return sendDeptName;
	}
	public void setSendDeptName(String sendDeptName) {
		this.sendDeptName = sendDeptName;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getReceiveDept() {
		return receiveDept;
	}
	public void setReceiveDept(String receiveDept) {
		this.receiveDept = receiveDept;
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public Date getSendDateTime() {
		return sendDateTime;
	}
	public void setSendDateTime(Date sendDateTime) {
		this.sendDateTime = sendDateTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPublish() {
		return publish;
	}
	public void setPublish(String publish) {
		this.publish = publish;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getReceiveDeptName() {
		return receiveDeptName;
	}
	public void setReceiveDeptName(String receiveDeptName) {
		this.receiveDeptName = receiveDeptName;
	}
	public String toXML(List<YHJhTaskLog> jhTaskLogList) {
		 StringBuffer notifysendTasksStr = new StringBuffer();
		    for(YHJhTaskLog taskLog : jhTaskLogList){
		    	notifysendTasksStr.append("<task>" + taskLog.toXML() + "</task>");
		    }
		    String str = "<?xml version='1.0' encoding='UTF-8'?>"
		               + "<body>"
		               + "<seqId>"+seqId+"</seqId>"
		               + "<userId>"+userId+"</userId>"
		               + "<userName>"+userName+"</userName>"
		               + "<sendDept>"+sendDept+"</sendDept>"
		               + "<sendDeptName>"+sendDeptName+"</sendDeptName>"
		               + "<guid>"+guid+"</guid>"
		               + "<receiveDept>"+receiveDept+"</receiveDept>"
		               + "<receiveDeptName>"+receiveDeptName+"</receiveDeptName>"
		               + "<title>"+title+"</title>"
		               + "<content>"+content+"</content>"
		               + "<publish>"+publish+"</publish>"
		               + "<createDate>"+createDate.toString().substring(0, 19)+"</createDate>"
		               + "<attachmentId>"+attachmentId+"</attachmentId>"
		               + "<attachmentName>"+attachmentName+"</attachmentName>"
		               + "<sendDateTime>"+sendDateTime.toString().substring(0, 19)+"</sendDateTime>"
		               + "<tasks>"
		               + notifysendTasksStr.toString()
		               + "</tasks>"
		               + "</body>";
		    return str;
	}
	public String toXML() {
		    String str = "<?xml version='1.0' encoding='UTF-8'?>"
		               + "<body>"
		               + "<seqId>"+seqId+"</seqId>"
		               + "<userId>"+userId+"</userId>"
		               + "<userName>"+userName+"</userName>"
		               + "<sendDept>"+sendDept+"</sendDept>"
		               + "<sendDeptName>"+sendDeptName+"</sendDeptName>"
		               + "<guid>"+guid+"</guid>"
		               + "<receiveDept>"+receiveDept+"</receiveDept>"
		               + "<receiveDeptName>"+receiveDeptName+"</receiveDeptName>"
		               + "<title>"+title+"</title>"
		               + "<content>"+content+"</content>"
		               + "<publish>"+publish+"</publish>"
		               + "<createDate>"+createDate.toString().substring(0, 19)+"</createDate>"
		               + "<attachmentId>"+attachmentId+"</attachmentId>"
		               + "<attachmentName>"+attachmentName+"</attachmentName>"
		               + "<sendDateTime>"+sendDateTime.toString().substring(0, 19)+"</sendDateTime>"
		               + "</body>";
		    return str;
	}
	
}