<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ include file="/core/inc/header.jsp" %>
<%
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
<title>发文拟稿</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/utility.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
function doInit() {
  loadFileName();
}
var fileName = [];
function loadFileName() {
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocAct/getDocType.act" ;
  var json = getJsonRs(url);
  if (json.rtState == "0") {
    fileName = json.rtData;
    for (var i = 0 ;i < json.rtData.length ;i++) {
      var data = json.rtData[i];
      var op = new Element("option");
      op.value = data.seqId;
      op.innerHTML = data.name;
      $('docType').appendChild(op);
    }
  }
}
var docWords = [];
function loadWord(type) {
  $('docWord').update("<option value=\"\">文件字</option>");
  $('flowId').value = "";
  if (!type) {
    return ;
  }
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocAct/getDocWordByType.act";
  var json = getJsonRs(url , "type=" + type);
  if (json.rtState == "0") {
    $('flowId').value = json.rtData.flowType;
    docWords = json.rtData.docWords;
    for (var i = 0 ;i < docWords.length ;i++) {
      var data = docWords[i];
      var op = new Element("option");
      op.value = data.seqId;
      op.innerHTML = data.name;
      $('docWord').appendChild(op);
    }
  }
}
function getName(seqId , datas) {
  for (var i = 0 ;i < datas.length ;i++) {
    var data = datas[i];
    if (seqId == data.seqId) {
      return data.name;
    }
  }
}
function getStyle(seqId) {
  for (var i = 0 ;i < docWords.length ;i++) {
    var data = docWords[i];
    if (seqId == data.seqId) {
      return data.indexStyle;
    }
  }
}
function createNewDoc(){
  if (!$('docWord').value) {
    alert("请选择文件字！");
    return ;
  }
  if (!$('flowId').value || !$('docType').value ) {
    alert("请为文件类型:"+ getName($('docType').value , fileName)+"绑定相应的流程！");
    return ;
  }
  var flowId = $('flowId').value;
  var word = getName($('docWord').value , docWords);
  var style= getStyle($('docWord').value);
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocAct/createWorkFlow.act";
  var json = getJsonRs(url , "flowId=" + flowId + "&skin=" + skin + "&word=" + word + "&docStyle=" + style + "&docType=" + $('docType').value + "&wordId=" + $('docWord').value);
  if(json.rtState == "0"){
    location = contextPath + "/core/funcs/doc/flowrun/list/inputform/index.jsp?skin=<%=skin%>&sortId="+ sortId +"&runId=" + json.rtData.runId + "&flowId=" + flowId + "&prcsId=1&flowPrcs=1&isNew=1";
  } else {
    alert(json.rtMsrg);
  }
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"><span class="big1" id="notifytitle"> 新建公文</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<form action="" method="post" name="form_new">
<table width="60%" class="TableBlock" align="center">
<tr>
    <td class="TableContent">文件类型：</td>
    <td class="TableData">
    <select name="docType" id="docType" class="BigSelect" onchange="loadWord(this.value)">
    <option value="">--请选择文件类型--</option>
     </select>
    </td>
  </tr>
  <tr>
    <td class="TableContent">文&nbsp;&nbsp;件&nbsp;&nbsp;字：</td>
    <td class="TableData">
    <select name="docWord" id="docWord" class="BigSelect" >
    <option value="">--请选择文件字--</option>
     </select>
    </td>
  </tr>
  <tr class="TableControl">
    <td colspan=2 align="center">
    <input type="button" class="BigButton" value="新建公文" onclick="createNewDoc()">
    <input type="hidden" id="flowId" name="flowId" value=""/>
    </td>
  </tr>
</table>
</form>
</body>
</html>