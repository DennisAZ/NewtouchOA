<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String basePath = (String)session.getAttribute("basePath");
String basePathWindow = (String)session.getAttribute("basePathWindow");
%>
<head>
<title>子系统模块索引</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/tree.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="DTreeold.js" ></script>
<script type="text/javascript">
var currId = null;
var isDetl = false;

/**
 * 是否是任务
 */
function isTask(nodeId) {
  var imgCntrl = $("img1-" + nodeId);
  if (imgCntrl) {
    return false;
  }
  return true;
}
/**
 * 处理单击节点
 */
function doClickNode(nodeId) {
  if (currId != nodeId) {
    if (currId) {
      //document.getElementById("a-" + currId).style.backgroundColor = "#FFFFFF";
    }
    //document.getElementById("a-" + nodeId).style.backgroundColor = "#CCDADF";
  }
  currId = nodeId;
  var taskFlag = isTask(nodeId);
  isDetl = taskFlag;
  parent.dispClient("/<%=basePath%>/sys/taskindex.jsp?taskPath=" + nodeId + "&taskFlag=" + (taskFlag ? "1" : "0"));  
}
function doInit() {
  new MyTreeInit('di',
      '<%=contextPath%>/yh/rad/taskmgr/act/YHTaskAct/getTree.act?id=',
      {isNoTree:false},
      false,
      {isHaveCheckbox:false},
      {clickFunc:doClickNode,isHaveLink:false,linkAddress:'<%=contextPath%>/',target:'_blank'});
}
/**
 * 新增加模块
 */
function addTopModule() {
  parent.dispClient("/<%=basePath%>/sys/moduleinput.jsp");
}
/**
 * 新增加模块
 */
function addModule() {
  if (!currId) {
    alert("请选择上级模块");
    return;
  }
  if (isDetl) {
    alert("当前节点是任务,请选择模块");
    return;
  }
  parent.dispClient("/<%=basePath%>/sys/moduleinput.jsp?parentPath=" + currId);
}
/**
 * 新增加任务
 */
function addTask() {
  if (!currId) {
    alert("请选择上级模块");
    return;
  }
  if (isDetl) {
    alert("当前节点是任务,请选择模块");
    return;
  }
  parent.dispClient("/<%=basePath%>/sys/moduleinput.jsp?parentPath=" + currId + "&isDetl=1");
}
/**
 * 编辑
 */
function editEntry() {
  if (!currId) {
    alert("请选择模块");
    return;
  }
  var tmpIndex = currId.lastIndexOf(".");
  var parentPath = currId.substring(0, tmpIndex);
  parent.dispClient("/<%=basePath%>/sys/moduleinput.jsp?parentPath="
      + parentPath + "&taskPath=" + currId);
}
</script>
</head>
<body onload="doInit();" topmargin="3">
<div id="di"></div>
</body>
</html>