<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
 <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
 <%@ page import="yh.core.funcs.person.data.YHPerson,yh.core.global.YHSysProps" %>
<%
String seqId = request.getParameter("seqId"); 
String runId = request.getParameter("runId");
String flowId = request.getParameter("flowId");
String prcsId = request.getParameter("prcsId");
String flowPrcs = request.getParameter("flowPrcs");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑会签意见</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link href="<%=cssPath %>/cmp/swfupload.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript"><!--
var upload_limit=1,limit_type="<%=YHSysProps.getLimitUploadFiles()%>";
var oa_upload_limit="<%=YHSysProps.getLimitUploadFiles()%>";
var attachUrl = "<%=YHSysProps.getAttachPath()%>";//附件路径
var swfupload;
var seqId = '<%=seqId%>';
var runId = '<%=runId%>';
var flowId = '<%=flowId%>';
var prcsId = '<%=prcsId%>';
var flowPrcs = '<%=flowPrcs%>';

var isSaveFeedUpload = false;//是在保存会签意思的时候上传后保存

var content = "";
var feedbackUrl = contextPath + "<%=moduleSrcPath %>/act/YHFeedbackAct";
function doInit(){
  var param = 'runId=' + runId 
    + '&flowId=' + flowId
    +'&prcsId=' + prcsId 
    + "&flowPrcs=" + flowPrcs
    + '&seqId=' + seqId;
  var url = feedbackUrl +  "/getFeedbacks.act";
  var json = getJsonRs(url , param);
  if ( json.rtState == '0' ) {
    content = json.rtData.content;
    $('attachmentId').value = json.rtData.attachmentId;
    $('attachmentName').value = json.rtData.attachmentName;
    //还要处理附件
  } else {
    alert(json.rtMsrg);
  }
}
function FCKeditor_OnComplete( editorInstance ) {
  editorInstance.SetData( content ) ;
}
function updateFeedback(){
  var attachDiv = $('ATTACHMENT1_div').innerHTML.trim();
  if (attachDiv) {
    isSaveFeedUpload = true;
    uploadFeedAttach();
    return null;
  }
  var url = feedbackUrl +  "/saveFeedback.act";
  var oEditor = FCKeditorAPI.GetInstance('FCKeditor2') ;
  var json = getJsonRs(url , $('ATTACHMENT1_formFile').serialize() + "&content=" + encodeURIComponent(oEditor.GetXHTML()));
  if ( json.rtState == '0' ) {
    try {
      
      opener.location.reload();
      //opener.loadFeedback();
    } catch (e) {
    }
    close();
    //closeModalWindow('editFeedback');
  } else {
    alert(json.rtMsrg);
  }
}
function uploadFeedAttach() {
  $('isFeedAttach').value = 'true';
  $("ATTACHMENT1_formFile").submit();
}
function handleSingleUpload(returnState , tmp , objStr){
  $('ATTACHMENT1_div').innerHTML = "";
  $('ATTACHMENT1_upload_div').hide();
  if (returnState == '1') {
    alert(tmp);
    removeFiles("ATTACHMENT1_formFile");
    return ;
  }
  eval(objStr);
  var id = obj.id;
  var name = obj.name;
  addFeedAttach(id , name);
  if (isSaveFeedUpload) {
    isSaveFeedUpload = false;
    updateFeedback();
  }
}
function addFeedAttach(id , name) {
  var tmpId = $('attachmentId').value;
  var tmpName = $('attachmentName').value;
  
  $('feedAttachments').show();
  var tmp = $('feedAttachments').innerHTML.trim() ;
  var arra = name.split("*");
  for(var i = 0 ;i < arra.length ; i++) {
    var str = arra[i];
    if (str) {
      tmp += "<img src='" + imgPath +"/attach.png'>" + str + ";";
    }
  }
  $('feedAttachments').update(tmp);
  $('attachmentId').value = tmpId + id;
  $('attachmentName').value = tmpName + name;
}

function restoreFile(i) {
  var attachmentName = $('ATTACH_NAME' + i).value;
  var attachmentDir = $('ATTACH_DIR' + i).value;
  var diskId = $('DISK_ID' + i).value;
  var param = "attachmentName=" + attachmentName 
       + "&attachmentDir=" + attachmentDir + "&diskId=" + diskId;
  if (attachmentName) {
    var url = contextPath + "<%=moduleSrcPath %>/act/YHAttachmentAct/restoreFile.act?isFeedAttach=true";;
    var json = getJsonRs(url , param);
    if (json.rtState == '0') {
      obj = json.rtData;
      var id = obj.id;
      var name = obj.name;
      addFeedAttach(id , name);
    }
  }
  $('SelFileDiv' + i).update("");
  $('ATTACH_NAME' + i).value = "";
  $('ATTACH_DIR' + i).value = "";
  $('DISK_ID' + i).value = "";
}
/**
 * 清除会签内容
 * @return
 */
function clearFeedbackContent() {
  var oEditor = FCKeditorAPI.GetInstance('FCKeditor2') ;
  oEditor.SetData("");
}
function removeFiles(formId){
  var inputtmp =  $(formId);
  var inputs = inputtmp.getElementsByTagName("INPUT");
  var attach = [];
  for(var i = 0 ;i < inputs.length ; i++) {
    var input = inputs[i];
    if (input.type == 'file') {
      attach.push(input);
    }
  }
  for(var i = 0 ;i < attach.length ; i++) {
    var input = attach[i];
    if (input) {
      var parent = input.parentNode;
      parent.removeChild(input);
    }
  }
}
</script>
</head>
<body  topmargin="5" onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td height=24><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><b> 编辑会签意见</b></td>
  </tr>
</table>
<form name="ATTACHMENT1_formFile" id="ATTACHMENT1_formFile"action="<%=contextPath %><%=moduleSrcPath %>/act/YHAttachmentAct/uploadFile.act" target="returnPage"  method="post" enctype="multipart/form-data">
<table border="0" align="center" width="100%" class="TableList">
	    <tr>
        <td class="TableContent">       
        <div>        	
       <script type="text/javascript">
        var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
        var oFCKeditor = new FCKeditor( 'FCKeditor2' ) ;
        oFCKeditor.BasePath    = sBasePath ;
        oFCKeditor.Height       = 150 ;
        var sSkinPath = sBasePath + 'editor/skins/silver/';
        oFCKeditor.Config['SkinPath'] = sSkinPath ;
        oFCKeditor.Config['PreloadImages'] =
                        sSkinPath + 'images/toolbar.start.gif' + ';' +
                        sSkinPath + 'images/toolbar.end.gif' + ';' +
                        sSkinPath + 'images/toolbar.buttonbg.gif' + ';' +
                        sSkinPath + 'images/toolbar.buttonarrow.gif' ;
        oFCKeditor.ToolbarSet = 'feedback';
        oFCKeditor.Value  = '' ;
        oFCKeditor.Create();
        </script>
        </div>
      </tr>
       <tr>
   <td class="TableContent" style="display:none" align=left id="feedAttachments"></td>
 </tr>
      <tr>
      
    <td class="TableContent" align=left>
<script>ShowAddFile(1);</script> 
<script>$("ATTACHMENT1_upload_div").innerHTML='<a href="javascript:uploadFeedAttach();">上传附件</a>'</script>
</td></tr>
    <tr class="TableFooter">
    	<td align="center"> 
       <input type="hidden" id="isFeedAttach" name="isFeedAttach"/>  
         <input type="hidden" id="attachmentId" name="attachmentId"/>
        <input type="hidden" id="attachmentName" name="attachmentName"/>   
        <input type="button"  class="SmallButtonW" value="提交意见" onclick="updateFeedback()">
        <input type="button" style="display:none" onclick="clearFeedbackContent()" value="清空" class="SmallButton"/> 
        <input type="hidden"  name="seqId" id="seqId" value="<%=seqId%>">
        <input type="hidden"  name="flowId" id="flowId"  value="<%=flowId%>">
        <input type="hidden"  name="runId" id="runId" value="<%=runId%>">
        <input type="hidden"  name="prcsId" id="prcsId" value="<%=prcsId%>">
        <input type="hidden"  name="flowPrcs" id="flowPrcs" value="<%=flowPrcs%>">
      </td>
    </tr>
</table>
</form>
<iframe id="returnPage" name="returnPage" style="display:none"></iframe>
</body>
</html>