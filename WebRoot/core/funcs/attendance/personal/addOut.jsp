<%@ page language="java" import="java.util.*, yh.core.funcs.person.data.YHPerson" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId"); 
  //判断是否为管理员
  //判断是否自己是审批人员

  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
  int userId = user.getSeqId();
  String userName = user.getUserName();
  String userPriv = user.getUserPriv();
  boolean IsManage= false;
  if(userPriv.equals("1")){
    IsManage = true;
  }
  String curDateStr = YHUtility.getCurDateTimeStr();
  String curDateTime = curDateStr.substring(11,16);
  int time = Integer.parseInt(curDateStr.substring(11,13))+2;
  if(time<24){
    if(time<10){
      curDateTime = "0" + time + curDateStr.substring(13,16);
    }else{
      curDateTime = time + curDateStr.substring(13,16);
    } 
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建外出登记</title>
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
//比较时间段的
function doOnload(){
  var userId = '<%=userId%>';
  var userName = '<%=userName%>';
  var requestURL; 
  var prcsJson; 
  requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendManagerAct/selectManagerPerson.act"; 
  var json = getJsonRs(requestURL); 
 // alert(rsText);
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
  var parameters = {
      inputId:'outDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date'
  };
  new Calendar(parameters);
  getSysRemind();//短信
//手机
  getMoblieSmsRemind("moblieSmsRemindDiv","moblieSmsRemind");
}
//判断是否要显示短信提醒


function showTimeBegin(){
  showTime2("outTime1", false, attendDuty);
}

function showTimeEnd(){
  showTime2("outTime2", false, attendDuty);
}

function attendDuty(){
  if($("outTime1").value && $("outTime2").value){
    if($("outTime1").value <= $("outTime2").value){
      $("attendDuty").style.display = '';
      getAttendDuty();
    }
  }
}

function getAttendDuty(){
  var outDate = $("outDate").value;
  var beginDate = outDate + " " + $("outTime1").value +":00";
  var endDate = outDate + " " + $("outTime2").value +":00";
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendLeaveAct/showMonth.act";
  var rtJson = getJsonRs(url, "endDate=" + endDate + "&beginDate=" + beginDate);
  if(rtJson.rtState == "0"){
    var auto = "自动补登记日期：" +rtJson.rtData.data;
    var noCheck = "";
    if(rtJson.rtData.dataNo){
      noCheck = "<br>不需要审核月份："+rtJson.rtData.dataNo;
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
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;新建外出登记</span><br>
    </td>
  </tr>
</table>
<br>
<form action="<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendOutAct/addOut.act"  method="post" id = "form1" name="form1" class="big1" onsubmit="return CheckForm();">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.attendance.personal.data.YHAttendOut"/>
  <table class="TableBlock" width="90%" align="center">
    <%if(IsManage){
       %>
    <tr>
      <td nowrap class="TableData"> 外出人：</td>
      <td class="TableData">
  	   <input type="hidden" name="user" id="user" value=""  />
      <textarea name="userDesc" id="userDesc"  rows="1" cols="10" readonly="readonly" >
      </textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser();">指定</a>
        <span style="font-size:12px;">(说明：帮指定人外出登记，仅oa管理员有此权限。不填写为本人外出登记。)</span>
      </td>
    </tr>
    <%} %>
    <tr>
      <td nowrap class="TableData"> 外出原因：<font style='color:red'>*</font></td>
      <td class="TableData">
      	 <textarea id = "outType" name="outType" class="BigInput" cols="60" rows="3"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 外出时间：<font style='color:red'>*</font></td>
      <td class="TableData">
         日期 <input type="text" id = "outDate" name="outDate" size="15" maxlength="5" class="BigStatic" readonly value="<%=curDateStr.substring(0,10) %>">
         <img id="date" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
         由 <input type="text" id = "outTime1" name="outTime1" size="5" maxlength="5" class="BigInput" value="<%=curDateStr.substring(11,16) %>" >
         <img src="<%=imgPath%>/clock.gif" align="absMiddle" border="0" align="absMiddle" style="cursor:pointer" onclick="showTimeBegin();">
         至 <input type="text" id = "outTime2" name="outTime2" size="5" maxlength="5" class="BigInput" value="<%=curDateTime %>">
         <img src="<%=imgPath%>/clock.gif" align="absMiddle" border="0" align="absMiddle" style="cursor:pointer" onclick="showTimeEnd();"><br>
      </td>
    </tr>
     <tr nowrap id="attendDuty" style="display:none;">
    <td class="TableData" colspan="2">
      <div id="listDiv"></div>
       </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 审批人：</td>
      <td class="TableData">
        <select name="leaderId" id = "leaderId">
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 短信提醒：</td>
      <td class="TableData">   <span id="smsRemindDiv"><input type="checkbox" id="smsRemind" name="smsRemind" ><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>    <span id="moblieSmsRemindDiv"><input type="checkbox" id="moblieSmsRemind" name="moblieSmsRemind" ><label for="smsRemind">使用手机短信提醒</label>&nbsp;&nbsp;</span>  </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="submit" value="申请外出" class="BigButton" title="申请外出">&nbsp;&nbsp;
        <input type="button" value="返回上页" class="BigButton" onclick="history.go(-1);">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>

</body>
</html>