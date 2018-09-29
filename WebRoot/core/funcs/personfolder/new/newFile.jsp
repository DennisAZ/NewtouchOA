<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	String folderPath=request.getParameter("folderPath");
	
	if(seqId==null){
  	seqId="";
	}
	if(folderPath==null){
	  folderPath="";
	}
%>
<%
	String contentIdStr=(String)request.getParameter("contentId");
	String actionFlag=(String)request.getParameter("actionFlag");
	
	String newAttachIdStr=(String)request.getAttribute("newAttachIdStr");
	String newAttachNameStr=(String)request.getAttribute("newAttachNameStr");
	
	if(newAttachIdStr==null){
	  newAttachIdStr="";
	}
	if(newAttachNameStr==null){
	  newAttachNameStr="";
	}
	
	int contentId=0;
	if(contentIdStr != null){
		contentId=Integer.parseInt(contentIdStr);
	}	
	if(actionFlag == null){
		actionFlag = "";
	}
	
	 YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
	 int folderCapacity = loginUser.getFolderCapacity();

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.core.funcs.person.data.YHPerson"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建文件</title>
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
	//alert('newFile.jsp:<%=seqId%>');
var folderCapacity='<%=folderCapacity%>'
var fckContentStr = "";
var seqId='<%=seqId%>';
var requestURL="<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFileContentAct";
function sendForm(){
	//alert("sendForm");
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
		//alert("为空");
  	var subject=$("subject").value;
  	var contentNo=$("contentNo").value;
  	//var content=$("content").value;
  	var attachmentName=$("attachmentName").value.trim();
  	var attachmentDesc=$("attachmentDesc").value;

  	var attach_dir=$("ATTACH_DIR").value;
  	var attach_name=$("ATTACH_NAME").value;
  	
  	if(checkForm()){
  		var fileType=selectType();		
  		//var smsPerson=getSmsCheck();
  		var pars = $('form1').serialize() + "&content=" + encodeURIComponent(oEditor.GetXHTML());
  		var url=requestURL+"/addNewFileInfo.act?seqId="+ seqId + "&fileType=" + fileType + "&contentId=<%=contentId%>&folderPath=<%=folderPath%>";
  	 // var URL=encodeURI(url);
  	  //alert(URL);
  		var rtJson=getJsonRs(url,pars);
  		if(rtJson.rtState == '0'){
  	  	//bindJson2Cntrl(rtJson.rtData);
  	 
  	  	location="<%=contextPath%>/core/funcs/personfolder/folder.jsp?seqId=<%=seqId %>";
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

function checkForm(){
  
	var subject=$("subject").value.trim();
	if(subject==""){
		alert("文件名称不能为空!");
		$("subject").focus();
		$("subject").select();
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
	//var content=$("content").value;
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
		$("newAttachForm").submit();		
	}


	//alert("fileType:"+fileType);
	//var url=requestURL+"/createFile.act?seqId=<%=seqId%>&contentId=<%=contentId%>&fileType="+fileType+"&attachmentName="+attachmentName+"&subject="+subj+"&contentNo="+contentNo+"&content="+content+"&atttDesc="+atttDesc;
	//var URL=encodeURI(url);
	//location.href=URL
}

function editNewAttach(){
  var newAttachIdStr='<%=newAttachIdStr%>';
  var newAttachNameStr='<%=newAttachNameStr%>'
  if(newAttachIdStr){
  //alert("newAttId>>" + '<%=newAttachIdStr%>' +"  newAttName>>>" + '<%=newAttachNameStr%>');
  	office(newAttachNameStr,newAttachIdStr,"file_folder",4,'','');
  }
}



function doInit(){
	//$("content").focus();
	var showFlag=treeSize();
	//alert("showFlag>>"+showFlag);
	if(showFlag!=0){
		$("subject").focus();
	}
	editNewAttach();
	//alert("contentId>>"+'<%=contentId%>');
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
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
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
      cancelButtonId : "btnCancelBatchFile"
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
  if(checkForm()){
    var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
	 	  
	  $("subject1").value=$("subject").value;
	  $("contentNo1").value=$("contentNo").value;
	  $("content1").value=oEditor.GetXHTML();
	  $("attachmentName1").value=$("attachmentName").value.trim();
	  $("attachmentDesc1").value=$("attachmentDesc").value;
	  $("fileType1").value=selectType();

	  if($("ATTACH_DIR").value){
	  	$("ATTACHMENT_ID").value=$("ATTACH_DIR").value;
			$("ATTACHMENT_NAME").value=$("ATTACH_NAME").value;
			$("DISK_ID_Flag").value=$("DISK_ID").value;
		 }
	$("retrunFlag").value="returnNew";
	  
    var actionURL = "<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFileContentAct/newFileSingleUpload.act?failseFlag=returnNew&seqId=<%=seqId%>&contentId=<%=contentId %>";
    $("formFile").action = actionURL;
	$("formFile").submit();
  }  
}

function showAttName(conStr){
	//alert("showAttName contentId>>"+conStr);
	//alert("actionFlag>>"+'<%=actionFlag %>');
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

		
		for(var j=0;j<attIdArray.length-1;j++){
			//alert("attId>>>" + attIdArray[j] + "   attName>>>"+attNameArray[j]);
			$("returnAttId").value += "," + attIdArray[j];
			$("returnAttName").value += "*" + attNameArray[j];

			$("imgattachmentId").value += "," + attIdArray[j];
			$("imgattachmentName").value += "*" + attNameArray[j];
			
		//	alert(attIdArray[j]);
			
		}

		//attachMenuUtil("attr","file_folder",null,$('returnAttName').value ,$('returnAttId').value,false,"","","","","",'<%=contentId%>');

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
	//alert("attaId>>"+attachId +"  attaName>>"+ attachName +"  attrchIndex>>"+attrchIndex);
	var url = requestURL + "/updateAttchNameById.act?contentId=" + attrchIndex +"&attId=" + attachId + "&attName=" + attachName;
	var json=getJsonRs(encodeURI(url));
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


//插入图片
function InsertImage(src){
 	var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
 	if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG )  	{
 		oEditor.InsertHtml( "<img src='"+ src  + "'/>") ;
 	}
}


function treeSize() {
  var flag=1;
  var requestUrl = contextPath + "/yh/core/funcs/personfolder/act/YHFolderSizeAct";
  var url = requestUrl + "/getFolderSize.act";
  var json = getJsonRs(url);
  if (json.rtState == '0'){
    var size = json.rtData.fileSize;
    //alert("size>>>>"+size  +"  folderCapacity>>>"+folderCapacity);
    //var total=folderCapacity-size;
    //alert(size);
    if(folderCapacity == 0){
    	$("formDiv").show();
    }else{
   	  if(size<=0){
 	      $("showInfo").innerHTML="您的个人文件柜已超过容量限制(<%=folderCapacity%> MB)，请清除您的无用文件！";
 	      //$("formDiv").hide();
 				$("returnDiv").show();
 				flag=0;
 	    }else{
 	      $("formDiv").show();
 	      flag=size;
 	    } 
    }
     
  }
  //alert("flag>>"+flag);
  return flag;
}

</script>
</head>
<body onload="doInit();" >	 

<div id="formDiv" style="display:none; ">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath%>/core/funcs/filefolder/images/notify_new.gif" align="middle"><b><span class="Big1"> 新建文件</span></b>
    </td>
  </tr>
</table>

<form name="form1"  id="form1" enctype="multipart/form-data" action=""  method="post"  onsubmit="">
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
      <input type = "hidden" id="returnAttName" name="returnAttName"></input>
       <input type = "hidden" id="returnAttId" name="returnAttId"></input>
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
      <input type="hidden" name="ATTACHMENT_ID_OLD1" id="ATTACHMENT_ID_OLD" value="">
      <input type="hidden" name="ATTACHMENT_NAME_OLD1" id="ATTACHMENT_NAME_OLD" value="">
      
      <input type="hidden" id="moduel" name="moduel" value="file_folder">
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

  <tr align="center" class="TableControl">
    <td colspan="2" nowrap>     
      <input type="button" value="确定" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
      <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/core/funcs/personfolder/folder.jsp?seqId=<%=seqId %>'">
    </td>
  </tr>
</table>
</form>

<div>
 <form id="formFile"  aname="formFile" ction="" method="post" enctype="multipart/form-data" >
	  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
	  <input id="subject1" name="subject1" type="hidden" value="">
	  <input id="contentNo1" name="contentNo1" type="hidden" value="">
	   <input id="content1" name="content1" type="hidden" value="">
	  <input id="attachmentName1" name="attachmentName1" type="hidden" value="">
	  <input id="fileType1" name="fileType1" type="hidden" value="">
	  <input id="attachmentDesc1" name="attachmentDesc1" type="hidden" value="">

		<input id="actionFlag" name="actionFlag" type="hidden"	value="<%=actionFlag%>"> 
		<input id="retrunFlag"	name="retrunFlag" type="hidden" value="">   
		<input id="returnFolderFlag"	name="returnFolderFlag" type="hidden" value=""> 

	   
	   <input type="hidden" name="ATTACH_DIR" id="ATTACHMENT_ID" value="">
     <input type="hidden" name="ATTACH_NAME" id="ATTACHMENT_NAME" value="">
     <input type="hidden" name="DISK_ID" id="DISK_ID_Flag" value="">
 </form>
<iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</div>

<div>
<form id="newAttachForm" name="newAttachForm" action="<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFileContentAct/createFile.act?seqId=<%=seqId%>&contentId=<%=contentId%>" method="post">
	<input type="hidden" id="newSubject" name="newSubject" value="">
	<input type="hidden" id="newAttachmentName" name="newAttachmentName" value="">
	<input type="hidden" id="newContentNo" name="newContentNo" value="">	
	<input type="hidden" id="newAtttDesc" name="newAtttDesc" value="">
	<input type="hidden" id="newContent" name="newContent" value="">
	<input type="hidden" id="newFileType" name="newFileType" value="">
	
	<input id="newRetrunFlag"	name="newRetrunFlag" type="hidden" value="returnNew">  
</form>
</div>

</div>

<div id="returnDiv" style="display: none">
<table class="MessageBox" align="center" width="500">
  <tr>
    <td class="msg warning">
      <h4 class="title">警告</h4>
      <div class="content" style="font-size:12pt"><span id="showInfo"></span></div>
    </td>
  </tr>
</table>
<br>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="javascript:location.href='<%=contextPath%>/core/funcs/personfolder/folder.jsp?seqId=<%=seqId %>'">
</div>
</div>



</body>
</html>