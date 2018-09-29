<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@page isELIgnored="false"%> 
<%@ page import="java.sql.Timestamp,java.util.Set,java.util.List,java.util.Map" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ include file="/core/inc/header.jsp" %>
<%
YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
String flowId = request.getParameter("flowId");  
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
String flowName = request.getParameter("flowName");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作流查询</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
var flowName = "<%=flowName %>";
var flowId = "<%=flowId %>";

function doQuery() {
  var url = "query.jsp";
  var tmp = encodeURIComponent(flowName);
  var versionNo = $('versionNo').value;
  document.location.href = url + "?versionNo="+versionNo+"&skin="+ skin +"&flowId="+flowId+"&sortId="+sortId+"&flowName="+ tmp;
}
function doInit(){
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFormVersionAct/getVersionByFlow.act";
  var json = getJsonRs(url, "flowId=" + flowId) ;
  if (json.rtState == '0') {
    var aRunId = json.rtData;
    var ss = aRunId.split(",");
    for (var i=0;i<ss.length;i++) {
      var runId = ss[i];
      var option = new Element("option");
      option.value = runId;
      option.update(runId);
      $('versionNo').appendChild(option);
    }
  }
}
</script>
</head>
<body onload="doInit()">
<div>
<img src="<%=imgPath %>/infofind.gif" align="absmiddle">&nbsp;<span class="big3" ><span id="span1"> </span> 工作流高级查询：选择需要查询的表单版本&nbsp;&nbsp;<input type="button"  value="返回" class="BigButton" onClick="location='flowList.jsp?skin=<%=skin %>&sortId=<%=sortId %>';"></span>
</div>
<form>
<table id="flowTable" border="0" width="50%"  class="TableList"  >
  <tr class="TableLine1">
  <td align="center"> 
  流程：<%=flowName %><br>
  </td>
  </tr><tr class="TableLine1">
  <td align="center"> 
请选择表单版本：<select id="versionNo" name="versioinNo"></select>
  </td>
  </tr><tr class="TableLine1">
  <td align="center"> 
<input type="button"  value="查询" class="BigButton" onClick="doQuery();">
  </td>
  </tr></table>

</form>
</body>
</html>