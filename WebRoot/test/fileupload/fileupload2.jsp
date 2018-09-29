<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
session.setAttribute("user", "yzq");
%>
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
<title>SWFUpload Demos - Simple Demo</title>
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript">
var upload_limit=1,limit_type="php,php3,php4,php5,";
var oa_upload_limit="php,php3,php4,php5,";
var swfupload;

function initSwf() {
  var linkColor = document.linkColor;
  var settings = {
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
    upload_url: "<%=contextPath %>/test/core/act/YHTestFileUploadAct/uploadFile.act",
    post_params: {"PHPSESSID" : "<%=session.getId()%>"},
    file_size_limit : "1000 MB",
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
    upload_success_handler : uploadSuccess,
    upload_complete_handler : uploadComplete,
    queue_complete_handler : queueComplete
  };

  swfupload = new SWFUpload(settings);
};

function upload_attach()
{
  //document.getElementById("formFile").submit();
  $("btnFormFile").click();
}
/**
 * 处理文件上传
 */
function handleSingleUpload(rtState, rtMsrg, rtData) {
  alert(rtMsrg + rtData);
  removeAllFile();
  //alert($("form1").serialize());
}
</script>
</head>
<body>
  <form name="form1" id="form1">
    <table>
      <tr height="25" id="attachment1">
        <td nowrap class="TableData"><span id="ATTACH_LABEL">附件上传：</span></td>
        <td class="TableData">
          <script>ShowAddFile();</script>
          <script>ShowAddImage();</script>
          <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
          <input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
          <input type="hidden" name="ATTACHMENT_NAME_OLD" id="ATTACHMENT_NAME_OLD" value="">
        </td>
      </tr>
    </table>
  </form>
  <form id="formFile" action="<%=contextPath %>/test/core/act/YHTestFileUploadAct/uploadFile2.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  
  <form id="ATTACHMENT1_formFile" action="<%=contextPath %>/test/core/act/YHTestFileUploadAct/uploadFile2.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
<iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</body>
</html>