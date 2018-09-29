<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="utility.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>空白页</title>
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<br>
<br>
<br>
<%
	Msg msg = new Msg(out);
	msg.message("", "请选择文件夹进行浏览");
%>
</body>
</html>