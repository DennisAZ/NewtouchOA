<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqIdStr = request.getParameter("seqId");
	int seqId = 0;
	if (!YHUtility.isNullorEmpty(seqIdStr)) {
		seqId = Integer.parseInt(seqIdStr);
	}
	String optFlag = request.getParameter("optFlag");
	if (YHUtility.isNullorEmpty(optFlag)) {
		optFlag = "";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>会议纪要</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript">
var optFlag = "<%=optFlag%>";
var fckContentStr = "";

//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function doInit(){
	if(optFlag == "1"){
		//$("showFormDiv").hide();
		$("doSubmitDiv").show();
	}else{
		$("showFormDiv").show();
		getMeetingSummaryDetail();
	}
	
}

function getMeetingSummaryDetail(){
  var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct/getMeetingSummaryDetail.act?seqId=${param.seqId}";
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    var data = json.rtData;
    bindJson2Cntrl(json.rtData);
    fckContentStr = json.rtData.summary;
    if($("readPeopleId") && $("readPeopleId").value.trim()){
      bindDesc([{cntrlId:"readPeopleId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
  	var selfdefMenu = {
      	office:["downFile","read","edit","deleteFile"], 
        img:["downFile","play","deleteFile"],  
        music:["downFile","play","deleteFile"],  
		    video:["downFile","play","deleteFile"], 
		    others:["downFile","deleteFile"]
			}
		attachMenuSelfUtil("attr","meeting",$('attachmentName1').value ,$('attachmentId1').value, '','',json.rtData.seqId,selfdefMenu);
  }else{
    var data = json.rtData;
    bindJson2Cntrl(json.rtData);
    alert(json.rtMsrg); 
  }
}



function FCKeditor_OnComplete( editorInstance ) {
  editorInstance.SetData( fckContentStr ) ;
}

function doSubmit(){
  $("optFlag").value = "1";
  upload_attach();
}

function upload_attach(){
	var oEditor = FCKeditorAPI.GetInstance('fileFolder');
  var fckContentStr = oEditor.GetXHTML();
	$("summary").value = fckContentStr;
	document.form1.action="<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct/updateMeetingSummary.act";
	$("form1").submit();
}

//删除附件
function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
  var attachNameOld = $('attachmentName1').value; 
  var attachIdOld = $('attachmentId1').value; 
  var attachNameArrays = attachNameOld.split("*"); 
  var attachIdArrays = attachIdOld.split(","); 
  var attaName = ""; 
  var attaId = ""; 
  for (var i = 0 ; i < attachNameArrays.length ; i++) {
    if (!attachIdArrays[i] || attachIdArrays[i] == attachId) { 
    	continue; 
    }
    attaName += attachNameArrays[i] + "*"; 
    attaId += attachIdArrays[i] + ","; 
  }
  $('attachmentId1').value = attaId; 
  $('attachmentName1').value = attaName;
  var requestURL = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct";
  var url = requestURL + "/delFloatFile.act?delOpt=summary&delAttachId=" + attaId +"&delAttachName=" + encodeURIComponent(attaName) + "&seqId=" + ${param.seqId};
  var json = getJsonRs(url);
	if(json.rtState =='1'){
		alert(json.rtMsrg);
		return false;
	}else{
	  prcsJson=json.rtData;
		var updateFlag=prcsJson.updateFlag;
		if(updateFlag == "1"){
		  return true;
		  window.location.reload();
		}else{
			return false;
		}
	}
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<div id="showFormDiv" style="display: none">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" width="22" height="18"><span class="big3"> 会议纪要</span>
    </td>
  </tr>
</table>
<br>

<form enctype="multipart/form-data" action="" target="_self" method="post" name="form1" id="form1">
<table class="TableBlock" width="90%" align="center">
<tr>
   <td nowrap class="TableContent" width="80">会议名称：</td>
   <td class="TableData" colspan="3">
   <input type="text" name="mName" id="mName" size="40" maxlength="100" class="BigStatic" readonly value="">
   </td>
</tr>
<tr>
   <td nowrap class="TableContent" width="80">指定读者：</td>
   <td class="TableData" colspan="3">
   	<input type="hidden" name="readPeopleId" id="readPeopleId" value="">
     <textarea name="readPeopleIdDesc" id="readPeopleIdDesc" class="BigStatic" cols="50" rows="3" class="BigStatic" wrap="yes" readonly></textarea>
     <a href="javascript:;" class="orgAdd" onClick="selectUser(['readPeopleId', 'readPeopleIdDesc']);">添加</a>
     <a href="javascript:;" class="orgClear" onClick="$('readPeopleId').value='';$('readPeopleIdDesc').value='';">清空</a>
   </td>
</tr>
<tr>
   <td valign="top" nowrap class="TableContent" width="80">纪要内容：</td>
   <td class="TableData" colspan="3">
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

<tr>
   <td class="TableContent">附件文件:</td>
  <td nowrap class="TableData" colspan="3">
    <input type = "hidden" id="attachmentId1" name="attachmentId1"></input>
     <input type = "hidden" id="attachmentName1" name="attachmentName1"></input>
     <span id="attr"></span>
  </td>
</tr>

<tr height="25" id="attachment1">
  <td nowrap class="TableContent"><span id="ATTACH_LABEL">附件上传：</span></td>
  <td class="TableData"><script>ShowAddFile();</script> 
 	<script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
 	<input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
 	<input type="hidden" name="ATTACHMENT_NAME_OLD"	id="ATTACHMENT_NAME_OLD" value="">
			
  </td>
</tr>
<tr class="TableControl">
<td align="center" colspan="4">
	<input type="hidden" value="<%=seqId %>" name="seqId" id="seqId" >
 	<input type="hidden" value="" name="summary" id="summary">
 	<input type="hidden" value="" name="optFlag" id="optFlag">
  <input type="button" value="保存" class="BigButton" title="保存会议纪要" onclick="doSubmit();">&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="button" class="BigButton" value="关闭" onclick="window.close()">
</td>
</tr>
</table>
</form>
</div>

<div id="doSubmitDiv" style="display: none">
<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">设置成功！</div>
    </td>
  </tr>
</table>
<br><center>
	<input type="button" value="关闭" class="BigButton" onClick="window.close();">	
</center>
</div>
</body>
</html>