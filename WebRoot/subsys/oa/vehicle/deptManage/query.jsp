<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%
String dmerStatus = request.getParameter("dmerStatus") == null ? "" :  request.getParameter("dmerStatus");
String name = "";
if (dmerStatus.equals("0")) {
  name = "待批申请";
}
if (dmerStatus.equals("1")) {
  name = "已准申请";
}
if (dmerStatus.equals("3")) {
  name = "未准申请";
}
%>
<html>
<head>
<title>车辆使用管理</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script Language=JavaScript> 
//加载数据
var dmerStatus = "<%=dmerStatus%>";
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/vehicleDept.act?dmerStatus=" + dmerStatus;
   cfgs = {
    dataAction: url,
    container: "giftList",
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"hidden", name:"vId2", text:"vId", width: "1%",align:"center"},
       {type:"text", name:"vId", text:"车牌号", width: "7%",align:"center",render:toVID},
       {type:"text", name:"vuUser", text:"用车人", width: "7%",align:"center"},
       {type:"text", name:"vuStart", text:"开始时间", width: "10%",align:"center",render:toDate},
       {type:"text", name:"vuEnd", text:"结束时间",align:"center", width: "10%",render:toDate2},
       {type:"hidden", name:"vuStatus", text:"状态",align:"center", width: "1%"},
       {type:"hidden", name:"dmerStatus", text:"状态2",align:"center", width: "1%"},
       {type:"selfdef", name:"caozuo", text:"操作",align:"center",width:"10%",render:toCaozuo}]//,render:toMing
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  $('numSpan').update(total);
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无<%=name%>!</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table);
  }
  autoUsaBack();
}

//车牌号
function toVID(cellData,recordIndex, columIndex){
  var vId = this.getCellData(recordIndex,"vId");
  var vId2 = this.getCellData(recordIndex,"vId2");
  var seqId =  this.getCellData(recordIndex,"seqId");
  return "<a href=javascript:query_Vehicle(" + vId2 + ")>" + vId + "</a>";
}
//时间前19
function toDate(cellData, recordIndex, columIndex){
  var vuStart = this.getCellData(recordIndex,"vuStart");
  return vuStart.substr(0,19);
}
//时间前19
function toDate2(cellData, recordIndex, columIndex){
  var vuEnd = this.getCellData(recordIndex,"vuEnd");
  return vuEnd.substr(0,19);
}
//操作
function toCaozuo(cellData,recordIndex, columIndex){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var vuStatus =  this.getCellData(recordIndex,"vuStatus");
  var dmerStatus =  this.getCellData(recordIndex,"dmerStatus");
  var isHookRun = this.getCellData(recordIndex,"isHookRun");
  var flowId = this.getCellData(recordIndex,"flowId");

  var detail="<a href=javascript:usageDetail(" + seqId + ")>详细信息</a>&nbsp;";
  var yes= "&nbsp;<a href=javascript:deptReason(" + seqId + ")>不批准</a>&nbsp;";
  var no=  "<a href=javascript:checkup(" + seqId + ",1)>批准</a>";
  if(isHookRun!="0"){
       yes="";
       no="";
      detail="<a href='javascript:void(0)' onclick='formView("+isHookRun+" , "+ flowId +")'>查看流程</a>";
    }
  
  if(dmerStatus == "0") {
    return detail+yes+no;
   
  }
  if(dmerStatus == "1") {
    if (vuStatus == "0") {
      return detail;
    } else {
      return detail;
    }
  }
  if(dmerStatus == "3") {
    return detail+yes;
  }
}

//查看详情
function usageDetail(seqId) {
  window.open('<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectDetail.act?seqId=' + seqId,'','height=500,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
//车辆情况
function vehicleDetail(vId) {
  window.open("<%=contextPath%>/subsys/oa/vehicle/manage/queryVehicle.jsp?seqId=" + vId,'','height=420,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}

function query_Vehicle(seqId) { 
  var requestURL= "<%=contextPath%>/subsys/oa/vehicle/manage/queryVehicle.jsp?seqId=" + seqId;
  openDialogResize(requestURL , 600, 450); 
}
//不批准原因
function deptReason(seqId) {
  window.open('<%=contextPath%>/subsys/oa/vehicle/deptManage/deptReason.jsp?seqId=' + seqId,'','height=420,width=520,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
//批准
function checkup(seqId,dmerStatus) {
  var msg = '确认要批准该记录吗？';
  if (window.confirm(msg)) {
    var url = '<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/updateDmerStatus.act?seqId=' + seqId + '&dmerStatus=' + dmerStatus;
    var json=getJsonRs(url);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      alert("批准成功!");
      window.location.reload();
    }
  }
}
//撤销
function checkXiao(seqId,dmerStatus) {
  var msg = '确认要撤销该记录吗？';
  if (window.confirm(msg)) {
    var url = '<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/updateDmerStatus.act?seqId=' + seqId + '&dmerStatus=' + dmerStatus;
    var json=getJsonRs(url);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      alert("撤销成功!");
      window.location.reload();
    }
  }
}
//自动使用和回收
function autoUsaBack(){
  var url = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/getAutoUsageBack.act";
  var json=getJsonRs(url);
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()">
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/vehicle.gif"><span class="big3"> <%=name %></span>
    </td>
 
    <td valign="bottom" class="small1">共&nbsp;<span class="big4" id="numSpan"></span>&nbsp;条车辆记录
    </td>
    </tr>
</table>
<br>
<div id="giftList" style="padding-left: 10px; padding-right: 10px;"></div>
<div id="returnNull"></div>
</body>
</html>

