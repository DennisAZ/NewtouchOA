<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建组织信息</title>
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
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
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript">
var oFCKeditor = new FCKeditor('orgActive');
var oFCContact = new FCKeditor('orgContact');
var oFCNote = new FCKeditor('orgNote');

var upload_limit=1,limit_type="php,php3,php4,php5,";
//图片插入到正文function getContent(id,fckId){
  var oEditor = FCKeditorAPI.GetInstance(fckId) ;
  $(id).value=oEditor.GetXHTML();
}
function InsertImage(src) { 
  var oEditor = FCKeditorAPI.GetInstance('orgActive') ; //FCK实例 
  if (oEditor.EditMode == FCK_EDITMODE_WYSIWYG ) { 
    oEditor.InsertHtml( "<img src = '"+ src + "'/>") ; 
  }
  var oFCContact = FCKeditorAPI.GetInstance('orgContact') ; //FCK实例 
  if (oFCContact.EditMode == FCK_EDITMODE_WYSIWYG ) { 
    oFCContact.InsertHtml( "<img src = '"+ src + "'/>") ; 
  }
  var oFCNote = FCKeditorAPI.GetInstance('orgNote') ; //FCK实例 
  if (oFCNote.EditMode == FCK_EDITMODE_WYSIWYG ) { 
    oFCNote.InsertHtml( "<img src = '"+ src + "'/>") ; 
  } 
}
function checkForm() {
  if ($("orgNum").value == "") {
    alert("组织编号必填!");
    $("orgNum").focus();
    $("orgNum").select();
    return false;
  }
  if ($("orgName").value == "") {
    alert("组织名称必填!");
    $("orgName").focus();
    $("orgName").select();
    return false;
  }
  if ($("orgNation").value == "") {
    alert("国别必填!");
    $("orgNation").focus();
    $("orgNation").select();
    return false;
  }
  return true;
}
var isUploadBackFun = false;
function doInit() {
  if (checkForm()) {
    getContent('orgActive','orgActive');
    getContent('orgContact','orgContact');
    getContent('orgNote','orgNote');
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
     return ;
    }
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/source/org/act/YHSourceOrgAct/addUpdateOrg.act"; 
    var json=getJsonRs(requestURL,pars); 
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      var prc = json.rtData;
      parent.projId = prc.seqId;
       alert("保存成功!");
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
    doInit();
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
  return true;
}
function doOnload() {
//时间
  var parameters = {
      inputId:'orgEstablishTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
}
</script>
</head>
<body  topmargin="5px" onLoad="doOnload();">

<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td>
    <img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 新建组织信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form id="form1" method="post" name="form1">
    <input type="hidden" name="dtoClass" value="yh.subsys.oa.profsys.source.org.data.YHSourceOrg">
<table class="TableBlock" width="80%" align="center">
    <tr>
      <td nowrap class="TableContent" >组织编号：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="orgNum" id="orgNum" value="" size=20>
      </td>       
      <td nowrap class="TableContent" >组织名称：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="orgName" id="orgName" value="" size=20>
      </td>       
    </tr>
    <tr>
      <td nowrap class="TableContent">国别：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="orgNation" id="orgNation" value="" size=20>
      </td>
      <td nowrap class="TableContent" >领导人：</td>
      <td nowrap class="TableData">
      <input type="text" name="orgLeader" id="orgLeader" value="" size="10" class="BigInput" >  
    </td> 
    </tr>
    <tr>
      <td nowrap class="TableContent">规模：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="orgScale" id="orgScale" value="" size=20>
      </td>
      <td nowrap class="TableContent">成立时间：</td>
      <td nowrap class="TableData">  
       <INPUT type="text" readonly name="orgEstablishTime" id="orgEstablishTime" class=BigInput size="10" value="">
        <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">
      </td>
    </tr>
    <tr>
     <td nowrap class="TableContent" width="90">发行刊物：</td>
      <td class="TableData" colspan="3">
        <textarea name="orgPublication" id="orgPublication" cols="45" rows="2" class="BigInput"></textarea>
      </td>     
   </tr>
   <tr>
      <td nowrap class="TableContent">主要从事活动：</td>
      <td class="TableData" colspan="3">
      <div><script language=JavaScript>    
         oFCKeditor.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
         oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
         oFCKeditor.Height = "150px";
         oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
         oFCKeditor.ToolbarSet="DiaryBar";
         oFCKeditor.Create();
         </script>
      </div>
        <input type="hidden" id="orgActive" name="orgActive" value=""/>
     </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">交往情况：</td>
      <td class="TableData" colspan="3">
      <div><script language=JavaScript>    
         oFCContact.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
         oFCContact.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
         oFCContact.Height = "150px";
         oFCContact.SkinPath = oFCContact.BasePath + 'skins/silver/' ; 
         oFCContact.ToolbarSet="DiaryBar";
         oFCContact.Create();
         </script>
      </div>
      <input type="hidden" id="orgContact" name="orgContact" value="" style="display:none" />
      </td>
    </tr>   
    <tr>
      <td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3">
      <div><script language=JavaScript>    
         oFCNote.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
         oFCNote.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
         oFCNote.Height = "150px";
         oFCNote.SkinPath = oFCNote.BasePath + 'skins/silver/' ; 
         oFCNote.ToolbarSet="DiaryBar";
         oFCNote.Create();
         </script>
      </div>
      <input type="hidden" id="orgNote" name="orgNote" value="" style="display:none" />
     </td>
    </tr>
      <tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData" colspan="3">
      <input type="hidden" id="attachmentName" name="attachmentName"></input>
       <input type="hidden" id="attachmentId" name="attachmentId"></input>
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
      <td colspan="5" nowrap>
        <input type="button" value="保存" class="BigButton" onclick="doInit();">&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="history.go(-1);">
      </td>
    </tr>
  </table>
</form>
<form id="formFile" action="<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHProjectAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe> 
</body>
</html>