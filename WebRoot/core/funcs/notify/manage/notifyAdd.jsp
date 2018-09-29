<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson,yh.core.global.YHSysProps" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
  String isFw = request.getParameter("isFw");
  if (isFw == null || isFw=="") {
    isFw = "";
  }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/swfupload.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/notify/js/openWin.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/Javascript" src="js/notifyAdd.js" ></script>
<script type="text/javascript" src="js/interfaceLogic.js"></script>
<script type="text/javascript">
var upload_limit=1,limit_type="<%=YHSysProps.getLimitUploadFiles()%>"; 
var oa_upload_limit="<%=YHSysProps.getLimitUploadFiles()%>";

var seqId = "<%=seqId%>";
var isFw = "<%=isFw%>";
var swfupload;
function initSwfUpload() {
    var linkColor = document.linkColor;
    var settings = {
      flash_url : contextPath + "/core/cntrls/swfupload.swf",
      upload_url:  contextPath + "/yh/core/funcs/notify/act/YHNotifyHandleAct/fileLoad.act",
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
      button_placeholder_id : "",
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
    if(isFw){
      seqId = "";
    }
 //   showAttach(attachmentIds,attachmentName,"showAtt");
  }
 function InsertImage(src){ 
  var oEditor = FCKeditorAPI.GetInstance('Econtent') ; //FCK实例 
  if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG ) {     
     oEditor.InsertHtml( "<img src='"+ src + "'/>") ; 
  } 
 } 
 function showSubject(){
   var actionSize = $('actionSize').value;
   var actionLight = $('actionLight').value;
   var actionFont = $('actionFont').value;
   var actionLights = $('actionLights').value;
   var actionColor = $('actionColor').value;
   var actionFlag = $('actionLightFlag').value;
   
   var subjectFont = "font-family:" + actionFont + ";font-size:" + actionSize + ";color:" + actionColor + ";filter:" + actionFlag + "(Direction=120, color=" + actionLights + ");";
   $('subjectMainShow').innerHTML = $('subject').value;
   $('subjectMainShow').setStyle(subjectFont);
   
   $('subject_tr').style.display = "";
 }
</script>
</head>
<body onload="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"><span class="big1" id="notifytitle"> 新建公告通知</span>&nbsp;&nbsp;
      <a id="status" href="javascript:;" class="dropdown" onclick="showMenuStatus(event);" hidefocus="true"><span id="name" style="color: #00A00">选择格式</span></a>&nbsp;
    </td>
  </tr>
</table>
<form enctype="multipart/form-data" action="<%=contextPath %>/yh/core/funcs/notify/act/YHNotifyHandleAct/addNotify.act"  method="post" name="notifyForm" id="notifyForm">
<table class="TableBlock" width="95%" align="center">
    <tr>
      <td nowrap class="TableData" width='15%'><select name="typeId" id="typeId" style="background: white;width: 100px" title="公告通知类型可在“系统管理”->“分类码管理”模块设置。">
          <option value="">选择公告类型</option>
        </select></td>
      <td class="TableData" >
        <input type="text" name="subject" id="subject" size="55" maxlength="200" class="BigInput" value="请输入公告标题..." style="color: #8896A0"
         onMouseOver="if($F('subject')=='请输入公告标题...') document.getElementById('subject').style.color='#000000';" 
         onMouseOut="if($F('subject')=='请输入公告标题...') document.getElementById('subject').style.color='#8896A0';" 
         onFocus="if($F('subject')=='请输入公告标题...') {document.getElementById('subject').value='';document.getElementById('subject').style.color='#000000';}">
      </td>
    </tr>
	<tr id="subject_tr" style="display: none;height:50px;">
   <td nowrap class="TableData" >预览</td>
   <td class="TableData">
     <span id="subjectMainShow"></span>
   </td>
	</tr>
	<tr>
	  <td nowrap class="TableData">标题样式：</td>
	  <td nowrap class="TableData">
	      <input type="hidden" name="FONT_FAMILY" value="">
	      <input type="hidden" name="FONT_SIZE" value="">
	      <input type="hidden" name="FONT_COLOR" value="">
	      <input type="hidden" name="FONT_FILTER" value="">
	  <span style="padding-top:5px;padding-left:10px;">
	   <a id="showMeunA" href="#" onclick="showFont(event);"><span id="actionNameFont">字体</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
	  </span>
	  <span style="padding-top:5px;padding-left:10px;">
	   <a id="showMeunB" href="#" onclick="showSize(event);"><span id="actionNameSize">大小</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
	  </span>
	  <span style="padding-top:5px;padding-left:10px;">
	   <a id="showMeunC" href="#" onclick="showColor(event);"><span id="actionNameColor">颜色</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
	  </span>
	  <span style="padding-top:5px;padding-left:10px;display:none;">
	   <a id="showMeunD" href="#" onclick="showLight(event);"><span id="actionNameLight">效果</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
	  </span>
	  <span style="padding-top:5px;padding-left:10px;"><a href="#" onclick="showSubject();">预览</a></span>
	  <input type="hidden" name="actionFont" id="actionFont" value="">
	  <input type="hidden" name="actionSize" id="actionSize" value="">
	  <input type="hidden" name="actionLight" id="actionLight" value="">
	  <input type="hidden" name="actionLights" id="actionLights" value="">
	  <input type="hidden" name="actionColor" id="actionColor" value="">
	  <input type="hidden" name="actionLightFlag" id="actionLightFlag" value="">
	  <input type="hidden" name="subjectFont" id="subjectFont" value="">
	  </td>
	</tr>
    <tr>
      <td nowrap class="TableData">按部门发布：<br><a href="javascript:;" id="href_txt" onClick="javascript:changeRange();">按人员或角色发布</a></td>
      <td class="TableData">
        <input type="hidden" id="dept" name="toId" value="">
        <textarea cols=40 id="deptDesc" name="toName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['dept','deptDesc'] , 5);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="ClearUser('toId','toName')">清空</a>
       &nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>
   <tr id="rang_user" style="display:none">
      <td nowrap class="TableData">按人员发布：</td>
      <td class="TableData">
        <input type="hidden" id="user" name="userId" value="">
        <textarea cols=40 id="userDesc" name="userName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectUser(['user', 'userDesc'] , 5);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('userId','userName')">清空</a>
      </td>
   </tr>
   <tr id="rang_role" style="display:none;">
      <td nowrap class="TableData">按角色发布：</td>
      <td class="TableData">
        <input type="hidden" id="role" name="privId" value="">
        <textarea cols=40 id="roleDesc" name="privName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectRole(['role', 'roleDesc'] , 5);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('privId','privName')">清空</a><br>
        发布范围取部门、人员和角色的并集

      </td>
   </tr>
    <tr id="url_address" style="display:none">
      <td nowrap class="TableData">超级链接地址：</td>
      <td class="TableData">
        <input type="text" id="urlAdd" name="urlAdd" size="55" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
     <tr id="sendTimeTr" style="display:none">
      <td nowrap class="TableData">发布时间：</td>
      <td class="TableData">
        <input type="text" id="sendTime" name="sendTime" class="BigInput" size="15" maxlength="19" value="" >
         <img id="sendTimeImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
          &nbsp;&nbsp;<a href="javascript:resetTime();">设置为当前时间</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">有效期：</td>
      <td class="TableData">
    <input type="text" id = "beginDate" name="beginDate" class="BigInput" size="10" maxlength="10" value="">
        <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
        至 <input type="text" id = "endDate" name="endDate" class="BigInput" size="10" maxlength="10" value="">
        <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0"  style="cursor:pointer">

        为空为手动终止
      </td>
     </tr>
   <tr id="attr_tr">
      <td nowrap class="TableData">附件: </td>
      <td class="TableData" id="showAttachment">
        <input type="hidden" id="attachmentId"  name="attachmentId">
        <input type="hidden" id="attachmentName"  name="attachmentName">
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
		          <script>ShowAddFile();ShowAddImage();</script>
		          <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:void(0)" onclick="javascript:upload_attach();return false;">上传附件</a>'</script>
		          <input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
                  <input type="hidden" name="ATTACHMENT_NAME_OLD" id="ATTACHMENT_NAME_OLD" value="">
		          <span id="spanButtonUpload" title="批量上传附件"> </span>
		        </div>
		        <input type="checkbox" name="download" id="download" checked><label for="download">允许下载Office附件</label>&nbsp;&nbsp;
              <input type="checkbox" name="print" id="print" checked><label for="print">允许打印Office附件</label>&nbsp;&nbsp;&nbsp;<font color="gray">都不选中则只能阅读附件内容</font>
      </td>
    </tr>
 
    <tr id="notifyAuditingSingle">
      <td nowrap class="TableData"> &nbsp;短信提醒：</td>
      <td class="TableData">
          <span id="smsRemindDiv" >
           <input type="checkbox" name="mailRemind" id="mailRemind" ><label for="mailRemind">使用内部短信提醒   </label>&nbsp;&nbsp;
           </span>
           <span id="remidDiv" >
           <input type="checkbox" name="remind" id="remind" ><label for="remind">使用手机短信提醒   </label>&nbsp;&nbsp;
           </span>
      </td>
    </tr>
    <tr id="notifyAuditingSingleTop">
      <td nowrap class="TableData"> &nbsp;置顶：</td>
      <td class="TableData"><input type="checkbox" name="top" id="top"><label for="top">使公告通知置顶，显示为重要</label>
      &nbsp;&nbsp;<input type="text" name="topDays" id="topDays2" size="3" maxlength="4" class="BigInput" value="0">&nbsp;天后结束置顶，0表示一直置顶

      </td>
    </tr>

    
   <tr id="editor">
   <td class="TableData" colspan='2' id="contentTd">
    <script language=JavaScript>
      if (isTouchDevice) {
        $("contentTd").insert("<textarea id=\"contentTextarea\" cols=\"80\" rows=\"5\"></textarea>");
      } else {
	      oFCKeditor.Config["CustomConfigurationsPath"] = "<%=contextPath %>/core/funcs/email/js/fckconfig.js"; 
	      oFCKeditor.BasePath = "<%=contextPath %>/core/js/cmp/fck/fckeditor/";  
	      //oFCKeditor.Height = "100%";
	      // oFCKeditor.Width = "600px";
	      oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
	      oFCKeditor.Config["PluginsPath"] = "plugins/";
	      oFCKeditor.ToolbarSet="EmailBar";
	      //  alert(loadDataDom);   
	      oFCKeditor.Create();  
      }
      </script>  
      </td>
    </tr>
   
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <span id="notifyAuditingSinglepublish">
         <input type="button" value="发布" class="BigButton" onClick="sendForm('1');">&nbsp;&nbsp;
        </span>
        <span id="notifyAuditingSingleFlow" style="display:none">
        <input type="button" value="提交审批" class="BigButton" onClick="sendForm('2');">&nbsp;&nbsp;
        </span>
         <input type="button" value="保存" class="BigButton" onClick="sendForm('0');">&nbsp;&nbsp;
         <span id="editBack" style="display:none">
           <input type="button" value="返回" class="BigButton" onClick="goBack();">
          </span>
     </td>
  </tr>
 </table>
 <input type="hidden" id="format" name="format" value="0">
  <input type="hidden" id="content" name="content" value="">
  <input type="hidden" id="isFw" name="isFw" value="">
  <input type="hidden" id="publish" name="publish" value="">
  <input type="hidden" id="seqId" name="seqId" value="">
  <input type="hidden" name="op" value="">
</form>
<form id="formFile"
	action="<%=contextPath %>/yh/core/funcs/notify/act/YHNotifyHandleAct/uploadFile.act"
	method="post" enctype="multipart/form-data" target="commintFrame">
<input id="btnFormFile" name="btnFormFile" type="submit"
	style="display: none"></input></form>
<iframe  name="commintFrame" id="commintFrame" style="display:none"></iframe>
<input type="hidden"  id="prnt"  value=""/>
<input type="hidden"  id="down"  value=""/>
</body>
<script type="text/javascript"><!--
/**
 * 判断有没有上传的附件
 */
function  jugeFile(){
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
function bindValidDtFunc() {
	bindAssertDateTimePrcBatch([{id:"beginDate", type:"d"}, {id:"endDate", type:"d"}]);
}

function bindValidDtFuncDt() {
	bindAssertDateTimePrcBatch([{id:"sendTime", type:"dt"}]);
}
bindValidDtFunc();
bindValidDtFuncDt();
--></script>
</html>