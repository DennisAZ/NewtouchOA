<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String sortName = request.getParameter("sortName");
if (sortName == null) {
  sortName = "";
}
String skin = request.getParameter("skin");
String skinJs = "messages";
if (skin != null && !"".equals(skin)) {
  skinJs = "messages_" + skin;
} else {
  skin = "";
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/flowrun/log/js/flowRunLog.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/skin.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
var sortName = "<%=sortName%>";
function doInit(){
  skinObjectToSpan(flowrun_log_index);
  if (sortName) {
    sortId = getSortIdsByName(sortName);
    $('sortId').value = sortId;
  }
  loadFlowType("flowId");
  var beginParameters = {
      inputId:'startTime',
      property:{isHaveTime:true}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endTime',
      property:{isHaveTime:true}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);
}
</script>
<title>工作流日志查询</title>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3"> 流程日志查询</span>
    </td>
  </tr>
</table>
<form id="form1" name="form1">
<input type=hidden value="<%=skin %>" name="skin" id="skin"/>
<input type=hidden value="<%=sortId %>" name="sortId" id="sortId"/>
<table width="500px"  class="TableList" align="center" >
   <tr>
    <td nowrap class="TableContent">选择流程：</td>
    <td nowrap class="TableData">
    <select name="flowId" id="flowId" style="width:200px">
      <option value="">全部流程</option>
     </select>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent"> 流水号：</td>
    <td nowrap class="TableData">
    <input type="text" class="SmallInput" size=10  name="runId" >
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent"><span id="span1">工作名称/文号：</span> </td>
    <td nowrap class="TableData">
    <input type="text" class="SmallInput" size=20  name="runName">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent"> 处理人：</td>
    <td nowrap class="TableData">
      <input type="text" class="SmallInput" size=20 readOnly id="userName">
      <INPUT type="hidden" name="userId" id="userId">
      <A class=orgAdd onclick="selectSingleUser(['userId' , 'userName'])" href="javascript:;">添加</A> 
      <a href="#" class="orgClear" onClick="Clear('userId','userName')">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent"> IP地址：</td>
    <td nowrap class="TableData">
    <input type="text" class="SmallInput" size=20 id="ipAddrss"  name="ipAddrss">
    </td>
  </tr>
  <tr>
      <td nowrap class="TableContent"> 发生时间范围：</td>
      <td nowrap class="TableData">
        从 ：<input type="text" name="startTime" id="startTime" size="20" maxlength="20" class="BigInput" value="">
        <img src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" id="beginDateImg">
        <br>至： <input type="text" name="endTime"  id="endTime" size="20" maxlength="20" class="BigInput" value="">
        <img src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" id="endDateImg">
      </td>
  </tr>
  <tr>
      <td nowrap class="TableContent"> 日志类别：</td>
      <td nowrap class="TableData">
        <select name="logType">
          <option value="">所有日志</option>
          <option value=1>转交日志</option>
          <option value=2>委托日志</option>
          <option value=3>删除日志</option>
          <option value=4>销毁日志</option>
          <option value=5>还原日志</option>
          <option value=6>修改日志</option>
          <option value=7>其它日志</option>
        </select>
      </td>
  </tr>
  <tr class="TableFooter">
      <td nowrap colspan="2" align="center">
        <input type="button" onclick="doQuery('form1')" class="BigButton" value="查询">&nbsp;
        <input type="button" onclick="exportExecl('form1');" class="BigButton" value="导出">&nbsp;
        <input type="button" onclick="doDeleteBySearch('form1');" class="BigButton" value="删除">
      </td>
  </tr>
</table>
</form>
</body>
</html>