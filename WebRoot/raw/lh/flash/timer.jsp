<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>flash</title>
<style type="text/css">
#cityList {
float:left;
width:200px;
}
#flash {
float:left;
width:500px;
padding-left:5px;
border:1px solid blue; 
}
</style>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/orgselect.css">
<script type="text/javascript" src="<%=contextPath %>/raw/lh/fckeditor/fckeditor.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script src="AC_RunActiveContent.js" language="javascript"></script>
<script type="text/javascript">
var nowSelected = "";
var selectedColor = "rgb(0, 51, 255)";
var moveColor = "#ccc";
var years =  ["1999","2003","2007","2011"];
//flash里面选中的调用的js的方法。
function selectYear(year) {
  var oldCitye = nowSelected
  nowSelected = year;
  if (oldCitye) {
    $(oldCitye).style.backgroundColor = "";
  }
  if (year) {
    $(year).style.backgroundColor = selectedColor;
  }
}
//flash里面移出时的调用的js的方法。
function disSelectYear(year) {
  if (city) {
    $(city).style.backgroundColor = "";
  }
  nowSelected = "";
}
//js里面选中时间栏时间所调用的方法
function selectYearTd(year) {
  selectYear(year);
  getSwf("time").selectYear(year);
}
//js里面移出时间栏时间所调用的方法
function disSelectYearTd(year) {
  if (year) {
    $(year).style.backgroundColor = "";
  }
  nowSelected = "";
  getSwf("time").disSelectYear(year);
}
function getSwf(swfID) {
  if (navigator.appName.indexOf("Microsoft") != -1) {
    return window[swfID];
  } else {
    return document[swfID];
  }
}
function doInit(){
  for (var i = 0 ;i < years.length ;i++) {
    var ci = years[i];
    addRow(ci);
  }
}
function addRow(ci) {
  var tr = new Element("TR" , {"class":"TableLine1"});
  tr.align = 'center';
  $('list').appendChild(tr);
  var td = new Element("td");
  td.update(ci);
  td.id = ci;
  tr.appendChild(td);
  register(td , ci);
}
function register(td , ci) {
  td.onmouseover = function () {
    selectYearTd(ci);
  }
  td.onmouseout = function () {
    disSelectYearTd(ci);
  }
}
//点击flash中的年所调　用的接口
function onClickYear(year) {
alert(year);
}
</script>
</head>

<body onload="doInit()">
<div id="cityList" >
<table  border="1" cellspacing="0" width="100%" class="TableBlock">
<tbody id="list">
</tbody>
</table>
</div>
&nbsp;&nbsp;
<div id="flash">
  <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0" width="420" height="340" id="time" align="middle">
  <param name="allowScriptAccess" value="sameDomain" />
  <param name="allowFullScreen" value="false" />
  <param name="movie" value="mytime.swf" /><param name="quality" value="high" /><param name="bgcolor" value="#ffffff" />  <embed src="mytime.swf" quality="high" bgcolor="#ffffff" width="420" height="340" name="time" align="middle" allowScriptAccess="sameDomain" allowFullScreen="false" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
  </object>
</div>
</body>
</html>