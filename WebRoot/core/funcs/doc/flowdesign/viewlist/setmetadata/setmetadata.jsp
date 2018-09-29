<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
    <%
String seqId = request.getParameter("seqId");
String flowId = request.getParameter("flowId");
String isList = request.getParameter("isList"); 
if(isList == null ){
  isList = "";
}
%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置隐藏字段</title>
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/style.css">

<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/ExchangeSelect1.0.js" ></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
<script type="text/javascript">
var procsId ;
var seqId = "<%=seqId%>";
var flowId = "<%=flowId%>";
var isList = "<%=isList%>";
var actionPath = contextPath + "<%=moduleSrcPath %>/act/YHFlowProcessAct";
function doInit(){
  var url = actionPath + "/getMetadataMsg.act";
  var json = getJsonRs(url , "seqId=" + seqId + "&flowId=" + flowId);
  if(json.rtState != "0"){
    alert('请联系管理员');
    return ;
  }
  var selectedItem = json.rtData.selectedItem;
  var items =  json.rtData.items;
  procId = json.rtData.procId;
  
  $('procsIdSpan').innerHTML = '步骤 '+ procId + '- 设置元数据提取项';
  
  var selected = [];
  var disSelected = [];
  var aSelectedItem = selectedItem.split(',');
  
  for(var i = 0 ;i < items.length ;i++){
    var itemTmp = items[i];
    if(itemTmp){
	  var isExist = false;
	  for(var j = 0 ; j < aSelectedItem.length;j++){
	    if(aSelectedItem[j] && aSelectedItem[j] == itemTmp.value){
	      isExist = true;
	      selected.push(itemTmp);
	      break;
	    }
	  }
	  if(!isExist){
	    disSelected.push(itemTmp);
	  }
    }
  }
  new ExchangeSelectbox({containerId:'selectItemDiv'
	    ,selectedArray:selected
	    ,disSelectedArray:disSelected 
		,isOneLevel:false
	    ,selectedChange:exchangeHandler
    }); 
  
  $('fieldStr').value = selectedItem; 
}
function exchangeHandler(ids){
  $('fieldStr').value = ids;
}
function commit(){
  var url = actionPath + "/setMatadataItem.act";
  var json = getJsonRs(url , $('fieldStr').serialize() +'&seqId=' + seqId  );
  if(json.rtState == '0'){
    if(!isList){
      try {
        opener.location.reload();
      } catch (e) {

      }
      window.close();
    }else{
      history.back();
    }
  }else{
    alert(json.rtMsrg);
  }
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span id="procsIdSpan" class="big3"> </span><br>
    </td>
  </tr>
</table>
<div id="selectItemDiv" align=center></div>
<div align=center>点击条目时，可以组合CTRL或SHIFT键进行多选</div>
<div align=center><input type="hidden" id="fieldStr" name="fieldStr"><input type="button" class="BigButton" value="保 存" onclick="commit()">&nbsp;&nbsp;&nbsp;&nbsp;
<% if("".equals(isList)){ %>
  <input type="button"  value="关闭" class="BigButton" onclick="closeWindow();">
  <% }else{ %>
  <input type="button"  value="返回" class="BigButton" onclick="history.back();">
  <% } %>
      </div>
</body>
</html>