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
 String flowId = request.getParameter("flowId");
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
var skin = "<%=skin%>";
var requestUrl  = contextPath + "/yh/core/funcs/workflow/act/YHFlowRunAct";
function createWork(flowId){
  var url = requestUrl + "/createNewWork.act";
  var json = getJsonRs(url , "flowId=" + flowId + "&skin=" + skin);
  if(json.rtState == "0"){
    location = contextPath + "/core/funcs/workflow/flowrun/list/inputform/index.jsp?skin=<%=skin%>&sortId="+ sortId +"&runId=" + json.rtData + "&flowId=" + flowId + "&prcsId=1&flowPrcs=1&isNew=1";
  }else{
    alert(json.rtMsrg);
  }
}
</script>
</head>
<body onload="createWork(<%=flowId%>)">
</body>
</html>