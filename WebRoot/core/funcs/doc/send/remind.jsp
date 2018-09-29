<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
String type = request.getParameter("type");
String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信催办</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId %>";
var type = "<%=type %>";
function doInit() {
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocSendAct/getRemindInfo.act";
  var json = getJsonRs(url , "seqId="  + seqId + "&type=" + type);
  if (json.rtState == '0') {
    var userIds = json.rtData.userIds.split(",") ;
    $("toId").value = json.rtData.userIds;
    var userNames = json.rtData.userNames.split(",") ;
    $('content').innerHTML = json.rtMsrg;
    var delImg = '<img src="'+ imgPath +'/remove.png" align="absmiddle" onclick=cancel_user(this)>';
    var userArray = new Array();
    for (var i = 0 ;i < userIds.length ;i ++) {
      if (userIds[i]) 
        userArray[i] = '<span class="underline" id="'+ userIds[i] +'">'+ userNames[i] + delImg +'</span>';
    }
    var userName = userArray.join("");
    $('userNameDiv').update(userName);
    //if (json.rtData.sms2Priv) {
	 // $('sms2RemindDiv').show();
   // }
  }
  $('button').focus();
}
function cancel_user(obj)
{
	var user_str=$("toId").value;
	var user=obj.parentNode.id;
	if(user_str.indexOf(user+",")==0)
      user_str=user_str.replace(user+",","");
    if(user_str.indexOf(","+user+",")>0)
      user_str=user_str.replace(","+user+",",",");
    $("toId").value=user_str;
	if(isMoz) {
	  var obj_parent=obj.parentNode;
	  obj_parent.parentNode.removeChild(obj_parent);
	} else {
	  obj.parentNode.removeNode(true);	
	}
}

function CheckForm() {
  if( !$('toId').present()) {
    alert("收信人不能为空！");
    return (false);
  }

  if(!$('content').present()) { 
    alert("短信内容不能为空！");
    return (false);
  }
  return (true);
}

function CheckSend() {
  if(event.keyCode==10) {
    remind();
  }
}
function remind() {
  if(CheckForm()) {
    url = contextPath + "/yh/core/funcs/doc/send/act/YHDocSendAct/remindUser.act";
    var json = getJsonRs(url , $('smsRemindForm').serialize());
    if (json.rtState == '0') {
	  alert('短信已发送');
    }
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/sms.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 短信催办超时流程</span>
    </td>
  </tr>
</table>
 <form  method="post" name="smsRemindForm" id="smsRemindForm">
 <table border="0" width=500" align=center class="TableList">
    <tr>
      <td class="TableHeader">
      	收信人      </td>
    </tr>
    <tr>
      <td class="TableData" >
        <input type="hidden" id="toId" name="toId" value="">
        <input type="hidden" id="type" name="type" value="<%=type %>">
        <div id="userNameDiv"></div>
      </td>
    </tr>
    <tr>
      <td class="TableHeader">
      	短信内容
      </td>
    </tr>
    <tr>
      <td class="TableData">
        <textarea cols=70 name="content" id="content" rows="8" class="BigInput" wrap="on" onkeypress="CheckSend()"></textarea>
        <div id=sms2RemindDiv style="display:none">
        <input type="checkbox" name="sms2Remind" id="sms2Remind">
        <img src="<%=imgPath %>/mobile_sms.gif"><label for="sms2Remind"><u style="cursor:pointer">同时使用手机短信提醒</u></label>&nbsp;&nbsp;
        </div>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" id="button" onclick="remind()" value="发送短信" class="BigButton">&nbsp;&nbsp;
        <input type="button" id="button" onclick="window.close()" value="关闭" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>

</body>
</html>