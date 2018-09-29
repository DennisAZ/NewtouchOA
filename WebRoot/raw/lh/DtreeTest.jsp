<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/raw/lh/fckeditor/fckeditor.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript"><!--
var tree = null;
function doInit(){
  var p = {bindToContainerId:'di'
    ,isOnceLoad:true
	,requestUrl:contextPath + '/raw/lh/dtree/YHDTreeAct/getNoTreeOnce.act'
	,checkboxPara:{isHaveCheckbox:true}
	,treeStructure:{isNoTree:true,regular:'3,2,2,4'}
	,isFolder:true	
  };
  tree =  new DTree(p);
  tree.show();
}
function test(s){
  alert(s);
}
function test2(id){  
}
function addNode(){
  var nodeJsonObj = new Object();
  nodeJsonObj.nodeId = '11112121113';
  nodeJsonObj.name = '11112121113';
  nodeJsonObj.isHaveChild = '0';
  //nodeJsonObj.parentId = '11';
  tree.addNode(nodeJsonObj);
}
function remove1(){
  //tree.removeNode('5');
  tree.removeNode('11112121111');
  tree.removeNode('11112121110');
}
function getTreeNode(){
 var obj =  $('di').tree.getCurrNode();
 alert($('di').tree);
}
function getFirstNode(){
  var obj = $('di').tree.getFirstNode();
  alert(obj.nodeId);
 }
function openNode(id){
	tree.open(id);
}
function closeNode(id){
  $('di').tree.close(id);  
}
function getCheckedList(){
  var list = tree.getCheckedList();
  alert(list);
}
--></script>
</head>

<body onload="doInit()">
<div id="di" style="border: 1px solid #6E91C7"></div>
<input onclick="addNode()" value="addOne" type="button"/>
<input onclick="remove1()" value="removeNode" type="button"/>
<input onclick="getTreeNode()" value="getTreeNode" type="button"/>
<input onclick="getFirstNode()" value="getFirstNode" type="button"/>
<input onclick="openNode('5')" value="open" type="button"/>
<input onclick="closeNode('1')" value="close" type="button"/>
<input onclick="getCheckedList()" value="getCheckedList" type="button"/>
</body>
</html>