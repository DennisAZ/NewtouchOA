<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  XHTML  1.0  Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
  String sendFlag = request.getParameter("type");
%>
<meta  http-equiv="Content-Type"  content="text/html;  charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<title>初始化...</title>
</head>

<body topmargin="5">

<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <%if("1".equals(sendFlag)){ %>
      <div class="content" style="font-size:12pt">系统初始化成功！</div>
      <%}else{ %>
      
          <div class="content" style="font-size:12pt">系统初始化失败，请与相关人员联系！</div>
           
            <%} %>
    </td>
  </tr>
</table>
<br><center><input type="button" class="BigButton" value="重新登录" onclick="window.location.href='<%=contextPath %>';"></center></body>
</html>