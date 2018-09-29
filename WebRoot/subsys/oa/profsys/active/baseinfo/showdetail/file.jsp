<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "0" :  request.getParameter("seqId");
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<html>
<head>
<title>项目相关文档管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
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
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script Language="JavaScript">
var upload_limit=1,limit_type="php,php3,php4,php5,";
function doOnload() {
  var projId = parent.projId;
  if (projId) {
    document.getElementById("projId").value = projId;
  }
  selectInMem();
}
//添加项目相关文档
var isUploadBackFun = false;
//附件上传
function upload_attach(){
  if(checkForm()){
    $("btnFormFile").click();
  }  
}
function handleSingleUpload(rtState,rtMsrg,rtData) {
  var data = rtData.evalJSON(); 
  $('attachmentId').value += "," + data.attrId;
  $('attachmentName').value += "*" + data.attrName;
  attachMenuUtil("showAtt","profsys",null,$('attachmentName').value ,$('attachmentId').value,false);
  removeAllFile();
  if(isUploadBackFun){
    doInit();
  }
  return true;
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
//上传后可以显示浮动菜单

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
  
  return true;
}

//查询是否有项目相关文档

var pageMgr = null;
var cfgs = null;
function selectInMem(){
  var projId = parent.projId;
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectFileAct/queryOutFileByProjId.act?projId=" + projId + "&projFileType=2";
   cfgs = {
    dataAction: url,
    container: "giftList",
    moduleName:"profsys",  
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"fileNum", text:"文档编号", width: "6%",align:"center"},
       {type:"text", name:"fileName", text:"文档名称", width: "6%",align:"center"},
       {type:"text", name:"fileType", text:"文档类别", width: "6%",align:"center"},
       {type:"text", name:"fileCreator", text:"创建人", width: "6%",align:"center"},
       {type:"text", name:"fileTitle", text:"文档主题词", width: "6%",align:"center"},
       {type:"hidden", name:"attachId", text:"附件ID",align:"center",width:"1%"},
       {type:"text", name:"attach", text:"附件",align:"center",width:"8%",dataType:"attach"},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"8%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if (total <= 0) {
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>尚未添加项目相关文档！</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table); 
  }
}
function toOpts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href='#' onclick='getFileById(" + seqId + ");'>查看明细</a> ";
}
function deleteFileById(seqId){
  var msg="确定要删除该项 吗?";
  if(window.confirm(msg)){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectFileAct/deleteFileById.act?seqId=" + seqId; 
    var json=getJsonRs(requestURL); 
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    alert("删除成功！");
    window.location.reload();
  }
}
//修改
function getFileById(seqId) {
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectFileAct/getFileById.act?seqId=" + seqId; 
    var json = getJsonRs(requestURL);
    if (json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ;
    }
    var prc = json.rtData; 
    if (prc.seqId) {
      var seqId = prc.seqId;
      $("fileNum").innerHTML = prc.fileNum;
      $("fileName").innerHTML = prc.fileName;
      $("fileType").innerHTML = prc.fileType;
      $("fileCreator").innerHTML = prc.projCreatorName;
      $("fileTitle").innerHTML = prc.fileTitle;
      $("fileContent").innerHTML = prc.fileContent;
      $("fileNote").innerHTML = prc.fileNote;
      $("attachmentName").value = prc.attachmentName;
      $("attachmentId").value = prc.attachmentId;
      doOnloadFile(seqId);
    }
}
var  selfdefMenu = {
  	office:["downFile","dump","read"], 
    img:["downFile","dump","play"],  
    music:["downFile","dump","play"],  
    video:["downFile","dump","play"], 
    others:["downFile","dump"]
	}
function doOnloadFile(seqId){
  var attr = $("attr");
  attachMenuSelfUtil(attr,"profsys",$('attachmentName').value ,$('attachmentId').value, '','',seqId,selfdefMenu);
}
</script>
</head>
<body  topmargin="5" onLoad="doOnload();">
<input type="hidden" id="qRen" name="qRen" value="">
<table border="0" width="50%" cellspacing="0" cellpadding="3" class="small">
  <tr><td>
  <img src="<%=imgPath%>/notify_new.gif" align="absmiddle"/>
  <span class="big3">添加项目相关文档</span><td></tr>
</table>
<div id="seqIdDiv"></div>
<input type="hidden" name="dtoClass" value="yh.subsys.oa.profsys.data.YHProjectFile">
<input type="hidden" name="projFileType" id="projFileType" value="0">
<input type="hidden" name="projId" id="projId" value="">
<input type="hidden" name="seqId" id="seqId" value="">
<table class="TableBlock" border="0" width="70%" align="center">
    <tr>
      <td nowrap class="TableContent" width="90">文档编号：</td>
      <td nowrap class="TableData" id="fileNum">
      </td>
    <td nowrap class="TableContent" width="90">文档名称：</td>
      <td nowrap class="TableData" id="fileName">
      </td> 
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">文档类别：</td>
      <td nowrap class="TableData" id="fileType" >
      </td>
       <td nowrap class="TableContent" width="90">创建人：</td>
      <td nowrap class="TableData" id="fileCreator">
      </td> 
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">文档主题词：</td>
      <td class="TableData" colspan="3"  id="fileTitle">
      </td>     
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">内容：</td>
      <td class="TableData" colspan="3" id="fileContent">
</td>
    </tr>
     <tr>
      <td nowrap class="TableContent" width="90">备注：</td>
      <td class="TableData" colspan="3"  id="fileNote">
       </td>
    </tr>   
    <tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData" colspan="3">
      <input type = "hidden" id="attachmentName" name="attachmentName"></input>
       <input type = "hidden" id="attachmentId" name="attachmentId"></input>
        <span id="attr"></span>
      </td>
    </tr>
  
   <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
 <div id="but">
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();parent.window.close()">&nbsp;
      </div>
      </td>
  </tr> 
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr><td>
  <img src="<%=imgPath %>/user_group.gif" align="absmiddle"/>
  <span class="big3">项目相关文档列表</span>
  <td></tr>
</table>
<div id="giftList" style="padding-left: 10px; padding-right: 10px"></div>
<div id="returnNull"></div>
</body>
</html>