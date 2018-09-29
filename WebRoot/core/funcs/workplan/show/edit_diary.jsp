<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkDetail"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>修改进度日志</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/swfupload.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
var upload_limit = 1,limit_type = limitUploadFiles;
var oa_upload_limit = limitUploadFiles;
var swfupload;
function doInit2() {
  work();
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
  attachMenuUtil("showAtt","work_plan",null,$('attachmentName').value ,$('attachmentId').value,false);
  //$('attr_tr').style.display = ''; 
  removeAllFile();
  if (isUploadBackFun) {
    checkForm2();
    isUploadBackFun = false;
  }
}
var isUploadBackFun = false;
function checkForm2() {
  if (checkForm()) {
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
      return ;
    }
    var pars = $('form1').serialize() ;
    var url = contextPath + "/yh/core/funcs/workplan/act/YHWorkDetailAct/updateDetaiId2.act";
    var json = getJsonRs(url,pars);
    if (json.rtState == "1") {
      alert(json.rtMsrg);   
    }else {
      window.location = contextPath + "/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetaiId.act?seqId=" + document.getElementById("planId").value;
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
    if (checkForm()) {
      var pars = $('form1').serialize() ;
      var url = contextPath + "/yh/core/funcs/workplan/act/YHWorkDetailAct/updateDetaiId2.act";
      var json = getJsonRs(url,pars);
      if(json.rtState == "1"){
        alert(json.rtMsrg);   
        return false;
      }
      return true;
    }
}

function checkForm() {
  var num = /^[0-9]*$/;
  if (!$("PERCENT").value) { 
    alert("估计完成量与总量的百分比不能为空！");
    selectLast($("PERCENT")); 
    return false;
  }
  if (!num.exec(document.getElementById("PERCENT").value)) { 
    alert("估计完成量与总量的百分比只能为数字！");
    selectLast($("PERCENT")); 
    return false;
  }
  if (parseInt(document.getElementById("PERCENT").value) > 100) { 
    alert("估计完成量与总量的百分比只能为0~100！");
    selectLast($("PERCENT")); 
    return false;
  }
  if (parseInt(document.getElementById("PERCENT").value) < parseInt(document.getElementById("sunNum").value)) { 
    alert("估计完成量不能比上次少！");
    selectLast($("PERCENT")); 
    return false;
  }
  return true;
}
function checkTrue() {
  if (document.getElementById("WRITE_IN_WORK").checked) {
    document.getElementById("chi").value = "true";
  }else {
    document.getElementById("chi").value = "false";
  }
}
function checkBox2() {
  if (document.getElementById("smsfla").checked) {
    document.getElementById("smsflag").value = "true";
  }else {
    document.getElementById("smsflag").value = "false";
  }
}
function doInit() {
  attachMenuUtil("showAtt","work_plan",null,$('attachmentName').value ,$('attachmentId').value,false);
  work();
  moblieSmsRemind('sms2Remind','sms2Check');
}
function work() {
  var now = new Date(); 
  var year = now.getYear();
  var month = now.getMonth()+1;
  var da = now.getDate();
  var hour = now.getHours(); 
  var minute = now.getMinutes();
  var second = now.getSeconds();
  if (eval(hour) < 10) { 
    hour = "0" + hour;
  }
  if (eval(month) < 10) {
    month = "0" + month;
  }
  if (eval(da) < 10) {
    da= "0" + da;
  }
  if (eval(minute) < 10) {
    minute = "0" + minute;
  }
  if (eval(second) < 10) {
    second= "0" + second;
  }
  if (hour >= 12) {
    document.getElementById("WRITE_TIME").value = year + "-" + month + "-" + da + "  " + hour + ":" + minute + ":" + second;
  }else {
    document.getElementById("WRITE_TIME").value = year + "-" + month + "-" + da + "  " + hour + ":" + minute + ":" + second;
  }
}

function myOpen3(seqId) {
  window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetaiId.act?seqId=" + seqId,"myOpen3","height=550,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=90,left=290,resizable=yes");
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
<body onload="doInit()" topmargin="5">
<%
YHWorkDetail detail = (YHWorkDetail)request.getAttribute("detailId");
%>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20">&nbsp;<span class="big3"> 修改进度日志</span>
    </td>
  </tr>
</table>
<br>
<form name="form1" id="form1">
<table class="TableBlock" align="center">
 <tr>
  <td nowrap class="TableContent" width="90">当前时间：</td>
  <td class="TableData"><input type="text" name="WRITE_TIME" id="WRITE_TIME"
   size="19" readonly maxlength="100" class="BigStatic" value="">
   <input type="hidden" name="sqlName" id="sqlName" value="<%=request.getParameter("sqlName")%>">
  </td>
 </tr>
 <tr>
  <td nowrap class="TableContent">完成百分比：<input type="hidden" name="sunNum" id="sunNum" value="<%=detail.getPercent() %>"></td>
  <td class="TableData" colspan="1"><input type="text"
   name="PERCENT" id="PERCENT" size="2" class="BigInput" value="<%=detail.getPercent() %>"> <font
   size="3"></font> 上次进度值：<%=detail.getPercent() %> （注：估计完成量与总量的百分比） </td>
 </tr>
 <tr>
  <td nowrap class="TableContent">进度详情：</td>
  <td class="TableData" colspan="1"><textarea name="PROGRESS" id="PROGRESS"
   class="BigInput" cols="55" rows="5"><%if(detail.getProgress()!=null){%><%=detail.getProgress() %><%}%></textarea></td>
 </tr>
<tr id="attr_tr">
      <td nowrap class="TableContent">附件: </td>
      <td class="TableData">
      
        <font color='#00AA00'><%if(detail.getAttachmentName()!=null){ %>
          <input type="hidden" id="attachmentName2" name="attachmentName2" value='<%=detail.getAttachmentName() %>'>
          <input type="hidden" id="attachmentId" name="attachmentId" value="<%=detail.getAttachmentId() %>">
          <input type="hidden" id="attachmentName" name="attachmentName" value="<%=detail.getAttachmentName() %>">
          <span id="showAtt"></span>
      <%} else {%>
          <input type="hidden" id="attachmentId" name="attachmentId" value="">
          <input type="hidden" id="attachmentName" name="attachmentName" value="">
          <span id="showAtt"></span>
      <%} %>
      </font> 
        <input type="hidden" id="PERCENT2" name="PERCENT2" value="<%=detail.getPercent() %>">
        <input type="hidden" id="ensize" name="ensize">
        <input type="hidden" id="seqId" name="seqId" value="<%=detail.getSeqId()%>">
        <input type="hidden" name="planId" id="planId" value="<%=detail.getPlanId()%>">
        <input type="hidden" name="writer" id="writer" value="<%=detail.getWriter()%>">
        </td>
    </tr>
      <tr>
      <td nowrap class="TableContent">附件选择：</td>
      <td class="TableData" id="fsUploadRow">
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
          <div style="display:none"><span id="spanButtonUpload" title="批量上传附件"> </span></div></div>
            </td>
    </tr> 
 <tr>
  <td nowrap class="TableContent">提醒：</td>
  <td class="TableData"><div><input type='checkbox'name='smsfla' id='smsfla' onClick='checkBox2();' checked>
<label for='SMS_REMIND'>使用内部短信提醒</label>
 <input type='hidden'  name='smsflag' id='smsflag' value="true"></div>
 <div id="sms2Remind"><input type="checkbox" name="sms2Check" id="sms2Check" value="1"  onClick="checkBox3();">
<label for="SMS_REMIND">使用手机短信提醒</label>&nbsp;&nbsp;
<input type="hidden" name="sms2flag" id="sms2flag" value="false">
</div>
 </td>
 </tr>
 <tr>
  <td nowrap class="TableContent">是否写入工作日志：</td>
  <td class="TableData"><input type="checkbox" name="WRITE_IN_WORK"
   id="WRITE_IN_WORK" OnClick="checkTrue()"> (注意：勾选会将进度详情写入工作日志中)
   <input type="hidden" name="chi" id="chi" value='false'>
   </td>
 </tr>
 <tr class="TableControl">
  <td  colspan="2" align="center"><input
   type="button" onClick="checkForm2()" value="确定" class="BigButton">
  &nbsp;&nbsp;<input type="button"
   class="BigButton" value="返回" onclick="javascript:myOpen3('<%=detail.getPlanId()%>');" title="返回">&nbsp;&nbsp;</td>
 </tr>
</table>
</form>
<form id="formFile" action="<%=contextPath %>/yh/core/funcs/workplan/act/YHWorkPlanHandleAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
  <input type="hidden" id="moduel" name="moduel" value="work_plan">
</body>
</html>