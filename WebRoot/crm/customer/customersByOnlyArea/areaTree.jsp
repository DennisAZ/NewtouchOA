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
<title>显示各种功能</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/menu_left.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/Javascript">	
var loginUserId = <%=loginUser.getSeqId()%>;
var isAdmin = <%=loginUser.isAdminRole()%>;
var postPrivs = <%=loginUser.getPostPriv()%>;
var tree =  "";
var index = "";
function doInit(){ 
  var data = {
        fix:true,
    	panel:'menuList',
    	data:[{title:'查询区域范围', action:getTree}
    	  ]
       };

   var menu = new MenuList(data);
   index = menu.getContainerId(1);
   menu.showItem(this,{},1);
   getTree();
}

function getTree(){
  $(index).update("");
  tree = new DTree({bindToContainerId:index
    , requestUrl:'<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/getAllByOnlyAreas.act?id='
    , isOnceLoad:false
    , treeStructure:{isNoTree:false}
    , linkPara:{clickFunc:getChildOrEdit}
    , isUserModule:true
    , isHaveTitle:true
  });
	tree.show(); 
	tree.open("organizationNodeId");
}

function getChildOrEdit(areaId){
  var area = tree.getNode(areaId);
  var areaName = area.name;
  if(areaId == "organizationNodeId"){
    return;
  }
  
   var requestUrl = "<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/isOrNotManageAreaPriv.act";
   var rtJsons = getJsonRs(requestUrl, "areaId="+areaId);
   if(rtJsons.rtState == '0'){
     if(rtJsons.rtData[0].isOrNotPriv){
       parent.contentFrame.location = contextPath + "/customAction.do?op=toListCustomersByOnlyArea&range=0&cusState=2&str="+areaId+"&areaName="+encodeURI(areaName);
     }else{    
	   parent.contentFrame.location = "<%=contextPath%>/crm/customer/customersByOnlyArea/showMsg.jsp";
	   return ;
	 }
  }else{    
	  alert(rtJsons.rtMsrg);
	  return ;
  }
}
</script>
</head>
<body onload="doInit()" style="overflow-x:hidden;">
<div id="menuList"></div>
</body>
</html>