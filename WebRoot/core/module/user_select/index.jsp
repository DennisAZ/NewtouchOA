<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String TO_ID = request.getParameter("TO_ID");
  String TO_NAME = request.getParameter("TO_NAME");
  String MODULE_ID = request.getParameter("MODULE_ID");
  String USER_SEQ_ID = request.getParameter("USER_SEQ_ID");
  String USER_DEPT = request.getParameter("USER_DEPT");
  String USER_PRIV = request.getParameter("USER_PRIV");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>选择人员</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
</head>
<frameset rows="*,30"  rows="*" frameborder="no" border="1" framespacing="0" id="frame1" onload="">
   <frameset cols="200,*"  rows="*" frameborder="yes" border="1" framespacing="0" id="frame2">
      <frame name="dept" src="dept.jsp?MODULE_ID=<%=MODULE_ID%>&TO_ID=<%=TO_ID%>&TO_NAME=<%=TO_NAME%>&USER_SEQ_ID=<%=USER_SEQ_ID%>&USER_DEPT=<%=USER_DEPT%>&USER_PRIV=<%=USER_PRIV%>">
      <frame name="user" src="selected.jsp">
   </frameset>
   <frame name="control" scrolling="no" src="control.jsp">
</frameset>
</html>