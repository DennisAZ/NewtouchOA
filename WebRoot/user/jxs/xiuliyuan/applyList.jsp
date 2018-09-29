<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检测记录</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link  href="<%=cssPath  %>/cmp/tab.css"  rel="stylesheet"  type="text/css"  />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/workflow.js"></script>
<script type="text/javascript">
function doInit() {
  var url = contextPath + "/subsys/inforesource/docmgr/sendManage/newWorkFlow.jsp?sortId=&skin=&flowId=571";
  var  ca  =    [{title:"申请查询",  contentUrl:"search.jsp",useIframe:true}
  ,{title:"新建申请",  contentUrl:url ,useIframe:true}]
  buildTab(ca  ,  "content");
}
</script>
</head>
<body onload="doInit()">
<div id="container"></div>
</body>
</html>