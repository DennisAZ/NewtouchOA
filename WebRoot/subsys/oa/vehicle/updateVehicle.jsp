<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.vehicle.data.YHVehicleUsage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
YHVehicleUsage usage = (YHVehicleUsage)request.getAttribute("usage");
%>
<html>
<head>
<title></title>
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/style.css">
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/sms/js/smsutil.js"></script>
<%if(usage != null) { %>
<script Language="JavaScript"> 
var count = 0;
function IsNumber(str) {
  return str.match(/^[0-9]*$/) != null;
}
function checkForm() {
  if (document.getElementById("vId2").value == "") {
    alert("请指定车辆！");
    document.getElementById("vId2").focus();
    document.getElementById("vId2").select();
    return (false);
  }
  if ($("vuStart").value == "") {
    alert("起始时间不能为空！");
    document.getElementById("vuStart").focus();
    document.getElementById("vuStart").select();
    return (false);
  }
  if (document.getElementById("vuEnd").value == "") {
    alert("结束时间不能为空！");
    document.getElementById("vuEnd").focus();
    document.getElementById("vuEnd").select();
    return (false);
  }
  if ($("vuStart").value != "" && checkDateTime("vuStart") == false) {
    alert("起始日期格式不对，请输入形如：2010-10-10 12:12:12");
    document.getElementById("vuStart").focus();
    document.getElementById("vuStart").select();
    return (false);
  }
  if ($("vuEnd").value != "" && checkDateTime("vuEnd") == false) {
    alert("结束日期格式不对，请输入形如：2010-10-10 12:12:12");
    document.getElementById("vuEnd").focus();
    document.getElementById("vuEnd").select();
    return (false);
  }
  if (document.getElementById("vuStart").value == document.getElementById("vuEnd").value) {
    alert("开始时间与结束时间不能相等！");
    document.getElementById("vuEnd").focus();
    document.getElementById("vuEnd").select();
    return (false);
  }
  if (document.getElementById("vuStart").value >= document.getElementById("vuEnd").value) {
    alert("开始时间不能大于结束时间！");
    document.getElementById("vuEnd").focus();
    document.getElementById("vuEnd").select();
    return (false);
  }
  if (document.getElementById("vuMileage").value != "" && !IsNumber(document.getElementById("vuMileage").value)){
    alert("里程应为整数！");
    document.getElementById("vuMileage").focus();
    document.getElementById("vuMileage").select();
    return (false);
  }
  if(document.getElementById("vuOperator").value=="") {
    alert("请指定调度人员！");
    document.getElementById("vuOperator").focus();
    document.getElementById("vuOperator").select();
    return (false);
  }
  if (document.getElementById("deptManagerDesc").value != "") {
	  $('dmerStatus').value="0";
    document.getElementById("showFlag").value = "0";
  }
  if (document.getElementById("deptManagerDesc").value == "") {
    document.getElementById("showFlag").value = "1";
  }
  return true;
}
//提交申请
function checkForm2() {
  if (checkForm()) {
    var pars = $('form1').serialize() ;
    var requestURL="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/editVehicle.act";
    var json=getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      alert("修改成功!");
      window.location.reload();
    }
  }
}
//司机名称
function doUser() {
  if(document.getElementById("vuDriver").value.trim() != ""){
    bindDesc([{cntrlId:"vuDriver", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }else {
    document.getElementById("vuDriverDesc").value = "";
  }
}
function showvehicle(){
  var select = document.getElementById("vId2"); 
  var tem1 = document.getElementById("vId2").value.indexOf("~`");
  var tem2 = document.getElementById("vId2").value.split("~`")[0];
  var tem3 = document.getElementById("vId2").value.split("~`")[1];
  if(count != 0){
	  document.getElementById("vuDriver").value = tem3;
  }
  document.getElementById("vId").value = document.getElementById("vId2").value.split("~`")[0];
  document.getElementById("vehicle_detail").src = "<%=contextPath %>/subsys/oa/vehicle/queryVehicle.jsp?seqId=" + tem2;
  doUser();
}

function doInit() {
  selectNum();
  doName();
  //时间
  var parameters = {
      inputId:'vuStart',
      property:{isHaveTime:true}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
  var parameters = {
      inputId:'vuEnd',
      property:{isHaveTime:true}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
  doUserName();
  moblieSmsRemind('sms2Remind3','sms2Check');
  moblieSmsRemind('sms2Remind2','SMS2_REMIND');
  getSysRemind();
  toNameStr();
  selectValue("<%=usage.getVId()%>");
  selectValue2("<%=usage.getVuOperator()%>");
  count++;
}

//车牌号
function selectNum() {
  var requestURL="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/selectUseVehicle.act";
  var json=getJsonRs(requestURL);
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prcsJson = json.rtData; 
  var select = document.getElementById("vId2"); 
  for (var i = 0;i < prcsJson.length; i++) { 
    var option = document.createElement("option");
    option.value = prcsJson[i].seqId + "~`" + prcsJson[i].vDriver; //seqId
    option.text = prcsJson[i].vNum;//车牌号

    var selectObj = $('vId2');
    selectObj.options.add(option, selectObj.options ? selectObj.options.length : 0); 
  }
 // showDetail();
  showvehicle()
}
//调度人员名称
function doName() {
  var requestURLs = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleOperatorAct/selectManagerPerson.act"; 
  var rtJson = getJsonRs(requestURLs); 
  if(rtJson.rtState == '1') {
    alert(rtJson.rtMsrg); 
    return ; 
  }
  var prcsJsons = rtJson.rtData; 
  var selects = document.getElementById("vuOperator"); 
  for(var i = 0;i < prcsJsons.length; i++){ 
    var option = document.createElement("option"); 
    option.value = prcsJsons[i].seqId; 
    option.text = prcsJsons[i].userName;
    var selectObj = $('vuOperator');
    selectObj.options.add(option,selectObj.options ? selectObj.options.length : 0); 
  }
}
//调度人员名称
function doUserName() {
  var requestURLs = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleOperatorAct/selectPerson.act"; 
  var rtJson = getJsonRs(requestURLs); 
  var prcsJsons = rtJson.rtData; 
  if(rtJson.rtState == '1') {
    alert(rtJson.rtMsrg); 
    return ; 
  }
  var div = new Element('div').update(prcsJsons);
  $('listName').appendChild(div); 
}
var vuDeptField = null;
var vuDeptNameField = null;
function getDept(vuDept,vuDeptName){
  vuDeptField = vuDept;
  vuDeptNameField = vuDeptName;
  var URL= contextPath + "/subsys/oa/vehicle/getDept.jsp";
  openDialogResize(URL , 300, 355);
}

/** 
 *js代码 
 *是否显示手机短信提醒 
 */
function moblieSmsRemind(remidDiv,remind) {
   var requestUrl = "<%=contextPath%>/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=9";
   var rtJson = getJsonRs(requestUrl); 
   if (rtJson.rtState == "1"){ 
     alert(rtJson.rtMsrg); 
     return ; 
   }
   var prc = rtJson.rtData;
   var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
   if (moblieRemindFlag == '2') {
     $(remidDiv).style.display = ''; 
     $(remind).checked = true;
     document.getElementById("smsSJ").value = "1";
     document.getElementById("sms2Remind").value = "1";
   } else if(moblieRemindFlag == '1') { 
     $(remidDiv).style.display = ''; 
     $(remind).checked = false; 
   }else{
     $(remidDiv).style.display = 'none'; 
   }
 }

//判断是否要显示短信提醒 
function getSysRemind(){ 
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=9"; 
  var rtJson = getJsonRs(requestUrl); 
  if (rtJson.rtState == "1") { 
    alert(rtJson.rtMsrg); 
    return ; 
  }
  var prc = rtJson.rtData; 
  var allowRemind = prc.allowRemind; 
  var defaultRemind = prc.defaultRemind; 
  var mobileRemind = prc.mobileRemind;
  if (allowRemind == '2') {
    $("smsRemindDiv").style.display = 'none';
    $("smsRemindDiv2").style.display = 'none';
  }else{ 
    if (defaultRemind == '1') { 
      $("smsflag2").checked = true;
      $("SMS_REMIND").checked = true;
      document.getElementById("smsflag").value = "1";
      document.getElementById("smsRemind").value = "1";
    }
  }
}
//选择发送消息
function checkBox2() {
  if (document.getElementById("smsflag2").checked) {
     document.getElementById("smsflag").value = "1";
  }else {
   document.getElementById("smsflag").value = "0";
  }
}

function checkBox3() {
  if (document.getElementById("sms2Check").checked) {
     document.getElementById("smsSJ").value = "1";
  }else {
   document.getElementById("smsSJ").value = "0";
  }
}

//提醒申请人
function checkBox(ramCheck,sms) {
  if (document.getElementById(ramCheck).checked) {
     document.getElementById(sms).value = "1";
  }else {
   document.getElementById(sms).value = "0";
  }
}
//SEQID转成NAME
function toNameStr() {
  if(document.getElementById("vuUser").value.trim() != ""){
    bindDesc([{cntrlId:"vuUser",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("vuDept").value.trim() != ""){
    bindDesc([{cntrlId:"vuDept",dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
  }
  if(document.getElementById("deptManager").value.trim() != ""){
    bindDesc([{cntrlId:"deptManager",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
}
//选中车牌
function selectValue(val){
  var otypo = document.getElementById("vId2");
  for (var i = 0; i < otypo.options.length; i++) {
      if (otypo.options[i].value.split("~`")[0] == val) {
        otypo.options[i].selected = true;
        if(count != 0){
        	document.getElementById("vuDriver").value = otypo.options[i].value.split("~`")[1];
        }
        document.getElementById("vId").value = otypo.options[i].value.split("~`")[0];
        document.getElementById("vehicle_detail").src = "<%=contextPath %>/subsys/oa/vehicle/queryVehicle.jsp?seqId=" + otypo.options[i].value.split("~`")[0];
      }
  }
}
//选中调度员
function selectValue2(val){
  var otypo = document.getElementById("vuOperator");
  for (var i = 0; i < otypo.options.length; i++) {
      if (otypo.options[i].value == val) {
        otypo.options[i].selected = true;
      }
  }
  if(document.getElementById("vuDriver").value.trim() != ""){
    bindDesc([{cntrlId:"vuDriver", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
}

function openPer() {
  window.open('<%=contextPath%>/subsys/oa/vehicle/prearrange.jsp','','height=500,width=820,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=100,resizable=yes');
}
</script>
</head>
 
<body class="bodycolor" topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif"><span class="big3"> 车辆使用编辑</span>
    </td>
  </tr>
</table>
<br>
<form id="form1" name="form1">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.vehicle.data.YHVehicleUsage">
<input type="hidden" name="seqId" value="<%=usage.getSeqId()%>">
<input type="hidden" name="vuProposer" value="<%=usage.getVuProposer()%>">
<input type="hidden" name="vuRequestDate" value="<%=usage.getVuRequestDate().toString().substring(0,19)%>">
<input type="hidden" name="vuStatus" value="<%=usage.getVuStatus()%>">
<input type="hidden" name="showFlag" id="showFlag"  value="<%=usage.getShowFlag() %>">
<input type="hidden" name="dwerStatus" id="dwerStatus"  value="<%=usage.getDmerStatus() %>">
<input type="hidden" name="vuMileageTrue" id="vuMileageTrue"  value="<%=usage.getVuMileageTrue() %>">
<input type="hidden" name="vuParkingFees" id="vuParkingFees"  value="<%=usage.getVuParkingFees() %>">
<input type="hidden" id="smsRemind" name="smsRemind" value="<%=usage.getSmsRemind() %>">
<input type="hidden" id="sms2Remind" name="sms2Remind" value="<%=usage.getSms2Remind() %>">
<input type="hidden" id="dmerStatus" name="dmerStatus" value="1">
<table class="TableBlock" align="center" width="90%">
    <tr>
      <td nowrap class="TableData" width="80"> 车 牌 号：<label style="color: red">*</label></td>
      <td class="TableData" width="230">
      <input type="hidden" id="vId" name="vId" value="">
        <select name="vId2" id="vId2" onchange="showvehicle();">
        </select>
      &nbsp;<a href="javascript:;" onClick="javascript:openPer();">预约情况</a>        
      </td>
      <td nowrap class="TableData" width="80"> 司　　机：</td>
      <td class="TableData" >
       <input type="hidden" name="vuDriver" id="vuDriver" value="<%=usage.getVuDriver() %>">
       <input type="text" name="vuDriverDesc" id="vuDriverDesc" class="BigStatic" value="" readonly="readonly">
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['vuDriver','vuDriverDesc']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('vuDriver').value='';$('vuDriverDesc').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 用 车 人：</td>
      <td class="TableData">
      <%if(YHUtility.isNullorEmpty(usage.getVuUser())) {%>
          <input type="hidden" name="vuUser" id="vuUser" value="">
      <%}else {%>
         <input type="hidden" name="vuUser" id="vuUser" value="<%=usage.getVuUser() %>">
      <%} %>
        <input type="text" name="vuUserDesc" id="vuUserDesc" class="BigStatic" value="" readonly>
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['vuUser','vuUserDesc']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('vuUser').value='';$('vuUserDesc').value='';">清空</a>
      </td>
      <td nowrap class="TableData" width="80"> 用车部门：</td>
      <td class="TableData">
      <%if(YHUtility.isNullorEmpty(usage.getVuDept())) {%>
         <input type="hidden" name="vuDept" id="vuDept" value="">
      <%}else {%>
        <input type="hidden" name="vuDept" id="vuDept" value="<%=usage.getVuDept() %>">
      <%} %>
        <input type="text" name="vuDeptDesc" id="vuDeptDesc" value="" class=BigStatic size=20 maxlength=100 readonly>
        <a href="javascript:;" class="orgAdd" onClick="getDept('vuDept','vuDeptDesc');">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('vuDept').value='';$('vuDeptDesc').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 起始时间：<label style="color: red">*</label></td>
      <td class="TableData">
        <input type="text" id ="vuStart" name="vuStart" size="20" maxlength="19" class="BigInput" value="<%=usage.getVuStart().toString().substring(0,19)%>"> 
      <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">
      
      </td>
      <td nowrap class="TableData" width="80"> 结束时间：<label style="color: red">*</label></td>
      <td class="TableData">
        <input type="text" id ="vuEnd" name="vuEnd" size="20" maxlength="19" class="BigInput" value="<%=usage.getVuEnd().toString().substring(0,19)%>">     
      <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 目 的 地：</td>
      <td class="TableData">
      <%if(usage.getVuDestination() != null) {%>
        <input type="text" name="vuDestination" size="20" maxlength="100" class="BigInput" value="<%=usage.getVuDestination().replace("\"","")%>">
      <%}else {%>
      <input type="text" name="vuDestination" size="20" maxlength="100" class="BigInput" value="">
      <%} %>
      </td>
      <td nowrap class="TableData" width="80"> 里　　程：</td>
      <td class="TableData">
        <input type="text" name="vuMileage" id="vuMileage" size="10" maxlength="10" class="BigInput" value="<%=usage.getVuMileage() %>"> (公里)
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 部门审批人：</td>
      <td class="TableData">
      <%if(YHUtility.isNullorEmpty(usage.getDeptManager())) {%>
      <input type="hidden" name="deptManager" id="deptManager" value="">
      <%}else {%>
      <input type="hidden" name="deptManager" id="deptManager" value="<%=usage.getDeptManager()%>">
      <%} %>
        <input type="text" name="deptManagerDesc" id="deptManagerDesc"  size="13" class="BigStatic" value="" readonly>&nbsp;
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['deptManager','deptManagerDesc']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('deptManager').value='';$('deptManagerDesc').value='';">清空</a>
      </td>
      <td nowrap class="TableData" width="80"> 调 度 员：<label style="color: red">*</label></td>
      <td class="TableData">
        <select name="vuOperator" id="vuOperator">
        </select>  (注：负责审批)
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 在线调度<br>人员：</td>
      <td class="TableData" colspan="3"><div id="listName"></div> </td>
    </tr>   
    <tr>
      <td nowrap class="TableData" width="80"> 事　　由：</td>
      <td class="TableData" colspan="3">
        <textarea name="vuReason" id="vuReason" class="BigInput" cols="74" rows="2"><%if(usage.getVuReason() != null){%><%=usage.getVuReason() %><%} %></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 备　　注：</td>
      <td class="TableData" colspan="3">
        <textarea name="vuRemark" class="BigInput" cols="74" rows="2"><%if(usage.getVuRemark() != null){%><%=usage.getVuRemark() %><%} %></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 提醒调度员：</td>
      <td class="TableData" colspan="3">
      <span id="smsRemindDiv">&nbsp;
<input type="checkbox" name="smsflag2" id="smsflag2" onClick="checkBox2();">
  <label>使用内部短信提醒</label>&nbsp;&nbsp;</span>

  <span id="sms2Remind3">&nbsp;&nbsp;<input type="checkbox" name="sms2Check" id="sms2Check" onClick="checkBox3();">
<label>使用手机短信提醒</label>&nbsp;&nbsp;
</span>
<input type="hidden" name="smsflag" id="smsflag" value="0">
<input type="hidden" name="smsSJ" id="smsSJ" value="0">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData" width="80">通知申请人：</td>
    <td class="TableData" colspan="3">
<span id="smsRemindDiv2">&nbsp;
<input type="checkbox" name="SMS_REMIND" id="SMS_REMIND" onClick="checkBox('SMS_REMIND','smsRemind')">
<label>使用内部短信提醒</label>&nbsp;&nbsp;
</span>
<span id="sms2Remind2">&nbsp;
<input type="checkbox" name="SMS2_REMIND" id="SMS2_REMIND" onClick="checkBox('SMS2_REMIND','sms2Remind')">
<label>使用手机短信提醒</label> 
</span>
 </td>
  </tr>
    <tr class="TableControl">
      <td nowrap colspan="4" align="center">
        <input type="button" value="保存" class="BigButton" onclick="checkForm2();">&nbsp;&nbsp;
        <input type="button" class="BigButton" value="返回" onClick="javascript:history.back();">
      </td>
    </tr>
    
    <tr class="TableControl">
    <td  nowrap colspan="4">
    <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr><td>
    <iframe id="vehicle_detail" width="90%" height="220" frameBorder="0" frameSpacing="0" scrolling="yes" align="center">
</iframe>
</td></tr>
</table>
    </td>
    </tr>   
    </table>
</form>
<%} else {%>
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>未找到相应的记录!</div>
    </td>
  </tr>
</table>
<%} %>
</body>
</html>
