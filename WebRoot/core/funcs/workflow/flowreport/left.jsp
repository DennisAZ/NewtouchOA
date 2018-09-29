<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
 <%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>报表设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">

<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/flowreport/js/report.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/DTree1.0.js" ></script>
<script language="JavaScript">

var tree =null;
function doInit(){
 tree = new DTree({bindToContainerId:'flow_list'
   ,requestUrl:contextPath + '/yh/core/funcs/workflow/act/YHFlowSortAct/getSortList.act?id='
   ,isOnceLoad:false//不是同步加载
   ,linkPara:{clickFunc:openNode}//为每个结点的a标签加下点击事件
 });
 tree.show(); 
}
function openNode(nodeId) {
  if (nodeId.indexOf("T") >= 0) {
    id = nodeId.substr(1);
    parent.mainFrame.location.href="list.jsp?fId="+id;
  } 
}
</script>
</head>
<body class="bodycolor" onLoad="doInit();">

<div id="left" style="float:left;width:200px;padding-left:10px;padding-bottom:20px;">
  <div id="title">
    <img src="<%=imgPath%>/report.gif" align="absmiddle"><span class="big3"> 流程报表设置</span>
  </div>
<br>
     <div id="flow_list">
     </div>
</div>

</body>
</html>
