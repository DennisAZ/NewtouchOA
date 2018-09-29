	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<%@ include file="/core/inc/header.jsp" %>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
	<link rel="stylesheet" href = "<%=cssPath%>/style.css">
	<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/syslog/js/sysyearlog.js"></script>
	<title>系统日志</title>
	<script type="text/javascript">
	var jso = [
	  {title:"日志概况", contentUrl:"<%=contextPath %>/core/funcs/system/syslog/logsituation.jsp",  useIframe:true}
	   ,{title:"年度数据", contentUrl:"<%=contextPath %>/core/funcs/system/syslog/year.jsp", useIframe:true}
	   ,{title:"时段统计", contentUrl:"<%=contextPath %>/core/funcs/system/syslog/hour.jsp",  useIframe:true}
	  // ,{title:"时段统计", contentUrl:"<%=contextPath %>/core/funcs/system/syslog/manager.jsp",  useIframe:true}
	  ,{title:"日志管理", contentUrl:"<%=contextPath %>/yh/core/funcs/system/syslog/act/YHSysLogSaveAct/getsysradio.act", useIframe:true}
	  
	];
	function doInit(){
	  buildTab(jso, 'syslog');
	}
	</script>
	</head>
		<body onload="doInit()">
		 <div id = "syslog"></div>
		</body>
	</html>