<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  YHPerson person = (YHPerson)session.getAttribute("LONGIN_USER"); 
  if(person == null){
    response.sendRedirect("login.jsp");
    return  ;
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><%=StaticData.SOFTTITLE%></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<meta name="author" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="keywords" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="description" content="<%=StaticData.SOFTKEYWORD%>" />
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />

<style type="text/css">
*{padding:0px;margin:0px;}
body{font-size:12px;}
#header{height:80px;width:100%;}
#header #bannerArea{width:100%;height:55px;background:#fff url(<%=imgPath %>/frame/header_logo_center.jpg) repeat-x;}
#header #bannerArea #logo{float:left;width:560px;height:55px;background:#fff url(<%=imgPath %>/frame/header_logo_left.jpg) no-repeat left top;}
#header #bannerArea #panel{width:300px;height:55px;line-height:50px;float:right;background:url(<%=imgPath %>/img/header_logo_line.jpg) no-repeat left center;padding-left:20px;}
#panel span{margin-right:10px;}
span.userArea{background:url(<%=imgPath %>/frame/user.png) no-repeat left center;font-weight:bold;color:#FFFFFF;padding-left:20px;height:30px;line-height:30px;}
input.commButton{height:33px;width:72px;background:url(<%=imgPath %>/frame/header_login_nodot.jpg) no-repeat center center;border:0px;font-weight:bold;color:#FFFFFF;line-height:33px;vertical-align:middle;cursor:pointer;}

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
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="index.js"></script>
</head>
<body onload="doInit()">
<div id="header">
	<!-- LOGO AND USER PANEL -->
	<div id="bannerArea">
		<div id="logo"></div>
		<div id="panel">
			<span class="userArea"><%=person.getUserName() %></span>
			<span><input  class="commButton" onclick="doLoginOut()" type="button" name="logout" value="注销" /></span>
			<span><input  class="commButton" type="button" name="btnDesk" name="btnDesk" value="桌面" /></span>
		</div>
	</div>
   <!-- Nav -->
	<div id="nav">
		<div id="navTrees">
		</div>
		<div id="nav_fetch"><img src="<%=imgPath %>/frame/nav_fetch.gif" onclick="DisplayNav();"/></div>
	</div>
</div>
<iframe id="mainFrame" width=0 height=0 name="mainFrame">
</iframe>
</body>
</html>