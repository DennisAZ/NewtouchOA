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

      <div align="center">承揽合同（简二）</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　签订日期：＿＿＿＿年＿＿月＿＿日<BR> 

　　<BR> 

　　承揽方（全称）：＿＿＿＿＿＿<BR> 

　　定作方（全称）：＿＿＿＿＿＿<BR> 

　　经双方协商，签订本合同，以资共同信守。<BR> 

　　１．品名、规格、数量、价格、交提货日期：<BR> 

　　┌──┬──┬──┬──┬──┬──┬─────────┬──────┐<BR> 

　　│品名│规格│单位│数量│单价│金额│分期交（收）货数量│超欠幅度％　│<BR> 

　　│　　│　　│　　│　　│　　│　　├─┬─┬─┬─┬─┤　　　　　　│<BR> 

　　│　　│　　│　　│　　│　　│　　│月│月│月│月│月│　　　　　　│<BR> 

　　├──┼──┼──┼──┼──┼──┼─┼─┼─┼─┼─┼──────┤<BR> 

　　│　　│　　│　　│　　│　　│　　│　│　│　│　│　│　　　　　　│<BR> 

　　├──┼──┼──┼──┼──┼──┼─┼─┼─┼─┼─┼──────┤<BR> 

　　│　　│　　│　　│　　│　　│　　│　│　│　│　│　│　　　　　　│<BR> 

　　├──┼──┼──┼──┼──┼──┼─┼─┼─┼─┼─┼──────┤<BR> 

　　│　　│　　│　　│　　│　　│　　│　│　│　│　│　│　　　　　　│<BR> 

　　├──┴──┴──┴──┴──┴──┴─┴─┴─┴─┴─┴──────┤<BR> 

　　│总金额（大写）　佰　拾　万　仟　佰　拾　元　角　分＄＿＿＿＿　　　　│<BR> 

　　└──────────────────────────────────┘<BR> 

　　２．质量标准、验收方法及地点：＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　３．原材料来源及互利方法：＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　４．交（收）货方法、地点及运杂费负担：＿＿＿＿＿＿＿＿＿＿<BR> 

　　５．货款结算时间及方法：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　６．包装要求及包装物回收办法、费用负担：＿＿＿＿＿＿＿＿＿<BR> 

　　７．违约责任：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　８．其他：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　９．不可抗力＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　１０．本合同自签订之日起，双方盖章签字后生效。<BR> 

　　１１．本合同在有效期内任何一方违约，双方都有权向仲裁机关提出书面申诉，要求调解、仲裁处理。<BR> 

　　１２．本合同正本两份，双方各执一份＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　１３．本合同有效期自＿＿＿＿年＿＿月＿＿日至＿＿＿＿年＿＿月＿＿日止。<BR> 

　　┌────────────────┬─────────────────┐<BR> 

　　│承揽人：法人委托书号码＿＿＿＿　│定作人：法人委托书号码＿＿＿＿＿＿│<BR> 

　　│　　　　营业执照号码　　　　　　│　　　　营业执照号码　　　　　　　│<BR> 

　　├────────────────┼─────────────────┤<BR> 

　　│承揽人：　　　　　　　　　　　　│定作人：　　　　　（盖章）　　　　│<BR> 

　　│法定代表人：　　　　　　　　　　│法定代表人：　　　　　　　　　　　│<BR> 

　　│开户银行：　　　　帐号：　　　　│开户银行：　　　　　　帐号：　　　│<BR> 

　　│电话号码：　　　　电挂：　　　　│电话号码：　　　　　　电挂：　　　│<BR> 

　　│住址：　　　　　　　　　　　　　│住址：　　　　　　　　　　　　　　│<BR> 

　　└────────────────┴─────────────────┘（注：因栏目所限，内容较多的可加附页于后）<BR> 

　　<BR> 

　　<BR> 

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
