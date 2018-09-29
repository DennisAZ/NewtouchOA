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

      <div align="center">货物运单</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　┌─────────┐<BR> 

　　货物指定于　月　日搬入　　　　××铁路局　　　　　│承运人／托运人装车│<BR> 

　　货位：　　　　　　　　　　　　　货物运单　　　　　├─────────┤<BR> 

　　计划号码或运输号码：　　托运人→发站→到站→收货人│承运人／托运人施封│<BR> 

　　└─────────┘<BR> 

　　运到期限　　日　　　　　　　　　　　　　　　　　　　　货票第　　　号<BR> 

　　┌─────────────────┬────────────────┐<BR> 

　　│　　　　　　托运人填写　　　　　　│　　　　　承运人填写　　　　　　│<BR> 

　　├──┬──┬─────┬─────┼────┬─────┬──┬──┤<BR> 

　　│发站│　　│到站(局)　│　　　　　│车种车号│　　　　　│货车│　　│<BR> 

　　│　　│　　│　　　　　│　　　　　│　　　　│　　　　　│标重│　　│<BR> 

　　├──┴──┴────┬┴─────┼────┼─────┴──┴──┤<BR> 

　　│到站所属省(市)自治区│　　　　　　│施封号码│　　　　　　　　　　　│<BR> 

　　├──┬──┬────┴──────┼────┼─────┬─────┤<BR> 

　　│　　│名称│　　　　　　　　　　　│经　　由│铁路货车篷│　　　　　│<BR> 

　　│托运│　　│　　　　　　　　　　　│　　　　│布号码　　│　　　　　│<BR> 

　　│人　├──┼─────┬──┬──┼────┼───┬─┴─────┤<BR> 

　　│　　│住址│　　　　　│电话│　　│　　　　│　　　│　　　　　　　│<BR> 

　　├──┼──┼─────┴──┴──┼────┤集装箱│　　　　　　　│<BR> 

　　│收货│名称│　　　　　　　　　　　│运价里程│号　码│　　　　　　　│<BR> 

　　│人　├──┼─────┬──┬──┼────┤　　　│　　　　　　　│<BR> 

　　│　　│住址│　　　　　│电话│　　│　　　　│　　　│　　　　　　　│<BR> 

　　├──┼──┼──┬──┼──┴──┼────┼───┼──┬──┬─┤<BR> 

　　│货物│件数│包装│货物│托运人确定│承运人确│计费　│运　│运　│运│<BR> 

　　│名称│　　│　　│价格│重量(公斤)│定重量( │重量　│价　│价　│　│<BR> 

　　│　　│　　│　　│　　│　　　　　│公斤)　 │　　　│号　│率　│费│<BR> 

　　├──┼──┼──┼──┼─────┼────┼───┼──┼──┼─┤<BR> 

　　│　　│　　│　　│　　│　　　　　│　　　　│　　　│　　│　　│　│<BR> 

　　├──┼──┼──┼──┼─────┼────┼───┼──┼──┼─┤<BR> 

　　│　　│　　│　　│　　│　　　　　│　　　　│　　　│　　│　　│　│<BR> 

　　├──┼──┼──┼──┼─────┼────┼───┼──┼──┼─┤<BR> 

　　│　　│　　│　　│　　│　　　　　│　　　　│　　　│　　│　　│　│<BR> 

　　├──┼──┼──┼──┼─────┼────┼───┼──┼──┼─┤<BR> 

　　│　　│　　│　　│　　│　　　　　│　　　　│　　　│　　│　　│　│<BR> 

　　├──┼──┼──┼──┼─────┼────┼───┼──┼──┼─┤<BR> 

　　│合计│　　│　　│　　│　　　　　│　　　　│　　　│　　│　　│　│<BR> 

　　├──┼──┴──┴──┴─────┼────┼───┴──┴──┴─┤<BR> 

　　│托运│保险：　　　　　　　　　　　│　　　　│　　　　　　　　　　　│<BR> 

　　│人记│　　　　　　　　　　　　　　│承运人　│　　　　　　　　　　　│<BR> 

　　│载事│　　　　　　　　　　　　　　│记载事项│　　　　　　　　　　　│<BR> 

　　│项　│　　　　　　　　　　　　　　│　　　　│　　　　　　　　　　　│<BR> 

　　└──┴──────────┬───┴────┴──┬───────┬┘<BR> 

　　注：本单不作为收款凭证，托│　　　　　　　　　到日│　　　　　发日│<BR> 

　　运人签约须知见背面。　│托运人盖章或签字　站期│　　　　　站期│<BR> 

　　规格：350×185mm　　　│　　年　月　日　　交戳│　　　　　承戳│<BR> 

　　│　　　　　　　　　付　│　　　　　运　│<BR> 

　　<BR> 

　　│　　　领货凭证<BR> 

　　│<BR> 

　　│车种及车号<BR> 

　　│<BR> 

　　│货票第　　号<BR> 

　　│<BR> 

　　│运到期限　　日<BR> 

　　│┌────┬─────┐<BR> 

　　││发　站　│　　　　　│<BR> 

　　│├────┼─────┤<BR> 

　　││到　站　│　　　　　│<BR> 

　　│├────┼─────┤<BR> 

　　││托运人　│　　　　　│<BR> 

　　│├────┼─────┤<BR> 

　　││收货人　│　　　　　│<BR> 

　　│├────┼──┬──┤<BR> 

　　││货物名称│件数│重量│<BR> 

　　│├────┼──┼──┤<BR> 

　　││　　　　│　　│　　│<BR> 

　　│├────┼──┼──┤<BR> 

　　││　　　　│　　│　　│<BR> 

　　│├────┼──┼──┤<BR> 

　　││　　　　│　　│　　│<BR> 

　　│├────┴──┴──┤<BR> 

　　││找运人盖章或签字　　│<BR> 

　　││　　　　　　　　　　│<BR> 

　　│├──────────┤<BR> 

　　││发站承运日期戳　　　│<BR> 

　　││　　　　　　　　　　│<BR> 

　　│└──────────┘<BR> 

　　│　　注：收货人领<BR> 

　　│　　　　货须知见<BR> 

　　│　　　　背面<BR> 

　　│<BR> 

　　<BR> 

　　领货凭证（背面）　　　　　　　　　货物运单（背面）<BR> 

　　┌─────────────────┬────────────────┐<BR> 

　　│　　　　　收货人领货须知　　　　　│　　　　　托运人须知　　　　　　│<BR> 

　　│　　　　　　　　　　　　　　　　　│　　　　　　　　　　　　　　　　│<BR> 

　　│　１．收货人接到托运人寄交的领货凭│　１．托运人持本货物运单向铁路托│<BR> 

　　│证后，应及时向到站联系领取货物。　│运货物，证明并确认和愿意遵守铁路│<BR> 

　　│　２．收货人领取货物已超过免费暂存│货物运输的有关规定。　　　　　　│<BR> 

　　│期限时，应按规定支付货物暂存费。　│　２．货物运单所记载的货物名称、│<BR> 

　　│　３．收货人在到站领取货物，如遇货│重量与货物的实际完全相符，托运人│<BR> 

　　│物未到时，应要求到站在本证背面加盖│对其真实性负责。　　　　　　　　│<BR> 

　　│车站戳证明货物未到。　　　　　　　│　３．货物的内容、品质和价值是托│<BR> 

　　│　　　　　　　　　　　　　　　　　│运人提供的，承运人在接收和承运货│<BR> 

　　│　　　　　　　　　　　　　　　　　│物时并未全部核对。　　　　　　　│<BR> 

　　│　　　　　　　　　　　　　　　　　│　４．托运人应及时将领货凭证寄交│<BR> 

　　│　　　　　　　　　　　　　　　　　│收货人，凭以联系到站领取货物。　│<BR> 

　　└─────────────────┴────────────────┘<BR> 

　　（注：本须知排印时，　　　　　　　　（注：本须知排印时，<BR> 

　　应放在凭证背面下端）　　　　　　　　应放在运单背面右下测）<BR> 

　　<BR> 

　　<BR> 

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
