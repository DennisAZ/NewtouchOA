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

<META content="Microsoft FrontPage 4.0" name=GENERATOR>

</HEAD>

<BODY class="bodycolor" topmargin="0">



<BR>

<table width="500" border="0" cellspacing="1" cellpadding="3" align="center" class="TableBlock">

  <tr> 

    <td height="27" class="TableHeader">

      <div align="center">企业财产保险投保单</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　投保人：＿＿＿＿＿＿＿＿　　　投保单号：＿＿＿＿＿＿＿＿＿＿<BR> 

　　<BR> 

　　┌───┬────────┬────┬─────┬────┬─────┐<BR> 

　　│　　　│　　投保财产　　│以何种价│保险金额　│费　率　│　保险费　│<BR> 

　　│　　　│　　项　　目　　│值投保　│　（元）　│（‰）　│　（元）　│<BR> 

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

　　├────┬──────────────────┬──────────┤<BR> 

　　│　特别　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　　│<BR> 

　　│　　　　│　　　　　　　　　　　　　　　　　　│占用性质：　　　　　│<BR> 

　　│　约定　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　　│<BR> 

　　├────┴──────────────────┼──────────┤<BR> 

　　│投保人地址：　　　　　　开户银行：　　　　　　│　本投保单未经本公司│<BR> 

　　│电　　　话：　　　　　　银行帐号：　　　　　　│签章不发生法律效力。│<BR> 

　　│联　系　人：　　　　　　财产座落地址：＿＿＿　│　　　　　　　　　　│<BR> 

　　│行　　　业：　　　　　　＿＿＿＿＿＿＿＿＿＿　│　　　　　　　　　　│<BR> 

　　│所　有　制：　　　　　　　　共　　　个地址　　│　　　　　　　　　　│<BR> 

　　├───────────────────────┤　　　　　　　　　　│<BR> 

　　│　　本投保人兹声明上述各　　投保人签章：　　　│中国人民保险公司签章│<BR> 

　　│项均属事实，并同意以本投　　　　　　　　　　　│　　　　　　　　　　│<BR> 

　　│保单作为订立保险合同的依　　　　　　　　　　　│　　　年　　月　　日│<BR> 

　　│据。　　　　　　　　　　　　　　年　　月　　日│　　　　　　　　　　│<BR> 

　　└───────────────────────┴──────────┘<BR> 

　　本保险也适用于国家机关、事业单位、人民团体投保。<BR> 

　　经（副经）理；　　　　　　　经办人：<BR> 

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
