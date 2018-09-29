var NOUSER = false;
var User,UserDesc,UserOp ,UserOpDesc;
var selectedColor = '#d1ffeb';
var query;
var moveColor = "#ccc";
var selectRole = '0';

var menu = null;
var deptIndex = "";
var roleIndex = "";
var actionUrl = contextPath + moduleSrcPath +"/act/YHProcessUserSelectAct";
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
  var conDiv = $(deptIndex);
  conDiv.update("");
  var div = new Element("div");
  div.className = "TableControl";
  div.style.color = 'blue';
  div.align = 'center';
  div.update("本部门经办人");
  conDiv.appendChild(div);
  
  var url =  contextPath + "/yh/core/funcs/dept/act/YHDeptTreeAct/getTree.act?id=";
  tree = new DTree({bindToContainerId:deptIndex
                ,requestUrl:url
                ,isOnceLoad:false
                ,checkboxPara:{isHaveCheckbox:false}
          ,linkPara:{clickFunc:treeNodeClick}
          , isUserModule:true
  });
  tree.show();
  var obj = tree.getFirstNode();
  //tree.open(obj.nodeId);
}
function doInit(){
  var parentWindowObj = window.dialogArguments;
  getAccord();
  var selectedProc = parentWindowObj.selectedProc;
  var flowId = parentWindowObj.flowId;

  //区别以后
  if(!selectedProc){
    selectedProc =  parentWindowObj.seqId;
    query = "seqId=" + selectedProc ;
  }else{
    query = "prcsId=" + selectedProc + "&flowId=" + flowId;
  }
//设置主办人，经办人
  UserOp = parentWindowObj.UserHo;
  UserOpDesc = parentWindowObj.UserHoDesc;
  User = parentWindowObj.UserOp;
  UserDesc = parentWindowObj.UserOpDesc;
  getAllUser();
}

function setSelected(selectedOp, selectedHo){
  //设置经办人选 中
  for(var i = 0 ; i < selectedOp.length ; i++){
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
  td.innerHTML = '主办人';
  td.isChecked = false;
  td.onclick = function(){
    //选 中
    if(this.isChecked){
      UserOp.value = '';
      UserOpDesc.value = '';
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
      UserOp.value = id;
      UserOpDesc.value = name;
      if(!sibling.isChecked){
        User.value += id + ',';
        if(UserDesc.innerHTML){
          UserDesc.innerHTML += name + ',';
        }else{
          UserDesc.innerHTML = name + ',';
        }
        sibling.style.backgroundColor = selectedColor;
        sibling.isChecked = true;
      }
      this.style.backgroundColor = selectedColor;
      this.isChecked = true;
    }
  }
  tr.appendChild(td);
  td = document.createElement("td");
  td.width = "200px";
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
          UserOpDesc.value = '';
          tdOp.style.backgroundColor = '';
          tdOp.isChecked = false;
        }
        var userStr = User.value;
        var userDescStr = UserDesc.innerHTML.trim();
        User.value = getOutofStr(userStr , trParent.id.substr(3));
        UserDesc.innerHTML = getOutofStr(userDescStr , this.innerHTML.trim());
        this.style.backgroundColor = '';
        this.isChecked = false;
      }else{
        var tmp = trParent.id.substr(3);
        var name = this.innerHTML.trim();
        User.value += tmp + ",";
        if(UserDesc.innerHTML){
        UserDesc.innerHTML += name  + ",";
        }else{
        UserDesc.innerHTML =  name + ",";
        }
        this.style.backgroundColor = selectedColor;
        this.isChecked = true;
        
        if (!UserOp.value) {
          var tdOp = trParent.firstChild;
          if(tdOp){
            UserOp.value = tmp;
            UserOpDesc.value = name;
            tdOp.style.backgroundColor = selectedColor;
            tdOp.isChecked = true;
          }
        }
     }

  }
  tr.appendChild(td);
  return tr;
}
function removeTr(){
  var tbody = $("persons");
  var trList = tbody.getElementsByTagName('tr');
  for(i = trList.length-1; i>=0; i--) {
    tbody.removeChild(trList[i]);
  }
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
      UserStr += userId + ',';
      UserDescStr += userName + ',';
  }
  }
  User.value = UserStr;
  UserDesc.innerHTML = UserDescStr;
}
function disSelectedAll(){
  var trs = getAllTr(i);
  var UserStr = User.value;
  if(!UserDesc.innerHTML){
  return ;
  }
  var UserDescStr = UserDesc.innerHTML.trim();
  for(var i = 0 ; i< trs.length ;i++){
    var trTmp = trs[i];
    var td = trTmp.lastChild;
    if(td.isChecked){
      var userId = trTmp.id.substr(3);
      var userName = td.innerHTML.trim();
      UserStr = getOutofStr(UserStr , userId);
      UserDescStr = getOutofStr(UserDescStr , userName);
      td.style.backgroundColor = "";
      td.isChecked = false;
      
      var parentTr = td.parentNode;
      if (parentTr) {
        var td1 = parentTr.firstChild;
        if (td1 && td1.isChecked) {
          UserOp.value = "";
          UserOpDesc.value = "";
          td1.style.backgroundColor = "";
          td1.isChecked = false;
        }
      }
    }
  }
  
  User.value = UserStr;
  UserDesc.innerHTML = UserDescStr;
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
    var tmpQuery = query + "&deptId=" + id;
    var url = contextPath   + moduleSrcPath +"/act/YHFlowProcessAct/getPrivUser.act"
    var getUserOp = getJsonRs(url, tmpQuery);
    if(getUserOp.rtState != '0'){
      return ;
    }
    var userOp = getUserOp.rtData;
    if(!userOp || userOp.length <= 0 ){
      getRightNoData(name , '此部门下没有定义经办人');
      return ;
    }
    getRightPal(name , userOp , 0);
  }else{
    alert('此步骤还没有设置经办人');
  } 
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
  //设置主办人，经办人
  setSelected(User.value.split(','), UserOp.value);
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
function getAllUser() {
  $('right').update("");
  var getUserOpUrl = actionUrl + "/getPrivUser.act";
  var getUserOp = getJsonRs(getUserOpUrl, query);
  if(getUserOp.rtState != '0'){
    return ;
  }
  var userOp = getUserOp.rtData;
  if(!userOp || userOp.length <= 0 ){
    NOUSER = true;
    getRightNoData("全部经办人" , "此步骤没有设置经办人");
    return ;
  }
  getRightPal('全部经办人' , userOp , 0);
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
  var json = getJsonRs(url , query + "&roleId=" + roleId);
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