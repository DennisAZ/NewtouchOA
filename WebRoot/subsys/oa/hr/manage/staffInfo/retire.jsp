<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String queryDate1 = request.getParameter("queryDate1");
String queryDate2 = request.getParameter("queryDate2");
if(YHUtility.isNullorEmpty(queryDate1)){
	queryDate1 = "";
}
if(YHUtility.isNullorEmpty(queryDate2)){
	queryDate2 = "";
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.core.util.YHUtility"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>退休人员</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/manage/staffInfo/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/manage/staffInfo/js/staffInfoListLogic.js"></script>
<script type="text/javascript">
function doInit(){
	var param = "";
	param = "queryDate1=" + encodeURIComponent("<%=queryDate1%>");
	param += "&queryDate2=" + encodeURIComponent("<%=queryDate2%>");
	var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/staffInfo/act/YHRetireQueryAct/queryRetirListJson.act?" + param;
	var cfgs = {
	    dataAction: url,
	    container: "listContainer",
	    sortIndex: 1,
	    sortDirect: "desc",
	    colums: [
	       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
	       {type:"data", name:"deptId",  width: '20%', text:"部门", render:deptIdFunc},
	       {type:"data", name:"staffName",  width: '20%', text:"姓名", render:infoCenterFunc},
	       {type:"data", name:"staffSex",  width: '20%', text:"性别", render:staffSexFunc},
	       {type:"data", name:"staffBirth",  width: '20%', text:"出生年月", render:staffBirthFunc},
	       {type:"selfdef", text:"操作", width: '20%',render:detailOpts}]
	  };
	  pageMgr = new YHJsPage(cfgs);
	  pageMgr.show();
	  var total = pageMgr.pageInfo.totalRecord;
	  if(total){
	    showCntrl('listContainer');
	    var mrs = " 共 " + total + " 条记录 ！";
	    showCntrl('delOpt');
	  }else{
	    WarningMsrg('无符合条件的记录！', 'msrg');
	  }
}
</script>
</head>
<body onload="doInit();">

<br>
<div id="listContainer" style="display:none;width:100;">
</div>

<div id="msrg">
</div>
<br>
<div align="center">
<input type="button" class="SmallButton" value="关闭" onClick="window.close();">
</div>  
</body>
</html>