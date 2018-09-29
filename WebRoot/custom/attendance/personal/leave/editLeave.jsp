<%@ page language="java" import=" yh.core.funcs.person.data.YHPerson" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  //判断是否为管理员
  //判断是否自己是审批人员
  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
  int userId = user.getSeqId();
  String seqId = request.getParameter("seqId");
  if(seqId==null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>修改请假登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function CheckForm(){
  if(document.getElementById("leaveType").value.trim()==""){ 
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
  var leaveDate1 = document.getElementById("leaveDate1");
  var leaveDate2 = document.getElementById("leaveDate2");
  var leaveDate1Array  = leaveDate1.value.trim().split(" ");
  var leaveDate2Array  = leaveDate2.value.trim().split(" ");
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
    leaveDate2.focus();
    leaveDate2.select();
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
    leaveDate1.focus();
    leaveDate1.select();
    return (false);
  }
  return (true);
}
function doOnload(){
  var userId = '<%=userId%>';
  var seqId = '<%=seqId%>';
  var requestUrlLeave = "<%=contextPath%>/yh/custom/attendance/act/YHPersonalLeaveAct/selectLeaveById.act?seqId=" + seqId;
  var prcsLeave = getJsonRs(requestUrlLeave);
  if(prcsLeave.rtState == '1'){ 
    alert(prcsLeave.rtMsrg); 
    return ; 
  }
  var prcs = prcsLeave.rtData;
  var leaderId = "";
  var leaderName = "";
  if(prcs.seqId){
    var seqID = prcs.seqId;
    leaderId = prcs.leaderId;
    leaderName = prcs.leaderName;
    var leaveType = prcs.leaveType;
    var leaveDate1 = prcs.leaveDate1;
    var leaveDate2 = prcs.leaveDate2;
    var allow = prcs.allow;
    var annualLeave = prcs.annualLeave;
    var destroyTime = prcs.destroyTime;
    document.getElementById("seqId").value = seqId;
    document.getElementById("leaveType").value = leaveType;
    document.getElementById("leaveDate1").value = leaveDate1;
    document.getElementById("leaveDate2").value = leaveDate2;
  }
 
  //得到审批人
  var requestURL; 
  var prcsJson; 
  requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendManagerAct/selectManagerPerson.act"; 
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  prcsJson = json.rtData;
  var selects = document.getElementById("leaderId"); 
  for(var i = 0; i< prcsJson.length; i++){
    var option = document.createElement("option"); 
    if(prcsJson[i].seqId == leaderId){
      option.value = prcsJson[i].seqId ; 
      option.innerHTML = prcsJson[i].userName; 
      selects.appendChild(option);  
    }
  }
  for(var i = 0; i< prcsJson.length; i++){
    if(userId!=prcsJson[i].seqId&&prcsJson[i].seqId != leaderId){
      var option = document.createElement("option"); 
      option.value = prcsJson[i].seqId; 
      option.innerHTML = prcsJson[i].userName; 
      selects.appendChild(option); 
    }
  }
  //日期
  var date1Parameters = {
     inputId:'leaveDate1',
     property:{isHaveTime:true}
     ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
     inputId:'leaveDate2',
     property:{isHaveTime:true}
     ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
  getSysRemind();
//手机
  getMoblieSmsRemind("moblieSmsRemindDiv","moblieSmsRemind")
}
//判断是否要显示短信提醒
function getSysRemind(){
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=6";
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  var allowRemind = prc.allowRemind;
  var defaultRemind = prc.defaultRemind;
  if(allowRemind=='2'){
    $("smsRemindDiv").style.display = 'none';
  }else{
    if(defaultRemind=='1'){
      $("smsRemind").checked = true;
    }
  }
  //return prc;
}
/** 
*js代码 
*是否显示手机短信提醒 
*/ 
function getMoblieSmsRemind(remidDiv,remind){ 
var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=6"; 
var rtJson = getJsonRs(requestUrl); 
if(rtJson.rtState == "1"){ 
alert(rtJson.rtMsrg); 
return ; 
} 
var prc = rtJson.rtData; 
var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
if(moblieRemindFlag == '2'){ 
$(remidDiv).style.display = ''; 
$(remind).checked = true; 
}else if(moblieRemindFlag == '1'){ 
$(remidDiv).style.display = ''; 
$(remind).checked = false; 
}else{ 
$(remidDiv).style.display = 'none'; 
} 
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;修改请假登记</span>
    </td>
  </tr>
</table>
<br>
<form action="<%=contextPath%>/yh/custom/attendance/act/YHPersonalLeaveAct/updateLeave.act"method="post" id="form1" name="form1" class="big1"onsubmit="return CheckForm();">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.custom.attendance.data.YHPersonalLeave" />
  <table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableData"> 请假原因：</td>
      <td class="TableData">
      	 <textarea id="leaveType" name="leaveType" class="BigInput" cols="60" rows="3"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 请假时间：</td>
      <td class="TableData">
        <input type="text" id="leaveDate1" name="leaveDate1" size="20" maxlength="22" class="BigInput" value="2010-02-01 17:57:09">
        <img id="date1" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
     至 <input type="text" id="leaveDate2" name="leaveDate2" size="20" maxlength="22" class="BigInput" value="2010-02-01 17:57:09">
        <img id="date2" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
     </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 审批人：</td>
      <td class="TableData">
        <select id="leaderId" name="leaderId" class="">
        </select>请假登记修改后需重新审批
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 短信提醒：</td>
      <td class="TableData"><span id="smsRemindDiv"><input type="checkbox" id="smsRemind" name="smsRemind" ><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>    <span id="moblieSmsRemindDiv"><input type="checkbox" id="moblieSmsRemind" name="moblieSmsRemind" ><label for="moblieSmsRemind">使用手机短信提醒</label>&nbsp;&nbsp;</span></td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" value="" id ="seqId" name="seqId"></input>
        <input type="submit" value="请假" class="BigButton" title="请假登记">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="location='index.jsp'">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>