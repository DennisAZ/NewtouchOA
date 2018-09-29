<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
String moduleId = request.getParameter("moduleId");
if (moduleId == null) {
  moduleId = "";
}
String privNoFlag = request.getParameter("privNoFlag");
if (privNoFlag == null) {
  privNoFlag = "";
}
String noAllDept = request.getParameter("noAllDept");
boolean hasAllDept = true;
if (noAllDept != null && !"".equals(noAllDept)) {
  hasAllDept = false;
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置部门</title>
<base target="_self">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/orgselect.css">
<link rel="stylesheet" href = "<%=cssPath %>/menu_left.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/javascript"><!--
var moduleId = "<%=moduleId%>";
var privNoFlag = "<%=privNoFlag%>";
var selectedColor = "rgb(0, 51, 255)";
var DeptId,DeptName;
var deptList = [];
var deptPriv = 1;
var deptIndex = "";
var defaultIndex = "";
var selectGroup = '0';
var moveColor = "#ccc";

function doInit() {
  getAccord();
  //doInit1();
}
function getAccord(){
  var data = {
  panel:'left',
  data:[{title:'选择部门', action:getTree},
       {title:'自定义组', action:getDefaultGroup }]
   };
  menu = new MenuList(data);
  deptIndex = menu.getContainerId(1);
  defaultIndex = menu.getContainerId(2);
  menu.showItem(this,{},1);
  getTree();
}
function getDefaultGroup() {
  $(defaultIndex).update("");
  url = contextPath + "/yh/core/funcs/doc/group/act/YHDeptGroupAct/getDeptGroups.act";
  var json = getJsonRs(url);
  if (json.rtState == '0' ) {
     var pUser = json.rtData;
     if (pUser.length > 0) {
       for(var i = 0 ; i < pUser.length ; i++){
         var tmp = pUser[i];
         $(defaultIndex).appendChild(createGroupDiv(tmp));
       }
     }
   }
}
function createGroupDiv(group) {
  var id = group.seqId;
  var name = group.groupName;
  var div = new Element('div' , {'class' : 'role'}).update(name);
  div.align = 'center';
  if (selectGroup == id) {
    div.isChecked = true;
    div.style.backgroundColor = moveColor;
  }
  div.onmouseout = function(){
    if (!div.isChecked){
      div.style.backgroundColor = '';
    }
  }
  div.onmouseover = function(){
    if (!div.isChecked){
      div.style.backgroundColor = moveColor;
    }
  }
  div.onclick = function(){
    selectGroup = id;
    var divs = $(defaultIndex).getElementsByTagName("div");
    for(var i = 0 ; i < divs.length ; i++){
      var tmp = divs[i];
      tmp.isChecked = false;
      tmp.style.backgroundColor = "";
    }
    div.isChecked = true;
    div.style.backgroundColor = moveColor;
    getUserByGroup(id , name);
  }
  return div;
}
function getUserByGroup(id , name) {
  var url = contextPath + "/yh/core/funcs/doc/group/act/YHDeptGroupAct/getDeptByGroup.act?groupId=" + id;
  var json = getJsonRs(url);
  if (json.rtState == '0') {
    var deptList = json.rtData;
    removeDiv();
    if (deptList.size() >0 ){
   	  addDiv(deptList);
      setSelected(DeptId.value.split(','));
    } else {
      $('addAll').hide();
      $('disAll').hide();
      $('deptsDiv').update("<div align=center style='padding-top:5px'>未定义部门</div>");
    }
  }
}
function getTree(){
  $(deptIndex).update("");
  var url = contextPath + "/yh/core/funcs/orgselect/act/YHDeptSelectAct/getDeptTree.act?privNoFlag=" + privNoFlag;
  if (moduleId) {
    url += "&moduleId=" + moduleId +  "&id=" ;
  } else {
    url += "&id=";
  }
  tree = new DTree({bindToContainerId:deptIndex
  	            ,requestUrl:url
  	            ,isOnceLoad:false
  	            ,checkboxPara:{isHaveCheckbox:false}
  	            ,linkPara:{clickFunc:treeNodeClick}
  });
  tree.show();
  var obj = tree.getFirstNode();

  var parentWindowObj = window.dialogArguments;
  var deptRetNameArray = parentWindowObj["deptRetNameArray"];
  
  if (deptRetNameArray && deptRetNameArray.length == 2) {
    var deptCntrl = deptRetNameArray[0];
    var deptDescCntrl = deptRetNameArray[1];
    DeptId = parentWindowObj.$(deptCntrl);
    DeptName = parentWindowObj.$(deptDescCntrl);
  }else {
    DeptId = parentWindowObj.$("dept");
    DeptName = parentWindowObj.$("deptDesc");
  }  
  getDefaultDept();
}
function getDefaultDept() {
  url = contextPath + "/yh/core/funcs/orgselect/act/YHDeptSelectAct/getDefaultDept.act?privNoFlag=" + privNoFlag;
  if (moduleId) {
    url += "&moduleId=" + moduleId ;
  }
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    if (json.rtMsrg == "1") {
      $('title').update('全体部门');
      $('title').onclick = function() {
        selectedAllDept();
      }
      $('title').className = "clickable";
    } else {
      $('title').hide();
      $('comeBack').hide();
    }
    removeDiv();
    var deptList = json.rtData;
    if (deptList.size() >0 ){
   	  addDiv(deptList);
      setSelected(DeptId.value.split(','));
    } else {
      $('addAll').hide();
      $('disAll').hide();
      $('deptsDiv').update("<div align=center style='padding-top:5px'>请选择部门</div>");
    }
 }
}
function treeNodeClick(id){
  url = contextPath + "/yh/core/funcs/orgselect/act/YHDeptSelectAct/getDeptIndent.act?privNoFlag=" + privNoFlag;
  if (moduleId) {
    url += "&moduleId=" + moduleId  ;
  }
  var node = tree.getNode(id);
  //有这部门的权限  if (node && node.extData) {
    var json = getJsonRs(url, "deptId=" + id);
    if(json.rtState == '0'){
      var obj = tree.getNode(id);
      $('title').update(obj.name);
      if (obj.name != '全体部门') {
        $('title').onclick = function() {
  
        }
        $('title').className = "";
      }
      removeDiv();
      var deptList = json.rtData;
      if (deptList.size() > 0) {
        $('addAll').show();
        $('disAll').show();
      }
      addDiv(deptList);
      setSelected(DeptId.value.split(','));
    }
  }
}

function setSelected(selectedDept){
  if(selectedDept.length > 0 && selectedDept[0] == 0){
    return;
  }
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
    checkAllDept();
   
    var deptStr = DeptId.value;
    var deptDescStr = "";
    if (DeptName.tagName == 'INPUT') {
      deptDescStr =  DeptName.value; 
    } else {
      deptDescStr = DeptName.value;
      if (deptDescStr && deptDescStr.trim) {
        deptDescStr = deptDescStr.trim();
      }
    }

    var deptId = this.id.substr(4);
    var deptName = this.innerHTML.trim();
    if (deptName.indexOf("├") >= 0 ) {
      deptName = deptName.split("├")[1];
    }
    
    if(this.isChecked){
       DeptId.value = getOutofStr(deptStr , deptId);
       if (DeptName.tagName == 'INPUT') {
         DeptName.value = getOutofStr(deptDescStr , deptName);
       } else {
	    DeptName.value = getOutofStr(deptDescStr , deptName);
       }

	    this.isChecked = false;
  	  //this.style.backgroundColor = '';
  	  this.className = "item";
    }else{
      if (DeptId.value.length > 0) {
        DeptId.value += "," + deptId;
      }else {
        DeptId.value = deptId;
      }
      if (DeptName.tagName == 'INPUT') {
        if(DeptName.value){
          DeptName.value += "," + deptName;
        }else{
          DeptName.value = deptName;
        }
      } else {
        if(DeptName.value){
          DeptName.value += "," + deptName;
        }else{
          DeptName.value = deptName;
        }
      }
      this.isChecked = true;
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
  if (DeptName.tagName == 'INPUT') {
    if(DeptName.value){
      var deptNameStr = DeptName.value;
    }else{
      var deptNameStr = "";
    }
  } else {
    if(DeptName.value){
      var deptNameStr = DeptName.value;
      if (deptNameStr && deptNameStr.trim) {
        deptNameStr = deptNameStr.trim();
      }
    }else{
      var deptNameStr = "";
    }
  }
  if (deptNameStr) {
    deptNameStr += ",";
  }
  if (deptIdStr) {
    deptIdStr += ",";
  }
  for(var i = 0 ;i < divs.length ;i++){
	var div = divs[i];
	if(!div.isChecked){
	  var deptId = div.id.substr(4);

 	 var deptName = div.innerHTML.trim();
    if (deptName.indexOf("├") >= 0 ) {
      deptName = deptName.split("├")[1];
    }
    div.isChecked = true;
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
  if (DeptName.tagName == 'INPUT') {
    DeptName.value = deptNameStr;
  } else {
    DeptName.value = deptNameStr;
  }
}
function disSelectedAll(){
  
  var divs = $('deptsDiv').getElementsByTagName('div');
  var deptIdStr = DeptId.value;
  if (DeptName.tagName == 'INPUT') {
    if(!DeptName.value){
      return ;
    }
  } else {
    if(!DeptName.value){
      return ;
    }
  }
  var deptNameStr = "" ;
  if (DeptName.tagName == 'INPUT') {
    deptNameStr = DeptName.value;
  } else {
    deptNameStr = DeptName.value;
    if (deptNameStr && deptNameStr.trim) {
      deptNameStr = deptNameStr.trim();
    }
  }
  
  for(var i = 0 ; i< divs.length ;i++){
    var div = divs[i];
  	if(div.isChecked){
  	  var deptId = div.id.substr(4);
   	  var deptName = div.innerHTML.trim();
      if (deptName.indexOf("├") >= 0 ) {
        deptName = deptName.split("├")[1];
      }
   	  deptIdStr = getOutofStr(deptIdStr , deptId);
      
   	  deptNameStr = getOutofStr(deptNameStr , deptName);
   	  div.isChecked = false;
   	  div.className = "item";
	}
  }
  DeptId.value = deptIdStr;
  if (DeptName.tagName == 'INPUT') {
    DeptName.value = deptNameStr;
  } else {
    DeptName.value = deptNameStr;
  }
  
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
    $('title').onclick = function() {
      
      selectedAllDept();
    }
    $('title').className = "clickable";
    removeDiv();
    var deptList = json.rtData;
    if (deptList.size() > 0 ) {
      $('addAll').show();
      $('disAll').show();
    }
  	addDiv(deptList);
    setSelected(DeptId.value.split(','));
  }
}
function selectedAllDept(){
  <% if (hasAllDept) {%>
  DeptId.value = 0;
  if (DeptName.tagName == 'INPUT') {
    DeptName.value = "全体部门";
  } else {
    DeptName.value = "全体部门";
  }
  window.close();
  <% }%>
}
function checkAllDept(){
  if(DeptId.value == 0 || DeptId.value == "ALL_DEPT"){
    DeptId.value = "";
    if (DeptName.tagName == 'INPUT') {
      DeptName.value = "";
    } else {
      DeptName.value = "";
    }
  }
}
--></script>
</head>
<body onload="doInit()">
  
<div id="left">
</div>
<div id="right" class="list">
  <table class="TableTop" width="100%">
  <tr>
    <td class="left">
    </td>
    <td class="center">
      <span id="title" onclick="selectedAllDept()" class="clickable">全体部门</span>
    </td>
    <td class="right">
    </td>
  </tr>
</table>

<div class="op">
  <input type="button" class="BigButtonB" value="全部添加" onclick="selectedAll()" id="addAll">
  <input type="button" class="BigButtonB" value="全部删除" onclick="disSelectedAll()" id="disAll">
</div>
  <div id="deptsDiv" align="left">
  </div>
  <div class="item"><input type="button" class="BigButtonB" onclick="comeBack()" id="comeBack" value="返回到全部"></input></div>
</div>
<div style="text-align: center; padding-top: 10px; clear: both;">
  <input type=button class="SmallButtonW" value="确定" onclick="window.close()"/>
</div>
</body>
</html>