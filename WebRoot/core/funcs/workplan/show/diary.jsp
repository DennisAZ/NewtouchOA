<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkDetail"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkPlan"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="yh.core.funcs.person.data.YHPerson" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>进度图日志详情</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/swfupload.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function doInit() {
  var lenght = parseInt($('count').value);
  for(var i = 0; i < lenght ; i++) {
    if($('attachmentName_' + i)) {
      attachMenuUtil("showAtt_" + i ,"work_plan",null,$('attachmentName_' + i).value ,$('attachmentId_' + i).value,true, i);
    }
  }
  doInit2();
}
function deleteId2(seqId,planId) {
  var msg = "确认要删除该进度日志吗?";
  if (window.confirm(msg)) {
  window.location = "<%=contextPath %>/yh/core/funcs/workplan/act/YHWorkDetailAct/deleteDetaiId.act?seqId=" + seqId + "&planId=" + planId;
  } 
}
function updateId(seqId) {
  window.location = "<%=contextPath %>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectId2.act?seqId=" + seqId;
}
function doInit2() {
  var lenght = parseInt($('count').value);
  for(var i = 0; i < lenght ; i++) {
    var writerCoun = "writerName" + i; 
    var writerCounVlue = document.getElementById + "(writerName" + i + ")";
    if(writerCounVlue.value != ""){
      bindDesc([{cntrlId:writerCoun, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<%
  YHWorkDetail detail = new YHWorkDetail();
  List<YHWorkDetail> list = (List<YHWorkDetail>)request.getAttribute("listId");
  YHWorkPlan plan = (YHWorkPlan)request.getAttribute("plan");
  if (list.size() > 0) {
%>
<table border="0" width="100%" cellspacing="0" cellpadding="3"
 class="small">
 <tr>
  <td class="Big"><img src="<%=imgPath%>/edit.gif" width="22"
   height="18">&nbsp;<span class="big3"> 进度日志详情(<%=plan.getName() %>&nbsp;&nbsp;<%if(plan.getStatrTime() != null){%><%=plan.getStatrTime() %><%} %>&nbsp;至&nbsp;<%if(plan.getEndTime() != null){%><%=plan.getEndTime() %><%} %>)</span></td>
 </tr>
</table>
<br>
<table class="TableList" width="95%" align="center">
 <tr class="TableHeader">
  <td nowrap align="center">作者</td>
  <td nowrap align="center">内容</td>
  <td nowrap align="center">附件</td>
  <td nowrap align="center">日志时间</td>
  <td nowrap align="center">进度百分比</td>
 </tr>
 <%for (int i = 0; i < list.size() ; i++) {
   detail = list.get(i);
   %>
 <tr class='TableLine1'>
  <td align="center"><%if(detail.getWriter()!=null) {%>
    <div id="writerName<%=i%>Desc"></div>
    <input type="hidden" name="writerName<%=i%>" id="writerName<%=i%>" value="<%=detail.getWriter()%>">
    <%
   }else {
 %>
 <div id="writerName<%=i%>Desc"></div>
    <input type="hidden" name="writerName<%=i%>" id="writerName<%=i%>" value="">
 <%} %>
  </td>
  <td align="center" width="20%"><%if(detail.getProgress()!=null) {%><%=detail.getProgress() %><%} %></td>
  <td align="center"><%if(detail.getAttachmentName()!=null) {%> 
  <input type="hidden" id="attachmentId_<%=i %>" name="attachmentId_<%=i %>" value="<%=detail.getAttachmentId()%>">
        <input type="hidden" id="attachmentName_<%=i %>" name="attachmentName_<%=i %>" value="<%=detail.getAttachmentName() %>">
        <input type="hidden" id="ensize" name="ensize">
        <span id="showAtt_<%=i %>"></span>
  <%}else {%>&nbsp;<%} %>
  </td>
  <td align="center"><%if(detail.getWriteTime()!=null) {%><%=detail.getWriteTime() %><%} %></td>
  <td align="center"><%=detail.getPercent() %>%</td>
 </tr>
 <%} %>
</table>
<%}else {%>
<table class="MessageBox" align="center" width="340">
<tr><td class='msg info'>
<div class='content' style='font-size:12pt'>进度日志已删除!
</div>
</td></tr>
</table>
<% } %>
<div align="center"><input type="button" class="BigButton" value="关闭" onClick="window.close();" title="关闭窗口"></div>

<form id="formFile" action="<%=contextPath %>/yh/core/funcs/workplan/act/YHWorkPlanHandleAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
  <input type='hidden' id=count name=count value="<%=list.size() %>"/>
  <input type="hidden" name="WRITE_TIME" id="WRITE_TIME" size="19" readonly maxlength="100" class="BigStatic" value="">
</body>
</html>