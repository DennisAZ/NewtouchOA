<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String runId = request.getParameter("runId");
  String prcsId = request.getParameter("prcsId");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>归档公文</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rollfilelogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript">
var field = [['主题词','fileSubject'],['密级','secret'],['标题','fileTitle'],['紧急程度','urgency'],['页数','filePage'],['份数','printPage']]
var runId = "<%=runId%>";
var prcsId = "<%=prcsId %>";
var requestUrl = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDocAct";
function doInit(){
  setDate();
  getSecretFlag("RMS_SECRET","secret");
  getSecretFlag("RMS_URGENCY","urgency");
  getSecretFlag("RMS_FILE_TYPE","fileType");
  getSecretFlag("RMS_FILE_KIND","fileKind");
  getRmsRollSelect("rollId");
  loadFlowRunData();
 
  $('runId').value = runId;
}
function loadFlowRunData() {
  var url = requestUrl + "/loadPigeonholeData.act";
  var json = getJsonRs(url , "runId=" + runId + "&prcsId=" + prcsId);
  if (json.rtState == "0") {
    var runData = json.rtData.runData;
    if (runData) {
      setRunData(runData);
    }
    var handlerTime = json.rtData.handlerTime;
    if (handlerTime) {
      $('handlerTimeTd').update("&nbsp;" + handlerTime);
      $('handlerTime').value = handlerTime;
    }
    var docAttachment = json.rtData.doc;
    if (!docAttachment) {
      docAttachment = {};
    }
    var attachment = json.rtData.attachment;
    setAttachment(docAttachment , attachment);
  }
}
function setAttachment(docAttachment , attachment) {
  var  selfdefMenu = {
     office:["downFile","read"], 
      img:["downFile","play"],  
      music:["downFile","play"],  
	    video:["downFile","play"], 
	    others:["downFile"]
		}

	var  docSelfdefMenu = {
    	office:["downFile","read"], 
      img:["downFile","play"],  
      music:["downFile","play"],  
	    video:["downFile","play"], 
	    others:["downFile"]
		}
     if (!docAttachment.docName) {
       docAttachment.docName  = "";
     }
     if (!docAttachment.docId) {
       docAttachment.docId = "";
     }
     $('docName').value = docAttachment.docName;
     $('docId').value = docAttachment.docId;
     $('attachmentId').value = attachment.attachmentId;
     $('attachmentName').value = attachment.attachmentName;
	attachMenuSelfUtil("attr","roll_manage",attachment.attachmentName ,attachment.attachmentId, 'bb','',"",selfdefMenu);
	attachMenuSelfUtil("docAttr","roll_manage",docAttachment.docName ,docAttachment.docId, 'doc','',"",docSelfdefMenu);
}
function setRunData(runData) {
  if(runData && runData.length > 0){
   for(i=0;i < runData.length;i++) { 
     var name = runData[i][0];
     var value = runData[i][1];
     if (!name) {
       continue;
     }
     for (j = 0 ;j < field.length ;j ++) {
       var f = field[j];
       if (f[0] == name) {
         var fi = f[1];
         setValue(fi , value);
       }
     }
    }
  }
}
  function setSelectedValue(fi , value) {
    var option = fi.getElementsByTagName("option");
    for (var i = 0 ;i < option.length ;i ++) {
      var op = option[i];
      var text = op.innerHTML;
      if (text == value) {
        op.selected = true;
      }
    }
  }
function setValue(fi , value) {
  var f = $(fi);
  if (fi != 'fileCode') {
    if (f) {
      if (f.tagName == "SELECT") {
        setSelectedValue(f , value);
      } else {
        f.value = value;
      }
    }
  } else {
    // f.value = "京政外发(2010)" + value;
  }
  
}

function checkForm(){
	if($("fileCode").value == ""){
	   alert("文件号不能为空！");
	   $("fileCode").focus();
    return (false);
	}
	if($("fileTitle").value == ""){
	   alert("文件名称不能为空！");
	   $("fileTitle").focus();
      return (false);
	}
	return (true);	
}




function doSubmit(){
	if(checkForm()){
	  var param = $('form1').serialize();
      var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDocAct/pigeonhole.act";
      var json = getJsonRs(url , param);
      if (json.rtState == "0") {
        alert("归档成功！");
        top.win.closePage();
      }
	}
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

</script>
</head>
<body onload="doInit();">

<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 文件归档</span>
    </td>
  </tr>
</table>
 
<form  action="<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsFileAct/addFileInfo.act"  method="post" name="form1" id="form1"  enctype="multipart/form-data">
<table class="TableBlock" width="90%"  align="center">
  <TR>
      <TD class="TableData">文件号：</TD>
      <TD class="TableData">
       <INPUT name="fileCode" id="fileCode" size=20 maxlength="100" class="BigInput" value="">
      </TD>
      <TD class="TableData">文件主题词：</TD>
      <TD class="TableData">
       <INPUT name="fileSubject" id="fileSubject" size=30 maxlength="100" class="BigInput">
      </TD>
  </TR>
  <TR>
      <TD class="TableData">文件标题：</TD>
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
       <INPUT name="sendUnit" id="sendUnit" size=30 value="" class="BigInput">
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
			<select name="secret" id="secret" class="BigSelect">
				<option value="" ></option>
			</select>
   	</TD>
    <TD class="TableData">紧急等级：</TD>
    <TD class="TableData">
			<select name="urgency" id="urgency" class="BigSelect">
				<option value="" ></option>
			</select>
   	</TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">文件分类：</TD>
      <TD class="TableData">
	<select name="fileType" id="fileType" class="BigSelect">
	  <option value="" ></option>
	</select>
      </TD>
      <TD class="TableData">公文类别：</TD>
      <TD class="TableData">
	<select name="fileKind" id="fileKind" class="BigSelect">
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
		<select name="rollId" id="rollId" class="SmallSelect">
		</select>
      </TD>
   </TR>
    <TR>
      <TD nowrap class="TableData">处理时长：</TD>
      <TD class="TableData" id="handlerTimeTd">
      </TD>
      <TD class="TableData">流转次数：</TD>
      <TD class="TableData">&nbsp;
      <%=prcsId %>次
      <input value="" type="hidden" id="handlerTime" name="handlerTime"/>
      <input value="<%=prcsId %>" type="hidden" id="turnCount" name="turnCount"/>
      </TD>
   </TR>
   <tr height="25">
      <td nowrap class="TableData">正文：</td>
      <td class="TableData" colspan="3">
        <input type="hidden" name="docId" id="docId"/>
        <input type="hidden" name="docName" id="docName"/>
        <span id="docAttr"></span>
      </td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData">附件权限：</td>
      <td class="TableData" colspan="3">
         <input type="checkbox" name="downloadYn" id="downloadYn" value="1" checked>允许下载、打印附件      </td>
    </tr>     
    <tr height="25">
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData" colspan="3">
		<input type="hidden" name="attachmentId" id="attachmentId" value="">
	    <input type="hidden" name="attachmentName"	id="attachmentName" value="">
        <span id="attr"></span>
      </td>
    </tr> 
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
        <input type="hidden" id="runId" name="runId" value=""/>
        <input type="button" value="归档" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;
        <input type="reset" value="重置" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>

</body>
</html>