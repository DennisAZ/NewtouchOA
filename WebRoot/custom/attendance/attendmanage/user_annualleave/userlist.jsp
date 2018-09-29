<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
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
 // Ino = menu.getContainerId(2);
  menu.showItem(this,{},1);
  getTree();
}
function getTree(){
  tree = new DTree({bindToContainerId:index
    , requestUrl:'/yh/yh/core/module/org_select/act/YHOrgSelectModule/getTree.act?MODULE_ID='+ 4 + '&privNoFlag=2&hrFlag=1&id='
    , isOnceLoad:false
    , treeStructure:{isNoTree:false}
    , linkPara:{clickFunc:toListAttendan}
    , isUserModule:true
  });
	tree.show(); 	
	var node = tree.getFirstNode();
    tree.open(node.nodeId);
}
function toListAttendan(userId){
  if(userId.substr(0,1)=='r'){
    userId = userId.substr(1,userId.length);
    //userId = 233;
    var attendance = window.parent.attendance;
    attendance.location = "<%=contextPath%>/custom/attendance/attendmanage/user_annualleave/blank.jsp?userId=" + userId;
  }
}

function openWindow(){
  var url = contextPath + "/custom/attendance/attendmanage/beanch/beanch.jsp";  
  var widths = screen.availWidth/2;  
  var heights = screen.availHeight/2; 
  if(heights == 0 || !heights){
    heights = 700;
  }
  myleft=(screen.availWidth)/3;
  window.open(url, "window","status=0,toolbar=no,menubar=no,location=no,scrollbars=no,left=100,top=10,resizable=yes,height="+ heights +", width="+ widths +"");
}
</script>
</head>
<body onload="doOnload()">
<div id="menuList"></div>
<br>
<span><input class="BigButtonC" onclick="openWindow();return false;" type="button" value="年休假批量设置"/></span>
</body>
</html>