<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<html>
<head>
<title>菜单主分类</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- meta http-equiv="refresh" content="3" >--%>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/menu_left.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript">
var index = "";
function doInit(){
  var data = {
    fix:true,
    panel: 'menuList',
    index: '1',
    data: [{title:'增加菜单主分类', action:sysMenuList},
            //{title:'错误子菜单列表', action:noLink},
            //{title:'菜单备份', action:menuImp},
            {title:'菜单设置', action:menuPara}]
  };
  var menu = new MenuList(data);
  index = menu.getContainerId(1);
  
  iframeFun(index);
  menu.showItem(this,{},1);
  var cnt = 0;
}
function iframeOnload(){
  document.getElementById("iframeD").style.height = "370px";
    //window.frames["iframeD"].document.body.scrollHeight + 5;
  //window.frames["iframeD"].document.body.scrolling = "no";
}

function loadData(){
  var url = "<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/listSysMenu.act";
  location = url;
}

function noLink(){
  var parent = window.parent.contentFrame;
  parent.location = "<%=contextPath%>/core/funcs/menu/nolink.jsp";
}

function menuImp(){
  var parent = window.parent.contentFrame;
  parent.location = "<%=contextPath%>/core/funcs/menu/impexp.jsp";
}

function menuPara(){
  var parent = window.parent.contentFrame;
  parent.location = "<%=contextPath%>/core/funcs/menu/menupara.jsp";
}

function sysMenuList(){
  var parent = window.parent.contentFrame;
  parent.location = "<%=contextPath%>/core/funcs/menu/sysmenuinput.jsp";
}

function iframeFun(index){
  var iframeDiv = document.createElement("iframe");
  
  iframeDiv.id = "iframeD";
  iframeDiv.setAttribute("name", "iframeName");
  iframeDiv.src = "<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/listSysMenu.act";
  iframeDiv.align = "center";
  iframeDiv.width = "100%";
  iframeDiv.style.border = "none";
  iframeDiv.frameborder = "0";
  iframeDiv.style.height = "200px";
  $(index).style.margin ="0px";
  $(index).style.padding ="0px";
  
  $(index).appendChild(iframeDiv);
}

function releaseCatch() {
  /**
  var cnt = 0;
  for (var id in Element.cache) {
    Element.cache[id] = null;
    cnt++;
  }
  **/
  //alert(cnt);
}
</script>
</head>
<body onbeforeunload="releaseCatch();" topmargin="5" onload="doInit()">
<div>
    <img src="<%=contextPath %>/core/styles/imgs/green_arrow.gif"></img>
    &nbsp;<font size="3">菜单主分类设置</font>
  </div>
<div id="menuList">
</div>
</body>
</html>