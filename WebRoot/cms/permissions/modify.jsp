<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑栏目</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/cms/column/js/columnLogic.js"></script>
<script type="text/javascript">

function doInit(){
  var url = "<%=contextPath%>/yh/cms/column/act/YHColumnAct/getColumnDetail.act?seqId=<%=seqId%>";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    getTemplate(data.stationId);
    bindJson2Cntrl(rtJson.rtData);
    if(data.paging == 1){
      $('pagingTr').style.display = "";
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getTemplate(stationId){
  var url = "<%=contextPath%>/yh/cms/station/act/YHStationAct/getTemplate.act?stationId="+stationId;
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var prcs = rtJson.rtData;
	  var selectObj = $("templateIndexId");
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
	
  var url = "<%=contextPath%>/yh/cms/column/act/YHColumnAct/getTemplate.act?stationId="+stationId;
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var prcs = rtJson.rtData;
	  var selectObj = $("templateArticleId");
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
}

function doSubmit(){
  if(checkForm()){
    var url = "<%=contextPath%>/yh/cms/column/act/YHColumnAct/updateColumn.act";
  	var rtJson = getJsonRs(url,mergeQueryString($("form1")));
  	if(rtJson.rtState == "0"){
  	  alert("栏目修改成功！");
			var curTree = parent.parent.frames["file_tree"].tree;
			var curNode = curTree.getNode('<%=seqId%>'+',column'); 
			if(curNode){
				curNode.name = $('columnName').value;
				curTree.updateNode(curNode.nodeId, curNode);
			}
  	}
  }
}

function checkForm(){
  if($("columnName").value == ""){
    alert("栏目名称不能为空！");
    $("columnName").focus();
    return (false);
  }
  
  if($("columnPath").value == ""){
    alert("存储路径名称不能为空！");
    $("columnPath").focus();
    return (false);
  }

  if(pathFlag){
    alert("存储路径名称已存在！");
    $("columnPath").focus();
    return (false);
  }
  return true;
}

function clickPag(flag){
  if(flag == 1){
    $('pagingTr').style.display = "";
  }
  else{
    $('pagingTr').style.display = "none";
  }
}

var pathFlag = false;
function checkPath(){
  if($('columnPath').value == ""){
    return;
  }
  var url = "<%=contextPath%>/yh/cms/column/act/YHColumnAct/checkPath.act?stationId="+$('stationId').value+"&parentId="+$('parentId').value+"&seqId="+$('seqId').value+"&columnPath="+$('columnPath').value;
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
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 编辑栏目</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form action="" method="post" name="form1" id="form1">
	<input type="hidden" id="seqId" name="seqId" value="<%=seqId%>">
  <input type="hidden" name="stationId" id="stationId" >
  <input type="hidden" name="parentId" id="parentId" >
  <input type="hidden" name="columnIndex" id="columnIndex" >
	<table class="TableBlock" width="80%" align="center">
	  <tr>
        <td nowrap class="TableData">栏目名称：<font color="red">*</font> </td>
        <td class="TableData">
          <input type="text" name="columnName" id="columnName" class="BigInput" size="15">
        </td>
        <td nowrap class="TableData">栏目标题：</td>
        <td class="TableData">
          <input type="text" name="columnTitle" id="columnTitle" class="BigInput" size="15">
        </td>
	  </tr>
    <tr>
      <td nowrap class="TableData">存储路径名称：<font color="red">*</font> </td>
      <td class="TableData">
        <input type="text" name="columnPath" id="columnPath" class="BigInput" size="15" onblur="checkPath()">&nbsp;
        <span id="userIdSpan"></span>
      </td>
      <td nowrap class="TableData">是否显示在首页导航： </td>
      <td class="TableData">
        &nbsp;&nbsp;
        <input type="radio" name="showMain" id="showMainYes" size="15" onclick="clickPag(1)" value="1">是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="radio" name="showMain" id="showMainNo" size="15" onclick="clickPag(0)" value="0" checked>否
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">归档天数： </td>
      <td class="TableData" colspan="3">
 				<select name="archive" id="archive" style="width:130px;">
					<option value="0">无</option>
					<option value="1">一个月</option>	
					<option value="2">三个月</option>	
					<option value="3">半年</option>
					<option value="4">一年</option>	
        </select>
      </td>
    </tr> 
    <tr>
    	<td nowrap class="TableData">选择索引模板： </td>
      <td class="TableData">
        <select name="templateIndexId" id="templateIndexId" style="width:130px;">
        	<option value=0>暂不选择</option>
        </select>
      </td>
 	    <td nowrap class="TableData">选择文章模板： </td>
      <td class="TableData">
        <select name="templateArticleId" id="templateArticleId" style="width:130px;">
        	<option value=0>暂不选择</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">是否分页显示： </td>
      <td class="TableData" colspan="3">
        &nbsp;&nbsp;
        <input type="radio" name="paging" id="pagingYes" size="15" onclick="clickPag(1)" value="1">是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="radio" name="paging" id="pagingNo" size="15" onclick="clickPag(0)" value="0" checked>否
      </td>
	  </tr>
  	<tr id="pagingTr" style="display:none;">
      <td nowrap class="TableData">最大索引页数： </td>
      <td class="TableData">
        <input type="text" name="maxIndexPage" id="maxIndexPage" class="BigInput" size="15">
      </td>
      <td nowrap class="TableData">分页条数：</td>
      <td class="TableData">
        <input type="text" name="pagingNumber" id="pagingNumber" class="BigInput" size="15">
      </td>
	  </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="hidden" name="dtoClass" id="dtoClass" value="yh.cms.column.data.YHCmsColumn">
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
      </td>
    </tr>
  </table>
</form>

</body>
</html>