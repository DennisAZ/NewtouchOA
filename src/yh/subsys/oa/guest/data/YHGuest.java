package yh.subsys.oa.guest.data;

import java.util.Date;

public class YHGuest {
  private int seqId;
  private String guestNum;//GUEST_NUM varchar(20) 贵宾编号
  private String guestName;//GUEST_NAME  varchar(40) 贵宾姓名
  private String guestUnit;//GUEST_UNIT  varchar(200)  贵宾所在单位
  private String guestPhone;//GUEST_PHONE varchar(200)  贵宾联系电话
  private Date guestAttendTime;//GUEST_ATTEND_TIME DATE  来会时间
  private Date guestLeaveTime;//GUEST_LEAVE_TIME  Date  离会时间
  private String guestType;//GUEST_TYPE  varchar(20) 贵宾类型
  private String guestDept;//GUEST_DEPT  varchar(20) 接待部门ID
  private String guestCreator;//GUEST_CREATOR varchar(40) 创建人ID
  private String guestNote;//GUEST_NOTE  CLOB  备注
  private String guestDiner;//GUEST_DINER varchar(20) 是否用餐0:否1：是
  private String attachmentId;//ATTACHMENT_ID CLOB  附件ID字符串
  private String attachmentName;//ATTACHMENT_NAME CLOB  附件名称字符串
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getGuestNum() {
    return guestNum;
  }
  public void setGuestNum(String guestNum) {
    this.guestNum = guestNum;
  }
  public String getGuestName() {
    return guestName;
  }
  public void setGuestName(String guestName) {
    this.guestName = guestName;
  }
  public String getGuestUnit() {
    return guestUnit;
  }
  public void setGuestUnit(String guestUnit) {
    this.guestUnit = guestUnit;
  }
  public String getGuestPhone() {
    return guestPhone;
  }
  public void setGuestPhone(String guestPhone) {
    this.guestPhone = guestPhone;
  }
  public Date getGuestAttendTime() {
    return guestAttendTime;
  }
  public void setGuestAttendTime(Date guestAttendTime) {
    this.guestAttendTime = guestAttendTime;
  }
  public Date getGuestLeaveTime() {
    return guestLeaveTime;
  }
  public void setGuestLeaveTime(Date guestLeaveTime) {
    this.guestLeaveTime = guestLeaveTime;
  }
  public String getGuestType() {
    return guestType;
  }
  public void setGuestType(String guestType) {
    this.guestType = guestType;
  }
  public String getGuestDept() {
    return guestDept;
  }
  public void setGuestDept(String guestDept) {
    this.guestDept = guestDept;
  }
  public String getGuestCreator() {
    return guestCreator;
  }
  public void setGuestCreator(String guestCreator) {
    this.guestCreator = guestCreator;
  }
  public String getGuestNote() {
    return guestNote;
  }
  public void setGuestNote(String guestNote) {
    this.guestNote = guestNote;
  }
  public String getGuestDiner() {
    return guestDiner;
  }
  public void setGuestDiner(String guestDiner) {
    this.guestDiner = guestDiner;
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

}
