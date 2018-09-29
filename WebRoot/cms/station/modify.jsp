<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");
String flag = request.getParameter("flag");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑站点</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/cms/template/js/stationLogic.js"></script>
<script type="text/javascript">
var flag = "<%=flag %>";
function doInit(){
  var url = "<%=contextPath%>/yh/cms/station/act/YHStationAct/getTemplate.act?stationId=<%=seqId %>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var prcs = rtJson.rtData;
	  var selectObj = $("templateId");
	  for(var i = 0 ; i < prcs.length ; i++){
	    var prc = prcs[i];
	    var seqId = prc.seqId;
	    var templateName = prc.templateName;
	    var myOption = document.createElement("option");
	    myOption.value = seqId;
	    myOption.text = templateName;
	    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
	  }
	}
	
  var url = "<%=contextPath%>/yh/cms/station/act/YHStationAct/getStationDetail.act?seqId=<%=seqId%>";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function doSubmit(){
  if(checkForm()){
    var url = "<%=contextPath%>/yh/cms/station/act/YHStationAct/updateStation.act";
  	var rtJson = getJsonRs(url,mergeQueryString($("form1")));
  	if(rtJson.rtState == "0"){
  	  alert("站点修改成功！");
  	  if(flag==1){
  			var curTree = parent.frames["file_tree"].tree;
  			var curNode = curTree.getNode('<%=seqId%>'+',station'); 
  			if(curNode){
  				curNode.name = $('stationName').value;
  				curTree.updateNode(curNode.nodeId, curNode);
  			}
  	  }
  	  else{
  	  	location.href = "/yh/cms/station/manage.jsp";
  	  }
  	}
  }
}

function checkForm(){
  if($("stationName").value == ""){
    alert("站点名称不能为空！");
    $("stationName").focus();
    return (false);
  }
  
  if($("stationPath").value == ""){
    alert("存储路径名称不能为空！");
    $("stationPath").focus();
    return (false);
  }
  
  if(pathFlag){
    alert("存储路径名称已存在！");
    $("stationPath").focus();
    return (false);
  }
  return true;
}

var pathFlag = false;
function checkPath(){
  if($('stationPath').value == ""){
    return;
  }
  var url = "<%=contextPath%>/yh/cms/station/act/YHStationAct/checkPath.act?seqId="+$('seqId').value+"&stationPath="+$('stationPath').value;
  var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
	  var prcs = rtJson.rtData;
	  var divNode = document.getElementById("userIdSpan");
	  if(prcs == 1){
	    divNode.innerHTML = "<img src='<%=imgPath%>/dtree/error.gif' />该路径名称已存在";
	    pathFlag = true;
	  }
	  else{
	    divNode.innerHTML = "<img src='<%=imgPath%>/dtree/correct.gif' />";
	    pathFlag = false;
	  }
	}
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 编辑站点</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form action="" method="post" name="form1" id="form1">
	<input type="hidden" id="seqId" name="seqId" value="<%=seqId%>">
	<table class="TableBlock" width="80%" align="center">
	  <tr>
        <td nowrap class="TableData">站点名称：<font color="red">*</font> </td>
        <td class="TableData">
          <input type="text" name="stationName" id="stationName" class="BigInput" size="15">
        </td>
        <td nowrap class="TableData">站点域名：</td>
        <td class="TableData">
          <input type="text" name="stationDomainName" id="stationDomainName" class="BigInput" size="15">
        </td>
	  </tr>
	  <tr>
	    <td nowrap class="TableData">选择索引模板： </td>
      <td class="TableData" colspan="3">
        <select name="templateId" id="templateId" style="width:130px;">
        	<option value=0>暂不选择</option>
        </select>
      </td>
    </tr>
    <tr>
			<td nowrap class="TableData">存储路径名称：<font color="red">*</font></td>
      <td class="TableData" colspan="3">
        <input type="text" name="stationPath" id="stationPath" class="BigInput" size="15" onblur="checkPath()">&nbsp;
        <span id="userIdSpan"></span>
      </td>
	  </tr>
    <tr>
      <td nowrap class="TableData">站点索引扩展名： </td>
      <td class="TableData">
				<select name="extendName" id="extendName" style="width:130px;">
					<option value="html">html</option>
					<option value="htm">htm</option>	
					<option value="jsp">jsp</option>	
					<option value="php">php</option>	
        </select>
      </td>
      <td nowrap class="TableData">站点文章扩展名： </td>
      <td class="TableData">
 				<select name="articleExtendName" id="articleExtendName" style="width:130px;">
					<option value="html">html</option>
					<option value="htm">htm</option>	
					<option value="jsp">jsp</option>	
					<option value="php">php</option>	
        </select>
      </td>
    </tr> 
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="hidden" name="dtoClass" id="dtoClass" value="yh.cms.station.data.YHCmsStation">
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
      </td>
    </tr>
  </table>
</form>

</body>
</html>