<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.vehicle.data.YHVehicleUsage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
YHVehicleUsage usage = (YHVehicleUsage)request.getAttribute("usage");
%>
<html>
<head>
<title>车辆使用详细信息</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<%if (usage != null) {%>
<script type="text/javascript">
function doInit() {
  if(document.getElementById("vuDriver").value.trim() != ""){
    bindDesc([{cntrlId:"vuDriver",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("vId").value.trim() != ""){
    bindDesc([{cntrlId:"vId",dsDef:"VEHICLE,SEQ_ID,V_NUM"}]);
  }
  if(document.getElementById("vuProposer").value.trim() != ""){
    bindDesc([{cntrlId:"vuProposer",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("vuUser").value.trim() != ""){
    bindDesc([{cntrlId:"vuUser",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("vuDept").value.trim() != ""){
    bindDesc([{cntrlId:"vuDept",dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
  }
  if(document.getElementById("vuOperator").value.trim() != ""){
    bindDesc([{cntrlId:"vuOperator",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("deptManager").value.trim() != ""){
    bindDesc([{cntrlId:"deptManager",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()"> 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/vehicle.gif"><span class="big3">车辆使用详细信息</span><br>
    </td>
  </tr>
</table>
 
<table class="TableBlock" width="100%">
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">车牌号：</td>
      <td nowrap align="left" class="TableData" id="vIdDesc">
      <input type="hidden" id="vId" name="vId" value="<%=usage.getVId()%>">
      </td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">司机：</td>
      <td nowrap align="left" class="TableData" id="vuDriverDesc">
      <input type="hidden" id="vuDriver" name="vuDriver" value="<%=usage.getVuDriver()%>">
      </td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="80" class="TableContent">申请人：</td>
      <td nowrap align="left" class="TableData" id="vuProposerDesc">
      <input type="hidden" id="vuProposer" name="vuProposer" value="<%=usage.getVuProposer()%>">
      </td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">申请时间：</td>
      <td nowrap align="left" class="TableData"><%=usage.getVuRequestDate().toString().substring(0,19)%></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="80" class="TableContent">用车人：</td>
      <td nowrap align="left" class="TableData" id="vuUserDesc">
      <input type="hidden" id="vuUser" name="vuUser" value="<%=usage.getVuUser()%>">
      </td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">用车部门：</td>
      <td nowrap align="left" class="TableData" id="vuDeptDesc">
      <input type="hidden" id="vuDept" name="vuDept" value="<%=usage.getVuDept()%>"></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="80" class="TableContent">事由：</td>
      <td align="left" class="TableData"><%if(usage.getVuReason() != null) {%><%=usage.getVuReason()%><%} %></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">开始时间：</td>
      <td nowrap align="left" class="TableData"><%=usage.getVuStart().toString().substring(0,19)%></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="80" class="TableContent">结束时间：</td>
      <td nowrap align="left" class="TableData"><%=usage.getVuEnd().toString().substring(0,19)%></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="80" class="TableContent">目的地：</td>
      <td nowrap align="left" class="TableData"><%if(usage.getVuDestination() != null) {%><%=usage.getVuDestination()%><%} %></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">里程：</td>
      <td nowrap align="left" class="TableData"><%=usage.getVuMileage()%></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="80" class="TableContent">调度员：</td>
      <td nowrap align="left" class="TableData" id="vuOperatorDesc">
      <input type="hidden" id="vuOperator" name="vuOperator" value="<%=usage.getVuOperator()%>">
      </td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="80" class="TableContent">部门审批人：</td>
      <td nowrap align="left" class="TableData" id="deptManagerDesc">
<input type="hidden" id="deptManager" name="deptManager" value="<%=usage.getDeptManager()%>">
</td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="80" class="TableContent">当前状态：</td>
      <td nowrap align="left" class="TableData">
     <%if (usage.getVuStatus().equals("0") && !usage.getDmerStatus().equals("3")) {%>
     待批
     <%} %>
     <%if (usage.getVuStatus().equals("1")) {%>
     已准
     <%} %>
     <%if (usage.getVuStatus().equals("2")) {%>
    使用中
     <%} %>
     <%if (usage.getVuStatus().equals("3") || usage.getDmerStatus().equals("3")) {%>
     未准
     <%} %>
     <%if (usage.getVuStatus().equals("4")) {%>
     结束
     <%} %>
     
      </td>
  </tr>
  <%
  if (!YHUtility.isNullorEmpty(usage.getDeptReason())) {
  %>
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">部门未准理由</td>
      <td align="left" class="TableData"><%=usage.getDeptReason()%></td>
  </tr>
  <%}
  if (!YHUtility.isNullorEmpty(usage.getOperatorReason())) {
  %>
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">调度员未准理由</td>
      <td align="left" class="TableData"><%=usage.getOperatorReason() %></td>
  </tr>
  <%}%>
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">备注：</td>
      <td align="left" class="TableData"><%if(usage.getVuRemark() != null) {%><%=usage.getVuRemark()%><%} %></td>
  </tr>
  <tr align="center" class="TableControl">
      <td colspan="2">
        <input type="button" value="打印" class="BigButton" onclick="document.execCommand('Print');" title="直接打印表格页面">&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
      </td>
  </tr>
</table>
<%} else {%>
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
