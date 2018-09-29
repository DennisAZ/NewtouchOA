<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
  String type = request.getParameter("type");
  String userName = request.getParameter("userName");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>国务院发展研究中心专栏导航</title>
<link href="style/css/css.css" rel="stylesheet" type="text/css" />
<link href="style/css/css-other.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
</head>

<body>
<div class="frame-box-two">
<div class="s-left">
   <div class="s-menu">
      <div class="menutit-bg">专栏导航</div>
	  <div class="menu-button1"><a href="modules/ldzl/lead-<%=type %>.jsp" target="leadData">个人简历</a></div>
	  <div class="menu-button1"><a href="modules/leader/leader-a.jsp?userName=<%=userName %>" target="leadData">重要活动</a></div>
	  <div class="menu-button1"><a href="modules/leader/leader-b.jsp?userName=<%=userName %>" target="leadData">重要讲话</a></div>
   </div>
</div>
</body>
</html>
