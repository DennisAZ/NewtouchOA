<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String basePath = (String)session.getAttribute("basePath");
String basePathWindow = (String)session.getAttribute("basePathWindow");
String taskPath = request.getParameter("taskPath");
String taskFlag = request.getParameter("taskFlag");
%>
<head>
<title>客户端组件库</title>
<script type="text/javascript">
var taskPath = "<%=taskPath%>";
var taskFlag = "<%=taskFlag%>";
/**
 * 显示客户端内容
 */
function dispClient(url) {
  var contentFrame = document.getElementById("contentWindow");
  contentFrame.src = contextPath + url;
}
</script>
</head>
<frameset rows="*"  cols="200, *" frameborder="no" border="0" framespacing="1">
  <frame name="docsEntry" id="docsEntry" scrolling="yes" src="<%=contextPath %>/<%=basePath %>/sys/taskdocentrys.jsp?taskPath=<%=taskPath %>&taskFlag=<%=taskFlag %>" frameborder="1">
  <frame name="contentWindow" id="contentWindow" src="" frameborder="1">
</frameset>
</html>