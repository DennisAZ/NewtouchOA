var tr_cc_index = "";
var tr_bcc_index = "";
var tr_w_index = "";
var tr_wbcc_index = "";
var tr_wcc_index = "";

function checkSendMail(){
  var ischeck = true;
  if(!$('toId').value){
    if(!$('copyToId').value 
        && !$('toWebmail').value 
        && !$('toWebmailCopy').value
       // && !$('toWebmailSecret').value
        //&& !$('secretToId').value
        ){
      ischeck = false;
    }else{
      ischeck = true;
    }
  }
  return ischeck;
}
var linkManMeun;
/**
 * 发送邮件
 * 
 * args == {}
 * 
 * @return
 */
function sendMail(){
  //saveMaiByUp();
  if(window.onbeforeunload){
    window.onbeforeunload = "";
  }
  if(!checkSendMail()){
    alert("请选择收件人！");
    return;
  }
  
  $('toWebmail').value = $('toWebmail').value.trim();
  if($('toWebmail').value && !checkEmailM($('toWebmail').value)){
    if (webmailOnly){
      alert("收件人格式不正确!");
    }else {
      alert("外部收件人,邮件格式不正确!");
    }
    if ($('toWebmail')){
      $('toWebmail').select();
    }
    return;
  }
  $('toWebmailCopy').value = $('toWebmailCopy').value.trim();
  if($('toWebmailCopy').value && !checkEmailM($('toWebmailCopy').value)){
    if (webmailOnly){
      alert("抄送人格式不正确!");
    }else {
      alert("外部收件人,邮件格式不正确!");
    }
    if ($('toWebmailCopy')){
      $('toWebmailCopy').select();
    }
    return;
  }
  $('toWebmailSecret').value = $('toWebmailSecret').value.trim();
  if($('toWebmailSecret').value && !checkEmailM($('toWebmailSecret').value)){
    if (webmailOnly){
      alert("密送人格式不正确!");
    }else {
      alert("外部收件人,邮件格式不正确!");
    }
    if ($('toWebmailSecret')){
      $('toWebmailSecret').select();
    }
    return;
  }
  var msg = "邮件主题为空，确认要发送邮件吗？";
  if(!$("form1").subject.value){
    if(!confirm(msg)) {
      return ;
    }
  }
  

  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/sendMailFromFlow.act";
  document.form1.boxId.value = "0";
  document.form1.sendFlag.value = "1";
  $('sendFlag').value = 1;
  $("form1").action = url;
  $("form1").submit();
  
}
function saveByTime(){
  if(window.onbeforeunload){
    window.onbeforeunload = "";
    }
  var FCK = FCKeditorAPI.GetInstance('Econtent'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行
  var FORM_MODE = FCK.EditingArea.Mode;
  
  // 获取编辑区域的常量——源文件模式
  var editingAreaFrame = document.getElementById('Econtent___Frame');
  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
  if(FORM_MODE == editModeSourceConst) {
    FCK.Commands.GetCommand( 'Source' ).Execute();
  } 
  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
  var textStr = FORM_HTML;
  textStr = textStr.replace(/[\n\r\f]/g,"");
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/saveMailByTime.act";
  document.form1.content.value = textStr;
  //$("form1").removeAttribute("enctype");
  $("form1").setAttribute("enctype" ,"application/x-www-form-url") ;
  var rtJson = getJsonRs(url,$("form1").serialize());
   if(rtJson.rtState == "0"){
     var seqId = rtJson.rtData;
     $("seqId").value = seqId;
   }
}
function timing_save() {
  if (!isTouchDevice && FCKeditorAPI.GetInstance('Econtent').IsDirty() ) {
    saveByTime();
    FCKeditorAPI.GetInstance('Econtent').ResetIsDirty();
  }
}
/**
 * 
 * @param seqId
 * @return
 */
function showSelfBox(){
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getSelfDefBox.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var boxs = rtJson.rtData;
    if(counts.inNew){
      $('isNew').style.display = ''; 
    }
    bindJson2Cntrl(counts);
  }
}
/**
 * 
 * @param seqId
 * @return
 */
function bindSelfBox(contrlId){
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getSelfDefBox.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var boxs = rtJson.rtData;
    if(boxs.length > 0){
      for(var i = 0 ; i< boxs.length; i ++ ){
        var optStr = "<option value=" + boxs[i].inboxId+ ">" + boxs[i].inboxName + "</option>";
        $(contrlId).insert(optStr,'bottom');
      }
      if($(contrlId).options[0]){
        $(contrlId).options[0].selected = true;
      }
    }
  }
}
function sendMailForDbox(){
  if(window.onbeforeunload){
    window.onbeforeunload = "";
    }
  var readStr = getChecked();
  if(readStr == "") {
    alert("请至少选择一封要发送的邮件。");
    return;
 }
  msg='确认要发送所选邮件吗？';
  if(window.confirm(msg)) {
    var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/sendMailAll.act";
    var rtJson = getJsonRs(url,"seqIds=" + readStr);
    if(rtJson.rtState == "0"){
      //alert(rtJson.rtMsrg);
      location.reload();
    }else{
      alert(rtJson.rtMsrg);
     }
  }
}

/**
 * 标记为已读,将数据库中的email是否已读字段标记为已读
 * 
 * @return;
 */
function readMail() {
  var readStr = getChecked(1);
  if(readStr == "") {
     alert("要将邮件标记为已读，请至少选择其中一封。");
     return;
  }
  msg='确认要将所选邮件标记为已读吗？';
  if(window.confirm(msg)) {
    if(readMailById(readStr)){
      location.reload();
      if(parent.mail_menu)
        parent.mail_menu.location.reload();
    }
  }
}
function readMailById(id) {
  var param = "mailId=" + id + "&fieldValue=1"
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/readFlag.act";
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == "0"){
    return true;
  }else{
    return false;
  }
}
/**
 * 查看邮件状态
 * 
 * @param bodyId
 * @param type
 *          [0(表示发件人查看邮件状态)|1(收件人查看邮件状态)]
 * @return
 */
function readStatus(bodyId,type){
  if(!type){
    type = 0;
  }
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/readStatus.act?bodyId="+ bodyId + "&type=" + type;
  window.open(url);
}
/**
 * 重发邮件
 * 
 * @return
 */
function reSendMail(){
  bindCg(json,fck,url);
  if(parent.mail_menu)
    parent.mail_menu.location.reload();
}
/**
 * 保存邮件到草稿箱
 * 
 * @return saveMailByUp
 */
function saveMail(type){
  if(window.onbeforeunload){
    window.onbeforeunload = "";
    }
  var textStr;
  if (isTouchDevice) {
    textStr = $("contentTextarea").value;
  }
  else {
    var FCK = FCKeditorAPI.GetInstance('Econtent'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行    
    // by dq 090521
    var FORM_MODE = FCK.EditingArea.Mode;
    
    // 获取编辑区域的常量——源文件模式
    var editingAreaFrame = document.getElementById('Econtent___Frame');
    var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
    if(FORM_MODE == editModeSourceConst)
    {
      FCK.Commands.GetCommand( 'Source' ).Execute();
    } 
    // $("formName").value = formName;
    var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
    //var textStr = FCK.GetXHTML(false);  
    textStr = FORM_HTML;
  }
  textStr = textStr.replace(/[\n\r\f]/g,"");
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/saveMail.act";
  document.form1.content.value = textStr;
  if(type == 2){
    $("form1").seqId.value = '';
  }
  $("form1").action = url;
  $("form1").enctype = "multipart/form-data";
  $("form1").submit();
  if(parent.mail_menu)
    parent.mail_menu.location.reload();
}
/**
 * 
 * @return
 */
function saveMaiByUp(){
  if(window.onbeforeunload){
  window.onbeforeunload = "";
  }
  var FCK = FCKeditorAPI.GetInstance('Econtent'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行
                                                  // by dq 090521
  var FORM_MODE = FCK.EditingArea.Mode;
 
  // 获取编辑区域的常量——源文件模式
  var editingAreaFrame = document.getElementById('Econtent___Frame');
  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
  if(FORM_MODE == editModeSourceConst)
  {
    FCK.Commands.GetCommand( 'Source' ).Execute();
  } 
 // $("formName").value = formName;
  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
  var textStr = FORM_HTML;
  textStr = textStr.replace(/[\n\r\f]/g,"");
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/saveMailByUp.act";
  document.form1.content.value = textStr;
  $("form1").action = url;
  $("form1").submit();
  if(parent.mail_menu)
    parent.mail_menu.location.reload();
}
/**
 * 
 * @param seqId
 * @return
 */
function updateMail(seqId){
  if(window.onbeforeunload){
    window.onbeforeunload = "";
  }
  var textStr;
  if (isTouchDevice) {
    textStr = $("contentTextarea") && $("contentTextarea").value || "";
  } else {
    var FCK = FCKeditorAPI.GetInstance('Econtent'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行    // by dq 090521
    var FORM_MODE = FCK.EditingArea.Mode;
    
    // 获取编辑区域的常量——源文件模式
    var editingAreaFrame = document.getElementById('Econtent___Frame');
    var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
    if(FORM_MODE == editModeSourceConst) {
      FCK.Commands.GetCommand( 'Source' ).Execute();
    } 
    // $("formName").value = formName;
    var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
    textStr = FORM_HTML;
  }
  textStr = textStr.replace(/[\n\r\f]/g,"");
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/updateMail.act";
  document.form1.content.value = textStr;
  $("form1").action = url;
  $("form1").submit();
}
 /**
   * 
   * @param seqId
   * @return
   */
function saveBox(){
  // alert("ssss");
  var url = contextPath +  "/yh/core/funcs/email/act/YHEmailBoxAct/saveBox.act";
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if(rtJson.rtState == "0"){
    alert(rtJson.rtMsrg);
  }else{
    alert(rtJson.rtMsrg);
   }
}
/**
 * 
 * @param seqId
 * @return
 */
function updateBox(){
 // alert("ssss");
 var url = contextPath +  "/yh/core/funcs/email/act/YHEmailBoxAct/updateBox.act";
 var rtJson = getJsonRs(url, mergeQueryString($("form1")));
 if(rtJson.rtState == "0"){
   alert(rtJson.rtMsrg);
   location = contextPath + "/core/funcs/email/mailbox/index.jsp";
 }else{
   alert(rtJson.rtMsrg);
  }
}
/**
 * 
 * @param seqId
 * @return
 */
function getBoxById(boxId){
 // alert("ssss");
 var url = contextPath +  "/yh/core/funcs/email/act/YHEmailBoxAct/getBoxById.act?boxId=" + boxId;
 var rtJson = getJsonRs(url);
 if(rtJson.rtState == "0"){
   bindJson2Cntrl(rtJson.rtData);
 }else{
   alert(rtJson.rtMsrg);
  }
}
function uploadSuccessOver(file, serverData){
  try {
    var progress = new FileProgress(file, this.customSettings.progressTarget);
// progress.setComplete();
// progress.setStatus("Complete.");
    progress.toggleCancel(false);
    var json = null;
    json = serverData.evalJSON();
    if(json.state=="1") {
       progress.setError();
       progress.setStatus("上传失败：" + serverData.substr(5));
       
       var stats=this.getStats();
       stats.successful_uploads--;
       stats.upload_errors++;
       this.setStats(stats);
    } else {
       $('attachmentId').value += json.data.attachmentId;
       $('attachmentName').value += json.data.attachmentName;
       var attachmentIds = $("attachmentId").value;
       var attachmentName = $("attachmentName").value;
       var ensize =  $('ensize').value;
       if(ensize){
       $('ensize').value =(json.data.size + parseInt(ensize));
       }else{
         $('ensize').value =json.data.size ;
       }// 附件大小
       showAttach(attachmentIds,attachmentName,"showAtt");
    }
  } catch (ex) {
    this.debug(ex);
  }
}
/**
 * @param
 * @return
 */
function delForShortCut(delType,shortCutType,boxId,msg){
  if(!confirm(msg)) {
    return ;
  }
  var param = "shortCutType=" + shortCutType + "&delType=" + delType ;
  if(boxId == 0 || boxId){
    param += "&boxId=" + boxId;
  }
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/deletMailForShort.act";
  var rtJson = getJsonRs(url, param);
  if (rtJson.rtState == "0") {
    window.location.reload();
    if(parent.mail_menu)
      parent.mail_menu.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  } 
}
/**
 * 判断邮件的状态
 * 
 * @return [1发件箱已删除|2发件箱未读|3发件箱已读|4收件件箱已删除|5收件箱未读new|6收件箱已读]
 */
function mailStatus(mailBodyId,typesy,emailId){
  if(!emailId){
    emailId = "";
  }
  var  param = "bodyId=" + mailBodyId + "&type=" + typesy + "&emailId=" + emailId ;
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getMailStatus.act?" + param;
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  }else {
    alert(rtJson.rtMsrg); 
    return -1;
  } 
}

function smsBack(cntrlId){
  var toId = $(cntrlId).value;
  if(toId){
    var actionUrl =contextPath + "/core/funcs/message/sendmessage.jsp?toId=" + toId;
    openWindow(actionUrl, "回复微讯", 800, 360);
  }
}
/**
 * 删除邮件 type删除时的邮箱类型1，2，3 [1表示收件箱，2表示已删除邮件箱，3表示发件箱]
 * 
 * url删除邮件后页面加载地址 url = "<%=contextPath%>/yh/core/funcs/email/act/YHInnerEMailAct/listInBox.act?boxId=0";
 * 
 * @return
 */
 function deleteMail (type,reloadurl,id,notParent) {
   var msg = "";
   var delete_str = "";
   if(!id){
     var delete_str = getChecked();
     if(delete_str == "") {
     alert("要删除邮件，请至少选择其中一封。");
     return;
     }
   }else{
     delete_str = id;
   }
   if(type == 2){
     msg = "邮件永久删除后将不能恢复，确认要删除所选邮件吗？";
   }else if(type == 3){
     msg='确认要删除所选邮件吗？\n注意：如果删除内部收件人未读邮件，内部收件人将不会接收到该邮件！';
   }else{
     msg = '确认要删除所选邮件吗？';
   }
   if(window.confirm(msg)) {
     var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/deletM.act?bodyId="+ delete_str + "&deType=" + type ;
     var rtJson = getJsonRs(url);
     if(rtJson.rtState == "1"){
       alert(rtJson.rtMsrg);
     }
     if(reloadurl){
       if(notParent){
         location = reloadurl;
       }else{
         parent.location = reloadurl;
       }
       if(parent.mail_menu)
         parent.mail_menu.location.reload();
     }else{
       location.reload();
       if(parent.mail_menu)
         parent.mail_menu.location.reload();
     }
   }
 }
 /**
   * 取得收件箱/已删除邮件箱新邮件数量 // 发件箱收件人已删除邮件数量看
   * 
   * @param type
   *          [1收件箱新邮件,2已删除邮件箱新邮件,3发件箱已删除邮件]
   * @param boxId
   * @param cntrlId
   *          绑定空间ID
   * @return
   */
function getNewDelCount(type,boxId,cntrlId){
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getNewDelCount.act?boxId="+ boxId + "&type=" + type ;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    $(cntrlId).innerHTML =  rtJson.rtData;
  }else{
    alert(rtJson.rtMsrg);
  }
}
   function getNewDelCount2(type,boxId,cntrlId){
     var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getNewDelCount.act?boxId="+ boxId + "&type=" + type + "&queryType=" + $('type').value ;
     var rtJson = getJsonRs(url , "email=" +$('email').value  );
     if(rtJson.rtState == "0"){
       $(cntrlId).innerHTML =  rtJson.rtData;
     }else{
       alert(rtJson.rtMsrg);
     }
   }
/**
 * 导出邮件
 * 
 * @return
 */
function exportMail(){
  var delete_str = getChecked();
  if(!delete_str){
    alert("请选择要导出的邮件!");
    return;
  }
  location = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/exportToExcel.act?emailIds=" + delete_str ;
}
/**
 * 草稿箱
 * @return
 */
function exportMail2(){
  var delete_str = getChecked();
  if(!delete_str){
    alert("请选择要导出的邮件!");
    return;
  }
  location = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/exportToExcel2.act?emailIds=" + delete_str ;
}
/**
 * 批量导出邮件
 * 
 * @return
 */
function exportEml(){
  var delete_str = getChecked();
  if(!delete_str){
    alert("请选择要导出的邮件!");
    return;
  }
  location = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/exportToEml.act?emailIds=" + delete_str ;
}

function exportEmlOut(){
  var delete_str = getChecked();
  if(!delete_str){
    alert("请选择要导出的邮件!");
    return;
  }
  location = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/exportToEml2.act?emailIds=" + delete_str ;
}
/**
 * 批量导出邮件
 * 
 * @return
 */
function exportEml2(id){
  location = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/exportToEml.act?emailIds=" + id ;
}
/**
 * 邮件查询
 * 
 * @param type
 *          [1|2|3|4] [收件箱|发件箱|已删除邮件箱|草稿箱]
 * @param form
 * @return
 */
function mailSearch(type,form,name){
  var query = "";
  var url = "";
  if(checkDateTime("startTime") == false){
    $("startTime").focus();
    $("startTime").select();
    alert("日期格式不对，请输入形如：2010-10-10 12:12:12");
    return;
  }
  if(checkDateTime("endTime") == false){
    $("endTime").focus();
    $("endTime").select();
    alert("日期格式不对，请输入形如：2010-10-10 12:12:12");
    return;
  }
  query = $("form1").serialize();
  if(type == 1){
    url = contextPath + "/core/funcs/email/query/inboxSearch.jsp?";
  }else if(type == 2){
    url = contextPath + "/core/funcs/email/query/sendboxSearch.jsp?";
  }else if(type == 3){
    url = contextPath + "/core/funcs/email/query/delboxSearch.jsp?";
  }else if(type == 4){
    url = contextPath + "/core/funcs/email/query/outboxSearch.jsp?";
  }else{
    alert("没有此查询类型:" + type);
    return;
  }
  location =  url + query +"&name=" + name ;
}
//判断是否要显示短信提醒 
function getSysRemind(remidDiv,remind){ 
  var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=2"; 
  var rtJson = getJsonRs(requestUrl); 
    if(rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  } 
  var prc = rtJson.rtData; 
  var allowRemind = prc.allowRemind;;//是否允许显示 
  var defaultRemind = prc.defaultRemind;//是否默认选中 
  var mobileRemind = prc.mobileRemind;//手机默认选中 
  if(allowRemind=='2'){ 
    $(remidDiv).style.display = 'none'; 
  }else{ 
    if(defaultRemind=='1'){ 
      $(remind).checked = true; 
    } 
  } 
  //return prc; 
}
//判断是否要显示短信提醒 
function moblieSmsRemind(remidDiv,remind){ 
  var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=2"; 
  var rtJson = getJsonRs(requestUrl); 
    if(rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  } 
  var prc = rtJson.rtData; 
  var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
  if(moblieRemindFlag == '2'){ 
    $(remidDiv).style.display = '';
    $(remind).checked = true;
  }else if(moblieRemindFlag == '1'){ 
    $(remidDiv).style.display = '';
    $(remind).checked = false;
  }else{
    $(remidDiv).style.display = 'none'; 
  }
}
function CopySome(obj){
  if(document.all){
      window.clipboardData.setData("Text",obj); 
  }
  alert("内容已复制到剪贴板了");
}

function loadLinkMan(bindId){
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getLastLinkMan.act?";
  var rtJson = getJsonRs(url); 
  var menus = new Array();
  if(rtJson.rtState == "0"){
    var users = rtJson.rtData;
    for ( var i = 0; i < users.length; i++) {
      var user = users[i];
      var extData = "{userId:\"" + user.userId + "\",userName:\"" + user.userName + "\",bindId:\"" + bindId + "\"}"; 
      extData = extData.evalJSON(); 
      var menu =  { name:"<div style=\"padding-top:5px;margin-left:10px\">" + user.userName + "<div>",action:selectLikeMan,extData:extData}
      menus.push(menu);
    }
  }
  return menus;
}
function setLinkManMeun(bindId){
  linkManMeun = loadLinkMan(bindId);
}
function showMenuLinkMan(cntrl,bindId){
  try{
    if(!linkManMeun){
      linkManMeun = setLinkManMeun(bindId);
    }
    var menu = new Menu({bindTo: cntrl , menuData:linkManMeun});
    menu.show(event);
  }catch(e){}
}
function bindLikeMan(cntrl,bindId){
  if (cntrl) {
    Event.observe(cntrl, 'mouseover', showMenuLinkMan.bind(event,cntrl,bindId)); 
  }
}

function bindImportant(cntrl,important){
  var importStr0 = "<span class=TextColor>一般邮件</span>";
  var importStr1 = "<span class=TextColor1>重要邮件</span>";
  var importStr2 = "<span class=TextColor2>非常重要</span>";
  var importCntrl = "";
 // var extData = "{userId:\'" + user.userId + "\',userName:\"" + user.userName + "\",bindId:\"" + bindId + "\"}"; 
  //extData = extData.evalJSON(); 
  var menu =  [{ name:"<div style=\"padding-top:5px;margin-left:10px\">" + importStr0 + "<div>",action:selectImportant,extData:[importStr0,0]}
               ,{ name:"<div style=\"padding-top:5px;margin-left:10px\">" + importStr1 + "<div>",action:selectImportant,extData:[importStr1,1]}
               ,{ name:"<div style=\"padding-top:5px;margin-left:10px\">" + importStr2 + "<div>",action:selectImportant,extData:[importStr2,2]}
      ];
  if(important == "1"){
    importCntrl = importStr1;
  }else if(important == "2"){
    importCntrl = importStr2;
  }else{
    importCntrl = importStr0;
  }
  $(cntrl).innerHTML = importCntrl + "<span style=\"font-family:Webdings\">6</span>";
  $("important").value = important;
  if (cntrl) {
    Event.observe(cntrl, 'mouseover', showMenuImport.bind(event,cntrl,menu)); 
  }
}
function showMenuImport(cntrl,importmenu){
  var menu = new Menu({bindTo: cntrl , menuData:importmenu});
  menu.show(event);
}
function selectImportant(dom,cntrlId,extData){
  var important = extData[1];
  var importStr = extData[0];
  $("important").value = important ;
  $(cntrlId).innerHTML = importStr + "<span style=\"font-family:Webdings\">6</span>";
}
function selectLikeMan(dom,cntrlId,extData){
  var userId = extData.userId;
  var userName = extData.userName;
  var bindId = extData.bindId;
  var User = $(bindId);
  var ids = User.value;
  
  if(findInSet(ids,userId)){
    selectUser2(bindId,userId , userName);
  }else{
    disSelectUser2(bindId,userId , userName);
  }
 // alert(extData.bindId);
}

function selectUser2(cntrId,id , userName) {
  var User = $(cntrId);
  var UserDesc = $(cntrId + "Desc");
  var userStr = User.value;
  var userDescStr = "";
  if (UserDesc.tagName == "INPUT") {
    userDescStr = UserDesc.value;
    UserDesc.value = getOutofStr2(userDescStr , userName);
  } else {
    userDescStr = UserDesc.innerHTML.trim();
    UserDesc.innerHTML = getOutofStr2(userDescStr , userName);
  }
  User.value = getOutofStr2(userStr , id);
}

function disSelectUser2(cntrId,id , userName) {
  var User = $(cntrId);
  var UserDesc = $(cntrId + "Desc");
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
function getOutofStr2(str, s){
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
function findInSet(str, s){
  var aStr = str.split(',');
  if(aStr.contains(s)){
    return true;
  }else{
    false;
  }
}

function isBoxNameExist(boxName,boxId){
  if(!boxId){
    boxId = "";
  }
  var url = contextPath + "/yh/core/funcs/email/act/YHEmailBoxAct/isBoxNameExist.act?";
  var rtJson = getJsonRs(url,"boxName=" + boxName + "&boxId=" + boxId);
  if(rtJson.rtState == "0"){
    return rtJson.rtData.isExist;
  }
  
}

function isFull(){
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/isFull.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var obj =  rtJson.rtData;
    if(obj.isFull){
      var url = contextPath + "/core/funcs/email/new/isFull.jsp?mailSize=" + obj.mailSize + "&inBox=" + obj.inBox + "&sendBox=" + obj.sendBox;
      location = url;
    }
  }
}
/**
 * 取得剩余容量
 * @param userId
 * @return
 */
function getSpare(){
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getSpareCapacity.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == '0'){
    var result = rtJson.rtData.spareCapacitySize;
    if(result == -1){
      result = -1;
    }else {
      result = rtJson.rtData.spareCapacitySize /(1024*1024);

    }
    return result;
  }else{
    return -1;
  }
}
function doBachDown(){
  var attachmentName = $('attachmentName').value;
  var attachmentId = $('attachmentId').value;
  var module = "email";
  batchDownload(attachmentName,attachmentId,module,"邮件附件打包下载");
}

function copy_email(){
  parent.mail_view.document.execCommand('selectall'); 
  parent.mail_view.document.execCommand('copy');
  parent.mail_view.document.execCommand('unselect');
  //alert(window.clipboardData.getData("Text"))
  alert("邮件全文已复制到剪贴板！");
}

function getUserInfo(){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/getUserInfo.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == '0'){
    return rtJson.rtData;
  }else{
    return "";
  }
}

function bachDownByList(){
  var delete_str = getChecked();
  if(!delete_str){
    alert("请选择需要打包下载附件的邮件!");
    return;
  }
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getAttachInfo.act?emailId=" + delete_str ;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == '0'){
    var obj = rtJson.rtData;
    if(!obj.attachmentName || !obj.attachmentId){
      alert("邮件中不包含附件!");
      return;
    }
    var attachmentName = obj.attachmentName;
    var attachmentId =  obj.attachmentId;
    var module = "email";
    batchDownload(attachmentName,attachmentId,module,"邮件附件打包下载"); 
  }
}
/**
 * 绑定外部邮箱的配置
 * @return
 */
function bindWebMailByJson(rtJson){
  var select = $("fromWebmail");
  var data = new Array();
  var selectValue = "";
  if(rtJson.rtState == "0"){
    if (rtJson.rtData.length > 0 ) {
      for(var i = 0 ; i < rtJson.rtData.length; i ++){
        var obj = new Object();
        selectValue = seqId;
        obj.code = rtJson.rtData[i].seqId;
        obj.desc = rtJson.rtData[i].email;
        data[i] = obj;
      }
      
      tr_w_index = "1";
      $("tr_webmail").show();
      $('tr_webmaildesc').show();
    } else {
      $('webmailSpan').hide();
      if (webmailOnly) {
        $('emailTr').show();
      }
    }
  }
  if(data.length > 0){
    $("webmailInfo").innerHTML = "通过该邮箱发送邮件";
    if (!webmailOnly) {
      $("webmailInfo").innerHTML += "给外部收件人";
    }
   
  }else{
    var url = contextPath + "/core/funcs/email/webbox/manager/index.jsp";
    $(webmailInfo).innerHTML = "请到“<a href='" + url + "'>Internet邮件</a>”模块建立邮箱并设置密码";
  }
  loadSelectData(select, data, selectValue);
}
/**
 * 
 * @param cntrlId
 * @param infoCntrlId
 * @return
 */
function bindWebMail(){
  //var rtJson = getJsonRs(url);
  var url = contextPath + "/yh/core/funcs/email/act/YHWebmailAct/listWebmailInfo.act";
  getJsonRsAsyn(url,'',bindWebMailByJson,true);
}

function checkEmail(el){//用正则表达式判断
  var regu = "^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z-]+))@([a-zA-Z0-9-]+[.])+([a-zA-Z]|net|NET|asia|ASIA|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT|cn|CN|cc|CC)$"
  var re = new RegExp(regu);
  if(el.search(re) < 0){
    return false; //非法
  }
  return true;//正确
}
function checkEmailM(str){
  var reg = /^(?:(.*\<)?[a-zA-Z0-9_\-]+@[a-zA-Z0-9_-]+(?:\.[a-zA-Z0-9_\-]+)+\>?,?)*$/;
  return str.match(reg);
}
//var reg = /^(?:[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(?:\\.[a-zA-Z0-9_-]+)+,?)*$/
//
//  var str ="sklj@lksd.com,clsjkd@lksd.cn";

