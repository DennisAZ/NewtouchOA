<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String TO_ID = request.getParameter("TO_ID");
  String TO_NAME = request.getParameter("TO_NAME");
  String MODULE_ID = request.getParameter("MODULE_ID");
  String USER_SEQ_ID = request.getParameter("USER_SEQ_ID");
  String USER_DEPT = request.getParameter("USER_DEPT");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树型部门目录</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript">
var TO_ID = "<%=TO_ID%>";
var TO_NAME = "<%=TO_NAME%>";
var MODULE_ID = "<%=MODULE_ID%>";
var USER_SEQ_ID = "<%=USER_SEQ_ID%>";
var USER_DEPT = "<%=USER_DEPT%>";
var tree;
function doInit()
{
  var url = contextPath + "/yh/core/module/dept_select/act/YHDeptSelectAct/getTree.act?DEPT_PAR_ID=";
  tree = new DTree({bindToContainerId:'xtree',
    requestUrl:url,
    isOnceLoad:false,
    //checkboxPara:{isHaveCheckbox:false,disCheckedFun:test,checkedFun:test2},
    linkPara:{clickFunc:getDeptInfo, linkAddress:'index.jsp?id=', target:'user'}
    //treeStructure:{isNoTree:false,regular:'3,2,2,4'}
  });
  tree.show();
	var obj = tree.getFirstNode();
	tree.open(obj.nodeId);
	getDeptInfo(obj.nodeId);
}
function getDeptInfo(id)
{
	var obj = tree.getNode(id);
	var parentId = obj.parentId;
	var deptLocal = encodeURI(obj.name);
	var deptParent = tree.getNode(parentId);
	if(deptParent == null)
	{
		deptParent = '无';
	}
	deptParent = deptParent.name;
	var url = '<%=contextPath%>/yh/core/module/dept_select/act/YHDeptSelectAct/getDept.act?DEPT_PAR_ID=' + id + '&TO_ID=' + TO_ID + '&TO_NAME=' + TO_NAME 
		+ '&MODULE_ID=' + MODULE_ID + '&USER_SEQ_ID=' + USER_SEQ_ID + '&USER_DEPT=' + USER_DEPT;
	self.parent.frames["dept_list"].location = url;
}
</script>
</head>
<body class="" topmargin="1" leftmargin="0" onload="doInit()">
  <div id="xtree" class="moduleContainer treeList">
	</div>
</body>
</html>
