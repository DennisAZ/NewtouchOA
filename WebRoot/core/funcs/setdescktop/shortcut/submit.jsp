<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body topmargin="5">
<table class="MessageBox" align="center" width="400">
  <tr>
    <td class="msg info">

      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">菜单快捷组定义已保存！</div>
    </td>
  </tr>
</table>
<div align="center">
  <input type="button"  value="返回" class="BigButton" name="back" onClick="history.back();">
</div>
</body>
</html>