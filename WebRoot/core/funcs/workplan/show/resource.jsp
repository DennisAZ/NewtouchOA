<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkPlan"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkPerson"%>
<%@page import="yh.core.funcs.person.data.YHPerson" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>添加计划任务</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/swfupload.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function doInit() {
  //doInit2();
  doInit3();
  showCalendar('statrTime',false,'beginDateImg');
  showCalendar('endTime',false,'endDateImg');
  var lenght = parseInt($('count').value);
  for(var i = 0; i < lenght ; i++) {
    if($('attachmentName_' + i)) {
      attachMenuUtil("showAtt_" + i ,"work_plan",null,$('attachmentName_' + i).value ,$('attachmentId_' + i).value,true, i);
    }
  }
  moblieSmsRemind('sms2Remind','sms2Check');
}
function checkForm() {   
  if (!$("statrTime").value || !$("endTime").value) { 
    alert("开始时间与结束时间不能为空！");
    selectLast($("statrTime")); 
    return false;
  }
  if (document.getElementById("statrTime").value > document.getElementById("endTime").value ) {
    alert("开始时间不能大于结束时间！");
    selectLast($("endTime"));
    return false;
  }
  if (document.getElementById("PPLAN_CONTENT").value == "") {
    alert("计划内容不能为空！");
    document.getElementById("PPLAN_CONTENT").focus(); 
    return false;
  }
  return true;
}
var isUploadBackFun = false;
var isPlanId;
var isName;
function checkForm2(planId,name) {
  if (checkForm()) { 
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
      isPlanId = planId;
      isName = name;
      return ;
    }
    var param1 = "seqId=" + planId + "&name=" + name;
    var param = encodeURI(param1);
    var pars = $('form1').serialize() ;
    var url = contextPath + "/yh/core/funcs/workplan/act/YHWorkPersonAct/addPerson.act";
    var json = getJsonRs(url,pars);
    if (json.rtState == "1") {
      alert(json.rtMsrg);
    }else {
      window.location = contextPath + "/yh/core/funcs/workplan/act/YHWorkPersonAct/selectPerson.act?" + param;
    }
  }
}
function jugeFile(){
  var formDom  = document.getElementById("formFile");
  var inputDoms  = formDom.getElementsByTagName("input"); 
  for(var i=0; i<inputDoms.length; i++){
    var idval = inputDoms[i].id;
    if(idval.indexOf("ATTACHMENT")!=-1){
      return true;
    }
  } 
  return false; 
}

var upload_limit = 1,limit_type = limitUploadFiles;
var oa_upload_limit = limitUploadFiles;
var swfupload;
function doInit2() {
  var linkColor = document.linkColor;
  var settings = {
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
    upload_url: "<%=contextPath %>/yh/core/funcs/notify/act/YHNotifyHandleAct/fileLoad.act",
    post_params: {"PHPSESSID" : "<%=session.getId()%>"},
    file_size_limit : (maxUploadSize + " MB"),
    file_types : "*.*",
    file_types_description : "All Files",
    file_upload_limit : 100,
    file_queue_limit : 0,
    custom_settings : {
      uploadRow : "fsUploadRow",
      uploadArea : "fsUploadArea",
      progressTarget : "fsUploadProgress",
      startButtonId : "btnStart",
      cancelButtonId : "btnCancel"
    },
    debug: false,
    button_image_url: "<%=imgPath %>/uploadx4.gif",
    button_width: "65",
    button_height: "29",
    button_placeholder_id: "spanButtonPlaceHolder",
    button_text: '<span class=\"textUpload\">批量上传附件</span>',
    button_text_style: ".textUpload{color:" + linkColor + ";}",
    button_text_top_padding : 1,
    button_text_left_padding : 18,
    button_placeholder_id : "spanButtonUpload",
    button_width: 160,
    button_height: 18,
    button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
    button_cursor: SWFUpload.CURSOR.HAND,
    
    file_queued_handler : fileQueued,
    file_queue_error_handler : fileQueueError,
    file_dialog_complete_handler : fileDialogComplete,
    upload_start_handler : uploadStart,
    upload_progress_handler : uploadProgress,
    upload_error_handler : uploadError,
    upload_success_handler : uploadSuccessOver,
    upload_complete_handler : uploadComplete,
    queue_complete_handler : queueComplete
  };
  swfupload = new SWFUpload(settings);
  var attachmentIds = $("attachmentId").value;
  var attachmentName = $("attachmentName").value;
  showAttach(attachmentIds,attachmentName,"showAtt");
}
function uploadSuccessOver(file, serverData) {
  try {
    var progress = new FileProgress(file, this.customSettings.progressTarget);
//  progress.setComplete();
//  progress.setStatus("Complete.");
  progress.toggleCancel(false);
  var json = null;
  json = serverData.evalJSON();
  if (json.state == "1") {
     progress.setError();
     progress.setStatus("上传失败：" + serverData.substr(5));
     
     var stats = this.getStats();
     stats.successful_uploads--;
     stats.upload_errors++;
     this.setStats(stats);
  } else {
  $('attachmentId').value += json.data.attachmentId;
    $('attachmentName').value += json.data.attachmentName;
    var attachmentIds = $("attachmentId").value;
    var attachmentName = $("attachmentName").value;
    var ensize =  $('ensize').value;
    if (ensize) {
    $('ensize').value =(json.data.size + parseInt(ensize));
  }else {
  $('ensize').value =json.data.size ;
    }//附件大小
    showAttach(attachmentIds,attachmentName,"showAtt");
  }
} catch (ex) {
  this.debug(ex);
}
}
/**
* 处理附件的显示

* @param cntrlId
* @return
*/
function showAttach(attrIds,attrNames,cntrId) {
  var reStr = "<div id='attrDiv'>";
  var ym = "";
  var attrId = ""
  var attrIdArrays = attrIds.split(",");
  var attrNameArrays = attrNames.split("*");
  for (var i = 0 ; i <= attrIdArrays.length; i++) {
    if (!attrIdArrays[i]) {
      continue;
    }
    var key = attrIdArrays[i];
    var attrName = attrNameArrays[i];
    var value = attrName.substring( attrName.indexOf("_")+1, attrName.length);
    reStr += "<a href=\"javascript:downFile(\'" + key + "\',\'" + value + "\');\" title=\"" + value + "\">" + value + "</a><br>";
    //MODULE=email&amp;YM=1001&amp;ATTACHMENT_ID=216664316&amp;ATTACHMENT_NAME=SoftMgrUninst.exe
  }
  reStr += "</div>";
  if (cntrId) {
    $(cntrId).innerHTML = reStr;
  }else {
    document.write(reStr);
  }
}
function upload_attach()
{
    $("btnFormFile").click();
}
function handleSingleUpload(rtState, rtMsrg, rtData) {
//var data = rtData.evalJSON(); 
  //$('attachmentId').value += data.attrId;
  //$('attachmentName').value += data.attrName;
  //showAttach($('attachmentId').value,$('attachmentName').value ,"showAtt");
  //$('attr_tr').style.display = ''; 
  //removeAllFile();

  var data = rtData.evalJSON(); 
  $('attachmentId').value += "," + data.attrId;
  $('attachmentName').value += "*" + data.attrName;
  //showAttach($('returnAttId').value,$('returnAttName').value ,"attr",false);
  attachMenuUtil("showAtt","work_plan",null,$('attachmentName').value ,$('attachmentId').value,false,'pre');
  //$('attr_tr').style.display = ''; 
  removeAllFile();
  if (isUploadBackFun) {
    checkForm2(isPlanId,isName);
    isUploadBackFun = false;
  }
}

function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
  var attachNameOld = $('attachmentName').value; 
  var attachIdOld = $('attachmentId').value; 
  var attachNameArrays = attachNameOld.split("*"); 
  var attachIdArrays = attachIdOld.split(","); 
  var attaName = ""; 
  var attaId = ""; 
  for(var i = 0 ; i < attachNameArrays.length ; i++) { 
    if(!attachIdArrays[i] || attachIdArrays[i] == attachId) { 
      continue; 
    }
    attaName += attachNameArrays[i] + "*"; 
    attaId += attachIdArrays[i] + ","; 
  } 
    $('attachmentId').value = attaId; 
    $('attachmentName').value = attaName;
    return true;
}



function deleteId(seqId,name,planId) {
  var msg = "确认要删除该计划任务吗?";
  var param1 = "seqId=" + seqId + "&planId=" + planId + "&name=" + name;
  var param = encodeURI(param1);
  var url = contextPath + "/yh/core/funcs/workplan/act/YHWorkPersonAct/deletePerson.act?" + param;
  if (window.confirm(msg)) {
  window.location = url;
  } 
}
function selectId(seqId,name) {
  window.location = "<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkPersonAct/selectId.act?seqId=" + seqId + "&name=" + name;
}
function doInit3(){
  if(document.getElementById("nameId").value != ""){
    bindDesc([{cntrlId:"nameId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("nameId2").value != ""){
    bindDesc([{cntrlId:"nameId2", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
 }

function checkBox2() {
  if (document.getElementById("smsfla").checked) {
    document.getElementById("smsflag").value = "true";
  }else {
    document.getElementById("smsflag").value = "false";
  }
}

function InsertImage(src) { 
  return;
}


function checkBox3() {
  if (document.getElementById("sms2Check").checked) {
     document.getElementById("sms2flag").value = "true";
  }else {
   document.getElementById("sms2flag").value = "false";
  }
}
/** 
 *js代码 
 *是否显示手机短信提醒 
 */
function moblieSmsRemind(remidDiv,remind) {
   var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=12";
   var rtJson = getJsonRs(requestUrl); 
   if (rtJson.rtState == "1"){ 
     alert(rtJson.rtMsrg); 
     return ; 
   }
   var prc = rtJson.rtData;
   var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
   if (moblieRemindFlag == '2') {
     $(remidDiv).style.display = ''; 
     $(remind).checked = true;
     document.getElementById("sms2flag").value = "true";
   } else if(moblieRemindFlag == '1') { 
     $(remidDiv).style.display = ''; 
     $(remind).checked = false; 
   }else{
     $(remidDiv).style.display = 'none'; 
   }
 }
</script>
</head>
<%
YHWorkPerson person = new YHWorkPerson();
List<YHWorkPerson> list = (List<YHWorkPerson>)request.getAttribute("person");
YHWorkPlan plan = (YHWorkPlan)request.getAttribute("plan");

//创建人，负责人
YHPerson personLog = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
int len = 0;
boolean flag = false;
boolean personFlag = false;
if (!YHUtility.isNullorEmpty(plan.getLeader2Desc())) {
  len = plan.getLeader2Desc().split(",").length;
  for (int j = 0 ; j < len; j++) {
    if (plan.getLeader2Desc().split(",")[j].equals(String.valueOf(personLog.getSeqId()))) {
      flag = true;
    }
  }
}
if (plan.getCreator().equals(String.valueOf(personLog.getSeqId()))) {
  flag = true;
}

String name = (String)request.getAttribute("name");
%>
<body topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3"
 class="small">
 <tr>
  <td class="Big"><img src="<%=imgPath%>/edit.gif" width="22"
   height="18" >&nbsp;<span class="big3"><span id="nameIdDesc"><input type="hidden" name="nameId" id="nameId" value="<%=request.getAttribute("name")%>"></span>&nbsp;－&nbsp;计划任务&nbsp;－
  &nbsp;<%=plan.getName() %>&nbsp;(<%=plan.getStatrTime() %>&nbsp;至&nbsp;<%if(plan.getEndTime() != null){%><%=plan.getEndTime() %><%} %>)</span></td>
 </tr>
</table>
<br>
<%if (list.size() > 0 && flag) { %>
<table class="TableList" width="95%" align="center">
 <tr class="TableHeader">
  <td nowrap align="center" width="15%">开始时间</td>
  <td nowrap align="center" width="15%">结束时间</td>
  <td nowrap align="center" width="30%">计划任务</td>
  <td nowrap align="center">附件</td>
  <td nowrap align="center" width="30%">相关资源</td>
  <td nowrap align="center" width="10%">操作</td>
 </tr>
 <%for (int i = 0 ; i < list.size() ; i++) {
    person = list.get(i);
 %>
 <tr class="TableLine1">
  <td align="center"><%if (person.getPbegeiDate() != null){%><%=person.getPbegeiDate() %><%} %></td>
  <td align="center"><%if (person.getPendDate() != null){%><%=person.getPendDate() %><%} %></td>
  <td align="center"><%if (person.getPplanContent() != null){%><%=person.getPplanContent() %><%} %></td>
  <td align="center"><%if (person.getAttachmentName() != null){%>
   <input type="hidden" id="attachmentId_<%=i %>" name="attachmentId_<%=i %>" value="<%=person.getAttachmentId()%>">
   <input type="hidden" id="attachmentName_<%=i %>" name="attachmentName_<%=i %>" value="<%=person.getAttachmentName()%>">
   <input type="hidden" id="ensize" name="ensize">
   <span id="showAtt_<%=i %>"></span>
  <%} %></td>
  <td align="center"><%if (person.getPuseResource() != null){%><%=person.getPuseResource() %><%} %></td>
  <td nowrap align="center"><a href="javascript:selectId('<%=person.getSeqId()%>','<%=name%>');"> 修改</a> <a href="javascript:deleteId('<%=person.getSeqId()%>','<%=request.getAttribute("name")%>',<%=person.getPlanId() %>)"> 删除</a>
  </td>
 </tr>
 <%}  %>
</table>
<%} else { %>
<table class="MessageBox" align="center" width="340">
<tr><td class='msg info'>
<div class='content' style='font-size:12pt'>无计划任务</div>
</td></tr></table>
<%} %>
<br>
<%if (flag) {
  personFlag = true;
%>
<table border="0" width="95%" cellspacing="0" cellpadding="3"
 class="small">
 <tr>
  <td class="Big"><img src="<%=imgPath%>/edit.gif" 
   width="22" height="20">&nbsp;<span class="big3"> 增加计划任务(<span id="nameId2Desc"><input type="hidden" name="nameId2" id="nameId2" value="<%=request.getAttribute("name")%>"></span>)</span></td>
 </tr>
</table>
<br>
<form name="form1" id="form1">
<table class="TableBlock" width="95%" align="center">
 <tr>
<td width="23%" nowrap class="TableContent">&nbsp;开始时间：</td>
<td width="19%" class="TableData">
<input type="text" id="statrTime" name="statrTime" size="10"
maxlength="19" class="BigInput" value="" readonly> 
<img id="beginDateImg" src="<%=imgPath%>/calendar.gif" border="0" style="cursor:pointer;vertical-align:bottom"></td>
<td width="18%" nowrap class="TableContent">&nbsp;结束时间：</td>
<td width="40%" class="TableData"><input
type="text" id="endTime" name="endTime" size="10" maxlength="19"
class="BigInput" value="" readonly> <img id="endDateImg"
src="<%=imgPath%>/calendar.gif" 
border="0" style="cursor:pointer;vertical-align:bottom"></td>
</tr>
 <tr>
  <td class="TableContent">&nbsp;计划任务内容：
  <input type="hidden" name="PLAN_ID"  id="PLAN_ID" value="<%=plan.getSeqId() %>">
  <input type="hidden" name="PUSER_ID" id="PUSER_ID" value="<%=request.getAttribute("name")%>"></td>
  <td class="TableData" colspan="3"><textarea cols=65
   name="PPLAN_CONTENT" id="PPLAN_CONTENT" rows=5 class="BigINPUT" ></textarea></td>
 </tr>
 <tr>
  <td class="TableContent">&nbsp;相关资源：</td>
  <td class="TableData" colspan="3"><textarea cols=65
   name="PUSE_RESOURCE" rows=5 class="BigINPUT" id="PUSE_RESOURCE"></textarea></td>
 </tr> 
 <tr id="attr_tr">
      <td nowrap class="TableContent">&nbsp;附件: </td>
      <td class="TableData" colspan="3">
        <input type="hidden" id="attachmentId" name="attachmentId" value="">
        <input type="hidden" id="attachmentName" name="attachmentName" value="">
        <input type="hidden" id="ensize" name="ensize">
        <input type="hidden" id="writer" name="writer" value="系统管理员">
        <span id="showAtt"></span>
        </td>
    </tr>
      <tr>
      <td nowrap class="TableContent">&nbsp;附件选择：</td>
      <td class="TableData" id="fsUploadRow"  colspan="3">
           <div id="fsUploadArea" class="flash" style="width:380px;">
             <div id="fsUploadProgress"></div>
             <div id="totalStatics" class="totalStatics"></div>
             <div id="Divs">
               <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onClick="swfupload.startUpload();" disabled="disabled">&nbsp;&nbsp;
               <input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onClick="swfupload.cancelQueue();" disabled="disabled">&nbsp;&nbsp;
            <input type="button" class="SmallButtonW" value="刷新页面" onClick="location.reload();">
            </div>
            </div>
            <div id="attachment1">
      <script>ShowAddFile();</script>
       <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
          <input type="hidden" name="attachmentId" value="">
          <input type="hidden" name="attachmentName" value=""> 
          <div style="display:none"><span id="spanButtonUpload" title="批量上传附件"> </span></div></div>
            </td>
    </tr> 
     <tr>
  <td nowrap class="TableContent">&nbsp;提醒：</td>
  <td class="TableData" colspan="3"><div>&nbsp;<input type='checkbox' name='smsfla' id='smsfla' onClick='checkBox2();' checked>
<label for='SMS_REMIND'>使用内部短信提醒</label>
 <input type='hidden'  name="smsflag" id="smsflag" value="true"></div>
 <div id="sms2Remind">&nbsp;<input type="checkbox" name="sms2Check" id="sms2Check" value="1"  onClick="checkBox3();">
<label for="SMS_REMIND">使用手机短信提醒</label>&nbsp;&nbsp;
<input type="hidden" name="sms2flag" id="sms2flag" value="false">
</div>
 </td>
 </tr>   
 <tr align="center" class="TableControl">
  <td colspan="4"><input type="button" value=" 保存 " class="BigButton" onClick="checkForm2('<%=plan.getSeqId() %>','<%=request.getAttribute("name")%>')">&nbsp;&nbsp; <input type="button"
   value=" 关闭 " class="BigButton" onClick="window.close();">&nbsp;&nbsp;
   <input type='hidden' id='sqlId' name='sqlId' value="<%=plan.getSeqId()%>"/>
   <input type='hidden' id='sqlName' name='sqlName' value="<%=request.getAttribute("name")%>"/>
  </td>
 </tr>
</table>
</form>
<%}if (!personFlag) {%>
<div align="center"><input type="button" value="关闭" class="BigButton" onClick="window.close();"></div>
<input type="hidden" id="nameId2" name="nameId2" value="">
<div id="sms2Remind" style="display:none">&nbsp;<input style="display:none" type="checkbox" name="sms2Check" id="sms2Check" value="1">
<input type="hidden" name="sms2flag" id="sms2flag" value="false">
</div>
<%} %>

<form id="formFile" action="<%=contextPath %>/yh/core/funcs/workplan/act/YHWorkPlanHandleAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
  <input type='hidden' id=count name=count value="<%=list.size() %>"/>
  <input type="hidden" id="moduel" name="moduel" value="work_plan">
</body>
</html>