package yh.core.funcs.dept.data;

import java.util.ArrayList;
import java.util.Iterator;

import yh.core.data.YHDsField;

public class YHDepartment {

  @Override
  public String toString() {
    return "YHDepartment [deptCode=" + deptCode + ", deptFunc=" + deptFunc
        + ", deptList=" + deptList + ", deptName=" + deptName + ", deptNo="
        + deptNo + ", deptParent=" + deptParent + ", faxNo=" + faxNo
        + ", leader1=" + leader1 + ", leader2=" + leader2 + ", manager="
        + manager + ", seqId=" + seqId + ", telNo=" + telNo + "]";
  }
  private int seqId = 0;
  private String deptName = null;
  private String telNo = null;
  private String faxNo = null;
  private String deptNo = null;
  private int deptParent ;
  private String manager = null;
  private String leader1 = null;
  private String leader2 = null;
  private String deptFunc = null;
  private String deptCode;
  
  public String getDeptCode() {
    return deptCode;
  }

  public void setDeptCode(String deptCode) {
    this.deptCode = deptCode;
  }
  private ArrayList<YHDeptPosition> deptList = null;
 
  public Iterator itDept(){
    if(deptList==null){
      deptList=new ArrayList<YHDeptPosition>();
    }
    return deptList.iterator();
  }
  
  public ArrayList<YHDeptPosition> getDeptList() {
    return deptList;
  }
  public void setDeptList(ArrayList<YHDeptPosition> deptList) {
    this.deptList = deptList;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getDeptName() {
    return deptName;
  }
  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }
  public String getTelNo() {
    return telNo;
  }
  public void setTelNo(String telNo) {
    this.telNo = telNo;
  }
  public String getFaxNo() {
    return faxNo;
  }
  public void setFaxNo(String faxNo) {
    this.faxNo = faxNo;
  }
  public String getDeptNo() {
    return deptNo;
  }
  public void setDeptNo(String deptNo) {
    this.deptNo = deptNo;
  }

  public int getDeptParent() {
    return deptParent;
  }
  public void setDeptParent(int deptParent) {
    this.deptParent = deptParent;
  }
  public String getManager() {
    return manager;
  }
  public void setManager(String manager) {
    this.manager = manager;
  }
  public String getLeader1() {
    return leader1;
  }
  public void setLeader1(String leader1) {
    this.leader1 = leader1;
  }
  public String getLeader2() {
    return leader2;
  }
  public void setLeader2(String leader2) {
    this.leader2 = leader2;
  }
  public String getDeptFunc() {
    return deptFunc;
  }
  public void setDeptFunc(String deptFunc) {
    this.deptFunc = deptFunc;
  }
}
