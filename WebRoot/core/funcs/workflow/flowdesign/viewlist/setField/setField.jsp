<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String seqId = request.getParameter("seqId");
String flowId = request.getParameter("flowId");
String openflag = request.getParameter("openflag");
if(openflag == null ){
  openflag = "";
}
%>
<%
String isList = request.getParameter("isList");

%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置可写字段</title>
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
var openflag = "<%=openflag %>";
var isList = "<%=isList == null || "".equals(isList) ? "" : "1"%>";
var actionPath = contextPath + "/yh/core/funcs/workflow/act/YHFlowProcessAct";
function doInit(){
  var url = actionPath + "/getFieldMsg.act";
  var json = getJsonRs(url , "seqId=" + seqId + "&flowId=" + flowId);
  if(json.rtState != "0"){
    alert('请联系管理员');
    return ;
  }
  var selectedItem = json.rtData.prcsItem;
  var items =  json.rtData.items;
  var attachPriv = json.rtData.attachPriv;
  var itemAuto = json.rtData.itemAuto;
  procId = json.rtData.procId;
  
  $('procsIdSpan').innerHTML = '步骤 '+ procId + '- 编辑可写字段';
  
  var selected = [];
  var disSelected = [];
  var aSelectedItem = selectedItem.split(',');
  var aItem = items.split(',');
  
  for(var i = 0 ;i < aItem.length ;i++){
    var itemTmp = aItem[i];
    if(itemTmp){
	  var isExist = false;
	  for(var j = 0 ; j < aSelectedItem.length;j++){
	    if(aSelectedItem[j] && aSelectedItem[j] == itemTmp){
	      isExist = true;
	      var tmp = {};
	      tmp.value = itemTmp;
	      if(itemTmp == '[B@]'){
	        tmp.text =  '[工作名称/文号]';
	      }else if(itemTmp == '[A@]'){
	        tmp.text =  '[流程公共附件]';
	      }else{
	        tmp.text =  itemTmp;
	      }
	      selected.push(tmp);
	      break;
	    }
	  }
	  if(!isExist){
	    var tmp = {};
	    tmp.value = itemTmp;
	    if(itemTmp == '[B@]'){
	      tmp.text =  '[工作名称/文号]';
	    }else if(itemTmp == '[A@]'){
	      tmp.text =  '[流程公共附件]';
	    }else{
	      tmp.text =  itemTmp;
	    }
	    disSelected.push(tmp);
	  }
    }
  }
  new ExchangeSelectbox({containerId:'selectItemDiv'
	    ,selectedArray:selected
	    ,disSelectedArray:disSelected 
		,isOneLevel:false
	    ,selectedChange:exchangeHandler
    }); 
  
  $('fieldStr').value = selectedItem;
  
  $('itemAutoDiv').innerHTML = itemAuto;

  var aAttachPriv = attachPriv.split(',');
  for(var i = 0 ;i < aAttachPriv.length ;i ++){
    if(aAttachPriv[i]){
      $('priv' + aAttachPriv[i]).checked = true;
    }
  } 
  
}
function exchangeHandler(ids){
  $('fieldStr').value = ids;
}
function commit(flag){
  var url = actionPath + "/setFormItem.act";
  var json = getJsonRs(url , $('setFieldForm').serialize());
  if(json.rtState == '0'){
    if (!openflag) {
      if (!flag) {
        if(!isList){
          try {
            opener.location.reload();
          } catch (e) {

          }
          window.close();
        }else{
          history.back();
        }
      }
    }else {
      alert("保存成功！");
    }
  }else{
    alert(json.rtMsrg);
  }
}
function turn(type) {
  commit(1);
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowProcessAct/turn.act";
  var json = getJsonRs(url , "flowId=" + flowId + "&seqId=" + seqId + "&type=" + type);
  if (json.rtState == '0') {
    if (!json.rtData) {
      if (type) {
        alert("无上一步骤！");
      } else {
        alert("无下一步骤！");
      }
      return;
    }
    var url = contextPath + "/core/funcs/workflow/flowdesign/viewlist/setField/setField.jsp?flowId="+flowId + "&isList=" + isList + "&openflag=" + openflag;
    if (json.rtData) {
      url += "&seqId=" + json.rtData;
    }
    this.location.href = url;
  }
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span id="procsIdSpan" class="big3"> </span>
   保存并转到：<input onclick='turn(1)'  class='SmallButtonW' value="上一步" type="button">&nbsp;<input onclick='turn()'  class='SmallButtonW' value="下一步" type="button"><br>
    </td>
  </tr>
</table>
<form  method="post" name="setFieldForm" id="setFieldForm">

  <div id="selectItemDiv" align=center></div>
  <div>
  <table width="450" border="0" align="center" class="TableList">
  <tr class="TableContent">
    <td colspan="3">
    <b>允许在不可写情况下自动赋值的宏控件</b>
    <input type="button" class="SmallButtonW" value="设置" onclick="location='setAutoField.jsp?flowId=<%=flowId%>&seqId=<%=seqId%><%=(isList != null ? "&isList=true" : "") %>&openflag=<%=openflag %>'">
    <br>
    <div id="itemAutoDiv"></div>
    </td>
  </tr>
  <tr class="TableContent">
    <td colspan="3">
    <b>公共附件中的Office文档详细权限设置</b>
    <br><input type="checkbox" id="priv1" name="privNew"><label for="PRIV1">新建权限</label>
    <input type="checkbox" id="priv2" name="privEdit"><label for="PRIV2">编辑权限</label>
    <input type="checkbox" id="priv3" name="privDel"><label for="PRIV3">删除权限</label>
    <input type="checkbox" id="priv4" name="privOfficeDown"><label for="PRIV4">下载权限</label>
    <input type="checkbox" id="priv5" name="privOfficePrint"><label for="PRIV5">打印权限</label>
    </td>
  </tr>
  <tr>
    <td align="center" class="TableFooter" colspan="3">
      <input type="button" class="BigButton" value="保 存" onclick="commit()">&nbsp;&nbsp;&nbsp;&nbsp;
		<% if(isList == null || "".equals(isList)){ %>
  <input type="button"  value="关闭" class="BigButton" onclick="closeWindow();">
  <% }else{ %>
  <input type="button"  value="返回" class="BigButton" onclick="history.back();">
  <% } %>
      <input type="hidden" name="flowId" value="<%=flowId %>">
      <input type="hidden" name="seqId" value="<%=seqId %>">
      <input type="hidden" name="type" value="isWrite">
      <input type="hidden" name="fieldStr" id="fieldStr" value="">
    </td>
  </tr>
</table>
</div>
</form>

</body>
</html>