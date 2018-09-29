<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%
 String msg = (String)request.getAttribute("message");
%>
<html>
<head>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script>
  function closeMe(){
     window.close();    
  }
</script>
</head>
<body>
<table align="center" width="230" class="MessageBox">
  <tbody><tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div style="font-size: 12pt;" class="content"><%=msg%></div>
    </td>
  </tr>
</tbody>
</table><br>
<center><input type="button" onclick="closeMe(); return false;" value= "关闭" class="BigButton"></center>
</body>
</html>


