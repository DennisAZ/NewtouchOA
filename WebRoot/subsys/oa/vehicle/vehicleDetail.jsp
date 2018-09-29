<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>车辆详细信息</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript">
function repairremind(vuId) {
  window.open('<%=contextPath%>/subsys/oa/vehicle/repairremind.jsp?vuId=194','','height=340,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
function orderDetail(vuId) {
  window.open('<%=contextPath%>/subsys/oa/vehicle/orderDetail.jsp?vuId=194','','height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
</script>
</head>
<body class="bodycolor" topmargin="5">
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/vehicle.gif"><span class="big3">车辆详细信息</span><br>
    </td>
  </tr>
</table>
<table class="TableBlock" width="100%">
  <tr class="TableLine2">
      <td nowrap align="left" width="80" class="TableContent">型号：</td>
      <td nowrap align="left" class="TableData">hh-008</td>
      <td class="TableData" width="40%" rowspan="6">
<center>暂无照片</center> 
      </td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">车牌号：</td>
      <td nowrap align="left" class="TableData">88912</td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="80" class="TableContent">司机：</td>
      <td nowrap align="left" class="TableData">uuu</td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">车辆类型：</td>
      <td nowrap align="left" class="TableData">巴士</td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="80" class="TableContent">购置日期：</td>
      <td nowrap align="left" class="TableData">2008-02-12</td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">购买价格：</td>
      <td nowrap align="left" class="TableData">100000</td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="80" class="TableContent">发动机号码：</td>
      <td nowrap align="left" class="TableData" colspan="2">6667</td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">预定情况：</td>
      <td nowrap align="left" class="TableData" colspan="2"><a href="javascript:;" onClick="orderDetail('194')" title="点击查看详情">共1条预定信息</a></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="80" class="TableContent">当前状态：</td>
      <td nowrap align="left" class="TableData" colspan="2"><font color="#00AA00"><b>可用</b></font></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="80" class="TableContent">备注：</td>
      <td align="left" class="TableData" colspan="2">dddd</td>
  </tr>
  <tr align="center" class="TableControl">
      <td colspan="3">
        <input type="button" value="打印" class="BigButton" onclick="document.execCommand('Print');" title="直接打印表格页面">&nbsp;&nbsp;
        <input type="button" value="车辆保养提醒" class="BigButtonC" onClick="javascript:repairremind('194')">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
      </td>
  </tr>
</table>
 
</body>
 
</html>