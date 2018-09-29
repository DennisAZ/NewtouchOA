<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="yh.core.util.YHUtility" %>
<%@ page import="yh.oa.tools.StaticData"%>
<%
String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
YHPerson loginPerson = (YHPerson)session.getAttribute("LOGIN_USER");
String seqId = (String) request.getParameter("seqId");
String fromId = (String) request.getParameter("fromId");
String subject = (String) request.getParameter("subject");
String newsTime = (String) request.getParameter("newsTime");
String attachmentId = (String) request.getParameter("attachmentId");
String attachmentName = (String) request.getParameter("attachmentName");
String fromName = (String) request.getParameter("fromName");
String content = (String) request.getParameter("content");

String attachmentIds[] = attachmentId.split(",");
String attachmentNames[] = attachmentName.replace("*",",").split(",");

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
  <div class="list_top_left"><a class="ButtonBack" href="<%=contextPath %>/yh/pda/news/act/YHPdaNewsAct/search.act?P=<%= loginPerson.getSeqId()%>"></a></div>
  <span class="list_top_center">阅读公告通知</span>
</div>
<div id="list_main" class="list_main">
   <div class="read_title"><%=subject %> </div>
   <div class="read_time"><%=fromName %> <%=newsTime.substring(0,16) %></div>
<%
if(!YHUtility.isNullorEmpty(attachmentId) && !YHUtility.isNullorEmpty(attachmentName)){
  %><div class="read_attachment">附件：<%
  for(int i = 0; i < attachmentNames.length; i++){
%>
   <a href="<%=contextPath %>/yh/core/funcs/office/ntko/act/YHNtkoAct/downFile.act?module=news&attachmentId=<%=attachmentIds[i] %>&attachmentName=<%=attachmentNames[i] %>"><%=attachmentNames[i] %></a>&nbsp;&nbsp;&nbsp;&nbsp;
<%
  }
  %></div><%
}
%>
   <div class="read_content"><%=content %></div>
</div>
<div id="list_bottom">
  <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>
