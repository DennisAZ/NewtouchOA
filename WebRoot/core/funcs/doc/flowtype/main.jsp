<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%String flowId = request.getParameter("flowId"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/root.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript">
function doInit() {
  var jso = [
             {title:"流程基本属性"
               , useTextContent:false 
               , imgUrl:imgPath + "/edit.gif"
               , contentUrl:"<%=contextPath %><%=moduleContextPath %>/flowtype/edit.jsp?flowId=<%=flowId%>" 
               , useIframe:true}
             ,{title:"管理权限", useTextContent:false ,imgUrl:imgPath + "/node_user.gif" , contentUrl:"<%=contextPath %><%=moduleContextPath %>/flowtype/setPriv.jsp?flowId=<%=flowId%>" , useIframe:true}
             ,{title:"查询字段" , useTextContent:false ,imgUrl:imgPath + "/node_user.gif" , contentUrl:"<%=contextPath %><%=moduleContextPath %>/flowtype/setQueryItem.jsp?flowId=<%=flowId%>" , useIframe:true}
             ,{title:"打印模板" , useTextContent:false ,imgUrl:imgPath + "/node_user.gif" , contentUrl:"<%=contextPath %><%=moduleContextPath %>/flowtype/setprint/index.jsp?flowId=<%=flowId%>" , useIframe:true}
             ];
  
  buildTab(jso, 'contentDiv', 710);
}

function delFlowType(){
  if(confirm("确定删除吗？\n这将删除以下数据：\n1,流程描述与步骤设置。\n2,依托于该流程的所有工作。"))
  {
    var url =  contextPath + "<%=moduleSrcPath %>/act/YHFlowTypeAct/delFlowType.act?flowId=<%=flowId%>";
  	var json = getJsonRs(url, "flowId=<%=flowId%>");
  	if(json.rtState == '0'){
      alert(json.rtMsrg);
      parent.leftFrame.location.reload();
    //histroy.back();
    //转到列表页 	}else{
      alert(json.rtMsrg);
  	}
  }
}

</script>
</head>
<body onload="doInit()">
<div id="contentDiv"></div>


</body>
</html>