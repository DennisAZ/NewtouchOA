<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId == null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>外出归来，主管确认外出记录</title>
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
function CheckForm(){
  if(document.getElementById("outType").value==""){ 
    alert("外出原因不能为空！");
    document.getElementById("outType").focus();
    document.getElementById("outType").select();
    return (false);
  }
   if(document.getElementById("outDate") == ""){
     alert("外出日期不能为空！");
     document.getElementById("outDate").focus();
     document.getElementById("outDate").select();
     return false;
   }
   if(!isValidDateStr(document.getElementById("outDate").value)){
     alert("起始日期格式不对,应形如 2010-02-01");
     document.getElementById("outDate").focus();
     document.getElementById("outDate").select();
     return false;
   }
   if(document.getElementById("outTime1").value==""){ 
     alert("外出起止时间不能为空！");
     document.getElementById("outTime1").focus();
     document.getElementById("outTime1").select();
     return (false);
   }
   if(document.getElementById("outTime2").value==""){ 
     alert("外出起止时间不能为空！");
     document.getElementById("outTime2").focus();
     document.getElementById("outTime2").select();
     return (false);
   }
   var type1 =   "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]$"　; 
   var type2 = "^\:[0-5]?[0-9]$"　;
   var re1 = new RegExp(type1); 
   var re2 = new RegExp(type2);
   if(document.getElementById("outTime1").value != ""){
     if(document.getElementById("outTime1").value.match(re1) == null || document.getElementById("outTime1").value.match(re2) != null) { 
       alert( "开始时间格式不正确,应形如 8:00"); 
       document.getElementById("outTime1").focus();
       document.getElementById("outTime1").select();
       return false;
     }
   }
   if(document.getElementById("outTime2").value != ""){
     if(document.getElementById("outTime2").value.match(re1) == null || document.getElementById("outTime2").value.match(re2) != null) { 
       alert( "结束时间格式不正确,应形如 8:00"); 
       document.getElementById("outTime2").focus();
       document.getElementById("outTime2").select();
       return false;
     }
   }
   var beginInt;
   var endInt;
   var beginArray = document.getElementById("outTime1").value.split(":");
   var endArray = document.getElementById("outTime2").value.split(":");
   for(var i = 0 ; i<beginArray.length; i++){
     beginInt = parseInt("" + beginArray[i]+ "",10);  
     endInt = parseInt("" + endArray[i]+ "",10);
     if((beginInt - endInt) > 0){
       alert("外出结束时间应晚于外出开始时间!");
       document.getElementById("outTime2").focus();
       document.getElementById("outTime2").select();
       return false;
     }else if(beginInt - endInt<0){
       return true;
     }else{
       if(i==1){
         alert("外出结束时间应晚于外出开始时间!");
         document.getElementById("outTime2").focus();
         document.getElementById("outTime2").select();
         return false;
        }
     }  
   }
   return (true);
}
function doOnload(){
  var seqId = '<%=seqId%>';
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendOutAct/selectOutById.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prcs = rtJson.rtData;
  var seqId = prcs.seqId;
  var leaderId = prcs.leaderId;
  var leaderName = prcs.leaderName;
  var outType = prcs.outType;
  var createDate = prcs.createDate;
  var submitTime = prcs.submitTime;
  var outTime1 = prcs.outTime1;
  var outTime2 = prcs.outTime2;
  var reason = prcs.reason;
  document.getElementById("seqId").value = seqId;
  document.getElementById("outType").value = outType;
  document.getElementById("outDate").value = submitTime.substr(0,10);
  document.getElementById("outTime1").value = outTime1;
  document.getElementById("outTime2").value = outTime2;
  var parameters = {
      inputId:'outDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date'
  };
  new Calendar(parameters);
  getSysRemind();
//手机
  getMoblieSmsRemind("moblieSmsRemindDiv","moblieSmsRemind")
}
function Init(){
  if( CheckForm()){
    var requestURL= "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendOutAct/updateStatus.act?type=1";
    var rtJson = getJsonRs(requestURL, mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    if(rtJson.rtState == "0"){
      window.close();
      return ;
    }
  } 
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
<body class="" topmargin="5" onload="doOnload();">
<div id="bodyDiv">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;外出归来，主管确认外出记录</span><br>
    </td>
  </tr>
</table>
<br>
<form action=""  method="post" id="form1" name="form1" class="big1" onsubmit="return CheckForm();">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.attendance.personal.data.YHAttendOut"/>
  <table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableData"> 外出原因：</td>
      <td class="TableData">
      	 <textarea id = "outType" name="outType" class="BigInput" cols="60" rows="3" readonly></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 外出时间：</td>
      <td class="TableData">
         日期 <input type="text" id = "outDate" name="outDate" size="15" maxlength="5" class="BigStatic" readonly value="2010-01-30">
         <img id="date" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
         由 <input type="text" id = "outTime1" name="outTime1" size="5" maxlength="5" class="BigInput" value="">
       <img src="<%=imgPath%>/clock.gif" align="absMiddle" border="0" align="absMiddle" style="cursor:pointer" onclick="showTime2('outTime1')">
          
                  至 <input type="text" id = "outTime2" name="outTime2" size="5" maxlength="5" class="BigInput" value="23:57">
         <img src="<%=imgPath%>/clock.gif" align="absMiddle" border="0" align="absMiddle" style="cursor:pointer" onclick="showTime2('outTime2')"><br>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 短信提醒：</td>
      <td class="TableData"><span id="smsRemindDiv"><input type="checkbox" id="smsRemind" name="smsRemind" ><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>    <span id="moblieSmsRemindDiv"><input type="checkbox" id="moblieSmsRemind" name="moblieSmsRemind" ><label for="smsRemind">使用手机短信提醒</label>&nbsp;&nbsp;</span></td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type = "hidden" id = "seqId" name = "seqId" value  = "<%=request.getParameter("seqId")%>"></input>
        <input type = "hidden" id = "status" name = "status" value  = "<%=request.getParameter("status")%>"></input>
        <input type="button" value="保存" class="BigButton" onclick="Init();">&nbsp;&nbsp;
	    <input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</div>
<div id="closeDiv" style="display:none" align ="center">
    <input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
</div>
</body>
</html>