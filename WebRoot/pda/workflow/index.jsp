<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.pda.workflow.data.YHPdaWorkflow" %>
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
List workflowes = (List) request.getAttribute("workflowes");
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
  <span class="list_top_center">工作流</span>
</div>
<div id="list_main" class="list_main">
<%
int pageSize = (Integer)request.getAttribute("pageSize");
int thisPage = (Integer)request.getAttribute("thisPage");
int totalPage = (Integer)request.getAttribute("totalPage");
int count = workflowes.size();
for(int i = 0 ; i < count ; i++){
  if(i >= pageSize)
    break;
  YHPdaWorkflow workflow = (YHPdaWorkflow)workflowes.get(i);
  int prcsId = workflow.getPrcsId();
  int runId = workflow.getRunId();
  String runName = workflow.getRunName();
  int flowId = workflow.getFlowId();
  String flowName = workflow.getFlowName();
  String flowType = workflow.getFlowType();
  String prcsFlag = workflow.getPrcsFlag();
  int flowPrcs = workflow.getFlowPrcs();
  String feedback = workflow.getFeedback();
  
  String opFlag = workflow.getOpFlag();
  String opFlagDesc = "";
  if("1".equals(opFlag))
    opFlagDesc="主办";
  
  String prcsName = workflow.getPrcsName();
  String prcsNameStr = "";
  if("1".equals(flowType)){
    prcsNameStr = "第"+prcsId+"步："+prcsName;
  }
  else{
    prcsNameStr="第"+prcsId+"步";
    feedback = "0";
  }
  


  String prs = "&prcsId="+prcsId
              +"&runId="+runId
              +"&flowId="+flowId
              +"&prcsFlag="+prcsFlag
              +"&flowPrcs="+flowPrcs
              +"&opFlag="+opFlag;
  %>
   <div class="list_item">
      <div class="list_item_subject"><%=i+1+(thisPage-1)*pageSize %>. [<%=runId %>] - <b><%=flowName %></b> - <%=runName %></div>
      <div class="list_item_time"><%=prcsNameStr %> <%=opFlagDesc %></div>
      <div class="list_item_time"></div>
      <div class="list_item_arrow"></div>
      <div class="list_item_op">
        <a href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/getFormPrintMsg.act?P=<%=loginPerson.getSeqId() %>&flowId=<%=flowId %>&runId=<%=runId %>&prcsId=<%=prcsId %>&flowPrcs=<%=flowPrcs %>&opFlag=<%=opFlag %>&feedback=<%=feedback %>">表单</a>
<%
if(!"1".equals(opFlag)){
%>
        <a href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/stop.act?P=<%=loginPerson.getSeqId() %>&flowId=<%=flowId %>&runId=<%=runId %>&prcsId=<%=prcsId %>&flowPrcs=<%=flowPrcs %>">办理完毕</a>
<%
}
else{
%>
         <a href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/getHandlerData.act?P=<%=loginPerson.getSeqId() %>&flowId=<%=flowId %>&runId=<%=runId %>&prcsId=<%=prcsId %>&flowPrcs=<%=flowPrcs %>&opFlag=<%=opFlag %>&feedback=<%=feedback %>">主办</a>
         <a href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/turn.act?P=<%=loginPerson.getSeqId() %>&flowId=<%=flowId %>&runId=<%=runId %>&prcsId=<%=prcsId %>&flowPrcs=<%=flowPrcs %>">转交</a>
<%
}
if(!"1".equals(feedback)){
%>
         <a href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/sign.act?P=<%=loginPerson.getSeqId() %>&flowId=<%=flowId %>&runId=<%=runId %>&prcsId=<%=prcsId %>&flowPrcs=<%=flowPrcs %>">会签</a>
<%
}
if(!"1".equals(flowType)){
%>
         <a href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/stop.act?P=<%=loginPerson.getSeqId() %>&flowId=<%=flowId %>&runId=<%=runId %>&prcsId=<%=prcsId %>&flowPrcs=<%=flowPrcs %>">结束流程</a>
<%
}
%>
      </div>
  </div>
<% 
}
if(count==0)
  out.println("<div id=\"message\" class=\"message\">无待办工作</div>");
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
      <a href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=1" id="pageFirst" class="pageFirst" title="首页"></a>
      <a href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=<%=thisPage-1 %>" id="pagePrevious" class="pagePrevious" title="上一页"></a>
    <%}
      if(thisPage == totalPage ){%>
      <a href="javascript:void(0);" id="pageNext" class="pageNextDisable" title="下一页"></a>
      <a href="javascript:void(0);" id="pageLast" class="pageLastDisable" title="末页"></a>
    <%} else{%>
    <a href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=<%=thisPage+1 %>" id="pageNext" class="pageNext" title="下一页"></a>
    <a href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/search.act?P=<%=loginPerson.getSeqId() %>&thisPage=<%=totalPage %>" id="pageLast" class="pageLast" title="末页"></a>
    <%} %>
  </div>
  </div>
  <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>
