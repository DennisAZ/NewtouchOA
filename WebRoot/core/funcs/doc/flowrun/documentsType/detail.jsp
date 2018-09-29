<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
	String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件类型信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function doInit(){
	var url = "<%=contextPath%><%=moduleSrcPath %>/act/YHDocumentsTypeAct/getDocumentsTypeDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		getFlowTypeName(data.flowType);
		if(data.documentsFont){
		  var urls = contextPath + "<%=moduleSrcPath %>/act/YHDocumentsTypeAct/getDocWordById.act?seqId="+data.documentsFont;
		  var rtJsons = getJsonRs(urls);
		  var prc = rtJsons.rtData;
		  if(rtJsons.rtState == '0'){
		    if(prc){
		      $('documentsFont').innerHTML = prc;
		    }
		  }
		}
	}else{
		alert(rtJson.rtMsrg);
	}
}
function getFlowTypeName(id) {
  var urls = contextPath + "<%=moduleSrcPath %>/act/YHDocumentsTypeAct/getFlowTypeName.act?seqId="+id;
  var rtJsons = getJsonRs(urls);
  if(rtJsons.rtState == '0'){
    var  prc = rtJsons.rtData;
    $('flowType').update(prc);
  }
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 文件类型信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent">文件名称：</td>
    <td align="left" class="TableData" width="180"><div id="documentsName"></div> </td>
    <td align="left" width="120" class="TableContent">绑定流程：</td>
    <td align="left" class="TableData" width="180"><div id="flowType"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">文件字：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="documentsFont"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">套红模板：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="documentsWordModel"></div></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>
</body>
</html>