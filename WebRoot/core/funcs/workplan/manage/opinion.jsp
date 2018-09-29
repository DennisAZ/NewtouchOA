<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="yh.core.funcs.workplan.data.YHWorkDetail"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkPlan"%>
<%@page import="yh.core.funcs.person.data.YHPerson" %>
<html>
<head>
<title>添加批注</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
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
  List<YHWorkDetail> list = (List<YHWorkDetail>)request.getAttribute("list");
  YHWorkPlan plan = (YHWorkPlan)request.getAttribute("plan");
  if (list.size() > 0) {
%>
<table class="small" width="100%" cellspacing="0"  cellpadding="3">
<tr align='center' class='TableHeader'><tr>
<td class='Big'><img src='<%=imgPath%>/form.gif' width='22' height='18'>&nbsp;
<span class='big3'> 领导批注记录 (<%=plan.getName() %>&nbsp;&nbsp;<%if(plan.getStatrTime() != null){%><%=plan.getStatrTime() %><%} %>&nbsp;至&nbsp;<%if(plan.getEndTime() != null){%><%=plan.getEndTime() %><%} %>)</span></td>
</tr></table><br>
<table class="TableList"  align="center" width="100%">
<tr align='center' class='TableHeader'><td>批注领导</td>
<td width='120'>批注内容</td>
<td>附件</td>
<td width='120'>批注时间</td></tr>
 <% for (int i = 0; i < list.size(); i++) {
    detail = list.get(i);
 %>
<tr align="center" class="TableLine1" title="批注">
<td><%if(detail.getWriter() != null){%>
    <div id="writerName<%=i%>Desc"></div>
    <input type="hidden" name="writerName<%=i%>" id="writerName<%=i%>" value="<%=detail.getWriter()%>">
    <%
   }else {
 %>
 <div id="writerName<%=i%>Desc"></div>
    <input type="hidden" name="writerName<%=i%>" id="writerName<%=i%>" value="">
 <%} %>
</td>
<td><%if(detail.getProgress() != null){%><%=detail.getProgress() %><%} %></td>
<td><%if(detail.getAttachmentName() != null){%>
<input type="hidden" id="attachmentId_<%=i %>" name="attachmentId_<%=i %>" value="<%=detail.getAttachmentId() %>">
        <input type="hidden" id="attachmentName_<%=i %>" name="attachmentName_<%=i %>" value="<%=detail.getAttachmentName()  %>">
        <input type="hidden" id="ensize" name="ensize">
        <span id="showAtt_<%=i %>"></span>
<%} %></td>
<td align='center'><%if(detail.getWriteTime() != null){%><%=detail.getWriteTime() %><%} %></td></tr>
<%} %>
 </table>
 <div id="writerName<%=0%>Desc" style="display:none"></div>
<input type="hidden" name="writerName<%=0%>" id="writerName<%=0%>" value="<%=detail.getWriter() %>">
 <%} if (list.size() > 0) {%>
 <% for (int i = 0; i < list.size(); i++) {
    detail = list.get(i);
 %>
    <div id="writerName<%=i%>Desc" style="display:none"></div>
    <input type="hidden" name="writerName<%=i%>" id="writerName<%=i%>" value="<%=detail.getWriter()%>">
 <%}}if (list.size() <= 0) {%>
<table class="MessageBox" align="center" width="340">
<tr><td class='msg info'>
<div class='content' style='font-size:12pt'>批注内容已经删除!</div>
</td></tr></table>
<%} %>
<div align="center"><input type='button' class='BigButton' value='关闭' onClick='window.close();' title='关闭窗口'></div>
<input type='hidden' id=count name=count value="<%=list.size()%>"/>
</body>
</html>