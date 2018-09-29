<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId == null) {
    seqId = "";
  }
  String toId = request.getParameter("toId");
  if(toId == null) {
    toId = "";
  }
  String smsType = request.getParameter("smsType");
  if(smsType == null) {
    smsType = "0";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发微讯</title>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/censorcheck/js/censorcheckUtil.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/message/js/messageutil.js"></script>
<script type="text/javascript">
  var oFCKeditor = new FCKeditor('sms_content');

  var seqId = "<%=seqId%>";
  var toId = "<%=toId%>";
  function doInit() {
    showCalendar("sendTime", true, "startTimeImg");
    if (!isTouchDevice) {
	    var FCK = FCKeditorAPI.GetInstance("sms_content"); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
	    if (FCK.EditingArea) {
	      if (fckTimer) {
	        clearTimeout(fckTimer);
	        fckTimer = null;
	      }
	    }else {
	      fckTimer = setTimeout(doInit, 100);
	      return;
	    }
	    if(seqId) {
	      var url = contextPath + "/yh/core/funcs/message/act/YHMessageAct/getMessageBodyContent.act";
	      var rtJson = getJsonRs(url, "seqId=" + seqId);
	      if (rtJson.rtState == "0") {
	        var content =  rtJson.rtData.content;
	        
	        $("content").value = content;
	        var FORM_MODE = FCK.EditingArea.Mode; //获取编辑区域的常量——源文件模式
	        var editingAreaFrame = document.getElementById("sms_content___Frame");
	        var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
	        if(FORM_MODE == editModeSourceConst) {
	          FCK.Commands.GetCommand( 'Source' ).Execute();
	        }
	        //var sendTime = obj.sendTime;
	        FCK.EditingArea.Window.document.body.innerHTML = content;
	        // = content;
	      }else {
	        alert(rtJson.rtMsrg); 
	      }
	    }
    }
    if(toId) {
     // alert(toId);
      document.getElementById("user").value = toId;
      bindDesc([{cntrlId:"user", dsDef:"PERSON,SEQ_ID,USER_NAME"}]); 
    }
  }

  function check() {
    var cntrl = document.getElementById("user");
    if (!cntrl.value) {
  	  alert("收信人不能为空！");
  	  return false;
    }
    cntrl = document.getElementById("content"); 
    //alert(cntrl.value);
    if ((cntrl.value).trim() == "") {
  	  alert("微讯内容不能为空！");
  	  return false;
    }
    if(checkDateTime("sendTime") == false){
      $("sendTime").focus();
      $("sendTime").select();
      alert("日期格式不对，请输入形：2010-11-11 11:11:11");
      return false;
    }
    return true;
  }
  function checkWords(content){
		return false;
  }
  function commitSms() {
    if (isTouchDevice) {
      $('content').value = $("contentTextarea").value;
    } else {
	    //alert(document.getElementById("toId").value);
	    var FCK = FCKeditorAPI.GetInstance('sms_content'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行	                                                    // by dq 090521
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
	    var contentHtml = FCK.EditingArea.Window.document.body.innerText || FCK.EditingArea.Window.document.body.textContent;
	    $('content').value = FORM_HTML.replace(/[\n\r\f]/g, "");
	  }
		if(!check()){
		  return ;
		}
	    var censorStr1 = censor($('content').value,1,1);
	    if (censorStr1 == "BANNED") {
	      bannedFunc(1);
	      return;
	    }else if (censorStr1 == "MOD") {
	      var censorUrl = contextPath + "/yh/core/funcs/sms/act/YHSmsAct/censor.act?smsType=0";
	      var rtJson = getJsonRs(censorUrl, mergeQueryString($("smsBodyInfoForm")));
	      modFunc(1);
	      return;
	    }
	   if (checkWords($('content').value)) {
	       $('content').value = checkWords($('content').value);
		}
		var url = "<%=contextPath%>/yh/core/funcs/message/act/YHMessageAct/addMessageBody.act?messageType="+<%=smsType%>;
	    var rtJson = getJsonRs(url, mergeQueryString($("smsBodyInfoForm")));
	    if (rtJson.rtState == "0") {
			bindJson2Cntrl(rtJson.rtData);
			var toId = document.getElementById("user").value;
			var url = "<%=contextPath %>/core/funcs/message/submit.jsp?toId="+toId;
			location = url;
    }
  }

  function SelectUser(TO_ID, TO_NAME){
    var URL = "/yh/core/funcs/dept/userselect.jsp?TO_ID=" + TO_ID + "&TO_NAME=" + TO_NAME;
    openDialog(URL,'400', '350');
  }

  function clearUser() {
    $('user').value='';
    $('userDesc').value='';
  }
  
  function checkform(){ //发送即时通讯
	  

	    if(typeof(window.external.OA_SMS) != 'undefined'){  //在精灵中打开
	      var cntrl = document.getElementById("content"); 
	   // alert(cntrl);
	       if ((cntrl.value).trim() == "") {
	         alert("微讯内容不能为空！");
	         return false;
	       }
	       var content = (cntrl.value).trim();

	       cntrl = document.getElementById("user");
	       if (!cntrl.value) {
	         alert("收信人不能为空！");
	         return false;
	       }
	       
	       var idstr=(cntrl.value).split(",");
	   
	    for(var i=0;i<idstr.length;i++){
	    	 var id=idstr[i];
	     	if(id!=""){
	    	 	 window.external.OA_MSG("SEND_MSG","1","2012-05-11 12:23:01","0","wwwwwwwww", "ww");
	      	}
	     }
	    }
	  
  
  }
  
  
</script>
</head>
<body onload="doInit()">
<table>
  <tr>
    <td class="Big">
      <img src="<%=contextPath %>/core/styles/imgs/green_plus.gif" WIDTH="16" HEIGHT="16" align="absmiddle" />
        <span class="big3"> 发微讯</span>
    </td>
  </tr>
</table>
<form id="smsBodyInfoForm" name="smsBodyInfoForm" method="post" >
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.message.data.YHMessageBody"/>
  <table class="TableBlock" width="90%" align="center">
    <tr>
      <td class="TableData" width="100">
        <font style="color:red">*</font>    
                  收信人：
      </td>
      <td class="TableData" nowrap>
         <input type="hidden" name="user" id="user" value="" />
          <textarea name="userDesc" id="userDesc"  rows="2" cols="55" class="BigStatic" readOnly></textarea>
          <a href="javascript:;" class="orgAdd" onClick="selectUser(['user','userDesc'],12);">添加</a>
          <a href="javascript:;"  class="orgClear" onClick="clearUser()">清空</a>
      </td>
    </tr>
    <tr>
      <td class="TableData">
        <font style="color:red">*</font>    
                        微讯内容：       
      </td>
      <td class="TableData" id="contentTd">
       <script language=JavaScript>
       if (isTouchDevice) {
         $("contentTd").insert("<textarea id=\"contentTextarea\" cols=\"40\" rows=\"10\"></textarea>");
       } else {
	       oFCKeditor.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/sms/js/fckconfig.js"; 
	       oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
	       oFCKeditor.Height = "200px";
	       oFCKeditor.Width = "100%";
	       oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/default/' ; 
	       oFCKeditor.ToolbarSet="EmailBar";
	       //  alert(loadDataDom);   
	       oFCKeditor.Create();
       }
      </script>  
       <input type="hidden" name="content" id="content" value="" />
      </td>
    </tr>
    <tr>
      <td class="TableData">
                      发送时间：
      </td>
      <td class="TableData">
       <input type="text" name="sendTime" id="sendTime" size="20" maxlength="19" class="BigInput" value="">
        <img id="startTimeImg" align="absMiddle" src="<%=imgPath%>/cmp/email/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >&nbsp;&nbsp;&nbsp;为空则即时发送 
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2">
        <input type="button" value="发送" class="BigButton" onclick="commitSms();checkform();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>