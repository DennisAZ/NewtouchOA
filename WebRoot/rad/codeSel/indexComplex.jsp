<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%
   //清除缓存，防止模式窗口页面不更新的情况
   response.setHeader("Pragma","No-Cache");
   response.setHeader("Cache-Control","No-Cache");
   response.setDateHeader("Expires", 0); 
   %>
<html>
<head>
<title>代码浏览选择   -- </title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
var subId = "";
function onSubmit(){
	var prWindow = window.dialogArguments;
	var sValue = "";
	var tab = document.getElementById("selectTab");
	var index = tab.rows.length;
	for(var i=0;i<index;i++){
		var row = tab.rows[i];
		var cell = row.cells[0];
		if(sValue!=""){
			sValue += ","; 
			}
		sValue += cell.innerHTML;
		}
	prWindow.document.getElementById("pre2").value = sValue;
  window.close();
}
function onc(dom){
  if(dom.isSelect){
		return;
    }
  var tab = document.getElementById("selectTab");
  var index = tab.rows.length;
  var row = tab.insertRow(index);
  var cell = row.insertCell(0);
  cell.joinId = dom.id;
 
  dom.isSelect = true;
  cell.style.cursor = "pointer";
  cell.onclick = function(){
    dom.isSelect = false;
    tab.deleteRow(row.rowIndex);
    }
  cell.innerHTML = dom.innerHTML;
}
</script>
</head>
<!-- 在模式窗口中使用F5刷新页面 -->
<base target="_self">
<body onkeydown="if (event.keyCode==116){reload.click()}">
<a id="reload" href="index.jsp" style="display:none">reload...</a>
<!-- 控件窗口 -->
<div style="width: 430px;height: 330px;">
<table width="100%" height="100%">
	 <tr>
	 <td width="60%" height="100%">
	 <div style="width:100%;height:100%;border-right: thin solid green;">
  <!-- 查询窗口 
  --><div class="selectModle" style="width: 100%;height:8%;" >
			<input type="text" id="select"> <input type="button" value="查询">
	</div><!--
	 主数据窗口 
 --><div class="dataModle" style="width: 100%;height:300px;overflow-y:scroll ;">
			 <div style="width: 100%;height:100%;scrollbar-base-color : buttonface; " >
					<table  width="100%">
						<tr>
							<td style="cursor:pointer; cursor:pointer;" id="sub1" onclick="onc(this)" >ss1</td>
						</tr>
							<tr>
						<td style="cursor:pointer; cursor:pointer;" id="sub2" onclick="onc(this)">ss2</td>
					</tr>
						<tr>
						<td style="cursor:pointer; cursor:pointer;" id="sub3" onclick="onc(this)">ss3</td>
					</tr>
						<tr>
						<td style="cursor:pointer; cursor:pointer;" id="sub4" onclick="onc(this)">ss4</td>
					</tr>
				</table>
				</div>
		</div>
		</div>
</td>

<td width="40%" height="100%">
	<div style="width:100%;height:100%; ">
				<div style="width: 100%;height:300px;overflow-y:scroll ;" >
					<table  width="100%" id = "selectTab">
					</table>
				</div>
				<!--
		 按键据窗口 
	--><div class="buttonModle" style="width:100%;height:10%;">
		<input type="button" value="确定" onclick="onSubmit()"> <input type="button" value="取消">
	</div>
	</div>
	</td>
	</tr>
</table>
</div>
</body>
</html>