<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>外出归来，主管确认外出记录</title>
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
  if(document.getElementById("outType").value==""){ 
    alert("外出原因不能为空！");
    document.getElementById("outType").focus();
    document.getElementById("outType").select();
    return (false);
  }
   if(document.getElementById("outDate") == ""){
     alert("外出日期不能为空！");
     document.getElementById("outDate").focus();
     document.getElementById("outDate").select();
     return false;
   }
   if(!isValidDateStr(document.getElementById("outDate").value)){
     alert("起始日期格式不对,应形如 2010-02-01");
     document.getElementById("outDate").focus();
     document.getElementById("outDate").select();
     return false;
   }
   if(document.getElementById("outTime1").value==""){ 
     alert("外出起止时间不能为空！");
     document.getElementById("outTime1").focus();
     document.getElementById("outTime1").select();
     return (false);
   }
   if(document.getElementById("outTime2").value==""){ 
     alert("外出起止时间不能为空！");
     document.getElementById("outTime2").focus();
     document.getElementById("outTime2").select();
     return (false);
   }
   var type1 =   "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]$"　; 
   var type2 = "^\:[0-5]?[0-9]$"　;
   var re1 = new RegExp(type1); 
   var re2 = new RegExp(type2);
   if(document.getElementById("outTime1").value != ""){
     if(document.getElementById("outTime1").value.match(re1) == null || document.getElementById("outTime1").value.match(re2) != null) { 
       alert( "开始时间格式不正确,应形如 8:00"); 
       document.getElementById("outTime1").focus();
       document.getElementById("outTime1").select();
       return false;
     }
   }
   if(document.getElementById("outTime2").value != ""){
     if(document.getElementById("outTime2").value.match(re1) == null || document.getElementById("outTime2").value.match(re2) != null) { 
       alert( "结束时间格式不正确,应形如 8:00"); 
       document.getElementById("outTime2").focus();
       document.getElementById("outTime2").select();
       return false;
     }
   }
   var beginInt;
   var endInt;
   var beginArray = document.getElementById("outTime1").value.split(":");
   var endArray = document.getElementById("outTime2").value.split(":");
   for(var i = 0 ; i<beginArray.length; i++){
     beginInt = parseInt("" + beginArray[i]+ "",10);  
     endInt = parseInt("" + endArray[i]+ "",10);
     if((beginInt - endInt) > 0){
       alert("外出结束时间应晚于外出开始时间!");
       document.getElementById("outTime2").focus();
       document.getElementById("outTime2").select();
       return false;
     }else if(beginInt - endInt<0){
       return true;
     }else{
       if(i==1){
         alert("外出结束时间应晚于外出开始时间!");
         document.getElementById("outTime2").focus();
         document.getElementById("outTime2").select();
         return false;
        }
     }  
   }
   return (true);
}
function doOnload(){
  var seqId = '<%=seqId%>';
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendOutAct/selectOutById.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prcs = rtJson.rtData;
  var seqId = prcs.seqId;
  var userId = prcs.userId;
  var userName = prcs.userName;
  var leaderId = prcs.leaderId;
  var leaderName = prcs.leaderName;
  var outType = prcs.outType;
  var createDate = prcs.createDate;
  var submitTime = prcs.submitTime;
  var outTime1 = prcs.outTime1;
  var outTime2 = prcs.outTime2;
  var reason = prcs.reason;
  document.getElementById("userName").innerHTML = userName;
  document.getElementById("outTime1").value = outTime1;
  document.getElementById("outTime2").value = outTime2
  document.getElementById("outType").value = outType;
  document.getElementById("outDate").value = submitTime.substr(0,10);
  //初始化程序
  var parameters = {
      inputId:'outDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date'
  };
  new Calendar(parameters);
}
function Init(){
  if(checkForm()){
    var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageOutAct/updateOutById.act";
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
<div id="div">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 外出归来，主管确认外出记录</span><br>
    </td>
  </tr>
</table>
<br>	
<div align="center">
  <form action="#"  method="post" id="form1" name="form1" class="big1">
  <table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableData"> 外出人员：</td>
      <td id="userName" class="TableData" align="left">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 外出原因：</td>
      <td class="TableData">
      	 <textarea id="outType" name="outType" class="BigInput" cols="60" rows="4"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 外出时间：</td>
      <td class="TableData">
         日期 <input type="text" id="outDate" name="outDate" size="15" maxlength="5" class="BigStatic" readonly value="2010-03-18">
         <img id="date" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
         由 <input type="text" id="outTime1"  name="outTime1" size="5" maxlength="5" class="BigInput" value="13:36">
         <img src="<%=imgPath%>/clock.gif" align="absMiddle" border="0" align="absMiddle" style="cursor:pointer" >
         至 <input type="text" id="outTime2" name="outTime2" size="5" maxlength="5" class="BigInput" value="14:45">
         <img src="<%=imgPath%>/clock.gif" align="absMiddle" border="0" align="absMiddle" style="cursor:pointer" ><br>
      </td>
    </tr>
  </table>

<br><br><br>
<center>	
	<input type="hidden" id="seqId" name="seqId" value="<%=seqId %>">
	<input type="button" value="保存" class="BigButton" onclick="Init();">&nbsp;&nbsp;
	<input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
</center>	

</form>
</div>	
</div>
<div id="returnDiv" style="display:none">
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