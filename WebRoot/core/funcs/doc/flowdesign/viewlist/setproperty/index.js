var baseContentUrl = contextPath + moduleContextPath +  "/flowdesign/viewlist/setproperty"
var requestUrl = contextPath + moduleSrcPath +  "/act/YHFlowProcessAct";
var prcsList ; //步骤列表
var prcsNode ;//步骤节点
var flowList; //流程列表
var disAipList;//呈批单
var formItem = new Array() ;//表单字段
var UserHo,UserHoDesc,UserOp ,UserOpDesc;
var UserHi,UserDesc;
var AutoUsers,mailToDesc;
var selectedProc;
var reg = /['"]/g;
var tabs = null;
function doInit(){
  var jso = [
             {title:"基本属性", onload:showInfo.bind(window, "baseInfo")
               , useTextContent:true
               ,imgUrl:imgPath + "/edit.gif"
               , contentUrl: baseContentUrl + "/basicInfo.jsp?isList=" + isList, useIframe:false}
               ,{title:"智能选人", onload:showInfo.bind(window, "autoSelect")
              , useTextContent:true
              ,imgUrl:imgPath + "/node_user.gif"
              , contentUrl: baseContentUrl + "/autoSelect.jsp?isList=" + isList, useIframe:false}
            ,{title:"流转设置", onload:showInfo.bind(window, "flowDispatch"), 
              useTextContent:true 
              ,imgUrl:imgPath + "/edit.gif"
              ,contentUrl:baseContentUrl + "/flowDispatch.jsp?isList=" + isList, useIframe:false}
            ,{title:"提醒设置", onload:showInfo.bind(window, "warnDispatch")
              , useTextContent:true
              ,imgUrl:imgPath + "/edit.gif"
              , contentUrl: baseContentUrl + "/warnDispatch.jsp?isList=" + isList, useIframe:false}
            ,{title:"其它设置", onload:showInfo.bind(window, "otherDispatch")
              , useTextContent:true
              ,imgUrl:imgPath + "/edit.gif"
              , contentUrl: baseContentUrl + "/otherDispatch.jsp?isList=" + isList, useIframe:false}
            ];
  tabs = buildTab(jso, 'contentDiv');
  if(isEdit){
    //修改
    displayEditMessage();
  }else{
    //增加
    displayAddMessage();
  }
  
  createExchangeSelect();
  var tmp = [];
  for (var i = 0 ;i < formItem.length ;i++) {
    var item = {};
    item.value = formItem[i].text;
    item.text = formItem[i].text;
    tmp.push(item);
  }
  setOptions($('fldParent').getElementsByTagName('optgroup')[0], tmp);
}
function createExchangeSelect(){
  var processTo = new Array();
  var disProcessTo = new Array();
  if(prcsNode){
    aPrcsNode = prcsNode.prcsTo.split(",");
  }else{
    aPrcsNode = [];
  }
  
  for(var i = 0 ;i < prcsList.length ;i++){
    var prcsTmp = prcsList[i];
    var isExist = false;
    for(var j = 0 ; j < aPrcsNode.length;j++){
      if(aPrcsNode[j] && aPrcsNode[j] == prcsTmp.prcsId){
        isExist = true;
        break;
      }
    }
    if(!isExist){
      var tmp = {};
      tmp.value = prcsTmp.prcsId;
      if(prcsTmp.prcsId != '0'){
        tmp.text =  prcsTmp.prcsId + "," + prcsTmp.prcsName;
      }else{
        tmp.text =  prcsTmp.prcsName;
      }
      disProcessTo.push(tmp);
    }
  }
  for (var i = 0 ;i < aPrcsNode.length ;i++) {
    for(var j = 0 ;j < prcsList.length ;j++){
      var prcsTmp = prcsList[j];
      if(aPrcsNode[i] && aPrcsNode[i] == prcsTmp.prcsId){
        var tmp = {};
        tmp.value = prcsTmp.prcsId;
        if(prcsTmp.prcsId != '0'){
          tmp.text =  prcsTmp.prcsId + "," + prcsTmp.prcsName;
        }else{
          tmp.text =  prcsTmp.prcsName;
        }
        processTo.push(tmp);
        break;
      }
    }
  }
  new ExchangeSelectbox({containerId:'next_process'
    , selectedArray:processTo
    , disSelectedArray:disProcessTo
    ,isSort:true 
    ,selectName:'nextProcess'
      ,selectedChange:exchangeHandler
      ,titleText:{selectedTitle:'下一步骤',disSelectedTitle:'备选步骤'}
  }); 
}
var roleList = [];
function displayAddMessage(){
  var url = requestUrl + "/getAddMessage.act";
  var addMessage = getJsonRs(url, 'flowId=' + $F('flowSeqId'));
  if(addMessage.rtState == '0'){
    $('prcsId').value = addMessage.rtData.maxPrcsId;
    var formItemTmp = addMessage.rtData.fromItem;
    for(var i = 0 ; i < formItemTmp.length ;i++){
      var tmp = {};
      tmp.value = formItemTmp[i].id;
      tmp.text = formItemTmp[i].title;
      formItem.push(tmp);
    }
    prcsList = addMessage.rtData.prcsList;
    flowList = addMessage.rtData.flowList;
    disAipList = addMessage.rtData.disAip;
    roleList = addMessage.rtData.role;
  }
  setOptions($('childFlowName'), flowList);
}
function displayEditMessage(){
  var url = requestUrl + "/getEditMessage.act";
  var editMessage = getJsonRs(url, 'flowId=' + $F('flowSeqId') + '&seqId='+ $F('seqId') + '');
  if(editMessage.rtState != '0'){
    //alert('数据有问题');
    return ;
  }
  
  var formItemTmp = editMessage.rtData.fromItem;
  for(var i = 0 ; i < formItemTmp.length ;i++){
    var tmp = {};
    tmp.value = formItemTmp[i].id;
    tmp.text = formItemTmp[i].title;
    formItem.push(tmp);
  }
  prcsList = editMessage.rtData.prcsList;
  flowList = editMessage.rtData.flowList;
  disAipList = editMessage.rtData.disAip;
  roleList = editMessage.rtData.role;
  setOptions($('childFlowName'), flowList);
  
  prcsNode = editMessage.rtData.prcsNode;
  $('prcsId').value = prcsNode.prcsId;
  $('prcsName').value = prcsNode.prcsName;
  
  
  if(prcsNode.childFlow != 0){
    displayChildFlow(editMessage);
    AutoUsers = editMessage.rtData.autoUsers;
    mailToDesc = editMessage.rtData.mailToDesc;
  }else{
    AutoUsers = editMessage.rtData.autoUsers;
    mailToDesc = editMessage.rtData.mailToDesc;
    $('prcsTo').value = prcsNode.prcsTo;
  }
}
function displayChildFlow(editMessage){
  tabs.setDisable(true , 1);
  tabs.setDisable(true , 2);
  tabs.setDisable(true , 3);
  tabs.setDisable(true , 4);
  $('processTbody').hide();
  $('childFlow').value = 1;
  $('childFlowName').value = prcsNode.childFlow;
  if(prcsNode.attach1){
    $('attach1').checked = true;
    $('attach0').checked = false;
  }else{
    $('attach1').checked = false;
    $('attach0').checked = true;
  }
  var childItem = editMessage.rtData.childItem.split(",");
  var childItemTemp = [];
  for(var j = 0 ;j < childItem.length ; j ++){
    if(childItem[j]){
      var tmp = {};
      tmp.value = childItem[j];
      tmp.text = childItem[j];
      childItemTemp.push(tmp);
    }
  }
 
  setOptions($('fldChild').getElementsByTagName('optgroup')[0] , childItemTemp);
  
  $('fldSubSpan').show();
  
  var aRelation = prcsNode.relation.split(',');
  
  for(var i = 0 ;i < aRelation.length;i++){
    if(aRelation[i].trim()){
      var tmp = aRelation[i] + " , ";
      mapadd(tmp);
    }
    
  }
  if(prcsNode.act0 == 0){
    $('overAct0').checked = true;
    $('overAct1').checked = false;
  }else{
    $('overAct0').checked = false;
    $('overAct1').checked = true;
    var list = new Array();
    for(var i = 0 ; i < prcsList.length;i++){
      var tmp = {};
      tmp.value = prcsList[i].prcsId;
      tmp.text = prcsList[i].prcsName;
      if(tmp.value != 0 && tmp.value != $('prcsId').value){
        list.push(tmp);
      }
    }
    setOptions($('prcsBack'), list , prcsNode.prcsTo);
    $('prcsTo').value = prcsNode.prcsTo + ',';
    $('backUserHo').value = prcsNode.backUserHo;
    $('backUserOp').value = prcsNode.backUserOp;
    
    var backUsers = editMessage.rtData.backUsers;
    $('backUserHoDesc').value = backUsers.backUserHoDesc;
    $('backUserOpDesc').innerHTML = backUsers.backUserOpDesc;
    $('selback1').show();
    $('selback2').show();
  }
  $('childFlowTbody').show();
  
}
function childFlowChange(value){
  if(value == '0'){
    $('fldSubSpan').hide();
    $('relationDiv').innerHTML = "";
    $('relation').value = "";
    //去掉map
  }else{
    //取得子流程formItem
    var url = requestUrl + "/getFormItem.act";
    var json = getJsonRs(url , "flowId=" + value);
    var childItem = [];
    if(json.rtState != "0"){
      return ; 
    }    
    var sChildItem = json.rtData;
    var tmp = sChildItem.split(",");
    for(var i = 0 ;i < tmp.length ;i ++){
      if(tmp){
        var obj = {};
        obj.value = obj.text = tmp[i];
        childItem.push(obj);
      }
    }
    var options = $('childFlowName').getElementsByTagName('option');
    $('prcsName').value = options[$('childFlowName').selectedIndex].innerHTML
    var group = $('fldChild').getElementsByTagName('optgroup')[0];
    delOptions(group);
    
    for(var i = 0 ;i< childItem.length ;i++){
      var option = new Element('option').update(childItem[i].text);
      option.value = childItem[i].value;
      group.appendChild(option);
    }
    $('fldSubSpan').show();
    $('relationDiv').innerHTML = "";
    $('relation').value = "";
  }
}
function clearPrcsUser(){
  clearUser('backUserHoDesc','backUserHo');
  clearUser('backUserOpDesc','backUserOp')
}
function delOptions(el){
  el.innerHTML = "";
}
function setOptions(el,aOption,value){
  for(var i = 0 ;i< aOption.length ;i++){
    var option = new Element('option').update(aOption[i].text);
    if (value && aOption[i].value == value) {
      option.selected = true;
    }
    option.value = aOption[i].value;
    el.appendChild(option);
  }
}

function exchangeHandler(ids){
  $('prcsTo').value = ids;
}
function addRelation(){
  var option1 = "",option2 = "";
  var oFldParent = $('fldParent');
  var oFldChild = $('fldChild'); 
  if(oFldParent.selectedIndex  != -1 && oFldChild.selectedIndex != -1){
    option1 = oFldParent.options[oFldParent.selectedIndex].value;
    option2 = oFldChild.options[oFldChild.selectedIndex].value;
    mapadd(option1 + " => " + option2 + " , ");
  }else {
    alert("请选择子流程的字段");
  }    
}
function mapadd(data) {
  if(!data){
    var data = "";
  }
  if($('relation').present()){
    $('relation').value += data;
  }else{
    $('relation').value = data;
  }
  var relArray = $F('relation').split(",");
  $('relationDiv').innerHTML = "";
  for(var i = 0; i < relArray.length - 1; i++){
    var imgUrl = contextPath + moduleContextPath +  "/flowdesign/viewlist/setproperty/img/remove.png" ;
    $('relationDiv').innerHTML += relArray[i] +
        "<img src='"+ imgUrl +"' onclick='mapdel("+ i +")' title=\"删除映射关系\"> ,"; 
  }
}

function mapdel(index) { 
  var newValue = "";
  var relArray = $F('relation').split(",");
  relArray[index] = "";
  for(var i = 0; i < relArray.length - 1; i++) { 
    if(relArray[i]){
      newValue += relArray[i] + ",";
    }
  }
  $('relation').value = newValue;
  mapadd();
}

function overActClick(){
  
  if($('overAct0').checked){
    $('prcsBack').value = '0';
    $('backUserOp').value = '';
    $('backUserOpDesc').innerHTML  = '';
    $('backUserHo').value = '';
    $('backUserHoDesc').value = '';
    
    $('selback1').hide();
    $('selback2').hide();
  }else{
    var options = $('prcsBack').getElementsByTagName('option');
    if(options.length <= 1 ){
      for(var i = 0 ;i< prcsList.length ;i++){
        if(isEdit){
          var flag = (prcsNode.prcsId != prcsList[i].prcsId);
        }else{
          var flag = true;
        }
        if(flag 
            && prcsList[i].prcsId != '0' ){
          var option = new Element('option').update(prcsList[i].prcsId + "、" + prcsList[i].prcsName);
          option.value = prcsList[i].prcsId;
          $('prcsBack').appendChild(option);
        }
      }
    }
    $('selback1').show();
    $('selback2').show();
  }
  
}

function commit(){
  if(!$('prcsId').present()){
    alert('序号不能为空');
    return ;
  }
  var reg1 = /^[0-9]*$/;
  if(!reg1.test($('prcsId').value)){
    alert("序号只能输入数字！");
    return ;
  }

  if($F('childFlow') == '0'){
    if(!$('prcsName').present()){
      alert('步骤名称不能为空');
      return ;
    }
    var reg = /['"]/g;
    if ($('prcsName').value.match(reg)) {
      alert("步骤名称不能有\"'\"和\"\"\"字符！");
      $('prcsName').focus();
      return ;
    }
  }else{
    if($F('childFlowName') == '0'){
      alert('请选择子流程类型');
      return;
    }
    if ($('overAct1').checked 
        && $('prcsBack').value == "0") {
      alert('请选择返回的步骤！');
      return;
    }
  }
  if(isEdit){
    var url = requestUrl +  "/doUpdate.act";
  }else{
    var url = requestUrl + "/doSave.act";
  }
  var timeCtrl = $('timeOut');
  if (timeCtrl) {
    var timeOut = timeCtrl.value;
    if (timeOut && (!isNumber(timeOut) || parseFloat(timeOut) < 0)) {
      alert("办理时限只能为正小数!")
      return ;
    }
  }
  var json = getJsonRs(url, $('workflowForm').serialize());
  if(json.rtState == '0'){
    if(!isList){
      try{
        parent.opener.location.reload();
      } catch(e) {
        
      }
      window.close();
    }else{
      history.back();
    }
  }else{
    alert(json.rtMsrg);
  }
}
function showInfo(){
  //根据标签页设置智能选人 
  var id = arguments[0];
  if(id == 'autoSelect'){
    $('openedAutoSelect').value = 1;
    setAutoSelectPage();
  }
  if(id == 'flowDispatch'){
    $('openedFlowDispatch').value = 1;
    if(isEdit){
      setFlowDispatchPage();
    }
  }
  if(id == 'warnDispatch'){
    $('openedWarnDispatch').value = 1;
    if(isEdit){
      setWarnDispatchPage();
    }
  }
  if(id == 'otherDispatch'){
    $('openedOtherDispatch').value = 1;
    setOptions($('dispAip'), disAipList);
    if(isEdit){
      setOtherDispatchPage();
    }
  }
}
function setOtherDispatchPage(){
  $('timeOut').value = prcsNode.timeOut;
  if(prcsNode.timeOutType == 1){
    $('timeOutType1').checked = false;
    $('timeOutType2').checked = true;
  }else{
    $('timeOutType2').checked = false;
    $('timeOutType1').checked = true;
  }
  if(prcsNode.timeExcept.charAt(0) == '0'){
    $('timeExcept1').checked = false;
  }else{
    $('timeExcept1').checked = true; 
  }
  if(prcsNode.timeExcept.charAt(1) == '0'){
    $('timeExcept2').checked = false;
  }else{
    $('timeExcept2').checked = true; 
  }
  
  setSelectedValue('extend' , prcsNode.extend);
  setSelectedValue('extend1' , prcsNode.extend1);
  setSelectedValue('dispAip' , prcsNode.dispAip);
  $('plugin').value = prcsNode.plugin;
}
function setSelectedValue(select , value) {
  var option = $(select).getElementsByTagName("option");
  for (var i = 0 ;i < option.length ;i ++) {
    var op = option[i];
    if (op.value == value) {
      op.selected = true;
    }
  }
}
function setWarnDispatchPage(){
  var remindFlag = prcsNode.remindFlag;
  if((remindFlag&0x200)>0){
    $('remindOrnot').checked = true;
    $('remindfld').disabled = false;
    if((remindFlag&0x100)>0){
      $('smsRemindNext').checked = true;
    }
    if((remindFlag&0x80)>0){
      $('sms2RemindNext').checked = true;
    }
    if((remindFlag&0x40)>0){
      $('webMailRemindNext').checked = true;
    }
    if((remindFlag&0x20)>0){
      $('smsRemindStart').checked = true;
    }
    if((remindFlag&0x10)>0){
      $('sms2RemindStart').checked = true;
    }
    if((remindFlag&8)>0){
      $('webMailRemindStart').checked = true;
    }
    if((remindFlag&4)>0){
      $('smsRemindAll').checked = true;
    }
    if((remindFlag&2)>0){
      $('sms2RemindAll').checked = true;
    }
    if((remindFlag&1)>0){
      $('webMailRemindAll').checked = true;
    }
  }
  $('userDesc').innerHTML = mailToDesc;
  $('user').value = prcsNode.mailTo;
}
function setFlowDispatchPage(){
  if ($('autoType')) {
    if ( $('autoType').value == '0') {
      setTip(true);
    } else {
      setTip(false);
    }
  } else {
    if (prcsNode.autoType == '0') {
      setTip(true);
    } else {
      setTip(false);
    }
  }
  $('topDefault').value = prcsNode.topDefault;
  $('userLock').value = prcsNode.userLock;
  
  $('feedBack').value = prcsNode.feedBack;
  if(prcsNode.feedBack == 1){
    $('signLookDiv').hide();
  }else{
    $('signLook').value = prcsNode.signLook;
  }
  $('turnPriv').value = prcsNode.turnPriv;
  $('allowBack').value = prcsNode.allowBack;
  $('syncDeal').value = prcsNode.syncDeal;
  $('gatherNode').value = prcsNode.gatherNode;
}
function setAutoSelectPage(){
  if(isEdit){
    $('userFilter').value = prcsNode.userFilter;
    $('autoType').value = prcsNode.autoType;
    var list = new Array();
    //修改时，步骤有个当前步骤和增加时，的当步骤的列表不一样
    for(var i = 0 ; i < prcsList.length;i++){
      if(prcsList[i].prcsId != prcsNode.prcsId){
        var tmp = {};
        tmp.value = prcsList[i].prcsId;
        tmp.text = prcsList[i].prcsName;
        if(tmp.value != 0){
          list.push(tmp);
        }
      }
    }
    setOptions($('autoBaseUser'), list);
  }else{
    //增加时
    var list = new Array();
    for(var i = 0 ; i < prcsList.length;i++){
      var tmp = {};
      tmp.value = prcsList[i].prcsId;
      tmp.text = prcsList[i].prcsName;
      if(tmp.value != 0){
        list.push(tmp);
      }
    }
    setOptions($('autoBaseUser'), list);
  }
  var list = new Array();
  for(var i = 0 ; i < prcsList.length;i++){
    var tmp = {};
    tmp.value = prcsList[i].prcsId;
    tmp.text = prcsList[i].prcsName;
    if(tmp.value != 0){
      list.push(tmp);
    }
  }
  var list2 = new Array();
  for(var i = 0 ; i < roleList.length;i++){
    var tmp = {};
    tmp.value = roleList[i].seqId;
    tmp.text = roleList[i].roleName;
    if(tmp.value != 0){
      list2.push(tmp);
    }
  }
  setOptions($('autoPrcsUser'), list);
  setOptions($('formListItem'), formItem);
  setOptions($('roleListItem') , list2);
  
  if(isEdit){
    var autoType = $F('autoType');
    if( autoType == '2'
       || autoType == '4'
         || autoType == '6'){
      $('autoBaseUser').value = prcsNode.autoBaseUser;
      $('baseUserDiv').show();
    }else if(autoType == '7'){
      $('formListItem').value = prcsNode.formListItem;
      $('formItemDiv').show();
    }else if(autoType == '8'){
      $('autoPrcsUser').value = prcsNode.autoPrcsUser;
      $('prcsUserDiv').show();
    }else if(autoType == '3'){
      $('autoUserHo').value = prcsNode.autoUserHo;
      $('autoUserOp').value = prcsNode.autoUserOp;
      $('autoUserHoName').value = AutoUsers.autoUserHoName;
      $('autoUserOpDesc').innerHTML = AutoUsers.autoUserOpDesc;
      $('autoPrcsUser').value = prcsNode.autoPrcsUser;
      $('autoUsersetDiv').show();
    }else if(autoType == '20'){
      $('roleListItem').value = prcsNode.autoSelectRole;
      $('roleItemDiv').show();
    }
  }
  
}
function prcsTypeChange(){
  if($F('childFlow') == '0'){
    tabs.setDisable(false , 1);
    tabs.setDisable(false , 2);
    tabs.setDisable(false , 3);
    tabs.setDisable(false , 4);
    $('childFlowTbody').hide();
    $('processTbody').show();
  }else{
    tabs.setDisable(true , 1);
    tabs.setDisable(true , 2);
    tabs.setDisable(true , 3);
    tabs.setDisable(true , 4);
    $('processTbody').hide();
    $('childFlowTbody').show();
    
  }
}
function addUserAfterValidate(backUserHo,backUserHoDesc,backUserOp ,backUserOpDesc){
  if($F('prcsBack') == '0'){
    alert("请选择返回的流程步骤"); 
    return ;
  }
  addUser(backUserHo,backUserHoDesc,backUserOp ,backUserOpDesc , $F('prcsBack'));
}
function addUser(backUserHo,backUserHoDesc,backUserOp ,backUserOpDesc , par){
  this.UserHo = $(backUserHo);
  this.UserHoDesc = $(backUserHoDesc);
  this.UserOp = $(backUserOp);
  this.UserOpDesc = $(backUserOpDesc);
  
  if(par){
    this.selectedProc = par;
  }else{
    this.selectedProc = "";
  }
  var chooseType = window.showModalDialog(contextPath + moduleContextPath +  "/flowdesign/viewlist/select/FlowUserSelect.jsp",window,"dialogLeft=480px;dialogTop=350px;dialogWidth=510px;dialogHeight=400px;status=no;resizable=no");
}

function clearUser(input, hidden){
  if($(input).tagName == 'INPUT'){
  $(input).value = "";  
  }else if($(input).tagName == 'TEXTAREA'){
  $(input).innerHTML = '';  
  }
  $(hidden).value = "";
}

function multiUserSelect(userHi, userDesc){
  UserHi = $(userHi) ;
  UserDesc = $(userDesc);
  var chooseType = window.showModalDialog(contextPath + "/core/funcs/doc/flowdesign/viewlist/select/MultiUserSelect.jsp",window,"dialogLeft=500px;dialogTop=350px;dialogWidth=500px;dialogHeight=400px;status=no;resizable=no"); 
}
function setTip(flag) {
  var lockinfo = $('lockinfo');
  if (!lockinfo) {
    return ;
  }
  if (flag) {
    lockinfo.update("是否允许修改主办人相关选项");
  } else {
    lockinfo.update("是否允许修改主办人相关选项及默认经办人");
  }
}
function autoTypeSet(select){
  if (select.value == '0') {
    setTip(true);
  } else {
    setTip(false);
  }
  if(select.value == '2' 
    || select.value == '6'
      || select.value == '4'){
    $('prcsUserDiv').hide();
    $('autoUsersetDiv').hide();
    $('formItemDiv').hide();
    $('roleItemDiv').hide();
    $('baseUserDiv').show();
  }else if(select.value == '1' 
    || select.value == '0'
      || select.value == '5'){
    $('prcsUserDiv').hide();
    $('formItemDiv').hide();
    $('roleItemDiv').hide();
    $('autoUsersetDiv').hide();
    $('baseUserDiv').hide();
  }else if(select.value == '7'){
    $('prcsUserDiv').hide();
    $('autoUsersetDiv').hide();
    $('roleItemDiv').hide();
    $('baseUserDiv').hide();
    $('formItemDiv').show();
  }else if(select.value == '3'){
    $('prcsUserDiv').hide();
    $('roleItemDiv').hide();
    $('formItemDiv').hide();
    $('baseUserDiv').hide();
    $('autoUsersetDiv').show();
  }else if(select.value == '8'){
    $('formItemDiv').hide();
    $('roleItemDiv').hide();
    $('baseUserDiv').hide();
    $('autoUsersetDiv').hide();
    $('prcsUserDiv').show();
  }else if(select.value == '20'){
    $('formItemDiv').hide();
    $('baseUserDiv').hide();
    $('autoUsersetDiv').hide();
    $('prcsUserDiv').hide();
    $('roleItemDiv').show();
  }
  
}
function signChange(select){
  if(select.value == '1'){
    $('signLookDiv').hide();
  }else{
    $('signLookDiv').show();
  }
}
function closeWindow() {
  //closeModalWindow('ProcessProperty');
 
  window.close();
}
function selPlugin(){
  var url = contextPath + moduleSrcPath +  "/act/YHPluginAct/getPluginList.act" ;
  loc_y=loc_x=200;
  loc_x=document.body.scrollLeft+event.clientX-event.offsetX;
  loc_y=document.body.scrollTop+event.clientY-event.offsetY;
  LoadDialogWindow(url,self,loc_x, loc_y, 380, 350);
}
function LoadDialogWindow(URL, parent, loc_x, loc_y, width, height){ 
  window.showModalDialog(URL,parent,"edge:raised;scroll:1;status:0;help:0;resizable:1;dialogWidth:"+width+"px;dialogHeight:"+height+"px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px",true);
}

String.prototype.trim = function(){
  return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.ltrim = function(){
  return this.replace(/(^\s*)/g,"");
}
String.prototype.rtrim = function(){
  return this.replace(/(\s*$)/g,"");
}