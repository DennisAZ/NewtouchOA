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
	String curDateStr = YHUtility.getCurDateTimeStr();
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
  if($("leaderId").value == ''){
    alert("审批人员不能为空！");
    $("leaderId").focus();
    $("leaderId").select();
    return false;
  }  
  if(document.getElementById("leaveDate1").value.trim()==""){ 
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
    leaveDate2.focus();
    leaveDate2.select();
    return (false);
  }
  if(leaveDate1Array[0].substring(0,4)!=leaveDate2Array[0].substring(0,4)){
    alert("年休假申请开始年份和结束年份必须是同一年份！");
    leaveDate2.focus();
    leaveDate2.select();
    return false;
  }
  var IsInt = "^[0-9]*[1-9][0-9]*$";//正整数　
  var IsInt2 =   "^-?\\d+$";//整数
  var re2 = new RegExp(IsInt2);
  if(document.getElementById("leaveDays").value.match(re2) == null ){
    document.getElementById("leaveDays").value = 1;
    //alert("使用年假天数格式不对，应为整数!");
  }
  if(parseInt($("leaveDays").value,10)>parseInt($("annual").value,10)){
    alert("申请年休假天数不能大于剩余年休假天数!");
    
    return false;
  }
  return checkTime(1);
  return (true);
}
function doOnload(){
  var userId = '<%=userId%>';
  var seqId = '<%=seqId%>';
  var requestUrlLeave = "<%=contextPath%>/yh/custom/attendance/act/YHAnnualLeaveAct/selectAnnualLeaveById.act?seqId=" + seqId;
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
    var leaveDays = prcs.leaveDays;
    leaveDays = parseInt(leaveDays,10);
    
    document.getElementById("seqId").value = seqId;
    document.getElementById("leaveDate1").value = leaveDate1;
    document.getElementById("leaveDate2").value = leaveDate2;
    document.getElementById("leaveDays").value = leaveDays;
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

  //得到年休假剩余天数
  getAnnualOverplus();
  //日期
  var date1Parameters = {
     inputId:'leaveDate1',
     property:{isHaveTime:true}
     ,bindToBtn:'date1'
       ,callbackFun:checkTime
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
     inputId:'leaveDate2',
     property:{isHaveTime:true}
     ,bindToBtn:'date2'
       ,callbackFun:checkTime
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

function DateCompare(asStartDate,asEndDate){   
  var miStart=Date.parse(asStartDate.replace(/-/g,'/'));   
  var miEnd=Date.parse(asEndDate.replace(/-/g,'/'));    
 // alert((miEnd-miStart)/(1000*24*3600)+1); 
  $("leaveDays").value= (miEnd-miStart)/(1000*24*3600)+1;
} 
function checkTime(data){
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
 // var test = Test(leaveDate1,leaveDate2);

  //return test;
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
    leaveDate2.focus();
    leaveDate2.select();
    return (false);
  }

  var beginDate = leaveDate1Array[0];
  var endDate = leaveDate2Array[0];
  DateCompare(beginDate,endDate);
}
function getAnnualOverplus(){
  var requestUrl = "<%=contextPath%>/yh/custom/attendance/act/YHAnnualLeaveAct/getAnnualOverplus.act";
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  if(prc.overplusDays){
    $("annualOverplus").innerHTML = prc.overplusDays;
    $("annual").value =  prc.overplusDays;
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
<form action="<%=contextPath%>/yh/custom/attendance/act/YHAnnualLeaveAct/updateAnnualLeave.act"method="post" id="form1" name="form1" class="big1" onsubmit="return CheckForm();">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.custom.attendance.data.YHAnnualLeave" />
  <table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableData"> 请假时间：</td>
      <td class="TableData">
        <input type="text" id="leaveDate1" name="leaveDate1" size="20" maxlength="22" class="BigInput" value="<%=curDateStr%>" onchange="checkTime(1);">
        <img id="date1" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
     至 <input type="text" id="leaveDate2" name="leaveDate2" size="20" maxlength="22" class="BigInput" value="<%=curDateStr%>"  onchange="checkTime(1);">
        <img id="date2" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
     </td>
    </tr>
    <tr>
		<td nowrap class="TableData">请假天数：</td>
		<td class="TableData"><input type="text" name="leaveDays" id="leaveDays" size="3" maxlength="3" class="BigStatic"" value="0" readonly="readonly">天，年休假剩余<font color="red" id="annualOverplus">0</font>天

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
        <input type="hidden" id="annual" name="annual" value="0" >&nbsp;&nbsp;
        <input type="submit" value="请假" class="BigButton" title="请假登记">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="location='index.jsp'">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>