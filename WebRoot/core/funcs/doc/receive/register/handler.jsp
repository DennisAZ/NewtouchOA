<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = YHUtility.null2Empty(request.getParameter("seqId"));
%>
<html>
<head>
<title>收文办理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript"></script>
<script type="text/Javascript">
var seqId = "<%=seqId %>";
function doInit() {
  getFlowType();
  var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocRegisterAct/getRecRegBySeqId.act?seqId="+seqId;
  var json = getJsonRs(url); 
  if(json.rtState == "0"){
    var data = json.rtData;
    $('recDocName').value = data.recDocName;
    $('recDocId').value = data.recDocId;
    if (data.recDocId) {
      attachMenuUtil("showRecDoc","doc",null,data.recDocName,data.recDocId,true,"doc","","",true);
      setSel("showRecDoc" , data.recDocId ,data.recDocName );
      $('doc').show();
    }
    $('attachmentName').value = data.attachmentName;
    $('attachmentId').value = data.attachmentId;
    if (data.attachmentId) {
      attachMenuUtil("showAtt","doc",null,$('attachmentName').value ,$('attachmentId').value,true,"att","","",true);
      setSel("showAtt" , data.attachmentId ,data.attachmentName );
    } 
  }
}
function handler(){
  if ($('flowType').value == '') {
    alert("请选择办理流程！");
    return false;
  }
  var attids = getAttIds();
  window.returnValue = {attid:attids , flowId:$('flowType').value , sort:sortId};
  window.close();
}
function getAttIds() {
  var attids = "";
  var checkboxs = document.getElementsByTagName("input");
  for (var i = 0 ;i < checkboxs.length ; i++){
    var checkbox = checkboxs[i];
    if (checkbox && (checkbox.type == 'checkbox' ||checkbox.type == 'CHECKBOX') && checkbox.checked) {
      attids += checkbox.value + ",";
    }
  }
  return attids;
}
function setSel(div , attId , attName) {
  var ids = attId.split(","); 
  var names = attName.split(",");
  var nodes = $(div).childNodes;
  for (var i = 0 ;i < nodes.length ; i++){
    var node = nodes[i];
     if (node && ids[i]) {
       $(node).insert("<input type=\"checkbox\" value=\""+ids[i]+"\" checked  name=\"attCheck\"><label for=\"attCheck\">带入办理流程</label>");
     }
  }
}
var sortId = "";
function getFlowType() {
  var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocRegisterAct/getFlowType.act";
  var json = getJsonRs(url);
  if (json.rtState = '0') {
    createList(json.rtData);
    sortId = json.rtMsrg;
  }
}
function createList(flowTypes) {
  var str = "<option value=\"\" selected>请选择流程</option>";
  for (var i =0 ;i < flowTypes.length ;i++) {
    var fl = flowTypes[i];
    var option = "<option value=\"" + fl.id + "\">"+fl.name+"</option>";
    str += option;
  }
  //if (flowTypes.length > 0 ) {
    $('flowType').update(str);
 // }
}
</script>
</head>
<body onload="doInit();">
<div id="show">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif"/><span class="big3">收文办理</span>
    </td>
  </tr>
</table>
<form  id="form1" method="post" name="form1" >
<input type="hidden" value="<%=seqId %>" id="seqId" name="seqId"/>
<table class="TableBlock" width="70%" align="center">
  <tr>
    <td nowrap class="TableContent" width="120">办理流程：<font style="color:red">*</font>&nbsp;</td>
    <td nowrap class="TableData">
       <select name="flowType" id="flowType">
      <option value="" selected>请选择流程</option> 
       </select>
    </td>
   </tr>
   <tr style="display:none" id="doc">
      <td nowrap class="TableContent">来文正文: </td>
      <td class="TableData">
        <span id="showRecDoc">
        </span>
        <input type="hidden" id="recDocId" name="recDocId">
        <input type="hidden" id="recDocName" name="recDocName">
      </td>
    </tr>
   <tr id="attr_tr">
      <td nowrap class="TableContent">附件: </td>
      <td class="TableData" id="showAttachment">
        <input type="hidden" id="attachmentId" name="attachmentId">
        <input type="hidden" id="attachmentName" name="attachmentName">
        <input type="hidden" id="ensize" name="ensize">
        <input type="hidden" id="moduel" name="moduel" value="doc">
        <span id="showAtt">
        </span>
      </td>
    </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
         <input type="button"  value="确认办理" class="BigButton"  name="button" onclick="handler();"/>&nbsp;
        <input type="button" value="关闭" class="BigButton"  name="button" onclick="window.close()"/>
        <input type="hidden" id="recId" name="recId"/>
   </td>
   </tr>
</table>
</form>
</div>
</body>
</html>