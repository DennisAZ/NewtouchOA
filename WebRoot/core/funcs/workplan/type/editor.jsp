<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="yh.core.funcs.workplan.data.YHPlanType"%>
<html>
<head>
<title>工作计划类型设置</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript">
function checkForm() {
  var num = /^[0-9]*$/;
  if (!$("TYPE_NO").value) {
    alert("排序不能为空！");
    selectLast($("TYPE_NO")); 
    return false;
  }
  if (!num.exec(document.getElementById("TYPE_NO").value)) { 
    alert("排序只能为数字！");
    selectLast($("TYPE_NO")); 
    return false;
  }
  if (!$("TYPE_NAME").value ) { 
    alert("类型不能为空！");
    selectLast($("TYPE_NAME")); 
    return false;
  }
  return true;
}
function doInit () {
  selectLast($("TYPE_NO"));
}
</script>
</head>
<body  topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif"  width="16" height="16"> <span class="big3"> 修改计划类型</span>
    </td>
  </tr>
</table>
<%
  YHPlanType type = (YHPlanType)request.getAttribute("type");
%>
<form  method="post" name="form1" onSubmit="return checkForm();" action="<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanTypeAct/updateType.act">
<table class="TableBlock"  width="450"  align="center" >
   <tr>
    <td nowrap class="TableData">排序号：<font style='color:red'>*</font></td>
    <td nowrap class="TableData">
        <input type="text" name="TYPE_NO" id="TYPE_NO" class="BigInput" size="2" maxlength="10" value="<%=type.getTypeNO() %>">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">计划类型名称：<font style='color:red'>*</font></td>
    <td nowrap class="TableData">
    <input type="hidden" name="seqId" id="seqId" class="BigInput" size="25" maxlength="25" value="<%=type.getSeqId()%>">
        <input type="text" name="TYPE_NAME" id="TYPE_NAME" class="BigInput" size="25" maxlength="25" value="<%=type.getTypeName()%>">&nbsp;
    </td>
   </tr>
   <tr class="TableControl">
    <td colspan="2" align="center">
        <input type="submit" value="修改" class="BigButton" title="修改计划类型" name="button">
        <input type='button' class='BigButton' value='返回' onClick='javascript:history.back();' title='返回' >
    </td>
    
</table>
 </form>
</body>
</html>