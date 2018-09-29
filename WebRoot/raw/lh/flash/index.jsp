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
.selected {
color:#FF0000;
text-align:center;
}
.myitem {
color:#000000;
text-align:center;
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
var citys = [["city1","湾仔"]
              ,["city2","中西区",44]
              ,["city3","东区"]
              ,["city4","南区"]
              ,["city5","观塘"]
              ,["city6","黄大仙"]
                ,["city7","九龙城"]
                  ,["city8","油尖旺"]
                  ,["city9","深水埗"]
                    ,["city10","离岛"]
                      ,["city11","新界"]
                        ,["city12","葵青"]
                        ,["city13","大埔"]
                          ,["city14","北区"]
                            ,["city15","荃湾"]
                              ,["city16","屯门"]
                              ,["city17","元朗"]
                                ,["city18","沙田"]
                                  ,["city19","西贡"]    
                ]
function selectCity(city) {
  var oldCitye = nowSelected
  nowSelected = city;
  if (oldCitye) {
    $(oldCitye).className = "myitem";
  }
  if (city) {
    $(city).className = "selected";
  }
}
function disSelectCity(city) {
  if (city) {
    $(city).className = "myitem";
  }
  nowSelected = "";
}
function selectCityTd(city) {
  selectCity(city);
  getSwf("aa").selectCity(city);
}
function disSelectCityTd(city) {
  if (city) {
    $(city).className = "myitem";
  }
  nowSelected = "";
  getSwf("aa").disSelectCity(city);
}
function getSwf(swfID) {
  if (navigator.appName.indexOf("Microsoft") != -1) {
    return window[swfID];
  } else {
    return document[swfID];
  }
}
function doInit(){
  for (var i = 0 ;i < citys.length ;i++) {
    var ci = citys[i];
    addRow(ci);
  }
}
function addRow(ci) {
  var tr = new Element("TR" , {"class":"TableLine1"});
  $('list').appendChild(tr);
  var td = new Element("td");
  td.align = 'center';
  td.update(ci[1]);
  td.id = ci[0];
  tr.appendChild(td);
  register(td , ci[0]);
}
function register(td , ci) {
  td.onmouseover = function () {
    selectCityTd(ci);
  }
  td.onmouseout = function () {
    disSelectCityTd(ci);
  }
}
//点击flash中的所调用的接口
function onClickCity(selectedCity) {
  
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
<script language="javascript">
  if (AC_FL_RunContent == 0) {
    alert("此页需要 AC_RunActiveContent.js");
  } else {
    AC_FL_RunContent(
      'codebase', 'http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0',
      'width', '420',
      'height', '340',
      'src', 'aa',
      'quality', 'high',
      'pluginspage', 'http://www.macromedia.com/go/getflashplayer',
      'align', 'middle',
      'play', 'true',
      'loop', 'true',
      'scale', 'showall',
      'wmode', 'window',
      'devicefont', 'false',
      'id', 'aa',
      'bgcolor', '#ffffff',
      'name', 'aa',
      'menu', 'true',
      'allowFullScreen', 'false',
      'allowScriptAccess','sameDomain',
      'movie', 'aa',
      'salign', ''
      ); //end AC code
  }
</script>
<noscript>
  <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0" width="420" height="340" id="aa" align="middle">
  <param name="allowScriptAccess" value="sameDomain" />
  <param name="allowFullScreen" value="false" />
  <param name="movie" value="aa.swf" /><param name="quality" value="high" /><param name="bgcolor" value="#ffffff" />  <embed src="两个.swf" quality="high" bgcolor="#ffffff" width="420" height="340" name="aa" align="middle" allowScriptAccess="sameDomain" allowFullScreen="false" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
  </object>
</noscript>
</div>
</body>
</html>