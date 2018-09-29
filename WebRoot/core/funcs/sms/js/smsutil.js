
function getToIdByBodyId(smsBodyId){
  var toId = "";
  var url = contextPath + "/yh/core/funcs/sms/act/YHSmsAct/getSmsToId.act";
  var rtJson = getJsonRs(url, "bodyId=" + smsBodyId);
  if (rtJson.rtState == "0") {
    toId = rtJson.rtData;
  }
  return toId;
}
/**
 * 短信提示使得弹出窗口的内容
 * @return
 */
function toPromptStr(obj){
  var remindUrl = "";
  if(obj.remindUrl){
    remindUrl =  contextPath +  obj.remindUrl;
  }else {
    remindUrl = contextPath + "/yh/core/funcs/sms/act/YHSmsTestAct/acceptedSms.act?pageNo=0&pageSize=20";
  }
  var contentValue =  obj.content.replace(/[\n\r\f]/g,"");
  var promptStr = new Array(); 
 // var altStr = obj.content.replace(/<br>/g,"");
  promptStr.push("<DIV class=\"module_sms listColor\" id=module_" + obj.seqId + ">");
  promptStr.push("<DIV class=head><DIV CLASS=moduleHeader><IMG  src=\"" + imgPath + "/sms/sms_type"+ obj.smsType +".gif\" align=absMiddle>&nbsp;&nbsp;" + getSmsTypeDesc(obj.smsType) + "</DIV></div>");
  promptStr.push("<DIV class=module_body>");
  promptStr.push("<DIV class=time><U  style=\"CURSOR: hand\"><input type=\"hidden\" id=\"fromId_" + obj.seqId + "\" value=\"" + obj.fromId + "\"><span id=\"fromId_" + obj.seqId + "Desc\"></span></U>&nbsp;&nbsp;" + obj.sendTime + " </DIV>");
  promptStr.push("<DIV class=content>" + contentValue + "</DIV>");
  promptStr.push("<DIV class=module_page_sms align=right>");
  promptStr.push("<A href=\"javascript:SmsRead(" + obj.seqId + ");\">已阅</A>&nbsp;&nbsp; ");
  promptStr.push("<A href=\"javascript:SmsBack(" + obj.seqId + "," + obj.fromId + ");\">回复</A>&nbsp;&nbsp;"); 
  promptStr.push("<A title=点击查看内容详情 href=\"javascript:Details(\'" +  remindUrl + "\',\'" + obj.seqId + "\',0);\">查看详情</A>&nbsp;&nbsp; ");
  promptStr.push("</DIV></DIV></DIV>");
  return promptStr;
}
/**
 * 短信提示使得弹出窗口的内容
<div class="module_sms listColor">
  <div class="head">
    <div  class="moduleHeader">
      <img src="/images/sms_type5.gif" alt="日程安排"  width=16 height=16 align="absmiddle"/> 个人短信   </div> </div>
  <div class="module_body">
    <div class="time">
      <u title="部门：java1组" style="cursor:pointer">系统管理员</u> <%=smsContentWant.getSendTime() %>   </div>
    <div class="content"><%=smsContentWant.getContent() %></div>
    <div class="module_page_sms">
    <a href="#" onclick="parent.send_sms('admin','系统管理员');"> 回复</a>&nbsp;
    <a href="javascript:parent.openURL('%2Fgeneral%2Fsms%2Freceive%2F',0);" title="点击查看内容详情"> 查看详情</a>&nbsp;
    <a href="delete.php?SMS_ID=156&DEL_TYPE=1&start=0"> 删除</a>
    </div>
  </div>
</div>
 * @return
 */
function toPanelInBoxStr(obj){
  var remindUrl = "";
  if(obj.remindUrl){
    remindUrl =  contextPath + obj.remindUrl;
  }else {
    remindUrl = contextPath + "/yh/core/funcs/sms/act/YHSmsTestAct/acceptedSms.act?pageNo=0&pageSize=20";
  }
  if(obj.remindTime){
    obj.sendTime = obj.remindTime;
  }
  //var altStr = obj.content.replace(/<br>/g,"");
  var timeStr =  obj.sendTime.substring(5,16);
  var contentValue =  obj.content.replace(/[\n\r\f]/g,"");
  var promptStr = "<DIV class=\"module_sms listColor2\" id=module_" + obj.smsBodyId + ">"
    + "<input type=\"hidden\" id=\"deleteFlag\" value=\"" + obj.smsBodyId  + "\">"
    + "<DIV class=head2 ><DIV CLASS=moduleHeader2><IMG  src=\"" + imgPath + "/sms/sms_type"+ obj.smsType +".gif\" align=absMiddle>&nbsp;&nbsp;" + getSmsTypeDesc(obj.smsType) + "</DIV></div>"
    + "<DIV class=module_body3>"
    + "<DIV class=time><U  style=\"CURSOR: hand\"><input type=\"hidden\" id=\"fromId_" + obj.smsBodyId + "\" value=\"" + obj.fromId + "\"><span id=\"fromId_" + obj.smsBodyId + "Desc\"></span></U>&nbsp;&nbsp;" + timeStr + " </DIV>"
    + "<DIV class=content>" + contentValue + "</DIV>"
    + "<DIV class=module_page_sms align=right>"
   // + "<A href=\"javascript:SmsBack(" + obj.smsBodyId + "," + obj.fromId + ");\">回复</A>&nbsp;&nbsp;" 
    + "<A title=点击查看内容详情 href=\"javascript:Details(\'" + remindUrl + "\',\'" + obj.smsBodyId + "\',0);\">查看详情</A>&nbsp;&nbsp; "
    + "<A title=删除  href=\"javascript:deleteSms(\'" + obj.smsBodyId + "\');\"> 删除</A>&nbsp;&nbsp; "
    + "</DIV></DIV></DIV>";
  return promptStr;
}
function toPanelSentBoxStr(obj){
  var remindUrl = "";
  if(obj.remindUrl){
    remindUrl =  contextPath +  obj.remindUrl;
  }else {
    remindUrl = contextPath + "/yh/core/funcs/sms/act/YHSmsTestAct/acceptedSms.act?pageNo=0&pageSize=20";
  }
  var toId = getToIdByBodyId(obj.bodyId);
  var isShowToId = true;
  if(toId){
    var toIds = toId.split(",");
    if(toIds.length > 1){
      isShowToId = false;
    }
  }
//  var altStr = obj.content.replace(/<br>/g,"");
  var timeStr =  obj.sendTime.substring(5,16);
  var contentValue =  obj.content.replace(/[\n\r\f]/g,"");
  var promptStr =  "<DIV class=\"module_sms listColor2\" id=module_" + obj.bodyId + ">"
    + "<input type=\"hidden\" id=\"deleteFlag\" value=\"" + obj.bodyId  + "\">"
    + "<DIV class=head2><DIV CLASS=moduleHeader2><IMG src=\"" + imgPath + "/sms/sms_type"+ obj.smsType +".gif\" align=absMiddle>&nbsp;&nbsp;" + getSmsTypeDesc(obj.smsType) + "</DIV></div>"
    + "<DIV class=module_body3>"
    + "<DIV class=time><U  style=\"CURSOR: hand\">" ;
  if(isShowToId){
    promptStr +=  "<input type=\"hidden\" id=\"fromId_" + obj.bodyId + "\" value=\"" + obj.fromId + "\"><span id=\"fromId_" + obj.bodyId + "Desc\"></span>";
  }else{
    promptStr +=  " <a href=\"javascript:showStatus('" + obj.bodyId + "');\">查看</a>";
  }
  promptStr += "</U>&nbsp;&nbsp;" + timeStr + " </DIV><DIV class=content>" +  contentValue + "</DIV>"
    + "<DIV class=module_page_sms align=right nowrap>";
  if(isShowToId){
    promptStr += "<A href=\"javascript:smsSend(\'" + obj.fromId + "\');\">发送</A>&nbsp;" 
      + "<A title=重新发送  href=\'javascript:resetSms(\"" + obj.bodyId + "\",\"" + obj.content.replace(/\"/g,"\\\"") + "\"," + obj.smsType + ",\"" + obj.remindUrl + "\");\'>重发</A>&nbsp;";
   // if(obj.smsType == "0"){
    //  promptStr += "<A title=聊天记录  href=\"javascript:Details(\'" + remindUrl + "\',\'" + obj.bodyId + "\',0);\"> 聊天记录</A>&nbsp;";
   // }
  }
  promptStr += "<A title=删除  href=\"javascript:deleteSmsById(\'" + obj.bodyId + "\');\"> 删除</A>&nbsp; "
    + "</DIV></DIV></DIV>";
  return promptStr;
}
/**
 * 
 * @param url
 * @param id
 * @param type
 * @return
 */
function gotoURLSms(url,id,type){
  var urlStr= encodeURI(url); 
  top.dispParts(urlStr); 
}

function getSmsTypeDesc(smsType){
  var url = contextPath + "/yh/core/funcs/sms/act/YHSmsAct/getSmsTypeDesc.act?";
  var rtJson = getJsonRs(url, "smsType=" + smsType + "&code=SMS_REMIND");
  if(rtJson.rtState == "0"){
    return rtJson.rtData;
  }else{
    return "";
  }
}
function detailsForSms(url,id,type){
  markRead(id);
  gotoURLSms(url);
}
/**
 * 取得历史记录
 */
function redHistory(cntrlId,contentId,fromId,date,type,seqId){
  var imgId = "img4_"+cntrlId;
  if($(cntrlId).style.display){
    $(imgId).src = imgPath + "/expanded.gif";
    var url = contextPath + "/yh/core/funcs/sms/act/YHSmsAct/getHistory.act?type="+type;
    var rtJson = getJsonRs(url, "userId=" + fromId + "&date=" + date);
    if(rtJson.rtState == '0'){
      var str = bindHistory(rtJson.rtData,type,seqId);
      $(contentId).innerHTML = '';
      $(contentId).insert(str,'content');
      $(cntrlId).style.display = '';
      for(var i = 0; i < rtJson.rtData.length ; i++){
        bindDesc([{cntrlId:"userId_" + i + "_" + seqId, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
      }
    }else {
      alert(rtJson.rtMsrg);
     }
  } else {
    $(imgId).src = imgPath + "/collapsed.gif";
    $(cntrlId).style.display = 'none';
  }
}
/**
 * 绑定历史记录正文
 * @param objs
 * @return
 */
function bindHistory(objs,type,tyId){
  if(!objs){
    return "";
    }
 
  var toClassStr = 'to';
  var fromClassStr = 'from';
 if(type == 0){
   toClassStr = 'from';
   fromClassStr = 'to';
  }
  var historyHtml = "";
  for(var i = 0; i < objs.length ; i++){
    var obj = objs[i];
    if(obj.fromId){
      historyHtml += "<DIV class='" + toClassStr + "'><input id=\"userId_" + i + "_"+ tyId + "\"  type=hidden value='" + obj.fromId;
    }else {
      historyHtml += "<DIV class='" + fromClassStr + "'><input id=\"userId_" + i + "_"+ tyId + "\" type=hidden value='" + obj.toId;
    }
    historyHtml += "'><span id=\"userId_" + i + "_" + tyId + "Desc\"></span>&nbsp;&nbsp;&nbsp;&nbsp;" + obj.sendTime + "</DIV>"
    + "<DIV class=msg>" + obj.content + "</DIV>";
  }
  return historyHtml;
}
/**
 * 标记为读
 * @param seqId
 * @return
 */
function markRead(seqId){
  var url = contextPath + "/yh/core/funcs/sms/act/YHSmsAct/resetFlag.act";
  var rtJson = getJsonRs(url,"seqId=" + seqId);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
  }
}
/**
 * 删除的快捷键操作
 * @param shortCutType
 * @param shortCutType delType为1时 1删除所有收件箱信息 、2删除所有已读短息信息
 * delType为2时 1表示删除所有发件箱短息、2表示删除所有已提醒收件人短息、3删除所有收件人已删除的邮件
 * @param delType 1代表收件人，2代表发件人
 * @return
 */
function delShortCut(shortCutType,delType,msrg){
  if(!confirm(msrg)) {
    return ;
  }
  var url = contextPath + "/yh/core/funcs/sms/act/YHSmsAct/delSmsByShortcut.act";
  var rtJson = getJsonRs(url, "shortCutType=" + shortCutType + "&deType=" + delType);
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  } 
}
/**
 * 全部标记为读
 * @param readType 1代表收件箱
 * @return
 */
function readAll(){
  if(!confirm("确认全部标记为读！")) {
    return ;
  }
  var url = contextPath + "/yh/core/funcs/sms/act/YHSmsAct/resetFlagAll.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  } 
}

function checkMags(cntrlId){
  var ids= ""
  var checkArray = $$('input');
  for(var i = 0 ; i < checkArray.length ; i++){
    if(checkArray[i].name == cntrlId && checkArray[i].checked ){
      if(ids != ""){
        ids += ",";
      }
      ids += checkArray[i].value;
    }
  }
  return ids;
}
function getAllIds(cntrlId){
  var ids= ""
  var checkArray = $$('input');
  for(var i = 0 ; i < checkArray.length ; i++){
    if(checkArray[i].id == cntrlId){
      if(ids != ""){
        ids += ",";
      }
      ids += checkArray[i].value;
    }
  }
  return ids;
}
/**
 * 标记为已读
 * @return
 */
function doMarkRead(){
  var ids = checkMags('deleteFlag');
  if(!ids){
    alert("请选择需要标记为读的内部短信！");
    return;
  }
  if(!confirmRead()) {
    return ;
  }
  var deleteAllFlags = ids.split(',');
  for(var i = 0; i < deleteAllFlags.length; i++) {
  var id =  deleteAllFlags[i];   //SMS_BODY  seqID
  if(id){
    markRead(id);
  }
  }
  window.location.reload();
}
/**
 * 已读提示
 * @return
 */
function confirmRead() {
  if(confirm("确定要将所选短信标记为读！")) {
    return true;
  }else {
    return false;
  }
}
/**
 * 删除提示
 * @return
 */
function confirmDel() {
  if(confirm("确定要删除所选短信！")) {
    return true;
  }else {
    return false;
  }
}
/**
 * 显示日期控件
 * 
 * @return
 */
function showCalendar(cntrlId, isHaveTime, imgId) {
  var beginParameters = {
    inputId : cntrlId,
    property : {
      isHaveTime : isHaveTime
    },
    bindToBtn : imgId
  };
  new Calendar(beginParameters);
}
/**
 * 清除类容
 * 
 * @param ctrlId
 * @param ctrlIdDesc
 * @return
 */
function Clear() {
  var args = $A(arguments);
  for ( var i = 0; i < args.length; i++) {
    var cntrl = $(args[i]);
    if (cntrl) {
      if (cntrl.tagName.toLowerCase() == "td"
          || cntrl.tagName.toLowerCase() == "div"
          || cntrl.tagName.toLowerCase() == "span") {
        cntrl.innerHTML = '';
      } else {
        cntrl.value = '';
      }
    }
  }
}
/**
 * 判断时间格式
 * @param str
 * @return
 */
function isValidDateStrTime(str) { 
  if (!str) { 
    return; 
   } 
  var type1 = "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"; 
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2); 
  var strArray = str.trim().split(" "); 
  if(strArray.length!=2){ 
    return false; 
  }else{ 
    if(!isValidDateStr(strArray[0])||strArray[1].match(re1) == null || strArray[1].match(re2) != null){ 
      return false; 
    } 
   } 
  }
function checkDateTime(cntrlId){
  var dateStr = $(cntrlId).value;
  return isValidDateStrTime(dateStr) ;
}
function showContent(seqId,type) {
  if(type){
    markRead(seqId);
  }
  var actionUrl = contextPath + "/core/funcs/sms/showcontent.jsp?seqId=" + seqId;
  
  if (top.YH.version) {
    new top.YH.Window({
      title: "查看短信正文",
      width: "360px",
      height: "160px",
      layer: "upper",
      src: actionUrl
    }).show();
  }
  else {
    openWindow(actionUrl, "查看短信正文", '360', '160');
  }
  
  if(type){
    location.reload();
  }
}
function getPersonInfo(userId){
  var url = contextPath + "/yh/core/funcs/sms/act/YHSmsAct/getPersonInfo.act?userId=" + userId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    return rtJson.rtData;
  }else{
    return "";
  }
}