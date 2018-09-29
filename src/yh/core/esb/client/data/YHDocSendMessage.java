package yh.core.esb.client.data;
import java.util.Date;

import yh.core.esb.client.data.YHEsbMessage;
import yh.core.esb.client.logic.YHObjectUtility;
public class YHDocSendMessage extends YHEsbMessage {
  
  public static String KEY_SEND_DOC_MESSAGE = "send_doc";
  private int runId;
  private String fromDept;
  private String fromDeptName;
  private Date sendTime;
  private String docName ;
  private String title;
  private String doc;
  public String getDoc() {
    return doc;
  }
  public void setDoc(String doc) {
    this.doc = doc;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  /**
   * 
   * @param runId 发文流水号
   * @param fromDept 发文部门
   * @param fromDeptName 发文部门名称
   * @param sendTime 发送的时间
   * @param docName 公文文件名
   * @param doc 发送公文文号
   * @throws Exception
   */
  public YHDocSendMessage(int runId, String fromDept, String fromDeptName,
      Date sendTime ,String docName , String doc , String title) throws Exception {
    this.runId = runId;
    this.fromDept = fromDept;
    this.fromDeptName = fromDeptName;
    this.sendTime = sendTime;
    this.docName = docName;
    this.doc = doc;
    this.title = title;
    this.setMessage(KEY_SEND_DOC_MESSAGE);
    this.setData(YHObjectUtility.writeObject(this));
  }
  public String getDocName() {
    return docName;
  }
  public void setDocName(String docName) {
    this.docName = docName;
  }
  public int getRunId() {
    return runId;
  }
  public void setRunId(int runId) {
    this.runId = runId;
  }
  public Date getSendTime() {
    return sendTime;
  }
  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
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
}
