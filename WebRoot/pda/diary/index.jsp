<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.pda.diary.data.YHPdaDiary" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="yh.oa.tools.StaticData"%>
<%
String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
YHPerson loginPerson = (YHPerson)session.getAttribute("LOGIN_USER");
List diarys = (List) request.getAttribute("diarys");
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
  <span class="list_top_center">工作日志</span>
  <div class="list_top_right"><a class="ButtonB" href="<%=contextPath %>/pda/diary/new.jsp?P=<%=loginPerson.getSeqId() %>">写日志</a></div>
</div>
<div id="list_main" class="list_main">
<%
int pageSize = (Integer)request.getAttribute("pageSize");
int thisPage = (Integer)request.getAttribute("thisPage");
int totalPage = (Integer)request.getAttribute("totalPage");
int count = diarys.size();
for(int i = 0 ; i < count ; i++){
  if(i >= pageSize)
    break;
  YHPdaDiary diary = (YHPdaDiary)diarys.get(i);
  int seqId = diary.getSeqId();
  Date diaDate = diary.getDiaDate();
  String diaDateStr = diaDate.toString().substring(0,10);
  int diaType = diary.getDiaType();
  String diaTypeDesc = "";
  if(diaType == 1)
    diaTypeDesc = "工作日志";
  else if(diaType == 2)
    diaTypeDesc = "个人日志";
  String content = diary.getContent();
  String contentStr = content.length() > 21 ? content.substring(0,20) : content;%>
<a class="list_item" href="<%=contextPath %>/pda/diary/edit.jsp?P=<%=loginPerson.getSeqId() %>&seqId=<%=seqId %>&diaType=<%=diaType %>&content=<%=content %>" hidefocus="hidefocus" >
   <div class="list_item_subject"><%=i+1+(thisPage-1)*pageSize %>.<%=diaTypeDesc %> <%=contentStr %></div>
   <div class="list_item_time"><%=diaDateStr %></div>
   <div class="list_item_arrow"></div>
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
      <a href="<%=contextPath %>/yh/pda/diary/act/YHPdaDiaryAct/doint.act?P=<%=loginPerson.getSeqId() %>&thisPage=1" id="pageFirst" class="pageFirst" title="首页"></a>
      <a href="<%=contextPath %>/yh/pda/diary/act/YHPdaDiaryAct/doint.act?P=<%=loginPerson.getSeqId() %>&thisPage=<%=thisPage-1 %>" id="pagePrevious" class="pagePrevious" title="上一页"></a>
    <%}
      if(thisPage == totalPage ){%>
      <a href="javascript:void(0);" id="pageNext" class="pageNextDisable" title="下一页"></a>
      <a href="javascript:void(0);" id="pageLast" class="pageLastDisable" title="末页"></a>
    <%} else{%>
    <a href="<%=contextPath %>/yh/pda/diary/act/YHPdaDiaryAct/doint.act?&P=<%=loginPerson.getSeqId() %>&thisPage=<%=thisPage+1 %>" id="pageNext" class="pageNext" title="下一页"></a>
    <a href="<%=contextPath %>/yh/pda/diary/act/YHPdaDiaryAct/doint.act?&P=<%=loginPerson.getSeqId() %>&thisPage=<%=totalPage %>" id="pageLast" class="pageLast" title="末页"></a>
    <%} %>
  </div>
  </div>
  <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>
