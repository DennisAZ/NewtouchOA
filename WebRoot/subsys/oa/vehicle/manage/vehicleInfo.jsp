<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId==null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>车辆信息管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type ="text/javascript">
var ParSeqId = '<%=seqId%>';
function check_all(){
  for (i=0;i<document.getElementsByName("email_select").length;i++){
    if(document.getElementsByName("allbox")[0].checked)
      document.getElementsByName("email_select").item(i).checked=true;
    else
      document.getElementsByName("email_select").item(i).checked=false;
   }
  if(i==0){
    if(document.getElementsByName("allbox")[0].checked)
      document.getElementsByName("email_select").checked=true;
    else
      document.getElementsByName("email_select").checked=false;
   }
}
function check_one(el){
   if(!el.checked)
      document.getElementsByName("allbox")[0].checked=false;
}
function get_checked(){
  checked_str="";
  for(i=0;i<document.getElementsByName("email_select").length;i++){
    el=document.getElementsByName("email_select").item(i);
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
    }
  }
  if(i==0){
    el=document.getElementsByName("email_select");
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
    }
  }
  return checked_str;
}
function deleteVehicle(){
  delete_str=get_checked();
  delete_str = delete_str.substr(0,delete_str.length-1);
  if(delete_str==""){
    alert("要删除车辆，请至少选择其中一条。");
    return;
  }
  msg='确认要删除所选车辆吗？';
  if(window.confirm(msg)){
    window.location = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/deleteVehicle.act?seqIds="+delete_str;
    window.location.reload();
  }
}
var pageMgr = null;
function doOnload(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/selectVehiclePage.act?seqId="+ParSeqId; 
  var cfgs = {
      dataAction: requestURL,
      container: "listDiv",
      afterShow:insertTr,
      howRecordCnt:true,
      colums: [
         {type:"selfdef",name:"email_select", text:"选择",align:"center", width:"5%",render:toCheck},
         {type:"hidden", name:"seqId", text:"ID",align:"center", width:"10%"},
         {type:"text", name:"vModel",align:"center", text:"厂牌型号", width:"8%"},
         {type:"text", name:"vNum", text:"车牌号",align:"center", width:"7%" ,render:toVNum},
         {type:"text", name:"vDriver", text:"司机",align:"center", width:"7%"},
         {type:"text", name:"classDesc", text:"类型",align:"center", width:"6%"},
         {type:"text", name:"vDate", text:"购置日期", align:"center",width:"7%",render:tovDate},
         {type:"text", name:"vStatus", text:"状态",align:"center", width:"5%",render:tovStatus},
         {type:"hidden", name:"insuranceFlag", text:"是否提醒",align:"center"},
         {type:"selfdef", width: "30%",align:"center",text:"操作",render:opts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      document.getElementById("deleteAll").style.display = "";
    }
}
function toVNum(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var vNum = this.getCellData(recordIndex,"vNum");
  var str = "<a href='#' onclick='query_Vehicle(\""+ seqId +"\")'>" + vNum + " </a>&nbsp;" 
  return str;
}
function toCheck(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<input type='checkbox'  id='email_select' name='email_select' value='" + seqId + "' onClick='check_one(self);'>";
}
function tovStatus(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var vStatus = this.getCellData(recordIndex,"vStatus");
  var vcStatus = "正常";
  if(vStatus=="0"){
    vcStatus = "<a href='<%=contextPath%>/subsys/oa/vehicle/news.jsp?vId="+seqId+"&reqStatus=1'> 正常</a>"
  }
  if(vStatus=="1"){
    vcStatus = "损坏";
  }
  if(vStatus=="2"){
    vcStatus = "维修中";
  }
  if(vStatus=="3"){
    vcStatus = "报废";
  }
  return vcStatus;
}
function tovDate(cellData, recordIndex, columInde){
  var vDate = this.getCellData(recordIndex,"vDate");
  return vDate.substr(0,10);
}
function opts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var str = "<a href='#' onclick='openOrder(\""+ seqId +"\")'>预定情况 </a>&nbsp;" 
  + "<a href='#' onclick='Vehicle_Maintenan(\""+ seqId +"\")'>维护记录</a>&nbsp;" 
  + "<a href='#' onclick='Vehicle_File(\""+ seqId +"\")'>车辆档案 </a>&nbsp;" 
  + "<a href='#' onclick='update_Vehicle(\""+ seqId +"\")'>修改 </a>&nbsp;" 
  + "<a href='#' onclick='delete_Vehicle(\""+ seqId +"\")'>删除</a>&nbsp;";
  if(ParSeqId){
	  var insuranceFlag = this.getCellData(recordIndex,"insuranceFlag");
	  if(insuranceFlag == 0){
		  str = "<a href='#' onclick='stopInsurance(\""+ seqId +"\")'>停止保险提醒 </a>&nbsp;" + str;
	  }
  }
  return str;
}

function stopInsurance(seqId){
  var url= "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/stopInsurance.act";  
  var json=getJsonRs(url, 'seqId=' + seqId); 
  if(json.rtState == '0'){ 
	  alert("成功停止今年该车辆保险提醒！");
    window.location.reload();
  }
  else{ 
    alert(json.rtMsrg); 
  } 
}

function delete_Vehicle(seqId){  
  msg='确认要删除该车辆吗？'; 
  if(window.confirm(msg)) { 
    var url= "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/deleteVehicleById.act";  
    var json=getJsonRs(url, 'seqId=' + seqId); 
    if(json.rtState == '0'){ 
      window.location.reload();
    }else{ 
      alert(json.rtMsrg); 
    } 
  } 
} 
function update_Vehicle(seqId) { 
  window.location.href = "<%=contextPath%>/subsys/oa/vehicle/manage/updateVehicle.jsp?seqId=" + seqId; 
} 
function query_Vehicle(seqId) { 
  var requestURL= "<%=contextPath%>/subsys/oa/vehicle/manage/queryVehicle.jsp?seqId=" + seqId;
  window.open(requestURL,'','height=500,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
  //openDialogResize(requestURL , 600, 450); 
} 
function Vehicle_Maintenan(seqId){
  var requestURL= "<%=contextPath%>/subsys/oa/vehicle/maintenance/queryMaintenanceVehicle.jsp?seqId=" + seqId;
  window.open(requestURL,'','height=500,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
  //openDialogResize(requestURL , 600, 450); 
}
function Vehicle_File(seqId){
  var requestURL= "<%=contextPath%>/subsys/oa/vehicle/manage/vehicleFile.jsp?seqId=" + seqId;
  //openDialogResize(requestURL , 600, 300); 
  window.open(requestURL,"calendar","height=250,width=700,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=300,top=280,resizable=yes");    
}
function openOrder(seqId) {
  window.open('<%=contextPath%>/subsys/oa/vehicle/orderDetail.jsp?seqId=' + seqId,'','height=500,width=780,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=260,top=150,resizable=yes');
}
function insertTr(){
  var table = pageMgr.getDataTableDom();
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行
  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.align='center';
  mynewcell.innerHTML = "<input type='checkbox' name='allbox' id='allbox_for' onClick='check_all();'>";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.colSpan = "7";
  mynewcell.innerHTML = "<label for='allbox_for'>全选</label> &nbsp;<a href='javascript:deleteVehicle();' title='删除所选礼品'>"
      + "<img src='<%=imgPath%>/delete.gif' align='absMiddle'/>&nbsp;删除所选车辆</a>&nbsp";
}
function test(){
  var url= "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/Test.act";  
  var json=getJsonRs(url); 
}
</script>
</head>
<body  topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/vehicle.gif" HEIGHT="20"><span class="big3"> 车辆信息管理</span>
    </td>
  </tr>
</table>
<br>
  <div id = "listDiv">
  </div>
  <div id="deleteAll" style="display:none">
</div>


</body>
</html>  