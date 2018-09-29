<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.rad.taskmgr.data.YHRadUser" %>
<%@ page import="yh.core.data.YHSysOperator" %>
<%@page import="java.util.List"%>
<%
YHSysOperator opt = (YHSysOperator)session.getAttribute("user");
String userName = opt.getName();
List<YHRadUser> userList = (List<YHRadUser>)session.getAttribute("userList");
%>
<head>
<title>子系统模块索引</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
var userName = "<%=userName%>";
//var rtText = null;
function doInit() {
  if (userName == "yzq") {
    displayModuleTask();
  }else {
    dispTaskOfPerson();
  }
}

/**
 * 显示模块任务列表
 */
function displayModuleTask() {
  switchModuleList(true);
  parent.displayModuleTask();
}
/**
 * 显示某个人的任务 
 */
function dispTaskOfPerson() {
  switchModuleList(true);
  var person = document.getElementById("responsiblePerson").value;
  var includeDoneCntrl = document.getElementById("includeDone");
  var includeCancelCntrl = document.getElementById("includeCancel");
  var includeDone = includeDoneCntrl.checked ? "1" : "0";
  var includeCancel = includeCancelCntrl.checked ? "1" : "0";
  parent.dispTaskOfPerson(person, includeDone, includeCancel);
}
/**
 * 添加顶层模块
 */
function addModule(funcSort) {
  var func = parent.taskPackageIndex[funcSort];
  if (!func) {
    alert("请切换到模块管理");
    return;
  }
  func();
}

var isOpen = true;
function switchModuleList(state) {
  parent.switchModuleList(state);
  if (state === true || state === false) {
    isOpen = state;
  }else {
    isOpen = !isOpen;
  }
  if (isOpen) {
    $("btnSwitch").value = "隐藏";
  }else {
    $("btnSwitch").value = "显示";
  }
}
</script>
</head>
<body onload="doInit();" topmargin="3">
<table>
  <tr>
<%
if (userName.equals("yzq")) {
%>  
    <td>
      <input onclick="displayModuleTask();" class="BigButton" type="button" value="管理"></input>
      <input onclick="addModule('addTopModule');" class="BigButton" type="button" value="新增顶层"></input>
      <input onclick="addModule('addModule');" class="BigButton" type="button" value="新增下级"></input>      
      <input id="manage" onclick="addModule('addTask');" class="BigButton" type="button" value="新增任务"></input>
      <input onclick="addModule('editEntry');" class="BigButton" type="button" value="编辑"></input>
    </td>
<%
}
%>
    <td>
      <input id="btnSwitch" onclick="switchModuleList();" class="BigButton" type="button" value="隐藏"></input>
    </td>
    <td>
               责任人：
     <select name="responsiblePerson" id="responsiblePerson">
<%
  for (YHRadUser tmpUser : userList) {
    String selectStr = "";
    if (userName.equals(tmpUser.getName())) {
      selectStr = "selected";
    }
%>
       <option value="<%=tmpUser.getName() %>" <%=selectStr %>><%=tmpUser.getFullName() %></option>
<%
  }
%>
     </select>
               包含已完成：<input type="checkbox" name="includeDone" id="includeDone" value="1"></input>
               包含已取消：<input type="checkbox" name="includeCancel" id="includeCancel" value="1"></input>
    </td>
    <td>
      <input id="query" onclick="dispTaskOfPerson();" class="BigButton" type="button" value="查询"></input>
    </td>
  </tr>
</table>
</body>
</html>