<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM");
String seqId = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("seqId")));
String subject = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("subject")));
String toIdStr = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("toIdStr")));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加参会人员 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">

function doInit(){
	var toIdStr = encodeURIComponent("<%=toIdStr%>");
	staffNameFunc(toIdStr);
	getSysRemind("smsRemindDiv","smsRemind",3);
}

function doSubmit(){
  if(checkForm()){
    $("form1").submit();
  }
}

function checkForm(){
  if($("toIdDesc").value == ""){
    alert("参会范围（人员）不能为空！");
    $("toIdDesc").focus();
    return (false);
  }
	return true;
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
if(allowRemind=='2'){ 
  $(remidDiv).style.display = 'none'; 
}else{
  $(remidDiv).style.display = ''; 
  if(defaultRemind=='1'){ 
    $(remind).checked = true; 
  } 
}
if(document.getElementById(remind).checked){
  document.getElementById(remind).value = "1";
}else{
  document.getElementById(remind).value = "0";
}
}
//设置提醒值

function checkBox(ramCheck){
  if(document.getElementById(ramCheck).checked){
    document.getElementById(ramCheck).value = "1";
  }else{
    document.getElementById(ramCheck).value = "0";
  }
}

//单位员工名称
function staffNameFunc(toIdStr){
  var url = contextPath + "/yh/subsys/oa/netmeeting/text/act/YHNetmeetingAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + toIdStr);
  if (rtJson.rtState == "0") {
    $('toIdDesc').innerHTML = rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 增加参会人员-(会议主题：<%=subject %>) </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/netmeeting/text/act/YHNetmeetingAct/updateNetmeetingInfo.act"  method="post" name="form1" id="form1" onsubmit="">
	<table class="TableBlock" width="50%" align="center">
	  <tr>
	  	<td nowrap class="TableData">增加参会人员：<font color="red">*</font> </td>
	  	<td class="TableData">
        <input type="hidden" name="toId" id="toId" value="<%=toIdStr %>">
        <textarea cols="40" name="toIdDesc" id="toIdDesc" rows="2" style="overflow-y:auto;" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['toId', 'toIdDesc']);">添加</a>
	    </td>
	  </tr>
    <tr>
      <td nowrap class="TableData">会议开始时提醒参会者：</td>
      <td class="TableData" >
        <span id="smsRemindDiv" style="display: none"><input type="checkbox" name="smsRemind" id="smsRemind" value="" onclick="checkBox('smsRemind')"><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="hidden" name="seqId" id="seqId" value="<%=seqId %>">
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
        <input type="button" value="返回" onclick="history.go(-1);" class="BigButton">
      </td>
    </tr>
  </table>
</form>

</body>
</html>