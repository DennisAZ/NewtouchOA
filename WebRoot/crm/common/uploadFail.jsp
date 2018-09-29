<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>上传文件</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" type="text/css" href="css/style.css">
    <style type="text/css">
    	#upload{
			width:420px; 
			text-align:left;
		}
    </style>

  </head>
  
  <body>
  <div id="upload">
    	<fieldset>
        	<legend class="blackblue ">上传附件</legend>
    		&nbsp;&nbsp;<img src="images/content/fail.gif" style="vertical-align:middle;"/>&nbsp;上传失败！&nbsp;请重新<a href="common/docUpload.jsp">上传</a>
            <span class="red">
            	${errorMsg}
            </span>
 		</fieldset>
  </div>
  </body>
</html>
