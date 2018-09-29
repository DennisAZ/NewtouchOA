<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String SeqId = request.getParameter("SeqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物品领用清单</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffLeave/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffLeave/js/staffLeaveLogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/leave/act/YHHrStaffLeaveAct/getSelectGoods.act?SeqId=<%=SeqId%>";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"data", name:"leavePerson",  width: '20%', text:"物品名称" ,align: 'center'},
       {type:"data", name:"position",  width: '10%', text:"数量" ,align: 'center'},
       {type:"data", name:"quitType",  width: '10%', text:"单位" ,align: 'center' },
       {type:"data", name:"quitTimePlan",  width: '10%', text:"状态" ,align: 'center' ,render:transFlag},
       {type:"data", name:"lastSalaryTime",  width: '10%', text:"日期" ,align: 'center' ,render:splitDateFunc}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无领用物品记录', 'msrg');
  }
}

function transFlag(cellData, recordIndex,columInde) {
  var str = "";
  switch(cellData){
    case '0':str = "采购";break;
    case '1':str = "领用";break;
    case '2':str = "借用";break;
    case '3':str = "归还";break;
    case '4':str = "报废";break;
    case '5':str = "维护";break;
  }
  return "<center>" + str + "</center>";
}


//截取时间
function splitDateFunc(cellData, recordIndex,columInde) {
  var str = "";
  if(cellData){
    str = cellData.substr(0,10);
  }
  return "<center>" + str + "</center>";
}
</script>
</head>
<body onload="doInit();">
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none"></div>
<div id="msrg">
</div>
</body>
</html>