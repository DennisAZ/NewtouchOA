<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
  String toIdName = request.getParameter("toIdName");
  if (toIdName == null){
    toIdName = "";
  }
  String sendTime = request.getParameter("sendTime");
  if (sendTime == null){
    sendTime = "";
  }
  String bodyId = request.getParameter("bodyId");
  if (bodyId == null){
    bodyId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>读邮件</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/censorcheck/js/censorcheckUtil.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var bodyId = "<%=bodyId%>";
var toIdName = "";
var sendTime = "<%=sendTime%>";
var subject = "";   
var contents = "";
var message = "";
var valueStr = "";
var toIdStr = "";
//var sun = key.replace(/法轮功/g, "aa");
function doInit(){
  emailContent(seqId);
  var urls = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorEmailBody.act";
  var rtJsonEmail = getJsonRs(urls, "bodyId="+bodyId);
  if(rtJsonEmail.rtState == "0"){
    if(rtJsonEmail.rtData != ""){
      var emailShow = $('emailShow');
      emailShow.style.display = ""; 
      var copyToId = rtJsonEmail.rtData[0].copyToId;
      var attachmentId = rtJsonEmail.rtData[0].attachmentId;
      var attachmentName = rtJsonEmail.rtData[0].attachmentName;
      var important = rtJsonEmail.rtData[0].important;
      var toId = rtJsonEmail.rtData[0].toId;
      toIdStr = toId;
      var subject = rtJsonEmail.rtData[0].subject;
      showAttach(attachmentId,attachmentName,"showAtt");
      var importantFlag = "";
      if(important == "0" || important == ""){
        importantFlag = "";
      }else if(important == '1'){
        importantFlag = "&nbsp;&nbsp;<span class=\"TextColor1\">重要</span>";
      }else if(important == '2'){
        importantFlag = "&nbsp;&nbsp;<span class=\"TextColor2\">非常重要</span>";
      }
      document.getElementById("subject").innerHTML = subject+importantFlag;
     // bindJson2Cntrl(rtJsonEmail.rtData.data);
      if(attachmentName){
        //showTr('atttr');
        attachMenuUtil("showAtt","email",null,attachmentName,attachmentId,true);
       }
      
      if(attachmentId != ""){
        var div = document.getElementById("attachment");
        //div.style.display = "";
      }
      valueStr = copyToId;
      if(copyToId != ""){
        var div = document.getElementById("copyTo");
        div.style.display = "";
      }
  //    if(copyToId.length > 80){
 //       var allLink = document.getElementById("allLink");
 //       allLink.style.display = "";
//      }
 //     if(toId.length > 80){
 //       var toNameLink = document.getElementById("toNameLink");
 //       toNameLink.style.display = "";
 //     }
        $("toId").value = toIdName;
        bindDesc([{cntrlId:"toId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
        var toIdDesc = $('toIdDesc').innerHTML;
        if(toIdDesc.indexOf(",") != -1){
          showAllUser('toIdDesc','showAllUser',80);
         //$('showReadStatus').style.display = "";
         }
      //showAllUser('toIdDesc','showAllUser',80);
      var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getUserNameStrs.act";
      var rtJson = getJsonRs(url, "idStrs=" + copyToId);
      if (rtJson.rtState == "0") {
        var userNameStrs = rtJson.rtData[0].userNameStrs;
        document.getElementById("copyToIdDesc").innerHTML = userNameStrs;
      } else {
        alert(rtJson.rtMsrg); 
      }
      //if(copyToId != "" || copyToId != null){
        //bindDesc([{cntrlId:"copyToId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
      //	     ]);
      //}
    }else{
      var emailDel = $('emailDel');
      emailDel.style.display = ""; 
    }
  }else{
  	alert(rtJsonEmail.rtMsrg); 
  }
  
  var moduleCode = "0";
  var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorWords.act";
  var rtJson = getJsonRs(url);
  var replaceStr  = "";
  message = contents;
  if(rtJson.rtState == "0"){
	for(var i = 0; i < rtJson.rtData.length; i++) {
	  var find = rtJson.rtData[i].find;
	  var replacement = rtJson.rtData[i].replacement;
      if(message.indexOf(find) != "-1" && replacement == "{MOD}"){
        var re = eval("/"+find+"/g");
        var colorStr = "<span style='color:#000000;background: #FFFF00;text-decoration: underline;' title='审核词汇'>"+find+"</span> ";
        replaceStr = message.replace(re, colorStr);
        //document.getElementById("contents").innerHTML = replaceStr;
        message = replaceStr;
      }else if(message.indexOf(find) != "-1" && replacement != "{BANNED}" && replacement != "{MOD}"){
        if(message.indexOf(find)!="-1"){
          var re = eval("/"+find+"/g");
          var colorStr = "<span style='color:#0000FF;background: #FFFF00;text-decoration: underline;' title='过滤词汇'>"+replacement+"</span>";
          sun = message.replace(re, colorStr);
          message = sun;
        }
      }
	}
	document.getElementById("contents").innerHTML = message;
  }else{
	alert(rtJson.rtMsrg); 
  }
}

function emailContent(seqId){
  var urls = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorEmailContent.act";
  var rtJson = getJsonRs(urls, "seqId="+seqId);
  if(rtJson.rtState == "0"){
    for(var i = 0; i < rtJson.rtData.length; i++){
      var seqId = rtJson.rtData[i].seqId;
      var subject = rtJson.rtData[i].cont.SUBJECT;
      var bodyId = rtJson.rtData[i].cont.BODY_ID;
      var formId = rtJson.rtData[i].cont.FROM_ID;
      var toId = rtJson.rtData[i].cont.TO_ID;
      toIdName += toId + ",";
      var sendTime = rtJson.rtData[i].cont.SEND_TIME;
      contents = rtJson.rtData[i].cont.CONTENT;
      var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorChangeName.act?formId="+formId;
      var rtJson2 = getJsonRs(url);
      for(var x = 0; x < rtJson2.rtData.length; x++){
        $("formIdName").innerHTML = rtJson2.rtData[x].userName;
      }
      var urlss = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorChangeName.act?formId="+toId;
      var rtJson1 = getJsonRs(urlss);
      for(var y = 0; y < rtJson1.rtData.length; y++){
        //toIdName += rtJson1.rtData[y].userName + ",";
      }
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function confirmDel() {
  if(confirm("确认要删除所选内部邮件吗？")) {
    return true;
  }else {
    return false;
  }
}

function confirmPass() {
  if(confirm("确认要通过所选内部邮件吗？")) {
    return true;
  }else {
    return false;
  }
}

function confirmDeny() {
  if(confirm("确认要拒绝所选内部邮件吗？")) {
    return true;
  }else {
    return false;
  }
}

function checkPass(){
  if(!confirmPass()) {
  	return ;
  }  
  var count = 0;
  var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getcheckDenyCensor.act";
  var rtJson = getJsonRs(url, "idStrs=" + seqId);
  var censorFlag = "";
  if (rtJson.rtState == "0") {
    for(var x = 0; x < rtJson.rtData.length; x++){
      censorFlag = rtJson.rtData[x].censorFlag;
      censorID = rtJson.rtData[x].seqId;
      if (censorFlag == "1") {
        location = "<%=contextPath %>/core/funcs/system/censorcheck/email/manage.jsp?count="+count;
      } else {
        var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/checkPassCensor.act";
        var rtJson = getJsonRs(url, "sumStrs=" + censorID);
        if (rtJson.rtState == "0") {
          count++;
          location = "<%=contextPath %>/core/funcs/system/censorcheck/email/manage.jsp?count="+count;
        } else {
          alert(rtJson.rtMsrg); 
        }
      }
    }
  } else {
    alert(rtJson.rtMsrg); 
  } 

  //var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/sendMailByCoren.act";
  //var rtJson = getJsonRs(url,"bodyId="+bodyId);
  //if(rtJson.rtState == "0"){
    //alert(rtJson.rtMsrg); 
  //}else{
//	alert(rtJson.rtMsrg); 
  //}
  var textFlag = bodyId + ",";
  emailPass(seqId, textFlag); 
}

function checkDeny(){
  if(!confirmDeny()) {
  	return ;
  } 
  var count = 0;
  var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getcheckDenyCensor.act";
  var rtJson = getJsonRs(url, "idStrs=" + seqId);
  var censorFlag = "";
  if (rtJson.rtState == "0") {
    for (var x = 0; x < rtJson.rtData.length; x++) {
      censorFlag = rtJson.rtData[x].censorFlag;
      censorID = rtJson.rtData[x].seqId;
      if (censorFlag == "1" || censorFlag == "2") { //不可以拒绝（已经通过或者拒绝了就不能再拒绝）
        location = "<%=contextPath %>/core/funcs/system/censorcheck/email/manage.jsp?count="+count;
      } else {                                      //可以拒绝
        var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/checkDenyCensor.act";
        var rtJson = getJsonRs(url, "sumStrs=" + censorID);
        if (rtJson.rtState == "0") {
          count++;
          location = "<%=contextPath %>/core/funcs/system/censorcheck/email/manage.jsp?count="+count;
        } else {
          alert(rtJson.rtMsrg); 
        }
      }
    }
   } else {
    alert(rtJson.rtMsrg); 
  } 
}

function checkDelete(){
  if(!confirmDel()) {
  	return ;
  }  
  var count = 0;
  var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/deleteCensor.act";
  var rtJson = getJsonRs(url, "sumStrs=" + seqId);
  if (rtJson.rtState == "0") {
    count++;
    location = "<%=contextPath %>/core/funcs/system/censorcheck/email/manage.jsp?count="+count;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function alertName(){
  var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getUserNameStrs.act";
  var rtJson = getJsonRs(url, "idStrs=" + valueStr);
  if (rtJson.rtState == "0") {
    var userNameStrs = rtJson.rtData[0].userNameStrs;
    alert(userNameStrs);
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function alertToIdName(){
  var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getUserNameStrs.act";
  var rtJson = getJsonRs(url, "idStrs=" + toIdStr);
  if (rtJson.rtState == "0") {
    var userNameStrs = rtJson.rtData[0].userNameStrs;
    alert(userNameStrs);
  } else {
    alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<div id="emailDel" style="display:none;">
<table class="MessageBox" align="center" width="280">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">该邮件已被删除</div>
    </td>
  </tr>
</table>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="window.history.back();">
</div>
</div>

<div id="emailShow" style="display:none;">
 <table class="TableBlock" width="95%" align="center">
    <tr>
      <td class="TableHeader" height=30>
      	<img src="<%=imgPath%>/email_open.gif" WIDTH="22" HEIGHT="20" align="absmiddle">
      	<span id="subject"></span></td>
    </tr>
    <tr>
      <td class="TableData" width="20%">
      	<b>发件人：</b><span id="formIdName"></span><br>
        <b>收件人：</b>
        <input type="hidden" id="toId" name="toId"><span id="toIdDesc"></span>
        <a id="showAllUser" style="display:none">全部名单</a>&nbsp;
        <br>
        <div id="copyTo" style="display: none;position: absolute;">
        <b>抄　送：</b><span id="copyToIdDesc" name="copyToIdDesc"></span>
        &nbsp;&nbsp;<span id="allLink" style="display: none;"><a href="javascript:onClick=alertName();" title="">全部名单</a></span>
        </div>
       <br>
      <b>发送于：</b><%=sendTime%>
      </td>
    </tr>
    <tr class="TableData" height="25" id="atttr" name="atttr">
      <td class="TableContent"> <b>附　件：</b><span id="showAtt" name="showAtt"></span>
      </td>
    </tr>
    <tr class="big">
      <td class="TableData" height="160" valign="top" id="contents"></td>
    </tr>
    <tr id="attachment" style="display:none;">
      <td class="TableData">
        <img src="<%=imgPath%>/image.gif" align="absmiddle" border="0">&nbsp;附件图片: <br><br>
      </td>
    </tr>
  </table>
<br>
<center>
  <input type="button" value="通过" class="BigButton" onClick="checkPass();">&nbsp;&nbsp;
  <input type="button" value="拒绝" class="BigButton" onClick="checkDeny();">&nbsp;&nbsp;
  <input type="button" value="删除" class="BigButton" onClick="checkDelete();">&nbsp;&nbsp;
  <input type="button" value="返回" class="BigButton" onClick="window.history.back();">
</center>
</div>
</body>
</html>