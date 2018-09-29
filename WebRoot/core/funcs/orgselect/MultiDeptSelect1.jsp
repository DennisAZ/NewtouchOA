<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置部门</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/orgselect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/javascript"><!--
var selectedColor = "rgb(0, 51, 255)";
var DeptId,DeptName;
var deptList = [];
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
  var deptRetNameArray = parentWindowObj["deptRetNameArray"];
  if (deptRetNameArray && deptRetNameArray.length == 2) {
    var deptCntrl = deptRetNameArray[0];
    var deptDescCntrl = deptRetNameArray[1];
    DeptId = parentWindowObj.$(deptCntrl);
    DeptName = parentWindowObj.$(deptDescCntrl);
  }else {
    DeptId = parentWindowObj.$("apply_to_dept");
    DeptName = parentWindowObj.$("apply_to_dept_name");
  }  
  url = contextPath + "/yh/core/funcs/orgselect/act/YHDeptSelectAct/getDeptIndent.act";
  var json = getJsonRs(url, "deptId=" + obj.nodeId);
  if(json.rtState == "0"){
		deptList = json.rtData;
		addDiv(deptList);
		setSelected(DeptId.value.split(','));
  }
}
function treeNodeClick(id){
  url = contextPath + "/yh/core/funcs/orgselect/act/YHDeptSelectAct/getDeptIndent.act";
  var json = getJsonRs(url, "deptId=" + id);
  if(json.rtState == '0'){
    var obj = tree.getNode(id);
    $('title').update(obj.name);
    removeDiv();
    var deptList = json.rtData;
  	addDiv(deptList);
    setSelected(DeptId.value.split(','));
  }
}

function setSelected(selectedDept){
  for(var i = 0 ;i < selectedDept.length ;i++){
    var selectedDiv = $("Div-" + selectedDept[i]);
    if(selectedDiv){
      selectedDiv.isChecked = true;
      //selectedDiv.style.backgroundColor = selectedColor;
      selectedDiv.className = "item select";
	  }
  }
}

function addDiv(depts){
  var divs = $("deptsDiv");
  if(depts.length > 0){
    for(var i = 0 ; i < depts.length ; i++){
      var dept = depts[i];
  	  var div = createDiv(dept);
  	  divs.appendChild(div);
    }
  }
}
function createDiv(dept){
  var div = new Element('div',{'class':'item'}).update(dept.deptName)
  div.id = "Div-" + dept.deptId ;
  
  div.onmouseout = function(){
    if(!this.isChecked){
      //this.style.backgroundColor = '';
      this.className = "item";
	  }else {
	    this.className = "item select";
	  }
  }
  div.onmouseover = function(){
    if(!this.isChecked){
      //div.style.backgroundColor = '#FFF';
      this.className = "item hover";
    }else {
      this.className = "item select";
    }
  }
  div.onclick = function(){
    var deptStr = DeptId.value;
	var deptDescStr = DeptName.innerHTML.trim();

    var deptId = this.id.substr(4);
    var deptName = this.innerHTML.trim().split("├")[1];
    if(this.isChecked){
      DeptId.value = getOutofStr(deptStr , deptId);
	    DeptName.innerHTML = getOutofStr(deptDescStr , deptName);
	    this.isChecked = false;
  	  //this.style.backgroundColor = '';
  	  this.className = "item";
    }else{
      if (DeptId.value.length > 0) {
        DeptId.value += "," + deptId;
      }else {
        DeptId.value = deptId;
      }
      if(DeptName.innerHTML){
        DeptName.innerHTML += "," + deptName;
      }else{
        DeptName.innerHTML = deptName;
      }
      this.isChecked = true;
      //this.style.backgroundColor = selectedColor;
      this.className = "item select";
    }
	
  }
  return div;
}
function removeDiv(){
  var divs = $("deptsDiv");
  var divList = divs.getElementsByTagName('div');
  for (i=divList.length-1; i>=0; i--) {
    divs.removeChild(divList[i]);
  }
}
function selectedAll(){
  var divs = $('deptsDiv').getElementsByTagName('div');
  var deptIdStr = DeptId.value;
  if(DeptName.innerHTML){
    var deptNameStr = DeptName.innerHTML.trim();
  }else{
    var deptNameStr = "";
  }
  for(var i = 0 ;i < divs.length ;i++){
		var div = divs[i];
		if(!div.isChecked){
		  var deptId = div.id.substr(4);
	 	  var deptName = div.innerHTML.trim().split("├")[1];
	 	  div.isChecked = true;
	    //div.style.backgroundColor = selectedColor;
	    div.className = "item select";  
	    deptIdStr += deptId + ',';
	    deptNameStr += deptName + ',';
		}
  }
  if (deptIdStr && deptIdStr.lastIndexOf(",") == deptIdStr.length - 1) {
    deptIdStr = deptIdStr.substring(0, deptIdStr.length - 1);
  }
  if (deptNameStr && deptNameStr.lastIndexOf(",") == deptNameStr.length - 1) {
    deptNameStr = deptNameStr.substring(0, deptNameStr.length - 1);
  }
  DeptId.value = deptIdStr;
  DeptName.innerHTML = deptNameStr;
}
function disSelectedAll(){
  var divs = $('deptsDiv').getElementsByTagName('div');
  var deptIdStr = DeptId.value;
  
  if(!DeptName.innerHTML){
    return ;
  }
  var deptNameStr = DeptName.innerHTML.trim();
  for(var i = 0 ; i< divs.length ;i++){
    var div = divs[i];
  	if(div.isChecked){
  	  var deptId = div.id.substr(4);
   	  var deptName = div.innerHTML.trim().split("├")[1];
   	  deptIdStr = getOutofStr(deptIdStr , deptId);
      
   	  deptNameStr = getOutofStr(deptNameStr , deptName);
   	  div.isChecked = false;
      //div.style.backgroundColor = "";
   	  div.className = "item";
	  }
  }
  DeptId.value = deptIdStr;
  DeptName.innerHTML = deptNameStr;
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

function comeBack(){
  url = contextPath + "/yh/core/funcs/orgselect/act/YHDeptSelectAct/getDeptIndent.act";
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    $('title').update('全体部门');
    removeDiv();
    var deptList = json.rtData;
  	addDiv(deptList);
    setSelected(DeptId.value.split(','));
  }
}

--></script>
</head>
<body onload="doInit()">
<div id="left"></div>
<div id="right">
  <div id="title" class="item">全体部门</div>
  <div onclick="selectedAll()"  class="item TableControl">全部添加</div>
  <div onclick="disSelectedAll()"  class="item TableControl">全部删除</div>
  <div id="deptsDiv" align="left">
  </div>
  <div class="item" onclick="comeBack()"  onmouseout="this.style.backgroundColor=''" onmouseover="this.style.backgroundColor='#FFF'">&lt&lt返回到全部</div>
</div>
<div style="position:absolute;top:350px;left:245px;height:50px;width:60px;">
  <input type=button class="SmallButtonW" value="关闭" onclick="window.close()"/>
</div>
</body>
</html>