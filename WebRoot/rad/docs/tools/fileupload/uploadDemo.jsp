<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />  
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
var isUploadBackFun = false;
var file_size_limit =  1000;
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;
function initSwfUpload() {
  var linkColor = document.linkColor;
  var settings = {
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
    upload_url: "<%=contextPath %>/yh/rad/docs/common/file/YHFileUpLoadDemo/uploadFile.act",
    post_params: {"PHPSESSID" : "<%=session.getId()%>"},
    file_size_limit : ((file_size_limit == -1 ) ? 1000 : file_size_limit )+ " MB",
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
    button_text: '<span class=\"textUpload\">批量上传</span>',
    button_text_style: ".textUpload{color:" + linkColor + ";}",
    button_text_top_padding : 1,
    button_text_left_padding : 18,
    button_placeholder_id : "spanButtonUpload",
    button_width: 70,
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
  var attachmentNames = $("attachmentName").value;
  //showAttach(attachmentIds,attachmentName,"showAtt");
  if(attachmentIds){
    $('attaTr').style.display = "";
    attachMenuUtil("showAtt","email",null,attachmentNames,attachmentIds,false);

  }
};

function doInit(){
  initSwfUpload();
}

function upload_attach(){
  $("formFile").submit();
}

function uploadSuccessOver(file, serverData){
 // alert("ss");
  //return;
  try {
    var progress = new FileProgress(file, this.customSettings.progressTarget);
// progress.setComplete();
// progress.setStatus("Complete.");
    progress.toggleCancel(false);
    var json = null;
    json = serverData.evalJSON();
    if(json.state=="1") {
       progress.setError();
       progress.setStatus("上传失败：" + serverData.substr(5));
       
       var stats=this.getStats();
       stats.successful_uploads--;
       stats.upload_errors++;
       this.setStats(stats);
    } else {
      for(var i = 0 ; i < json.rtData.length ; i ++){
       $('attachmentId').value += json.rtData[i].attachmentId;
       $('attachmentName').value += json.rtData[i].attachmentName;
      }
       var attachmentIds = $("attachmentId").value;
       var attachmentNames = $("attachmentName").value;
       attachMenuUtil("showAtt","test",null,attachmentNames,attachmentIds,false);
    }
  } catch (ex) {
    alert(ex);
    this.debug(ex);
  }
}

function handleSingleUpload(rtState, rtMsrg, rtData) {
  var data = rtData.evalJSON(); 
  $('attachmentId').value += data.attachmentId;
  $('attachmentName').value += data.attachmentName;
  attachMenuUtil("showAtt","test",null,$('attachmentName').value,$('attachmentId').value,false);
  removeAllFile();
  if (isUploadBackFun) {
    isUploadBackFun = false;
  }
}
</script>
</head>
<body onload="doInit()">
<form name="form1" id="form1" action="<%=contextPath %>/yh/core/funcs/email/act/YHInnerEMailAct/sendMail.act" method="post" enctype="multipart/form-data" >
<div id="fsUploadRow">
<div id="fsUploadArea" class="flash" style="width:380px;">
				     <div id="fsUploadProgress"></div>
				     <div id="totalStatics" class="totalStatics"></div>
				     <div>
				       <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="swfupload.startUpload();" disabled="disabled">&nbsp;&nbsp;
				       <input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onclick="swfupload.cancelQueue();" disabled="disabled">&nbsp;&nbsp;
				       <input type="button" class="SmallButtonW" value="刷新页面" onclick="upload_attach();">
				    </div>
			      </div>
			      <div id="attachment1">
		          <script>ShowAddFile();ShowAddImage();</script>
		          <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
		          <input type="hidden" name="ATTACHMENT_ID_OLD" value="">
		          <input type="hidden" name="ATTACHMENT_NAME_OLD" value="">	 
		          <span id="spanButtonUpload" title="批量上传附件"> </span>
		        </div>
		         <input type="hidden" id="attachmentId" name="attachmentId">
                 <input type="hidden" id="attachmentName" name="attachmentName">
                 <input type="hidden" id="moduel" name="moduel" value="test">
                 <br></br>
                 <div id="showAtt"></div>
                 </div>
                 </form>
  <form name="formFile" id="formFile" action="<%=contextPath %>/yh/rad/docs/common/file/YHFileUpLoadDemo/uploadFileSign.act" method="post" enctype="multipart/form-data" target="commintFrame"></form>
<iframe width="0" height="0" name="commintFrame" id="commintFrame"></iframe>

</body>
</html>