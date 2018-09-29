<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/menu_left.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript">
var tree = null;
function doInit(){
  var data = {
			panel:'menuList',
			index:'1',
			data:[{title:'组织机构列表'}]
			};
	var menu = new MenuList(data);
	index = menu.getContainerId(1);
	menu.showItem(this,{},1);
  
	var url = contextPath + "/yh/core/esb/client/act/YHDeptTreeAct/getTree.act?id=";
	tree = new DTree({bindToContainerId:index
		              ,requestUrl:url
			          ,isOnceLoad:false
			          ,checkboxPara:{isHaveCheckbox:false}
			          ,linkPara:{clickFunc:test,linkAddress:'index.jsp?id=',target:'_blank'}
					  , isUserModule:true
					  , isHaveTitle:true
	});
  tree.show();
  tree.open("organizationNodeId");
}

function test(id){

  if ("organizationNodeId" != id) 
    parent.deptinput.location = "deptinput.jsp?deptId=" + id ;
  else 
    parent.deptinput.location = "deptinput.jsp";
}
function broadcast(){
  var url = contextPath + "/yh/core/esb/client/act/YHDeptTreeAct/broadcast.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    alert(rtJson.rtMsrg);
    parent.deptListTree.location.reload();
    return ;
  }
}
</script>
</head>
<body onload="doInit()" style="overflow-x:hidden;">
 <div id="menuList">
</div>
<div align=center style="margin-top:5px">
<input class="SmallButton" type="button" value="添加" onclick="parent.deptinput.location.href='deptinput.jsp'"/> 
<input class="SmallButton" type="hidden" value="同步" onclick="broadcast()"/>
</div>
</body>
</html>