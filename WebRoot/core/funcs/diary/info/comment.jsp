<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String diaId = request.getParameter("diaId");
  if(diaId == null){
    diaId = "";
  }
  String userId = request.getParameter("userId");
  if(userId == null){
    userId = "";
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
var id = "<%=diaId%>";
function doInit(){
  if (!isTouchDevice) {
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
  }
  
  var url = "<%=contextPath%>/yh/core/funcs/diary/act/YHDiaryAct/getDiaDiaryById.act?diaId=<%=diaId%>";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    $('diaName').innerHTML = " 点评日志  ("+rtJson.rtData.subject+")";
  }
  diaDetaile(id);
  listComment(id,false);
  $('diaId').value = id;
  getSysRemind("smsRemindDiv","smsRemind");
  moblieSmsRemind("sms2RemindDiv","sms2Remind");
}
function doSubmit(){
  var ind = saveComment('form1');
  if(ind){
    location.reload();
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<div id="readbody">
<div>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="18" HEIGHT="18" align="absmiddle"><span class="big3" id="diaName"> </span>
    </td>
  </tr>
</table>
</div>
<table class="TableTop" width="100%">
  <tr>
    <td class="left">
    </td>
    <td class="center">
      日志类型：<span id="diaryType"></span>
    </td>
    <td class="right">
    </td>
  </tr>
</table>
<div class="diary_type">[写日志时间：<span id="diaTime"></span>]</div>
<div class="content" style="overflow-y:auto;overflow-x:auto;width=100%;height=100%"><span id="compressContent"></span></div>
<div class="content" id="comment_<%=diaId %>">
</div>
<form  name="form1" id="form1">
<input type="hidden" id="diaId" name="diaId"></input>
<table class="TableBlock" width="100%" align="center">
  <tr>
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
     </div> 
   </td>
   <tr>
    <td class="TableData" colspan="2">
     <div  id="smsRemindDiv" style="float:left;">  
    <input type="checkbox" name="smsRemind" id="smsRemind"  value="1" ><label for="smsRemind">使用事务提醒</label>&nbsp;&nbsp;
      </div>
      <div id="sms2RemindDiv" style="float:left;">   
        <input type="checkbox" name="sms2Remind" id="sms2Remind" value="1"><label for="sms2Remind">使用手机短信提醒</label>
      </div>
    </td>
    
    </tr>
    <tr align="center" class="TableControl">
      <td nowrap>
        <input type="button" value="保存" class="SmallButton" onClick="doSubmit()">&nbsp;&nbsp;
        <input type="button" value="返回" class="SmallButton" onClick="history.back()">
      </td>
    </tr>
  </table>
</form>
</div>
</body>
</html>