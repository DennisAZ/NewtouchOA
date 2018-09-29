<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<title>Insert  title  here</title>
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
</head> 
<body class="bodycolor" topmargin="5" onload=""> 
 <table class="MessageBox" align="center" width="460"> 
  <tr> 
    <td class="msg info"> 
      <h4 class="title">提示</h4> 
      <div class="content" style="font-size:12pt">指定范围内的手机短信记录已删除！</div> 
    </td> 
  </tr> 
</table> 
<br><center><input type="button" class="BigButton" value="返回" onclick="location.href = '<%=contextPath %>/core/funcs/mobilesms/sendManage/index.jsp'"></center>
</body> 
</html> 