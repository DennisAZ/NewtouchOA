package yh.core.funcs.workflow.data;

import java.util.Date;

public class YHFlowReport {
private int seqId ; 
private int tid ;
private int  flowId;
private String  rName ;
private  String listItem;
private String queryItem;
private String  createuser;
private Date createdate;
private String  groupType;
private String groupField ;
public int getSeqId() {
  return seqId;
}
public void setSeqId(int seqId) {
  this.seqId = seqId;
}
public int getTid() {
  return tid;
}
public void setTid(int tid) {
  this.tid = tid;
}
public int getFlowId() {
  return flowId;
}
public void setFlowId(int flowId) {
  this.flowId = flowId;
}
public String getRName() {
  return rName;
}
public void setRName(String rName) {
  this.rName = rName;
}
public String getListItem() {
  return listItem;
}
public void setListItem(String listItem) {
  this.listItem = listItem;
}
public String getQueryItem() {
  return queryItem;
}
public void setQueryItem(String queryItem) {
  this.queryItem = queryItem;
}
public String getCreateuser() {
  return createuser;
}
public void setCreateuser(String createuser) {
  this.createuser = createuser;
}
public Date getCreatedate() {
  return createdate;
}
public void setCreatedate(Date createdate) {
  this.createdate = createdate;
}
public String getGroupType() {
  return groupType;
}
public void setGroupType(String groupType) {
  this.groupType = groupType;
}
public String getGroupField() {
  return groupField;
}
public void setGroupField(String groupField) {
  this.groupField = groupField;
}

  
  
}
