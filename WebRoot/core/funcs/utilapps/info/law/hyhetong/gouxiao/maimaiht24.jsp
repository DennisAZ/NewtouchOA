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

      <div align="center">化肥、化学农药、农膜商品购销合同</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　合同编号：<BR> 

　　签订地点：<BR> 

　　签订时间：　　年　月　日<BR> 

　　供方＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　需方＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　一、产品名称、商标含量规格、数量、金额、供货时间及数量<BR> 

　　┌──┬──┬──┬──┬─┬─┬─┬───────────────┐<BR> 

　　│商品│商标│含量│计量│数│单│金│　　　　送取货时间及数量　　　│<BR> 

　　│名称│牌号│规格│单位│量│价│额├──┬──┬──┬──┬─┬─┤<BR> 

　　│　　│　　│　　│　　│　│　│　│合计│　　│　　│　　│　│　│<BR> 

　　├──┼──┼──┼──┼─┼─┼─┼──┼──┼──┼──┼─┼─┤<BR> 

　　│　　│　　│　　│　　│　│　│　│　　│　　│　　│　　│　│　│<BR> 

　　├──┼──┼──┼──┼─┼─┼─┼──┼──┼──┼──┼─┼─┤<BR> 

　　│　　│　　│　　│　　│　│　│　│　　│　　│　　│　　│　│　│<BR> 

　　├──┼──┼──┼──┼─┼─┼─┼──┼──┼──┼──┼─┼─┤<BR> 

　　│　　│　　│　　│　　│　│　│　│　　│　　│　　│　　│　│　│<BR> 

　　├──┼──┼──┼──┼─┼─┼─┼──┼──┼──┼──┼─┼─┤<BR> 

　　│　　│　　│　　│　　│　│　│　│　　│　　│　　│　　│　│　│<BR> 

　　├──┼──┼──┼──┼─┼─┼─┼──┼──┼──┼──┼─┼─┤<BR> 

　　│　　│　　│　　│　　│　│　│　│　　│　　│　　│　　│　│　│<BR> 

　　├──┴──┴──┴──┴─┴─┴─┴──┴──┴──┴──┴─┴─┤<BR> 

　　│合计金额（大写）　　　　　　　　　　　　　　　　　　　　　　　　　│　<BR> 

　　└─────────────────────────────────┘<BR> 

　　<BR> 

　　续表<BR> 

　　┌─────────────────────────────────┐<BR> 

　　│二、质量标准及要求　　　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　│三、供方对质量负责的期限　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　│四、送（取）货方式　　　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　│五、运输方式及到达站（港）和费用负担　　　　　　　　　　　　　　　│<BR> 

　　│六、合理损耗计算方法　　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　│七、包装标准及费用负担　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　│八、验收方式及提出异议期限　　　　　　　　　　　　　　　　　　　　│<BR> 

　　│九、结算方式及期限　　　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　│十、违约责任　　　　　　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　│十一、解决合同纠纷的方式　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　│十二、其它约定事项　　　　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　├─────────┬────────┬──────────────┤<BR> 

　　│　　供　　方　　　│　　需　　方　　│鉴（公）证意见：　　　　　　│<BR> 

　　│　　　　　　　　　│　　　　　　　　│　　　　　　　　　　　　　　│<BR> 

　　│单位名称（章）　　│单位名称（章）　│　　　　　　　　　　　　　　│<BR> 

　　│单位地址　　　　　│单位地址　　　　│　　　　　　　　　　　　　　│<BR> 

　　│法定代表人　　　　│法定代表人　　　│　　　　　　　　　　　　　　│<BR> 

　　│电　　话　　　　　│电　　话　　　　│　　　　　　　　　　　　　　│<BR> 

　　│电　　挂　　　　　│电　　挂　　　　│　　　　　　　　　　　　　　│<BR> 

　　│图文传真　　　　　│图文传真　　　　│　　　　　　　　　　　　　　│<BR> 

　　│开户银行　　　　　│开户银行　　　　│　　　　　　　　　　　　　　│<BR> 

　　│帐　　号　　　　　│帐　　号　　　　│　　　　　　　　　　　　　　│<BR> 

　　│邮政编码　　　　　│邮政编码　　　　│经办人　鉴（公）证机关（章）│<BR> 

　　├─────────┴────────┴──────────────┤<BR> 

　　│　　　　　　　　　　　　　有效期限：　至　　　年　　月　　日　　　│<BR> 

　　└─────────────────────────────────┘<BR> 

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
