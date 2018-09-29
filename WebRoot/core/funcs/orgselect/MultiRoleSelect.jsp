<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
String moduleId = request.getParameter("moduleId");
if (moduleId == null) {
  moduleId = "";
}
String privNoFlag = request.getParameter("privNoFlag");
if (privNoFlag == null || "".equals(privNoFlag)) {
  privNoFlag = "0";
}
String privOp = request.getParameter("privOp");
if (privOp == null) {
  privOp = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置角色</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/orgselect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript"><!--
var selectedColor = "rgb(0, 51, 255)";
var RoleId,RoleName;
var moduleId = "<%=moduleId%>";
var privNoFlag = "<%=privNoFlag%>";
var privOp = "<%=privOp%>";
function doInit(){
  var parentWindowObj = window.dialogArguments;

  var roleRetNameArray = parentWindowObj["roleRetNameArray"];
  if (roleRetNameArray && roleRetNameArray.length == 2) {
    var roleCntrl = roleRetNameArray[0];
    var roleDescCntrl = roleRetNameArray[1];
    RoleId = parentWindowObj.$(roleCntrl);
    RoleName = parentWindowObj.$(roleDescCntrl);
  }else {
    RoleId = parentWindowObj.$("role");
    RoleName = parentWindowObj.$("roleDesc");
  }
  
  url = contextPath + "/yh/core/module/org_select/act/YHRoleSelectAct/getRoles.act?&privOp=" + privOp;
  if (moduleId) {
    url += "&moduleId=" + moduleId + "&privNoFlag="+ privNoFlag ;
  }
  var json = getJsonRs(url);
  if(json.rtState == "0"){
	  roleList = json.rtData;
	  addDiv(roleList);
  }
  setSelected(RoleId.value.split(","));
}

function setSelected(selectedDept){
  for(var i = 0 ;i < selectedDept.length ;i++){
    var selectedDiv = $("Div-" + selectedDept[i]);
    if(selectedDiv){
      selectedDiv.isChecked = true;
      selectedDiv.className = "item select";
	  }
  }
}

function addDiv(roles){
  var divs = $("rolesDiv");
  if(roles.length > 0 ){
    for(var i = 0 ; i < roles.length ; i++){
      var dept = roles[i];
    	var div = createDiv(dept);
    	divs.appendChild(div);
    }
  }else{
  	$('hasRole').hide();
	$('noRole').show();
  }
}
function createDiv(role){
  var div = new Element('div',{'class':'item'}).update(role.privName)
  div.id = "Div-" + role.privNo ;
  div.onmouseout = function(){
    if(!this.isChecked){
      this.className = "item";
	  }else {
      this.className = "item select";
    }
  }
  div.onmouseover = function(){
    if(!this.isChecked){
      this.className = "item";
	  }else {
		  this.className = "item select";
	  }
  }
  div.onclick = function(){
    var roleStr = RoleId.value;
	var roleNameStr = RoleName.value;
    if (roleNameStr && roleNameStr.trim) {
      roleNameStr = roleNameStr.trim();
    }

    var roleId = this.id.substr(4);
    var roleName = this.innerHTML.trim();
    if(this.isChecked){
      RoleId.value = getOutofStr(roleStr , roleId);
      RoleName.value = getOutofStr(roleNameStr , roleName);
	    this.isChecked = false;
	    this.className = "item";
    }else{
      if (RoleId.value.length > 0) {
        RoleId.value += "," + roleId;
      }else {
        RoleId.value = roleId;
      }
      if(RoleName.value){
        RoleName.value += "," + roleName;
      }else{
        RoleName.value = roleName;
      }
      this.isChecked = true;
      this.className = "item select";
    }
	
  }
  return div;
}

function selectedAll(){
  var divs = $('rolesDiv').getElementsByTagName('div');
  var roleIdStr = RoleId.value;
  if(RoleName.value){
    var roleNameStr = RoleName.value;
    if (roleNameStr && roleNameStr.trim) {
      roleNameStr = roleNameStr.trim();
    }
  }else{
    var roleNameStr = "";
  }
  if (roleNameStr) {
    roleNameStr += ",";
  }
  if (roleIdStr) {
    roleIdStr += ",";
  }
  for(var i = 0 ;i < divs.length ;i++){
		var div = divs[i];
		if(!div.isChecked){
		  var roleId = div.id.substr(4);
	 	  var roleName = div.innerHTML.trim();
	 	  div.isChecked = true;
      div.className = "item select";
      roleIdStr += roleId + ',';
      roleNameStr += roleName + ',';
		}
  }
  if (roleIdStr && roleIdStr.lastIndexOf(",") == roleIdStr.length - 1) {
    roleIdStr = roleIdStr.substring(0, roleIdStr.length - 1);
  }
  if (roleNameStr && roleNameStr.lastIndexOf(",") == roleNameStr.length - 1) {
    roleNameStr = roleNameStr.substring(0, roleNameStr.length - 1);
  }
  RoleId.value = roleIdStr;
  RoleName.value = roleNameStr;
}
function disSelectedAll(){
  var divs = $('rolesDiv').getElementsByTagName('div');
  var roleIdStr = RoleId.value;
  
  if(!RoleName.value){
    return ;
  }
  var roleNameStr = RoleName.value;
  if (roleNameStr && roleNameStr.trim) {
    roleNameStr = roleNameStr.trim();
  }
  for(var i = 0 ; i< divs.length ;i++){
    var div = divs[i];
  	if(div.isChecked){
  	  var roleId = div.id.substr(4);
   	  var roleName = div.innerHTML.trim();
   	  roleIdStr = getOutofStr(roleIdStr , roleId);
      
   	  roleNameStr = getOutofStr(roleNameStr , roleName);
   	  div.isChecked = false;
    	div.className = "item";
	  }
  }
  RoleId.value = roleIdStr;
  RoleName.value = roleNameStr;
}
function getOutofStr(str, s){
  var aStr = str.split(',');
  var strTmp = "";
  var j = 0 ;//控制重名
  for(var i = 0 ;i < aStr.length ; i++){
    if(aStr[i] && (aStr[i] != s || j != 0)){
      strTmp += aStr[i] + ',';
    }else{
      if(aStr[i] == s){
    	  j = 1;
      }  
    }
  }
  if (strTmp && strTmp.lastIndexOf(",") == strTmp.length - 1) {
    strTmp = strTmp.substring(0, strTmp.length - 1);
  }
  return strTmp;
}
-->
</script>
</head>
<body onload="doInit()">
<div id="rightRole">
<div id="hasRole" class="list">
<table class="TableTop" width="100%">
  <tr>
    <td class="left">
    </td>
    <td class="center" id="title">
      <div id="title" class="header" >选择角色</div>
    </td>
    <td class="right">
    </td>
  </tr>
</table>
<div class="op">
  <input type="button" class="BigButtonB" value="全部添加" onclick="selectedAll()">
  <input type="button" class="BigButtonB" value="全部删除" onclick="disSelectedAll()">
</div>
<div id="rolesDiv">
</div>

</div>
<div id="noRole" align="center" class="item" style="display:none;color:red">未定义角色</div>
</div>
<div style="text-align: center;padding-top: 10px;">
  <input type=button class="SmallButtonW" value="确定" onclick="window.close()"/>
</div>
</body>
</html>