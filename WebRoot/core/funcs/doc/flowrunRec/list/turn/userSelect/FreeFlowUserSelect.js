var NOUSER = false;
var User,UserDesc,UserOp ,UserOpDesc;
var selectedColor = '#d1ffeb';
var moveColor = "#ccc";
var query;
var parentWindowObj ;//父窗口的引用
var flowId ;
var runId;
var tree;

var topFlag = true;//默认为指定主办人，这时主办人可以点
var nowPrcsId = '';

var selectRole = '0';
var selectGroup = '0';
var actionUrl = contextPath + "/yh/core/funcs/doc/act/YHFreeFlowUserSelectAct";
var deptName = "";

var menu = null;
var deptIndex = "";
var roleIndex = "";
var defaultIndex = "";
function getAccord(){
  var data = {
  panel:'left',
  data:[{title:'全部经办人', action:getAllUser},
       //{title:'按流程选择', action:getFlowUser },
       {title:'按部门选择', action:getDept },
       {title:'按角色选择', action:getRole },
       {title:'自定义组', action:getDefaultGroup }]
   };
  menu = new MenuList(data);
  deptIndex = menu.getContainerId(2);
  roleIndex =  menu.getContainerId(3);
  defaultIndex =  menu.getContainerId(4);
  menu.showItem(this,{},2);
  getDept();
}
function getAllUser() {
  $('right').update("");
  var users = [];
  users = loadData();
  if(!users || users.length <= 0 ){
    //去取服务器 ,所有经办人
    NOUSER = true;
    getRightNoData("所有经办人" , '此步骤没有设置经办人');
    return ;
  }
  getRightPal("所有经办人" , users , 0);
}
function getFlowUser() {
  //$('right').update("");
  
}
function getDept(){
  var conDiv = $(deptIndex);
  conDiv.update("");
  var div = new Element("div");
  div.className = "TableControl";
  div.style.color = 'blue';
  div.align = 'center';
  div.update("本部门经办人");
  div.onclick=function(){
    $('right').update("");
    var users = loadData(deptId);
    if(!users || users.length <= 0 ){
      getRightNoData("本部门经办人" , '此部门下没有定义经办人');
      return ;
    }
    getRightPal("本部门经办人" , users , 0);
  }
  conDiv.appendChild(div);
  var config = {bindToContainerId:deptIndex
      , requestUrl:contextPath + "/yh/core/funcs/orgselect/act/YHDeptSelectAct/getDeptTree.act?id="
      , isOnceLoad:false
      , linkPara:{clickFunc:treeNodeClick}
      , isUserModule:true
    }
  tree = new DTree(config);
  tree.show(); 
}

function doInit(){
  parentWindowObj = window.dialogArguments;
  //不是指定主办人选项，这时主办人不能点
  if(parentWindowObj.topFlagInput.value != '0'){
    topFlag = false;
  }
  nowPrcsId = parentWindowObj.nowPrcsId;
  getAccord();
  flowId = parentWindowObj.flowId;
  runId = parentWindowObj.runId;
 //主办人
  UserOp= parentWindowObj.prcsOpUser;
  UserOpDesc = parentWindowObj.prcsOpUserNameSpan;
  //经办人
  UserDesc = parentWindowObj.prcsUserNameSpan;
  User = parentWindowObj.prcsUser; 
  getAllUser();
}
function parseCtrlToStr(ctrl){
  var spans = ctrl.getElementsByTagName("span");
  var str = "";
  for(var i = 0 ;i < spans.length ; i++){
    var span = spans[i];
    var tmp = span.innerHTML;
    var name = tmp.split("<IMG")[0];
    str += name.trim() + ",";
  }
  return str;
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
    if(idArray[i]){
      rtArray.add({userId: idArray[i], userName: nameArray[i]});
    }
  }
  return rtArray;
}


function setSelected(selectedOp, selectedHo){
  //设置经办人选 中  for(var i = 0 ; i < selectedOp.length ; i++){
  var selectedTr = $('TR-' + selectedOp[i]);
    if(selectedTr){
      var selectedTd = selectedTr.lastChild;
      selectedTd.isChecked = true;
      selectedTd.style.backgroundColor = selectedColor;
    }
  }
  //设置主办人选中
  var selectedTr = $('TR-' + selectedHo);
  if(selectedTr){
    var selectedTd = selectedTr.firstChild;
    selectedTd.isChecked = true;
    selectedTd.style.backgroundColor = selectedColor;
  }
}
function addTr(userOp , tbody ){
  for(var i = 0 ; i < userOp.length ; i++){
    var user = userOp[i];
    if (user) {
      var tr = createTr(user);
      tbody.appendChild(tr);
    }
  }  
}
function createTr(user){
  var tr = document.createElement("tr");
  tr.className = 'TableLine1';
  tr.id = 'TR-' + user.userId;
  tr.onmouseover = function(){
    this.style.backgroundColor = '#FFF';
  }
  tr.onmouseout = function(){
  this.style.backgroundColor = '';
  }
  var td = document.createElement("td");
  td.className = "userHost";
  //var td = new Element('td' , {'class':'userHost'}).update('主办人');
  td.innerHTML = '主办人';
  td.isChecked = false;
  td.onclick = function(){
    //选 中    if(topFlag){
      if(this.isChecked){
        UserOp.value = '';
        UserOpDesc.innerHTML = '';
        this.style.backgroundColor = '';
        this.isChecked = false;
      }else{
        var userId = UserOp.value;
        var tr = $('TR-' + userId);
        if(tr){
          var tdTmp = tr.firstChild;
          tdTmp.style.backgroundColor = '';
          tdTmp.isChecked = false;
        }
        var sibling = this.nextSibling;
        var id = this.parentNode.id.substr(3);
        
        var name = sibling.innerHTML.trim();
        
        addOpUser(id , name);
        if(!sibling.isChecked){
          User.value += id + ',';
          addUser(id , name);
          sibling.style.backgroundColor = selectedColor;
          sibling.isChecked = true;
        }
        this.style.backgroundColor = selectedColor;
        this.isChecked = true;
      }
    }
  }
  tr.appendChild(td);
  var td = document.createElement("td");
  td.className = "userOp";
  td.innerHTML = user.userName;
  td.isChecked = false;
  td.onclick = function(){
    var trParent = this.parentNode;
    //选 中
    if(this.isChecked){
      var tdOp = trParent.firstChild;
      //也是选中的
      if(tdOp.isChecked){
      UserOp.value = '';
        UserOpDesc.innerHTML = '';
        tdOp.style.backgroundColor = '';
        tdOp.isChecked = false;
      }
      var userStr = User.value;
      
      var id = trParent.id.substr(3);
      User.value = getOutofStr(userStr , id);
   
      removeUser(id);
      this.style.backgroundColor = '';
      this.isChecked = false;
    }else{
      var id = trParent.id.substr(3);
      User.value += id + ",";
      var userName = this.innerHTML.trim();

      addUser(id , userName);
      
      this.style.backgroundColor = selectedColor;
      this.isChecked = true;
    }
  }
  tr.appendChild(td);
  return tr;
}
//添加主办人
function addOpUser(id , name) {
  UserOp.value = id;
  UserOpDesc.innerHTML = "<span>" + name
    + "<img align=\"absmiddle\" onclick=\"javascript:cancelUser(this,'"+ id +"',true ,'"+ nowPrcsId +"' );\" src=\"" + imgPath + "/remove.png\"/></span>";
  
}

function getAllTr(i) { 
  var trs = new Array();
  if (i != undefined) {
    trs = $('list-' + i).getElementsByTagName('tr');
  } else {
    for(var i = 0 ;;i++) {
      tr = $('list-' + i);
      if (!tr) {
        break; 
      }
      var tmpTr = tr.getElementsByTagName('tr');
      for(var j = 0 ;j < tmpTr.length ; j++) {
        if (tmpTr[j]) {
          trs.push(tmpTr[j]);
        }
      }
    }
  }
  return trs;
}
function selectedAll(i){
  var trs = getAllTr(i);
  var UserStr = User.value;
  if(UserDesc.innerHTML){
    var UserDescStr = UserDesc.innerHTML.trim();
  }else{
    var UserDescStr = "";
  }
  for(var i = 0 ; i< trs.length ;i++){
  var trTmp = trs[i];
  var td = trTmp.lastChild;
  if(!td.isChecked){
    var userId = trTmp.id.substr(3);
      var userName = td.innerHTML.trim();
    td.style.backgroundColor = selectedColor;
    td.isChecked = true;
    User.value += userId + ",";
    addUser(userId , userName);
  }
  }
}
function disSelectedAll(i){
  var trs = getAllTr(i);
  if(!UserDesc.innerHTML){
  return ;
  }
  var UserDescStr = UserDesc.innerHTML.trim();
  for(var i = 0 ; i< trs.length ;i++){
  var trTmp = trs[i];
  var td = trTmp.lastChild;
  var td2 = trTmp.firstChild;
  if(td.isChecked){
    var userId = trTmp.id.substr(3);
    var userName = td.innerHTML.trim();
      td.style.backgroundColor = "";
      var UserStr = User.value;
      User.value = getOutofStr(UserStr , userId);
      td.isChecked = false;
      removeUser(userId);
  }
  if (td2.isChecked){
      td2.style.backgroundColor = "";
      td2.isChecked = false;
      UserOp.value = '';
    UserOpDesc.innerHTML = '';
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
  return strTmp;
}
function treeNodeClick(id){
  if(!NOUSER){
    $('right').update("");
    var treeNode = tree.getNode(id);
    var name = treeNode.name;
    var users = loadData(id);
    
    if(!users || users.length <= 0 ){
      getRightNoData(name , '此部门下没有定义经办人');
      return ;
    }
    getRightPal(name , users , 0);
  }
}
function removeUser(id){
  var img = parentWindowObj.$("userRemove-" + id  + "-" + nowPrcsId);
  UserDesc.removeChild(img.parentNode);
}
function addUser(id , name){
  var tmp = "<span>" + name + "<img id=\"userRemove-" + id  + "-" + nowPrcsId +"\" align=\"absmiddle\" onclick=\"javascript:cancelUser(this,'"+ id +"',false,'"+ nowPrcsId +"');\" src=\"" + imgPath + "/remove.png\"/>&nbsp;</span>";
  UserDesc.innerHTML += tmp; 
}
function loadData(deptId){
  var users = [];
  url = actionUrl + "/getUsers.act";
  var par = "flowId=" + flowId + "&runId=" + runId; 
  if(deptId){
    par += "&deptId=" + deptId;
  }
  var json = getJsonRs(url , par);
  if(json.rtState == '0'){
    users = json.rtData;
  }
  return users;
}
function getRightNoData(title , tip) {
  var right = $('right');
  setTitle(title);
  if (!tip) {
    tip = "未定义用户";
  }
  var noData = new Element("div" , {'color' : 'red'}).update(tip);
  noData.style.fontSize = "10pt";
  noData.style.color = "red";
  noData.style.paddingTop = "10px";
  noData.style.paddingBottom = "10px";
  right.appendChild(noData);
}
/**
 * 　标题,　数据,　pal号, flag 控制多个pal时全选和不全选函数的
 * @param title
 * @param users
 * @param i
 * @param flag
 * @return
 */
function getRightPal(title  , users , i , flag){
  var right = $('right');
  setTitle(title);
  var hasData = new Element("div");
  hasData.id = "hasData-" + i;  
  right.appendChild(hasData);
  if (i == 0) {
    var addAll = new Element("div" , {'class' : 'item'}).update("全部添加");
    addAll.onclick = function () {
      if (flag) {
        selectedAll();
      } else {
        selectedAll(i);
      }
    }
    hasData.appendChild(addAll);
    var disAll = new Element("div" , {'class' : 'item2'}).update("全部删除");
    disAll.onclick = function () {
      if (flag) {
        disSelectedAll();
      } else {
        disSelectedAll(i);
      }
    }
    hasData.appendChild(disAll);
  }
  var table = new Element("table" , {'class' : 'TableList2'});
  table.width = '100%';
  var tbody = new Element("tbody");
  tbody.id = "list-" + i;
  table.appendChild(tbody);
  hasData.appendChild(table);
  addTr(users , tbody );
  //设置主办人，经办人  setSelected(User.value.split(','), UserOp.value);
}
function setTitle (title) {
  var tDiv = new Element("div" , {'class' : 'item TableControl'}).update(title);
  tDiv.style.fontSize = '12pt';
  tDiv.style.color = 'blue';
  tDiv.style.fontWeight = 'bold'; 
  tDiv.style.borderTop = '1px solid #669'; 
  var right = $('right');
  right.appendChild(tDiv);
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
  if (!NOUSER) {
    $('right').update("");
    var requestUrl = contextPath + "/yh/core/module/org_select/act/YHOrgSelect2Act";
    var url = requestUrl + "/getUserByRole.act";
    var par = "flowId=" + flowId + "&runId=" + runId; 
    par = par + "&roleId=" + roleId;
    var json = getJsonRs(url , par);
    if (json.rtState == '0') {
      var us = json.rtData.principalRole ;
      roleUser = us;
      var b = 0 ;
      if (us && us.length > 0) {
        getRightPal(privName , us , b , true);
        b ++ ;
      } else {
        getRightNoData(privName , "没有经办人" );
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
      if (us2 && us2.length > 0) {
        getRightPal("辅助角色" , us2 , b , true);
      }
    }
  }
}
function getDefaultGroup() {
  $(defaultIndex).update("");
  var requestUrl = contextPath + "/yh/core/module/org_select/act/YHOrgSelect2Act";
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

function getUserByGroup(id , name){
  if (!NOUSER) {
    $('right').update("");
    var requestUrl = contextPath + "/yh/core/module/org_select/act/YHOrgSelect2Act";
    var url = requestUrl + '/getUserByGroup.act';
    var par = "flowId=" + flowId + "&runId=" + runId; 
    par += "&groupId=" + id;
    var json = getJsonRs(url , par);
    tip = "没有经办人";
    if (json.rtState == '0') {
      var us = json.rtData ;
      if (us && us.length > 0) {
        getRightPal(name ,us , 0);
      } else {
        getRightNoData(name ,tip );
      }
    }
  }
}

function doSearch(value) {
  if (!NOUSER) {
    $('right').update("");
    var requestUrl = contextPath + "/yh/core/module/org_select/act/YHOrgSelect2Act";
    var url = requestUrl + '/getUserBySearch.act';
    var par = "flowId=" + flowId + "&runId=" + runId; 
    par += "&userName=" + encodeURI(value);
    var json = getJsonRs(url , par);
    tip = "未查询到用户";
    if (json.rtState == '0') {
      var us = json.rtData ;
      if (us && us.length > 0) {
        getRightPal("人员查询" ,us , 0);
      } else {
        getRightNoData("人员查询" ,tip );
      }
    }
  }
}
function setFilterTitle() {
  var conDiv = $(deptIndex);
  conDiv.update("");
  var div = new Element("div");
  div.className = "TableControl";
  div.style.color = 'blue';
  div.align = 'center';
  div.update(deptName);
  conDiv.appendChild(div);
  
  div = new Element("div");
  div.style.color = 'red';
  div.align = 'left';
  div.update("此步骤为只允许选择本部门经办人");
  conDiv.appendChild(div);
}