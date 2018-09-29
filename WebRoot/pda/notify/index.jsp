<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.pda.notify.data.YHPdaNotify" %>
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
List notifies = (List) request.getAttribute("notifies");
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
  <span class="list_top_center">公告通知</span>
</div>
<div id="list_main" class="list_main">
<%
int pageSize = (Integer)request.getAttribute("pageSize");
int thisPage = (Integer)request.getAttribute("thisPage");
int totalPage = (Integer)request.getAttribute("totalPage");
int count = notifies.size();
for(int i = 0 ; i < count ; i++){
  if(i >= pageSize)
    break;
  YHPdaNotify notify = (YHPdaNotify)notifies.get(i);
  int seqId = notify.getSeqId();
  String fromId = notify.getFromId();
  String fromName = notify.getUserName();
  String subject = notify.getSubject();
  String top = notify.getTop();
  String topStr = "";
  if("1".equals(top)) 
    topStr="<img src='"+contextPath+"/pda/style/images/top.png' />";
 else 
   topStr="";   
  String typeId = notify.getTypeId();
  String classDesc = notify.getClassDesc() == null ? "" : "【"+notify.getClassDesc()+"】";
  Date beginDate = notify.getBeginDate();
  String attachmentId = notify.getAttachmentId() == null ? "" : notify.getAttachmentId();
  String attachmentName = notify.getAttachmentName() == null ? "" : notify.getAttachmentName();
  String content = notify.getContent();
  String prs = "&seqId="+seqId+"&fromId="+fromId+"&fromName="+fromName+"&subject="+subject+"&top="+top+"&typeId="+typeId+"&beginDate="+beginDate.toString()
              +"&attachmentId="+attachmentId+"&attachmentName="+attachmentName+"&content="+content;
  %>
<a class="list_item" href="<%=contextPath %>/pda/notify/read.jsp?P=<%=loginPerson.getSeqId() %><%=prs %>" hidefocus="hidefocus" >
   <div class="list_item_subject"><%=i+1+(thisPage-1)*pageSize %>.<%=classDesc %><%=subject %> <%=topStr %></div>
   <div class="list_item_time"><%=fromName %> <%=beginDate.toString().substring(0,10) %></div>
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
  out.println("<div id=\"message\" class=\"message\">无新公告通知</div>");
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
      <a href="<%=contextPath %>/yh/pda/notify/act/YHPdaNotifyAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=1" id="pageFirst" class="pageFirst" title="首页"></a>
      <a href="<%=contextPath %>/yh/pda/notify/act/YHPdaNotifyAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=<%=thisPage-1 %>" id="pagePrevious" class="pagePrevious" title="上一页"></a>
    <%}
      if(thisPage == totalPage ){%>
      <a href="javascript:void(0);" id="pageNext" class="pageNextDisable" title="下一页"></a>
      <a href="javascript:void(0);" id="pageLast" class="pageLastDisable" title="末页"></a>
    <%} else{%>
    <a href="<%=contextPath %>/yh/pda/notify/act/YHPdaNotifyAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=<%=thisPage+1 %>" id="pageNext" class="pageNext" title="下一页"></a>
    <a href="<%=contextPath %>/yh/pda/notify/act/YHPdaNotifyAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=<%=totalPage %>" id="pageLast" class="pageLast" title="末页"></a>
    <%} %>
  </div>
  </div>
  <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>
