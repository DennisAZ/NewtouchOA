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

//图片插入到正文

function InsertImage(src) { 
  var oEditor = FCKeditorAPI.GetInstance('PROFSYS_MEM_CONTENT') ; //FCK实例 
  //alert(src);
  if (oEditor.EditMode == FCK_EDITMODE_WYSIWYG ) { 
  oEditor.InsertHtml( "<img src = '"+ src + "'/>") ; 
  } 
}
function editFck(content) {//给FCK赋值
  var oEditor = FCKeditorAPI.GetInstance('PROFSYS_MEM_CONTENT') ;
  oEditor.SetData(content);
}
function getContent(id,fckId){//FCK传值
  var oEditor = FCKeditorAPI.GetInstance(fckId) ;
  $(id).value=oEditor.GetXHTML();
}
function doOnload(){
  var projId = parent.projId;
  $("projId").value = projId;
  selectInMem();
  //时间
  var parameters = {
      inputId:'memBirth',
      property:{isHaveTime:false}
      ,bindToBtn:'date'
  };
  new Calendar(parameters);

  
}
//表单验证
function checkForm() {
   if ($("memNum").value == "") {
     alert("编号必填!");
     $("memNum").focus();
     $("memNum").select();
     return false;
  }
   if ($("memRole").value == "") {
    alert("成员身份必填!");
    $("memRole").focus();
    $("memRole").select();
    return false;
   }
   if ($("memName").value == "") {
     alert("姓名必填!");
     $("memName").focus();
     $("memName").select();
     return false;
   }
   if ($("memSex").value == "") {
     alert("性别必填!");
     $("memSex").focus();
     $("memSex").select();
     return false;
   }
   if ($("memIdNum").value == "") {
     alert("证件号码必填!");
     $("memIdNum").focus();
     $("memIdNum").select();
     return false;
   }
   getContent('memNote','PROFSYS_MEM_CONTENT');
   return true;
}

var  selfdefMenu = {
  	office:["downFile","dump","read","deleteFile"], 
    img:["downFile","dump","play","deleteFile"],  
    music:["downFile","dump","play","deleteFile"],  
    video:["downFile","dump","play","deleteFile"], 
    others:["downFile","dump","deleteFile"]
	}
function doOnloadFile(){
  var attr = $("attr");
  var seqId  = $("seqId").value;
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
  $("seqId").value = seqId;
  var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/in/YHInProjectMemAct/getMemById.act?seqId=" + seqId; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData; 
  if(prc.seqId){
    var seqId = prc.seqId;
    $("memNum").value = prc.memNum;
    $("memRole").value = prc.memRole;
    $("memName").value = prc.memName;
    $("memSex").value = prc.memSex;
    $("memNation").value = prc.memNation;
    $("memPosition").value = prc.memPosition;
    $("memPositionName").value = prc.memPositionName;
    $("memBirthplace").value = prc.memBirthplace;
    $("memNativePlace").value = prc.memNativePlace;
    $("memBirth").value = prc.memBirth.substr(0,10);
    $("memIdNum").value = prc.memIdNum;
    $("memPhone").value = prc.memPhone;
    $("memMail").value = prc.memMail;
    $("memFax").value = prc.memFax;
    $("memAddress").value = prc.memAddress;
    $("memNote").value = prc.memNote;
    //给fck传值
    editFck(prc.memNote);
    $("attachmentName").value = prc.attachmentName;
    $("attachmentId").value = prc.attachmentId;
    if(prc.attachmentId != ''){
      doOnloadFile();
    }
  }
}

function doInit(){
  if (checkForm()) {
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
      return;
    }
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/in/YHInProjectMemAct/addUpdateMem.act";
    var json = getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      alert("保存成功!");
      var prc = json.rtData;
      //parent.seqId = prc.seqId;
      window.location.reload();
      parent.opener.location.reload();
    }
  }

}



var pageMgr = null;
var cfgs = null;
function selectInMem(){
  var projId = $("projId").value;
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/in/YHInProjectMemAct/queryInMemByProjId.act?projId="+projId;
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
       {type:"text", name:"memBirth", text:"出生年月", width: "6%",align:"center",render:toBirth},
       {type:"text", name:"memIdNum", text:"证件号码", width: "6%",align:"center"},
       {type:"text", name:"memPhone", text:"联系方式", width: "6%",align:"center",render:toInfo},
       {type:"hidden", name:"memMail", text:"邮件", width: "6%",align:"center"},
       {type:"hidden", name:"memFax", text:"传真", width: "6%",align:"center"},
       {type:"hidden", name:"memAddress", text:"地址", width: "6%",align:"center"},
       {type:"hidden", name:"attachId", text:"附件ID", width: "6%",align:"center"},
       {type:"text", name:"attachName", text:"附件",align:"center",width:"8%",dataType:"attach"},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"8%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if (total <= 0) {
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>尚未添加项目成员！</div></td></tr>"
        );
    $('memDiv').style.display = "none";
    $('returnNull').update(table); 
  }
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
  return   "<a href='#' onclick='getMemById(" + seqId + ");'>编辑</a> "
           +"<a href='javascript:deleteMemById(" + seqId + ");'> 删除 </a> ";
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
 
<body  onload="doOnload();" style="margin:10px;">
<table border="0" width="50%" cellspacing="0" cellpadding="3" class="small" style="margin-bottom:10px;">
  <tr><td>
  <img src="<%=imgPath%>/notify_new.gif" align="absmiddle"/>
  <span class="big3">添加项目成员</span><td></tr>
</table>
 <form action="#" method="post" name="form1" id="form1"  onSubmit="return checkForm();">

<table class="TableBlock" border="0" width="80%" align="center">
    <tr>
      <td nowrap class="TableContent">成员编号：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memNum" id="memNum" value="" size="10" maxLength="40">
      </td>
     <td nowrap class="TableContent" width="90">成员身份：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
       <select name="memRole" id="memRole">
       <option value="" >--请选择--</option>
       <option value="0" >外办人员</option>
       <option value="2" >外宾</option>
       </select>
    </td>  
    
    
    </tr>
    <tr>
      <td nowrap class="TableContent">姓名：<span style="color:red">*</span></td>
      <td nowrap class="TableData" colspan="3">
        <input type="text" class="BigInput" name="memName" id="memName" value="" size=20 maxLength="20">
        <INPUT type="hidden" name="memNameId" id="memNameId">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['memNameId','memName']);">选择</a>
   <a href="javascript:;" class="orgClear" onClick="$('memName').value='';$('memNameId').value='';">清空 </a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">性别：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
        <select name="memSex" id="memSex">
        <option value="" >--请选择--</option>
       <option value="1" >男</option>
       <option value="2" >女</option>
       </select>
    </td>
        <td nowrap class="TableContent">职务：</td>
      <td nowrap class="TableData">
          <input type="hidden" name="memPosition" id="memPosition">
        <textarea cols=20 name="memPositionName" id="memPositionName" rows=1 class="BigStatic" wrap="yes" readonly></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectRole(['memPosition','memPositionName']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('memPositionName').value='';$('memPosition').value='';">清空 </a>
      </td> 
     </tr>
     <tr>
      <td nowrap class="TableContent">民族：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memNation" id="memNation" value="" size="10" maxLength="20">
      </td>
      <td nowrap class="TableContent">籍贯：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name=memNativePlace id="memNativePlace" value="" size="10" maxLength="20">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">出生地：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memBirthplace" id="memBirthplace" value="" size="20" maxLength="20">
      </td>
      <td nowrap class="TableContent">出生年月： </td>
      <td nowrap class="TableData"> 
        <input type="text"  name="memBirth" id="memBirth" class=BigInput size="10" value="" readonly>
        <img id="date" src="<%=imgPath%>/calendar.gif" align="absMiddle"  border="0" style="cursor:pointer">
      </td>
     </tr>
     <tr>
      <td nowrap class="TableContent" width="90">证件号码：<span style="color:red">*</span></td>
      <td nowrap class="TableData" >
     <input type="text" class="BigInput" name="memIdNum" id="memIdNum" value="" size="20" maxLength="20">
      </td>
      <td nowrap class="TableContent">电话：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memPhone" id="memPhone" value="" size="20" maxLength="20">
      </td>
    </tr>
     <tr>
      <td nowrap class="TableContent">邮箱：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memMail" id="memMail" value="" size="20" maxLength="20">
      </td>
      <td nowrap class="TableContent">传真：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memFax" id="memFax" value="" size="20" maxLength="20">
      </td> 
     </tr>
     <tr> 
      <td nowrap class="TableContent">地址：</td>
      <td nowrap class="TableData" colspan="3">
        <input type="text" class="BigInput" name="memAddress" id="memAddress" value="" size="30" maxLength="20">
      </td> 
     </tr>
     <tr>
      <td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3">
<div><script language=JavaScript>    
         var oFCKeditor = new FCKeditor('PROFSYS_MEM_CONTENT');//实例
         oFCKeditor.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
         oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
         oFCKeditor.Height = "300px";
         oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
         oFCKeditor.ToolbarSet="DiaryBar";
         oFCKeditor.Create();
         </script>
      <input type="hidden" id="memNote" name="memNote" value="">
</div>    
</tr>

      <tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData" colspan="3">

      <input type = "hidden" id="attachmentName" name="attachmentName"></input>
       <input type = "hidden" id="attachmentId" name="attachmentId"></input>
        <span id="attr">无附件</span>
      </td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData" colspan="3">
       <script>ShowAddFile();ShowAddImage();</script>
	   <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
       <input type="hidden" id="ATTACHMENT_ID_OLD"  name="ATTACHMENT_ID_OLD" value="">
	   <input type="hidden" id="ATTACHMENT_NAME_OLD"  name="ATTACHMENT_NAME_OLD" value="">	   
      </td>
    </tr>
   <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
      <input type="hidden" name="OP" value="">
      <input type="hidden" name="seqId" id="seqId" value="<%=seqId %>">
      <input type="hidden" name="projId" id="projId" value="<%=projId %>">
        <input type="hidden" name="projMemType" id="projMemType" value="0">
      <input type="hidden" name="dtoClass" value="yh.subsys.oa.profsys.data.YHProjectMem">
      <input type="hidden" id="moduel" name="moduel" value="profsys">
      <input type="button" value="保存" class="BigButton" onclick="doInit();">&nbsp;
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();   parent.window.close()">&nbsp;
        
      </td>
  </tr> 
</table>
  </form>
  <form id="formFile" action="<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectCalendarAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe> 
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
<div id="returnNull"></div>
</body>
</html>
 

