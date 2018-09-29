<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
 <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
 <%
 String ruleId = request.getParameter("ruleId");
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
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/skin.js"></script>
<script type="text/javascript">
var requestUrl = contextPath + "<%=moduleSrcPath %>/act/YHRuleAct";
var ruleId = "<%=ruleId%>";
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
function doInit() {
  skinObjectToSpan(flowrun_rule_edit); 
  loadRule();
  loadFlowType();
  var beginParameters = {
      inputId:'beginDate',
      property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endDate',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);
  
}
function loadRule() {
  var url = requestUrl + "/getRuleById.act";
  var json = getJsonRs(url , "ruleId=" + ruleId);
  if (json.rtState == "0") {
    window.flowId = json.rtData.flowId;
    $('beginDate').value = json.rtData.beginDate;
    $('endDate').value = json.rtData.endDate;
    $('toId').value = json.rtData.toId ;
    $('toName').value = json.rtData.toName ;
    $('userId').value = json.rtData.userId;
  }
}
function clearUser(arr) {
  $(arr[0]).value = "";
  $(arr[1]).value = "";
}
//加载流程
function loadFlowType(){
  var url = contextPath+'<%=moduleSrcPath %>/act/YHFlowTypeAct/getFlowTypeJson1.act?sortId=' + sortId;
  var json = getJsonRs(url);
  var rtData = json.rtData;   
  for(var i = 0 ;i < rtData.length ; i ++) {      
    var opt = document.createElement("option") ;      
    opt.value = rtData[i].seqId ; 
    opt.innerHTML = rtData[i].flowName ;      
    $('flowId').appendChild(opt) ; 
    if (flowId ==  rtData[i].seqId ) {
      opt.selected = true;
    }                       
  }    
} 
function checkForm() {
  if(!$('toId').value){
    alert("被委托人不能为空!");
    return (false);
  }
  if(!$('flowId').value){ 
    alert("请选择工作流程!");
    return false;
  }
  if ($('beginDate').value && !isValidDateStr($('beginDate').value)) {
    alert("生效日期格式不对，应形如 1999-01-02");
    return false;
  }
  if ($('endDate').value && !isValidDateStr($('endDate').value)) {
    alert("终止日期格式不对，应形如 1999-01-02");
    return false;
  }
  saveRule();
}
function saveRule() {
  var url = requestUrl + "/updateRule.act";
  var json = getJsonRs(url , $('editForm').serialize());
  if (json.rtState == "0") {
    history.back();
  }
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span id="span1" class="big3"> </span>
    </td>
  </tr>
</table>

<form  method="post" name="editForm" id="editForm" >
<table width="450" class="TableList" align="center" >
   <tr>
    <td nowrap class="TableData">选择流程：</td>
    <td nowrap class="TableData">
    <select name="flowId" id="flowId" class="SmallSelect" style="width:200px">
      <option value="">请选择流程</option>
  </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">被委托人：</td>
    <td nowrap class="TableData">
    <input type="text" class="SmallInput" size=20 readOnly name="toName" id="toName" value="">
    <INPUT type="hidden" name="toId" id="toId" value="">
    <input type="button" class="SmallButtonW" value="选择" onclick="selectSingleUser(['toId', 'toName']);">
    <input type="button" class="SmallButtonW" value="清空" onclick="clearUser(['toId', 'toName'])">
    </td>
   </tr>
   <tr>
      <td nowrap class="TableData"> 有效期：</td>
      <td class="TableData">
        生效日期：<input type="text" name="beginDate" id="beginDate" size="10" maxlength="10" class="BigInput"/>
        <img src="<%=imgPath %>/calendar.gif" id="beginDateImg" align="absMiddle" border="0" style="cursor:pointer"><br>
        终止日期：<input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" />
        <img src="<%=imgPath %>/calendar.gif" id="endDateImg" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="hidden" name="userId" id="userId" value="">
        <INPUT type="hidden" name="ruleId" id="ruleId" value="<%=ruleId %>">
        <INPUT type="hidden" name="queryUserId" id="queryUserId" value="">
        <input type="button" onclick="checkForm()" value="保存" class="BigButton">
        <input type="button" value="返回" class="BigButton" onclick="history.back()">
    </td>
</tr>
</table>
  </form>
</body>
</html>