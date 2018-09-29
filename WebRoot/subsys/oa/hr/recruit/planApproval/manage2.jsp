<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>已批准招聘计划</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/recruit/planApproval/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/planApproval/js/recruitPlanApprovalLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/plan/act/YHHrRecruitPlanAct/getRecruitPlanWaitListJson.act?planStatus=1";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"planNo",  width: '10%', text:"计划编号" ,align: 'center'},
       {type:"data", name:"planName",  width: '10%', text:"计划名称" ,align: 'center' },
       {type:"data", name:"planRecrNo",  width: '10%', text:"招聘人数" ,align: 'center' },
       {type:"data", name:"startDate",  width: '10%', text:"开始日期" ,align: 'center' ,render:splitDateFunc},
       {type:"data", name:"planStatus",  width: '10%', text:"计划状态" ,align: 'center' ,render:planStatus},
       {type:"selfdef", text:"操作", width: '15%',render:opts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无已批准招聘计划', 'msrg');
  }
}

function planStatus(cellData, recordIndex,columInde) {
	switch(cellData){
	  case 0 : return "待审批";
	  case 1 : return "<font color='green'>已批准</font>";
	  case 2 : return "<font color='red'>未批准</font>";
	}
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absMiddle"><span class="big3">&nbsp;已批准招聘计划 </span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
</table>
</div>

<div id="msrg">
</div>
</body>
</html>