<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=StaticData.SOFTTITLE%></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<meta name="author" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="keywords" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="description" content="<%=StaticData.SOFTKEYWORD%>" />
<style type="text/css">
*{padding:0px;margin:0px;}
body{background:#fff url(<%=imgPath %>/frame/index_bg.jpg) no-repeat center top;}
#layout{height:350px;width:510px;position:absolute;top:50%;left:50%;margin-top:-175px;margin-left:-255px;background:url('<%=imgPath %>/frame/login_area_shadow.png') no-repeat top left;}
#login_area_pic{width:486px;height:277px;margin-top:10px;margin-left:10px;}
#login_area{height:30px;margin-top:20px;}
input{height:20px;line-height:20px;padding:0;margin:0;width:120px;vertical-align:middle;}
#login_area span{font-size:13px;color:#0a578c;font-weight:bold;margin-left:12px;}
input.Log_input{border:1px solid #7fb5da;padding:2px 0px 0px 3px;*padding:1px 3px;background:#fff url(<%=imgPath %>/frame/textinputbg.gif) repeat-x;}
input.Log_submit{width:65px;height:22px;padding:0px;margin:0px;border:none;background:#fff url(<%=imgPath %>/frame/login_bt_login.png) no-repeat 0 0;margin-left:10px;color:#333333;font-weight:bold;cursor:pointer;}
input.Log_submit:hover{background-position:0 -38px;}
</style>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
function doLogin(){
  if(!$('userName').present()){
	alert("请输入用户名");
	$('userName').focus();
	return ;
  }
  var pars = $('loginForm').serialize() ;
  var url = contextPath + "/yh/core/funcs/system/act/YHSystemAct/doLoginIn.act";
  $('layout').hide();
  $('displayDiv').show();
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    window.location = contextPath + "/core/funcs/display/index.jsp";
  }else{
	alert(json.rtMsrg);	
	$('displayDiv').hide();
	$('layout').show();
	$('loginForm').reset()
	$('userName').focus();
  }
}
</script>
</head>
<body onload="$('pwd').focus();">

<div id="layout">
	<div id="login_area_pic"><img src="<%=imgPath %>/frame/login_area.jpg" alt="<%=StaticData.SOFTTITLE%>" title="<%=StaticData.SOFTTITLE%>"/></div>
	<form method=post action="/yh/core/funcs/display/index.jsp" id='loginForm'>
	<div id="login_area">
		<span>用户名：<input class="Log_input"  type="text" name="userName"  value="liuhan" id="userName" tabindex="1"/></span>
		<span>密 码：<input class="Log_input"  type="password" name="pwd" id="pwd" value="liuhan" tabindex="2"/></span>
		<span><input class="Log_submit" type="button"  name="button" value="登录"  onclick="doLogin();"/></span>
	</div>
	</form>
  </div>
<div id="displayDiv" align="center" style="height:800px;display:none">
<div><img src="img/loading.gif"/></div>
<div>正在进入。。。请稍后</div>
</div>
</body>
</html>
