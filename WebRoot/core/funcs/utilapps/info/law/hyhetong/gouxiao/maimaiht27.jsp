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

      <div align="center">销售确认书（一）</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> （凭卖方样品买卖）</p>

      <p> 　　<BR> 

　　＿＿＿＿＿＿公司为一方（以下简称卖方）＿＿＿＿＿＿＿＿公司为另一方（以下简称买方）双方授权代表友好协商，取得一致意见，签订销售确认书，其条款如下：<BR> 

　　１．根据买方要求，卖方同意提供关于２３７６２型２５１号零备件，凭卖方样品买卖，详见卖方样品图解（略）。<BR> 

　　２．本销售确认书货款总金额为＿＿＿＿＿＿＿＿Ｕ．Ｓ．Ｄ．（大写：＿＿＿＿美元）为Ｆ．Ｏ．Ｂ．价，即包括下列各项费用：<BR> 

　　（１）＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿价格；<BR> 

　　（２）货物从生产厂到达＿＿＿＿＿＿＿＿＿＿交货港的运输费；<BR> 

　　（３）适合空运条件的包装费；<BR> 

　　（４）买方委托卖方办理销售确认书所列货物由＿＿＿＿＿＿＿＿＿港空运至＿＿＿＿＿港。运输费、保险费、手续费等一切费用均由买方支付。<BR> 

　　３．＿＿＿＿＿＿＿＿公司于付款日期向＿＿＿＿＿＿＿＿＿银行开出现金支票。卖方收到现金支票后即开出如下单据：<BR> 

　　（１）空运提单；<BR> 

　　（２）商业发货票（运费、保险费及手续费等收据）；<BR> 

　　（３）装箱单一式两份（其中一份装入箱内）。<BR> 

　　４．本销售确认书的货物为空运包装，每个装箱两侧均用英文刷写下列标记：<BR> 

　　＿＿＿＿＿＿＿＿＿＿公司净重（公斤）＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　标记：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　长×宽×高：＿＿＿＿＿＿毫米×＿＿＿＿＿毫米×＿＿＿＿＿＿毫米<BR> 

　　卸货港：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　收货人：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　发货人：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　销售确认书号：＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿<BR> 

　　５．买卖双方各持英文销售确认书一份，至代表签字之日起立即生效。<BR> 

　　买方：＿＿＿＿＿＿＿＿＿＿＿　签字：＿＿＿＿＿＿＿＿＿<BR> 

　　卖方：＿＿＿＿＿＿＿＿＿＿＿　签字：＿＿＿＿＿＿＿＿＿<BR> 

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
