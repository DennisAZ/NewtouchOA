<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑员工离职信息</title>
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
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"  type="text/css" />
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/subsys/oa/hr/manage/staffLeave/js/staffLeaveLogic.js"></script>
<script type="text/javascript">
var fckContentStr = "";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function doInit(){
  getSecretFlag("HR_STAFF_LEAVE1","quitType");
  deptFunc1();
  setDate();
  getSysRemind("smsRemindDiv","smsRemind",64);
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/leave/act/YHHrStaffLeaveAct/getLeaveDetail.act?seqId=<%=seqId%>";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
    if(data.applicationDate){
      $("applicationDate").value = data.applicationDate.substr(0,10);
    }
    if(data.quitTimePlan){
      $("quitTimePlan").value = data.quitTimePlan.substr(0,10);
    }
    if(data.quitTimeFact){
      $("quitTimeFact").value = data.quitTimeFact.substr(0,10);
    }
    if(data.lastSalaryTime){
      $("lastSalaryTime").value = data.lastSalaryTime.substr(0,10);
    }
    if(data.leavePerson){
      bindDesc([{cntrlId:"leavePerson", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    fckContentStr = data.quitReason;
    if(data.attachmentId){
      $("returnAttId").value = data.attachmentId;
      $("returnAttName").value = data.attachmentName;
      var selfdefMenu = {
          office:["downFile","dump","read","edit","deleteFile"], 
          img:["downFile","dump","play","deleteFile"],  
          music:["downFile","play","deleteFile"],  
          video:["downFile","play","deleteFile"], 
          others:["downFile","deleteFile"]
      }
      attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','','<%=seqId%>',selfdefMenu);
    }else{
      $('attr').innerHTML = "无附件";
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
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
  var selects = document.getElementById("leaveDept");
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  return userId;
}

function FCKeditor_OnComplete( editorInstance ) {
  editorInstance.SetData( fckContentStr ) ;
}

//日期
function setDate(){
  var date1Parameters = {
     inputId:'applicationDate',
     property:{isHaveTime:false}
     ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
     inputId:'quitTimePlan',
     property:{isHaveTime:false}
     ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);

  var date3Parameters = {
   inputId:'quitTimeFact',
   property:{isHaveTime:false}
   ,bindToBtn:'date3'
  };
  new Calendar(date3Parameters);
  
  var date4Parameters = {
    inputId:'lastSalaryTime',
    property:{isHaveTime:false}
    ,bindToBtn:'date4'
  };
  new Calendar(date4Parameters);
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

function doSubmit(){
  var oEditor = FCKeditorAPI.GetInstance('fileFolder');
  $("quitReason").value = oEditor.GetXHTML();
  if(checkForm()){
    $("form1").submit();
    //alert("通过。。。。");
  }
}

function checkForm(){
  if($("leavePersonDesc").value == ""){
    alert("离职人员不能为空！");
    $("leavePersonDesc").focus();
    return (false);
  }

  if($("quitReason").value == ""){
    alert("离职原因不能为空！");
    return (false);
  }

  var applicationDate = $("applicationDate").value;
  if(applicationDate){
    if(!isValidDateStr(applicationDate)){
      alert("申请日期格式不对，应形如 2010-01-02");
      $("applicationDate").focus();
      $("applicationDate").select();
      return false;
    }
  }
  var quitTimePlan = $("quitTimePlan").value;
  if(quitTimePlan){
    if(!isValidDateStr(quitTimePlan)){
      alert("拟离职日期格式不对，应形如 2010-01-02");
      $("quitTimePlan").focus();
      $("quitTimePlan").select();
      return false;
    }
  }
  if(applicationDate && quitTimePlan){
    if(applicationDate > quitTimePlan){
      alert(" 拟离职日期不能小于申请日期！");
      $("quitTimePlan").focus(); 
      $("quitTimePlan").select(); 
      return false;
    }
  }

  var quitTimeFact = $("quitTimeFact").value;
  if(quitTimeFact){
    if(!isValidDateStr(quitTimeFact)){
      alert("实际离职日期格式不对，应形如 2010-01-02");
      $("quitTimeFact").focus();
      $("quitTimeFact").select();
      return false;
    }
  }
  var lastSalaryTime = $("lastSalaryTime").value;
  if(lastSalaryTime){
    if(!isValidDateStr(lastSalaryTime)){
      alert("工资截止日期格式不对，应形如 2010-01-02");
      $("lastSalaryTime").focus();
      $("lastSalaryTime").select();
      return false;
    }
  }
  if(lastSalaryTime && quitTimeFact){
    if(lastSalaryTime > quitTimeFact){
      alert(" 实际离职日期不能小于工资截止日期！");
      $("quitTimeFact").focus(); 
      $("quitTimeFact").select(); 
      return false;
    }
  }
  return true;
}

//插入图片
function InsertImage(src){
  var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
  if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG )   {
    oEditor.InsertHtml( "<img src='"+ src  + "'/>") ;
  }
}
//删除附件
function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/leave/act/YHHrStaffLeaveAct/delFloatFile.act?delAttachId=" + attachId + "&seqId=<%=seqId%>";
  //var json = getJsonRs(url);
  var json=getJsonRs(url);
  if(json.rtState =='1'){
    alert(json.rtMsrg);
    return false;
  }else{
    prcsJson=json.rtData;
    var updateFlag=prcsJson.updateFlag;
    if(updateFlag == "1"){
      return true;
    }else{
      return false;
    }
  }
}

function getDeptId(){
  if(!$('leavePerson').value) return;
  var requestUrl = contextPath + "/yh/subsys/oa/hr/manage/transfer/act/YHHrStaffTransferAct/getDeptId.act?transferPerson=" + $('leavePerson').value; 
  var rtJson = getJsonRs(requestUrl); 
  if(rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  } 
  $('leaveDept').value = rtJson.rtData.deptId;
}

function selectGoods(){
  if($('leavePerson').value == ""){
    alert("未指定离职人员");
    return;
  }
  else{
    var url = "<%=contextPath%>/subsys/oa/hr/manage/staffLeave/selectGoods.jsp?SeqId="+$('leavePerson').value;
    newWindow(url, 500, 500);
  }
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 编辑员工离职信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/manage/leave/act/YHHrStaffLeaveAct/updateLeaveInfo.act"  method="post" name="form1" id="form1" onsubmit="">
  <table class="TableBlock" width="80%" align="center">
    <tr>
      <td nowrap class="TableData">离职人员：<font color="red">*</font> </td>
      <td nowrap class="TableData">
        <input type="hidden" name="leavePerson" id="leavePerson" value="">
        <input type="text" name="leavePersonDesc" id="leavePersonDesc" class="BigStatic" readonly size="15">
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['leavePerson', 'leavePersonDesc'],null,null,1);getDeptId();">添加</a>
        &nbsp;<a href="javascript:selectGoods();">查看领用物品</a>
      </td>
      <td nowrap class="TableData">担任职务：</td>
      <td class="TableData">
        <input type="text" name="position" id="position" class="BigInput" size="15">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">离职类型： </td>
      <td class="TableData" >
        <select name="quitType" id="quitType"  title="离职类型可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">离职类型&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
      </td>
      <td nowrap class="TableData">申请日期：</td>
      <td class="TableData">
        <input type="text" name="applicationDate" id="applicationDate" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">拟离职日期：</td>
      <td class="TableData">
        <input type="text" name="quitTimePlan" id="quitTimePlan" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">实际离职日期：</td>
      <td class="TableData">
        <input type="text" name="quitTimeFact" id="quitTimeFact" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">工资截止日期：</td>
      <td class="TableData">
        <input type="text" name="lastSalaryTime" id="lastSalaryTime" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date4" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">离职部门： </td>
      <td class="TableData">
        <select name="leaveDept" id="leaveDept" class="inputSelect">
          <option value="" >请选择</option>
        </select>               
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">去向： </td>
      <td class="TableData" colspan=3>
        <textarea name="trace" id="trace" cols="78" rows="2" class="BigInput" value=""></textarea>
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData">离职手续办理： </td>
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
    <tr class="TableData" id="attachment2">
      <td nowrap>附件文档：</td>
      <td nowrap colspan=3>
      <input type = "hidden" id="returnAttId" name="returnAttId"></input>
      <input type = "hidden" id="returnAttName" name="returnAttName"></input>
      <span id="attr"></span> 
      </td>
   </tr>  
   <tr height="25" id="attachment1">
      <td nowrap class="TableData"><span id="ATTACH_LABEL">附件上传：</span></td>
       <td class="TableData" colspan="3">
      <script>ShowAddFile();</script>
      <script></script>
      <script></script> 
      <input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
      <input type="hidden" name="ATTACHMENT_NAME_OLD" id="ATTACHMENT_NAME_OLD" value="">
      <%--插入图片 --%>
      <input type="hidden" id="moduel" name="moduel" value="">
      <input type="hidden" id="imgattachmentId" name="imgattachmentId">
      <input type="hidden" id="imgattachmentName" name="imgattachmentName">
     </td>
   </tr>
   <tr>
      <td nowrap class="TableData"> 提醒：</td>
      <td class="TableData" colspan=3>
        <span id="smsRemindDiv" style="display: none"><input type="checkbox" name="smsRemind" id="smsRemind" value="" onclick="checkBox('smsRemind')"><label for="smsRemind">短信通知相关人员(如财务人员,办公室) </label>&nbsp;&nbsp;</span><br>
        <input type="hidden" name="smsRemind1" id="smsRemind1" value="">
        <textarea cols="40" name="smsRemind1Desc" id="smsRemind1Desc" rows="2" style="overflow-y:auto;" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['smsRemind1', 'smsRemind1Desc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('smsRemind1').value='';$('smsRemind1Desc').value='';">清空</a>
      </td>
   </tr>
    <tr id="EDITOR">
      <td class="TableData" colspan="4"> 调动原因：


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
        <input type="hidden" name="quitReason" id="quitReason" value="">
        <input type="hidden" name="seqId" id="seqId" value="<%=seqId %>">
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
        <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath %>/subsys/oa/hr/manage/staffLeave/manage.jsp'">
      </td>
    </tr>
  </table>
</form>

</body>
</html>