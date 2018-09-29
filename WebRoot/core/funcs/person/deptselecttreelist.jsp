<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树状列出人员表</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/javascript">
var tree = null;
function doInit(){
  tree = new DTree({bindToContainerId:'di'
    ,requestUrl:'<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getGradeTree.act?id='
    ,isOnceLoad:false
    ,treeStructure:{isNoTree:false}
    ,linkPara:{clickFunc:addDeptFunction}
  });
 tree.show(); 
}

function addDeptFunction(deptId){
  var dept = tree.getNode(deptId);
  var deptName = dept.name;
  window.dialogArguments.document.getElementById("postDeptDesc").value  = deptName;
  window.dialogArguments.document.getElementById("postDept").value  = deptId; 
}
</script>
</head>
<body onload="doInit()">
<div id="di"></div>
<div align="center">
  <input type="button" value="关闭窗口" onclick="window.close()"/>
</div>
</body>
</html>