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

      <div align="center">承揽合同（半成品）</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　合同编号：＿＿＿＿＿＿＿＿<BR> 

　　定作方：＿＿＿＿＿＿＿＿＿（甲方）<BR> 

　　承揽方：＿＿＿＿＿＿＿＿<BR> 

　　（乙方）<BR> 

　　签订日期：＿＿＿＿年＿＿月＿＿日<BR> 

　　签订地点：＿＿＿＿＿＿＿＿＿＿<BR> 

　　<BR> 

　　经充分协商，签订本合同，共同信守。<BR> 

　　１．品名（或项目）、数量及价款：<BR> 

　　┌──────┬─────┬────┬──┬─────┬───────┐<BR> 

　　│加工定作物品│型号、规格│计量单位│数量│价款或酬金│交（提）定作物│<BR> 

　　│名称或项目　│　　　　　│　　　　│　　├──┬──┤或完成工作日期│<BR> 

　　│　　　　　　│　　　　　│　　　　│　　│单价│金额│　　　　　　　│<BR> 

　　├──────┼─────┼────┼──┼──┼──┼───────┤<BR> 

　　│　　　　　　│　　　　　│　　　　│　　│　　│　　│　　　　　　　│<BR> 

　　├──────┼─────┼────┼──┼──┼──┼───────┤<BR> 

　　│　　　　　　│　　　　　│　　　　│　　│　　│　　│　　　　　　　│<BR> 

　　├──────┼─────┼────┼──┼──┼──┼───────┤<BR> 

　　│　　　　　　│　　　　　│　　　　│　　│　　│　　│　　　　　　　│<BR> 

　　├──────┴─────┴────┴──┴──┴──┴───────┤<BR> 

　　│合计金额（人民币：　）佰　拾　万　仟　佰　拾　元　角　分　　　　　　│<BR> 

　　└──────────────────────────────────┘<BR> 

　　２．使用原材料（半制品）及主要辅料规定：<BR> 

　　（１）甲方向乙方提供部分：<BR> 

　　┌──┬─────┬────┬──┬──┬──┬─────┬─────┐<BR> 

　　│品名│型号、规格│计量单位│数量│单价│金额│提供日期　│消耗定额　│<BR> 

　　├──┼─────┼────┼──┼──┼──┼─────┼─────┤<BR> 

　　│　　│　　　　　│　　　　│　　│　　│　　│　　　　　│　　　　　│<BR> 

　　├──┼─────┼────┼──┼──┼──┼─────┼─────┤<BR> 

　　│　　│　　　　　│　　　　│　　│　　│　　│　　　　　│　　　　　│<BR> 

　　├──┼─────┼────┼──┼──┼──┼─────┼─────┤<BR> 

　　│　　│　　　　　│　　　　│　　│　　│　　│　　　　　│　　　　　│<BR> 

　　├──┼─────┼────┼──┼──┼──┼─────┼─────┤<BR> 

　　│　　│　　　　　│　　　　│　　│　　│　　│　　　　　│　　　　　│<BR> 

　　└──┴─────┴────┴──┴──┴──┴─────┴─────┘<BR> 

　　质量要求：<BR> 

　　检验及提供方法：＿＿＿＿＿＿＿＿＿<BR> 

　　价款处理方法：＿＿＿＿＿＿＿＿＿＿<BR> 

　　（２）乙方提供部分：<BR> 

　　┌─────┬─────┬────┬─────────────────┐<BR> 

　　│品名及产地│型号、规格│质量要求│允许使用代用品名称及规定　　　　　│<BR> 

　　├─────┼─────┼────┼─────────────────┤<BR> 

　　│　　　　　│　　　　　│　　　　│　　　　　　　　　　　　　　　　　│<BR> 

　　├─────┼─────┼────┼─────────────────┤<BR> 

　　│　　　　　│　　　　　│　　　　│　　　　　　　　　　　　　　　　　│<BR> 

　　├─────┼─────┼────┼─────────────────┤<BR> 

　　│　　　　　│　　　　　│　　　　│　　　　　　　　　　　　　　　　　│<BR> 

　　└─────┴─────┴────┴─────────────────┘<BR> 

　　３．加工方法及质量、技术标准和负责期限：＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　４．包装要求及费用负担：＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　５．交（提）定作物办法（包括运输方式和费用负担）、地点：＿＿＿＿＿<BR> 

　　６．验收标准、方法和期限：＿＿＿＿＿＿＿＿<BR> 

　　７．给付定金数额、时间、方法：＿＿＿＿＿＿＿＿<BR> 

　　８．结算方式和期限：＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　９．违约责任：＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　１０．其他：＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　未尽事宜由双方另行协商。<BR> 

　　┌────────────┬─────────┬───────────┐<BR> 

　　│　　　　　　　　　　　　│　　定作方　　　　│　　　　承揽方　　　　│<BR> 

　　├────────────┼─────────┼───────────┤<BR> 

　　│　　　单位名称　　　　　│　　　　　（盖章）│　　　　　　　（盖章）│<BR> 

　　├────────────┼─────────┼───────────┤<BR> 

　　│　　法定代表人　　　　　│　　　　　　　　　│　　　　　　　　　　　│<BR> 

　　├────────────┼─────────┼───────────┤<BR> 

　　│　　签约代表　　　　　　│　　　　　　　　　│　　　　　　　　　　　│<BR> 

　　├────────────┼─────────┼───────────┤<BR> 

　　│　　　　地址　　　　　　│　　　　　　　　　│　　　　　　　　　　　│<BR> 

　　├────────────┼─────────┼───────────┤<BR> 

　　│　　　　电话　　　　　　│　　　　　　　　　│　　　　　　　　　　　│<BR> 

　　├────────────┼─────────┼───────────┤<BR> 

　　│　　　　电挂　　　　　　│　　　　　　　　　│　　　　　　　　　　　│<BR> 

　　├────────────┼─────────┼───────────┤<BR> 

　　│　　　开户银行及帐号　　│　　　　　　　　　│　　　　　　　　　　　│<BR> 

　　├────────────┴─────────┴───────────┤<BR> 

　　│保证单位　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　│　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　│　　　　　　　　　　　　　　　　　（盖章）　　　　　　　　（盖章）　│<BR> 

　　└──────────────────────────────────┘<BR> 

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
