<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>维护记录管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type ="text/javascript">
function getVehicleRepairType(){
  //VEHICLE_REPAIR_TYPE
  var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/getCodeItem2.act"; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prcs = json.rtData; 
  var selectObj = $("vmType");
  for(var i = 0 ; i < prcs.length ; i++){
    var prc = prcs[i];
    var seqId = prc.classCode;
    var classNO = prc.classNo;
    var calssDesc = prc.classDesc;
    var myOption = document.createElement("option");
    myOption.value = seqId;
    myOption.text = calssDesc;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}
function CheckForm(){

  if(document.form1.vmBeginDate.value!=""&&!isValidDateStr($("vmBeginDate").value)){
    alert("维护开始日期格式不对，形如：2010-01-01");
    $("vmBeginDate").focus();
    $("vmBeginDate").select();
    return false;
  }
  if(document.form1.vmEndDate.value!=""&&!isValidDateStr($("vmEndDate").value)){
    alert("维护开始日期格式不对，形如：2010-01-01");
    $("vmEndDate").focus();
    $("vmEndDate").select();
    return false;
  }
  if($("vmFeeMin").value!=''&& !isNumber($("vmFeeMin").value)){
    alert("维护费用应为数字！");
    $("vmFeeMin").focus();
    $("vmFeeMin").select();
    return false;
  }
  if($("vmFeeMax").value!=''&& !isNumber($("vmFeeMax").value)){
    alert("维护费用应为数字！");
    $("vmFeeMax").focus();
    $("vmFeeMax").select();
    return false;
  }
  return true;
}
function doOnload(){
  getVehicleRepairType();
    var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/selectVehicle.act"; 
    var json = getJsonRs(requestURL); 
    //alert(rsText);
    if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
    } 
    var prcsJson = json.rtData;
    var select = document.getElementById("vId"); 
    for(var i=0;i<prcsJson.length;i++){ 
    // alert(i+":"+prcsJson.length);
    var option = document.createElement("option"); 
    option.value = prcsJson[i].seqId; 
    option.innerHTML = prcsJson[i].vNum; 
    select.appendChild(option);  
    }

    var date1Parameters = {
        inputId:'vmBeginDate',
        property:{isHaveTime:false}
        ,bindToBtn:'date1'
    };
    new Calendar(date1Parameters);
    var date1Parameters = {
        inputId:'vmEndDate',
        property:{isHaveTime:false}
        ,bindToBtn:'date2'
    };
    new Calendar(date1Parameters);
}
function doInit(orderType){
  if(CheckForm()){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleMaintenanceAct/selectVehicleMaintenance.act?orderType="+orderType; 
    var json = getJsonRs(requestURL,mergeQueryString($("form1"))); 
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    } 
    var prcsJson = json.rtData;
    
    $("term").style.display = "none";
    $("maintenanceTable").style.display = "";
    $("maintenanceList").style.display = "";

    var arrowGif = "arrow_down.gif";
    if(orderType=='asc'){
      orderType = "desc";
      arrowGif = "arrow_up.gif";
 
    }else{
      orderType = "asc";
      arrowGif = "arrow_down.gif";
    }
    if(prcsJson.length>0){
      var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tr class='TableHeader'>"
          +" <td nowrap align='center'>车牌号</td>"
          +"<td nowrap align='center'>维护类型</td>"
          +"<td nowrap align='center'>维护原因</td>"
          +"<td nowrap align='center' onclick=doInit('"+orderType+"') style='cursor:pointer;' width='30%'><u>维护日期</u><img border=0 src='<%=imgPath%>/"+arrowGif+"' width='11' height='10'></td> "     
          +"<td nowrap align='center'>经办人</td>"
          +"<td nowrap align='center'>维护费用</td>"
          +"<td nowrap align='center'>备注</td>"
          +"<td nowrap align='center'>操作</td></tr>");
      $("maintenanceList").innerHTML = "";
      $("maintenanceList").appendChild(table);
      for(var i=0; i<prcsJson.length ; i ++){
         var prc = prcsJson[i];
         var vmType = prc.vmType;
         var vmTypeDesc = "";
         if(vmType=='0'){
 		      vmTypeDesc = "维修";
 		  }
 		  if(vmType=='1'){
 		    vmTypeDesc = "加油";
 		  }
 		  if(vmType=='2'){
 		    vmTypeDesc = "洗车";
 		  }
 		  if(vmType=='3'){
 		    vmTypeDesc = "年检";
 		  }
 		 if(vmType=='4'){
		    vmTypeDesc = "其他";
		  }
         var tr = new Element('tr',{"class":"TableData"});
         table.appendChild(tr);
         tr.update("<td align='center'>" + prc.vNum + "</td>"
             +"<td align='center'>" + vmTypeDesc + "</td>"
             +"<td align='center'>" + prc.vmReason + "</td>"
             +"<td align='center'>" + prc.vmRequestDate + "</td>"
             +"<td align='center'>" + prc.vmperson + "</td>"
             +"<td align='center'>" + insertKiloSplit(prc.vmFee,2) + "</td>"
             +"<td align='center'>" + prc.vmRemark + "</td>"
             +"<td align='center'>"
             +"<a href='javascript:update_vehicle(" + prc.seqId + ");'>修改</a>&nbsp;"
             +"<a href='javascript:delete_vehicle(" + prc.seqId + ");'>删除</a></td>");
      }

      var tr = new Element('tr',{"class":"TableControl"});
      table.appendChild(tr);
      tr.update("<td nowrap align='center'><b>合计：</b></td>"
         +"<td nowrap align='center' colspan='4'></td>"
         +"<td nowrap align='right'><span id = 'feeTotal'></span> </td>"
         +"<td nowrap align='center' colspan='2'>"+insertKiloSplit(json.rtMsrg,2)+"</td>");

      var div = new Element('div' ,{"align":"center"});
      $("maintenanceList").appendChild(div);
      div.update("<input type='button' value='导出' class='BigButton' onclick='exportCSV()'>&nbsp;&nbsp;"
       +"<input type='button' class='BigButton' value='打印' onclick='window.print();'>&nbsp;&nbsp;"
       +"<input type='button' value='返回' class='BigButton' onclick='returns();'>");
    }else{
      var table = new Element('table',{"class":"MessageBox", "align":"center" ,"width":"300"}).update("<tr>"
        +"<td class='msg info'>"
        +"<div class='content' style='font-size:12pt'>暂无车辆维护信息</div>"
        +"</td></tr>");
      $("maintenanceList").innerHTML = "";
      $("maintenanceList").appendChild(table);
    }
  }		
}
function returns(){
  window.location.href = "<%=contextPath %>/subsys/oa/vehicle/maintenance/maintenanceTerm.jsp";
}

function delete_vehicle(seqId){
  var msg='确认要删除该维护信息吗？';
  if(window.confirm(msg)){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleMaintenanceAct/deleteVehicleMiantenById.act?seqId="+seqId ;
    var json = getJsonRs(requestURL); 
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    } 
    //window.location.reload();
    doInit('desc');
  }
}
function update_vehicle(seqId){ 
  window.location.href = "<%=contextPath%>/subsys/oa/vehicle/maintenance/updatevehiclemainten.jsp?seqId=" + seqId;
}
function exportCSV(){
  var vId = $("vId").value;
  var vmBeginDate = $("vmBeginDate").value;
  var vmType = $("vmType").value;
  var vmReason = $("vmReason").value;
  var vmPerson = $("vmPerson").value;
  var vmEndDate = $("vmEndDate").value;
  var vmFeeMin = $("vmFeeMin").value;
  var vmFeeMax = $("vmFeeMax").value;
  var vmRemark = $("vmRemark").value;
  var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleMaintenanceAct/exportCSV.act?vId="+vId+"&vmBeginDate="+vmBeginDate+"&vmReason="+vmReason+"&vmPerson="+vmPerson+"&vmEndDate="+vmEndDate+"&vmFeeMin="+vmFeeMin+"&vmFeeMax="+vmFeeMax+"&vmRemark="+vmRemark ;
  requestURL = encodeURI(requestURL);
  window.location.href = requestURL;
}
function doInit2(){
  if(CheckForm()){
    var query = $("form1").serialize(); 
    window.location.href = "<%=contextPath%>/subsys/oa/vehicle/maintenance/selectvehiclemaintenance.jsp?"+query;
    
  }
}
</script>
</head>
<body onload = "doOnload()">
<div id="term">
 <form action="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleMaintenanceAct/selectVehicleMaintenance.act" enctype="multipart/form-data" method="get" name="form1" id = "form1" >
<table class="TableBlock" width="500"align="center">
  <tr>
      <td colspan="2" nowrap class="TableHeader">
      <img src="<%=imgPath%>/green_arrow.gif"> 请指定查询条件：
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80"> 车 牌 号：</td>
      <td class="TableData" width="470">
        <select id = "vId" name="vId" >
            <option value="">请选择</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 维护日期：</td>
      <td class="TableData">
        <input type="text" id ="vmBeginDate" name="vmBeginDate" size="10" maxlength="10" class="BigInput" value="">
        <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" align="absMiddle" style="cursor:pointer" > 至
        <input type="text" id = "vmEndDate" name="vmEndDate" size="10" maxlength="10" class="BigInput" value="">
        <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" align="absMiddle" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 维护类型：</td>
      <td class="TableData">
        <SELECT id = "vmType" name="vmType" >
          <option value="">请选择</option>
        </SELECT>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 维护原因：</td>
      <td class="TableData">
        <input type="text" id = "vmReason" name="vmReason" size="30" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 经 办 人：</td>
      <td class="TableData">
        <input type="text" id = "vmPerson" name="vmPerson" size="10" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 维护费用：</td>
      <td class="TableData">
        <input type="text" id = "vmFeeMin" name="vmFeeMin" size="10" maxlength="15" class="BigInput" value="" > 至
        <input type="text" id = "vmFeeMax" name="vmFeeMax" size="10" maxlength="15" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 备　　注：</td>
      <td class="TableData">
        <input type="text" id = "vmRemark" name="vmRemark" size="30" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="查询" class="BigButton" onclick="doInit2();">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">
      </td>
    </tr>
  </table>
</form>
</div>
<div id="maintenanceTable" style="display:none">
   <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/vehicle.gif" HEIGHT="20"><span class="big3"> 车辆维护管理</span>
    </td>
  </tr>
</table>
  <div id="maintenanceList"></div>
</div>
</body>
</html>