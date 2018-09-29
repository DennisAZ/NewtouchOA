<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="yh.rad.taskmgr.data.YHTask" %>
<%
String basePath = (String)session.getAttribute("basePath");
String basePathWindow = (String)session.getAttribute("basePathWindow");
  List<YHTask> taskList = (List)request.getAttribute(YHActionKeys.RET_DATA);
%>
<head>
<title></title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="MyCalendar.js"></script>
<script type="text/javascript">
/**
 * 页面加载处理
 */
function doInit() {
}
/**
 * 显示任务
 */
function displayTask(taskPath) {
  parent.dispClient("/<%=basePath%>/sys/taskindex.jsp?taskPath=" + taskPath + "&taskFlag=1");  
}
</script>
</head>
<body onload="doInit();" topmargin="3">
<table>
<%
for (YHTask task : taskList) {
%>
  <tr>
    <td>
      <a href="javascript:displayTask('<%=task.getTaskPath() %>')"><%=task.getTaskDesc() %></a>
    </td>
  </tr>
<%
}
%>
</table>
</body>
</html>