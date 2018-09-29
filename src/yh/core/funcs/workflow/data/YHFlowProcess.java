package yh.core.funcs.workflow.data;

import java.sql.Connection;

import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;

public class YHFlowProcess {
  private int seqId;
  private int flowSeqId;
  private int prcsId;
  private String prcsName;
  private String prcsUser;
  private String prcsItem;
  private String hiddenItem;
  private String prcsDept;
  private String prcsPriv;
  private String prcsTo;
  private int setLeft;
  private int setTop;
  private String plugin;
  private String prcsItemAuto;
  private String prcsIn;
  private String prcsOut;
  private String feedback;
  private String prcsInSet;
  private String prcsOutSet;
  private String autoType;
  private String autoUserOp;
  private String autoUser;
  private String userFilter;
  private String timeOut;
  private String timeExcept;
  private String signlook;
  private String topDefault;
  private String userLock;
  private String mailTo;
  private String syncDeal;
  private String syncDealCheck;
  private String turnPriv;
  private int childFlow;
  private String gatherNode;
  private String allowBack;
  private String attachPriv;
  private int autoBaseUser;
  private String conditionDesc;
  private String relation;
  private int remindFlag;
  private int dispAip;
  private String timeOutType;
  private String metadataItem;
  private String extend;
  private String extend1;
  private String docCreate;
  private String docAttachPriv;
  public String getDocCreate() {
    return docCreate;
  }
  public void setDocCreate(String docCreate) {
    this.docCreate = docCreate;
  }
  public String getDocAttachPriv() {
    return docAttachPriv;
  }
  public void setDocAttachPriv(String docAttachPriv) {
    this.docAttachPriv = docAttachPriv;
  }

  public String getAutoSelectRole() {
    return autoSelectRole;
  }

  public void setAutoSelectRole(String autoSelectRole) {
    this.autoSelectRole = autoSelectRole;
  }
  private String autoSelectRole;
  public String getExtend() {
    return extend;
  }
  
  public String getExtend1() {
    return extend1;
  }

  public void setExtend1(String extend1) {
    this.extend1 = extend1;
  }

  public void setExtend(String extend) {
    this.extend = extend;
  }
  public String getMetadataItem() {
    return metadataItem;
  }
  public void setMetadataItem(String metadataItem) {
    this.metadataItem = metadataItem;
  }
  public int getSeqId() {
    return this.seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getFlowSeqId() {
    return this.flowSeqId;
  }
  public void setFlowSeqId(int flowSeqId) {
    this.flowSeqId = flowSeqId;
  }
  public int getPrcsId() {
    return this.prcsId;
  }
  public void setPrcsId(int prcsId) {
    this.prcsId = prcsId;
  }
  public String getPrcsName() {
    return this.prcsName;
  }
  public void setPrcsName(String prcsName) {
    this.prcsName = prcsName;
  }
  public String getPrcsUser() {
    return this.prcsUser;
  }
  public void setPrcsUser(String prcsUser) {
    this.prcsUser = prcsUser;
  }
  public String getPrcsItem() {
    return this.prcsItem;
  }
  public void setPrcsItem(String prcsItem) {
    this.prcsItem = prcsItem;
  }
  public String getHiddenItem() {
    return this.hiddenItem;
  }
  public void setHiddenItem(String hiddenItem) {
    this.hiddenItem = hiddenItem;
  }
  public String getPrcsDept() {
    
    return this.prcsDept;
  }
  public void setPrcsDept(String prcsDept) {
    this.prcsDept = prcsDept;
  }
  public String getPrcsPriv() {
    return this.prcsPriv;
  }
  public void setPrcsPriv(String prcsPriv) {
    this.prcsPriv = prcsPriv;
  }
  public String getPrcsTo() {
    return this.prcsTo;
  }
  public void setPrcsTo(String prcsTo) {
    this.prcsTo = prcsTo;
  }
  public int getSetLeft() {
    return this.setLeft;
  }
  public void setSetLeft(int setLeft) {
    this.setLeft = setLeft;
  }
  public int getSetTop() {
    return this.setTop;
  }
  public void setSetTop(int setTop) {
    this.setTop = setTop;
  }
  public String getPlugin() {
    return this.plugin;
  }
  public void setPlugin(String plugin) {
    this.plugin = plugin;
  }
  public String getPrcsItemAuto() {
    return this.prcsItemAuto;
  }
  public void setPrcsItemAuto(String prcsItemAuto) {
    this.prcsItemAuto = prcsItemAuto;
  }
  public String getPrcsIn() {
    return this.prcsIn;
  }
  public void setPrcsIn(String prcsIn) {
    this.prcsIn = prcsIn;
  }
  public String getPrcsOut() {
    return this.prcsOut;
  }
  public void setPrcsOut(String prcsOut) {
    this.prcsOut = prcsOut;
  }
  public String getFeedback() {
    return this.feedback;
  }
  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }
  public String getPrcsInSet() {
    return this.prcsInSet;
  }
  public void setPrcsInSet(String prcsInSet) {
    this.prcsInSet = prcsInSet;
  }
  public String getPrcsOutSet() {
    return this.prcsOutSet;
  }
  public void setPrcsOutSet(String prcsOutSet) {
    this.prcsOutSet = prcsOutSet;
  }
  public String getAutoType() {
    return this.autoType;
  }
  public void setAutoType(String autoType) {
    this.autoType = autoType;
  }
  public String getAutoUserOp() {
    return this.autoUserOp;
  }
  public void setAutoUserOp(String autoUserOp) {
    this.autoUserOp = autoUserOp;
  }
  public String getAutoUser() {
    return this.autoUser;
  }
  public void setAutoUser(String autoUser) {
    this.autoUser = autoUser;
  }
  public String getUserFilter() {
    return this.userFilter;
  }
  public void setUserFilter(String userFilter) {
    this.userFilter = userFilter;
  }
  public String getTimeOut() {
    return this.timeOut;
  }
  public void setTimeOut(String timeOut) {
    this.timeOut = timeOut;
  }
  public String getTimeExcept() {
    return this.timeExcept;
  }
  public void setTimeExcept(String timeExcept) {
    this.timeExcept = timeExcept;
  }
  public String getSignlook() {
    return this.signlook;
  }
  public void setSignlook(String signlook) {
    this.signlook = signlook;
  }
  public String getTopDefault() {
    return this.topDefault;
  }
  public void setTopDefault(String topDefault) {
    this.topDefault = topDefault;
  }
  public String getUserLock() {
    return this.userLock;
  }
  public void setUserLock(String userLock) {
    this.userLock = userLock;
  }
  public String getMailTo() {
    return this.mailTo;
  }
  public void setMailTo(String mailTo) {
    this.mailTo = mailTo;
  }
  public String getSyncDeal() {
    return this.syncDeal;
  }
  public void setSyncDeal(String syncDeal) {
    this.syncDeal = syncDeal;
    
  }
  public String getSyncDealCheck() {
    return this.syncDealCheck;
  }
  public void setSyncDealCheck(String syncDealCheck) {
    this.syncDealCheck = syncDealCheck;
  }
  public String getTurnPriv() {
    return this.turnPriv;
  }
  public void setTurnPriv(String turnPriv) {
    this.turnPriv = turnPriv;
  }
  public int getChildFlow() {
    return this.childFlow;
  }
  public void setChildFlow(int childFlow) {
    this.childFlow = childFlow;
  }
  public String getGatherNode() {
    return this.gatherNode;
  }
  public void setGatherNode(String gatherNode) {
    this.gatherNode = gatherNode;
  }
  public String getAllowBack() {
    return this.allowBack;
  }
  public void setAllowBack(String allowBack) {
    this.allowBack = allowBack;
  }
  public String getAttachPriv() {
    return this.attachPriv;
  }
  public void setAttachPriv(String attachPriv) {
    this.attachPriv = attachPriv;
  }
  public int getAutoBaseUser() {
    return this.autoBaseUser;
  }
  public void setAutoBaseUser(int autoBaseUser) {
    this.autoBaseUser = autoBaseUser;
  }
  public String getConditionDesc() {
    return this.conditionDesc;
  }
  public void setConditionDesc(String conditionDesc) {
    this.conditionDesc = conditionDesc;
  }
  public String getRelation() {
    return this.relation;
  }
  public void setRelation(String relation) {
    this.relation = relation;
  }
  public int getRemindFlag() {
    return this.remindFlag;
  }
  public void setRemindFlag(int remindFlag) {
    this.remindFlag = remindFlag;
  }
  public int getDispAip() {
    return this.dispAip;
  }
  public void setDispAip(int dispAip) {
    this.dispAip = dispAip;
  }
  public String getTimeOutType() {
    return this.timeOutType;
  }
  public void setTimeOutType(String timeOutType) {
    this.timeOutType = timeOutType;
  }
  public StringBuffer toComplexJSON(){
    StringBuffer sb = new StringBuffer("{");
    /*
    prcsId:1, prcsType:1, childFlow:'1', attach1:1
    , map:'来文单位=>申请出发时间, 文件名称=>目的地及路线  ,页码=>部门主管意见  ,'
      , act1:1 
      , prcsBack:2 
      , backUserOp:'4', backUserOpDesc:'jack', backUser:'6', backUserDesc:'lucy'*/
     sb.append("prcsId:" + this.prcsId + ",");
     sb.append("childFlow:" + this.childFlow + ",");
     if(this.childFlow != 1){
       sb.append("prcsName:'" + this.prcsName + "',");
       if(this.allowBack.equals("1")){
         sb.append("attach1:" + this.allowBack + ",");
       }else{
         sb.append("attach0:" + this.allowBack + ",");
       }
       sb.append("relation:'" + this.relation + "'");
       if(this.prcsTo.equals("")){
         sb.append("act0:0,");
       }else{
         sb.append("act1:1,");
         sb.append("backUserHo:'" + this.autoUserOp + "',");
         sb.append("backUserOp:'" + this.autoUser + "'");
       }
     }else{
      /* var stepNode = {prcsId:3, prcsType:0, prcsName:'审批结果'
         , nextProc:'公告通知，内部邮件，日程安排'
         , userFilter:2
         , autoType:7
         , itemlistauto:2
         , topDefault:1
         , userLock:1
         , feedback:2
         , turnPriv:1
         , allowBack:1
         , syncDeal:1
         , gatherNode:1
         , remindOrnot:0
         , smsRemindNext:1
         , sms2RemindNext:0
         , webMailRemindNext:1
         , mailTo:1
         , mailToDesc:'lily'
         , timeOut:2
         , timeouttype1:1
         , timeexcept2:7
         , plugin:'work hard at goal'
           };*/
       sb.append("prcsName:'" + this.prcsName + "',");
       sb.append("prcsTo:'" + this.prcsTo + "',");
       sb.append("userFilter:" + this.userFilter + ",");
       sb.append("autoType:" + this.autoType + ",");
       if(this.autoType.equals("7")){
         sb.append("itemlistauto:" + this.autoUser + ",");
       }else if(this.autoType.equals("8")){
         sb.append("autoPrcsUser:" + this.autoBaseUser + ",");
       }else if(this.autoType.equals("3")){
         sb.append("autoUser:'" + this.autoUser + "',");
         sb.append("autoUserOp:'" + this.autoUserOp + "',");
       }else if(this.autoType.equals("2")
           ||this.autoType.equals("4")
           ||this.autoType.equals("6")){
         sb.append("autoBaseUser:'" + this.autoBaseUser + "',");
       }
       sb.append("topDefault:" + this.topDefault + ",");
       sb.append("userLock:" + this.userLock +",");
       sb.append("feedBack:" + this.feedback + ",");
       sb.append("turnPriv:" + this.turnPriv + ",");
       sb.append("allowBack:" + this.allowBack + ",");
       sb.append("syncDeal:" + this.syncDeal + ",");
       sb.append("gatherNode:" + this.gatherNode +",");
       //sb.append("remindOrnot:" + this.remindFlag + ",");
       
       sb.append("mailTo:'" + this.mailTo + "',");
       sb.append("timeOut:" + this.timeOut +",");
       if(this.timeOutType.equals("0")){
         
       }
       if(this.timeOutType.equals("1")){
         sb.append("timeouttype1:" + this.timeOutType + ",");
       }else{
         sb.append("timeouttype0:" + this.timeOutType + ",");
       }
       sb.append("timeexcept:'" + this.timeExcept + "',");
       sb.append("plugin:'" + this.plugin + "'");
     } 
    sb.deleteCharAt(sb.length() -1 );
    sb.append("}");
    return sb;
  }
  public StringBuffer toJSON() throws Exception, Exception{
    StringBuffer sb = new StringBuffer("{");
    
    sb.append("prcsId:" + this.prcsId + ",");
    sb.append("childFlow:" + this.childFlow + ",");
    sb.append("prcsName:'" + this.prcsName + "',");
    if(this.childFlow != 0){
      if(this.allowBack.equals("1")){
        sb.append("attach1:" + this.allowBack + ",");
      }else{
        sb.append("attach0:" + this.allowBack + ",");
      }
      if(this.relation != null){
        sb.append("relation:'" + this.relation + "',");
      }else{
        sb.append("relation:'',");
      }
      if (!YHUtility.isNullorEmpty(this.prcsTo) && this.prcsTo.endsWith(",")) {
        this.prcsTo = this.prcsTo.substring(0 , this.prcsTo.length() - 1);
      }
      if("0".equals(this.prcsTo) || YHUtility.isNullorEmpty(this.prcsTo)){
        sb.append("act0:0,");
        sb.append("prcsTo:'" + this.prcsTo + "',");
        sb.append("backUserHo:'',");
        sb.append("backUserOp:'',");
      }else{
        sb.append("act1:1,");
        sb.append("prcsTo:'" + this.prcsTo + "',");
        sb.append("backUserHo:'" + this.autoUserOp + "',");
        sb.append("backUserOp:'" + this.autoUser + "',");
      }
      if(this.userFilter == null){
        this.userFilter = "0";
      }
      sb.append("userFilter:" + this.userFilter + ",");
      if(this.autoType == null){
        this.autoType = "0";
      }
      sb.append("autoType:" + this.autoType + ",");
      if(this.autoType.equals("7")){
        sb.append("formListItem:'" + this.autoUser + "',");
      }else if(this.autoType.equals("8")){
        sb.append("autoPrcsUser:" + this.autoBaseUser + ",");
      }else if(this.autoType.equals("3")){
        if (this.autoUser == null) {
          sb.append("autoUserOp:'',");
        } else {
          sb.append("autoUserOp:'" + this.autoUser + "',");
        }
        if (this.autoUserOp == null) {
          sb.append("autoUserHo:'',");
        } else {
          sb.append("autoUserHo:'" + this.autoUserOp + "',");
        }
      }else if(this.autoType.equals("2")
          ||this.autoType.equals("4")
          ||this.autoType.equals("6")){
        sb.append("autoBaseUser:'" + this.autoBaseUser + "',");
      }
      if(this.topDefault == null){
        this.topDefault = "0";
      }
      sb.append("topDefault:" + this.topDefault + ",");
      if(this.userLock == null){
        this.userLock = "0";
      }
      sb.append("userLock:" + this.userLock +",");
      if(this.feedback == null){
        this.feedback = "0";
      }
      sb.append("feedBack:" + this.feedback + ",");
      if(this.signlook == null){
        this.signlook = "0";
      }
      sb.append("signLook:" + this.signlook + ",");
      if(this.turnPriv == null){
        this.turnPriv = "0";
      }
      sb.append("turnPriv:" + this.turnPriv + ",");
      if(this.allowBack == null){
        this.allowBack = "0";
      }
      sb.append("allowBack:" + this.allowBack + ",");
      if(this.syncDeal == null){
        this.syncDeal = "0";
      }
      sb.append("syncDeal:" + this.syncDeal + ",");
      if(this.gatherNode == null){
        this.gatherNode = "0";
      }
      sb.append("gatherNode:" + this.gatherNode +",");
      
      sb.append("remindFlag:" + this.remindFlag + ",");
      
      if(this.mailTo == null){
        this.mailTo = "";
      }
      sb.append("mailTo:'" + this.mailTo + "',");
      if(this.timeOut == null){
        this.timeOut = "";
      }
      sb.append("timeOut:'" + this.timeOut +"',");
      if(this.timeOutType == null){
        
      }
      sb.append("timeOutType:" + this.timeOutType +",");
      if(this.timeExcept == null){
        this.timeExcept = "00";
      }
      sb.append("timeExcept:'" + this.timeExcept + "',");
      if (extend == null) {
        extend = "";
      }
      sb.append("extend:'" + extend + "',");
      sb.append("dispAip:" + this.dispAip + ",");
      if(this.plugin == null){
        this.plugin = "";
      }
      sb.append("plugin:'" + this.plugin + "'");
    }else{
      if(this.prcsTo == null){
        this.prcsTo = "";
      }
      sb.append("prcsTo:'" + this.prcsTo + "',");
      if(this.userFilter == null){
        this.userFilter = "0";
      }
      sb.append("userFilter:" + this.userFilter + ",");
      
      if(this.autoType == null){
        this.autoType = "0";
      }
      sb.append("autoType:" + this.autoType + ",");
      if(this.autoType.equals("7")){
        sb.append("formListItem:'" + this.autoUser + "',");
      }else if(this.autoType.equals("8")){
        sb.append("autoPrcsUser:" + this.autoBaseUser + ",");
      }else if(this.autoType.equals("3")){
        if (this.autoUser == null) {
          sb.append("autoUserOp:'',");
        } else {
          sb.append("autoUserOp:'" + this.autoUser + "',");
        }
        if (this.autoUserOp == null) {
          sb.append("autoUserHo:'',");
        } else {
          sb.append("autoUserHo:'" + this.autoUserOp + "',");
        }
      }else if(this.autoType.equals("2")
          ||this.autoType.equals("4")
          ||this.autoType.equals("6")){
        sb.append("autoBaseUser:'" + this.autoBaseUser + "',");
      }
      if(this.topDefault == null){
        this.topDefault = "0";
      }
      sb.append("topDefault:" + this.topDefault + ",");
      if(this.userLock == null){
        this.userLock = "0";
      }
      sb.append("userLock:" + this.userLock +",");
      if(this.feedback == null){
        this.feedback = "0";
      }
      sb.append("feedBack:" + this.feedback + ",");
      if(this.signlook == null){
        this.signlook = "0";
      }
      sb.append("signLook:" + this.signlook + ",");
      if(this.turnPriv == null){
        this.turnPriv = "0";
      }
      sb.append("turnPriv:" + this.turnPriv + ",");
      if(this.allowBack == null){
        this.allowBack = "0";
      }
      sb.append("allowBack:" + this.allowBack + ",");
      if(this.syncDeal == null){
        this.syncDeal = "0";
      }
      sb.append("syncDeal:" + this.syncDeal + ",");
      if(this.gatherNode == null){
        this.gatherNode = "0";
      }
      sb.append("gatherNode:" + this.gatherNode +",");
      
      sb.append("remindFlag:" + this.remindFlag + ",");
      
      if(this.mailTo == null){
        this.mailTo = "";
      }
      sb.append("mailTo:'" + this.mailTo + "',");
      if(this.timeOut == null){
        this.timeOut = "";
      }
      sb.append("timeOut:'" + this.timeOut +"',");
      if(this.timeOutType == null){
        
      }
      sb.append("timeOutType:" + this.timeOutType +",");
      if(this.timeExcept == null){
        this.timeExcept = "00";
      }
      sb.append("timeExcept:'" + this.timeExcept + "',");
      if (extend == null) {
        extend = "";
      }
      sb.append("extend:'" + extend + "',");
      if (extend1 == null) {
        extend1 = "";
      }
      sb.append("extend1:'" + extend1 + "',");
      sb.append("autoSelectRole:'" + YHUtility.null2Empty(autoSelectRole) + "',");
      sb.append("dispAip:" + this.dispAip + ",");
      if(this.plugin == null){
        this.plugin = "";
      }
      sb.append("plugin:'" + this.plugin + "'");
    } 
   sb.append("}");
   return sb;
 }
}
