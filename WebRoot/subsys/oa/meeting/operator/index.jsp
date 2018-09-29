<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>指定会议室管理员</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript">
var fckContentStr = "";
var operator = "";
var roomRule = "";
function doInit(){
   var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingOperatorAct/getMeetingOperator.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     $("coptToId").value = rtJson.rtData.paraValue;
     operator = rtJson.rtData.paraName;
     $("seqIdOperator").value = rtJson.rtData.seqId;

     if($("coptToId") && $("coptToId").value.trim()){
       bindDesc([{cntrlId:"coptToId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
     }
   } else {
     alert(rtJson.rtMsrg); 
   }
   getMeetingRoomRule();
}

function FCKeditor_OnComplete( editorInstance ) {
  editorInstance.SetData( fckContentStr ) ;
}

function getMeetingRoomRule(){
  var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingOperatorAct/getMeetingRoomRule.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    fckContentStr = rtJson.rtData.paraValue;
    roomRule = rtJson.rtData.paraName;
    $("seqIdRule").value = rtJson.rtData.seqId;

    if($("coptToId") && $("coptToId").value.trim()){
      bindDesc([{cntrlId:"coptToId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function doSubmit(){
  var oEditor = FCKeditorAPI.GetInstance('fileFolder');
  var url = null;
  var rtJson = null;
  var seqIdOperator = $("seqIdOperator").value;
  var meetingOperator = $("coptToId").value;
  url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingOperatorAct/";
  if (operator == "MEETING_OPERATOR") {
    url += "updateMeetingOperator.act?meetingOperator="+meetingOperator+"&seqId="+seqIdOperator;
  } else {
    url += "addMeetingOperator.act?meetingOperator="+meetingOperator;
  }
  rtJson = getJsonRs(url, mergeQueryString($("form1")));

  if (rtJson.rtState != "0") {
    alert(rtJson.rtMsrg);
    return;
  }

  var urls = null;
  var rtJson = null;
  var seqIdRule = $("seqIdRule").value;
  var fckContentStr = encodeURIComponent(oEditor.GetXHTML());
  urls = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingOperatorAct/";
  if (roomRule == "MEETING_ROOM_RULE") {
    urls += "updateMeetingRoomRule.act?paraValue="+fckContentStr+"&seqId="+seqIdRule;
  } else {
    urls += "addMeetingRoomRule.act?paraValue="+fckContentStr;
  }
  rtJson = getJsonRs(urls, mergeQueryString($("form1")));
  
  if (rtJson.rtState != "0") {
    alert(rtJson.rtMsrg);
    return;
  }
  //alert(rtJson.rtMsrg);
  location = "<%=contextPath %>/subsys/oa/meeting/operator/submit.jsp";
}

</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 指定管理员及会议室管理制度</span>
    </td>
  </tr>
</table>
<br><br>
<form action="" method="post" name="form1" id="form1">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.subsys.oa.meeting.act.YHMeetingOperatorAct.java"/>
<input type="hidden" name="seqId" id="seqId" value="">
<input type="hidden" name="seqIdOperator" id="seqIdOperator" value="">
<input type="hidden" name="seqIdRule" id="seqIdRule" value="">
<table class="TableBlock" width="70%" align="center">
   <tr bgcolor="#CCCCCC">
    <td nowrap class="TableData" >会议室管理员：</td>
   </tr>
  <tr>
    <td class="TableData">
    <input type="hidden" name="coptToId" id="coptToId" value="">
    <textarea cols=45 name="coptToIdDesc" id="coptToIdDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea>
    <a href="javascript:;" class="orgAdd" onClick="selectUser(['coptToId', 'coptToIdDesc']);">添加</a>
     <a href="javascript:;" class="orgClear" onClick="$('coptToId').value='';$('coptToIdDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData" colspan="2">会议室管理制度：</td>
   </tr>
  <tr id="editor">
    <td class="TableData">
 <div>
     <script language=JavaScript>    
      var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
      var oFCKeditor = new FCKeditor( 'fileFolder' ) ;
      oFCKeditor.BasePath = sBasePath ;
      oFCKeditor.Height = 200;
      var sSkinPath = sBasePath + 'editor/skins/office2003/';
      oFCKeditor.Config['SkinPath'] = sSkinPath ;
      oFCKeditor.Config['PreloadImages'] =
                      sSkinPath + 'images/toolbar.start.gif' + ';' +
                      sSkinPath + 'images/toolbar.end.gif' + ';' +
                      sSkinPath + 'images/toolbar.buttonbg.gif' + ';' +
                      sSkinPath + 'images/toolbar.buttonarrow.gif' ;
      //oFCKeditor.Config['FullPage'] = true ;
       oFCKeditor.ToolbarSet = "fileFolder";
      oFCKeditor.Value = '' ;
      oFCKeditor.Create();
     </script>
  </div>
    </td>
  </tr>
  <tr class="TableControl">
    <td align="center" valign="top" colspan="3">
      <input type="button" class="BigButton" value="保 存" onclick="doSubmit();">&nbsp;&nbsp;
    </td>
  </tr>
</table>
  </form>
</body>
</html>