<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<script>
function gotoIndex(){
  window.location.href = contextPath + "/yh/core/oaknow/act/YHOAKnowAct/OAKnowIndex.act";
 }
</script>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
</head>
<body>
<table align="center" width="230" class="MessageBox">
  <tbody><tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div style="font-size: 12pt;" class="content">提问成功!</div>
    </td>
  </tr>
</tbody>
</table><br>
<center><input type="button" onclick="javascript:gotoIndex();return false;" value="返回" class="BigButton"></center>
</body>
</html>


