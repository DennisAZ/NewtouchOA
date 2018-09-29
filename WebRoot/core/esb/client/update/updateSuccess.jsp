<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  XHTML  1.0  Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<title>操作结果</title>

<script type="text/javascript">
function doSubmit(){
	$("form1").submit();
}
</script>
</head>

<body topmargin="5">

<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">系统升级成功，重启电脑完成更新！</div>
    </td>
  </tr>
</table>
<form id="form1" action="<%=contextPath%>/yh/core/esb/client/update/act/YHUpdateClientAct/restartSystem.act" method="post" enctype="multipart/form-data">
<center><input type="button" class="BigButton" value="重启" onclick="doSubmit()"></center>
</form>
</body>
</html>