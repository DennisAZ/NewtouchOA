<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@ page import="yh.core.funcs.system.act.YHSystemAct" %>
<%
Integer randomInt = (Integer)request.getSession().getAttribute("RANDOM_NUMBER");

int randomNum = 123456;
if (randomInt != null) {
  randomNum = randomInt;
}
%>
<html>
<head>
<title><%=StaticData.SOFTCOMPANY_SHORTNAME%>数据交换平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="author" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="keywords" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="description" content="<%=StaticData.SOFTKEYWORD%>" />
<link rel="stylesheet" href = "<%=contextPath%>/core/templates/default/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/tdPass.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/rsa.js"></script>
<style>
.login-bg {
  background-image: url(<%=contextPath%>/core/esb/server/style/images/login_bg.jpg);
}
</style>
<script type="text/javascript">
var randomNum = "<%=randomNum%>";
function doLogin(){
  if(!$('userName').present()){
    alert("请输入用户名");
    $('userName').focus();
    return ;
  }
  loginNamePass();
}

/**
 * 用户名密码登录
 */
function loginNamePass() {

  var pars = $('loginForm').serialize() ;
  var url = contextPath + "/yh/core/funcs/system/act/YHSystemAct/doLoginIn.act";
  $('layout').hide();
  
  try {
	  var json = getJsonRs(url,pars);
	  loginComplete(json);
  } catch (e){
    alert('服务器连接中断');
  }
}

function loginComplete(json) {
  if (json.rtState == "0"){
    //记录上次成功登陆的用户名
    if (json.rtData.saveUserName == "1") {
      setCookie('userName',$('userName').value);
    }else {
      setCookie('userName','');
    }
    
    var url = contextPath + "/core/esb/server/index.jsp";
    //alert(url)
    switch (json.rtData.menuType) {
    case '1':
      window.location.href = url;
      break;
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
  if (getCookie('userName')){
	  $('userName').value = getCookie('userName');
    $('pwd').focus();
  }
  else{
    $('userName').focus();
  }
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
</script>
</head>
<body onload="doInit()">
<div id="layout" align="center">
	<form method="post" id='loginForm'>
	  <div class="login-bg" id="loginBg">
	    <div class="login-username">
	      <div class="login-input-bg">
	        <input type="text" onfocus="doFocus(this)" name="userName" id="userName" tabindex="1">
	      </div>
	    </div>
	    <div class="login-password">
	      <div class="login-input-bg">
	        <input type="password" onfocus="doFocus(this)" name="pwd" id="pwd" value="" tabindex="2">
	      </div>
	    </div>
	    <div class="login-btn" id="btnLogin" onclick="doLogin();"></div>
	  </div>
	</form>
</div>
</body>
</html>
