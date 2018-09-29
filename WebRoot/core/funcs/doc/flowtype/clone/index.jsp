<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
String flowId = request.getParameter("flowId"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>流程克隆</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var flowId = "<%=flowId%>";
var requestUrl = contextPath + "<%=moduleSrcPath %>/act/YHFlowTypeCorAct"
function doInit() {
  var url = requestUrl + "/getCloneMsg.act";
  var json = getJsonRs(url , "flowId=" + flowId);
  if (json.rtState == '0') {
    var flowNo = json.rtData.flowNo;
    var flowName = json.rtData.flowName;
    $("flowNo").value = flowNo;
    $("flowName").value = flowName;
  }
  $('flowNo').focus();
}
function submitForm() {
  if (!$("flowName").present()) {
    alert("流程名称不能为空!");
    $("flowName").focus();
    return ;
  }
  var reg = /['"]/g;
  if ($('flowName').value.match(reg)) {
    alert("流程名称不能有\"'\"和\"\"\"字符！");
    $('flowName').focus();
    return ;
  } 
  
  var reg1 = /[^\d]/g;
  var flowNo = $("flowNo");
  var str = flowNo.value;
  if (!str && str.match(reg1)) {
    alert("流程排序号只能为数字！");
    flowNo.focus();
    return ;
  } 
  var url = requestUrl + "/clone.act";
  var json = getJsonRs(url , $('form1').serialize());
  if (json.rtState == '0') {
    closeModalWindow('clone');
  }
}

</script>
</head>
<body  topmargin="5" onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 流程克隆</span><br>
    </td>
  </tr>
</table>

<br>
 <form  method="post" name="form1" id="form1">
<table width="450" align="center" class="TableBlock">
    <tr>
      <td nowrap class="TableData">新流程排序号：</td>
      <td class="TableData">
        <input type="text"  onkeyup="value=value.replace(/[^\d]/g,'')"  name="flowNo" id="flowNo" size="4" maxlength="100" class="BigInput" value=""> 控制同一分类下流程的排序
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">新流程名称：</td>
      <td class="TableData">
        <input type="text"  name="flowName" id="flowName" size="30" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type='hidden' value="<%=flowId %>" name="flowId" id=flowId>
        <input type="button" onclick="submitForm()"  value="保存" class="BigButton" name="button">&nbsp;&nbsp;
        <input type="button"  value="关闭" class="BigButton" name="back" onClick=" closeModalWindow('clone');">
      </td>
    </tr>
 
</table>
 </form>
</body>
</html>