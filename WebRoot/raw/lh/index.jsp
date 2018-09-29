<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String baseUrl = "http://localhost:8080";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>通达YH管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<meta name="author" content="通达YH管理平台,通达,YH,进销存" />
<meta name="keywords" content="通达YH管理平台,通达,YH,进销存" />
<meta name="description" content="通达YH管理平台,通达,YH,进销存" />
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<style type="text/css">
*{padding:0px;margin:0px;}
body{font-size:12px;}
#header{height:80px;width:100%;}
#header #bannerArea{width:100%;height:55px;background:#fff url(./img/header_logo_center.jpg) repeat-x;}
#header #bannerArea #logo{float:left;width:560px;height:55px;background:#fff url(./img/header_logo_left.jpg) no-repeat left top;}
#header #bannerArea #panel{width:300px;height:55px;line-height:50px;float:right;background:url(./img/header_logo_line.jpg) no-repeat left center;padding-left:20px;}
#panel span{margin-right:10px;}
span.userArea{background:url(./img/user.png) no-repeat left center;font-weight:bold;color:#FFFFFF;padding-left:20px;height:30px;line-height:30px;}
input.commButton{height:33px;width:72px;background:url(./img/header_login_nodot.jpg) no-repeat center center;border:0px;font-weight:bold;color:#FFFFFF;line-height:33px;vertical-align:middle;cursor:pointer;}

#nav{background-color:#FBFCEC;height:25px;width:100%;border-bottom:1px solid #AFB293;position:relative;}
#nav ul{list-style-type:none;}
#nav #navTrees{width:850px;float:left;}
#nav ul li{height:25px;line-height:25px;list-style-type:none;display:block;float:left;width:auto;padding:0px 4px;margin-left:14px;font-weight:bold;font-size:13px;color:#666666;}
#nav ul li a:link{color:#666666;text-decoration:none;}
#nav ul li a:hover{color:#666666;text-decoration:underline;}
#nav_fetch{width:12px;height:12px;top:5px;right:13px;position:absolute;cursor:pointer;}
</style>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="js/index.js"></script>
<script type="text/javascript">
var local = 1; 
var length = 10;
var baseUrl = "<%=baseUrl%>";
var D = Ext.lib.Dom;
function NavDisplayInit(){ 
  
  
  doInit();
  var tagDom = document.getElementById('navTrees'); 
  var arrNavs = tagDom.getElementsByTagName('li'); 
  if(arrNavs.length > length){ 
  document.getElementById('nav_fetch').style.display = ''; 
  for(i=0;i<length;i++){ 
  arrNavs[i].style.display = ''; 
  } 
  }else{ 
  for(i=0;i<arrNavs.length;i++){ 
  arrNavs[i].style.display = ''; 
  } 
  } 
  } 

  //更换NAV导航菜单 
  function DisplayNav(){ 
  var tagDom = document.getElementById('navTrees'); 
  var arrNavs = tagDom.getElementsByTagName('li'); 
  //初始化 
  //if(!Get()){Set('1');} 
  local = local >= Math.ceil((arrNavs.length)/length) ? 1 : local+1; 
  var nextOrder = local; 
  var nextOrderStart = (parseInt(local)-1)*length; 
  var nextOrderEnd = parseInt(local)*length-1 > arrNavs.length ? arrNavs.length : parseInt(local)*length-1; 
  for(i=0;i<arrNavs.length;i++){ 
  if(nextOrderStart <= i && nextOrderEnd >= i){ 
  arrNavs[i].style.display = ''; 
  }else if(nextOrderStart > i || i>nextOrderEnd){ 
  arrNavs[i].style.display = 'none'; 
  } 
  } 
  }

</script>
</head>
<body onload="NavDisplayInit();">
<div id="header">
	<!-- LOGO AND USER PANEL -->
	<div id="bannerArea">
		<div id="logo"></div>
		<div id="panel">
			<span class="userArea">系统管理员</span>
			<span><input class="commButton" type="button" name="logout" value="注销" /></span>
			<span><input class="commButton" type="button" name="logout" value="桌面" /></span>
		</div>
	</div>
   <!-- Nav -->
	<div id="nav">
		<div id="navTrees">
		</div>
		<div id="nav_fetch"><img src="./img/nav_fetch.gif" onclick="DisplayNav();"/></div>
	</div>
</div>
<iframe id="contentFrame" width=100% height=1024 name="contentFrame">
</iframe>

<div style="display:none"><iframe width="0" height="0" id="singleSignFrame" src="<%=baseUrl%>/logincheck.php?USERNAME=admin&PASSWORD=">
</iframe></div>
 </body>
</html>