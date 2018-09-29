<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>

<title>附件导航</title>
<script type="text/javascript">
var upload_limit=1,limit_type="php,php3,php4,php5,";
var oa_upload_limit="php,php3,php4,php5,";
function upload_attach() {
  $('moduel1').value = $('moduel').value;
  $("formFile").submit();
}
function handleSingleUpload(rtState, rtMsrg, rtData) {
  var data = rtData.evalJSON(); 
  $('attachmentId').value += data.attrId;
  $('attachmentName').value += data.attrName;
  $('moduel1').value = $('moduel').value;
  var readOnly = false;
  if($('readOnly').checked){
    readOnly = true;
  }
  alert(data.attrName);
  attachMenuUtil("showAtt",$('moduel').value,null,$('attachmentName').value,$('attachmentId').value,readOnly);
  removeAllFile();
}
function deleteAttachBackHand(attachName,attachId,attrchIndex){
  var attachNameOld = $('attachmentName').value;
  var attachIdOld =  $('attachmentId').value;
  var attachNameArrays = attachNameOld.split("*");
  var attachIdArrays = attachIdOld.split(",");
  var attaName = "";
  var attaId = "";
  for(var i = 0 ; i < attachNameArrays.length ; i++){
    if(!attachNameArrays[i] || attachNameArrays[i] == attachName){
      continue;
    }
    attaName += attachNameArrays[i] + "*";
    attaId += attachIdArrays[i] + ",";
  }
  $('attachmentId').value = attaName;
  $('attachmentName').value = attaName;
  //alert('删除附件事后工作！>> <附件名：' + attachName +' > <附件Id：' + attachId +' > <附件编号：'+ attrchIndex + '>');
}
</script>
</head>
<body>
模块名称:<input id="moduel" type="text" value="test">
是否只读:<input id="readOnly" type="checkbox"><br>
附件名称:<textarea id="attachmentName" rows="4" cols="30" ></textarea><br>
附件ID&nbsp;&nbsp;&nbsp;&nbsp;:<textarea id="attachmentId"  rows="4" cols="30"></textarea><br>
<table>
  <tr height="25">
    <td nowrap class="TableData">附件:</td>
    <td nowrap class="TableData"><div id="showAtt"></div></td>
  </tr>
  <tr height="25">
    <td nowrap class="TableData">附件选择：</td>
    <td class="TableData">
     <script>ShowAddFile();ShowAddImage();</script>
     <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
     <input type="hidden" id="ATTACHMENT_ID_OLD"  name="ATTACHMENT_ID_OLD" value="">
     <input type="hidden" id="ATTACHMENT_NAME_OLD"  name="ATTACHMENT_NAME_OLD" value="">     
    </td>
  </tr>
</table>
<form name="formFile" id="formFile" action="<%=contextPath %>/test/core/act/YHTestAttaMeun/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="moduel1" name="moduel" type="hidden">
</form>
<iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</body>
</html>