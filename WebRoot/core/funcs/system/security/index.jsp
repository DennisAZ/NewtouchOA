<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>系统安全与设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var passFalg = "";
function initVerificationCode() {
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getVerificationCode.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    
    if(rtJson.rtData == "1"){
      document.getElementById("VERIFICATION_CODE1").checked = true;
    }else{
      document.getElementById("VERIFICATION_CODE2").checked = true;
    }
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function doInit(){
  initVerificationCode();
   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityInitPass.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //secInitPass = document.getElementById("paraName").value;
     if(document.getElementById("paraValue").value == "1"){
       document.getElementById("SEC_INIT_PASS1").checked = true;
     }else{
       document.getElementById("SEC_INIT_PASS2").checked = true;
     }
   }else {
     alert(rtJson.rtMsrg); 
   }
   
   //密码定时过期
   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityPassFlag.act";
   var rtJson = getJsonRs(url);
   var passTime = document.getElementById("passTime");
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //secPassFlag = document.getElementById("paraName").value;
     passFalg = document.getElementById("paraValue").value;
     if(document.getElementById("paraValue").value == "1"){
       document.getElementById("SEC_PASS_FLAG1").checked = true;
       passTime.style.display = '';
       //document.getElementById("SEC_PASS_FLAG2").style.dispaly = "";
     }else{
       document.getElementById("SEC_PASS_FLAG2").checked = true;
       passTime.style.display = 'none';
     }
   }else {
     alert(rtJson.rtMsrg); 
   }
   
   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityPassTime.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //secPassTime = document.getElementById("paraValue").value;
     document.getElementById("SEC_PASS_TIME").value = document.getElementById("paraValue").value;
   }else {
     alert(rtJson.rtMsrg);
   }

   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityPassMin.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     //bindJson2Cntrl(rtJson.rtData);
     //secPassMin = document.getElementById("paraName").value;
     if(rtJson.rtData.paraValue == ""){
       document.getElementById("SEC_PASS_MIN").value = "8";
     }else{
       document.getElementById("SEC_PASS_MIN").value = rtJson.rtData.paraValue;
     }
     
   }else {
     alert(rtJson.rtMsrg); 
   }

   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityPassMax.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     //bindJson2Cntrl(rtJson.rtData);
     //secPassMax = document.getElementById("paraName").value;
     if(rtJson.rtData.paraValue == ""){
       document.getElementById("SEC_PASS_MAX").value = "20";
     }else{
       document.getElementById("SEC_PASS_MAX").value = rtJson.rtData.paraValue;
     }
   }else {
     alert(rtJson.rtMsrg);
   }

   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityPassSafe.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     var d = rtJson.rtData;
     //secPassSafe = document.getElementById("paraName").value;
     if(d.SEC_PASS_SAFE == "1"){
       document.getElementById("SEC_PASS_SAFE").checked = true;
     }
     if(d.SEC_PASS_SAFE_SC == "1"){
       document.getElementById("SEC_PASS_SAFE_SC").checked = true;
     }
   }else {
     alert(rtJson.rtMsrg); 
   }

   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityRetryBan.act";
   var rtJson = getJsonRs(url);
   var retryBan = document.getElementById("retryBan");
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //secRetryBan = document.getElementById("paraName").value;
     if(document.getElementById("paraValue").value == "1"){
       document.getElementById("SEC_RETRY_BAN1").checked = true;
       retryBan.style.display = '';
     }else{
       document.getElementById("SEC_RETRY_BAN2").checked = true;
       retryBan.style.display = 'none';
     }
   }else {
     alert(rtJson.rtMsrg); 
   }
   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityRetryTimes.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     //bindJson2Cntrl(rtJson.rtData);
     document.getElementById("SEC_RETRY_TIMES").value = rtJson.rtData.paraValue;
     //secRetryTimes = document.getElementById("paraName").value;
   }else {
     alert(rtJson.rtMsrg); SEC_PASS_TIME
   }

   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityBenTime.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     document.getElementById("SEC_BAN_TIME").value = document.getElementById("paraValue").value;
    // secBanTime = document.getElementById("paraName").value;
   }else {
     alert(rtJson.rtMsrg);
   }

   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityLoginKey.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //loginKey = document.getElementById("paraName").value;
     if(document.getElementById("paraValue").value == "1"){
       document.getElementById("LOGIN_KEY1").checked = true;
     }else{
       document.getElementById("LOGIN_KEY2").checked = true;
     }
   }else {
     alert(rtJson.rtMsrg); 
   }

   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityUserMen.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //secUserMen = document.getElementById("paraName").value;
     if(document.getElementById("paraValue").value == "1"){
       document.getElementById("SEC_USER_MEM1").checked = true;
     }else{
       document.getElementById("SEC_USER_MEM2").checked = true;
     }
   }else {
     alert(rtJson.rtMsrg); 
   }
   //使用用户KEY登录时是否需要输入用户名
   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityKeyuser.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //secKeyUser = document.getElementById("paraName").value;
     if(document.getElementById("paraValue").value == "1"){
       document.getElementById("SEC_KEY_USER1").checked = true;
     }else{
       document.getElementById("SEC_KEY_USER2").checked = true;
     }
   }else {
     alert(rtJson.rtMsrg); 
   }

   //是否启用动态密码卡
   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecritySecureKey.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //loginSecureKey = document.getElementById("paraName").value;
     if(document.getElementById("paraValue").value == "1"){
       document.getElementById("LOGIN_SECURE_KEY1").checked = true;
     }else{
       document.getElementById("LOGIN_SECURE_KEY2").checked = true;
     }
   }else {
     alert(rtJson.rtMsrg); 
   }
   /*

   //是否启用RTX客户端

   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityUseRtx.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //secUseRtx = document.getElementById("paraName").value;
     if(document.getElementById("paraValue").value == "1"){
       document.getElementById("SEC_USE_RTX1").checked = true;
     }else{
       document.getElementById("SEC_USE_RTX2").checked = true;
     }
   }else {
     alert(rtJson.rtMsrg); 
   }
   */
   //精灵IM模式

   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityImModule.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
	   var data=rtJson.rtData;

     if(data.paraValue == "IE"){
       document.getElementById("IM_MODULE2").checked = true;
     }else{
       document.getElementById("IM_MODULE1").checked = true;
     }
   }else {
     alert(rtJson.rtMsrg); 
   }
   
   
 
   //显示用户登录IP
   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityShowIp.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //secShowIpo = document.getElementById("paraName").value;
     if(document.getElementById("paraValue").value == "0"){
       document.getElementById("SEC_SHOW_IP0").checked = true;
     }else if(document.getElementById("paraValue").value == "1"){
       document.getElementById("SEC_SHOW_IP1").checked = true;
     }else if(document.getElementById("paraValue").value == "2"){
       document.getElementById("SEC_SHOW_IP2").checked = true;
     }else{
       document.getElementById("SEC_SHOW_IP2").checked = true;
     }
   }else {
     alert(rtJson.rtMsrg); 
   }
  
   //记忆在线状态

   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityOnStatus.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //secOnStatus = document.getElementById("paraName").value;
     if(document.getElementById("paraValue").value == "1"){
       document.getElementById("SEC_ON_STATUS1").checked = true;
     }else{
       document.getElementById("SEC_ON_STATUS2").checked = true;
     }
   }else {
     alert(rtJson.rtMsrg); 
   }
   
   //保留痕迹选项1
   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityOcMark.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //secOcMark = document.getElementById("paraName").value;
     if(document.getElementById("paraValue").value == "2"){
       document.getElementById("SEC_OC_MARK").value = "allow_save";
       document.getElementById("mark_radio_span").style.display = "";
     }else if(document.getElementById("paraValue").value == "1"){
       document.getElementById("mark_radio_span").style.display = "none";
       document.getElementById("SEC_OC_MARK").value = "must_save";
     }else if(document.getElementById("paraValue").value == "3"){
       document.getElementById("mark_radio_span").style.display = "none";
       document.getElementById("SEC_OC_MARK").value = "forbid_save";
     }
   }else {
     alert(rtJson.rtMsrg); 
   }
   
   //保留痕迹选项2
   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityMarkDefault.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //secOcMarkDefault = document.getElementById("paraName").value;
     if(document.getElementById("paraValue").value == "1"){
       document.getElementById("SEC_OC_MARK_DEFAULT1").checked = true;
     }else{
       document.getElementById("SEC_OC_MARK_DEFAULT2").checked = true;
     }
   }else {
     alert(rtJson.rtMsrg); 
   }

 //显示痕迹
   var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/getSecrityOcRevision.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //secOcRevision = document.getElementById("paraName").value;
     if(document.getElementById("paraValue").value == "1"){
       document.getElementById("SEC_OC_REVISION1").checked = true;
     }else{
       document.getElementById("SEC_OC_REVISION2").checked = true;
     }
   }else {
     alert(rtJson.rtMsrg); 
   }
   
}

//function play(){
 // document.getElementById("passTime").style.dispaly="";
//}
//function noplay(){
 // alert('none');
 // document.getElementById("passTime").style.dispaly="none";
//}

function check(){
  var reg = /^[0-9]*$/;
 // var secPassTime1 = document.getElementById("SEC_PASS_TIME");
 // if(document.getElementById("SEC_PASS_TIME1").value == "1"&&document.getElementById("SEC_PASS_TIME2").value != "0"){
  //  if (!reg.test(secPassTime1.value)) {
  	//	alert("错误 有效期应为整数！");
  	//	secPassTime1.focus();
  	//	return false;
   // }
 // }
  
  
  var secPassMin = document.getElementById("SEC_PASS_MIN"); 
  if (!secPassMin.value || !reg.test(secPassMin.value)) {
    alert("错误 密码长度应为整数！");
    secPassMin.select();
    secPassMin.focus();
    return false;
  }
  
  var secPassMax = document.getElementById("SEC_PASS_MAX"); 
  if (!secPassMax.value || !reg.test(secPassMax.value)) {
    alert("错误  密码长度应为整数！");
    secPassMax.focus();
    return false;
  }
//alert($("SEC_PASS_MIN").value);
  if(parseInt($("SEC_PASS_MIN").value) > parseInt($("SEC_PASS_MAX").value)){
    alert("错误 密码最大长度不能小于最小长度！");
    secPassMin.focus();
    secPassMin.select();
    return false;
  }
  
  var secRetryTimes = document.getElementById("SEC_RETRY_TIMES");
  var secBanTime = document.getElementById("SEC_BAN_TIME");
  if(document.getElementById("SEC_RETRY_BAN1").value == "1"){
    if (!secRetryTimes.value && !reg.test(secRetryTimes.value)) {
      alert("错误  重试次数应为整数！");
  	  secRetryTimes.focus();
  	  return false;
    }else if(!secBanTime.value && !reg.test(secBanTime.value)){
      alert("错误  分钟数应为整数！");
      secBanTime.focus();
  	  return false;
    }
  }
//if(document.getElementById("SEC_PASS_MIN").value > document.getElementById("SEC_PASS_MAX").value){
  //   alert("错误 密码最大长度不能小于最小长度！");
  //   document.getElementById("SEC_PASS_MIN").focus();
  //   return false;
//   }
  return true;
}


function sec_oc_mark() {
  var markObj = document.getElementById("SEC_OC_MARK");

  //如果选项不是“允许保留”则默认值选项隐藏
  if (markObj.value != "allow_save") {
     document.getElementById("mark_radio_span").style.display = "none";
  } else {
     document.getElementById("mark_radio_span").style.display = "";
  }
}

var secInitPass = "";
var secPassFlag = "";
var secPassTime = "";
var secPassMin = "";
var secPassMax = "";
var secPassSafe = "";
var secPassSafeSc = "";
var secRetryBan = "";
var secRetryTimes = "";
var secBanTime = "";
var loginKey = "";
var secUserMen = "";
var secKeyUser = "";
var loginSecureKey = "";
var secUseRtx = "";
var secShowIp = "";
var secOnStatus = "";
var secOcMark = "";
var secOcMarkDefault = "";
var secOcRevision = "";
function commit(){
  if(!check()){
    return;
  }
 var d = "";
  var secInitPassStr = document.getElementsByName("SEC_INIT_PASS");
  for(var i = 0; i < secInitPassStr.length; i++){
    if(secInitPassStr[i].checked){
      secInitPass = secInitPassStr[i].value;
    }
  } 
 
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateInitPass.act?secInitPass="+secInitPass;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  //alert(rtJson.rtMsrg);
  
  var secPassFlagStr = document.getElementsByName("SEC_PASS_FLAG");
  for(var i = 0; i < secPassFlagStr.length; i++){
    if(secPassFlagStr[i].checked)
      secPassFlag = secPassFlagStr[i].value;
  } 
  //alert(secPassFlag);
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updatePassFlag.act?secPassFlag="+secPassFlag;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));

  var reg = /^[0-9]*$/;
  var secPassTime1 = document.getElementById("SEC_PASS_TIME");
  if(secPassFlag == "1"){
    if (!secPassTime1.value || !reg.test(secPassTime1.value) || secPassTime1.value < 1) {
  		alert("错误 有效期应为大于1的整数！");
  		secPassTime1.focus();
  		secPassTime1.select();
  		return false;
    }
    var secPassTime = document.getElementById("SEC_PASS_TIME").value;
    var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updatePassTime.act?secPassTime="+secPassTime;
    var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  }

  var secPassMin = document.getElementById("SEC_PASS_MIN").value;
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updatePassMin.act?secPassMin="+secPassMin;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));

  var secPassMax = document.getElementById("SEC_PASS_MAX").value;
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updatePassMax.act?secPassMax="+secPassMax;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));

  
  var secPassSafeStr = document.getElementById("SEC_PASS_SAFE");
  if(secPassSafeStr.checked == true){
    secPassSafe = "1";
  }else{
    secPassSafe = "0";
  }
  
  if($("SEC_PASS_SAFE_SC").checked == true){
    secPassSafeSc = "1";
  }else{
    secPassSafeSc = "0";
  }
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updatePassSafe.act";
  var rtJson = getJsonRs(url, {secPassSafe: secPassSafe, secPassSafeSc: secPassSafeSc});

  var secRetryTimes = document.getElementById("SEC_RETRY_TIMES");
  var secBanTime = document.getElementById("SEC_BAN_TIME");
  var secRetryBanStr = document.getElementsByName("SEC_RETRY_BAN");
  for(var i = 0; i < secRetryBanStr.length; i++){
    if(secRetryBanStr[i].checked)
      secRetryBan = secRetryBanStr[i].value;
  } 
  var reg = /^[0-9]*$/;
  if(secRetryBan == "1"){
    if (!reg.test(secRetryTimes.value)) {
  		alert("错误  重试次数应为整数！");
  		secRetryTimes.focus();
  		return false;
    }
    if (secRetryTimes.value < 1) {
      alert("错误  重试次数应大于0！");
      secRetryTimes.focus();
      return false;
    }
    if(!reg.test(secBanTime.value)){
      alert("错误  分钟数应为整数！");
      secBanTime.focus();
  		return false;
    }
    if (secBanTime.value < 1) {
      alert("错误 分钟数应大于0！");
      secBanTime.focus();
      return false;
    }
    var secRetryTimes = document.getElementById("SEC_RETRY_TIMES").value;
    var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateRetryTimes.act?secRetryTimes="+secRetryTimes;
    var rtJson = getJsonRs(url, mergeQueryString($("form1")));

    var secBanTime = document.getElementById("SEC_BAN_TIME").value;
    var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateBanTime.act?secBanTime="+secBanTime;
    var rtJson = getJsonRs(url, mergeQueryString($("form1")));
    
  }
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateRetryBan.act?secRetryBan="+secRetryBan;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));

  var secRetryUserMenStr = document.getElementsByName("SEC_USER_MEM");
  for(var i = 0; i < secRetryUserMenStr.length; i++){
    if(secRetryUserMenStr[i].checked)
      secUserMen = secRetryUserMenStr[i].value;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateUserMen.act?secUserMen="+secUserMen;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));

  var secLoginKeyStr = document.getElementsByName("LOGIN_KEY");
  for(var i = 0; i < secRetryUserMenStr.length; i++){
    if(secLoginKeyStr[i].checked)
      secLoginKey = secLoginKeyStr[i].value;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateLoginKey.act?secLoginKey="+secLoginKey;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));

  var secKeyUserStr = document.getElementsByName("SEC_KEY_USER");
  for(var i = 0; i < secKeyUserStr.length; i++){
    if(secKeyUserStr[i].checked)
      secKeyUser = secKeyUserStr[i].value;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateKeyUser.act?secKeyUser="+secKeyUser;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));

  var loginSecureKeyStr = document.getElementsByName("LOGIN_SECURE_KEY");
  for(var i = 0; i < loginSecureKeyStr.length; i++){
    if(loginSecureKeyStr[i].checked)
      loginSecureKey = loginSecureKeyStr[i].value;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateSecureKey.act?loginSecureKey="+loginSecureKey;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));

  var secUseRtxStr = document.getElementsByName("SEC_USE_RTX");
  for(var i = 0; i < secUseRtxStr.length; i++){
    if(secUseRtxStr[i].checked)
      secUseRtx = secUseRtxStr[i].value;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateUseRtx.act?secUseRtx="+secUseRtx;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));

  var secShowIpStr = document.getElementsByName("SEC_SHOW_IP");
  for(var i = 0; i < secShowIpStr.length; i++){
    if(secShowIpStr[i].checked)
      secShowIp = secShowIpStr[i].value;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateShowIp.act?secShowIp="+secShowIp;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  
  //精灵模式
  var secOnStatusStr = document.getElementsByName("IM_MODULE");
  for(var i = 0; i < secOnStatusStr.length; i++){
    if(secOnStatusStr[i].checked)
      secOnStatus = secOnStatusStr[i].value;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateImModule.act?secOnStatus="+secOnStatus;

  var rtJson = getJsonRs(url, mergeQueryString($("form1")));

  var secOcMarkStr = document.getElementById("SEC_OC_MARK");
  var option = secOcMarkStr.getElementsByTagName("option");
  for(var i = 0; i < option.length; i++){
    if(option[i].selected){
      secOcMark = option[i].value;
    }
  }
  
  if(secOcMark == "allow_save"){
    secOcMark = "2";
    var secOcMarkDefaultStr = document.getElementsByName("SEC_OC_MARK_DEFAULT");
    for(var i = 0; i < secOcMarkDefaultStr.length; i++){
      if(secOcMarkDefaultStr[i].checked)
        secOcMarkDefault = secOcMarkDefaultStr[i].value;
    } 
    var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateOcMarkDefault.act?secOcMarkDefault="+secOcMarkDefault;
    var rtJson = getJsonRs(url, mergeQueryString($("form1")));

    var secOcMarkDefault1 = "0";
    if (document.getElementById("SEC_OC_MARK_DEFAULT1").checked) {
      secOcMarkDefault1 = "1";
    } 
    var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateSecOcMarkDefault.act?secOcMarkDefault="+secOcMarkDefault1;
    var rtJson = getJsonRs(url);
  }else if(secOcMark == "must_save"){
    secOcMark = "1";
  }
  else if(secOcMark == "forbid_save"){
    secOcMark = "3";
  }
 
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateOcMark.act?secOcMark="+secOcMark;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));

  var secOcRevisionStr = document.getElementsByName("SEC_OC_REVISION");
  for(var i = 0; i < secOcRevisionStr.length; i++){
    if(secOcRevisionStr[i].checked)
      secOcRevision = secOcRevisionStr[i].value;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/system/security/act/YHSecurityAct/updateOcRevision.act?secOcRevision="+secOcRevision;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  
  location = "<%=contextPath %>/core/funcs/system/security/submit.jsp";
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/sys.gif" align="absmiddle"><span class="big3">&nbsp;系统安全与设置</span>
    </td>
  </tr>
</table>
<br/>
<form name="form1" id="form1" method="post">
<input type="hidden" name="seqId" id="seqId" value="">
<input type="hidden" name="paraName" id="paraName" value="">
<input type="hidden" name="paraValue" id="paraValue" value="">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.diary.data.YHDiary.java"/>
<table class="TableList" width="100%">
<tr class="TableHeader" align="center">
    <td width="120">选项</td>
    <td>参数</td>
    <td width="250">备注</td>
  </tr>
  <tr class="TableData" align="center" height="30">
    <td width="120"><b>初始密码登录修改密码</b></td>
    <td align="left">
       <input type="radio" name="SEC_INIT_PASS" id="SEC_INIT_PASS1" value="1" ><label for="SEC_INIT_PASS1">是</label>
       <input type="radio" name="SEC_INIT_PASS" id="SEC_INIT_PASS2" value="0" ><label for="SEC_INIT_PASS2">否</label>
    </td>
    <td width="250" align="left">
       用户用初始密码登录需修改密码
    </td>
  </tr>
  <tr class="TableData" align="center" height="30">
    <td width="120"><b>密码定时过期</b></td>
    <td align="left">
       <input type="radio" name="SEC_PASS_FLAG" id="SEC_PASS_FLAG1" value="1" onclick="passTime.style.display=''"><label for="SEC_PASS_FLAG1">是</label>
       <input type="radio" name="SEC_PASS_FLAG" id="SEC_PASS_FLAG2" value="0" onclick="passTime.style.display='none'"><label for="SEC_PASS_FLAG2">否</label> &nbsp;
       <span id="passTime" style="display: 'none';">密码有效期：<Input type="text" name="SEC_PASS_TIME" id="SEC_PASS_TIME" class="SmallInput" value="" size="3" style="text-align:center;"> 天</span>
    </td>
    <td width="250" align="left">
       如果超过了密码的有效期，则在用户登录时将强制用户修改密码。

    </td>
  </tr>
  <tr class="TableData" align="center" height="30">
    <td width="120"><b>密码强度</b></td>
    <td align="left">
       密码长度：<Input type="text" name="SEC_PASS_MIN" id="SEC_PASS_MIN" class="SmallInput" value="" size="2" maxlength="2" style="text-align:center;"> — <Input type="text" name="SEC_PASS_MAX" id="SEC_PASS_MAX" class="SmallInput" value="" size="2" maxlength="2" style="text-align:center;"> 位

       <input type="checkbox" name="SEC_PASS_SAFE" id="SEC_PASS_SAFE"><label for="SEC_PASS_SAFE">密码必须同时包含字母和数字</label>
       <input type="checkbox" name="SEC_PASS_SAFE_SC" id="SEC_PASS_SAFE_SC"><label for="SEC_PASS_SAFE_SC">密码必须包含特殊字符</label>
    </td>
    <td width="250" align="left">
       设置密码强度，以保证密码的安全性。

    </td>
  </tr>
  <tr class="TableData" align="center" height="30">
    <td width="120"><b>登录错误次数限制</b></td>
    <td align="left">
       <input type="radio" name="SEC_RETRY_BAN" id="SEC_RETRY_BAN1" value="1" onclick="retryBan.style.display=''" ><label for="SEC_RETRY_BAN1">是</label>
       <input type="radio" name="SEC_RETRY_BAN" id="SEC_RETRY_BAN2" value="0" onclick="retryBan.style.display='none'" ><label for="SEC_RETRY_BAN2">否</label> &nbsp;
       <span id="retryBan" style="display: 'none';">
         登录错误重试 <Input type="text" name="SEC_RETRY_TIMES" id="SEC_RETRY_TIMES" class="SmallInput" value="" size="2" style="text-align:center;"> 次后
         <Input type="text" name="SEC_BAN_TIME" id="SEC_BAN_TIME" class="SmallInput" value="" size="2" style="text-align:center;"> 分钟内禁止再次登录</span>
    </td>
    <td width="250" align="left">
       如果选择“是”，则登录错误重试数次后会被限制数分钟内不能登录。

    </td>
  </tr>
  <tr class="TableData" align="center" height="30">
    <td width="120"><b>允许登录时记忆用户名</b></td>
    <td align="left">
       <input type="radio" name="SEC_USER_MEM" id="SEC_USER_MEM1" value="1" ><label for="SEC_USER_MEM1">是</label>
       <input type="radio" name="SEC_USER_MEM" id="SEC_USER_MEM2" value="0" ><label for="SEC_USER_MEM2">否</label>
    </td>
    <td width="250" align="left">
       登录界面记忆上次成功登录的用户名可以方便用户登录，但可能会带来安全隐患。

    </td>
  </tr>
  <tr class="TableData" align="center" height="30" style="display:none;">
    <td width="120"><b>是否启用USB用户KEY</b></td>
    <td align="left">
       <input type="radio" name="LOGIN_KEY" id="LOGIN_KEY1" value="1" ><label for="LOGIN_KEY1">是</label>
       <input type="radio" name="LOGIN_KEY" id="LOGIN_KEY2" value="0" ><label for="LOGIN_KEY2">否</label>
    </td>
    <td width="250" align="left">
      购买了USB Key的用户请选中此项<br>
      启用后，在登录界面所有用户会被提示安装USB Key控件
    </td>
  </tr>
  <tr class="TableData" align="center" height="30" style="display:none;">
    <td width="120"><b>使用用户KEY登录时是否需要输入用户名</b></td>
    <td align="left">
       <input type="radio" name="SEC_KEY_USER" id="SEC_KEY_USER1" value="1" ><label for="SEC_KEY_USER1">是</label>
       <input type="radio" name="SEC_KEY_USER" id="SEC_KEY_USER2" value="0" ><label for="SEC_KEY_USER2">否</label>
    </td>
    <td width="250" align="left">
       在使用用户KEY登录时,不用输入用户名直接输入密码进行登录

    </td>
  </tr>
  <tr class="TableData" align="center" height="30" style="display:none;">
    <td width="120"><b>是否启用动态密码卡</b></td>
    <td align="left">
       <input type="radio" name="LOGIN_SECURE_KEY" id="LOGIN_SECURE_KEY1" value="1" ><label for="LOGIN_SECURE_KEY1">是</label>
       <input type="radio" name="LOGIN_SECURE_KEY" id="LOGIN_SECURE_KEY2" value="0" ><label for="LOGIN_SECURE_KEY2">否</label>
    </td>
    <td width="250" align="left">
      购买了动态密码卡的用户请选中此项<br>
      启用后，在登录界面需要输入动态密码才可以登录
    </td>
  </tr>
  <tr class="TableData" align="center" height="30">
    <td width="120"><b>是否使用验证码</b></td>
    <td align="left">
       <input type="radio" name="VERIFICATION_CODE" id="VERIFICATION_CODE1" value="1" ><label for="VERIFICATION_CODE1">是</label>
       <input type="radio" name="VERIFICATION_CODE" id="VERIFICATION_CODE2" value="0" ><label for="VERIFICATION_CODE2">否</label>
    </td>
    <td width="250" align="left">
       在安装RTX客户端的情况下，选择启用RTX可以通过OA中跟RTX进行通信。

    </td>
  </tr>
  <!-- 
  <tr class="TableData" align="center" height="30">
    <td width="120"><b>启用RTX客户端</b></td>
    <td align="left">
       <input type="radio" name="SEC_USE_RTX" id="SEC_USE_RTX1" value="1" ><label for="SEC_USE_RTX1">是</label>
       <input type="radio" name="SEC_USE_RTX" id="SEC_USE_RTX2" value="0" ><label for="SEC_USE_RTX2">否</label>
    </td>
    <td width="250" align="left">
       在安装RTX客户端的情况下，选择启用RTX可以通过OA中跟RTX进行通信。

    </td>
  </tr>
   -->
  <tr class="TableData" align="center" height="30">
    <td width="120"><b>显示用户登录IP</b></td>
    <td align="left">
       <input type="radio" name="SEC_SHOW_IP" id="SEC_SHOW_IP0" value="0" ><label for="SEC_SHOW_IP0">不显示</label>
       <input type="radio" name="SEC_SHOW_IP" id="SEC_SHOW_IP1" value="1" ><label for="SEC_SHOW_IP1">仅管理员可见</label>
       <input type="radio" name="SEC_SHOW_IP" id="SEC_SHOW_IP2" value="2" ><label for="SEC_SHOW_IP2">所有用户可见</label>
    </td>
    <td width="250" align="left">
       全部人员列表和用户资料是否显示用户最近一次登录的IP。如显示IP，用户多时可能会导致用户列表加载缓慢。

    </td>
  </tr>
  <tr class="TableData" align="center" height="30">
    <td width="120"><b>记忆在线状态</b></td>
    <td align="left">
       <input type="radio" name="SEC_ON_STATUS" id="SEC_ON_STATUS1" value="1" ><label for="SEC_ON_STATUS1">是</label>
       <input type="radio" name="SEC_ON_STATUS" id="SEC_ON_STATUS2" value="0" ><label for="SEC_ON_STATUS2">否</label>
    </td>
    <td width="250" align="left">
       用户重新登录后是否记忆上次设置的在线状态(如 忙碌、离开等)。

    </td>
  </tr>
  <tr class="TableData" align="center" height="30">
    <td width="120"><b>保留痕迹选项</b></td>
    <td align="left">
       <select name="SEC_OC_MARK" id="SEC_OC_MARK" onchange="sec_oc_mark();">
         <option value="allow_save" >允许保留 </option>
         <option value="must_save" >强制保留 </option>
         <option value="forbid_save" >强制不保留 </option>
       </select>
       <span id="mark_radio_span">
          <input type="radio" name="SEC_OC_MARK_DEFAULT" id="SEC_OC_MARK_DEFAULT1" value="1" ><label for="SEC_OC_MARK_DEFAULT1">默认保留 </label>
          <input type="radio" name="SEC_OC_MARK_DEFAULT" id="SEC_OC_MARK_DEFAULT2" value="0" ><label for="SEC_OC_MARK_DEFAULT2">默认不保留 </label>
       </span>
    </td>
    <td width="250" align="left">
       Word文档在线编辑保留痕迹相关选项。

    </td>
  </tr>
  <tr class="TableData" align="center" height="30">
    <td width="120"><b>显示痕迹</b></td>
    <td align="left">
       <input type="radio" name="SEC_OC_REVISION" id="SEC_OC_REVISION1" value="1" ><label for="SEC_OC_REVISION1">是 </label>
       <input type="radio" name="SEC_OC_REVISION" id="SEC_OC_REVISION2" value="0" ><label for="SEC_OC_REVISION2">否 </label>
    </td>
    <td width="250" align="left">
       Word文档在线阅读是否显示痕迹。

    </td>
  </tr>
  <tr class="TableData" align="center" height="30" style="display:none;">
    <td width="120"><b>IM浏览模式</b></td>
    <td align="left">
       <input type="radio" name=IM_MODULE id="IM_MODULE1" value="IM" ><label for="SEC_OC_REVISION1">IM模式 </label>
       <input type="radio" name="IM_MODULE" id="IM_MODULE2" value="IE" ><label for="SEC_OC_REVISION2">浏览器模式</label>
    </td>
    <td width="250" align="left">
                 在IM使用中直接打开没有IM权限时可在IE中打开，设置浏览器模式就在IE中打开。

    </td>
  </tr>
  
  <tr class="TableControl" align="center">
    <td colspan="3">
       <Input type="button" name="submit" class="BigButton" value="保存" onclick="commit()">
    </td>
  </tr>
</form>
</body>
</html>