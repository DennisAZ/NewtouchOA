<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String basePath = request.getParameter("basePath");
session.setAttribute("basePath", basePath);
String basePathWindow = basePath.replace("/", "\\");
session.setAttribute("basePathWindow", basePathWindow);
%>
<head>
<title>客户端组件库</title>
<script type="text/javascript">
/**
 * 显示模块任务列表
 */
function displayModuleTask() {
  var moduleTaskCntrl = document.getElementById("taskPackageIndex");
  moduleTaskCntrl.src = contextPath + "/<%=basePath%>/sys/moduletree.jsp";
}
/**
 * 显示功能部件
 */
function dispTaskList(project, taskPackage, task) {
  document.getElementById("topFrameSet").rows = "40, *, 0";
  var taskPackageCntrl = document.getElementById("taskPackageIndex");
  var queryParam = "project=" + project + "&taskPackage=" + taskPackage;
  if (task) {
    queryParam += "&task=" + task;
  }
  taskPackageCntrl.src = contextPath + "/<%=basePath%>/sys/taskList.jsp?" + queryParam;
}
/**
 * 显示客户端内容
 */
function dispClient(url) {
  var contentFrame = document.getElementById("clientWindow");
  contentFrame.src = contextPath + url;
}

/**
 * 显示某个人的任务列表
 */
function dispTaskOfPerson(person, includeDone, includeCancel) {
  var contentFrame = document.getElementById("taskPackageIndex");
  var queryParam = "responsiblePerson=" + person + "&includeDone=" + includeDone + "&includeCancel=" + includeCancel;
  contentFrame.src = contextPath + "/yh/rad/taskmgr/act/YHTaskAct/loadTaskList.act?" + queryParam;
}

var isOpen = true;
function switchModuleList(state) {
  if (state === true || state === false) {
    isOpen = state;
  }else {
    isOpen = !isOpen;
  }
  var contentFrameSet = document.getElementById("contentFrameSet");
  if (isOpen) {
    contentFrameSet.cols = "250,*";
  }else {
    contentFrameSet.cols = "0,*";
  }
}
</script>
</head>
<frameset name="topFrameSet" id="topFrameSet" cols="*"  rows="40, *, 0" frameborder="no" border="0" framespacing="1">
  <frame name="projectTaskpackage" id="projectTaskpackage" scrolling="no" src="<%=contextPath %>/<%=basePath %>/sys/toolbar.jsp" frameborder="0">
  <frameset name="contentFrameSet" id="contentFrameSet" rows="*"  cols="250, *" frameborder="no" border="0" framespacing="1">
    <frame name="taskPackageIndex" id="taskPackageIndex" scrolling="yes" src="" frameborder="1">
    <frame name="clientWindow" id="clientWindow" scrolling="yes" src="/<%=contextPath %>/core/inc/empty.html" frameborder="1">
  </frameset>
  <frame name="clientFrame" id="clientFrame" scrolling="no" src="" frameborder="0">
</frameset>
</html>