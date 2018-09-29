package com.psit.struts.entity;

import java.io.Serializable;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;

public class YHPerson  implements Serializable{
  private long seqId;
  private String userId;
  private String userName;
  private String byname;
  private String useingKey;
  private String password;
  private String keySn;
  private String secure;
  private String userPriv;
  private String postPriv;
  private String postDept;
  private int deptId;
  private String deptIdOther;
  private String sex;
  private Date birthday;
  private String isLunar;
  private String telNoDept;
  private String faxNoDept;
  private String addHome;
  private String postNoHome;
  private String telNoHome;
  private String mobilNo;
  private String bpNo;
  private String email;
  private String oicq;
  private String icq;
  private String msn;
  private String nickName;
  private String auatar;
  private String callSound;
  private String bbsSignature;
  private int bbsCounter = 0;
  private int dutyType = 0;
  private Date lastVisitTime;
  private String smsOn;
  private String menuType;
  private int uin;
  private int picId;
  private int authorize;
  private int canbroadcast;
  private int disabled;
  private String mobileSp;
  private String mobilePs1;
  private String mobilePs2;
  private Date lastPassTime;
  private String theme;
  private String shortcut;
  private String panel;
  private int onLine;
  private String onStatus;
  private String userDefine;
  private String mobilNoHidden;
  private String mytableLeft;
  private String mytableRight;
  private int emailCapacity = 0;
  private int folderCapacity = 0;
  private String userPrivOther;
  private int userNo;
  private String notLogin;
  private String notViewUser;
  private String notViewTable;
  private String bkground;
  private String bindIp;
  private String lastVisitIp;
  private String menuImage;
  private String weatherCity;
  private String showRss;
  private String myRss;
  private String remark;
  private String menuExpand;
  private int webmailCapacity;
  private int webmailNum;
  private String myStatus;
  private int score;
  private String tderFlag;
  private String limitLogin;
  private String concernUser;
  private String  uniqueId;
  private Set cusCorCus = new HashSet(0);//客户
  //精灵集成使用的两个字段
  //by pjn(2011-03-31)
  private int imRange;
  private String photo;
	/** default constructor */
	public YHPerson() {
	}
  public YHPerson(Long seNo)
  {
  	this.seqId=seNo;
  }
  public YHPerson(Long seNo,String userName,Set cusCorCus)
  {
  	this.seqId=seNo;
  	this.userName=userName;
  	this.cusCorCus=cusCorCus;
  }
  public int getImRange() {
    return imRange;
  }
  
  public void setImRange(int imRange) {
    this.imRange = imRange;
  }
  
  public String getPhoto() {
    return photo;
  }
  
  public void setPhoto(String photo) {
    this.photo = photo;
  }

  //用户扩展属性集合
  //by pjn(2010-12-02)
  private String paramSet;
  public String getParamSet() {
    return paramSet;
  }
  

  public void setParamSet(String paramSet) {
    this.paramSet = paramSet;
  }

  //用户默认门户ID
  //by pjn(2010-11-30)
  private int defaultPortal;

  public int getDefaultPortal() {
    return defaultPortal;
  }

  public void setDefaultPortal(int defaultPortal) {
    this.defaultPortal = defaultPortal;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  //默认导航菜单状态
  private String nevMenuOpen;

  /**
   * 是否admin用户
   * @return
   */
  public boolean isAdmin() {
    return YHSysProps.getAdminName().equals(userId);
  }
  
  /**
   * 是否系统管理员角色

   * @return
   */
  public boolean isAdminRole() {
   
    
    return "1".equals(this.userPriv);
  }
  
  
  /**
   * 是否系统管理员角色
   * @return
   */
  public boolean isAdminRole(Connection conn) {
    boolean result=false;
    YHPersonLogic pl=new YHPersonLogic();
    String privId;
    String privId1;
    try {
      privId = pl.getPrivName(conn, "OA 安全员");
    //  privId1 = pl.getPrivName(conn, "OA 审计员");
      if("1".equals(this.userPriv)){
        result=true;
      }else if(privId.equals(this.userPriv)){
        result=true;
      }/*else if(privId1.equals(this.userPriv)){
        result=true;
      }*/
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return result;
  }
  
 

  public long getSeqId() {
    return seqId;
  }
  public void setSeqId(long seqId) {
    this.seqId = seqId;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  } 
  public String getByname() {
    return byname;
  }
  public void setByname(String byname) {
    this.byname = byname;
  }
  public String getSecure() {
    return secure;
  }
  public void setSecure(String secure) {
    this.secure = secure;
  }
  public String getOicq() {
    return oicq;
  }
  public void setOicq(String oicq) {
    this.oicq = oicq;
  }
  public String getIcq() {
    return icq;
  }
  public void setIcq(String icq) {
    this.icq = icq;
  }
  public String getUseingKey() {
    return useingKey;
  }
  public void setUseingKey(String useingKey) {
    this.useingKey = useingKey;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getKeySn() {
    return keySn;
  }
  public void setKeySn(String keySn) {
    this.keySn = keySn;
  }
  public String getUserPriv() {
    return userPriv;
  }
  public void setUserPriv(String userPriv) {
    this.userPriv = userPriv;
  }
  public String getPostPriv() {
    return postPriv;
  }
  public void setPostPriv(String postPriv) {
    this.postPriv = postPriv;
  }
  public String getPostDept() {
    return postDept;
  }
  public void setPostDept(String postDept) {
    this.postDept = postDept;
  }
  public int getDeptId() {
    return deptId;
  }
  public void setDeptId(int deptId) {
    this.deptId = deptId;
  }
  public String getDeptIdOther() {
    return deptIdOther;
  }
  public void setDeptIdOther(String deptIdOther) {
    this.deptIdOther = deptIdOther;
  }
  public String getSex() {
    return sex;
  }
  public void setSex(String sex) {
    this.sex = sex;
  }
  public Date getBirthday() {
    return birthday;
  }
  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }
  public String getIsLunar() {
    return isLunar;
  }
  public void setIsLunar(String isLunar) {
    this.isLunar = isLunar;
  }
  public String getTelNoDept() {
    return telNoDept;
  }
  public void setTelNoDept(String telNoDept) {
    this.telNoDept = telNoDept;
  }
  public String getFaxNoDept() {
    return faxNoDept;
  }
  public void setFaxNoDept(String faxNoDept) {
    this.faxNoDept = faxNoDept;
  }
  public String getAddHome() {
    return addHome;
  }
  public void setAddHome(String addHome) {
    this.addHome = addHome;
  }
  public String getPostNoHome() {
    return postNoHome;
  }
  public void setPostNoHome(String postNoHome) {
    this.postNoHome = postNoHome;
  }
  public String getTelNoHome() {
    return telNoHome;
  }
  public void setTelNoHome(String telNoHome) {
    this.telNoHome = telNoHome;
  }
  public String getMobilNo() {
    return mobilNo;
  }
  public void setMobilNo(String mobilNo) {
    this.mobilNo = mobilNo;
  }
  public String getBpNo() {
    return bpNo;
  }
  public void setBpNo(String bpNo) {
    this.bpNo = bpNo;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getMsn() {
    return msn;
  }
  public void setMsn(String msn) {
    this.msn = msn;
  }
  public String getNickName() {
    return nickName;
  }
  public void setNickName(String nickName) {
    this.nickName = nickName;
  }
  public String getAuatar() {
    return auatar;
  }
  public void setAuatar(String auatar) {
    this.auatar = auatar;
  }
  public String getCallSound() {
    return callSound;
  }
  public void setCallSound(String callSound) {
    this.callSound = callSound;
  }
  public String getBbsSignature() {
    return bbsSignature;
  }
  public void setBbsSignature(String bbsSignature) {
//    if(bbsSignature!= null && !"".equals(bbsSignature.trim())){
//      bbsSignature = bbsSignature.replace("\"", "\\\"").replace("\r", "").replace("\n", "");
//    }
    this.bbsSignature = bbsSignature;
  }
  public int getBbsCounter() {
    return bbsCounter;
  }
  public void setBbsCounter(int bbsCounter) {
    this.bbsCounter = bbsCounter;
  }
  public int getDutyType() {
    return dutyType;
  }
  public void setDutyType(int dutyType) {
    this.dutyType = dutyType;
  }
  public Date getLastVisitTime() {
    return lastVisitTime;
  }
  public void setLastVisitTime(Date lastVisitTime) {
    this.lastVisitTime = lastVisitTime;
  }
  public String getSmsOn() {
    return smsOn;
  }
  public void setSmsOn(String smsOn) {
    this.smsOn = smsOn;
  }
  public String getMenuType() {
    return menuType;
  }
  public void setMenuType(String menuType) {
    this.menuType = menuType;
  }
  public int getUin() {
    return uin;
  }
  public void setUin(int uin) {
    this.uin = uin;
  }
  public int getPicId() {
    return picId;
  }
  public void setPicId(int picId) {
    this.picId = picId;
  }
  public int getAuthorize() {
    return authorize;
  }
  public void setAuthorize(int authorize) {
    this.authorize = authorize;
  }
  public int getCanbroadcast() {
    return canbroadcast;
  }
  public void setCanbroadcast(int canbroadcast) {
    this.canbroadcast = canbroadcast;
  }
  public int getDisabled() {
    return disabled;
  }
  public void setDisabled(int disabled) {
    this.disabled = disabled;
  }
  public String getMobileSp() {
    return mobileSp;
  }
  public void setMobileSp(String mobileSp) {
    this.mobileSp = mobileSp;
  }
  public String getMobilePs1() {
    return mobilePs1;
  }
  public void setMobilePs1(String mobilePs1) {
    this.mobilePs1 = mobilePs1;
  }
  public String getMobilePs2() {
    return mobilePs2;
  }
  public void setMobilePs2(String mobilePs2) {
    this.mobilePs2 = mobilePs2;
  }
  public Date getLastPassTime() {
    return lastPassTime;
  }
  public void setLastPassTime(Date lastPassTime) {
    this.lastPassTime = lastPassTime;
  }
  public String getTheme() {
    return theme;
  }
  public void setTheme(String theme) {
    this.theme = theme;
  }
  public String getShortcut() {
    return shortcut;
  }
  public void setShortcut(String shortcut) {
    this.shortcut = shortcut;
  }
  public String getPanel() {
    return panel;
  }
  public void setPanel(String panel) {
    this.panel = panel;
  }

  public String getOnStatus() {
    return onStatus;
  }
  public void setOnStatus(String onStatus) {
    this.onStatus = onStatus;
  }
  public String getUserDefine() {
    return userDefine;
  }
  public void setUserDefine(String userDefine) {
    this.userDefine = userDefine;
  }
  public String getMobilNoHidden() {
    return mobilNoHidden;
  }
  public void setMobilNoHidden(String mobilNoHidden) {
    this.mobilNoHidden = mobilNoHidden;
  }
  public String getMytableLeft() {
    return mytableLeft;
  }
  public void setMytableLeft(String mytableLeft) {
    this.mytableLeft = mytableLeft;
  }
  public String getMytableRight() {
    return mytableRight;
  }
  public void setMytableRight(String mytableRight) {
    this.mytableRight = mytableRight;
  }
  public int getEmailCapacity() {
    return emailCapacity;
  }
  public void setEmailCapacity(int emailCapacity) {
    this.emailCapacity = emailCapacity;
  }
  public int getFolderCapacity() {
    return folderCapacity;
  }
  public void setFolderCapacity(int folderCapacity) {
    this.folderCapacity = folderCapacity;
  }
  public String getUserPrivOther() {
    return userPrivOther;
  }
  public void setUserPrivOther(String userPrivOther) {
    this.userPrivOther = userPrivOther;
  }
  public int getUserNo() {
    return userNo;
  }
  public void setUserNo(int userNo) {
    this.userNo = userNo;
  }
  public String getNotLogin() {
    return notLogin;
  }
  public void setNotLogin(String notLogin) {
    this.notLogin = notLogin;
  }
  public String getNotViewUser() {
    return notViewUser;
  }
  public void setNotViewUser(String notViewUser) {
    this.notViewUser = notViewUser;
  }
  public String getNotViewTable() {
    return notViewTable;
  }
  public void setNotViewTable(String notViewTable) {
    this.notViewTable = notViewTable;
  }
  public String getBkground() {
    return bkground;
  }
  public void setBkground(String bkground) {
    this.bkground = bkground;
  }
  public String getBindIp() {
    return bindIp;
  }
  public void setBindIp(String bindIp) {
    this.bindIp = bindIp;
  }
  public String getLastVisitIp() {
    return lastVisitIp;
  }
  public void setLastVisitIp(String lastVisitIp) {
    this.lastVisitIp = lastVisitIp;
  }
  public String getMenuImage() {
    return menuImage;
  }
  public void setMenuImage(String menuImage) {
    this.menuImage = menuImage;
  }
  public String getWeatherCity() {
    return weatherCity;
  }
  public void setWeatherCity(String weatherCity) {
    this.weatherCity = weatherCity;
  }
  public String getShowRss() {
    return showRss;
  }
  public void setShowRss(String showRss) {
    this.showRss = showRss;
  }
  public String getMyRss() {
    return myRss;
  }
  public void setMyRss(String myRss) {
    this.myRss = myRss;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  public String getMenuExpand() {
    return menuExpand;
  }
  public void setMenuExpand(String menuExpand) {
    this.menuExpand = menuExpand;
  }
  public int getWebmailCapacity() {
    return webmailCapacity;
  }
  public void setWebmailCapacity(int webmailCapacity) {
    this.webmailCapacity = webmailCapacity;
  }
  public int getWebmailNum() {
    return webmailNum;
  }
  public void setWebmailNum(int webmailNum) {
    this.webmailNum = webmailNum;
  }
  public String getMyStatus() {
    return myStatus;
  }
  public void setMyStatus(String myStatus) {
    this.myStatus = myStatus;
  }
  public int getScore() {
    return score;
  }
  public void setScore(int score) {
    this.score = score;
  }
  public String getTderFlag() {
    return tderFlag;
  }
  public void setTderFlag(String tderFlag) {
    this.tderFlag = tderFlag;
  }
  public String getLimitLogin() {
    return limitLogin;
  }
  public void setLimitLogin(String limitLogin) {
    this.limitLogin = limitLogin;
  }
  public String getConcernUser() {
    return concernUser;
  }
  public void setConcernUser(String concernUser) {
    this.concernUser = concernUser;
  }
  public int getOnLine() {
    return onLine;
  }
  public void setOnLine(int onLine) {
    this.onLine = onLine;
  }
  
  public String getNevMenuOpen() {
    return nevMenuOpen;
  }

  public void setNevMenuOpen(String nevMenuOpen) {
    this.nevMenuOpen = nevMenuOpen;
  }
  
  @Override
  public String toString() {
    return "YHPerson [addHome=" + addHome + ", auatar=" + auatar
        + ", authorize=" + authorize + ", bbsCounter=" + bbsCounter
        + ", bbsSignature=" + bbsSignature + ", bindIp=" + bindIp
        + ", birthday=" + birthday + ", bkground=" + bkground + ", bpNo="
        + bpNo + ", byname=" + byname + ", callSound=" + callSound
        + ", canbroadcast=" + canbroadcast + ", concernUser=" + concernUser
        + ", deptId=" + deptId + ", deptIdOther=" + deptIdOther + ", disabled="
        + disabled + ", dutyType=" + dutyType + ", email=" + email
        + ", emailCapacity=" + emailCapacity + ", faxNoDept=" + faxNoDept
        + ", folderCapacity=" + folderCapacity + ", icq=" + icq + ", isLunar="
        + isLunar + ", keySn=" + keySn + ", lastPassTime=" + lastPassTime
        + ", lastVisitIp=" + lastVisitIp + ", lastVisitTime=" + lastVisitTime
        + ", limitLogin=" + limitLogin + ", menuExpand=" + menuExpand
        + ", menuImage=" + menuImage + ", menuType=" + menuType + ", mobilNo="
        + mobilNo + ", mobilNoHidden=" + mobilNoHidden + ", mobilePs1="
        + mobilePs1 + ", mobilePs2=" + mobilePs2 + ", mobileSp=" + mobileSp
        + ", msn=" + msn + ", myRss=" + myRss + ", myStatus=" + myStatus
        + ", mytableLeft=" + mytableLeft + ", mytableRight=" + mytableRight
        + ", nickName=" + nickName + ", notLogin=" + notLogin
        + ", notViewTable=" + notViewTable + ", notViewUser=" + notViewUser
        + ", oicq=" + oicq + ", onLine=" + onLine + ", onStatus=" + onStatus
        + ", panel=" + panel + ", password=" + password + ", picId=" + picId
        + ", postDept=" + postDept + ", postNoHome=" + postNoHome
        + ", postPriv=" + postPriv + ", remark=" + remark + ", score=" + score
        + ", secure=" + secure + ", seqId=" + seqId + ", sex=" + sex
        + ", shortcut=" + shortcut + ", showRss=" + showRss + ", smsOn="
        + smsOn + ", tderFlag=" + tderFlag + ", telNoDept=" + telNoDept
        + ", telNoHome=" + telNoHome + ", theme=" + theme + ", uin=" + uin
        + ", useingKey=" + useingKey + ", userDefine=" + userDefine
        + ", userId=" + userId + ", userName=" + userName + ", userNo="
        + userNo + ", userPriv=" + userPriv + ", userPrivOther="
        + userPrivOther + ", weatherCity=" + weatherCity + ", webmailCapacity="
        + webmailCapacity + ", webmailNum=" + webmailNum + "]";
  }
  public String toJSON(){
    StringBuffer sb = new StringBuffer("{");
    sb.append("seqId:" + this.seqId);
    if(this.userId != null){
      sb.append(",userId:'" + this.userId + "'");
    }else{
      sb.append(",userId:''");
    }
    sb.append(",userName:'" + this.userName + "'");
    if(this.byname != null){
      sb.append(",byname:'" + this.byname + "'");
    }else{
      sb.append(",byname:''");
    }
    
    sb.append(",useingKey:'" + this.useingKey + "'");
    if(this.password != null){
      sb.append(",password:'" + this.password + "'");
    }else{
      sb.append(",password:''");
    }
    
    sb.append(",keySn:'" + this.keySn + "'"); 
    sb.append(",secure:'" + this.secure + "'");
    sb.append(",userPriv:'" + this.userPriv + "'");
    sb.append(",postPriv:'" + this.postPriv + "'");
    if(this.postDept != null){
      sb.append(",postDept:'" + this.postDept + "'");
    }else{
      sb.append(",postDept:''");
    }
    
    sb.append(",deptId:" + this.deptId);
    if(this.deptIdOther != null){
      sb.append(",deptIdOther:'" + this.deptIdOther + "'");
    }else{
      sb.append(",deptIdOther:''");
    }
    
    sb.append(",sex:'" + this.sex + "'");
    if(this.birthday == null){
      sb.append(",birthday:''");
    }else{
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String sBirthday = sdf.format(this.birthday);
      sb.append(",birthday:'" + sBirthday + "'");
    }
    sb.append(",isLunar:'" + this.isLunar + "'");
    if(this.telNoDept != null){
      sb.append(",telNoDept:'" + this.telNoDept + "'");
    }else{
      
      sb.append(",telNoDept:''");
    }
    
    sb.append(",faxNoDept:'" + this.faxNoDept + "'");
    sb.append(",addHome:'" + this.addHome + "'");
    sb.append(",postNoHome:'" + this.postNoHome + "'");
    
    sb.append(",telNoHome:'" + this.telNoHome + "'");
    if(this.mobilNo != null){
      sb.append(",mobilNo:'" + this.mobilNo + "'");
      
    }else{
      sb.append(",mobilNo:''");
      
    }
        
    sb.append(",bpNo:'" + this.bpNo + "'"); 
    
    if(this.email != null){
      sb.append(",email:'" + this.email + "'");
    }else{
      sb.append(",email:''");
    }
    
    sb.append(",oicq:'" + this.oicq + "'");
    sb.append(",icq:'" + this.icq + "'");
    sb.append(",msn:'" + this.msn + "'");
    sb.append(",nickName:" + this.nickName);
    sb.append(",auatar:'" + this.auatar + "'");
   
    sb.append(",callSound:'" + this.callSound + "'");
    sb.append(",bbsSignature:'" + this.bbsSignature + "'");
    sb.append(",bbsCounter:" + this.bbsCounter ); 
    sb.append(",dutyType:" + this.dutyType );
    sb.append(",lastVisitTime:'" + this.lastVisitTime + "'");
    sb.append(",smsOn:'" + this.smsOn + "'");
    sb.append(",menuType:'" + this.menuType + "'");
    sb.append(",uin:" + this.uin);
    sb.append(",picId:" + this.picId);
   
    sb.append(",authorize:" + this.authorize );
    sb.append(",canbroadcast:" + this.canbroadcast);
    sb.append(",disabled:" + this.disabled); 
    sb.append(",mobileSp:'" + this.mobileSp + "'");
    sb.append(",mobilePs1:'" + this.mobilePs1 + "'");
    sb.append(",mobilePs2:'" + this.mobilePs2 + "'");
    sb.append(",lastPassTime:'" + this.lastPassTime + "'");
    sb.append(",theme:" + this.theme);
    sb.append(",shortcut:'" + this.shortcut + "'");
 
    sb.append(",panel:" + this.panel );
    sb.append(",onLine:" + this.onLine);
    sb.append(",onStatus:'" + this.onStatus + "'");
    sb.append(",userDefine:'" + this.userDefine + "'");
    sb.append(",mobilNoHidden:'" + this.mobilNoHidden + "'");
    sb.append(",mytableLeft:'" + this.mytableLeft + "'");
    sb.append(",mytableRight:" + this.mytableRight);
    sb.append(",emailCapacity:" + this.emailCapacity);
    
    if(this.bindIp != null){
      sb.append(",bindIp:'" + this.bindIp+ "'");
    }else{
      sb.append(",bindIp:''");
    }
    sb.append(",bkground:'" + this.bkground + "'");
    sb.append(",notViewTable:'" + this.notViewTable + "'");
    sb.append(",notViewUser:'" + this.notViewUser + "'");
    
    sb.append(",notLogin:'" + this.notLogin + "'");
    if(this.userPrivOther != null){
      sb.append(",userPrivOther:'" + this.userPrivOther + "'");
    }else{
      sb.append(",userPrivOther:''");
    }
    
    sb.append(",userNo:" + this.userNo);
    sb.append(",folderCapacity:" + this.folderCapacity);


    sb.append(",lastVisitIp:'" + this.lastVisitIp + "'");
    sb.append(",menuImage:'" + this.menuImage + "'");
    sb.append(",weatherCity:'" + this.weatherCity + "'"); 
    sb.append(",showRss:'" + this.showRss + "'");
    sb.append(",myRss:'" + this.myRss + "'");
    if(this.remark != null){
      sb.append(",remark:'" + this.remark + "'");
      
    }else{
      sb.append(",remark:''");
    }
    
    sb.append(",menuExpand:'" + this.menuExpand + "'");
    sb.append(",webmailCapacity:" + this.webmailCapacity);
    sb.append(",webmailNum:" + this.webmailNum);
    sb.append(",myStatus:'" + this.myStatus + "'");
   
    sb.append(",concernUser:'" + this.concernUser + "'");
    sb.append(",limitLogin:'" + this.limitLogin + "'");
    sb.append(",tderFlag:'" + this.tderFlag + "'");
    sb.append(",score:" + this.score);
    
    sb.append("}");
    return sb.toString();
  }
  public String toJsonSimple(){
    StringBuffer sb = new StringBuffer("{");
    sb.append("userId:'" + this.seqId + "',");
    sb.append("userName:'" +  this.userName +"'");
    sb.append("},");
    return sb.toString();
  }
  
  public String getJsonSimple(){
    String up = "{";
    up += "privNo:" + this.seqId + ",";  
    up += "privName:'" + this.userName + "'";  
    up += "},";
    return up; 
  }
  
  /**
   * 安全日志
   * @return
   */
  public String toJsonLog(){
    StringBuffer sb = new StringBuffer("{");
    sb.append("seqId:'" + this.seqId + "',");
    sb.append("userId:'" + this.userId + "',");
    sb.append("userName:'" +  this.userName +"',");
    sb.append("deptId:'" +  this.deptId +"',");
    
    sb.append("userPriv:'" +  this.userPriv +"'");
    sb.append("}");
    return sb.toString();
  }
	public Set getCusCorCus() {
		return cusCorCus;
	}
	public void setCusCorCus(Set cusCorCus) {
		this.cusCorCus = cusCorCus;
	}
}
