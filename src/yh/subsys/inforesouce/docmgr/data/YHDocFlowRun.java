package yh.subsys.inforesouce.docmgr.data;

import java.util.Date;

public class YHDocFlowRun {
  private int runId;
  private String docId;
  private String docName;
  private String docStyle;
  private int seqId;
  private String doc;
  private int docNum;
  private String docYear;
  private int docWord;
  private Date draftTime;
  private int docType;
  public YHDocFlowRun() {
    
  }
  public YHDocFlowRun(int runId, String doc, String docYear, int docWord,
      Date draftTime, int docType) {
    this.runId = runId;
    this.doc = doc;
    this.docYear = docYear;
    this.docWord = docWord;
    this.draftTime = draftTime;
    this.docType = docType;
  }
  public int getDocType() {
    return docType;
  }
  public void setDocType(int docType) {
    this.docType = docType;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getDoc() {
    return doc;
  }
  public void setDoc(String doc) {
    this.doc = doc;
  }
  public int getDocNum() {
    return docNum;
  }
  public void setDocNum(int docNum) {
    this.docNum = docNum;
  }
  public String getDocYear() {
    return docYear;
  }
  public void setDocYear(String docYear) {
    this.docYear = docYear;
  }
  public int getDocWord() {
    return docWord;
  }
  public void setDocWord(int docWord) {
    this.docWord = docWord;
  }
  public Date getDraftTime() {
    return draftTime;
  }
  public void setDraftTime(Date draftTime) {
    this.draftTime = draftTime;
  }
  public Date getWrittenTime() {
    return writtenTime;
  }
  public void setWrittenTime(Date writtenTime) {
    this.writtenTime = writtenTime;
  }
  public Date getToFileTime() {
    return toFileTime;
  }
  public void setToFileTime(Date toFileTime) {
    this.toFileTime = toFileTime;
  }
  private Date writtenTime;
  private Date toFileTime;
  
  
  public String getDocStyle() {
    return docStyle;
  }
  public void setDocStyle(String docStyle) {
    this.docStyle = docStyle;
  }
  public int getRunId() {
    return runId;
  }
  public void setRunId(int runId) {
    this.runId = runId;
  }
  public String getDocId() {
    return docId;
  }
  public void setDocId(String docId) {
    this.docId = docId;
  }
  public String getDocName() {
    return docName;
  }
  public void setDocName(String docName) {
    this.docName = docName;
  }
}
