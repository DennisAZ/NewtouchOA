package yh.subsys.oa.rollmanage.data;

import java.util.Date;

public class YHRmsFile {

	private int seqId;
	private int rollId;				//案卷ID
	private String addUser;			//文件创建人
	private Date addTime;				//文件创建时间形
	private String delUser;			//文件销毁人
	private Date delTime;				//文件销毁时间
	private String modUser;			//修改人
	private Date modTime;				//修改时间
	private String fileCode;		//文件号
	private String fileTitle;		//文件标题(名称)
	private String fileTitleo;		//副标题
	private String fileSubject;	//主题词
	private String sendUnit;		//发文单位
	private Date sendDate;			//发文时间
	private String secret;			//密级
	private String urgency;			//紧急密级
	private String fileKind;		//公文类别
	private String fileType;		//文件分类
	private String filePage;		//文件页数
	private String printPage;						//打印页数
	private String remark;							//备注
	private String attachmentId;				//附件ID
	private String attachmentName;			//附件NAME串
	private int downloadYn;							//附件下载/打印权限
	private int runId;									//公文流程实例ID
	private String docAttachmentId;			//正文附件ID串
	private String docAttachmentName;		//正文附件NAME串  private String handlerTime ; //处理时长
  private String turnCount; //流转次数
	
  private String fileWord;
  private String fileYear;
  private String issueNum;
  private int deadline;  //归档期限
	public int getDeadline() {
	return deadline;
}
public void setDeadline(int deadline) {
	this.deadline = deadline;
}
	public String getFileWord() {
    return fileWord;
  }
  public void setFileWord(String fileWord) {
    this.fileWord = fileWord;
  }
  public String getFileYear() {
    return fileYear;
  }
  public void setFileYear(String fileYear) {
    this.fileYear = fileYear;
  }
  public String getIssueNum() {
    return issueNum;
  }
  public void setIssueNum(String issueNum) {
    this.issueNum = issueNum;
  }
  public String getHandlerTime() {
    return handlerTime;
  }
  public void setHandlerTime(String handlerTime) {
    this.handlerTime = handlerTime;
  }
  public String getTurnCount() {
    return turnCount;
  }
  public void setTurnCount(String turnCount) {
    this.turnCount = turnCount;
  }
  public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public int getRollId() {
		return rollId;
	}
	public void setRollId(int rollId) {
		this.rollId = rollId;
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
	public String getDelUser() {
		return delUser;
	}
	public void setDelUser(String delUser) {
		this.delUser = delUser;
	}
	
	public Date getDelTime() {
		return delTime;
	}
	public void setDelTime(Date delTime) {
		this.delTime = delTime;
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
	public String getFileCode() {
		return fileCode;
	}
	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}
	public String getFileTitle() {
		return fileTitle;
	}
	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

	public String getFileTitleo() {
		return fileTitleo;
	}
	public void setFileTitleo(String fileTitleo) {
		this.fileTitleo = fileTitleo;
	}
	public String getFileSubject() {
		return fileSubject;
	}
	public void setFileSubject(String fileSubject) {
		this.fileSubject = fileSubject;
	}
	public String getSendUnit() {
		return sendUnit;
	}
	public void setSendUnit(String sendUnit) {
		this.sendUnit = sendUnit;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	public String getFileKind() {
		return fileKind;
	}
	public void setFileKind(String fileKind) {
		this.fileKind = fileKind;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFilePage() {
		return filePage;
	}
	public void setFilePage(String filePage) {
		this.filePage = filePage;
	}
	public String getPrintPage() {
		return printPage;
	}
	public void setPrintPage(String printPage) {
		this.printPage = printPage;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public int getDownloadYn() {
		return downloadYn;
	}
	public void setDownloadYn(int downloadYn) {
		this.downloadYn = downloadYn;
	}
	public int getRunId() {
		return runId;
	}
	public void setRunId(int runId) {
		this.runId = runId;
	}
	public String getDocAttachmentId() {
		return docAttachmentId;
	}
	public void setDocAttachmentId(String docAttachmentId) {
		this.docAttachmentId = docAttachmentId;
	}
	public String getDocAttachmentName() {
		return docAttachmentName;
	}
	public void setDocAttachmentName(String docAttachmentName) {
		this.docAttachmentName = docAttachmentName;
	}
	
	
	
	
	
	
}
