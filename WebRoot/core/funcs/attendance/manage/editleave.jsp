<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>修改请假记录</title>
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
  if(document.getElementById("leaveType").value==""){ 
    alert("请假原因不能为空！");
    document.getElementById("leaveType").focus();
    document.getElementById("leaveType").select();
    return (false);
  }
  if(document.getElementById("leaveDate1").value==""){ 
    alert("请假开始时间不能为空！");
    document.getElementById("leaveDate1").focus();
    document.getElementById("leaveDate1").select();
    return (false);
  }
  if(document.getElementById("leaveDate2").value==""){ 
    alert("请假结束时间不能为空！");
    document.getElementById("leaveDate2").focus();
    document.getElementById("leaveDate2").select();
    return (false);
  }
  if(document.getElementById("destroyTime").value==""){ 
    alert("销假时间不能为空！");
    document.getElementById("destroyTime").focus();
    document.getElementById("destroyTime").select();
    return (false);
  }
  var leaveDate1 = document.getElementById("leaveDate1");
  var leaveDate2 = document.getElementById("leaveDate2");
  var destroyTime = document.getElementById("destroyTime");
  var leaveDate1Array  = leaveDate1.value.trim().split(" ");
  var leaveDate2Array  = leaveDate2.value.trim().split(" ");
  var destroyTimeArray = destroyTime.value.trim().split(" ");
  var type1 =   "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　;
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2);
  if(leaveDate1Array.length!=2){
    alert("请假开始时间格式不对，应形如 1999-01-01 12:12:12");
    leaveDate1.focus();
    leaveDate1.select();
    return false;
  }else{
    if(!isValidDateStr(leaveDate1Array[0])||leaveDate1Array[1].match(re1) == null || leaveDate1Array[1].match(re2) != null){
      alert("请假开始时间格式不对，应形如 1999-01-01 12:12:12");
      leaveDate1.focus();
      leaveDate1.select();
      return false;
    }
  }
  if(leaveDate2Array.length!=2){
    alert("请假结束时间格式不对，应形如 1999-01-01 12:12:12");
    leaveDate1.focus();
    leaveDate1.select();
    return false;
  }else{
    if(!isValidDateStr(leaveDate2Array[0])||leaveDate2Array[1].match(re1) == null || leaveDate2Array[1].match(re2) != null){
      alert("请假结束时间格式不对，应形如 1999-01-01 12:12:12");
      leaveDate2.focus();
      leaveDate2.select();
      return false;
    }
  }
  if(document.getElementById("leaveDate1").value >= document.getElementById("leaveDate2").value){ 
    alert("请假结束时间要晚于请假开始时间！");
    return (false);
  }
  var IsInt = "^[0-9]*[1-9][0-9]*$";//正整数　
  var IsInt2 =   "^-?\\d+$";//整数
  var re2 = new RegExp(IsInt2);
  //if(document.getElementById("annualLeave").value.match(re2) == null ){
    //alert("使用年假天数格式不对，应为整数!");
    //document.getElementById("annualLeave").focus();
    //document.getElementById("annualLeave").select();
   // return false;
 // }
  if(destroyTimeArray.length!=2){
    alert("请假开始时间格式不对，应形如 1999-01-01 12:12:12");
    destroyTime.focus();
    destroyTime.select();
    return false;
  }else{
    if(!isValidDateStr(destroyTimeArray[0])||destroyTimeArray[1].match(re1) == null || destroyTimeArray[1].match(re2) != null){
      alert("销假时间格式不对，应形如 1999-01-01 12:12:12");
      destroyTime.focus();
      destroyTime.select();
      return false;
    }
  }
  return (true);
}
function doOnload(){
  var seqId = '<%=seqId%>';
  var requestUrlLeave = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendLeaveAct/selectLeaveById.act?seqId=" + seqId;
  var prcsLeave = getJsonRs(requestUrlLeave);
  if(prcsLeave.rtState == '1'){ 
    alert(prcsLeave.rtMsrg); 
    return ; 
  }
  var prcs = prcsLeave.rtData;
  var seqID = prcs.seqId;
  var leaderId = prcs.leaderId;
  var leaderName = prcs.leaderName;
  var leaveType = prcs.leaveType;
  var leaveDate1 = prcs.leaveDate1;
  var leaveDate2 = prcs.leaveDate2;
  var destroyTime = prcs.destroyTime;
  var allow = prcs.allow;
  var annualLeave = prcs.annualLeave;
  var destroyTime = prcs.destroyTime;
  document.getElementById("seqId").value = seqId;
  document.getElementById("leaveType").value = leaveType;
  document.getElementById("leaveDate1").value = leaveDate1;
  document.getElementById("leaveDate2").value = leaveDate2;
  document.getElementById("destroyTime").value = destroyTime;
  //document.getElementById("annualLeave").value = annualLeave;
  //初始化程序
  var parameters1 = {
      inputId:'leaveDate1',
      property:{isHaveTime:true}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters1);
  var parameters2 = {
      inputId:'leaveDate2',
      property:{isHaveTime:true}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters2);
  var parameters3 = {
      inputId:'destroyTime',
      property:{isHaveTime:true}
      ,bindToBtn:'date3'
  };
  new Calendar(parameters3);
}
function Init(){
  if(checkForm()){
    var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageLeaveAct/updateLeaveById.act";
    var rtJson = getJsonRs(requestURL,mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    parent.opener.location.reload();
    document.getElementById("returnDiv").style.display = "";
    document.getElementById("div").style.display = "none";
  }
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<div id="div">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 修改请假记录</span><br>
    </td>
  </tr>
</table>
<br>	
<div align="center">
  <form action=""  method="post" id="form1" name="form1" class="big1">
  <table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableData"> 请假原因：</td>
      <td class="TableData" colspan="3">
      	 <textarea id="leaveType" name="leaveType" class="BigInput" cols="60" rows="3">补假：额外器</textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 请假时间：</td>
      <td class="TableData" colspan="3" align="left">
        <input type="text" id="leaveDate1" name="leaveDate1" size="20" maxlength="22" class="BigInput" value="">
        <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
     至 <input type="text" id="leaveDate2" name="leaveDate2" size="20" maxlength="22" class="BigInput" value="">
        <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
  </td>
    </tr>
    <tr align="left">
      
      <td nowrap class="TableData"> 销假时间：</td>
      <td class="TableData"  colspan="3">
      	 <input type="text" id="destroyTime" name="destroyTime" size="20" maxlength="22" class="BigInput" value="">
         <img id="date3" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
    </td>      
    </tr>
  </table>

<br><br><br>
<center>	
	<input type="hidden" id="seqId" name="seqId" value="">
	<input type="button" value="保存" class="BigButton" onclick="Init();">&nbsp;&nbsp;
	<input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
</center>	
</form>
</div>	
</div>
<div id="returnDiv" style="display:none">
<table class="MessageBox" align="center" width="220">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">修改成功</div>
    </td>
  </tr>
</table>
<center>
	<input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
</center>
</div>
</body>
</html>