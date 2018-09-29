<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>自动切换</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script>
var jso = [{title:"a", content:"aaaaaa", imgUrl:"/yh/raw/ljf/imgs/1hrms.gif"},
	  {title:"b", content:"bbbbbbb", imgUrl:"/yh/raw/ljf/imgs/1news.gif"},
	  {title:"c", content:"ccccccc", imgUrl:"/yh/raw/ljf/imgs/asset.gif"},
	  {title:"d", content:"dddddd", imgUrl:"/yh/raw/ljf/imgs/asset.gif"}];
</script>
</head>
<body onload="buildTab(jso)">
</body>
</html>