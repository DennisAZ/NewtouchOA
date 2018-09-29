<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会议室管理制度</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript">
var requestUrl = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct";
function doInit(){
	var url = requestUrl + "/getMeetingRule.act";
	var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ;
	}
  var prcs = rtJson.rtData;
 	$("textInfo").innerHTML = prcs.meetingRule;
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" HEIGHT="20" width="20" align="middle"><span class="big3"> 会议室管理制度</span>
    </td>
  </tr>
</table>
<br>
<table border="0" width="100%" cellpadding="3" cellspacing="1" class="TableBlock" align="center">
<tr>
	<td class="TableData" nowrap ><span id="textInfo"></span>
	</td>
</tr>
</table>
<br>
<center><input type="button" value="关 闭" class="SmallButton" onclick="window.close()"></center>





</body>
</html>