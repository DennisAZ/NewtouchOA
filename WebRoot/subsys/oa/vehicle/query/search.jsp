<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String vuStatus = request.getParameter("vuStatus") == null ? "" :  request.getParameter("vuStatus");
String vId = request.getParameter("vId") == null ? "" :  request.getParameter("vId");
String vuDriver = request.getParameter("vuDriver") == null ? "" :  request.getParameter("vuDriver");
String vuRequestDateMin = request.getParameter("vuRequestDateMin") == null ? "" :  request.getParameter("vuRequestDateMin");
String vuRequestDateMax = request.getParameter("vuRequestDateMax") == null ? "" :  request.getParameter("vuRequestDateMax");
String vuUser = request.getParameter("vuUser") == null ? "" :  request.getParameter("vuUser");
String vuDept = request.getParameter("vuDept") == null ? "" :  request.getParameter("vuDept");
String vuStartMin = request.getParameter("vuStartMin") == null ? "" :  request.getParameter("vuStartMin");
String vuStartMax = request.getParameter("vuStartMax") == null ? "" :  request.getParameter("vuStartMax");
String vuEndMin = request.getParameter("vuEndMin") == null ? "" :  request.getParameter("vuEndMin");
String vuEndMax = request.getParameter("vuEndMax") == null ? "" :  request.getParameter("vuEndMax");
String vuProposer = request.getParameter("vuProposer") == null ? "" :  request.getParameter("vuProposer");
String vuReason = request.getParameter("vuReason") == null ? "" :  request.getParameter("vuReason");
String vuRemark = request.getParameter("vuRemark") == null ? "" :  request.getParameter("vuRemark");
String vuOperator = request.getParameter("vuOperator") == null ? "" :  request.getParameter("vuOperator");
YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<html>
<head>
<title>车辆使用记录查询</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
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
var param = "vuStatus=<%=vuStatus%>&vId=<%=vId%>&vuDriver=<%=vuDriver%>&vuRequestDateMin=<%=vuRequestDateMin%>&vuRequestDateMax=<%=vuRequestDateMax%>"
  + "&vuUser=<%=vuUser%>&vuDept=<%=vuDept%>&vuStartMin=<%=vuStartMin%>&vuStartMax=<%=vuStartMax%>&vuEndMin=<%=vuEndMin%>"
  + "&vuEndMax=<%=vuEndMax%>&vuProposer=<%=vuProposer%>&vuReason=<%=vuReason%>&vuRemark=<%=vuRemark%>&vuOperator=<%=vuOperator%>";
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/vehicleAll.act?" + param;
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
       {type:"text", name:"vuStart", text:"开始时间", width: "10%",align:"center",render:toDate,sortDef:{type:0, direct:"desc"}},
       {type:"text", name:"vuEnd", text:"结束时间",align:"center", width: "10%",render:toDate2,sortDef:{type:0, direct:"desc"}},
       {type:"text", name:"vuRemark", text:"备注",align:"center", width: "7%"},
       {type:"hidden", name:"vuStatus", text:"状态",align:"center", width: "1%"},
       {type:"hidden", name:"dmerStatus", text:"状态2",align:"center", width: "1%"},
       {type:"hidden", name:"vuOperator", text:"调度员",align:"center", width: "1%"},
       {type:"selfdef", name:"caozuo", text:"操作",align:"center",width:"10%",render:toCaozuo}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件的信息!</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table);
  }
}
//车牌号
function toVID(cellData,recordIndex, columIndex){
  var vId = this.getCellData(recordIndex,"vId");
  var vId2 = this.getCellData(recordIndex,"vId2");
  var seqId =  this.getCellData(recordIndex,"seqId");
  return "<a href=javascript:vehicleDetail(" + vId2 + ")>" + vId + "</a>";
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
  var vuOperator =  this.getCellData(recordIndex,"vuOperator");
  var personId = "<%=person.getSeqId()%>";
  if (vuOperator == personId && personId == "1") {
    return "<a href=javascript:usageDetail(" + seqId + ")>详细信息</a>"
    + "&nbsp;<a href=javascript:updateVehicle(" + seqId + ")>修改</a>&nbsp;<a href=javascript:deleteUsage(" + seqId + ")>删除</a>";
  }
  if (vuOperator == personId && personId != "1") {
    return "<a href=javascript:usageDetail(" + seqId + ")>详细信息</a>"
    + "&nbsp;<a href=javascript:updateVehicle(" + seqId + ")>修改</a>";
  }
  if (personId == "1") {
    return "<a href=javascript:usageDetail(" + seqId + ")>详细信息</a>"
    + "&nbsp;<a href=javascript:deleteUsage(" + seqId + ")>删除</a>";
  }else {
    return "<a href=javascript:usageDetail(" + seqId + ")>详细信息</a>";
  }
}
//查看详情
function usageDetail(vuId) {
  window.open('<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectDetail.act?seqId=' + vuId,'','height=500,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
//车辆情况
function vehicleDetail(vId) {
  window.open("<%=contextPath%>/subsys/oa/vehicle/manage/queryVehicle.jsp?seqId=" + vId,'','height=420,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
//删除
function deleteUsage(vuId) {
  var msg = '确认要删除该记录吗？';
  if (window.confirm(msg)) {
    var url = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/deleteVehicle.act?seqId=" + vuId;
    //window.location = url;
    var json=getJsonRs(url);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      alert("删除成功!");
      window.location.reload();
    }
  }
}

//修改
function updateVehicle(vuId) {
  var url = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectId2.act?seqId=" + vuId
  window.open(url,'','height=400,width=700,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
</script>
 
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">&nbsp;<img src="<%=imgPath%>/infofind.gif"><span class="big3"> 车辆使用记录查询</span>
    </td>
  </tr>
</table>
<br>
<div id="giftList" style="padding-left: 10px; padding-right: 10px;"></div>
<div id="returnNull"></div>
<br>
<center><input type="button" class="BigButton" value="返回" onClick="javascript:history.back();"></center>
</body>
</html>
