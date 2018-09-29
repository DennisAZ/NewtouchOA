var selectedColor = "rgb(0, 51, 255)";
var moveColor = "#ccc";
var User,UserDesc;
var selectRole = '0';

var selectRole = '0';

var menu = null;
var deptIndex = "";
var roleIndex = "";
var requestUrl = contextPath + "/yh/core/module/org_select/act/YHOrgSelect2Act";
var actionUrl = contextPath +moduleSrcPath+"/act/YHProcessUserSelectAct";
function getAccord(){
  var data = {
  panel:'left',
  data:[{title:'全部经办人', action:getAllUser},
       {title:'按部门选择', action:getDept },
       {title:'按角色选择', action:getRole }]
   };
  menu = new MenuList(data);
  deptIndex = menu.getContainerId(2);
  roleIndex =  menu.getContainerId(3);
  menu.showItem(this,{},2);
  getDept();
}
function getDept(){
  var config = {bindToContainerId:deptIndex
      , requestUrl:contextPath + "/yh/core/funcs/orgselect/act/YHDeptSelectAct/getDeptTree.act?id="
      , isOnceLoad:false
      , linkPara:{clickFunc:treeNodeClick}
      , isUserModule:true
    }
  if (!isSingle) {
    config.checkboxPara = {isHaveCheckbox:true, checkedFun : checkedHandler , disCheckedFun:disCheckedHander ,expandEvent:false};
  }
  tree = new DTree(config);
  tree.show(); 
}
function checkedHandler(id) {
  getDeptUser (id);
  //选中所有
  selectedAll();
}
function disCheckedHander(id){
  getDeptUser (id);
  disSelectedAll();
}
function getDeptUser (id) {
  $('right').update("");
  var url = contextPath  +moduleSrcPath+"/act/YHFlowProcessAct/getPrivUser.act"
  var json = getJsonRs(url , 'deptId=' + id + "&prcsId=" + prcsId + "&flowId=" + flowId );
  if (json.rtState == '0') {
    var dept = json.rtData;
    for (var i = 0 ;i < dept.length ;i ++) {
      var name = dept[i].deptName;
      var us = dept[i].user;
      
      if (i == 0) {
        if (us.length > 0) {
          getDeptRightPal(name , true , 0 , true);
          addUserDiv(us , 0);
          setSelected(User.value.split(','));
        } else {
          getDeptRightPal(name  , false , 0 , true);
        }
      } else {
        if (us.length > 0) {
          getDeptRightPal(name , true , i , false);
          addUserDiv(us , i);
          setSelected(User.value.split(','));
        } else {
          getDeptRightPal(name  , false , i , false);
        }
      }
    }
  }
}
function getDeptRightPal(name  , hasData , i , isParent) {
  var right = $('right');
  var tDiv = new Element("div" , {'class' : 'header'}).update(name);
  if (!isParent) {
    tDiv.align = 'left';
  }
  tDiv.id = "title-" + i;
  tDiv.isTitle = true;
  right.appendChild(tDiv);

  if (hasData) {
    var hasData = new Element("div");
    hasData.id = "hasData-" + i;  
    right.appendChild(hasData);
    if (isParent) {
      var addAll = new Element("div" , {'class' : 'item TableControl'}).update("全部添加");
      addAll.onclick = function () {
        selectedAll();
      }
      hasData.appendChild(addAll);
      var disAll = new Element("div" , {'class' : 'item TableControl'}).update("全部删除");
      disAll.onclick = function () {
        disSelectedAll();
      }
      hasData.appendChild(disAll);
    }
    var list = new Element("div");
    list.id = "list-" + i;
    hasData.appendChild(list);
  } else {
    if (isParent)  {
      var noData = new Element("div" , {'color' : 'red'}).update("未定义用户");
      noData.style.fontSize = "10pt";
      noData.style.paddingTop = "5px";
      right.appendChild(noData);
    }
  }
}

function doInit(){
  getAccord();
  //var obj = tree.getFirstNode();
  //tree.open(obj.nodeId); 
  var parentWindowObj = window.dialogArguments;
  var userRetNameArray = parentWindowObj["userRetNameArray"];
  if (userRetNameArray && userRetNameArray.length == 2) {
    var userCntrl = userRetNameArray[0];
    var userDescCntrl = userRetNameArray[1];
    User = parentWindowObj.$(userCntrl);
    UserDesc = parentWindowObj.$(userDescCntrl);
  }else {
    User = parentWindowObj.$("user");
    UserDesc = parentWindowObj.$("userDesc");
  }
  getAllUser();
}
function getAllUser() {
  $('right').update("");
  var getUserOpUrl = actionUrl + "/getPrivUser.act";
  var getUserOp = getJsonRs(getUserOpUrl, "flowId=" + flowId + "&prcsId=" + prcsId);
  if(getUserOp.rtState != '0'){
    return ;
  }
  var userOp = getUserOp.rtData;
  if (userOp.length > 0) {
    getRightPal("全部经办人"  , true , true , 0 );
    addUserDiv(userOp , 0);
    setSelected(User.value.split(','));
  } else {
    getRightPal("全部经办人" , true , false , 0 ,  "此步骤没有设置经办人" );
  }
}

/**
 * 解析已经选中的人员

 * @userIdStr       人员ID字符串

 * @userNameStr     人员名称字符串

 */
function parseSelectedUsers(userIdStr, userNameStr) {
  var rtArray = [];
  if (!userIdStr || !userNameStr) {
    return rtArray;
  }
  var idArray = userIdStr.split(",");
  var nameArray = userNameStr.split(",");
  if (idArray.length != nameArray.length) {
    return rtArray;
  }
  for (var i = 0; i < idArray.length; i++) {
    if (idArray[i]) {
      rtArray.add({userId: idArray[i], userName: nameArray[i]});
    }
  }
  return rtArray;
}
function treeNodeClick(id){
  var url = contextPath  +moduleSrcPath+"/act/YHFlowProcessAct/getPrivUser.act"
  var json = getJsonRs(url , 'deptId=' + id + "&prcsId=" + prcsId + "&flowId=" + flowId );
  if(json.rtState == '0'){
    $("right").update("");
    var users = json.rtData;
    var obj = tree.getNode(id);
    var title = "";
    if (obj) {
      title = obj.name;
    }
    if (users.length > 0) {
      getRightPal(title , true , true , 0 );
      addUserDiv(users , 0);
      setSelected(User.value.split(','));
    } else {
      getRightPal(title , true , false , 0 );
    }
  }
}

function setSelected(selectedUser){
  for(var i = 0 ;i < selectedUser.length ;i++){
    var selectedDiv = $("Div-" + selectedUser[i]);
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

function createDiv(user){
  var userName = user.userName;
  var deptName = user.deptName;
  var isOnline = user.isOnline;
  var userId = user.userId;
  
  tmp = userName;
  if (deptName) {
    tmp = "[" + deptName + "]"  + tmp ;
  }
  if (isOnline) {
    if (isOnline == '1') {
      tmp += "<font color=red>(在线)<font>";
    }
  }
  var userDiv = new Element('div',{'class':'item'}).update(tmp);
  userDiv.id = "Div-" + userId ;
  userDiv.isSelected = false;
  userDiv.userName = userName;
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
      if (!isSingle) { 
        var userStr = User.value;
        var userDescStr = UserDesc.innerHTML.trim();
        User.value = getOutofStr(userStr , this.id.substr(4));
        UserDesc.innerHTML = getOutofStr(userDescStr , userName);
      } else {
        User.value = "";
        UserDesc.value = "";
      }
      this.isSelected = false;
      this.className = "item";
    }else{
      if (!isSingle) { 
        var userStr = User.value;
        var userDescStr = UserDesc.innerHTML.trim();
        if (User.value.length > 0) {
          User.value += "," + this.id.substr(4);
        }else {
          User.value = this.id.substr(4) ;
        }
        if(UserDesc.innerHTML){
          UserDesc.innerHTML += "," + userName;
        }else{
          UserDesc.innerHTML = userName ;
        }
        this.isSelected = true;
        this.className = "item select";
      } else {
        User.value = this.id.substr(4);
        UserDesc.value = userName;
        close();
      }
    }
  }
  return userDiv;
}

function selectedAll(i){
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
  var userStr = User.value;
  if(UserDesc.innerHTML){
    var userDescStr = UserDesc.innerHTML.trim();
  }else{
    var userDescStr = "";
  }
  if (userDescStr) {
    userDescStr += ",";
  }
  if (userStr) {
    userStr += ",";
  }
  for(var i = 0;i < divs.length ;i++){
    var div = divs[i];
    if(!div.isSelected){
      var userId = div.id.substr(4);
      var userName = div.userName;
      div.isSelected = true;
      div.className = "item select";
      userStr += userId + ',';
      userDescStr += userName + ',';
    }
  }
  if (userStr && userStr.lastIndexOf(",") == userStr.length - 1) {
    userStr = userStr.substring(0, userStr.length - 1);
  }
  if (userDescStr && userDescStr.lastIndexOf(",") == userDescStr.length - 1) {
    userDescStr = userDescStr.substring(0, userDescStr.length - 1);
  }
  User.value = userStr;
  UserDesc.innerHTML = userDescStr;
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
  var userStr = User.value;
  if(!UserDesc.innerHTML){
    return ;
  }
  var userDescStr = UserDesc.innerHTML.trim();
  for(var i = 0 ; i< divs.length ;i++){
    var div = divs[i];
    if(div.isSelected){
      var userId = div.id.substr(4);
      var userName = div.userName;
      userStr = getOutofStr(userStr , userId);
      
      userDescStr = getOutofStr(userDescStr , userName);
      div.isSelected = false;
      div.className = "item";
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
  if (strTmp && strTmp.lastIndexOf(",") == strTmp.length - 1) {
    strTmp = strTmp.substring(0, strTmp.length - 1);
  }
  return strTmp;
}
function getRightPal(title , isHaveAll , hasData , i , tip){
  var right = $('right');
  var tDiv = new Element("div" , {'class' : 'header'}).update(title);
  tDiv.id = "title" + i;
  right.appendChild(tDiv);
  if (hasData) {
    var hasData = new Element("div");
    hasData.id = "hasData-" + i;  
    right.appendChild(hasData);
    if (isHaveAll && !isSingle) {
      var addAll = new Element("div" , {'class' : 'item TableControl'}).update("全部添加");
      addAll.onclick = function () {
        selectedAll(i);
      }
      hasData.appendChild(addAll);
      var disAll = new Element("div" , {'class' : 'item TableControl'}).update("全部删除");
      disAll.onclick = function () {
        disSelectedAll(i);
      }
      hasData.appendChild(disAll);
    }
    var list = new Element("div");
    list.id = "list-" + i;
    hasData.appendChild(list);
  } else {
     if (!tip) {
       tip = "未定义用户";
     }
     var noData = new Element("div" , {'color' : 'red'}).update(tip);
     noData.style.fontSize = "10pt";
     noData.style.paddingTop = "5px";
     right.appendChild(noData);
  }
}


function getSelectedUser() {
  $('right').update("");
  tip = "暂未选择人员";
  var us = parseSelectedUsers(User.value, UserDesc.value);
  if (us.length > 0) {
    getRightPal('已选人员' , true , true , 0 , tip);
    addUserDiv(us , 0);
    setSelected(User.value.split(','));
  } else {
    getRightPal('已选人员' , true , false , 0 , tip);
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
  var url = actionUrl + "/getUserByRole.act";
  var json = getJsonRs(url , "roleId=" + roleId +"&prcsId=" + prcsId + "&flowId=" + flowId);
  if (json.rtState == '0') {
    var us = json.rtData.principalRole ;
    roleUser = us;
    if (us.length > 0) {
      getRightPal(privName , true , true , 0);
      addUserDiv(us , 0);
      setSelected(User.value.split(','));
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
        if (tmp2.userId == tmp.userId) {
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
      setSelected(User.value.split(','));
    }
  }
}
function getDefaultGroup() {
  $(defaultIndex).update("");
  var url = requestUrl + "/getUserGroup.act";
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
    
   url = requestUrl + "/getUserGroup.act?isPublic=true";
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
  var url = requestUrl + "/getUserByGroup.act?groupId=" + id;
  var json = getJsonRs(url);
  if (json.rtState == '0') {
    var us = json.rtData ;
    if (us.length > 0) {
      getRightPal(name , true , true , 0 , "尚未添加用户");
      addUserDiv(us , 0);
      setSelected(User.value.split(','));
    } else {
      getRightPal(name , true , false , 0 , "尚未添加用户");
    }
  }
}
function getOnline() {
  $('right').update("");
  var url = requestUrl + "/getUserByOnline.act";
  var json = getJsonRs(url);
  if (json.rtState == '0') {
    var us = json.rtData ;
    if (us.length > 0) {
      getRightPal("全部在线人员" , true , true , 0);
      addUserDiv(us , 0);
      setSelected(User.value.split(','));
    } else {
      getRightPal("全部在线人员" , true , false , 0 );
    }
  }
}
function doSearch(value) {
  $('right').update("");
  var url = requestUrl + "/getUserBySearch.act";
  var json = getJsonRs(url , "userName=" + encodeURI(value));
  tip = "未查询到用户";
  if (json.rtState == '0') {
    var us = json.rtData ;
    if (us.length > 0) {
      getRightPal("人员查询" , true , true , 0 , tip);
      addUserDiv(us , 0);
      setSelected(User.value.split(','));
    } else {
      getRightPal("人员查询" , true , false , 0 ,tip );
    }
  }
}
