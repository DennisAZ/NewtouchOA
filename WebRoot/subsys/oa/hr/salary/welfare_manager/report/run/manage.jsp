<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工资上报待办流程</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/salary/report/run/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/salary/report/run/js/reportLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/salary/report/act/YHHrReportAct/getReportListJson.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"salCreater",  width: '10%', text:"流程创建者" ,align: 'center' },
       {type:"data", name:"beginDate",  width: '10%', text:"起始日期" ,align: 'center' ,render:splitDateFunc},
       {type:"data", name:"endDate",  width: '10%', text:"截止日期" ,align: 'center' ,render:splitDateFuncNull},
       {type:"data", name:"salMonth",  width: '10%', text:"工资月份" ,align: 'center' },
       {type:"data", name:"content",  width: '10%', text:"备注" ,align: 'center' },
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
    WarningMsrg('尚未定义', 'msrg');
  }
}

function issendFunc(cellData, recordIndex, columIndex){
  switch(cellData){
    case '0' : return "<font color='green'>执行中</font>";
    case '1' : return "<font color='red'>已终止</font>";
  }
}

function splitDateTimeFunc(cellData, recordIndex,columInde) {
  var str = "";
  if(cellData){
    str = cellData.substr(0,19);
  }
  return "<center>" + str + "</center>";
}

function splitDateFuncNull(cellData, recordIndex,columInde) {
  var str = "";
  if(cellData){
    str = cellData.substr(0,10);
  }
  else
	  return "<center>需手动终止</center>";
  return "<center>" + str + "</center>";
}
</script>
</head>
<body topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absMiddle"><span class="big3">&nbsp;工资上报待办流程 </span>
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