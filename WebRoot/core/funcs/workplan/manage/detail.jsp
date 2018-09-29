<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="yh.core.funcs.workplan.data.YHWorkPlan"%>
<%@page import="java.text.SimpleDateFormat"%>
<html>
<head>
<title>工作计划详情</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
  function myOpen(seqid) {
    window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetai.act?seqId=" + seqid,"myNewOpen","height=550,width=800,top=90,left=290,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=1");
  }
  function myOpen2(seqid) {
    window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetai2.act?seqId=" + seqid,"myOpen2","status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=250,top=0,resizable=yes");
  }
  function myOpen3(seqId) {
    window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetaiId.act?seqId=" + seqId,"myOpen3","height=550,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=90,left=290,resizable=yes");
  }
  function doInit() {
    attachMenuUtil("showAtt","work_plan",null,$('attachmentName').value ,$('attachmentId').value,true);
    doInit3();
  }
  function doInit3(){
    if(document.getElementById("leader3").value.trim() != ""){
      bindDesc([{cntrlId:"leader3", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    if(document.getElementById("leader1").value.trim() != ""){
      bindDesc([{cntrlId:"leader1", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    if(document.getElementById("leader2").value.trim() != ""){
      bindDesc([{cntrlId:"leader2", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    if(document.getElementById("manager").value.trim() != ""){
      bindDesc([{cntrlId:"manager", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    if(document.getElementById("dept").value.trim() != "" && document.getElementById("dept").value != "0" && document.getElementById("dept").value != "ALL_DEPT"){
      bindDesc([{cntrlId:"dept", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
    }
    if(document.getElementById("creator").value.trim() != ""){
      bindDesc([{cntrlId:"creator", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    if(document.getElementById("type") && document.getElementById("type").value.trim() != ""){
      bindDesc([{cntrlId:"type", dsDef:"PLAN_TYPE,SEQ_ID,TYPE_NAME"}]);
    }
   }
</script>
</head>
<body  topmargin="5" onLoad="doInit()">
<%
YHWorkPlan plan = (YHWorkPlan) request.getAttribute("planData");
Date date = new Date();
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
String time = sf.format(date);
String year = sf.format(date).substring(0,4);
String mod = sf.format(date).substring(5,7);
String day = sf.format(date).substring(8,10);

int endTime1 = Integer.parseInt(year);//当前时间
int endTime2 = Integer.parseInt(mod);
int endTime3 = Integer.parseInt(day);

String eTime = null;
String eTime2 = null;
String eTime3 = null;

String begin1 = null;
String begin2 = null;
String begin3 = null;

int time1 = 0;//结束时间
int time2 = 0;
int time3 = 0;

int beginTime1 = 0;
int beginTime2 = 0;
int beginTime3 = 0;

if (plan != null) {
  if (plan.getEndTime() != null) {
    eTime = String.valueOf(plan.getEndTime()).substring(0,4);
    eTime2 = String.valueOf(plan.getEndTime()).substring(5,7);
    eTime3 = String.valueOf(plan.getEndTime()).substring(8,10);
    time1 = Integer.parseInt(eTime);
    time2 = Integer.parseInt(eTime2);
    time3 = Integer.parseInt(eTime3);
  }
  if (plan.getStatrTime() != null) {
    begin1 = String.valueOf(plan.getStatrTime()).substring(0,4);
    begin2 = String.valueOf(plan.getStatrTime()).substring(5,7);
    begin3 = String.valueOf(plan.getStatrTime()).substring(8,10);
    beginTime1 = Integer.parseInt(begin1);
    beginTime2 = Integer.parseInt(begin2);
    beginTime3 = Integer.parseInt(begin3);
  }
%>
<table border="0" width="100%" cellspacing="0" cellpadding="3"
  class="small">
  <tr>
    <td class="Big"><img
      src="<%=imgPath%>/work_plan.gif" WIDTH="22"
      HEIGHT="20" align="absmiddle">&nbsp;<span class="big3"> 工作计划详情
    - <%=plan.getName()%></span><br>
    </td>
  </tr>
</table>

<table class="TableBlock" width="100%">
  <tr>
    <td align="center" class="TableContent" width="80">计划名称</td>
    <td align="left" class="TableData"><%=plan.getName()%></td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">计划内容</td>
    <td align="left" class="TableData Content"
      style="word-break: break-all">
    <%
      if (plan.getContent() != null) {
    %><%=plan.getContent()%> <%
   }else{
 %>
 &nbsp;
 <%} %>
    </td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">开始时间</td>
    <td align="left" class="TableData"><%=plan.getStatrTime()%></td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">结束时间</td>
    <td align="left" class="TableData"><%
      if (plan.getEndTime() != null) {
    %><%=plan.getEndTime()%><%
   }
 %>&nbsp;</td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">计划类别</td>
    <td align="left" class="TableData" id="typeDesc">
    <%
      if (plan.getType() != null) {
    %>
    <input type="hidden" name="type" id="type" value="<%=plan.getType()%>">
     <%
   }
 %>
    </td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">开放部门</td>
    <td align="left" class="TableData"  id="deptDesc">
    <%
      if (plan.getDeptParentDesc() != null) {
    %>
    <input type="hidden" name="deptParent" id="dept" value="<%=plan.getDeptParentDesc()%>">
    <%if (plan.getDeptParentDesc().equals("0") || plan.getDeptParentDesc().equals("ALL_DEPT")){%>全体部门<%} %>
     <%
   }else {
 %>
     <input type="hidden" name="deptParent" id="dept" value=""> 
     <%} %>
    </td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">开放人员</td>
    <td align="left" class="TableData" id="managerDesc">
    <%
      if (plan.getManagerDesc() != null) {
    %>
    <input type="hidden" name="manager" id="manager" value="<%=plan.getManagerDesc()%>"> 
    <%
   }else {
 %>
 <input type="hidden" name="manager" id="manager" value=""> 
 <%} %>
    </td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">负责人</td>
    <td align="left" class="TableData" id="leader2Desc">
    <%
      if (plan.getLeader2Desc() != null) {
    %>
    <input type="hidden" name="leader2" id="leader2" value="<%=plan.getLeader2Desc() %>">
    <%
   }else {
     %>
     <input type="hidden" name="leader2" id="leader2" value="">
     <%} %>
    </td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">参与人</td>
    <td align="left" class="TableData" id="leader1Desc">
    <%
      if (plan.getLeader1Desc() != null) {
    %><input type="hidden" name="leader1" id="leader1" value="<%=plan.getLeader1Desc()%>">
    <%
   }else {
     %>
     <input type="hidden" name="leader1" id="leader1" value="">
     <%} %>
    </td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80" id="">创建人</td>
    <td align="left" class="TableData" id="creatorDesc">
    <%
      if (plan.getCreator() != null) {
    %>
    <input type="hidden" name="creator" id="creator" value="<%=plan.getCreator() %>">
    <%
   }else {
     %>
     <input type="hidden" name="creator" id="creator" value="">
     <%} %>
    </td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">批注领导</td>
    <td align="left" class="TableData" id="leader3Desc">
    <%
      if (plan.getLeader3Desc() != null) {
    %>
    <input type="hidden" name="leader3" id="leader3" value="<%=plan.getLeader3Desc()%>"> 
     <%
   }else {
     %>
     <input type="hidden" name="leader3" id="leader3" value=""> 
     <%} %>
    </td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">创建日期</td>
    <td align="left" class="TableData">
    <%
      if (plan.getCreatedate() != null) {
    %><%=plan.getCreatedate()%> <%
   }
 %>
    </td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">进度详情</td>
    <td align="left" class="TableData">
    <a href="javascript:myOpen3('<%=plan.getSeqId()%>')" ; title="查看进度日志">查看进度日志</a>
    <a href="javascript:myOpen2('<%=plan.getSeqId()%>')" ; title="查看进度图">查看进度图</a></td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">领导批注</td>
    <td align="left" class="TableData"><a
      href="javascript:myOpen('<%=plan.getSeqId()%>')">查看领导批注</a></td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">附件文件</td>
    <td align="left" class="TableData">
    <%
      if (plan.getAttachmentname() != null) {
    %><input type="hidden" id="attachmentId" name="attachmentId" value="<%=plan.getAttachmentid()%>">
        <input type="hidden" id="attachmentName" name="attachmentName" value="<%=plan.getAttachmentname() %>">
        <input type="hidden" id="ensize" name="ensize">
        <span id="showAtt"></span>
        <%
   } else {
 %>无<%
   }
 %>
 <div style="display:none">
        <input type="hidden" id="attachmentId" name="attachmentId" value="<%=plan.getAttachmentid()%>">
        <input type="hidden" id="attachmentName" name="attachmentName" value="<%=plan.getAttachmentname() %>">
        <input type="hidden" id="ensize" name="ensize">
        <span id="showAtt"></span>
        </div>
    </td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">附件说明</td>
    <td align="left" class="TableData">
    <%
      if (!YHUtility.isNullorEmpty(plan.getAttachmentcomment())) {
    %><%=plan.getAttachmentcomment()%>
    <%
      }
    %>&nbsp;
    </td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">备注</td>
    <td align="left" class="TableData">
    <%
      if (!YHUtility.isNullorEmpty(plan.getRemark())) {
    %><%=plan.getRemark()%> <%
   }else {
 %>
&nbsp;
 <%} %>
    </td>
  </tr>
  <tr>
    <td align="center" class="TableContent" width="80">状态</td>
    <td align="left" class="TableData">
    <%
      if (plan.getPublish().equals("0")) {
    %> <font color='#FF0000'><b>未发布</b></font>
    <%
      }
    %> <%
   if (!plan.getPublish().equals("2") && (plan.getEndTime() != null) && (time1 > endTime1 || (time1 == endTime1 && time2 > endTime2) || (time1 == endTime1 && time2 == endTime2 && time3 > endTime3)) && (beginTime1 < endTime1 || (beginTime1 == endTime1 && beginTime2 < endTime2) || (beginTime1 == endTime1 && beginTime2 == endTime2 && beginTime3 <= endTime3))) {
 %> <font color='#00AA00'><b>进行中</b></font>
 <%
   }if (!plan.getPublish().equals("2") && (plan.getEndTime() == null) && (beginTime1 < endTime1 || (beginTime1 == endTime1 && beginTime2 < endTime2) || (beginTime1 == endTime1 && beginTime2 == endTime2 && beginTime3 <= endTime3))) {
 %> <font color='#00AA00'><b>进行中</b></font>
    <%}if(plan.getPublish().equals("1") && (beginTime1 > endTime1 || (beginTime1 == endTime1 && beginTime2 > endTime2) || (beginTime1 == endTime1 && beginTime2 == endTime2 && beginTime3 > endTime3))){%>
<font><b>未开始</b></font>
          <%} %>
          
     <%
   if (plan.getPublish().equals("2")) {
 %> <font color='#FF0000'><b>暂停</b></font>
    <%
      }
    %> <%if(plan.getEndTime() != null) {
   if (!plan.getPublish().equals("0") && (plan.getPublish().equals("3") || (time1 < endTime1 || (time1 == endTime1 && time2 < endTime2) || (time1 == endTime1 && time2 == endTime2 && time3 <= endTime3)))) {
 %> <font color='#FF0000'><b>已结束</b></font>
    <%
      }}
    %>
    
  </tr>
  <tr class="TableControl">
    <td colspan="9" align="center"><input type="button" title="收藏当前页" value="收藏本页"
      class="BigButton"
      onClick="javascript:window.external.AddFavorite('http://localhost<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/workSelect2.act?seqId=<%=plan.getSeqId()%>','工作计划详情');">&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" value="打印" class="BigButton"
      onClick="document.execCommand('Print');" title="直接打印表格页面">&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" value="关闭" class="BigButton"
      onClick="window.close();" title="关闭窗口"></td>
  </tr>
</table>
<%
  }else{
%>
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>工作计划已被删除!</div>
    <input type="hidden" id="attachmentId" name="attachmentId" value="">
        <input type="hidden" id="attachmentName" name="attachmentName" value="">
         <input type="hidden" id="leader3" name="leader3" value="">
          <input type="hidden" id="leader2" name="leader2" value="">
           <input type="hidden" id="leader1" name="leader1" value="">
            <input type="hidden" id="manager" name="manager" value="">
             <input type="hidden" id="type" name="type" value="">
              <input type="hidden" id="creator" name="creator" value="">
              <input type="hidden" id="dept" name="dept" value="">
    </td>
  </tr>
</table>
<div align="center"><input type="button"
  value="关闭" class="BigButton" onClick="window.close();"></div>
<%} %>
</body>
</html>