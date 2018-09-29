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
var subId = null;
function onSubmit(){
	var prWindow = window.dialogArguments;
	prWindow.document.getElementById("pre1").value = document.getElementById(subId).innerHTML;
  window.close();
}
function onc(dom){
	subId = dom.id;
}
</script>
</head>
<!-- 在模式窗口中使用F5刷新页面 -->
<base target="_self">
<body onkeydown="if (event.keyCode==116){reload.click()}">
<a id="reload" href="index.jsp" style="display:none">reload...</a>
<!-- 控件窗口 -->
<div style="width: 400px;height: 330px;">
  <!-- 查询窗口 
  -->
  <div class="selectModle" align="center">
			<input type="text" id="select"> <input type="button" value="查询">
	</div><!--
	 主数据窗口 
 --><div class="dataModle" style="width: 100%;height:70%;border: thin solid green;">
		<table  width="100%">
			<tr>
				<td style="cursor:pointer; cursor:pointer;" id="sub1" onclick="onc(this)">ss1</td>
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
	</div><!--
		 按键据窗口 
	--><div class="buttonModle" align="center">
		<input type="button" value="确定" onclick="onSubmit()"> &nbsp;&nbsp;&nbsp;&nbsp; <input type="button" value="取消">
	</div>
</div>
</body>
</html>