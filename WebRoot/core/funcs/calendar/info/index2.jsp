<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  //日安排
  String userId = request.getParameter("userId");
  String year = request.getParameter("year");
  String month = request.getParameter("month");
  String day = request.getParameter("day");
  String userIds = request.getParameter("userIds");
  String deptId = request.getParameter("deptId");
  String dwm = request.getParameter("dwm");
  
  //周安排查询  year userId deptId dwm
  String date1 = request.getParameter("date1");
  String date7 = request.getParameter("date7");
  String week = request.getParameter("week");
  
  String date = request.getParameter("date");
%>
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
  var userId = '<%=userId%>';
  var year = '<%=year%>';
  var month = '<%=month%>';
  var day = '<%=day%>';
  var userIds = '<%=userIds%>';
  var dwm = '<%=dwm%>';
  var date1 = '<%=date1%>';
  var date7 = '<%=date7%>';
  var week = '<%=week%>';
  var date = '<%=date%>';
  var jso = [
             {title:"日常事务", useTextContent:false , contentUrl:"<%=contextPath %>/core/funcs/calendar/info/newcalendar.jsp?userId="+userId+"&year="+year+"&month="+month+"&day="+day+"&userIds="+userIds+"&dwm="+dwm+"&date1="+date1+"&date7="+date7+"&week="+week+"&date="+date , imgUrl:"<%=imgPath%>/email_close.gif",useIframe:true}            
             ,{title:"周期性事务", useTextContent:false , contentUrl:"<%=contextPath %>/core/funcs/calendar/info/newaffair.jsp?userId="+userId+"&year="+year+"&month="+month+"&day="+day+"&userIds="+userIds+"&dwm="+dwm+"&date1="+date1+"&date7="+date7+"&week="+week+"&date="+date  , imgUrl:"<%=imgPath%>/email_open.gif",useIframe:true}            
             ,{title:"任务", useTextContent:false , contentUrl:"<%=contextPath %>/core/funcs/calendar/info/newtask.jsp?userId="+userId+"&year="+year+"&month="+month+"&day="+day+"&userIds="+userIds+"&dwm="+dwm+"&date1="+date1+"&date7="+date7+"&week="+week+"&date="+date , imgUrl:"<%=imgPath%>/edit.gif",useIframe:true}            
             ];

  buildTab(jso, 'contentDiv', 600);
}
</script>
</head>
<body onload="doInit()">
<div id="contentDiv"></div>

</body>
</html>