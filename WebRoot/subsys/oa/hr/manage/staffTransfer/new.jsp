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
<title>新建人事调动信息</title>
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
<script type="text/javascript"	src="<%=contextPath%>/subsys/oa/hr/manage/staffTransfer/js/staffTransferLogic.js"></script>
<script type="text/javascript">
var fckContentStr = "";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function doInit(){
	getSecretFlag("HR_STAFF_TRANSFER1","transferType");
	deptFunc1();
	deptFunc2();
	setDate();
	getSysRemind("smsRemindDiv","smsRemind",56);
	moblieSmsRemind("sms2RemindDiv","sms2Remind",56);
}

//获取全体部门列表
function deptFunc1(){
  var url = "/yh/yh/subsys/oa/rollmanage/act/YHRmsRollRoomAct/selectDeptToAttendance.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("tranDeptBefore");
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  return userId;
}

//获取全体部门列表
function deptFunc2(){
  var url = "/yh/yh/subsys/oa/rollmanage/act/YHRmsRollRoomAct/selectDeptToAttendance.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("tranDeptAfter");
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  return userId;
}

//日期
function setDate(){
  var date1Parameters = {
     inputId:'transferDate',
     property:{isHaveTime:false}
     ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
     inputId:'transferEffectiveDate',
     property:{isHaveTime:false}
     ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
}

function doSubmit(){
	var oEditor = FCKeditorAPI.GetInstance('fileFolder');
	$("tranReason").value = oEditor.GetXHTML();
  if(checkForm()){
    $("form1").submit();
  }
}

function checkForm(){
  if($("transferPersonDesc").value == ""){
    alert("调动员工不能为空！");
    $("transferPersonDesc").focus();
    return (false);
  }

  if($("tranDeptBefore").value == ""){
    alert("调动前部门不能为空！");
    $("tranDeptBefore").focus();
    return (false);
  }

  if($("tranDeptAfter").value == ""){
    alert("调动后部门不能为空！");
    $("tranDeptAfter").focus();
    return (false);
  }

  if($("materialsCondition").value == ""){
    alert("调动手续办理不能为空！");
    $("materialsCondition").focus();
    return (false);
  }

  if($("tranReason").value == ""){
    alert("调动原因不能为空！");
    return (false);
  }

  var transferDate = $("transferDate").value;
  if(transferDate){
    if(!isValidDateStr(transferDate)){
      alert("调动日期格式不对，应形如 2010-01-02");
      $("transferDate").focus();
      $("transferDate").select();
      return false;
    }
  }

  var transferEffectiveDate = $("transferEffectiveDate").value;
  if(transferEffectiveDate){
    if(!isValidDateStr(transferEffectiveDate)){
      alert("调动生效日期格式不对，应形如 2010-01-02");
      $("transferEffectiveDate").focus();
      $("transferEffectiveDate").select();
      return false;
    }
  }
  if(transferDate && transferEffectiveDate){
	  if(transferDate > transferEffectiveDate){
	    alert(" 调动日期不能小于调动生效日期！");
	    $("transferDate").focus(); 
	    $("transferDate").select(); 
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

function getDeptId(){
	if(!$('transferPerson').value) return;
  var requestUrl = contextPath + "/yh/subsys/oa/hr/manage/transfer/act/YHHrStaffTransferAct/getDeptId.act?transferPerson=" + $('transferPerson').value; 
  var rtJson = getJsonRs(requestUrl); 
  if(rtJson.rtState == "1"){ 
	  alert(rtJson.rtMsrg); 
    return ; 
  } 
  $('tranDeptBefore').value = rtJson.rtData.deptId;
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 新建人事调动信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/manage/transfer/act/YHHrStaffTransferAct/addStaffTransferInfo.act"  method="post" name="form1" id="form1" onsubmit="">
	<table class="TableBlock" width="80%" align="center">
	  <tr>
	  	<td nowrap class="TableData">调动人员：<font color="red">*</font> </td>
	  	<td class="TableData">
	      <input type="hidden" name="transferPerson" id="transferPerson" value="" >
	      <input type="text" name="transferPersonDesc" id="transferPersonDesc" class="BigStatic" readonly size="15" >
	      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['transferPerson', 'transferPersonDesc'],null,null,1);getDeptId();">添加</a>
	    </td>
	    <td nowrap class="TableData">调动类型： </td>
	    <td class="TableData" >
	      <select name="transferType" id="transferType"  title="调动类型可在“人力资源设置”->“HRMS代码设置”模块设置。">
	        <option value="">调动类型&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
	      </select>
	    </td>
	  </tr>
	  <tr>
	    <td nowrap class="TableData">调动日期：</td>
	    <td class="TableData">
	      <input type="text" name="transferDate" id="transferDate" size="11" maxlength="10"  class="BigInput" value="" readonly>
	      <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
	    </td>
	    <td nowrap class="TableData">调动生效日期：</td>
	    <td class="TableData">
	      <input type="text" name="transferEffectiveDate" id="transferEffectiveDate" size="11" maxlength="10"  class="BigInput" value="" readonly>
	      <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
	    </td>
	  </tr>
	  <tr>
	    <td nowrap class="TableData">调动前单位：</td>
	    <td class="TableData">
	      <input type="text" name="tranCompanyBefore" id="tranCompanyBefore" class="BigInput" size="15">
	    </td>
	    <td nowrap class="TableData">调动后单位：</td>
	    <td class="TableData">
	      <input type="text" name="tranCompanyAfter" id="tranCompanyAfter" class="BigInput" size="15">
	    </td>
	  </tr>
	  <tr>
      <td nowrap class="TableData">调动前职务：</td>
      <td class="TableData">
        <input type="text" name="tranPositionBefore" id="tranPositionBefore" class="BigInput" size="15" > 
      </td>
      <td nowrap class="TableData">调动后职务：</td>
      <td class="TableData">
        <input type="text" name="tranPositionAfter" id="tranPositionAfter" class="BigInput" size="15">
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData">调动前部门：<font color="red">*</font> </td>
      <td class="TableData">
	      <select name="tranDeptBefore" id="tranDeptBefore" class="inputSelect">
	        <option value="" >请选择</option>
	      </select>               
	    </td>
      <td nowrap class="TableData">调动后部门：<font color="red">*</font> </td>
      <td class="TableData">
        <select name="tranDeptAfter" id="tranDeptAfter" class="inputSelect">
          <option value="" >请选择</option>
        </select>               
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData">调动手续办理：<font color="red">*</font> </td>
      <td class="TableData" colspan=3>
        <textarea name="materialsCondition" id="materialsCondition" cols="78" rows="2" class="BigInput" value=""></textarea>
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan=3>
        <textarea name="remark" id="remark" cols="78" rows="2" class="BigInput" value=""></textarea>
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
      <td class="TableData" colspan="4"> 调动原因：<font color="red">*</font>
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
        <input type="hidden" name="tranReason" id="tranReason" value="">
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
      </td>
    </tr>
  </table>
</form>

</body>
</html>