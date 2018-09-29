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

      <div align="center">抵押物清单</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR>

        　　───┬─┬─┬─┬──┬──┬──┬──┬──┬──┬─┬──┬─┬─<BR>

        　　│规│　│　│原值│折旧│基期│抵押│产权│有效│质│设定│　│<BR>

        　　抵押物│格│单│数│(万 │率　│价值│价值│证书│使用│量│抵押│处│备<BR>

        　　 名称│型│位│量│元) │(％)│(万 │(万 │及编│期　│状│状况│所│注<BR>

        　　│ │　 │　│　　│　　│元) │元) │号　│　　│况│　　│　│ <BR>

        　　───┼─┼─┼─┼──┼──┼──┼──┼──┼──┼─┼──┼─┼─<BR>

        　 │　 │ 　│　 │　│　　│　　│　　│　　│　　│　│　　│　│<BR>

        　 │　 │ 　│　 │　│　　│　　│　　│　　│　　│　│　　│　│<BR>

        　 │　 │ 　│　 │　│　　│　　│　　│　　│　　│　│　　│　│<BR>

        　 │　 │ 　│　 │　│　　│　　│　　│　　│　　│　│　　│　│<BR>

        　　───┴─┴─┴─┴──┴──┴──┴──┴──┴──┴─┴──┴─┴─<BR>

        　　注：基期价值为设定抵押时对抵押物的估价。<BR>

        　　抵押人公章：　　　　　抵押权人公章：<BR>

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
