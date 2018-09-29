<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String TO_ID = request.getParameter("TO_ID");
  String TO_NAME = request.getParameter("TO_NAME");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript">
var TO_ID = "<%=TO_ID%>";
var TO_NAME = "<%=TO_NAME%>";
var tree;
function doInit(){
  var url =  contextPath + "/yh/core/funcs/dept/act/YHDeptTreeAct/getTree.act?id=";
  //new MyTreeInit('deplist',url,{isNoTree:false},false,
  //{isHaveCheckbox:false},
  //{clickFunc:test,isHaveLink:true,linkAddress:'//core/funcs/dept/user.jsp?seqId=',target:'user'});	
  tree = new DTree({bindToContainerId:'deplist'
                    ,requestUrl:url
			        ,isOnceLoad:false
			        //,checkboxPara:{isHaveCheckbox:false,disCheckedFun:test,checkedFun:test2}
			        ,linkPara:{clickFunc:test,linkAddress:'index.jsp?id=',target:'user'}
			        //,treeStructure:{isNoTree:false,regular:'3,2,2,4'}	
  });
  tree.show();
  var obj = tree.getFirstNode();
  tree.open(obj.nodeId);
  var to_name = TO_NAME;
  var parentWindowObj = window.parent.dialogArguments;
  if(!parentWindowObj.document.getElementById(to_name).value){
    test(obj.nodeId);
  }
}

function test(id){
  var to_name = TO_NAME;
  var obj = tree.getNode(id);
  var parentId = obj.parentId;
  var deptLocal = encodeURI(obj.name);
  var deptParent = tree.getNode(parentId);
  if(deptParent == null){
    deptParent = '无';
  }
  deptParent = deptParent.name;
  //alert(deptLocal);
  var parentWindowObj = window.parent.dialogArguments;
  var url = '<%=contextPath%>/yh/core/funcs/dept/act/YHDeptAct/selectDept.act?treeId='+id+'&deptLocal='+deptLocal+'&TO_ID='+TO_ID+'&TO_NAME='+TO_NAME;
  window.parent.user.location = url;
  //var deptParent = $('a-' + id).innerHTML;
  //var parentWindowObj = window.dialogArguments;
}
</script>
</head>
<body scroll="no" noresize topmargin="1" leftmargin="0" onload="doInit()">
<div id="deplist"></div>
</body>
</html>