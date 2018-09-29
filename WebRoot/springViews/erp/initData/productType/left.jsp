<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>仓库管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<style type="text/css">
<!--
#deplist{display: none;position:absolute;left: 300px;top:50px;width: 0px solid #c30;width: 200px;height: 180px;background: #ccc;overflow: hidden;}
-->
</style>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript">
function doInit(){
  showTree('dtreeId');
}

function showTree(type)
{
	var	url = contextPath + "/SpringR/productType/showWHTree/"+new Date().getTime(); 
    var d = new dTree('d');
    
	 jQuery.ajax({
	   type : 'GET',
	   url : url,
	   success : function(json_data){ 
	   var data1 = JSON.stringify(json_data);
	   var data2 = eval("(" + data1 + ")");
	   if(data2.length > 0){
	    	$(data2).each(function(i,item){  
			 var NodeId = item.id;
			 var Parent_id = item.parentId;
			 var area_name = item.name;
			 var treeCode=item.treeCode;
			 d.add(NodeId,Parent_id,area_name,"javascript:doEdit('"+ NodeId +"','"+ area_name +"','"+ treeCode +"')","","","","",true);
 			});
	   }else{
	   		alert("当前树还没有形成，当前添加的默认为根节点，请先添加树根节点名称！");
	   		return ;
	   }
 		document.getElementById(type).innerHTML = d;
 		display(type); //调用显示这棵树
	   }
	 });
}

function doEdit(id,name,treeCode){
	parent.deptinput.location="add.jsp?id="+id+"&name="+name+"&treeCode="+treeCode;
}
  function  display(type){
   	document.getElementById(type).style.display = "block";
   }
   
   function  disappear(type){
   	document.getElementById(type).style.display = "none";
   }
</script>
</head>
<body onload="doInit()" style="overflow-x:hidden;">
<div id="dtreeId" class="dtree" style="overflow-x:auto;overflow-y:auto;overflow: auto; display: block;"></div>
</body>
</html>