<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
  <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
  <%@ page import="yh.core.funcs.doc.receive.data.YHDocConst" %>
<%
String flowId = request.getParameter("flowId");  
String sortId = request.getParameter("sortId");
String webroot = request.getRealPath("/");
String sortName = YHDocConst.getProp(webroot  , YHDocConst.DOC_RECEIVE_FLOW_SORT) ;
String skin = "receive";
if (sortId == null) {
  sortId = "";
}
if (flowId == null) {
  flowId = "";
}
if (sortName == null) {
  sortName = "";
}
  //request.getParameter("skin");
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/skin.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var flowId = "<%=flowId%>";
var sortName = "<%=sortName%>";
var baseContentUrl = contextPath + "/core/funcs/doc/flowrunRec/list";
function doInit(){
  if (sortName) {
    sortId = getSortIdsByName(sortName);
  }
  var url = contextPath + "/yh/core/funcs/doc/act/YHMyWorkAct/hasWork.act?sortId=" + sortId + "&flowId=" + flowId;
  var json = getJsonRs(url) ;
  var flag = false;//没有未接收的工作
  if (json.rtState == '0') {
    flag = json.rtData;
  } 
  var jso = [
             {title:'未接收' ,useTextContent:true, contentUrl:baseContentUrl + "/index1.jsp?type=1&sortId=" + sortId + "&skin=<%=skin%>&flowId=" + flowId, useIframe:true}
             ,{title:'办理中' ,useTextContent:true, contentUrl:baseContentUrl + "/index1.jsp?type=2&sortId=" + sortId + "&skin=<%=skin%>&flowId=" + flowId, useIframe:true}
             ,{title:'已办结' ,useTextContent:true,  contentUrl:baseContentUrl + "/index1.jsp?type=3&sortId=" + sortId + "&skin=<%=skin%>&flowId=" + flowId, useIframe:true}
             ];
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
