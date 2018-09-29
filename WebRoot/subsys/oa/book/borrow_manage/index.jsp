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
	<title>借还书管理</title>
	<script type="text/javascript">
	var json = [
	  {title:"借书管理", contentUrl:contextPath + "/yh/subsys/oa/book/act/YHBookRuleAct/index.act",  useIframe:true}
	   ,{title:"还书管理", contentUrl:contextPath + "/subsys/oa/book/borrow_manage/borrow/query.jsp?flag=1", useIframe:true}
	  ,{title:"历史记录查询", contentUrl:contextPath + "/subsys/oa/book/borrow_manage/borrow/query.jsp?flag=0", useIframe:true}	  
	];
	function doInit(){
	  buildTab(json, 'books');
	}
	</script>
	</head>
		<body onload="doInit()">
		 <div id = "books"></div>
		</body>
	</html>