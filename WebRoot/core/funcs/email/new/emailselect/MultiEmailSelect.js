var selectedColor = "rgb(0, 51, 255)";
var moveColor = "#ccc";

var outUser;
var selectGroup = '0';
var selectRole = '0';
var selectBook = '0';
var selectPrivateBook = '0';
var index = "";
var publicAddress = "";
var deptIndex = "";
var roleIndex = "";
var defaultIndex = "";
var ii = /&lt;.*@.*&gt;/;

var requestUrl = contextPath + "/yh/core/funcs/address/act/YHAddressAct";
var requestUrl2 = contextPath + "/yh/core/funcs/address/act/YHEmailSelectAct";
function getAccord(){
  var data = {
  panel:'left',
  data:[{title:'已选邮箱', action:getSelectedUser},
       {title:'按部门选择', action:getTree},
       {title:'按角色选择', action:getRole},
       {title:'自定义组', action:getDefaultGroup },
       {title:'在线人员', action:getOnline},
       {title:'个人通讯簿', action:getPrivateBook},
       {title:'公共通讯簿', action:getPublicBook}]
   };
  menu = new MenuList(data);
  deptIndex = menu.getContainerId(2);
  roleIndex = menu.getContainerId(3);
  defaultIndex = menu.getContainerId(4);
  index = menu.getContainerId(6);
  publicAddress = menu.getContainerId(7);
  menu.showItem(this,{},2);
  getTree();
}
function getTree(){
  $(deptIndex).update("");
  var url = contextPath + "/yh/core/funcs/orgselect/act/YHDeptSelectAct/getDeptTree.act?";
  url += "id=";
  var config = {bindToContainerId:deptIndex
      , requestUrl:url
      , isOnceLoad:false
      , linkPara:{clickFunc:treeNodeClick}
      , isUserModule:true
    }
  tree = new DTree(config);
  tree.show(); 
}
function doInit(){
  var parentWindowObj = window.dialogArguments;
  
  window.onunload = function() {
    parentWindowObj.addUnloadEvt && parentWindowObj.addUnloadEvt();
  }
  outUser = parentWindowObj.$(parentWindowObj.ctrlConst);
  
  getAccord();
  if (!outUser.innerHTML.trim()) {
    getDefaultUser();
  }else {
    getSelectedUser();
  }
}
function getDefaultUser() {
  $('right').update("");
  var url = requestUrl2 + "/getDefaultEmail.act";
  var json = getJsonRs(url);
  tip = "未定义邮箱";
  if (json.rtState == '0') {
    var us = json.rtData ;
    var name = json.rtMsrg;
    if (us.length > 0) {
      getRightPal(name , true , true , 0 , tip);
      addUserDiv(us , 0);
      setSelected(outUser.innerHTML.trim().split(','));
    } else {
      getRightPal(name , true , false , 0 ,tip );
    }
  }
}
function treeNodeClick(id){
  var url = requestUrl2  + "/getEmailsByDept.act"
  var node = tree.getNode(id);
  //有这部门的权限
  if (node && node.extData) {
    var json = getJsonRs(url , 'deptId=' + id);
    if(json.rtState == '0'){
      $("right").update("");
      var users = json.rtData;
      var name = json.rtMsrg;
      if (users.length > 0) {
        getRightPal(name , true , true , 0 );
        addUserDiv(users , 0);
        setSelected(outUser.innerHTML.trim().split(','));
      } else {
        getRightPal(name , true , false , 0 );
      }
    }
  }
}
function getRole(){
  $(roleIndex).update("");
  var url = contextPath + "/yh/core/module/org_select/act/YHRoleSelectAct/getRoles.act";
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    roleList = json.rtData;
    addRole(roleList);
  }
}
function addRole(roles) {
  var divs = $(roleIndex);
  if(roles.length > 0 ){
    for(var i = 0 ; i < roles.length ; i++){
      var role = roles[i];
      var div = createRoleDiv(role);
      divs.appendChild(div);
    }
  }
}
function createRoleDiv(role) {
  var div = new Element('div' , {'class' : 'role'}).update(role.privName);
  div.align = 'center';
  if (selectRole == role.privNo) {
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
    selectRole = role.privNo;
    var divs = $(roleIndex).getElementsByTagName("div");
    for(var i = 0 ; i < divs.length ; i++){
      var tmp = divs[i];
      tmp.isChecked = false;
      tmp.style.backgroundColor = "";
    }
    div.isChecked = true;
    div.style.backgroundColor = moveColor;
    getUserByRoleId(role.privNo , role.privName);
  }
  return div;
}

function getUserByRoleId(roleId , privName){
  $('right').update("");
  var url = requestUrl2 + "/getEmailByRole.act";
  var json = getJsonRs(url , "roleId=" + roleId );
  if (json.rtState == '0') {
    var us = json.rtData.principalRole ;
    roleUser = us;
    if (us.length > 0) {
      getRightPal(privName , true , true , 0);
      addUserDiv(us , 0);
      setSelected(outUser.innerHTML.trim().split(','));
    } else {
      getRightPal(privName , true , false , 0  );
    }
    //辅助角色,
    us = json.rtData.supplementRole;
    //判断辅助角色中的人有没有在主角色里,如果在则踢出
    us2 = new Array();
    for (var i = 0 ;i < us.length ; i ++) {
      var tmp = us[i];
      var flag = false;
      for (var j = 0 ;j < roleUser.length ; j ++) {
        var tmp2 = roleUser[j];
        if (tmp2.email == tmp.email) {
          flag = true;
        }
      }
      if (!flag) {
        us2.push(tmp);
      }
    }
    if (us2.length > 0) {
      getRightPal("辅助角色" , true , true , 1);
      addUserDiv(us2 , 1);
      setSelected(outUser.innerHTML.trim().split(','));
    }
  }
}
function getDefaultGroup() {
  $(defaultIndex).update("");
  var url = requestUrl2 + "/getUserGroup.act";
  var singleJson = getJsonRs(url);
  if (singleJson.rtState == '0' ) {
    var singleUser = singleJson.rtData;
    if (singleUser.length > 0) {
      var div = new Element("div").update("个人自定义组");
      div.isTitle = true;
      div.className = 'item TableControl';
      div.style.fontWeight = 'bold';
      div.style.fontSize = '10pt';
      div.align = 'center';
      $(defaultIndex).appendChild(div);
      for(var i = 0 ; i < singleUser.length ; i++){
        var tmp = singleUser[i];
        $(defaultIndex).appendChild(createGroupDiv(tmp));
      }
    }
  }
    
   url = requestUrl2 + "/getUserGroup.act?isPublic=true";
   var json = getJsonRs(url);
   if (json.rtState == '0' ) {
      var pUser = json.rtData;
      if (pUser.length > 0) {
        var div = new Element("div").update("公共自定义组");
        div.isTitle = true;
        div.className = 'item TableControl';
        div.style.fontWeight = 'bold';
        div.style.fontSize = '10pt';
        div.align = 'center';
        $(defaultIndex).appendChild(div);
        for(var i = 0 ; i < pUser.length ; i++){
          var tmp = pUser[i];
          $(defaultIndex).appendChild(createGroupDiv(tmp));
        }
      }
    }
}
function getOnline() {
  $('right').update("");
  var url = requestUrl2 + "/getEmailByOnline.act";
  var json = getJsonRs(url);
  if (json.rtState == '0') {
    var us = json.rtData ;
    if (us.length > 0) {
      getRightPal("全部在线人员邮箱" , true , true , 0);
      addUserDiv(us , 0);
      setSelected(outUser.innerHTML.trim().split(','));
    } else {
      getRightPal("全部在线人员邮箱" , true , false , 0 );
    }
  }
}
function getSelectedUser() {
  $('right').update("");
  tip = "暂未选择邮箱";
  var us = parseSelectedUsers(outUser.innerHTML.trim());
  if (us.length > 0) {
    getRightPal('已选邮箱' , true , true , 0 , tip);
    addUserDiv(us , 0);
    setSelected(outUser.innerHTML.trim().split(','));
  } else {
    getRightPal('已选邮箱' , true , false , 0 , tip);
  }
}
/**
* 解析已经选中的人员


* @userIdStr       人员ID字符串


* @userNameStr     人员名称字符串


*/
function parseSelectedUsers(userIdStr) {
 var rtArray = [];
 var sUserState = "";
 var stateArray = [];
 if (!userIdStr) {
   return rtArray;
 }
 var idArray = userIdStr.split(",");
 
 for (var i = 0; i < idArray.length; i++) {
   var str = idArray[i];
   if (str) {
     var name = "";
     var em = "";
     if (str.match(ii)) {
       var b = str.indexOf("&lt;");
       name = str.substring(0 , b);
       em = str.substring(b+4 , str.length - 4);
       rtArray.add({ userName: name , email : em});
     } else {
       rtArray.add({ userName: "" , email : idArray[i]});
     }
   }
 }
 return rtArray;
}
function getPrivateBook(){
  $(index).update("");
  var url = requestUrl + "/getContactPersonGroup.act";
  var singleJson = getJsonRs(url);
  if (singleJson.rtState == '0' ) {
    var singleUser = singleJson.rtData;
    tmp = {seqId:'0', userId:loginUserId, groupName:'默认'};
    var div = createPrivateDiv(tmp);
    $(index).appendChild(div);
    if (singleUser.length > 0) {
      for(var i = 0 ; i < singleUser.length ; i++){
        singleUser[i].userId = loginUserId;
        $(index).appendChild(createPrivateDiv(singleUser[i]));
        if (i == 0) {
          clickPriv(singleUser[i].seqId , singleUser[i].userId , singleUser[i].groupName);
        }
      }
    }
  }
}
function clickPriv(id , name , bookName){
  $('right').update("");
  var seqId = id;
  var userId = name;
  var url = contextPath + "/yh/core/funcs/email/act/YHEmailSelectAct/getMobileSelect.act";
  var json = getJsonRs(url, "seqId=" + seqId + "&userId="+userId);
  if (json.rtState == "0") {
    var us = json.rtData;
    roleUser = us;
    if (us.length > 0) {
      getRightPal(bookName , true , true , 0);
      addUserDiv(us , 0);
      setSelected(outUser.innerHTML.trim().split(','));
    } else {
      getRightPal(bookName , true , false , 0  );
    }
  }
}
//全选function selectedAll(i){
  var divs = new Array();
  if (i != undefined) {
    divs = $('list-' + i).getElementsByTagName('div');
  } else {
    var divs1 = $('right').getElementsByTagName('div');
    for (var i = 0 ;i < divs1.length ;i ++) {
      var tmp = divs1[i];
      if ( tmp.id 
          && tmp.id.indexOf('list-') != -1) {
        var div2 = tmp.getElementsByTagName('div');
        for(var j = 0 ; j < div2.length ;j ++) {
          divs.push(div2[j]);
        }
      }
    }
  }
  var userStr = outUser.innerHTML.trim();
  if (userStr) {
    userStr += ",";
  }
  for(var i = 0;i < divs.length ;i++){
    var div = divs[i];
    if(!div.isSelected){
      var userId = div.id.substr(4);
      var str = div.innerHTML.trim();
      div.isSelected = true;
      div.className = "item select";
      userStr += str + ',';
    }
  }
  if (userStr && userStr.lastIndexOf(",") == userStr.length - 1) {
    userStr = userStr.substring(0, userStr.length - 1);
  }
  outUser.innerHTML= userStr;
}

function disSelectedAll(i){
  var divs = new Array();
  if (i != undefined) {
    divs = $('list-' + i).getElementsByTagName('div');
  } else {
    var divs1 = $('right').getElementsByTagName('div');
    for (var i = 0 ;i < divs1.length ;i ++) {
      var tmp = divs1[i];
      if ( tmp.id 
          && tmp.id.indexOf('list-') != -1) {
        var div2 = tmp.getElementsByTagName('div');
        for(var j = 0 ; j < div2.length ;j ++) {
          divs.push(div2[j]);
        }
      }
    }
  }
  var userStr = outUser.innerHTML.trim();
  
  for(var i = 0 ; i< divs.length ;i++){
    var div = divs[i];
    if(div.isSelected){
      var userId = div.id.substr(4);
      var str = div.innerHTML.trim();
      userStr = getOutofStr(userStr , userId);
      userStr = getOutofStr(userStr , str);
      div.isSelected = false;
      div.className = "item";
    }
  }
  outUser.innerHTML= userStr;
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

function createDiv(tmp){
  var str ="";
  if (tmp.userName) {
    str = tmp.userName + "&lt;"+ tmp.email + "&gt;";
  } else {
    str = tmp.email;
  }
  var userDiv = new Element('div',{'class':'item'}).update(str)
  userDiv.id = "Div-" + tmp.email;
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
    //选中到不选 中    if(this.isSelected){
      var email = this.id.substring("Div-".length);
      var userDescStr = outUser.innerHTML.trim();
      var str = this.innerHTML.trim();
      var str1 = getOutofStr(userDescStr , email);
      str1 = getOutofStr(str1 , str);
      outUser.innerHTML = str1 
      this.isSelected = false;
      this.className = "item";
    }else{
      var email =  this.id.substring("Div-".length);
      var userDescStr = outUser.innerHTML.trim();
      var str = this.innerHTML.trim();
      if(outUser.innerHTML.trim()){
        outUser.innerHTML += "," + str;
      }else{
        outUser.innerHTML = str  ;
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
  if (selectPrivateBook == id) {
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
    selectPrivateBook = id;
    var divs = $(index).getElementsByTagName("div");
    for(var i = 0 ; i < divs.length ; i++){
      var tmp = divs[i];
      tmp.isChecked = false;
      tmp.style.backgroundColor = "";
    }
    div.isChecked = true;
    div.style.backgroundColor = moveColor;
    clickPriv(id , userId , group.groupName);
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
    getUserByGroup(id , name);
  }
  return div;
}
function getUserByGroup(id , name) {
  $('right').update("");
  var url = requestUrl2 + "/getEmailByGroup.act?groupId=" + id;
  var json = getJsonRs(url);
  if (json.rtState == '0') {
    var us = json.rtData ;
    if (us.length > 0) {
      getRightPal(name , true , true , 0 , "尚未添加邮箱");
      addUserDiv(us , 0);
      setSelected(outUser.innerHTML.trim().split(','));
    } else {
      getRightPal(name , true , false , 0 , "尚未添加邮箱");
    }
  }
}
function getPublicBook() {
  $(publicAddress).update("");
  var url = requestUrl + "/getPublicContactPersonGroup.act";
  var singleJson = getJsonRs(url);
  if (singleJson.rtState == '0' ) {
    var singleUser = singleJson.rtData;
    tmp = {seqId:'0', userId:'', groupName:'默认'}
    $(publicAddress).appendChild(createPublicDiv(tmp));
    if (singleUser.length > 0) {
      for(var i = 0 ; i < singleUser.length ; i++){
        var tmp = singleUser[i];
        $(publicAddress).appendChild(createPublicDiv(tmp));
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
        if (aStr[i].indexOf("&lt;" + s + "&gt;") == -1) {
          strTmp += aStr[i] + ',';
        }
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


function createPublicDiv(group) {
  var id = group.seqId;
  var name = group.groupName;
  var div = new Element('div' , {'class' : 'role'}).update(name);
  div.align = 'center';
  if (selectBook == id) {
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
    selectBook = id;
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
    clickNameIndex(id , name , group.groupName);
  }
  return div;
}
function clickNameIndex(id, name ,bookName){
  $('right').update("");
  var seqId = id;
  var url = contextPath + "/yh/core/funcs/email/act/YHEmailSelectAct/getPublicMobileSelect.act";
  var json = getJsonRs(url, "seqId=" + seqId);
  if(json.rtState == "0"){
    var us = json.rtData;
    roleUser = us;
    if (us.length > 0) {
      getRightPal(bookName , true , true , 0);
      addUserDiv(us , 0);
      setSelected(outUser.innerHTML.trim().split(','));
    } else {
      getRightPal(bookName , true , false , 0  );
    }
  }
}
function getRightPal(title , isHaveAll , hasData , i , tip){
  var right = $('right');
  var tDiv;
  if (i == 0) {
    tDiv = new Element("table", {
      "class": "TableTop",
      "width": "100%"
    });
    
    var tr = tDiv.insertRow(0);
    var tdl = tr.insertCell(0);
    var td = tr.insertCell(1);
    var tdr = tr.insertCell(2);
    tdl.className = "left";
    td.className = "center";
    tdr.className = "right";
    td.innerHTML = title;
  }
  else {
    tDiv = new Element("div" , {'class' : 'header'}).update(title);
  }
  tDiv.id = "title" + i;
  right.appendChild(tDiv);
  if (hasData) {
    var hasData = new Element("div", {"class": "list"});
    hasData.id = "hasData-" + i;  
    right.appendChild(hasData);
    if (isHaveAll) {
      var addAll = new Element("input" , {type: "button", 'class' : 'BigButtonB', value: "全部添加"});
      addAll.onclick = function () {
        selectedAll(i);
      }
      var disAll = new Element("input" , {type: "button", 'class' : 'BigButtonB', value: "全部删除"});
      disAll.onclick = function () {
        disSelectedAll(i);
      }
      var d = new Element("div", {"class": "op"});
      hasData.appendChild(d.insert(addAll).insert(disAll));
    }
    var list = new Element("div");
    list.id = "list-" + i;
    hasData.appendChild(list);
  } else {
     if (!tip) {
       tip = "未定义邮箱";
     }
     var noData = new Element("div" , {'color' : 'red'}).update(tip);
     noData.style.fontSize = "10pt";
     noData.style.paddingTop = "5px";
     right.appendChild(noData);
  }
}

function setSelected(selectedUser){
  for(var i = 0 ;i < selectedUser.length ;i++){
    var str = selectedUser[i];
    if (str.match(ii)) {
      var b = str.indexOf("&lt;");
      str = str.substring(b+4 , str.length -4);
    }
    var selectedDiv = $("Div-" + str);
    if(selectedDiv){
      selectedDiv.isSelected = true;
      selectedDiv.className = "item select";
    }
  }
}
function addUserDiv(users , i){
  var divs = $("list-" + i);
  if(users.length > 0){
    for(var i = 0 ; i < users.length ; i++){
      var user = users[i];
      var div = createDiv(user);
      divs.appendChild(div);
    }
  }
}
function doSearch(value) {
  $('right').update("");
  var url = requestUrl2 + "/getEmailBySearch.act";
  var json = getJsonRs(url , "userName=" + encodeURI(value));
  tip = "未查询到用户";
  if (json.rtState == '0') {
    var us = json.rtData ;
    if (us.length > 0) {
      getRightPal("人员查询" , true , true , 0 );
      addUserDiv(us , 0);
      setSelected(outUser.innerHTML.trim().split(','));
    } else {
      getRightPal("人员查询" , true , false , 0 );
    }
  }
}