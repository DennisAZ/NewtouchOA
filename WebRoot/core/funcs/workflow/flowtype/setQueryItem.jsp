<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String sFlowId = request.getParameter("flowId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置查询字段</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript">
var flowId = "<%=sFlowId%>";
var sortId = "";
function doInit(){
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowManageAct/getQueryItem.act";
  var json = getJsonRs(url , "flowId=" + flowId);
  if (json.rtState == "0") {
    sortId = json.rtData.sortId;
    queryItem = json.rtData.queryItem;
    items = json.rtData.formItem;
    createExchangeSelect(items.split(",") , queryItem.split(","));
  }
}
function createExchangeSelect(allFormItems , listFilds){
  var selected = new Array();
  var disSelected = new Array();
  for(var i = 0 ;i < allFormItems.length ;i++){
    var itemTmp = allFormItems[i];
    if (!itemTmp) {
      continue;
    }
	var isExist = false;
  	for(var j = 0 ; j < listFilds.length;j++){
      if(listFilds[j] == itemTmp){
      	isExist = true;
      	var tmp = {};
      	tmp.value = itemTmp;
      	tmp.text = itemTmp;
      	selected.push(tmp);
     	 break;
    	}
  	}
  	if(!isExist){
      var tmp = {};
      tmp.value = itemTmp;
      tmp.text = itemTmp;
      disSelected.push(tmp);
  	}
  }
  new ExchangeSelectbox({containerId:'container'
    , selectedArray:selected
    , disSelectedArray:disSelected 
    , isSort:false
    , selectName:'selectedName'
    , selectedChange:exchangeHandler
    , titleText:{selectedTitle:'工作流查询字段',disSelectedTitle:'备选字段'}}); 
} 
function commit() {
  var fldStr = $F("fldStr");
  fldStr = encodeURIComponent(fldStr);
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowManageAct/setQueryItem.act";
  var json = getJsonRs(url , "fldStr=" + fldStr + "&flowId=" + flowId);
  if (json.rtState == '0') {
    alert('操作成功!');
  }
}
function exchangeHandler(ids) {
  $('fldStr').value = ids;
}
</script>
</head>
<body onload="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 设置查询字段</span>
      <span class="small1"> 用于设置工作流高级查询界面，显示的表单查询字段</span>
      <br>
    </td>
  </tr>
</table>
 
<br>
  <div id="container" align=center></div>
<div align=center>点击条目时，可以组合CTRL或SHIFT键进行多选</div>
<div align=center>
<input type="hidden" id="fldStr" name="fldStr">
      <input type="button" class="BigButton" value="保 存" onclick="commit()">&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" class="BigButton" value="返 回" onclick="history.back()">
      </div>
</body>
</html>