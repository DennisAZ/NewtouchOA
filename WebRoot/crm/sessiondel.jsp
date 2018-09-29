<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
session.invalidate();

out.print("<script type='text/javascript'>window.location.href='login.jsp';</script>");
out.flush();
//response.sendRedirect("login.jsp");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<base href="<%=basePath%>"></base>
 <title>退出登录</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	 
</head>
 <body>
</body>
</html>
