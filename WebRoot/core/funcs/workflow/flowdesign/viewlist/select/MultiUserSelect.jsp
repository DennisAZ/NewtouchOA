<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择人员</title>
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/javascript">
var selectedColor = "rgb(0, 51, 255)";
var moveColor = "#FFF";
var User,UserDesc;
function doInit(){
  var url =  contextPath + "/yh/core/funcs/dept/act/YHDeptTreeAct/getTree.act?id=";
  tree = new DTree({bindToContainerId:'left'
                ,requestUrl:url
                ,isOnceLoad:false
                ,checkboxPara:{isHaveCheckbox:false}
                ,linkPara:{clickFunc:treeNodeClick}
  });
  tree.show();
  var obj = tree.getFirstNode();
  tree.open(obj.nodeId); 

  var parentWindowObj = window.dialogArguments;
  User = parentWindowObj.UserHi;
  UserDesc = parentWindowObj.UserDesc;
  seqId = parentWindowObj.seqId;
  //判断有没有userId
  if(User.value){
    $('title').update('已选人员');
    
    var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowProcessAct/getUsers.act";

    var json = getJsonRs(url, "seqId=" + seqId);
    if(json.rtState != "0"){
	  return ;
    }
    var users = json.rtData;
      //[{userId:'liuhan1',userName:'liuhan1'},{userId:'liuhan',userName:'liuhan2'},{userId:'liuhan3',userName:'liuhan3'},{userId:'liuhan5',userName:'liuhan7'}];
    addDiv(users);
    setSelected(User.value.split(','));
  }else{
  //默认取第一个节点的人员
    $('title').update(obj.name);
    treeNodeClick(obj.nodeId);
  } 
}
function treeNodeClick(id){
  var url = contextPath  + "/yh/core/funcs/workflow/act/YHFlowProcessAct/getPersonsByDept.act"
  var json = getJsonRs(url , 'deptId=' + id);
  if(json.rtState == '0'){
    var obj = tree.getNode(id);
    $('title').update(obj.name);
    removeDiv();
    var users = json.rtData;
    addDiv(users);
    setSelected(User.value.split(','));
  }
}
function removeDiv(){
  var div = $("persons");
  var divList = divs.getElementsByTagName('div');
  for(i = divList.length-1; i>=0; i--) {
    div.removeChild(divList[i]);
  }
}
function setSelected(selectedUser){
  for(var i = 0 ;i < selectedUser.length ;i++){
    var selectedDiv = $("Div-" + selectedUser[i]);
    if(selectedDiv){
      selectedDiv.isSelected = true;
      selectedDiv.style.backgroundColor = selectedColor;
    }
  }
}
function setSingleSelected(userId){
   
}
function setDisSelected(){
  
}
function addDiv(users){
  var divs = $("persons");
  if(users.length > 0){
    $('hasUser').show()
    $('noUser').hide()
    for(var i = 0 ; i < users.length ; i++){
      var user = users[i];
      var div = createDiv(user);
      divs.appendChild(div);
    }
  }else{
    $('hasUser').hide()
    $('noUser').show()
  } 
}
function createDiv(user){
  var userDiv = new Element('div',{'class':'item'}).update(user.userName)
  userDiv.id = "Div-" + user.userId ;
  userDiv.isSelected = false;
  userDiv.onmouseout = function(){
    if(!userDiv.isSelected){
      userDiv.style.backgroundColor = '';
    }
  }
  userDiv.onmouseover = function(){
    if(!userDiv.isSelected){
      userDiv.style.backgroundColor = moveColor;
    }
  }
  userDiv.onclick = function(){
    if(this.isSelected){
      var userStr = User.value;
      var userDescStr = UserDesc.innerHTML.trim();

      User.value = getOutofStr(userStr , this.id.substr(4));
      UserDesc.innerHTML = getOutofStr(userDescStr , this.innerHTML.trim());
      this.isSelected = false;
      this.style.backgroundColor = '';
      
    }else{
      var userStr = User.value;
      var userDescStr = UserDesc.innerHTML.trim();
      var userName = this.innerHTML.trim();
      User.value += this.id.substr(4) + ",";
      if(UserDesc.innerHTML){
        UserDesc.innerHTML += userName + ',';
      }else{
        UserDesc.innerHTML = userName + ',';
      }
      this.isSelected = true;
      this.style.backgroundColor = selectedColor;
    }
  
  }
  return userDiv;
}
function removeDiv(){
  var divs = $("persons");
  var divList = divs.getElementsByTagName('div');
  for (i=divList.length-1; i>=0; i--) {
    divs.removeChild(divList[i]);
  }
}
function selectedAll(){
  var divs = $('persons').getElementsByTagName('div');
  var userStr = User.value;
  if(UserDesc.innerHTML){
    var userDescStr = UserDesc.innerHTML.trim();
  }else{
    var userDescStr = "";
  }
  
  for(var i = 0 ;i < divs.length ;i++){
    var div = divs[i];
    if(!div.isSelected){
      var userId = div.id.substr(4);
      var userName = div.innerHTML.trim();
      div.isSelected = true;
      div.style.backgroundColor = selectedColor;
      userStr += userId + ',';
      userDescStr += userName + ',';
    }
  }
  User.value = userStr;
  UserDesc.innerHTML = userDescStr;
}
function disSelectedAll(){
  var divs = $('persons').getElementsByTagName('div');
  var userStr = User.value;
  if(!UserDesc.innerHTML){
    return ;
  }
  var userDescStr = UserDesc.innerHTML.trim();
  for(var i = 0 ; i< divs.length ;i++){
    var div = divs[i];
    if(div.isSelected){
      var userId = div.id.substr(4);
      var userName = div.innerHTML.trim();
      userStr = getOutofStr(userStr , userId);
      
      userDescStr = getOutofStr(userDescStr , userName);
      div.isSelected = false;
      div.style.backgroundColor = "";
    }
  }
  User.value = userStr;
  UserDesc.innerHTML = userDescStr;
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
  return strTmp;
}
String.prototype.trim = function(){
  return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.ltrim = function(){
  return this.replace(/(^\s*)/g,"");
}
String.prototype.rtrim = function(){
  return this.replace(/(\s*$)/g,"");
}
</script>
<style>
#left{
	width: 200px;
	float: left;
	border: 1px solid #669;
}
#right{
	float: left;
	width: 250px;
	border: 1px solid #669;
	margin-left: 4px;
	text-align: center;
	font-size: 12px;
}
#title{
	font-size:12pt;
	color: #0a4fff;
	background-color: #d4f0ff;
padding-top:4px;
	padding-bottom:4px;	
}
.item{
	border-bottom-width: 1px;
	border-bottom-style: solid;
	border-bottom-color: #669;
padding-top:4px;
	padding-bottom:4px;		
}
.operate{
	  border-bottom-width: 1px;
	  border-bottom-style: solid;
	  border-bottom-color: #669;	
}
.userHost{
	width: 100px;
	text-align: center;
	float: left;
	border-right-width: 1px;
	border-right-style: solid;
	border-right-color: #669;	
}
.userOper{
   	width: 149px;
	text-align: center;
	
}
</style>
</head>
<body onload="doInit()">
<div id="left"></div>
<div id="right">
<div id="title" class="item">已选择人员</div>
<div id="hasUser">
<div onclick="selectedAll()"  class="item" onmouseout="this.style.backgroundColor=''" onmouseover="this.style.backgroundColor='#FFF'">全部添加</div>
<div onclick="disSelectedAll()"  class="item" onmouseout="this.style.backgroundColor=''" onmouseover="this.style.backgroundColor='#FFF'">全部删除</div>
<div id="persons">

</div>

</div>
<div id="noUser" style="display:none;color:red">未定义用户</div>
<div align="center"><input type=button value="确定" onclick="window.close()"/></div>

</div>
</body>
</html>