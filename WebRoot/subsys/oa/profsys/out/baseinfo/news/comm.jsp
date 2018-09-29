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
var projId = parent.seqId;
var projSeqId = 0;
var oFCKNote = new FCKeditor('PROFSYS_NOTE');
var FCKContent = new FCKeditor('PROFSYS_CONTENT');
var upload_limit=1,limit_type="php,php3,php4,php5,";
function InsertImage(src) { 
  var oEditorNote = FCKeditorAPI.GetInstance('PROFSYS_NOTE') ; //FCK实例 
  if (oEditorNote.EditMode == FCK_EDITMODE_WYSIWYG ) { 
    oEditorNote.InsertHtml( "<img src = '"+ src + "'/>") ; 
  }
  var oEditorContent = FCKeditorAPI.GetInstance('PROFSYS_CONTENT') ; //FCK实例 
  if (oEditorContent.EditMode == FCK_EDITMODE_WYSIWYG ) { 
    oEditorContent.InsertHtml( "<img src = '"+ src + "'/>") ; 
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
      inputId:'commTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
}
//表单验证
function checkForm() {
   if ($("commNum").value == "") {
     alert("纪要编号必填!");
     $("commNum").focus();
     $("commNum").select();
     return false;
  }
   if ($("commMemCn").value == "") {
    alert("中方人员必填!");
    $("commMemCn").focus();
    $("commMemCn").select();
    return false;
   }
   if ($("commMemFn").value == "") {
     alert("外方人员必填!");
     $("commMemFn").focus();
     $("commMemFn").select();
     return false;
   }
   if ($("commTime").value == "") {
     alert("时间不能为空!");
     $("commTime").focus();
     $("commTime").select();
     return false;
   }
   if ($("commPlace").value == "") {
     alert("地点必填!");
     $("commPlace").focus();
     $("commPlace").select();
     return false;
   }
   return true;
}
//添加会议纪要
var isUploadBackFun = false;
function checkForm2() {
  var FCK = FCKeditorAPI.GetInstance('PROFSYS_NOTE'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行
  var FORM_MODE = FCK.EditingArea.Mode;
  // 获取编辑区域的常量——源文件模式
  var editingAreaFrame = document.getElementById('PROFSYS_NOTE___Frame');
  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
  if(FORM_MODE == editModeSourceConst) {
    FCK.Commands.GetCommand('Source').Execute();
  } 
  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
  document.getElementById("commNote").value = FORM_HTML;
  
  var FCK2 = FCKeditorAPI.GetInstance('PROFSYS_CONTENT');
  var FORM_MODE2 = FCK2.EditingArea.Mode;
  var editingAreaFrame2 = document.getElementById('PROFSYS_CONTENT___Frame');
  var editModeSourceConst2 = editingAreaFrame2.contentWindow.FCK_EDITMODE_SOURCE;
  if(FORM_MODE2 == editModeSourceConst2) {
    FCK2.Commands.GetCommand('Source').Execute();
  } 
  var FORM_HTML2 = FCK2.EditingArea.Window.document.body.innerHTML;
  document.getElementById("commContent").value = FORM_HTML2;

  if (checkForm()) {
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
      return ;
    }
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectCommAct/addProjectComm.act";
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
//修改会议纪要
var isUploadBackFun = false;
function checkForm3() {
  var FCK = FCKeditorAPI.GetInstance('PROFSYS_NOTE'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行
  var FORM_MODE = FCK.EditingArea.Mode;
  // 获取编辑区域的常量——源文件模式
  var editingAreaFrame = document.getElementById('PROFSYS_NOTE___Frame');
  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
  if(FORM_MODE == editModeSourceConst) {
    FCK.Commands.GetCommand('Source').Execute();
  } 
  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
  document.getElementById("commNote").value = FORM_HTML;
  
  var FCK2 = FCKeditorAPI.GetInstance('PROFSYS_CONTENT');
  var FORM_MODE2 = FCK2.EditingArea.Mode;
  var editingAreaFrame2 = document.getElementById('PROFSYS_CONTENT___Frame');
  var editModeSourceConst2 = editingAreaFrame2.contentWindow.FCK_EDITMODE_SOURCE;
  if(FORM_MODE2 == editModeSourceConst2) {
    FCK2.Commands.GetCommand('Source').Execute();
  } 
  var FORM_HTML2 = FCK2.EditingArea.Window.document.body.innerHTML;
  document.getElementById("commContent").value = FORM_HTML2;
  
  if (checkForm()) {
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
      return ;
    }
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectCommAct/updateProjectComm.act";
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
  if ($("qRen").value > 0) {
    var FCK = FCKeditorAPI.GetInstance('PROFSYS_NOTE'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行
    var FORM_MODE = FCK.EditingArea.Mode;
    // 获取编辑区域的常量——源文件模式
    var editingAreaFrame = document.getElementById('PROFSYS_NOTE___Frame');
    var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
    if(FORM_MODE == editModeSourceConst) {
      FCK.Commands.GetCommand('Source').Execute();
    } 
    var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
    document.getElementById("commNote").value = FORM_HTML;
    
    var FCK2 = FCKeditorAPI.GetInstance('PROFSYS_CONTENT');
    var FORM_MODE2 = FCK2.EditingArea.Mode;
    var editingAreaFrame2 = document.getElementById('PROFSYS_CONTENT___Frame');
    var editModeSourceConst2 = editingAreaFrame2.contentWindow.FCK_EDITMODE_SOURCE;
    if(FORM_MODE2 == editModeSourceConst2) {
      FCK2.Commands.GetCommand('Source').Execute();
    } 
    var FORM_HTML2 = FCK2.EditingArea.Window.document.body.innerHTML;
    document.getElementById("commContent").value = FORM_HTML2;
    
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectCommAct/updateProjectComm.act";
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

//查询是否有会议纪要
var pageMgr = null;
var cfgs = null;
function selectInMem(){
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectCommAct/queryOutCommByProjId.act?projId=" + projSeqId;
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
  return "<a href='#' onclick='getCommById(" + seqId + ");'>编辑</a> "
         +"<a href='javascript:deleteCommById(" + seqId + ");'> 删除 </a> ";
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
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectCommAct/getCommById.act?seqId=" + seqId; 
    var json = getJsonRs(requestURL);
    if (json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ;
    }
    var prc = json.rtData; 
    if (prc.seqId) {
      var seqId = prc.seqId;
      $("commNum").value = prc.commNum;
      $("commName").value = prc.commName;
      $("commMemCn").value = prc.commMemCn;
      $("commMemFn").value = prc.commMemFn;
      $("commTime").value = prc.commTime.substr(0,10);
      $("commPlace").value = prc.commPlace;
      var oFCKContent = FCKeditorAPI.GetInstance('PROFSYS_CONTENT') ;
      oFCKContent.SetData(prc.commContent);
      var oFCKNote = FCKeditorAPI.GetInstance('PROFSYS_NOTE') ;
      oFCKNote.SetData(prc.commNote);
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
<form id="form1" name="form1">
<div id="seqIdDiv"></div>
<input type="hidden" name="dtoClass" value="yh.subsys.oa.profsys.data.YHProjectComm">
<input type="hidden" name="projCommType" id="projCommType" value="1">
<input type="hidden" name="projId" id="projId" value="">
<input type="hidden" name="commContent" id="commContent" value="">
<input type="hidden" name="commNote" id="commNote" value="">
<input type="hidden" value="<%=user.getSeqId() %>" name="projCreator" id="projCreator">
<input type="hidden" value="<%=sf.format(new Date())%>" name="projDate" id="projDate">
<table class="TableBlock" border="0" width="70%" align="center">
    <tr>
      <td nowrap class="TableContent">纪要编号：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="commNum" id="commNum" value="" size="10" maxlength="19">
      </td>  
    <td nowrap class="TableContent">纪要名称：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="commName" id="commName" value="" size="10" maxlength="49">
      </td> 
    </tr>
    <tr>
      <td nowrap class="TableContent">中方人员：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="commMemCn" id="commMemCn" value="" size="10" maxlength="19">
      </td>  
    <td nowrap class="TableContent">外方人员：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="commMemFn" id="commMemFn" value="" size="10" maxlength="19">
      </td> 
    </tr>
      <tr>
      <td nowrap class="TableContent">时间：<span style="color:red">*</span></td>
      <td nowrap class="TableData"> 
        <INPUT type="text" readonly name="commTime" id="commTime" class=BigInput value="" size="10">
        <img src="<%=imgPath%>/calendar.gif" align="absMiddle"  id="date1" name="date1" border="0" style="cursor:pointer">
      </td>
      <td nowrap class="TableContent">地点：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="commPlace" id="commPlace" value="" size=30>
      </td> 
     </tr>
     <tr>
      <td nowrap class="TableContent">内容：</td>
      <td class="TableData" colspan="3">
    <div><script language=JavaScript>    
         FCKContent.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
         FCKContent.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
         FCKContent.Height = "300px";
         FCKContent.SkinPath = FCKContent.BasePath + 'skins/silver/' ; 
         FCKContent.ToolbarSet="DiaryBar";
         FCKContent.Create();
         </script>
      <input type="hidden" id="PROFSYS_CONTENT" name="PROFSYS_CONTENT" value="">
      </div>
</td>
    </tr>
     <tr>
      <td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3">
 <div><script language=JavaScript>
         oFCKNote.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
         oFCKNote.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
         oFCKNote.Height = "300px";
         oFCKNote.SkinPath = oFCKNote.BasePath + 'skins/silver/' ; 
         oFCKNote.ToolbarSet="DiaryBar";
         oFCKNote.Create();
         </script>
      <input type="hidden" id="PROFSYS_NOTE" name="PROFSYS_NOTE" value="">
      </div>
</td>
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
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();parent.window.close()">&nbsp;
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