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
<title>编辑工作委托</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
var pageMgr = null;
var requestUrl = contextPath + "/yh/core/funcs/workflow/act/YHRuleAct";
function clearUser(arr) {
  $(arr[0]).value = "";
  $(arr[1]).value = "";
}
function init() {
  skinObjectToSpan(flowrun_rule_from); 
  var url = requestUrl + "/getList.act?type=true&sortId=" + sortId;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    paramFunc: getQueryParam,
    showRecordCnt: true,
    colums:[
      {type:"data", name:"runId", text:"流水号", width: 50 , render:centerRender},       
      {type:"data", name:"runName", text:tooltipMsg1, width: 300 , render:runNameRender},
      {type:"hidden", name:"flowId", text:"顺序号" },
      {type:"data", name:"prcsName", text:"步骤名称", width: 200 ,render:prcsNameRender},
      {type:"data",  name:"nowState",text:"当前状态", width: 100 , render:centerRender},
      {type:"data",  name:"toName",text:"被委托人", width: 100},
      {type:"data", name:"ruleTime",text:"委托时间", width: 200,dataType:"dateTime",format:'yyyy-mm-dd'}
      ]
    };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}
function getQueryParam() {
  return $("form1").serialize();
}
function centerRender(cellData, recordIndex, columIndex) {
  result = "<div align=center>" + cellData + "</div>";
  return result ;
}
function runNameRender(cellData, recordIndex, columIndex) {
  var runId = this.getCellData(recordIndex,"runId"); 
  var flowId = this.getCellData(recordIndex,"flowId");
  result = "<div align=center><a href='#' onclick='formView("+ runId +" , "+ flowId +")'>" ;
  result += cellData;
  result += "</a></div>";
  return result;
}
function prcsNameRender(cellData, recordIndex, columIndex) {
  var runId = this.getCellData(recordIndex,"runId"); 
  var flowId = this.getCellData(recordIndex,"flowId");
  result = "<div align=center><a href='#' onclick='flowView("+ runId +" , "+ flowId +" , \""+ cellData +"\" , \""+ sortId +"\" , \""+ skin +"\")'>" ;
  result += cellData;
  result += "</a></div>";
  return result;
}
</script>
</head>
<body  topmargin="5" leftmargin=0 onload="init();" >
<form id="form1" name="form1">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
  <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="absmiddle"><span id="span1" class="big3"> 已委托工作记录</span>&nbsp;</td>
  </tr>
  
<% if (user.isAdminRole()) {  %>
 <tr>
    <td>
    &nbsp;<span style="font-size:9pt;">按人员查询：
    <input type="text" name="queryUserName" id="queryUserName" size="15" class="SmallStatic" readonly value="">
    <input type="hidden" name="queryUserId" id="queryUserId"  value="">
    <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['queryUserId', 'queryUserName'])">选择</a>
    <a href="javascript:;" class="orgClear" onClick="clearUser(['queryUserId', 'queryUserName']);">清空</a>
     <input type="button" class="SmallButton" value="查询" onclick="pageMgr.search();"/>
    </span>
    </td>
    </tr>
<% }%>

</table>

<div id="listContainer" style="padding-left:5px"></div>
</form>
</body>
</html>