<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<%

String serviceName = request.getServerName();
int port = request.getLocalPort();
String filePath = YHSysProps.getAttachPath() + "/notify/" ;
filePath = filePath.replace("\\" ,"\\\\");
%>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发文登记</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/src/cmp/Menu.js"></script>
<script type="text/javascript"  src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="js/new.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function initSwfUpload() {
    var linkColor = document.linkColor;
    var settings = {
      flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
      upload_url: "<%=contextPath %>/yh/subsys/jtgwjh/notifyManage/act/YHJhNotifyInfoAct/fileLoad.act",
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
    var attachmentName = $("attachmentName").value;
    var selfdefMenu = {
    	      office:["downFile","read","edit"], 
    	      img:["downFile","play"],  
    	      music:["downFile","play"],  
    	      video:["downFile","play"], 
    	      others:["downFile"]
    	  }
    if(attachmentIds){
       // attachMenuUtil("showAtt","notify",null,attachmentName,attachmentIds,false);
    }
      
  }

function doSubmit(){
	//var oEditor = FCKeditorAPI.GetInstance('fileFolder');
	//$("content").value = oEditor.GetXHTML();
	if(checkForm()){
	    
	    $('notifyTitleConfirm').innerHTML = $('notifyTitle').value;
	    $('reciveDeptConfirm').innerHTML = $('reciveDeptDesc').value;
	    var attachmentName = $('attachmentName').value;
	    while( attachmentName.indexOf("*") != -1 ) {
	      attachmentName = attachmentName.replace("*","<br>"); 
	    }
	    $('attachmentConfirm').innerHTML = attachmentName;
	    $('contentConfirm').innerHTML = $('content').value;
	    $("titleSpan").innerHTML = " 公告信息确认";
	    $("form1").style.display = "none";
	    $("confirmDiv").style.display = "";
	  }
	}
	
function doSubmit2(){
	  $("form1").submit();
	}
//确认页面发送事件
function doSubmit3(){
  $("form1").action = "<%=contextPath%>/yh/subsys/jtgwjh/notifyManage/act/YHJhNotifyInfoAct/addAndSendNotifyInfo.act";
  $("form1").submit();
}

//获取select选中的值
function getSelectedInner(sel){
  return sel.options[sel.options.selectedIndex].innerHTML;
}
//确认返回
function returnInfo(){
	  $("titleSpan").innerHTML = " 添加公告";
	  $("confirmDiv").style.display = "none";
	  $("form1").style.display = "";
	}
//验证
function checkForm(){
  if($("notifyTitle").value == ""){
    alert("文件标题不能为空！");
    $("notifyTitle").focus();
    return (false);
  }

  if($("reciveDept").value == ""){
    alert("接收单位不能为空！");
    return (false);
  }
  if($("content").value == ""){
	    alert("公告内容不能为空！");
	    return (false);
 }
  return true;
}
</script>
</head>
<body onload="initSwfUpload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3" id="titleSpan"> 添加公告</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/jtgwjh/notifyManage/act/YHJhNotifyInfoAct/addNotifyInfo.act"  method="post" name="form1" id="form1" onsubmit="">
	<table class="TableBlock" width="80%" align="center">
	  <tr>
      <td nowrap class="TableData">公告标题：<font color="red">*</font> </td>
      <td class="TableData"colspan="5">
        <input type="text" name="notifyTitle" id="notifyTitle" class="BigInput" size="80">
        <input type="hidden" id="forwordId" name="forwordId" value="0"> 
      </td>
	  </tr>
	 <tr>
      <td nowrap class="TableData">接收单位：<font color="red">*</font> </td>
      <td class="TableData" colspan="5">
        <input type="hidden" id="reciveDept" name="reciveDept" value="">
        <textarea id="reciveDeptDesc" name="reciveDeptDesc" rows="3" style="width:80%" class="BigStatic" readonly></textarea>
        <a href="javascript:void(0);" class="orgAdd" onClick="selectOutDept(['reciveDept', 'reciveDeptDesc']);">添加</a>
        <a href="javascript:void(0);" class="orgClear" onClick="$('reciveDept').value='';$('reciveDeptDesc').value='';">清空</a>
      </td>
    </tr>
 <tr>
      <td nowrap class="TableData">内容： </td>
      <td class="TableData" colspan="5">
        <textarea id="content" name="content" rows="8" style="width:97%" class="BigInput"></textarea>
      </td>
    </tr>
       <tr id="attr_tr">
      <td nowrap class="TableData">附件: </td>
      <td class="TableData" id="showAttachment">
        <input type="hidden" id="attachmentId" name="attachmentId">
        <input type="hidden" id="attachmentName" name="attachmentName">
        <input type="hidden" id="ensize" name="ensize">
        <input type="hidden" id="moduel" name="moduel" value="notify">
        <span id="showAtt">
        </span>
      </td>
    </tr>
      <tr id="fileShowId">
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData" id="fsUploadRow">
	         <div id="fsUploadArea" class="flash" style="width:380px;">
				     <div id="fsUploadProgress"></div>
				     <div id="totalStatics" class="totalStatics"></div>
				     <div>
				       <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="swfupload.startUpload();" disabled="disabled">&nbsp;&nbsp;
				       <input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onclick="swfupload.cancelQueue();" disabled="disabled">&nbsp;&nbsp;
				       <input type="button" class="SmallButtonW" value="刷新页面" onclick="upload_attach_group();">
				    </div>
			      </div>
			      <div id="attachment1">
		           <input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
                  <input type="hidden" name="ATTACHMENT_NAME_OLD" id="ATTACHMENT_NAME_OLD" value="">
                   <span id="spanButtonUpload" title="批量上传附件"> </span>
                  
		        </div>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan='6' nowrap>
        <input type="button" value="确定" onclick="doSubmit();" class="BigButton">
      </td>
    </tr>
  </table>
</form>

<div id="confirmDiv" style="display:none;">
  <table class="TableBlock" width="60%" align="center">
    <tr>
      <td nowrap class="TableData" style="width: 150px;">公告标题： </td>
      <td class="TableData" colspan="3">
        <div id="notifyTitleConfirm"></div>
      </td>
    </tr>  
    <tr>
      <td nowrap class="TableData" style="width: 120px;">接收单位： </td>
      <td class="TableData" colspan="3">
        <div id="reciveDeptConfirm"></div>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="width: 120px;">内容： </td>
      <td class="TableData" colspan="3">
        <div id="contentConfirm"></div>
      </td>
    </tr>
        <tr>
      <td nowrap class="TableData" style="width: 120px;">附件： </td>
      <td class="TableData" colspan="3">
        <div id="attachmentConfirm" name="attachmentConfirm"></div>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan='6' nowrap>
        <input type="button" value="返回" onclick="returnInfo()" class="BigButton">
        <input type="button" value="保存" onclick="doSubmit2();" class="BigButton">
        <input type="button" id="sendButton" value="发送" onclick="doSubmit3()" class="BigButton">
      </td>
    </tr>
  </table>
</div>
</body>
</html>