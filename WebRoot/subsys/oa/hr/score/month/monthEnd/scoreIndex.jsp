<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
  String flowId = request.getParameter("flowId") == null ? "" :  request.getParameter("flowId");
  String groupId = request.getParameter("groupId") == null ? "" :  request.getParameter("groupId");
  String userId = request.getParameter("userId") == null ? "" :  request.getParameter("userId");
  String groupFlag = request.getParameter("groupFlag") == null ? "" :  request.getParameter("groupFlag");
  String checkFlag = request.getParameter("checkFlag") == null ? "" :  request.getParameter("checkFlag");
  String month = request.getParameter("month") == null ? "" :  request.getParameter("month");
  String year = request.getParameter("year") == null ? "" :  request.getParameter("year");
  String seqId = "";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>月考核</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript">

</script>
</head>
<frameset rows="*"  cols="180,*" frameborder="NO" border="0" framespacing="0" id="frame1">
    <frame name="file_tree" id="file_tree" scrolling="auto" src="left.jsp?flowId=<%=flowId %>&userId=<%=userId %>&checkFlag=<%=checkFlag %>&groupId=<%=groupId %>&year=<%=year %>&month=<%=month %> "  frameborder="no">
    <frame name="hrms" id="hrms" scrolling="auto" src="quizList.jsp?flowId=<%=flowId %>&userId=<%=userId %>&currPage=1&checkFlag=<%=checkFlag %>&groupId=<%=groupId %>&year=<%=year %>&month=<%=month %>" frameborder="no">
</frameset>