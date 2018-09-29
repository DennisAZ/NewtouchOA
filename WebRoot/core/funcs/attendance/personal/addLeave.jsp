<%@ page language="java" import="yh.core.funcs.person.data.YHPerson"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String flowId = request.getParameter("flowId");
			//判断是否为管理员
			//判断是否自己是审批人员

			YHPerson user = (YHPerson) request.getSession().getAttribute(
					YHConst.LOGIN_USER);
			int userId = user.getSeqId();
			String userPriv = user.getUserPriv();
		  boolean IsManage= false;
		  if(userPriv.equals("1")){
		    IsManage = true;
		  }
			String curDateStr = YHUtility.getCurDateTimeStr();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建请假登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript"
	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript"
	src="<%=contextPath%>/core/js/orgselect.js"></script>
<script type="text/javascript">
function CheckForm(){
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
     return (false);
   }
   if(leaveDate1Array[0].substring(0,4)!=leaveDate2Array[0].substring(0,4)){
     alert("年休假申请开始年份和结束年份必须是同一年份！");
     leaveDate2.focus();
     leaveDate2.select();
     return false;
   }
   if(!isNumbers($("leaveDays").value)){
 		 alert("培训预算格式错误,应形如  1.00");
 		 $("leaveDays").focus();
		 $('leaveDays').select();		
 		 return false;
 	 }
   var IsInt = "^[0-9]*[1-9][0-9]*$";//正整数　
   var IsInt2 =   "^-?\\d+$";//整数
   var re = new RegExp(IsInt2);
   //if(document.getElementById("annualLeave").value.match(re) == null ){
   //  alert("使用年假天数格式不对，应为整数!");
   //  document.getElementById("annualLeave").focus();
   //  document.getElementById("annualLeave").select();
   //  return false;
  // }
   //if(parseInt($("leaveDays").value,10)>parseInt($("annual").value,10)){
   //  alert("申请年休假天数不能大于剩余年休假天数!");
   //  return false;
  // }
   if($("leaderId").value == ''){
     alert("审批人员不能为空！");
     $("leaderId").focus();
     $("leaderId").select();
     return false;
   }  
   return (true);
}

/**
 * 判断小数位后２位
 * @param aValue
 * @return
 */
function isNumbers(aValue) { 
  var digitSrc = "0123456789"; 
  aValue = "" + aValue; 
  if (aValue.substr(0, 1) == "-") { 
    aValue = aValue.substr(1, aValue.length - 1); 
  } 
  var strArray = aValue.split("."); 
  // 含有多个“.” 
  if (strArray.length > 2) { 
    return false; 
  } 
  var tmpStr = ""; 
  for (var i = 0; i < strArray.length; i++) { 
    tmpStr += strArray[i]; 
  } 
  for (var i = 0; i < tmpStr.length; i++) { 
    var tmpIndex = digitSrc.indexOf(tmpStr.charAt(i)); 
    if (tmpIndex < 0) { 
  // 有字符不是数字 
      return false; 
    } 
  } 
  if(aValue.indexOf(".") != -1){
    var str = aValue.substr(aValue.indexOf(".")+1, aValue.length-1);
    if(str.length > 2){
      return false;
    }
    if(str.length == 0){
      return false;
    }
  }
  // 是数字 
  return true;
}

function doOnload(){
  var userId = '<%=userId%>';
  var requestURL; 
  var prcsJson; 
  requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendManagerAct/selectManagerPerson.act"; 
  var json = getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  prcsJson = json.rtData;
  var selects = document.getElementById("leaderId"); 
  for(var i = 0; i< prcsJson.length; i++){
    if(userId!=prcsJson[i].seqId){
      var option = document.createElement("option"); 
      option.value = prcsJson[i].seqId; 
      option.innerHTML = prcsJson[i].userName; 
      selects.appendChild(option); 
    }
  }
  var date1Parameters = {
      inputId:'leaveDate1',
      property:{isHaveTime:true}
      ,bindToBtn:'date1'
      ,callbackFun:attendDuty
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'leaveDate2',
      property:{isHaveTime:true}
      ,bindToBtn:'date2'
      ,callbackFun:attendDuty
  };
  new Calendar(date2Parameters);
//判断是否要显示短信提醒
  getAnnualOverplus();
  getSysRemind();
//手机
  getMoblieSmsRemind("moblieSmsRemindDiv","moblieSmsRemind")
}
//判断是否要显示短信提醒


function DateCompare(asStartDate,asEndDate){   
  var miStart=Date.parse(asStartDate.replace(/-/g,'/'));   
  var miEnd=Date.parse(asEndDate.replace(/-/g,'/'));    
 // alert((miEnd-miStart)/(1000*24*3600)+1); 
  $("leaveDays").value= (miEnd-miStart)/(1000*24*3600);
} 

function attendDuty(){
  if($("leaveDate1").value && $("leaveDate2").value){
    if($("leaveDate1").value <= $("leaveDate2").value){
      $("attendDuty").style.display = '';
      getAttendDuty();
      var leaveDate1Array  = $("leaveDate1").value.trim().split(" ");
      var leaveDate2Array  = $("leaveDate2").value.trim().split(" ");
      var beginDate = leaveDate1Array[0];
      var endDate = leaveDate2Array[0];
      DateCompare(beginDate,endDate);
    }
  }
}

function getAnnualOverplus(userIdStr){
  var requestUrl = "<%=contextPath%>/yh/custom/attendance/act/YHAnnualLeaveAct/getAnnualOverplus.act";
  if (userIdStr) {
    requestUrl += "?userIdStr=" + userIdStr;
  }
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  //if(prc.overplusDays){
    $("annualOverplus").innerHTML = prc.overplusDays;
    $("annual").value =  prc.overplusDays;
  //}
}

function getAttendDuty(){
  var beginDate = $("leaveDate1").value;
  var endDate = $("leaveDate2").value;
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendLeaveAct/showMonth.act";
  var rtJson = getJsonRs(url, "endDate=" + endDate + "&beginDate=" + beginDate);
  if(rtJson.rtState == "0"){
    var auto = "自动补登记日期：" +rtJson.rtData.data;
    var noCheck = "";
    if(rtJson.rtData.dataStr){
      noCheck = "<br>不需要审核月份："+rtJson.rtData.dataStr;
    }
    $("listDiv").innerHTML = auto + noCheck;
  }else{
    alert(rtJson.rtMsrg); 
  }
}


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
function Test(leaveDate1,leaveDate2){
 // var leaveDate1 = document.getElementById("leaveDate1");
 // var leaveDate2 = document.getElementById("leaveDate2");
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
<table border="0" width="100%" cellspacing="0" cellpadding="3"
	class="small">
	<tr>
		<td class="Big"><img
			src="<%=imgPath%>/views/attendance.gif" WIDTH="22"
			HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;请假登记</span></td>
	</tr>
</table>
<br>
<form
	action="<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendLeaveAct/addLeave.act"
	method="post" id="form1" name="form1" class="big1"
	onsubmit="return CheckForm();"><input type="hidden"
	id="dtoClass" name="dtoClass"
	value="yh.core.funcs.attendance.personal.data.YHAttendLeave" />
<table class="TableBlock" width="90%" align="center">
	    <%if(IsManage){
       %>
    <tr>
      <td nowrap class="TableData"> 请假人：</td>
      <td class="TableData">
  	   <input type="hidden" name="user" id="user" value=""  />
      <textarea name="userDesc" id="userDesc"  rows="1" cols="10" readonly="readonly" >
      </textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser();getAnnualOverplus($('user').value)">指定</a>
        <span style="font-size:12px;">(说明：帮指定人外出登记，仅oa管理员有此权限。不填写为本人外出登记。)</span>
      </td>
    </tr>
    <%} %>
	<tr>
		<td nowrap class="TableData">请假原因： <font style='color:red'>*</font></td>
		<td class="TableData"><textarea id="leaveType" name="leaveType"
			class="BigInput" cols="60" rows="3"></textarea></td>
	</tr>
	<tr>
		<td nowrap class="TableData">请假时间： <font style='color:red'>*</font></td>
		<td class="TableData"><input type="text" id="leaveDate1"
			name="leaveDate1" size="20" maxlength="22" class="BigInput"
			value="<%=curDateStr%>"> <img id="date1"
			src="<%=imgPath%>/calendar.gif" align="absMiddle"
			border="0" style="cursor: hand"> 至 <input type="text"
			id="leaveDate2" name="leaveDate2" size="20" maxlength="22"
			class="BigInput" value="<%=curDateStr%>"> <img id="date2"
			src="<%=imgPath%>/calendar.gif" align="absMiddle"
			border="0" style="cursor: hand"></td>
	</tr>
 <tr nowrap id="attendDuty" style="display:none;">
    <td class="TableData" colspan="2">
      <div id="listDiv"></div>
       </td>
    </tr>
    <tr>
  <td nowrap class="TableData">请假天数：</td>
  <td class="TableData"><input type="text" name="leaveDays" id="leaveDays" size="3" maxlength="3" class="BigInput" value="0">天，年休假剩余<font color="red" id="annualOverplus">0</font>天

  </td>
 </tr>
	<tr style="display:none">
		<td nowrap class="TableData">使用年休假(<font color="red">整数</font>)：</td>
		<td class="TableData"><input type="text" id="annualLeave"
			name="annualLeave" size="3" maxlength="3" class="BigInput" value="0">天，年休假剩余<font
			color="red">0</font>天</td>
	</tr>
	<tr>
		<td nowrap class="TableData">审批人：</td>
		<td class="TableData"><select id="leaderId" name="leaderId">
		</select></td>
	</tr>
	<tr>
		<td nowrap class="TableData">短信提醒：</td>
		<td class="TableData"><span id="smsRemindDiv"><input type="checkbox" id="smsRemind" name="smsRemind" ><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>    <span id="moblieSmsRemindDiv"><input type="checkbox" id="moblieSmsRemind" name="moblieSmsRemind" ><label for="smsRemind">使用手机短信提醒</label>&nbsp;&nbsp;</span></td>
	</tr>
	<tr align="center" class="TableControl">
		<td colspan="2" nowrap><input type="submit" value="请假"
			class="BigButton" title="请假登记">&nbsp;&nbsp; <input
			type="button" value="返回" class="BigButton"
			onclick="location='leave.jsp'">&nbsp;&nbsp;</td>
	</tr>
</table>
<input type="hidden" id="annual" name="annual" value="0" >&nbsp;&nbsp;
</form>
</body>
</html>