<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>通达YH管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<meta name="author" content="通达YH管理平台,通达,YH,进销存" />
<meta name="keywords" content="通达YH管理平台,通达,YH,进销存" />
<meta name="description" content="通达YH管理平台,通达,YH,进销存" />
<style type="text/css">
*{padding:0px;margin:0px;}
body{background:#fff url(./img/index_bg.jpg) no-repeat center top;}
#layout{height:350px;width:510px;position:absolute;top:50%;left:50%;margin-top:-175px;margin-left:-255px;background:url('./img/login_area_shadow.png') no-repeat top left;}
#login_area_pic{width:486px;height:277px;margin-top:10px;margin-left:10px;}
#login_area{height:30px;margin-top:20px;}
input{height:20px;line-height:20px;padding:0;margin:0;width:120px;vertical-align:middle;}
#login_area span{font-size:13px;color:#0a578c;font-weight:bold;margin-left:12px;}
input.Log_input{border:1px solid #7fb5da;padding:2px 0px 0px 3px;*padding:1px 3px;background:#fff url(./img/textinputbg.gif) repeat-x;}
input.Log_submit{width:65px;height:22px;padding:0px;margin:0px;border:none;background:#fff url(./img/login_bt_login.png) no-repeat 0 0;margin-left:10px;color:#333333;font-weight:bold;cursor:pointer;}
input.Log_submit:hover{background-position:0 -38px;}
</style>
</head>
<body>
  
  <div id="layout">
	<div id="login_area_pic"><img src="./img/login_area.jpg" alt="通达YH管理平台" title="通达YH管理平台"/></div>
	<form method=post action="index.jsp">
	<div id="login_area">
		<span>用户名：<input class="Log_input" type="text" name="username" value="" tabindex="1"/></span>
		<span>密 码：<input class="Log_input" type="password" name="password" value="" tabindex="2"/></span>
		<span><input class="Log_submit" type="submit" name="submit" value="登录" /></span>
	</div>
	</form>
  </div>
 </body>
</html>