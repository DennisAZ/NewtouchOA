var selectedColor = "rgb(0, 51, 255)";
var moveColor = "#ccc";
var User,UserDesc;
var UserC,UserDescC;
var UserBC,UserDescBC;
var toWebmail,toWebmailC,toWebmailBC;

var selectRole = '0';
var selectGroup = '0';

var selectBook = '0';
var selectPrivateBook = '0';
var menu = null;
var tree =  null;
var deptIndex = "";
var roleIndex = "";
var defaultIndex = "";
var publicBookIndex = "";
var privateBookIndex = "";

var tr_cc_index = "";
var tr_bcc_index = "";
var tr_w_index = "";
var tr_wbcc_index = "";
var tr_wcc_index = "";
var colCount = 1;


var requestUrl = contextPath + "/yh/core/module/org_select/act/YHOrgSelect2Act";
function getAccord(){
  var data = {
  panel:'left',
  data:[
       {title:'按部门选择', action:getTree},
       {title:'按角色选择', action:getRole},
       {title:'自定义组', action:getDefaultGroup },
       {title:'在线人员', action:getOnline}
       ]
   };
  
  if (tr_w_index || tr_wbcc_index || tr_wcc_index) {
    data.data.push({title:'个人通讯簿', action:getPrivateBook});
    data.data.push({title:'公共通讯簿', action:getPublicBook});
  }
  menu = new MenuList(data);
  deptIndex = menu.getContainerId(1);
  roleIndex = menu.getContainerId(2);
  defaultIndex = menu.getContainerId(3);
  if (tr_w_index || tr_wbcc_index || tr_wcc_index) {
    publicBookIndex = menu.getContainerId(5);
    privateBookIndex = menu.getContainerId(6);
  }
  menu.showItem(this,{},1);
  getTree();
}
function getTree(){
  $(deptIndex).update("");
  var url = contextPath + "/yh/core/funcs/orgselect/act/YHDeptSelectAct/getDeptTree.act?";
  if (moduleId) {
    url += "moduleId=" + moduleId + "&privNoFlag="+ privNoFlag +"&id=" ;
  } else {
    url += "id=";
  }
  var config = {bindToContainerId:deptIndex
      , requestUrl:url
      , isOnceLoad:false
      , linkPara:{clickFunc:treeNodeClick}
      , isUserModule:true
    }
  if (!isSingle && !moduleId) {
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
  var url = requestUrl + "/getUserByDept.act?deptId=" + id;
  if (moduleId) {
    url += "&moduleId=" + moduleId + "&privNoFlag="+ privNoFlag ;
  }
  var json = getJsonRs(url);
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
    td.innerHTML = name;
  }
  else {
    tDiv = new Element("div" , {'class' : 'header'}).update(name);
  }
  
  if (!isParent) {
    tDiv.align = 'left';
  }
  tDiv.id = "title-" + i;
  tDiv.isTitle = true;
  right.appendChild(tDiv);

  if (hasData) {
    var hasData = new Element("div", {"class": "list"});
    hasData.id = "hasData-" + i;  
    right.appendChild(hasData);
    if (isParent) {
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
    if (isParent)  {
      var noData = new Element("div" , {'color' : 'red'}).update("未定义用户");
      noData.style.fontSize = "10pt";
      noData.style.paddingTop = "5px";
      right.appendChild(noData);
    }
  }
}

function doInit(){
  var parentWindowObj = window.dialogArguments;
   tr_cc_index = parentWindowObj.tr_cc_index;
   tr_bcc_index = parentWindowObj.tr_bcc_index;
   tr_w_index =parentWindowObj.tr_w_index;
   tr_wbcc_index = parentWindowObj.tr_wbcc_index;
   tr_wcc_index = parentWindowObj.tr_wcc_index;
  
  getAccord();
 // var obj = tree.getFirstNode();
  //tree.open(obj.nodeId); 
  if (tr_cc_index) {
    colCount++;
  }
  if (tr_bcc_index) {
    colCount++;
  }
  if (tr_w_index) {
    colCount++;
  }
  if (tr_wbcc_index) {
    colCount++;
  }
  if (tr_wcc_index) {
    colCount++;
  }
  
  var userRetNameArray = parentWindowObj["userRetNameArray"];
  //if (userRetNameArray) {
    var userCntrl = userRetNameArray[0];
    var userDescCntrl = userRetNameArray[1];
    var userCntrlc = userRetNameArray[2];
    var userDescCntrlc = userRetNameArray[3];
    var userCntrlbc = userRetNameArray[4];
    var userDescCntrlbc = userRetNameArray[5];
    
    var userCntrlw = userRetNameArray[6];
    var userCntrlwc = userRetNameArray[7];
    var userCntrlwbc = userRetNameArray[8];
    
    User = parentWindowObj.$(userCntrl);
    UserDesc = parentWindowObj.$(userDescCntrl);
    UserC = parentWindowObj.$(userCntrlc);
    UserDescC = parentWindowObj.$(userDescCntrlc);
    UserBC = parentWindowObj.$(userCntrlbc);
    UserDescBC = parentWindowObj.$(userDescCntrlbc);
    
    toWebmail = parentWindowObj.$(userCntrlw);
    toWebmailC = parentWindowObj.$(userCntrlwc);
    toWebmailBC = parentWindowObj.$(userCntrlwbc);
 // }else {
 //   User = parentWindowObj.$("user");
  //  UserDesc = parentWindowObj.$("userDesc");
  //}
  //存在已经选中的人员
    /*  if (!User.value) {
    getDefaultUser();
  }else {
    getSelectedUser();
  }
  */
    getDefaultUser();
}


/**
 * 解析已经选中的人员

 * @userIdStr       人员ID字符串

 * @userNameStr     人员名称字符串

 */
function parseSelectedUsers(userIdStr, userNameStr) {
  var rtArray = [];
  var sUserState = "";
  var stateArray = [];
  if (!userIdStr || !userNameStr) {
    return rtArray;
  }
  var idArray = userIdStr.split(",");
  var nameArray = userNameStr.split(",");
  if (idArray.length != nameArray.length) {
    return rtArray;
  }
  var url = requestUrl + "/getUserState.act?ids=" + userIdStr;
  var json = getJsonRs(url);
  if (json.rtState == '0') {
    sUserState = json.rtData;
    
  } else {
    return rtArray; 
  }
  stateArray = sUserState.split(",");
  if (idArray.length != stateArray.length) {
    return rtArray;
  }
  for (var i = 0; i < idArray.length; i++) {
    if (idArray[i]) {
      rtArray.add({userId: idArray[i], userName: nameArray[i] , isOnline : stateArray[i] });
    }
  }
  return rtArray;
}
function treeNodeClick(id){
  var url = requestUrl  + "/getPersonsByDept.act"
  if (moduleId) {
    url += "?moduleId=" + moduleId + "&privNoFlag="+ privNoFlag ;
  }
  var node = tree.getNode(id);
  //有这部门的权限  if (node && node.extData) {
    var json = getJsonRs(url , 'deptId=' + id + "&notLoginIn=" + notLoginIn);
    if(json.rtState == '0'){
      $("right").update("");
      var users = json.rtData;
      var name = json.rtMsrg;
      if (users.length > 0) {
        getRightPal(name , true , true , 0 );
        addUserDiv(users , 0);
        setSelected(User.value.split(',')
            , UserC.value.split(",")
            , UserBC.value.split(",")
            , toWebmail.innerHTML.split(",")
            , toWebmailC.innerHTML.split(",")
            , toWebmailBC.innerHTML.split(",")
        );
      } else {
        getRightPal(name , true , false , 0 );
      }
    }
  }
}

function setSelected(selectedUser , selectedUserC , selectedUserBC , selectedEmail, selectedEmailC, selectedEmailBC){
  setSelectedUser(selectedUser ,"td-");
  
  if (tr_cc_index) {
    setSelectedUser(selectedUserC ,"cc-td-");
  }
  
  if (tr_bcc_index) {
    setSelectedUser(selectedUserBC ,"cc-td-");
  }
  
  if (tr_w_index) {
    setSelectedUser(selectedEmail ,"w-td-");
  }
  if (tr_wcc_index) {
    setSelectedUser(selectedEmailC ,"wc-td-");
  }
  if (tr_wbcc_index) {
    setSelectedUser(toWebmailBC ,"wbc-td-");
  }
}
function setSelectedUser(value , pre) {
  for(var i = 0 ;i < value.length ;i++){
    var td = $( pre + value[i]);
    if(td){
      td.isSelected = true;
      td.className = "item select";
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
  var userEmail = user.email;
  if (userName) {
    userEmail = userName + "(" + email + ")";
  }
  tmp = userName;
  if (deptName) {
    tmp = "[" + deptName + "]"  + tmp ;
  }
  if (isOnline) {
    if (isOnline != '0') {
      tmp += "<font color=red>(在线)<font>";
    }
  }
  var userDiv = new Element('div',{'class':'item'}).update(tmp);
  userDiv.id = "tr-" + userId ;
  var tr = new Element("tr");
  tDiv.appendChild(tr);
  var tdl = tr.insertCell(0);
  tdl.className = "left";
  
  createUserTd(1,tr , "td-" , userId , userName , User,UserDesc);
  
  var i2 = 2;
  if (tr_cc_index) {
    createUserTd(i2,tr ,"cc-td-" , userId , userName , UserC,UserDescC);
    i2++;
  }
  
  if (tr_bcc_index) {
    createUserTd(i2,tr ,"bcc-td-" , userId , userName , UserBC,UserDescBC);
    i2++;
  }
  
  if (tr_w_index) {
    createEmailTd(i2 , tr , "w-td-" ,  user.email , userEmail, toWebmail);
    i2++;
  }
  if (tr_wcc_index) {
    createEmailTd(i2 , tr , "wc-td-" ,  user.email , userEmail, toWebmailC);
    i2++;
  }
  if (tr_wbcc_index) {
    createEmailTd(i2 , tr , "wbc-td-" ,  user.email , userEmail, toWebmailBC);
    i2++;
  }
  var tdr = tr.insertCell(i2);
  tdr.className = "right";
  return userDiv;
}
function createEmailTd(i2 , tr , pre , email , emailName, emailCtrl){
  var str = emailName;
  var td = tr.insertCell(i2);
  td.className = "center";
  td.id = pre + str;
  td.innerHTML = str;
  
  td.isSelected = false;
  td.onmouseout = function(){
    if(!this.isSelected){
      this.className = "item";
    }else {
      this.className = "item select";
    }
  }
  td.onmouseover = function(){
    if(!this.isSelected){
      this.className = "item hover";
    }else {
      this.className = "item select";
    }
  }
  addEventEmailTd(td , emailCtrl , pre);
}
function addEventEmailTd(td , emailCtrl , pre) {
  td.onclick = function(){
    //选 中到不选 中
    if(this.isSelected){
      var email = this.id.substring(pre.length);
      var userDescStr = emailCtrl.innerHTML.trim();
      emailCtrl.innerHTML = getOutofStr(userDescStr , email);
      this.isSelected = false;
      this.className = "item";
    }else{
      var email =  this.id.substring(pre.length);
      var userDescStr = emailCtrl.innerHTML.trim();
      var str = this.innerHTML.trim();
      if(emailCtrl.innerHTML){
        emailCtrl.innerHTML += "," + email;
      }else{
        emailCtrl.innerHTML = email  ;
      }
      this.isSelected = true;
      this.className = "item select";
    }
  }
}

function createUserTd(i2,tr , pre , userId , userName , User,UserDesc) {
  var td = tr.insertCell(i2);
  td.className = "center";
  td.id = pre + userId;
  td.innerHTML = userName;
  
  td.isSelected = false;
  td.userName = userName;
  td.onmouseout = function(){
    if(!this.isSelected){
      this.className = "item";
    }else {
      this.className = "item select";
    }
  }
  td.onmouseover = function(){
    if(!this.isSelected){
      this.className = "item hover";
    }else {
      this.className = "item select";
    }
  }
  addEventUserTd(td , userId , userName , User , UserDesc);
}
function addEventUserTd(td , userId , userName , User , UserDesc) {
  td.onclick = function(){
    //选 中到不选 中
    if(this.isSelected){
      selectUser(userId ,  userName, User , UserDesc);
      this.isSelected = false;
      this.className = "item";
    }else{
      disSelectUser(userId , userName , User , UserDesc);
      this.isSelected = true;
      this.className = "item select";
    }
  }
}

function selectUser(id , userName , User , UserDesc) {
  var userStr = User.value;
  var userDescStr = "";
  if (UserDesc.tagName == "INPUT") {
    userDescStr = UserDesc.value;
    UserDesc.value = getOutofStr(userDescStr , userName);
  } else {
    userDescStr = UserDesc.innerHTML.trim();
    UserDesc.innerHTML = getOutofStr(userDescStr , userName);
  }
  User.value = getOutofStr(userStr , id);
}
function disSelectUser(id , userName , User , UserDesc) {
  
  var userStr = User.value;
  var userDescStr = "";
  
  if (UserDesc.tagName == "INPUT") {
    userDescStr = UserDesc.value;
  } else {
    userDescStr = UserDesc.innerHTML.trim();
  }
  if (User.value.length > 0) {
    User.value += "," + id;
  }else {
    User.value = id ;
  }
  if (UserDesc.tagName == "INPUT") {
    if (UserDesc.value) {
      UserDesc.value += "," + userName;
    } else {
      UserDesc.value =  userName;
    }
  } else {
    if(UserDesc.innerHTML){
      UserDesc.innerHTML += "," + userName;
    }else{
      UserDesc.innerHTML = userName;
    }
  }
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
  if (UserDesc.tagName == "INPUT") {
    if(UserDesc.value){
      var userDescStr = UserDesc.value;
    }else{
      var userDescStr = "";
    }
  } else {
    if(UserDesc.innerHTML){
      var userDescStr = UserDesc.innerHTML.trim();
    }else{
      var userDescStr = "";
    }
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
  if (UserDesc.tagName == 'INPUT') {
    UserDesc.value = userDescStr;
  } else {
    UserDesc.innerHTML = userDescStr;
  }
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
  if (UserDesc.tagName == 'INPUT') {
    if(!UserDesc.value){
      return ;
    }
  } else {
    if(!UserDesc.innerHTML){
      return ;
    }
  }
  
  if (UserDesc.tagName == "INPUT") {
    if(UserDesc.value){
      var userDescStr = UserDesc.value;
    }else{
      var userDescStr = "";
    }
  } else {
    if(UserDesc.innerHTML){
      var userDescStr = UserDesc.innerHTML.trim();
    }else{
      var userDescStr = "";
    }
  }
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
  if (UserDesc.tagName == 'INPUT') {
    UserDesc.value = userDescStr;
  } else {
    UserDesc.innerHTML = userDescStr;
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
function createTitle(tDiv , title  ) {
  var tr = tDiv.insertRow(0);
  var tdl = tr.insertCell(0);
  var td = tr.insertCell(1);
  var tdr = tr.insertCell(2);
  td.colSpan = colCount;
  tdl.className = "left";
  td.className = "center";
  tdr.className = "right";
  td.innerHTML = title;
}
function createSubTitle(tDiv , i) {
  var tr = new Element("tr");
  tDiv.appendChild(tr);
  var tdl = tr.insertCell(0);
  //tdl.className = "left";
  
  var tdU = tr.insertCell(1);
  tdU.className = "center";
  tdU.innerHTML = "<input type='checkbox' id='user-" + i +"'/><label for='user-" + i +"'>内部邮件</label>";
  
  var i2 = 2;
  if (tr_cc_index) {
    var tdC = tr.insertCell(i2);
    tdC.className = "center";
    tdC.innerHTML = "<input type='checkbox' id='userC-" + i +"'/><label for='userC-" + i +"'>抄送</label>";
    i2++;
  }
  
  if (tr_bcc_index) {
    var tdBC = tr.insertCell(i2);
    tdBC.className = "center";
    tdBC.innerHTML = "<input type='checkbox' id='userBC-" + i +"'/><label for='userBC-" + i +"'>密送</label>";
    i2++;
  }
  
  if (tr_w_index) {
    var tdW = tr.insertCell(i2);
   tdW.className = "center";
    tdW.innerHTML = "<input type='checkbox' id='w-" + i +"'/><label for='w-" + i +"'>外部邮件</label>";
    i2++;
  }
  if (tr_wcc_index) {
    var tdWc = tr.insertCell(i2);
    tdWc.className = "center";
    tdWc.innerHTML = "<input type='checkbox' id='wc-" + i +"'/><label for='wc-" + i +"'>外部抄送</label>";
    i2++;
  }
  if (tr_wbcc_index) {
    var tdWc = tr.insertCell(i2);
    tdWbc.className = "center";
    tdWbc.innerHTML = "<input type='checkbox' id='wbc-" + i +"'/><label for='wbc-" + i +"'>外部抄送</label>";
    i2++;
  }
 
  var tdr = tr.insertCell(i2);
  //tdr.className = "right";
}
function getRightPal(title , isHaveAll , hasData , i , tip){
  var right = $('right');
  var tDiv = $("rigthTable");
  if (!tDiv) {
    tDiv = new Element("table", {
      "class": "TableTop",
      "width": "100%"
    });
    tDiv.id= "rigthTable";
    right.appendChild(tDiv);
  } 
  createTitle(tDiv , title , i);
  /*
  var tbody  = new Element("tbody");
  tbody.id = "title-" + i;
  tDiv.appendChild(tbody);
  */
  if (hasData) {
    createSubTitle(tDiv,i);
    var tbody = new Element("tbody");
    tbody.id = "list-" + i;
    tDiv.appendChild(tbody);
    /*
    var hasData = new Element("div", {"class": "list"});
    hasData.id = "hasData-" + i;  
    right.appendChild(hasData);
    if (isHaveAll && !isSingle) {
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
    */
    
  } else {
     if (!tip) {
       tip = "未定义用户或邮箱";
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
  if (moduleId) {
    url += "?moduleId=" + moduleId + "&privNoFlag="+ privNoFlag ;
  }
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
  var url = requestUrl + "/getUserByRole.act";
  if (moduleId) {
    url += "?moduleId=" + moduleId + "&privNoFlag="+ privNoFlag ;
  }
  var json = getJsonRs(url , "roleId=" + roleId + "&notLoginIn=" + notLoginIn);
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
  if (moduleId) {
    url += "&moduleId=" + moduleId + "&privNoFlag="+ privNoFlag ;
  }
  var json = getJsonRs(url , "notLoginIn=" + notLoginIn);
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
  if (moduleId) {
    url += "?moduleId=" + moduleId + "&privNoFlag="+ privNoFlag ;
  }
  var json = getJsonRs(url , "notLoginIn=" + notLoginIn);
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
  if (moduleId) {
    url += "?moduleId=" + moduleId + "&privNoFlag="+ privNoFlag ;
  }
  var json = getJsonRs(url , "userName=" + encodeURI(value) + "&notLoginIn=" + notLoginIn);
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
function getDefaultUser() {
  $('right').update("");
  var url = requestUrl + "/getDefaultUser.act";
  if (moduleId) {
    url += "?moduleId=" + moduleId + "&privNoFlag="+ privNoFlag ;
  }
  var json = getJsonRs(url ,  "notLoginIn=" + notLoginIn);
  tip = "未定义用户或邮箱";
  if (json.rtState == '0') {
    var us = json.rtData ;
    var name = json.rtMsrg;
    if (us.length > 0) {
      getRightPal(name , true , true , 0 , tip);
      //addUserDiv(us , 0);
      /*setSelected(User.value.split(',')
          , UserC.value.split(",")
          , UserBC.value.split(",")
          , toWebmail.innerHTML.split(",")
          , toWebmailC.innerHTML.split(",")
          , toWebmailBC.innerHTML.split(","));*/
    } else {
      getRightPal(name , true , false , 0 ,tip );
    }
  }
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