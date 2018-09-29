<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String contentIdStr=request.getParameter("contentId");
	if(contentIdStr==null){
	  contentIdStr="";
	}	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>转发对话框</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
//alert('<%=contentIdStr%>');
var requestURL="<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFileContentAct";


function doInit(){
	//moblieSmsRemind("sms2remindDiv");
	getSysRemind("smsRemindDiv","smsRemind",20);
	moblieSmsRemind("sms2RemindDiv","sms2Remind",20);
}

function checkForm(){
  var userIdStr=$("user").value;  
  if (userIdStr=="")   {
  	 alert("请选择接收人！");
    return (false);
  }
 return (true);
}

function sendForm(){
  if(checkForm()){
    var userIdStrs = document.getElementById("user").value;
    var smsResendStr=$("smsRemind"); 
    var mobileResendStr=$("sms2Remind"); 

    var mobileResend="";
    var smsResend="";
    //alert(smsResend);
    if(smsResendStr.checked){
      smsResend="smsResend";
    }
    if(mobileResendStr.checked){
    	mobileResend="smsMobile";
    }
    //alert("smsResend>>"+ smsResend+ "  mobileResend>>"+mobileResend);
    //alert(userIdStrs);
    var url = requestURL + "/resendTo.act?contentIdStr=<%=contentIdStr%>&userIdStrs=" + userIdStrs +"&smsResend=" + smsResend + "&mobileResend=" + mobileResend;
  	var rtJson = getJsonRs(url);
  	//alert("rsText:"+rsText);
	 	if(rtJson.rtState == "0"){
	 	 	var prc = rtJson.rtData;
	 		var sentFlag=prc.sentFlag;
	 		if(sentFlag == 1){
	 		  $("formDiv").style.display="none";		  
	 		  $("returnDiv").show();
	 		  return;
	   	}
	  }
  }
}

//判断是否要显示短信提醒 
function getSysRemind(remidDiv,remind,type){ 
	var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=" + type; 
	var rtJson = getJsonRs(requestUrl); 
	if(rtJson.rtState == "1"){ 
		alert(rtJson.rtMsrg); 
		return ; 
	} 
	var prc = rtJson.rtData; 
	//alert(rsText);
		var allowRemind = prc.allowRemind;;//是否允许显示 
		var defaultRemind = prc.defaultRemind;//是否默认选中 
		var mobileRemind = prc.mobileRemind;//手机默认选中 
	if(allowRemind=='2'){ 
		$(remidDiv).style.display = 'none'; 
	}else{
		$(remidDiv).style.display = ''; 
		if(defaultRemind=='1'){ 
			if(remind){
				$(remind).checked = true; 
			}
		} 
	}
	if(remind){
		if(document.getElementById(remind).checked){
			document.getElementById(remind).value = "1";
		}else{
			document.getElementById(remind).value = "0";
		}
	}
}


/** 
*js代码 
*是否显示手机短信提醒 
*/ 
//判断是否要显示手机短信提醒 
function moblieSmsRemind(remidDiv,remind,type){
	var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=" + type; 
	var rtJson = getJsonRs(requestUrl); 
	if(rtJson.rtState == "1"){ 
		alert(rtJson.rtMsrg); 
		return ; 
	} 
	var prc = rtJson.rtData; 
	//alert(rsText);
	var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
	if(moblieRemindFlag == '2'){ 
		$(remidDiv).style.display = '';
		if(remind){
			$(remind).checked = true;
		}
	}else if(moblieRemindFlag == '1'){ 
		$(remidDiv).style.display = '';
		if(remind){
			$(remind).checked = false;
		}
	}else{
		$(remidDiv).style.display = 'none'; 
	}
	if(remind){
		if(document.getElementById(remind).checked){
			document.getElementById(remind).value = "1";
		}else{
			document.getElementById(remind).value = "0";
		}
	}
}


function toBack(){
	//alert("tt");
	//$("errorDir").hide();
	//$("formDiv").show();
	//$("FILE_NAME").focus();
	//$("FILE_NAME").select();
	location.reload();
}
</script>
</head>
<body onload="doInit();">
<div id="formDiv" style="display: ">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/arrow_right.gif"  width="17" height="17"><span class="big3"> 文件转发</span><br>
    </td>
  </tr>
</table>
<form  method="post" name="form1">
<table class="TableBlock" width="100%" align="center">
  <tr>
    <td nowrap class="TableContent" width="50">转发给：</td>
   	 <td class="TableData">
	      <input type="hidden" name="user" id="user" value="">
	      <textarea cols=40 name="userDesc" id="userDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
	      <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
	      <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
	   </td>   
  </tr>
  <tr>
    <td nowrap class="TableContent">提醒：</td>
    <td class="TableData" colspan="3">
    	<span id="smsRemindDiv" style="display: none;">
				<input type="checkbox" name="smsRemind" id="smsRemind" >
				<label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;
			</span>
			<span id="sms2RemindDiv" style="display: none;">
				<input type="checkbox" name="sms2Remind" id="sms2Remind" >	
				<label for="sms2Remind">使用手机短信提醒</label>&nbsp;&nbsp;  
			</span>
    </td>
  </tr>
</table>
<br>
<center>
<input type="hidden" name="CONTENT_ID1" value="<?=$CONTENT_ID?>">
<input type="button" value="转发" onclick="sendForm();"  class="BigButton" title="转发文件">&nbsp;&nbsp;
<input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
</center>
</form>
</div>

<div id="returnDiv" style="display: none">
<table class="MessageBox" align="center" width="260">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">文件转发成功</div>
    </td>
  </tr>
</table>
<br>
<div align="center"><input type="button" class="BigButton" value="返回" onclick="toBack();"></div>
</div>





</body>
</html>