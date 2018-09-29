<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String idstr = request.getParameter("diaId");
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

<title>今日日志</title>
<script type="text/javascript">
var id = <%=idstr%>;
var oFCKeditor = new FCKeditor('DIARY_CONTENT');
function doInit(){
  $("subject").focus();
  //showDate(new Date(), 'diaDate');
  if(id){
    diaDetaile(id,1,'user',true);
  }
  if($('user').value){
    bindDesc([{cntrlId:"user", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  showCalendar('diaDate',false,'dateId');
  if (isTouchDevice) {
    $("contentTextarea").value = $('compressContent').value;
  }
  else {
	  var FCK = FCKeditorAPI.GetInstance('DIARY_CONTENT');
	  if (FCK.EditingArea) {
	    if (fckTimer) {
	      clearTimeout(fckTimer);
	      fckTimer = null;
	    }
	  }else {
	    fckTimer = setTimeout(doInit, 100);
	    return;
	  }
	  var FORM_MODE = FCK.EditingArea.Mode; //获取编辑区域的常量——源文件模式
	  var editingAreaFrame = document.getElementById("DIARY_CONTENT___Frame");
	  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
	  if(FORM_MODE == editModeSourceConst) {
	    FCK.Commands.GetCommand( 'Source' ).Execute();
	  }
	  FCK.EditingArea.Window.document.body.innerHTML = $('compressContent').value;
  }
}
function handleSingleUpload(rtState, rtMsrg, rtData) {
  var data = rtData.evalJSON();
  if(data.type == 0){ 
	  $('attachmentId').value += data.attrId;
	  $('attachmentName').value += data.attrName;
	  showAttach($('attachmentId').value,$('attachmentName').value ,"attr",false);
	  $('attr_tr').style.display = ''; 
	  removeAllFile();
	  if (isUploadBackFun) {
	    doSubmit();
	    isUploadBackFun = false;
	  }
	}else{
      isUploadBackFun = false;
	  alert("选择的附件，超过了规定的大小!");
	}
}

function deleteAttachBackHand(attachName,attachId,attrchIndex){
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
  try{
    updateDiaryByAjax('form1');
    return true;
   }catch(ex){
    return false;
   }
  //alert('删除附件事后工作！>> <附件名：' + attachName +' > <附件Id：' + attachId +' > <附件编号：'+ attrchIndex + '>');
}
/**
 * 提交
 */
function doSubmit(){
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
  if(checkDate("diaDate") == false){
    $("diaDate").focus();
    $("diaDate").select();
    alert("日期格式不对，请输入形：2010-04-09");
    return;
  }
  var isSucss = updateDiaryByAjax('form1');
  if(isSucss){
    alert("保存成功");
    location = contextPath + "/core/funcs/diary/last.jsp";
  }else{
    alert("保存失败,可能网络出现错误,请重试!")
  }
}
function InsertImage(src){ 
  var oEditor = FCKeditorAPI.GetInstance('DIARY_CONTENT') ; 
  if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG ) { 
    oEditor.InsertHtml( "<img src='"+ src + "'/>") ; 
  } 
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="18" HEIGHT="18" align="absmiddle">&nbsp;&nbsp;<span class="big3"> 编辑日志</span>
    </td>
  </tr>
</table>
<br>
<form id="form1">
  <input type="hidden" id="seqId" name="seqId" value=""></input>
  <table class="TableBlock" width="100%" align="center">
    <tr>
      <td nowrap class="TableData" width="80">日志标题：</td>
      <td class="TableData">
        <input type="text" name="subject" id="subject" size=50 class="BigInput" value="cy(OA 管理员)2010-03-04 日志" style="behavior:url(#default#userdata)">
        <a href="javascript:;" class="orgClear" onClick="Clear('subject')">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80">日志类型：</td>
      <td class="TableData">
        <select id="diaType" name="diaType" class="BigSelect">
          <option value="1">工作日志</option>
          <option value="2">个人日志</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">日期：</td>
      <td class="TableData">
        <input type="text" id="diaDate" name="diaDate" size="10" maxlength="10" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" id="dateId">
      </td>
    </tr>
    <tr  id="attr_tr" style="display:none">
      <td nowrap class="TableData">附件：</td>
      <td class="TableData">
      <input type = "hidden" id="attachmentName" name="attachmentName"></input>
       <input type = "hidden" id="attachmentId" name="attachmentId"></input>
        <span id="attr"></span>
      </td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData">
       <script>ShowAddFile();ShowAddImage();</script>
	   <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
	   <input type="hidden" id="ATTACHMENT_ID_OLD" name="ATTACHMENT_ID_OLD" value="">
	   <input type="hidden" id="ATTACHMENT_NAME_OLD" name="ATTACHMENT_NAME_OLD" value="">	   
      </td>
    </tr>
    <tr class="TableControl">
      <td class="TableData" colspan="2" id="contentTd">
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
	      <input type="hidden" id="compressContent" name="compressContent" value="" />
      </div>      
      </td>
    </tr>
    <tr>
      <td class="TableHeader" colspan="2"><div onclick="showCntrl('share')" title="点击展开/收缩选项" style="cursor:pointer;width:120px;"><img src="<%=imgPath%>/green_arrow.gif" align="absMiddle"> 指定共享范围</div></td>
    </tr>       
    <tr id="share" style="display:none">
      <td nowrap class="TableData">共享范围：</td>
      <td class="TableData">
      <input type="hidden" id="moduel" name="moduel" value="diary">
          <input type="hidden" name="toId" id="user" value="" />
          <textarea id="userDesc"  rows="2" cols="40" readOnly ></textarea>
        <a href="#" class="orgAdd" onClick="selectUser();">添加</a>
        <a href="#" class="orgClear" onClick="Clear('user','userDesc')">清空</a>
      </td>
    </tr> 
     
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" name="OP" value="1">
        <input type="hidden" name="FROM" value="">        
        <input type="button" value="保存修改" class="SmallButtonB"  onClick="doSubmit();">&nbsp;&nbsp;
        <input type="button" value="返回" class="SmallButtonA" onClick="location='<%=contextPath %>/core/funcs/diary/last.jsp'">
      </td>
    </tr>
  </table>
</form>
<form name="formFile" id="formFile" action="<%=contextPath %>/yh/core/funcs/diary/act/YHDiaryAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame"></form>
<iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</body>
</html>