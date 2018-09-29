<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String confKey = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("confKey")));
	String confType = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("confType")));
	String duringTime = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("duringTime")));
	String hostName = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("hostName")));
	String startTime = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("startTime")));
	String endTime = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("endTime")));
	String status = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("status")));
	String subject = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("subject")));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会议信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/netmeeting/video/js/util.js"></script>
<script type="text/javascript">
function doInit(){
	var duringTime = "<%=duringTime%>";

	//持续时间
  var hour = (duringTime/60 + ".0");
  hour = hour.split(".")[0];
  var min = duringTime%60;
  duringTime = hour + "小时" + min + "分钟"
  if(hour < 1){
    duringTime = min + "分钟";
  }
  if(min == 0){
    duringTime = hour + "小时";
  }
  $('duringTime').innerHTML = duringTime;


  //会议类型
  var confType = "<%=confType%>";
  switch(confType){
	  case '0' : confType = "预约会议";break;
	  case '1' : confType = "即时会议";break;
	  case '2' : confType = "固定会议";break;
	  case '3' : confType = " 周期会议";break;
	}
  $('confType').innerHTML = confType;

  //时间
  var startTime = "<%=startTime%>";
  var endTime = "<%=endTime%>";
  startTime = startTime.replace("T"," ");
  endTime = endTime.replace("T"," ");
  $('startTime').innerHTML = startTime;
  $('endTime').innerHTML = endTime;
}

function doSubmit(){
  if(checkForm()){
	  var pars = Form.serialize($('form1'));
	  var url = "<%=contextPath%>/yh/subsys/oa/netmeeting/video/act/YHVideoMeetingAct/doStartVideoMeeting.act";
	  var json = getJsonRs(url,pars);
	  if(json.rtState == "0"){
		  var data = json.rtData;
		  if(data.ciURL){
	      var meetingURL = data.ciURL + "?siteId=1&dt=GMT";
	      $('form1').action = meetingURL;
	      $('form1').target ="_blank";
	      $('token').value = data.token;
	      $('form1').submit();
		  }
		  else if(data.data == 'empty'){
			   window.parant.location.href = "<%=contextPath %>/subsys/oa/netmeeting/video/manage/check.jsp";
		  }
      else if(data.data == 'error'){
    	  window.parant.location.href = "<%=contextPath %>/subsys/oa/netmeeting/video/manage/error.jsp";
      }
      else{
        alert(data.error);
      }
		} else{
	    alert("启动会议失败！");
	  }
  }
}

function checkForm(){
	if($("password").value == ""){
	  alert("会议密码不能为空！");
	  $("password").focus();
	  return (false);
	}
	return (true);
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 会议信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="40%" align="center">
  <tr>
    <td align="left" width="80" class="TableContent">主题：</td>
    <td align="left" class="TableData" width="180"><%=subject %></td>
  </tr>
  <tr>
    <td align="left" width="80" class="TableContent">会议号：</td>
    <td align="left" class="TableData" width="180"><%=confKey %></td>
  </tr>
  <tr>
    <td align="left" width="80" class="TableContent">会议类型：</td>
    <td align="left" class="TableData" width="180" id="confType"></td>
  </tr>
  <tr>
    <td align="left" width="80" class="TableContent">主持人：</td>
    <td align="left" class="TableData Content" width="180"><%=hostName %></td>
  </tr>
  <tr>
    <td align="left" width="80" class="TableContent">开始时间：</td>
    <td align="left" class="TableData" width="180" id="startTime"></td>
  </tr>
  <tr>
    <td align="left" width="80" class="TableContent">结束时间：</td>
    <td align="left" class="TableData Content" width="180" id="endTime"></td>
  </tr>
  
  <tr>
    <td align="left" width="80" class="TableContent">持续时间：</td>
    <td align="left" class="TableData Content" width="180" id="duringTime"></td>
  </tr>
</table><p><br></br>
<form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="40%" align="center">
  <tr>
    <td align="left" width="80" class="TableContent">会议密码：<font color="red">*</font></td>
    <td align="left" class="TableData" width="180">
      <input type="password" id="password" name="password" >
      <input type="hidden" id="confKey" name="confKey" value="<%=confKey %>">
    </td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="2">
      <input type="button" value="确定" class="BigButton" onClick="doSubmit()" >
      <input type="button" value="返回" class="BigButton" onClick="history.go(-1);">
    </td>
  </tr>
</table>
<input type="hidden" id="token" name="token">
</form>
</body>
</html>