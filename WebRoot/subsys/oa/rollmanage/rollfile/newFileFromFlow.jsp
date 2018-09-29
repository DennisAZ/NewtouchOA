<%@page import="java.util.Map,java.lang.String"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String flowId= request.getParameter("flowId");
String runId= request.getParameter("runId");
String prcsId= request.getParameter("prcsId");
String flowPrcs= request.getParameter("flowPrcs");
String flowRMSFileNum = (String)request.getAttribute("FlowRMSFileNum");

Map<String,Object> map=(Map<String,Object>)request.getAttribute("attMap");
 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建文件</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rollfilelogic.js"></script>
<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>

<script type="text/javascript">
var requestURL = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

var runId = '<%=runId%>';
var flowId = '<%=flowId%>';
var prcsId = '<%=prcsId%>';
var flowPrcs = '<%=flowPrcs%>';
var flowRMSFileNum = '<%=flowRMSFileNum%>';

function getAttachImage(ext) {
	  var imgSrc = "";
	  if(ext == 'gif' || ext == 'jpg' || ext == 'bmp' || ext == 'png'){
	    imgSrc = imgPath + "/gif.gif";
	  }else if(ext == 'doc' || ext == 'docx'){
	    imgSrc = imgPath + "/doc.gif";
	  }else if(ext == 'xlsx' || ext == 'xls'){
	    imgSrc = imgPath + "/xls.gif";
	  }else if(ext == 'ppt' || ext == 'pptx'){
	    imgSrc = imgPath + "/ppt.gif";
	  }else if(ext == 'exe'){
	    imgSrc = imgPath + "/exe.gif";
	  }else if(ext == 'chm'){
	    imgSrc = imgPath + "/chm.gif";
	  }else if(ext== 'txt'){
	    imgSrc = imgPath + "/txt.gif";
	  }else if (ext == 'rar' || ext == 'zip') {
	    imgSrc = imgPath + "/rar.gif";
	  } else{
	    imgSrc = imgPath + "/defaut.gif";
	  }
	  return imgSrc;
	}
function doInit(){
	if(flowRMSFileNum != '0')
	{
		if(!window.confirm("该工作流已归档过，是否再次归档?"))
		{
			window.close();
		}
	}
	setDate();
	getSecretFlag("RMS_SECRET","secret");
	getSecretFlag("RMS_URGENCY","urgency");
	getSecretFlag("RMS_FILE_TYPE","fileType");
	getSecretFlag("RMS_FILE_KIND","fileKind");
	//getSecretFlag("FILE_WORD","fileWord");
	getSecretFlag("FILE_YEAR","fileYear");
	//getSecretFlag("ISSUE_NUM","issueNum");
	//getRmsRollSelect("rollId");
	checkSelectBox2();
	checkSelectBox3();
	getAttachments();
}
function getAttachments(){
	  var param = 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId +"&flowPrcs=" + flowPrcs;
	  var url = contextPath + "/yh/core/funcs/workflow/act/YHAttachmentAct/getAttachments.act";
	  var json = getJsonRs(url , param);
	  var attachmentName="<%=map.get("attName")%>"+"*";
	  var attachmentId="<%=map.get("attId")%>"+",";
	  var str="<a href='javascript:void(0)' onclick='downAction( \""+"<%=map.get("attId")%>"+"\", \""+"<%=map.get("attName")%>"+"\")'><img src='" + getAttachImage('html') + "'/>&nbsp;"+"<%=map.get("attName")%>"+"</a><br/>";
	  if(json.rtState == '0'){
		    var attachment = json.rtData;
		    if(attachment.length > 0){
		    	
		      for(var i = 0 ;i < attachment.length ;i ++){
		        var attach = attachment[i];
		        attachmentName+=attach.attachmentName+"*";
		        attachmentId+=attach.attachmentId+",";
		        str+=addAttachment(attach);
		      }
		     
		    }
		    $("showAtt").innerHTML=str;
		      $("attachmentId").value=attachmentId;
		      $("attachmentName").value=attachmentName;
		  }
}

function downAction(id,name){
	 downLoadFile(name ,  id, "workflow");
	}
function addAttachment(attachment){
	  var ext = attachment.ext;
	  var imgStr = getAttachImage(ext);
	  var str = "<a href='javascript:void(0)' onclick='downAction( \""+ attachment.attachmentId +"\", \""+ attachment.attachmentName +"\")'><img src='" + imgStr + "'/>&nbsp;" + attachment.attachmentName + "</a><br/>";
	  return str; 
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

function doSubmit(){
	if(checkForm()){
		if($("downloadYn").checked){
			$("downloadYn").value=1;
		}
		$("sendUnit").value = $("deptDesc").value;
		$("form1").submit();
	}
}

function isNum(num){
   var reNum=/^\d*$/;
   return(reNum.test(num));
}

function setDate(){
//日期
var date1Parameters = {
   inputId:'sendDate',
   property:{isHaveTime:false}
   ,bindToBtn:'date1'
};
new Calendar(date1Parameters);
}

function checkDate(){
	var leaveDate1 = document.getElementById("sendDate"); 
	var leaveDate1Array = leaveDate1.value.trim().split(" "); 
	if(!leaveDate1.value){
		return true;
	}
	if(!isValidDateStr(leaveDate1Array[0])){
		alert("日期格式不对，应形如 1999-01-01"); 
		leaveDate1.focus(); 
		leaveDate1.select(); 
		return false; 
	}
	return true;
}
</script>
</head>
<body onload="doInit();">

<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 新建文件</span>
    </td>
  </tr>
</table>
<form  action="<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsFileAct/addFileFromFlow.act"  method="post" name="form1" id="form1"  enctype="multipart/form-data">
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
      <input  name="issueNum" id="issueNum"  type="text">
      </TD>
  </TR>
   <tr>
  <TD class="TableData">文件主题词：</TD>
      <TD class="TableData"  colspan="3">
       <INPUT name="fileSubject" id="fileSubject" size=40 maxlength="100" class="BigInput">
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
       <INPUT name="sendUnit" id="sendUnit" type="hidden" value="">
       <INPUT name="deptDesc" id="deptDesc" size=20 maxlength="100" class="BigInput">
       <input type="hidden" name="dept" id="dept" value="">
       <span><a href="javascript:;" class="orgAdd" onClick="selectDept();">选择</a></span>
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
			<select name="secret" id="secret">
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
    <tr>
       <TD nowrap class="TableData">归档期限：</TD>
       <td class="TableData" colspan="3">
		<select name="deadline" id="deadline">
		</select>
      </td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData">附件权限：</td>
      <td class="TableData" colspan="3">
         <input type="checkbox" name="downloadYn" id="downloadYn" value="" checked><label for='downloadYn'>允许下载、打印附件</label>
      </td>
    </tr>    
    <tr height="25">
      <td nowrap class="TableData">附件：</td>
      <td class="TableData" colspan="3">
       <span id="showAtt">
        </span>
      </td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData"></td>
      <td class="TableData" colspan="3">
         <script>ShowAddFile();</script>
				<input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
				<input type="hidden" name="ATTACHMENT_NAME_OLD"	id="ATTACHMENT_NAME_OLD" value="">
				<input type="hidden" name="attachmentId"	id="attachmentId" value="">
				<input type="hidden" name="attachmentName"	id="attachmentName" value="">
			    <input type="hidden" id="runId" name="runId" value="<%=runId%>">
 				<input type="hidden" id="flowId" name="flowId" value="<%=flowId%>">
								
      </td>
    </tr>   
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
        <input type="button" value="新建" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;
        <!--  
        <input type="reset" value="重置" class="BigButton">&nbsp;&nbsp;
        -->
      </td>
    </tr>
  </table>
</form>

</body>
</html>