<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String stationId = request.getParameter("stationId");
String columnId = request.getParameter("columnId");
String treeId = request.getParameter("treeId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建文章</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/swfupload.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/cms/station/js/contentLogic.js"></script>
<script type="text/javascript">
var fckContentStr = "";
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;
var isUploadBackFun = false;

var stationId = '<%=stationId %>';
var columnId = '<%=columnId %>';
function doInit(){
  if(stationId == 'null' && parentId == 'null'){
    $('showInfo2').style.display = "";
  }
  else{
    $('showInfo').style.display = "";
    var date1Parameters = {
        inputId:'contentDate',
        property:{isHaveTime:true}
        ,bindToBtn:'date1'
     };
     new Calendar(date1Parameters);
     
     var now = new Date();
     var year = now.getFullYear();       //年
     var month = now.getMonth() + 1;     //月
     var day = now.getDate();            //日
    
     var clock = year + "-";
     if(month < 10){
         clock += "0";
     }
     clock += month + "-";
     if(day < 10){
         clock += "0";
     }
     clock += day + " ";
     clock += now.toLocaleTimeString();
     $('contentDate').value = clock;
  }
	
  var url = "<%=contextPath%>/yh/cms/column/act/YHColumnAct/getInfomation.act?stationId="+stationId+"&parentId="+columnId;
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var prcs = rtJson.rtData;
	  $('columnName').value = prcs.columnName;
	  $('stationId').value = prcs.stationId;
	}
	initSwfUpload();
}

function doSubmit(){
	var oEditor = FCKeditorAPI.GetInstance('fileFolder');
	$("content").value = oEditor.GetXHTML();
  if(checkForm()){
    var url = "<%=contextPath%>/yh/cms/content/act/YHContentAct/addContent.act";
  	var rtJson = getJsonRs(url,mergeQueryString($("form1")));
  	if(rtJson.rtState == "0"){
  	  alert("文章新建成功！");
  	  location.href = contextPath + "/cms/content/manage.jsp?stationId="+$('stationId').value+"&columnId="+$('columnId').value;
  	}
  }
}

function checkForm(){
  if($("contentName").value == ""){
    alert("文章标题不能为空！");
    $("contentName").focus();
    return (false);
  }
  return true;
}

function clearnOnBor(){
  window.onbeforeunload = "";
}

function upload_attach() {
  $("btnFormFile").click();
}
function initSwfUpload() {
  var linkColor = document.linkColor;
  var settings = {
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
    upload_url: "<%=contextPath %>/yh/cms/content/act/YHContentAct/fileLoad.act",
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
  
  if(attachmentIds){
    attachMenuUtil("showAtt","cms",null,attachmentName,attachmentIds,false);
  }
}
function InsertImage(src){ 
  var oEditor = FCKeditorAPI.GetInstance('fileFolder') ; //FCK实例 
  if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG ) {     
     oEditor.InsertHtml( "<img src='"+ src + "'/>") ; 
  } 
}
function uploadSuccessOver(file, serverData){
  try {
    var progress = new FileProgress(file, this.customSettings.progressTarget);
    progress.toggleCancel(false);
    var json = null;
    json = serverData.evalJSON();
    if (json.state == "1") {
      progress.setError();
      progress.setStatus("上传失败：" + serverData.substr(5));
      var stats=this.getStats();
      stats.successful_uploads--;
      stats.upload_errors++;
      this.setStats(stats);
    }else {
      $('attachmentId').value += json.data.attachmentId;
      $('attachmentName').value += json.data.attachmentName;
      var attachmentIds = $("attachmentId").value;
      var attachmentName = $("attachmentName").value;
      var ensize =  $('ensize').value;
      if(ensize){
        $('ensize').value =(json.data.size + parseInt(ensize));
      }else {
        $('ensize').value =json.data.size ;
      }//附件大小
      attachMenuUtil("showAtt","cms",null,$('attachmentName').value ,$('attachmentId').value,false);
    }
  }catch(ex) {
    this.debug(ex);
  }
}

/**
 * 处理文件上传
 */
function handleSingleUpload(rtState, rtMsrg, rtData) {
  if (rtState != 0) {
    alert(rtMsrg);
    return;
  }
  var data = rtData.evalJSON(); 
  $('attachmentId').value +=  data.attrId;
  $('attachmentName').value +=  data.attrName;   
  var  selfdefMenu = {
    office:["downFile","dump","read","edit","deleteFile"], 
    img:["downFile","dump","play","deleteFile","insertImg"],
    music:["downFile","dump","play","deleteFile"],  
    video:["downFile","dump","play","deleteFile"], 
    others:["downFile","dump","deleteFile"]
  }
  attachMenuSelfUtil("showAtt","cms",$('attachmentName').value ,$('attachmentId').value, '','','',selfdefMenu);
  removeAllFile();
  if (isUploadBackFun == true) {
    sendForm(savePar);
    isUploadBackFun = false;
  }
}

//浮动菜单文件的删除 
function deleteAttachBackHand(attachName,attachId,attrchIndex){ 
  var url= contextPath + "/yh/cms/content/act/YHContentAct/delFloatFile.act?attachId=" + attachId +"&attachName=" + attachName ; 
  var json=getJsonRs(encodeURI(url)); 
  if(json.rtState =='1'){ 
    alert(json.rtMsrg); 
    return false; 
  }else { 
    prcsJson=json.rtData; 
    var updateFlag=prcsJson.updateFlag; 
    if(updateFlag){ 
      var ids = $('attachmentId').value ;
      if (!ids) {
        ids = ""; 
      }
      var names =$('attachmentName').value;
      if (!names) {
        names = ""; 
      }
      var idss = ids.split(",");
      var namess = names.split("*");
     
      var newId = getStr(idss , attachId , ",");
      var newname = getStr(namess , attachName , "*");  
     
      $('attachmentId').value = newId;
      $('attachmentName').value = newname;
      return true; 
   }else{ 
     return false; 
   }  
  } 
}

function getStr(ids , id , split) {
  var str = "";
  for (var i = 0 ; i< ids.length ;i ++){
    var tmp = ids[i];
    if (tmp) {
      if (tmp != id) {
        str += tmp + split;
      }
    }
  }
  return str;
}
</script>
</head>
<body onload="doInit();">
<div id="showInfo" name="showInfo" style="display:none;">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 新建文章</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form action="" method="post" name="form1" id="form1">
  <input type="hidden" name="stationId" id="stationId" value="<%=stationId %>">
  <input type="hidden" name="columnId" id="columnId" value="<%=columnId %>">
	<table class="TableBlock" width="80%" align="center">
	  <tr>
      <td nowrap class="TableData">文章标题：<font color="red">*</font> </td>
      <td class="TableData" colspan="3">
        <input type="text" name="contentName" id="contentName" class="BigInput" size="65">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">文章副标题：</td>
      <td class="TableData" colspan="3">
        <input type="text" name="contentTitle" id="contentTitle" class="BigInput" size="65">
      </td>
	  </tr>
    <tr>
      <td nowrap class="TableData">摘要：</td>
      <td class="TableData" colspan="3">
      	<textarea id="contentAbstract" name="contentAbstract" cols="50" rows="3"></textarea>
      </td>
    </tr> 
    <tr>
    	<td nowrap class="TableData">关键字： </td>
      <td class="TableData">
      	<input type="text" name="keyword" id="keyword" class="BigInput" size="15" >
      </td>
 	    <td nowrap class="TableData">来源： </td>
      <td class="TableData">
      	<input type="text" name="contentSource" id="contentSource" class="BigInput" size="15" >
      </td>
    </tr>
    <tr>
    	<td nowrap class="TableData">文件名： </td>
      <td class="TableData">
      	<input type="text" name="contentFileName" id="contentFileName" class="BigInput" size="15" >
      </td>
 	    <td nowrap class="TableData">作者： </td>
      <td class="TableData">
      	<input type="text" name="contentAuthor" id="contentAuthor" class="BigInput" size="15" >
      </td>
    </tr>
 	  <tr>
      <td nowrap class="TableData">发布日期： </td>
      <td class="TableData">
 	      <input type="text" name="contentDate" id="contentDate" size="20" maxlength="20"  class="BigInput" value="" readonly>
	      <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">所属栏目：</td>
      <td class="TableData">
        <input type="text" name="columnName" id="columnName" class="BigInput" size="15" disabled>
      </td>
	  </tr>
    <tr id="attr_tr">
      <td nowrap class="TableData">附件: </td>
      <td class="TableData" colspan="3" id="showAttachment">
        <input type="hidden" id="attachmentId" name="attachmentId">
        <input type="hidden" id="attachmentName" name="attachmentName">
        <input type="hidden" id="ensize" name="ensize">
        <input type="hidden" id="moduel" name="moduel" value="cms">
        <span id="showAtt">
        </span>
      </td>
    </tr>
    <tr id="fileShowId">
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData" colspan="3" id="fsUploadRow">
         <div id="fsUploadArea" class="flash" style="width:380px;">
			     <div id="fsUploadProgress"></div>
			     <div id="totalStatics" class="totalStatics"></div>
			     <div>
			       <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="swfupload.startUpload();" disabled="disabled">&nbsp;&nbsp;
			       <input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onclick="swfupload.cancelQueue();" disabled="disabled">&nbsp;&nbsp;
			    </div>
	      </div>
	      <div id="attachment1">
          <script>ShowAddFile();ShowAddImage();</script>
          <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
          <input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
          <input type="hidden" name="ATTACHMENT_NAME_OLD" id="ATTACHMENT_NAME_OLD" value="">
          <span id="spanButtonUpload" title="批量上传附件"> </span>
        </div>
      </td>
    </tr>
    <tr id="EDITOR">
      <td class="TableData" colspan="4"> 文章内容：
        <div>
         <script language=JavaScript>    
          var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
          var oFCKeditor = new FCKeditor( 'fileFolder' ) ;
          oFCKeditor.BasePath = sBasePath ;
          oFCKeditor.Height = 300;
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
      	<input type="hidden" name="content" id="content" value="">
        <input type="hidden" name="dtoClass" id="dtoClass" value="yh.cms.content.data.YHCmsContent">
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
      </td>
    </tr>
  </table>
</form>
<form id="formFile" action="<%=contextPath %>/yh/cms/content/act/YHContentAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
</form>
<iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</div>
<div id="showInfo2" name="showInfo2" style="display:none;">
</div>
</body>
</html>