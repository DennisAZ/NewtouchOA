<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	if(seqId==null){
  	seqId="0";
	}
	String contentId=request.getParameter("contentId");
	if(contentId==null){
	  contentId="";
	}
%>
<%
	String newAttachIdStr=(String)request.getAttribute("newAttachIdStr");
	String newAttachNameStr=(String)request.getAttribute("newAttachNameStr");
	
	if(newAttachIdStr==null){
	  newAttachIdStr="";
	}
	if(newAttachNameStr==null){
	  newAttachNameStr="";
	}else {
		newAttachNameStr=YHUtility.encodeSpecial(newAttachNameStr);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑文件</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<!-- 文件上传 -->
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var contentId = "<%=contentId%>";
var fckContentStr = "";
function checkForm(){
	var subject=$("subject").value;
	if(subject==""){
		alert("文件名称不能为空!");
		$("subject").focus();
		return false;
	}
	if(checkStr(subject)){
		alert("不能包含有以下字符/\:*<>?\"|！");
		$("subject").select();
		$("subject").focus();
		return false;
	}
	//if(checkName()==1){
	//	return true;
	//}else{
	 // return false;
	//}
	return true;	
}
function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return str.match(re); 
}
var requestURL = "<%=contextPath%>/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct";
function sendForm(){
  var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
  var ATTACHMENT_div=$("ATTACHMENT_div").innerHTML;
  //alert(ATTACHMENT_div);
  if(ATTACHMENT_div){
		//alert("有值");
		$("ATTACHMENT_ID").value=$("ATTACH_DIR").value;
		$("ATTACHMENT_NAME").value=$("ATTACH_NAME").value;
		$("DISK_ID_Flag").value=$("DISK_ID").value;
		//alert($("ATTACHMENT_ID").value +"  >>>" + $("ATTACHMENT_NAME").value  +">>>>"+ $("DISK_ID").value);
 	  $("returnFolderFlag").value="returnFolder";  
 	  upload_attach();
  }else{
  	if(checkForm()){
  		var fileType = selectType();		
  		var smsPerson = getSmsCheck();
  		var mobileSmsPerson = getMobileSmsCheck();
  		var folderPath = getFilePath('<%=seqId%>');
  		var pars = $('form1').serialize() + "&content=" + encodeURIComponent(oEditor.GetXHTML());
  		var url = requestURL + "/updateFileInfoById.act?contentId="+contentId+"&sortId="+seqId +"&fileType="+fileType +"&smsPerson="+smsPerson + "&mobileSmsPerson=" + mobileSmsPerson +"&folderPath=" + encodeURIComponent(folderPath);
  		var rtJson=getJsonRs(url,pars);
  		if(rtJson.rtState == '0'){
  			location.href = "<%=contextPath%>/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/folder.jsp?seqId=<%=seqId%>";
  	  }else{
  		 	  alert(rtJson.rtMsrg); 
  		}	
  	}
  }
}

function selectType(){
	var docTypes=document.getElementsByName("DOC_TYPE");
	var docType="";
	for(var i=0;i<docTypes.length;i++){
		if(docTypes[i].checked){
			docType=docTypes[i].value;
		}
	}
	return docType;
}

function getSmsCheck(){
  var smsPerson="";
  var smsArry = document.getElementsByName("SMS_SELECT_REMIND");
  if(smsArry[0].checked){
		smsPerson = $("user").value;
  }else if(smsArry[1].checked){
		smsPerson = "allPrivPerson";
  }
  return smsPerson;
}

function getMobileSmsCheck(){
  var smsPerson="";
  var smsArry = document.getElementsByName("moblieSMS_SELECT_REMIND");
  if(smsArry[0].checked){
		smsPerson = $("user1").value;
  }else if(smsArry[1].checked){
		smsPerson = "allPrivPerson";
  }
  //alert(smsPerson);
  return smsPerson;
}
/**
 * 获取路径
 * @param seqId
 * @return
 */
function getFilePath(seqId){
	var returnFilePath="";
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct";
	var url = requestURLStr + "/getSortNameById.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
  	var data = rtJson.rtData;
  	returnFilePath = data.folderPath;
  } else {
    alert(rtJson.rtMsrg); 
  }
	return returnFilePath;
}

function newAttach(){
	var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
	var attachmentName = $("attachmentName").value;
	var subject = $("subject").value;
	var contentNo = $("contentNo").value;
	var atttDesc = $("attachmentDesc").value;
	var fileType = selectType();
	
	var subj="";
	if(fileType==""){
		alert("请选择文件类型！");
		return (false);
	}
	if(trim(attachmentName)==""){
		alert("附件名不能为空!");
		return (false);
	}
	if(trim(subject)==""){
		$("subject").value= attachmentName;		
		subj=attachmentName
	}else{
		subj=subject;
	}	
	$("newAttachmentName").value=attachmentName;
	$("newSubject").value = subj;
	$("newContentNo").value =contentNo;
	$("newContent").value = oEditor.GetXHTML();
	$("newAtttDesc").value = atttDesc;
	$("newFileType").value = fileType;
	if(checkForm()){
		if(checkStr(attachmentName)){
			alert("不能包含有以下字符/\:*<>?\"|！");
			$("attachmentName").select();
			$("attachmentName").focus();
			return false;
		}
		var pars = Form.serialize($('newAttachForm'));
		location.href = "<%=contextPath%>/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct/newAttrachFile.act?" + pars + "&seqId=<%=seqId%>&contentId=<%=contentId%>";
	}
}
function editNewAttach(){
  var newAttachIdStr='<%=newAttachIdStr%>';
  var newAttachNameStr="<%=newAttachNameStr%>";
  if(newAttachIdStr){
  	office(newAttachNameStr,newAttachIdStr,"confidential",4,'','');
  }
}

function doInit(){
	editNewAttach();
	moblieSmsRemind("sms2remindDiv");
	getSysRemind("sms1remindDiv");
	var url = requestURL + "/getFileContentInfoById.act?contentId="+contentId;
	var json = getJsonRs(url);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	bindJson2Cntrl(json.rtData);
	var data = json.rtData;
  $('returnAttId').value = data.attachmentId;
  $('returnAttName').value = data.attachmentName;
  
	$("imgattachmentId").value = data.attachmentId;
	$("imgattachmentName").value = data.attachmentName;
  $('attachmentName').value = "";
  fckContentStr = data.content;
  var  selfdefMenu = {
     	office:["downFile","dump","read","edit","deleteFile"], 
       img:["downFile","dump","play","deleteFile","insertImg"],  
       music:["downFile","dump","play","deleteFile"],  
	    video:["downFile","dump","play","deleteFile"], 
	    others:["downFile","dump","deleteFile"]
		}
	attachMenuSelfUtil(attr,"confidential",$('returnAttName').value ,$('returnAttId').value, '','','<%=contentId%>',selfdefMenu);
}

//重命名文件function renameFile(attachName,attachId,moudel){
	//alert(attachName +" id>"+attachId +" Mod>"+moudel);
	//newWindow("<%=contextPath%>/core/funcs/netdisk/rename.jsp?seqId=<%=seqId%>&DISK_ID=" + getPath() + "&FILE_NAME=" + file_list);
	var url=contextPath + "/core/funcs/filefolder/renameFile.jsp?contentId=<%=contentId%>&attachName=" + encodeURIComponent(attachName) +"&attachId=" + attachId + "&moudel=" + moudel;
	//alert(url);
	loc_x = (screen.availWidth-300)/2;
	loc_y = (screen.availHeight-300)/2;
	window.open(url, "confidential","height=200,width=350,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
			+ loc_y + ", left=" + loc_x + ", resizable=yes");
}

//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;
function initSwf() {
  var linkColor = document.linkColor;
  var settings = {
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
    upload_url: "",
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

function upload_attach(){
  if(checkForm()){
    var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
	  $("subject1").value=$("subject").value;
	  $("contentNo1").value=$("contentNo").value;
	  $("content1").value=oEditor.GetXHTML();
	  $("attachmentName1").value=$("attachmentName").value;
	  $("attachmentDesc1").value=$("attachmentDesc").value;
	  $("fileType1").value=selectType();
	  if($("ATTACH_DIR").value){
	  	$("ATTACHMENT_ID").value=$("ATTACH_DIR").value;
			$("ATTACHMENT_NAME").value=$("ATTACH_NAME").value;
			$("DISK_ID_Flag").value=$("DISK_ID").value;
		 }
			$("retrunFlag").value="returnEdit";
	  $("formFile").submit();
  }  
}

//浮动菜单文件的删除function deleteAttachBackHand(attachName,attachId,attrchIndex){
	var url= requestURL +"/delFloatFile.act?attachId=" + attachId +"&attachName=" + encodeURIComponent(attachName) + "&contentId=" + attrchIndex;
	var json=getJsonRs(url);
	if(json.rtState =='1'){
		alert(json.rtMsrg);
		return false;
	}else{
	  prcsJson=json.rtData;
		var updateFlag = prcsJson.updateFlag;
		if(updateFlag){
			//alert(updateFlag);
		  return true;
		}else{
			return false;
		}
	}
}

function FCKeditor_OnComplete( editorInstance ) {
  editorInstance.SetData( fckContentStr ) ;
}

function checkName(){
  var flag=0;
  var subjectName= $("subject").value;

  if(trim(subjectName)){
    //alert("checkName>>>" +subjectName);
    var pars = $('form1').serialize();
    var checkUrl = requestURL + "/checkEditSubjectName.act?seqId=<%=seqId%>&contentId=<%=contentId%>";
    var rtJson = getJsonRs(checkUrl,pars);

    //alert(rsText);
    if(rtJson.rtState == '0'){
      var prcsJsonStr = rtJson.rtData;
  		var isHaveFlag = prcsJsonStr.isHaveFlag;
  		//alert(isHaveFlag);
  		if(isHaveFlag==1){
			  $("errorSubject").innerHTML="<b style='color:red'>该文件已经存在,请重新输入!";

				$("subject").focus();
				$("subject").select();
				
			}else{
			  //$("errorSubject").innerHTML="<b style='color:green'>文件可以新建!";
			  flag=1;
			}
    }
  }
  return flag;
}

function clearErrSubjName(){
  $("errorSubject").innerHTML="";  
}

//插入图片
function InsertImage(src){
 	var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
 	if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG )  	{
 		oEditor.InsertHtml( "<img src='"+ src  + "'/>") ;
 	}
}

/** 
*js代码 
*是否显示手机短信提醒 
*/ 
function moblieSmsRemind(remidDiv){ 
	var moblieSmsUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=31"; 
	var rtJson = getJsonRs(moblieSmsUrl); 
	if(rtJson.rtState == "1"){ 
		alert(rtJson.rtMsrg); 
		return ; 
	} 
	var prc = rtJson.rtData; 
	var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
	//alert(moblieRemindFlag);
	if(moblieRemindFlag == '2'){ 
		$(remidDiv).style.display = ''; 
	}else if(moblieRemindFlag == '1'){ 
		$(remidDiv).style.display = ''; 
	}else{ 
		$(remidDiv).style.display = 'none'; 
	} 
}
//判断是否要显示短信提醒 
function getSysRemind(remidDiv,remind){ 
  var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=31"; 
  var rtJson = getJsonRs(requestUrl); 
    if(rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  } 
  var prc = rtJson.rtData; 
  //alert(rsText);
  var allowRemind = prc.allowRemind;;//是否允许显示 
  var defaultRemind = prc.defaultRemind;//是否默认选中 
  var mobileRemind = prc.mobileRemind;//手机默认选中 
  if(allowRemind=='2'){ 
    $(remidDiv).style.display = 'none'; 
  }else{ 
    if(defaultRemind=='1'){ 
      //$(remind).checked = true; 
    } 
  } 
}

</script>

</head>
<body onload="doInit();">
	 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath%>/core/funcs/filefolder/images/notify_new.gif" align="middle"><b><span class="Big1"> 编辑文件</span></b>
    </td>
  </tr>
</table>

<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1" onsubmit="">
<input type="hidden" value="yh.core.funcs.filefolder.data.YHFileContent" name="dtoClass">
<table class="TableBlock" width="100%" align="center">
  <tr>
    <td nowrap class="TableData"> 文件名称：</td>
    <td class="TableData">
      <input type="text" name="subject" id="subject" size="50" maxlength="100" class="BigInput" value="" onblur="clearErrSubjName();">
      <span id="errorSubject"></span>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData"> 排序号：</td>
    <td class="TableData">
      <input type="text" name="contentNo"  id="contentNo" size="10" maxlength="20" class="BigInput" value="">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData" valign="top"> 文件内容：</td>
    <td class="TableData">
				<%-- <textarea rows="13" cols="80%" id="content" name="content"></textarea>--%>
		<div>
	    <script language=JavaScript>    
		    var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
		    var oFCKeditor = new FCKeditor( 'fileFolder' ) ;
		    oFCKeditor.BasePath = sBasePath ;
		    oFCKeditor.Height = 200;
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
  <tr class="TableContent">
    <td nowrap>附件文档：</td>
    <%--<td nowrap><span id="attName"></span> </td> --%>
      <td class="TableData">
       	<input type = "hidden" id="returnAttId" name="returnAttId"></input>
      	<input type = "hidden" id="returnAttName" name="returnAttName"></input>
       	<span id="attr"></span>
    	</td>
  </tr>
  <tr height="25">
    <td nowrap class="TableData">新建附件：</td>
    <td class="TableData">
      <input type="radio" onclick="selectType();" name="DOC_TYPE" value="doc" id="NEW_TYPE1"><label for="NEW_TYPE1">Word文档</label>
      <input type="radio" onclick="selectType();" name="DOC_TYPE" value="xls" id="NEW_TYPE2"><label for="NEW_TYPE2">Excel文档</label>
      <input type="radio" onclick="selectType();" name="DOC_TYPE" value="ppt" id="NEW_TYPE3"><label for="NEW_TYPE3">PPT文档</label>&nbsp;&nbsp;
      <b>附件名：</b><input type="text" name="attachmentName" id="attachmentName" size="20" class="SmallInput" value="新建文档">
      <input type="button" class="SmallButtonW" value="新建附件" onclick="newAttach();">
      
    </td>
  </tr>
  <tr height="25">
    <td nowrap class="TableData">附件选择：</td>
    <td class="TableData">
       <script>ShowAddFile();</script>
      <script>ShowAddImage('','img');</script>
      <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
      <input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
      <input type="hidden" name="ATTACHMENT_NAME_OLD" id="ATTACHMENT_NAME_OLD" value="">
      
      <input type="hidden" id="moduel" name="moduel" value="confidential">
      <input type="hidden" id="imgattachmentId" name="imgattachmentId">
			<input type="hidden" id="imgattachmentName" name="imgattachmentName">
      
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData"> 附件说明：</td>
    <td class="TableData">
      <input type="text" name="attachmentDesc" id="attachmentDesc" size="50" maxlength="50" class="BigInput" value="">
    </td>
  </tr>
  <tr id="sms1remindDiv" style="">
    <td nowrap class="TableData">内部短信提醒：</td>
    <td class="TableData">
    	<input type="radio" name="SMS_SELECT_REMIND" id="SMS_SELECT_REMIND0" value="0" onclick="document.getElementById('SMS_SELECT_REMIND_SPAN').style.display='';" checked><label for="SMS_SELECT_REMIND0">手动选择被提醒人员</label>
   		<input type="radio" name="SMS_SELECT_REMIND" id="SMS_SELECT_REMIND1" value="1" onclick="document.getElementById('SMS_SELECT_REMIND_SPAN').style.display='none';"><label for="SMS_SELECT_REMIND1">提醒全部有权限人员</label><br>
   		<span id="SMS_SELECT_REMIND_SPAN">
   		<input type="hidden" name="user" id="user" value="">
   		<textarea cols=40 name="userDesc" id="userDesc" rows="2" class="BigStatic" wrap="yes" readonly></textarea>
   		<a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
   		<a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
   		</span>
    </td>
  </tr>
  
  <tr id="sms2remindDiv" style="display: none;">
		<td nowrap class="TableData">手机短信提醒：</td>
		<td class="TableData">
		<input type="radio"	name="moblieSMS_SELECT_REMIND" id="moblieSMS_SELECT_REMIND0" value="0" onclick="document.getElementById('moblieSMS_SELECT_REMIND_SPAN').style.display='';"
			checked><label for="moblieSMS_SELECT_REMIND0">手动选择被提醒人员</label> 
			<input 	type="radio" name="moblieSMS_SELECT_REMIND" id="moblieSMS_SELECT_REMIND1"	value="1"	onclick="document.getElementById('moblieSMS_SELECT_REMIND_SPAN').style.display='none';">
			<label	for="moblieSMS_SELECT_REMIND1">提醒全部有权限人员</label><br>
		<span id="moblieSMS_SELECT_REMIND_SPAN">
		<input type="hidden"	name="user1" id="user1" value="">
		 <textarea cols=40	name="userDesc1" id="userDesc1" rows="2" class="BigStatic" wrap="yes"	readonly></textarea> 
		 <a href="javascript:;" class="orgAdd" onClick="selectUser(['user1', 'userDesc1']);">添加</a> 
		 <a href="javascript:;"	class="orgClear" onClick="$('user1').value='';$('userDesc1').value='';">清空</a>
		</span></td>
	</tr>
	
  <tr align="center" class="TableControl">
    <td colspan="2" nowrap>
      <input type="hidden" value="" name="CONTENT_ID">
      <input type="hidden" name="OP" value="">
      <input type="hidden" name="SORT_ID" value="8" >
      <input type="hidden" name="ATTACHMENT_ID_OLD" value="">
      <input type="hidden" name="ATTACHMENT_NAME_OLD" value="">
      <input type="hidden" name="FILE_SORT" value="1" >
      <input type="button" value="确定" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
      <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/folder.jsp?seqId=<%=seqId %>'">
    </td>
  </tr>
</table>
</form>
<div>
 <form id="formFile" action="<%=contextPath%>/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct/newFileSingleUpload.act?seqId=<%=seqId%>&contentId=<%=contentId %>" method="post" enctype="multipart/form-data">
	  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;">
	  <input id="subject1" name="subject1" type="hidden" value="">
	  <input id="contentNo1" name="contentNo1" type="hidden" value="">
	   <input id="content1" name="content1" type="hidden" value="">
	  <input id="attachmentName1" name="attachmentName1" type="hidden" value="">
	  <input id="fileType1" name="fileType1" type="hidden" value="">	
	  <input id="attachmentDesc1" name="attachmentDesc1" type="hidden" value="">
	  
	  <input id="actionFlag" name="actionFlag" type="hidden"	value="edit"> 
		<input id="retrunFlag"	name="retrunFlag" type="hidden" value="">   
		<input id="returnFolderFlag"	name="returnFolderFlag" type="hidden" value="">  
	  
	  <input type="hidden" name="ATTACH_DIR" id="ATTACHMENT_ID" value="">
    <input type="hidden" name="ATTACH_NAME" id="ATTACHMENT_NAME" value="">
    <input type="hidden" name="DISK_ID" id="DISK_ID_Flag" value="">
	   
 </form>
<iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</div>

<div>
<form id="newAttachForm" name="newAttachForm" action="" method="post">
	<input type="hidden" id="newSubject" name="newSubject" value="">
	<input type="hidden" id="newAttachmentName" name="newAttachmentName" value="">
	<input type="hidden" id="newContentNo" name="newContentNo" value="">	
	<input type="hidden" id="newAtttDesc" name="newAtttDesc" value="">
	<input type="hidden" id="newContent" name="newContent" value="">
	<input type="hidden" id="newFileType" name="newFileType" value="">
	<input id="retrunFlag"	name="retrunFlag" type="hidden" value="returnEdit">   
</form>
</div>

</body>
</html>