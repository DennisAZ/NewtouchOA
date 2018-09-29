<%@ page language="java" contentType="text/html; charset=iso-8859-1" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>最近联系人</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=contextPath%>/core/frame/ispirit/n12/org/style/smsbox.css">
<%
 String ISPIRIT= request.getParameter("ISPIRIT");
%>
<style type="text/css">

.TableData sub-module-item{
cursor:pointer;
 
}


#recent_data table tr td{
cursor:pointer;
}
body {
    background-color:#DAEAF2;
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
</style>

<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/frame/ispirit/n12/js/ipanel_org.js" ></script>
<script type="text/Javascript">
var ispirit = "<%=ISPIRIT%>";
function doInit(){
	//var recentUser=getRecentUser();
	 var recentUser=get_im_recent_user();

	getRecentData(recentUser) ;
}

function onClickSentMsg(uid,uname){
	  if(typeof(window.external.OA_SMS) != 'undefined' && window.top.bIMLogin== true){  //在精灵中打开
	      window.external.OA_SMS(uid,uname,"SEND_MSG");
	    }	else{ //在浏览器中打开
	        var url="/yh/core/funcs/message/smsback.jsp?fromId="+uid;
	        window.open (url, 'newwindow', 'height=340, top='+(screen.height-340)/2+',left='+(screen.width-700)/2+', width=700, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=n o, status=no');

	   }
	
	
	
}
/* 
var uid = $(this).attr('uid');
var user_name = $(this).attr('user_name');

parent.send_msg(uid, user_name); */

function getRecentUser(){
	return
	
	return "1,2,3,4,5,6";
}

function getRecentData(recentUser) {
	  var url = "<%=contextPath%>/yh/core/funcs/system/ispirit/n12/org/act/YHIsPiritOrgAct/getModuleData.act";
	  var rtJson = getJsonRs(url,"MODULE=recent&RECENT_USER="+recentUser);
	  if (rtJson.rtState == "0") {
		
     $("recent_data").innerHTML=rtJson.rtData.data;
		  
	  }else {
	    alert(rtJson.rtMsrg); 
	  }
	}

</script>


</head>
<body onLoad="doInit();">
<div id="recent_data"></div>
</body>
</html>