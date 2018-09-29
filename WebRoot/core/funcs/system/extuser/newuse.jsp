<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
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
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
function doInit(){
  if(seqId){
    var url = "<%=contextPath%>/yh/core/funcs/system/extuser/act/YHExtUserAct/getEditExtUser.act?seqId="+seqId;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      bindJson2Cntrl(rtJson.rtData);
      for(var i = 0; i < rtJson.rtData.length; i++){
        document.getElementById("seqId").value = rtJson.rtData[i].seqId;
        document.getElementById("userId").value = rtJson.rtData[i].userId;
        var authModule = rtJson.rtData[i].authModule;
        if(authModule == "1"){
          document.getElementById("moduleSms").checked = true;
        }else if(authModule == "4"){
          document.getElementById("moduleWorkflow").checked = true;
        }else if(authModule == "1,4,"){
          document.getElementById("moduleWorkflow").checked = true;
          document.getElementById("moduleSms").checked = true;
        }
        if(rtJson.rtData[i].useFlag == "1"){
          document.getElementById("useFlag1").checked = true;
        }else{
          document.getElementById("useFlag0").checked = true;
        }
        if(rtJson.rtData[i].remark == "null" || rtJson.rtData[i].remark == ""){
          document.getElementById("remark").value = "";
        }else{
          document.getElementById("remark").value = rtJson.rtData[i].remark;
        }
        if(rtJson.rtData[i].postfix == "null" || rtJson.rtData[i].postfix == ""){
          document.getElementById("postfix").value = "";
        }else{
          document.getElementById("postfix").value = rtJson.rtData[i].postfix;
        }
      }
    }else{
    	alert(rtJson.rtMsrg); 
    }
    var editUser = $('editUser');
    editUser.style.display = "";
  }else{
    var newUser = $('newUser');
    newUser.style.display = "";
    document.getElementById("form1").reset();
  }
}

function check(){
  var userId = document.getElementById("userId");
  var password = document.getElementById("password");
  var password1 = document.getElementById("password1");
  if(userId.value == ""){
    alert("用户名不能为空！");
    userId.select();
    userId.focus();
    return false;
  }
  if(password.value == ""){
    alert("密码不能为空！");
    password.select();
    password.focus();
    return false;
  }
  if(password1.value == ""){
    alert("确认密码不能为空！");
    password1.select();
    password1.focus();
    return false;
  }
  if(password.value != "" && password.value != password1.value){
    alert("两次输入的密码不一致！");
    password1.focus();
    return false;
  }
  return true;
}

function commit(){
  if(!check()){
    return;
  }
  var useFlagstr = ""; 
  var moduleSmsStr = "";
  var moduleWorkflowStr = "";
  var seqId = document.getElementById("seqId").value;
  var userId = document.getElementById("userId").value;
  
  var password = document.getElementById("password").value;
  var useFlag = document.getElementsByName("useFlag");
  for(var i = 0; i < useFlag.length; i++){
    if(useFlag[i].checked){
      useFlagstr = useFlag[i].value;
    }
  } 

  var moduleSms = document.getElementById("moduleSms");
  if (moduleSms.checked == true) {
    moduleSmsStr = "1";
  } else {
    moduleSmsStr = "0";
  }
  
  var moduleWorkflow = document.getElementById("moduleWorkflow");
  if (moduleWorkflow.checked == true) {
    moduleWorkflowStr = "4";
  } else {
    moduleWorkflowStr = "0";
  }
  var remark = document.getElementById("remark").value;
  var postfix = document.getElementById("postfix").value;
  if (seqId) {
    var url = "<%=contextPath%>/yh/core/funcs/system/extuser/act/YHExtUserAct/updateExtUser.act?seqId="+seqId+"&userId="+encodeURIComponent(userId)+"&password="+password+"&useFlagstr="+useFlagstr+"&remark="+encodeURIComponent(remark)+"&moduleSmsStr="+moduleSmsStr+"&moduleWorkflowStr="+moduleWorkflowStr+"&postfix="+encodeURIComponent(postfix);
    var rtJson = getJsonRs(url, mergeQueryString($("form1")));
    if (rtJson.rtState == "0") {
      location = "<%=contextPath %>/core/funcs/system/extuser/add.jsp?seqId="+seqId;
    } else {
	  alert(rtJson.rtMsrg);
    }

  } else {
    var url = "<%=contextPath%>/yh/core/funcs/system/extuser/act/YHExtUserAct/addExtUser.act?userId="+encodeURIComponent(userId)+"&password="+password+"&useFlagstr="+useFlagstr+"&remark="+encodeURIComponent(remark)+"&moduleSmsStr="+moduleSmsStr+"&moduleWorkflowStr="+moduleWorkflowStr+"&postfix="+encodeURIComponent(postfix);
    var rtJson = getJsonRs(url, mergeQueryString($("form1")));
    if (rtJson.rtState == "0") {
      location = "<%=contextPath %>/core/funcs/system/extuser/add.jsp";
    } else {
	  alert(rtJson.rtMsrg);
    }
  }
  
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<div id="newUser" style="display:none">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;新建用户</span>
    </td>
  </tr>
</table>
</div>
<div id="editUser" style="display:none">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;编辑用户</span>
    </td>
  </tr>
</table>
</div>
<form method="post" name="form1" id="form1">
<input type="hidden" name="seqId" id="seqId" value="">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.extuser.data.YHExtUser.java"/>
<table class="TableBlock" width="550" align="center">
   <tr class="TableData">
    <td nowrap class="TableContent" width="120">用户名：</td>
    <td>
        <input type="text" name="userId" id="userId" value="" class="BigInput" size="20" maxlength="20">
    </td>
   </tr>
   <tr class="TableData">
    <td nowrap class="TableContent">密码：</td>
    <td>
        <input type="password" name="password" id="password" value="" class="BigInput" size="20">&nbsp;
    </td>
   </tr>
   <tr class="TableData">
    <td nowrap class="TableContent">确认密码：</td>
    <td>
        <input type="password" name="password1" id="password1" value="" class="BigInput" size="20">&nbsp;
    </td>
   </tr>
   <tr class="TableData">
    <td nowrap class="TableContent">用户状态：</td>
    <td>
    	  <input type="radio" name="useFlag" id="useFlag1" value="1" checked><label for="USE_FLAG1">启用</label>&nbsp;
    	  <input type="radio" name="useFlag" id="useFlag0" value="0" ><label for="USE_FLAG0">停用</label>&nbsp;
    </td>
   </tr>
   <tr class="TableData">
    <td nowrap class="TableContent">模块权限：</td>
    <td>
    	  <input type="checkbox" name="moduleSms" id="moduleSms" ><label for="MODULE_SMS">内部短信</label>&nbsp;
    	  <input type="checkbox" name="moduleWorkflow" id="moduleWorkflow" ><label for="MODULE_WORKFLOW">工作流</label>&nbsp;
    </td>
   </tr>
   <tr class="TableData">
    <td nowrap class="TableContent" width="120">备注：</td>
    <td>
        <input type="text" name="remark" id="remark" value="" class="BigInput" size="51" maxlength="200">
    </td>
   </tr>
   <tr class="TableData">
    <td nowrap class="TableContent" width="120">内容后缀(仅内部短信有效)：</td>
    <td>
        <textarea name="postfix" id="postfix" class="BigInput" cols="50" rows="5"></textarea>
        <br>“内容后缀”将追加在内部短信等内容的后边，以区别于OA内部发送的信息。
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="hidden" name="UID" value="">
        <input type="button" value="确 定" class="BigButton" name="button" onclick="commit()">&nbsp;&nbsp;
    </td>
</table>
</form>
</body>
</html>