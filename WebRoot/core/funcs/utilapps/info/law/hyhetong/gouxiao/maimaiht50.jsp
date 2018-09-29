<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>法律法规查询</TITLE>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<style type="text/css">
<!--a            { text-decoration: none; font-size: 9pt; color: black; font-family: 宋体 }

.text        { font-size: 9pt; font-family: 宋体 }

.text1       { color: #0000A0; font-size: 11pt; font-family: 宋体 }

.text2       { color: #008080; font-size: 9pt; font-family: 宋体 }

.text3       { color: #0F8A91; font-size: 11pt; font-family: 宋体 }

.l100        { line-height: 14pt; font-size: 9pt }

td           { font-family: 宋体; font-size: 9pt; line-height: 13pt }

input        { font-size: 9pt; font-family: 宋体 }

p            { font-size: 9pt; font-family: 宋体 }

--></style>



</HEAD>

<BODY class="bodycolor" topmargin="0">

<BR>

<table width="500" border="0" cellspacing="1" cellpadding="3" align="center" class="TableBlock">

  <tr> 

    <td height="27" class="TableHeader">

      <div align="center">针纺织品购销分合同及台帐明细表</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　针纺织品购销分合同<BR> 

　　┌──────┬───┐<BR> 

　　│目录顺序号　│　　　│　　<BR> 

　　└──────┴───┘　　　　　　　　　　　　　　　　　　　　　一式<BR> 

　　需方单位　　　　　　　　　　供方单位　　　　　合同编号：Ｎｏ　　　两份<BR> 

　　┌────┬─────────┬───────────┬─────┐<BR> 

　　│商品货号│品名规格及花色号　│　双方议定遵守条款：　│需方单位　│第第<BR> 

　　├────┼─────────┤１．本购销合同的履行，│　盖章　　│一三<BR> 

　　│　　　　│　　　　　　　　　│应按供需双方签订的购销│　　　　　│联联<BR> 

　　│　　　　├───┬──┬──┤总合同的规定办理。　　│　　　　　│第第<BR> 

　　│　　　　│　　　│　　│　　│２．一方要求变更或解除│法定代表　│二四<BR> 

　　├────┼───┼──┼──┤合同时，应采用书面或电│人或委托　│联联<BR> 

　　│需方要货│　　　│　　│　　│报（必须注明商品名称、│代理人签字│由由<BR> 

　　├────┼───┼──┼──┤合同编号）向对方提出，├─────┤供需<BR> 

　　│供需成交│　　　│　　│　　│对方应在接到通知后十五│供方单位　│货方<BR> 

　　├────┴┬──┴──┴──┤天内答复，逾期不答复，│　盖章　　│单单<BR> 

　　│　　　　　│暂定　与实际执行│即视为同意。　　　　　│　　　　　│位位<BR> 

　　│　供货单位│价　　价相差幅度│３．其它的约定事项：　│　　　　　│留留<BR> 

　　├─────┼──┬─────┤　　　　　　　　　　　│　　　　　│存存<BR> 

　　│　　　　元│　元│　　　±％│　　　　　　　　　　　│法定代表　│<BR> 

　　├────┬┴──┴─────┤　　　　　　　　　　　│人或委托　│<BR> 

　　│交货期限│　年　月至　年　月│　　　　　　　　　　　│代理人签字│<BR> 

　　└────┴─────────┴───────────┴─────┘<BR> 

　　签订日期：　　　　年　　月　　日　　　　　签订地点<BR> 

　　<BR> 

　　<BR> 

　　针纺织品购销分合同台帐明细表<BR> 

　　┌───────────────────┬────┬──────┬─┐<BR> 

　　│　　　　　执　行　记　录　　　　　　　│履行情况│变更手续依据│　│<BR> 

　　├─┬─┬─┬─┬─┬─┬─┬─┬─┬─┼─┬──┼──┬─┬─┤　│<BR> 

　　│日│发│　│　│　│日│发│　│　│　│全│部分│全部│　│　│　│<BR> 

　　│期│货│规│数│金│期│货│规│数│金│部│履行│解除│存│摘│备│<BR> 

　　├─┤单│格│量│额├─┤单│格│量│额│执├──┼──┤何│要│注│<BR> 

　　│月│号│　│　│　│月│号│　│　│　│行│需供│需供│处│和│　│<BR> 

　　├─┤　│　│　│　├─┤　│　│　│　│　│方方│方方│　│号│　│<BR> 

　　│日│　│　│　│　│日│　│　│　│　│　│提提│提提│　│码│　│<BR> 

　　│　│　│　│　│　│　│　│　│　│　│　│出出│出出│　│　│　│<BR> 

　　├─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼──┼──┼─┼─┼─┤<BR> 

　　│　│　│　│　│　│　│　│　│　│　│　│　　│　　│　│　│　│<BR> 

　　├─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼──┼──┼─┼─┼─┤<BR> 

　　│　│　│　│　│　│　│　│　│　│　│　│　　│　　│　│　│　│<BR> 

　　├─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼──┼──┼─┼─┼─┤<BR> 

　　│　│　│　│　│　│　│　│　│　│　│　│　　│　　│　│　│　│<BR> 

　　├─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼──┼──┼─┼─┼─┤<BR> 

　　│　│　│　│　│　│　│　│　│　│　│　│　　│　　│　│　│　│<BR> 

　　├─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼──┼──┼─┼─┼─┤<BR> 

　　│　│　│　│　│　│　│　│　│　│　│　│　　│　　│　│　│　│<BR> 

　　├─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼──┼──┼─┼─┼─┤<BR> 

　　│　│　│　│　│　│　│　│　│　│　│　│　　│　　│　│　│　│<BR> 

　　├─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼──┼──┼─┼─┼─┤<BR> 

　　│　│　│　│　│　│　│　│　│　│　│　│　　│　　│　│　│　│<BR> 

　　├─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼──┼──┼─┼─┼─┤<BR> 

　　│　│　│　│　│　│　│　│　│　│　│　│　　│　　│　│　│　│<BR> 

　　├─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼──┼──┼─┼─┼─┤<BR> 

　　│　│　│　│　│　│　│　│　│　│　│　│　　│　　│　│　│　│<BR> 

　　├─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼──┼──┼─┼─┼─┤<BR> 

　　│　│　│　│　│　│　│　│　│　│　│　│　　│　　│　│　│　│<BR> 

　　├─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼──┼──┼─┼─┼─┤<BR> 

　　│　│　│　│　│　│　│　│　│　│　│　│　　│　　│　│　│　│<BR> 

　　└─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴──┴──┴─┴─┴─┘　　<BR> 

　　<BR> 

　　　　 </p>

    </td>

  </tr>

</table>

<br><center>
<input type="button" class="BigButton" value="回上一层" onclick="history.go(-1)">
<input type="button" class="BigButton" value="回主目录" onclick="location='../../index.jsp';">
</center><br>

</html>
