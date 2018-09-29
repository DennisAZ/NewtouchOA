<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>电话区号查询</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script Language="JavaScript">
var provinces,areas;
function Init(){

  var url = "<%=contextPath %>/yh/core/funcs/utilapps/info/telNo/act/YHTelNoAct/showMain.act"
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    provinces = (json.rtData.province).split(",");
    areas = (json.rtData.area).split(",");
    var tr,td,a;
    for(var i = 0 ; i < provinces.length ; i++){
      if(i%5 == 0){
        if(tr) $('table1').insert(tr);
        tr = new Element('tr', {"class" : "TableData"});
      }
      td = new Element('td', {"align" : "center"});
      a = new Element('a',{"href" : "telInfo.jsp?province="+provinces[i]});
      a.insert(provinces[i]);
      td.insert(a);
      tr.insert(td)
    }
    if(provinces.length%5 > 0){
      $('table1').insert(tr);
    }
    tr = null;

    for(var i = 0 ; i < areas.length ; i++){
      if(i%5 == 0){
        if(tr) $('table2').insert(tr);
        tr = new Element('tr', {"class" : "TableData"});
      }
      td = new Element('td', {"align" : "center"});
      a = new Element('a',{"href" : "telInfo.jsp?area="+areas[i]});
      a.insert(areas[i]);
      td.insert(a);
      tr.insert(td)
    }
    if(provinces.length%5 > 0){
      $('table2').insert(tr);
    }
  }
}

function CheckForm1(){
   if($('area').value==""){
     alert("查询条件不能为空");
     return false;
   }
   else
     window.location.href = "<%=contextPath%>/core/funcs/utilapps/info/telNo/search.jsp?area=" + encodeURI($('area').value);
}

function CheckForm2(){
   if($('telNo').value==""){
     alert("查询条件不能为空");
     return false;
   }
   else
     window.location.href = "<%=contextPath%>/core/funcs/utilapps/info/telNo/search.jsp?telNo=" + encodeURI($('telNo').value);
}

/** 
* 处理键盘按键press事件 
*/ 
function documentKeypress(e){ 
  var id = document.activeElement.id; 
  if(id != 'area' && id != 'telNo' ){ 
    return; 
  } 

  var currKey = 0; 
  var e = e || event; 
  currKey = e.keyCode || e.which || e.charCode; 

  if(currKey == 13){ 
    if(id == 'area'){
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

<body class="bodycolor" topmargin="5" onload="Init()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/infofind.gif" align="absmiddle"><span class="big3"> 电话区号查询</span>
    </td>
  </tr>
</table>

<br>
<div align="center">

  <table class="TableBlock" width="450" align="center">
  <tbody id="table1">
    <tr class="TableHeader">
      <td nowrap align="center" colspan="5"><b>中国 - 省(直辖市/自治区)</b></td>
    </tr>
  </tbody>
  </table>

<br>

  <table class="TableBlock" width="450" align="center">
  <tbody id="table2">
    <tr class="TableHeader">
      <td nowrap align="center" colspan="5"><b>国际 - 大洲</b></td>
    </tr>
  </tbody>
  </table>

<br>

<table class="TableBlock" width="450" align="center">
<tr class="TableHeader">
    <td colspan="3" align="center"><b>模糊查询</b></td>
</tr>

<tr class="TableData">
    <td>市/区/县/街道的名称包含：</td>
    <td align="center"><input type="text" name="area" id="area" size="20" class="BigInput"></td>
    <td align="center"><input type="button" value="查询" class="BigButton" title="进行查询" name="button" onclick="CheckForm1()"></td>
</tr>

<tr class="TableData">
    <td>电话区号包含：</td>
    <td align="center"><input type="text" name="telNo" id="telNo" size="20" class="BigInput"></td>
    <td align="center"><input type="button" value="查询" class="BigButton" title="进行查询" name="button" onclick="CheckForm2()"></td>
</tr>
</table>

</div>

</body>
</html>