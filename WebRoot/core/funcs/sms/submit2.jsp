<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("toId");
  if(seqId == null) {
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发送内部短信</title>

<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
  var seqId = "<%=seqId%>";
  function keepSend(id){
    location=contextPath + '/core/funcs/sms/smsBackIf.jsp?fromId='+id;
  }
</script>
</head>
<body  onload="">
<table class="MessageBox" align="center" width="260">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">短信息已发送</div>
    </td>
  </tr>
</table>
<br>
<br>
<div align="center">
 <input type="button" id="button1" name="button1" value="继续发短信" class="BigButton" onClick="keepSend('<%=seqId%>')"> &nbsp;&nbsp;
  <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath %>/core/funcs/sms/smsBackIf.jsp'">
</div>
</body>
</html>