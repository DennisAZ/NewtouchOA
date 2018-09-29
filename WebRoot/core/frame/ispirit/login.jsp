<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@ page import="yh.core.funcs.system.act.YHSystemAct" %>
<%
String useUsbKeyStr = (String)request.getAttribute("useUsbKey");
String secKeyUserStr = (String)request.getAttribute("secKeyUser");
String ieTitle = (String)request.getAttribute("ieTitle");
String useYHErpStr = YHSysProps.getString("useYHErp");

if (YHUtility.isNullorEmpty(ieTitle)) {
  ieTitle = StaticData.SOFTTITLE;
}

if (YHUtility.isNullorEmpty(useUsbKeyStr) || YHUtility.isNullorEmpty(secKeyUserStr)) {
  request.getRequestDispatcher("/yh/core/funcs/system/act/YHSystemAct/prepareLoginInIM.act").forward(request, response);
  return;
}

boolean useUsbKey = "1".equals(useUsbKeyStr);
boolean secKeyUser = "1".equals(secKeyUserStr);
boolean useYHErp = "1".equals(useYHErpStr);

Integer randomInt = (Integer)request.getSession().getAttribute("RANDOM_NUMBER");

int randomNum = 123456;
if (randomInt != null) {
  randomNum = randomInt;
}
String template = YHSystemAct.queryTemplate(request);
template = contextPath + "/core/templates/" + template;
%>
<html>
<head>
<title><%=ieTitle %></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="author" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="keywords" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="description" content="<%=StaticData.SOFTKEYWORD%>" />
<link rel="stylesheet" href = "style/login.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/tdPass.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/rsa.js"></script>
<script type="text/javascript">
var useUsbKey = <%=useUsbKey%>;
var secKeyUser = <%=secKeyUser%>;
var useYHErp = <%=useYHErp%>;
var randomNum = "<%=randomNum%>";
function doLogin(){
  if (useUsbKey) {
     if(!CheckForm()){
       return;
     }
     if (!$("KEY_SN").value || !$("KEY_DIGEST").value || !$("KEY_USER").value) {
       if(!$('userName').present()){
         alert("请输入用户名");
         $('userName').focus();
         return;
       }
     }
     if (secKeyUser) {
       if(!$('userName').present()){
         alert("请输入用户名");
         $('userName').focus();
         return;
       }
     }
     loginUsbKey();
  }else {
    if(!$('userName').present()){
      alert("请输入用户名");
      $('userName').focus();
      return ;
    }
    loginNamePass();
  }
}

function CheckForm(){
  $("KEY_SN").value = "";
  $("KEY_DIGEST").value = "";
  $("KEY_USER").value = ""
  try{
		var theDevice = document.getElementById("tdPass");
		var KeySN = READ_SN(theDevice);
		if (KeySN < 0) {
		  return true;
		}
		var Digest = COMPUTE_DIGEST(theDevice, randomNum);
		if (Digest < 0) {
		  return true;
		}
		var Key_UserID = READ_KEYUSER(theDevice);
		if (Key_UserID < 0) {
		  return true;
		}
		$("KEY_SN").value = KeySN;
		$("KEY_DIGEST").value = Digest;
		$("KEY_USER").value = Key_UserID
		//var rsa = new RSAKey();
		//rsa.setPublic("aebc3bedeeff7587f512736b8ffba63ac033c1bbf1a51a821af920a49bc7786f", "10001");
		 //$("pwd").value = rsa.encrypt($("pwd").value);
  } catch(ex){
    return false;
  }
  return true;
}

<%
if (useUsbKey) {
%>
/**
 * 用户名密码登录
 */
function loginUsbKey() {
  var pars = $('loginForm').serialize() ;
  var url = contextPath + "/yh/core/funcs/system/act/YHSystemAct/doLoginIn.act";
  $('layout').hide();
  //$('displayDiv').show();
  var json = getJsonRs(url,pars);
  loginComplete(json);
}

function showTdPassObject(){
  //document.getElementById("tdPassObject").innerHTML='<object id="tdPass" name="tdPass" CLASSID="clsid:0272DA76-96FB-449E-8298-178876E0EA89" CODEBASE="/yh/core/cntrls/tdPass.cab#Version=1,00,0000" BORDER="0" VSPACE="0" HSPACE="0" ALIGN="TOP" HEIGHT="0" WIDTH="0"></object>';
  //document.getElementById("installTdPass").style.display="none";
}
<%
}
%>

/**
 * 用户名密码登录
 */
function loginNamePass() {

  var pars = $('loginForm').serialize() ;
  var url = contextPath + "/yh/core/funcs/system/act/YHSystemAct/doLoginIn.act";
  $('layout').hide();
  //$('displayDiv').show();
  
  try {
	  var json = getJsonRs(url,pars);
	  loginComplete(json);
  } catch (e){
    alert('服务器连接中断');
  }
}

function loginComplete(json) {
  if (json.rtState == "0"){
	  //设置精灵字符集
	   if(typeof(window.external.OA_SMS) != 'undefined') {
		 //  alert("1");
      window.external.OA_SMS("IS_UN","1","INIT");  //0 GBK,1 UTF-8
    }
	
	  
    if (useYHErp) {
      yherpSso(json.rtData);
    }
    //记录上次成功登陆的用户名
    if (json.rtData.saveUserName == "1") {
      setCookie('userName',$('userName').value);
    }else {
      setCookie('userName','');
    }

    //im的处理

    
    //wcscmp(param3,_T("NAME"));
     
    var url = contextPath + "/core/frame/ispirit/index.jsp";
    switch (json.rtData.menuType) {
    case '1':
      window.location.href = url;
      break;
      /*
    case '2':
      window.open(url, "_blank","top=0,left=0,toolbar=yes," +
           "location=yes, directories=no, status=no, scrollbars=yes," +
           "resizable=yes, copyhistory=no, width=" + window.screen.width + "," + 
           "height=" + window.screen.height);
       window.opener=null;
       window.open("","_self");
       window.close();
      return;
    case '3': 
      window.open(url, "_blank","top=0,left=0,toolbar=no," +
           "location=no, directories=no, status=no, menubar=no, scrollbars=yes," +
           "resizable=yes, copyhistory=no, width=" + window.screen.width + "," +
           "height=" + window.screen.height);
      window.opener=null;
      window.open("", "_self");
      window.close();
      return;
      */
    default:
      window.location.href = url;
    }
  } else{
    switch(json.rtData.code){
      case 0:{
        alert(json.rtMsrg);
        $('loginForm').reset();
        $('layout').show();
        $('userName').focus();
        break;
      }
      case 1:{
      }
      case 2:{
      }
      case 3:{
        alert(json.rtMsrg);
        $('pwd').value = '';
        $('layout').show();
        $('userName').focus();
        break;
      }
      case 4:{
        alert(json.rtMsrg);
        $('layout').show();
        break;
      }
      case 5:{
        alert(json.rtMsrg);
        $('pwd').value = '';
        $('layout').show();
        $('pwd').focus();
        break;
      }
      case 6:{
        window.location = contextPath + "/core/frame/pass.jsp";
        break;
      }
      case 7:{
        window.location = contextPath + "/core/frame/pass.jsp";
        break;
      }
      case 8:{
        alert('用户名或密码错误超过 ' + json.rtData.msg.times + ' 次，请等待' + json.rtData.msg.minutes + '分钟后重试!');
        $('pwd').value = '';
        $('layout').show();
        $('pwd').focus();
        break;
      }
      case 9:{
        alert(json.rtMsrg);
        $('pwd').value = '';
        $('layout').show();
        $('userName').focus();
        break;
      }
      default:{
        alert("登录失败!");
        $('layout').show();
      }
    }
  }
}
/**
 * 处理用户获取
 */
function doFocus(obj) {
  obj.select();
}

var KEY_ENTER = 0X000D;

/**
 * 处理键盘按键press事件
 */
function documentKeypress(e){
  var id = document.activeElement.id;
  if (id != 'userName' && id != 'pwd'){
    return;
  }
  
  var currKey = 0;
  var e = e || event;
  currKey = e.keyCode || e.which || e.charCode;

  if (currKey == KEY_ENTER){
    doLogin();
  }
}

document.onkeypress = documentKeypress;

function doInit(){
}


/**
 * 设置cookie
 */
function setCookie(name,value){
  var Days = 30;
  var exp  = new Date();
  exp.setTime(exp.getTime() + Days*24*60*60*1000);
  document.cookie = name + "="+ escape (value) + ";path=/;expires=" + exp.toGMTString();
}

/**
 * 读取cookie
 */
function getCookie(name){
  
  var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
  if (arr != null){
    return unescape(arr[2]);
  }
  else{
    return null;
  }
  return null;
}

/**
 * 进销存单点登录

 */
function yherpSso(userinfo) {
  var srcs = [
              "/yherp/login.do?userName=",
              userinfo.userName,
              "&action_flag=login_check_depend_on_oa_ea&seqId=",
              userinfo.seqId,
              "&deptId=",
              userinfo.deptId];
  var url = srcs.join("");
  try {
    var json = getJsonRsAsyn(url);
  } catch (e){
    
  }
}

/**
 * im使用的登陆函数
 */
function AutoLogin(userName, pwd) {
  $('userName').value = userName || '';
  $('pwd').value = pwd || '';
  
  //添加动态密码卡
  if(true){
	  $("tb_tips").style.dispaly="block";
	//  window.external.OA_SMS("","","SECURE_PASS");
  }
  
  doLogin();
}
</script>
</head>
<body class="bodycolor" onload="doInit();" scroll="no" topmargin="5">
<div id="layout" align="center">
	<form name="loginForm" id="loginForm" method="post">
		<table class="TableBlock" width="90%" align="center">
		    <tr class="TableHeader">
		      <td align="center">登录OA精灵</td>
		    </tr>
		    <tr class="TableControl">
		      <td class="small">
		
		        <img src="style/images/login_user.gif" align="absmiddle"><b>用户名:</b>
		        <input type="text" class="SmallInput" name="userName" id="userName" size="10" value=""><br>
		        <img src="style/images/login_pass.gif" align="absmiddle"><b>密　码:</b>
		        <input type="password" class="SmallInput" name="pwd" id="pwd" size="10" value="">
		      </td>
		    </tr>
		     <tr class="TableControl">
		      <td align="center">
		
		        <input type="hidden" name="I_VER" value="">
		        <input type="button" name="Submit" class="BigButton" onclick="doLogin();" value="登 录">
		      </td>
		    </tr>
		</table>
<table id="tb_tips" class="MessageBox" align="center" width="180" style="display:none;">
  <tr>
    <td class="msg" style="padding-left:20px;text-align:center;">
      <div class="content" style="font-size:12pt">请输入动态密码卡</div>
    </td>
  </tr>
</table>
	</form>
</div>
	  <%
	  if (useUsbKey) {
	  %>
	    <div id="tdPassObject" style="display:none;"><object id="tdPass" name="tdPass" CLASSID="clsid:0272DA76-96FB-449E-8298-178876E0EA89" CODEBASE="/yh/core/cntrls/tdPass.cab#Version=1,00,0000" BORDER="0" VSPACE="0" HSPACE="0" ALIGN="TOP" HEIGHT="0" WIDTH="0"></object></div>
	    <input type="hidden" name="KEY_SN" id="KEY_SN" value="">
	    <input type="hidden" name="KEY_USER" id="KEY_USER" value="">
	    <input type="hidden" name="KEY_DIGEST" id="KEY_DIGEST" value="">
	  <%
	  }
	  %>
</body>
</html>
