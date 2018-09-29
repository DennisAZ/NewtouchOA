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
<script type="text/javascript">
	//alert('newFile.jsp:<%=seqId%>');
	//alert('edit.jsp:<%=contentId%>');
var seqId='<%=seqId%>';
var contentId='<%=contentId%>';

function checkForm(){
	var subject=$("subject").value;
	if(subject==""){
		alert("文件名称不能为空!");
		$("subject").focus();
		return false;
	}
	return true;	
	
}

var requestURL="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct";
function sendForm(){
	if(checkForm()){
		var fileType=selectType();		
		var url=requestURL+"/updateFileInfoById.act?contentId="+contentId+"&sortId="+seqId +"&fileType="+fileType;
		var rtJson=getJsonRs(url,mergeQueryString($("form1")));
		if(rtJson.rtState == '0'){
	  	bindJson2Cntrl(rtJson.rtData);
	  	//alert("保存成功！");
	  	location="folder.jsp?seqId="+seqId;
	  }else{
		 	  alert(rtJson.rtMsrg); 
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
	var attachmentName=$("attachmentName").value;
	var subject=$("subject").value;
	var fileType=selectType();
	//alert(subject);
	//alert("newAttach");
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
	}	
	//alert("fileType:"+fileType);
	var url=requestURL+"/newFile.act?contentId="+contentId+"&fileType="+fileType+"&attachmentName="+attachmentName+"&subject="+subject;
	var URL=encodeURI(url);
	var rtJson=getJsonRs(URL);
	if(rtJson.rtState == '0'){
  	//alert("编辑成功！");
  	location.reload();
  }else{
	 	  alert(rtJson.rtMsrg); 
	}		
}

function doInit(){
	//alert("doInit");
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
		$("content").value=prcs.content;
		$("attachmentDesc").value=prcs.attachmentDesc;
		//$("attName").innerHTML=prcs.attachmentName;
		var arry=prcs.attachmentName.split("*");
		var attachmentName="";
		for(var k=0;k<arry.length-1;k++){
			//alert(k+":"+arry[k]);
			attachmentName+=arry[k]+"<br>";
		}	
		$("attName").innerHTML=attachmentName;		
	
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
				<textarea rows="13" cols="80%" id="content" name="content"></textarea>
			</div>
	</td>
  </tr>
  <tr class="TableContent">
    <td nowrap>附件文档：</td>
    <td nowrap><span id="attName"></span>   </td>
  </tr>
  <tr height="25">
    <td nowrap class="TableData">新建附件：</td>
    <td class="TableData">
      <input type="radio" onclick="selectType();" name="DOC_TYPE" value="doc" id="NEW_TYPE1"><label for="NEW_TYPE1">Word文档</label>
      <input type="radio" onclick="selectType();" name="DOC_TYPE" value="xls" id="NEW_TYPE2"><label for="NEW_TYPE2">Excel文档</label>
      <input type="radio" onclick="selectType();" name="DOC_TYPE" value="ppt" id="NEW_TYPE3"><label for="NEW_TYPE3">PPT文档</label>&nbsp;&nbsp;
      <b>附件名：</b><input type="text" name="attachmentName" id="attachmentName" size="20" class="SmallInput" value="新建文档">
      <input type="button" class="SmallButton" value="新建附件" onclick="newAttach();">
      
    </td>
  </tr>
  <tr height="25">
    <td nowrap class="TableData">附件选择：</td>
    <td class="TableData">
       <script></script>
       <script></script>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData"> 附件说明：</td>
    <td class="TableData">
      <input type="text" name="attachmentDesc" id="attachmentDesc" size="50" maxlength="50" class="BigInput" value="">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">内部事务提醒：</td>
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
  <tr align="center" class="TableControl">
    <td colspan="2" nowrap>
      <input type="hidden" value="" name="CONTENT_ID">
      <input type="hidden" name="OP" value="">
      <input type="hidden" name="SORT_ID" value="8" >
      <input type="hidden" name="ATTACHMENT_ID_OLD" value="">
      <input type="hidden" name="ATTACHMENT_NAME_OLD" value="">
      <input type="hidden" name="FILE_SORT" value="1" >
      <input type="button" value="确定" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
      <input type="button" value="返回" class="BigButton" onClick="location='folder.jsp?seqId=<%=seqId %>'">
    </td>
  </tr>
</table>
</form>






</body>
</html>