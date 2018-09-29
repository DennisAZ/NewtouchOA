<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>选择人员</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/orgselect.css">
<link rel="stylesheet" href = "<%=cssPath %>/menu_left.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript">
var selectedColor = "rgb(0, 51, 255)";
var moveColor = "#FF0000";
var UserDesc;
var index = "";
var publicAddress = "";
var selectGroup = '-1';
var deptList = [];
var requestUrl = contextPath + "/yh/core/funcs/address/act/YHAddressAct";
function doInit(){
  var parentWindowObj = window.dialogArguments;
  UserDesc = parentWindowObj.$('outUser');
	var data = {
	        //fix:true,      //默认第一个不能收缩			panel:'left',
			data:[{title:'个人通讯簿', action:getPrivateGroup},
			      {title:'公共通讯簿', action:getDefaultGroup}]
			};
	var menu = new MenuList(data);
	index = menu.getContainerId(1);
	publicAddress = menu.getContainerId(2);
	//loadNameIndex();
	menu.showItem(this,{},1);
	getPrivateGroup();
	//loadData();
}
function loadData(){
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getContactPersonGroup.act";
  var rtJson = getJsonRs(url, null);
  if (rtJson.rtState == "0") {
    var container = document.getElementById(index);
    var table = document.createElement("table");
    var tbody = document.createElement("tbody");
    table.width = "93%";
    var show1tr = document.createElement("tr");
    var show1td = document.createElement("td");
    mouseOverHander(show1td);
    show1td.className = "TableLine2";
    show1td.align = "center";
    //show1td.id = rtJson.rtData[i].seqId;
    show1td.userId = "0";
    show1td.groupFlag = "0";
    show1td.id = "0";
    show1td.groupName = "默认";
    show1td.onclick = function(){
      clickPriv(this);
    }
    show1td.innerHTML ="默认";
    show1tr.appendChild(show1td);
    tbody.appendChild(show1tr);
    table.appendChild(tbody);
    for(var i = 0; i < rtJson.rtData.length; i++) {
      if(rtJson.rtData[i].userId == ""){
        break;
      }
      var show1tr = document.createElement("tr");
      var show1td = document.createElement("td");
      //show1td.onmouseover = function(){
        //this.className = "TableControl";
        //show1td.style.backgroundColor = "#E0E0E0";
      //}
      //show1tr.onmouseout = function(){
        //show1td.style.backgroundColor = "#FFFFFF";
      //}
      mouseOverHander(show1td);
      show1td.className = (i % 2 == 0) ? "TableLine1" : "TableLine2";
      show1tr.style.cursor = "pointer";
      show1td.align = "center";
      show1td.id = rtJson.rtData[i].seqId;
      show1td.userId = rtJson.rtData[i].userId;
      show1td.groupName = rtJson.rtData[i].groupName;
      show1td.onclick = function(){
        clickPriv(this);
      }
      show1td.innerHTML = rtJson.rtData[i].groupName;
      show1tr.appendChild(show1td);
      tbody.appendChild(show1tr);
      table.appendChild(tbody);
      container.appendChild(table);
    }        
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function clickPriv(id , name){
  var seqId = id;
  var userId = name;
  var url = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/getMobileSelect.act";
  var json = getJsonRs(url, "seqId=" + seqId + "&userId="+userId);
  if(json.rtState == "0"){
    var datas = json.rtData;
    addDiv(datas);
    setSelected(UserDesc.value.split(','));
    if(datas == ""){
      $("noGroup").style.display = "";
      $("right").style.display = "none";
      $("showDiv").style.display = "none";
    }else{
      $("deptsDiv").style.display = "";
      $("showDiv").style.display = "none";
      $("noGroup").style.display = "none";
      $("selectAllDiv").style.display = "";
      $("disSelectedAllDiv").style.display = "";
      $("right").style.display = "";
      //setSelected(DeptId.value.split(','));
    }
  }
}

function setSelected(selectedDept){
  for(var i = 0 ;i < selectedDept.length ;i++){
    var selectedDiv = $(selectedDept[i]);
    if(selectedDiv){
      selectedDiv.isSelected = true;
      selectedDiv.className = "item select";
   }
  }
}

function clickNameIndex(id, name){
  var seqId = id;
  var url = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/getPublicMobileSelect.act";
  var json = getJsonRs(url, "seqId=" + seqId);
  if(json.rtState == "0"){
    var datat = json.rtData;
    addDiv(datat);
    if(datat == ""){
      $("noGroup").style.display = "";
      $("right").style.display = "none";
      $("showDiv").style.display = "none";
    }else{
      $("deptsDiv").style.display = "";
      $("showDiv").style.display = "none";
      $("noGroup").style.display = "none";
      $("selectAllDiv").style.display = "";
      $("disSelectedAllDiv").style.display = "";
      $("right").style.display = "";
      //setSelected(DeptId.value.split(','));
    }
  }
}

function createDiv(tmp){
  var userDiv = new Element('div',{'class':'item'}).update(tmp.psnName)
  userDiv.id = tmp.mobilNo;
  userDiv.isSelected = false;
  userDiv.isSelected = false;
  userDiv.onmouseout = function(){
    if(!this.isSelected){
      this.className = "item";
    }else {
      this.className = "item select";
    }
  }
  userDiv.onmouseover = function(){
    if(!this.isSelected){
      this.className = "item hover";
    }else {
      this.className = "item select";
    }
  }
  userDiv.onclick = function(){
    //选 中到不选 中
    if(this.isSelected){
      var mobileNo = this.id;
      var userDescStr = UserDesc.innerHTML.trim();
      UserDesc.innerHTML = getOutofStr(userDescStr , mobileNo);
      this.isSelected = false;
      this.className = "item";
    }else{
      var mobileNo = this.id;
      var userDescStr = UserDesc.innerHTML.trim();
      var str = this.innerHTML.trim();
      if(UserDesc.innerHTML){
        UserDesc.innerHTML += "," + mobileNo;
      }else{
        UserDesc.innerHTML = mobileNo  ;
      }
      this.isSelected = true;
      this.className = "item select";
    }
  }
  return userDiv;
}


function loadNameIndex(){
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getPublicContactPersonGroup.act";
  var rtJson = getJsonRs(url, null);
  if (rtJson.rtState == "0") {
    var container1 = document.getElementById(publicAddress);
    var table = document.createElement("table");
    var tbody = document.createElement("tbody");
    table.width = "93%";
    var show2tr = document.createElement("tr");
    var show2td = document.createElement("td");
    show2td.className = "TableLine2";
    show2td.align = "center";
    //show1td.id = rtJson.rtData[i].seqId;
    show2td.userId = "";
    show2td.groupFlag = "0";
    show2td.id = "0";
    show2td.groupName = "默认";
    show2td.onclick = function(){
      //clickNameIndex(this);
    }
    show2td.innerHTML ="默认";
    show2tr.appendChild(show2td);
    tbody.appendChild(show2tr);
    table.appendChild(tbody);
    for(var i = 0; i < rtJson.rtData.length; i++) {
      var show2tr = document.createElement("tr");
      var show2td = document.createElement("td");
      mouseOverHander(show2td);
      show2td.className = (i % 2 == 0) ? "TableLine1" : "TableLine2";
      show2tr.style.cursor = "pointer";
      show2td.align = "center";
      show2td.id = rtJson.rtData[i].seqId;
      show2td.userId = rtJson.rtData[i].userId;
      show2td.groupName = rtJson.rtData[i].groupName;
      show2td.onclick = function(){
        //clickNameIndex(this);
      }
      show2td.innerHTML = rtJson.rtData[i].groupName;
      show2tr.appendChild(show2td);
      tbody.appendChild(show2tr);
      table.appendChild(tbody);
      container1.appendChild(table);
    }        
  } else {
  	alert(rtJson.rtMsrg); 
  }
}

function manageGroup(){
  var parent = window.parent.addressmain;
  //parent.location = "<%=contextPath%>/core/funcs/address/private/group/index.jsp";
}

function getPrivateGroup(){
  $(index).update("");
  var url = requestUrl + "/getContactPersonGroup.act";
  var singleJson = getJsonRs(url);
  if (singleJson.rtState == '0' ) {
    var singleUser = singleJson.rtData;
    if (singleUser.length > 0) {
      tmp = {seqId:'0', userId:'', groupName:'默认'}
      $(index).appendChild(createPrivateDiv(tmp));
      for(var i = 0 ; i < singleUser.length ; i++){
        var tmp = singleUser[i];
        if(!tmp.userId){
          break;
        }
        $(index).appendChild(createPrivateDiv(tmp));
      }
    }
  }
}

function createPrivateDiv(group) {
  var id = group.seqId;
  var name = group.groupName;
  var userId = group.userId;
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
    var divs = $(index).getElementsByTagName("div");
    for(var i = 0 ; i < divs.length ; i++){
      var tmp = divs[i];
      //不是标题
      if (!tmp.isTitle) {
        tmp.isChecked = false;
        tmp.style.backgroundColor = "";
      }
    }
    div.isChecked = true;
    div.style.backgroundColor = moveColor;
    //getUserByGroup(id , name);
    clickPriv(id , userId);
  }
  return div;
}

function getDefaultGroup() {
  $(publicAddress).update("");
  var url = requestUrl + "/getPublicContactPersonGroup.act";
  var singleJson = getJsonRs(url);
  if (singleJson.rtState == '0' ) {
    var singleUser = singleJson.rtData;
    if (singleUser.length > 0) {
      tmp = {seqId:'0', userId:'', groupName:'默认'}
      $(publicAddress).appendChild(createGroupDiv(tmp));
      for(var i = 0 ; i < singleUser.length ; i++){
        var tmp = singleUser[i];
        $(publicAddress).appendChild(createGroupDiv(tmp));
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
    var divs = $(publicAddress).getElementsByTagName("div");
    for(var i = 0 ; i < divs.length ; i++){
      var tmp = divs[i];
      //不是标题
      if (!tmp.isTitle) {
        tmp.isChecked = false;
        tmp.style.backgroundColor = "";
      }
    }
    div.isChecked = true;
    div.style.backgroundColor = moveColor;
    //getUserByGroup(id , name);
    clickNameIndex(id , name);
  }
  return div;
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

//全选
function selectedAll(){
  var divs = $('deptsDiv').getElementsByTagName('div');
  if(UserDesc.innerHTML){
    var userDescStr =UserDesc.innerHTML.trim();
  }else{
    var userDescStr = "";
  }
  if (userDescStr) {
    userDescStr += ",";
  }
  for(var i = 0;i < divs.length ;i++){
    var div = divs[i];
    if(!div.isSelected){
      var mobilId = div.id;
      div.isSelected = true;
      div.className = "item select";
      userDescStr += mobilId + ',';
    }
  }
  if (userDescStr && userDescStr.lastIndexOf(",") == userDescStr.length - 1) {
    userDescStr = userDescStr.substring(0, userDescStr.length - 1);
  }
  UserDesc.innerHTML = userDescStr;
}

//全部删除
function disSelectedAll(){
  var divs = $('deptsDiv').getElementsByTagName('div');
  if(!UserDesc.innerHTML){
    return ;
  }
  var userDescStr = UserDesc.innerHTML.trim();
  for(var i = 0 ; i< divs.length ;i++){
    var div = divs[i];
    if(div.isSelected){
      var mobilId = div.id;
      userDescStr = getOutofStr(userDescStr , mobilId);
      div.isSelected = false;
      div.className = "item";
    }
  }
  UserDesc.innerHTML = userDescStr;
}

function addDiv(data){
  var divs = $("deptsDiv");
  if(data.length > 0){
    $("deptsDiv").innerHTML = "";
    for(var i = 0 ; i < data.length ; i++){
      var tmp = data[i];
      var div = createDiv(tmp);
      divs.appendChild(div);
    }
  }
}

function removeDiv(){
  var divs = $("deptsDiv");
  var divList = divs.getElementsByTagName('div');
  for (i=divList.length-1; i>=0; i--) {
    divs.removeChild(divList[i]);
  }
}

function mouseOverHander(show1td){
  show1td.onmouseover = function(){
    show1td.style.backgroundColor = "#E0E0E0";
  }
  show1td.onmouseout = function(){
    show1td.style.backgroundColor = "#FFFFFF";
  }
}
</script>
</head>
<body onload="doInit()"  style="padding-right:0px">
<div id="noGroup" style="position:absolute;left:280px;top:30px;display:none;">
  <table class="MessageBox" align="center" width="210"> 
  <tr> 
    <td class="msg blank"> 
      <div class="content" style="font-size:12pt">该分组尚无记录</div> 
    </td> 
  </tr> 
</table>
</div>
 <div id="showDiv" style="position:absolute;left:280px;top:30px;">
    <table class="MessageBox" align="center" width="170"> 
      <tr> 
        <td class="msg blank"> 
          <h4 class="title">提示</h4> 
          <div class="content" style="font-size:12pt">请选择分组</div> 
        </td> 
      </tr> 
     </table>
  </div>
<div id="left">
</div>
<div id="right" style="display:none;">
  <div id="selectAllDiv" style="display:none;" onclick="selectedAll()"  class="item TableControl">全部添加</div>
  <div id="disSelectedAllDiv" style="display:none;" onclick="disSelectedAll()"  class="item TableControl">全部删除</div>
  <div id="deptsDiv" style="display:none;" align="left">
  </div>
</div>
<div style="position:absolute;top:330px;left:245px;height:50px;width:60px;">
  <input type=button class="SmallButtonW" value="关闭" onclick="window.close()"/>
</div>
</body>
</html>