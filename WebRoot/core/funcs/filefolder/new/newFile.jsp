<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String seqId = request.getParameter("seqId");
	String folderPath = request.getParameter("folderPath");
	if (seqId == null) {
		seqId = "";
	}
	if (folderPath == null) {
		folderPath = "";
	}
%>
<%
  String actionFlag = (String) request.getParameter("actionFlag");
	String returnAttId = (String) request.getParameter("returnAttId");
	String returnAttName = (String) request.getParameter("returnAttName");
	String contentIdStr = (String) request.getParameter("contentId");
	
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
	

	int contentId = 0;

	if (actionFlag == null) {
		actionFlag = "";
	}
	if (returnAttId == null) {
		returnAttId = "";
	}
	if (returnAttName == null) {
		returnAttName = "";
	}
	if (contentIdStr != null) {
		contentId = Integer.parseInt(contentIdStr);
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建文件</title>
<link rel="stylesheet"
	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/orgselect.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript">
	//alert('newFile.jsp:<%=seqId%>');
//alert("actionFlag>>"+'<%=actionFlag%>' +"  contentId>>>"+ '<%=contentId%>'+"  returnAttId>>" + '<%=returnAttId%>'+"  returnAttName>>" +'<%=returnAttName%>');
//alert('<%=newAttachIdStr%>' +"  >>>" + '<%=newAttachNameStr%>');
var fckContentStr = "";
var seqId='<%=seqId%>';
var requestURL="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct";
function sendForm(){
	//alert("sendForm");
	//取得fck内容
	 var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
	 //var fckContentStr=encodeURIComponent(oEditor.GetXHTML());
	 //alert(fckContentStr);
	
	 var ATTACHMENT_div=$("ATTACHMENT_div").innerHTML;
	 //alert(ATTACHMENT_div);
	  if(ATTACHMENT_div){
			
			$("ATTACHMENT_ID").value=$("ATTACH_DIR").value;
			$("ATTACHMENT_NAME").value=$("ATTACH_NAME").value;
			$("DISK_ID_Flag").value=$("DISK_ID").value;
			//alert($("ATTACHMENT_ID").value +"  >>>" + $("ATTACHMENT_NAME").value  +">>>>"+ $("DISK_ID").value);
			    
	 	  $("returnFolderFlag").value="returnFolder";  
	 	  upload_attach();

	  	   
			
	  }else{
	  	var subject=$("subject").value;
	  	var contentNo=$("contentNo").value;
	  	//var content=$("content").value;
	  	var attachmentName=$("attachmentName").value.trim();
	  	var attachmentDesc=$("attachmentDesc").value;

	  	var attach_dir=$("ATTACH_DIR").value;
	  	var attach_name=$("ATTACH_NAME").value;
	  	
	  	if(checkForm()){
		  	//alert("content>>>"+content);
	  		var fileType=selectType();		
	  		var smsPerson=getSmsCheck();
	  		var mobileSmsPerson=getMobileSmsCheck();
	  		//alert(smsPerson);
	  		var pars = $('form1').serialize() + "&content=" + encodeURIComponent(oEditor.GetXHTML());
	  		//var url=requestURL+"/addNewFileInfo.act?seqId="+ seqId + "&fileType=" + fileType + "&subject="+ subject+ "&contentNo=" +contentNo +"&attachmentName="+attachmentName+"&attachmentDesc="+attachmentDesc+"&contentId=<%=contentId%>&folderPath=<%=folderPath%>&smsPerson="+smsPerson;
	  		var url=requestURL+"/addNewFileInfo.act?seqId="+ seqId + "&fileType=" + fileType +"&contentId=<%=contentId%>&folderPath=<%=folderPath%>&smsPerson="+smsPerson + "&mobileSmsPerson=" + mobileSmsPerson;
	  	  //var URL=url;
	  	  var enUrl=encodeURI(url);
	  		var rtJson=getJsonRs(enUrl,pars);
	  		if(rtJson.rtState == '0'){
	  	  	//bindJson2Cntrl(rtJson.rtData);
	  	 
	  	  	location="<%=contextPath%>/core/funcs/filefolder/folder.jsp?seqId="+seqId;
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

function checkForm(){
	var subject=$("subject").value.trim();
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
	
	if(checkName()==1){
		return true;
	}else{
	  return false;
	}
	
	return true;	
	
}

function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return str.match(re); 
}

function newAttach(){
  var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;

	var attachmentName=$("attachmentName").value.trim();
	var subject=$("subject").value;
	var contentNo=$("contentNo").value;
	var atttDesc=$("attachmentDesc").value;
	var fileType=selectType();

	
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
	$("newFileType").value=fileType;

	if(checkForm()){
		if(checkStr(attachmentName)){
			alert("不能包含有以下字符/\:*<>?\"|！");
			$("attachmentName").select();
			$("attachmentName").focus();
			return false;
		}
		var pars = Form.serialize($('newAttachForm'));
		location.href = "<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct/createFile.act?" + pars + "&seqId=<%=seqId%>&contentId=<%=contentId%>";
		//$("newAttachForm").submit();
		
	}
		
}


function editNewAttach(){
  var newAttachIdStr='<%=newAttachIdStr%>';
  var newAttachNameStr="<%=newAttachNameStr%>";
  //var ccccd=encodeURIComponent(newAttachNameStr);
  //alert(ccccd);
  if(newAttachIdStr){
  //alert("newAttId>>" + '<%=newAttachIdStr%>' +"  newAttName>>>" + '<%=newAttachNameStr%>');
  	office(newAttachNameStr,newAttachIdStr,"file_folder",4,'','');
  }
}



function doInit(){
	$("subject").focus();
	editNewAttach();
	getSysRemind("smsRemindDiv","",16);
	moblieSmsRemind("sms2RemindDiv","",16);	
	var conStr='<%=contentId%>';	
	if(conStr!=0){	
		showAttName(conStr);
	}
}

//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function initSwf() {
  var linkColor = document.linkColor;
  var settings = {
    flash_url : "<%=contextPath%>/core/cntrls/swfupload.swf",
    upload_url: "",
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

    button_image_url: "<%=imgPath%>/uploadx4.gif",
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
  //document.getElementById("formFile").submit();
  if(checkForm()){
		var smsPerson1 = getSmsCheck();
		var mobileSmsPerson1 = getMobileSmsCheck();
		$('smsPerson1').value = smsPerson1;
		$('mobileSmsPerson1').value = mobileSmsPerson1;
    var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
	 	  
	  $("subject1").value=$("subject").value;
	  $("contentNo1").value=$("contentNo").value;
	  $("content1").value=oEditor.GetXHTML();
	  $("attachmentName1").value=$("attachmentName").value.trim();
	  $("attachmentDesc1").value=$("attachmentDesc").value;
	  $("fileType1").value=selectType();

		$("ATTACHMENT_ID").value=$("ATTACH_DIR").value;
		$("ATTACHMENT_NAME").value=$("ATTACH_NAME").value;
		$("DISK_ID_Flag").value=$("DISK_ID").value;
		//alert($("ATTACHMENT_ID").value +"  >>>" + $("ATTACHMENT_NAME").value  +">>>>"+ $("DISK_ID").value);
		    
 	  //$("attachAction").value="newAttach";  
		//var newAttAchrr=$("attachAction").value;

	  if($("ATTACH_DIR").value){
	  	$("ATTACHMENT_ID").value=$("ATTACH_DIR").value;
			$("ATTACHMENT_NAME").value=$("ATTACH_NAME").value;
			$("DISK_ID_Flag").value=$("DISK_ID").value;
		 }
			$("retrunFlag").value="returnNew";
	  
  	//$("btnFormFile").click();
	  $("formFile").submit();
  }  
}

//插入图片
function InsertImage(src){
 	var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
 	if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG )  	{
 		oEditor.InsertHtml( "<img src='"+ src  + "'/>") ;
 	}
}



function showAttName(conStr){
	//alert("showAttName>>"+conStr);
	//alert("actionFlag>>"+'<%=actionFlag%>');
	var url=requestURL + "/getFileContentInfoById.act?contentId="+conStr;
	var json=getJsonRs(url);
	//alert(rsText);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson=json.rtData;
	if(prcsJson.length>0){
		var prcs=prcsJson[0];
	  $("subject").value=prcs.subject;
		$("contentNo").value=prcs.contentNo;
		fckContentStr=prcs.content;
		
		$("attachmentDesc").value=prcs.attachmentDesc;
		//$("attName").innerHTML=prcs.attachmentName;
		var attIdArray = prcs.attachmentId.split(",");
		var attNameArray = prcs.attachmentName.split("*");

		var attId = $("returnAttId").value;
		var attName = $("returnAttName").value;
		
		for(var k=0;k<attNameArray.length-1;k++){
			//attName += "," + attNameArray[k];
		}	
		//$("attName").innerHTML=attachmentName;		

			var returnAttIdStr = "";
			var returnAttNameStr = "";
		for(var j=0;j<attIdArray.length-1;j++){
			$("returnAttId").value += attIdArray[j] +",";
		  $("returnAttName").value += attNameArray[j]+"*";

			$("imgattachmentId").value += attIdArray[j] +",";
		  $("imgattachmentName").value += attNameArray[j]+"*";
			
		}
	
		//if(attIdArray.length>0){
		 // returnAttIdStr = returnAttIdStr.substr(0,returnAttIdStr.length-1);
		 //  returnAttNameStr = returnAttNameStr.substr(0,returnAttNameStr.length-1);
	    //}
		//$("returnAttId").value = returnAttIdStr;
	   // $("returnAttName").value = returnAttNameStr;
	   //attachMenuUtil(container,moduleName,deleteAction,attachNames,attachIds,readOnly,prec , code,isCanPrint,canDown,opFlag,seqId)
	   
			//attachMenuUtil("attr","file_folder",null,$("returnAttName").value ,$("returnAttId").value,false,"","","","","",'<%=contentId%>');

	  var  selfdefMenu = {
      	office:["downFile","dump","read","edit","deleteFile"], 
        img:["downFile","dump","play","deleteFile","insertImg"],  
        music:["downFile","dump","play","deleteFile"],  
		    video:["downFile","dump","play","deleteFile"], 
		    others:["downFile","dump","deleteFile"]
			}
		attachMenuSelfUtil(attr,"file_folder",$('returnAttName').value ,$('returnAttId').value, '','','<%=contentId%>',selfdefMenu);
		

	}

}


//浮动菜单文件的删除
function deleteAttachBackHand(attachName,attachId,attrchIndex){
	var url= requestURL +"/delFloatFile.act?attachId=" + attachId +"&attachName=" + encodeURIComponent(attachName) + "&contentId=" + attrchIndex;
  //alert(attachId  +"  " +attachName +"   " +attrchIndex);
  //alert(url);
	var json=getJsonRs(url);
	if(json.rtState =='1'){
		alert(json.rtMsrg);
		return false;
	}else{
	  prcsJson=json.rtData;
		var updateFlag=prcsJson.updateFlag;
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
    var checkUrl = requestURL + "/checkSubjectName.act?seqId=<%=seqId%>&contentId=<%=contentId%>";
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

//判断是否要显示短信提醒 
function getSysRemind(remidDiv,remind,type){ 
	var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=" + type; 
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
		$(remidDiv).style.display = ''; 
		if(defaultRemind=='1'){ 
			if(remind){
				$(remind).checked = true; 
			}
		} 
	}
	if(remind){
		if(document.getElementById(remind).checked){
			document.getElementById(remind).value = "1";
		}else{
			document.getElementById(remind).value = "0";
		}
	}
}


/** 
*js代码 
*是否显示手机短信提醒 
*/ 
//判断是否要显示手机短信提醒 
function moblieSmsRemind(remidDiv,remind,type){
	var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=" + type; 
	var rtJson = getJsonRs(requestUrl); 
	if(rtJson.rtState == "1"){ 
		alert(rtJson.rtMsrg); 
		return ; 
	} 
	var prc = rtJson.rtData; 
	//alert(rsText);
	var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
	if(moblieRemindFlag == '2'){ 
		$(remidDiv).style.display = '';
		if(remind){
			$(remind).checked = true;
		}
	}else if(moblieRemindFlag == '1'){ 
		$(remidDiv).style.display = '';
		if(remind){
			$(remind).checked = false;
		}
	}else{
		$(remidDiv).style.display = 'none'; 
	}
	if(remind){
		if(document.getElementById(remind).checked){
			document.getElementById(remind).value = "1";
		}else{
			document.getElementById(remind).value = "0";
		}
	}
}







</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3"
	class="small">
	<tr>
		<td class="Big"><img
			src="<%=contextPath%>/core/funcs/filefolder/images/notify_new.gif"
			align="middle"><b><span class="Big1"> 新建文件</span></b></td>
	</tr>
</table>

<form action="" method="post" name="form1" id="form1" onsubmit="">
<input type="hidden" value="yh.core.funcs.filefolder.data.YHFileContent"
	name="dtoClass">
<table class="TableBlock" width="100%" align="center">
	<tr>
		<td nowrap class="TableData">文件名称：</td>
		<td class="TableData">
			<input type="text" name="subject"	id="subject" size="50" maxlength="100" class="BigInput" value="" onblur="clearErrSubjName();">
			<span id="errorSubject"></span>
		</td>
	</tr>
	<tr>
		<td nowrap class="TableData">排序号：</td>
		<td class="TableData"><input type="text" name="contentNo"
			id="contentNo" size="10" maxlength="20" class="BigInput" value="">
		</td>
	</tr>
	<tr>
		<td nowrap class="TableData" valign="top">文件内容：</td>
		<td class="TableData">
		<%-- <div><textarea rows="13" cols="80%" id="content" name="content"></textarea></div>--%>
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
			<td class="TableData">
			<input type="hidden" id="returnAttName"	name="returnAttName">
			<input type="hidden" id="returnAttId"	name="returnAttId">
		<span id="attr"></span>
		</td>
	</tr>
	<tr height="25">
		<td nowrap class="TableData">新建附件：</td>
		<td class="TableData"><input type="radio" onclick="selectType();"	name="DOC_TYPE" value="doc" id="NEW_TYPE1">	<label for="NEW_TYPE1">Word文档</label> 
		<input type="radio"	onclick="selectType();" name="DOC_TYPE" value="xls" id="NEW_TYPE2"><label	for="NEW_TYPE2">Excel文档</label> 
		<input type="radio"	onclick="selectType();" name="DOC_TYPE" value="ppt" id="NEW_TYPE3"><label	for="NEW_TYPE3">PPT文档</label>&nbsp;&nbsp; <b>附件名：</b>
		<input type="text" name="attachmentName" id="attachmentName" size="20" class="SmallInput" value="新建文档">
		<input type="button" class="SmallButtonW" value="新建附件" onclick="newAttach();"></td>
	</tr>
	<tr height="25">
		<td nowrap class="TableData">附件选择：</td>
		<td class="TableData"><script>ShowAddFile();</script> <script>ShowAddImage('','img');</script>
			<script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
			<input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
			<input type="hidden" name="ATTACHMENT_NAME_OLD"	id="ATTACHMENT_NAME_OLD" value="">
			
			<input type="hidden" id="moduel" name="moduel" value="file_folder">
      <input type="hidden" id="imgattachmentId" name="imgattachmentId">
			<input type="hidden" id="imgattachmentName" name="imgattachmentName">
		</td>
	</tr>
	<tr>
		<td nowrap class="TableData">附件说明：</td>
		<td class="TableData"><input type="text" name="attachmentDesc" id="attachmentDesc" size="50" maxlength="50" class="BigInput" value=""></td>
	</tr>
	<tr id="smsRemindDiv" style="display: none">
		<td nowrap class="TableData">内部短信提醒：</td>
		<td class="TableData">
		<input type="radio"	name="SMS_SELECT_REMIND" id="SMS_SELECT_REMIND0" value="0" onclick="document.getElementById('SMS_SELECT_REMIND_SPAN').style.display='';"
			checked><label for="SMS_SELECT_REMIND0">手动选择被提醒人员</label> 
			<input 	type="radio" name="SMS_SELECT_REMIND" id="SMS_SELECT_REMIND1"	value="1"	onclick="document.getElementById('SMS_SELECT_REMIND_SPAN').style.display='none';">
			<label	for="SMS_SELECT_REMIND1">提醒全部有权限人员</label><br>
		<span id="SMS_SELECT_REMIND_SPAN"> <input type="hidden"	name="user" id="user" value=""> 
		<textarea cols=40	name="userDesc" id="userDesc" rows="2" class="BigStatic" wrap="yes"	readonly></textarea>
		<a href="javascript:;" class="orgAdd"	onClick="selectUser();">添加</a> 
		<a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
		</span></td>
	</tr>
	
	<tr id="sms2RemindDiv" style="display: none">
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
		<input type="button" value="确定" 	onclick="sendForm();" class="BigButton">&nbsp;&nbsp; 
		<input type="button" value="返回" class="BigButton"	onClick="location='<%=contextPath%>/core/funcs/filefolder/folder.jsp?seqId=<%=seqId%>'">
		</td>
	</tr>
</table>
</form>

<div>
<form id="formFile"	action="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct/newFileSingleUpload.act?seqId=<%=seqId%>&contentId=<%=contentId%>&folderPath=<%=folderPath%>"
	method="post" enctype="multipart/form-data">
	<input id="btnFormFile" name="btnFormFile" type="submit"	style="display: none;">
	<input id="subject1" name="subject1" type="hidden" value=""> 
	<input id="contentNo1"	name="contentNo1" type="hidden" value=""> 
	<input id="content1"	name="content1" type="hidden" value="">
	<input id="attachmentName1" name="attachmentName1" type="hidden" value="">
	<input id="fileType1" name="fileType1" type="hidden" value=""> 
	<input	id="attachmentDesc1" name="attachmentDesc1" type="hidden" value="">
	
	<input id="actionFlag" name="actionFlag" type="hidden"	value="<%=actionFlag%>"> 
	<input id="retrunFlag"	name="retrunFlag" type="hidden" value="">   
	<input id="returnFolderFlag"	name="returnFolderFlag" type="hidden" value=""> 
	
	<input type="hidden" name="ATTACH_DIR" id="ATTACHMENT_ID" value="">
	<input type="hidden" name="ATTACH_NAME" id="ATTACHMENT_NAME" value="">
  <input type="hidden" name="DISK_ID" id="DISK_ID_Flag" value="">
		
	<input id="smsPerson1" name="smsPerson1" type="hidden" value=""> 
	<input id="mobileSmsPerson1" name="mobileSmsPerson1" type="hidden" value="">
		
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

</form>
</div>
</body>
</html>