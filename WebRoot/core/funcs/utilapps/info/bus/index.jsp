<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>公交查询</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script Language="JavaScript">
function Check1(){
   if(document.form1.start.value.trim()=="" || document.form1.end.value.trim()=="")   {
     alert("起始站与终到站均不能为空！可以输入不完整名称");
     return false;
   }
   else{
	   window.location.href = "search.jsp?address=" + $('city').value + "&start=" + encodeURI($('start').value) + "&end=" + encodeURI($('end').value);
   }
}

function New(){
   var url="new.jsp?city=" +$ ("city").value;
   location=url;
}

function Check2(){
   if(document.form1.lineId.value.trim()=="")   {
     alert("线路查询中公交线路不能为空！");
     return (false);
   }
   else{
	   window.location.href = "search.jsp?address=" + $('city').value + "&lineId=" + encodeURI($('lineId').value);
   }
}


/** 
* 处理键盘按键press事件 
*/ 
function documentKeypress(e){ 
	var id = document.activeElement.id; 
	if(id != 'start' && id != 'end' && id != 'lineId'){ 
	  return; 
	} 

	var currKey = 0; 
	var e = e || event; 
	currKey = e.keyCode || e.which || e.charCode; 

	if(currKey == 13){ 
		if(id == 'start' || id == 'end'){
			Check1();
		}
		else {
			Check2();
		}
	} 
} 

document.onkeypress = documentKeypress;

</script>
</head>

<body class="bodycolor" topmargin="5" >

<form action=""  name="form1">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/infofind.gif" align="absmiddle"><span class="big3"> 选择城市 </span><br>
    </td>
  </tr>
</table>

<div align="center">
  <select name="city" id="city" class="BigSelect">
    <option value="BJ" >北京 </option>
    <option value="CD" >成都 </option>
    <option value="CQ" >重庆 </option>
    <option value="GZ" >广州 </option>
    <option value="HZ" >杭州 </option>
    <option value="KM" >昆明 </option>
    <option value="NJ" >南京 </option>
    <option value="QD" >青岛 </option>
    <option value="SH" >南京 </option>
    <option value="SZ" >深圳 </option>
    <option value="TJ" >天津 </option>
    <option value="WH" >武汉 </option>
    <option value="XA" >西安 </option>
  </select>
  &nbsp;&nbsp;<input type="button" value="新建线路" class="BigButton" title="新建公交线路" onclick="New()">
</div>

<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/infofind.gif" align="absmiddle"><span class="big3"> 按车站查询 </span><br>
    </td>
  </tr>
</table>

<div align="center">
<span class="big3">
  起始站：<input type="text" name="start" id="start" size="10" maxlength="50" class="BigInput"  title="起始站与终到站可以输入不完整名称" >&nbsp;&nbsp;
    终到站：<input type="text" name="end" id="end" size="10" maxlength="50" class="BigInput"  title="起始站与终到站可以输入不完整名称" >
    <input type="button" value="查询" class="BigButton" title="开始查询" onclick="Check1()">
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
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/infofind.gif" align="absmiddle"><span class="big3"> 按线路查询 </span><br>
    </td>
  </tr>
</table>

<div align="center">
<span class="big3">
  公交线路：<input type="text" name="lineId" id="lineId" size="10" maxlength="50" class="Biginput" title="输入公交线路（阿拉伯数字）" >
    <input type="button" value="查询" class="BigButton" title="开始查询" onclick="Check2()">
</span>
</form>
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
