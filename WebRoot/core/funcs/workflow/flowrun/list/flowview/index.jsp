<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String runIdStr = request.getParameter("runId");
String flowIdStr = request.getParameter("flowId");
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String skin = request.getParameter("skin");
if (skin == null) {
  skin = "";
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/core/inc/header.jsp" %>
<title></title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript"><!--
var runId = '<%=runIdStr%>';
var flowId = '<%=flowIdStr%>';
function doInit(){
  var url = contextPath + "/yh/core/funcs/workflow/act/YHMyWorkAct/getWorkMsg.act";
  var json = getJsonRs(url , "runId=" + runId + "&flowId=" + flowId);
  var type = 1;
  //取得流程实例相关信息
  if(json.rtState == '0'){
    document.title = '流程图-'+ json.rtData.flowName +'-('+ runId +')-' + json.rtData.runName;
		type = json.rtData.flowType;
  }
	var jso = [];
    if (window.navigator.appVersion.match(/9./i)!='9.') {
      jso.push({title:"图形视图",imgUrl:imgPath + "/asset.gif", contentUrl:"<%=contextPath %>/core/funcs/workflow/flowrun/list/flowview/viewgraph.jsp?runId=<%=runIdStr%>&flowId=<%=flowIdStr%>", useIframe:true});
     } else {
       var requestURL = contextPath + "/yh/core/funcs/workflow/act/YHMyWorkAct";
       var url = requestURL + "/getPrcsList1.act";
      jso.push({title:"图形视图",useTextContent:true,imgUrl:imgPath + "/asset.gif", contentUrl:url + "?flowId=<%=flowIdStr%>&runId=<%=runIdStr%>", useIframe:true});
     }
	jso.push({title:"列表视图",imgUrl:imgPath + "/edit.gif", contentUrl:"<%=contextPath %>/core/funcs/workflow/flowrun/list/flowview/viewlist.jsp?runId=<%=runIdStr%>&flowId=<%=flowIdStr%>" , useIframe:true});
	jso.push({title:"流程日志",imgUrl:imgPath + "/edit.gif", contentUrl:"<%=contextPath %>/core/funcs/workflow/flowrun/list/flowview/runlog.jsp?runId=<%=runIdStr%>&flowId=<%=flowIdStr%>" , useIframe:true});
	//如果是固定流程显示流程设计图
	if(type == 1){
      if (window.navigator.appVersion.match(/9./i)!='9.') {
        jso.push({title:"流程设计图",imgUrl:imgPath + "/workflow.gif", contentUrl:"<%=contextPath %>/core/funcs/workflow/flowrun/list/viewgraph/index.jsp?flowId=<%=flowIdStr%>" , useIframe:true});
       } else {
         jso.push({title:"流程设计图",useTextContent:true,imgUrl:imgPath + "/asset.gif", contentUrl:"<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowProcessAct/getProcessList1.act?flowId=<%=flowIdStr%>&type=1", useIframe:true});
       }
    }
	var buttonContent = "<div style='margin-left:400px'><input type=button value='超时催办提醒' onclick='remind()' class='SmallButtonC'/>" 
		+ "<input  type=button value='刷新' onclick='Refresh()' class='SmallButtonW'/>" 
		+ "<input  type=button value='打印' onclick='print()' class='SmallButtonW'/>" 
		+ "<input type=button  value='关闭' onclick='closePage()' class='SmallButtonW'/></div>";
	buildTab(jso, 'contentDiv', 560 , buttonContent); 
}
/**
 * 超时催办提醒
 */
function remind(){
  var to_id = document.frames[0].document.getElementById("timeToId");
	if (to_id) {
	  var to_val = to_id.value;
	  if(!to_val) {
		alert("无超时或者未接受的工作！");
	  } else {
		var url = contextPath + "/core/funcs/workflow/flowrun/list/sms/index.jsp?skin=<%=skin%>&sortId=<%=sortId%>&toId=" + encodeURI( to_val ) +"&content=" + encodeURI("您有已超时或未接收的待办工作，请及时办理！");
		window.open(url,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=600,height=450,left=200,top=100");
	  }
  }
  else {
	alert("请切换至图形视图或者列表视图！");
  }
}
function Refresh(){
	location.href = location.href;
}
function closePage(){
  window.close();
  //closeModalWindow('flowView');
}
function print(){
  contentFrame.document.execCommand('Print');
}
--></script>
</head>
<body onload="doInit()">

<div id="contentDiv"></div>
</body>
</html>