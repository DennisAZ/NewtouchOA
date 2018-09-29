<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ include file="/core/inc/header.jsp" %>
<%
 String sortId = request.getParameter("sortId");
 String sortName = request.getParameter("sortName");
 
 if (sortId == null) {
   sortId = "";
 }
 if (sortName == null) {
   sortName = "";
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
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var sortName = "<%=sortName%>";
var isParent = true;
function doInit() {
  if (sortName) {
    sortId = getSortIdsByName(sortName);
  }
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowSortAct/getFlowType.act?sortId=" + sortId;
  var json = getJsonRs(url);
  var flowType = [];
  if (json.rtState == '0') {
    flowType = json.rtData;
  }
  var baseContentUrl = contextPath + "/core/funcs/workflow/flowrun/list";
  var jso = [{title:"我的发文", useTextContent:true, contentUrl:baseContentUrl + "/index1.jsp?type=3&sortId=" + sortId + "&flowId=&noOperate=true&skin=<%=skin%>", useIframe:true}];
  var j = 1;
  for (var i = 0 ;i < flowType.length ; i ++) {
    var node = flowType[i];
    if (node) {
      var newTab = {title:node.flowName, useTextContent:true, contentUrl:contextPath + "/subsys/inforesource/docmgr/sendManage/newWorkFlow.jsp?sortId=" + sortId + "&skin=<%=skin%>&flowId="+ node.flowId ,useIframe:true };
      jso[j] = newTab;
      j++; 
    }
  }
  buildTab(jso, 'listDiv');//实例化标签页
}
</script>
</head>
<body onload="doInit()">
<div id="listDiv"></div>
</body>
</html>