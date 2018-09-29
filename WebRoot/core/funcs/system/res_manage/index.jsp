<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统资源管理</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function doInit() {
var jso = [
           { title:"资源监控", useTextContent:true, contentUrl:"monitor/index.jsp", useIframe:true}
           ,{title:"资源占用情况", useTextContent:true,  contentUrl:"query/index.jsp" , useIframe:true}
           ,{title:"资源回收", useTextContent:true,  contentUrl:"callback/index.jsp" , useIframe:true}
           ,{title:"日志文件下载", useTextContent:true,  contentUrl:"log/" , useIframe:true}
           ];
buildTab(jso, 'div');
}
</script>
</head>
<body onload="doInit()">
<div id="div"></div>
</body>
</html>