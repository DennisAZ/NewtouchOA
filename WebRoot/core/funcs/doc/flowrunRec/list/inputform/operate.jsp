<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson,yh.core.global.YHSysProps" %>
<%
String runId = request.getParameter("runId");
String flowId = request.getParameter("flowId");
String prcsId = request.getParameter("prcsId");
String flowPrcs = request.getParameter("flowPrcs");
String isNewStr = request.getParameter("isNew");

YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
int userId = loginUser.getSeqId();
int isNew = 0 ;
if(isNewStr != null && !"".equals(isNewStr)){
  isNew = Integer.parseInt(isNewStr);
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
<title>操作区域</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/Menu.js" ></script>
   <script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/skin.js"></script>
<script type="text/javascript">
var from = parent.main;
var sortId = "<%=sortId%>";
var runId = '<%=runId%>';
var flowId = '<%=flowId%>';
var prcsId = '<%=prcsId%>';
var flowPrcs = '<%=flowPrcs%>';
var skin = "<%=skin%>";
var isLoaded = false;

var par = "runId=" + runId + "&flowId=" + flowId + "&prcsId=" + prcsId + "&flowPrcs=" + flowPrcs + "&sortId="+ sortId;
function comeBack () {
  from.mouse_is_out = false;
  var url =   contextPath + "/core/funcs/doc/flowrunRec/list/index.jsp?skin="+ skin +"&sortId=" + sortId;
  parent.location = url;
}
function showMore() {
  $('focusSpan').show();
  $('focusConSpan').show();
  $('favSpan').show();
  
  $('moreA').update("<<<");
  $('moreA').onclick = function () {
    closeMore();
  }
}
function closeMore(){
  $('focusSpan').hide();
  $('focusConSpan').hide();
  $('favSpan').hide();
  
  $('moreA').update("更多>>");
  $('moreA').onclick = function () {
    showMore();
  }
}
function focus(){
  if(window.confirm(tooltipMsg1)) {
    var url = contextPath + "/yh/core/funcs/doc/flowrunRec/act/YHFlowManageAct/focus.act";
    var json = getJsonRs(url, par) ;
    if (json.rtState == '0') {
      alert(json.rtMsrg); 
    }
  }
}

function addFav() {
  var url = contextPath + "/core/funcs/doc/flowrunRec/list/print/index.jsp?runId="+runId + "&flowId="+flowId;
  var urlDesc = from.runName;
  url = encodeURIComponent(url);
  urlDesc = encodeURI(encodeURI(urlDesc));
  var URL = contextPath + "/core/funcs/setdescktop/fav/add.jsp?urlDesc=" + urlDesc + "&url=" + url;
  window.open(URL);
}
function showFocus() {
  from.showFocus(true);
}
function doInit(){
  skinObjectToSpan(flowrun_list_inputform_operate);
  isLoaded = true;
}
</script>
</head>
<body style="margin:0px;padding:0px" onload="doInit()">
<div class="TableControl" id="wpiroot"  style="height:30px;display:none">
<DIV style="FLOAT: left; ">&nbsp;
&nbsp;&nbsp;<span id="isOperDesign">
<a href="javascript:from.openPrintForm()"><img src="<%=imgPath %>/print.gif"/>打印表单</a></span>
<span>
<a href="javascript:from.openWorkFlow()"><img src="<%=imgPath %>/workflow.gif"/>查看流程图</a></span>&nbsp; 
<span style="display:none" id="addUser">
  <a href="javascript:from.addUser()" title="增加此步骤经办人"><img  width=16 height=16 src="<%=imgPath %>/notify_new.gif" align=absmiddle>增加经办人</a>
</span>
<span style="display:none" id="focusSpan">
<a href="javascript:focus()"><img src="<%=imgPath %>/focus.gif"/><span id="span1"></span></a></span>&nbsp; 
<span style="display:none"  id="focusConSpan">
<a href="javascript:void(0)" onclick="showFocus()"><img src="<%=imgPath %>/focus.gif"/>查看关注情况</a></span>&nbsp;
<span style="display:none"  id="favSpan">
<a href="javascript:void(0)" onclick="addFav()"><img src="<%=imgPath %>/favorites.gif"/>收藏</a></span>&nbsp;
<span>
<a href="javascript:void(0)" id="moreA" onclick="showMore()">更多>></a></span>&nbsp; 
</DIV>

<DIV style="FLOAT: right">
<span id="turnSpan"  style='display:none'><input value="下一步" id="turnNextButton" class="SmallButton" type="button" onclick="from.saveForm(1)"/></span>
<span id="span2">
<span id="endSpan"  style='display:none'><input value="结束流程" id="endButton"  class="SmallButtonW" type="button" /></span>
</span>
<span id="backSpan"  style='display:none'><input value="回退"  class="SmallButtonW" type="button" /></span>
<span id="saveSpan"  style='display:none'><input value="保存"  class="SmallButtonW" type="button" onclick="from.saveForm(0)"/></span>
<span id="span4">
<span id="saveAndBackSpan"  style='display:none'><input value="保存并返回" type="button" class="SmallButtonC" onclick="from.saveForm(2)"/></span>
</span>
<span id="handlerCompleteSpan" style='display:none'><input value="办理完毕" type="button" class="SmallButtonW" onclick="from.finishRun()"/></span>
<span id="span5">
<span id="cancleSpan" style="display:none"><input value="取消"  class="SmallButtonW" type="button" onclick="from.cancelRun();"/></span>
</span>
<span id="span6">
<span id="comebackSpan" style="display:none"><input value="返回"  class="SmallButtonW" type="button" onclick="comeBack()"/></span>
</span>
<span id="span7"></span>
&nbsp; &nbsp; 
</DIV>
</div>
</body>
</html>