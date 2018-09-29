<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
session.setAttribute("user", "yzq");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件上传</title>
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript">
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

window.onload = function() {
  var linkColor = document.linkColor;
  var settings = {
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
    upload_url: "<%=contextPath %>/test/core/act/YHTestFileUploadAct/uploadFile.act",
    post_params: {"PHPSESSID" : "<%=session.getId()%>"},
    file_size_limit : (maxUploadSize + " MB"),
    file_types : "*.*",
    file_types_description : "All Files",
    file_upload_limit : 100,
    file_queue_limit : 0,
    custom_settings : {
      uploadRow : "fsUploadRow",
      uploadArea : "fsUploadArea",
      progressTarget : "fsUploadProgress",	//上传处理
      startButtonId : "btnStart",						//开始上传
      cancelButtonId : "btnCancel"  				//全部取消
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
    upload_success_handler : uploadSuccess,
    upload_complete_handler : uploadComplete,
    queue_complete_handler : queueComplete
  };

  swfupload = new SWFUpload(settings);
}

</script>


</head>
<body>

  <form name="form1" id="form1" action="<%=contextPath %>/test/core/act/YHTestFileUploadAct/uploadFile.act" method="post" enctype="multipart/form-data" >
    <table>     
      <tr id="fsUploadRow">
        <td colspan="3">
	        <div id="fsUploadArea" class="flash" style="width:380px;"><!-- 必需 -->
				  <div id="fsUploadProgress"></div>
				  <div id="totalStatics" class="totalStatics"></div>
				  <div>
				    <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="swfupload.startUpload();" disabled="disabled">&nbsp;&nbsp;
				    <input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onclick="swfupload.cancelQueue();" disabled="disabled">&nbsp;&nbsp;
				    <input type="button" class="SmallButtonW" value="刷新页面" onclick="window.location.reload();">
				  </div>
			</div>
		  </td>
      </tr>
      <tr height="25" id="attachment1">
	      <td nowrap class="TableData"><span id="ATTACH_LABEL">附件上传：</span></td>
	    <%--<td class="TableData">
	        <script>ShowAddFile();ShowAddImage();</script>
	        <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
	        <input type="hidden" name="ATTACHMENT_ID_OLD" value="">
	        <input type="hidden" name="ATTACHMENT_NAME_OLD" value="">	        
	      </td>  --%> 
	      <td class="TableData">
	        <span id="spanButtonUpload" title="批量上传附件"> </span>
	      </td>
	      
      </tr>
    </table>
  </form>
   
<%--
<br>
<form name="form1" id="form1" action="<%=contextPath %>/yh/test/core/act/YHTestFileUploadAct/uploadFile.act" method="post" enctype="multipart/form-data" >
<table>
  <tr id="">
    <td colspan="3">
	    <div id="fsUploadArea" class="flash" style="width:380px;">
		  <div id="fsUploadProgress"></div>
			<div id="totalStatics" class="totalStatics"></div>
			<div>
			  <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="swfupload.startUpload();" >&nbsp;&nbsp;
				<input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onclick="swfupload.cancelQueue();" >&nbsp;&nbsp;
				<input type="button" class="SmallButtonW" value="刷新页面" onclick="window.location.reload();">
		  </div>
		 </div>
	 </td>
  </tr>
</table>
</form>  
<br>
<span id="spanButtonUpload" title="批量上传"></span>&nbsp;&nbsp;
 --%> 

</body>
</html>