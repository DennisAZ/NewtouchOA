<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String msrg = (String)request.getAttribute("act.retmsrg");
if (msrg == null) {
  msrg = "运行中出现了错误";
}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js" ></script>
</head>
<body>
<table align="center" class="MessageBox" width="300">
  <tr>
    <td class="msg warning">
     &nbsp;<%=msrg %>    
    </td>
  </tr>
</table>
</body>
</html>