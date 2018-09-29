<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId == null) {
    seqId = "";
  }
  
  String fromId = request.getParameter("fromId");
  if(fromId == null) {
    fromId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发微讯</title>

<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript">
var seqId = "<%=seqId%>";
var fromId = "<%=fromId%>";
var oFCKeditor = new FCKeditor('sms_content');
function doInit() {
  if(document.getElementById('sendTime')){
    var parameters = { 
        inputId:'sendTime', 
        property:{isHaveTime:true} 
    }; 
    new Calendar(parameters);
   }  
    if(seqId) {
     var url = "<%=contextPath %>/yh/core/funcs/message/act/YHMessageAct/getMessageBodyContent.act";
     var rtJson = getJsonRs(url, "seqId=" + seqId);
    if (rtJson.rtState == "0") {
      document.getElementById("content").value = rtJson.rtData.content;
      
      FCKSetContent(rtJson.rtData.content) ;
      
    }else {
     alert(rtJson.rtMsrg); 
    }
  } 

  if(fromId) {
    document.getElementById("toId").value = fromId;
    bindDesc([{cntrlId:"toId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  
  
  
  
  
  jQuery(document).keypress(function(e){
   　 var currKey = e.keyCode||e.which||e.charCode;
    if(currKey==10){
      commitSms();
    }
    
  });
  
  
  
  
}

  function check() {
    var cntrl = document.getElementById("toIdDesc");
    if (!cntrl.value) {
  	  alert("收信人不能为空！");
  	  return false;
    }
    var cntrld = document.getElementById("content"); 
   // alert(cntrld.value == "");
    if ((cntrld.value).trim() == "") {
     
  	  alert("内容不能为空！");
  	  //cntrld.focus();
  	  return false;
    }
    return true;
  }
  
  function commitSms() {
    var FCK = FCKeditorAPI.GetInstance('sms_content'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行
    // by dq 090521
    var FORM_MODE = FCK.EditingArea.Mode;

    // 获取编辑区域的常量——源文件模式
    var editingAreaFrame = document.getElementById('sms_content___Frame');
    var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
    if(FORM_MODE == editModeSourceConst)
    {
    FCK.Commands.GetCommand( 'Source' ).Execute();
    } 
    // $("formName").value = formName;
    var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
   // $('content').value = FORM_HTML.replace(/[\n\r\f]/g,"<br>");
    var contentHtml = FCK.EditingArea.Window.document.body.innerText || FCK.EditingArea.Window.document.body.textContent;
   // var contentStr=  $('content').value;
   // $('content').value="<font size=\"+1\" color=\"#FF0000\">群发：</font>"+contentStr;
    
    FORM_HTML="<font size=\"+1\" color=\"#FF0000\">群发：</font>"+FORM_HTML;
    $('content').value = FORM_HTML.replace("<p>","").replace("</p>","").replace(/[\n\r\f]/g,"<br>");
  
    if(!check()){
      return;
    }
	var url = "<%=contextPath%>/yh/core/funcs/message/act/YHMessageAct/addMessageBody.act";
	
    var rtJson = getJsonRs(url, mergeQueryString($("smsBodyInfoForm")));
    if (rtJson.rtState == "0") {
      var toId = $("toId").value;
      var url = "<%=contextPath %>/core/funcs/message/submit2.jsp?toId="+toId;
      location = url;
    }
  }

  
  function FCKSetContent(content) {
	    var FCK = FCKeditorAPI.GetInstance('sms_content'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行

	    // by dq 090521
	    var FORM_MODE = FCK.EditingArea.Mode;

	    // 获取编辑区域的常量——源文件模式
	    var editingAreaFrame = document.getElementById('sms_content___Frame');
	    var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
	    if(FORM_MODE == editModeSourceConst)
	    {
	    FCK.Commands.GetCommand( 'Source' ).Execute();
	    } 
	    FCK.EditingArea.Window.document.body.innerHTML = rtData.content;

	  }

  
  
  function SelectUser(TO_ID, TO_NAME){
    selectUser([TO_ID, TO_NAME]);
  }

  function clearUser() {
    document.getElementById("toIdDesc").value = "";
    document.getElementById("toId").value = "";
  }

  function close_window() {
    try {
	    if (typeof (window.external.OA_SMS) == 'undefined'
	        || !window.external.OA_SMS("", "", "GET_VERSION")
	        || window.external.OA_SMS("", "", "GET_VERSION") < '20110223') {
	      parent.close();
	    } else {
	      window.external.OA_SMS("", "", "CLOSE_WINDOW")
	    }
    } catch(e) {
      parent.close();
    } finally {
      
    }
  }
  



</script>
</head>
<body  onload="doInit()">
<br>
<form id="smsBodyInfoForm" name="smsBodyInfoForm" method="post">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.message.data.YHMessageBody"/>
  <input id="smsType" name="smsType" value="0" type="hidden"></input>
  <table class="TableBlock" width="90%" height="240" align="center">
    <tr>
      <td class="TableData" nowrap>
        <input type="hidden" id="toId" name="user" value="">
        <textarea cols="30" rows="2" id="toIdDesc" name="userDesc" class="BigStatic" readonly></textarea>
        <a href="javascript:;" class="orgAdd"  onClick="SelectUser('toId','toIdDesc')">添加</a>
        <a href="javascript:;" class="orgClear" onClick="clearUser()">清除</a>
      </td>
    </tr>
    <tr>
      <td class="TableData">
       <script language=JavaScript>    
       oFCKeditor.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/sms/js/fckconfig.js"; 
       oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
       oFCKeditor.Height = "180px";
       oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/default/' ; 
       oFCKeditor.ToolbarSet="EmailBar";
       //  alert(loadDataDom);   
       oFCKeditor.Create();  
      </script>  
       <input type="hidden" name="content" id="content" value="" />
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2">
      <span style="float:left;">按Ctrl+Enter发送</span>
      <input type="hidden" name="I_VER" value="">
        <input type="button" value="发送" class="BigButton" onclick="commitSms()">
        <!--  <input type="button" value="消息记录" class="BigButton" onClick="show_msg()">&nbsp;&nbsp;-->
        <input type="button" value="关闭" class="BigButton" onclick="close_window()">
      </td>
    </tr>
  </table>
</form>
</body>
</html>