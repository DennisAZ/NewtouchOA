<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
  String module = request.getParameter("module") == null ? "" :  request.getParameter("module");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人事分析</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>    

</head>
<frameset rows="130,*"  frameborder="NO" border="0" framespacing="0" id="frame1">
    <frame name="select" id="select" scrolling="auto" src="select.jsp?module=<%=module%>"  frameborder="no">
    <frame name="tumain" id="tumain" scrolling="auto" src="analysis.jsp?module=<%=module%>" frameborder="no">
</frameset>