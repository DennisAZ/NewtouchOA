<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String flowId = request.getParameter("flowId"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理权限</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript"><!--
var flowId = '<%=flowId%>';
var UserHi,UserDesc;
var privObj = {};
menuData1 = [{ name:'<div  style="padding-top:5px;margin-left:10px">管理与监控权限(全局)</div>',action:setAction,extData:'MANAGE,管理与监控权限(全局)'}
			,{ name:'<div  style="padding-top:5px;margin-left:10px">管理与监控权限(本部门)</div>',action:setAction,extData:'MANAGE_DEPT,管理与监控权限(本部门)'}
			,{ name:'<div  style="padding-top:5px;margin-left:10px">查询权限(全局)</div>',action:setAction,extData:'QUERY,查询权限(全局)'}
			,{ name:'<div  style="padding-top:5px;margin-left:10px">查询权限(本部门)</div>',action:setAction,extData:'QUERY_DEPT,查询权限(本部门)'}
			,{ name:'<div  style="padding-top:5px;margin-left:10px">编辑权限</div>',action:setAction,extData:'EDIT,编辑权限'}
			,{ name:'<div  style="padding-top:5px;margin-left:10px">点评权限</div>',action:setAction,extData:'COMMENT,点评权限'}
			]
function setAction(){
  //取一下权限
  var name = arguments[2].split(',')[1];
  var act = arguments[2].split(',')[0];
  $('action').value = act;
  $('actionName').update(name);
  var obj = eval("privObj." + act);
  if(act == 'COMMENT'){
    if (obj.commentPriv1 == '1') {
	  $('commentPriv1').checked = true;
    } else {
      $('commentPriv1').checked = false;
    }
    if (obj.commentPriv2 == '1') {
  	  $('commentPriv2').checked = true;
    } else {
      $('commentPriv2').checked = false;
    }
    $('notComment').hide();
    $('comment').show();
  }else{
	setPrivCtrl(obj);
    $('comment').hide();
    $('notComment').show();
  }
}
function setPrivCtrl(privNode) {
  $('privUser').value = privNode.privUser;
  $('privDept').value = privNode.privDept;
  $('role').value = privNode.role;

  $('privUserName').update(privNode.privUserName);
  $('privDeptName').update(privNode.privDeptName);
  $('roleDesc').update(privNode.roleDesc);
}
function showMenu(event){
  var divStyle = {border:'1px solid #69F',width:'200px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:$('showMeunA') , menuData:menuData1 , attachCtrl:true }, divStyle);
  menu.show(event);
}
function clearUser(input, hidden){
  if($(input).tagName == 'INPUT'){
    $(input).value = "";  
  }else if($(input).tagName == 'TEXTAREA'){
    $(input).innerHTML = '';  
  }
  $(hidden).value = "";
}
function setPrivAction() { 
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowManageAct/setPriv.act";
  var json = getJsonRs(url , $('setPrivForm').serialize());
  if (json.rtState == "0") {
	var act = $('action').value;
	//把值回去
	var  str = "";
	if (act == 'COMMENT') {
	  str = "privObj." + act + " = { ";
	  if ($("commentPriv1").checked) {
		str += "commentPriv1:'1'";  
	  } else {
	    str += "commentPriv1:'0'";  
	  }
	  if ($("commentPriv2").checked) {
		str += ",commentPriv2:'1'";  
	  } else {
		str += ",commentPriv2:'0'";  
	  }  
	  str += "}";
	} else {
	  str = "privObj." + act + " = { " 
		+ " privUser:'"+ $('privUser').value +"'  " 
		+ " , privDept:'"+ $('privDept').value +"'  " 
		+ " ,role:'"+ $('role').value +"'  " 
		+ " , privUserName:'"+ $F('privUserName') +"'  " 
		+ " , privDeptName:'"+ $F('privDeptName') +"'  " 
		+ " ,roleDesc:'"+ $F('roleDesc') +"'}" ;
	}
	eval(str);
	var tip = "保存成功！";
  	if (act == "MANAGE") {
	  tip = "管理与监控权限(全局) " + tip;
  	} else if (act == "MANAGE_DEPT") {
	  tip = "管理与监控权限(本部门) " + tip;
  	} else if (act == "QUERY") {
	  tip = "查询权限(全局) " + tip;
  	} else if (act == "QUERY_DEPT") {
	  tip = "查询权限(本部门) " + tip;
  	} else if (act == "EDIT") {
	  tip = "编辑权限  " + tip;
  	} else if (act == "COMMENT") {
	  tip = "点评权限 " + tip;
  	}
  	alert(tip);  
  }
}
function doInit() {
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowManageAct/getPriv.act";
  var json = getJsonRs(url , "flowId=" + flowId);
  if (json.rtState == "0") {
    privObj = json.rtData;
    setPrivCtrl(privObj.MANAGE);
  }
}
</script>
</head>
<body onload="doInit()" style="margin:0px">
<table border="0" width="600px" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="small"><img src="<%=imgPath %>/node_user.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 管理权限设置</span>&nbsp;
  <span style="font-weight:bold;font-size:14px;">
     <img src="<%=imgPath %>/cal_list.gif" align="absmiddle"><a id="showMeunA" href="#" onmouseover="showMenu(event);" hidefocus="true"><span id="actionName">管理与监控权限(全局)</span></a>&nbsp;
    </span>
    </td>
  </tr>
</table>

<form action=""  method="post" name="setPrivForm" id="setPrivForm">
<table width="600px"  class="TableList" align="center" >
<tbody id="notComment">
    <tr>
      <td nowrap class="TableContent"" align="center">授权范围：<br>（人员）</td>
      <td class="TableData">
        <input type="hidden" name="privUser" id="privUser" value="">
        <textarea cols=40 name="privUserName" id="privUserName" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd"  onClick="selectUser(['privUser','privUserName'])">添加</a>
        <a href="javascript:;" class="orgClear" onClick="clearUser('privUserName', 'privUser')">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableContent" align="center">授权范围：<br>（部门）</td>
      <td class="TableData">
        <input type="hidden" name="privDept" id="privDept" value="">
        <textarea cols=40 name="privDeptName" id="privDeptName" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd"  onClick="selectDept(['privDept','privDeptName'])">添加</a>
       <a href="javascript:;" class="orgClear"  onClick="clearUser('privDeptName', 'privDept')">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableContent"" align="center">授权范围：<br>（角色）</td>
      <td class="TableData">
        <input type="hidden" name="role" id="role" value="">
        <textarea cols=40 name="roleDesc" id="roleDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole()">添加</a>
        <a href="javascript:;" class="orgClear" onClick="clearUser('roleDesc', 'role')">清空</a>
      </td>
   </tr>
   </tbody>

<tbody id="comment" style="display:none">
   <tr>
   	  <td nowrap class="TableContent" align="center">允许管理与监控人员点评</td>
      <td class="TableData" align="center">
        <input type="checkbox" name="commentPriv1" value="1" id="commentPriv1"/>
      </td>
   <tr>
      <td nowrap class="TableContent"" align="center">允许查询人员点评</td>
      <td class="TableData" align="center">
         <input type="checkbox" name="commentPriv2" value="1" id="commentPriv2"/>
      </td>
   </tr>
   </tbody>

   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
    <input type="hidden" name="flowId" id="flowId" value="<%=flowId %>">
    <input type="hidden" name="action" id="action" value="MANAGE">
        <input type="button" value="确定" onclick="setPrivAction();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="history.back()">
    </td>
</table>
</form>
</body>
</html>