<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkDetail"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>修正批注</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/swfupload.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>

<script type="text/javascript">
  function doInit() {
    work();
    attachMenuUtil("showAtt","work_plan",null,$('attachmentName').value ,$('attachmentId').value,false);
    //doInit2();
    //attachMenuUtil("showAtt","notify",null,$('attachmentName').value ,$('attachmentId').value,false);
  }
  function checkBox2() {
    if (document.getElementById("smsfla").checked) {
      document.getElementById("smsflag").value = "true";
    }else {
      document.getElementById("smsflag").value = "false";
    }
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
  function check() {
    if (!$("PROGRESS").value) {
      alert("内容不能为空！");
      selectLast($("PROGRESS")); 
      return false;
    }
    var msg = "确认要修改批注领导和评注吗?";
    if (!window.confirm(msg)) {
      return false;
    }
    return true;
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
//    progress.setComplete();
//    progress.setStatus("Complete.");
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
      isAdd();
      isUploadBackFun = false;
    }
  }

  function checkForm() {   
    if (!$("PROGRESS").value) {
      alert("内容不能为空！");
      selectLast($("PROGRESS")); 
      return false;
    }
    return true;
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
        var url = contextPath + "/yh/core/funcs/workplan/act/YHWorkDetailAct/updateDetail2.act";
        var json = getJsonRs(url,pars);
        if(json.rtState == "1"){
          alert(json.rtMsrg);
          return false;
        }
        return true;
      }
  }

  function myOpen(seqid) {
    window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetai.act?seqId=" + seqid,"myOpen","height=550,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=90,left=290,resizable=yes");
  }
  function InsertImage(src) { 
   return;
  }
  var isUploadBackFun = false;
  function isAdd() {
    if (checkForm()) {
      if(jugeFile()){//如果有没有上传的文件，则进行上传
        $("formFile").submit();
        isUploadBackFun = true;
        return ;
      }
      var pars = $('form1').serialize() ;
      var url = contextPath + "/yh/core/funcs/workplan/act/YHWorkDetailAct/updateDetail2.act";
      var json = getJsonRs(url,pars);
      if(json.rtState == "1"){
        alert(json.rtMsrg);
      }else {
        window.location.href = contextPath + "/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetai.act?seqId=" + document.getElementById("planId").value;
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
</script>
</head>
<body topmargin="5" onload="doInit()">
<%
  YHWorkDetail detail = (YHWorkDetail)request.getAttribute("detailId");
%>
<div><img src="<%=imgPath%>/edit.gif" width="20"
      height="20">&nbsp;<span class='big3'>修改批注 </span></div><br>
<form name="form1" id="form1" enctype="multipart/form-data">
<table class="TableBlock" width="100%">
<tr>
<td nowrap class='TableContent' width='90'>&nbsp;批注时间：</td> 
<td class='TableData' align='left'> 
  <input type='text' name='WRITE_TIME' id='WRITE_TIME' size='19' readonly maxlength='100' class='BigStatic' value=''>       
</td> 
</tr>          
 <tr> 
 <td class='TableContent'>&nbsp;批注内容：</td> 
<td class='TableData' colspan='1'><textarea name='PROGRESS' id='PROGRESS' class='BigInput' cols='80' rows='8'><%if (detail.getProgress() != null) {%><%=detail.getProgress() %><%} %></textarea></td>
</tr>
<tr id="attr_tr">
<td nowrap class="TableContent">&nbsp;附件: </td>
<td class="TableData">
<input type="hidden" id="seqId" name="seqId" value="<%=detail.getSeqId() %>">
<%if (detail.getAttachmentId()!=null) {%>
<input type="hidden" id="attachmentId2" name="attachmentId2" value="<%=detail.getAttachmentId()%>"><%} %>
<input type="hidden" name="planId" id="planId" value="<%=detail.getPlanId() %>">
<%if (detail.getAttachmentName() != null) {%>
<input type="hidden" id="attachmentName2" name="attachmentName2" value="<%=detail.getAttachmentName() %>">
<input type="hidden" id="attachmentId" name="attachmentId" value="<%=detail.getAttachmentId()%>">
<input type="hidden" id="attachmentName" name="attachmentName" value="<%=detail.getAttachmentName() %>">
<span id="showAtt"></span>
<%}else {%>
<input type="hidden" id="attachmentId" name="attachmentId" value="">
<input type="hidden" id="attachmentName" name="attachmentName" value="">
<span id="showAtt"></span>
<%} %>
<input type="hidden" id="ensize" name="ensize">
<input type="hidden" id="writer" name="writer" value="系统管理员">
</td>
</tr>
 <tr>
 <td class="TableContent">&nbsp;附件选择：</td>
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
          <input type="hidden" name="attachmentId" value="">
          <input type="hidden" name="attachmentName" value=""> 
          <div style="display:none"><span id="spanButtonUpload" title="批量上传附件"> </span></div></div>
</td>
</tr>       
 <tr> 
 <td nowrap  class='TableControl' colspan='2' align='center'> 
<input type='button' onClick="isAdd()" value='修改批注' class='BigButton'>&nbsp;&nbsp; 
<input type='button' class='BigButton' value='返回' onClick="javascript:myOpen('<%=detail.getPlanId()%>');" title='返回'> 
</td>
</tr>
</table>
</form>
<form id="formFile" action="/yh/yh/core/funcs/workplan/act/YHWorkPlanHandleAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
  <input type="hidden" id="moduel" name="moduel" value="work_plan">
</body>
</html>