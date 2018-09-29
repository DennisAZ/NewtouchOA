<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">


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
             {title:"人员考勤记录", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/core/funcs/attendance/manage/attendance.jsp" ,useIframe:true}
             ,{title:"上下班登记统计", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/core/funcs/attendance/manage/statisticindex.jsp" ,useIframe:true}
             //,{title:"人员年休假天数设定", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/custom/attendance/attendmanage/user_annualleave/index.jsp" ,useIframe:true}
              ];

  var weeks = ["周日","周一","周二","周三","周四","周五","周六"]; 
  buildTab(jso, 'contentDiv', 800);
}

</script>
</head>
<body onload="doInit()">
<div id="contentDiv"></div>

</body>
</html>