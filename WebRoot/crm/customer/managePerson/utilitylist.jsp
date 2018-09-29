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
<script type="text/Javascript" src="<%=contextPath%>/core/js/src/cmp/DTree1.0.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/Javascript">	
var loginUserId = <%=loginUser.getSeqId()%>;
var isAdmin = <%=loginUser.isAdminRole()%>;
var postPrivs = <%=loginUser.getPostPriv()%>;
var tree =  "";
var index = "";
function doInit(){ 
  var data = {
        fix:true,
    	panel:'menuList',
    	data:[{title:'在职人员', action:getTree},
          {title:'用户查询', action:getQuery}
    	  ]
       };
   if(postPrivs == "1"){
    data.data.push({title:'离职人员/外部人员', action:getUserNew});
   }

   var menu = new MenuList(data);
   index = menu.getContainerId(1);
   menu.showItem(this,{},1);
   getTree();
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

function getUserNew(){
  var person = window.parent.contentFrame;
  var deptIdStr = 0;
  person.location = "<%=contextPath%>/crm/customer/managePerson/usernew.jsp?deptId="+deptIdStr;
}

function getQuery(){
  var person = window.parent.contentFrame;
  person.location = "<%=contextPath%>/crm/customer/managePerson/query.jsp";
}

function getChildOrEdit(id){
  var dept = tree.getNode(id);
  var userName = dept.name;
  var userId = dept.extData;
  if(id == "organizationNodeId"){
    return;
  }
  if(id.indexOf('r') == -1){
   var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getPostPrivDept.act";
   var rtJsons = getJsonRs(urls, "deptId="+id);
   if(rtJsons.rtState == '0'){
     if(rtJsons.rtData[0].isPriv){
       parent.contentFrame.location = "<%=contextPath%>/crm/customer/managePerson/usernew.jsp?deptId="+ id;
     }
   }
  }else{   
	id = id.substring(1, id.length);
	parent.contentFrame.location  = "<%=contextPath%>/customAction.do?op=toListCustomersByPerson&range=0&cusState=2&seqId="+id+"&userName="+encodeURI(userName);
  }
}
</script>
</head>
<body onload="doInit()" style="overflow-x:hidden;">
<div id="menuList"></div>
 <!--  <table cellscpacing="1" cellpadding="3" width="98%">
  <tr class="TableLine2">
    <td align="center">
      <a href="<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/listOffDutyPerson.act" target="contentFrame" title="离职人员/外部人员"><span>离职人员/外部人员</span></a>
    </td>
  </tr>
</table>-->
</body>
</html>