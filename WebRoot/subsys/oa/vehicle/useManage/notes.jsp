<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%
 String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
 %>
<html>
<head>
<title>出车情况 </title>
</head>
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
<script type="text/javascript">
var seqId = "<%=seqId%>";
//提交申请
function doInit() {
  //var pars = $('form1').serialize() ;
  var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/selectNotes.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURL); 
  var prcsJsons = rtJson.rtData; 
  if (rtJson.rtState == '1') {
    alert(rtJson.rtMsrg); 
    return ;
  }
  if (prcsJsons.vuMileageTrue > 0) {
    $('vuMil').update("<input type='text' name='vuMileageTrue' id='vuMileageTrue' size='5'  maxlength='5' class='SmallInput' value='" + prcsJsons.vuMileageTrue +"'>"); 
  }
  if (prcsJsons.vuMileageTrue <= 0) {
    $('vuMil').update("<input type='text' name='vuMileageTrue' id='vuMileageTrue' size='5' maxlength='5' class='SmallInput' value=''>"); 
  }
  if (prcsJsons.vuParkingFees > 0) {
    $('vuParking').update("<input type='text' name='vuParkingFees' id='vuParkingFees' size='5' maxlength='5' class='SmallInput' value='" + prcsJsons.vuParkingFees +"'>"); 
  }
  if (prcsJsons.vuParkingFees <= 0) {
    $('vuParking').update("<input type='text' name='vuParkingFees' id='vuParkingFees' size='5' maxlength='5' class='SmallInput' value=''>"); 
  }
}
function IsNumber(str) {
  return str.match(/^[0-9]*$/) != null;
}
function checkForm() {
  if (document.getElementById("vuMileageTrue").value != "" && !IsNumber(document.getElementById("vuMileageTrue").value)) {
    alert("里程应为数字！");
    document.getElementById("vuMileageTrue").focus();
    document.getElementById("vuMileageTrue").select();
    return (false);
  }
  if (document.getElementById("vuParkingFees").value != "" && !IsNumber(document.getElementById("vuParkingFees").value)){
    alert("费用应为数字！");
    document.getElementById("vuParkingFees").focus();
    document.getElementById("vuParkingFees").select();
    return (false);
  }
  return true;
}
//提交申请
function checkForm2() {
  if (checkForm()) {
    var pars = $('form1').serialize() ;
    var requestURL="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/updateNotes.act";
    var json=getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      alert("修改成功!");
      window.close();
      parent.opener.location.reload();
    }
  }
}
</script>
<body class="bodycolor" topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/vehicle.gif"><span class="big3"> 出车情况</span>
    </td>
  </tr>
</table>
<form name="form1" id="form1">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.vehicle.data.YHVehicleUsage">
<table class="TableBlock" align="center" width="90%">
    <tr>
      <td nowrap class="TableContent" width="20%">里程数：</td>
      <td class="TableData">
        <span id="vuMil"></span>&nbsp;公里</td>
   </tr>
   <tr>
      <td nowrap class="TableContent" width="20%"> 费用：</td>
      <td class="TableData">
       <span id="vuParking"></span>&nbsp;元(包括停车费,高速费,过桥费,加油费等)</td>      
    </tr>
  <tr class="TableControl">
      <td nowrap colspan="2" align="center">
    <input type="hidden" name="seqId" id="seqId" value="<%=seqId%>">
     <input type="button" value="保存" class="BigButton" onclick="checkForm2();">&nbsp;&nbsp;
    <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();window.close();  ">
      </td>
    </tr>
</table>
</form>
</body>
