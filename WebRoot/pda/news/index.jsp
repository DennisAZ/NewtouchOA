<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.pda.news.data.YHPdaNews" %>
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
List news = (List) request.getAttribute("news");
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
  <span class="list_top_center">最新新闻</span>
</div>
<div id="list_main" class="list_main">
<%
int pageSize = (Integer)request.getAttribute("pageSize");
int thisPage = (Integer)request.getAttribute("thisPage");
int totalPage = (Integer)request.getAttribute("totalPage");
int count = news.size();
for(int i = 0 ; i < count ; i++){
  if(i >= pageSize)
    break;
  YHPdaNews oneNew = (YHPdaNews)news.get(i);
  int seqId = oneNew.getSeqId();
  String provider = oneNew.getProvider();
  String fromName = oneNew.getUserName();
  String subject = oneNew.getSubject();
  Date newsTime = oneNew.getNewsTime();
  String format = oneNew.getFormat();
  String typeId = oneNew.getTypeId();
  String classDesc = oneNew.getClassDesc() == null ? "" : "【"+oneNew.getClassDesc()+"】";
  String attachmentId = oneNew.getAttachmentId() == null ? "" : oneNew.getAttachmentId();
  String attachmentName = oneNew.getAttachmentName() == null ? "" : oneNew.getAttachmentName();
  String content = oneNew.getContent();
  String prs = "&seqId="+seqId
              +"&provider="+provider
              +"&subject="+YHUtility.encodeURL(subject)
              +"&typeId="+typeId
              +"&newsTime="+newsTime.toString()
              +"&format="+format
              +"&fromName="+fromName
              +"&attachmentId="+attachmentId
              +"&attachmentName="+attachmentName
              +"&content="+content;
  %>
   <a class="list_item" href="<%=contextPath %>/pda/news/read.jsp?P=<%=loginPerson.getSeqId() %><%=prs %>" hidefocus="hidefocus">
      <div class="list_item_subject"><%=i+1+(thisPage-1)*pageSize %>.<%=classDesc %><%=subject %></div>
      <div class="list_item_time"><%=fromName %> <%=newsTime.toString().substring(0,10) %></div>
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
  out.println("<div id=\"message\" class=\"message\">无最新新闻</div>");
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
      <a href="<%=contextPath %>/yh/pda/news/act/YHPdaNewsAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=1" id="pageFirst" class="pageFirst" title="首页"></a>
      <a href="<%=contextPath %>/yh/pda/news/act/YHPdaNewsAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=<%=thisPage-1 %>" id="pagePrevious" class="pagePrevious" title="上一页"></a>
    <%}
      if(thisPage == totalPage ){%>
      <a href="javascript:void(0);" id="pageNext" class="pageNextDisable" title="下一页"></a>
      <a href="javascript:void(0);" id="pageLast" class="pageLastDisable" title="末页"></a>
    <%} else{%>
    <a href="<%=contextPath %>/yh/pda/news/act/YHPdaNewsAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=<%=thisPage+1 %>" id="pageNext" class="pageNext" title="下一页"></a>
    <a href="<%=contextPath %>/yh/pda/news/act/YHPdaNewsAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=<%=totalPage %>" id="pageLast" class="pageLast" title="末页"></a>
    <%} %>
  </div>
  </div>
  <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>
