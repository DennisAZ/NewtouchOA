<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建工资流程</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/subsys/oa/hr/salary/manage/salFlow/js/salFlowLogic.js"></script>
<script type="text/javascript">

function doInit(){
	var myDate = new Date();
	var year = myDate.getFullYear();
	var month = myDate.getMonth()+1;
	if(month<10) month = 0 +''+ month;
	var day = myDate.getDate();
	if(day<10) day = 0 +''+ day;
	setDate();
	$('beginDate').value = year+'-'+month+'-'+day;
	$('endDate').value = year+'-'+month+'-'+day;
	setYearAndMonth();
	$('salYear').value = myDate.getFullYear();
	$('salMonth').value = myDate.getMonth()+1;
	$('remark').value = (myDate.getMonth()+1)+'月份工资';
	getSysRemind("smsRemindDiv","smsRemind",4);
	moblieSmsRemind("sms2RemindDiv","sms2Remind",4);
}

function setYearAndMonth(){
	var option1;
	var option2;
	for(var i = 2000 ; i<2102 ; i++){
	  $('salYear').insert(option1);
	  option1 = new Element('option', {"value" : i});
		option1.insert(i+'年');
	}
  for(var i = 1 ; i<14 ; i++){
    $('salMonth').insert(option2);
    option2 = new Element('option', {"value" : i});
    option2.insert(i+'月');
  }
}

//日期
function setDate(){
  var date1Parameters = {
     inputId:'beginDate',
     property:{isHaveTime:false}
     ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
     inputId:'endDate',
     property:{isHaveTime:false}
     ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
}

function doSubmit(){
  if(checkForm()){
    $("form1").submit();
  }
}

function checkForm(){
  if($("beginDate").value == ""){
    alert("起始日期不能为空！");
    $("beginDate").focus();
    return (false);
  }

  if($("endDate").value == ""){
    alert("截止日期不能为空！");
    $("endDate").focus();
    return (false);
  }
	return true;
}

//判断是否要显示短信提醒 
function getSysRemind(remidDiv,remind,type){ 
var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=" + type; 
var rtJson = getJsonRs(requestUrl); 
if(rtJson.rtState == "1"){ 
  alert(rtJson.rtMsrg); 
  return ; 
} 
var prc = rtJson.rtData; 
//alert(rsText);
var allowRemind = prc.allowRemind;;//是否允许显示 
var defaultRemind = prc.defaultRemind;//是否默认选中 
var mobileRemind = prc.mobileRemind;//手机默认选中 
if(allowRemind=='2'){ 
  $(remidDiv).style.display = 'none'; 
}else{
  $(remidDiv).style.display = ''; 
  if(defaultRemind=='1'){ 
    $(remind).checked = true; 
  } 
}
if(document.getElementById(remind).checked){
  document.getElementById(remind).value = "1";
}else{
  document.getElementById(remind).value = "0";
}
}
//设置提醒值

function checkBox(ramCheck){
  if(document.getElementById(ramCheck).checked){
    document.getElementById(ramCheck).value = "1";
  }else{
    document.getElementById(ramCheck).value = "0";
  }
}
//判断是否要显示手机短信提醒 
function moblieSmsRemind(remidDiv,remind,type){
  var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=" + type; 
  var rtJson = getJsonRs(requestUrl); 
  if(rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  } 
  var prc = rtJson.rtData; 
  //alert(rsText);
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
  if(document.getElementById(remind).checked){
    document.getElementById(remind).value = "1";
  }else{
    document.getElementById(remind).value = "0";
  }
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3">新建工资流程</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/salary/salFlow/act/YHHrSalFlowAct/addSalFlowInfo.act"  method="post" name="form1" id="form1" onsubmit="">
	<table class="TableBlock" width="50%" align="center">
	  <tr>
      <td nowrap class="TableData">起始日期：<font color="red">*</font> </td>
      <td class="TableData">
        <input type="text" name="beginDate" id="beginDate" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
	  </tr>
	  <tr>
	    <td nowrap class="TableData">截止日期：<font color="red">*</font> </td>
	    <td class="TableData">
	      <input type="text" name="endDate" id="endDate" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
	    </td>
	  </tr>
    <tr>
      <td nowrap class="TableData">工资月份：<font color="red">*</font> </td>
      <td class="TableData">
                    年度：<select id="salYear" name="salYear"></select>
                    月份：<select id="salMonth" name="salMonth"></select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData">
        <textarea name="remark" id="remark" cols="25" rows="2" class="BigInput" ></textarea>
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData"> 提醒：</td>
      <td class="TableData" colspan=3>
        <span id="smsRemindDiv" style="display: none"><input type="checkbox" name="smsRemind" id="smsRemind" value="" onclick="checkBox('smsRemind')"><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>
        <span id="sms2RemindDiv" style="display: none"><input type="checkbox" name="sms2Remind" id="sms2Remind" value="" onclick="checkBox('sms2Remind')"><label for="sms2Remind">使用手机短信提醒 </label>&nbsp;&nbsp;</span>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
        <input type="button" value="返回" onclick="history.go(-1);" class="BigButton">
      </td>
    </tr>
  </table>
</form>

</body>
</html>