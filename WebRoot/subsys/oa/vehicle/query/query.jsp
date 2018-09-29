<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%
String status = request.getParameter("status");
String name = "";
if (status.equals("0")) {
  name = "待批申请";
}
if (status.equals("1")) {
  name = "已准申请";
}
if (status.equals("2")) {
  name = "使用中车辆";
}
if (status.equals("3")) {
  name = "未准申请";
}
%>
<html>
<head>
<title>车辆查询</title>
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
<script> 
//加载数据
var status = "<%=status%>";
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/vehicleQuery.act?status=" + status;
   cfgs = {
    dataAction: url,
    container: "giftList",
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"hidden", name:"vId2", text:"vId", width: "1%",align:"center"},
       {type:"text", name:"vId", text:"车牌号", width: "7%",align:"center",render:toVID},
       {type:"text", name:"vuUser", text:"用车人", width: "7%",align:"center"},
       {type:"text", name:"vuReason", text:"事由", width: "7%",align:"center"},
       {type:"text", name:"vuDestination", text:"目的地", width: "7%",align:"center"},
       {type:"text", name:"vuStart", text:"开始时间", width: "10%",align:"center",render:toDate},
       {type:"text", name:"vuEnd", text:"结束时间",align:"center", width: "10%",render:toDate2},
       {type:"text", name:"vuRemark", text:"备注",align:"center", width: "7%"},
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
  //var dmerStatus =  this.getCellData(recordIndex,"dmerStatus");
  return "<a href=javascript:usageDetail(" + seqId + ")>详细信息</a>";
}
//查看详情
function usageDetail(vuId) {
  window.open('<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectDetail.act?seqId=' + vuId,'','height=500,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
//车辆情况
function vehicleDetail(vId) {
  window.open("<%=contextPath%>/subsys/oa/vehicle/manage/queryVehicle.jsp?seqId=" + vId,'','height=420,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
function query_Vehicle(seqId) { 
  var requestURL= "<%=contextPath%>/subsys/oa/vehicle/manage/queryVehicle.jsp?seqId=" + seqId;
  openDialogResize(requestURL , 600, 450); 
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
    <td class="Big"><img src="<%=imgPath%>/vehicle.gif"><span class="big3"><%=name%></span>
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
