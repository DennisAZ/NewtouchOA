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
</head>
<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/game.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 游戏</span><br>
    </td>
    </tr>
</table>

<br>

<table class="TableBlock" width="450" align="center">
    <tr class="TableHeader">
      <td nowrap align="center">游戏列表</td>
    </tr>
<script type="text/Javascript">
var game_name_array = new Array('俄罗斯方块','五联珠','三连棋','拯救金鱼','指球','南京','跳舞机','21点','打砖块','高尔夫','拉霸');
var count = game_name_array.length;
for(var i = 0 ; i < count ; i++){
  document.write('<tr class="TableData"> ' 
	      + '<td align="center"> '
	      + '<a href="game.jsp?game_name=' + i + '&game_name_desc=' + game_name_array[i] + '">' + game_name_array[i] + '</a></td> '
	      + '</tr>');
}
</script>
</table>
</body>
</html>
