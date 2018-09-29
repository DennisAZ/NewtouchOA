<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>jsTree v.1.0 - full featured demo</title>
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/jquery.cookie.js"></script>
	<script type="text/javascript" src="js/jquery.hotkeys.js"></script>
	<script type="text/javascript" src="js/jquery.jstree.min.js"></script>
	<link href="css/default/style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
	<style type="text/css">
	html, body { 
		margin:0; padding:0; 
		font: normal 12px verdana;
	}
	body, td, th, pre, code, select, option, input, textarea { font-family:verdana,arial,sans-serif; font-size:12px; }
	.demo, .demo input, .jstree-dnd-helper, #vakata-contextmenu { font-size:12px; font-family:Verdana; }
	#container {
	overflow:hidden;
	position:relative;
}
	#demo {
	font: normal 13px verdana;
	width:auto;
}

	#menu { height:30px; overflow:auto; }
	#text { margin-top:1px; }

	#alog { font-size:9px !important; margin:5px; border:1px solid silver; }

	.node_input {
	  display:block;
		height:18px;
		width:91px;
		border:none;
    position:relative;
    top:1px;
    left:30px;
		background: url(imgs/input_text.jpg) no-repeat left top;
	}
	
	.tree_header {
		cursor: pointer;
		height:27px;
		background: url(imgs/tree_header.jpg) no-repeat left top;
	}
	
	a {
	  outline-style:none;
	}
	
	a:link,a:visited,a:active,a:hover {
		text-decoration: none;
	}
	
	.tree_header span{
		position:relative;
		left:30px;
		top:8px;
	}
	
  .node_input input{
    width:90px;
    display:block;
    border:none;
    background-color:transparent;
  }
	</style>
	<%
		String tree = (String)request.getAttribute("treeData");	  
	%>
<script type="text/javascript" src="js/tree.js"></script>
</head>
<body>
<form id="form1" name="form1" onsubmit="" action="">
<div id="container">
<div id="demo" style="width:165px;overflow:hidden;"></div>
</div>
</form>
<div>
<!-- 
<input type="text" value="" onkeydown="keydown(event)"/> 
 -->
</div>
<div id="data">
</div>
</body>
</html>