<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示各种功能</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/menu_left.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/Javascript">	
var loginUserId = <%=loginUser.getSeqId()%>;
var isAdmin = <%=loginUser.isAdminRole()%>;
var postPrivs = <%=loginUser.getPostPriv()%>;
var tree =  "";
var index = "";
var index2 = "";
function doInit(){ 
  var data = {
    	panel:'menuList',
    	data:[{title:'在职人员', action:getTree},{title:'离职人员/外部人员', action:getUserNew}
    	  ]
       };
      var menu = new MenuList(data);
      index = menu.getContainerId(1);
      index2 = menu.getContainerId(2);
      menu.showItem(this,{},1);
      getTree();
}
function getUserNew() {
  $(index2).update("");
  var url = contextPath + "/yh/core/funcs/system/resManage/act/YHResManageAct/openUser.act";
  var json = getJsonRs(url);
  if (json.rtState == "0") {
    var list = json.rtData;
    if (list.length <= 0) {
      return ;
    } 
    var table = "<table class=\"TableList\" width='100%'><tbody id=\"dataList\"></tbody></table>";
    $(index2).update(table);
    for (var i = 0 ;i < list.length ; i++ ) {
      var data = list[i];
      addTr(data);
    }
  }
}
function getTree(){
  $(index).update("");
  tree = new DTree({bindToContainerId:index
    , requestUrl:'<%=contextPath%>/yh/core/module/org_select/act/YHOrgSelectModule/getPersonTree.act?id='
    , isOnceLoad:false
    , treeStructure:{isNoTree:false}
    , linkPara:{clickFunc:getChildOrEdit}
    , isUserModule:true
    , isHaveTitle:true
  });
	tree.show(); 
	tree.open("organizationNodeId");
}
function getChildOrEdit(id){
  var dept = tree.getNode(id);
  var userName = dept.name;
  var userId = dept.extData;
  if(id == "organizationNodeId"){
    return;
  }
  if(id.indexOf('r') == -1){
   parent.userRes.location = "deptRs.jsp?deptId="+ id;
  }else{   
	id = id.substring(1, id.length);
	openUser(id , userName);
  }
}
function openUser(id , userName) {
  parent.userRes.location = "userRs.jsp?seqId="+ id +"&userName="+encodeURI(userName);
}
function addTr(data) {
  var privName = data.privName;
  var userName = data.userName;
  var userId = data.userId;

  var tr = new Element("tr" , {'class' : 'TableData'});
  $("dataList").appendChild(tr);
  td = new Element("td");
  td.align = 'center';
  td.update(privName);
  tr.appendChild(td);

  td = new Element("td");
  td.align = 'center';
  userName = "<a href='javascript:void(0)' onclick='openUser("+userId+" , \""+userName+"\")'>" + userName + "</a>";
  td.update(userName);
  tr.appendChild(td);
}
</script>
</head>
<body onload="doInit()" style="overflow-x:hidden;">
<div id="menuList"></div>
</body>
</html>