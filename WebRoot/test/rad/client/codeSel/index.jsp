<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>主页面</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="/yh/core/js/datastructs.js"></script>
<script type="text/Javascript" src="/yh/core/js/sys.js" ></script>
<script type="text/Javascript" src="/yh/core/js/prototype.js" ></script>
<script type="text/Javascript" src="/yh/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="/yh/rad/codeSel/codeSel.js" ></script>
<script type="text/Javascript" src="/yh/rad/grid/grid.js" ></script>

<link rel="stylesheet" type="text/css" href="/yh/raw/cy/grid/grid.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
function onSubmit3(){
	//selectCode ({sort:1,tableNo:"",codeField:"",nameField:"",filterField:"",filterValue:"",currValue:"",orderBy:""});
	var curr = document.getElementById("pre3").value; 
	selectCode ({sort:"1",tableNo:"10002",codeField:"代码",nameField:"priovinceName",codeFieldNo:"编号",nameFieldNo:"priovinceNo",filterField:"priovinceName",filterValue:"",currValue:curr,orderBy:"priovinceNo"});
}
function onSubmit4(){
  var curr = document.getElementById("pre4").value; 
	//selectCode ({sort:1,tableNo:"",codeField:"",nameField:"",filterField:"",filterValue:"",currValue:"",orderBy:""});
	selectCode ({sort:"2",tableNo:"10002",codeField:"代码",nameField:"priovinceName",codeFieldNo:"编号",nameFieldNo:"priovinceNo",filterField:"priovinceName",filterValue:"",currValue:curr,orderBy:"priovinceName"});
}
</script>
</head>
<body >
<div id="grid1"></div>
<font class="big3">代码选择控件</font><br>
值：<input type="text" id ="pre3" class="BigInput">
<input type="button" value="单一代码选择" onclick="onSubmit3()" class="BigButton"><br>
值：<input type="text" id ="pre4" class="BigInput">
<input type="button" value="多行代码选择" onclick="onSubmit4()" class="BigButton"><br>
</body>
</html>