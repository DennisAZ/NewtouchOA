<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
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
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/javascript"><!--
var selectedColor = "rgb(0, 51, 255)";
var DeptId,DeptName;
var deptList = [];
var DeptFlag = null;

function doInit(){
  var url = contextPath + "/yh/core/esb/client/act/YHDeptTreeAct/getTreePermissions.act?id=";
  tree = new DTree({bindToContainerId:'deptTree'
  	            ,requestUrl:url
  	            ,isOnceLoad:false
  	            ,checkboxPara:{isHaveCheckbox:false}
  	            ,linkPara:{clickFunc:treeNodeClick}
  });
  tree.show();
  tree.open("organizationNodeId");

  var parentWindowObj = window.dialogArguments;
  var deptRetNameArray = parentWindowObj["deptRetNameArray"];
  
  if (deptRetNameArray && (deptRetNameArray.length == 2 || deptRetNameArray.length == 3)) {
    var deptCntrl = deptRetNameArray[0];
    var deptDescCntrl = deptRetNameArray[1];
    DeptId = parentWindowObj.$(deptCntrl);
    DeptName = parentWindowObj.$(deptDescCntrl);
    if(deptRetNameArray.length > 2){
      var reciveDeptFlag = deptRetNameArray[2];
      DeptFlag = parentWindowObj.$(reciveDeptFlag);
    }
  }else {
    DeptId = parentWindowObj.$("dept");
    DeptName = parentWindowObj.$("deptDesc");
  }  
  getDefaultDept();
}
function getDefaultDept() {
  url = contextPath + "/yh/core/esb/client/act/YHDeptTreeAct/getDeptsByDeptParent.act?deptId=0";
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    if (json.rtMsrg == "1") {
      $('title').update('全体部门');
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
      document.getElementsByClassName("op")[0].style.display = 'none';
      document.getElementsByClassName("item")[0].style.display = 'none';
      $('deptsDiv').update("<div class='op' align=center style='padding-top:5px'><b>无可选外发部门<b></div>");
    }
 }
}
function treeNodeClick(id){
  url = contextPath + "/yh/core/esb/client/act/YHDeptTreeAct/getDeptsByDeptParent.act";
  //var node = tree.getNode(id);
  //有这部门的权限  //if (node && node.extData) {
  var obj = tree.getNode(id);
  if (id=='organizationNodeId') {
    id = '0';
  }
    var json = getJsonRs(url, "deptId=" + id);
    if(json.rtState == '0'){
      
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
  //}
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
  var div;
  if(dept.deptTelLine == '1'){
    div = new Element('div',{'class':'item','style':'color : orange'}).update(dept.deptName);
  }
  else{
    div = new Element('div',{'class':'item'}).update(dept.deptName);
  }
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
      deptDescStr = DeptName.innerHTML.trim();
    }

    var deptId = this.id.substr(4);
    var deptName = this.innerHTML.trim().split("├")[1];
    if(this.isChecked){
       DeptId.value = getOutofStr(deptStr , deptId);
       if(DeptFlag){
	       DeptFlag.value = getOutofStr(DeptFlag.value , this.style.color == 'orange' ? deptId+'*' : deptId);
       }
       if (DeptName.tagName == 'INPUT') {
         DeptName.value = getOutofStr(deptDescStr , deptName);
       } else {
	       DeptName.innerHTML = getOutofStr(deptDescStr , deptName);
       }

	    this.isChecked = false;
  	  //this.style.backgroundColor = '';
  	  this.className = "item";
    }else{
      if (DeptId.value.length > 0) {
        DeptId.value += "," + deptId;
        if(DeptFlag){
          DeptFlag.value += this.style.color == 'orange' ? "," + deptId + '*' :  "," + deptId;
        }
      }else {
        DeptId.value = deptId;
        if(DeptFlag){
          DeptFlag.value = this.style.color == 'orange' ? deptId + '*' : deptId;
        }
      }
      if (DeptName.tagName == 'INPUT') {
        if(DeptName.value){
          DeptName.value += "," + deptName;
        }else{
          DeptName.value = deptName;
        }
      } else {
        if(DeptName.innerHTML){
          DeptName.innerHTML += "," + deptName;
        }else{
          DeptName.innerHTML = deptName;
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
  var deptFlagStr;
  if(DeptFlag){
    deptFlagStr = DeptFlag.value;
  }
  if (DeptName.tagName == 'INPUT') {
    if(DeptName.value){
      var deptNameStr = DeptName.value;
    }else{
      var deptNameStr = "";
    }
  } else {
    if(DeptName.innerHTML){
      var deptNameStr = DeptName.innerHTML.trim();
    }else{
      var deptNameStr = "";
    }
  }
  if (deptNameStr) {
    deptNameStr += ",";
  }
  if (deptIdStr) {
    deptIdStr += ",";
    if(DeptFlag){
      deptFlagStr += ",";
    }
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
	    if(DeptFlag){
	      deptFlagStr += div.style.color == 'orange' ? deptId + '*,' : deptId+',';
	    }
	    deptNameStr += deptName + ',';
		}
  }
  if (deptIdStr && deptIdStr.lastIndexOf(",") == deptIdStr.length - 1) {
    deptIdStr = deptIdStr.substring(0, deptIdStr.length - 1);
    if(DeptFlag){
      deptFlagStr = deptFlagStr.substring(0, deptFlagStr.length - 1);
    }
  }
  if (deptNameStr && deptNameStr.lastIndexOf(",") == deptNameStr.length - 1) {
    deptNameStr = deptNameStr.substring(0, deptNameStr.length - 1);
  }
  DeptId.value = deptIdStr;
  if(DeptFlag){
    DeptFlag.value = deptFlagStr;
  }
  if (DeptName.tagName == 'INPUT') {
    DeptName.value = deptNameStr;
  } else {
    DeptName.innerHTML = deptNameStr;
  }
}
function disSelectedAll(){
  
  var divs = $('deptsDiv').getElementsByTagName('div');
  var deptIdStr = DeptId.value;
  var deptFlagStr;
  if(DeptFlag){
    deptFlagStr = DeptFlag.value;
  }
  if (DeptName.tagName == 'INPUT') {
    if(!DeptName.value){
      return ;
    }
  } else {
    if(!DeptName.innerHTML){
      return ;
    }
  }
  var deptNameStr = "" ;
  if (DeptName.tagName == 'INPUT') {
    deptNameStr = DeptName.value;
  } else {
    deptNameStr = DeptName.innerHTML.trim();
  }
  
  for(var i = 0 ; i< divs.length ;i++){
    var div = divs[i];
  	if(div.isChecked){
  	  var deptId = div.id.substr(4);
   	  var deptName = div.innerHTML.trim().split("├")[1];
   	  deptIdStr = getOutofStr(deptIdStr , deptId);
   	  if(DeptFlag){
   	    deptFlagStr = getOutofStr(deptFlagStr , div.style.color == 'orange' ? deptId+'*' : deptId);
   	  }
      
   	  deptNameStr = getOutofStr(deptNameStr , deptName);
   	  div.isChecked = false;
   	  div.className = "item";
	}
  }
  DeptId.value = deptIdStr;
  if(DeptFlag){
    DeptFlag.value = deptFlagStr;
  }
  if (DeptName.tagName == 'INPUT') {
    DeptName.value = deptNameStr;
  } else {
    DeptName.innerHTML = deptNameStr;
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
  url = contextPath + "/yh/core/esb/client/act/YHDeptTreeAct/getDeptsByDeptParent.act?deptId=0";
  var json = getJsonRs(url);
   if(json.rtState == "0"){
    $('title').update('全体部门');
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
    DeptName.innerHTML = "全体部门";
  }
  window.close();
  <% }%>
}
function checkAllDept(){
  if(DeptId.value == 0 || DeptId.value == "ALL_DEPT"){
    DeptId.value = "";
    if(DeptFlag){
      DeptFlag.value = "";
    }
    if (DeptName.tagName == 'INPUT') {
      DeptName.value = "";
    } else {
      DeptName.innerHTML = "";
    }
  }
}
--></script>
</head>
<body onload="doInit()">
  <div style="margin : 8px 0px 0px 15px;">提示：<span style="color : orange;">橙色名称为拨号单位。</span></div>
<div id="left" style="padding-top: 2px;width: 360px;height: 440px;">
  <table class="BlockTop" width="100%">
    <tr>
      <td class="left">
      </td>
      <td class="center" >
      选择部门
      </td>
      <td class="right">
      </td>
    </tr>
  </table>
  <div id="deptTree"></div>
  <div style="display:none" >
  <div style="color:red" id=startTime></div>
  <div style="color:red" id=endTime></div>
  <div style="color:red" id=useTime></div>
  </div>
</div>
<div id="right" class="list" style="padding-top: 2px;width: 340px;padding-right: 20px;">
  <table class="TableTop" width="100%">
  <tr>
    <td class="left">
    </td>
    <td class="center">
      <span id="title" >全体部门</span>
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