function uploadSuccessOver(file, serverData){
  try {
    var progress = new FileProgress(file, this.customSettings.progressTarget);
    progress.toggleCancel(false);
    var json = null;
    json = serverData.evalJSON();
    if (json.state == "1") {
      progress.setError();
      progress.setStatus("上传失败：" + serverData.substr(5));
      var stats=this.getStats();
      stats.successful_uploads--;
      stats.upload_errors++;
      this.setStats(stats);
    }else {
      $('attachmentId').value += json.data.attachmentId;
      $('attachmentName').value += json.data.attachmentName;
      var attachmentIds = $("attachmentId").value;
      var attachmentName = $("attachmentName").value;
      var ensize =  $('ensize').value;
      if(ensize){
        $('ensize').value =(json.data.size + parseInt(ensize));
      }else {
        $('ensize').value =json.data.size ;
      }//附件大小
      var  selfdefMenu = {
    		    office:["downFile","read","deleteFile"], 
    		    img:["downFile","deleteFile"],
    		    music:["downFile","deleteFile"],  
    		    video:["downFile","deleteFile"], 
    		    others:["downFile","deleteFile"]
    		  }
      attachMenuSelfUtil("showAtt","notify",$('attachmentName').value ,$('attachmentId').value,'','','',selfdefMenu);
    }
  }catch(ex) {
    this.debug(ex);
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
    //attachMenuUtil("showAtt","news",null,$('attachmentName').value ,$('attachmentId').value,false);
    
    //reStr += "<a href=\"javascript:downFile(\'" + key + "\',\'" + value + "\');\" title=\"" + value + "\">" + value + "</a><br>";
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

function upload_attach_group() {
  saveNewsByUp();
}

function upload_attach() {
  $("btnFormFile").click();
}

/**
 * 处理文件上传
 */
function handleSingleUpload(rtState, rtMsrg, rtData) {
  if (rtState != 0) {
    alert(rtMsrg);
    return;
  }
  var data = rtData.evalJSON(); 
  $('attachmentId').value +=  data.attrId;
  $('attachmentName').value +=  data.attrName;   
  var  selfdefMenu = {
    office:["downFile","read","deleteFile"], 
    img:["downFile","deleteFile"],
    music:["downFile","deleteFile"],  
    video:["downFile","deleteFile"], 
    others:["downFile","deleteFile"]
  }
  attachMenuSelfUtil("showAtt","notify",$('attachmentName').value ,$('attachmentId').value, '','','',selfdefMenu);
  removeAllFile();
  if (isUploadBackFun == true) {
    sendForm(savePar);
    isUploadBackFun = false;
  }
}

/**
 * 
 * @return
 */



//判断是否要显示短信提醒 
function getSysRemind(){ 
  var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=14"; 
  var rtJson = getJsonRs(requestUrl); 
  if(rtJson.rtState == "1"){ 
     alert(rtJson.rtMsrg); 
     return ; 
  } 
  var prc = rtJson.rtData; 
  var allowRemind = prc.allowRemind; 
  var defaultRemind = prc.defaultRemind;
  if(allowRemind=='2'){ 
    $("smsRemindDiv").style.display = 'none'; 
  }else{ 
    if(defaultRemind=='1'){ 
      $("mailRemind").checked = true; 
    } 
  } 
//return prc; 
 }

//浮动菜单文件的删除 
function deleteAttachBackHand(attachName,attachId,attrchIndex){ 
  var url= contextPath + "/yh/subsys/jtgwjh/notifyManage/act/YHJhNotifyInfoAct/delFloatFile.act?attachId=" + attachId +"&attachName=" + attachName ; 
  if (seqId) {
    url += "&seqId=" + seqId;
  }
  var json=getJsonRs(encodeURI(url)); 
  if(json.rtState =='1'){ 
    alert(json.rtMsrg); 
    return false; 
  }else { 
    prcsJson=json.rtData; 
    var updateFlag=prcsJson.updateFlag; 
    if(updateFlag){ 
      var ids = $('attachmentId').value ;
      if (!ids) {
        ids = ""; 
      }
      var names =$('attachmentName').value;
      if (!names) {
        names = ""; 
      }
      var idss = ids.split(",");
      var namess = names.split("*");
     
      var newId = getStr(idss , attachId , ",");
      var newname = getStr(namess , attachName , "*");  
     
      $('attachmentId').value = newId;
      $('attachmentName').value = newname;
      return true; 
   }else{ 
     return false; 
   }  
  } 
}
function getStr(ids , id , split) {
  var str = "";
  for (var i = 0 ; i< ids.length ;i ++){
    var tmp = ids[i];
    if (tmp) {
      if (tmp != id) {
        str += tmp + split;
      }
    }
  }
  return str;
}
/** 
*js代码 
*是否显示手机短信提醒 
*/ 
function moblieSmsRemind(remidDiv,remind){ 
  var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=14"; 
  var rtJson = getJsonRs(requestUrl); 
  if(rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  } 
  var prc = rtJson.rtData; 
  moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中   
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