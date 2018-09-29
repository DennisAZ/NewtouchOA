<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String errCount = request.getParameter("errCount");
  if (errCount == null){
    errCount = "";
  }
  String okCount = request.getParameter("okCount");
  if (okCount == null){
    okCount = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>短信提醒设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var errCount = "<%=errCount%>";
var okCount = "<%=okCount%>";
</script>
</head>
<body topmargin="5">
<table class="MessageBox" align="center" width="330">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">成功添加<%=okCount%>条，跳过<%=errCount%>条</div>
    </td>
  </tr>
</table>
<center>
  <Input type="button" name="button" class="BigButton" value="返回" onclick="window.history.back();">
</center>
</body>
</html>