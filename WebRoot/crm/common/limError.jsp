<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>操作权限</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
	<link rel="stylesheet" type="text/css" href="css/style.css">
  </head>
  
  <body>
  <div id="limOperate" style="text-align:center; padding-top:10px">
      <div class="warnBox">
      	<div id="warnContent3">
        	<div id="warnTitle" style="padding-top:8px; height:30px;">对不起，您没有权限进行此次操作！</div>
        </div>
      </div>
  </div>
  </body>
</html>
