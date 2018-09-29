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
<title>新建招聘计划</title>
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
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/subsys/oa/hr/recruit/plan/js/recruitPlanLogic.js"></script>
<script type="text/javascript">
var fckContentStr = "";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function doInit(){
	getSecretFlag("HR_RECRUIT_PLAN1","planDitch");
	setDate();
	getSysRemind("smsRemindDiv","smsRemind",63);
	moblieSmsRemind("sms2RemindDiv","sms2Remind",63);
}

//日期
function setDate(){
  var date1Parameters = {
     inputId:'startDate',
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
  if($("planNo").value.trim() == ""){
    alert("计划编号不能为空！");
    $("planNo").focus();
    return (false);
  }
  if($("planName").value.trim() == ""){
    alert("招聘名称不能为空！");
    $("planName").focus();
    return (false);
  }

  if($("planRecrNo").value.trim() == ""){
    alert("招聘人数不能为空！");
    $("planRecrNo").focus();
    return (false);
  }

  if($("startDate").value.trim() == ""){
    alert("开始日期不能为空！");
    $("startDate").focus();
    return (false);
  }

  if($("approvePerson").value.trim() == ""){
    alert("审批人不能为空！");
    return (false);
  }

  if($("planBcws").value.trim() != "" && isNaN($("planBcws").value)){
    alert("预算费用必须为数字！");
    return (false);
  } 

  var startDate = $("startDate").value;
  var endDate = $("endDate").value;
  if(startDate && endDate){
    if(startDate > endDate){
      alert("开始日期不能大于结束日期！");
      $("endDate").focus(); 
      $("endDate").select(); 
      return false;
    }
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
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 新建招聘计划</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/recruit/plan/act/YHHrRecruitPlanAct/addRecruitPlanInfo.act"  method="post" name="form1" id="form1" onsubmit="">
	<table class="TableBlock" width="80%" align="center">
	  <tr>
	  	<td nowrap class="TableData">计划编号：<font color="red">*</font> </td>
	  	<td class="TableData">
	      <input type="text" name="planNo" id="planNo" class="BigInput" size="15" maxlength="50" > 
	    </td>
	    <td nowrap class="TableData">计划名称：<font color="red">*</font> </td>
	    <td class="TableData" >
	      <input type="text" name="planName" id="planName" class="BigInput" size="15"  maxlength="100" > 
	    </td>
	  </tr>
	  <tr>
	    <td nowrap class="TableData">招聘渠道：</td>
	    <td class="TableData" >
        <select name="planDitch" id="planDitch"  title="招聘渠道可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">请选择&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
      </td>
	    <td nowrap class="TableData">预算费用：</td>
	    <td class="TableData">
	      <input type="text" name="planBcws" id="planBcws" class="BigInput" size="15" > 元 
	    </td>
	  </tr>
    <tr>
      <td nowrap class="TableData">开始日期：<font color="red">*</font> </td>
      <td class="TableData">
        <input type="text" name="startDate" id="startDate" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">结束日期：</td>
      <td class="TableData">
        <input type="text" name="endDate" id="endDate" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">招聘人数：<font color="red">*</font> </td>
      <td class="TableData">
        <input type="text" name="planRecrNo" id="planRecrNo" class="BigInput" size="15" > 人
      </td>
      <td nowrap class="TableData">审批人：<font color="red">*</font> </td>
      <td class="TableData">
        <input type="hidden" name="approvePerson" id="approvePerson" value="" >
        <input type="text" name="approvePersonDesc" id="approvePersonDesc" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['approvePerson', 'approvePersonDesc']);">添加</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">招聘说明：</td>
      <td class="TableData" colspan=3>
        <textarea name="recruitDirection" id="recruitDirection" cols="78" rows="2" class="BigInput" value=""></textarea>
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData">招聘备注：</td>
      <td class="TableData" colspan=3>
        <textarea name="recruitRemark" id="recruitRemark" cols="78" rows="2" class="BigInput" value=""></textarea>
      </td>
    </tr> 
    <tr height="25" id="attachment1">
      <td nowrap class="TableData"><span id="ATTACH_LABEL">附件上传：</span></td>
       <td class="TableData" colspan="3">
	    <script>ShowAddFile();</script>
	    <script></script>
	    <script></script> 
			<input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
			<input type="hidden" name="ATTACHMENT_NAME_OLD"	id="ATTACHMENT_NAME_OLD" value="">
			<%--插入图片 --%>
			<input type="hidden" id="moduel" name="moduel" value="">
      <input type="hidden" id="imgattachmentId" name="imgattachmentId">
			<input type="hidden" id="imgattachmentName" name="imgattachmentName">
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
      </td>
    </tr>
  </table>
</form>

</body>
</html>