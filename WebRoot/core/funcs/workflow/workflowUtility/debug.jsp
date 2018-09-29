<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>下载文件debug</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript">
function downLoad(type) {
  var url = contextPath + "/yh/core/funcs/workflow/act/YHUpdateAct/getDebug.act?type=" + type;
  window.open(url);
}
</script>
</head>
<body>
<input type="button" value="下载debug文件" onclick="downLoad('yzq')">
<input type="button" value="下载安装debug文件" onclick="downLoad('yh')">
</body>
</html>