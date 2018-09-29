<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String confKey = request.getParameter("confKey");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑视频会议信息</title>
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
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/netmeeting/video/js/util.js"></script><script type="text/javascript">
var nowdate = new Date().format("yyyy-MM-dd hh"); 
function doInit(){
  setDate();
  var url = "<%=contextPath%>/yh/subsys/oa/netmeeting/video/act/YHVideoMeetingAct/getVideoMeeting.act?confKey=<%=confKey%>";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
    $('confKeyDiv').innerHTML = "&nbsp;" + data.confKey;
    if(data.conferencePattern == 0){
      $('conferencePattern1').checked = true;
    }
    else if(data.conferencePattern == 1){
    	$('conferencePattern2').checked = true;
    }
    $('startDate').value = (data.startTime).replace("T"," ");
    $('endDate').value = (data.endTime).replace("T"," ");
    $('hostId').value = 1;
    $('hostIdDesc').value = data.hostName;
    bindDesc([{cntrlId:"attendees", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }else{
    alert(rtJson.rtMsrg); 
  }
}

//日期
function setDate(){
  var date1Parameters = {
     inputId:'startDate'
     ,property:{isHaveTime:true,format:'yyyy-MM-dd hh:mm'}
     ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
     inputId:'endDate'
     ,property:{isHaveTime:true,format:'yyyy-MM-dd hh:mm'}
     ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
}


function doSubmit(){
  if(checkForm()){
    $("form1").submit();
  }
}

function checkForm(){
  if($("subject").value == ""){
    alert("主题不能为空！");
    $("subject").focus();
    return (false);
  }

  if($("password").value == ""){
    alert("密码不能为空！");
    $("password").focus();
    return (false);
  }

  if($("password1").value == ""){
    alert("确认密码不能为空！");
    $("password1").focus();
    return (false);
  }
  
  if($("startDate").value == ""){
    alert("开始时间不能为空！");
    $("startDate").focus();
    return (false);
  }

  if($("startDate").value != ""){
	    nowdate = nowdate + ":00"
    if($("startDate").value < nowdate){
      alert("开始时间不能小于当前时间！");
      $("startDate").focus();
      return (false);
    }
  }
  
  if($("endDate").value == ""){
    alert("结束时间不能为空！");
    $("endDate").focus();
    return (false);
  }

  if($("hostIdDesc").value == ""){
    alert("主持人不能为空！");
    $("hostIdDesc").focus();
    return (false);
  }

  if($("attendeesDesc").value == ""){
    alert("参与人不能为空！");
    $("attendeesDesc").focus();
    return (false);
  }

  var password = $("password").value;
  var password1 = $("password1").value;
  if(password != password1){
    alert("确认密码不正确！");
    $("password1").focus();
    $("password1").select();
    return (false);
  }

  var startDate = $('startDate').value;
  var endDate = $('endDate').value;
  if(startDate > endDate){
    alert("结束时间不能小于开始时间");
    $("endDate").focus(); 
    $("endDate").select(); 
    return false;
  }
  return true;
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 编辑视频会议信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form action="<%=contextPath%>/yh/subsys/oa/netmeeting/video/act/YHVideoMeetingAct/updateVideoMeetingInfo.act"  method="post" name="form1" id="form1" onsubmit="">
  <table class="TableBlock" width="80%" align="center">
    <tr>
      <td nowrap class="TableData">会议号：</td>
      <td class="TableData" colspan=3>
        <span id="confKeyDiv"></span>
        <input type="hidden" name="confKey" id="confKey" class="BigInput" size="27">
      </td>
    </tr>
    <tr>    
      <td nowrap class="TableData">会议主题：<font color="red">*</font> </td>
      <td class="TableData" >
        <input type="text" name="subject" id="subject" class="BigInput" size="25">
      </td>
      <td nowrap class="TableData">会议模式：<font color="red">*</font> </td>
      <td class="TableData">
        <input type="radio" name="conferencePattern" id="conferencePattern1" value="0">主持人控制模式
        <input type="radio" name="conferencePattern" id="conferencePattern2" value="1">自由模式
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">密码：<font color="red">*</font> </td>
      <td class="TableData" >
        <input type="password" name="password" id="password" class="BigInput" size="27">
      </td>
      <td nowrap class="TableData">确认密码：<font color="red">*</font> </td>
      <td class="TableData" >
        <input type="password" name="password1" id="password1" class="BigInput" size="27">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">开始时间：<font color="red">*</font> </td>
      <td class="TableData">
        <input type="text" name="startDate" id="startDate" size="21" maxlength="10"  class="BigInput" readonly onchange="subDateTime(this)">
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">结束时间：<font color="red">*</font> </td>
      <td class="TableData">
        <input type="text" name="endDate" id="endDate" size="21" maxlength="10"  class="BigInput" readonly onchange="subDateTime(this)">
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">主持人：<font color="red">*</font> </td>
      <td class="TableData" colspan=3>
        <input type="hidden" name="hostId" id="hostId" value="">
        <input type="text" name="hostIdDesc" id="hostIdDesc" class="BigStatic" readonly size="25">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">参与人：<font color="red">*</font> </td>
      <td class="TableData" colspan=3>
        <input type="hidden" name="attendees" id="attendees" >
        <textarea cols="60" name="attendeesDesc" id="attendeesDesc" rows="2" style="overflow-y:auto;" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['attendees', 'attendeesDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('attendees').value='';$('attendeesDesc').value='';">清空</a>
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData">会议描述：</td>
      <td class="TableData" colspan=3>
        <textarea name="agenda" id="agenda" cols="60" rows="2" class="BigInput" ></textarea>
      </td>
    </tr>   
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
        <input type="button" value="返回" onclick="history.go(-1);" class="BigButton">
      </td>
    </tr>
  </table>
</form>

</body>
</html>