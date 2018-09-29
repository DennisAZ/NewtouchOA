<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
SimpleDateFormat sf = new SimpleDateFormat("yyyy");
String dayTime = sf.format(new Date());
String seqId = request.getParameter("seqId") == null ? "0" : request.getParameter("seqId");
String headerStr = "收文登记";
if(!seqId.equals("0")){
  headerStr = "编辑收文";
}

%>
<html>
<head>
<title>项目类别</title>
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

//验证
function checkForm() {
  /*if($("sendDeptName").value.trim() == ""){
    alert("发送单位是必填项!");
    $("sendDeptName").focus();
    $("sendDeptName").select();
    return false;
  }*/
  if($("docTitle").value.trim() == ""){
    alert("文件标题是必填项!");
    $("docTitle").focus();
    $("docTitle").select();
    return false;
  }
/*  if (!isInteger($("printCount").value.trim())) {
    alert("打印份数为整数类型!");
    $("printCount").focus();
    $("printCount").select();
    return false;
  }*/
  if (!isInteger($("pageCount").value.trim())) {
    alert("文件页数为整数类型!");
    $("pageCount").focus();
    $("pageCount").select();
    return false;
  }
  if (!isInteger($("attachCount").value.trim())) {
    alert("附件数为整数类型!");
    $("attachCount").focus();
    $("attachCount").select();
    return false;
  }
  return true;
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

  $("docNo").readOnly  = true;
  $("docType").disabled = true;
  $("docType").disabled = true;
  
  $("docType").disabled = true;
  }
}


 /*
 **新建或删除
 */
 
function doInit(){
  if(checkForm()){
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
     return ;
    }
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/addUpdateGroup.act"; 
    var json=getJsonRs(requestURL,pars); 
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      var prc = json.rtData;
      alert("保存成功！")
      window.location.reload();
    }
  }
}
//附件处理开始
/**
 * 上传文件后执行
 */
function handleSingleUpload(rtState,rtMsrg,rtData) {
  var data = rtData.evalJSON(); 
  $('attachmentId').value += "," + data.attrId;
  $('attachmentName').value += "*" + data.attrName;
  attachMenuUtil("attr",$("moduel").value,null,$('attachmentName').value ,$('attachmentId').value,false);
  removeAllFile();
  if(isUploadBackFun){//返回后再次执行
    doInit();
  }
  return true;
}

//删除附件
var isDel = false;
function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
	var attachNameOld = $('attachmentName').value; 
	var attachIdOld = $('attachmentId').value; 
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
	
	$('attachmentId').value = attaId; 
	$('attachmentName').value = attaName;
	isDel = true;
	//doInit();
	return true;
}
var selfdefMenu = {
    office:["read","deleteFile"], 
    img:["play","deleteFile"],  
    music:["play","deleteFile"],  
    video:["play","deleteFile"], 
    others:["deleteFile"]
  }
var  selfdefMenu2 = {
    office:["read"], 
    img:["play"],  
    music:["play"],  
    video:["play"], 
    others:[]
  }

function doOnloadFile(){
  var attr = $("attr");
  var seqId  = $("seqId").value;
  attachMenuSelfUtil(attr,$("moduel").value,$('attachmentName').value ,$('attachmentId').value, '','',seqId,selfdefMenu2);
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
  //window.location.href = "waitReceive.jsp";
  history.go(-1);
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

      <td nowrap class="TableData">发文单位： <font color="red">*</font></td>
      <td class="TableData"colspan="5"  id="sendDeptName">
     <!--    	<input type="hidden" id="sendDept" name="sendDept" value="">
        <textarea name="sendDeptName" rows="3" style="width:80%"></textarea>
       -->
      </td>
	  </tr>
	  <tr>
      <td nowrap class="TableData">文件标题： <font color="red">*</font></td>
      <td class="TableData"colspan="5">
        <input type="text" name="docTitle" id="docTitle" class="BigInput" size="80">
      </td>
	  </tr>
	  <tr>
      <td nowrap class="TableData">文件类型： </td>
      <td class="TableData" style="width: 147px;">
        <select id="docType" name="docType" style="width:71px;"  >
          <option value='0'>普通</option>
          <option value='1'>公文</option>
        </select>
      </td>
       <td nowrap class="TableData">密级： </td>
      <td class="TableData">
        <select id="securityLevel" name="securityLevel" style="width:71px;">
               <option value='0'>普通</option>
          <option value='1'>内部</option>
          <option value='2'>秘密</option>
          <option value='3'>机密</option>
        </select>
      </td>
	   
      <td nowrap class="TableData">紧急程度： </td>
      <td class="TableData">
        <select id="urgentType" name="urgentType" style="width:71px;" >
          <option value='0'>一般</option>
          <option value='1'>紧急</option>
          <option value='2'>特急</option>
        </select>
      </td>
	  </tr>
    <tr>
     
      <td nowrap class="TableData">保密期限： </td>
      <td class="TableData">
        <input type="text" name="securityTime" id="securityTime" class="BigInput" size="5"> 年
      </td>
      <td nowrap class="TableData">打印份数： </td>
      <td class="TableData" id="printCount">
       
      </td>
       <td nowrap class="TableData">文号： </td>
      <td class="TableData">
        <input type="text" name="docNo" id="docNo" class="BigInput" size="15"  >
      </td>
       
    </tr>
    <tr>
     
      <td nowrap class="TableData">文件页数： </td>
      <td class="TableData">
        <input type="text" name="pageCount" id="pageCount" class="BigInput" size="5">
      </td>
      <td nowrap class="TableData">附件数： </td>
      <td class="TableData">
        <input type="text" name="attachCount" id="attachCount" class="BigInput" size="5">
      </td>
      
      <td nowrap class="TableData"> </td>
	    <td class="TableData">
	    <span style="display:none;"><select id="docKind" name="docKind" style="width:71px;">
          <option value='0'>普通</option>
          <option value='1'>通知</option>
        </select></span>
        
	    </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注： </td>
      <td class="TableData" colspan="5">
        <textarea id="remark" name="remark" rows="3" style="width:97%" class="BigInput"></textarea>
      </td>
    </tr>
    <tr  style="display:none;">
      <td nowrap class="TableData">接收单位： </td>
      <td class="TableData" colspan="5">
        <input type="hidden" id="receiveDept" name="receiveDept" value="">
        <textarea id="receiveDeptName" name="receiveDeptName" rows="3" style="width:80%" class="BigStatic"></textarea>
        <a href="javascript:void(0);" class="orgAdd" onClick="selectDept(['receiveDept', 'receiveDeptName'])">添加</a>
        <a href="javascript:void(0);" class="orgClear" onClick="$('receiveDept').value='';$('receiveDeptName').value='';">清空</a>
      </td>
    </tr>
  
       <tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData" colspan="5">

      <input type = "hidden" id="attachmentName" name="attachmentName"></input>
       <input type = "hidden" id="attachmentId" name="attachmentId"></input>
        <span id="attr">无附件</span>
      </td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData" colspan="5">
       
       <script>ShowAddFile();</script>
	   <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
       <input type="hidden" id="ATTACHMENT_ID_OLD"  name="ATTACHMENT_ID_OLD" value="">
	   <input type="hidden" id="ATTACHMENT_NAME_OLD"  name="ATTACHMENT_NAME_OLD" value="">	   
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan='6' nowrap>
      <input type="hidden" name="dtoClass" value="yh.subsys.jtgwjh.docReceive.data.YHJhDocrecvInfo">
         <input type="hidden" id="seqId" name="seqId" value="">
  
        <input type="hidden" name="careContent" id="careContent" value="">
        <input type="button" value="重置" onclick="form1.reset()" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="保存" onclick="doInit();" class="BigButton">

        <% if(!seqId.equals("0")){ %>
             <input type="button" value="返回" onclick="returnInfo()" class="BigButton">
        <%} %>
      </td>
    </tr>
  </table>
</form>

<br>
<form id="formFile" action="<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
   <input type="hidden" id="moduel" name="moduel" value="docReceive">
  </form>
  <iframe width="0" height="0" name="commintFrame" id="commintFrame"></iframe>


</body>