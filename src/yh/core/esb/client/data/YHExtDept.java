package yh.core.esb.client.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class YHExtDept implements Serializable {
  public YHExtDept(String deptNo , String deptName, String esbUser,
      String deptParent, String deptDesc, String deptStatue, String deptTelLine
      , String deptCode, String deptFullName, String deptPasscard, String deptGroup,String deptOfficialName) {
    super();
    this.deptName = deptName;
    this.esbUser = esbUser;
    this.deptNo = deptNo;
    this.deptParent = deptParent;
    this.deptDesc = deptDesc;
    this.deptStatue = deptStatue;
    this.deptTelLine = deptTelLine;
    this.deptCode = deptCode;
    this.deptFullName = deptFullName;
    this.deptPasscard = deptPasscard;
    this.deptGroup = deptGroup;
    this.deptOfficialName = deptOfficialName;
    //this.syncState = syncState;
  }
  public YHExtDept(String deptNo , String deptName, String esbUser,
      String deptParent, String deptDesc, String deptFlag, String deptPermissions
      , String deptPermissionsDesc, String deptStatue, String deptTelLine
      , String deptCode, String deptFullName, String deptPasscard, String deptGroup) {
    super();
    this.deptName = deptName;
    this.esbUser = esbUser;
    this.deptNo = deptNo;
    this.deptParent = deptParent;
    this.deptDesc = deptDesc;
    this.deptFlag = deptFlag;
    this.deptPermissions = deptPermissions;
    this.deptPermissionsDesc = deptPermissionsDesc;
    this.deptStatue = deptStatue;
    this.deptTelLine = deptTelLine;
    this.deptCode = deptCode;
    this.deptFullName = deptFullName;
    this.deptPasscard = deptPasscard;
    this.deptGroup = deptGroup;
    //this.syncState = syncState;
  }
  public YHExtDept() {
    
  }
  public String deptId;
  
  public String getDeptId() {
    return deptId;
  }
  public void setDeptId(String deptId) {
    this.deptId = deptId;
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
  public String getEsbUser() {
    return esbUser;
  }
  public void setEsbUser(String esbUser) {
    this.esbUser = esbUser;
  }
  public String getDeptNo() {
    return deptNo;
  }
  public void setDeptNo(String deptNo) {
    this.deptNo = deptNo;
  }
  public String getDeptParent() {
    return deptParent;
  }
  public void setDeptParent(String deptParent) {
    this.deptParent = deptParent;
  }
  public String getDeptDesc() {
    return deptDesc;
  }
  public void setDeptDesc(String deptDesc) {
    this.deptDesc = deptDesc;
  }
  public String getSyncState() {
    return syncState;
  }
  public void setSyncState(String syncState) {
    this.syncState = syncState;
  }
  public String getDeptFlag() {
    return deptFlag;
  }
  public void setDeptFlag(String deptFlag) {
    this.deptFlag = deptFlag;
  }
  public String getDeptPermissions() {
    return deptPermissions;
  }
  public void setDeptPermissions(String deptPermissions) {
    this.deptPermissions = deptPermissions;
  }
  public String getDeptPermissionsDesc() {
    return deptPermissionsDesc;
  }
  public void setDeptPermissionsDesc(String deptPermissionsDesc) {
    this.deptPermissionsDesc = deptPermissionsDesc;
  }
  public String getDeptStatue() {
    return deptStatue;
  }
  public void setDeptStatue(String deptStatue) {
    this.deptStatue = deptStatue;
  }
  public String getDeptTelLine() {
    return deptTelLine;
  }
  public void setDeptTelLine(String deptTelLine) {
    this.deptTelLine = deptTelLine;
  }
  public int seqId;
  public String deptName;
  public String esbUser;
  public String deptNo;
  public String deptParent;
  public String deptDesc;
  public String syncState;
  public String deptFlag;
  public String deptPermissions;
  public String deptPermissionsDesc;
  public String deptStatue;
  public String deptTelLine;
  
  public String deptFullName;//DEPT_FULL_NAME
  public String deptPasscard;//DEPT_PASSCARD
  public String deptCode;
  public String deptGroup;
  
  public String deptOfficialName;

  public String getDeptOfficialName() {
    return deptOfficialName;
  }
  public void setDeptOfficialName(String deptOfficialName) {
    this.deptOfficialName = deptOfficialName;
  }
  public String getDeptFullName() {
    return deptFullName;
  }
  public void setDeptFullName(String deptFullName) {
    this.deptFullName = deptFullName;
  }
  public String getDeptPasscard() {
    return deptPasscard;
  }
  public void setDeptPasscard(String deptPasscard) {
    this.deptPasscard = deptPasscard;
  }
  public String getDeptCode() {
    return deptCode;
  }
  public void setDeptCode(String deptCode) {
    this.deptCode = deptCode;
  }
  public String getDeptGroup() {
    return deptGroup;
  }
  public void setDeptGroup(String deptGroup) {
    this.deptGroup = deptGroup;
  }
}
