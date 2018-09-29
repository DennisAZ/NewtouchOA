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

      <div align="center">汽车产品供需合同</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　分配单号：　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　共　　　页　第　　　页需要主管部门：　　　　　　　　　　　　　　　　　　　　　　签订地点：　　　合同号：（　　　　）字　　　号　<BR> 

　　┌───────────────────────┬──────────┐<BR> 

　　│　　　　　　　　需　　　　　　方　　　　　　　│　　　供　　　方　　│<BR> 

　　├──┬──────┬──────┬──────┼──────────┤<BR> 

　　│　　│　订货单位　│　收货单位　│　付款单位　│　　　　　　　　　　│<BR> 

　　├──┼──────┼──────┼──────┼──┬─┬──┬──┤<BR> 

　　│单位│　　　　　　│　　　　　　│　　　　　　│法定│　│联系│　　│<BR> 

　　│名称│　　　　　　│　　　　　　│　　　　　　│代表│　│人　│　　│<BR> 

　　├──┼─┬──┬─┼─┬──┬─┼─┬──┬─┼──┼─┼──┼──┤<BR> 

　　│法定│　│联系│　│　│联系│　│　│联系│　│邮政│　│电挂│　　│<BR> 

　　│代表│　│　人│　│　│人　│　│　│人　│　│编码│　│　　│　　│<BR> 

　　├──┼─┼──┼─┼─┼──┼─┼─┼──┼─┼──┼─┴──┴──┤<BR> 

　　│邮政│　│电挂│　│　│电挂│　│　│电挂│　│通讯│　　　　　　　│<BR> 

　　│编码│　│　　│　│　│　　│　│　│　　│　│地址│　　　　　　　│<BR> 

　　├──┴─┼──┴─┼─┴──┴─┼─┴──┴─┼──┴┬──────┤<BR> 

　　│通讯地址│　　　　│　　　　　　│　　　　　　│开户行│　　　　　　│<BR> 

　　├────┼────┼──────┼──────┼───┴─┬────┤<BR> 

　　│电　　话│　　　　│　　　　　　│　　　　　　│　　　　　│电　话　│<BR> 

　　├────┼────┼──────┼──────┼─────┼────┤<BR> 

　　│开户行　│　　　　│　　　　　　│　　　　　　│　　　　　│　　　　│<BR> 

　　├────┼────┼──────┼──────┤　　　　　│　　　　│<BR> 

　　│银行帐号│　　　　│　　　　　　│　　　　　　│　　　　　│　　　　│<BR> 

　　└────┴────┴──────┴──────┴─────┴────┘<BR> 

　　┌────┬─────┬────┬────┬────────┬────┐<BR> 

　　│付款方式│　　　　　│付款期限│　　　　│中转、联运（港）│　　　　│<BR> 

　　└────┴─────┴────┴────┴────────┴────┘<BR> 

　　┌─┬─┬──┬─┬──┬──┬───┬───────┬─┬─┬──┐<BR> 

　　│　│　│产品│　│数量│单价│总金额│　交货进度　　│交│代│验　│<BR> 

　　│序│型│名称│代├──┼──┼───┼─┬─┬─┬─┤货│运│交货│<BR> 

　　│号│号│及　│码│(辆)│(元)│(元)　│一│二│三│四│方│到│地点│<BR> 

　　│　│　│规格│　│　　│　　│　　　│季│季│季│季│式│站│　　│<BR> 

　　├─┼─┼──┼─┼──┼──┼───┼─┼─┼─┼─┼─┼─┼──┤<BR> 

　　│　│　│　　│　│　　│　　│　　　│　│　│　│　│　│　│　　│<BR> 

　　├─┼─┼──┼─┼──┼──┼───┼─┼─┼─┼─┼─┼─┼──┤<BR> 

　　│　│　│　　│　│　　│　　│　　　│　│　│　│　│　│　│　　│<BR> 

　　├─┼─┼──┼─┼──┼──┼───┼─┼─┼─┼─┼─┼─┼──┤<BR> 

　　│　│　│　　│　│　　│　　│　　　│　│　│　│　│　│　│　　│<BR> 

　　├─┼─┼──┼─┼──┼──┼───┼─┼─┼─┼─┼─┼─┼──┤<BR> 

　　│　│　│　　│　│　　│　　│　　　│　│　│　│　│　│　│　　│<BR> 

　　├─┼─┼──┼─┼──┼──┼───┼─┼─┼─┼─┼─┼─┼──┤<BR> 

　　│　│　│　　│　│　　│　　│　　　│　│　│　│　│　│　│　　│<BR> 

　　├─┼─┼──┼─┼──┼──┼───┼─┼─┼─┼─┼─┼─┼──┤<BR> 

　　│　│　│　　│　│　　│　　│　　　│　│　│　│　│　│　│　　│<BR> 

　　├─┼─┼──┼─┼──┼──┼───┼─┼─┼─┼─┼─┼─┼──┤<BR> 

　　│　│　│　　│　│　　│　　│　　　│　│　│　│　│　│　│　　│<BR> 

　　├─┼─┼──┼─┼──┼──┼───┼─┼─┼─┼─┼─┼─┼──┤<BR> 

　　│　│　│　　│　│　　│　　│　　　│　│　│　│　│　│　│　　│<BR> 

　　├─┼─┼──┼─┼──┼──┼───┼─┼─┼─┼─┼─┼─┼──┤<BR> 

　　│　│　│　　│　│　　│　　│　　　│　│　│　│　│　│　│　　│<BR> 

　　└─┴─┴──┴─┴──┴──┴───┴─┴─┴─┴─┴─┴─┴──┘　　<BR> 

　　其它条款：<BR> 

　　<BR> 

　　一、结算方式：　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　<BR> 

　　１．需方自提车及公路代运，按交货期款到后发车。　　　　　　　　　　　　　　　　　　　　　　　　　　　<BR> 

　　２．供方代运，以供方委托铁路起运日为交货期、货款及运杂费等发车后向需<BR> 

　　方办理托收承付。　　　　　　　　<BR> 

　　二、供方为需方代运和代押运的汽车，供方除向需方核收运费和运输保险费外，每<BR> 

　　辆另收厂内吊装费和押运费　　元／辆。<BR> 

　　三、产品质量：按供方规定的出厂检验标准，如发生质量问题，按供方有关规定实<BR> 

　　行“三包”。　　　　　　　　　　<BR> 

　　四、单价内不含车辆购置附加费。　<BR> 

　　五、违约责任：供方逾期交货（铁路运输等不可抗拒原因除外）应向需方付违约金<BR> 

　　元／辆。需方逾期提货，按到期合同货款的　　％向供方付违约金　　<BR> 

　　元／辆，并付　　％利息（月息）。合同签订后双方必须全面履行，如需修改<BR> 

　　或终止，应经双方协商一致签具修订或撤销的协议书。因故要求合同变更，应<BR> 

　　在交货期前４５天以函（电）向对方协商。本合同未尽事宜，由双方协商解决<BR> 

　　，不能达成一致意见时按《经济合同法》及《工矿产品购销合同条例》有关条<BR> 

　　款规定执行。　　　　　　　　　　　　　　<BR> 

　　六、本合同正本一式两份，供需双方各执一份，其余为副本（副本三份）。<BR> 

　　七、其它约定事项：<BR> 

　　<BR> 

　　<BR> 

　　需方签字：　　　　　　　　　　　　　　　供方签字：<BR> 

　　签订日期：　　　年　　月　　日<BR> 

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
