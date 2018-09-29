<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>游戏</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<%
String game_name = request.getParameter("game_name");
String game_name_desc = request.getParameter("game_name_desc");
%>
</head>
<body class="bodycolor" topmargin="5">
<%
//if(!check_time_range($MYOA_GAME_TIME_RANGE)){
//   Message("禁止","当前时间禁止玩游戏");
//   exit;
//}
%>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/game.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 游戏 - <%=game_name_desc%></span><br>
    </td>
    </tr>
</table>

<div align="center">
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="/inc/swflash.cab#version=6,0,0,0" width="500" height="400">
<param name=movie value="<%=game_name %>.swf">
<param name=quality value=high>
</object>

<br>
<br>
<input type="button" value="全屏" language="JavaScript" onClick="window.open('<%=game_name %>.swf','game_full','status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes')" class="BigButton">&nbsp;&nbsp;
<input type="button" value="重新开始" language="JavaScript" onClick="location.reload()" class="BigButton">&nbsp;&nbsp;
<input type="button" value="返回" language="JavaScript" onClick="location='index.jsp'" class="BigButton">
</div>
</body>
</html>
