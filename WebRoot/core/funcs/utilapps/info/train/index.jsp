<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>列车时刻查询</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script Language="JavaScript">
function CheckForm1(){
   if($('start').value.trim()=="" || $('end').value.trim()==""){ 
	   alert("模糊查询中起始站与终到站均不能为空！可以输入不完整名称");
     return false;
   }
   else{
	   window.location.href = "<%=contextPath%>/core/funcs/utilapps/info/train/search.jsp?start=" + encodeURI($('start').value) + "&end=" + encodeURI($('end').value);
	 }
}

function CheckForm2(){
   if($('train').value.trim()==""){ 
	   alert("精确查询中车次不能为空！");
     return false;
   }
   else{
	   window.location.href = "<%=contextPath%>/core/funcs/utilapps/info/train/search.jsp?train=" + encodeURI($('train').value);
   }
}

/** 
* 处理键盘按键press事件 
*/ 
function documentKeypress(e){ 
  var id = document.activeElement.id; 
  if(id != 'start' && id != 'end' && id != 'train'){ 
    return; 
  } 

  var currKey = 0; 
  var e = e || event; 
  currKey = e.keyCode || e.which || e.charCode; 

  if(currKey == 13){ 
    if(id == 'start' || id == 'end'){
    	CheckForm1();
    }
    else {
    	CheckForm2();
    }
  } 
} 

document.onkeypress = documentKeypress;
</script>
</head>

<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/infofind.gif" align="absmiddle"><span class="big3"> 列车时刻模糊查询</span><br>
    </td>
  </tr>
</table>

<div align="center">
<span class="big3">
      起始站：<input type="text" name="start" id="start" size="10" maxlength="50" class="BigInput" title="起始站与终到站可以输入不完整名称" >&nbsp;&nbsp;
        终到站：<input type="text" name="end" id="end" size="10" maxlength="50" class="BigInput" title="起始站与终到站可以输入不完整名称" >
        <input type="button" value="查询" class="BigButton" name="button" title="开始查询" onclick="CheckForm1()">
 </span>
</div>

<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/infofind.gif" align="absmiddle"><span class="big3"> 列车时刻精确查询</span><br>
    </td>
  </tr>
</table>

<div align="center">
<span class="big3">
  车次：<input type="text" name="train" id="train" size="10" maxlength="50" class="Biginput" >
        <input type="button" value="查询" class="BigButton" name="button" onclick="CheckForm2()">
 </span>
</div>

<br>
<table class="MessageBox" align="center" width="260">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">数据仅供参考</div>
    </td>
  </tr>
</table>
</body>
</html>
