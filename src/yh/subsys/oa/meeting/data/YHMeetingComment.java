package yh.subsys.oa.meeting.data;

import java.util.Date;

public class YHMeetingComment {

  private int seqId;
  private int parentId;

	private int meetingId;
  private String userId;
  private String attachmentId;
  private String attachmentName;
  private String content;
  private Date reTime;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
  public int getMeetingId() {
    return meetingId;
  }
  public void setMeetingId(int meetingId) {
    this.meetingId = meetingId;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
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
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public Date getReTime() {
    return reTime;
  }
  public void setReTime(Date reTime) {
    this.reTime = reTime;
  }
  
}
