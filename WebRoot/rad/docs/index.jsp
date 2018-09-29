<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String catagory = request.getParameter("catagory");
%>
<head>
<title>客户端组件库</title>
</head>
<frameset rows="*"  cols="200, *" frameborder="no" border="0" framespacing="1" id="frame1">
  <frame name="clientCmpIndex" scrolling="yes" src="<%=contextPath %>/rad/docs/catagory.jsp?path=<%=catagory %>" frameborder="1">
  <frame name="clientCmp" scrolling="yes" src="" frameborder="1">
</frameset>
</html>