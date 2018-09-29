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

      <div align="center">综架承揽合同</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　委托方：＿＿＿＿＿纺织器材厂（甲方）<BR> 

　　承揽方：＿＿＿＿＿电镀厂（乙方）<BR> 

　　１．加工的项目、产品名称、规格＿＿＿＿数量＿＿＿＿价格、交货期：<BR> 

　　┌──┬──────┬───┬────┬────┬────┬─────┐<BR> 

　　│项目│产品名称、规│单位　│数量　　│单价　　│总价　　│交货日期　│<BR> 

　　│　　│格　　　　　│　　　│　　　　│（元）　│（元）×├──┬──┤<BR> 

　　│　　│　　　　　　│　　　│　　　　│　　　　│　　　　│一季│二季│<BR> 

　　│　　│　　　　　　│　　　│　　　　│　　　　│　　　　│度　│度　│<BR> 

　　├──┼──────┼───┼────┼────┼────┼──┼──┤<BR> 

　　│镀锌│综架1600mm以│只　　│10万　　│0.15　　│15000万 │5万 │5万 │<BR> 

　　│　　│上　　　　　│　　　│　　　　│　　　　│　　　　│　　│　　│<BR> 

　　├──┼──────┼───┼────┼────┼────┼──┼──┤<BR> 

　　│淬火│综条　　　　│支　　│100万　 │0.11　　│110000　│50万│50万│<BR> 

　　├──┼──────┼───┼────┼────┼────┼──┼──┤<BR> 

　　│　　│　　　　　　│　　　│　　　　│　　　　│　　　　│　　│　　│<BR> 

　　├──┼──────┴───┴────┴────┴────┴──┴──┤<BR> 

　　│合计│（大写）拾贰万伍千元　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　└──┴───────────────────────────────┘<BR> 

　　２．质量标准：乙方根据甲方提供图纸规定的技术要求和质量标准进行加工。在加工工艺过程及运<BR> 

　　输中甲方提供的加工件允许损坏千分之＿＿＿，但损坏件必须归还甲方。<BR> 

　　３．原料供应：加工用锌块由甲方供应。<BR> 

　　４．交货方式：由乙方负责往返运输，运费由乙方自负。<BR> 

　　５．质量验收方法：完成加工后由甲方按质量标准验收。<BR> 

　　６．结算方式：通过银行托收。<BR> 

　　７．违约责任：甲方委托乙方加工镀锌、淬火等，乙方应按规定日期完成；逾期由乙方负担甲方的<BR> 

　　经济损失，每天按加工产品总值千分之＿＿＿交纳罚金。甲方未按规定的时间和数量交付乙方加工物<BR> 

　　品，因而造成乙方停工损失，应按实际损失赔偿。<BR> 

　　<BR> 

　　甲方：＿＿＿＿＿＿纺织器材厂　　　　　乙方：＿＿＿＿电镀厂<BR> 

　　法定代表人：（盖章）　　　　　　　　　法定代表人：（盖章）<BR> 

　　地址：＿＿＿＿＿＿＿　　　　　　　　　地址：＿＿＿＿＿＿＿<BR> 

　　银行帐号：＿＿＿＿＿＿　　　　　　　　银行帐号：＿＿＿＿＿<BR> 

　　<BR> 

　　＿＿＿＿年＿＿月＿＿日<BR> 

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
