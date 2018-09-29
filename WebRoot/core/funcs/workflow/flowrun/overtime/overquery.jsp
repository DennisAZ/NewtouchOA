<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String skin = request.getParameter("skin");
String skinJs = "messages";
if (skin != null && !"".equals(skin)) {
  skinJs = "messages_" + skin;
} else {
  skin = "";
}
%>
<html>
<head>
<title>超时工作列表</title>
<style>
.color1 {color:#FFBC18;}
.color2 {color:#50C625;}
.color3 {color:#F4A8BD;}
.color4 {color:#F4A8BD;}
</style>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
 <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
  <script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
  <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
 <script type="text/javascript" src="js/overjs.js"></script>
 <script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
</script>
</head>
<body onload="doInit()">
<form  name="queryForm" id="queryForm">
<table id="flowTable" border="0" width="100%" class="TableList" >
  <tr class="TableLine1">
  <td align="left"> <input type="hidden" id="sortId" name="sortId" value="<%=sortId %>"/>流程：  <select name="flowList" onmouseover="this.setStyle({'width':'auto'})" onChange="this.setStyle({'width':'100px'})" id="flowList" style="width:100px">
  <option value="0" id="span1"></option>
  </select>&nbsp;&nbsp;
  
  
  状态：<select name="flowStatus" id="flowStatus">
  <option value="ALL">所有状态</option>
  <option value="doing">办理中 </option>
      <option value="end">已办结 </option>
  </select>&nbsp;&nbsp;
   <div style="margin-top: 3px;">
   <span id="span2">
  </span>
  <input type="text" id="statrTime" name="statrTime" size="10" maxlength="19" readonly class="BigStatic" value="">
  <img id="beginDateImg" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      到
  <input type="text" id="endTime" name="endTime" size="10" maxlength="19"  readonly class="BigStatic">
  <img id="endDateImg" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
  <a href="javascript:empty_date()">清空</a>
   </div>
  </td> 
  <td align=right><input id="queryBtn" type="button" class="BigButton" onclick="queryData()" value="开始查询"></td>
  </tr>
  </table>
  </form>
   <div style="padding-top:10px"></div>
 <div id = "listContainer"> </div>
  <div id="msrg" align=center style="display:none">
  </div> 
  <table class="TableList" border=0 width="100%" style="margin:0;display:none"  id="flowRunOpTab">
  <tr class="TableControl">
  <td colspan="10">
  <input type="button"  onclick="exportCsv()"  value="导出CSV" class="BigButton"  title="导出CSV">
  </td>
  </tr>
  </table>
</body>
</html>