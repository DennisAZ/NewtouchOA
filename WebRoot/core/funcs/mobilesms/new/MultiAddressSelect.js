var selectedColor = "rgb(0, 51, 255)";
var moveColor = "#ccc";

var outUser;
var selectGroup = '0';
var index = "";
var publicAddress = "";

var requestUrl = contextPath + "/yh/core/funcs/address/act/YHAddressAct";
function doInit(){
  var parentWindowObj = window.dialogArguments;
  outUser = parentWindowObj.$('outUser');
  var data = {
      panel:'left',
      data:[{title:'个人通讯簿', action:getPrivateGroup},
            {title:'公共通讯簿', action:getDefaultGroup}]
      };
  var menu = new MenuList(data);
  index = menu.getContainerId(1);
  publicAddress = menu.getContainerId(2);
  menu.showItem(this,{},1);
  getPrivateGroup();
}

function getPrivateGroup(){
  $(index).update("");
  var url = requestUrl + "/getContactPersonGroup.act";
  var singleJson = getJsonRs(url);
  if (singleJson.rtState == '0' ) {
    var singleUser = singleJson.rtData;
    tmp = {seqId:'0', userId:loginUserId, groupName:'默认'}
    $(index).appendChild(createPrivateDiv(tmp));
    if (singleUser.length > 0) {
      for(var i = 0 ; i < singleUser.length ; i++){
        singleUser[i].userId = loginUserId;
        $(index).appendChild(createPrivateDiv(singleUser[i]));
        if (i == 0) {
          clickPriv(singleUser[i].seqId , singleUser[i].userId);
        }
      }
    }
  }
}
function clickPriv(id , name){
  var seqId = id;
  var userId = name;
  var url = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/getMobileSelect.act";
  var json = getJsonRs(url, "seqId=" + seqId + "&userId="+userId);
  if (json.rtState == "0") {
    var list = json.rtData;
    addDiv(list);
    setSelected(outUser.innerHTML.trim().split(','))
    if (list.length > 0) {
      $('right').show();
      $('noGroup').hide();
    } else {
      $('right').hide();
      $('noGroup').show();
    }
  }
}
//全选

function selectedAll(){
  var divs = $('deptsDiv').getElementsByTagName('div');
  if(outUser.innerHTML){
    var userDescStr =outUser.innerHTML.trim();
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
  outUser.innerHTML = userDescStr;
}

//全部删除
function disSelectedAll(){
  var divs = $('deptsDiv').getElementsByTagName('div');
  if(!outUser.innerHTML){
    return ;
  }
  var userDescStr = outUser.innerHTML.trim();
  for(var i = 0 ; i< divs.length ;i++){
    var div = divs[i];
    if(div.isSelected){
      var mobilId = div.id;
      userDescStr = getOutofStr(userDescStr , mobilId);
      div.isSelected = false;
      div.className = "item";
    }
  }
  outUser.innerHTML = userDescStr;
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
function setSelected(selectedDept){
  for(var i = 0 ;i < selectedDept.length ;i++){
    var selectedDiv = $(selectedDept[i]);
    if(selectedDiv){
      selectedDiv.isSelected = true;
      selectedDiv.className = "item select";
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
      var userDescStr = outUser.innerHTML.trim();
      outUser.innerHTML = getOutofStr(userDescStr , mobileNo);
      this.isSelected = false;
      this.className = "item";
    }else{
      var mobileNo = this.id;
      var userDescStr = outUser.innerHTML.trim();
      var str = this.innerHTML.trim();
      if(outUser.innerHTML){
        outUser.innerHTML += "," + mobileNo;
      }else{
        outUser.innerHTML = mobileNo  ;
      }
      this.isSelected = true;
      this.className = "item select";
    }
  }
  return userDiv;
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
      tmp.isChecked = false;
      tmp.style.backgroundColor = "";
    }
    div.isChecked = true;
    div.style.backgroundColor = moveColor;
    clickPriv(id , userId);
  }
  return div;
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

function getDefaultGroup() {
  $(publicAddress).update("");
  var url = requestUrl + "/getPublicContactPersonGroup.act";
  var singleJson = getJsonRs(url);
  if (singleJson.rtState == '0' ) {
    var singleUser = singleJson.rtData;
    tmp = {seqId:'0', userId:'', groupName:'默认'}
    $(publicAddress).appendChild(createGroupDiv(tmp));
    if (singleUser.length > 0) {
      for(var i = 0 ; i < singleUser.length ; i++){
        var tmp = singleUser[i];
        $(publicAddress).appendChild(createGroupDiv(tmp));
      }
    }
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
    clickNameIndex(id , name);
  }
  return div;
}
function clickNameIndex(id, name){
  var seqId = id;
  var url = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/getPublicMobileSelect.act";
  var json = getJsonRs(url, "seqId=" + seqId);
  if(json.rtState == "0"){
    var list = json.rtData;
    addDiv(list);
    setSelected(outUser.innerHTML.trim().split(','))
    if (list.length > 0) {
      $('right').show();
      $('noGroup').hide();
    } else {
      $('right').hide();
      $('noGroup').show();
    }
  }
}