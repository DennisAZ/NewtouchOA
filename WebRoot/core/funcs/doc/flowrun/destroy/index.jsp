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
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/flowrun/destroy/js/destroy.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/skin.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
var sortName = "<%=sortName%>";
function doInit(){
  skinObjectToSpan(flowrun_destroy_index); 
  if (sortName) {
    sortId = getSortIdsByName(sortName);
    $('sortId').value = sortId;
  }
  loadFlowType("flowId");
  var beginParameters = {
      inputId:'startTime',
      property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endTime',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);
}
</script>
<title>工作销毁</title>
</head>
<body onload="doInit()">
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/trash.gif" align="absmiddle"><span id="span2" class="big3"> </span>
      <span class="small1" id="span1"></span>
    </td>
  </tr>
</table>
<br>  
<form id="form1" name="form1">
<input type=hidden value="<%=skin %>" name="skin" id="skin"/>
<input type=hidden value="<%=sortId %>" name="sortId" id="sortId"/>
<table width="450"  class="TableList"  align="center" >
   <tr>
    <td nowrap class="TableContent">选择流程：</td>
    <td nowrap class="TableData">
       <select name="flowId" id="flowId" onmouseover="this.setStyle({'width':'auto'})" onChange="this.setStyle({'width':'100px'})" style="width:100px">
      <option value="">请选择流程</option>
  </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">流程发起人：</td>
    <td nowrap class="TableData">
      <textArea cols=40 rows=3 class="BigStatic" readOnly name="toIdDesc" id="toIdDesc"></textArea>
      <INPUT type="hidden" name="toId" id="toId">
      <a href="#" class="orgAdd" onClick="selectUser(['toId', 'toIdDesc']);">添加</a>
      <a href="#" class="orgClear" onClick="Clear('toId','toIdDesc')">清空</a>
    </td>
   </tr>
   <tr>
      <td nowrap class="TableContent"> 日期：</td>
      <td class="TableData">
        开始日期：<input type="text" name="startTime" id="startTime" size="10" maxlength="10" class="BigInput" value="">
        <img src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" id="beginDateImg"><br>
        截止日期：<input type="text"  name="endTime" id="endTime" size="10" maxlength="10" class="BigInput" value="">
        <img src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" id="endDateImg">
      </td>
    </tr>
   <tr>
      <td nowrap class="TableContent"> 流水号：</td>
      <td class="TableData">
      <input type="text" class="SmallInput" name="runId" size=8>
      </td>
    </tr>
   <tr>
      <td nowrap class="TableContent"> <span id="span3"></span></td>
      <td class="TableData">
      <input type="text" class="SmallInput" name="runName" size=40>
      </td>
    </tr>
    <td nowrap  class="TableFooter" colspan="2" align="center">
        <input type="hidden" name="op">
        <input type="button" onclick="doQuery('form1');" value="查询" class="BigButton">
        <input type="button" onclick="destroyBySearch('form1');" value="销毁" class="BigButton">
        <input type="button" onclick="recoverBysearch('form1');" value="还原" class="BigButton">
    </td>
</table>
</form>
 
<table class="MessageBox" align="center" width="500">
  <tr>
    <td class="msg warning">
      <div class="content" style="font-size:12pt">警告：该操作具有一定危险性，非OA管理员严禁操作，请合理设置本模块菜单权限</div>
    </td>
  </tr>
</table>

</body>
</html>