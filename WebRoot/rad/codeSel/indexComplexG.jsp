<%@ page language = "java" contentType = "text/html; charset=UTF-8"
  pageEncoding = "UTF-8"%>
  <%@ page import = "java.net.*" %>
  <%@ include file = "/core/inc/header.jsp"%>
  <%
  //清除缓存，防止模式窗口页面不更新的情况
  request.setCharacterEncoding("UTF-8");
  response.setHeader("Pragma","No-Cache");
  response.setHeader("Cache-Control","No-Cache");
  response.setHeader("Charset","UTF-8");
  response.setDateHeader("Expires", 0); 
  %>
<html>
<head>
<title>代码浏览选择   -- </title>
<link rel = "stylesheet" href = "<%=cssPath%>/views.css" type = "text/css" />
<link rel = "stylesheet" href = "<%=cssPath%>/style.css" type = "text/css" />
<script type = "text/Javascript" src = "<%=contextPath %>/core/js/datastructs.js"></script>
<script type = "text/Javascript" src = "<%=contextPath %>/core/js/sys.js" ></script>
<script type = "text/Javascript" src = "<%=contextPath %>/core/js/prototype.js" ></script>
<script type = "text/Javascript" src = "<%=contextPath %>/core/js/smartclient.js" ></script>
<script type = "text/javascript" src = "<%=contextPath %>/rad/grid/grid.js">
</script>
<link rel = "stylesheet" type = "text/css" href = "<%=contextPath %>/rad/grid/grid.css"/>
<meta http-equiv = "Content-Type" content = "text/html; charset=UTF-8">
<script type = "text/javascript">
var prWindow = window.dialogArguments[0];
var hd = window.dialogArguments[1];
var url = window.dialogArguments[2];
var filterName = window.dialogArguments[3];
var filterValue = window.dialogArguments[4];
var currValue = window.dialogArguments[5];
var grid = new YHGrid(hd,url,null,4);
grid.addListener([{name:"click",fun:onc},{name:"mouseover",fun:mouseOver},{name:"mouseout",fun:mouseOut}]);
function mouseOver(tdom){
  var tab = tdom.parentNode;
  var index = tab.rows.length;
  for(var i= 1;i < index;i++){
    var r = tab.rows[i];
    if(r != tdom){
      if(r.className != 'TableLineSelect' && r.cells[1].innerHTML != "&nbsp;"){
         r.className = r.nomClassName;
      }
    }
  }
  if(tdom.className != 'TableLineSelect' && tdom.cells[1].innerHTML != "&nbsp;"){
    tdom.className = 'TableLineHover';
  }
}
function mouseOut(tdom){
  if(tdom.className != 'TableLineSelect' && tdom.cells[1].innerHTML != "&nbsp;"){
    tdom.className = tdom.nomClassName;
  }
}
function onSubmit(){
  var sValue = "";
  var tab = document.getElementById("selectTab");
  var index = tab.rows.length;
  for(var i = 1;i < index;i++){
    var row = tab.rows[i];
		var cell = row.cells[0];
		if(sValue != ""){
      sValue += ","; 
    }
    sValue += cell.innerHTML;
  }
  prWindow.document.getElementById("pre4").value = sValue;
  window.close();
}
function onc(tdom){
  if( tdom.cells[1].innerHTML == "&nbsp;"){
    return;
  }
  var tab = document.getElementById("selectTab");
  var index = tab.rows.length;
  var subrow = null;
  for(var i = 1;i < index;i++){
  	var r = tab.rows[i];
		if(tdom.cells[1].innerHTML ==  r.cells[0].innerHTML){
      subrow = r;
    }
  }
  if(tdom.className != 'TableLineSelect'){
    tdom.className = 'TableLineSelect';
  	var ev = "onclick";
  	var row = tab.insertRow(index);
  	if((index % 2) != 0){
  	  row.className = 'TableLine1';
  	  row.nomClassName = 'TableLine1';
    }else{
			row.className = 'TableLine2';
			row.nomClassName = 'TableLine2';
    }
  	var cell = row.insertCell(0);
  	cell.joinId = tdom.id;
  	cell.style.cursor = "pointer";
  	var dom = tdom;
    cell[ev] = function(){
    	tab.deleteRow(row.rowIndex);
    	tdom.className = tdom.nomClassName;
    	 for(var tr = 1 ;tr < tab.rows.length ; tr++){
      	  tab.rows[tr].className = 'TableLine' + ((tab.rows[tr].rowIndex%2) ? 1 : 2);
       }
    }
    if(tdom.cells[1].innerHTML != "&nbsp;"){
      cell.innerHTML = tdom.cells[1].innerHTML;
     }
  }else{  
    if(subrow){
      tab.deleteRow(subrow.rowIndex);
      for(var tr = 1 ;tr < tab.rows.length ; tr++){
        tab.rows[tr].className = 'TableLine' + ((tab.rows[tr].rowIndex%2) ? 1 : 2);
      }
    }
    tdom.className = tdom.nomClassName;
  }
}
function loadLis(tab){
  var tab1 = tab;
  var tab2 = document.getElementById("selectTab");
  var index1 = tab1.rows.length;
  var index2 = tab2.rows.length;
  var subrow = null;
  for(var i = 0;i < index2;i++){
  	var r2 = tab2.rows[i];
		for(var j = 0; j < index1 ;j++){
      var r1 = tab1.rows[j];
      if(r1.cells[1].innerHTML != "&nbsp;" && r1.cells[1].innerHTML == r2.cells[0].innerHTML){
      r1.className = "TableLineSelect";
      }
    }
  }
}
function selIn(event){
  var e = event ? event :(window.event ? window.event : null); 
  if(e.keyCode == 13){
    onSel();
  }
}
function loads(){
  var tab2 = document.getElementById("selectTab");
  var str = currValue;
  var strs = str.split(",");
  for(var i = 0;i < strs.length;i++){
    var index = tab2.rows.length;
		var row = tab2.insertRow(index);
		if((index % 2) != 0){
      row.className = 'TableLine1';
  	  row.nomClassName = 'TableLine1';
    }else{
      row.className = 'TableLine2';
      row.nomClassName = 'TableLine2';
    }
		var cell = row.insertCell(0);
		cell.style.cursor = "pointer";
 		cell.onclick = function(){
 		  var tab1 = document.getElementById("YH_grid_table");
 		  var index1 = tab1.rows.length;
 		  for(var j = 0; j < index1 ;j++){
		    var r1 = tab1.rows[j];
		    if(r1.cells[1].innerHTML == this.innerHTML){
 	  		  r1.className = r1.nomClassName;
		    }
      }
      tab2.deleteRow(this.parentNode.rowIndex);
      for(var tr = 1 ;tr < tab2.rows.length ; tr++){
        tab2.rows[tr].className = 'TableLine' + ((tab2.rows[tr].rowIndex%2) ? 1 : 2);
      }
    }
  	cell.innerHTML =	strs[i];
  }
  var d = document.getElementById("grid1");
  grid.loadListener = loadLis ;
  grid.rendTo("grid1");
}
function onReset(){
  window.close();
}
function onSel(){
  var se = document.getElementById("select").value;
  se = trim(se);
  var url1 = url+"&filterName=" + filterName + "&filterValue=" + encodeURIComponent(se) ;
  grid.reShow(url1);
  grid.reShowBar(url1);
}
function onDeleAll(){
  var tab1 = document.getElementById("YH_grid_table");
  var tab2 = document.getElementById("selectTab");
  var index1 = tab1.rows.length;
  var index2 = tab2.rows.length;
  var subrow = null;
  for(var i = 1;i < index2;i++){
    var r2 = tab2.rows[1];
		for(var j = 0; j < index1 ;j++){
      var r1 = tab1.rows[j];
		  if(r1.cells[1].innerHTML != "&nbsp;" && r1.cells[1].innerHTML == r2.cells[0].innerHTML){
        r1.className = r1.nomClassName;
		  }
    }
		r2.parentNode.removeChild(r2);
  }
}
</script>
</head>
<!-- 在模式窗口中使用F5刷新页面 -->
<base target = "_self">
<body  onkeydown = "if (event.keyCode==116){reload.click()}" onload = "loads()" >
<a id = "reload" href = "index.jsp" style = "display:none">reload...</a>
<!-- 控件窗口 -->
<div style = "width: 430px;height: 330px;">
  <table width = "100%" height = "100%">
    <tr>
      <td width = "60%" height = "100%">
        <div style = "width:100%;height:100%;border-right: thin solid #C0BBB4;">
          <!-- 查询窗口 -->
          <div class = "selectModle" style = "width: 100%;height:8%;" >
		  	    <input class = "BigInput" type = "text" id = "select" onkeydown = "selIn(event)"> <input class = "SmallButton" type = "button" value = "查询" onclick = "onSel()">
	        </div>
	        <div style="width:1px;height:10px;"></div>
	        <!--主数据窗口 -->
	        <div class = "dataModle" style = "width: 100%;height:300px;overflow-y:auto ;">
		  	     <div id = "grid1" style = "width: 100%;height:100%;scrollbar-base-color : buttonface; " >
		  		   </div>
		      </div>
        </div>
      </td>
      <td width = "40%" height = "100%">
        <div style = "width:100%;height:100%; ">
          <div style = "width: 100%;height:300px;overflow-y:auto ;" >
            <table  width = "100%" id = "selectTab" style = "text-align:center ;">
			  		  <tr class = "TableHeader">
			  			  <th>已选取代码</th>
			  		  </tr>
			  		</table>
		  		</div>
			  	<!-- 按键据窗口 -->
			  	<div class = "buttonModle" style = "width:100%;height:10%;">
		        <input type = "button" class = "SmallButton" value = "确定" onclick = "onSubmit()"> <input type = "button" class = "SmallButton" value = "清空" onclick = "onDeleAll()"> <input class = "SmallButton" type = "button" value = "取消" onclick = "onReset()" >
	        </div>
        </div>
      </td>
    </tr>
  </table>
</div>
</body>
</html>