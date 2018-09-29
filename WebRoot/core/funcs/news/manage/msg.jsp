<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
Integer msrg = (Integer)request.getAttribute("count");

%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js" ></script>
 <script type="text/javascript">
	function goBack(){
    window.location.href = "<%=contextPath%>/core/funcs/news/manage/newsQuery.jsp";
  }
</script>
</head>
<body>
<table align="center" class="MessageBox" width="300">
  <tr>
    <td class="msg info">
     &nbsp;删除了<%=msrg %>条记录  
    </td>
  </tr>  
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="javascript:goBack(); return false;"></center>
</body>
</html>