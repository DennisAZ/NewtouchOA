<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
  String seqId = request.getParameter("seqId") == null ? "" : request.getParameter("seqId");
  String projId = request.getParameter("projId") == null ? "" : request.getParameter("projId");
%>
<html>
<head>
<title>项目成员管理</title>
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
<script type="text/javascript"> 
var seqId = "<%=seqId%>";
var upload_limit=1,limit_type="php,php3,php4,php5,";
function doOnload(){
  var projId = parent.projId;
  $("projId").value = projId;
  selectInMem();
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

var isUploadBackFun = false;
//附件上传
function upload_attach(){
  if(true){
   $("btnFormFile").click();
  }  
}
function handleSingleUpload(rtState,rtMsrg,rtData) {
  var data = rtData.evalJSON(); 
  $('attachmentId').value += "," + data.attrId;
  $('attachmentName').value += "*" + data.attrName;
  attachMenuUtil("attr","profsys",null,$('attachmentName').value ,$('attachmentId').value,false);
  removeAllFile();
  if(isUploadBackFun){
    doInit();
  }
  return true;
}
//有附件，也执行上传附件

function jugeFile(){
var formDom  = document.getElementById("formFile");
var inputDoms  = formDom.getElementsByTagName("input"); 
for(var i=0; i<inputDoms.length; i++){
  var idval = inputDoms[i].id;
  if(idval.indexOf("ATTACHMENT")!=-1){
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
  var url= "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectMemAct/deleleFile.act?attachId=" + attachId +"&attachName=" + encodeURIComponent(attachName) + "&seqId=" + attrchIndex;
  var rtJson = getJsonRs(url); 
    if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  return true;
}

function getMemById(seqId){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/in/YHInProjectMemAct/getMemById.act?seqId=" + seqId; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData; 
  if(prc.seqId){
    $("memNum").innerHTML = prc.memNum;
    $("memName").innerHTML = prc.memName;
    var memSexDesc = "";
    if(prc.memSex=='1'){
      memSexDesc = "男";
     }
    if(prc.memSex=='2'){
      memSexDesc = "女";
     }
    $("memSex").innerHTML = memSexDesc;
    $("memPosition").innerHTML = prc.memPositionName;
    var roleDesc = "";
    if(prc.memRole =='0'){
      roleDesc = "外办人员";
    }
    if(prc.memRole =='2'){
      roleDesc = "外宾";
    }
    $("memRole").innerHTML = roleDesc;
    $("unitNum").innerHTML = prc.unitNum;
    $("unitManNum").innerHTML = prc.unitManNum;
    $("unitName").innerHTML = prc.unitName;
    if(prc.includeFn == 'on'){
      $("includeFn").checked = true;
    }
    $("memNote").innerHTML = prc.memNote;
    $("attachmentName").value = prc.attachmentName;
    $("attachmentId").value = prc.attachmentId;
      doOnloadFile(seqId);
  }
}

var pageMgr = null;
var cfgs = null;
function selectInMem(){
  var projId = $("projId").value;
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/active/YHActiveProjectMemAct/queryActiveMemByProjId.act?projId="+projId;
   cfgs = {
    dataAction: url,
    container: "memDiv",
    moduleName:"profsys",  
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"memNum", text:"成员编号", width: "6%",align:"center"},
       {type:"text", name:"memPosition", text:"职务", width: "6%",align:"center",render:toMemPosition},
       {type:"text", name:"memName", text:"姓名", width: "6%",align:"center"},
       {type:"text", name:"memSex", text:"性别", width: "6%",align:"center",render:toSex},
       {type:"text", name:"unitNum", text:"单位编号", width: "6%",align:"center"},
       {type:"text", name:"unitManNum", text:"单位名称", width: "6%",align:"center"},
       {type:"text", name:"unitName", text:"单位人数", width: "6%",align:"center",},
       {type:"hidden", name:"includeFn", text:"包含外住", width: "6%",align:"center"},
       {type:"text", name:"attachName", text:"附件",align:"center",width:"8%",dataType:"attach"},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"8%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
}
function toSex(cellData, recordIndex, columInde){
  var memSex = this.getCellData(recordIndex,"memSex");
  var sexStr = "";
  if(memSex==1){
    sexStr = "男";
  }
  if(memSex==2){
    sexStr = "女";
  }
  return sexStr;
}
function toMemPosition(cellData, recordIndex, columInde){
  var memPosition = this.getCellData(recordIndex,"memPosition");
  var seqId = "?seqId=" + memPosition;
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectMemAct/userName.act" + seqId;
  var rtJson = getJsonRs(requestUrl);
  var userList = rtJson.rtData;
  return userList;
}
function toBirth(cellData, recordIndex, columInde){
  var memBirth = this.getCellData(recordIndex,"memBirth");
  return memBirth.substr(0,10);
}
function toOpts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return   "<a href='#' onclick='getMemById(" + seqId + ");'>查看详情</a> ";
}
function toInfo(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var memPhone = this.getCellData(recordIndex,"memPhone");
  var memMail = this.getCellData(recordIndex,"memMail");
  var memFax = this.getCellData(recordIndex,"memFax");
  var memAddress = this.getCellData(recordIndex,"memAddress");
  var infoStr = "<font color=red>电话：" + memPhone + ";邮箱：" + memMail + ";传真：" +memFax + ";地址：" + memAddress + ";</font>&nbsp;&nbsp;&nbsp;";
  return   "<div style='cursor:pointer' onclick=getTD(this," + seqId + ",\"" + encodeURIComponent(infoStr) + "\")>点击查看</div>";
}
function getTD(obj,seqId,info){
  var objTD =obj.parentNode;
  var objTR =objTD.parentNode;
  var objTable = objTR.parentNode;
  currRowIndex = objTR.rowIndex;
  if($("td_"+seqId)){//删除一行
    objTable.deleteRow(currRowIndex+1);
  }else{
    var mynewrow = objTable.insertRow(currRowIndex + 1);//新建一行
    var cellnum = mynewrow.cells.length;
    mynewcell=mynewrow.insertCell(cellnum);
    mynewcell.id = "td_"+seqId;
    mynewcell.align="right";
    mynewcell.colSpan="9";
    mynewcell.innerHTML = decodeURIComponent(info);
  }
}
function deleteMemById(seqId){
  var msg="确定要删除该项 吗?";
  if(window.confirm(msg)){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectMemAct/deleteMemById.act?seqId=" + seqId; 
    var json=getJsonRs(requestURL); 
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    alert("删除成功！");
    window.location.reload();
  }
}

</script>
</head>
 
<body topmargin="5" onLoad="doOnload();">

<table border="0" width="50%" cellspacing="0" cellpadding="3" class="small">
  <tr><td>
  <img src="<%=imgPath%>/notify_new.gif" align="absmiddle"/>
  <span class="big3">添加项目成员</span><td></tr>
</table>
<table class="TableBlock" border="0" width="80%" align="center">
 <tr>
      <td colspan=4 class="TableContent">
      <img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 个人信息
       </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">成员编号：</td>
      <td nowrap class="TableData" id="memNum" width="120"></td>
     <td nowrap class="TableContent" width="90">成员身份：</td>
      <td nowrap class="TableData" id="memRole" width="170"></td>  
    </tr>

    <tr>
      <td nowrap class="TableContent" width="90">姓名：</td>
      <td nowrap class="TableData" colspan="3"  id="memName"></td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">性别：</td>
      <td nowrap class="TableData" id="memSex"></td>
        <td nowrap class="TableContent" width="90">职务：</td>
      <td nowrap class="TableData" id="memPosition"></td> 
     </tr>
     <tr>
      <td colspan=4 class="TableContent"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 单位信息 </td>
    </tr>
     <tr>
      <td nowrap class="TableContent" width="90">单位编号：</td>
      <td nowrap class="TableData" id="unitNum">
      </td>
      <td nowrap class="TableContent" width="90">单位人数：</td>
      <td class="TableData"  >
      <span id="unitManNum"></span>
        &nbsp;&nbsp;<input type="checkbox" name="includeFn" id="includeFn"  readonly ><label for="INCLUDE_FN">含驻京外国人</label><br>
      </td>
    </tr>
     <tr>
      <td nowrap class="TableContent" width="90">单位名称：</td>
      <td nowrap class="TableData" colspan="3" id="unitName">
      </td>
     </tr>
     <tr>
      <td nowrap class="TableContent" width="90">备注：</td>
      <td class="TableData" colspan="3" id="memNote"> 
</tr>
<tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData" colspan="3">

      <input type = "hidden" id="attachmentName" name="attachmentName"></input>
       <input type = "hidden" id="attachmentId" name="attachmentId"></input>
        <span id="attr">无附件</span>
      </td>
    </tr>
   <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
      <input type="hidden" name="OP" value="">

      <input type="hidden" name="projMemType" id="projMemType" value="2">
      <input type="hidden" name="projId" id="projId" value="2">
      <input type="hidden" name="dtoClass" value="yh.subsys.oa.profsys.data.YHProjectMem">
      <input type="hidden" id="moduel" name="moduel" value="profsys">
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();   parent.window.close()">&nbsp;
        
      </td>
  </tr> 
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
<div>
  <img src="<%=imgPath%>/user_group.gif" align="absmiddle"/>
  <span class="big3">项目成员列表</span>
</div>
<div id="memDiv"></div>
</body>
</html>
 



