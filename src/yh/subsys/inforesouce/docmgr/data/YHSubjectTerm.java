package yh.subsys.inforesouce.docmgr.data;



//import java.util.ArrayList;
//import java.util.Iterator;

//import yh.core.data.YHDsField;

public class YHSubjectTerm {
  @Override
  public String toString() {
    return "YHSubjectTerm [word=" + word + ", parentId=" + parentId+ ", seqId=" + seqId + ",sortNo="+sortNo+",typeFlag="+typeFlag+"]";
  }
  private int seqId = 0;
  private String word = null;
  private int parentId = 0;
  private int sortNo=0;
  private int typeFlag=0;
  
  public int getSortNo() {
	return sortNo;
  }
  public void setSortNo(int sortNo) {
	this.sortNo = sortNo;
  }
  public int getTypeFlag() {
	return typeFlag;
  }
  public void setTypeFlag(int typeFlag) {
	this.typeFlag = typeFlag;
  }
  public int getSeqId() {
	return seqId;
  }
  public void setSeqId(int seqId) {
	this.seqId = seqId;
  }
  public String getWord() {
	return word;
  }
  public void setWord(String word) {
	this.word = word;
  }
  public int getParentId() {
	return parentId;
  }
  public void setParentId(int parentId) {
	this.parentId = parentId;
  }
}
  