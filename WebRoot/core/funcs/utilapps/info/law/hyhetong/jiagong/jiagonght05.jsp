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

      <div align="center">承揽合同（条款）</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　定作方：＿＿＿＿＿＿＿（简称甲方）<BR> 

　　承揽方：＿＿＿＿＿＿＿（简称乙方）<BR> 

　　经双方协商一致，订立下列条款，共同信守。<BR> 

　　┌─────┬──────┬───┬───┬───┬─────────┐<BR> 

　　│品名或项目│规格、型号　│单位　│数量　│单价　│价款或酬金（元）　│<BR> 

　　├─────┼──────┼───┼───┼───┼─────────┤<BR> 

　　│　　　　　│　　　　　　│　　　│　　　│　　　│　　　　　　　　　│<BR> 

　　├─────┼──────┼───┼───┼───┼─────────┤<BR> 

　　│　　　　　│　　　　　　│　　　│　　　│　　　│　　　　　　　　　│<BR> 

　　├─────┼──────┼───┼───┼───┼─────────┤<BR> 

　　│　　　　　│　　　　　　│　　　│　　　│　　　│　　　　　　　　　│<BR> 

　　├─────┴──────┴───┴───┴───┴─────────┤<BR> 

　　│金额：　仟　佰　拾　万　仟　佰　拾　元　角　分　　　　　　　　　　　│<BR> 

　　└──────────────────────────────────┘<BR> 

　　１．产品的质量、包装、加工方法：<BR> 

　　２．原材料的提供以及规格、数量、质量：<BR> 

　　３．履行的期限、地点和方式：<BR> 

　　４．验收标准和方法：<BR> 

　　５．结算方式：<BR> 

　　６．违约责任：除不可抗力的原因外，任何一方不履行合同，都要承担违约责任。<BR> 

　　（１）承揽方的违约责任：不能交付定作物或完不成工作，偿付不能交付定作物或不能完成工作部分价款总值＿＿＿＿％或酬金总额＿＿＿＿的违约金。<BR> 

　　（２）定作方的违约责任：中途变更定作物的数量、规格、质量和设计等，赔偿承揽方因此造成的损失，中途解除合同，属承揽方提供原材料的，偿付未履行部分价款总值＿＿＿％的违约金；不属承揽方提供原材料的，偿付承揽方以未履行部分酬金＿＿＿＿％的违约金。<BR> 

　　７．双方约定的其它条款：<BR> 

　　８．以上条款如有未尽事宜，应以书面协议，作为附件。<BR> 

　　９．本合同执行中发生纠纷，双方应及时协商解决，协商不成时，任何一方都可以向合同管理机关申请调解、仲裁，也可以直接向人民法院起诉。<BR> 

　　１０．本合同一式＿＿＿份，正本双方各执＿＿＿＿份，副本送工商行政管理局所存查。<BR> 

　　１１．本合同有效期限：从双方签署之日起到＿＿＿＿年＿＿月＿＿日止。<BR> 

　　１２．本合同签订日期：＿＿＿＿年＿＿月＿＿日<BR> 

　　<BR> 

　　定作方：＿＿＿＿＿　　　　　　　　　承揽方：＿＿＿＿＿<BR> 

　　法定代表人：＿＿＿＿＿　　　　　　　法定代表人：＿＿＿＿<BR> 

　　开户银行：＿＿＿＿＿＿　　　　　　　开户银行：＿＿＿＿＿<BR> 

　　帐号：＿＿＿＿＿＿＿　　　　　　　　帐号：＿＿＿＿＿＿＿<BR> 

　　<BR> 

　　＿＿＿＿年＿＿月＿＿日<BR> 

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
