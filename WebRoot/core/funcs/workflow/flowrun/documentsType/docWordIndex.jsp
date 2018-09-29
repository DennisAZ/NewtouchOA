<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<%
String flag = request.getParameter("flag");
%>
<title>选择文件字</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<frameset cols="*"  rows="40,*" frameborder="yes" border="1" framespacing="0" id="bottom">
  <frame name="query" src="docWordQuery.jsp?flag=<%=flag%>" scrolling="no" frameborder="yes">
  <frame id="bookinfo" name="plannoinfo" src="docWordInfo.jsp?flag=<%=flag%>" frameborder="no">
</frameset>
</html>
