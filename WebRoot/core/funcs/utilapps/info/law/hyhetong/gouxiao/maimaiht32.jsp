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

      <div align="center">民用爆破器材购销合同</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　产品名称：　　　　　计量单位：　　　　　签订地点：　　　　　　合同编号：<BR> 

　　生产厂：　　　牌号商标：　　年　月　日　　　调拨通知书编号：　　　字号<BR> 

　　┌───┬──┬───┬───┬─────────────────┬─┐<BR> 

　　│规格　│数量│单价　│总金额│　　　　交(提)货时间　　　　　　　│备│<BR> 

　　│型号　│　　│(元)　│(元)　├──┬──────────────┤注│<BR> 

　　│　　　│　　│　　　│　　　│合计│　　　　　　　　　　　　　　│　│<BR> 

　　├───┼──┼───┼───┼──┼──────────────┼─┤<BR> 

　　│　　　│　　│　　　│　　　│　　│　　　　　　　　　　　　　　│　│<BR> 

　　├───┼──┼───┼───┼──┼──────────────┼─┤<BR> 

　　│　　　│　　│　　　│　　　│　　│　　　　　　　　　　　　　　│　│<BR> 

　　├───┴──┴───┴───┴──┴──────────────┴─┤<BR> 

　　│合计人民币金额(大写)　　　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　├───────────────┬──────────────────┤<BR> 

　　│1.本合同按《经济合同法》、《工│5.验收标准、方法及提出异议期限：　　│<BR> 

　　│矿产品购销合同条例》、《中华人├──────────────────┤<BR> 

　　│民共和国爆炸物品管理条例》及有│6.结算方式及期限：　　　　　　　　　│<BR> 

　　│关民爆器材管理规定执行。　　　├──────────────────┤<BR> 

　　│　　　　　　　　　　　　　　　│7.包装标准、包装物的供应与回收费用负│<BR> 

　　│　　　　　　　　　　　　　　　│　担：　　　　　　　　　　　　　　　│<BR> 

　　├───────────────┼──────────────────┤<BR> 

　　│2.质量要求、质量标准：　　　　│8.违约责任：　　　　　　　　　　　　│<BR> 

　　├───────────────┼──────────────────┤<BR> 

　　│　　　　　　　　　　　　　　　│9.解决合同纠纷的方式：本合同在履行过│<BR> 

　　│3.交(提)货地点、方式：　　　　│程中发生争议，由当事人双方协商解决。│<BR> 

　　│　　　　　　　　　　　　　　　│协商不成，当事人双方同意由＿＿＿＿　│<BR> 

　　│　　　　　　　　　　　　　　　│仲裁委员会仲裁(当事人双方未在本合同 │<BR> 

　　│　　　　　　　　　　　　　　　│中约定仲裁机构，事后又未达成书面仲裁│<BR> 

　　│　　　　　　　　　　　　　　　│协议的，可向人民法院起诉)。　　　　 │<BR> 

　　├───────────────┼──────────────────┤<BR> 

　　│4.运输方式及到达站(港)　　　　│10.其他约定事项：　　　　　　　　　 │<BR> 

　　├─┬────────┬─┬──┴───────┬──────────┤<BR> 

　　│　│单位名称(章)：＿│　│单位名称(章):＿＿＿ │物资主管部门签证意见│<BR> 

　　│供│单位地址：＿＿　│需│单位地址：＿＿＿＿＿│：　　　　　　　　　│<BR> 

　　│　│法定代表人：＿＿│　│法定代表人：＿＿＿＿│　　　签证部门(章)　│<BR> 

　　│　│委托代理人：＿＿│　│委托代理人：＿＿＿＿│　　　(签证不收费)　│<BR> 

　　│　│电话：＿＿＿＿＿│　│电话：＿＿＿＿＿＿＿├──────────┤<BR> 

　　│方│电报挂号：＿＿＿│　│电报挂号：＿＿＿＿＿│鉴(公)证意见：　　　│<BR> 

　　│　│开户银行：＿＿＿│方│开户银行：＿＿＿＿＿│经办人：　　　　　　│<BR> 

　　│　│帐号：＿＿＿＿＿│　│帐号：＿＿＿＿＿＿＿│　鉴(公)证机关(章)　│<BR> 

　　│　│邮政编码：＿＿＿│　│邮政编码：＿＿＿＿＿│　　　年　月　日　　│<BR> 

　　│　│发货单位：＿＿＿│　│收货单位：＿＿＿＿＿│(注：除国家另有规定 │<BR> 

　　│　│　　　　　　　　│　│　　　　　　　　　　│外鉴(公)证实行自愿原│<BR> 

　　│　│　　　　　　　　│　│　　　　　　　　　　│则。)　　　　　　　 │<BR> 

　　└─┴────────┴─┴──────────┴──────────┘<BR> 

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
