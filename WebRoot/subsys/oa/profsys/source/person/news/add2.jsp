<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>修改人士信息</title>
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript">
function onFian() {
  window.location.href = "<%=contextPath%>/subsys/oa/profsys/source/person/index1.jsp";
}
</script>
</head>
<body class="bodycolor" topmargin="5">
<table class="MessageBox" align="center" width="350">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">信息修改成功！</div>
    </td>
  </tr>
</table>
<center>
<input type="button" value="返回" class="BigButton" onClick="onFian();">
</center>
</body>
</html>
