<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
 <%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>报表设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/Accordion.css">

<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/allreport/js/report.js"></script>
<script language="JavaScript">

function doInit(){
	loadData();
}
</script>
</head>
<body class="bodycolor" onLoad="doInit();">

<div id="left" style="float:left;width:200px;padding-left:10px;padding-bottom:20px;">
  <div id="title">
    <img src="<%=imgPath%>/report.gif" align="absmiddle"><span class="big3"> 流程报表设置</span>
  </div>
<br>
  <div>
     <div id="flow_list">
     </div>
  </div>
</div>

</body>
</html>
