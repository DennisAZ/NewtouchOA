package yh.core.funcs.workplan.data;
import java.io.Serializable;
import java.sql.Date;

import yh.core.util.YHUtility;

public class YHWorkPlan implements Serializable{
  private int seqId;//计划ID
  private String name;//计划名称
  private String content;//计划内容
  private Date statrTime ;//有效期--开始日期
  private Date endTime ; // 有效期--结束日期
  private String type;// 计划类型,对应PLAN_TYPE表的TYPE_ID字段       
  private String deptParentDesc;//发布范围（部门）,逗号分隔的部门ID串 
  private String leader2Desc;//负责人,逗号分隔的用户ID串
  private String leader1Desc;//参与人,逗号分隔的用户ID串
  private String creator;//创建人取当前登录的用户
  private Date createdate;//  创建时间,取系统当前日期，形如：2010-03-04
  private String attachmentid;// 附件ID串, 逗号分隔的随机数串
  private String attachmentname;// 附件名称串  *号分隔的文件名串 
  private String attachmentcomment;//  附件说明
  private String remark;//  备注 
  private String managerDesc;// 发布范围（人员）逗号分隔的用户ID串
  private String suspendflag;// 暂停标记,0-暂停,1-未暂停
  private String publish;// 发布标识,0-未发布,1-已发布 
  private String leader3Desc;// 批注领导,逗号分隔的用户ID串 
  private String smsflag;//  短信标记 进度没有达到100%，就到了结束日期的，会发短信提醒相应的参与人  SMS_FLAG 标记是否发过短信

  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getContent() {
    String contentName = null;
    if (!YHUtility.isNullorEmpty(content)) {
      contentName = content.replaceAll("\r\n", "");
      return contentName;
    } else {
      return contentName;
    }
  }
  public void setContent(String content) {
    this.content = content;
  }
  public Date getStatrTime() {
    return statrTime;
  }
  public void setStatrTime(Date statrTime) {
    this.statrTime = statrTime;
  }
  public Date getEndTime() {
    return endTime;
  }
  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getDeptParentDesc() {
    return deptParentDesc;
  }
  public void setDeptParentDesc(String deptParentDesc) {
    this.deptParentDesc = deptParentDesc;
  }
  public String getLeader2Desc() {
    return leader2Desc;
  }
  public void setLeader2Desc(String leader2Desc) {
    this.leader2Desc = leader2Desc;
  }
  public String getLeader1Desc() {
    return leader1Desc;
  }
  public void setLeader1Desc(String leader1Desc) {
    this.leader1Desc = leader1Desc;
  }
  public String getCreator() {
    return creator;
  }
  public void setCreator(String creator) {
    this.creator = creator;
  }
  public Date getCreatedate() {
    return createdate;
  }
  public void setCreatedate(Date createdate) {
    this.createdate = createdate;
  }
  public String getAttachmentid() {
    return attachmentid;
  }
  public void setAttachmentid(String attachmentid) {
    this.attachmentid = attachmentid;
  }
  public String getAttachmentname() {
    return attachmentname;
  }
  public void setAttachmentname(String attachmentname) {
    this.attachmentname = attachmentname;
  }
  public String getAttachmentcomment() {
    return attachmentcomment;
  }
  public void setAttachmentcomment(String attachmentcomment) {
    this.attachmentcomment = attachmentcomment;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  public String getManagerDesc() {
    return managerDesc;
  }
  public void setManagerDesc(String managerDesc) {
    this.managerDesc = managerDesc;
  }
  public String getSuspendflag() {
    return suspendflag;
  }
  public void setSuspendflag(String suspendflag) {
    this.suspendflag = suspendflag;
  }
  public String getPublish() {
    return publish;
  }
  public void setPublish(String publish) {
    this.publish = publish;
  }
  public String getLeader3Desc() {
    return leader3Desc;
  }
  public void setLeader3Desc(String leader3Desc) {
    this.leader3Desc = leader3Desc;
  }
  public String getSmsflag() {
    return smsflag;
  }
  public void setSmsflag(String smsflag) {
    this.smsflag = smsflag;
  }

}
