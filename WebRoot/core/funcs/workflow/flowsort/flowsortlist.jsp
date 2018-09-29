<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列出流程分类</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
function delFlowSort(seqId, haveChild, sortParent) {
  if(!confirm("确认删除！")) {
    return ;
  }

  var url = "<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowSortAct/deleteFlowSort.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    alert(rtJson.rtMsrg); 
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}
function doInit() {
  var url = "<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowSortAct/listFlowSort.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    for (var i = 0 ;i < data.length ;i++) {
      var d = data[i];
      addRow(d , i);
    }
  }
}
function addRow(data , i) {
  var tmp = "TableLine1";
  if (i % 2 == 0 ) {
    tmp = "TableLine2";
  }
  var tr = new Element("tr" , {"class":tmp});
  $('flowSortList').appendChild(tr);
  var td =  new Element("td");
  td.align = "left";
  td.update(data.sortNo);
  tr.appendChild(td);
  var td2 =  new Element("td");
  td2.align = "left";
  td2.update(data.text);
  tr.appendChild(td2);

  var td3 =  new Element("td");
  td3.align = "center";
  td3.update(data.flowTypeCount);
  tr.appendChild(td3);

  var td4 =  new Element("td");
  td4.align = "left";
  var op = "<a href='"+ contextPath +"/core/funcs/workflow/flowsort/flowsortinput.jsp?seqId="+data.value+"&sortParent="+data.sortParent+"'>编辑</a>";
  if (data.flowTypeCount == 0 && !data.isHaveChild) {
    op += "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='delFlowSort("+data.value+")'>删除</a>";
  }
  td4.update(op);
  tr.appendChild(td4);
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3">
  <tr>
    <td>
      <img src="<%=imgPath %>/notify_open.gif" />
      <span class="big3"> 管理流程分类</span>
    </td>
  </tr>
</table>

<table cellscpacing="1" cellpadding="3" width="500" class=TableList align="center">
  <tr class="TableHeader">
    <td>序号</td>
    <td>名称</td>
    <td>流程数量</td>
    <td>操作</td>
  </tr>
  <tbody id="flowSortList"></tbody>
</table>
</body>
</html>