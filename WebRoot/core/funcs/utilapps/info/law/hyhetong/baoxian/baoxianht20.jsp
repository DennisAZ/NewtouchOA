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

      <div align="center">企业财产保险单</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　保险单号＿＿＿＿＿＿＿<BR> 

　　鉴于＿＿＿＿＿＿＿＿＿（以下称被保险人）已向本公司投保企业财产保险以及附加<BR> 

　　＿＿＿＿＿＿＿险，并同意按本保险条款约定交纳保险费，本公司特签发本保险单并同意依照本保险公<BR> 

　　司企业财产保险条款和附加险条款及其特别约定条件，承担被保险人下列财产的保险责任。<BR> 

　　┌───┬────────┬────┬─────┬────┬─────┐<BR> 

　　│　　　│　　承保财产　　│以何种价│保险金额　│费　率　│　保险费　│<BR> 

　　│　　　│　　项　　目　　│值承保　│　（元）　│（‰）　│　（元）　│<BR> 

　　│　　　├────────┼────┼─────┼────┼─────┤<BR> 

　　│　　　│　　　　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　│　基　├────────┼────┼─────┼────┼─────┤<BR> 

　　│　　　│　　　　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　│　　　├────────┼────┼─────┼────┼─────┤<BR> 

　　│　　　│　　　　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　│　　　├────────┼────┼─────┼────┼─────┤<BR> 

　　│　　　│　　　　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　│　本　├────────┼────┼─────┼────┼─────┤<BR> 

　　│　　　│　　　　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　│　　　├────────┼────┼─────┼────┼─────┤<BR> 

　　│　　　│　　　　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　│　　　├──┬─────┼────┼─────┼────┼─────┤<BR> 

　　│　险　│特险│　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　│　　　│　　├─────┼────┼─────┼────┼─────┤<BR> 

　　│　　　│约财│　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　│　　　│　　├─────┼────┼─────┼────┼─────┤<BR> 

　　│　　　│保产│　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　├───┴──┴─────┴────┴─────┴────┴─────┤<BR> 

　　│总保险金额人民币（大写）　　　　　　　　　　　　￥：　　　　　　　　│<BR> 

　　├───┬────────┬────┬─────┬────┬─────┤<BR> 

　　│　　　│　　　　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　│　　　├────────┼────┼─────┼────┼─────┤<BR> 

　　│　附　│　　　　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　│　　　├────────┼────┼─────┼────┼─────┤<BR> 

　　│　加　│　　　　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　│　　　├────────┼────┼─────┼────┼─────┤<BR> 

　　│　险　│　　　　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　│　　　├────────┼────┼─────┼────┼─────┤<BR> 

　　│　　　│　　　　　　　　│　　　　│　　　　　│　　　　│　　　　　│<BR> 

　　├───┴────────┴────┴─────┴────┴─────┤<BR> 

　　│总保险费人民币（大写）　　　　　　　　　　　　　￥：　　　　　　　　│<BR> 

　　├──────────────────────────────────┤<BR> 

　　│保险责任期限自　　年　　月　　日零时起至　　年　　月　　日二十四时止│<BR> 

　　├────┬─────────────────────────────┤<BR> 

　　│　特别　│　　　　　　　　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　│　　　　│　　　　　　　　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　│　约定　│　　　　　　　　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　├────┴──────────────────┬──────────┤<BR> 

　　│被保险人地址：　　　　　　　　　　　　　　　　│　　　　　　　　　　│<BR> 

　　│电　　　　话：　　　　　　　　　　　　　　　　│　　　　　　　　　　│<BR> 

　　│行　　　　业：　　　　　　　　　　　　　　　　│中国人民保险公司签章│<BR> 

　　│所　有　制：　　　　　　　　　　　　　　　　　│　　　　　　　　　　│<BR> 

　　│占用　性质：　　　　　　　　　　　　　　　　　│　　　　　　　　　　│<BR> 

　　│财产座落地址：　　＿＿＿＿＿＿＿＿＿＿＿　　　│　　　年　　月　　日│<BR> 

　　│　　　　　　　　　　　共　　　　　个地址　　　│　　　　　　　　　　│<BR> 

　　└───────────────────────┴──────────┘<BR> 

　　被保险人收到本保险单后请即核对，如有错误立即通知本公司。<BR> 

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
