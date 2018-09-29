<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
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
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
var tree = null;
function doInit(){
  skinObjectToSpan(flowrun_new_left);
  tree = new DTree({bindToContainerId:'sortList'
    ,requestUrl:contextPath + '/yh/core/funcs/workflow/act/YHFlowSortAct/getSortListR.act?id='
    ,isOnceLoad:false//不是同步加载
    ,linkPara:{clickFunc:openNode}//为每个结点的a标签加下点击事件
  });
  tree.show(); 
}
function openNode(nodeId) {
  if (nodeId.indexOf("F") >= 0) {
    id = nodeId.substr(1);
    parent.mainFrame.location= "edit.jsp?flowId="+ id + "&sortId=" + sortId + "&skin=" + skin;
  }
}
function actionFuntion(tmp){ 
  
}
</script>
</head>
<body onload='doInit()'> 
<br>
<table style="margin-left:5px;" border="0" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/workflow_new.png" align="absbottom"><span class="big3" id="span1"> 
</span><br>
    </td>
  </tr>
</table>
<br>

<div id='sortList'></div>
<br>
</body>
</html>