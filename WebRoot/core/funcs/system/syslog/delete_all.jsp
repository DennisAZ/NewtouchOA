	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<%@ include file="/core/inc/header.jsp" %>
	<%@ page import="yh.core.funcs.person.data.YHPerson" %>
	<%
	YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
	
	%>
	<html>
	<head>
	<title>超时工作列表</title>
	<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
	<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
	<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
	<style>
	.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
	.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
	</style>
	<link rel="stylesheet" href="<%=cssPath %>/page.css">
	<link rel="stylesheet" href ="<%=cssPath %>/style.css">
	<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css">
	<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
  <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
  <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
  <script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
  <script type="text/javascript" src="js/overjs.js"></script>
  <script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
  <script type="text/javascript"><!--
	var isAdmin = <%=loginUser.isAdmin()%>;
	--></script>
	 <%
	 String ref = request.getHeader("REFERER");
	 %>
	</head>
	<body class="bodycolor" topmargin="5" >
	<form  name="queryForm" id="queryForm">
	<%
	  if(loginUser.isAdmin()==false){
	  %>
	  <table width="320" align="center" class="MessageBox">
	   <tr>
	    <td class="msg forbidden">
	      <div style="font-size: 12pt;" class="content">提示,只有admin用户可以结转日志！</div>
	    </td>
	  </tr>
	</table>
	 <%}else if(request.getParameter("right").equals("true")){%>
	<table width="320" align="center" class="MessageBox">
	 <tr>
	    <td class="msg info">
	      <div style="font-size: 12pt;" class="content">选定的日志已清空</div>
	    </td>
	  </tr>
	</table>
	<% }%> 
	<table align="center">
		<tr>
			<td>
				<center>
				  <input type="button" class="BigButton"  value="返回" onclick="javascript:window.location='<%=ref%>'">
				</center>
			</td>
		</tr>
	</table>
	 </form>   
	</body>
	</html>