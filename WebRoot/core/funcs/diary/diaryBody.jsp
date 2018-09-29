<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
 <%
   String dateStr = request.getParameter("DiaDateDiary");
   if(dateStr == null){
     dateStr = "";
   }
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
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
<script type="text/javascript">
var dateStr ="<%= dateStr%>";
var oFCKeditor = new FCKeditor('DIARY_CONTENT');
function UseCheckForm(){
 // alert("UseCheckForm");
}
/**
 * 提交
 */
function doSubmit(form){
  if(jugeFile()){//如果有没有上传的文件，则进行上传
    $("formFile").submit();
    isUploadBackFun = true;
    return ;
  }
  if(!($("subject").value).trim()){
    $("subject").focus();
    alert("日志标题不能为空！")
    return;
   }
  var succes = saveDiaryByAjax(form);
  if(succes){
    location.reload();
  }
}
function doInit(){
  if(dateStr){
    showDate(dateStr, 'diaDate');
  }else{
    showDate(new Date(), 'diaDate');
    dateStr =  showDate(new Date());
  }
  $('diaTitle').insert(dateStr + " 日志 ",'content');
  lastEntryByDate('bodyContent',dateStr);
  $('body_top').style.display = ''; 
  $('searchDiv').style.display = '';
  var loginUser = getUserInfo();
  var userInfo = "";
  if(loginUser){
    userInfo = loginUser.userName + " (" + loginUser.privName + ") ";
  }
  $('form1').subject.value =  userInfo + showDate(dateStr) + " 日志";
}
/**
 * 附件上传回调函数
 */
function handleSingleUpload(rtState, rtMsrg, rtData) {
  
  var data = rtData.evalJSON(); 
  if(data.type == 0){ 
	  $('attachmentId').value += data.attrId;
	  $('attachmentName').value += data.attrName;
	  showAttach($('attachmentId').value,$('attachmentName').value ,"attr",false,"dia");
	  $('attr_tr').style.display = ''; 
	  removeAllFile();
	  if (isUploadBackFun) {
	    doSubmit('form1');
	    isUploadBackFun = false;
	  }
  }else{
    isUploadBackFun = false;
    alert("选择的附件，超过了规定的大小!");
  }
}
function deleteAttachBackHand(attachName,attachId,attrchIndex){
  try{
    var attachNameOld = $('attachmentName').value;
    var attachIdOld =  $('attachmentId').value;
    var attachNameArrays = attachNameOld.split("*");
    var attachIdArrays = attachIdOld.split(",");
    var attaName = "";
    var attaId = "";
    for(var i = 0 ; i < attachNameArrays.length ; i++){
      if(!attachIdArrays[i] || attachIdArrays[i] == attachId){
        continue;
      }
      attaName += attachNameArrays[i] + "*";
      attaId += attachIdArrays[i] + ",";
    }
    $('attachmentId').value = attaId;
    $('attachmentName').value = attaName;
    return true;
  }catch(ex){
    return false;
  }
  //alert('删除附件事后工作！>> <附件名：' + attachName +' > <附件Id：' + attachId +' > <附件编号：'+ attrchIndex + '>');
}

function InsertImage(src){ 
  var oEditor = FCKeditorAPI.GetInstance('DIARY_CONTENT') ; 
  if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG ) { 
    oEditor.InsertHtml( "<img src='"+ src + "'/>") ; 
  } 
}
</script>
<title>工作日志</title>
</head>
<body onload="doInit();">
<div id="body_top" style="display:none">
<table border="0" width="100%" cellspacing="0" cellpadding="3" margin-top="3" class="small">
  <tr>
    <td class="Big">&nbsp;<img src="<%=imgPath%>/diary.gif" WIDTH="18" HEIGHT="18" align="absmiddle"><span id="diaTitle" class="big3"> </span>
    </td>
  </tr>
</table>
</div>
<div id="bodyContent"></div>
<div id="searchDiv" style="display:none">
<br>
<form id="form1">
<table class="TableBlock" width="97%" align="center">
  <tr>
    <td class="TableHeader"colspan="4"> 快速新建日志</td>
  </tr>
  <tr>
    <td class="TableData" nowrap>日志标题：</td>
    <td class="TableData" colspan="3">
      <input type="text" id="subject" name="subject" size="47" class="BigInput" value="">
      <input type="button" class="SmallButton" value="清空" onclick="Clear('subject')">
    </td>
  </tr>
  <tr>
    <td class="TableData">日志类型：</td>
    <td class="TableData">
      <select name="diaType" class="BigSelect">
        <option value=1>工作日志</option>
        <option value=2>个人日志</option>
      </select></td>
  <td nowrap class="TableData">日期：</td>
      <td class="TableData">
        <input type="text" id="diaDate" name="diaDate" size="10" maxlength="10" class="BigStatic" value="" readonly>
      </td>
  </tr>
  <tr id="attr_tr" style="display:none">
      <td nowrap class="TableData">附件：</td>
      <td class="TableData" colspan="3">
      <input type = "hidden" id="attachmentName" name="attachmentName"></input>
       <input type = "hidden" id="attachmentId" name="attachmentId"></input>
        <div id="attr"></div>
      </td>
    </tr> 
  <tr>
    <td class="TableData">附件选择：</td>
    <td class="TableData" colspan="3">
       <script>ShowAddFile();ShowAddImage();</script>
	   <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
       <input type="hidden" name="ATTACHMENT_ID_OLD" value="">
	   <input type="hidden" name="ATTACHMENT_NAME_OLD" value="">	
    </td>
  </tr>
  <tr>
    <td class="TableData" colspan="4" id="contentTd">
      <div>
          <script language=JavaScript>    
          if (isTouchDevice) {
            $("contentTd").insert("<textarea id=\"contentTextarea\" cols=\"80\" rows=\"10\"></textarea>");
          }
          else {
						oFCKeditor.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
						oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
						oFCKeditor.Height = "300px";
						oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
						oFCKeditor.ToolbarSet="DiaryBar";
						oFCKeditor.Create();  
          }
	      </script>  
	      <input type="hidden" id="content" name="content" value="" />
     </div>
   </td>
  </tr>
  <tr class="TableControl">
    <td colspan="4" align="center">
          <input type="hidden" id="moduel" name="moduel" value="diary">
       <input type="button" value="保存" onclick="doSubmit('form1')" class="SmallButton">
    </td>
  </tr>
  </table>
</form>
</div>
<form name="formFile" id="formFile" action="<%=contextPath %>/yh/core/funcs/diary/act/YHDiaryAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame"></form>
<iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</body>
</html>