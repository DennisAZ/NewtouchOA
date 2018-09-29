<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.core.funcs.notify.data.YHNotify"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" /> 
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var oFCKeditor = new FCKeditor('Econtent');
var fckTimer = null; 
function changeRange()
{
   if (document.getElementById("rang_role").style.display=="none")
   {
      document.getElementById("rang_role").style.display="";
      document.getElementById("rang_user").style.display="";
      document.getElementById("href_txt").innerText="隐藏按人员或角色发布";
   }
   else
   {
      document.getElementById("rang_role").style.display="none";
      document.getElementById("rang_user").style.display="none";
      document.getElementById("href_txt").innerText="按人员或角色发布";
   }
}

function saveNotify(){
	  var FCK = FCKeditorAPI.GetInstance('Econtent'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
	  var FORM_MODE = FCK.EditingArea.Mode;
	 
	  //获取编辑区域的常量——源文件模式
	  var editingAreaFrame = document.getElementById('Econtent___Frame');
	  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
	  if(FORM_MODE == editModeSourceConst)
	  {
	    FCK.Commands.GetCommand( 'Source' ).Execute();
	  } 
	 // $("formName").value = formName;
	  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
	  var textStr = FORM_HTML;
	  textStr = textStr.replace(/\"/g,"\\\"");
	  textStr = textStr.replace(/\'/g,"\'");
	  textStr = textStr.replace(/[[\n\r\f]/g,"");
	  var url = contextPath + "/yh/core/funcs/notify/act/YHNotifyHandleAct/addNotify.act";
	  document.notifyForm.content.value = textStr;
	  //alert(textStr);
	  $("notifyForm").action = url;
	  $("notifyForm").submit();
}

var seqId = "<%=seqId%>";

var oFCKeditor = new FCKeditor('Econtent');
var fckTimer = null; 
function doInit(){
  var FCK = FCKeditorAPI.GetInstance('Econtent'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
  if (FCK.EditingArea) {
    if (fckTimer) {
      clearTimeout(fckTimer);
      fckTimer = null;
    }
  }else {
    fckTimer = setTimeout(doInit, 100);
    return;
  }
  initSwfUpload();
  if(seqId) {
	var url = "/yh/yh/core/funcs/notify/act/YHNotifyHandleAct/editNotify.act";
	var json = getJsonRs(url , par);
    if(json.rtState == "0"){
    	 bindJson2Cntrl(json.rtData);
    }else{
		alert(json.rtMsrg); 
    }
  }
}
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function initSwfUpload() {
  var linkColor = document.linkColor;
  var settings = {
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
    upload_url: "<%=contextPath %>/yh/core/funcs/email/act/YHInnerEMailAct/fileLoad.act",
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
    upload_success_handler : uploadSuccess,
    upload_complete_handler : uploadComplete,
    queue_complete_handler : queueComplete
  };

  swfupload = new SWFUpload(settings);
  var attachmentIds = $("attachmentId").value;
  var attachmentName = $("attachmentName").value;
  showAttach(attachmentIds,attachmentName,"showAtt");
};
function upload_attach()
{
  saveMaiByUp();
}
function showMenu(selectesId,i) {
}
function changeFormat(typeID)
{ 
   if(typeID=="1")
   {
      document.getElementById("editor").style.display="none";
      document.getElementById("attachment1").style.display="";
      document.getElementById("url_address").style.display="none";
      document.getElementById("format").value="1";
      document.getElementById("status").innerText="MHT格式";
   }
   else if(typeID=="0")
   {
      document.getElementById("editor").style.display="";
      document.getElementById("attachment1").style.display="";
      document.getElementById("url_address").style.display="none";
      document.getElementById("format").value="0";
      document.getElementById("status").innerText="普通格式";
   }
   else if(typeID=="2")
   {
      document.getElementById("editor").style.display="none";
      document.getElementById("attachment1").style.display="none";
      document.getElementById("url_address").style.display="";
      document.getElementById("urlAdd").value="http://";
      document.getElementById("format").value="2";
      document.getElementById("status").innerText="超级链接";
   }
}
function resetTime()
{
   document.form1.SEND_TIME.value="";
}
</script>

</head>
<body onload="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"><span class="big1">修改公告通知</span>&nbsp;&nbsp;
    <select name="typeSelect" class="BigSelect" onChange="changeFormat(this.value)">
        <option value="0" selected> <a href="javascript:changeFormat(0);" style="color:#0000FF;">普通格式</a></option>
        <option value="1"><a href="javascript:changeFormat(1);" style="color:#0000FF;" title="mht格式支持图文混排，Word文档可以直接另存为mht文件；超级链接可直接链接到具体网址。">MHT格式</a></option>
        <option value="2"><a href="javascript:changeFormat(2);" style="color:#0000FF;">超级链接</a></option>
     </select>
    </td>
  </tr>
</table>
<form enctype="multipart/form-data" action="update.php"  method="post" name="form1">
<table class="TableBlock" width="95%" align="center">
    <tr>
      <td nowrap class="TableData"><select name="typeId" style="background: white;" title="公告通知类型可在“系统管理”->“分类码管理”模块设置。">
        <option value="1">选择公告类型</option>
        </select>
       </td>
      <td class="TableData">
         <input type="text" name="subject" size="55" maxlength="200" class="BigInput" value="">
      </td>
    </tr>    
   <tr>
      <td nowrap class="TableData">&nbsp;按部门发布：<br>&nbsp;<a href="javascript:;" id="href_txt" onClick="javascript:changeRange();">按人员或角色发布</a></td>
      <td class="TableData">
        <input type="hidden" id="dept" name="toId" value="">
        <textarea cols=40 id="deptDesc" name="ToName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['dept','deptDesc'] , 5);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="ClearUser()">清空</a>
       &nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>
   <tr id="rang_user" style="display:none">
      <td nowrap class="TableData">&nbsp;按人员发布：</td>
      <td class="TableData">
        <input type="hidden" id="user" name="userId" value="">
        <textarea cols=40 id="userDesc" name="userName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectUser(['user', 'userDesc'] , 5);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('copyToId', 'copyToName')">清空</a>
      </td>
   </tr>
   <tr id="rang_role" style="display:none;">
      <td nowrap class="TableData">&nbsp;按角色发布：</td>
      <td class="TableData">
        <input type="hidden" id="role" name="privId" value="">
        <textarea cols=40 id="roleDesc" name="privName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectRole(['role', 'roleDesc'] , 5);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('privId', 'privName')">清空</a><br>
        发布范围取部门、人员和角色的并集


      </td>
   </tr>
    <tr id="url_address" style="display:none">
      <td nowrap class="TableData"> 超级链接地址：</td>
      <td class="TableData">
        <input type="text" name="urlAdd" size="55" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 发布时间：</td>
      <td class="TableData">
        <input type="text" name="sendTime" size="20" maxlength="20" class="BigInput" value="">
       <img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" onclick="javascript:;">
        &nbsp;&nbsp;<a href="javascript:resetTime();">重置为当前时间</a>
      </td>
    </tr>
   <tr>
      <td nowrap class="TableData"> &nbsp;有效期：</td>
      <td class="TableData">
        <input type="text" id = "beginDate" name="beginDate" class="BigInput" size="10" maxlength="10" value="2010-01-26">
        <img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" onclick="javascript:;">
                     至&nbsp;&nbsp; <input type="text" id = "endDate" name="endDate" class="BigInput" size="10" maxlength="10" value="2010-01-26">
        <img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" onclick="javascript:;">

        为空为手动终止
      </td>
     </tr>
   <tr>
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData" id="fsUploadRow">
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
		        <input type="checkbox" name="download" id="download" checked><label for="download">允许下载Office附件</label>&nbsp;&nbsp;
              <input type="checkbox" name="print" id="print" checked><label for="print">允许打印Office附件</label>&nbsp;&nbsp;&nbsp;<font color="gray">都不选中则只能阅读附件内容</font>
      </td>
      <tr>
      <td nowrap class="TableData"> &nbsp;短信提醒：</td>
      <td class="TableData">
           <input type="checkbox" name="tixing" id="tixing" checked><label for="download">使用内部短信提醒   </label>&nbsp;&nbsp;
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> &nbsp;置顶：</td>
      <td class="TableData"><input type="checkbox" name="top" id="top"><label for="top">使公告通知置顶，显示为重要</label>
      &nbsp;&nbsp;<input type="text" name="topDays" size="3" maxlength="4" class="BigInput" value="0">&nbsp;天后结束置顶，0表示一直置顶


      </td>
    </tr>
  
    <tr id="editor">
   <td class="TableData" colspan='2'>
    <DIV style="MARGIN-TOP: 5px; RIGHT: 40px; POSITION: absolute"><A title=清空邮件内容 onclick="clearEdit();" href="#">清空</A></DIV>
    <script language=JavaScript>    
       oFCKeditor.Config["CustomConfigurationsPath"] = "yh/core/funcs/email/js/fckconfig.js"; 
       oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
       oFCKeditor.Height = "300px";
       // oFCKeditor.Width = "600px";
       oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
       oFCKeditor.Config["PluginsPath"] = "yh/core/funcs/email/editor/plugins/";
       oFCKeditor.ToolbarSet="EmailBar";
       //  alert(loadDataDom);   
       oFCKeditor.Create();  
      </script>  
      </td>
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" name="publish" value="">
        <input type="hidden" name="op" value="">
        <input type="hidden" value="" class="BigButton" name="">
        <input type="button" value="保存" class="BigButton" onClick="sendForm('0');">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="location='index1.php?start=<?=$start?>'">
      </td>
    </tr>
  </table>
  <input type="hidden" name="FORMAT" value="0">
</form>
</body>
</html>