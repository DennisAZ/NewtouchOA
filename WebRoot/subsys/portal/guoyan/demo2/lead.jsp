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
<title>领导资料</title>
</head>
<frameset  cols="200,*" frameborder="no" border="0" framespacing="0" id="frame1">
    <frame name="leadMenu" scrolling="no" src="lead-a.jsp?userName=<%=userName %>&type=<%=type %>" frameborder="0">
    <frame name="leadData" scrolling="no" src="modules/ldzl/lead-<%=type %>.jsp?userName=<%=userName %>" frameborder="0">
</frameset>
</html>