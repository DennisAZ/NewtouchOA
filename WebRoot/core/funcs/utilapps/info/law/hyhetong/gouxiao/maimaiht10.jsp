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

      <div align="center">工矿产品供应调拨合同</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　供方：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿　　<BR> 

　　需方：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　一、产品名称、规格、牌号、质量、供货时间及数量<BR> 

　　合同编号：<BR> 

　　签订地点：<BR> 

　　签订时间：年　月　日<BR> 

　　┌──┬──┬──┬──┬─┬────────────────────┐<BR> 

　　│　　│　　│　　│　　│　│　　　　交（提）货时间及数量　　　　　　│<BR> 

　　│产品│品种│商标│计量│数├───┬───┬───┬───┬────┤<BR> 

　　│名称│规格│牌号│单位│量│合　计│　　　│　　　│　　　│　　　　│<BR> 

　　├──┼──┼──┼──┼─┼───┼───┼───┼───┼────┤<BR> 

　　│　　│　　│　　│　　│　│　　　│　　　│　　　│　　　│　　　　│<BR> 

　　├──┼──┼──┼──┼─┼───┼───┼───┼───┼────┤<BR> 

　　│　　│　　│　　│　　│　│　　　│　　　│　　　│　　　│　　　　│<BR> 

　　├──┴──┴──┴──┴─┴───┴───┴───┴───┴────┤<BR> 

　　│　　合计人民币金额（大写）　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　└──────────────────────────────────┘<BR> 

　　（注：空格如不够用，可以另接。）<BR> 

　　<BR> 

　　二、本合同仅适用于系统内部工矿产品的供应与调拨<BR> 

　　─────────────────────────────────────────────<BR> 

　　──────────<BR> 

　　三、本合同按《经济合同法》、《工矿产品购销合同条例》及国务院有关部委制订的合同管理条<BR> 

　　例、法执行。<BR> 

　　─────────────────────────────────────────────<BR> 

　　──────────<BR> 

　　<BR> 

　　┌──────────┬───────────┬───────────┐<BR> 

　　│　　供　　　方　　　│　　　需　　　方　　　│　　　　　　　　　　　│<BR> 

　　│单位名称（章）　　　│　单位名称（章）　　　│　鉴（公）证意见：　　│<BR> 

　　│单位地址：　　　　　│　单位地址：　　　　　│　　　　　　　　　　　│<BR> 

　　│法定代表人：　　　　│　法定代表人：　　　　│　　　　　　　　　　　│<BR> 

　　│委托代理人：　　　　│　委托代理人：　　　　│　经　办　人：　　　　│<BR> 

　　│电　　话：　　　　　│　电　　话：　　　　　│　鉴（公）证机关（章）│<BR> 

　　│电报挂号：　　　　　│　电报挂号：　　　　　│　　　年　　月　　日　│<BR> 

　　│开户银行：　　　　　│　开户银行：　　　　　│（注：除国家另有规定　│<BR> 

　　│帐　　号：　　　　　│　帐　　号：　　　　　│　外，鉴（公）证实行　│<BR> 

　　│邮政编码：　　　　　│　邮政编码：　　　　　│　　　自愿原则）　　　│<BR> 

　　└──────────┴───────────┴───────────┘<BR> 

　　有效期限：　　年　　月　　日至　　年　　月　　日<BR> 

　　<BR> 

　　监制部门：　　　　　　　　　　　　　印制单位：<BR> 

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
