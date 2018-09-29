<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<link rel="stylesheet" href ="<%=cssPath%>/cmp/tab.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript">
function doInit(){
  var jso = [{title:"a",  content:"aaaaaa",  imgUrl:"<%=imgPath%>/cmp/tab/1hrms.gif"},
    {title:"b",  content:"bbbbbbb",  imgUrl:"<%=imgPath%>/cmp/tab/1news.gif"},
    {title:"c",  content:"ccccccc",  imgUrl:"<%=imgPath%>/cmp/tab/asset.gif"},
    {title:"d",  content:"dddddd",  imgUrl:"<%=imgPath%>/cmp/tab/asset.gif"}];

  buildTab(jso, 'contentDiv');
}
</script>
</head>
<body onclick="doInit()">
<div id="contentDiv"></div>
</body>
</html>