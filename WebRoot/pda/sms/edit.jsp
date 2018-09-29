<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.pda.sms.data.YHPdaSms" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="yh.oa.tools.StaticData"%>
<%
String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
YHPerson loginPerson = (YHPerson)session.getAttribute("LOGIN_USER");
String fromName = (String) request.getParameter("fromName") == null ? "" : (String) request.getParameter("fromName");
%>
<!doctype html>
<html>
<head>
<title><%=StaticData.SOFTTITLE%></title>
<meta name="viewport" content="width=device-width" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath %>/pda/style/list.css" />
</head>
<body>
<div id="list_top">
  <div class="list_top_left"><a class="ButtonBack" href="<%=contextPath %>/yh/pda/sms/act/YHPdaSmsAct/doint.act?P=<%= loginPerson.getSeqId()%>"></a></div>
  <span class="list_top_center">写新短信</span>
  <div class="list_top_right"><a class="ButtonA" href="javascript:document.form1.submit();">发送</a></div>
</div>
<div id="list_main" class="list_main">
   <form action="<%=contextPath %>/yh/pda/sms/act/YHPdaSmsAct/sendSms.act?P=<%= loginPerson.getSeqId()%>"  method="post" name="form1">
      收信人姓名：<input type="text" style="width:100%;" name="toName" value="<%=fromName %>"><br><br>
      短信内容：<br>
      <textarea style="width:100%;" name="content" rows="3" wrap="on"></textarea>
      <input type="hidden" name="P" value="<%= loginPerson.getSeqId()%>">
</form>
</div>
<div id="list_bottom">
  <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>