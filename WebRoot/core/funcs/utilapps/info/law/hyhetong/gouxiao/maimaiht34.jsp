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

      <div align="center">售货合同</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　合　同　号：＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　日　　　期：＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　签约地点：＿＿＿＿＿＿＿＿＿＿＿＿＿卖方：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　地址：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿　电报挂号：＿＿＿＿＿＿＿＿<BR> 

　　电　　传：＿＿＿＿＿＿＿＿<BR> 

　　买方：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　地址：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿　电报挂号：＿＿＿＿＿＿＿＿<BR> 

　　电　　传：＿＿＿＿＿＿＿＿<BR> 

　　买卖双方同意按下列条款由买方购进，由卖方出售下列商品，订立本合同：<BR> 

　　┌─────────┬────────┬────┬──────┐<BR> 

　　１．│　　品名及规格　　│　　　数量　　　│　单价　│　　总价　　│<BR> 

　　├─────────┼────────┼────┼──────┤<BR> 

　　│　　　　　　　　　│　　　　　　　　│　　　　│　　　　　　│<BR> 

　　└─────────┴────────┴────┴──────┘<BR> 

　　２．合同总值：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　３．包　　装：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　４．保　　险：由卖方按发票金额１１０％投保。<BR> 

　　５．唛　　头：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　６．装运口岸：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　７．目的口岸：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　８．装运期限：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　９．付款条件；买方应通过买卖双方同意的银行，开立以卖方为受益人的、不可撤销的、可转让和分割的信用证。该信用证凭装运单据在＿＿＿＿＿＿银行见单即付。该信用证必须在＿＿＿＿＿＿＿＿＿＿＿＿＿＿前开到卖方。信用证有效期为装船后１５天在＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿到期。<BR> 

　　１０．装运单据：卖方应提供下列单据。<BR> 

　　（１）已装船清洁提单；<BR> 

　　（２）发票；<BR> 

　　（３）装箱单；<BR> 

　　（４）保险单。<BR> 

　　１１．装运条件：<BR> 

　　（１）载运船由卖方安排，允许分批装运，并允许转船；<BR> 

　　（２）卖方于货物装船后，应将合同号码、品名、数量、船名、装船日期以电报通知买方。<BR> 

　　１２．索赔：卖方同意受理因货物的质量、数量和（或）规格与合同规定不符的异议索赔，但卖方仅负责赔偿由于制造工艺不良或材质不佳所造成的质量不符部份。有关安装不当或使用不善造成的索赔或损失，卖方均不予受理。提出索赔异议必须提供有名的、并经卖方认可公证行的检验报告。有关质量方面索赔异议应于货到目的地后３个月内提出，有关数量和（或）规格索赔异议应于货到目的地后３０天内提出。一切损失凡由于自然原因或属于船方或保险公司责任范围内者，卖方概不受理。如买方不能在合同规定期限内将信用证开出，或者开来的信用证不符合合同规定，而在接到卖方通知后，不能按期办妥修正，卖方可以撤销合同或延期交货，并有权提出赔偿要求。<BR> 

　　１３．不可抗力：因不可抗力不能如期交货或不能交货时，卖方不负责任。但卖方必须向买方提供中国国际贸易促进委员会或其他有关机构所出具的证明。<BR> 

　　１４．仲裁：凡因执行本合同或与本合同有关事项所发生的一切争执，应由双方通过友好协商方式解决。如不能取得协议时，则在被告国家根据被告国家仲裁机构的仲裁程序规则进行仲裁。仲裁裁决是终局的，对双方具有同等的约束力。仲裁费用，除仲裁机构另有决定外，均由败诉方负担。<BR> 

　　１５．其他：对本合同之任何变更及增加，仅在以书面经双方签字后，方为有效，任何一方在未取得对方书面同意前，无权将本合同规定之权利及义务转让给第三者。<BR> 

　　自本合同签订后，以前有关本批交易的函电均作为无效。<BR> 

　　１６．备注：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　买方：＿＿＿＿＿＿＿＿＿＿＿＿＿＿卖方：＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　<BR> 

　　注：经买卖双方以电传或电报谈妥条件，议定合同货物品质、数量、交货期及价格等，尔后由卖方书就售货合同，正式签约后作为成交的依据。<BR> 

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
