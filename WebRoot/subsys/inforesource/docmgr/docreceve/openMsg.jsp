<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<%
  String msg = (String)request.getAttribute("msg");
  if(msg == null || msg == ""){
    msg = "提示出错！";
  }
%>
<script>
function closeWin(){
  window.opener.location.href = window.opener.location.href;
  window.close();
}


</script>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
</head>
<body>
<table align="center" width="230" class="MessageBox">
  <tbody><tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div style="font-size: 12pt; width:150px;" class="content"><%=msg %></div>
    </td>
  </tr>
</tbody>
</table><br>
<center><input type="button" onclick="javascript:closeWin();" value="关闭" class="BigButton"></center>
</body>
</html>


