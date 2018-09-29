<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String TO_ID = request.getParameter("TO_ID");
  String TO_NAME = request.getParameter("TO_NAME");
  String MODULE_ID = request.getParameter("MODULE_ID");
  String USER_SEQ_ID = request.getParameter("USER_SEQ_ID");
  String USER_DEPT = request.getParameter("USER_DEPT");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择<%out.println("管理范围内的");%>部门</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
</head>
<frameset rows="*,30"  rows="*" frameborder="NO" border="1" framespacing="0" id="bottom">
  <frameset cols="180,*"  rows="*" frameborder="YES" border="1" framespacing="0" id="bottom">
     <frame name="dept" src="dept.jsp?TO_ID=<%=TO_ID%>&TO_NAME=<%=TO_NAME%>&MODULE_ID=<%=MODULE_ID%>&USER_SEQ_ID=<%=USER_SEQ_ID%>&USER_DEPT=<%=USER_DEPT%>">
     </frame>
     <frame name="dept_list" src="dept_list.jsp?TO_ID=<%=TO_ID%>&TO_NAME=<%=TO_NAME%>">
  </frameset>
  <frame name="control" scrolling="no" src="control.jsp">
</frameset>