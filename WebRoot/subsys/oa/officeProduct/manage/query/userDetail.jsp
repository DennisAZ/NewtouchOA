<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String personId = request.getParameter("personId");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>明细结果查看</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/officeProduct/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/officeProduct/js/manageQueryLogic.js"></script>
<script type="text/javascript">

var deptId = "<%=personId%>"
	function doInit(){
		var url = "<%=contextPath%>/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct/getTransInfoListJson.act?personId=<%=personId%>";
		var cfgs = {
		    dataAction: url,
		    container: "listContainer",
		    sortIndex: 1,
		    sortDirect: "desc",
		    colums: [
		       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
		       {type:"data", name:"proName",  width: '10%', text:"办公用品名称", render:infoCenterFunc},
		       {type:"data", name:"transFlag",  width: '10%', text:"登记类型", render:transFlagFunc},
		       {type:"data", name:"borrower",  width: '15%', text:"领用/借用/归还人", render:borrowerFunc},
		       {type:"data", name:"transQty",  width: '10%', text:"数量", render:transQtyFunc},
		       {type:"data", name:"price",  width: '10%', text:"单价", render:infoCenterFunc},
		       {type:"data", name:"transDate",  width: '10%', text:"操作日期", render:transDateFunc},
		       {type:"data", name:"remark",  width: '15%', text:"备注", render:infoCenterFunc},
		       {type:"hidden", name:"proUnit",  width: '', text:"计量单位", render:infoCenterFunc},
		       {type:"hidden", name:"proPrice",  width: '', text:"存储", render:infoCenterFunc},
		       {type:"selfdef", text:"操作", width: '5',render:optsList}]
		  };
		  pageMgr = new YHJsPage(cfgs);
		  pageMgr.show();
		  var total = pageMgr.pageInfo.totalRecord;
		  if(total){
		    showCntrl('listContainer');
		    var mrs = " 共 " + total + " 条记录 ！";
		    //showCntrl('delOpt');
		  }else{
		    WarningMsrg('无操作记录！', 'msrg');
		  }
	}


</script>



</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" WIDTH="22" HEIGHT="22" align="absmiddle"><span class="big3">&nbsp;办公用品管理明细</span>
    </td>
  </tr>
</table>

<div id="listContainer" style="display:none;width:100;"></div>
<div id="msrg"></div>

</body>
</html>