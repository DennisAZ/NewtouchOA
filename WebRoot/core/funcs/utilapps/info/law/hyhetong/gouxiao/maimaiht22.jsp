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

      <div align="center">买卖合同（一）</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> 工矿产品购销合同</p>

      <p> 　　<BR> 

　　１．格式　工矿产品购销合同<BR> 

　　<BR> 

　　供方：×××　　　　　　　合同编号：<BR> 

　　需方：×××　　　　　　　签订地点：<BR> 

　　签订时间：　　年　　月　　日<BR> 

　　一、产品名称、商标、型号、厂家、数量、金额、供货时间及数量<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　（上述内容可以按国家工商行政管理局制定的示范文本的表格填写，也可以直接用文字表述）<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　二、质量要求、技术标准、供方对质量负责的条件和期限<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　三、交（提）货的方式<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　四、运输方式及到达地（港）费用的负担<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　五、合理损耗及其计算方法<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　六、包装标准、包装物的供应和回收<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　七、验收标准、方法及提出异议的期限<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　八、随机备品、配件工具数量及供应方法<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　九、结算方式及期限<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　十、担保<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　十一、违约责任<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　十二、解决合同纠纷的方式<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　十三、其他约定的事项<BR> 

　　＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿　　<BR> 

　　┌───────────┬───────────┬──────────┐<BR> 

　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　│<BR> 

　　│　供　　　　方　　　　│　需　　　　方　　　　│　鉴（公）证意见　　│<BR> 

　　│单位名称（章）：　　　│单位名称（章）：　　　│　　　　　　　　　　│<BR> 

　　│单位地址：　　　　　　│单位地址：　　　　　　│经办人：　　　　　　│<BR> 

　　│法定代表人：　　　　　│法定代表人：　　　　　│鉴（公）证机关　　　│<BR> 

　　│委托代理人：　　　　　│委托代理人：　　　　　│（章）　　　　　　　│<BR> 

　　│电　　话：　　　　　　│电　　话：　　　　　　│　　年　月　日　　　│<BR> 

　　│电报挂号：　　　　　　│电报挂号：　　　　　　│〔注：除国家另有规定│<BR> 

　　│开户银行：　　　　　　│开户银行：　　　　　　│外，鉴（公）证实行自│<BR> 

　　│帐　　号：　　　　　　│帐　　号：　　　　　　│愿原则〕　　　　　　│<BR> 

　　│邮政编码：　　　　　　│邮政编码：　　　　　　│　　　　　　　　　　│<BR> 

　　└───────────┴───────────┴──────────┘　　<BR> 

　　２．说明<BR> 

　　<BR> 

　　工矿产品购销合同是购销合同的一种形式，是一种以工矿产品为购销合同标的的合同。上述格式是国家工商行政管理局制定的示范文本。<BR> 

　　签订工矿产品购销合同应当注意的问题有：<BR> 

　　（１）合同主体，即当事人双方应当写明供方和需方，不能简称为甲方和乙方，以免在合同中引起误会。<BR> 

　　（２）产品名称，应当注明牌号、商标、生产厂家、型号、规格、等级、花色品种、是否为成套产品等具体内容，要写清楚，不能图省事而略写或者简写。<BR> 

　　（３）产品的质量要求要写明确，其适用的技术标准要写明标准的种类、标准号；供方对质量负责的条件和期限要具体。有国家标准的，一定要按照国家标准的要求办事；没有国家标准而有行业标准的，要按照行业标准办理；没有国家标准和行业标准的，其质量标准由双方约定。<BR> 

　　（４）合同中交货或者提货的方式要写清楚，是自己提货，还是供方代办托运；供方代办托运是以自己的名义还是以需方的名义，是铁路运输还是公路运输或者水运，到达的车站或者港口，如何验收以及运输费用的负担等，都要在合同中写清楚。<BR> 

　　（５）对于一些易损易耗的物品，应当规定合理损耗的数量及比例。<BR> 

　　（６）关于包装条款，不能简单写袋装、瓶装、桶装等，而应当注明是什么材料包装，包装要达到什么要求。<BR> 

　　（７）价款和酬金是合同的主要条款，要写明如何结算，怎么结算以及在什么期限内结算。不能笼统写货到清算或者发货清算。<BR> 

　　（８）对一些重要的产品购销合同，当事人双方都可以要求对方提供担保。担保通常应当订立书面的担保协议，作为主合同的附件。也可以在主合同中写明担保的方式以及担保的内容。<BR> 

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
