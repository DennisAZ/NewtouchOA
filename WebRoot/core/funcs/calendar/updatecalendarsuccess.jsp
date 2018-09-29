<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>今天</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
</head>
 <script type="text/javascript">
function adddiary(){
  var URL = "<%=contextPath%>/core/funcs/calendar/adddiary.jsp";
  window.open(URL,"calendar","height=530,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=100,resizable=yes");
}
 </script>
<body class="" topmargin="5">
 
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">已设置为完成状态</div>
    </td>
  </tr>
</table>
<div align="center">
   <input type="button" value="建立日志" class="BigButton" onclick="adddiary();">&nbsp;&nbsp;
   <input type="button" value="返回" class="BigButton" onclick="history.go(-1);">
</div>
</body>
</html>
