package yh.user.api.core.data;

import yh.core.util.YHReflectUtility;

public class YHPersonWrap {
  //用户对象
  private Object personObj = null;
  /**
   * 构造函数
   * @param personObj
   */
  public YHPersonWrap(Object personObj) {
    this.personObj = personObj;
  }
  
  /**
   * 取得流水号
   * @return
   */
  public int getSeqId() {
    try {
      return ((Integer)YHReflectUtility.getValue(personObj, "seqId")).intValue();
    }catch(Exception ex) {
      return 0;
    }
  }
  
  /**
   * 取得用户ID
   * @return
   */
  public String getUserId() {
    try {
      return String.valueOf(YHReflectUtility.getValue(personObj, "userId"));
    }catch(Exception ex) {
      return "";
    }
  }
  
  /**
   * 取得用户名
   * @return
   */
  public String getUserName() {
    try {
      return String.valueOf(YHReflectUtility.getValue(personObj, "userName"));
    }catch(Exception ex) {
      return "";
    }
  }
  
  /**
   * 取得别名
   * @return
   */
  public String getByname() {
    try {
      return String.valueOf(YHReflectUtility.getValue(personObj, "byname"));
    }catch(Exception ex) {
      return "";
    }
  }
  
  /**
   * 取得用户角色
   * @return
   */
  public String getUserPriv() {
    try {
      return String.valueOf(YHReflectUtility.getValue(personObj, "userPriv"));
    }catch(Exception ex) {
      return "";
    }
  }
  
  /**
   * 取得辅助角色串
   * @return
   */
  public String getUserPrivOther() {
    try {
      return String.valueOf(YHReflectUtility.getValue(personObj, "userPrivOther"));
    }catch(Exception ex) {
      return "";
    }
  }
  
  /**
   * 取得管理范围
   * @return
   */
  public String getPostPriv() {
    try {
      return String.valueOf(YHReflectUtility.getValue(personObj, "postPriv"));
    }catch(Exception ex) {
      return "";
    }
  }
  
  /**
   * 取得管理范围-指定部门
   * @return
   */
  public String getPostDept() {
    try {
      return String.valueOf(YHReflectUtility.getValue(personObj, "postDept"));
    }catch(Exception ex) {
      return "";
    }
  }
  
  /**
   * 取得部门ID
   * @return
   */
  public int getDeptId() {
    try {
      return ((Integer)YHReflectUtility.getValue(personObj, "deptId")).intValue();
    }catch(Exception ex) {
      return 0;
    }
  }
  
  /**
   * 取得辅助部门
   * @return
   */
  public String getDeptIdOther() {
    try {
      return String.valueOf(YHReflectUtility.getValue(personObj, "deptIdOther"));
    }catch(Exception ex) {
      return "";
    }
  }
  
  /**
   * 取得昵称
   * @return
   */
  public String getNickName() {
    try {
      return String.valueOf(YHReflectUtility.getValue(personObj, "nickName"));
    }catch(Exception ex) {
      return "";
    }
  }
  
  /**
   * 取得用户编码
   * @return
   */
  public int getUserNo() {
    try {
      return ((Integer)YHReflectUtility.getValue(personObj, "userNo")).intValue();
    }catch(Exception ex) {
      return 0;
    }
  }
  
  /**
   * 取得唯一标识
   * @return
   */
  public String getUniqueId() {
    try {
      return String.valueOf(YHReflectUtility.getValue(personObj, "uniqueId"));
    }catch(Exception ex) {
      return "";
    }
  }
  
  /**
   * 按字段名字取值
   * @return
   */
  public String getString(String fieldName) {
    try {
      return String.valueOf(YHReflectUtility.getValue(personObj, fieldName));
    }catch(Exception ex) {
      return "";
    }
  }
}
