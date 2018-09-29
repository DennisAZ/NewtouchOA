<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath %>/cmp/tab.css" rel="stylesheet" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/Menu.js" ></script>
<script type="text/javascript">
function doInit(){
  var tree = new DTree({bindToContainerId:'content'
    ,requestUrl:'<%=contextPath%>/yh/rad/docs/tree/YHDTreeAct/getTree.act?id='
    ,isOnceLoad:false//不是同步加载
    ,treeStructure:{isNoTree:false}//每个结点id的不是编码结构
    ,linkPara:{clickFunc:addDeptFunction}//为每个结点的a标签加下点击事件
    ,contextMenu:openRight
  });
 tree.show(); 
}

  
function openRight(event, nodeId){
   var tree = $("content").tree;//取得树实例
   nodeId2 = tree.liEncode + nodeId;
   var pageMenuData2 = [
                        {name:'新增',action:add,icon:imgPath + '/cmp/rightmenu/addStep.gif' , extData:nodeId}
                       ,{name:'修改',action:update,icon:imgPath + '/cmp/rightmenu/addStep.gif', extData:nodeId}
                       ,{name:'删除',action:del,icon:imgPath + '/cmp/rightmenu/addStep.gif', extData:nodeId}
                        ];
   var menu = new Menu({bindTo:nodeId2 , menuData:pageMenuData2});
   menu.show(event);
}
 function add(event ,bindTo, extData,item) {
   alert("新增" + extData);
 }
 function del(event ,bindTo, extData,item) {
   alert("删除" + extData);
 }
 function update(event ,bindTo, extData,item) {
   alert("修改" + extData);
 }
function addDeptFunction(deptId){
  var tree = $("content").tree;//取得树实例
  var node = tree.getNode(deptId);
  alert("你单击的单位：" + node.name + ","+(node.isHaveChild == 1 ? "有" : "无")+"子部门：" );
}
</script>
</head>

<body onload="doInit()">
<div id="content"></div>
</body>
</html>