/**
 * 
 * @param message  要检查的文本
 * @param module   模块编号：0-邮件；1-短信；2-手机短信
 * @param flag     0-标题； 1-内容
 * @return
 */

function censor(message, module, flag){
  var msrg = "";
  if(flag == "0"){//标题
    msrg = censorSubject(message, module, flag);
    //alert(subject);
  }else{//内容
    msrg = censorContent(message, module, flag);
  }
  return msrg;
}

/**
 * 校验内容是否含有敏感、禁止词语
 * @param message
 * @param moduleCode
 * @param flag
 * @return
 */
function censorContent(message, moduleCode, flag){
  var moduleCode = "0";
  var url = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorWords.act";
  var rtJson = getJsonRs(url);
  var sun  = "";
  //var message = "ddeee法轮功你好ff你好哈哈ssff哈哈";
  if(rtJson.rtState == "0"){
  for(var i = 0; i < rtJson.rtData.length; i++) {
    var find = rtJson.rtData[i].find;
    var replacement = rtJson.rtData[i].replacement;
      if(message.indexOf(find)!="-1"&&replacement=="{BANNED}"){
        var url = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorBanned.act?moduleCode="+moduleCode;
        location = url;
        return "BANNED";
      }else if(message.indexOf(find)!="-1"&&replacement=="{MOD}"){
        var moduleCode = "0";
        var url = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorMod.act?moduleCode="+moduleCode;
        location = url;
        return "MOD";
      }else{
        if(message.indexOf(find)!="-1"){
          var re = eval("/"+find+"/g");
          sun = message.replace(re, replacement);
          message = sun;
        }
      }
  }
  return message;
  }else{
  alert(rtJson.rtMsrg); 
  }
}

/**
 * 校验标题是否有敏感、禁止词语
 * @param message
 * @param moduleCode
 * @param flag
 * @return
 */

function censorSubject(message, moduleCode, flag){
  var moduleCode = "0";
  var url = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorWords.act";
  var rtJson = getJsonRs(url);
  var sun  = "";
  //var message = "ddeee你好ff你好哈哈ssff哈哈";
  if(rtJson.rtState == "0"){
  for(var i = 0; i < rtJson.rtData.length; i++) {
    var find = rtJson.rtData[i].find;
    //alert(find);
    var replacement = rtJson.rtData[i].replacement;
      if(message.indexOf(find) != "-1" && replacement == "{BANNED}"){
        var url = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorBanned.act?moduleCode="+moduleCode;
        location = url;
        return "BANNED";
      }else if(message.indexOf(find) != "-1" && replacement == "{MOD}"){
        var moduleCode = "0";
        var url = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorMod.act?moduleCode="+moduleCode;
        location = url;
        return "MOD";
      }else{
        if(message.indexOf(find)!="-1"){
          var re = eval("/"+find+"/g");
          sun = message.replace(re, replacement);
          message = sun;
        }
      }
  }
  return message;
  }else{
  alert(rtJson.rtMsrg); 
  }
}

/**
 * 
 * @param moduleCode  模块编号：0-邮件；1-短信；2-手机短信
 * @param jsonStr 邮件/短信/手机短信 的json数据
 * @return
 */
function getJsonData(moduleCode, jsonStr){
  var url = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/addJsonContent.act";
  var rtJson = getJsonRs(url, "jsonStr=" + jsonStr +"&moduleCode=" + moduleCode);
  if (rtJson.rtState == "0") {
    alert(rtJson.rtMsrg);
  }else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 查询禁止词语过滤提示内容
 * @param moduleCode
 * @return
 */
function bannedFunc(moduleCode){
  location = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorBanned.act?moduleCode="+moduleCode;
}

/**
 * 查询敏感词语过滤提示内容
 * @param moduleCode
 * @return
 */
function modFunc(moduleCode){
  location = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorMod.act?moduleCode="+moduleCode;
}

/**
 * 内部邮件通过审核
 * @param idStrsVal
 * @param textFlag
 * @return
 */
function emailPass(idStrsVal, textFlag){
  var moduleCode = "0";
  var url = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/sendMailByCoren.act";
  var bodyStr = textFlag.substr(0, textFlag.length - 1);
  var rtJsons = getJsonRs(url,"bodyId="+bodyStr);
  if(rtJsons.rtState == "0"){
    //alert(rtJsons.rtMsrg); 
  }else{
    alert(rtJsons.rtMsrg); 
  }
  
  //var urls = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getSmsRemind.act";
  //var rtJson = getJsonRs(urls, "moduleCode="+moduleCode);
  //if(rtJson.rtState == "0"){
    //if(rtJson.rtData == "1"){
      emailPassFunc(idStrsVal);
    //}
  //}else{
    //alert(rtJson.rtMsrg); 
  //}
}

function emailPassFunc(idStrsVal){
  var urls = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getPassEmailJson.act";
  var rtJson = getJsonRs(urls, "seqId="+idStrsVal);
  if(rtJson.rtState == "0"){
    //alert(rsText);
    for(var i = 0; i < rtJson.rtData.length; i++){
      var formId = rtJson.rtData[i].cont.FROM_ID;
      var toId = rtJson.rtData[i].cont.TO_ID;
      var contents = rtJson.rtData[i].cont.CONTENT;
      var bodyId = rtJson.rtData[i].cont.BODY_ID;
      var urls =  contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/doEmailBack.act";
      var rtJsons = getJsonRs(urls,"formId="+formId+"&toId="+toId+"&content="+contents+"&emailBodyId="+bodyId);
      if(rtJsons.rtState == "0"){
        //alert(rtJson.rtMsrg); 
      }else{
       alert(rtJson.rtMsrg); 
      }
    }
    //alert(rtJson.rtMsrg); 
  }else{
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 内部短信通过审核
 * @param idStrsVal
 * @return
 */

function smsPass(idStrsVal){
  var url = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getPassSmsJson.act";
  var rtJson = getJsonRs(url,"seqId="+idStrsVal);
  if(rtJson.rtState == "0"){
    for(var i = 0; i < rtJson.rtData.length; i++){
      var formId = rtJson.rtData[i].cont.FROM_ID;
      var toId = rtJson.rtData[i].cont.TO_ID;
      var contents = rtJson.rtData[i].cont.CONTENT;
      var type = rtJson.rtData[i].cont.SMS_TYPE;
      var remindUrl = rtJson.rtData[i].cont.REMIND_URL;
      var urls =  contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/doSmsBack.act";
      var rtJsons = getJsonRs(urls,"formId="+formId+"&toId="+toId+"&content="+contents+"&type="+type+"&remindUrl="+remindUrl);
      if(rtJsons.rtState == "0"){
        //alert(rtJson.rtMsrg); 
      }else{
       alert(rtJson.rtMsrg); 
      }
    }
    //alert(rtJson.rtMsrg); 
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function mobileSmsPass(idStrsVal){
  var url = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getPassMobileSmsJson.act";
  var rtJson = getJsonRs(url,"seqId="+idStrsVal);
  if(rtJson.rtState == "0"){
    for(var i = 0; i < rtJson.rtData.length; i++){
      var fromId = rtJson.rtData[i].cont.FROM_ID;
      var toId = rtJson.rtData[i].cont.TO_ID || "";
      var contents = rtJson.rtData[i].cont.CONTENT;
      var phone = rtJson.rtData[i].cont.PHONE1 || "";
      var sendTime = rtJson.rtData[i].cont.SEND_TIME;
      var urls =  contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/doMobileSmsBack.act";
      var rtJsons = getJsonRs(urls,"fromId="+fromId+"&toId="+toId+"&content="+encodeURIComponent(contents)+"&phone="+phone+"&sendTime="+sendTime);
      if(rtJsons.rtState == "0"){
        //alert(rtJson.rtMsrg); 
      }else{
       alert(rtJson.rtMsrg); 
      }
    }
    //alert(rtJson.rtMsrg); 
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function highLight(contents){
  var url = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCensorWords.act";
  var rtJson = getJsonRs(url);
  var replaceStr  = "";
  var message = contents;
  if(rtJson.rtState == "0"){
  for(var i = 0; i < rtJson.rtData.length; i++) {
    var find = rtJson.rtData[i].find;
    var replacement = rtJson.rtData[i].replacement;
      if(message.indexOf(find) != "-1" && replacement == "{MOD}"){
        var re = eval("/"+find+"/g");
        var colorStr = "<span style='color:#000000;background: #FFFF00;text-decoration: underline;' title='审核词汇'>"+find+"</span> ";
        replaceStr = message.replace(re, colorStr);
        //document.getElementById("contents").innerHTML = replaceStr;
        return replaceStr;
      }else if(message.indexOf(find) != "-1" && replacement != "{BANNED}" && replacement != "{MOD}"){
        if(message.indexOf(find)!="-1"){
          var re = eval("/"+find+"/g");
          var colorStr = "<span style='color:#0000FF;background: #FFFF00;text-decoration: underline;' title='过滤词汇'>"+find+"</span>";
          sun = message.replace(re, colorStr);
          message = sun;
        }
      }
  }
  }else{
  alert(rtJson.rtMsrg); 
  }
}

function Test(leaveDate1,leaveDate2){ 
//var leaveDate1 = document.getElementById("leaveDate1"); 
//var leaveDate2 = document.getElementById("leaveDate2"); 
  var leaveDate1Array = leaveDate1.value.trim().split(" "); 
  var leaveDate2Array = leaveDate2.value.trim().split(" "); 
  var type1 = "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2); 
  
  if(leaveDate1.value && leaveDate2.value){
    if(leaveDate1.value > leaveDate2.value){
      alert("错误 起始时间不能大于截至时间！");
      leaveDate1.focus(); 
      leaveDate1.select(); 
      return false;
    }
  }
  if(leaveDate1.value){
    if(leaveDate1Array.length != 2){ 
      alert("错误,起始时间格式不对，应形如 1999-01-02 14:55:20"); 
      leaveDate1.focus(); 
      leaveDate1.select(); 
      return false; 
    }else{ 
      if(!isValidDateStr(leaveDate1Array[0]) || leaveDate1Array[1].match(re1) == null || leaveDate1Array[1].match(re2) != null){ 
        alert("错误,起始时间格式不对，应形如 1999-01-02 14:55:20"); 
        leaveDate1.focus(); 
        leaveDate1.select(); 
      return false; 
      } 
    } 
  }
  if(leaveDate2.value){
    if(leaveDate2Array.length != 2){ 
      alert("错误,截止时间格式不对，应形如 1999-01-02 14:55:20"); 
      leaveDate2.focus(); 
      leaveDate2.select(); 
      return false; 
    }else{ 
      if(!isValidDateStr(leaveDate2Array[0])||leaveDate2Array[1].match(re1) == null || leaveDate2Array[1].match(re2) != null){ 
      alert("错误,截止时间格式不对，应形如 1999-01-02 14:55:20"); 
      leaveDate2.focus(); 
      leaveDate2.select(); 
      return false; 
      } 
    } 
  }
  return true;
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

/**
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件

 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"280\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}

/**
 * 隐藏显示控件
 * 
 * @param cntrlId
 * @return
 */
function showCntrl(cntrlId) {
  if ($(cntrlId)) {
    if ($(cntrlId).style.display) {
      $(cntrlId).style.display = '';
    } else {
      $(cntrlId).style.display = 'none';
    }
  }
}

/**
 * 是否对信息过滤有审核权限
 * @param moduleCode
 * @return
 */

function isPrivFunc(moduleCode){
  var url = contextPath + "/yh/core/funcs/system/censorcheck/act/YHCensorCheckAct/getCheckUser.act";
  var rtJson = getJsonRs(url, "moduleCode=" + moduleCode);
  if(rtJson.rtState == "0"){
    if(rtJson.rtData == "1"){
      WarningMsrg('您没有审核权限！', 'msrg','forbidden');
    }else{
      showCntrl('isPriv');
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}