<%@ page language="java" import="java.util.*, yh.core.funcs.person.data.YHPerson" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  //判断是否为管理员
  //判断是否自己是审批人员
  String seqId = request.getParameter("seqId");
  if(seqId==null){
    seqId = "";
  }
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
<title>编辑值班登记</title>
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
<script type="text/javascript" src="<%=contextPath %>/custom/attendance/personal/duty/js/dutyLogic.js"></script>
<script type="text/javascript">
//比较时间段的
function doOnload(){
  var userId = '<%=userId%>';
  var seqId = '<%=seqId%>';
  $("seqId").value = seqId;
  var requestURL = "<%=contextPath%>/yh/custom/attendance/act/YHDutyAct/getDuty.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prc = rtJson.rtData;
  var leaderId = "";
  var leaderName = "";
  if(prc.seqId){
    var seqId = prc.seqId;
    leaderId = prc.leaderId;
    leaderName = prc.leaderName;
    var reason = prc.reason;
    document.getElementById("seqId").value = seqId;
    document.getElementById("dutyDesc").value = prc.dutyDesc;
    document.getElementById("dutyTime").value = prc.dutyTime.substr(0,10);
    document.getElementById("endDate").value = prc.endDate;
    document.getElementById("beginDate").value = prc.beginDate;
  }
  var requestURLManager = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendManagerAct/selectManagerPerson.act"; 
  var managerJson = getJsonRs(requestURLManager); 
  //alert(rsText);
  if(managerJson.rtState == '1'){ 
    alert(managerjson.rtMsrg); 
    return ; 
  }
  prcsJson = managerJson.rtData;
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
  var parameters = {
      inputId:'dutyTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
      ,callbackFun:attendDuty
  };
  new Calendar(parameters);
  getSysRemind();//短信
//手机
  getMoblieSmsRemind("moblieSmsRemindDiv","moblieSmsRemind");
}
//判断是否要显示短信提醒

function doSubmit(){
  if(checkForm()){
  var seqId = $("seqId").value;
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/custom/attendance/act/YHDutyAct/updateDuty.act";
  var rtJson = getJsonRs(url,pars);
  if(rtJson.rtState == "0"){
    window.location.href = contextPath + "/custom/attendance/personal/duty/index.jsp";
  }else{
    alert(rtJson.rtMsrg); 
  }
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
//检查两个时间时候有效（yyyy-MM-dd hh:mm:ss）
function chekDate(leaveDate1,leaveDate2){
  // var leaveDate1 = document.getElementById("leaveDate1");
  // var leaveDate2 = document.getElementById("leaveDate2");
   var leaveDate1Array  = leaveDate1.value.trim().split(" ");
   var leaveDate2Array  = leaveDate2.value.trim().split(" ");
   var type1 =   "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
   var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　;
   var re1 = new RegExp(type1); 
   var re2 = new RegExp(type2);
   if(leaveDate1Array.length!=2){
     alert("开始时间格式不对，应形如 1999-01-01 12:12:12");
     leaveDate1.focus();
     leaveDate1.select();
     return false;
   }else{
     if(!isValidDateStr(leaveDate1Array[0])||leaveDate1Array[1].match(re1) == null || leaveDate1Array[1].match(re2) != null){
       alert("开始时间格式不对，应形如 1999-01-01 12:12:12");
       leaveDate1.focus();
       leaveDate1.select();
       return false;
     }
   }
   if(leaveDate2Array.length!=2){
     alert("结束时间格式不对，应形如 1999-01-01 12:12:12");
     leaveDate2.focus();
     leaveDate2.select();
     return false;
   }else{
     if(!isValidDateStr(leaveDate2Array[0])||leaveDate2Array[1].match(re1) == null || leaveDate2Array[1].match(re2) != null){
       alert("结束时间格式不对，应形如 1999-01-01 12:12:12");
       leaveDate2.focus();
       leaveDate2.select();
       return false;
     }
   }
   if(document.getElementById("beginTime").value>=document.getElementById("endTime").value){
     alert("结束时间应该大于开始时间！");
     document.getElementById("endTime").focus();
     document.getElementById("endTime").select();
     return false;
   }
 }


</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;编辑值班登记</span><br>
    </td>
  </tr>
</table>
<br>
<form action="<%=contextPath%>/yh/custom/attendance/act/YHOvertimeRecordAct/updateOvertime.act"  method="post" id = "form1" name="form1" class="big1" onsubmit="return CheckForm();">
  <table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableData"> 值班原因：</td>
      <td class="TableData">
      	 <textarea id = "dutyDesc" name="dutyDesc" class="BigInput" cols="60" rows="3"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 值班时间：</td>
     <td class="TableData">
          <input type="text" id = "dutyTime" name="dutyTime" size="10" maxlength="10" value="<%=curDateStr.substring(0,10) %>">
         <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0">
         
         <input type="text" id = "beginDate" name="beginDate" size="6" maxlength="10" value="" onclick="showTimeBegin();">&nbsp;至
         <input type="text" id = "endDate" name="endDate" size="6" maxlength="10" value="" onclick="showTimeEnd();">
         <span id="attendDuty" style="display:none;"></span>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData"> 审批人：</td>
      <td class="TableData">
        <select name="leaderId" id="leaderId" class="">
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 短信提醒：</td>
      <td class="TableData">   
      <span id="smsRemindDiv"><input type="checkbox" id="smsRemind" name="smsRemind" >
      <label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>    
      <span id="moblieSmsRemindDiv"><input type="checkbox" id="moblieSmsRemind" name="moblieSmsRemind" >
      <label for="moblieSmsRemind">使用手机短信提醒</label>&nbsp;&nbsp;</span>  </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type ="hidden" id = "seqId" name = "seqId" value = ""></input>
        <input type="hidden" name="hour" id="hour" value="">
      <input type="hidden" name="dutyType" id="dutyType" value="">
      <input type="hidden" name="festivalAdd" id="festivalAdd" value="">
      <input type="hidden" name="weekAdd" id="weekAdd" value="">
      <input type="hidden" name="dutyMoney" id="dutyMoney" value="">
      <input type="hidden" name="normalAdd" id="normalAdd" value="">
        <input type="button" value="申请值班" class="BigButton" title="申请值班" onclick="doSubmit();">&nbsp;&nbsp;
        <input type="button" value="返回上页" class="BigButton" onclick="history.go(-1);">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>

</body>
</html>