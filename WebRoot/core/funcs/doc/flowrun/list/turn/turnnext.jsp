<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ include file="/core/inc/header.jsp" %>
<%
String runId = request.getParameter("runId");
String flowId = request.getParameter("flowId");
String prcsId = request.getParameter("prcsId");
String flowPrcs = request.getParameter("flowPrcs");
String sIsManage = request.getParameter("isManage");
boolean isManage = false;
if (sIsManage != null || "".equals(sIsManage)) {
  isManage = Boolean.valueOf(sIsManage);
}
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
<title>工作办结</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
 <link rel="stylesheet" href ="turn_style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
  <script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/skin.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="turnnext.js"></script>
<script type="text/javascript">
var runId = '<%=runId%>';
var flowId = '<%=flowId%>';
var prcsId = '<%=prcsId%>';
var flowPrcs = '<%=flowPrcs%>';
var isManage =   <%=isManage%>;
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";

var data = [{name:'表单',icon:imgPath + "/sys_config.gif",action:setAction,extData:0}
,{ name:'流程图',icon:imgPath + "/workflow.gif",action:setAction,extData:1}
,{ name:'流程设计图',icon:imgPath + "/asset.gif",action:setAction,extData:2}
]
function addUser(i , userFilter , childFlow , parentFlowId , backPrcs){
  if (userFilter == null || userFilter == "") {
    userFilter = "0";
  }
  if (childFlow == null || childFlow == "") {
    childFlow = "0";
  }
  if (!parentFlowId) {
    parentFlowId = "";
  }
  if (!backPrcs) {
    backPrcs = "";
  }
  var chooseType = window.showModalDialog(contextPath + "/core/funcs/doc/flowrun/list/turn/userSelect/FlowUserSelect.jsp?prcsId=" + i + "&userFilter=" + userFilter + "&childFlow=" + childFlow + "&parentFlowId=" + parentFlowId+ "&backPrcs=" + backPrcs,window,"dialogLeft=480px;dialogTop=350px;dialogWidth=520px;dialogHeight=400px;status=no;resizable=no"); 
}

function setAction(){
  var flag = arguments[2];
  
  if (flag == 0) {
    formView(runId, flowId);
  } else if (flag == 1) {
    var title = "流程图-"+flowName+" (" + runId + ")" +  runName;
    flowView(runId , flowId ,title  , sortId , skin);
  } else {
	viewGraph(flowId);
  }
}

</script>
</head>
<body onload="doInit()"  onunload="">
<div id="outPass" style="display:none">
<table width="100%" cellspacing="0" cellpadding="3" border="0" class="small">
  <tbody><tr>
    <td class="Big"><img align="absmiddle" src="<%=imgPath %>/green_arrow.gif"/>&nbsp;&nbsp;<span class="big3" id="flowNameSpan">  - 转交下一步骤</span>
    </td>
  </tr>
</tbody></table>
<form name="turnForm" id="turnForm" method="post" action="">
<table width="100%" border="0" class="TableList">
    <tbody><tr class="TableHeader">
      <td nowrap height="25" colspan="2">
      	<div style="float: left;"><img align="absmiddle" src="<%=imgPath %>/workflow.gif"/> 流水号<%=runId %> - <span id="runNameSpan"></span> 
      	<a onmouseover="menu = new Menu({bindTo:$('view_select') , menuData:data , attachCtrl:true});menu.show(event);" id="view_select" href="javascript:void(0)">查看相关图表 <img align="absMiddle" src="<%=imgPath %>/menu_arrow_down.gif"/>
      	</a>
       </div>
      </td>
    </tr>

<tr height="25">
  <td  class="TableContent" colspan="2"> 当前步骤为第<b><span class="big4"><%=prcsId %></span></b>步：<span id="prcsNameSpan" style="text-align:left"></span><span  id="userNameStrSpan"></span> </td>
</tr>
 <tr id="gatherMsg" style="display:none" align=center><td>
 <table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td class="msg info">
            此步骤为强制合并步骤，尚有步骤未转交至此步骤，您不能继续转交下一步！
            </td>
        </tr>
    </tbody>
    
</table></td>
</tr>
   <tr class="TableData"  id="nextPrcsTr">
     <td colspan="2"  id="nextPrcsTd">
     
      <div id="selectPrcsTitle" style="margin: 3px 0pt 3px 3px; font-weight: bold;">
     	<img align="absmiddle" src="<%=imgPath %>/workflow.gif"/> 选择下一步骤
     	    <a title="说明：主办人是某步骤的负责人，可以编辑表单、公共附件和转交流程" href="javascript:void(0)">说明</a>
     </div>

  </td>
</tr>
</tbody>
<tbody id="prcsList"></tbody>
    <tr id="need_remind">
      <td colspan="2">
  <div  class="TableHeader PrcsName" style="margin-bottom: 5px; font-weight: bold;display:none"> 短信提醒以下人员</div>
  <div><span style="margin-right: 5px;display:none" id="divSmsNex"><b>短信提醒</b>：  <input type="checkbox" style="display:none" name="sms2RemindNext" id="sms2RemindNext"><img style="display:none" id="sms2RemindNextImg" align="absmiddle" title="手机短信提醒" src="<%=imgPath %>/mobile_sms.gif"/> 
  <input type="checkbox"  id="smsRemindNext" name="smsRemindNext"/><img align="absmiddle" title="内部短信提醒" src="<%=imgPath %>/sms.gif"/>   
   <input type="checkbox" id="webMailRemindNext" name="webMailRemindNext" style="display:none" /><img style="display:none"  align="absmiddle" title="Internet邮件提醒" src="<%=imgPath %>/webmail.gif"/>    </span>
    <span style="margin-right: 5px;display:none" id="divSmsStart"> 
    <span title=""  class="underline"><b>发起人</b>：</span>
    <input type="checkbox"  style="display:none" name="sms2RemindStart" id="sms2RemindStart" ><img  style="display:none" id="sms2RemindStartImg" align="absmiddle" title="手机短信提醒" src="<%=imgPath %>/mobile_sms.gif"/> 
    <input type="checkbox" id="smsRemindStart" name="smsRemindStart"/><img align="absmiddle" title="内部短信提醒" src="<%=imgPath %>/sms.gif"/>    
    <input type="checkbox" id="webMailRemindStart" name="webMailRemindStart" style="display:none" /><img style="display:none"  align="absmiddle" title="Internet邮件提醒" src="<%=imgPath %>/webmail.gif"/>    </span>
    <span style="margin-right: 5px;display:none" id="divSmsAll"> <span title="" class="underline"><b>全部经办人</b>：</span>
    <input type="checkbox"  style="display:none" name="sms2RemindAll" id="sms2RemindAll"><img  style="display:none" id="sms2RemindAllImg" align="absmiddle" title="手机短信提醒" src="<%=imgPath %>/mobile_sms.gif"/> 
    <input type="checkbox" id="smsRemindAll" name="smsRemindAll"/><img align="absmiddle" title="内部短信提醒" src="<%=imgPath %>/sms.gif"/>    
    <input type="checkbox" id="webMailRemindAll" name="webMailRemindAll" style="display:none" /><img style="display:none"  align="absmiddle" title="Internet邮件提醒" src="<%=imgPath %>/webmail.gif"/>  </span>
  <div>
  <div  id="smsContentDiv" style="margin-top: 5px;">提醒内容：<input type="text" class="SmallInput" size="70" value="" id="smsContent" name="smsContent" onfocus="this.select()" onmouseover="this.focus()"/></div>
  </div></div>
   </td>
 </tr>
 <tr id="allNoPassTr" style="display:none" align=center><td>
 <table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td class="msg info">
            无符合条件的下一步骤！            </td>
        </tr>
    </tbody>
</table></td>
</tr>
 <tr class="TableControl" id="tableControl">
   <td nowrap align="center" colspan="2">
     <input type="hidden" value="" id=prcsTo name="prcsTo"/>
     <input type="hidden" name="runId" value="<%=runId%>" id="runId"/>
     <input type="hidden"  value="<%=flowId%>" name="flowId" id="flowId"/>
     <input type="hidden" value="" name="OP"/>
     <input type="hidden" value="" name="topFlagOld" id="topFlagOld"/>
     <input type="hidden"  name="prcsId" value="<%=prcsId%>" id="prcsId"/>
     <input type="hidden"  name="flowPrcs" value="<%=flowPrcs%>" id="flowPrcs"/>
     <input type="hidden" value="" id="prcsChoose" name="prcsChoose"/>
     <input type="hidden" value="<%=sortId %>" id="sortId" name="sortId"/>
     <input type="hidden" value="<%=skin %>" id="skin" name="skin"/>
     <input type="button" onclick="" class=SmallButtonW value="确认" name="mybutton" id="operateButton"/> 
     <% if (!isManage) { %>
     <!--<input type="button" onclick="location='../inputform/index.jsp?skin=<%=skin %>&sortId=<%=sortId %>&runId=<%=runId %>&flowId=<%=flowId %>&prcsId=<%=prcsId %>&flowPrcs=<%=flowPrcs %>'" class=SmallButtonW value="编辑表单"/>   --> 
     <%} %>
    <!-- 
     <input type="button" onclick="TurnNext_forwardPage()"  class=SmallButtonW value="返回列表"/>  
     -->
   </td>
 </tr>
</tbody></table>
</form>
</div>
<div id="noOutPass" align=center style="display:none">
   <table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="outMsgInfo" class="msg info">
            </td>
        </tr>
    </tbody>
</table>
</div>
<div>
</div>
</body>
</html>