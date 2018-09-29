	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8" isELIgnored="false"%>
	<%@ include file="/core/inc/header.jsp" %>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href = "<%=cssPath%>/style.css">
	<link rel="stylesheet" href = "<%=cssPath%>/style.css">
	<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/syslog/js/syslog.js"></script>
	<title>Insert title here</title>
	<script type="text/javascript">
	 function doInit(){
	   var queryParam = $("form1").serialize();
	   doInit3();
	 }
	</script>
	</head>
	<body onload="doInit()">
	
	<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
	  <tr>
	    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 日志概况</span>
	    </td>
	  </tr>
	</table>
	<form name="form1" id="form1">
	<table class="TableBlock" width="70%" align="center">
		<tr>
		   <td nowrap class="TableHeader" colspan="2">日志概况</td>
		</tr>
	
		<tr height="20">
			<td nowrap class="TableData" width="100" >总统计天数：</td>
			<td class="TableData" align="left" id=" str"></td>
		</tr>
		<tr height="20">
			<td nowrap class="TableData" width="100">总访问量：</td>
			<td class="TableData" align="left" id=" str1"> </td>
		</tr>
		<tr height="20">
			<td nowrap class="TableData" width="100">今年访问量：</td>
			<td class="TableData" align="left" id=" str2"> </td>
		</tr>
		
		<tr height="20">
			<td nowrap class="TableData" width="100">本月访问量:</td>
			<td class="TableData" align="left" id=" str3"></td>
		</tr>
		<tr height="20">
			<td nowrap class="TableData" width="100">今日访问量：</td>
			<td class="TableData" align="left" id=" str4"> </td>
		</tr>
		<tr height="20">
			<td nowrap class="TableData" width="100">平均每日访问量：</td>
			<td class="TableData" align="left" id=" str5"></td>
		</tr>
	</table>
	</form>
	<br>
	<table class="TableList" width="70%" align="center">
	  <thead>
	   <tr class="TableHeader">
	      <td nowrap colspan="5" align="center">最近10条日志</td>
	    </tr>
    </thead>
	  <tbody id="dataBody"></tbody>
	</table>
	</body>
	</html>