<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.oa.tools.StaticData"%>
<%
String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
YHPerson loginPerson = (YHPerson)session.getAttribute("LOGIN_USER");
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
  <div class="list_top_left"><a class="ButtonBack" href="<%=contextPath %>/yh/pda/diary/act/YHPdaDiaryAct/doint.act?P=<%= loginPerson.getSeqId()%>"></a></div>
  <span class="list_top_center">今日日志</span>
  <div class="list_top_right"><a class="ButtonA" href="javascript:document.form1.submit();">保存</a></div>
</div>
<div id="list_main" class="list_main">
<form action="<%=contextPath %>/yh/pda/diary/act/YHPdaDiaryAct/newDiary.act"  method="post" name="form1">
   日志类型:<br>
 <select name="diaType" class="BigSelect" style="width:100%;" >
   <option value="1" selected>工作日志</option>
   <option value="2" >个人日志</option>
 </select><br><br>
   日志内容:<br>
<textarea cols="18" name="content" rows="6" wrap="on" style="width:100%;"></textarea>
<br>
<input type="hidden" name="P" value="<%=loginPerson.getSeqId() %>">
</form>
</div>
<div id="list_bottom">
  <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>