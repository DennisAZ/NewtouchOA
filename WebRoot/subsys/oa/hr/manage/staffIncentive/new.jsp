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
<title>新建奖惩信息</title>
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
<script type="text/javascript"	src="<%=contextPath%>/subsys/oa/hr/manage/staffIncentive/js/staffIncentiveLogic.js"></script>
<script type="text/javascript">
var fckContentStr = "";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function doInit(){
	getSecretFlag("HR_STAFF_INCENTIVE1","incentiveItem");
	setDate();
	changeIncentiveType();
	getSysRemind("smsRemindDiv","smsRemind",58);
	moblieSmsRemind("sms2RemindDiv","sms2Remind",58);
}


//日期
function setDate(){
	var date1Parameters = {
	   inputId:'incentiveTime',
	   property:{isHaveTime:false}
	   ,bindToBtn:'date1'
	};
	new Calendar(date1Parameters);
}
function doSubmit(){
	if(checkForm()){
		var oEditor = FCKeditorAPI.GetInstance('fileFolder');
		$("incentiveDescription").value = oEditor.GetXHTML();
	//alert("sms2Remind>>" + $("sms2Remind").value   + "  smsRemind>>" + $("smsRemind").value);
		$("form1").submit();
		//alert("通过。。。。");
		
	}
}

function checkForm(){
	if($("staffNameDesc").value == ""){
		alert("员工姓名不能为空！");
		$("staffNameDesc").focus();
		return (false);
	}
	if($("incentiveItem").value == ""){
		alert("请选择奖惩项目！");
		$("incentiveItem").focus();
		return (false);
	}
	if($("salaryMonth").value.trim() == ""){
		alert("工资月份不能为空！");
		$("salaryMonth").focus();
		return (false);
	}
	if($("incentiveType").value == ""){
		alert("奖惩属性不能为空！");
		$("incentiveType").focus();
		return (false);
	}
	if($("incentiveTime").value == ""){
		alert("奖惩日期不能为空！");
		$("incentiveTime").focus();
		return false;
	}
	var incentiveTime = $("incentiveTime").value;
	if(incentiveTime){
		if(!isValidDateStr(incentiveTime)){
			alert("奖惩日期格式不对，应形如 2010-01-02");
			$("incentiveTime").focus();
			$("incentiveTime").select();
			return false;
		}
	}
	if($("addScore").value){
		if(!isNumbers($("addScore").value)){
			alert("您填写的分值应为数值!");
			$("addScore").focus();
			$("addScore").select();
			return false;
		}
	}
	if($("reduceScore").value){
		if(!isNumbers($("reduceScore").value)){
			alert("您填写的分值应为数值!");
			$("reduceScore").focus();
			$("reduceScore").select();
			return false;
		}
	}
	if($("incentiveAmount").value){
		if(!isNumbers($("incentiveAmount").value)){
			alert("您填写的奖惩金额格式错误，应形如 10000.00");
			$("incentiveAmount").focus();
			$("incentiveAmount").select();
			return false;
		}
	}
	if($("yearScore").value){
		if(!isNumbers($("yearScore").value)){
			alert("您填写的年终奖惩系数格式错误!");
			$("yearScore").focus();
			$("yearScore").select();
			return false;
		}
	}
	return true;
}

function changeIncentiveType(){
	var incentiveType = $("incentiveType").value;
	if(incentiveType == 1){
		$("addSpan").show();
		$("reduceSpan").hide();
	}
	if(incentiveType == 2){
		$("reduceSpan").show();
		$("addSpan").hide();
	}
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
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 新建奖惩信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/manage/act/YHHrStaffIncentiveAct/addStaffincentiveInfo.act"  method="post" name="form1" id="form1" onsubmit="">
<table class="TableBlock" width="80%" align="center">
  <tr>
  	<td nowrap class="TableData">单位员工：<font color="red">*</font> </td>
  	<td class="TableData" colspan=3>
      <input type="hidden" name="staffName" id="staffName" value="">
      <textarea cols=50 name="staffNameDesc" id="staffNameDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectUser(['staffName', 'staffNameDesc'],null,null,1);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('staffName').value='';$('staffNameDesc').value='';">清空</a>
   </td>
  </tr>
  <tr>
     <td nowrap class="TableData">奖惩项目：<font color="red">*</font> </td>
      <td class="TableData" colspan=3>
        <select name="incentiveItem" id="incentiveItem"  title="奖惩项目名称可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">项目名称</option>
        </select>
      </td> 
    </tr>
    <tr>
    	 <td nowrap class="TableData">奖惩日期：<font color="red">*</font> </td>
      <td class="TableData">
       <input type="text" name="incentiveTime" id="incentiveTime" size="8" maxlength="10"  class="BigInput" value="">
      <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    	<td nowrap class="TableData">工资月份：<font color="red">*</font> </td>
      <td class="TableData">
       <INPUT type="text" name="salaryMonth" id="salaryMonth" class=BigInput size="8" maxlength = "7" value="<%=curTime.format(new Date())%>">
      </td>
    </tr>
    <tr>
    	<td nowrap class="TableData">奖惩属性：</td>
      <td class="TableData">
        <select name="incentiveType" id="incentiveType" onchange="changeIncentiveType();">
          <option value="1">奖励</option>
          <option value="2">惩罚</option>
        </select>&nbsp;
        <span id="addSpan" style="display: none">加分<input type="text" name="addScore" id="addScore" size="5" maxlength="5">&nbsp;</span>
        <span id="reduceSpan" style="display: none">减分<input type="text" name="reduceScore" id="reduceScore" size="5" maxlength="5"></span>
      </td> 
      <td nowrap class="TableData">奖惩金额：</td>
      <td class="TableData">
        <INPUT type="text" name="incentiveAmount" id="incentiveAmount" class=BigInput size="8" maxlength="8" value="">&nbsp;元
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData">年终奖惩系数：</td>
      <td class="TableData" colspan=3>
        <input type="text" name="yearScore" id="yearScore" size="8" maxlength="5">
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan=3>
        <textarea name="remark" id="remark" cols="74" rows="3" class="BigInput" value=""></textarea>
      </td>
    </tr> 
    <tr class="TableData" id="attachment2">
      <td nowrap>附件文档：</td>
      <td nowrap colspan=3>无附件  
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
    <tr id="EDITOR">
      <td class="TableData" colspan="4"> 奖惩说明：
				<div>
		     <script language=JavaScript>    
		      var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
		      var oFCKeditor = new FCKeditor( 'fileFolder' ) ;
		      oFCKeditor.BasePath = sBasePath ;
		      oFCKeditor.Height = 200;
		      var sSkinPath = sBasePath + 'editor/skins/office2003/';
		      oFCKeditor.Config['SkinPath'] = sSkinPath ;
		      oFCKeditor.Config['PreloadImages'] =
		                      sSkinPath + 'images/toolbar.start.gif' + ';' +
		                      sSkinPath + 'images/toolbar.end.gif' + ';' +
		                      sSkinPath + 'images/toolbar.buttonbg.gif' + ';' +
		                      sSkinPath + 'images/toolbar.buttonarrow.gif' ;
		      //oFCKeditor.Config['FullPage'] = true ;
		       oFCKeditor.ToolbarSet = "fileFolder";
		      oFCKeditor.Value = '' ;
		      oFCKeditor.Create();
		     </script>
				</div>
			</td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
      	<input type="hidden" name="incentiveDescription" id="incentiveDescription" value="">
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
      </td>
    </tr>
  </table>
</form>

</body>
</html>