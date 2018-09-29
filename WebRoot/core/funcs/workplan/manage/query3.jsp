<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkPlan"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="yh.core.funcs.person.data.YHPerson" %>
<%
  String para = (String)request.getAttribute("paramValue");
  if (para != null) {
    para = para.replaceAll("\"","\\\\\"");
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作计划</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function workDelete(seqId) {
  var msg = "确认要删除该工作计划吗";
  if (window.confirm(msg)) {
    var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/deleteQuery3.act?seqId=" + seqId;
    window.location.href = url;
  }
  window.location.reload();
}
function updatePlu(seqId,pub) {
  var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/updateQuery3.act?seqId=" + seqId + "&pub=" + pub;
  window.location = url;
  window.location.reload();
}
function pdf() {
  var param = "<%=para%>";
  var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHExport/exportCsv.act?" + param;
  window.location = url;
}
function updatePlu2(seqId) {  
  var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/updatePlu2.act?seqId=" + seqId;
  window.location = url;
}
function myOpen(seqId) {
  window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetai.act?seqId=" + seqId,"myOpen","height=550,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=90,left=290,resizable=yes");
}
function myOpen2(seqId) {
  window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetai2.act?seqId=" + seqId,"myOpen2","status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=250,top=0,resizable=yes");
}
function workSelect2(seqId) {
  window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/workSelect2.act?seqId=" + seqId,"myOpen","height=650,width=700,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=90,left=290,resizable=yes");
}
function updatePlu4(seqId,pub,time) {
  var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/updatePlu4.act?seqId=" + seqId + "&pub=" + pub + "&time=" + time;
  window.location = url;
  window.location.reload();
}
function updatePlu8(seqId,pub,time) {
  var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/updatePlu8.act?seqId=" + seqId + "&pub=" + pub + "&time=" + time;
  window.location = url;
  window.location.reload();
}
function doInit() {
  var lenght = parseInt($('count').value);
  for(var i = 0; i < lenght ; i++) {
    if($('attachmentName_' + i)) {
      attachMenuUtil("showAtt_" + i ,"work_plan",null,$('attachmentName_' + i).value ,$('attachmentId_' + i).value,true,i);
    }
  }
  doInit3();
} 
function doInit3() {
  var lenght = parseInt($('count').value);
  for(var i = 0; i < lenght ; i++) {
    var numCount = "type" + i;
    var numCount3 = "leader2" + i;
    var numCount2 = "leader1" + i;
  
    var numCounVlue3 = document.getElementById + "(leader2" + i + ")";
    var numCounVlue2 = document.getElementById + "(leader1" + i + ")";
    var numCounVlue = document.getElementById + "(type" + i + ")";
    if(numCount.value != ""){
      bindDesc([{cntrlId:numCount, dsDef:"PLAN_TYPE,SEQ_ID,TYPE_NAME"}]);
    }
    if(numCount3.value != ""){
      bindDesc([{cntrlId:numCount3, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    if(numCount2.value != ""){
      bindDesc([{cntrlId:numCount2, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
  }
}
</script>
</head>
<body topmargin="5" onLoad="doInit()">
<%
  int len = 0;
  YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  List<YHWorkPlan> list = (List<YHWorkPlan>) request.getAttribute("worklist");
  YHWorkPlan plan = new YHWorkPlan();
  String pubType0 = "0";
  String pubType1 = "1";
  String pubType2 = "2";
  String pubType3 = "3";
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
  
  if (list.size() > 0) {
%>
<div>
<img src="<%=imgPath%>/edit.gif" width="18" height="18"><span class="big3"> 查询结果 </span>
</div>
<div align="center">
<span class="big3"> 共有<%=list.size() %>条记录 </span>
</div>
<table class="TableList" align="center" width="100%">
  <tr align='center' class='TableHeader'>
   <td width="7%">序号</td>
    <td width="13%">计划名称
    </td>
    <td  width="10%">起始时间
    </td>
    <td width="10%">结束时间</td>
    <td width="10%">计划类别
    </td>
    <td width="9%">负责人</td>
    <td width="9%">参与人</td>
    <td width="11%">附件</td>
    <td width="7%">状态</td>
    <td width="14%">操作</td>
  </tr>
  <%
    for (int i = 0; i < list.size(); i++) {
      boolean flag = false;
      boolean fla = false;
        plan = list.get(i);
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
        if (plan.getCreator().equals(String.valueOf(person.getSeqId()))) {
          flag = true;
        }
        if (String.valueOf(person.getSeqId()).equals("1")) {
          flag = true;
        }
        if (!YHUtility.isNullorEmpty(plan.getLeader2Desc())) {
          len = plan.getLeader2Desc().split(",").length;
          for (int j = 0 ; j < len; j++) {
            if (plan.getLeader2Desc().split(",")[j].equals(String.valueOf(person.getSeqId()))) {
              fla = true;
            }
          }
        }
  %>
  <tr align='center' class='TableLine1'>
    <td><%=plan.getSeqId()%></td>
    <td>
    <%
      if (plan.getName() != null) {
    %><a href='javascript:workSelect2(<%=plan.getSeqId()%>);'> <%=plan.getName()%></a>
    <%
      }
    %><input type='button' class='SmallButtonW' value='进度图' title='查看进度图'
      onClick='javascript:myOpen2(<%=plan.getSeqId()%>);'></td>
    <td>
    <%
      if (plan.getStatrTime() != null) {
    %><%=plan.getStatrTime()%> <%
   }
 %>
    </td>
    <td>
    <%
      if (plan.getEndTime() != null) {
    %><%=plan.getEndTime()%> <%
   }else {
 %>
 &nbsp;
 <%} %>
    </td>
    <td>
    <%
      if (plan.getType() != null) {
    %>
    <div id="type<%=i%>Desc"></div>
    <input type="hidden" name="type<%=i%>" id="type<%=i%>" value="<%=plan.getType()%>">
    <%
   }else {
     %>
     <div id="type<%=i%>Desc"></div>
        <input type="hidden" name="type<%=i%>" id="type<%=i%>" value="">
     <%} %>
    </td>
     <td>
    <%if (plan.getLeader2Desc() != null) {%>
    <div id="leader2<%=i%>Desc"></div>
    <input type="hidden" name="leader2<%=i%>" id="leader2<%=i%>" value="<%=plan.getLeader2Desc()%>">
    <%}else {%>
    <div id="leader2<%=i%>Desc"></div>
    <input type="hidden" name="leader2<%=i%>" id="leader2<%=i%>" value="">
    <%}%>
    </td> 
    
    <td>
    <%if (plan.getLeader1Desc() != null) {%>
    <div id="leader1<%=i%>Desc"></div>
    <input type="hidden" name="leader1<%=i%>" id="leader1<%=i%>" value="<%=plan.getLeader1Desc()%>">
    <%}else {%>
    <div id="leader1<%=i%>Desc"></div>
    <input type="hidden" name="leader1<%=i%>" id="leader1<%=i%>" value="">
    <%}%>
    </td>
    <td>
    <%
      if (YHUtility.isNullorEmpty(plan.getAttachmentname())) {
    %>无<%
      } else {
    %>
        <input type="hidden" id="attachmentId_<%=i %>" name="attachmentId" value="<%=plan.getAttachmentid()%>">
        <input type="hidden" id="attachmentName_<%=i %>" name="attachmentName" value="<%=plan.getAttachmentname() %>">
        <input type="hidden" id="ensize" name="ensize">
        <span id="showAtt_<%=i %>"></span>
        <%
   }
 %>
    </td>
    <%if (plan.getEndTime() == null) {
      if (plan.getPublish().equals("0")) {
    %>
    <td><font color='#FF0000'><b>未发布</b></font></td>
    <td>
    <%if(flag){ %>
    <a
      href='javascript:updatePlu2(<%=plan.getSeqId()%>);'>修改</a>&nbsp; <a
      href="javascript:workDelete('<%=plan.getSeqId()%>');">删除</a>&nbsp; <a
      href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType1%>','');">生效</a>
    <%}if(fla && !flag){%>
     <a href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType1%>','');">生效</a>
    <%} %>&nbsp;
    </td>
      <%} %>
    <%
      if (plan.getPublish().equals("1")  && (beginTime1 < endTime1 || (beginTime1 == endTime1 && beginTime2 < endTime2) || (beginTime1 == endTime1 && beginTime2 == endTime2 && beginTime3 <= endTime3))) {
    %>
    <td><font color='#00AA00'><b>进行中</b></font></td>
    <td>
    <%if(flag){ %>
    <a
      href='javascript:updatePlu2(<%=plan.getSeqId()%>);'>修改</a>&nbsp; <a
      href="javascript:workDelete(<%=plan.getSeqId()%>)">删除</a>&nbsp; <a
      href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu('<%=plan.getSeqId()%>','<%=pubType2%>');">暂停</a>&nbsp;
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType3%>','<%=time %>');">结束</a>&nbsp;
     <%}if(fla && !flag){%>
      <a href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu('<%=plan.getSeqId()%>','<%=pubType2%>');">暂停</a>&nbsp;
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType3%>','<%=time %>');">结束</a>&nbsp;
    <%} %>&nbsp;
    </td>
     <%
      } else {if(plan.getPublish().equals("1")){
    %>
     <td><font><b>未开始</b></font></td>
    <td>
    <%if(flag){%>
    <a
      href='javascript:updatePlu2(<%=plan.getSeqId()%>);'>修改</a>&nbsp; <a
      href="javascript:workDelete(<%=plan.getSeqId()%>)">删除</a>&nbsp; <a
      href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
   <a href="javascript:updatePlu8('<%=plan.getSeqId()%>','<%=pubType1%>','<%=time %>');">生效</a>
    <%} if (fla && !flag) {%>
    <a href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu('<%=plan.getSeqId()%>','<%=pubType2%>');">暂停</a>&nbsp;
   <a href="javascript:updatePlu8('<%=plan.getSeqId()%>','<%=pubType1%>','<%=time %>');">生效</a>
    <%} %>
    </td>
    <%}}%>
    
    <%
      if (plan.getPublish().equals("2")) {
    %>
    <td><font color='#FF0000'><b>暂停</b></font></td>
    <td>
    <%if(flag){ %>
    <a
      href='javascript:updatePlu2(<%=plan.getSeqId()%>);'>修改</a>&nbsp; <a
      href="javascript:workDelete(<%=plan.getSeqId()%>)">删除</a>&nbsp; <a
      href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu('<%=plan.getSeqId()%>','<%=pubType1%>');">继续</a>&nbsp;
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType3%>','<%=time %>');">结束</a>
    <%}if(fla && !flag){%>
    <a href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu('<%=plan.getSeqId()%>','<%=pubType1%>');">继续</a>&nbsp;
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType3%>','<%=time %>');">结束</a>
    <%} %>&nbsp;
    </td>
    <%
      }
    %>
    <%
      if (plan.getPublish().equals("3")) {
    %>
    <td><font color='#FF0000'><b>已结束</b></font></td>
    <td>
    <%if(flag){ %>
    <a
      href='javascript:updatePlu2(<%=plan.getSeqId()%>);'>修改</a>&nbsp; <a
      href="javascript:workDelete(<%=plan.getSeqId()%>)">删除</a>&nbsp; <a
      href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType1%>','');">生效</a>
    <%}if(fla && !flag){%>
     <a href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType1%>','');">生效</a>
    <%} %>&nbsp;
    </td>
    <%}} else { 
      if (time1 < endTime1 || (time1 == endTime1 && time2 < endTime2) || (time1 == endTime1 && time2 == endTime2 && time3 < endTime3) ) {
    %>
    <td><font color='#FF0000'><b>已结束</b></font></td>
    <td>
     <%if(flag){ %>
    <a
      href='javascript:updatePlu2(<%=plan.getSeqId()%>);'>修改</a>&nbsp; <a
      href="javascript:workDelete(<%=plan.getSeqId()%>)">删除</a>&nbsp; <a
      href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType1%>','');">生效</a>
 <%}if(fla && !flag){%>
     <a href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType1%>','');">生效</a>
<%} %>&nbsp;
    </td>
      <%} else {
        if (plan.getPublish().equals("0")) {
      %>
    <td><font color='#FF0000'><b>未发布</b></font></td>
    <td>
     <%if(flag){ %>
    <a
      href='javascript:updatePlu2(<%=plan.getSeqId()%>);'>修改</a>&nbsp; <a
      href="javascript:workDelete('<%=plan.getSeqId()%>');">删除</a>&nbsp; <a
      href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType1%>','');">生效</a>
     <%}if(fla && !flag){%>
     <a href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType1%>','');">生效</a>
    <%} %>&nbsp;
    </td>
      <%} %>
     <%
      if (plan.getPublish().equals("1")  && (beginTime1 < endTime1 || (beginTime1 == endTime1 && beginTime2 < endTime2) || (beginTime1 == endTime1 && beginTime2 == endTime2 && beginTime3 <= endTime3))) {
    %>
    <td><font color='#00AA00'><b>进行中</b></font></td>
    <td>
    <%if(flag){ %>
    <a
      href='javascript:updatePlu2(<%=plan.getSeqId()%>);'>修改</a>&nbsp; <a
      href="javascript:workDelete(<%=plan.getSeqId()%>)">删除</a>&nbsp; <a
      href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu('<%=plan.getSeqId()%>','<%=pubType2%>');">暂停</a>&nbsp;
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType3%>','<%=time %>');">结束</a>&nbsp;
     <%}if(fla && !flag){%>
      <a href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu('<%=plan.getSeqId()%>','<%=pubType2%>');">暂停</a>&nbsp;
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType3%>','<%=time %>');">结束</a>&nbsp;
    <%} %>&nbsp;
    </td>
     <%
      } else {if(plan.getPublish().equals("1")){
    %>
     <td><font><b>未开始</b></font></td>
    <td>
    <%if(flag){%>
    <a
      href='javascript:updatePlu2(<%=plan.getSeqId()%>);'>修改</a>&nbsp; <a
      href="javascript:workDelete(<%=plan.getSeqId()%>)">删除</a>&nbsp; <a
      href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
   <a href="javascript:updatePlu8('<%=plan.getSeqId()%>','<%=pubType1%>','<%=time %>');">生效</a>
    <%} if (fla && !flag) {%>
    <a href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu('<%=plan.getSeqId()%>','<%=pubType2%>');">暂停</a>&nbsp;
   <a href="javascript:updatePlu8('<%=plan.getSeqId()%>','<%=pubType1%>','<%=time %>');">生效</a>
    <%} %>
    </td>
    <%}}%>
    
    <%
      if (plan.getPublish().equals("2")) {
    %>
    <td><font color='#FF0000'><b>暂停</b></font></td>
    <td>
    <%if(flag){ %>
    <a
      href='javascript:updatePlu2(<%=plan.getSeqId()%>);'>修改</a>&nbsp; <a
      href="javascript:workDelete(<%=plan.getSeqId()%>)">删除</a>&nbsp; <a
      href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu('<%=plan.getSeqId()%>','<%=pubType1%>');">继续</a>&nbsp;
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType3%>','<%=time %>');">结束</a>
     <%}if(fla && !flag){%>
      <a href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu('<%=plan.getSeqId()%>','<%=pubType1%>');">继续</a>&nbsp;
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType3%>','<%=time %>');">结束</a>
    <%} %>&nbsp;
    </td>
    <%
      }
    %>
    <%
      if (plan.getPublish().equals("3")) {
    %>
    <td><font color='#FF0000'><b>已结束</b></font></td>
    <td>
    <%if(flag){ %>
    <a
      href='javascript:updatePlu2(<%=plan.getSeqId()%>);'>修改</a>&nbsp; <a
      href="javascript:workDelete(<%=plan.getSeqId()%>)">删除</a>&nbsp; <a
      href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType1%>','');">生效</a>
    <%}if(fla && !flag){%>
    <a href='javascript:myOpen(<%=plan.getSeqId()%>);'>批注</a>&nbsp; <br>
    <a href="javascript:updatePlu4('<%=plan.getSeqId()%>','<%=pubType1%>','');">生效</a>
    <%} %>&nbsp;
    </td>
   <%}}}%>
  </tr>
  <%
    }
  %>
  <tr class="TableControl">
    <td colspan="10" align="center">
    <input type='button' class='BigButton' value='导出' onClick='pdf();' title='导出' ><input type='button' class='BigButton' value='返回' onClick='javascript:history.back();' title='返回' >
    </td>
  </tr>
</table>
<%
  } else {
%>
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>无符合条件的工作计划</div>
    </td>
  </tr>
</table>
<div align="center"><input type="button" value=" 返回 " class="BigButton" onClick="javascript:history.back();"></div>
<%
  }
%>
<input type='hidden' id='count' name='count' value="<%=list.size() %>"/>
</body>
</html>