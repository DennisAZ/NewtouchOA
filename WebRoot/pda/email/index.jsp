<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.pda.email.data.YHPdaEmail" %>
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
List emails = (List) request.getAttribute("emails");
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
  <div class="list_top_left"><a class="ButtonBack" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
  <span class="list_top_center">最新邮件</span>
  <div class="list_top_right"><a class="ButtonB" href="<%=contextPath %>/pda/email/new.jsp?P=<%=loginPerson.getSeqId() %>">写邮件</a></div>
</div>
<div id="list_main" class="list_main">
<%
int pageSize = (Integer)request.getAttribute("pageSize");
int thisPage = (Integer)request.getAttribute("thisPage");
int totalPage = (Integer)request.getAttribute("totalPage");
int count = emails.size();
for(int i = 0 ; i < count ; i++){
  if(i >= pageSize)
    break;
  YHPdaEmail email = (YHPdaEmail)emails.get(i);
  int seqId = email.getSeqId();
  int fromId = email.getFromId();
  String subject = email.getSubject();
  Date sendTime = email.getSendTime();
  String important = email.getImportant();
  String importantStr = "";
  if("1".equals(important))
    importantStr = "<font color=red>重要</font>";
  else if("2".equals(important))
    importantStr = "<font color=red>非常重要</font>";
  String attachmentId = email.getAttachmentId() == null ? "" : email.getAttachmentId();
  String attachmentName = email.getAttachmentName() == null ? "" : email.getAttachmentName();
  String fromName = email.getUserName();
  String content = email.getContent();
  String prs = "&seqId="+seqId+"&fromId="+fromId+"&subject="+subject+"&sendTime="+sendTime.toString()
              +"&important="+important+"&attachmentId="+attachmentId+"&attachmentName="+attachmentName+"&fromName="+fromName+"&content="+content;
  %>
   <a class="list_item" href="<%=contextPath %>/pda/email/read.jsp?P=<%=loginPerson.getSeqId() %><%=prs %>" hidefocus="hidefocus">
      <div class="list_item_subject"><%=i+1+(thisPage-1)*pageSize %>.<%=subject %> <%=importantStr %></div>
      <div class="list_item_time"><%=fromName %> <%=sendTime.toString().substring(0,16) %></div>
      <div class="list_item_arrow"></div>
<%
   if(!YHUtility.isNullorEmpty(attachmentId) && !YHUtility.isNullorEmpty(attachmentName)){
%><div class="list_item_attachment"></div><%
   }
%>
   </a>
<% 
}
if(count==0)
  out.println("<div id=\"message\" class=\"message\">无符合条件的记录</div>");
%>
</div>
<div id="list_bottom">
  <div class="list_bottom_left">
  <div id="pageArea" class="pageArea">
           第<span id="pageNumber" class="pageNumber"><%=thisPage %>/<%=totalPage %></span>页

    <%if(thisPage == 1 || thisPage == 0){ %>
      <a href="javascript:void(0);" id="pageFirst" class="pageFirstDisable" title="首页"></a>
      <a href="javascript:void(0);" id="pagePrevious" class="pagePreviousDisable" title="上一页"></a>
    <%} else{%>
      <a href="<%=contextPath %>/yh/pda/email/act/YHPdaEmailAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=1" id="pageFirst" class="pageFirst" title="首页"></a>
      <a href="<%=contextPath %>/yh/pda/email/act/YHPdaEmailAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=<%=thisPage-1 %>" id="pagePrevious" class="pagePrevious" title="上一页"></a>
    <%}
      if(thisPage == totalPage ){%>
      <a href="javascript:void(0);" id="pageNext" class="pageNextDisable" title="下一页"></a>
      <a href="javascript:void(0);" id="pageLast" class="pageLastDisable" title="末页"></a>
    <%} else{%>
    <a href="<%=contextPath %>/yh/pda/email/act/YHPdaEmailAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=<%=thisPage+1 %>" id="pageNext" class="pageNext" title="下一页"></a>
    <a href="<%=contextPath %>/yh/pda/email/act/YHPdaEmailAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=<%=totalPage %>" id="pageLast" class="pageLast" title="末页"></a>
    <%} %>
  </div>
  </div>
  <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>
