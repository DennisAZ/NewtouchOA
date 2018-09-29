<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">


<style type="text/css">

.clearfix {
display:block;
margin:0;
padding:0;
bottom:0;
position:fixed;
width:100%;
z-index:88888;
}

.m-chat .chatnote {
float:left;
height:25px;
line-height:25px;
position:absolute;
}
.m-chat {
-moz-background-clip:border;
-moz-background-inline-policy:continuous;
-moz-background-origin:padding;
background:transparent url(http://xnimg.cn/imgpro/chat/xn-pager.png) repeat-x scroll 0 -396px;
border-left:1px solid #B5B5B5;
display:block;
height:25px;
margin:0 15px;
position:relative;
}
.operateLeft{
	float: left;
	font-size: 12px;
	text-align: right;
	width: 80px;
	border-right-width: 1px;
	border-right-style: solid;
	border-right-color: #999;
}
.operateRight{
	float: left;
	font-size: 12px;
	text-align: left;

}


</style>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript">
function doInit() {
  var jso = [
             {title:"个人考勤统计", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/custom/attendance/personal/statitic/index.jsp" ,useIframe:true}
             ,{title:"加班登记", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/custom/attendance/personal/overtime/index.jsp" , useIframe:true}
             ,{title:"请假登记", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/custom/attendance/personal/leave/index.jsp" ,useIframe:true}
             ,{title:"年休假登记", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/custom/attendance/personal/annualleave/index.jsp" , useIframe:true}
             ];

  
  buildTab(jso, 'contentDiv', 800);
}
function openDesign(){ 
  //alert('icon' + dd.extData);
 window.open("../flowdesign/index.jsp?flowId=<%=flowId%>","flow_design","height=600,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=10,left=10,resizable=yes");
}
</script>
</head>
<body onload="doInit()">
<div id="contentDiv"></div>

</body>
</html>