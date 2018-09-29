<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
  <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
  <%@ page  import="yh.core.funcs.doc.receive.data.YHDocConst"%>
<%
String webroot = request.getRealPath("/");
String sortName = YHDocConst.getProp(webroot  , YHDocConst.DOC_RECEIVE_FLOW_SORT) ;
if (sortName == null) {
  sortName = "";
}
String skin = "receive";
YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
String type = request.getParameter("type");
String tip = "未办理收文";
if ("1".equals(type)) {
  tip = "未办理收文";
} else if ("2".equals(type)) {
  tip = "办理中的收文";
} else if ("3".equals(type)) {
  tip = "已办结的收文";
}

%>
<html>
<head>
<title>收文登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css"/>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/receive/register/register.js"></script>
<script type="text/Javascript">
var cfgs = null;
var pageMgr = null;

var sortName = "<%=sortName%>";
var type = "<%=type %>";
var skin = "receive"; 
var sortId = "";
var userName = "<%=user.getUserName() %>";
var flowTypeStr = "";
var secrtGrade = "<%=YHDocConst.getProp(webroot , YHDocConst.SECRET_GRADE)%>";
var docType = "<%=YHDocConst.getProp(webroot , YHDocConst.DOC_TYPE)%>";
function getFlowType() {
  var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocRegisterAct/getFlowType.act";
  var json = getJsonRs(url);
  if (json.rtState = '0') {
    flowTypeStr = createList(json.rtData);
    sortId = json.rtMsrg;
  }
}

function createList(flowTypes) {
  var str = "";
  for (var i =0 ;i < flowTypes.length ;i++) {
    var fl = flowTypes[i];
    var option = "<option value=\"" + fl.id + "\">"+fl.name+"</option>";
    str += option;
  }
  return str;
}
function edit(seqId){
  var url = contextPath + "/core/funcs/doc/receive/register/docReg.jsp?seqId=" + seqId;
  window.open(url);
}
function comeback(seqId) {
  if (confirm("确认退回！")) {
    var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocSignAct/comeback.act?seqId=" + seqId;
    var json = getJsonRs(url);
    if (json.rtState = '0') {
      pageMgr.search();
    }
  }
}

</script>
</head>
<body onload="doInit();">


<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif"/><span class="big3"><%=tip %></span>
    </td>
  </tr>
</table>
<fieldset>
<form  name="queryForm" id="queryForm">
<table id="flowTable" border="0" width="100%"  class="TableList"  >
  <tr class="TableLine1">
  <td align="left">    类型：
  <select name="recType" id="recType">
 <option value="">所有类型</option>
 </select>&nbsp;&nbsp;
密级：<select name="secretsLevel" id="secretsLevel">
<option value=""></option>
  </select> &nbsp;&nbsp;
 登记时间： 从
  <input type="text" id="startTime" name="startTime" size="10" maxlength="19" readonly class="BigStatic" value="">
  <img id="beginDateImg" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      到
  <input type="text" id="endTime" name="endTime" size="10" maxlength="19"  readonly class="BigStatic">
  <img id="endDateImg" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
  <a href="javascript:empty_date()">清空</a>
    </td> 
  </tr><tr class="TableLine2">
  <td align="left"> 
  标题：
<input value="" id="title" name="title"> &nbsp;&nbsp;
来文文号：  <input value="" id="sendDocNo" name="sendDocNo"> &nbsp;&nbsp;
来文单位：  <input value="" id="fromDeptName" name="fromDeptName">
<input onclick="query()" value="查询" type="button" class="SmallButton">
<input type="hidden" value="<%=type %>" name="type" id="type">
  </td> 
  </tr>
  </table>
</fieldset>
</form>
<div id="container"></div>
</body>
</html>