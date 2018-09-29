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
<title>项目成员管理</title>
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
<script type="text/javascript">
var projId = parent.seqId;
var projSeqId = 0;
var oFCKeditor = new FCKeditor('PROFSYS_CONTENT');
var upload_limit=1,limit_type="php,php3,php4,php5,";
function InsertImage(src) { 
  var oEditor = FCKeditorAPI.GetInstance('PROFSYS_CONTENT') ; //FCK实例 
  if (oEditor.EditMode == FCK_EDITMODE_WYSIWYG ) { 
  oEditor.InsertHtml( "<img src = '"+ src + "'/>") ; 
  } 
}
function doInit() {
  if (projId == "") {
    document.getElementById("projId").value = '<%=seqId%>';
    projSeqId = '<%=seqId%>';
  } else {
    document.getElementById("projId").value = projId;
    projSeqId = projId;
  }
  doTime();
  selectInMem();
}
function doTime() {
  //时间
  var parameters = {
      inputId:'memBirth',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
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
   return true;
}
//添加项目成员
var isUploadBackFun = false;
function checkForm2() {
  var FCK = FCKeditorAPI.GetInstance('PROFSYS_CONTENT'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行
  var FORM_MODE = FCK.EditingArea.Mode;
  // 获取编辑区域的常量——源文件模式
  var editingAreaFrame = document.getElementById('PROFSYS_CONTENT___Frame');
  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
  if(FORM_MODE == editModeSourceConst) {
    FCK.Commands.GetCommand('Source').Execute();
  } 
  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
  document.getElementById("memNote").value = FORM_HTML;
  if (checkForm()) {
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
      return ;
    }
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectMemAct/addProjectMem.act";
    var json = getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      alert("添加成功!");
      window.location.reload();
    }
  }
}

//修改项目成员
var isUploadBackFun = false;
function checkForm3() {
  var FCK = FCKeditorAPI.GetInstance('PROFSYS_CONTENT'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行
  var FORM_MODE = FCK.EditingArea.Mode;
  // 获取编辑区域的常量——源文件模式
  var editingAreaFrame = document.getElementById('PROFSYS_CONTENT___Frame');
  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
  if(FORM_MODE == editModeSourceConst) {
    FCK.Commands.GetCommand('Source').Execute();
  } 
  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
  document.getElementById("PROFSYS_CONTENT").value = FORM_HTML;
  document.getElementById("memNote").value = FORM_HTML;
  if (checkForm()) {
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
      return ;
    }
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectMemAct/updateProjectMem.act";
    var json = getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      alert("修改成功!");
      window.location.reload();
    }
  }
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
  if (isUploadBackFun) {
    if ($("qRen").value > 0) {
      checkForm3();
    } else {
      checkForm2();
    }
    isUploadBackFun = false;
   }
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
  if ($("qRen").value > 0) {
    var FCK = FCKeditorAPI.GetInstance('PROFSYS_CONTENT'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行
    var FORM_MODE = FCK.EditingArea.Mode;
    // 获取编辑区域的常量——源文件模式
    var editingAreaFrame = document.getElementById('PROFSYS_CONTENT___Frame');
    var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
    if(FORM_MODE == editModeSourceConst) {
      FCK.Commands.GetCommand('Source').Execute();
    } 
    var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
    document.getElementById("memNote").value = FORM_HTML;
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectMemAct/updateProjectMem.act";
    var json = getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      window.location.reload();
    }
  }
  return true;
}
//查询是否有成员
var pageMgr = null;
var cfgs = null;
function selectInMem(){
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectMemAct/queryOutMemByProjId.act?projId=" + projSeqId;
   cfgs = {
    dataAction: url,
    container: "giftList",
    moduleName:"profsys",  
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"memNum", text:"成员编号", width: "6%",align:"center"},
       {type:"text", name:"memPosition", text:"职务", width: "6%",align:"center",render:toUser},
       {type:"text", name:"memName", text:"姓名", width: "6%",align:"center"},
       {type:"text", name:"memSex", text:"性别", width: "6%",align:"center",render:toSex},
       {type:"text", name:"memBirth", text:"出生年月", width: "6%",align:"center",render:toBirth},
       {type:"text", name:"memIdNum", text:"证件号码", width: "6%",align:"center"},
       {type:"text", name:"memPhone", text:"联系方式", width: "6%",align:"center",render:toInfo},
       {type:"hidden", name:"memMail", text:"邮件", width: "6%",align:"center"},
       {type:"hidden", name:"memFax", text:"传真", width: "6%",align:"center"},
       {type:"hidden", name:"memAddress", text:"地址", width: "6%",align:"center"},
       {type:"hidden", name:"attachId", text:"附件ID",align:"center",width:"1%"},
       {type:"text", name:"attach", text:"附件",align:"center",width:"8%",dataType:"attach"},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"8%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if (total <= 0) {
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无项目成员!</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table); 
  }
}
//职位
function toUser(cellData,recordIndex,columIndex){
  var memPosition = this.getCellData(recordIndex,"memPosition");
  var seqId = "?seqId=" + memPosition;
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectMemAct/userName.act" + seqId;
  var rtJson = getJsonRs(requestUrl);
  var userList = rtJson.rtData;
  return userList;
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
  return   "<div style='cursor:pointer' onclick=getTD(this," + seqId + ",\"" + encodeURIComponent(infoStr) + "\")>查看详情</div>";
}
function getTD(obj,seqId,info) {
  var objTD =obj.parentNode;
  var objTR =objTD.parentNode;
  var objTable = objTR.parentNode;
  currRowIndex = objTR.rowIndex;
  if ($("td_"+seqId)) {//删除一行
    objTable.deleteRow(currRowIndex+1);
  }else {
    var mynewrow = objTable.insertRow(currRowIndex + 1);//新建一行
    var cellnum = mynewrow.cells.length;
    mynewcell = mynewrow.insertCell(cellnum);
    mynewcell.id = "td_"+seqId;
    mynewcell.align = "right";
    mynewcell.colSpan = "9";
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
//修改
function getMemById(seqId) {
  if (seqId != "") {
    $("qRen").value = seqId;
    $("but").update("<input type='button' value='保存' class='BigButton' onclick='javascript:checkForm3()'>&nbsp;"
        + "<input type='button' value='关闭' class='BigButton' onclick='javascript:parent.opener.location.reload();parent.window.close()'>&nbsp;");
    $("seqIdDiv").update("<input type='hidden' value='" + seqId +"' id='seqId' name='seqId'>");
  }else {
    $("but").update("<input type='button' value='保存' class='BigButton' onclick='javascript:checkForm2()'>&nbsp;"
        + "<input type='button' value='关闭' class='BigButton' onclick='javascript:parent.opener.location.reload();parent.window.close()'>&nbsp;");
  }
  if (seqId != "") {
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/in/YHInProjectMemAct/getMemById.act?seqId=" + seqId; 
    var json = getJsonRs(requestURL);
    if (json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ;
    }
    var prc = json.rtData; 
    if (prc.seqId) {
      var seqId = prc.seqId;
      $("memNum").value = prc.memNum;
      $("memRole").value = prc.memRole;
      $("memName").value = prc.memName;
      $("memSex").value = prc.memSex;
      $("memNation").value = prc.memNation;
      $("memBirthplace").value = prc.memBirthplace;
      $("memNativePlace").value = prc.memNativePlace;
      $("memBirth").value = prc.memBirth.substr(0,10);
      $("memIdNum").value = prc.memIdNum;
      $("memPhone").value = prc.memPhone;
      $("memMail").value = prc.memMail;
      $("memFax").value = prc.memFax;
      $("memAddress").value = prc.memAddress;
      $("projCreator").value = prc.projCreator;
      $("projDate").value = prc.projDate.substr(0,10);
      var oEditor = FCKeditorAPI.GetInstance('PROFSYS_CONTENT') ;
      oEditor.SetData(prc.memNote);
      $("memPosition").value  = prc.memPosition;
      if ($("memPosition").value != "") {
        bindDesc([{cntrlId:"memPosition",dsDef:"USER_PRIV,SEQ_ID,PRIV_NAME"}]);
      }
      $("attachmentName").value = prc.attachmentName;
      $("attachmentId").value = prc.attachmentId;
      attachMenuUtil("showAtt","profsys",null,$('attachmentName').value ,$('attachmentId').value,false);
    }
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()">
<input type="hidden" id="qRen" name="qRen" value="">
<table border="0" width="50%" cellspacing="0" cellpadding="3" class="small">
  <tr><td>
  <img src="<%=imgPath%>/notify_new.gif" align="absmiddle"/>
  <span class="big3">添加项目成员</span><td></tr>
</table>
<form id="form1" name="form1" >
<input type="hidden" name="dtoClass" value="yh.subsys.oa.profsys.data.YHProjectMem">
<div id="seqIdDiv"></div>
<input type="hidden" id="memNote" name="memNote" value="">
<input type="hidden" id="projId" name="projId" value="">
<input type="hidden" id="projMemType" name="projMemType" value="1">
<input type="hidden" value="<%=user.getSeqId() %>" name="projCreator" id="projCreator">
<input type="hidden" value="<%=sf.format(new Date())%>" name="projDate" id="projDate">
<table class="TableBlock" border="0" width="80%" align="center">
    <tr>
      <td nowrap class="TableContent">成员编号：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memNum" id="memNum" value="" size="10" maxlength="19">
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
        <input type="text" name="memName" id="memName" value=""  size=20 maxlength="19">
        <INPUT type="hidden" name="memNameId" id="memNameId">
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['memNameId','memName']);">选择</a>
        <a href="javascript:;" class="orgClear" onClick="$('memNameId').value='';$('memName').value='';">清空 </a>
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
      <textarea cols=20 name="memPositionDesc" id="memPositionDesc" rows=1 class="BigStatic" wrap="yes" readonly></textarea>
      <INPUT type="hidden" name="memPosition" id="memPosition" value="">
      <a href="javascript:;" class="orgAdd" onClick="selectRole(['memPosition','memPositionDesc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('memPosition').value='';$('memPositionDesc').value='';">清空 </a>
      </td> 
     </tr>
     <tr>
      <td nowrap class="TableContent">民族：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memNation" id="memNation" value="" size="10" maxlength="19">
      </td>
      <td nowrap class="TableContent">籍贯：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memNativePlace" id="memNativePlace" value="" size="10" maxlength="19">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">出生地：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memBirthplace" id="memBirthplace" value="" size="10">
      </td>
      <td nowrap class="TableContent">出生年月： </td>
      <td nowrap class="TableData"> 
        <INPUT type="text" readonly name="memBirth" id="memBirth" class=BigInput size="10" value="">
        <img src="<%=imgPath%>/calendar.gif" align="absMiddle" id="date1" name="date1" border="0" style="cursor:pointer">
      </td>
     </tr>
     <tr>
      <td nowrap class="TableContent" width="90">证件号码：<span style="color:red">*</span></td>
      <td nowrap class="TableData" >
     <input type="text" class="BigInput" name="memIdNum" id="memIdNum" value="" size="20" maxlength="19">
      </td>
      <td nowrap class="TableContent">电话：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memPhone" id="memPhone" value="" size="20" maxlength="19">
      </td>
    </tr>
     <tr>
      <td nowrap class="TableContent">邮箱：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memMail" id="memMail" value="" size="20">
      </td>
      <td nowrap class="TableContent">传真：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memFax" id="memFax" value="" size="20">
      </td> 
     </tr>
     <tr> 
      <td nowrap class="TableContent">地址：</td>
      <td nowrap class="TableData" colspan="3">
        <input type="text" class="BigInput" name="memAddress" id="memAddress" value="" size="30">
      </td> 
     </tr>
     <tr>
      <td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3">
<div><script language=JavaScript>    
         oFCKeditor.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
         oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
         oFCKeditor.Height = "300px";
         oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
         oFCKeditor.ToolbarSet="DiaryBar";
         oFCKeditor.Create();
         </script>
      <input type="hidden" id="PROFSYS_CONTENT" name="PROFSYS_CONTENT">
</div>    
</tr>
       <tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData" colspan="3">
      <input type = "hidden" id="attachmentName" name="attachmentName"></input>
       <input type = "hidden" id="attachmentId" name="attachmentId"></input>
        <span id="showAtt"></span>
      </td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData" colspan="3">
        <input type="hidden" id="moduel" name="moduel" value="profsys">
       <script>ShowAddFile();ShowAddImage();</script>
     <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>  
      </td>
    </tr>
   <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
      <div id="but">
      <input type="button" value="保存" class="BigButton" onclick="javascript:checkForm2();">&nbsp;
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();   parent.window.close()">&nbsp;
      </div>
      </td>
  </tr> 
</table>
  </form>
  <form id="formFile" action="<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHProjectAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
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
<br>
<div id="giftList" style="padding-left: 10px; padding-right: 10px;"></div>
<div id="returnNull"></div>
</body>
</html>