<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<% 
String flowId = request.getParameter("flowId");
String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置属性</title>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/icons.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var flowId = "<%=flowId%>";
var isList = "1";
function doInit(){
  var jso = [];
  jso.push( {title:"基本属性",useTextContent:true,imgUrl:imgPath + "/asset.gif", contentUrl:"<%=contextPath %>/core/funcs/workflow/flowdesign/viewlist/setproperty/index.jsp?flowId=<%=flowId%>&seqId=<%=seqId%>&isList=1&openflag=1", useIframe:true});
  jso.push({title:"经办权限", useTextContent:true,imgUrl:imgPath + "/edit.gif", contentUrl:"<%=contextPath %>/core/funcs/workflow/flowdesign/viewlist/setPriv/setOperatePriv.jsp?flowId=<%=flowId%>&seqId=<%=seqId%>&isList=1&openflag=1" , useIframe:true});
  jso.push({title:"设置可写字段", useTextContent:true,imgUrl:imgPath + "/edit.gif", contentUrl:"<%=contextPath %>/core/funcs/workflow/flowdesign/viewlist/setField/setField.jsp?flowId=<%=flowId%>&seqId=<%=seqId%>&isList=1&openflag=1" , useIframe:true});
  jso.push({title:"设置隐藏字段", useTextContent:true,imgUrl:imgPath + "/edit.gif", contentUrl:"<%=contextPath %>/core/funcs/workflow/flowdesign/viewlist/setField/setHiddenField.jsp?flowId=<%=flowId%>&seqId=<%=seqId%>&isList=1&openflag=1" , useIframe:true});
  jso.push({title:"设置条件", useTextContent:true,imgUrl:imgPath + "/edit.gif", contentUrl:"<%=contextPath %>/core/funcs/workflow/flowdesign/viewlist/setCondition/setCondition.jsp?flowId=<%=flowId%>&seqId=<%=seqId%>&isList=1&openflag=1" , useIframe:true});
  buildTab(jso, 'contentDiv');
}
</script>
</head>
<body  onload="doInit()">
<form method="post" id="workflowForm" name="workflowForm">
<input type="hidden" value="<%=seqId %>" name="seqId" id="seqId">
<input type="hidden" value="<%=flowId %>" name="flowSeqId" id="flowSeqId">
<div id="contentDiv"></div>
</form>
</body>
</html>