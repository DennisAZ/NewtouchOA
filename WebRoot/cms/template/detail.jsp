<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>模板信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function doInit(){
  var url = "<%=contextPath%>/yh/cms/template/act/YHTemplateAct/getTemplateDetail.act?seqId=<%=seqId%>&flag=1";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(data);
		var templateTypeName = "";
    switch(data.templateType){
  		case 1 : templateTypeName = "索引";break;
  		case 2 : templateTypeName = "文章";break;
  		case 3 : templateTypeName = "包含";break;
  	}
    $('templateTypeName').innerHTML = templateTypeName;
		if(data.attachmentId){
		  $("returnAttId").value = data.attachmentId;
		  $("returnAttName").value = data.attachmentName;
		  var selfdefMenu = {
      	office:["downFile"], 
      	img:["downFile"],  
      	music:["downFile"],  
      	video:["downFile"], 
      	others:["downFile"]
      }
      attachMenuSelfUtil("attr","cms",$('returnAttName').value ,$('returnAttId').value, '','','<%=seqId%>',selfdefMenu);
		}
	}else{
		alert(rtJson.rtMsrg);
	}
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 模板信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent" nowrap>名称：</td>
    <td align="left" class="TableData" width="180"><div id="templateName"></div> </td>
    <td align="left" width="120" class="TableContent" nowrap>文件名：</td>
    <td align="left" class="TableData" width="180"><div id="templateFileName"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent" nowrap>模板类型：</td>
    <td align="left" class="TableData" width="180" colspan="3">
    	<div id="templateTypeName"></div> 
    </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent" nowrap>模板文件：</td>
    <td align="left" class="TableData Content" colspan="3">
    	<input type = "hidden" id="returnAttId" name="returnAttId"></input>
			<input type = "hidden" id="returnAttName" name="returnAttName"></input>
			<span id="attr"></span>
		</td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent" nowrap>模板内容：</td>
		<td align="left" class="TableData" width="180" colspan="3"><div id="templateContent"></div> </td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>
</body>
</html>