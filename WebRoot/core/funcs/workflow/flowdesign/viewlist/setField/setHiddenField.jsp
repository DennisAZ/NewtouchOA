<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
String seqId = request.getParameter("seqId");
String flowId = request.getParameter("flowId");
String isList = request.getParameter("isList"); 
if(isList == null  || "".equals(isList)){
  isList = "";
}else {
  isList = "1";
}
String openflag = request.getParameter("openflag");
if(openflag == null ){
  openflag = "";
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
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
var procsId ;
var seqId = "<%=seqId%>";
var flowId = "<%=flowId%>";
var isList = "<%=isList%>";
var openflag = "<%=openflag %>";
var actionPath = contextPath + "/yh/core/funcs/workflow/act/YHFlowProcessAct";
function doInit(){
  var url = actionPath + "/getFieldMsg.act";
  var json = getJsonRs(url , "seqId=" + seqId + "&flowId=" + flowId);
  if(json.rtState != "0"){
    alert('请联系管理员');
    return ;
  }
  var selectedItem = json.rtData.hiddenItem;
  var items =  json.rtData.items;
  procId = json.rtData.procId;
  
  $('procsIdSpan').innerHTML = '步骤 '+ procId + '- 编辑保密字段(保密字段对于本步骤主办人、经办人均为不可见)';
  
  var selected = [];
  var disSelected = [];
  var aSelectedItem = selectedItem.split(',');
  var aItem = items.split(',');
  
  for(var i = 0 ;i < aItem.length ;i++){
    var itemTmp = aItem[i];
    if(itemTmp){
	  var isExist = false;
	  for(var j = 0 ; j < aSelectedItem.length;j++){
	    if(aSelectedItem[j] && aSelectedItem[j] == itemTmp){
	      isExist = true;
	      var tmp = {};
	      tmp.value = itemTmp;
	      
	      if(itemTmp == '[B@]'){
	        continue;
	      }else if(itemTmp == '[A@]'){
	        continue;
	      }else{
	        tmp.text =  itemTmp;
	      }
	      selected.push(tmp);
	      break;
	    }
	  }
	  if(!isExist){
	    var tmp = {};
	    tmp.value = itemTmp;
	    if(itemTmp == '[B@]'){
	      continue;
	    }else if(itemTmp == '[A@]'){
	      continue;
	    }else{
	      tmp.text =  itemTmp;
	    }
	    disSelected.push(tmp);
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
function commit(flag){
  var url = actionPath + "/setFormItem.act";
  var json = getJsonRs(url , $('fieldStr').serialize() +'&seqId=' + seqId +'&type=hidden' );
  if(json.rtState == '0'){
    if (!openflag) {
      if (!flag) {
        if(!isList){
          try {
            opener.location.reload();
          } catch (e) {

          }
          window.close();
        }else{
          history.back();
        }
      }
    }else {
      alert("保存成功！");
    }
  }else{
    alert(json.rtMsrg);
  }
}
function turn(type) {
  commit(1);
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowProcessAct/turn.act";
  var json = getJsonRs(url , "flowId=" + flowId + "&seqId=" + seqId + "&type=" + type);
  if (json.rtState == '0') {
    if (!json.rtData) {
      if (type) {
        alert("无上一步骤！");
      } else {
        alert("无下一步骤！");
      }
      return;
    }
    var url = contextPath + "/core/funcs/workflow/flowdesign/viewlist/setField/setHiddenField.jsp?flowId="+flowId + "&isList=" + isList  + "&openflag=" + openflag;
    if (json.rtData) {
      url += "&seqId=" + json.rtData;
    }
    this.location.href = url;
  }
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span id="procsIdSpan" class="big3"> </span>
     <br>
     保存并转到：<input onclick='turn(1)'  class='SmallButtonW' value="上一步" type="button">&nbsp;<input onclick='turn()'  class='SmallButtonW' value="下一步" type="button">
    <br>
    </td>
  </tr>
</table>
<div id="selectItemDiv" align=center></div>
<div align=center>点击条目时，可以组合CTRL或SHIFT键进行多选</div>
<div align=center><input type="hidden" id="fieldStr" name="fieldStr"><input type="button" class="BigButton" value="保 存" onclick="commit()">&nbsp;&nbsp;&nbsp;&nbsp;
<% if(isList == null || "".equals(isList)){ %>
  <input type="button"  value="关闭" class="BigButton" onclick="closeWindow();">
  <% }else{ %>
  <input type="button"  value="返回" class="BigButton" onclick="history.back();">
  <% } %>
      </div>
</body>
</html>