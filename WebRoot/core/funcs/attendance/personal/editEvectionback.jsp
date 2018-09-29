<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>修改出差登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript">
function CheckForm(){
  if(document.getElementById("evectionDest").value==""){ 
    alert("出差地区不能为空！");
    document.getElementById("evectionDest").focus();
    document.getElementById("evectionDest").select();
    return (false);
  }
  if(document.getElementById("evectionDate1").value==""){ 
    alert("出差开始日期不能为空！");
    document.getElementById("evectionDate1").focus();
    document.getElementById("evectionDate1").select();
    return (false);
   }

  if(document.getElementById("evectionDate2").value==""){ 
    alert("出差结束日期不能为空！");
    document.getElementById("evectionDate2").focus();
    document.getElementById("evectionDate2").select();
    return (false);
   }

  if(!isValidDateStr(document.getElementById("evectionDate1").value)){
    alert("起始日期格式不对,应形如 2010-02-01");
    document.getElementById("evectionDate1").focus();
    document.getElementById("evectionDate1").select();
    return false;
    }
  if(!isValidDateStr(document.getElementById("evectionDate2").value)){
    alert("结束日期格式不对,应形如 2010-02-01");
    document.getElementById("evectionDate2").focus();
    document.getElementById("evectionDate2").select();
    return false;
    }

  var evectionDate1 = document.getElementById("evectionDate1").value;
  var evectionDate2 = document.getElementById("evectionDate2").value;
  var beginInt;
  var endInt;
  var beginArray = evectionDate1.split("-");
  var endArray = evectionDate2.split("-");
  for(var i = 0 ; i<beginArray.length; i++){
    beginInt = parseInt(beginArray[i],10);  
    endInt = parseInt(endArray[i],10);
    if((beginInt - endInt) > 0){
      alert("出差起始日期不能大于结束日期!");
      document.getElementById("evectionDate2").focus();
      document.getElementById("evectionDate2").select();
      return false;
    }else if((beginInt - endInt<0)){
      return true;
    }  
  }

  return (true);
}
function doOnload(){
  var seqId = '<%=request.getParameter("seqId")%>';
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
  /*var date1Parameters = {
      inputId:'evectionDate1',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
    };
  new Calendar(date1Parameters);*/
  var date2Parameters = {
      inputId:'evectionDate2',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
  getSysRemind();
//手机
  getMoblieSmsRemind("moblieSmsRemindDiv","moblieSmsRemind")
}
function Init(){
  if( CheckForm()){
    var requestURL= "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendEvectionAct/updateStatus.act";
    var rtJson = getJsonRs(requestURL, mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    if(rtJson.rtState == "0"){
      parent.opener.location.reload();
      window.close();
      return ;
    }
  } 
}
//判断是否要显示短信提醒
function getSysRemind(){
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=6";
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  var allowRemind = prc.allowRemind;
  var defaultRemind = prc.defaultRemind;
  if(allowRemind=='2'){
    $("smsRemindDiv").style.display = 'none';
  }else{
    if(defaultRemind=='1'){
      $("smsRemind").checked = true;
    }
  }
  //return prc;
}
/** 
*js代码 
*是否显示手机短信提醒 
*/ 
function getMoblieSmsRemind(remidDiv,remind){ 
var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=6"; 
var rtJson = getJsonRs(requestUrl); 
if(rtJson.rtState == "1"){ 
alert(rtJson.rtMsrg); 
return ; 
} 
var prc = rtJson.rtData; 
var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
if(moblieRemindFlag == '2'){ 
$(remidDiv).style.display = ''; 
$(remind).checked = true; 
}else if(moblieRemindFlag == '1'){ 
$(remidDiv).style.display = ''; 
$(remind).checked = false; 
}else{ 
$(remidDiv).style.display = 'none'; 
} 
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" align="absmiddle"><span class="big3">  &nbsp;修改出差登记</span>
    </td>
  </tr>
</table>
<form action="<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendEvectionAct/updateEvection.act"  method="post" id="form1" name="form1" onsubmit="return CheckForm();">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.attendance.personal.data.YHAttendEvection"/>
  <table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableData">出差地点：</td>
      <td class="TableData">
         <input type="text" id="evectionDest" name="evectionDest" size="50" maxlength="100" class="BigInput" readonly="readonly" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">出差时间：</td>
      <td class="TableData">
        <input type="text" id="evectionDate1" name="evectionDate1" size="10" maxlength="10" class="BigInput" readonly="readonly" value="">
        至 <input type="text" id="evectionDate2" name="evectionDate2" size="10" maxlength="10" class="BigInput" value="2010-02-02">
        <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">事由：</td>
      <td class="TableData">
        <textarea id="reason" name="reason" cols="50" rows="4" readonly="readonly" class="BigInput"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">提醒：</td>
      <td class="TableData">

   <span id="smsRemindDiv"><input type="checkbox" id="smsRemind" name="smsRemind" ><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>    <span id="moblieSmsRemindDiv"><input type="checkbox" id="moblieSmsRemind" name="moblieSmsRemind" ><label for="smsRemind">使用手机短信提醒</label>&nbsp;&nbsp;</span></td>  </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
          <input type="hidden" id="seqId" name="seqId" value=""></input>
       <input type="button" value="保存" onclick="Init();" class="BigButton">&nbsp;&nbsp;
	   <input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
