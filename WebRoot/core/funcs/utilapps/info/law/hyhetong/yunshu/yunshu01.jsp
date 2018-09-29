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

      <div align="center">汽车货物托运计划表</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　汽车货物托运计划表<BR> 

　　年　月（季度）汽车货物托运计划表<BR> 

　　─────────────────<BR> 

　　编号：<BR> 

　　┌─────┬────┬────┬──┬───┬─────┬─────┐<BR> 

　　│　货物名称│起运地　│到达地　│件数│重　量│核定意见　│　备　注　│<BR> 

　　│　及规格　│　　　　│　　　　│　　│(公斤)│　　　　　│　　　　　│<BR> 

　　├─────┼────┼────┼──┼───┼─────┼─────┤<BR> 

　　│　　　　　│　　　　│　　　　│　　│　　　│　　　　　│　　　　　│<BR> 

　　├─────┼────┼────┼──┼───┤　　　　　│　　　　　│<BR> 

　　│　　　　　│　　　　│　　　　│　　│　　　│　　　　　│　　　　　│<BR> 

　　├─────┼────┼────┼──┼───┤　　　　　│　　　　　│<BR> 

　　│　　　　　│　　　　│　　　　│　　│　　　│　　　　　│　　　　　│<BR> 

　　├─────┼────┼────┼──┼───┤　　　　　│　　　　　│<BR> 

　　│　　　　　│　　　　│　　　　│　　│　　　│　　　　　│　　　　　│<BR> 

　　├─────┼────┼────┼──┼───┤　　　　　│　　　　　│<BR> 

　　│　　　　　│　　　　│　　　　│　　│　　　│　　　　　│　　　　　│<BR> 

　　├─────┼────┼────┼──┼───┤　　　　　│　　　　　│<BR> 

　　│　　　　　│　　　　│　　　　│　　│　　　│　　　　　│　　　　　│<BR> 

　　├─────┼────┼────┼──┼───┤　　　　　│　　　　　│<BR> 

　　│　　　　　│　　　　│　　　　│　　│　　　│　　　　　│　　　　　│<BR> 

　　├─────┼────┼────┼──┼───┤　　　　　│　　　　　│<BR> 

　　│　　　　　│　　　　│　　　　│　　│　　　│　　　　　│　　　　　│<BR> 

　　├─┬───┴────┴────┴──┴───┼─────┼─────┤<BR> 

　　│　│　　　　　　　　　　　　　　　　　　　　│　托运人　│　承运人　│<BR> 

　　│特│　　　　　　　　　　　　　　　　　　　　│　签章　　│　签章　　│<BR> 

　　│约│　　　　　　　　　　　　　　　　　　　　│　　　　　│　　　　　│<BR> 

　　│事│　　　　　　　　　　　　　　　　　　　　│　　　　　│　　　　　│<BR> 

　　│项│　　　　　　　　　　　　　　　　　　　　│　　　　　│　　　　　│<BR> 

　　│　│　　　　　　　　　　　　　　　　　　　　│　年月日　│　年月日　│<BR> 

　　└─┴────────────────────┴─────┴─────┘<BR> 

　　托运人：　　　　　　　　电话：　　　　　　地址：<BR> 

　　〔说明〕<BR> 

　　应列入本托运计划表的主要货物是：<BR> 

　　（１）重点工程及重点厂矿企业需运输的货物及重点港、站集散的货物；<BR> 

　　（２）大宗货物及一级易燃、易爆、剧毒、放射性物品及长大、笨重物品；<BR> 

　　（３）季节性货物和节日市场供应的主要商品。<BR> 

　　本表一式３份：①承运人存查；②托运人回执；③调度留存。<BR> 

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
