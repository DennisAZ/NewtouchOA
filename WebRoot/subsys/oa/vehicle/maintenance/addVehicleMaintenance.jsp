<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>添加车辆维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<script type="text/javascript" >
function CheckForm(){
  if(document.form1.vId.value == ""){
    alert("车牌号不能为空！");
    $("vId").focus();
    $("vId").select();
    return false;
  }
  
  if(document.form1.vmRequestDate.value!=""&&!isValidDateStr($("vmRequestDate").value)){
    alert("购买日期格式不对，形如：2010-01-01！");
    $("vmRequestDate").focus();
    $("vmRequestDate").select();
    return false;
  }

  if(document.form1.vmFee.value!=""){
    if(!isNumber(document.form1.vmFee.value) || document.form1.vmFee.value < 0){
      alert("你输入的维护费用应是有效数字！");
      $("vmFee").focus();
      $("vmFee").select();
      return false;
    }
   }
  
  return true;
}
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
function doInit(){
  if(CheckForm()){
    var url="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleMaintenanceAct/addVehicleMainten.act";
    var rtJson = getJsonRs(url, mergeQueryString($("form1")));
    if(rtJson.rtState == '1'){ 
      alert(rtJson.rtMsrg); 
      return ; 
    }
    if(rtJson.rtState == '0'){ 
      alert(rtJson.rtMsrg); 
     } 
    window.location.reload();
   }
}
  function doOnload(){
    getVehicleRepairType();
    var requestURL="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/selectVehicle.act"; 
    var json=getJsonRs(requestURL); 
    //alert(rsText);
    if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
    } 
    var prcsJson = json.rtData;
    var select = document.getElementById("vId"); 
    for(var i=0;i<prcsJson.length;i++){ 
     // alert(i+":"+prcsJson.length);
    var option=document.createElement("option"); 
    option.value=prcsJson[i].seqId; 
    option.innerHTML=prcsJson[i].vNum; 
    select.appendChild(option);  
    }
    
    var date1Parameters = {
        inputId:'vmRequestDate',
        property:{isHaveTime:false}
        ,bindToBtn:'date'
    };
    new Calendar(date1Parameters);
  }
</script>
</head>

<body class="" topmargin="5" onload = "doOnload()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" HEIGHT="20"><span class="big3"> 车辆维护记录</span>
    </td>
  </tr>
</table>
<form action="#" method="post" name="form1" id = "form1">
<table class="TableBlock" align="center" width="550">
   <tr>
      <td nowrap class="TableContent" width="80"> 车 牌 号：<label style="color: red">*</label></td>
      <td class="TableData" colspan="3" width="470">
          <select id = "vId" name="vId">
          </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80"> 维护日期：</td>
      <td class="TableData" width="260">
        <input type="text" id = "vmRequestDate" name="vmRequestDate" size="12" maxlength="10" class="BigInput" value="">
        <img id="date" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" align="absMiddle" style="cursor:pointer" >
      </td>
      <td nowrap class="TableContent" width="80"> 维护类型：</td>
      <td class="TableData" width="130">
        <SELECT id="vmType" name="vmType" >
        </SELECT>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80"> 维护原因：</td>
      <td class="TableData" colspan="3">
        <textarea id = "vmReason" name="vmReason" class="BigInput" cols="60" rows="2"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80"> 经 办 人：</td>
      <td class="TableData">
        <input type="text" id = "vmPerson" name="vmPerson" size="12" maxlength="100" class="BigInput" value="">
      </td>
      <td nowrap class="TableContent" width="80"> 维护费用：</td>
      <td class="TableData" width="130">
        <input type="text" id = "vmFee" name="vmFee" size="12" maxlength="25" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80"> 备　　注：</td>
      <td class="TableData" colspan="3">
        <textarea id = "vmRemark" name="vmRemark" class="BigInput" cols="60" rows="2"></textarea>
      </td>
    </tr>
    <tr class="TableControl">
      <td nowrap colspan="4" align="center">
        <input type="hidden" value="" name="seqId">
        <input type="button" value="保存"  onclick = "doInit()" class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
    </table>
</form>

</body>
</html>