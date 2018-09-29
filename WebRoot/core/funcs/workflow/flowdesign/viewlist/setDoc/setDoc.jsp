<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String seqId = request.getParameter("seqId");
String flowId = request.getParameter("flowId");

%>
<%
String isList = request.getParameter("isList");

%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置公文相关</title>
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/style.css">

<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/ExchangeSelect1.0.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var flowId = "<%=flowId%>";
var procId;
var isList = "<%=isList == null ? "" : "1"%>";
var actionPath = contextPath + "/yh/core/funcs/workflow/act/YHFlowProcessAct";
function doInit(){
  var url = actionPath + "/getDocMsg.act";
  var json = getJsonRs(url , "seqId=" + seqId + "&flowId=" + flowId);
  if(json.rtState != "0"){
    alert('请联系管理员');
    return ;
  }
  var attachPriv = json.rtData.attachPriv;
  procId = json.rtData.procId;
  
  $('procsIdSpan').innerHTML = '步骤 '+ procId + '- 设置公文相关';
  
  var aAttachPriv = attachPriv.split(',');
  for(var i = 0 ;i < aAttachPriv.length ;i ++){
    if(aAttachPriv[i]){
      $('priv' + aAttachPriv[i]).checked = true;
    }
  } 
  var extend = json.rtData.extend;
  var extend1 = json.rtData.extend1;
  $('extend').value = extend;
  $('extend1').value = extend1;
  var docCreate = json.rtData.docCreate;
  $('DOC_CREATE').value = docCreate;
}
function commit(){
  var url = actionPath + "/setDocItem.act";
  var json = getJsonRs(url , $('setFieldForm').serialize());
  if(json.rtState == '0'){
    if(!isList){
      try {
        opener.location.reload();
      } catch (e) {

      }
      window.close();
    }else{
      history.back();
    }
  }else{
    alert(json.rtMsrg);
  }
}

</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span id="procsIdSpan" class="big3"> </span><br>
    </td>
  </tr>
</table>
<form  method="post" name="setFieldForm" id="setFieldForm">
  <div>
  <table width="450" border="0" align="center" class="TableList">
  <tr class="TableLine2"  style="display:">
    <td  class="TableContent" width=100px>强制归档：</td>
    <td  class="TableData">
     <select id="extend" name="extend">
     <option value="" selected>否</option>
     <option value="1">是</option>
     </select>
    </td>
    <td  class="TableContent" width=100px>分配文号：</td>
    <td  class="TableData" style="display:">
     <select id="extend1" name="extend1">
     <option value="" selected>否</option>
     <option value="1">是</option>
     </select>
    </td>
  </tr>
  <tr class="TableContent">
    <td colspan="4">
    <b>正文Office文档详细权限设置</b>
    <br>
    <input type="checkbox" id="priv2" name="privEdit"><label for="PRIV2">编辑权限</label>
    <input type="checkbox" id="priv3" name="privDel"><label for="PRIV3">删除权限</label>
    <input type="checkbox" id="priv4" name="privOfficeDown"><label for="PRIV4">下载权限</label>
    <input type="checkbox" id="priv5" name="privOfficePrint"><label for="PRIV5">打印权限</label>
    </td>
  </tr>
  <tr class="TableContent">
    <td  class="TableContent" width=100px >成文步骤：</td>
    <td  class="TableData"  colspan="3">
     <select id="DOC_CREATE" name="DOC_CREATE">
     <option value="" selected>否</option>
     <option value="1">是</option>
     </select>
    </td>
  </tr>
  <tr>
    <td align="center" class="TableFooter" colspan="4">
      <input type="button" class="BigButton" value="保 存" onclick="commit()">&nbsp;&nbsp;&nbsp;&nbsp;
		<% if(isList == null){ %>
  <input type="button"  value="关闭" class="BigButton" onclick="closeWindow();">
  <% }else{ %>
  <input type="button"  value="返回" class="BigButton" onclick="history.back();">
  <% } %>
      <input type="hidden" name="flowId" value="<%=flowId %>">
      <input type="hidden" name="seqId" value="<%=seqId %>">
      <input type="hidden" name="type" value="isWrite">
    </td>
  </tr>
</table>
</div>
</form>

</body>
</html>