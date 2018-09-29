<%@ page language = "java" contentType = "text/html; charset=UTF-8"
  pageEncoding = "UTF-8"%>
  <%@ include file = "/core/inc/header.jsp"%>
  <%
  //清除缓存，防止模式窗口页面不更新的情况
  response.setHeader("Pragma","No-Cache");
  response.setHeader("Cache-Control","No-Cache");
  response.setDateHeader("Expires", 0); 
  %>
<html>
<head>
<title>代码浏览选择   -- </title>
<meta http-equiv = "Content-Type" content = "text/html; charset=UTF-8">
<link rel = "stylesheet" href = "<%=cssPath%>/views.css" type = "text/css" />
<link rel = "stylesheet" href = "<%=cssPath%>/style.css" type = "text/css" />
<script type = "text/Javascript" src = "<%=contextPath %>/core/js/datastructs.js"></script>
<script type = "text/Javascript" src = "<%=contextPath %>/core/js/sys.js" ></script>
<script type = "text/Javascript" src = "<%=contextPath %>/core/js/prototype.js" ></script>
<script type = "text/Javascript" src = "<%=contextPath %>/core/js/smartclient.js" ></script>
<script type = "text/javascript" src = "<%=contextPath %>/rad/grid/grid.js">
</script>
<link rel = "stylesheet" type = "text/css" href = "<%=contextPath %>/rad/grid/grid.css"/>
<script type = "text/javascript">
var subId = null;
var valueId = null;
var value = null;
var prWindow = window.dialogArguments[0];
var hd = window.dialogArguments[1];
var url = window.dialogArguments[2];
var filterName = window.dialogArguments[3];
var filterValue = window.dialogArguments[4];
var currValue = window.dialogArguments[5];
var grid = new  YHGrid(hd,url,null,4);
grid.addListener([{name:"click",fun:onc}]);
function mouseOver1(tdom){
  var tab = tdom.parentNode;
  var index = tab.rows.length;
  for(var i = 1 ;i < index;i++){
    var r = tab.rows[i];
    if(r != tdom && r.className != 'TableLineSelect'){
 			r.className = r.nomClassName;
    }
 }
  if(tdom.className != 'TableLineSelect' && tdom.cells[1].innerHTML != "&nbsp;")
    tdom.className = 'TableLineHover';
}
function mouseOut1(tdom){
  if(tdom.className != 'TableLineSelect' && tdom.cells[1].innerHTML != "&nbsp;")
    tdom.className = tdom.nomClassName;
}
function onSubmit(){
  if(!value){
    value = "";
  }
  prWindow.setValue(valueId,value);
  //prWindow.document.getElementById("pre3").value = value;
  window.close();
}
function onc(tdom){
  var tab = tdom.parentNode?tdom.parentNode:this.parentNode;
  var index = tab.rows.length;
  for(var i= 1;i < index;i++){
    var r = tab.rows[i];
    if(r != tdom){
      r.className = r.nomClassName;
    }
  }
  if(tdom.className!='TableLineSelect' && tdom.cells[1].innerHTML != "&nbsp;"){
    tdom.className = 'TableLineSelect';
  }
  if(tdom.cells[1].innerHTML != "&nbsp;" && tdom.className == 'TableLineSelect' ){
    subId = tdom.cells[1].id;
    valueId = tdom.cells[0].innerHTML;
    value = tdom.cells[1].innerHTML;
  }else{
    subId = null; 
    valueId = null;
    value = null; 
  }
}
function loadLis(tab){
  var str = currValue;
  var tab1 = tab;
	var index1 = tab1.rows.length;
  for(var j=0; j < index1 ;j++){
    var r1 = tab1.rows[j];
    if(r1.cells[1].innerHTML == currValue){
      r1.className ='TableLineSelect';
      subId = r1.cells[1].id;
      valueId = r1.cells[0].innerHTML;
      value = r1.cells[1].innerHTML;
    }
  }
}
function loads(){
  var d = document.getElementById("grid1");
  grid.loadListener = loadLis ;
  grid.rendTo("grid1");
}
function onSel(){
  var se = document.getElementById("select").value;
  se = trim(se);
  var url1 = url + "&filterName=" + filterName + "&filterValue=" + encodeURIComponent(se);
  grid.reShow(url1);
  grid.reShowBar(url1);
}
function selIn(event){
  var e = event ? event :(window.event ? window.event : null); 
  if(e.keyCode == 13){
    onSel();
  }
}
function onReset(){
  window.close();
}
function onDeleAll(){
  if(subId){
    var dom = document.getElementById(subId);
    dom.parentNode.className = dom.parentNode.nomClassName;
    subId = null;
    valueId = null;
    value = null;
  }
}
</script>
</head>
<!-- 在模式窗口中使用F5刷新页面 -->
<base target = "_self">
<body onload = "loads()">
<a id = "reload" href = "index.jsp" style = "display:none">reload...</a>
<!-- 控件窗口 -->
<center>
<div style = "width: 400px;height: 330px;padding-top: 20px;">
  <!-- 查询窗口 -->
  <div class = "selectModle" align = "center">
    <input class = "BigInput" type = "text" id = "select" onkeydown = "selIn(event)"> <input class = "SmallButton"  type = "button" value = "查询" onclick = "onSel()">
  </div>
  <div style="width:1px;height:10px;"></div>
  <!--主数据窗口 -->
  <div id = "grid1" class = "dataModle" style = "width: 100%;height:70%;border: thin solid #C0BBB4;"></div>
  <div style="width:1px;height:10px;"></div>
  <!--
		 按键据窗口 
	-->
	<div class = "buttonModle" align = "center">
    <input type = "button" class = "SmallButton" value = "确定" onclick = "onSubmit()" align = "bottom" > &nbsp;<input type = "button" class = "SmallButton" value = "清空" onclick = "onDeleAll()" align = "bottom" >&nbsp; <input class = "SmallButton" type = "button" onclick = "onReset()" value = "取消" align = "bottom" >
	</div>
</div>
</center>
</body>
</html>