<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<% 
String MSG_GROUP_ID=request.getParameter("MSG_GROUP_ID");

%>
<html>
<head>
<title></title>

<script type="text/javascript" src="<%=contextPath%>/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath%>/core/js/jquery/yh/core/js/jquery/jquery.min1.6.2.js"></script>

<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=contextPath%>/core/frame/ispirit/n12/org/style/smsbox.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="js/group_msg_list.js"></script>
<script src="js/group_sms.js"></script>
<script type="text/javascript">
var msgGroupId = "<%=MSG_GROUP_ID%>";
var userName =getUserName();
function getUserName(){
	var url=contextPath + "/yh/core/funcs/system/ispirit/n12/org/act/YHIsPiritOrgAct/getUserName.act";
	   var json = getJsonRs(url);
	   if(json.rtState=="0"){
	     return json.rtData.data;
	   }else{
	     return "";
	   }
}

</script>
</head>
<body style=" background-color:#E8EBF2; " >
<a id="bottom1" href="#bottom"></a>
<div id="container"></div>
<div style="padding-bottom:10px;"></div>
<a name="bottom"></a>
<script>
var obj_a = document.getElementById("bottom1");
if(document.all) //for IE
   obj_a.click();
else if(document.createEvent){ //for FF
	 var ev = document.createEvent('HTMLEvents');
   ev.initEvent('click', false, true);
   obj_a.dispatchEvent(ev);
}
</script>
<iframe style="width: 0px; height: 0px; visibility: hidden;" id="empty" name="empty"></iframe>
<form id='form1' name='form1' action='<%=contextPath%>/yh/core/funcs/system/ispirit/n12/group/act/YHImGroupAct/groupMsgSend.act' target="empty" method='post' enctype='multipart/form-data'>
  <input type='hidden' name='MSG_GROUP_ID' id='MSG_GROUP_ID' value="<%=MSG_GROUP_ID%>"/>
  <textarea style='display:none' type='hidden' name='MSG_CONTENT' id='MSG_CONTENT'></textarea>
  <input style='display:none' type="file" name="ATTACHMENT_0" id="ATTACHMENT_0" size="1" hideFocus="true" />
</form>
</body>
</html>