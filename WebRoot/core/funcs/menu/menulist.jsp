<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树形结构</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/javascript">
var tree = null;
function doInit(){
  tree = new DTree({bindToContainerId:'di'
    ,requestUrl:'<%=contextPath%>/yh/core/funcs/menu/act/YHSysMenuAct/getNoTree.act?num='
    ,isOnceLoad:false
    ,treeStructure:{isNoTree:true,regular:'2,2,2'}
    ,linkPara:{clickFunc:addMenuFunction,dblclickFunc:closeWindow}    
  });
  tree.show();
}

function addMenuFunction(menuId){
  var menu = tree.getNode(menuId);
   var menuName = menu.name;
   window.dialogArguments.document.getElementById("menuIdDesc").value  = menuName;
   
   window.dialogArguments.document.getElementById("menuId").value  = menuId; 
}

</script>
</head>
<body onload="doInit()" >
<div id="di"></div>
</body>
</html>