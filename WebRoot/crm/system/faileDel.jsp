<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>操作失败</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	
	<link rel="stylesheet" type="text/css" href="crm/css/style.css">
	

  </head>
  
  <body>
  	<div style="text-align:center; padding-top:10px">
          <div class="warnBox">
            <div id="warnContent1">
                <div id="warnTitle">无法完成请求！</div>
                <div class="deepGray">${ms}&nbsp;</div>
            </div>
          </div>
      </div>
  </body>
</html>
