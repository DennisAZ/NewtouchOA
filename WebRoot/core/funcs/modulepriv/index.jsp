<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String uid = request.getParameter("uid");
  String userNameTemp = request.getParameter("userName");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<title>按模块设置管理范围</title>
</head>
<frameset  rows="*"  cols="181,*" frameborder="no" border="0" framespacing="0" id="frame2">	
  <frame name="privList" src="<%=contextPath%>/core/funcs/modulepriv/menu_left.jsp?uid=<%=uid%>&userName=<%=userNameTemp%>" frameborder="NO"/>
  <frame name="user_main" src="<%=contextPath%>/core/funcs/modulepriv/priv.jsp?uid=<%=uid%>&userName=<%=userNameTemp%>" frameborder="NO"/>
</frameset><noframes></noframes>
</html>
