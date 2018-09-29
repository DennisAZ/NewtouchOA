<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>维度权限设置</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>



</head>

<frameset rows="*"  cols="200,*" frameborder="NO" border="0" framespacing="0" id="frame1">
    <frame name="file_tree" id="file_tree" scrolling="auto" src="left.jsp?seqId=<%=seqId %>" frameborder="yes">
    <frame name="file_main" id="file_main" scrolling="auto" src="setPrivate.jsp?seqId=<%=seqId %>&ownerPriv=1&topSeqId=<%=seqId %>" frameborder="yes">
</frameset>
</html>