<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="js/index.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<script type="text/javascript"><!--
var isAdminRole = <%=user.isAdminRole()%>;
var loginUserId = <%=user.getSeqId() %>;
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
</script>
<title>工作委托</title>
</head>
<body onload="doInit()"> 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span class="big3"> 添加工作流自动委托规则（提前委托）</span>
    </td>
  </tr>
</table>
   <form   method="post" name="ruleForm"  id="ruleForm">
   <input type="hidden" value="<%=sortId %>" name="sortId" id="sortId"/>
<table width="450" class="TableList" cellpadding="3" align="center" >
   <tr>
    <td nowrap class="TableContent">选择流程：</td>
    <td nowrap class="TableData">
    <select name="flowId" id="flowId"  style="width:260px">
      <option value="">允许自由委托的流程可以提前委托，请选择</option>
   </select>
  <input type="checkbox" name="checkAll" id="checkAll" onclick="check_all();"><label for="checkAll">选择全部流程</label>
    </td>
   </tr>
   <% if (user.isAdminRole()) {%>
   <tr>
    <td nowrap class="TableContent">委托人：</td>
    <td nowrap class="TableData">
    <input type="text" class="SmallInput" size=20 readOnly id="userName" name="userName">
    <INPUT type="hidden" name="userId" id="userId">
    <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['userId', 'userName'])">选择</a>
    <a href="javascript:;" class="orgClear" onClick="clearUser(['userId', 'userName'])">清空</a>
    </td>
   </tr>
   <% } %>
   <tr>
    <td nowrap class="TableContent">被委托人：</td>
    <td nowrap class="TableData">
    <input type="text" class="SmallInput" size=20 id="toName" readOnly name="toName">
    <INPUT type="hidden" name="toId" id="toId">
    <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['toId', 'toName'])">选择</a>
    <a href="javascript:;" class="orgClear" onClick="clearUser(['toId', 'toName'])">清空</a>
    </td>
   </tr>
   
   <tr>
      <td nowrap class="TableContent" rowspan="2"> 有效期：</td>
         <td class="TableData">
        生效日期：<input type="text" name="beginDate" id="beginDate" size="10" maxlength="10" class="SmallInput" value="">
        <img align="absbottom" src="<%=imgPath %>/calendar.gif" id="beginDateImg" style="cursor:pointer">&nbsp;&nbsp;<input type="checkbox" id="alwaysOn" name="alwaysOn" id="ALWAYS_ON" onclick="data_disable();" ><label for="alwaysOn">一直有效</label></td>
    </tr>
    <tr>
    <td class="TableData">
        终止日期：<input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="SmallInput" value="">
        <img align="absbottom" src="<%=imgPath %>/calendar.gif" id="endDateImg"  border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
    <td nowrap  class="TableFooter" colspan="2" align="center">
        <input type="hidden" name="flowIdStr" id="flowIdStr">
        <input type="button" value="添加" onclick="checkForm()" class="BigButton" title="添加委托规则">
    </td>
 </tr>
</table>
  </form>
<br>
 
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath %>/dian1.gif" width="100%"></td>
 </tr>
</table>
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="absmiddle"><span class="big3"> 我的工作委托</span>
    <select id="ruleState" name="ruleState" onchange="loadRule()">
      <option value="0"  selected>委托状态</option>
      <option value="1">被委托状态</option>
    </select>
    &nbsp;&nbsp;&nbsp;
    <% if (user.isAdminRole()) {%>
    <span style="font-size:9pt;">按人员查询：
    <input type="text" name="queryUserName" id="queryUserName" size="15" class="SmallStatic" readonly value="">
    <input type="hidden" name="queryUserId" id="queryUserId" onpropertychange="loadRule();" value="">
    <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['queryUserId', 'queryUserName'])">选择</a>
    <a href="javascript:;" class="orgClear" onClick="clearUser(['queryUserId', 'queryUserName']);">清空</a>
  </span>
  <%} %>
    </td>
  </tr>
</table>
<div id="hasData">
    </div>
<br>
<div id="noData" align="center" style="display:none">
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt" id="noDataMsg"></div>
    </td>
  </tr>
</table>
 
</div>
</body>
</html>