<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
<%
String flowId = request.getParameter("flowId");  
String sortId = request.getParameter("sortId");
String sortName = request.getParameter("sortName");
if (sortId == null) {
  sortId = "";
}
if (flowId == null) {
  flowId = "";
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
<html>
<head>
<title>工作流</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var flowId = "<%=flowId%>";
var sortName = "<%=sortName%>";
var baseContentUrl = contextPath + "/core/funcs/workflow/flowrun/list";
function doInit(){
  if (sortName) {
    sortId = getSortIdsByName(sortName);
  }
  var url = contextPath + "/yh/core/funcs/workflow/act/YHMyWorkAct/hasWork.act?sortId=" + sortId + "&flowId=" + flowId;
  var json = getJsonRs(url) ;
  var flag = false;//没有未接收的工作
  if (json.rtState == '0') {
    flag = json.rtData;
  } 
  var jso = [
             { useTextContent:true, contentUrl:baseContentUrl + "/index1.jsp?type=1&sortId=" + sortId + "&skin=<%=skin%>&flowId=" + flowId, useIframe:true}
             ,{ useTextContent:true, contentUrl:baseContentUrl + "/index1.jsp?type=2&sortId=" + sortId + "&skin=<%=skin%>&flowId=" + flowId, useIframe:true}
             ,{ useTextContent:true,  contentUrl:baseContentUrl + "/index1.jsp?type=3&sortId=" + sortId + "&skin=<%=skin%>&flowId=" + flowId, useIframe:true}
             ,{ useTextContent:true,  contentUrl:baseContentUrl + "/allList.jsp?sortId=" + sortId + "&skin=<%=skin%>", useIframe:true}
             ];
  setTabTitle(flowrun_list_index, jso);
  if (!flag) {
    jso[1].isShow = true;
  }
  buildTab(jso, 'listDiv');//实例化标签页
}

</script>
</head>

<body onload="doInit()">
<div id="listDiv"></div>
</body>

</html>
