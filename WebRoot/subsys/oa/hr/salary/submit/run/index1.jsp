<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
String flowId = request.getParameter("flowId");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工资代办流程</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
var flowId = "<%=flowId%>";
</script>
</head>
<frameset cols="200,*" frameborder="0" border="0">  
  <frame name="menu" scrolling="no" src="<%=contextPath%>/subsys/oa/hr/salary/submit/run/leftTree2.jsp?flowId=<%=flowId %>" scrolling="auto" noresize/>
  <frame name="addressmain" src="<%=contextPath%>/subsys/oa/hr/salary/submit/run/blank.jsp" scrolling="auto"/>
</frameset>
</html>


