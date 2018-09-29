<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>安排查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function checkForm(){
  var sendTimeMin = document.getElementById("sendTimeMin");
  var sendTimeMax = document.getElementById("sendTimeMax");
  if(sendTimeMin.value!=""){
    if(!isValidDateStr(sendTimeMin.value)){
      alert("日期的格式不对，应形如 2010-02-01");
      document.getElementById("sendTimeMin").focus();
      document.getElementById("sendTimeMin").select();
      return false;
    }
  }
  if(sendTimeMax.value!=""){
    if(!isValidDateStr(sendTimeMax.value)){
      alert("日期的格式不对，应形如 2010-02-01");
      document.getElementById("sendTimeMax").focus();
      document.getElementById("sendTimeMax").select();
      return false;
    }
  }
  if(sendTimeMin.value!=""&&sendTimeMax.value!=""){
    if(compareDate(sendTimeMin , sendTimeMax)){
      alert("起始日期不能大于结束日期!");
      sendTimeMax.focus();
      sendTimeMax.select();
      return false;
    }  
  } 
  return true;
}
function compareDate(beginDate , endDate) {
  d1Arr=beginDate.value.split('-');
  d2Arr=endDate.value.split('-');
  v1=new Date(d1Arr[0],d1Arr[1],d1Arr[2]);
  v2=new Date(d2Arr[0],d2Arr[1],d2Arr[2]);
  return v1>v2;
}
function cal_export(){
  if(checkForm()){
    $("type").value="1";
    document.form1.target='_blank';
    document.form1.submit();
  }
  
}
function query(){
  if(checkForm()){
    $("type").value="";
    document.form1.target='_self';
    document.form1.submit();
  }
}
function returnBefore(){
  window.location.href="<%=contextPath%>/core/funcs/calendar/list.jsp";
}
function doOnload(){
  var date1Parameters = {
      inputId:'sendTimeMin',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'sendTimeMax',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3"> 日程安排查询</span>
    </td>
  </tr>
</table>
<br>
<form action="<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/selectCalendarByTerm.act"  method="post" name="form1" onsubmit="return checkForm();">
 <table  class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData" width="100"> 日期：</td>
      <td class="TableData">
        <input type="text" id="sendTimeMin" name="sendTimeMin" size="12" maxlength="10" class="BigInput" value="">
        <img id="date1" align="absMiddle" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" > 至&nbsp;
        <input type="text" id="sendTimeMax" name="sendTimeMax" size="12" maxlength="10" class="BigInput" value="">
        <img id="date2" align="absMiddle" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 优先程度：</td>
      <td class="TableData">
        <select id="calLevel" name="calLevel" class="BigSelect">
          <option value="">所有</option>
          <option value="0">未指定</option>
          <option value="1">重要/紧急</option>
          <option value="2">重要/不紧急</option>
          <option value="3">不重要/紧急</option>
          <option value="4">不重要/不紧急</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 状态：</td>
      <td class="TableData">
        <select id="overStatus" name="overStatus" class="BigSelect">
          <option value="0">所有</option>
          <option value="1">未开始</option>
          <option value="2">进行中</option>
          <option value="3">已超时</option>
          <option value="4">已完成</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 事务类型：</td>
      <td class="TableData">
        <select id="calType" name="calType" class="BigSelect">
          <option value="">所有</option>
          <option value="1">工作事务</option>
          <option value="2">个人事务</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 事务内容：</td>
      <td class="TableData">
        <input id="content" name="content" size="33" class="BigInput">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="查询" class="BigButton" onclick="query();">&nbsp;&nbsp;
           <input type="hidden" value="" id="type" name="type" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="导出/打印" class="BigButton" onclick="cal_export();">
        <input type="button" class="BigButton" value="返回" onclick=" history.go(-1);"> 
      </td>
    </tr>
  </table>
</form>
</body>
</html>