<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>修改出差记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function checkForm(){
  var evectionDate1 = document.getElementById("evectionDate1");
  var evectionDate2 = document.getElementById("evectionDate2");
  if(evectionDate1!=''){
    if(!isValidDateStr(document.getElementById("evectionDate1").value)){
      alert("起始日期格式不对,应形如 2010-02-01");
      document.getElementById("evectionDate1").focus();
      document.getElementById("evectionDate1").select();
      return false;
    }
  }
  if(evectionDate2!=''){
    if(!isValidDateStr(document.getElementById("evectionDate2").value)){
      alert("结束日期格式不对,应形如 2010-02-01");
      document.getElementById("evectionDate2").focus();
      document.getElementById("evectionDate2").select();
      return false;
    }
  }
  var beginInt;
  var endInt;
  var beginArray = evectionDate1.value.split("-");
  var endArray = evectionDate2.value.split("-");
  for(var i = 0 ; i<beginArray.length; i++){
    beginInt = parseInt(" " + beginArray[i],10);  
    endInt = parseInt(" " + endArray[i],10);
    if((beginInt - endInt) > 0){
      alert("出差起始日期不能大于结束日期!");
      evectionDate2.focus();
      evectionDate2.select();
      return false;
    }else if(beginInt - endInt<0){
      return true;
    }  
  }
  return true;
}
function doOnload(){
  var seqId = '<%=seqId%>';
  var requestUrlEvection = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendEvectionAct/selectEvectionById.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestUrlEvection); 
  //alert(rsText);
  if(rtJson.rtState == '1'){ 
    alert(rtJson.rtMsrg); 
    return ; 
  }
  var prcs = rtJson.rtData;
  var seqId = prcs.seqId;
  var leaderId = prcs.leaderId;
  var leaderName = prcs.leaderName;
  var evectionDest = prcs.evectionDest;
  var evectionDate1 = prcs.evectionDate1;
  var evectionDate2 = prcs.evectionDate2;
  var reason = prcs.reason;
  document.getElementById("evectionDest").value = evectionDest;
  document.getElementById("evectionDate1").value = evectionDate1.substr(0,10);
  document.getElementById("evectionDate2").value = evectionDate2.substr(0,10);
  document.getElementById("reason").value = reason;
  document.getElementById("seqId").value = seqId;
  //
  var date1Parameters = {
      inputId:'evectionDate1',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'evectionDate2',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);  
}
function Init(){
  if(checkForm()){
    var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageEvectionAct/updateEvectionById.act";
    var rtJson = getJsonRs(requestURL,mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    parent.opener.location.reload();
    document.getElementById("returnDiv").style.display = "";
    document.getElementById("div").style.display = "none";
  }
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 修改出差记录</span><br>
    </td>
  </tr>
</table>
<br>	
<div align="center" id="div">
<form action="" id="form1"  method="post" name="form1" class="big1">
  <table class="TableBlock" width="90%" align="center">   
    <tr>
      <td nowrap class="TableData">出差地点：</td>
      <td class="TableData" align="left">
         <input type="text" id="evectionDest" name="evectionDest" size="50" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">出差时间：</td>
      <td class="TableData" align="left">
        <input type="text" id="evectionDate1" name="evectionDate1" size="10" maxlength="10" class="BigInput" value="">
        <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
        至 <input type="text" id="evectionDate2" name="evectionDate2" size="10" maxlength="10" class="BigInput" value="">
        <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">事由：</td>
      <td class="TableData" align="left">
        <textarea id="reason" name="reason" cols="50" rows="4" class="BigInput"></textarea>
      </td>
    </tr>
  </table>
<br><br><br>
<center>	
	<input type="hidden" id="seqId"  name="seqId" value="<%=seqId%>">
	<input type="button" value="保存" class="BigButton" onclick="Init();">&nbsp;&nbsp;
	<input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
</center>	
</form>
</div>	
<div id="returnDiv"  align="center"  style="display:none">
<table class="MessageBox" align="center" width="220">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">修改成功</div>
    </td>
  </tr>
</table>
<center>
	<input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
</center>
</div>
</body>
</html>