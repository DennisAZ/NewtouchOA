<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="/yh/core/js/datastructs.js"></script>
<script type="text/Javascript" src="/yh/core/js/sys.js" ></script>
<script type="text/Javascript" src="/yh/core/js/prototype.js" ></script>
<script type="text/Javascript" src="/yh/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="/yh/rad/codeSel/codeSel.js" ></script>
<script type="text/Javascript" src="/yh/rad/grid/grid.js" ></script>

<title>Insert title here</title>
</head>
<body>
<script type="text/javascript">
function onSelectTable(){
  var curr = document.getElementById("pre4").value; 
	//selectCode ({sort:1,tableNo:"",codeField:"",nameField:"",filterField:"",filterValue:"",currValue:"",orderBy:""});
	selectCode ({sort:"2",tableNo:"99999",codeField:"代码",nameField:"tableNo",codeFieldNo:"编号",nameFieldNo:"tableName",filterField:"tableName",filterValue:"",currValue:curr,orderBy:""});
}
function onSelectPro(){
  var curr = document.getElementById("project").value;
  //打开一个模式窗口
   window
        .showModalDialog(
            'folderBorwer.jsp',
            window,
            'dialogWidth:430px;scroll:auto;dialogHeight:310px;help:no;directories:no;location:no;menubar:no;resizeable:no;status:no;toolbar:no;'); 
  //alert("select project");
}
function onSelectPac(){
  var curr = document.getElementById("project").value; 
  alert("select project");
}
</script>
</body>
选择所要生成代码的表 ：<input type = "text" id = "pre4" name = "tableNames">
<input type = "button" value = "选择..." onclick = "onSelectTable()"><br>
&nbsp;选&nbsp;&nbsp;&nbsp;&nbsp;择&nbsp;&nbsp;Eclipse&nbsp;&nbsp;工&nbsp;&nbsp;&nbsp;程 ：<input type = "text" id = "project">
<input type = "button" value = "选择..." onclick = "onSelectPro()"><br>
&nbsp;选&nbsp;&nbsp;&nbsp;&nbsp;择&nbsp;&nbsp;Eclipse&nbsp;&nbsp;包&nbsp;&nbsp;&nbsp;名 ：<input type = "text" id = "packageName" name = "packageName">
<input type = "button" value = "选择..." onclick = "onSelectPro()"><br>
</html>