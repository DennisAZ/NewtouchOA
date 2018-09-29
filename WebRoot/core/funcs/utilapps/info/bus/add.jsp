<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>公交查询</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script  type="text/Javascript" >
function Init(){
	if($('seqId').value == null ||$('seqId').value == "")
		$('type').innerHTML = "新建成功！";
	else
		$('type').innerHTML = "修改成功！";
}
</script>
</head>

<body class="bodycolor" topmargin="5" onload="Init()">
<input type="hidden" id="seqId" name="seqId" value="${param.seqId}">
<table class="MessageBox" align="center" width="240">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div id="type" class="content" style="font-size:12pt"></div>
    </td>
  </tr>
</table>

<center><input type="button" class="BigButton" value="返回" onclick="history.go(-2);"></center>
</body>
</html>
