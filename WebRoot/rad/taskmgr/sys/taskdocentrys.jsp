<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="yh.core.data.YHProps"%>
<%
String basePath = (String)session.getAttribute("basePath");
String basePathWindow = (String)session.getAttribute("basePathWindow");
String taskPath = request.getParameter("taskPath");
String taskFlag = request.getParameter("taskFlag");
String entryPropsUrl = null;
entryPropsUrl = "/" + basePath + "/sys/moduleinput.jsp?taskPath=" + taskPath;
int tmpIndex = taskPath.lastIndexOf(".");
String parentPath = null;
if (tmpIndex > 0) {
  parentPath = taskPath.substring(0, tmpIndex);
  entryPropsUrl += "&parentPath=" + parentPath;
}
String propsUrl = "/" + basePath + "/sys/taskbaseprops.jsp";
propsUrl += "?taskPath=" + taskPath;

int idIndex = 0; 
String infoPath = fullContextPath + basePathWindow + "\\" + taskPath.replace(".", "\\") + "\\info.text";
YHProps infoProps = new YHProps();
infoProps.loadProps(infoPath);
String entryDesc = infoProps.get("entryDesc");
%>
<head>
<title>任务列表</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
var taskPath = "<%=taskPath%>";
var taskFlag = "<%=taskFlag%>";

/**
 * 页面加载初始化
 */
function doInit() {
  nev("requirementsFunc");
}
/**
 * 页面导航
 */
function nev(action) {
  var url = null;
  if (action.indexOf("/") < 0) {
    url = "/core/funcs/webinfo/browser.jsp?filePath=<%=basePath%>/" + "<%=taskPath.replace(".", "/")%>" + "/docs/" + action;
  }else {
    url = action;
  }
  parent.window.dispClient(url);
}

var selectedId = "";
function doClick(obj) {
  if (selectedId == obj.id) {
    return;
  }
  if (selectedId) {
    document.getElementById(selectedId).style.backgroundColor = "#FFFFFF";
  }
  selectedId = obj.id;
  document.getElementById(selectedId).style.backgroundColor = "#CCDADF";
}

var isDispAdvanced = false;
function switchAdvanced() {
  isDispAdvanced = !isDispAdvanced;
  var cntrl = document.getElementById("advancedEntrys");
  var linkCntrl = document.getElementById("advancedHref");
  if (isDispAdvanced) {
    cntrl.style.display = "block";
    linkCntrl.innerHTML = "隐藏高级&lt;&lt;";
  }else {
    cntrl.style.display = "none";
    linkCntrl.innerHTML = "显示高级&gt;&gt;";
  }
}
</script>
</head>
<body onload="doInit();" topmargin="3">
<table class="lineTable" width="100%" cellspacing="1" cellpadding="3">
  <tr>
    <td class="big3" align="center" style="background-color:#CCCCCC">
      <%=entryDesc %>
    </td>
  </tr>  
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('requirementsFunc')">功能需求说明</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('wokplan')">任务执行计划</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('problems')">难点问题记录</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('worklog')">工作日志</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('advices')">重要回复</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('/core/funcs/smartform/index.jsp?formTemplt=<%=basePath %>/sys/formtmplt/check.jsp&formDataPath=<%=basePath %>/<%=taskPath.replace(".", "/") %>/docs/check')">阶段性确认</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('selftest')">测试Bug记录</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('/core/funcs/smartform/index.jsp?formTemplt=<%=basePath %>/sys/formtmplt/confirm.jsp&formDataPath=<%=basePath %>/<%=taskPath.replace(".", "/") %>/docs/confirm')">确认验收</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('acceptance')">验收报告</a>
    </td>
  </tr>
</table>
<table class="lineTable" width="100%" cellspacing="1" cellpadding="3">
  <tr>
    <td class="big3" align="center" style="background-color:#CCCCCC">
      <a id="advancedHref" href="javascript:switchAdvanced();">
                     显示高级>>
      </a>
    </td>
  </tr>
</table>
<table id="advancedEntrys" class="lineTable" style="display:none;" width="100%" cellspacing="1" cellpadding="3">
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('<%=propsUrl %>')">管理注册属性</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('requirementsNonFuncs')">非功能需求</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('roles')">角色</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('entity')">关注实体</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('refrences')">引用外系统的服务</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('/rad/devmgr/sys/webclient.jsp')">Web客户端</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('webinterface')">普通Web接口</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('messages')">发送的消息</a>
    </td>
  </tr>
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('eventinterface')">事件响应接口</a>
    </td>
  </tr>  
  <tr>
    <td id="link_<%=idIndex++ %>" onclick="doClick(this);">
      <a href="javascript:nev('webservices')">Web服务接口</a>
    </td>
  </tr>
</table>
</body>
</html>