<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath %>/cmp/tab.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/menu_left.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/javascript">
var deptIndex = "";
var roleIndex = "";
var defaultIndex = "";
var hasRoleData = false;
function getAccord(){
  var data = {
  panel:'left',
  data:[{title:'已选人员', action:getSelectedUser},
       {title:'按部门选择', action:getTree},
       {title:'按角色选择', action:getRole}]
   };
  menu = new MenuList(data);
  deptIndex = menu.getContainerId(2);//取得deptIndex便于后面应用 
  roleIndex = menu.getContainerId(3);//取得roleIndex便于后面应用 
  menu.showItem(this,{},3);//展示第二个菜单项
  getRole();
}
function getSelectedUser(){
  alert("已选人员处理方法");
}
function getTree(){
  var tree = $(deptIndex).tree;//取得树的实例
  //如果为空说明是还没有生成
  //因为第二次展开时还会调用这个函数，这样判断主要是为了防止树重复生成
  if (tree == null){
    var url = contextPath + "/yh/core/funcs/orgselect/act/YHDeptSelectAct/getDeptTree.act?id=";
    var config = {bindToContainerId:deptIndex
        , requestUrl:url
        , isOnceLoad:false
        , isUserModule:true //必须加上此项，处理兼容性问题
      }
    tree = new DTree(config);
    tree.show(); 
  }
}
function getRole(){
  if (!hasRoleData) {
    var url = contextPath + "/yh/core/module/org_select/act/YHRoleSelectAct/getRoles.act";
    var json = getJsonRs(url);
    if(json.rtState == "0"){
      roleList = json.rtData;
      var tabStr = "<table class='TableList' width='100%'><tbody id='roleList'></tbody></table>";
      $(roleIndex).update(tabStr);
      addRole(roleList);
    }
    hasRoleData = true;
  }
}
function addRole(roles) {
  //取得roleIndex面板
  if(roles.length > 0 ){
    for(var i = 0 ; i < roles.length ; i++){
      var role = roles[i];
      var tr = new Element("tr");
      td = new Element("td");
      td.align = 'center';
      td.update(role.privName);
      tr.appendChild(td);
      $('roleList').appendChild(tr);
    }
  }
}
</script>
</head>

<body onload="getAccord()">
<div id="left" style="width:300px"></div>
</body>
</html>