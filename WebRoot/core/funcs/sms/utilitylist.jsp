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
function doInit(){ 
  var data = {
      //  fix:true,
    	panel:'menuList',
    	data:[  {title:'未确认提醒', action:getNoConfirm},
    	       {title:'已接受提醒', action:getSend},
    	      {title:'已发送提醒', action:getTree},
          {title:'提醒查询', action:getQuery}
    	  ]
          //{title:'导出RTX格式'}
       };
 
   var menu = new MenuList(data);
   index = menu.getContainerId(3);
   // Ino = menu.getContainerId(2);
   menu.showItem(this,{},3);
   getTree();
   getNoConfirm();
 
}

function getTree(){
 <%--  $(index).update("");
  tree = new DTree({bindToContainerId:index
    , requestUrl:'<%=contextPath%>/yh/core/module/org_select/act/YHOrgSelectModule/getPersonTree.act?id='
    , isOnceLoad:false
    , treeStructure:{isNoTree:false}
    , linkPara:{clickFunc:getChildOrEdit}
    , isUserModule:true
    , isHaveTitle:true
  });
	tree.show(); 
	tree.open("organizationNodeId"); --%>
	  var person = window.parent.contentFrame;
	 person.location =   "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsTestAct/sentSmsList.act?pageNo=0&pageSize=20";
	
}

function getUserNew(){
  var person = window.parent.contentFrame;
  var deptIdStr = 0;
  person.location = "<%=contextPath%>/core/funcs/person/usernew.jsp?deptId="+deptIdStr;
}

function getSend(){
  var person = window.parent.contentFrame;
  person.location = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsTestAct/acceptedSms.act?pageNo=0&pageSize=20";
}

function getNoConfirm(){
	  var person = window.parent.contentFrame;
	  person.location = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsTestAct/notConfirm.act?pageNo=0&pageSize=20";
	}

function getQuery(){
    var person = window.parent.contentFrame;
    person.location ="<%=contextPath %>/core/funcs/sms/querysms.jsp";
  }

function getImport(){
  var flag = "1";
  var person = window.parent.contentFrame;
  person.location = "<%=contextPath%>/core/funcs/person/importPerson.jsp?flag="+flag;
}

function getSet(){
  var person = window.parent.contentFrame;
  person.location = "<%=contextPath%>/core/funcs/person/set.jsp";
}

function getSecureCard(){
	  var person = window.parent.contentFrame;
	  person.location = "<%=contextPath%>/core/funcs/person/secureCard.jsp";
	}

function getChildOrEdit(id){
  var dept = tree.getNode(id);
  var userName = dept.name;
  var userId = dept.extData;
  if(id == "organizationNodeId"){
    return;
  }
  if(id.indexOf('r') == -1){
//	parent.contentFrame.location = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/listInDutyPerson.act?id="+ id;
   var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getPostPrivDept.act";
   var rtJsons = getJsonRs(urls, "deptId="+id);
   if(rtJsons.rtState == '0'){
     if(rtJsons.rtData[0].isPriv){
       parent.contentFrame.location = "<%=contextPath%>/core/funcs/person/usernew.jsp?deptId="+ id;
       }
 //    if(rtJsons.rtData[0].postPriv == "0"){
 //      if(rtJsons.rtData[0].loginDeptId == id){
 //        parent.contentFrame.location = "<%=contextPath%>/core/funcs/person/usernew.jsp?deptId="+ id;
 //      }
 //    }else if(rtJsons.rtData[0].postPriv == "2"){
 //      var postDeptStr = rtJsons.rtData[0].postDept.split(',');
  //     for(var i = 0; i < postDeptStr.length; i++){
  //       if(postDeptStr[i] == id){
 //          parent.contentFrame.location = "<%=contextPath%>/core/funcs/person/usernew.jsp?deptId="+ id;
  //       }
  //     }
  //   }else{
       //parent.contentFrame.location = "<%=contextPath%>/core/funcs/person/usernew.jsp?deptId="+ id;
 //    }
   }
  }else{   
	id = id.substring(1, id.length);
	parent.contentFrame.location = "<%=contextPath%>/core/funcs/person/indutypersoninput.jsp?seqId="+ id +"&userName="+encodeURI(userName)+"&userId="+userId;
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