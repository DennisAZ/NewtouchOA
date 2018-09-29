<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  XHTML  1.0  Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
  String sendFlag = request.getParameter("sendFlag");
  String msg = URLDecoder.decode(request.getParameter("msg"), "UTF-8");
  String seqId = request.getParameter("seqId");
%>
<meta  http-equiv="Content-Type"  content="text/html;  charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/sendLogic.js"></script>
<title>操作结果</title>
<script> 
/**
 * 发送详细信息
 */
function detailSendInfo(seqId){
  var URL = contextPath + "/subsys/jtgwjh/sendDoc/detailSendInfo.jsp?seqId=" + seqId;
  newWindow(URL,'820', '500');
}

function sendComplete(seqId){
  var URL = contextPath + "/subsys/jtgwjh/sendDoc/complete/manage.jsp";
  newWindow(URL,'900', '550');
}
</script>
</head>

<body topmargin="5">

<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">发文登记信息保存成功！</div>
      <%if("1".equals(sendFlag)){ %>
      <div class="content" style="font-size:12pt"><%=msg %></div>
      <%} %>
    </td>
  </tr>
</table>
<br>
<center>
<input type="button" class="BigButton" value="返回" onclick="window.location.href='<%=contextPath %>/subsys/jtgwjh/sendDoc/new.jsp';">
<%if("1".equals(sendFlag)){ %>
<input type="button" class="BigButtonC" value="查看发文列表" onclick="sendComplete();">
<input type="button" class="BigButtonC" value="查看发文状态" onclick="detailSendInfo(<%=seqId %>);">
<%} %>
</center>
</body>
</html>