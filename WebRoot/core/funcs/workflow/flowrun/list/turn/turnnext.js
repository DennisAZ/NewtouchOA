var requestUrl = contextPath + "/yh/core/funcs/workflow/act/YHWorkTurnAct";
var isOnloadFinish = false;
var divStyle = {border:'1px solid #69F',width:'90px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};

var notAllFinist; //是否所有办理完毕
var nextPrcsList = [];
var isAllowTurn = false;//是否允许主办人强制转交

var firstCondition = -1; //第一个符合转入条件的
var flowName = "";
var runName = "";
var syncDeal = "0";
var gatherNode = "0";

function setTurnNext() {
  $('divSmsNex').show();
  $('smsContent').value = tooltipMsg2  + runName;
  $('operateButton').onclick = function(){
    checkTurn();
  }
  $('operateButton').value = "确认";
}
function selectAllPrcs(){
  //如果原来有结束步骤
  if ($("prcsChoose").value == "0,") {
    setEndNodeClose();
    $("prcsChoose").value = "";
    setTurnNext();
  }
  for (var i = 0 ; i < nextPrcsList.length ;i++) {
    var prcs = nextPrcsList[i];
    if (prcs.notInPass) {
      continue;
    }
    if (prcs.prcsId == '0') {
      $('prcsCheck_' + prcs.prcsId ).checked = false;
      var boxDiv = $('boxDiv_' + prcs.prcsId );
      setStyle ( boxDiv , false ) ;
    } else {
      $('prcsCheck_' + prcs.prcsId ).checked = true;
      var boxDiv = $('boxDiv_' + prcs.prcsId );
      setStyle ( boxDiv , true ) ;
      setUserSelect ( prcs ) ;
      $("prcsChoose").value += prcs.prcsId + ",";
    }
  }
}
function cancelAllPrcs(){
  for (var i = 0 ; i < nextPrcsList.length ;i++) {
    var prcs = nextPrcsList[i];
    if (prcs.notInPass) {
      continue;
    } else {
      $('prcsCheck_' + prcs.prcsId ).checked = false;
      var boxDiv = $ ( 'boxDiv_' + prcs.prcsId );
      setStyle(boxDiv , false ) ;
      if (prcs.prcsId != '0')
        $('userSelectTr_' + prcs.prcsId).hide();
    }
  }
  $("prcsChoose").value = "";
}
function doInit(){
  skinObjectToSpan(flowrun_list_turn_turnnext);
  var url = requestUrl + "/getTurnData.act";
  var json = getJsonRs(url , 'isManage='+isManage+'&runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs );
  if(json.rtState == '0'){
    var data = json.rtData;
    //转出条件检查
    if (!data || data.notOutPass != undefined) {
      if (data.notOutPass == undefined || !data.notOutPass) {
        data.notOutPass = "条件表达式错误";
      }
      $('outMsgInfo').update(data.notOutPass);
      $("noOutPass").show();
      return ;
    }
    flowName = data.flowName;
    runName = data.flowRun.runName;
    $('flowNameSpan').update(data.flowName + "- 转交下一步骤");//更新flowNameSpan
    $('runNameSpan').update(data.flowRun.runName);//更新runNameSpan
    $('prcsNameSpan').update(data.prcsName);//更新prcsNameSpan
    $('userNameStrSpan').update(data.flowRun.userNameStr); //更新所有经办人员的办理状态

    notAllFinist = data.flowRun.notAllFinish;//是否办理完毕
    isAllowTurn = data.flowRun.isAllowTurn;//是否允许强制转交
    syncDeal = data.syncDeal;
    gatherNode =  data.gatherNode;
    if (gatherNode == "1") {
      $('prcsList').hide();
      $('need_remind').hide();
      $('tableControl').hide();
      $('nextPrcsTr').hide();
      $('gatherMsg').show();
      $('outPass').show();
      return;
    }
    nextPrcsList = data.nextPrcs;
    var prcsTo = "";
    for ( var i = 0 ;i < nextPrcsList.length ;i ++ ){
      var tmp = nextPrcsList[i];
      appearBoxDiv(tmp , i );
      setPrcsTr(tmp);
      prcsTo = tmp.prcsId + ",";
    }
    //设置全选和取消
    if (syncDeal != "0") {
      var str = "<input value='全选' type='button' onclick='selectAllPrcs()' class='BigButton'/>&nbsp;<input value='全消' onclick='cancelAllPrcs()' type='button' class='BigButton'/>"
      //强制并发
      if (syncDeal == "2") {
        str = "&nbsp;此步骤强制并发";
      } else {
        str += "&nbsp;此步骤允许并发";
      }
      $('nextPrcsTd').insert(str);
    }
    //设置手机短信提醒
    if (data.sms2PrivNext) {
      $('sms2RemindNext').show();
      $('sms2RemindNextImg').show();
    }
    if (data.sms2PrivStart) {
      $('sms2RemindStart').show();
      $('sms2RemindStartImg').show();
    }
    if (data.sms2PrivAll) {
      $('sms2RemindAll').show();
      $('sms2RemindAllImg').show();
    }
    //有一个符合转入条件
    if (firstCondition != -1) {
      $('prcsTo').value = prcsTo;
      //默认选中第一个
      if (syncDeal == "0") {
        var firstPrcsNode = nextPrcsList[firstCondition];
        if(firstPrcsNode){
          $("prcsChoose").value = firstPrcsNode.prcsId + ",";
          setUserSelect(firstPrcsNode);
        }
      } else {
        selectAllPrcs(nextPrcsList);
      }
      var remindFlag = data.remindFlag;
      setRemindFlag(remindFlag , data.smsRemind , data.sms2Remind);
    } else {
      setConditionDiv();
      $('allNoPassTr').show();
    }
    $("outPass").show();
  }
}
/**
 * 设置提醒标志
 */
function setRemindFlag(remindFlag , smsRemind , sms2Remind) {
  if((remindFlag&0x100)>0){
    $('smsRemindNext').checked = true;
  } else if (remindFlag == 0 && findId(smsRemind , '7')) {
    $('smsRemindNext').checked = true;
  }
  if((remindFlag&0x40)>0){
    $('webMailRemindNext').checked = true;
  }
  if((remindFlag&0x20)>0){
    $('smsRemindStart').checked = true;
  }  else if (remindFlag == 0 && findId(smsRemind , '40')) {
    $('smsRemindStart').checked = true;
  }
  if((remindFlag&8)>0){
    $('webMailRemindStart').checked = true;
  }
  if((remindFlag&4)>0){
    $('smsRemindAll').checked = true;
  }  else if (remindFlag == 0 && findId(smsRemind , '41')) {
    $('smsRemindAll').checked = true;
  }
  if((remindFlag&1)>0){
    $('webMailRemindAll').checked = true;
  }
  if ((remindFlag&0x80)>0) {
    $('sms2RemindNext').checked = true;
  } else if (remindFlag == 0 && findId(sms2Remind , '7')) {
    $('sms2RemindNext').checked = true;
  }
  if ((remindFlag&0x10)>0) {
    $('sms2RemindStart').checked = true;
  }  else if (remindFlag == 0 && findId(sms2Remind , '40')) {
    $('sms2RemindStart').checked = true;
  }
  if ((remindFlag&2)>0) {
    $('sms2RemindAll').checked = true;
  } else if (remindFlag == 0 && findId(sms2Remind , '41')) {
    $('sms2RemindAll').checked = true;
  }
}

/**
 * 根据一个步骤对象.画出一个div,默认第一个选中
 * @param prcsNode
 * @return
 */
function appearBoxDiv(prcsNode  , i){
  var str = "g";
  if(i == 0){
    str = "r";
    //默认第一个为选中,给prcsChoose赋值,
    //$("prcsChoose").value = prcsNode.prcsId + ",";
  }
  var boxDiv = new Element("div" , {"class":"box_" + str});
  boxDiv.id = "boxDiv_" + prcsNode.prcsId;
  var div = new Element("div" , {"class": str +"1"});
  boxDiv.appendChild(  div );
  div = new Element("div" , {"class": str + "2"});
  boxDiv.appendChild(  div );
  
  var contentDiv = new Element ( "div" , {"class":"content_" + str} );
  var prcsTitleSpan = new Element("span");
  if (!prcsNode.notInPass) {
    if (syncDeal != "2") {
      prcsTitleSpan.observe ( 'click' , changePrcs.bindAsEventListener(  this,  prcsNode) );
    }
  } 
  prcsTitleSpan.id = "prcsTitleSpan_" + prcsNode.prcsId;
  
  var checkbox = "";
  if (prcsNode.notInPass) {
    checkbox = prcsNode.prcsName;
    prcsTitleSpan.title = prcsNode.notInPass;
  } else {
    //记录第一个满足条件的步骤
    if (firstCondition == -1) {
      firstCondition = i;
      checkbox = "<input style='display:none' type=\"checkbox\" checked id=\"prcsCheck_"+ prcsNode.prcsId +"\"/>";
    } else {
      checkbox = "<input style='display:none' type=\"checkbox\" id=\"prcsCheck_"+ prcsNode.prcsId +"\"/>";
    }
    prcsTitleSpan.title = "序号：" + prcsNode.prcsId;
  }
  
  var checkBoxSpan = new Element("span");
  if (!prcsNode.notInPass) {
    checkBoxSpan.className = (i == firstCondition ? 'check1' : 'check0') ;
  }
  checkBoxSpan.update(checkbox);
  if ( i == firstCondition ) { 
    prcsTitleSpan.style.color = 'red';
  } else {
    prcsTitleSpan.style.color = 'green';
  }
  var spanText = "";
  if (prcsNode.notInPass) {
    spanText = "条件不符";
  } else {
    spanText = "<img src=\"" + imgPath +  "/arrow_down.gif\"/>" + prcsNode.prcsName +"</span>";
  }
  
  var prcsNameSapn = new Element ("span" ).update (spanText);
  prcsNameSapn.style.textDecoration = "underline";
  prcsNameSapn.style.cursor = "pointer";
  prcsTitleSpan.appendChild (  checkBoxSpan );
  prcsTitleSpan.appendChild (  prcsNameSapn );
  contentDiv.appendChild (  prcsTitleSpan );
  boxDiv.appendChild (  contentDiv );
  div = new Element ( "div" , {"class": str +"3"});
  boxDiv.appendChild (  div );
  div = new Element ( "div" , {"class": str + "4"});
  boxDiv.appendChild (  div );
  $('nextPrcsTd').appendChild(boxDiv);
}
/**
 * 当点击一个步骤时
 */
function changePrcs(){
  var oldPrcsId = $F("prcsChoose");//原来的prcsId
  var prcsNode = arguments[1]; //取得对应的流程步骤对象

  var checkbox = $('prcsCheck_' + prcsNode.prcsId ) ;
  if ( syncDeal == "0" ) {
    oldPrcsId = oldPrcsId.substring(0 , oldPrcsId.length - 1);
    if (findId(oldPrcsId,prcsNode.prcsId)) { //如果原来为选中
      return ;
    }else{
      $( 'prcsCheck_' + prcsNode.prcsId).checked = true;
      $( 'prcsCheck_' + oldPrcsId).checked = false;
    }
    var oldBoxDiv = $ ( 'boxDiv_' + oldPrcsId ) ;
    setStyle ( oldBoxDiv , false );
    var boxDiv = $ ( 'boxDiv_' + prcsNode.prcsId );
    setStyle ( boxDiv , true ) ;
    
    $("prcsChoose").value = prcsNode.prcsId + ",";
    $("userSelectTr_" + oldPrcsId).hide();
    setUserSelect(prcsNode) ;
  } else {
    if (prcsNode.prcsId == '0') {
      if (oldPrcsId == "0,") {
        setEndNodeClose();
        setEndTrClose(prcsNode.parentRun , prcsNode.prcsBack)
        oldPrcsId = "";
        return;
      }
      var ss = oldPrcsId.split(",");
      for (var i = 0 ;i < ss.length ;i++) {
        var s = ss[i];
        if (!s) {
          continue;
        }
        var oldBoxDiv = $ ( 'boxDiv_' + s) ;
        setStyle ( oldBoxDiv , false );
        $("userSelectTr_" + s).hide();
      }
      setUserSelect(prcsNode) ;
      setEndNodeShow();
      setEndTrShow(prcsNode.parentRun , prcsNode.prcsBack)
      return ;
    }
    //如果原来有结束步骤

    if (oldPrcsId == "0,") {
      setEndNodeClose();
      $('userSelectTr_0').hide();
      oldPrcsId = "";
    }
    if (findId(oldPrcsId,prcsNode.prcsId)) { //如果原来为选中
      var oldBoxDiv = $ ( 'boxDiv_' + prcsNode.prcsId) ;
      setStyle ( oldBoxDiv , false );
      $("userSelectTr_" + prcsNode.prcsId).hide();
      $("prcsChoose").value = getOutofStr(oldPrcsId , prcsNode.prcsId);
    }else{
      $('prcsCheck_' + prcsNode.prcsId ).checked = true;
      var boxDiv = $ ( 'boxDiv_' + prcsNode.prcsId );
      setStyle ( boxDiv , true ) ;
      setUserSelect(prcsNode ) ;
      $("prcsChoose").value += prcsNode.prcsId + ",";
    }
  }
}
 function setEndTrClose(parentRun ,prcsBack) {
   if (parentRun != '0' && prcsBack && prcsBack != 0) {
     $('userSelectTr_0').hide();
   }
 }
 function setEndTrShow(parentRun , prcsBack) {
   if (parentRun != '0' && prcsBack && prcsBack != 0) {
     $('userSelectTr_0').show();
   } else {
     $('userSelectTr_0').hide();
   }
 }
 function setEndNodeClose() {
   var oldBoxDiv = $ ('boxDiv_0') ;
   setStyle ( oldBoxDiv , false );
   $("prcsChoose").value = "";
 }
 function setEndNodeShow() {
   var oldBoxDiv = $ ('boxDiv_0') ;
   setStyle ( oldBoxDiv , true );
   $("prcsChoose").value = "0,";
 }
/**
 * 设置boxDiv的样式,isRed设置是否为红
 * 
 */
function setStyle( el,  isRed){
  var str = "";
  if(isRed){
    str = "r";
  }else{
    str = "g";
  }
  var divList = el.getElementsByTagName("div");
  divList[0].className = str +"1";
  divList[1].className = str +"2";
  divList[2].className = "content_" + str;
  divList[3].className = str +"3";
  divList[4].className = str +"4";
  
  var prcsTitleSpan = divList[2].firstChild;
  
  var checkBoxSpan = prcsTitleSpan.firstChild;
  if(isRed){
    prcsTitleSpan.style.color = 'red';
    checkBoxSpan.className = "check1";
  }else{
    prcsTitleSpan.style.color = 'green';
    checkBoxSpan.className = "check0";
  }
}

function setUserSelect( prcsNode ){
  if(prcsNode.prcsId == '0'){
    hiddenAll();
    if (prcsNode.parentRun != '0' && prcsNode.prcsBack && prcsNode.prcsBack != 0) {
      $("userSelectTr_0").show();
    }
    $('smsContent').value = tooltipMsg1   + runName;
    $('operateButton').onclick = function(){
      if(notAllFinist){
        //不允许强制转交        if (!isAllowTurn && !isManage) {
          alert("经办人[" + notAllFinist + "]尚未办理完毕,不能转交流程!");
          return ;
        } else {
          if (!confirm("经办人[" + notAllFinist + "]尚未办理完毕,确认要转交下一步骤么?")) {
            return ;
          }
        }
      }
      //结束流程
      if (prcsNode.prcsBack && prcsNode.prcsBack != 0) {
        var topFlag = $F('topFlag_0');
        if(!$("prcsUser_0").present()){
          alert('请指定好所选步骤的经办人!');
          return ;
        }
        if(topFlag == '0'){
          if(!$("prcsOpUser_0").present()){
            alert('请指定好所选步骤的主办人!');
            return ;
          }
        }
      }
      var url = requestUrl + "/turnNext.act";
      var json = getJsonRs(url , $('turnForm').serialize());
      if(json.rtState == '0'){
        location = "../index.jsp";
      }else{
        alert(json.rtMsrg);
      }
    }
    $('operateButton').value = "结束流程";
    $('divSmsNex').hide();
  }else{
    if (syncDeal == "0") {
      //隐藏所有

      //hiddenAll();
    }
    $('userSelectTr_' + prcsNode.prcsId).show();
    setTurnNext();
  }
  //userFilter = prcsNode.userFilter;
}

function hiddenAll() {
  var prcsList = $('prcsList').childNodes;
  for (var i = 0 ;i < prcsList.length ; i++) {
    var prcs = prcsList[i];
    $(prcs).hide();
  }
}
/**
 * 根据给定的名字,将数据拼成"用户名<img id="deleteOpUser_id" align="absmiddle" >"的形式
 * 
 */
function setUserName(names , ids , hasRemove , id){
  var aNames = names.split(",");
  var aIds = ids.split(",");
  var result = "";
  for(var i = 0 ;i < aNames.length ;i ++){
    var tmp = aNames[i];
    if(tmp){
      if(hasRemove){
        result += "<span>" + tmp + "<img id=\"userRemove-"+ aIds[i] +"-"+id+"\" align=\"absmiddle\" onclick=\"javascript:cancelUser(this,'"+ aIds[i] +"',false , '"+id+"');\" src=\"" + imgPath + "/remove.png\"/>&nbsp;</span>";
      }else{
        result += "<span>" + tmp + "&nbsp;</span>"
      }
    }
  }
  return result;
}
/**
 * 清空
 */
function cancelAll(prcsId){
  $('prcsOpUserNameSpan_' + prcsId).update("");
  $('prcsUserNameSapn_'+ prcsId).update("");
  
  $('prcsUser_'+ prcsId).value = "";
  $('prcsOpUser_'+ prcsId).value = "";
}
/**
 * 删除单个用户
 * 对应图片 , id为userId, isOp是否为主办

 */
function cancelUser(img , id , isOp , prcsId){
  if(isOp){
    $("prcsOpUser_" + prcsId).value = "";
    img.parentNode.parentNode.removeChild(img.parentNode);
    return ;
  }
  //从用户id中移出id
  var prcsUser = $F('prcsUser_' + prcsId);
  if(id == $("prcsOpUser_"+ prcsId).value){
    $("prcsOpUser_" + prcsId).value = "";
    $('prcsOpUserNameSpan_' + prcsId).update("");
  }
  prcsUser = getOutofStr(prcsUser , id);
  $('prcsUser_'+ prcsId).value = prcsUser;
  img.parentNode.parentNode.removeChild(img.parentNode);  
}
/**
 * 从字符串str中移出s
 */
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
/**
 * 点击菜单执行的函数

 */
function flagMenuAction(){
  //取出flag
  var flag = arguments[2];
  var obj = arguments[3]; 
  var prcsId = obj.prcsId;
  $('topFlag_'+ prcsId).value = flag;
  if(flag == '2'){
    $('topFLagSpan_'+ prcsId).update("无主办人会签：");
  }else if(flag == '1'){
    $('topFLagSpan_'+ prcsId).update("先接收为主办：");
  }else{
    $('topFlag_'+ prcsId).value = '0';
    $('topFLagSpan_'+ prcsId).update("主办人：");
  }
  //不是主办人时,要清空主办人
  if(flag != 0){
    $('prcsOpUser_' + prcsId).value = "" ; 
    $("prcsOpUserNameSpan_"+ prcsId).update("");
  }
}
function selectUser(){
      
}
var isCanSend = true;
function checkTurn(){
  if (!isCanSend) {
    return;
  }
  var prcsChoose = $F('prcsChoose');
  if(!$("prcsChoose").present()){
    alert('请至少选择一个步骤进行转交!!');
    return ;
  }
  var aStr = prcsChoose.split(',');
  for(var i = 0 ;i < aStr.length ; i++){
    if(aStr[i]){
      var topFlag = $F('topFlag_' + aStr[i]);
      if(!$("prcsUser_" + aStr[i]).present()){
        alert('请指定好所选步骤的经办人!');
        return ;
      }
      if(topFlag == '0'){
        if(!$("prcsOpUser_"  + aStr[i]).present()){
          alert('请指定好所选步骤的主办人!');
          return ;
        }
      }
    }
  }
  if(notAllFinist){
    //不允许强制转交
    if (!isAllowTurn && !isManage) {
      alert("经办人[" + notAllFinist + "]尚未办理完毕,不能转交流程!");
      return ;
    } else {
      if (!confirm("经办人[" + notAllFinist + "]尚未办理完毕,确认要转交下一步骤么?")) {
        return ;
      }
    }
  }
  var url = requestUrl + "/turnNext.act";
  //alert(1);
  isCanSend = false;
  var json = getJsonRs(url , $('turnForm').serialize());
  if(json.rtState == '0'){
    TurnNext_forwardPage();
  }else if (json.rtState == '3'){
    isCanSend = true;
    //$('operateButton').disable = true;
    alert(json.rtMsrg);
  }
}
function TurnNext_forwardPage() {
  if (isManage) {
    location = contextPath + "/core/funcs/workflow/flowrun/manage/index.jsp?skin="+ skin +"&sortId=" + sortId;
  } else {
    location = "../index.jsp?skin="+skin+"&sortId=" + sortId;
  }
}
/**
 * 当所有步骤没有一个满足条件时
 * @return
 */
function setConditionDiv() {
 // $("selectPrcsTitle").hide();
  //$('userSelectTr').hide();
  $('need_remind').hide();
  $('tableControl').hide();
}
function findId (str , s) {
  if (!str) {
    return false; 
  } else {
    var ss = str.split(",");
    for (var i = 0 ;i < ss.length ; i++) {
      var tmp = ss[i] ;
      if (tmp && tmp == s) {
        return true;
      }
    }
    return false;
  }
}

function showMenu(event , id){
  var topFlagMenu = [{prcsId:id,name:'<div  style="padding-top:5px;margin-left:10px">主办人<div>',action:flagMenuAction,extData:'0'}
    ,{prcsId:id,name:'<div  style="padding-top:5px;margin-left:10px">无主办人会签<div>',action:flagMenuAction,extData:'2'}
    ,{prcsId:id,name:'<div  style="padding-top:5px;margin-left:10px">先接收为主办<div>',action:flagMenuAction,extData:'1'}];
  var menu = new Menu({bindTo:$("topFlagA_" + id) , menuData:topFlagMenu , attachCtrl:true} ,divStyle);
  menu.show(event);
}
/**
* 
* @param i
* @return
*/
function getPrcsText(tmp) {
 var i = tmp.prcsId;
 var nextPrcsNameA = "";
 var ss = "";
 if (tmp.prcsId == '0' && tmp.parentRun != '0' && tmp.prcsBack) {
   ss = ",'" + tmp.parentFlowId+ "','" + tmp.prcsBack + "'";
 }
 var userFilter = tmp.userFilter;
 if (tmp.noPriv) {
   nextPrcsNameA =  tmp.prcsName + "-<font color=red>尚未指定该步骤的经办权限，请与管理员联系 </forn>";
   selecteUserButton = "alert('尚未指定该步骤的经办权限，请与管理员联系')";
 }else{
   nextPrcsNameA =  tmp.prcsName ;
   selecteUserButton = "addUser("+ i +" , '"+userFilter+"', '"+tmp.childFlow+"'"+ss+")";
 }
 var topFlag = tmp.topFlag;
 var topFLagSpan = "";
 if(topFlag == '2'){
   topFLagSpan = "无主办人会签：";
 }else if(topFlag == '1'){
   topFLagSpan = "先接收为主办：";
 }else{
   topFlag = "0";
   topFLagSpan = "主办人：";
 }
 var isOrgClearShow = "none";
 var topFlagA = "";
 if ((tmp.prcsId == '0' && tmp.parentRun != '0' && tmp.prcsBack) || tmp.childFlow != '0') {
   isOrgClearShow = "";
 } else {
   if(!tmp.userLock){
     topFlagA = "showMenu(event , "+ i +")"; 
     isOrgClearShow = "";
   }else{
     topFlagA = "alert('不允许修改主办人相关选项')"; 
     if(tmp.isAutoSelect){
       selecteUserButton = "alert('不允许修改默认主办人和经办人')";
       isOrgClearShow = "none";
     }else{
       selecteUserButton = "addUser("+i+" ,'"+userFilter+"', '"+tmp.childFlow+"'"+ss+")";
       isOrgClearShow = "";
     }
   }
 }
 
 //为主办人时
 var prcsOpUserNameSpan = "";
 var prcsOpUser = "";
 if(topFlag == '0'){
   if(tmp.prcsOpUser){
     prcsOpUser = tmp.prcsOpUser;
     if(tmp.userLock && tmp.isAutoSelect){
       prcsOpUserNameSpan = "<span>" + tmp.prcsOpUserName + "</span>";
     }else{
       prcsOpUserNameSpan = "<span>" + tmp.prcsOpUserName
       + "<img align=\"absmiddle\" onclick=\"javascript:cancelUser(this,'"+ prcsOpUser +"',true ,'"+ i +"');\" src=\"" + imgPath + "/remove.png\"/></span>";
     }
   }else{
     prcsOpUser = "" ;
     prcsOpUserNameSpan = "";
   }
 }else{
   prcsOpUser = "" ; 
   prcsOpUserNameSpan = "" ;
 }
 var prcsUser = "";
 var prcsUserNameSapn = "";
 
 if(tmp.prcsUser){
   prcsUser = tmp.prcsUser;
   if(tmp.userLock && tmp.isAutoSelect){
     prcsUserNameSapn = setUserName(tmp.prcsUserName , tmp.prcsUser , false , tmp.prcsId); 
   }else{
     prcsUserNameSapn = setUserName(tmp.prcsUserName , tmp.prcsUser , true, tmp.prcsId); 
   }
 }else{
   prcsUser = "" ;
   prcsUserNameSapn = "";
 }
 var text = new Array();
 text[0] = '<tr id="userSelectTr_'+i+'" style="display:none" class="TableData"><td colspan="2">'
   + '<div class="TableHeader PrcsName"><img align="absmiddle" src="'+imgPath+'/node_user.gif"/>' 
   + '<b> <a href="javascript:void(0)" id="nextPrcsNameA_'+i+'">'+ nextPrcsNameA +'</a></b></div>';
 text[1] = '<div style="margin-left: 5px; line-height: 18px;padding-top:5px;">';
 text[2] = '<a  id="topFlagA_'+i+'" href="javascript:void(0)" onclick="'+ topFlagA +'">';
 text[3] = '<input type="hidden" value="'+tmp.flowChild+'" id="flowChild_'+i+'" name="flowChild_'+i+'"/><input type="hidden" id="topFlag_'+i+'" name="topFlag_'+i+'" value="'+topFlag+'" /><span id="topFLagSpan_'+i+'">'+topFLagSpan+'</span><img align="absMiddle" src="'+imgPath+'/menu_arrow_down.gif"/></a><span id="prcsOpUserNameSpan_'+i+'">'+prcsOpUserNameSpan+'</span><input type="hidden" value="'+prcsOpUser+'" id="prcsOpUser_'+i+'" name="prcsOpUser_'+i+'"/></div>';
 text[4] = '<div style="margin-left: 5px;padding-top:5px; line-height: 18px;">经办人：<span id="prcsUserNameSapn_'+i+'">'+prcsUserNameSapn+'</span><input type="hidden" value="'+prcsUser+'" id="prcsUser_'+i+'" name="prcsUser_'+i+'"/></div>';
 text[5] = '<div style="margin: 5px;padding-top:5px;"><input  type="button" value="选择人员" id="selecteUserButton_'+i+'" onclick="'+ selecteUserButton +'" class="SmallButtonW"/>';
 text[6] = '<a onclick="cancelAll('+i+')"  style="display:'+ isOrgClearShow +'"  id="orgClear_'+i+'" class="orgClear" href="javascript:void(0);">清空</a>';
 text[7] = '';
 if (tmp.prcsId == '0' && tmp.parentRun != '0' && tmp.prcsBack) {
   text[7] = '<input type="hidden" value="'+tmp.prcsBack+'" id="prcsBack" name="prcsBack"/>';
 }
 text[8] =  '</div></td></tr>';
 var prcsText= text.join("");
 return prcsText;
}
function setPrcsTr(tmp) {
 var text = getPrcsText(tmp);
 $('prcsList').insert(text);
}