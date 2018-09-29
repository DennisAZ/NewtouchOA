<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
 <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
 <%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
String flowId = request.getParameter("flowId");
String userId = request.getParameter("userId");
String prcsDate1Query = request.getParameter("prcsDate1Query");
String prcsDate2Query = request.getParameter("prcsDate2Query");
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
<title></title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
 <script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/skin.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
var pageMgr = null;
var requestUrl = contextPath + "<%=moduleSrcPath %>/act/YHWorkOverTimeAct";
function doInit() {
  skinObjectToSpan(flowrun_overtime_viewDetail);
  var url = requestUrl + "/viewDetail.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    paramFunc: getQueryParam,
    colums: [
      {type:"data", name:"runId", text:"流水号", width: 100 },
      {type:"data", name:"flowName", text:"流程名称", width: 300 , render:flowNameRender},
      {type:"data", name:"runName", text:tooltipMsg1, width: 400  , render:runNameRender },
      {type:"data",  name:"prcsName",text:"步骤名称", width: 400 ,render:prcsNameRender},
      {type:"data",  name:"prcsFlag",text:"主办人状态", width: 200},
      {type:"data",  name:"userName",text:"主办人", width: 200},
      {type:"data", name:"prcsTime",text:"接收时间", width: 400,dataType:"dateTime",format:'yyyy-mm-dd'},
      {type:"data", name:"deliverTime",text:"转交时间", width: 400,dataType:"dateTime",format:'yyyy-mm-dd'},
      {type:"hidden", name:"flowId", text:"顺序号" },
      
      {type:"data", name:"timeOut", text:"办理时限",width:200,render:handleTimeRender},
      {type:"data", name:"timeExcept", text:"超时时间",width:400,render:overTimeRender},
      {type:"hidden", name:"opFlag"},
      {type:"hidden", name:"createTime"}
      ]
    };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}
/**
 * 办理时限
 */
function handleTimeRender(cellData, recordIndex, columIndex){
  var timeOut = this.getCellData(recordIndex,"timeOut");
  var html = "";
  html ="<center>"+timeOut+"</center>";
  return html;
}

/**
 * 超时时间
 */
function overTimeRender(cellData, recordIndex, columIndex){
  var timeExcept = this.getCellData(recordIndex,"timeExcept");
  var html = "";
  if (!timeExcept) {
    timeExcept = '0';
  }
  html ="<center>"+ timeExcept.replace('null','0') + "</center>";
  return html;
}
function getQueryParam() {
  return $("form1").serialize();
}
function flowNameRender(cellData, recordIndex, columIndex) {
  var runId = this.getCellData(recordIndex,"runId"); 
  var flowId = this.getCellData(recordIndex,"flowId");
  result = "<div align=center><a href='#' onclick='viewGraph("+ flowId +")'>" ;
  result += cellData;
  result += "</a></div>";
  return result;
}
function runNameRender(cellData, recordIndex, columIndex) {
  var runId = this.getCellData(recordIndex,"runId"); 
  var flowId = this.getCellData(recordIndex,"flowId");
  result = "<div align=center><a href='#' onclick='showPrintWindow("+ runId +" , "+ flowId +")'>" ;
  result += cellData;
  result += "</a></div>";
  return result;
}
function showPrintWindow(runId , flowId) {
  formView(runId, flowId);
}
function prcsNameRender(cellData, recordIndex, columIndex) {
  var runId = this.getCellData(recordIndex,"runId"); 
  var flowId = this.getCellData(recordIndex,"flowId");
  result = "<div align=center><a href='#' onclick='flowView("+ runId +" , "+ flowId +" , \""+ cellData +"\", \""+ sortId +"\", \""+ skin +"\")'>" ;
  result += cellData;
  result += "</a></div>";
  return result;
}
</script>
</head>
<body style="width:1024px" topmargin="5" leftmargin=0 onload="doInit()" >
<form id="form1" name="form1">

<div id="listContainer" style="padding-left:5px"></div>
<input type="hidden" value="<%=flowId %>" id="flowId" name="flowId"/>
<input type="hidden" value="<%=userId %>" id="userId" name="userId"/>
<input type="hidden" value="<%=prcsDate1Query %>" id="prcsDate1Query" name="prcsDate1Query"/>
<input type="hidden" value="<%=prcsDate2Query %>" id="prcsDate2Query" name="prcsDate2Query"/>
</form>
</body>
</html>
