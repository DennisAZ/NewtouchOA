<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
int num = Integer.parseInt(request.getParameter("num").toString());
int sizeSun = Integer.parseInt(request.getParameter("numOne").toString());
int numOne = Integer.parseInt(request.getParameter("numOne").toString()) - num;
%>
<html>
<head>
<title>导入固定资产资料</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script> 
function blockNone() {
  if (document.getElementById("DivNone").style.display == 'none') {
    document.getElementById("DivNone").style.display = 'block';
  }else {
    document.getElementById("DivNone").style.display = 'none';
  }
}
</script>
</head>
 <body class="bodycolor" topmargin="5">
  <table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>
    <%if(sizeSun <= 0) {%>
    导入数据格式不正确,或没有数据!
    <%} else {%>
   总共<%=sizeSun%>条数据,导入<%=num%>条数据成功,导入<%=numOne%>条数据失败!
   <%} %>
  </div>
    </td>
  </tr>
</table>
 <div align="center">
 <input value="返回" type="button" class="BigButton" title="返回" name="button"  onclick="javascript:history.back();">
 <a href="javascript:blockNone();" title="格式模板">CSV格式模板</a>
 </div>
 <br>
<div align="center" id="DivNone" style="display:none">
<table>
<tr><td align="left" class="NoneBlock">资产编号,资产名称,资产值,资产类型,单据日期</td></tr>
<tr><td align="left" class="NoneBlock">0791020002008000001,其他电子计算机及其外围设备,0.0,电子产品及通信设备,2010-02-05</td></tr>
<tr><td align="left" class="NoneBlock">0791020002008000002,其他电子计算机,1.4,电子产品及通信设备,2010-04-15</td></tr>
</table>
</div>
 </body>

