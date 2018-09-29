<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	if(seqId==null){
  	seqId="";
	}
	String contentId=request.getParameter("contentId");
	if(contentId==null){
	  contentId="";
	}
	
	
	String managerPriSeqId=request.getParameter("managerPriSeqId");
	if(managerPriSeqId==null){
	  managerPriSeqId="0";
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
	//alert('newFile.jsp:<%=seqId%>');
	//alert('edit.jsp:<%=managerPriSeqId%>');
var seqId='<%=seqId%>';
var contentId='<%=contentId%>';
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
	return true;	
	
}

function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return str.match(re); 
}


//var requestURL="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct";
var requestURL="<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFileContentAct";
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
			    
	 	  $("returnFolderFlag").value="returnShareFolder";  
 	  upload_attach();

 	  
		
  }else{
  	if(checkForm()){
  		var fileType=selectType();		
  		var url=requestURL+"/updateFileInfoById.act?contentId="+contentId+"&sortId="+seqId +"&fileType="+fileType;
  		var rtJson=getJsonRs(url,mergeQueryString($("form1"))+"&content=" + encodeURIComponent(oEditor.GetXHTML()));
  		if(rtJson.rtState == '0'){
  	  	bindJson2Cntrl(rtJson.rtData);
  	  	//alert("保存成功！");  	  	
 		    //location="shareFolder.jsp?seqId="+seqId;
 		    location.href="<%=contextPath%>/core/funcs/personfolder/shareFolder.jsp?seqId=<%=seqId %>&managerPriSeqId=<%=managerPriSeqId %>";  			
  	  	
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

function newAttach(){
  var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
  
	var attachmentName=$("attachmentName").value;
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
		$("newAttachForm").submit();
	}
	
	

	//$("newAttachForm").submit();
	//alert("fileType:"+fileType);
	//var url=requestURL+"/newFile.act?seqId=<%=seqId%>&returnFlag=shareEdit&contentId="+contentId+"&fileType="+fileType+"&attachmentName="+attachmentName+"&subject="+subject+"&contentNo="+contentNo+"&content="+content+"&atttDesc="+atttDesc;
	//var URL=encodeURI(url);
	//location.href=URL;
	
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
	//alert("doInit");
	editNewAttach()
	var url=requestURL + "/getFileContentInfoById.act?contentId="+contentId;
	var json=getJsonRs(url);
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
		//var AttacontentId=prcs.contentId;
		//$("attName").innerHTML=prcs.attachmentName;
		var attIdArray = prcs.attachmentId.split(",");
		var attNameArray = prcs.attachmentName.split("*");

		var attId = $("returnAttId").value;
		var attName = $("returnAttName").value;
		
		for(var j=0;j<attIdArray.length-1;j++){
			//alert("attId>>>" + attIdArray[j] + "   attName>>>"+attNameArray[j]);
			$("returnAttId").value += "," + attIdArray[j];
			$("returnAttName").value += "*" + attNameArray[j];
			
			$("imgattachmentId").value += "," + attIdArray[j];
			$("imgattachmentName").value += "*" + attNameArray[j];
			
		}
		//attachMenuUtil("attr","file_folder",null,$('returnAttName').value ,$('returnAttId').value,false,"","","","","",'<%=contentId%>');

		var  selfdefMenu = {
      	office:["downFile","dump","read","edit","deleteFile","rename"], 
        img:["downFile","dump","play","deleteFile","rename","insertImg"],  
        music:["downFile","dump","play","deleteFile"],  
		    video:["downFile","dump","play","deleteFile"], 
		    others:["downFile","dump","deleteFile","rename"]
			}
		attachMenuSelfUtil(attr,"file_folder",$('returnAttName').value ,$('returnAttId').value, '','','<%=contentId%>',selfdefMenu);
	}
}

//重命名文件
function renameFile(attachName,attachId,moudel){
	var url=contextPath + "/core/funcs/personfolder/editRenameFile.jsp?contentId=<%=contentId%>&attachName=" + encodeURIComponent(attachName) +"&attachId=" + attachId + "&moudel=" + moudel;
	//alert(url);
	loc_x = (screen.availWidth-300)/2;
	loc_y = (screen.availHeight-300)/2;
	window.open(url, "personfolder","height=200,width=350,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
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
  //document.getElementById("formFile").submit();
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
			$("retrunFlag").value="shareEdit";
	  
	   var actionURL = "<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFileContentAct/newFileSingleUpload.act?failseFlag=shareEdit&seqId=<%=seqId%>&contentId=<%=contentId %>&managerPriSeqId=<%=managerPriSeqId%>";
	  $("formFile").action = actionURL;
	  $("formFile").submit();
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

//插入图片
function InsertImage(src){
 	var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
 	if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG )  	{
 		oEditor.InsertHtml( "<img src='"+ src  + "'/>") ;
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
<input type="hidden" value="yh.core.funcs.personfolder.data.YHFileContent" name="dtoClass">
<table class="TableBlock" width="100%" align="center">
  <tr>
    <td nowrap class="TableData"> 文件名称：</td>
    <td class="TableData">
      <input type="text" name="subject" id="subject" size="50" maxlength="100" class="BigInput" value="">
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
      <input type="hidden" value="" name="CONTENT_ID">
      <input type="hidden" name="OP" value="">
      <input type="hidden" name="SORT_ID" value="8" >
      <input type="hidden" name="ATTACHMENT_ID_OLD" value="">
      <input type="hidden" name="ATTACHMENT_NAME_OLD" value="">
      <input type="hidden" name="FILE_SORT" value="1" >
      <input type="button" value="确定" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
         
     	<input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/core/funcs/personfolder/shareFolder.jsp?seqId=<%=seqId %>&managerPriSeqId=<%=managerPriSeqId %>'">
     
    </td>
  </tr>
</table>
</form>

<div>
 <form id="formFile" name="formFile" action="" method="post" enctype="multipart/form-data">
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
    
    <input type="hidden" id="newManagerPriSeqId" name="newManagerPriSeqId" value="<%=managerPriSeqId %>">
	  
 </form>
<iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</div>

<div>
<form id="newAttachForm" name="newAttachForm" action="<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFileContentAct/newFile.act?seqId=<%=seqId%>&contentId=<%=contentId%>" method="post">
	<input type="hidden" id="newSubject" name="newSubject" value="">
	<input type="hidden" id="newAttachmentName" name="newAttachmentName" value="">
	<input type="hidden" id="newContentNo" name="newContentNo" value="">	
	<input type="hidden" id="newAtttDesc" name="newAtttDesc" value="">
	<input type="hidden" id="newContent" name="newContent" value="">
	<input type="hidden" id="newFileType" name="newFileType" value="">
	<input type="hidden" id="managerPriSeqId" name="managerPriSeqId" value="<%=managerPriSeqId %>">
	
	<input id="returnFlag"	name="returnFlag" type="hidden" value="shareEdit">   

</form>
</div>



</body>
</html>