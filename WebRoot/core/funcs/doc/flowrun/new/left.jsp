<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
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
<link rel="stylesheet" href ="<%=cssPath %>/cmp/Accordion.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/skin.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
function doInit(){
  skinObjectToSpan(flowrun_new_left);
  var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowSortAct/getSortListR.act?noIcon=1&sortId=" + sortId;
  var json = getJsonRs(url);
  var data = {attachCtrl:'sortList'
        ,accordionId:'subfield'
          ,data:[]
  };
  if(json.rtState == '0'){
	data.data = json.rtData;
  }
  var dd = new Accordion(data);
}
function getList(tmp){
  
}
function actionFuntion(tmp){ 
  parent.mainFrame.location= "edit.jsp?flowId="+ tmp.extData + "&sortId=" + sortId + "&skin=" + skin;

}
function iconActionFuntion(tmp){ 
   
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