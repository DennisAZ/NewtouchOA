<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect.js"></script>
<script type="text/javascript">
function doInit() {
  var jso = [
          	  {title:"图形视图",useTextContent:true, contentUrl:"<%=contextPath %>/raw/lh/workflow/canvas.jsp", useIframe:true}
             ,{title:"列表视图", useTextContent:true, contentUrl:"<%=contextPath %>/raw/lh/workflow/prcslist.jsp" , useIframe:true}
             ];
  buildTab(jso, 'contentDiv', 800);
}
</script>
</head>
<body onload="doInit()">
<div id="contentDiv"></div>
</body>
</html>