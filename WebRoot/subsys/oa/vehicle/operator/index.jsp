<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%String flowId = request.getParameter("flowId"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>指定调度人员</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function Init(){
  var url="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleOperatorAct/addUpdateOperator.act";
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if(rtJson.rtState == '1'){ 
    alert(rtJson.rtMsrg); 
    return ; 
  } 
  alert("保存成功！");
}
function doInit(){
  var requestURL ="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleOperatorAct/selectOperator.act"; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData;
  if(prc.operatorId){
    $("operatorId").value = prc.operatorId;
    $("operatorName").value = prc.operatorName;
  } 
}
</script>
</head>
<body topmargin="5" onload = "doInit()">
<form action="" method="post" name="form1" id = "form1">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
	<tr>
		<td class="Big"><img src="<%=imgPath%>/edit[1].gif" WIDTH="22"
			HEIGHT="20" align="absmiddle"><span class="big3">指定车辆调度人员</span>
		</td>
	</tr>
</table>
<br>
<table class="TableList" width="70%" align="center">
	<tr>
		<td class="TableContent">车辆调度人员：</td>
		<td class="TableData">
		  <input type="hidden" id = "operatorId" name="operatorId"value=""> 
		  <textarea cols=40 id = "operatorName" name="operatorName" rows=8 class="BigStatic" wrap="yes" readonly></textarea> 
		   <a href="javascript:;" class="orgAdd" onClick="selectUser(['operatorId','operatorName']);">添加</a>
           <a href="javascript:;" class="orgClear" onClick="$('operatorId').value='';$('operatorName').value='';">清空</a>
		</td>
	</tr>
	<tr class="TableData">
		<td align="center" valign="top" colspan="2"><input type="submit"
			class="BigButton" value="保 存" onclick = "Init()"></input>&nbsp;&nbsp;
		</td>
	</tr>
</table>
</form>
</body>
</html>
