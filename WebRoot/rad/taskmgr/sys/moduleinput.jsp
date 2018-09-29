<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="yh.core.data.YHProps"%>
<%@page import="java.io.File"%>
<%@page import="yh.rad.taskmgr.util.YHTaskUtility"%>
<%
String basePath = (String)session.getAttribute("basePath");
String basePathWindow = (String)session.getAttribute("basePathWindow");
String taskPath = YHUtility.null2Empty(request.getParameter("taskPath"));
String parentPath = YHUtility.null2Empty(request.getParameter("parentPath"));
String isDetl = YHUtility.null2Empty(request.getParameter("isDetl"));
String parentDesc = null;
String currDir = null;
String currDesc = null;
String currSortNo = null;
String currDetlFlag = isDetl;

int dirCnt = 0;
if (parentPath.length() < 1) {
  parentDesc = "无";
  dirCnt = YHTaskUtility.getModuleCnt(fullContextPath + basePathWindow);
}else {
  String infoPath = fullContextPath + basePathWindow + "\\" + parentPath.replace(".", "\\") + "\\info.text";
  YHProps infoProps = new YHProps();
  infoProps.loadProps(infoPath);
  parentDesc = infoProps.get("entryDesc");
  dirCnt = YHTaskUtility.getModuleCnt(fullContextPath + basePathWindow + "\\" + parentPath.replace(".", "\\"));
}
if (taskPath.length() < 1) {
  currDir = "";
  currDesc = "";
  currSortNo = String.valueOf((dirCnt / 100 + 1)) + YHUtility.getFixLengthStringFront(String.valueOf(dirCnt * 5), 2);
}else {
  int tmpIndex = taskPath.lastIndexOf(".");
  if (tmpIndex > 0) {
    currDir = taskPath.substring(tmpIndex + 1);
  }else {
    currDir = taskPath;
  }
  String infoPath = fullContextPath + basePathWindow + "\\" + taskPath.replace(".", "\\") + "\\info.text";
  YHProps infoProps = new YHProps();
  infoProps.loadProps(infoPath);
  currDesc = infoProps.get("entryDesc");
  currSortNo = infoProps.get("sortNo");
  currDetlFlag = YHUtility.null2Empty(infoProps.get("isDetl"));
  if (currSortNo == null) {
    currSortNo = "";
  }
}
String isUpdate = "0";
String disabledStr = "";
if (taskPath.length() > 0) {
  disabledStr = "readonly";
  isUpdate = "1";
}
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
/**
 * 执行保存
 */
function doSave() {
  if (!document.getElementById("entryDir").value) {
    alert("请填写入口路径");
    return;
  }
  if (!document.getElementById("entryDesc").value) {
    alert("请填写入口描述");
    return;
  }
  var rtJson = getJsonRs("<%=contextPath%>/yh/rad/taskmgr/act/YHTaskAct/saveModule.act", $("form1").serialize());
  alert(rtJson.rtMsrg);
}
/**
 * 页面加载处理
 */
function doInit() {
  if (!taskPath) {
    return;
  }
}
</script>
</head>
<body onload="doInit();" topmargin="3">
<form name="form1" name="form1">
<input type="hidden" name="dtoClass" id="dtoClass" value="yh.rad.taskmgr.data.YHModule"></input>
<input type="hidden" name="parentPath" id="parentPath" value="<%=parentPath %>"></input>
<input type="hidden" name="isUpdate" id="isUpdate" value="<%=isUpdate %>"></input>
<table>
  <tr>
    <td>
             上级模块
    </td>
    <td>
      <%=parentDesc %>
    </td>
  </tr>
  <tr>
    <td>
              入口路径
    </td>
    <td>
      <input name="entryDir" id="entryDir" type="text" value="<%=currDir %>" size="50" <%=disabledStr %>></input>
    </td>
  </tr>
  <tr>
    <td>
               入口描述
    </td>
    <td>
      <input name="entryDesc" id="entryDesc" type="text" value="<%=currDesc %>" size="50"></input>
    </td>
  </tr>
  <tr>
    <td>
               是否明细
    </td>
    <td>
      <select name="isDetl" id="isDetl">
        <option value="0" <%=currDetlFlag.equals("0") ? "selected" : "" %>>否</option>
        <option value="1" <%=currDetlFlag.equals("1") ? "selected" : "" %>>是</option>
      </select>
    </td>
  </tr>
  <tr>
    <td>
               排序号
    </td>
    <td>
      <input name="sortNo" id="sortNo" type="text" value="<%=currSortNo %>" size="3" maxlength="3"></input>
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