<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>dynamic</title>
<link rel="stylesheet" href = "css/tree.css">
<script type="text/javascript" src="js/prototype.js"></script>
<script type="text/javascript" src="js/MyTree1.js"></script>

</head>

<body onload="new MyTreeInit('di',
'http://localhost/yh/raw/lh/dtree/YHNoTreeAct/getTree.act?num=',
{isNoTree:true,regular:'3,2,2'},
false,
{isHaveCheckbox:true,disCheckedFun:test,checkedFun:test1},
{isHaveLink:false,linkAddress:'index.jsp?id=',target:'_blank'})">
<div id="di"></div>
</body>
</html>