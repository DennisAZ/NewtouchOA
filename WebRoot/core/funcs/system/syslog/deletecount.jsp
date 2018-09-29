	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<%@ page import="yh.core.funcs.person.data.YHPerson" %>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<%
	YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
	%>
	<html>
	<head>
	<%@ include file="/core/inc/header.jsp" %>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
	<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
	<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
	<link rel="stylesheet" href = "<%=cssPath%>/style.css">
	<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
	<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
	<title>系统日志查询</title>
	</head>
	<body class="bodycolor" topmargin="5">
		<br><br>
		<%
		  if(!loginUser.isAdmin() && !loginUser.isAdminRole()){
		%>
		 <table width="320" align="center" class="MessageBox">
		 <tr>
		    <td class="msg forbidden">
		      <div style="font-size: 12pt;" class="content">提示,只有admin用户可以删除日志！</div>
		    </td>
		  </tr>
		</table>
		<%  }else{
		%>
		<table class="MessageBox" align="center" width="330">
		  <tr>
		    <td class="msg info">
		      <div class="content" style="font-size:12pt">共删除 <%=request.getParameter("cou") %> 条日志记录</div>
		    </td>
		  </tr>
		</table>
		 <% }%>
		<br>
		<table align="center">
			<tr>
				<td>
					<center>
					  <input type="button" class="BigButton"  value="返回" onclick="javascript:window.location='<%=contextPath %>/yh/core/funcs/system/syslog/act/YHSysLogSaveAct/getsysradio.act'">
					</center>
				</td>
			</tr>
		</table>
	</body>
	</html>