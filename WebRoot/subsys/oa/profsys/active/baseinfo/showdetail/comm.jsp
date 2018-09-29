<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "0" :  request.getParameter("seqId");
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<html>
<head>
<title>会谈纪要管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
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
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script Language="JavaScript">
var upload_limit=1,limit_type="php,php3,php4,php5,";
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
function doOnload() {
  var projId = parent.projId;
  if (projId) {
    document.getElementById("projId").value = projId;
  }
  selectInMem();
}
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
  var url= "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectCommAct/deleleFile.act?attachId=" + attachId +"&attachName=" + encodeURIComponent(attachName) + "&seqId=" + attrchIndex;
  var rtJson = getJsonRs(url); 
    if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  return true;
}

//查询是否有会议纪要

var pageMgr = null;
var cfgs = null;
function selectInMem(){
  var projId = $("projId").value;
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectCommAct/queryOutCommByProjId.act?projId=" + projId + "&projCommType=2";
   cfgs = {
    dataAction: url,
    container: "giftList",
    moduleName:"profsys",  
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"commNum", text:"纪要编号", width: "6%",align:"center"},
       {type:"text", name:"commName", text:"纪要名称", width: "6%",align:"center"},
       {type:"text", name:"commMemCn", text:"中方人员", width: "6%",align:"center"},
       {type:"text", name:"commMemFn", text:"外方人员", width: "6%",align:"center"},
       {type:"text", name:"commTime", text:"时间", width: "6%",align:"center",render:toTime},
       {type:"text", name:"commPlace", text:"地点", width: "6%",align:"center"},
       {type:"hidden", name:"attachId", text:"附件ID",align:"center",width:"1%"},
       {type:"text", name:"attach", text:"附件",align:"center",width:"8%",dataType:"attach"},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"8%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if (total <= 0) {
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>尚未添加会议纪要！</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table); 
  }
}
function toTime(cellData, recordIndex, columInde){
  var commTime = this.getCellData(recordIndex,"commTime");
  return commTime.substr(0,10);
}
function toOpts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href='#' onclick='getCommById(" + seqId + ");'>查看明细</a> ";
}
function deleteCommById(seqId){
  var msg="确定要删除该项 吗?";
  if(window.confirm(msg)){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectCommAct/deleteCommById.act?seqId=" + seqId; 
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
function getCommById(seqId) {
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectCommAct/getCommById.act?seqId=" + seqId; 
    var json = getJsonRs(requestURL);
    if (json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ;
    }
    var prc = json.rtData; 
    if (prc.seqId) {
      var seqId = prc.seqId;
      $("commNum").innerHTML = prc.commNum;
      $("commName").innerHTML = prc.commName;
      $("commMemCn").innerHTML = prc.commMemCn;
      $("commMemFn").innerHTML = prc.commMemFn;
      $("commTime").innerHTML = prc.commTime.substr(0,10);
      $("commPlace").innerHTML = prc.commPlace;
      $("commContent").innerHTML = prc.commContent;
      $("commNote").innerHTML = prc.commNote;
      $("attachmentName").value = prc.attachmentName;
      $("attachmentId").value = prc.attachmentId;
      doOnloadFile(seqId);
    }
}
</script>
</head>
<body  topmargin="5" onLoad="doOnload()">
<table border="0" width="50%" cellspacing="0" cellpadding="3" class="small">
	<tr><td>
	<img src="<%=imgPath %>/notify_new.gif" align="absmiddle"/>
	<span class="big3">添加项目会谈纪要</span><td></tr>
</table>

<input type="hidden" id="qRen" name="qRen" value="">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.profsys.data.YHProjectComm">
<input type="hidden" name="projCommType" id="projCommType" value="0">
<input type="hidden" name="projId" id="projId" value="">
<input type="hidden" name="seqId" id="seqId" value="">
<input type="hidden" value="<%=user.getSeqId() %>" name="projCreator" id="projCreator">
<input type="hidden" value="<%=sf.format(new Date())%>" name="projDate" id="projDate">
<table class="TableBlock" border="0" width="80%" align="center">
    <tr>
      <td nowrap class="TableContent" width="90">纪要编号：</td>
      <td nowrap class="TableData"  id="commNum">
      </td>  
    <td nowrap class="TableContent" width="90">纪要名称：</td>
      <td nowrap class="TableData" id="commName">
      </td> 
    </tr>
    <tr>
      <td nowrap class="TableContent">中方人员：</td>
      <td nowrap class="TableData" id="commMemCn" >
      </td>  
    <td nowrap class="TableContent">外方人员：</td>
      <td nowrap class="TableData" id="commMemFn" >
      </td> 
    </tr>
      <tr>
      <td nowrap class="TableContent" width="90">时间：</td>
      <td nowrap class="TableData" id="commTime"> 
      </td>
      <td nowrap class="TableContent" width="90">地点：</td>
      <td nowrap class="TableData"  id="commPlace" >
      </td> 
     </tr>
     <tr>
      <td nowrap class="TableContent" width="90">内容：</td>
      <td class="TableData" colspan="3" id="commContent" >
</td>
    </tr>
     <tr>
      <td nowrap class="TableContent" width="90">备注：</td>
      <td class="TableData" colspan="3"  id="commNote"> 
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
  <img src="<%=imgPath%>/user_group.gif" align="absmiddle"/>
  <span class="big3">会议纪要列表</span>
  <td></tr>
</table>
<br>
<div id="giftList" style="padding-left: 10px; padding-right: 10px"></div>
<div id="returnNull"></div>
</body>
</html>