<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.vehicle.data.YHVehicleUsage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%
YHVehicleUsage usage = (YHVehicleUsage)request.getAttribute("usage");
%>
<html>
<head>
<title>车辆使用信息修改</title>
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
<script type="text/javascript" src="<%=contextPath%>/core/funcs/sms/js/smsutil.js"></script>
<%if(usage != null) { %>
<script Language="JavaScript"> 
function IsNumber(str) {
   return str.match(/^[0-9]*$/) != null;
}
function checkForm() {
  if (document.getElementById("vuEnd").value == "") {
    alert("结束时间不能为空！");
    document.getElementById("vuEnd").focus();
    document.getElementById("vuEnd").select();
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
  return true;
}
function doInit() {
  if(document.getElementById("vuDriver").value.trim() != ""){
    bindDesc([{cntrlId:"vuDriver",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("vId").value.trim() != ""){
    bindDesc([{cntrlId:"vId",dsDef:"VEHICLE,SEQ_ID,V_NUM"}]);
  }
  if(document.getElementById("vuUser").value.trim() != ""){
    bindDesc([{cntrlId:"vuUser",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("vuDept").value.trim() != ""){
    bindDesc([{cntrlId:"vuDept",dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
  }
//时间
  var parameters = {
      inputId:'vuEnd',
      property:{isHaveTime:true}
      ,bindToBtn:'requestDate'
  };
  new Calendar(parameters);
}

//提交申请
function checkForm2() {
  if (checkForm()) {
    var pars = $('form1').serialize() ;
    var requestURL="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleUsageAct/editVehicle2.act";
    var json=getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      alert("修改成功!");
      window.close();
      parent.opener.location.reload()
    }
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif"><span class="big3"> 车辆使用信息修改</span>
    </td>
  </tr>
</table>
<br>
 <form  method="post" name="form1" id="form1">
 <input type="hidden" name="dtoClass" value="yh.subsys.oa.vehicle.data.YHVehicleUsage">
 <input type="hidden" name="seqId" id="seqId" value="<%=usage.getSeqId()%>">
<table class="TableBlock" align="center" width="620">
    <tr>
      <td nowrap class="TableContent" width="80"> 车 牌 号：</td>
      <td class="TableData" width="230" id="vIdDesc">
      <input type="hidden" name="vId" id="vId" value="<%=usage.getVId()%>"></td>
      <td nowrap class="TableContent" width="80"> 司　　机：</td>
      <td class="TableData" width="230" id="vuDriverDesc">
        <%
      if (YHUtility.isNullorEmpty(usage.getVuDriver())) {
      %>
        <input type="hidden" name="vuDriver" id="vuDriver" value="">
      <%}else { %>
        <input type="hidden" name="vuDriver" id="vuDriver" value="<%=usage.getVuDriver()%>">
      <%} %>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80"> 用 车 人：</td>
      <td class="TableData" id="vuUserDesc">
      <%
      if (YHUtility.isNullorEmpty(usage.getVuUser())) {
      %>
      <input type="hidden" name="vuUser" id="vuUser" value="">
      <%}else { %>
      <input type="hidden" name="vuUser" id="vuUser" value="<%=usage.getVuUser() %>">
      <%} %>
      </td>
      <td nowrap class="TableContent" width="80"> 用车部门：</td>
      <td class="TableData" id="vuDeptDesc">
       <%
      if (YHUtility.isNullorEmpty(usage.getVuDept())) {
      %>
      <input type="hidden" name="vuDept" id="vuDept" value="<%=usage.getVuDept() %>">
      <%}else { %>
       <input type="hidden" name="vuDept" id="vuDept" value="<%=usage.getVuDept() %>">
      <%} %>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80"> 起始时间：</td>
      <td class="TableData"><%=usage.getVuStart().toString().substring(0,19)%>
      <input type="hidden" id="vuStart" name="vuStart" value="<%=usage.getVuStart().toString().substring(0,19)%>"></td>
      <td nowrap class="TableContent" width="80"> 结束时间：</td>
      <td class="TableData">
        <input type="text" name="vuEnd" id="vuEnd" class="BigInput" value="<%=usage.getVuEnd().toString().substring(0,19)%>">
        <img src="<%=imgPath%>/calendar.gif"  id="requestDate" name="requestDate" border="0" align="absMiddle" style="cursor:pointer">
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80"> 目 的 地：</td>
      <td class="TableData">
      <%if(!YHUtility.isNullorEmpty(usage.getVuDestination())) {%>
        <input type="text" name="vuDestination" id="vuDestination" size="20" maxlength="100" class="BigInput" value="<%=usage.getVuDestination().replace("\"","")%>">
      <%}else { %>
      <input type="text" name="vuDestination" id="vuDestination" size="20" maxlength="100" class="BigInput" value="">
      <%} %>
      </td>
      <td nowrap class="TableContent" width="80"> 里　　程：</td>
      <td class="TableData">
        <input type="text" name="vuMileage" id="vuMileage"  size="10" maxlength="10" class="BigInput" value="<%=usage.getVuMileage()%>"> (公里)
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80"> 事　　由：</td>
      <td class="TableData" colspan="3"> <%if(!YHUtility.isNullorEmpty(usage.getVuReason())) {%><%=usage.getVuReason() %><%} %></td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80"> 备　　注：</td>
      <td class="TableData" colspan="3">
        <textarea name="vuRemark" id="vuRemark" class="BigInput" cols="74" rows="5"><%if(!YHUtility.isNullorEmpty(usage.getVuRemark())) {%><%=usage.getVuRemark() %><%} %></textarea>
      </td>
    </tr>
    <tr class="TableControl">
      <td nowrap colspan="4" align="center">           
        <input type="button" value="保存" class="BigButton" onclick="checkForm2();">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
      </td>
    </tr>
    </table>
</form>
<%}else{%>
<table class="MessageBox" align="center" width="340">
<tr>
<td class='msg info'>
<div class='content' style='font-size: 12pt'>未找到相应的记录!</div>
</td>
</tr>
</table>
<div align="center"> <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();parent.window.close()">&nbsp;</div>
<%} %>
</body>
</html>
