<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ include file="/core/inc/header.jsp" %>
<%
 String sendBox = request.getParameter("sendBox") == null ? "" : request.getParameter("sendBox");
String inBox = request.getParameter("inBox") == null ? "" : request.getParameter("inBox");
String mailSize = request.getParameter("mailSize") == null ? "" : request.getParameter("mailSize");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/emaillogic.js"></script>
<title>发送提示</title>
</head>
<body>
<br>
<table class="MessageBox" align="center" width="500">
  <tr>
    <td class="msg warning">
      <H4 class=title>警告</H4>
      <div class="content" style="font-size:12pt">您的邮箱已超过容量限制，请清除您的无用邮件！<br><br>
        <div align=left>邮箱限制容量：<%=mailSize %> 字节
          <br>收件箱(包括已删除邮件箱)：<%=inBox %> 字节
          <br>发件箱：<%=sendBox %> 字节</div>
      </div>
    </td>
  </tr>
</table>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="location='<%=contextPath%>/core/funcs/email/outbox/index.jsp';">
</div>
</body>
</html>