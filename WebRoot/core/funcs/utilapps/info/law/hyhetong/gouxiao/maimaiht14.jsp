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

      <div align="center">供应合同</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> 合同编号_________________</p>

      <p> 　　<BR> 

　　签订日期_________________<BR> 

　　签证编号_________________<BR> 

　　供 应 合 同<BR> 

　　<BR> 

　　需方<BR> 

　　────────<BR> 

　　供方<BR> 

　　────────<BR> 

　　经双方协商，签订本合同，共同信守。<BR> 

　　<BR> 

　　┌───┬────┬──┬──┬──┬──┬────┬─────┐<BR> 

　　│品　名│规格型号│单位│数量│单价│金额│交货日期│超欠幅度％│<BR> 

　　├───┼────┼──┼──┼──┼──┼────┼─────┤<BR> 

　　│　　　│　　　　│　　│　　│　　│　　│　　　　│　　　　　│<BR> 

　　├───┼────┼──┼──┼──┼──┼────┼─────┤<BR> 

　　│　　　│　　　　│　　│　　│　　│　　│　　　　│　　　　　│<BR> 

　　├───┼────┼──┼──┼──┼──┼────┼─────┤<BR> 

　　│　　　│　　　　│　　│　　│　　│　　│　　　　│　　　　　│<BR> 

　　├───┼────┼──┼──┼──┼──┼────┼─────┤<BR> 

　　│　　　│　　　　│　　│　　│　　│　　│　　　　│　　　　　│<BR> 

　　├───┴────┴──┴──┴──┴──┴────┴─────┤<BR> 

　　│货款共计人民币(大写)　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　└───────────────────────────────┘<BR> 

　　1，质量标准：_________________________________________________________<BR> 

　　2，交货(运输)办法及地点：_____________________________________________<BR> 

　　3，包装要求及费用负担：_______________________________________________<BR> 

　　4，结算方式及限期：___________________________________________________<BR> 

　　5，其它：_____________________________________________________________<BR> 

　　(1)未按合同规定的时间、数量交货，少交而需方仍需要的，应按数补交，不需要的，可以退货，并<BR> 

　　承担每天误期货款的千分之三违约金。<BR> 

　　(2)品种，规格，质量，包装不符合同规定，需方同意收货的，根据供方所经营的品种，任意挑选，<BR> 

　　按质论价。需方不同意收货，允许退货。<BR> 

　　(3)其它：________________________________________________________________需方经济责任：<BR> 

　　(1)对无故变更品名，数量，不履行合同，给供方造成损失，应给付供方实际损失的千分之一赔偿<BR> 

　　金。<BR> 

　　(2)对无故不按合同规定时间取货，事前未声明，每延期一天，应承担每天迟取货款的千分之一违约<BR> 

　　金。<BR> 

　　(3)其它：________________________________________________________________供需双方由于人<BR> 

　　力不可抗拒的灾害和确非企业本身造成的原因，而不能履行合同时，经双方协商同意，由审核机关查实<BR> 

　　证明，经政府批准，可以减轻或免于经济责任。<BR> 

　　其它未尽事项，由双方协商，可另订附件。<BR> 

　　本合同一式　份，自工商行政管理机关审核之日起生效，有效期到　年　月　日<BR> 

　　┌─────────────────┬────────────────┐<BR> 

　　│　　供方(盖章)　　　　　　　　　　│　　　需方(盖章)　　　　　　　　│<BR> 

　　│　　　　　　　　　　　　　　　　　│　　　　　　　　　　　　　　　　│<BR> 

　　│　　　　　　　　　　　　　　　　　│　　　　　　　　　　　　　　　　│<BR> 

　　│　　　　　　　　　年　月　日　　　│　　　　　　　　　　年　月　日　│<BR> 

　　├─────────────────┼────────────────┤<BR> 

　　│　　签证机关(盖章)　　　　　　　　│　　　　　　　　　　　　　　　　│<BR> 

　　│　　　　　　　　　　　　　　　　　│　　　　　　　　　　　　　　　　│<BR> 

　　│　　　　　　　　　　　　　　　　　│　　　　　　　　　　　　　　　　│<BR> 

　　│　　　　　　　　　　　　　　　　　│　　　　　　　　　　　年　月　日│<BR> 

　　└─────────────────┴────────────────┘<BR> 

　　<BR> 

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
