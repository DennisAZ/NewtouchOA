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

      <div align="center">木材购销(订货)合同</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> 木材购销(订货)合同</p>

      <p> 　　签订时间：　年　月　日<BR> 

　　计量单位：　立方米<BR> 

　　┌──────────┐　　　　　　　　┌─────────┬────┐<BR> 

　　│　　　　　　　　　　│　　　　　　　　│　国家调拨通知书　│字第　号│<BR> 

　　│　　　　　　　　　　│　　　　　　　　├─────────┼────┤<BR> 

　　│　　　　　　　　　　│　　　　　　　　│省（区）调拨通知书│字第　号│<BR> 

　　├────┬─────┤　　　　　　　　├─────────┼────┤<BR> 

　　│任务部门│　　　　　│　　　　　　　　│管理局调拨通知书　│字第　号│<BR> 

　　├────┼─────┤　　　　　　　　├─────────┼────┤<BR> 

　　│用　途　│　　　　　│　　　　　　　　│合　　同　　号　　│字第　号│<BR> 

　　├────┴─┬───┴───┬─┬─┬┴┬─┬─┬────┴────┤<BR> 

　　│　　　　　　│　　到　　站　│材│规│等│单│数│　　　　年　　　　│<BR> 

　　│具体收货单位├─┬──┬──┤树│　│　│　│量├─┬─┬─┬─┬─┤<BR> 

　　│　　　　　　│路│车站│专用│种│格│级│价│合│月│月│月│月│月│<BR> 

　　│　　　　　　│局│港口│线　│　│　│　│　│计│　│　│　│　│　│<BR> 

　　├─┬────┼─┼──┼──┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┤　<BR> 

　　│１│　　　　│　│　　│　　│　│　│　│　│　│　│　│　│　│　│<BR> 

　　├─┼────┼─┼──┼──┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┤<BR> 

　　│２│　　　　│　│　　│　　│　│　│　│　│　│　│　│　│　│　│<BR> 

　　├─┼────┼─┼──┼──┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┤<BR> 

　　│３│　　　　│　│　　│　　│　│　│　│　│　│　│　│　│　│　│<BR> 

　　├─┴────┴─┴──┴──┴─┴─┴┬┴─┴─┴─┴─┴─┴─┴─┤<BR> 

　　│　　　　　　需　　　　　　方　　　　　│　　　　供　　　方　　　　　│<BR> 

　　├──┬──┬─┬──┬──┬──┬──┼──┬───────────┤<BR> 

　　│开户│结算│帐│通讯│电报│电话│邮政│开户│　　　　　　　　　　　│<BR> 

　　│银行│户头│号│地址│挂号│号码│编码│银行│　　　　　　　　　　　│<BR> 

　　├─┬┼──┼─┼──┼──┼──┼──┼──┼───┬────┬──┤<BR> 

　　│１││　　│　│　　│　　│　　│　　│帐号│　　　│电报挂号│　　│<BR> 

　　├─┼┼──┼─┼──┼──┼──┼──┼────┼─┴────┴──┤<BR> 

　　│２││　　│　│　　│　　│　　│　　│通讯地址│　　　　　　　　　│<BR> 

　　├─┼┼──┼─┼──┼──┼──┼──┼────┼─┬────┬──┤<BR> 

　　│３││　　│　│　　│　　│　　│　　│电话号码│　│邮政编码│　　│<BR> 

　　├─┴┴──┴─┴──┴──┴──┴──┼────┴─┴────┴──┤<BR> 

　　│１.本合同按《经济合同法》、《工矿产品 │　　　　　　　　　　　　　　│<BR> 

　　│　购销合同条例》、《木材统一送货办法》│　８．解决合同纠纷方式：　　│<BR> 

　　│　等有关规定执行。　　　　　　　　　　│　　　　　　　　　　　　　　│<BR> 

　　├───────────────────┼──────────────┤<BR> 

　　│２．运输方式：　　　　　　　　　　　　│　９．其他约定事项：　　　　│<BR> 

　　├───────────────────┼──────────────┤<BR> 

　　│３．结算方式：　　　　　　　　　　　　│　　　　　　　　　　　　　　│<BR> 

　　├───────────────────┼──────────────┤<BR> 

　　│４．交（提）货地点、方式：　　　　　　│　　　　　　　　　　　　　　│<BR> 

　　├───────────────────┼──────────────┤<BR> 

　　│５．捆绑用具的供应与回收：　　　　　　│　　　　　　　　　　　　　　│<BR> 

　　├───────────────────┼──────────────┤<BR> 

　　│６．检疫办法、地点及费用负担：　　　　│　　　　　　　　　　　　　　│<BR> 

　　├───────────────────┼──────────────┤<BR> 

　　│７．违约责任：　　　　　　　　　　　　│　　　　　　　　　　　　　　│<BR> 

　　└───────────────────┴──────────────┘<BR> 

　　订货单位：（章）　　　　代理人：　　　供货单位：（章）　　代理人：<BR> 

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
