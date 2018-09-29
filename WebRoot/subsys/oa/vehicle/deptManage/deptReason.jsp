<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
%>
<html>
<head>
<title>不准原因</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>"
function doInit() {
  var requestURL="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectDeptReason.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURL); 
  var prcsJsons = rtJson.rtData; 
  if(rtJson.rtState == '1') {
    alert(rtJson.rtMsrg); 
    return ;
  }
  if(prcsJsons.deptReason){
    var textarea = new Element('textarea',{"class":"BigInput","cols":"50","rows":"16","name":"deptReason","id":"deptReason"}).update(prcsJsons.deptReason);
    $('listName').appendChild(textarea); 
  }else{
    var textarea = new Element('textarea',{"class":"BigInput","cols":"50","rows":"16","name":"deptReason","id":"deptReason"}).update("");
    $('listName').appendChild(textarea);
  }

}
//不批准原因
function deptReasonFrom() {
  var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/updateDmerStatus.act?seqId=" + seqId + "&dmerStatus=3";
  var json = getJsonRs(requestURL);
  if (json.rtState == '1') {
    alert(json.rtMsrg); 
    return ; 
  } else {
    alert("不批准成功!");
    updateDeptReason();
  }
}
//不批准原因
function updateDeptReason() {
  var deptReason2 = document.getElementById("deptReason").value;
  var deptReason = encodeURI(deptReason2.replace("&",""));
  var url = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/updateDeptReason.act?seqId=" + seqId + "&deptReason=" + deptReason;
  var json = getJsonRs(url);
  window.location.reload();
}
</script>
</head> 
<body class="bodycolor" topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/vehicle.gif" align="absmiddle"><span class="big3"> 不准原因</span>
    </td>
  </tr>
</table>
<br>
<form id="form1" name="form1">
<input type="hidden" name="seqId" id="seqId" value="<%=seqId %>">
<table class="TableBlock" width="450" align="center" >  
   <tr>
     <td nowrap class="TableContent"> 不准原因：</td>
     <td class="TableData" colspan="1">
     <div id="listName">
     </div>
     </td>
   </tr> 
   <tr>
    <td nowrap class="TableControl" colspan="2" align="center">
      <input type="button" value="确定" class="BigButton" onClick="javascript:deptReasonFrom();">&nbsp;&nbsp;
      <input type="button" class="BigButton" value="关闭" onClick="javascript:parent.opener.location.reload();window.close();" title="关闭窗口">
    </td>
</table>
</form>
</body>
</html>
