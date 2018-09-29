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

      <div align="center">煤矿机电产品购销合同</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　煤炭<BR> 

　　<BR> 

　　需方编号：　　签订地点：　　签订日期：　　年　　月　　日　合同编号：<BR> 

　　┌──┬──┬──┬─┬─┬─┬───┬─┬──┬────┬───┬─┐<BR> 

　　│设备│　　│计量│　│数│　│要　求│　│合同│单价：　│合　同│　│<BR> 

　　│　　│　　│　　│　│　│　│　　　│　│　　├────┤　　　│　│<BR> 

　　│名称│　　│单位│　│量│　│交货期│　│价格│总价：　│交货期│　│<BR> 

　　├──┴──┴──┴─┼─┴─┴───┴─┴──┼────┴───┴─┤<BR> 

　　│主辅机型号规格：　　│　　需　　方　　　　　　│　供　　　　　方　　│<BR> 

　　│　　　　　　　　　　├────┬───────┼────┬─────┤<BR> 

　　│　　　　　　　　　　│订货单位│　　　　　　　│供货单位│　　　　　│<BR> 

　　│　　　　　　　　　　├────┼───────┼────┼─────┤<BR> 

　　│　　　　　　　　　　│单项工程│　　　　　　　│通讯地址│　　　　　│<BR> 

　　│　　　　　　　　　　├────┼───────┼──┬─┼───┬─┤<BR> 

　　│　　　　　　　　　　│通讯地址│　　　　　　　│邮编│　│代表人│　│<BR> 

　　│　　　　　　　　　　├──┼──┬───┬──┼──┼─┼───┼─┤<BR> 

　　│　　　　　　　　　　│邮编│　　│代表人│　　│电报│　│电　话│　│<BR> 

　　│　　　　　　　　　　├──┼──┼───┼──┼──┴─┼───┴─┤<BR> 

　　│　　　　　　　　　　│电话│　　│电　话│　　│结算银行│　　　　　│<BR> 

　　│　　　　　　　　　　│　　│　　│　　　│　　│及帐号　│　　　　　│　　　<BR> 

　　│　　　　　　　　　　├──┴─┬┴───┴──┼────┴─────┤<BR> 

　　│　　　　　　　　　　│结算银行│　　　　　　　│质量标准:　　　　　 │<BR> 

　　│　　　　　　　　　　│及帐号　│　　　　　　　│防爆检验合格证号:　 │<BR> 

　　├────┬─┬─┬─┴┬─┬─┴┬──────┤验收方法及期限:　　 │<BR> 

　　│运输方式│　│交│供方│　│到站│整车: 零担: │运杂费用承担:　　　 │<BR> 

　　├────┼─┤货├──┼─┼──┼──────┤包装要求及费用承担 :│<BR> 

　　│交(提)货│　│方│　　│　│结算│　　　　　　│　　　　　　　　　　│<BR> 

　　│地　　点│　│式│需方│　│方式│　　　　　　│　　　　　　　　　　│<BR> 

　　├────┴┬┴─┴──┴─┴──┴──────┼──────────┤ <BR> 

　　│违约责任　│　　　　　　　　　　　　　　　　　│鉴(公)证　　意见　　│<BR> 

　　├───┬─┴─────┬────┬──────┤　　　　　　　　　　│<BR> 

　　│选　择│　　　　　　　│记事:　 │争议解决方式│　　　　　　　　　　│<BR> 

　　│供　货│　　　　　　　│　　　　│　　　　　　│　签(公)证机关(章)　│<BR> 

　　│厂　家│　　　　　　　│　　　　│　　　　　　│　经办人:年 月　日　│<BR> 

　　├───┴───────┴────┴──────┴──────────┤<BR> 

　　│双方商定的其它事项另附,本合同附件 份　　　　　　　　　　　　　　　　│　　　　<BR> 

　　│此合同一式　份,供方份,需方份,鉴(公)证机关份　　　　　　　　　　　　 │<BR> 

　　└──────────────────────────────────┘依照《中华人民共和<BR> 

　　国经济合同法》和《工矿产品购销合同条例》，经双方协商一致，签订本合同并严格执行。<BR> 

　　监制部门：　　　　　　　印制单位：<BR> 

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
