<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%

String flowId = request.getParameter("flowId");
if(flowId == null|| flowId.equals("")){
  flowId = "2";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/core/inc/header.jsp" %>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
function doInit() {
  var jso = [];
  if (window.navigator.appVersion.match(/9./i)!='9.') {
    jso.push({title:"图形视图",useTextContent:true,imgUrl:imgPath + "/asset.gif", contentUrl:"<%=contextPath %>/core/funcs/workflow/flowdesign/canvas.jsp?flowId=<%=flowId%>", useIframe:true});
  } else {
    jso.push( {title:"图形视图",useTextContent:true,imgUrl:imgPath + "/asset.gif", contentUrl:"<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowProcessAct/getProcessList1.act?flowId=<%=flowId%>", useIframe:true});
  }

  var buttonContent = "<div style='margin-left:360px'>"			
    									+ "<input type=button  value='关闭' onclick='closePage()' class='SmallButtonW'/></div>";
  buildTab(jso, 'contentDiv', 560 , buttonContent);
}
function LoadWindow(URL){
	loc_x=(screen.availWidth-700)/2;
	loc_y=(screen.availHeight-700)/2;
	window.open(URL,"set_process","height=460,width=700,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}
function SavePosition(){
	//alert("系统正在完善中....");
	var iframe = document.frames["contentFrame"];
  
  if(iframe.SavePosition){
    iframe.SavePosition();
  }
}

function Refresh(){
	location.href = location.href;
}
function closePage(){
  window.close();
  //hideWindow();
}
function print(){
  contentFrame.document.execCommand('Print');
}
function copy_main(){
  contentFrame.document.execCommand("selectall");
  contentFrame.document.execCommand("copy");
  alert("流程图已复制到剪贴板！");
}
</script>
</head>
<body onload="doInit()">

<div id="contentDiv"></div>
</body>
</html>