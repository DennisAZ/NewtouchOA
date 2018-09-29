<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.person.data.YHPerson,yh.core.funcs.calendar.data.*"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("seqId")));
  String subject = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("subject")));
  String createUserId = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("createUserId")));
  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER); 
  int userId = user.getSeqId(); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>网络会议 </title>
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
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
var count = 0;
var usernameToSay = "";
var createUserId = "<%=createUserId%>";
var userId = "<%=userId%>";
var timer;
var meetingNotStop = true;
function doInit(){
	if(createUserId == userId)
		$('stop').style.display = "";
	getMsg();
	timer = window.setInterval("getMsg()",1000);
}

function getMsg(){
  var url = contextPath + "/yh/subsys/oa/netmeeting/text/act/YHNetmeetingAct/getMsg.act?seqId=" + <%=seqId%> + "&count=" + count;
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var json = rtJson.rtData;
    var br = ($('content').innerHTML + json.data).split('<BR>');
    if(br.length > 25){
    	var tempStr = "";
   	  for(var i = br.length - 25 ; i < br.length ; i++){
   		  tempStr = tempStr + '<BR>' + br[i];
   	  }
   	  $('content').innerHTML = tempStr;
    }
    else
    	$('content').innerHTML += json.data;
    count = json.count;
    var users = json.user;
    $('user').innerHTML = '';
    for(var i = 0; i < users.length ; i++){
    	var a = new Element('a', {"href" : "javascript:sayTo('" + users[i] + "')"});
    	a.insert(users[i]);
    	$('user').insert(a);
    	$('user').insert(new Element('br'));
    }
    $('userNum').innerHTML = users.length;

    if(json.stop == "stop"){
      window.clearInterval(timer);
      meetingNotStop = false;
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function submitSay(){
	if(meetingNotStop == false){
		alert("会议召集人已经结束会议！");
		return;
	}
	if(($('message').value).trim() == ""){
		alert("发言内容不能为空！");
		return;
	}
	var url = contextPath + "/yh/subsys/oa/netmeeting/text/act/YHNetmeetingAct/setMsg.act?seqId=" + <%=seqId%> 
	+ "&message=" + encodeURIComponent($('message').value) 
	+ "&color=" + $('color').value
	+ "&usernameToSay=" + usernameToSay
	+ "&quiet=" + $('quiet').checked;
	
	$('message').value = "";
	var rtJson = getJsonRs(url);
	if (rtJson.rtState == "0") {
	} else {
	  alert(rtJson.rtMsrg); 
	}
}

function sayTo(username){
	if($('option1'))
	  $('option1').remove();
	var option = new Element('option', {"id" : "option1" , "value" : username});
	option.insert(username);
	$('toId').insert(option);
	$('toId').value = username;
	usernameToSay = username;
}

function changeTo(){
	usernameToSay = $('toId').value;
	if(usernameToSay == 'allUser')
		usernameToSay = "";
}

function history(){
	var URL = "<%=contextPath%>/subsys/oa/netmeeting/text/detail.jsp?subject=<%=subject%>&seqId=<%=seqId%>";
	newWindow(URL,'820', '500');
}

function newWindow(url,width,height){
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  window.open(url, "meeting", 
      "height=" +height + ",width=" + width +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
      + locY + ", left=" + locX + ", resizable=yes");
}

function leaveMeeting(){
	location.href = "<%=contextPath %>/subsys/oa/netmeeting/text/index.jsp";
}

function stopMeeting(){
	if(window.confirm('确认要结束会议吗？\n结束后可以在文本会议管理中继续该会议')){
		window.clearInterval(timer);
	  var url = contextPath + "/yh/subsys/oa/netmeeting/text/act/YHNetmeetingAct/stopMeeting.act?seqId=<%=seqId%>";
	  var rtJson = getJsonRs(url);
	  if (rtJson.rtState == "0") {
		  $('stop').style.display = "none";
		  getMsg();
	  } else {
	    alert(rtJson.rtMsrg); 
	  }
	}
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" style="table-layout: fixed;">
  <tr>
    <td width="80%">
      <img src="<%=imgPath %>/netmeeting.gif" align="middle"><span class="big3"> 会议主题：<%=subject %> </span>&nbsp;&nbsp;
    </td>
    <td width="20%">
      <div style="text-align:center;">
        <b class="big3"> 在线人员： </b><span style="color:red" id="userNum"></span>人&nbsp;&nbsp;
      </div>
    </td>
  </tr>
  <tr>
    <td width="80%">
      <div style="white-space: nowrap; height: 400px; BORDER-RIGHT: #FFFFFF 5px ridge; BORDER-TOP: #FFFFFF 5px ridge; BORDER-LEFT: #FFFFFF 5px ridge; BORDER-BOTTOM: #FFFFFF 5px ridge; overflow-y: hidden; overflow-x: auto" id="content"></div>
    </td>
    <td width="20%">
      <div style="vertical-align: top;text-align:center;height: 400px;" id="user"></div>
    </td>
  </tr> 
  <tr>
    <td width="80%">
      <form name="form1" action="" method="post">
				<table class="TableBlock" width="100%" align="center">
				   <tr class="TableHeader">
				      <td>
				                    对象
				        <select name="toId" id="toId" title="点击右边列表中的名字来指定" onChange="changeTo();" class="smallselect">
				          <option value="allUser">所有人</option>
				        </select>&nbsp;
				                    字色
				        <select name="color" id="color" onChange="document.form1.message.focus();" class="smallselect">
				          <option style="COLOR: #000000" value="#000000" selected>黑色</option>
				          <option style="COLOR: #7EC0EE" value="#7EC0EE" >淡蓝</option>
				          <option style="COLOR: #0088FF" value="#0088FF" >海蓝</option>
				          <option style="COLOR: #0000ff" value="#0000ff" >草蓝</option>
				          <option style="COLOR: #000088" value="#000088" >深蓝</option>
				          <option style="COLOR: #8800FF" value="#8800FF" >蓝紫</option>
				          <option style="COLOR: #AB82FF" value="#AB82FF" >紫色</option>
				          <option style="COLOR: #FF88FF" value="#FF88FF" >紫金</option>
				          <option style="COLOR: #FF00FF" value="#FF00FF" >红紫</option>
				          <option style="COLOR: #FF0088" value="#FF0088" >玫红</option>
				          <option style="COLOR: #FF0000" value="#FF0000" >大红</option>
				          <option style="COLOR: #F4A460" value="#F4A460" >棕色</option>
				          <option style="COLOR: #CC9999" value="#CC9999" >浅褐</option>
				          <option style="COLOR: #888800" value="#888800" >卡其</option>
				          <option style="COLOR: #888888" value="#888888" >铁灰</option>
				          <option style="COLOR: #CCCCCC" value="#CCCCCC" >古黑</option>
				          <option style="COLOR: #90E090" value="#90E090" >绿色</option>
				          <option style="COLOR: #008800" value="#008800" >橄榄</option>
				          <option style="COLOR: #008888" value="#008888" >灰蓝</option>
				        </select>&nbsp;
				        <label for="quiet">悄悄话</label><input type="checkbox" name="quiet" id="quiet" onclick="" >
				     </td>
				  </tr>
				  <tr class="TableHeader">
				     <td>
				                  内容
				        <input type="text" id="message" name="message" size="80" class="smallInput" onkeydown="if(event.keyCode==13){submitSay();return false;}">
				        <input type="button" value="发言" class="smallButton" onclick="submitSay()">&nbsp;&nbsp;&nbsp;
				        <a href="#" onclick="history()")><b><font size='2'>查看历史记录</font></b></a>
				        <input type="hidden" name="MEET_ID" value="6">
				        <input type="hidden" name="TO_NAME" value="">
				        <input type="hidden" name="USER_NAME" value="yyb">
				        <br>
				        <br>
				     </td>
				  </tr>
				</table>
				</form>
    </td>
    <td width="20%">
      <div style="text-align:center;">
	      <input type="button" value="离开会场" class="smallButton" onclick="leaveMeeting()"><br>
	      <input type="button" value="结束会议" class="smallButton" onclick="stopMeeting()" id="stop" name="stop" style="display:none">
      </div>
    </td>
  </tr> 
</table>
<br>

</body>
</html>