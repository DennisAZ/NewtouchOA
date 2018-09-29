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
 * 
 */
 function checkBoxRender(cellData, recordIndex, columIndex){
   var emailBodyId = this.getCellData(recordIndex,"emailBodyId");
   var emailId = this.getCellData(recordIndex,"emailId");
   var html = "";
   var checkValue = "";
   if(emailId){
     checkValue = emailBodyId + "*" + emailId;
   }else{
     checkValue = emailBodyId;
   }
   html += "<input type=\"checkbox\" name=\"check_diay_search\" value=\"" + checkValue + "\" onclick=\"checkSelf()\" >";
   return html;
 }
 
 function checkSearchAll(){
   var checkArray = $$('input');
   for(var i = 0 ; i < checkArray.length ; i++){
     if(checkArray[i].name == "check_diay_search" ){
      checkArray[i].checked = $('allbox').checked ;
     }
   }
 }
 function checkSelf(){
    var allCheck = $('allbox');
    if(allCheck.checked){
      allCheck.checked = false;
    }
}
/**
 * 提示错误信息
 * @param msrg
 * @param cntrlId
 * @param type
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"360\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}
/**
 * 清空选择的用户
 */
function ClearUser(){
  var args = $A(arguments);
  for(var i = 0; i < args.length; i++ ){
    var cntrl = $(args[i]);
    if(cntrl){
      if (cntrl.tagName.toLowerCase() == "td"
          || cntrl.tagName.toLowerCase() == "div"
          || cntrl.tagName.toLowerCase() == "span") {
        cntrl.innerHTML =  '';
      } else{
        cntrl.value ='';
      }
    }
  }
}
 //id装换成文本
 // bindDesc([{cntrlId:"leader1", dsDef:"PERSON,SEQ_ID,USER_NAME"}
 //,{cntrlId:"leader2", dsDef:"PERSON,SEQ_ID,USER_NAME"}
 //,{cntrlId:"manager", dsDef:"PERSON,SEQ_ID,USER_NAME"}
 //  ,{cntrlId:"deptParent", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}
 //  ]);
 /**
  * 控件的显隐
  */
 function showTr(cntrlId,dom){
   try{
     $(cntrlId).style.display =($(cntrlId).style.display == "none") ? "":"none";
   }catch(e){
   //  alert(e);
     $(cntrlId).setAttribute('style').display = ($(cntrlId).getAttribute('style').display == "none") ? "":"none";
   }
 }
 /**
  * 处理附件的显示
  * @param cntrlId
  * @return
  */
 function showAttach(attrIds,attrNames,cntrId){
   var reStr = "<div id='attrDiv'>";
   var ym = "";
   var attrId = ""
   var attrIdArrays = attrIds.split(",");
   var attrNameArrays = attrNames.split("*");
   for(var i = 0 ; i <= attrIdArrays.length; i++){
     if(!attrIdArrays[i]){
       continue;
     }
     var key = attrIdArrays[i];
     var attrName = attrNameArrays[i];
     var value = attrName.substring( attrName.indexOf("_")+1, attrName.length);
     reStr += "<a href=\"javascript:downFile(\'" + key + "\',\'" + value + "\');\" title=\"" + value + "\">" + value + "</a><br>";
     //MODULE=email&amp;YM=1001&amp;ATTACHMENT_ID=216664316&amp;ATTACHMENT_NAME=SoftMgrUninst.exe
   }
   reStr += "</div>";
   if(cntrId){
     $(cntrId).innerHTML = reStr;
   }else{
     document.write(reStr);
   }
 }
 /**
  * 需要挂接菜单，可以直接输出或者注入到dom控件中
  * 菜单的挂接通过函数处理
  * @param attrIds
  * @param attrNames
  * @param cntrId
  * @return
  */
 function showAttach2(attrIds,attrNames,cntrId,menuFun){
   var reStr = "<div id='attrDiv'>";
   var ym = "";
   var attrId = ""
   var attrIdArrays = attrIds.split(",");
   var attrNameArrays = attrNames.split("*");
   for(var i = 0 ; i <= attrIdArrays.length; i++){
     if(!attrIdArrays[i]){
       continue;
     }
     var key = attrIdArrays[i];
     var attrName = attrNameArrays[i];
     var value = attrName.substring( attrName.indexOf("_") + 1, attrName.length);
     reStr += "<a href=\"javascript:downFile(\'" + key + "\',\'" + value + "\');\" title=\"" + value + "\" onmouseover='createDiv(event,\"1\")'>" + value + "</a><br>";
     //MODULE=email&amp;YM=1001&amp;ATTACHMENT_ID=216664316&amp;ATTACHMENT_NAME=SoftMgrUninst.exe
   }
   reStr += "</div>";
   if(cntrId){
     $(cntrId).innerHTML = reStr;
   }else{
     document.write(reStr);
   }
 }
 
 /**
  * 文件下载
  * @param attrId
  * @param name
  * @return
  */
 function downFile(attrId,name){
   var ym = attrId.split("_")[0];
   var attrId = attrId.split("_")[1];
   var attrName = name;
   var aPath = ym + "/" + attrId + "_" + attrName;
   var url = contextPath + '/getFile?uploadFileNameServer=core/funcs/email/attachment/' + aPath;
   window.open(url);
 }
 /**
  * 
  * @return
  */
function getChecked(pre) {
  var className = "check_diay_search";
  var ids= ""
  var checkArray = $$('input[name=check_diay_search]');
  for(var i = 0 ; i < checkArray.length ; i++){
    if(checkArray[i].name == className && checkArray[i].checked ){
      var obj = checkArray[i].value;
      var value = "";
      if(pre == 1){
        var values = obj.split("*");
        if(values.length > 1 && values[1]){
          value = values[1];
        }else{
          value = values[0];
        }
      }else{
        if(obj.indexOf("*") > -1){
          var values = obj.split("*");
          if(values.length > 1 && obj[1]){
            value = values[0];
          }else{
            value = values[0];
          }
        }else{
          value = obj;
        }
      }
      
      if(!value){
        continue;
      }
      if(ids != ""){
        ids += ",";
      }
      ids += value;
    }
  }
   return ids;
}
/**
 * 
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function userRender(cellData, recordIndex, columIndex) {
  //alert(cellData);
  var toIdArray = new Array();
  var emailBodyId = this.getCellData(recordIndex,"emailBodyId");
  var toId = cellData.toString();
  var copyToId = this.getCellData(recordIndex,"copyToId");
  var secretToId = this.getCellData(recordIndex,"secretToId");
  if ((toId && toId != '-1') || copyToIds || secretToIds) {
    var toIds = toId.split(",");
    var copyToIds = copyToId ? copyToId.split(",") : "";
    var secretToIds = secretToId ? secretToId.split(",") : "";
    toIdArray = toIdArray.concat(toIds,copyToIds,secretToIds).without("");
    if(toIdArray.length == 1){
      var toIdSeq = cellData;
      if(!toId){
        toIdSeq = copyToId;
      }
      var deptName = getUserNameById(toIdSeq);
      return "<center> <span id=\"span_" + recordIndex + "_" + columIndex + "\">" + deptName + "</span></center> ";
    } else {
      return "<center> <a href=\"javascript:readStatus(" + emailBodyId + ");\">查看</a></center>";
    }
  } 
  return "";
}

/**
 * 
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function userRenderByIn(cellData, recordIndex, columIndex) {
  var isWebmail = this.getCellData(recordIndex,"isWebmail");
  if(isWebmail == "1"){
    var webToUser = this.getCellData(recordIndex,"webFromMail");
    var name = webToUser.substring(0,webToUser.indexOf("<"));
    var mail = webToUser.substring(webToUser.indexOf("<") +1 ,webToUser.indexOf(">"));
    var html =  "";
    if(name.trim()){
      if(name.length > 10){
        name = name.substring(0,10) + "...";
      }      
      html = name;
    }else{
      if (mail.length > 10) {
        mail = mail.substring(0, 10) + "..."
      }
      html = "&lt;" + mail + "&gt;";
    }
    
    return "<a href=\"javascript:\" title=\"" + webToUser +"\">" + html + "</a>";
  }else{
    return cellData;
  }
}
 /**
  * 
  * @param cellData
  * @param recordIndex
  * @param columIndex
  * @return
  */
 function userRenderByIn2(cellData, recordIndex, columIndex) {
   var isWebmail = this.getCellData(recordIndex,"isWebmail");
   if(isWebmail == "1"){
     var webToUser = this.getCellData(recordIndex,"webFromMail");
     var name = webToUser.substring(0,webToUser.indexOf("<"));
     var mail = webToUser.substring(webToUser.indexOf("<") +1 ,webToUser.indexOf(">"));
     var html =  "";
     if(name.trim()){
       if(name.length > 10){
         name = name.substring(0,10) + "...";
       }      
       html = name;
     }else{
       if (mail.length > 10) {
         mail = mail.substring(0, 10) + "..."
       }
       html = "&lt;" + mail + "&gt;";
     }
     
     return "<a href=\"javascript:\" title=\"" + webToUser +"\">" + html + "</a>";
   }else{
     return userRender2(cellData, recordIndex, columIndex,this);
   }
 }
function userRender2(cellData, recordIndex, columIndex,obj) {
  //alert(cellData);
  var toIdArray = new Array();
  var emailBodyId = obj.getCellData(recordIndex,"emailBodyId");
  var toId = cellData.toString();
  var copyToId = obj.getCellData(recordIndex,"copyToId");
  var secretToId = obj.getCellData(recordIndex,"secretToId");
  var toIds = toId.split(",");
  var copyToIds = copyToId ? copyToId.split(",") : "";
  var secretToIds = secretToId ? secretToId.split(",") : "";
  toIdArray = toIdArray.concat(toIds,copyToIds,secretToIds).without("");
  if(toIdArray.length == 1){
    var toIdSeq = cellData;
    if(!toId){
      toIdSeq = copyToId;
    }
    var deptName = getUserNameById(toIdSeq);
    return "&nbsp;&nbsp;<span id=\"span_" + recordIndex + "_" + columIndex + "\">" + deptName + "</span>";
  }else{
    return "&nbsp;&nbsp;<a href=\"javascript:readStatus(" + emailBodyId + ");\">查看</a>";
  }
}
/**
 * 取得用户姓名
 * @return
 */
function getUserNameById(userId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/getUserName.act";
  var rtJson = getJsonRs(url , "userId=" +  userId);
  if(rtJson.rtState == '0'){
    return rtJson.rtData;
  }
}

 function reDeleteMail (type,url) {
   var delete_str = getChecked();
   if(delete_str == "") {
     alert("要恢复邮件，请至少选择其中一封。");
     return;
   }
   msg='确认要恢复所选邮件吗？';
   //alert(delete_str);
   if(window.confirm(msg)) {
     var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/deletM.act?bodyId="+ delete_str + "&deType=" + type + "&PAGE_START=&BOX_ID=&FIELD=&ASC_DESC=";
     var rtJson = getJsonRs(url);
     if(rtJson.rtState == "0"){
       alert("邮件已恢复！");
     }
     location.reload();
     if(parent.mail_menu)
       parent.mail_menu.location.reload();
     //history.back();
   }
 }
 /**
  * 回复邮件正文
  * @param json
  * @return
  */
 function bindReply(json,fck,url){
     var obj = json.rtData.data;
     var fromName = "";
     $('subject').value ="RE: " + obj.subject;//回复地址
     $('ensize').value = 0;//附件大小
     
     $('toId').value = obj.fromId;//回复地址
     bindDesc([{cntrlId:"toId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
     if(obj.toWebmail){
       $('toWebmail').value = obj.toWebmail;
     }
     if(obj.toWebmailCopy){
       $('toWebmailCopy').value = obj.toWebmailCopy;
     }
     if(obj.toWebmailSecret){
       $('toWebmailSecret').value = obj.toWebmailSecret;
     }
     if(obj.fromWebmailId){
       $("fromWebmail").value = obj.fromWebmailId;
     }
     fromName = $('toIdDesc').value;
     if (json.rtData.data.isWebmail) {
       fromName = $('toWebmail').value;
     }
     
     
     var FCK = FCKeditorAPI.GetInstance(fck); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
     var FORM_MODE = FCK.EditingArea.Mode; //获取编辑区域的常量——源文件模式
     var editingAreaFrame = document.getElementById(fck + "___Frame");
     var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
     if(FORM_MODE == editModeSourceConst) {
       FCK.Commands.GetCommand( 'Source' ).Execute();
     }
     var sendTime = obj.sendTime;
     var contentHtml = "";
     if(!obj.content){
       contentHtml = obj.compressContent;
     }else{
       contentHtml = obj.content;
     }
     var content = fromName + ",您好！<br /> "
                   + "<br /> "
                   +  "<br /> "
                   +  " ========" + fromName + "在" + sendTime + "的来信中写道：========<br /> "
                   +  "<div style=\"padding-right: 5px; padding-left: 5px; padding-bottom: 5px; border-left: #000000 2px solid; padding-top: 5px\">" + contentHtml + "</div>"
                   +  " <br /> "
                   +  " =================================================<br /> "
                   +  " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;致<br /> "
                   +  " 礼！ ";
     FCK.EditingArea.Window.document.body.innerHTML = content;
 }
 /**
  * 回复所有邮件正文
  * @param json
  * @return
  */
 function bindReplyAll(json,fck,url){
     var obj = json.rtData.data;
     
     var userId = json.rtData.LoginUser;
     $('toId').value = changId(obj.toId,userId,obj.fromId);//回复地址
     $('copyToId').value = obj.copyToId.trim();//回复地址
     $('subject').value ="RE: " + obj.subject;//回复地址
    // $('attachmentId').value = obj.attachmentId;//回复地址
    // $('attachmentName').value = obj.attachmentName;//回复地址
     $('ensize').value = 0;//附件大小
     bindDesc([{cntrlId:"toId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
     if(obj.copyToId.trim()){
       $("tr_cc").style.display = "";
       bindDesc([{cntrlId:"copyToId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
     }
     if(obj.toWebmail){
       $('toWebmail').value = obj.toWebmail;
     }
     if(obj.toWebmailCopy){
       $('toWebmailCopy').value = obj.toWebmailCopy;
     }
     if(obj.toWebmailSecret){
       $('toWebmailSecret').value = obj.toWebmailSecret;
     }
     if(obj.fromWebmailId){
       $("fromWebmail").value = obj.fromWebmailId;
     }
     var FCK = FCKeditorAPI.GetInstance(fck); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
     var FORM_MODE = FCK.EditingArea.Mode; //获取编辑区域的常量——源文件模式
     var editingAreaFrame = document.getElementById(fck + "___Frame");
     var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
     if(FORM_MODE == editModeSourceConst) {
       FCK.Commands.GetCommand( 'Source' ).Execute();
     }
     var reFromName = $('toIdDesc').value;
     if (json.rtData.data.isWebmail) {
       reFromName = $('toWebmail').value;
     }
     var fromName = getUserNameById(obj.fromId);
     var sendTime = obj.sendTime;
     if(!obj.content){
       contentHtml = obj.compressContent;
     }else{
       contentHtml = obj.content;
     }
     var content = reFromName + ",您好！<br /> "
                   + "<br /> "
                   +  "<br /> "
                   +  " ========" + fromName + "在" + sendTime + "的来信中写道：========<br /> "
                   +  "<div style=\"padding-right: 5px; padding-left: 5px; padding-bottom: 5px; border-left: #000000 2px solid; padding-top: 5px\">" + contentHtml + "</div>"
                   +  " <br /> "
                   +  " =================================================<br /> "
                   +  " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;致<br /> "
                   +  " 礼！ ";
     FCK.EditingArea.Window.document.body.innerHTML = content;
 }
function removeId(str,id){
  var ids = str.split(",");
  var resultId = "";
  for(var i = 0 ; i < ids.length ; i++){
    if(!ids[i] || ids[i] == id){
      continue;
    }
    resultId += ids[i] + ",";
  }
  return resultId;
}

function changId(str , id , chang){
  var ids = str.split(",");
  var ids2 = new Array();
  var resultId = "";
  for(var i = 0 ; i < ids.length ; i++){
    if(!ids[i]){
      continue;
    }
    if(ids[i] == id){
      if(!ids2.contains(chang) && chang != id){
        ids2.push(chang);
      }
    }else{
      ids2.push(ids[i]);
    }
  }
  for(var i = 0 ; i < ids2.length ; i++){
    resultId += ids2[i] + ",";
  }
  return resultId;
}
 /**
  * 转发邮件正文
  * @param json
  * @return
  */
 function bindFw(json,fck,url){
     var obj = json.rtData.data;
     //$('toId').value = obj.fromId;//回复地址
     $('subject').value ="FW: " + obj.subject;//回复地址
     //$('attachmentId').value = obj.attachmentId;//回复地址
     //$('attachmentName').value = obj.attachmentName;//回复地址
     if(obj.attachmentId){
       var attinfo = turan_attach(obj.attachmentName,obj.attachmentId,"email");
       if(attinfo && attinfo.attachId){
         $('attachmentId').value = attinfo.attachId;
         $('attachmentName').value  = attinfo.attachName;
       }
     }
     $('ensize').value = obj.ensize;//附件大小
     if(obj.toWebmail){
       $('toWebmail').value = obj.toWebmail;
     }
     if(obj.toWebmailCopy){
       $('toWebmailCopy').value = obj.toWebmailCopy;
     }
     if(obj.toWebmailSecret){
       $('toWebmailSecret').value = obj.toWebmailSecret;
     }
     if(obj.fromWebmailId){
       $("fromWebmail").value = obj.fromWebmailId;
     }
     //alert(rsText);
     var fromName = getUserNameById(obj.fromId);
     var sendTime = obj.sendTime;
     
     var FCK = FCKeditorAPI.GetInstance(fck); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
     var FORM_MODE = FCK.EditingArea.Mode; //获取编辑区域的常量——源文件模式
     var editingAreaFrame = document.getElementById(fck + "___Frame");
     var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
     if(FORM_MODE == editModeSourceConst) {
       FCK.Commands.GetCommand( 'Source' ).Execute();
     }
     var fromName = getUserNameById(obj.fromId);
     var sendTime = obj.sendTime;
     var contentHtml = "";
     if(!obj.content){
       contentHtml = obj.compressContent;
     }else{
       contentHtml = obj.content;
     }
     
     var content = "您好！<br /> "
    	 + "<br /> "
    	 +  "<br /> "
    	 +  " ========" + fromName + "在" + sendTime + "的来信中写道：========<br /> "
    	 +  "<div style=\"padding-right: 5px; padding-left: 5px; padding-bottom: 5px; border-left: #000000 2px solid; padding-top: 5px\">" + contentHtml + "</div>"
    	 +  " <br /> "
    	 +  " =================================================<br /> "
    	 +  " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;致<br /> "
    	 +  " 礼！ ";
     
     if (!isTouchDevice) {
       var FCK = FCKeditorAPI.GetInstance(fck); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
       var FORM_MODE = FCK.EditingArea.Mode; //获取编辑区域的常量——源文件模式
       var editingAreaFrame = document.getElementById(fck + "___Frame");
       var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
       if(FORM_MODE == editModeSourceConst) {
         FCK.Commands.GetCommand( 'Source' ).Execute();
       }
       if(!obj.content){
         contentHtml = obj.compressContent;
       }else{
         contentHtml = obj.content;
       }
       FCK.EditingArea.Window.document.body.innerHTML = content;
     } else {
       $("contentTextarea") && ($("contentTextarea").value = content);
     }
 }
 
 function turan_attach(attachName,attachId,module){
   var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/transferFolderAct.act?attachId=" + attachId + "&attachName=" + encodeURIComponent(attachName) + "&module=" + module;
   var rtJson = getJsonRs(url);
   if(rtJson.rtState == "0"){
     return rtJson.rtData;
   }else{
     return "";
   }
 }
 /**
  * 
  * @param json
  * @param fck
  * @param url
  * @return
  */
 function bindCg(json,fck,url,resendUserId){
   var obj = json.rtData.data;
   if(resendUserId.trim()){
     obj.toId = resendUserId;
   }
   if(obj.toId == "-1"){
     $('toId').value = "";
   }else{
     $('toId').value = obj.toId;//回复地址
   }
   bindImportant("importCont",obj.important);
   $('copyToId').value = obj.copyToId;//回复地址
   $('secretToId').value = obj.secretToId;//回复地址
   $('subject').value = obj.subject;//回复地址
   if(obj.attachmentId){
     $('attachmentId').value = obj.attachmentId;//回复地址
     $('attachmentName').value = obj.attachmentName;//回复地址
   }
   $('ensize').value = obj.ensize;//附件大小
   if($('copyToId').value.trim() ||$('secretToId').value.trim() || $('toId').value.trim() ){
     bindDesc([{cntrlId:"toId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
       ,{cntrlId:"copyToId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
       ,{cntrlId:"secretToId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
     ]);
   }
   if(obj.toWebmail){
     $('toWebmail').value = obj.toWebmail;
   }
   if(obj.toWebmailCopy){
     $('toWebmailCopy').value = obj.toWebmailCopy;
   }
   if(obj.toWebmailSecret){
     $('toWebmailSecret').value = obj.toWebmailSecret;
   }
   if(obj.fromWebmailId){
     $("fromWebmail").value = obj.fromWebmailId;
   }
   
   var contentHtml = "";
   if(!obj.content){
     contentHtml = obj.compressContent;
   }else{
     contentHtml = obj.content;
   }
   var fromName = $('toIdDesc').value;
   
   if (isTouchDevice) {
     $("contentTextarea") && ($("contentTextarea").value = contentHtml);
   } else {
     var FCK = FCKeditorAPI.GetInstance(fck); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
     var FORM_MODE = FCK.EditingArea.Mode; //获取编辑区域的常量——源文件模式
     var editingAreaFrame = document.getElementById(fck + "___Frame");
     var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
     if(FORM_MODE == editModeSourceConst) {
       FCK.Commands.GetCommand( 'Source' ).Execute();
     }
     //var sendTime = obj.sendTime;
     FCK.EditingArea.Window.document.body.innerHTML = contentHtml;
   }
}
 /**
  * 
  * @param json
  * @param fck
  * @param url
  * @return
  */
 function bindResend(json,fck,url,resendUserId){
   var obj = json.rtData.data;
   if(!resendUserId){
     $('toId').value = "";
   }else{
     $('toId').value = resendUserId;//回复地址
   }
   bindImportant("importCont",obj.important);
   $('subject').value = obj.subject;//回复地址
   if(obj.attachmentId){
     //$('attachmentId').value = obj.attachmentId;//回复地址
     //$('attachmentName').value = obj.attachmentName;//回复地址
     var attinfo = turan_attach(obj.attachmentName,obj.attachmentId,"email");
     if(attinfo && attinfo.attachId){
       $('attachmentId').value = attinfo.attachId;
       $('attachmentName').value  = attinfo.attachName;
     }
   }
   $('ensize').value = obj.ensize;//附件大小
   if($('copyToId').value.trim() ||$('secretToId').value.trim() || $('toId').value.trim() ){
     bindDesc([{cntrlId:"toId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
       ,{cntrlId:"copyToId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
       ,{cntrlId:"secretToId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
     ]);
   }
   if(obj.toWebmail){
     $('toWebmail').value = obj.toWebmail;
   }
   if(obj.toWebmailCopy){
     $('toWebmailCopy').value = obj.toWebmailCopy;
   }
   if(obj.toWebmailSecret){
     $('toWebmailSecret').value = obj.toWebmailSecret;
   }
   if(obj.fromWebmailId){
     $("fromWebmail").value = obj.fromWebmailId;
   }
   var FCK = FCKeditorAPI.GetInstance(fck); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
   var FORM_MODE = FCK.EditingArea.Mode; //获取编辑区域的常量——源文件模式
   var editingAreaFrame = document.getElementById(fck + "___Frame");
   var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
   if(FORM_MODE == editModeSourceConst) {
     FCK.Commands.GetCommand( 'Source' ).Execute();
   }
   var fromName = $('toIdDesc').value;
   //var sendTime = obj.sendTime;
   if(!obj.content){
     contentHtml = obj.compressContent;
   }else{
     contentHtml = obj.content;
   }
   FCK.EditingArea.Window.document.body.innerHTML = contentHtml;
}
 /**
  *String field,String value,int seqId,String tableName 
  *
  *updateFlag([{seqId:1,dsDef:"EMAIL,DELETE_FLAG,1"}
  *,{seqId:'2,3,4',dsDef:"EMAIL,DELETE_FLAG,2"}
  *]);
  */
 function updateFlag(paramArrays){
   var queryParam = "";
   for (var i = 0; i < paramArrays.length; i++) {
     var param = paramArrays[i];
     var seqId = param.seqId;
   //  alert(seqId);
     if (!seqId) {
       continue;
     }
     if (queryParam.length > 0) {
       queryParam += "/";
     }
     queryParam += seqId + ";" + param.dsDef;
   }
   if (!queryParam) {
     alert("没有输入有效的参数！");
     return;
   }
   var rtJson = getJsonRs(contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/flag.act", "queryParam=" + queryParam);
   //alert(rtJson.rtState);
   if( rtJson.rtState != "0"){
     alert("不成功");
   }
 }
 /**
  * 清空编辑区邮件
  * @return
  */
 function clearEdit(){
   var FCK = FCKeditorAPI.GetInstance('Econtent'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
   var FORM_MODE = FCK.EditingArea.Mode;

   //获取编辑区域的常量——源文件模式
   var editingAreaFrame = document.getElementById('Econtent___Frame');
   var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
   if(FORM_MODE == editModeSourceConst) {
     FCK.Commands.GetCommand( 'Source' ).Execute();
   } 
   // $("formName").value = formName;
   FCK.EditingArea.Window.document.body.innerHTML = '';
 }
 /**
  * type[1收件箱|2发件箱]
  * [1发件箱已删除|2发件箱未读|3发件箱已读|4收件件箱已删除|5收件箱未读new|6收件箱已读]
  * 绑定邮件状态
  * @return
  */
 function bindStatus(emailBodyId,type,emailId){
   if(!emailId){
     emailId = "";
   }
   var status = mailStatus(emailBodyId,type,emailId);
   var img = "";
   var title = "";
   var html = "";
   switch (status){   
     case "1" :  
       img = "mailDel";
       title = "收件人已删除";
       break;  
     case "2"   : 
       img = "mailClose";
       title = "收件人未读";
       break;
     case "3"   : 
       img = "mailOpen";
       title = "收件人已读";
       break; 
//     case "4"   : 
//       img = "mailDel";
//       title = "发件人已删除";
 //      break; 
     case "5"   : 
       img = "mailNew";
       title = "新邮件";
       break;
     case "6"   : 
       img = "mailOpen";
       title = "已读";
       break;
     case "7" : 
       img = "mailOpen";
       title = "外部邮件";
     default :
       img = "";
       title = "";  
   }   
   if(img){
     html = "<img  src=\"" + imgPath + "/cmp/email/" + img + ".gif\" title=\"" + title + "\"align=\"absmiddle\">";
   }
   return html;
 }
 /**
  * 邮件状态绑定
  * @param cellData
  * @param recordIndex
  * @param columIndex
  * @return
  */
 function statusRenderByIn(cellData, recordIndex, columIndex) {
//   var toIdArray = new Array();
//   var emailBodyId = this.getCellData(recordIndex,"emailBodyId");
//   var toId = this.getCellData(recordIndex,"toId");
//   var copyToId = this.getCellData(recordIndex,"copyToId");
//   var secretToId = this.getCellData(recordIndex,"secretToId");
//   var toIds = toId.split(",");
//   var copyToIds = copyToId ? copyToId.split(",") : "";
//   var secretToIds = secretToId ? secretToId.split(",") : "";
//   toIdArray = toIdArray.concat(toIds,copyToIds,secretToIds).without("");
//   if(toIdArray.length == 1){
//     var html = bindStatus(emailBodyId,1);
//     return "<center> " + html + "</center> ";
//   }else{
//     return "<center> <a href=\"javascript:readStatus(" + emailBodyId + ");\"><img  src=\"" + imgPath + "/cmp/email/node_dept.gif\" title=\"查看详情\"></a></center> ";
//   }
   return this.getCellData(recordIndex,"status");
 }
 /**
  * 邮件大小处理
  * @param cellData
  * @param recordIndex
  * @param columIndex
  * @return
  */
 function mailSizeRender(cellData, recordIndex, columIndex) {
   //alert(cellData);
   var result = "";
   if(cellData  < 1024){
     cellData = 1024;
   }
   if(cellData >= 1024 &&cellData < 1024*1024 ){
     result = Math.round(cellData/1024) + " K ";
   }else if(cellData >= 1024*1024 &&cellData < 1024*1024*1024){
     result = Math.round(cellData/(1024*1024)) + " M ";
   }else if(cellData >= 1024*1024*1024){
     result = Math.round(cellData/(1024*1024*1024)) + " G ";
   }
   return result + "&nbsp;";
 }
 /**
  * 
  * @param ctrlid
  * @param align
  * @param click
  * @param offset
  * @param duration
  * @param timeout
  * @param layer
  * @param showid
  * @param maxh
  * @param drag
  * @return
  */
 function showMenu(ctrlid, align, click, offset, duration, timeout, layer, showid, maxh, drag) {
   //alert("ss");
  var ctrlobj = $(ctrlid);
  if(!ctrlobj) return;
  if(isUndefined(align)) align = 0;
  if(isUndefined(click)) click = false;
  if(isUndefined(offset)) offset = 0;
    if(isUndefined(duration)) duration = 2;
    if(isUndefined(timeout)) timeout = 200;
    if(isUndefined(layer)) layer = 0;
    if(isUndefined(showid)) showid = ctrlid;
    var showobj = $(showid);
    var menuobj = $(showid + '_menu');
    if(!showobj|| !menuobj) return;
    if(isUndefined(maxh)) maxh = 400;
    if(isUndefined(drag)) drag = false;
   
    if(click && jsmenu['active'][layer] == menuobj) {
    hideMenu(layer);
    return;
    } else {
    hideMenu(layer);
    }
   
    var len = jsmenu['timer'].length;
    if(len > 0) {
    for(var i=0; i<len; i++) {
    if(jsmenu['timer'][i]) clearTimeout(jsmenu['timer'][i]);
   }
    }
   
   initCtrl(ctrlobj, click, duration, timeout, layer);
    ctrlobjclassName = ctrlobj.className;
    ctrlobj.className += ' hover';
    initMenu(ctrlid, menuobj, duration, timeout, layer, drag);
   
    menuobj.style.display = 'block';
    if(!is_opera) {
    menuobj.style.clip = 'rect(auto, auto, auto, auto)';
    }
    setMenuPosition(showid, align, offset);
   
   if(is_ie && is_ie < 7) {
    if(!jsmenu['iframe'][layer]) {
    var iframe = document.createElement('iframe');
    iframe.style.display = 'none';
    iframe.style.position = 'absolute';
    iframe.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)';
    $('append_parent') ? $('append_parent').appendChild(iframe) : menuobj.parentNode.appendChild(iframe);
    jsmenu['iframe'][layer] = iframe;
    }
    jsmenu['iframe'][layer].style.top = menuobj.style.top;
    jsmenu['iframe'][layer].style.left = menuobj.style.left;
    jsmenu['iframe'][layer].style.width = menuobj.w;
    jsmenu['iframe'][layer].style.height = menuobj.h;
    jsmenu['iframe'][layer].style.display = 'block';
    }
   
    if(maxh && menuobj.scrollHeight > maxh) {
    menuobj.style.height = maxh + 'px';
    if(is_opera) {
    menuobj.style.overflow = 'auto';
    } else {
    menuobj.style.overflowY = 'auto';
    }
    }
   
    if(!duration) {
    setTimeout('hideMenu(' + layer + ')', timeout);
    }
    jsmenu['active'][layer] = menuobj;
   } 
 function setMenuPosition(showid, align, offset) {
  var showobj = $(showid);
  var menuobj = $(showid + '_menu');
  if(isUndefined(align)) align = 0;
  if(isUndefined(offset)) offset = 0;
  if(showobj) {
    if(align==0) {
      showobj.pos = getMousePos();
      showobj.X = showobj.pos['left']-20;
      showobj.Y = showobj.pos['top'];
    } else {
      showobj.pos = fetchOffset(showobj);
      showobj.X = showobj.pos['left'];
      showobj.Y = showobj.pos['top']+showobj.scrollHeight-3;
    }
    var menu_offset = getMenuOffset(showobj.id);
    showobj.w = showobj.offsetWidth;
    showobj.h = showobj.offsetHeight;
    menuobj.w = menuobj.offsetWidth;
    menuobj.h = menuobj.offsetHeight;
    //if(align == 1)
    //menuobj.style.width = ((menuobj.clientWidth < showobj.w ? showobj.w : menuobj.clientWidth)+2) + 'px';
    if(offset < 3) {
      menuobj.style.left = ((showobj.X + menuobj.w > document.body.clientWidth) && (showobj.X + showobj.w - menuobj.w >= 0) ? showobj.X + showobj.w - menuobj.w : showobj.X) - menu_offset['left'] + 'px';
      if(offset == 1)
        menuobj.style.top = showobj.Y - menu_offset['top'] + 'px';
      else if(offset == 2)
        menuobj.style.top = (showobj.Y - menuobj.h - menu_offset['top']) + 'px';
      else {
        menuobj.style.top=showobj.Y - menu_offset['top'] + 'px';
        var bb = (document.compatMode && document.compatMode!="BackCompat") ? document.documentElement : document.body;
        if(parseInt(menuobj.style.top) + menuobj.h > bb.clientHeight + bb.scrollTop)
          menuobj.style.top = showobj.Y - menuobj.h + 'px';
      }
    } else if(offset == 3) {
      menuobj.style.left = (document.body.clientWidth - menuobj.clientWidth) / 2 + document.body.scrollLeft - menu_offset['left'] + 'px';
      menuobj.style.top = (document.body.clientHeight - menuobj.clientHeight) / 2 + document.body.scrollTop - menu_offset['top'] + 'px';
    }
    if(menuobj.style.clip && !is_opera) {
      menuobj.style.clip = 'rect(auto, auto, auto, auto)';
    }//alert(menuobj.style.left+" "+menuobj.style.top)
  }
} 
function hideMenu(layer) {
    if(isUndefined(layer)) layer = 0;
    if(jsmenu['active'][layer]) {
    try {
    $(jsmenu['active'][layer].ctrlkey).className = ctrlobjclassName;
    } catch(e) {}
    clearTimeout(jsmenu['timer'][jsmenu['active'][layer].ctrlkey]);
    jsmenu['active'][layer].style.display = 'none';
    if(is_ie && is_ie < 7 && jsmenu['iframe'][layer]) {
    jsmenu['iframe'][layer].style.display = 'none';
    }
    jsmenu['active'][layer] = null;
    }
   }
/**
 * 字符切断
 * @param str
 * @param length
 * @return
 */
function strCut(str,length){
  if(str.length >= length){
    var temp = str.substring(0,length).lastIndexOf(",");
    if(temp){
      str = str.substring(0,temp) + "...";
    }
  }
  return str;
}
/**
 * 查看全部名单
 * @param valueCntrId
 * @param acntrlId
 * @param length
 * @return
 */
function showAllUser(valueCntrId,acntrlId,length){
   var str = $(valueCntrId).innerHTML;
   if(str.length >= length){
     var spanValue = str;
     str = strCut(str,80);
     $(valueCntrId).innerHTML = str;
     $(acntrlId).title = spanValue;
     $(acntrlId).href = "javascript:alert('" + spanValue + "')";
     $(acntrlId).style.display = "";
   }
}
/**
 * 取得用户姓名
 * @return
 */
function getUserNameById(userId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/getUserName.act";
  var rtJson = getJsonRs(url , "userId=" +  userId);
  if(rtJson.rtState == '0'){
    return rtJson.rtData;
  }
}
function goBack(){
  location= contextPath + '/core/funcs/email/outbox/index.jsp?boxId=0';
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
function changeBox(dom){
  //alert(dom.value);
  var ids = getChecked();
  if(ids == "") {
    alert("请至少选择其中一封需要转移的邮件！");
    $('opForm').reset();
    return;
  }
  var boxId = dom.value;
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/changMail2OtherBox.act?bodyIds="+ ids + "&boxId=" + boxId ;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    window.location.reload();
    if(parent.mail_menu)
      parent.mail_menu.location.reload();
  }
}