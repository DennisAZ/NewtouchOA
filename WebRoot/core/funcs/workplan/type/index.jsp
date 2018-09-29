<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="yh.core.funcs.workplan.data.YHPlanType"%>
<html>
<head>
<title>工作计划类型设置</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript">
function doInit() {

  selectLast($("TYPE_NO"));
  var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanTypeAct/planType.act";
  var json = getJsonRs(url);
  if(json.rtState == "1") {
    alert(json.rtState);
    return;       
  }
 
  var prcs = json.rtData;
  if (prcs.length > 0) {
    var table = new Element('table',{"class":"TableList","align":"center","cellspacing":"0","cellpadding":"3" }).update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
        + "<td align='center' width='120'>排序号</td>"
        + "<td align='center' width='120'>类型名称</td>"
        + "<td width='120'>操作</td></tr><tbody>");
    $('listDiv').appendChild(table);
    
    for(var i = 0; i < prcs.length; i++) {
      var prc = prcs[i];
      var typeName = prc.typeName;
      var typeNO = prc.typeNO;
      var seqId = prc.seqId; 
      var tr = new Element('tr',{"align":"center","title":"类型","class":"TableData"});
      $('tbody').appendChild(tr);
         tr.update("<td>" +typeNO + "</td>"
               + "<td>" + typeName + "</td>"
               + "<td>"
               + "<a href='javascript:updateType(" + seqId + ")'>编辑</a>&nbsp;"
               + "<a href='javascript:deteleType(" + seqId + ")'>删除</a>&nbsp;"
               + "</td>");
    }
    var tr2 = new Element('tr',{"align":"center","title":"类型","class":"TableControl"});
    $('tbody').appendChild(tr2);
    tr2.update("<td colspan='3'><input class='BigButton' type='button' value='删除所有' onClick='javascript:deteleTypeAll();'></td>");
  }
}
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
function checkType() {
  var typeName = document.getElementById("TYPE_NAME").value;
  var param1 = "typeName=" + typeName;
  var param = encodeURI(param1);
  var reqURL = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanTypeAct/selectTypeName.act?" + param;
  var reqJson = getJsonRs(reqURL);
  if(reqJson.rtState == "1") {
    alert(reqJson.rtState);
    return false;       
  }
  var prcs = reqJson.rtData;
  if (prcs.length > 0) {
    alert("该类型已经存在!");
    return false;
  }
  return true;
}

function checkForm2() {
  if (checkForm()) {
    var typeName = document.getElementById("TYPE_NAME").value;
    var param1 = "typeName=" + typeName;
    var param = encodeURI(param1);
    var reqURL = contextPath + "/yh/core/funcs/workplan/act/YHPlanTypeAct/selectTypeName.act?" + param;
    var reqJson = getJsonRs(reqURL);
    var prcs = reqJson.rtData;
    if (prcs.length > 0) {
      alert("该类型已经存在!");
      return;
    } else {
      var pars = $('form1').serialize() ;
      var url = contextPath + "/yh/core/funcs/workplan/act/YHPlanTypeAct/addType.act";
      var json = getJsonRs(url,pars);
      if (json.rtState == "1") {
        alert(json.rtMsrg);
      } else {
        window.location = contextPath + "/core/funcs/workplan/type/index.jsp";
      }
    }
  }
}
function deteleType(seqId) {
  var msg = "确认要删除该计划类型吗?";
  if (window.confirm(msg)) {
  window.location.href = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanTypeAct/deleteType.act?seqId=" + seqId;
  }
}
function deteleTypeAll() {
  var msg = "确认要删除所有计划类型吗?";
  if (window.confirm(msg)) {
  window.location.href = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanTypeAct/deleteTypeAll.act";
  }
}
function updateType(seqId) {
  window.location.href = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanTypeAct/selectId.act?seqId=" + seqId;
}
</script>
</head>
<body  topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif"  width="16" height="16">&nbsp; <span class="big3"> 添加计划类型</span>
    </td>
  </tr>
</table>
<form  name="form1" id="form1">
<table class="TableBlock"  width="450"  align="center" >
   <tr>
    <td nowrap class="TableData">排序号：<font style='color:red'>*</font></td>
    <td nowrap class="TableData">
        <input type="text" name="TYPE_NO" id="TYPE_NO" class="BigInput" size="2" maxlength="10">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">计划类型名称：<font style='color:red'>*</font></td>
    <td nowrap class="TableData">
        <input type="text" name="TYPE_NAME" id="TYPE_NAME" class="BigInput" size="25" maxlength="25">&nbsp;
    </td>
   </tr>
   <tr class="TableControl">
    <td colspan="2" align="center">
        <input value="添加" class="BigButton" title="添加计划类型" type="button" onclick="checkForm2()">
    </td></tr>
</table>
 </form>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" width="18" height="17">&nbsp;<span class="big3"> 管理计划类型</span>    </td>
  </tr>
</table>

<br>
<div align="center" id="listDiv"></div>
</body>
</html>