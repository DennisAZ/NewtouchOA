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
<script type="text/javascript">
function doInit(){
 var tree = new DTree({bindToContainerId:'content'
    ,requestUrl:'<%=contextPath%>/yh/rad/docs/tree/YHDTreeAct/getTreeOnce.act'
    ,isOnceLoad:true//同步加载
    ,checkboxPara:{isHaveCheckbox:true,disCheckedFun:disCheckedFun,checkedFun:checkedFun ,expandEvent:true }
    ,treeStructure:{isNoTree:false}//每个结点id的不是编码结构
    ,linkPara:{clickFunc:addDeptFunction}//为每个结点的a标签加下点击事件
  });
 tree.show(); 
}
function disCheckedFun(id){
  $("nowDisChecked").update(id);
}
function checkedFun(id) {
  $("nowChecked").update(id);
}
function addDeptFunction(deptId){
  var tree = $("content").tree;//取得树实例
  var node = tree.getNode(deptId);
  alert("你击的单位：" + node.name + ","+(node.isHaveChild == 1 ? "有" : "无")+"子部门：" );
}
function getChecked(){
  var tree = $("content").tree;//取得树实例
  var str = tree.getCheckedList();
  $("checked").value = str;
}
</script>
</head>

<body onload="doInit()">
<input type="button" value="选中了" onclick="getChecked();"/> ：<input type="text" value="" id="checked" name="checked"/>
<br/>你刚选中了:<span id="nowChecked"></span><br/>
你刚取消了:<span id="nowDisChecked"></span>
<div id="content"></div>
</body>
</html>