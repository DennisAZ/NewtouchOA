<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>自动切换</title>
<link rel="stylesheet" href = "../css/tab.css">
<script type="text/javascript" src="../js/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script>
var jso = [{title:"a",contentUrl:"/yh/raw/ljf/YHCategoryAct1/getContent.act?title=a", imgUrl:"/yh/raw/ljf/imgs/1hrms.gif"},
	  {title:"b",contentUrl:"/yh/raw/ljf/YHCategoryAct1/getContent.act?title=b", imgUrl:"/yh/raw/ljf/imgs/1news.gif"},
	  {title:"c",contentUrl:"/yh/raw/ljf/YHCategoryAct1/getContent.act?title=c", imgUrl:"/yh/raw/ljf/imgs/asset.gif"},
	  {title:"d",contentUrl:"/yh/raw/ljf/YHCategoryAct1/getContent.act?title=d", imgUrl:"/yh/raw/ljf/imgs/asset.gif"}];
</script>
<body onload="buildTab(jso)">
</body>
</html>