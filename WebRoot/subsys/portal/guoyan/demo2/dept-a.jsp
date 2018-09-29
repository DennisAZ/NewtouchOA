<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header2.jsp" %>
    <%
    String type = request.getParameter("type").trim();
    String deptName = request.getParameter("deptName");
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>国务院发展研究中心部门专栏</title>
<link href="css.css" rel="stylesheet" type="text/css" />
<link href="style/css/css-other.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/portal.js"></script>
</head>
<body>
<div class="frame-box-two">
<div class="s-left">
   <div class="s-menu">
      <div class="menutit-bg">部门专栏</div>
	  <div class="menu-button1"><a href="modules/bmzl/<%=type %>-bmzz.jsp" target="deptData">部门职责</a></div>
	  <div class="menu-button1"><a href="modules/dept/deptNotifyList.jsp?deptName=<%=deptName %>" target="deptData">部门通知</a></div>
	  <div class="menu-button1"><a href="modules/dept/deptActiveList.jsp?deptName=<%=deptName %>" target="deptData">工作动态</a></div>
   </div>
</div>
</div>
</body>
</html>