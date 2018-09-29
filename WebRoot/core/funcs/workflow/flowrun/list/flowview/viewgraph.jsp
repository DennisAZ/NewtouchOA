<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
int styleIndex = 1;
String stylePath = contextPath + "/core/styles/style" + styleIndex;
String imgPath = stylePath + "/img";
String cssPath = stylePath + "/css";
String flowId = request.getParameter("flowId");
String runId = request.getParameter("runId");
%>
<HTML xmlns:vml="urn:schemas-microsoft-com:vml">
<HEAD>
<title></title>
<OBJECT id="vmlRender" classid="CLSID:10072CEC-8CC1-11D1-986E-00A0C955B42E" VIEWASTEXT></OBJECT>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<style>
vml\:* { FONT-SIZE: 12px; BEHAVIOR: url(#VMLRender) }
#tooltip {z-index:65535;position:absolute;border:1px solid #333;background:#f7f5d1;padding:2px 5px;color:#333;display:none;}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Tooltip.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script language="JavaScript" src="set_main.js"></script>
<script language="JavaScript">
var flowId = '<%=flowId%>';
var runId = '<%=runId%>';
var contextPath = '<%=contextPath%>';
var imgPath = '<%=imgPath%>';
</script>
</HEAD>
<BODY onload="createVml()">
<div id=content>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/workflow.gif" align="absmiddle"><span id="runMsg" class="big3"> 流水号：<%=runId %></span>&nbsp;
    </td>
  </tr>
</table>
<div>
颜色标识说明：<span style="color:#FFBC18;">■</span>未接收
&nbsp;&nbsp;<span style="color:#50C625;">■</span>办理中
&nbsp;&nbsp;<span style="color:#F4A8BD;">■</span>办理完毕
&nbsp;&nbsp;<span style="color:#D7D7D7;">■</span>预设步骤
<!-- &nbsp;&nbsp;<span style="color:#70A0DD;">■</span>子流程&nbsp;&nbsp;注：子流程可双击步骤查看流程图&nbsp;&nbsp;-->
</div>

<div id="canvas"></div>
</div>
<div id=message style=display:none></div>
<input type="hidden" value="" name="timeToId" id="timeToId">
</BODY>
</HTML>