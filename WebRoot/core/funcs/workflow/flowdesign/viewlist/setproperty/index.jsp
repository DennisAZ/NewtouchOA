<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<% 
String flowId = request.getParameter("flowId");
String seqId = request.getParameter("seqId");
String isList = request.getParameter("isList");
if(isList == null ){
  isList = "";
}
if(seqId == null){
  seqId = "";
}
boolean isEdit = false;
if(seqId != null && !seqId.equals("")){
  isEdit = true;
}
String openflag = request.getParameter("openflag");
if(openflag == null ){
  openflag = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置基本属性</title>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/icons.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="resources/css/ext-yhtheme.css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/adapter/prototype/ext-prototype-adapter.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/ext-all.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/frame/ux/Window.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/frame/ux/CardPanel.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/frame/ux/CardPanelNoHeader.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/frame/js/indexframe.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/frame/js/index.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/ExchangeSelect1.0.js" ></script>
<script type="text/javascript" src="index.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var flowId = "<%=flowId%>";
var isEdit = <%=isEdit%>;
var isList = "<%=isList%>";
var openflag = "<%=openflag %>";
</script>
</head>
<body  onload="doInit()">
<form method="post" id="workflowForm" name="workflowForm">
<input type="hidden" value="0" name="openedAutoSelect" id="openedAutoSelect">
<input type="hidden" value="0" name="openedFlowDispatch" id="openedFlowDispatch">
<input type="hidden" value="0" name="openedWarnDispatch" id="openedWarnDispatch">
<input type="hidden" value="0" name="openedOtherDispatch" id="openedOtherDispatch">

<input type="hidden" value="<%=seqId %>" name="seqId" id="seqId">
<input type="hidden" value="<%=flowId %>" name="flowSeqId" id="flowSeqId">
<div id="contentDiv"></div>

</form>
</body>
</html>