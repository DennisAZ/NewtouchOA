<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>错误页面</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/style.css">
  </head>
  
  <body>
  <div style="text-align:center; padding-top:10px">
      <div class="warnBox">
      	<div id="warnContent2">
        	<div id="warnTitle">很抱歉，您请求的页面出错了！</div>
        	<div class="deepGray">错误原因：<bean:message key="exceptionMess"/>&nbsp;</div>
        </div>
      </div>
  </div>
  </body>
</html>
