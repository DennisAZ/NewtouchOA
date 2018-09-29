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

      <div align="center">水水货物联运合同</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　┌─────────────────┐<BR> 

　　│　　本表经承运人签盖运输合同专用章│<BR> 

　　│后，具有月度运输合同效力，有关承运│<BR> 

　　│人与托运人，收货人之间的权利、义务│<BR> 

　　│关系和责任界限，按《水路货物运输规│<BR> 

　　│则》及运杂费用的有关规定办理。　　│<BR> 

　　└─────────────────┘<BR> 

　　<BR> 

　　<BR> 

　　货物于　　　月　　日　　　　　　　　　　　　　　　　　　<BR> 

　　<BR> 

　　集中于：　　　　交接清单号码　　　　　　　运单号码　　　<BR> 

　　<BR> 

　　┌──────┬───┬──┬──┬───┬───┬───┬─────┐<BR> 

　　│　船　　名　│　　　│航次│　　│起运港│　　　│到达港│　　　　　│<BR> 

　　├──────┼───┼──┼──┼───┴─┬─┼───┴┬────┤<BR> 

　　│第一接转船名│　　　│航次│　　│第一换装港│　│到达日期│收货人　│<BR> 

　　├──────┼───┼──┼──┼─────┼─┤（承运人│（签章）│<BR> 

　　│第二接转船名│　　　│航次│　　│第二换装港│　│　　章）│　　　　│<BR> 

　　├─┬─┬─┬┴┬─┬┴──┴─┬┴───┬─┼─┼─┬──┴────┤<BR> 

　　│发│　│　│　│　│托运人确定│计费重量│　│　│　│　应收费用　　│<BR> 

　　│货│货│件│包│价├─┬───┼──┬─┤等│费│金├─┬─┬─┬─┤<BR> 

　　│符│名│数│装│值│重│体积　│重量│体│级│率│额│项│第│第│第│<BR> 

　　│号│　│　│　│　│量│(长,宽│(吨)│积│　│　│　│目│一│二│三│<BR> 

　　│　│　│　│　│　│　│高)　 │　　│　│　│　│　│　│段│段│段│<BR> 

　　│　│　│　│　│　│吨│M　　 │　　│m3│　│　│　├─┼─┼─┼─┤<BR> 

　　├─┼─┼─┴─┴─┴─┴───┴──┴─┴─┴─┴─┤运│　│　│　│<BR> 

　　│　│　│　　　　　　　　　　　　　　　　　　　　　　│费│　│　│　│<BR> 

　　├─┼─┼──────────────────────┼─┼─┼─┼─┤<BR> 

　　│　│　│　　　　　　　　　　　　　　　　　　　　　　│装│　│　│　│<BR> 

　　├─┼─┼──────────────────────┤船│　│　│　│<BR> 

　　│　│　│　　　　　　　　　　　　　　　　　　　　　　│费│　│　│　│<BR> 

　　├─┼─┼──────────────────────┼─┼─┼─┼─┤<BR> 

　　│　│　│　　　　　　　　　　　　　　　　　　　　　　│　│　│　│　│<BR> 

　　├─┼─┼──────────────────────┼─┼─┼─┼─┤<BR> 

　　│　│　│　　　　　　　　　　　　　　　　　　　　　　│　│　│　│　│<BR> 

　　├─┴─┼──────────────────────┼─┼─┼─┼─┤<BR> 

　　│合计　│　　　　　　　　　　　　　　　　　　　　　　│　│　│　│　│<BR> 

　　├───┴────────────┬─────────┼─┼─┼─┼─┤<BR> 

　　│　运到期限（或约定）　　　　　　│托运人　　　　　　│总│　│　│　│<BR> 

　　├─┬──────────────┤公章）　月　日　　│计│　│　│　│<BR> 

　　│特│　　　　　　　　　　　　　　├─────────┼─┴─┼─┴─┤<BR> 

　　│约│　　　　　　　　　　　　　　│承运日期　　　　　│核算员│　　　│<BR> 

　　│事│　　　　　　　　　　　　　　│　　　　　　　　　├───┼───┤<BR> 

　　│项│　　　　　　　　　　　　　　│起运港承运人章　　│复核员│　　　│<BR> 

　　└─┴──────────────┴─────────┴───┴───┘ <BR> 

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
