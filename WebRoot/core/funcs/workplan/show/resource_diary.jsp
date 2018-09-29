<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkPlan"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkPerson"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>计划任务详情</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function doInit() {
  doInit2();
  var lenght = parseInt($('count').value);
  for(var i = 0; i < lenght ; i++) {
    if($('attachmentName_' + i)) {
      attachMenuUtil("showAtt_" + i ,"work_plan",null,$('attachmentName_' + i).value ,$('attachmentId_' + i).value,true, i);
    }
  }
}
function doInit2(){
  if(document.getElementById("nameId").value != ""){
    bindDesc([{cntrlId:"nameId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("nameId2").value != ""){
    bindDesc([{cntrlId:"nameId2", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
 }

function InsertImage(src) { 
  return;
}
</script>
</head>
<%
YHWorkPerson person = new YHWorkPerson();
List<YHWorkPerson> list = (List<YHWorkPerson>)request.getAttribute("person");
YHWorkPlan plan = (YHWorkPlan)request.getAttribute("plan");
String name = (String)request.getAttribute("name");
if (list.size() > 0) {
%>
<body topmargin="5" onLoad="doInit()">
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3"
 class="small">
 <tr>
  <td class="Big"><img src="<%=imgPath%>/edit.gif" width="22"
   height="18" >&nbsp;<span class="big3"><span id="nameIdDesc"><input type="hidden" name="nameId" id="nameId" value="<%=request.getAttribute("name")%>"></span>&nbsp;－&nbsp;计划任务&nbsp;－
  &nbsp;<%=plan.getName() %>&nbsp;(&nbsp;<%=plan.getStatrTime() %>&nbsp;至&nbsp; <%if(plan.getEndTime() != null){%><%=plan.getEndTime() %><%} %>&nbsp;)</span></td>
 </tr>
</table>
<br>
<br>
<table class="TableList" width="95%" align="center">
 <tr class="TableHeader">
  <td nowrap align="center" width="15%">开始时间</td>
  <td nowrap align="center" width="15%">结束时间</td>
  <td nowrap align="center" width="30%">计划任务</td>
  <td nowrap align="center">附件</td>
  <td nowrap align="center" width="30%">相关资源</td>
 </tr>
 <%for (int i = 0 ; i < list.size() ; i++) {
    person = list.get(i);
 %>
 <tr class="TableLine1">
  <td align="center"><%if (person.getPbegeiDate() != null){%><%=person.getPbegeiDate() %><%} %></td>
  <td align="center"><%if (person.getPendDate() != null){%><%=person.getPendDate() %><%} %></td>
  <td align="center"><%if (person.getPplanContent() != null){%><%=person.getPplanContent() %><%} %></td>
  <td align="center"><%if (person.getAttachmentName() != null){%>
   <input type="hidden" id="attachmentId_<%=i %>" name="attachmentId_<%=i %>" value="<%=person.getAttachmentId()%>">
   <input type="hidden" id="attachmentName_<%=i %>" name="attachmentName_<%=i %>" value="<%=person.getAttachmentName()%>">
   <input type="hidden" id="ensize" name="ensize">
   <span id="showAtt_<%=i %>"></span>
  <%} %></td>
  <td align="center"><%if (person.getPuseResource() != null){%><%=person.getPuseResource() %><%} %></td>
 </tr>
 <%}  %>
</table>
<%} else { %>
<table class="MessageBox" align="center" width="340">
<tr><td class='msg info'>
<div class='content' style='font-size:12pt'>计划任务已经删除!</div>
</td></tr></table>
<%} %>
<div align="center"><input type="button" value=" 关闭 " class="BigButton" onClick="window.close();"></div>
<input type="hidden" id="nameId" name="nameId" value="">
<input type="hidden" id="nameId2" name="nameId2" value="">
<input type='hidden' id=count name=count value="<%=list.size()%>"/>
<input type="hidden" id="moduel" name="moduel" value="work_plan">
</body>
</html>