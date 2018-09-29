<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
    <%
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String sortName = request.getParameter("sortName");
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
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/syslog/js/sysyearlog.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<title></title>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var sortName = "<%=sortName%>";
function doInit(){
  if (sortName) {
    sortId = getSortIdsByName(sortName);
  }
  var jso = [
             {contentUrl:"<%=contextPath %>/core/funcs/workflow/flowrun/overtime/overquery.jsp?sortId="+sortId+"&skin=<%=skin%>",  useIframe:true}
              ,{contentUrl:"<%=contextPath %>/core/funcs/workflow/flowrun/overtime/overtotal.jsp?sortId="+sortId+"&skin=<%=skin%>", useIframe:true}
           ];
  setTabTitle(flowrun_overtime_index, jso);
  buildTab(jso, 'syslog');
}
</script>
</head>
<body onload="doInit()">
<div id = "syslog"></div>
</body>
</html>