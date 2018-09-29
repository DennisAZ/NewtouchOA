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

      <div align="center">汽车货物运单</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　汽　　车　　货　　物　　运　　单<BR> 

　　××省汽车货物运单<BR> 

　　──────────<BR> 

　　<BR> 

　　托运人（单位）：　　经办人：　　电话：　　　地址：　　　运单编号：<BR> 

　　┌───┬─┬──┬───┬──┬─┬──────┬──┬───┬─┐<BR> 

　　│发货人│　│地址│　　　│电话│　│　装货地点　│　　│厂休日│　│<BR> 

　　├───┼─┼──┼───┼──┼─┼──────┼──┼───┼─┤<BR> 

　　│收货人│　│地址│　　　│电话│　│　卸货地点　│　　│厂休日│　│<BR> 

　　├───┼─┼──┼───┼──┼─┼───┬─┬┴──┼─┬─┴┬┤<BR> 

　　│付款人│　│地址│　　　│电话│　│约定起│月│约定到│月│需要││<BR> 

　　│　　　│　│　　│　　　│　　│　│运时间│日│达时间│日│车种││<BR> 

　　├──┬┴┬┴┬─┴───┼──┼─┼──┬┴─┼───┴┬┴─┬┴┤<BR> 

　　│货物│包│　│　体　积　│件重│重│保险│货物│　　　　│　　│　│<BR> 

　　│名称│装│件│长×宽×高│（千│量│保价│　　│计费项目│计费│单│<BR> 

　　│及　│形│数│（厘米）　│克）│　│价格│等级│　　　　│重量│价│<BR> 

　　│规格│式│　│　　　　　│　　│吨│　　│　　│　　　　│　　│　│<BR> 

　　├──┼─┼─┼─────┼──┼─┼──┼──┼────┼──┼─┤<BR> 

　　│　　│　│　│　　　　　│　　│　│　　│　　│运　费　│　　│　│<BR> 

　　│　　│　│　│　　　　　│　　│　│　　│　　├────┼──┼─┤<BR> 

　　├──┼─┼─┼─────┼──┼─┼──┼──┤装卸费　│　　│　│<BR> 

　　│　　│　│　│　　　　　│　　│　│　　│　　├────┼──┼─┤<BR> 

　　│　　│　│　│　　　　　│　　│　│　　│　　│　　　　│　　│　│<BR> 

　　├──┴─┴─┴─────┴──┴─┴──┴──┼────┼──┼─┤<BR> 

　　│　　　　　　　　　　　　　　　　　　　　　　　│　　　　│　　│　│<BR> 

　　├──┬─┬─┬─────┬──┬─┬──┬──┼────┴──┴─┤<BR> 

　　│合　│　│　│　　　　　│　　│　│　　│计费│　　　　　　　　　│<BR> 

　　│计　│　│　│　　　　　│　　│　│　　│里程│　　　　　　　　　│<BR> 

　　├──┴─┼─┴─┬───┴┬─┴┬┴──┴┬─┴┬────┬───┤<BR> 

　　│托　运　│　　　│付款人　│　　│承运人　│　　│承运人　│　　　│<BR> 

　　│记载事项│　　　│银行帐号│　　│记载事项│　　│银行帐号│　　　│<BR> 

　　├─┬──┴───┴────┴──┴────┴──┴┬───┼───┤<BR> 

　　│注│１．托运人请勿填写粗线栏内的项目。　　　　　　│托运人│承运人│<BR> 

　　│意│２．货物名称应填写具体品名，如货物品名过多，　│签章　│签章　│<BR> 

　　│事│　　不能在运单内逐一填写须另附物品清单。　　　│　　　│　　　│<BR> 

　　│项│３．保险或保价货物，在相应价格栏中填写货物声明│年月日│年月日│<BR> 

　　│　│　　价格。　　　　　　　　　　　　　　　　　　│　　　│　　　│<BR> 

　　└─┴───────────────────────┴───┴───┘〔说明〕<BR> 

　　１．填在一张货物运单内的货物必须是属同一托运人。对拼装分卸货物，应将每一拼装或分卸情况在运单记事栏内注明。易腐蚀、易碎货物、易溢漏的液体、危险货物与普通货物以及性质相抵触、运输条件不同的货物，不得用同一张运单托运。托运人、承运人修改运单时，须签字盖章。<BR> 

　　２．本运单一式２份：①受理存根；②托运回执。<BR> 

　　<BR> 

　　<BR> 

　　<BR> 

　　　　 </p>

    </td>

  </tr>

</table>

<br>

<TABLE align=center border=0 cellPadding=0 cel$*pacing=0 width=560>

  <TBODY>

  <TR>

    <TD>

      <DIV align=center>Copyright (C) 2000 ?*ww.lawyers.net.cn 　All right %*served.</DIV>

    </TD></TR>

  <TR bgColor=#000000>

    <TD hight="1"></TD></TR>

  <TR>

    <TD>

      <DIV align=center>版权所有.深圳市协图网络开发有限公司</DIV></TD></TR></TBODY></TABLE>　　 

<br><center>
<input type="button" class="BigButton" value="回上一层" onclick="history.go(-1)">
<input type="button" class="BigButton" value="回主目录" onclick="location='../../index.jsp';">
</center><br>

</html>
