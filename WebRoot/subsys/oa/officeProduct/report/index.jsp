<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>办公用品报表</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>

</head>
<frameset rows="*"  cols="200,*" frameborder="NO" border="0" framespacing="0" id="frame1">
    <frame name="hrmenu" id="hrmenu" scrolling="auto" src="menu.jsp"  frameborder="no">
    <frame name="hrmain" id="hrmain" scrolling="auto" src="index1.jsp?module=OFFICE_WPZB" frameborder="no">
</frameset>