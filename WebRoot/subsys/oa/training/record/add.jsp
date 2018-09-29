<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
  String userIdStr = request.getParameter("userIdStr")== null ? "" : YHUtility.encodeSpecial(request.getParameter("userIdStr"));
  String data = request.getParameter("data")== null ? "" : YHUtility.encodeSpecial(request.getParameter("data"));
%>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  XHTML  1.0  Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var userIdStr = "<%=userIdStr%>";
var data = "<%=data%>";
function doInit(){
  if(data != "0"){
    $("userIdStr").value = userIdStr;
    if($("userIdStr") && $("userIdStr").value.trim()){
      bindDesc([{cntrlId:"userIdStr", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
  }
  
}

</script>
<title>新建培训记录</title>
</head>

<body topmargin="5" onload="doInit()">

<%if("0".equals(data)) {%>
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">成功增加培训记录信息！</div>
    </td>
  </tr>
</table>
<%}else{ %>
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">成功增加培训记录信息！</div>
    </td>
  </tr>
</table>
<br>
<table class="MessageBox" align="center" width="400">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">以下人员信息已存在：<span id="userIdStrDesc"></span></div>
    </td>
  </tr>
</table>
<input type="hidden" name="userIdStr" id="userIdStr" value="">  
<%} %>
<br><center><input type="button" class="BigButton" value="返回" onclick="window.location.href='<%=contextPath %>/subsys/oa/training/record/newrecord.jsp';"></center></body>
</html>