<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String commentId = request.getParameter("commentId");
if(commentId == null){
  commentId = "";
}
String replyId = request.getParameter("replyId");
if(replyId == null){
  replyId = "";
}
String type = request.getParameter("type");
if(type == null){
  type = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
var oFCKeditor = new FCKeditor('DIARY_CONTENT');
var commentId = "<%=commentId%>";
var replyId = "<%=replyId%>";
var type = "<%=type%>";
function doInit(){
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
  diaCommentDetaile(commentId);
  $("usernameSpan").innerHTML = $("userIdDesc").value;
  //alert($("content").value);
  $("contentSpan").innerHTML = $("content").value;
  $('commentId').value = commentId;
  getSysRemind("remindDiv","smsRemind");
  moblieSmsRemind("sms2RemindDiv","sms2Remind");
  if(type == "1"){
     if(replyId){
       diaCommentReplyDetaile(replyId);
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
       FCK.EditingArea.Window.document.body.innerHTML = $('replyComment').value;
     }
  }
}
function doSubmit(){
  var suss = "";
  if(type){
    suss = updateCommentReply("form1");
  }else{
    suss = saveCommentReply("form1");
  }
  if(suss){
    location = contextPath + "/core/funcs/diary/comment/saveOk.jsp?commentId=<%=commentId %>&type=<%=type %>&replyId=<%=replyId %>";
  }
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" HEIGHT="20" width="20" align="absmiddle"><span class="big3"> 回复点评</span>
    </td>
  </tr>
</table>
 
<br>
<table class="TableList" width="90%" align="center">
  <tr class="TableHeader">
    <td nowrap style="text-align:left;">
    <input id="userId" name="userId" type="hidden">
    <input id="userIdDesc" type="hidden">
    <input id="content" type="hidden">
    <span style="float:left;">
    &nbsp;(</span><span id="usernameSpan" style="float:left;"></span><span style="float:left;">)
    &nbsp;点评内容 </span>
    <span id="sendTime" style="float:right;font-size:10px;"></span>
    <span style="float:right;font-size:10px;">点评时间：</span>
    &nbsp;</td>  
  </tr>
  <tr class="TableLine2">
    <td id="contentSpan"></td>
  </tr>
</table>
<br>
<form id="form1" name="form1">
  <table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableData" width="80">回复内容：</td>
      <td class="TableData">
        <div>
        <script language=JavaScript>    
	       oFCKeditor.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
	       oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
	       oFCKeditor.Height = "300px";
	       oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
	       oFCKeditor.ToolbarSet="DiaryBar";
	       oFCKeditor.Create();  
	      </script>  
          <input type="hidden" id="replyComment" name="replyComment" value="" style="display:none" />
        </div>     
     </td>
    </tr>
    <tr >
       <td nowrap class="TableData" width="80">提醒：</td>
       <td class="TableData">
       <div id="remindDiv" style="float:left;">
       <input type="checkbox" name="smsRemind" id="smsRemind" value="1"><label for="smsRemind">使用事务提醒</label>&nbsp;&nbsp;
       </div>
       <div id="sms2RemindDiv" style="float:left;">   
       <input type="checkbox" name="sms2Remind" id="sms2Remind" value="1"><label for="sms2Remind">使用手机短信提醒</label>
       </div>
       </td>
    </tr>    
  </table>
  <br><br><br>
  <center>  
    <input type="hidden" value="" name="commentId" id="commentId">
    <input type="hidden" value="" name="commentReplyId" id="commentReplyId">
    <input type="hidden" value="" name="replyer" id="replyer">
    <input type="hidden" value="" name="replyTime" id="replyTime">
    <input type="button" value="保存" class="SmallButton" onClick="doSubmit();">&nbsp;&nbsp;
    <input type="button" value="关闭" class="SmallButton" onClick="window.close();">
  <center> 
</form>
</body>
</html>