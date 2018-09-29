<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示各种功能</title>
<link rel="stylesheet"	href="<%=cssPath%>/menu_left.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript">
var tree =  "";
var index = "";
var Ino = "";
var xT = "";
function doInit()
{
	var data = {
			panel:'menuList',
			data:[{title:'在职人员', action:getTree},
						{title:'离职人员/外部人员', action:getT},
						{title:'员工日志查询'},
						{title:'最近10篇员工日志'}]
			};
	var menu = new MenuList(data);
	index = menu.getContainerId(1);
	Ino = menu.getContainerId(2);
}
function getTree()
{
  tree = new DTree({bindToContainerId:index
    , requestUrl:'<%=contextPath%>/yh/core/module/org_select/act/YHOrgSelectAct/getTree.act?MODULE_ID=' + 
    	document.getElementById("test").value + '&USER_DEPT=135&USER_SEQ_ID=181&id='
    , isOnceLoad:false
    , treeStructure:{isNoTree:false}
    , linkPara:{clickFunc:get}
  });
	tree.show(); 
}
function get(id)
{
	
}
function getT()
{
	xT = new DTree({bindToContainerId:Ino
    , requestUrl:'<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getTree.act?id='
    , isOnceLoad:false
    , treeStructure:{isNoTree:false}
    , linkPara:{clickFunc:get}
  });
	xT.show(); 
}
function get()
{}
</script>
</head>
<body onload="doInit()">
输入模块号<input id="test" type= "text" value="6"><br>
<div id="menuList" style="width:200px;"></div>
</body>
</html>