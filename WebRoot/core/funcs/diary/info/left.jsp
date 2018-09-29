<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/menu_left.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<title>Insert title here</title>
</head>
<script type="text/javascript">
var tree =  "";
var index = "";
function doInIt(){
  var data = {
	panel:'menuList',
	notExpand:true,
	data:[{title:'在职人员', action:getTree},
	  {title:'员工日志查询', action:toUserQuery},
      {title:'最新10篇员工日志', action:toLast}]
   };
  var menu = new MenuList(data);
  index = menu.getContainerId(1);
 // Ino = menu.getContainerId(2);
  menu.showItem(this,{},1);
  getTree();
}
function getTree(){
  tree = new DTree({bindToContainerId:index
    , requestUrl:'<%=contextPath%>/yh/core/module/org_select/act/YHOrgSelectModule/getTree.act?MODULE_ID='+ 4 + '&privNoFlag=2&id='
    , isOnceLoad:false
    , treeStructure:{isNoTree:false}
    , linkPara:{clickFunc:toListDiary}
    , isUserModule:true
  });
	tree.show(); 
}
function toLast(){
  var diaryBody = window.parent.diaryBody;
  diaryBody.location = "<%=contextPath%>/core/funcs/diary/info/userDiary.jsp?type=1";
}
function toUserQuery(){
  var diaryBody = window.parent.diaryBody;
  diaryBody.location = "<%=contextPath%>/core/funcs/diary/info/userDiaryQuery.jsp?type=1";
}
</script>
<body onload="doInIt()">
<div id="menuList" style="overflow:auto;height:460px" ></div>
</body>
</html>