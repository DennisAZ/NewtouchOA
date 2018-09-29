<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
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
var isAdminRole = <%=loginUser.isAdminRole()%>;
var tree = null;
function doInit(){
  var data = {
			panel:'menuList',
			index:'1',
			data:[{title:'部门列表'}]
			};
  if(isAdminRole){
    data.data.push({title:'公共自定义分组', action:publicGroup});
  }
	var menu = new MenuList(data);
	index = menu.getContainerId(1);
	//doInit();
	menu.showItem(this,{},1);
  
	var url = contextPath + "/yh/core/funcs/dept/act/YHDeptTreeAct/getTree.act?id=";
	//new MyTreeInit('deplist',url,{isNoTree:false},false,
			//{isHaveCheckbox:false,disCheckedFun:test,checkedFun:test1},
		//	{clickFunc:test,isHaveLink:true});
	tree = new DTree({bindToContainerId:index
		              ,requestUrl:url
			          ,isOnceLoad:false
			          ,checkboxPara:{isHaveCheckbox:false}
			          //,checkboxPara:{isHaveCheckbox:false,disCheckedFun:test,checkedFun:test2}
			          ,linkPara:{clickFunc:test,linkAddress:'index.jsp?id=',target:'_blank'}
					  , isUserModule:true
					  , isHaveTitle:true
			          //,treeStructure:{isNoTree:false,regular:'3,2,2,4'}	
	});
  tree.show();
  tree.open("organizationNodeId");

//////////////////////////////////////
 
}

function test(id){
 /* var li = $(id);
  var parentNode = li.parentNode;
  var deptLocal = $('a-' + id).innerHTML;
  //UL-45
  var parentId = parentNode.id.substr(3);
  var deptParent = '无';
  if(parentId != '0'){
    deptParent = $('a-' + parentId).innerHTML;
  }*/
  var obj = tree.getNode(id);
  var parentId = obj.parentId;
  var deptParent = '无';
  if(parentId != '0'){
  	deptParent = tree.getNode(parentId).name;
  }
  if(obj.extData == "isPriv"){
    window.parent.deptinput.location = "<%=contextPath%>/core/funcs/dept/deptedit.jsp?treeId=" + id + "&deptParent=" + encodeURIComponent(deptParent) + "&deptLocal=" + encodeURIComponent(obj.name) + "&parentId="+parentId;
  }
}
//function grouplist(){
 // var url = "/yh/core/funcs/dept/act/YHDeptAct/getGroupDept.act";
 // var rtJson = getJsonRs(url);
 // if (rtJson.rtState == "0") {
 //   parent.deptinput.location.href = "/core/funcs/dept/deptListTree.jsp";
 // }else {
 //   alert(rtJson.rtMsrg); 
 // }
//}

function publicGroup(){
  window.parent.deptinput.location = "<%=contextPath%>/core/funcs/dept/group/index.jsp";
}
</script>
</head>
<body onload="doInit()" style="overflow-x:hidden;">
 <div id="deplist"></div>
 <div id="menuList">
</div>
</body>
</html>