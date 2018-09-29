<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
String sFlowId = request.getParameter("flowId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>流程校验</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript"><!--
var flowId = "<%=sFlowId%>";
var data = [{isError:true , id:'prcsUserCheck',desc:['ddddd','ddddd','ddddd']}
	,{isError:true , id:'prcsToCheck',desc:['ddddd','ddddd','ddddd']}
	,{isError:false , id:'writableFieldCheck'}
	,{isError:true , id:'condFormulaCheck',desc:['ddddd','ddddd','ddddd']}]
function doInit() {
  //var url = "/checkFlowType.act";
  //var json = getJsonRs(url  ,"flowId=" + flowId);
  //if (json.rtData == '0') {
   // var data = json.rtData;
  
 	  data.each(function(obj){displayCheck(obj);});
     
  //}
}
function displayCheck (obj) {
  var id = obj.id + "Td";
  if (obj.isError) {
    obj.desc.each(function(tmp){
    	$(id).innerHTML += "<font color=red><b>" + tmp + "</b></font><br>";
      })
    
  } else {
     $(id).update("<font color=limegreen><b>正常！</b></font>");
  }
}
</script>
</head>
<body onload="doInit()">

<table style='border-collapse:collapse;padding-top:10px' border=1 cellspacing=0 cellpadding=0    width="100%"  class="TableList">
  <tr class="TableLine2">
  	<td nowrap align="center" colspan="2" height=30 class="TableHeader">
  		<b>校验结果</b><br>
    </td>
  </tr>

  <tr class="TableLine1">
    <td nowrap align="center">
    	<b>经办人检查</b>
    </td>
    <td id="prcsUserCheckTd">
    </td>
  </tr>

  <tr class="TableLine2">
    <td nowrap align="center">
    	<b>转交步骤检查</b>
    </td>
    <td  id="prcsToCheckTd">
    </td>
  </tr>

  <tr class="TableLine1">
    <td nowrap align="center">
    	<b>可写字段检查</b>
    </td>
    <td  id="writableFieldCheckTd">
    </td>
  </tr>

  <tr class="TableLine2">
    <td nowrap align="center">
    	<b>条件公式检查</b>
    </td>
    <td  id="condFormulaCheckTd">
    </td>
  </tr>
  <tr class="TableControl">
    <td align="center" colspan="2">
      <input type="button" class="BigButton" value="返 回" onclick="history.back();">
    </td>
  </tr>
</table>

</body>
</html>