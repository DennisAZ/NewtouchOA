<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
   String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
    	index:'1',
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
	, requestUrl:'<%=contextPath%>/yh/core/module/org_select/act/YHOrgSelectModule/getTree.act?MODULE_ID='+ 4 + '&privNoFlag=2&id=' 
	, isOnceLoad:false 
	, treeStructure:{isNoTree:false} 
	, linkPara:{clickFunc:toListAttendan} 
	, isUserModule:true 
	}); 
	tree.show(); 
	}
function toListAttendan(userId){
  var seqId = '<%=seqId%>'
  if(userId.substr(0,1)=='r'){
    userId = userId.substr(1,userId.length);
    //userId = 233;
    var userDimensionInfo = window.parent.userDimensionInfo;
    userDimensionInfo.location = "<%=contextPath%>/subsys/oa/confidentialFile/setConfidentialFile/showPriv/userfilefolder.jsp?userId=" + userId + "&seqId=" + seqId;
  }
}
</script>
</head>
<body class="" topmargin="1" leftmargin="0" onload="doOnload()">
<div id="menuList">
</div>
</body>
</html>