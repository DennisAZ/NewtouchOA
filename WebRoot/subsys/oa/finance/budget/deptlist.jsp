<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<link rel="stylesheet" href = "<%=cssPath%>/menu_left.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript">
function doOnload(){
  var data = {
    	panel:'menuList',
    	data:[{title:'在职人员'}
    	]
       };
  var menu = new MenuList(data);
  index = menu.getContainerId(1);
  Ino = menu.getContainerId(2);
  menu.showItem(this,{},1);
  getTree();
}
function getTree(){
	var url = contextPath + "/yh/core/funcs/dept/act/YHDeptTreeAct/getTree.act?id=";
	//new MyTreeInit('deplist',url,{isNoTree:false},false,
			//{isHaveCheckbox:false,disCheckedFun:test,checkedFun:test1},
		//	{clickFunc:test,isHaveLink:true});
	tree = new DTree({bindToContainerId:index
		              ,requestUrl:url
			          ,isOnceLoad:false
			          ,checkboxPara:{isHaveCheckbox:false}
			          //,checkboxPara:{isHaveCheckbox:false,disCheckedFun:test,checkedFun:test2}
			          ,linkPara:{clickFunc:toListAttendan,linkAddress:'index.jsp?id=',target:'_blank'}
					  , isUserModule:true
					  , isHaveTitle:true
			          //,treeStructure:{isNoTree:false,regular:'3,2,2,4'}	
	});
  tree.show()
}
function toListAttendan(deptId){
   var attendance = window.parent.attendance;
   if(!isInteger(deptId)){
     deptId = "";
   }
   attendance.location = "<%=contextPath%>/subsys/oa/finance/budget/deptDetail.jsp?deptId=" + deptId;
}
</script>
</head>
<body class="" topmargin="1" leftmargin="0" onload="doOnload()">
<div id="menuList"></div>
</body>
</html>