<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String runId = request.getParameter("runId");
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
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
var runId = "<%=runId %>";
var docStr = "";
function doInit() {
  var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDocAct/getDocNum.act";
  var json = getJsonRs(url , "runId=" + runId);
  if (json.rtState == "0") {
    var data = json.rtData;
    $('docWord').value = data.docWord;
    $('docYear').value = data.docYear;
    $('docWordSeqId').value = data.docWordSeqId;
    $('docNum').value = data.docNum;
    docStr = data.indexStyle;
    setDoc(data.docWord , data.docYear, data.docNum)
  }
}
function setDoc(docWord , docYear,docNum) {
  doc1 = docStr.replace("${文件字}",docWord);
  doc1 = doc1.replace("${年号}",docYear);
  doc1 = doc1.replace("${文号}",docNum);
  $('doc').value = doc1;
}
function getNum(docYear) {
  var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDocAct/getNum.act";
  var json = getJsonRs(url , "docWord=" + $('docWordSeqId').value + "&docYear=" + docYear);
  if (json.rtState == "0") {
    var data = json.rtData;
    $('docNum').value = data;
    setDoc($('docWord').value , docYear, data)
  }
}
function sendNum() {
  var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDocAct/sendNum.act";
  var json = getJsonRs(url , $('form1').serialize());
  if (json.rtState == "0") {
    alert("文号分配成功！");
    top.win.closeGiveNumPage();
  }
}
</script>
</head>
<body onload="doInit()">
<form id="form1" name="form1">
<table width="60%" class="TableBlock" align="center">
  <tr>
    <td class="TableContent">文件字：</td>
    <td class="TableData">
    <input name="docWord" id="docWord" type="text" readonly>
    </td>
  </tr>
  <tr>
    <td class="TableContent">年度：</td>
    <td class="TableData">
    <select name="docYear" id="docYear" onchange="getNum(this.value)">
    <option value="2010">2010</option>
    <option value="2011" selected>2011</option>
    <option value="2012">2012</option>
    <option value="2013">2013</option>
    </select>
    </td>
  </tr>
  <tr>
    <td class="TableContent">编号：</td>
    <td class="TableData">
    <input name="docNum" id="docNum" type="text" readonly>
    </td>
  </tr>
   <tr>
    <td class="TableContent">文号：</td>
    <td class="TableData">
    <input name="doc" id="doc" type="text" readonly/>
    </td>
  </tr>
  <tr class="TableControl">
    <td colspan=2 align="center">
    <input type="hidden" name="docWordSeqId" id="docWordSeqId" />
    <input type="hidden" name="runId" id="runId" value="<%=runId %>"/>
    <input type="button" class="BigButton" value="分配文号" onclick="sendNum()">
    </td>
  </tr>
</table>
</form>
</body>
</html>