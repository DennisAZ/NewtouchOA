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

      <div align="center">承揽合同</font></div>

    </td>

  </tr>

  <tr>

    <td height="54" valign="top" class="TableData">

      <p align="center"></p>

      <p align="center"></p>

      <p align="center"> </p>

      <p> 　　<BR> 

　　定作方：＿＿＿＿＿＿＿＿＿＿＿＿　　　　　合同编号：<BR> 

　　承揽方：＿＿＿＿＿＿＿＿＿＿＿＿　　　　　签订地点：<BR> 

　　一、项目名称、任务量、酬金、完成期限、备注　　签订时间：年　月　日<BR> 

　　┌────┬───────┬─────────────┬────┬──┐<BR> 

　　│　　　　│　　　　　　　│　　　酬　　　　　　金　　│　　　　│　　│<BR> 

　　│项目名称│任务量（工时）├──────┬──────┤完成期限│备注│<BR> 

　　│　　　　│　　　　　　　│　包　　干　│工资／小时　│　　　　│　　│<BR> 

　　├────┼───────┼──────┼──────┼────┼──┤<BR> 

　　│　　　　│　　　　　　　│　　　　　　│　　　　　　│　　　　│　　│<BR> 

　　├────┼───────┼──────┼──────┼────┼──┤<BR> 

　　│　　　　│　　　　　　　│　　　　　　│　　　　　　│　　　　│　　│<BR> 

　　├────┴───────┴──────┴──────┴────┴──┤<BR> 

　　│　　合计人民币金额（大写）　　　　　　　　　　　　　　　　　　　　　│<BR> 

　　└──────────────────────────────────┘<BR> 

　　（注：空格如不够用，可以另接。）<BR> 

　　二、项目的内容及质量技术要求<BR> 

　　─────────────────────────────────────────────<BR> 

　　──────────<BR> 

　　三、技术资料、图纸提供办法及保密要求<BR> 

　　─────────────────────────────────────────────<BR> 

　　──────────<BR> 

　　四、验收标准、方法和期限<BR> 

　　─────────────────────────────────────────────<BR> 

　　──────────<BR> 

　　五、结算方式及期限　　　　　　　　　　　<BR> 

　　─────────────────────────────────────────────<BR> 

　　──────────<BR> 

　　六、违约责任　　　　　　<BR> 

　　─────────────────────────────────────────────<BR> 

　　──────────<BR> 

　　七、解决合同纠纷的方式　　　　<BR> 

　　─────────────────────────────────────────────<BR> 

　　──────────<BR> 

　　八、双方协商的其它条款　　<BR> 

　　─────────────────────────────────────────────<BR> 

　　──────────　　<BR> 

　　┌─────────┬─────────┬──────────────┐<BR> 

　　│　定　　作　　方　│　承　　揽　　方　│　　　　　　　　　　　　　　│<BR> 

　　│单位名称（章）　　│　单位名称（章）　│　鉴（公）证意见：　　　　　│<BR> 

　　│单位地址：　　　　│　单位地址：　　　│　　　　　　　　　　　　　　│<BR> 

　　│法定代表人：　　　│　法定代表人：　　│　　　　　　　　　　　　　　│<BR> 

　　│委托代理人：　　　│　委托代理人：　　│　经　办　人：　　　　　　　│<BR> 

　　│电　　话：　　　　│　电　　话：　　　│　　　　　　　　　　　　　　│<BR> 

　　│电报挂号：　　　　│　电报挂号：　　　│　　鉴（公）证机关（章）　　│<BR> 

　　│开户银行：　　　　│　开户银行：　　　│　　　　　　年　　月　　日　│<BR> 

　　│帐　　号：　　　　│　帐　　号：　　　│（注：除国家另有规定外，　　│<BR> 

　　│邮政编码：　　　　│　邮政编码：　　　│　鉴（公）证实行自愿原则）　│<BR> 

　　└─────────┴─────────┴──────────────┘<BR> 

　　有效期限：　　年　　月　　日至　　年　　月　　日<BR> 

　　<BR> 

　　监制部门：　　　　　　　　　　　　　印制单位：<BR> 

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
