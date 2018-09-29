<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  XHTML  1.0  Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<title>桌面参数设置</title>
<script type="text/javascript">
function doInit() {
  if (${param.status} == 1) {
    document.getElementById("content").innerHTML = "导入失败,请检查zip文件无误后再次导入!";
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table class="MessageBox" align="center" width="240">
  <tr>
    <td class="msg info">
      <div id="content" class="content" style="font-size:12pt">成功导入${param.amount}条</div>
    </td>
  </tr>
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="history.back();"></center></body>
</html>