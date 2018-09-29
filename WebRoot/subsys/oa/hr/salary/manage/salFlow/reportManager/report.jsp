<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String flowId = YHUtility.null2Empty(request.getParameter("flowId"));
	String userId = YHUtility.null2Empty(request.getParameter("userId"));
	String deptId = YHUtility.null2Empty(request.getParameter("deptId"));
	String fldStr = YHUtility.null2Empty(request.getParameter("fldStr"));
	String deptFlag = YHUtility.null2Empty(request.getParameter("deptFlag"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工资报表</title>
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script type="text/javascript">
function doInit(){
	getSalItemTable();
}

function getSalItemTable(){
	var pars = Form.serialize($('form1'));
	var urlStr = "<%=contextPath%>/yh/subsys/oa/hr/salary/salFlow/act/YHHrSalFlowAct";
	var url = urlStr + "/getSalItemTable.act";
	var rtJson = getJsonRs(url,pars);
	//alert(rsText);
	if (rtJson.rtState == "0") {
		var table = "<table class='TableBlock' border=1 cellspacing=0 cellpadding=2 align='center'>";
		var prcs = rtJson.rtData.tableList;
			table = table + prcs+ "</table>";
			$("tableList").update(table);
	} else {
		alert(rtJson.rtMsrg);
	}
}

</script>
</head>
<body onload="doInit();">
<div>
<form action="" id="form1" name="form1" method="post">
<input type="hidden" id="flowId" name="flowId" value="<%=flowId %>">
<input type="hidden" id="userId" name="userId" value="<%=userId %>">
<input type="hidden" id="deptId" name="deptId" value="<%=deptId %>">
<input type="hidden" id="fldStr" name="fldStr" value="<%=fldStr %>">
<input type="hidden" id="deptFlag" name="deptFlag" value="<%=deptFlag %>">
</form>
</div>

<div id="tableList"></div>
</body>
</html>