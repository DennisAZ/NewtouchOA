<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function doInit() {
  var jso = [
    {title:"发送手机短信", useTextContent:false , imgUrl:"<%=imgPath%>/notify_new.gif", contentUrl:"<%=contextPath %>/core/funcs/mobilesms/new/index.jsp" ,useIframe:true},
    {title:"短信发送管理", useTextContent:false , imgUrl:"<%=imgPath%>/msg_fwd.gif", contentUrl:"<%=contextPath %>/core/funcs/mobilesms/sendManage/index.jsp" ,useIframe:true},
    {title:"短信接收查询", useTextContent:false , imgUrl:"<%=imgPath%>/msg_back.gif", contentUrl:"<%=contextPath %>/core/funcs/mobilesms/receiveManage/index.jsp" , useIframe:true}
  ];
  buildTab(jso, 'contentDiv', 800);
}
function openDesign(){ 
}
function delFlowType(){
  
}
</script>
</head>
<body onload="doInit()">
<div id="contentDiv"></div>

</body>
</html>