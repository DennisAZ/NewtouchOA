<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
	String seqId=request.getParameter("seqId");


%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑文件</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rollfilelogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript">
var requestURL = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsFileAct";

//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function doInit(){
checkSelectBox3();
	setDate();
  var url = requestURL + "/getRmsFileDetailById.act?seqId=${param.seqId}";
  var json = getJsonRs(url);
  //alert(rsText);
  if(json.rtState == "0"){
    var data = json.rtData;
    $('seqId').value = data.seqId;
    $('fileCode').value = data.fileCode;
    $('fileSubject').value = data.fileSubject;
    $('fileTitle').value = data.fileTitle;
    $('fileTitleo').value = data.fileTitleo;
    $('sendUnit').value = data.sendUnit;

    $('fileWord').value = data.fileWord;
    $('issueNum').value = data.issueNum;
    $('deadline').value=data.deadline;
    //alert(docAttachmentName);
    var sendDateStr = data.sendDate;
    if(sendDateStr){
    	$('sendDate').value = sendDateStr.substr(0, 10);
    }
    getSecretFlag("RMS_SECRET","secret",data.secret);
  	getSecretFlag("RMS_URGENCY","urgency",data.urgency);
  	getSecretFlag("RMS_FILE_TYPE","fileType",data.fileType);
  	getSecretFlag("RMS_FILE_KIND","fileKind",data.fileKind);

    
  	//getSecretFlag("FILE_WORD","fileWord",data.fileWord);
  	getSecretFlag("FILE_YEAR","fileYear",data.fileYear);
  	//getSecretFlag("ISSUE_NUM","issueNum",data.issueNum);
  	//getRmsRollSelect("rollId",data.rollId);
  	checkSelectBox2(data.rollId);
    $('filePage').value = data.filePage;
    $('printPage').value = data.printPage;
    $('remark').value = data.remark;

    if(data.downloadYn == 1){
    	$('downloadYn').checked = true;
    }
  	$("returnAttId").value = data.attachmentId;
		$("returnAttName").value = data.attachmentName;
  	$("returnDocAttId").value = data.docAttachmentId;
		$("returnDocAttName").value = data.docAttachmentName;
		var  selfdefMenu = {
      	office:["downFile","read","edit","deleteFile"], 
        img:["downFile","play","deleteFile"],  
        music:["downFile","play","deleteFile"],  
		    video:["downFile","play","deleteFile"], 
		    others:["downFile","deleteFile"]
			}
		var  docSelfdefMenu = {
      	office:["downFile","read","edit","deleteFile"], 
        img:["downFile","play","deleteFile"],  
        music:["downFile","play","deleteFile"],  
		    video:["downFile","play","deleteFile"], 
		    others:["downFile","deleteFile"]
			}
		attachMenuSelfUtil("attr","roll_manage",$('returnAttName').value ,$('returnAttId').value, '','',${param.seqId},selfdefMenu);
		attachMenuSelfUtil("docAttr","roll_manage",$('returnDocAttName').value ,$('returnDocAttId').value, 'docAttachment','',${param.seqId},docSelfdefMenu);
    
  }else{
    alert(rtJson.rtMsrg); 
  }
  
}

//浮动菜单文件的删除
function deleteAttachBackHand(attachName,attachId,attrchIndex){
	var url= requestURL +"/delFloatFile.act?attachId=" + attachId +"&attachName=" + encodeURIComponent(attachName) + "&seqId=" + attrchIndex;
  //alert("attachId>>>"+attachId  +"  attachName>>>" +attachName +"   attrchIndex>>" +attrchIndex);
  //alert(url);
	var json=getJsonRs(url);
	if(json.rtState =='1'){
		alert(json.rtMsrg);
		return false;
	}else{
	  prcsJson=json.rtData;
		var updateFlag=prcsJson.updateFlag;
		if(updateFlag == "1"){
		  return true;
		}else{
			return false;
		}
	}
}

//日期
function setDate(){
	var date1Parameters = {
	   inputId:'sendDate',
	   property:{isHaveTime:false}
	   ,bindToBtn:'date1'
	};
	new Calendar(date1Parameters);
}

function checkDate(){
	var leaveDate1 = document.getElementById("sendDate"); 
	if(!leaveDate1.value){
		return true;
	}
	if(!isValidDateStr(leaveDate1.value)){
		alert("日期格式不对，应形如  1999-01-01"); 
		leaveDate1.focus(); 
		leaveDate1.select(); 
		return false; 
	}
	return true;
}

function checkForm(){
	if($("fileCode").value.trim() == ""){
		alert("文件号不能为空！");
		$("fileCode").focus();
		$("fileCode").select();
    return (false);
	}
	if($("fileTitle").value.trim() == ""){
		alert("文件名称不能为空！");
		$("fileTitle").focus();
		$("fileTitle").select();
    return (false);
	}
	if(checkDate() == false ){
		return false;
	}
	if ($("filePage").value!=""){
	if (!isNum($("filePage").value)){
	    alert("文件页数必须为数字！");
		$("filePage").focus();
		$("filePage").select();
        return (false);
	}
	}
	if ($("printPage").value!=""){
	if (!isNum($("printPage").value)){
	    alert("打印页数必须为数字！");
		$("printPage").focus();
		$("printPage").select();
        return (false);
	}
	}
	return (true);	
}
function isNum(num){
   var reNum=/^\d*$/;
   return(reNum.test(num));
}

function doSubmit(){
	if(checkForm()){
		if($("downloadYn").checked){
			$("downloadYn").value = 1;
		}else{
			$("downloadYn").value = 0;
		}
		$("form1").submit();
	}
}


</script>
</head>
<body onload="doInit();">

<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 编辑文件 </span>
    </td>
  </tr>
</table>
 
<form  action="<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsFileAct/updateRmsFileById.act"  method="post" name="form1" id="form1"  enctype="multipart/form-data">
<table class="TableBlock" width="90%"  align="center">
  <TR>
      <TD class="TableData">文件号：<font style='color:red'>*</font></TD>
      <TD class="TableData">
       <INPUT name="fileCode" id="fileCode" size=20 maxlength="100" class="BigInput">
      </TD>
      <TD class="TableData">公文字：</TD>
      <TD class="TableData">
      <input  name="fileWord" id="fileWord" type="text">
      </TD>
  </TR>
    <TR>
      <TD class="TableData">公文年号：</TD>
      <TD class="TableData">
       <select name="fileYear" id="fileYear"></select>
      </TD>
      <TD class="TableData">公文期号：</TD>
      <TD class="TableData">
      <input  name="issueNum" id="issueNum" type="text">
      </TD>
  </TR>
  <tr>
  <TD class="TableData">文件主题词：</TD>
      <TD class="TableData" colspan="3">
       <INPUT name="fileSubject" id="fileSubject" size=30 maxlength="100" class="BigInput">
      </TD>
  </tr>
  
  <TR>
      <TD class="TableData">文件标题：<font style='color:red'>*</font></TD>
      <TD class="TableData">
       <INPUT name="fileTitle" id=fileTitle size=30 maxlength="100" class="BigInput">
      </TD>
      <TD class="TableData">文件辅标题：</TD>
      <TD class="TableData">
       <INPUT name="fileTitleo" id="fileTitleo" size=30 maxlength="100" class="BigInput">
      </TD>
  </TR>
  <TR>
      <TD class="TableData">发文单位：</TD>
      <TD class="TableData">
       <INPUT name="sendUnit" id="sendUnit" size=30 maxlength="100" class="BigInput">
      </TD>
      <TD class="TableData">发文日期：</TD>
      <TD class="TableData">
        <input type="text" name="sendDate" id="sendDate" size="19" maxlength="19" class="BigInput" value="">
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >&nbsp;
      </TD>
  </TR>
  <TR>
	  <TD nowrap class="TableData">密级：</TD>
	  <TD class="TableData">
			<select name="secret" id="secret" >
				<option value="" ></option>
			</select>
   	</TD>
    <TD class="TableData">紧急等级：</TD>
    <TD class="TableData">
			<select name="urgency" id="urgency" >
				<option value="" ></option>
			</select>
   	</TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">文件分类：</TD>
      <TD class="TableData">
	<select name="fileType" id="fileType" >
	  <option value="" ></option>
	</select>
      </TD>
      <TD class="TableData">公文类别：</TD>
      <TD class="TableData">
	<select name="fileKind" id="fileKind" >
		<option value="" ></option>
	</select>
     </TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">文件页数：</TD>
      <TD class="TableData">
        <input type="text" name="filePage" id="filePage" value="" size="10" maxlength="50" class="BigInput" >
      </TD>
      <TD class="TableData">打印页数：</TD>
      <TD class="TableData">
        <input type="text" name="printPage" id="printPage" value="" size="10" maxlength="50" class="BigInput" >
      </TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">备注：</TD>
      <TD class="TableData"><input type="text" name="remark" id="remark" value="" size="30" maxlength="100" class="BigInput"></TD>
      <TD class="TableData">所属案卷：</TD>
      <TD class="TableData">
		<select name="rollId" id="rollId" >
			
		</select>
      </TD>
   </TR>
    <tr height="25">
      <td nowrap class="TableData">正文：</td>
      <td class="TableData" >
      	<input type = "hidden" id="returnDocAttId" name="returnDocAttId"></input>
      	<input type = "hidden" id="returnDocAttName" name="returnDocAttName"></input>
       	<span id="docAttr"></span>      
      </td>
       <td nowrap class="TableData">归档期限：</td>
        <td class="TableData">
		<select name="deadline" id="deadline">
		</select>
      </td>
    </tr> 
    <tr height="25">
      <td nowrap class="TableData">附件权限：</td>
      <td class="TableData" colspan="3">
         <input type="checkbox" name="downloadYn" id="downloadYn" value="" ><label for='downloadYn'>允许下载、打印附件</label>
      </td>
    </tr>   
    <tr class="TableData">
      <td nowrap>附件文档：</td>
      <td nowrap colspan="3">
      	<input type = "hidden" id="returnAttId" name="returnAttId"></input>
      	<input type = "hidden" id="returnAttName" name="returnAttName"></input>
       	<span id="attr"></span>      
      </td>
    </tr>
   
    <tr height="25">
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData" colspan="3">
         <script>ShowAddFile();</script>
				<input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
				<input type="hidden" name="ATTACHMENT_NAME_OLD"	id="ATTACHMENT_NAME_OLD" value="">
								
      </td>
    </tr>   
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
      	<input type="hidden" id="seqId" name="seqId" value="<%=seqId %>">
        <input type="button" value="保存" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton"  onclick="javaScript:location.href='<%=contextPath %>/subsys/oa/rollmanage/rollfile/fileManage.jsp'" >&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</html>