<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%
String vuStatus = request.getParameter("vuStatus") == null ? "" :  request.getParameter("vuStatus");
String name = "";
if (vuStatus.equals("0")) {
  name = "待批申请";
}
if (vuStatus.equals("1")) {
  name = "已准申请";
}
if (vuStatus.equals("2")) {
  name = "使用中车辆";
}
if (vuStatus.equals("3")) {
  name = "未准申请";
}
if (vuStatus.equals("3")) {
  name = "未准申请";
}
if (vuStatus.equals("4")) {
  name = "使用结束车辆";
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
<script> 
//加载数据
var vuStatus = "<%=vuStatus%>";
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/useManage.act?vuStatus=" + vuStatus;
   cfgs = {
    dataAction: url,
    container: "giftList",
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"hidden", name:"vId2", text:"vId", width: "1%",align:"center"},
       {type:"text", name:"vId", text:"车牌号", width: "7%",align:"center",render:toVID},
       {type:"text", name:"vuUser", text:"用车人", width: "7%",align:"center"},
       {type:"text", name:"vuStart", text:"开始时间", width: "10%",align:"center",render:toDate,sortDef:{type:0, direct:"desc"}},
       {type:"text", name:"vuEnd", text:"结束时间",align:"center", width: "10%",render:toDate2,sortDef:{type:0, direct:"desc"}},
       {type:"hidden", name:"vuStatus", text:"状态",align:"center", width: "1%"},
       {type:"hidden", name:"dmerStatus", text:"状态2",align:"center", width: "1%"},
       {type:"selfdef", name:"caozuo", text:"操作",align:"center",width:"10%",render:toCaozuo}]
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
  //自动使用和回收
  autoUsaBack();
}
//车牌号
function toVID(cellData,recordIndex, columIndex){
  var vId = this.getCellData(recordIndex,"vId");
  var vId2 = this.getCellData(recordIndex,"vId2");
  var seqId =  this.getCellData(recordIndex,"seqId");
  return "<a href=javascript:vehicleDetail(" + vId2 + ")>" + vId + "</a>";
}

//操作
function toCaozuo(cellData,recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var vuStatus = this.getCellData(recordIndex,"vuStatus");
  var dmerStatus = this.getCellData(recordIndex,"dmerStatus");

  var isHookRun = this.getCellData(recordIndex,"isHookRun");
  var flowId = this.getCellData(recordIndex,"flowId");
  var detail="<a href=javascript:usageDetail(" + seqId + ")>详细信息</a>&nbsp;";
  var yes="<a href=javascript:checkup(" + seqId + ",1)>批准</a>&nbsp;";
  var no="<a href=javascript:operatorPeason(" + seqId + ")>不批准</a>&nbsp;";
  var edit="<a href=javascript:updateVehicle(" + seqId + ")>修改</a>&nbsp;";
  var cancel="<a href=javascript:checkCXiao(" + seqId + ",0)>撤销 </a>&nbsp;";
  var back="<a href=javascript:checkShouH(" + seqId + ",4)>收回</a>&nbsp;";
  if(isHookRun!="0"){
	  yes="";
	  no="";
	  edit="";
	  cancel="";
	  back="";
	  detail="<a href='javascript:void(0)' onclick='formView("+isHookRun+" , "+ flowId +")'>查看流程</a>&nbsp;";
	  }

  
  if(vuStatus == "0") {
    return detail
    + "<a href=javascript:openPrearrange(" + seqId + ")>预约情况</a>&nbsp;<br>"
    + yes
    + no
    + edit
    + "&nbsp;<a href=javascript:deleteUsage(" + seqId + ")>删除</a>";
  }
  if(vuStatus == "1") {
    return detail
    + "<a href=javascript:openPrearrange(" + seqId + ")>预约情况</a>&nbsp;<br>"
    + cancel
    + edit
    + "&nbsp;<a href=javascript:deleteUsage(" + seqId + ")>删除</a>";
  }
  if(vuStatus == "2") {
    return detail
    + "<a href=javascript:openPrearrange(" + seqId + ")>预约情况</a>&nbsp;<br>"
    + back
    + edit
    + "&nbsp;<a href=javascript:deleteUsage(" + seqId + ")>删除</a>";
  }
  if(vuStatus == "3") {
    return detail
    + "<a href=javascript:openPrearrange(" + seqId + ")>预约情况</a>&nbsp;<br>"
    + yes
    + edit
    + "&nbsp;<a href=javascript:deleteUsage(" + seqId + ")>删除</a>";
  }
  if(vuStatus == "4") {
    return detail
    + "<a href=javascript:openPrearrange(" + seqId + ")>预约情况</a>&nbsp;<br>"
    + "<a href=javascript:openNotes(" + seqId + ")>出车情况</a>&nbsp;"
    + edit
    + "&nbsp;<a href=javascript:deleteUsage(" + seqId + ")>删除</a>";
  }
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
//查看详情
function usageDetail(vuId) {
  window.open('<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectDetail.act?seqId=' + vuId,'','height=500,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
//车辆情况
function vehicleDetail(vId) {
  window.open("<%=contextPath%>/subsys/oa/vehicle/manage/queryVehicle.jsp?seqId=" + vId,'','height=420,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
//修改
function updateVehicle(vuId) {
  var url = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectId.act?seqId=" + vuId
  window.location.href = url;
}

//不批准原因OPERATOR_REASON
function operatorPeason(seqId) {
  window.open('<%=contextPath%>/subsys/oa/vehicle/useManage/operatorReason.jsp?seqId=' + seqId,'','height=420,width=520,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
//批准
function checkup(seqId,status) {
  var msg = '确认要批准该记录吗？';
  if (window.confirm(msg)) {
    var url = '<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/updateStatusId.act?seqId=' + seqId + '&status=' + status;
    var json=getJsonRs(url);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      if(json.rtData.flag == 1){
    	  alert("提示:您所批准的车辆在使用中！");
      }
      else{
    	  alert("批准成功!");
      }
      window.location.reload();
    }
  }
}
//撤销
function checkCXiao(seqId,status) {
  var msg = '确认要撤销该记录吗？';
  if (window.confirm(msg)) {
    var url = '<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/updateStatusId.act?seqId=' + seqId + '&status=' + status;
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
//收回
function checkShouH(seqId,status) {
  var msg = '确认要收回该使用车辆吗？';
  if (window.confirm(msg)) {
    var url = '<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/updateStatusId.act?seqId=' + seqId + '&status=' + status;
    var json=getJsonRs(url);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      alert("收回成功!");
      window.location.reload();
    }
  }
}
//预约情况
function openPrearrange() {
  window.open('<%=contextPath%>/subsys/oa/vehicle/prearrange.jsp','','height=500,width=820,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=100,resizable=yes');
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
//出车情况
function openNotes(seqId) {
  window.open('<%=contextPath%>/subsys/oa/vehicle/useManage/notes.jsp?seqId=' + seqId,'','height=150,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=200,left=300,resizable=yes');
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