<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String findName = request.getParameter("findName");
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>添加接口用户</title>
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
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var findName = "<%=findName%>";
var seqId = "<%=seqId%>";
var moduleCodeOld = "";
function doInit(){
  if(seqId){
    var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorModuleAct/getCensorModuleId.act?seqId="+seqId;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      bindJson2Cntrl(rtJson.rtData);
      for(var i = 0; i < rtJson.rtData.length; i++){
        document.getElementById("seqId").value = rtJson.rtData[i].seqId;
        var useFlag = rtJson.rtData[i].useFlag;
        var moduleCode = rtJson.rtData[i].moduleCode;
        moduleCodeOld = rtJson.rtData[i].moduleCode;
        if(moduleCode == "0"){
          document.getElementById("moduleCode").value = "0";
        }else if(moduleCode == "1"){
          document.getElementById("moduleCode").value = "1";
        }else if(moduleCode == "2"){
          document.getElementById("moduleCode").value = "2";
        }
        if(useFlag == "1"){
          document.getElementById("useFlag").checked = true;
        }else if(useFlag == "0"){
          document.getElementById("useFlag").checked = false;
        }
        if(rtJson.rtData[i].checkUser == "null" || rtJson.rtData[i].checkUser == ""){
          document.getElementById("checkUserId").value = "";
        }else{
          document.getElementById("checkUserId").value = rtJson.rtData[i].checkUser;
        }
        
        if(rtJson.rtData[i].smsRemind == "1"){
          document.getElementById("smsRemind").checked = true;
        }else{
          document.getElementById("smsRemind").checked = false;
        }
        if(rtJson.rtData[i].sms2Remind == "1"){
          document.getElementById("sms2Remind").checked = true;
        }else{
          document.getElementById("sms2Remind").checked = false;
        }
        if(rtJson.rtData[i].bannedHint == "null" || rtJson.rtData[i].bannedHint == ""){
          document.getElementById("bannedHint").value = "";
        }else{
          document.getElementById("bannedHint").value = rtJson.rtData[i].bannedHint;
        }
        if(rtJson.rtData[i].modHint == "null" || rtJson.rtData[i].modHint == ""){
          document.getElementById("modHint").value = "";
        }else{
          document.getElementById("modHint").value = rtJson.rtData[i].modHint;
        }
        if(rtJson.rtData[i].filterHint == "null" || rtJson.rtData[i].filterHint == ""){
          document.getElementById("filterHint").value = "";
        }else{
          document.getElementById("filterHint").value = rtJson.rtData[i].filterHint;
        }
      }
      if(document.getElementById("checkUserId").value != ""){
        bindDesc([{cntrlId:"checkUserId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
      	        ]);
      }
    }else{
    	alert(rtJson.rtMsrg); 
    }
  }
}

function commit(){
  var smsRemind = "";
  var sms2Remind = "";
  var useFlag = "";
  if(document.getElementById("smsRemind").checked == true){
    smsRemind = "1";
  }else{
    smsRemind = "0";
  }
  if(document.getElementById("sms2Remind").checked == true){
    sms2Remind = "1";
  }else{
    sms2Remind = "0";
  }
  if(document.getElementById("useFlag").checked == true){
    useFlag = "1";
  }else{
    useFlag = "0";
  }
  
  if(useFlag == "1"){
    if($("checkUserId").value == ""){
    	alert("请设置审核人员！");
    	$("checkUserId").select();
    	return false;
      }
  }
  var moduleCode = document.getElementById("moduleCode").value;
  var checkUser = document.getElementById("checkUserId").value;
  var bannedHint = document.getElementById("bannedHint").value;
  var modHint = document.getElementById("modHint").value;
  var filterHint = document.getElementById("filterHint").value;
  if(seqId){
    var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorModuleAct/updateCensorModule.act";
    var rtJson = getJsonRs(url, "moduleCode="+moduleCode+"&useFlag="+useFlag+"&smsRemind="+smsRemind+"&sms2Remind="+sms2Remind+"&checkUser="+checkUser+"&bannedHint="+encodeURIComponent(bannedHint)+"&modHint="+encodeURIComponent(modHint)+"&filterHint="+encodeURIComponent(filterHint)+"&seqId="+seqId+"&moduleCodeOld="+moduleCodeOld);
    if(rtJson.rtState == "0"){
      location = "<%=contextPath %>/yh/core/funcs/system/censorwords/act/YHCensorModuleAct/getCensorModule.act";
    }else{
	  alert(rtJson.rtMsrg);
    }
  }else{
    var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorModuleAct/insertCensorModule.act";
    var rtJson = getJsonRs(url, "moduleCode="+moduleCode+"&useFlag="+useFlag+"&smsRemind="+smsRemind+"&sms2Remind="+sms2Remind+"&checkUser="+checkUser+"&bannedHint="+encodeURIComponent(bannedHint)+"&modHint="+encodeURIComponent(modHint)+"&filterHint="+encodeURIComponent(filterHint));
    if(rtJson.rtState == "0"){
      location = "<%=contextPath %>/yh/core/funcs/system/censorwords/act/YHCensorModuleAct/getCensorModule.act";
    }else{
	  alert(rtJson.rtMsrg);
    }
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
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" WIDTH="16" HEIGHT="16" align="absmiddle"><span class="big3">&nbsp;过滤模块选项</span>
    </td>
  </tr>
</table>
<br/>
<form method="post" name="form1" id="form1">
<input type="hidden" name="seqId" id="seqId" value="">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.censorwords.data.YHCensorModule.java"/>
 <table class="TableBlock" width="100%" align="center">
    <tr>
      <td class="TableContent" width="100">模块名称：</td>
      <td class="TableData">
        <select name="moduleCode" id=moduleCode class="BigSelect">
           <option value="0" selected>内部邮件</option>
           <option value="1">内部短信</option>
           <option value="2">手机短信</option>
        </select>
      </td>
    </tr>
    <tr>
      <td class="TableContent">启用过滤：</td>
      <td class="TableData">
        <input type="checkbox" id="useFlag" name="useFlag"><label for="useFlag">启用过滤</label>
      </td>
    </tr>
    <tr>
      <td class="TableContent">审核人员：</td>
      <td class="TableData">
        <input type="hidden" name="checkUserId" id="checkUserId" value="">
        <textarea cols=50 name="checkUserIdDesc" id="checkUserIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        &nbsp;<input type="button" value="选 择" class="SmallButtonW" onClick="selectUser(['checkUserId', 'checkUserIdDesc'])" title="选择人员" name="button">
        &nbsp;<input type="button" value="清 空" class="SmallButtonW" onClick="ClearUser('checkUserId', 'checkUserIdDesc')" title="清空人员" name="button">
      </td>
    </tr>
    <tr>
      <td class="TableContent">短信提醒：</td>
      <td class="TableData">
        <input type="checkbox" id="smsRemind" name="smsRemind"><label for="smsRemind">使用内部短信提醒审核人员</label>&nbsp;&nbsp;
        <input type="checkbox" id="sms2Remind" name="sms2Remind"><label for="sms2Remind">使用手机短信提醒审核人员</label>
      </td>
    </tr>
    <tr>
      <td class="TableContent">禁止提示：</td>
      <td class="TableData">
        <textarea class="BigInput" name="bannedHint" id="bannedHint" cols="50" rows="3"></textarea>
        内容被禁止时提示的消息，为空则不提示
      </td>
    </tr>
    <tr>
      <td class="TableContent">审核提示：</td>
      <td class="TableData">
        <textarea class="BigInput" name="modHint" id="modHint" cols="50" rows="3"></textarea>
        内容需先审核才可通过时提示的消息，为空则不提示
      </td>
    </tr>
    <tr>
      <td class="TableContent">过滤提示：</td>
      <td class="TableData">
        <textarea class="BigInput" name="filterHint" id="filterHint" cols="50" rows="3"></textarea>
        内容被过滤时提示的消息，为空则不提示
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" name="start" value="">
        <input type="button" value="确定" class="BigButton" onClick="commit()">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="window.history.back();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>