//
package yh.core.funcs.workflow.data;

import java.sql.Connection;

import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;

public class YHFlowType {
  
  public int getSeqId() {
    return this.seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getFlowName() {
    return this.flowName;
  }
  public void setFlowName(String flowName) {
    this.flowName = flowName;
  }
  public int getFormSeqId() {
    return this.formSeqId;
  }
  public void setFormSeqId(int formSeqId) {
    this.formSeqId = formSeqId;
  }
  public String getFlowDoc() {
    return this.flowDoc;
  }
  public void setFlowDoc(String flowDoc) {
    this.flowDoc = flowDoc;
  }
  public String getFlowType() {
    return this.flowType;
  }
  public void setFlowType(String flowType) {
    this.flowType = flowType;
  }
  public String getManageUser() {
    return this.manageUser;
  }
  public void setManageUser(String manageUser) {
    this.manageUser = manageUser;
  }
  public int getFlowNo() {
    return this.flowNo;
  }
  public void setFlowNo(int flowNo) {
    this.flowNo = flowNo;
  }
  public int getFlowSort() {
    return this.flowSort;
  }
  public void setFlowSort(int flowSort) {
    this.flowSort = flowSort;
  }
  public String getAutoName() {
    return this.autoName;
  }
  public void setAutoName(String autoName) {
    this.autoName = autoName;
  }
  public int getAutoNum() {
    return this.autoNum;
  }
  public void setAutoNum(int autoNum) {
    this.autoNum = autoNum;
  }
  public int getAutoLen() {
    return this.autoLen;
  }
  public void setAutoLen(int autoLen) {
    this.autoLen = autoLen;
  }
  public String getQueryUser() {
    return this.queryUser;
  }
  public void setQueryUser(String queryUser) {
    this.queryUser = queryUser;
  }
  public String getFlowDesc() {
    return this.flowDesc;
  }
  public void setFlowDesc(String flowDesc) {
    this.flowDesc = flowDesc;
  }
  public String getAutoEdit() {
    return this.autoEdit;
  }
  public void setAutoEdit(String autoEdit) {
    this.autoEdit = autoEdit;
  }
  public String getNewUser(Connection conn) throws Exception {
    this.newUser =  YHOrgSelectLogic.changePriv(conn, this.newUser);
    return this.newUser;
  }
  public String getNewUser() throws Exception {
    return this.newUser;
  }
  public void setNewUser(String newUser) {
    this.newUser = newUser;
  }
  public String getQueryItem() {
    return this.queryItem;
  }
  public void setQueryItem(String queryItem) {
    this.queryItem = queryItem;
  }
  public String getCommentPriv() {
    return this.commentPriv;
  }
  public void setCommentPriv(String commentPriv) {
    this.commentPriv = commentPriv;
  }
  public int getDeptId() {
    return this.deptId;
  }
  public void setDeptId(int deptId) {
    this.deptId = deptId;
  }
  public String getFreePreset() {
    return this.freePreset;
  }
  public void setFreePreset(String freePreset) {
    this.freePreset = freePreset;
  }
  public String getFreeOther() {
    return this.freeOther;
  }
  public void setFreeOther(String freeOther) {
    this.freeOther = freeOther;
  }
  public String getQueryUserDept() {
    return this.queryUserDept;
  }
  public void setQueryUserDept(String queryUserDept) {
    this.queryUserDept = queryUserDept;
  }
  public String getManageUserDept() {
    return this.manageUserDept;
  }
  public void setManageUserDept(String manageUserDept) {
    this.manageUserDept = manageUserDept;
  }
  public String getEditPriv() {
    return this.editPriv;
  }
  public void setEditPriv(String editPriv) {
    this.editPriv = editPriv;
  }
  public String getListFldsStr() {
    return this.listFldsStr;
  }
  public void setListFldsStr(String listFldsStr) {
    this.listFldsStr = listFldsStr;
  }
  public String getAllowPreSet() {
    return this.allowPreSet;
  }
  public void setAllowPreSet(String allowPreSet) {
    this.allowPreSet = allowPreSet;
  }
  public String getModelId() {
    return this.modelId;
  }
  public void setModelId(String modelId) {
    this.modelId = modelId;
  }
  public String getModelName() {
    return this.modelName;
  }
  public void setModelName(String modelName) {
    this.modelName = modelName;
  }
  private int seqId;
  private String flowName;
  private int formSeqId;
  private String flowDoc;
  private String flowType;
  private String manageUser;
  private int flowNo;
  private int flowSort;
  private String autoName;
  private int autoNum;
  private int autoLen;
  private String queryUser;
  private String flowDesc;
  private String autoEdit;
  private String newUser;
  private String queryItem;
  private String commentPriv;
  private int deptId;
  private String freePreset;
  private String freeOther;
  private String queryUserDept;
  private String manageUserDept;
  private String editPriv;
  private String listFldsStr;
  private String allowPreSet;
  private String modelId;
  private String modelName;
  private int isSystem = 0 ;
  public int getIsSystem() {
    return isSystem;
  }
  public void setIsSystem(int isSystem) {
    this.isSystem = isSystem;
  }
  public String toJson(){
    StringBuffer sb  = new StringBuffer("{");
    sb.append("seqId:" + seqId);
    sb.append(",flowName:\"" + YHUtility.encodeSpecial((flowName != null ? flowName : ""))+ "\"");
    sb.append(",formId:" + formSeqId);
    sb.append(",flowDoc:'" + (flowDoc != null ? flowDoc : "")  + "'");
    sb.append(",manageUser:'" + (manageUser != null ? manageUser : "") + "'");
    sb.append(",flowNo:" + flowNo);
    sb.append(",flowSort:" + flowSort);
    sb.append(",autoName:\"" + YHUtility.encodeSpecial((autoName != null ? autoName : "") )+ "\"");
    sb.append(",autoNum:" + autoNum);
    sb.append(",autoLen:" + autoLen);
    sb.append(",deptId:" + deptId);
    if(flowType == null){
      sb.append(",flowType:''");
    }else{
      sb.append(",flowType:'" + this.flowType + "'");
    }
    
    sb.append(",queryUser:'" + (queryUser != null ? queryUser : "") + "'");
    sb.append(",flowDesc:\"" +  YHUtility.encodeSpecial((flowDesc != null ? flowDesc : ""))   + "\"");
    
    sb.append(",autoEdit:'" + (autoEdit != null ? autoEdit : "")  + "'");
    sb.append(",newUser:'" + (newUser != null ? newUser : "") + "'");
    sb.append(",queryItem:'" + (queryItem != null ? queryItem : "") + "'");
    sb.append(",commentPriv:'" + (commentPriv != null ? commentPriv : "") + "'");
    sb.append(",freePreset:'" + (freePreset != null ? freePreset : "") + "'");
    sb.append(",freeOther:'" + (freeOther != null ? freeOther : "") + "'");
    
    sb.append(",queryUserDept:'" + (queryUserDept != null ? queryUserDept : "") + "'");
    sb.append(",manageUserDept:'" + (manageUserDept != null ? manageUserDept : "") + "'");
    sb.append(",editPriv:'" + (editPriv != null ? editPriv : "") + "'");
    sb.append(",listFldsStr:\"" + YHUtility.encodeSpecial(listFldsStr != null ? listFldsStr : "") + "\"");
    sb.append(",allowPreSet:'" + (allowPreSet != null ? allowPreSet : "") + "'");
    sb.append(",modelId:'" + (modelId != null ? modelId : "") + "'");
    sb.append(",modelName:'" + (modelName != null ? modelName : "")  + "'");
    sb.append(",isSystem:'" + isSystem  + "'");
    sb.append("}");
    return sb.toString();
  }
}
