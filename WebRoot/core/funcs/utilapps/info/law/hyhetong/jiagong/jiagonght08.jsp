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

      <div align="center">承揽合同（简）</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　签订合同单位：＿＿＿＿＿＿（以下简称甲乙方）签订条款如下：<BR> 

　　１．产品名称、型号及规格、数量、价格及交货日期等如下表规定：<BR> 

　　┌───┬───┬─┬─┬──┬────────┬──────────┐<BR> 

　　│产品名│型号规│单│数│单价│　总价金额　　　│　交货日期及数量　　│<BR> 

　　│称　　│模　　│位│量│（元├────────┼─┬─┬─┬──┬─┤<BR> 

　　│　　　│　　　│　│　│）　│十万千百十元角分│　│　│　│　　│　│<BR> 

　　├───┼───┼─┼─┼──┼────────┼─┼─┼─┼──┼─┤<BR> 

　　│　　　│　　　│　│　│　　│　　　　　　　　│　│　│　│　　│　│<BR> 

　　├───┼───┼─┼─┼──┼────────┼─┼─┼─┼──┼─┤<BR> 

　　│　　　│　　　│　│　│　　│　　　　　　　　│　│　│　│　　│　│<BR> 

　　├───┼───┼─┼─┼──┼────────┼─┼─┼─┼──┼─┤<BR> 

　　│　　　│　　　│　│　│　　│　　　　　　　　│　│　│　│　　│　│<BR> 

　　├───┼───┴─┴─┴──┴────────┴─┴─┴─┴──┴─┤<BR> 

　　│合计　│人民币（大写）　　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　└───┴──────────────────────────────┘<BR> 

　　２．交货办法：<BR> 

　　（１）交货地点：＿＿＿＿＿＿＿＿＿<BR> 

　　（２）运输：由甲代办，但一切运杂费用等由乙负责，甲交运日期即为交货日期。<BR> 

　　３．付款办法：货物交货后，供方随同提单向乙托收或将单据寄交乙方，乙方根据供方单据电汇供方。<BR> 

　　４．验收办法：乙应派员到甲仓库验收，如不能派员验收，甲方即按合同规定产品规格数量代为检验。<BR> 

　　５．附则：<BR> 

　　（１）合同如有修改须经双方同意，双方另行签订合同。<BR> 

　　（２）在执行合同期间，如遇市场情况变化或其他原因乙方须在＿＿＿＿天前通知撤销加工合同，否则按违约处以＿＿＿＿＿＿罚金赔偿对方损失。<BR> 

　　（３）本合同须经双方签章并鉴证后生效。<BR> 

　　（４）本合同正本两份，双方各执一份。<BR> 

　　（５）货物运达站＿＿＿＿＿＿＿＿<BR> 

　　（６）收货人＿＿＿＿＿＿＿＿＿<BR> 

　　（７）合同纠纷的处理＿＿＿＿＿＿＿＿＿＿<BR> 

　　<BR> 

　　甲及代表人：（签字盖章）　　　　　　　乙及代表人：（签字盖章）<BR> 

　　开户银行：＿＿＿＿＿＿＿＿　　　　　　开户银行：＿＿＿＿＿＿＿<BR> 

　　帐号：＿＿＿＿＿＿＿＿＿＿　　　　　　帐号：＿＿＿＿＿＿＿＿＿<BR> 

　　电话：＿＿＿＿＿＿＿＿＿＿　　　　　　电话：＿＿＿＿＿＿＿＿＿<BR> 

　　<BR> 

　　签订日期：＿＿＿＿年＿＿月＿＿日<BR> 

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
