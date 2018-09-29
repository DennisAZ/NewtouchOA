<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<title>Insert title here</title>
</head>
<body>
<form id="form"action="<%=contextPath %>/yh/core/funcs/jexcel/act/YHExport/importCsv.act"  method="post" enctype="multipart/form-data">
<input type="file" name="file"></input>
<input>
</form>
<input type="button" value="提交" onclick="$('form').submit()">
</body>
</html>