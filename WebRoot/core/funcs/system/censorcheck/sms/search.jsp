<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%
  ArrayList<YHCensorWords> wordList = (ArrayList<YHCensorWords>)request.getAttribute("wordList");
  //int sumSize = wordList.size();
  Object userName = request.getAttribute("userName");
  String censorFlag = request.getParameter("censorFlag");
  if (censorFlag == null){
    censorFlag = "";
  }
  String checkBegin = request.getParameter("checkBegin");
  if (checkBegin == null){
    checkBegin = "";
  }
  
  String checkEnd = request.getParameter("checkEnd");
  if (checkEnd == null){
    checkEnd = "";
  }
  
  String fromId = request.getParameter("fromId");
  if (fromId == null){
    fromId = "";
  }
  String content = request.getParameter("content");
  content = YHUtility.encodeSpecial(content);
  if (content == null){
    content = "";
  }
  String toId = request.getParameter("toId");
  if (toId == null){
    toId = "";
  }
  String endDate = request.getParameter("endDate");
  if (endDate == null){
    endDate = "";
  }
  String beginDate = request.getParameter("beginDate");
  if (beginDate == null){
    beginDate = "";
  }
  
  String limit = request.getParameter("limit");
  if (limit == null){
    limit = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>短信审核</title>
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
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/censorcheck/js/censorcheckUtil.js"></script>
<script type="text/javascript">
var userName = "<%=userName%>";
var censorFlag = "<%=censorFlag%>";
var checkBegin = "<%=checkBegin%>";
var checkEnd = "<%=checkEnd%>";
var fromId = "<%=fromId%>";
var toIds = "<%=toId%>";
var content = "<%=content%>";
var beginDate = "<%=beginDate%>";
var endDate = "<%=endDate%>";
var limit = "<%=limit%>";
var censorFlaga = censorFlag;

function doInit(){
  var count = 0;
  var formIdName = "";
  var toIdName = "";
  var userSum = "";
  var checkPer = "";
  var moduleCode = 1;
  
  var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getSmsSearchJson.act?censorFlag="+censorFlaga+"&content="+encodeURIComponent(content);
  var rtJson = getJsonRs(url, "fromId="+fromId+"&checkBegin="+checkBegin+"&checkEnd="+checkEnd+"&toId="+toIds+"&beginDate="+beginDate+"&endDate="+endDate+"&moduleCode="+moduleCode+"&limit="+limit);
  if(rtJson.rtState == "0"){
    var table=new Element('table',{ "width":"100%","class":"TableBlock","align":"center"})
		.update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
				+"<td nowrap width='40' align='center'>选择</td>"
				+"<td nowrap width='80' align='center'>发信人</td>"				
				+"<td>收信人</td>"				
				+"<td>内容</td>"				
				+"<td nowrap width='140' align='center'>发送时间</td>"
				+"<td>状态</td>"
				+"<td>审核人</td>"
				+"<td align='center'>审核时间<img border=0 src='<%=imgPath%>/arrow_down.gif' width='11' height='10'> </td></tr><tbody>");
		
	$('listDiv').appendChild(table);
    for(var i = 0; i < rtJson.rtData.length; i++){
      var formId = rtJson.rtData[i].FROM_ID;
  	  var toId = rtJson.rtData[i].TO_ID;
  	  userSum += formId+",";
    }
  	for(var i = 0; i < rtJson.rtData.length; i++){
  	  count++;
  	  document.getElementById("count").innerHTML = count;
      var seqId = rtJson.rtData[i].seqId;
      var censorFlag = rtJson.rtData[i].censorFlag;
      if(censorFlag == 1){
        censorFlag = "<font color=green><b>√</b></font>";
      }else if(censorFlag == 2){
        censorFlag = "<font color=red><b>×</b></font>";
      }else if(censorFlag == 0){
        censorFlag = "待审核";
      }
      var checkUser = rtJson.rtData[i].checkUser;
      if(checkUser == "null" || checkUser == ""){
  	    checkUser = "";
      }
      var checkTime = rtJson.rtData[i].checkTime;
      //var bodyId = rtJson.rtData[i].cont.BODY_ID;
  	  //var subject = rtJson.rtData[i].cont.SUBJECT;
  	  var formId = rtJson.rtData[i].cont.FROM_ID;
  	  var toId = rtJson.rtData[i].cont.TO_ID;
  	  var sendTime = rtJson.rtData[i].cont.SEND_TIME;
  	  var contents = rtJson.rtData[i].cont.CONTENT;
  	  if(checkTime == "null" || checkTime == ""){
  	    checkTime = "";
      }
  	  var urlName = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorChangeName.act?formId="+formId;
      var rtJson2 = getJsonRs(urlName);
  	  for(var x = 0; x < rtJson2.rtData.length; x++){
  	    formIdName = rtJson2.rtData[x].userName;
  	  }
  	  if(checkUser!=""){
  	    var url3 = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorChangeName2.act?formId="+checkUser;
        var rtJson3 = getJsonRs(url3);
  	    for(var z = 0; z < rtJson3.rtData.length; z++){
  	      checkPer = rtJson3.rtData[z].userName;
  	    }
  	  }else{
  	    checkPer = "";
      }
  	  var url1 = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorChangeName.act?formId="+toId;
      var rtJson1 = getJsonRs(url1);
  	  for(var y = 0; y < rtJson1.rtData.length; y++){
  	    toIdName += rtJson1.rtData[y].userName + ",";
  	  }
  	  var tr=new Element('tr',{'class':'TableLine1'});			
			table.firstChild.appendChild(tr);
			tr.update("<td align='center'>"					
			  + "<input type='checkbox'id='deleteFlag' name='deleteFlag' value="+seqId+"></td><td align='center'>"
				+ formIdName + "</td><td align='left'>"					
				+ toIdName + "</td><td align='left'>"					
				+ contents +"</td><td>"					
				+ sendTime + "</td><td align='center'>"		
				+ censorFlag + "</td><td align='center'>"	
				+ checkPer + "</td><td align='center'>"	
				+ checkTime + "</td>"		
			);
			toIdName = "";
  	}
  }else{
  	alert(rtJson.rtMsrg); 
  }
  if(count==0){
    var div = document.getElementById("noCheck");
    div.style.display = "";
    var allTable = document.getElementById("allTable");
    allTable.style.display = "none";
  }
  //var key = "法你好法法法轮功哈哈等等法轮功呵呵法轮功。。。";
  //var num = key.indexOf("法轮功");
  //var sun = key.replace(/法轮功/g, "aa");
}

function checkAll(field) {
  var deleteFlags = document.getElementsByName("deleteFlag");
  for(var i = 0; i < deleteFlags.length; i++) {
    deleteFlags[i].checked = field.checked;
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

function deleteAll(){
  if(!confirmAll()) {
  	return ;
  }  
  var url = "<%=contextPath %>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/deleteAllCensorWords.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    //alert(rtJson.rtMsrg); 
    location = "<%=contextPath %>/core/funcs/system/censorwords/manage/deleteAll.jsp";
  }else {
    alert(rtJson.rtMsrg); 
  } 
}

function checkDeny(){
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("请至少选择其中一个。");
    return;
  }
  if(!confirmDeny()) {
  	return ;
  }  
  var deleteAllFlags = document.getElementsByName("deleteFlag");
  var censorID = "";
  var count = 0;
  //var idStrs = "";
  //for(var i = 0; i < deleteAllFlags.length; i++) {
	//if(deleteAllFlags[i].checked) {
   //   idStrs += deleteAllFlags[i].value + "," ;	
	//}	  
  //}  
  //var sumStrs = idStrs.substr(0,idStrs.length-1);
  var urlCheck = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getcheckDenyCensor.act";
  var seqIdStr = idStrs.split(',');
  for(var i = 0; i < seqIdStr.length; i++){
    var seqID = seqIdStr[i];
    var rtJsonDeny = getJsonRs(urlCheck, "idStrs=" + seqID);
    var censorFlag = "";
    if (rtJsonDeny.rtState == "0") {
     for (var x = 0; x < rtJsonDeny.rtData.length; x++) {
       censorFlag = rtJsonDeny.rtData[x].censorFlag;
       censorID = rtJsonDeny.rtData[x].seqId;
       if (censorFlag == "1" || censorFlag == "2") { //不可以拒绝（已经通过或者拒绝了就不能再拒绝）
         location = "<%=contextPath %>/core/funcs/system/censorcheck/email/manage.jsp?count="+count;
         //window.location.reload();
       } else {                                      //可以拒绝
         var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/checkDenyCensor.act";
         var rtJson = getJsonRs(url, "sumStrs=" + censorID);
         if (rtJson.rtState == "0") {
           count++;
           location = "<%=contextPath %>/core/funcs/system/censorcheck/email/manage.jsp?count="+count;
         }else {
           alert(rtJson.rtMsrg); 
         }
       }
     }
    } else {
      alert(rtJson.rtMsrg); 
    } 
  }
}

function checkPass(){
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("请至少选择其中一个。");
    return;
  }
  if(!confirmPass()) {
  	return ;
  }  
  //var deleteAllFlags = document.getElementsByName("deleteFlag");
  //var idStrs = "";
  var count = 0;
  //for(var i = 0; i < deleteAllFlags.length; i++) {
	//if(deleteAllFlags[i].checked) {
   //   idStrs += deleteAllFlags[i].value + "," ;	
	//}	  
  //}
 
  var urlPass = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getcheckDenyCensor.act";
  var seqIdStr = idStrs.split(',');
  for(var i = 0; i < seqIdStr.length; i++){
    var seqID = seqIdStr[i];
    var rtJsonDeny = getJsonRs(urlPass, "idStrs=" + seqID);
    var censorFlag = "";
    if (rtJsonDeny.rtState == "0") {
      for(var x = 0; x < rtJsonDeny.rtData.length; x++){
        censorFlag = rtJsonDeny.rtData[x].censorFlag;
        censorID = rtJsonDeny.rtData[x].seqId;
        if(censorFlag == "1"){
          location = "<%=contextPath %>/core/funcs/system/censorcheck/email/manage.jsp?count="+count;
        }else{
          var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/checkPassCensor.act";
          var rtJson = getJsonRs(url, "sumStrs=" + censorID);
          if (rtJson.rtState == "0") {
            count++;
            location = "<%=contextPath %>/core/funcs/system/censorcheck/email/manage.jsp?count="+count;
          }else {
            alert(rtJson.rtMsrg); 
          }
        }
      }
    }else {
      alert(rtJson.rtMsrg); 
    } 
  }
  smsPass(idStrs);
}

function deleteAllUser() {
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("请至少选择其中一个。");
    return;
  }
  if(!confirmDel()) {
  	return ;
  }  
  //var deleteAllFlags = document.getElementsByName("deleteFlag");
  //var idStrs = "";
  //for(var i = 0; i < deleteAllFlags.length; i++) {
	//if(deleteAllFlags[i].checked) {
   //   idStrs += deleteAllFlags[i].value + "," ;	
	//}	  
  //}
  
  //var sumStrs = idStrs.substr(0,idStrs.length-1);
  var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/deleteCensor.act";
  var rtJson = getJsonRs(url, "sumStrs=" + idStrs);
    if (rtJson.rtState == "0") {
     window.location.reload();
    }else {
      alert(rtJson.rtMsrg); 

    }
}

function SelectUser(userId,domId){ 
  URL = "/yh/core/funcs/dept/userselect.jsp?TO_ID=" + userId + "&TO_NAME=" + domId; 
  openDialog(URL,'400', '350'); 
}

function ClearUser(){ 
  var args = $A(arguments); 
  for(var i = 0; i < args.length; i++ ){ 
    var cntrl = $(args[i]); 
    if(cntrl){ 
      if (cntrl.tagName.toLowerCase() == "td" 
        || cntrl.tagName.toLowerCase() == "div" 
        || cntrl.tagName.toLowerCase() == "span") { 
        cntrl.innerHTML = ''; 
      } else{ 
        cntrl.value =''; 
      } 
    } 
  } 
}
//* @$message：要检查的文本
//* @module：模块编号：0-邮件；1-短信；2-手机短信
// flag 0-标题； 1-内容
function censor(message, module, flag){
  if(flag == "0"){//标题
    var subject = censorSubject(message, module, flag);
    //alert(subject);
  }else{//内容
    var content = censorContent(message, module, flag);
    alert(content+"==============");
  }
}

function censorContent(message, moduleCode, flag){
  var moduleCode = "0";
  var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorWords.act";
  var rtJson = getJsonRs(url);
  var sun  = "";
  var message = "ddeee法轮功你好ff你好哈哈ssff哈哈";
  if (rtJson.rtState == "0") {
	for (var i = 0; i < rtJson.rtData.length; i++) {
	  var find = rtJson.rtData[i].find;
	  var replacement = rtJson.rtData[i].replacement;
      if (message.indexOf(find)!="-1"&&replacement=="{BANNED}") {
        var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorBanned.act?moduleCode="+moduleCode;
        location = url;
        return "BANNED";
      } else if (message.indexOf(find)!="-1"&&replacement=="{MOD}") {
        var moduleCode = "0";
        var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorMod.act?moduleCode="+moduleCode;
        location = url;
        return "MOD";
      } else {
        if(message.indexOf(find)!="-1"){
          var re = eval("/"+find+"/g");
          sun = message.replace(re, replacement);
          message = sun;
        }
      }
	}
	return message;
  } else {
	alert(rtJson.rtMsrg); 
  }
}

function censorSubject(message, moduleCode, flag){
  var moduleCode = "0";
  var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorWords.act";
  var rtJson = getJsonRs(url);
  var sun  = "";
  var message = "ddeee你好ff你好哈哈ssff哈哈";
  if (rtJson.rtState == "0") {
	for (var i = 0; i < rtJson.rtData.length; i++) {
	  var find = rtJson.rtData[i].find;
	  //alert(find);
	  var replacement = rtJson.rtData[i].replacement;
      if (message.indexOf(find)!="-1"&&replacement=="{BANNED}") {
        var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorBanned.act?moduleCode="+moduleCode;
        location = url;
        return "BANNED";
      } else if (message.indexOf(find)!="-1"&&replacement=="{MOD}") {
        var moduleCode = "0";
        var url = "<%=contextPath%>/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorMod.act?moduleCode="+moduleCode;
        location = url;
        return "MOD";
      } else {
        if (message.indexOf(find)!="-1") {
          var re = eval("/"+find+"/g");
          sun = message.replace(re, replacement);
          message = sun;
        }
      }
	}
	return message;
  } else {
	alert(rtJson.rtMsrg); 
  }
}


</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3">&nbsp;审核短信查询（最多显示100条记录）</span>
    </td>
  </tr>
</table>

<div id="noCheck" style="display:none;">
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无需要审核的邮件</div>
    </td>
  </tr>
</table>
</div>

<div id="allTable" style="display:'';">
<div id="listDiv" align="center"></div>
 <table id="beSortTable" class="TableList" width="100%">
   <tr class='TableControl'>
     <td colspan='10'>
       &nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
       <input type='button' value='通过' class='SmallButtonW' onClick='checkPass();' title='通过'>&nbsp;&nbsp;
       <input type='button' value='拒绝' class='SmallButtonW' onClick='checkDeny();' title='拒绝'>&nbsp;&nbsp;
       <input type='button' value='删除' class='SmallButtonW' onClick='deleteAllUser();' title='删除邮件'>
     </td>
   </tr>
</table>
<center>

<table class="MessageBox" align="center" width="240">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">共<span id="count"></span>条记录</div>
    </td>
  </tr>
</table>
</div>
<input type="button" value="返回" class="BigButton" onclick="location='<%=contextPath%>/core/funcs/system/censorcheck/sms/index.jsp'">
</center>
</body>
</html>