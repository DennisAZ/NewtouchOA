<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.rad.taskmgr.data.YHRadUser" %>
<%@ page import="yh.core.data.YHSysOperator" %>
<%@page import="java.util.List"%>
<%
String basePath = (String)session.getAttribute("basePath");
String basePathWindow = (String)session.getAttribute("basePathWindow");
YHSysOperator opt = (YHSysOperator)session.getAttribute("user");
String userName = opt.getName();
List<YHRadUser> userList = (List<YHRadUser>)session.getAttribute("userList");

String taskPath = request.getParameter("taskPath");
String currDate = YHUtility.getCurDateTimeStr();
currDate = currDate.substring(0, 10);
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
var taskPath = "<%=taskPath%>";
var currDate = "<%=currDate%>";
/**
 * 执行保存
 */
function doSave() {
  if (!document.getElementById("taskDesc").value) {
    alert("请填写说明");
    return;
  }
  if (!document.getElementById("startDate").value) {
    alert("请填写启动日期");
    return;
  }
  if (!document.getElementById("workloads").value) {
    alert("请填写工作量");
    return;
  }
  var rtJson = getJsonRs("<%=contextPath%>/yh/rad/taskmgr/act/YHTaskAct/saveTask.act", $("form1").serialize());
  alert(rtJson.rtMsrg);
}
/**
 * 页面加载处理
 */
function doInit() {
  var rtJson = getJsonRs("<%=contextPath%>/yh/rad/taskmgr/act/YHTaskAct/getTask.act", "taskPath=" + taskPath);
  if (rtJson.rtState == "0") {
    bindJson2Cntrl(rtJson.rtData);
    var cntrl = document.getElementById("startDate");
    if (!cntrl.value) {
      cntrl.value = currDate;
    }
    cntrl = document.getElementById("workloads");
    if (!cntrl.value) {
      cntrl.value = 1;
    }
  }else {
    alert(rtJson.rtMsrg);
  }
}
</script>
</head>
<body onload="doInit();" topmargin="3">
<form name="form1" name="form1">
<input type="hidden" name="taskPath" id="taskPath" value="<%=taskPath %>"></input>
<input type="hidden" name="dtoClass" id="dtoClass" value="yh.rad.taskmgr.data.YHTask"></input>
<table>
  <tr>
    <td>
              说明
    </td>
    <td>
      <input name="taskDesc" id="taskDesc" type="text" value="" size="50"></input>
    </td>
  </tr>
  <tr>
    <td>
              起始日期
    </td>
    <td>
      <input name="startDate" id="startDate" type="text" value="" onclick="new MyCalendar(event,this)" onfocus="this.select()" readonly="readonly"></input>
    </td>
  </tr>  
  <tr>
    <td>
              责任人
    </td>
    <td>
      <select name="responsiblePerson" id="responsiblePerson">
<%
  for (YHRadUser tmpUser : userList) { 
%>
       <option value="<%=tmpUser.getName() %>"><%=tmpUser.getFullName() %></option>
<%
  }
%>
      </select>
    </td>
  </tr>
  <tr>
    <td>
             工作量（天）
    </td>
    <td>
      <input name="workloads" id="workloads" type="text" value="" size="10"></input>
    </td>
  </tr>
  <tr>
    <td>
             任务类别
    </td>
    <td>
      <select name="taskSort" id="taskSort">
        <option value="0">学习培训，态度端正</option>
        <option value="A">事务工作，踏实执行</option>
        <option value="B">技术开发，完成目标符合规范</option>
        <option value="C">研究实用化，热情能力</option>
        <option value="D">突破难点，激情灵感</option>
        <option value="E">执行任务，多方协调</option>
        <option value="F">设计系统，系统工程</option>
        <option value="G">规划方向，整体负责</option>
        <option value="H">教化掌舵，精神领袖</option>
      </select>
    </td>
  </tr>
  <tr>
    <td>
               功能部件
    </td>
    <td>
      <input name="funcsCmp" id="funcsCmp" type="text" value="" size="50"></input>
    </td>
  </tr>
  <tr>
    <td>
              状态
    </td>
    <td>
      <select name="state" id="state">
        <option value="1">启动</option>
        <option value="2">执行</option>
        <option value="9">完成</option>
        <option value="Z">取消</option>
      </select>
    </td>
  </tr>
  <tr>
    <td>
      &nbsp;
    </td>
    <td>
      &nbsp;
    </td>
  </tr>
</table>
<input onclick="doSave();" type="button" value="保存"></input>
</form>
</body>
</html>