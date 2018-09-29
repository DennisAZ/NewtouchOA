<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String im = request.getParameter("im");
  if(im == null){
    im = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/menu_left.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "styles/index.css">
<style>body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
}
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
<script type="text/Javascript" src="<%=contextPath%>/core/frame/ispirit/n12/search/js/frameSearch.js"></script>
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


var im =true;
function doInIt(){
  //如果是在im中打开,则设置宽度为100%
  if (im) {
    $$('body')[0].setStyle({width: '100%'});
  }

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

function search_clear(){
	  window.location.reload();
}


</script>
</head>
<style type="text/css">
#menuList ul li{
border-bottom:#85b6df 1px solid;

}

#menuList ul li:hover{
background-color:#FFFFFF;
}

.head{
height:25px;
line-height:25px;

}

.BlockTop tr{
   height:18px;
}

.BlockTop td{
border-bottom:1px;
border-bottom-color:#D4BFAA;
padding-left:4px;
   height:25px;
}

.BlockTop td.left {
  background-position: left 0;
  width: 0px;
}
.BlockTop td.center {
font-size:12px;
width:100%;
  background-position: 0 -32px;
  background-repeat: repeat-x;
}
.BlockTop td.right {
  background-position: right -64px;
  width: 0px;
}

DIV.moduleContainer{
color:#CCCCCC;
font-size:11px;
padding-left:5px;
  
}

</style>


<body onLoad="doInIt();" style="width: 177px;background-color:#FFFFFF;text-align: center;">
<div id="search_div">
<div id="search_start"></div>
<div id="search_input">
<input id="search" name="search" onkeyup="doSerach()"  >&nbsp;</input>
</div>
<div id="search_end" ><img onClick="search_clear();" src="styles/search_bg.png"></div>
</div><div id="menuList" style="text-align: left;" ></div>
</body>
</html>