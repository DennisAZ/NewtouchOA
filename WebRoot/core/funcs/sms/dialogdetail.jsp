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
<title>发送提醒</title>

<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var fromId = "<%=fromId%>";
function doInit() {
  if(document.getElementById('sendTime')){
    var parameters = { 
        inputId:'sendTime', 
        property:{isHaveTime:true} 
    }; 
    new Calendar(parameters);
   }  

//    if(seqId) {
//      var url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/getSmsBodyContent.act";
//      var rtJson = getJsonRs(url, "seqId=" + seqId);
 //     if (rtJson.rtState == "0") {
 //       document.getElementById("content").innerHTML = rtJson.rtData.content;
 //     }else {
 //       alert(rtJson.rtMsrg); 
 //     }
 //   }

  if(seqId) {
    var url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/changeName.act";
    var rtJson = getJsonRs(url, "toId=" + fromId);
    var seqIdPerson = null;
    if (rtJson.rtState == "0") {
      for(var i = 0; i < rtJson.rtData.length; i++){
        seqIdPerson = rtJson.rtData[i].seqId; 
        }
    }else {
      alert(rtJson.rtMsrg); 
    }
    document.getElementById("toIdDesc").value = fromId;
    document.getElementById("toId").value = seqIdPerson;
  }
}

  function check() {
    var cntrl = document.getElementById("toIdDesc");
    if (!cntrl.value) {
  	  alert("收信人不能为空！");
  	  return false;
    }

    var cntrld = document.getElementById("content"); 
    if ((cntrld.value).trim() == "") {
  	  alert("提醒内容不能为空！");
  	  cntrld.focus();
  	  return false;
    }

  }
  
  function commitSms() {
	var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/addSmsBody.act";
    var rtJson = getJsonRs(url, mergeQueryString($("smsBodyInfoForm")));
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg);
      //$("smsBodyInfoForm").reset();
    }
  }

  function SelectUser(TO_ID, TO_NAME){
    var URL = "/yh/core/funcs/dept/userselect.jsp?TO_ID=" + TO_ID + "&TO_NAME=" + TO_NAME;
    openDialog(URL,'450', '340');
  }

  function clearUser() {
    document.getElementById("toIdDesc").value = "";
    document.getElementById("toId").value = "";
  }
</script>
</head>
<body onload="doInit()">
<form id="smsBodyInfoForm" name="smsBodyInfoForm" method="post">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.sms.data.YHSmsBody"/>
  <table class="TableBlock" width="380" height="240" align="center">
    <tr>
      <td class="TableData">
        <input type="hidden" id="toId" name="toId" value="">
        <textarea cols="40" rows="2" id="toIdDesc" name="toIdDesc" class="BigStatic" readonly></textarea>
        <a href="javascript:;" onClick="SelectUser('toId','toIdDesc')">添加</a>
      </td>
    </tr>
    <tr>
      <td class="TableData">
        <textarea id="content" name="content" class="BigInput" cols="40" rows="8"></textarea>       
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2">
      <span style="float:left;">按Ctrl+回车发送</span>
      <input type="hidden" name="I_VER" value="">
        <input type="button" value="发送" class="BigButton" onclick="commitSms()">
        <input type="button" value="消息记录" class="BigButton" onClick="show_msg()">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
  <table class="TableBlock" width="380" height="240" align="center">
    <tr>
      <td class="TableData">
        <input type="hidden" id="toId" name="toId" value="">
        <textarea cols="40" rows="2" id="toIdDesc" name="toIdDesc" class="BigStatic" readonly></textarea>
        <a href="javascript:;" onClick="SelectUser('toId','toIdDesc')">添加</a>
      </td>
    </tr>
    <tr>
      <td class="TableData">
        <textarea id="content" name="content" class="BigInput" cols="40" rows="8"></textarea>       
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2">
      <span style="float:left;">按Ctrl+回车发送</span>
      <input type="hidden" name="I_VER" value="">
        <input type="button" value="发送" class="BigButton" onclick="commitSms()">
        <input type="button" value="消息记录" class="BigButton" onClick="show_msg()">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>