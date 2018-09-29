<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
String runId = request.getParameter("runId");
String flowId = request.getParameter("flowId");
String prcsId = request.getParameter("prcsId");

String sIsManage = request.getParameter("isManage");
if (sIsManage == null ) {
  sIsManage = "false";
}
String flowPrcs = "0";
if ("1".equals(prcsId)) {
  flowPrcs = "1";
}
boolean isManage = Boolean.valueOf(sIsManage);
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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/ExchangeSelect.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
  <script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/skin.js"></script>
<script type="text/javascript" src="openWin.js"></script>
<script type="text/javascript" src="turnnextfree.js"></script>
<script type="text/javascript">
var runId = '<%=runId%>';
var flowId = '<%=flowId%>';
var prcsId= '<%=prcsId%>';
var isManage = <%=isManage%>;
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";

var prcsOpUserNameSpan;
var prcsUserNameSpan ;
var prcsOpUser;
var prcsUser;
var topFlagInput;
</script>
</head>
<body onload="doInit()">
<form name="turnForm" id="turnForm" method="post" action="">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle">&nbsp;&nbsp;<span class="big3" id="flowNameSpan">  - 转交下一步骤</span><br>
    </td>
  </tr>
</table>
<br>
<table id="prcsTable" class="TableList" width="95%">
    <tr>
      <td  class="TableHeader" nowrap colspan="2"><div style="float:left;font-weight:bold"><img src="<%=imgPath %>/workflow.gif" align="absmiddle"> 流水号流水号<%=runId %> - <span id="runNameSpan"></span></div></td>
    </tr>
   <tbody id=prcsListTbody>
   
   </tbody>
   <tr class="TableData">
       <td>第<span class=big4 style="color:red"><%=Integer.parseInt(prcsId) + 1 %></span>步：(下一步骤)<br><font id="preSetDiv" color=red style="display:none">本步骤为预设步骤</font></td>
       <td id=prcsText></td>
    </tr>
    <tbody id="preListTbody"></tbody>
   <tr class="TableControl" height=30 id="previewDiv" style='display:none'>
      <td colspan="2">
       <input type="button" value="+ 增加下一个预设步骤"  onClick="addPre()" title="预设更多后续步骤的经办人和主办人" name="button">
       <span id="delBtn" style="display:none">
       <input type="button" value="- 删除最后一个预设步骤"  onClick="delPre()" title="删除最后一个预设步骤" name="button">
      </span>
      </td>
    </tr>
   
 	   <tr class="TableContent">
      <td colspan="2" height="20px"><div style="margin-bottom:5px;font-weight:bold"> 短信提醒</div>
       <div>
       <span id="sms2RemindSpan" style="display:none"><input type="checkbox" name="sms2Remind" id="sms2Remind"><img src="<%=imgPath %>/mobile_sms.gif"><label for="sms2Remind"><u style="cursor:pointer">手机短信</u></label></span>
       <input type="checkbox" name="smsRemind" id="smsRemind"><img src="<%=imgPath %>/sms.gif"><label for="smsRemind"><u style="cursor:pointer">内部短信</u></label>

    <input type="checkbox" name="webMailRemind" id="webMailRemind" style="display:none" >
    <img src="<%=imgPath %>/webmail.gif" style="display:none" ><label for="webMailRemind" style="display:none"><u style="cursor:pointer">Internet邮件</u></label></div>
短信内容：<input type="text" onmouseover="this.focus()" onfocus="this.select()" id="smsContent" name="smsContent" value="" size="100" maxlength="100" class="SmallInput">
     </td>
    </tr>
    <tr class="TableContent">
      <td nowrap colspan="2">
      <font color=red>说明：</font>主办人是某步骤的负责人，可以编辑表单、公共附件和转交流程
      </td>
    </tr>
    <tr class="TableControl">
      <td nowrap align="center" colspan="2">
        <input type="hidden"  name="runId" id="runId" value="<%=runId %>">
        <input type="hidden"  name="OP"  id="OP" value="">
        <input type="hidden"  name="prcsId" id="prcsId" value="<%=prcsId %>">
        <input type="hidden" name="flowId" id=flowId value="<%=flowId %>">
		<input type="hidden"  name="freeItemOld" id=freeItemOld value="">
		     <input type="hidden" value="<%=sortId %>" id="sortId" name="sortId"/>

        <input type="button"  value="确认" onclick="turnNext()" class="SmallButtonW" name="mybutton">&nbsp;&nbsp;
     <% if (!isManage) { %>
      <input type="button" onclick="location='../inputform/index.jsp?skin=<%=skin %>&sortId=<%=sortId %>&runId=<%=runId %>&flowId=<%=flowId %>&prcsId=<%=prcsId %>&flowPrcs=<%=flowPrcs %>'" class=SmallButtonW value="编辑表单"/>  
     <%} %>
     <input type="button" onclick="TurnNext_forwardPage()"  class=SmallButtonW value="返回列表"/>  
       <select name="operation" style="display:none" class="BigSelect" onchange="alert('系统正在完善中....')">
       <option value="" selected>更多操作...</option>
       <option value="notify">公告通知</option>
		<option value="mail_to">邮件</option>
		<option value="SaveFile">转存</option>
		<option value="roll">归档</option>
    </select>
      </td>
    </tr>
</table>
</form>
</body>
</html>