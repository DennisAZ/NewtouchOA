<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>元数据树</title>
<head>
<base target="_self">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/orgselect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/javascript">
var MateId,MateName;
function doInit(){
  var url = contextPath + "/yh/subsys/inforesouce/act/YHMateTreePlugAct/findMates.act?id=";    
  tree = new DTree({bindToContainerId:'left'
  	            ,requestUrl:url
  	            ,isOnceLoad:false
  	            ,checkboxPara:{isHaveCheckbox:false}
  	            ,linkPara:{clickFunc:treeNodeClick}
  });
  tree.show();
  var obj = tree.getFirstNode();

  var parentWindowObj = window.dialogArguments;
  var mateRetNameArray = parentWindowObj["mateRetNameArray"];    
  
  if (mateRetNameArray && mateRetNameArray.length == 2) {
    var mateCntrl = mateRetNameArray[0];
    var mateDescCntrl = mateRetNameArray[1];
    MateId = parentWindowObj.$(mateCntrl);
    MateName = parentWindowObj.$(mateDescCntrl);
  }else {
    MateId = parentWindowObj.$("mate");
    MateName = parentWindowObj.$("mateDesc");
  }  
  getDefMate();
}


function getDefMate() {
  url = contextPath + "/yh/subsys/inforesouce/act/YHMateTreePlugAct/findDefMate.act";  
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    removeDiv();
    var mateList = json.rtData;
    if (mateList.size() >0 ){
   	  addDiv(mateList);
      setSelected(MateId.value.split(','));
    } else {
      $('addAll').hide();
      $('disAll').hide();
      $('mateDiv').update("<div align=center style='padding-top:5px'>请选择数据元</div>");
    }
 }
}

function treeNodeClick(id){
  var url = contextPath + "/yh/subsys/inforesouce/act/YHMateTreePlugAct/getMateIndent.act";     
  var node = tree.getNode(id);  
  var json = getJsonRs(url, "id=" + id);
  if (node && node.extData) {
	  if(json.rtState == '0'){   
	    var obj = tree.getNode(id);
	    $('title').update(obj.name);
	    removeDiv();
	    var mateList = json.rtData;
      if (mateList.size() > 0) {
        $('addAll').show();
        $('disAll').show();
      }
     addDiv(mateList);
     setSelected(MateId.value.split(','));
	  }
  }
}

function removeDiv(){
  var divs = $("mateDiv");
  var divList = divs.getElementsByTagName('div');
  for (i=divList.length-1; i>=0; i--) {
    divs.removeChild(divList[i]);
  }  
}

function addDiv(mate){
  var divs = $("mateDiv");
  if(mate.length > 0){
    for(var i = 0 ; i < mate.length ; i++){
      var mat = mate[i];
  	  var div = createDiv(mat);
  	  divs.appendChild(div);
    }
  }
}

function createDiv(mat){
  var div = new Element('div',{'class':'item'}).update(mat.mateName)
  div.id = "Div-" + mat.mateId ;
  
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
    var deptStr = MateId.value;
    var deptDescStr = "";
    if (MateName.tagName == 'INPUT') {
      deptDescStr =  MateName.value; 
    } else {
      deptDescStr = MateName.innerHTML.trim();
    }

    var deptId = this.id.substr(4);
    var deptName = this.innerHTML.trim().split("├")[1];
    if(this.isChecked){
       MateId.value = getOutofStr(deptStr , deptId);
       if (MateName.tagName == 'INPUT') {
         MateName.value = getOutofStr(deptDescStr , deptName);
       } else {
	    MateName.innerHTML = getOutofStr(deptDescStr , deptName);
       }

	    this.isChecked = false;
  	  //this.style.backgroundColor = '';
  	  this.className = "item";
    }else{
      if (MateId.value.length > 0) {
        MateId.value += "," + deptId;
      }else {
        MateId.value = deptId;
      }
      if (MateName.tagName == 'INPUT') {
        if(MateName.value){
          MateName.value += "," + deptName;
        }else{
          MateName.value = deptName;
        }
      } else {
        if(MateName.innerHTML){
          MateName.innerHTML += "," + deptName;
        }else{
          MateName.innerHTML = deptName;
        }
      }
      this.isChecked = true;
      this.className = "item select";
    }
	
  }
  return div;
}

function selectedAll(){
  var divs = $('mateDiv').getElementsByTagName('div');
  var deptIdStr = MateId.value;
  if (MateName.tagName == 'INPUT') {
    if(MateName.value){
      var deptNameStr = MateName.value;
    }else{
      var deptNameStr = "";
    }
  } else {
    if(MateName.innerHTML){
      var deptNameStr = MateName.innerHTML.trim();
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
  MateId.value = deptIdStr;
  if (MateName.tagName == 'INPUT') {
    MateName.value = deptNameStr;
  } else {
    MateName.innerHTML = deptNameStr;
  }
}
function disSelectedAll(){
  
  var divs = $('mateDiv').getElementsByTagName('div');
  var deptIdStr = MateId.value;
  if (MateName.tagName == 'INPUT') {
    if(!MateName.value){
      return ;
    }
  } else {
    if(!MateName.innerHTML){
      return ;
    }
  }
  var deptNameStr = "" ;
  if (MateName.tagName == 'INPUT') {
    deptNameStr = MateName.value;
  } else {
    deptNameStr = MateName.innerHTML.trim();
  }
  
  for(var i = 0 ; i< divs.length ;i++){
    var div = divs[i];
  	if(div.isChecked){
  	  var deptId = div.id.substr(4);
   	  var deptName = div.innerHTML.trim().split("├")[1];
   	  deptIdStr = getOutofStr(deptIdStr , deptId);
      
   	  deptNameStr = getOutofStr(deptNameStr , deptName);
   	  div.isChecked = false;
   	  div.className = "item";
	}
  }
  MateId.value = deptIdStr;
  if (MateName.tagName == 'INPUT') {
    MateName.value = deptNameStr;
  } else {
    MateName.innerHTML = deptNameStr;
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

function checkAllDept(){
  if(MateId.value == 0 || MateId.value == "ALL_DEPT"){
    MateId.value = "";
    if (MateName.tagName == 'INPUT') {
      MateName.value = "";
    } else {
      MateName.innerHTML = "";
    }
  }
}
</script>
</head>
<body>
	<body onload="doInit();">  
<div id="left"> <div style="" >
 <div style="color:red" id=startTime></div>
   <div style="color:red" id=endTime></div>
   <div style="color:red" id=useTime></div>
   </div></div>
<div id="right">
	<div id="title" class="header" onclick="" >全部数据元</div>
  <div onclick="selectedAll(); return false;" id="addAll"  class="item TableControl">全部添加</div>
  <div onclick="disSelectedAll();return false;" id="disAll"  class="item TableControl">全部删除</div>
  <div id="mateDiv" align="left">
  </div>
</div>
<div style="position:absolute;top:330px;left:245px;height:30px;width:60px;">
  <input type=button class="SmallButtonW" value="确定" onclick="window.close()"/>
</div>
</body>
</body>
</html>