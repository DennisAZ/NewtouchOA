<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>

<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  XHTML  1.0  Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">


</script>
<title>试卷管理</title>
</head>

<body topmargin="5">

<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">试卷正被使用！</div>
    </td>
  </tr>
</table>

<br><center><input type="button" class="BigButton" value="返回" onclick="window.location.href='<%=contextPath %>/subsys/oa/examManage/paperManage/manage.jsp';"></center></body>
</html>