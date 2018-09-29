<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%
 String msg = (String)request.getAttribute("message");
%>
<html>
<head>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script>
  function goback(){
    window.location.href = contextPath + "/subsys/oa/book/borrow_manage/return/index.jsp";
  }
</script>
</head>
<body>
<table align="center" width="230" class="MessageBox">
  <tbody><tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div style="font-size: 12pt;" class="content">保存成功</div>
    </td>
  </tr>
</tbody>
</table><br>
<center><input type="button" onclick="goback();return false;" value= "返回" class="BigButton"></center>
</body>
</html>


