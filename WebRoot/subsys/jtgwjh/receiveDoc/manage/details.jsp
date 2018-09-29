<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
SimpleDateFormat sf = new SimpleDateFormat("yyyy");
String dayTime = sf.format(new Date());
String seqId = request.getParameter("seqId") == null ? "0" : request.getParameter("seqId");
String headerStr = "公文信息详情";
if(!seqId.equals("0")){
  headerStr = "收文详情";
}

%>
<html>
<head>
<title>公文信息详情</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/organizationDept/proj/js/project.js"></script>

<script type="text/javascript">
var seqId = '<%=seqId%>';
var upload_limit=1,limit_type="php,php3,php4,php5,";
function doOnload() {
  if(seqId > 0){
    getInfo(seqId);
    doOnloadFile();
  }

}


function getInfo(seqId){
  var requestURL = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/selectById.act?seqId=" + seqId ;
  var json = getJsonRs(requestURL);
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  } else {
    var prc = json.rtData;
    bindJson2Cntrl(prc);
    //普通附件中去掉正文附件  Strat
    var attachId = prc.attachmentId;
    var attachName = prc.attachmentName;
    var docId = prc.mainDocId;
    var docName = prc.mainDocName;
    var attachIdArr = attachId.split(",");
    var attachNameArr = attachName.split("*");
    var newAttachId = "";
    var newAttachName = "";
    for(var i = 0 ;i<attachIdArr.length ;i++){
      if(attachIdArr[i] && docId != attachIdArr[i]){
        newAttachId = newAttachId + attachIdArr[i] + ",";
        newAttachName = newAttachName + attachNameArr[i] + "*";
      }
    }
    if(newAttachId){
      newAttachId = newAttachId.substring(0,newAttachId.length-1);
      newAttachName = newAttachName.substring(0,newAttachName.length-1);
    }
    $("attachmentId").value = newAttachId;
    $("attachmentName").value = newAttachName;
    //end
    
    var docType = prc.docType;
    var docTypeStr = "普通";
    if(docType == '1'){
      docTypeStr = "公文";
    }
    $("docType").update(docTypeStr);
    
    var urgentType = prc.urgentType;
    var urgentTypeStr = "一般";
    if(urgentType == '1'){
      urgentTypeStr = "紧急";
    }else  if(urgentType == '2'){
      urgentTypeStr = "特急";
    }

    $("urgentType").update(urgentTypeStr);
    
    
    var securityLevel = prc.securityLevel;
    var securityLevelStr = "普通";
    if(securityLevel == '1'){
      securityLevelStr = "内部";
    }else if(securityLevel == '2'){
      securityLevelStr = "秘密";
	  }else if(securityLevel == '3'){
	    securityLevelStr = "机密";
	  }
    $("securityLevel").update(securityLevelStr);
    $("printCount").update(prc.printCount + "份");
  }
}

var  selfdefMenu = {
    office:["downFile","read"], 
    img:["downFile","play"],  
    music:["downFile","play"],  
    video:["downFile","play"], 
    others:["downFile"]
  }

function doOnloadFile(){
  var attr = $("attr");
  var seqId  = $("seqId").value;
  attachMenuSelfUtil(attr,$("moduel").value,$('attachmentName').value ,$('attachmentId').value, '','',seqId,selfdefMenu);
}
var isUploadBackFun = false;
//附件上传
function upload_attach(){
  if(checkForm()){
    $("btnFormFile").click();
  }  
}

//有附件，也执行上传附件
function jugeFile() {
  var formDom  = document.getElementById("formFile");
  var inputDoms  = formDom.getElementsByTagName("input"); 
  for (var i=0; i<inputDoms.length; i++) {
  var idval = inputDoms[i].id;
  if(idval.indexOf("ATTACHMENT") != -1){
    return true;
  }
} 
return false; 
}


function returnInfo(){
  window.location.href = "waitReceive.jsp";
}

function doReceive(){
  var requestURL = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/recvMessage.act"; 
  
  var json=getJsonRs(requestURL); 
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  } else {
    var prc = json.rtData;
    alert("保存成功！")
    window.location.reload();
  }
}

//附件处理结束
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td>
    </td>
  </tr>
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"><%=headerStr %></span>&nbsp;&nbsp;
    </td>
  </tr>
</table>

<form id="form1" name="form1">
	<table class="TableBlock" width="80%" align="center">
		 <tr>
      <td nowrap class="TableContent">发文单位： </td>
      <td class="TableData"colspan="3" id="sendDeptName" ></td>
	  </tr>
	  <tr>
      <td nowrap class="TableContent">文件标题： </td>
      <td class="TableData"colspan="3" id="docTitle" ></td>
	  </tr>
    <tr>
      <td nowrap class="TableContent">文号： </td>
      <td class="TableData" id="docNo" colspan="3"></td>
    </tr>
	  <tr>
      <td nowrap class="TableContent" style="width:147px;">文件类型： </td>
      <td class="TableData"  id="docType" ></td>
      <td nowrap class="TableContent" style="width:147px;">紧急程度： </td>
      <td class="TableData"  id="urgentType"  ></td>
	  </tr>
    <tr>
      <td nowrap class="TableContent" style="width:147px;">密级： </td>
      <td class="TableData"  id="securityLevel" ></td>
      <td nowrap class="TableContent" style="width:147px;">打印份数： </td>
      <td class="TableData" id="printCount" ></td>
    </tr>
    
    
    <tr>
      <td nowrap class="TableContent">备注： </td>
      <td class="TableData" colspan="3" id="remark"></td>
    </tr>
    <tr id="attr_tr">
      <td nowrap class="TableContent">附件文档：</td>
      <td class="TableData" colspan="3">
	      <input type = "hidden" id="attachmentName" name="attachmentName"></input>
	      <input type = "hidden" id="attachmentId" name="attachmentId"></input>
	      <span id="attr">无附件</span>
      </td>
    </tr>
  
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
        <input type="hidden" name="dtoClass" value="yh.subsys.jtgwjh.docReceive.data.YHJhDocrecvInfo">
        <input type="hidden" id="seqId" name="seqId" value="">
        <input type="hidden" id="moduel" name="moduel" value="docReceive">
        <input type="hidden" name="careContent" id="careContent" value="">
        <input type="button" value="关闭" onclick="window.close();" class="BigButton">
      </td>
    </tr>
  </table>
</form>

<br>

</body>