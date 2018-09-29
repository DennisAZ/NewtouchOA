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
<html>
<head>
<title>工作委托</title>
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
var baseContentUrl = contextPath + "/core/funcs/workflow/flowrun/rule";
var sortId = "<%=sortId%>";
var sortName = "<%=sortName%>";
function doInit(){
  skinObjectToSpan(flowrun_rule_index);
  if (sortName) {
    sortId = getSortIdsByName(sortName);
  }
  var jso = [{title:"委托规则", useTextContent:true, contentUrl:baseContentUrl + "/rule.jsp?sortId="+sortId+"&skin=<%=skin%>", imgUrl:imgPath + "/notify_new.gif", useIframe:true}
             ,{title:"已委托记录", useTextContent:true, contentUrl:baseContentUrl + "/from.jsp?sortId="+sortId+"&skin=<%=skin%>", imgUrl:imgPath +  "/endnode.gif", useIframe:true}
             ,{title:"被委托记录", useTextContent:true,  contentUrl:baseContentUrl + "/to.jsp?sortId="+sortId+"&skin=<%=skin%>", imgUrl:imgPath +  "/endnode.gif", useIframe:true}
             ];
  buildTab(jso, 'ruleDiv');//实例化标签页
}
</script>
</head>

<body onload="doInit()">
<div id="ruleDiv"></div>
</body>

</html>