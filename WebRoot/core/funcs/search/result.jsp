<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/menu_left.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<style>
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/search/js/frameSearch.js"></script>
<title>Insert title here</title>
<script>
var searchMenu = null;
var data = {
    panel:'menuList',
    data:[
        {title:'用户', action:getUserInfo},
        {title:'内部邮件(收件箱)',action:getEmialInfo},
        {title:'公告通知', action:getNotifyInfo},
        {title:'通讯簿', action:getAddressInfo},
        {title:'公共文件柜', action:getFileFolderInfo},
        {title:'工作流', action:getFlowWorkInfo}]
     };
function doInIt(){
  searchMenu = new MenuList(data);
  index = searchMenu.getContainerId(1);
 // Ino = menu.getContainerId(2);
  getUserInfo();
  searchMenu.showItem(this,{},1);
}

function doSerach(){
  $('menuList').innerHTML = '';
  doInIt();
}
function act(){
}
</script>
</head>
<body onload="doInIt();" style="background-color:transparent;margin:0 3px;text-align:center;">
<input id="search" name="search" type="text" onblur="doSerach()" style="width:110px" value="<%=request.getParameter("key") %>"></input>&nbsp;<input class="BigButton" type="button" value="搜索" onclick="doSerach()"></input>
<div id="menuList"style="width:80%;margin: 0 auto;" ></div>
</body>
</html>