var uploadNum = 0;
var uploadTotalFlag = true;
//上传成功回调函数
function uploadSuccessOver(file, serverData){
  $('showAtt').innerHTML = '';
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
       var attachmentId = json.data.attachmentId;
       var attachmentName = json.data.attachmentName;
       var attachmentSize = json.data.size;
       var ensize =  $('ensize').value;
       if(ensize){
         $('ensize').value = (json.data.size + parseInt(ensize));
       }else{
         $('ensize').value = json.data.size ;
       }// 附件大小
       var selfdefMenu = {
           office:["downFile","read","edit"], 
           img:["downFile","play"],  
           music:["downFile","play"],  
           video:["downFile","play"], 
           others:["downFile"]
       }
       attachMenuSelfUtilTable("showAttTbody","jtgw",attachmentName ,attachmentId, attachmentSize, '','','',selfdefMenu,uploadNum);
       $('attachMainShow').innerHTML = $('attachMainShow').innerHTML + attachmentName.replace('*',',');
       
       //计算附件大小
       var attachTotalCount = ($('ensize').value/1024).toFixed(0);
       if(attachTotalCount > uploadSize){
         $('attachTotalDiv').style.color = 'red';
       }
       else{
         $('attachTotalDiv').style.color = 'green';
       }
       
       var attachTotalCountShow = '';
       if(attachTotalCount > 1024){
         attachTotalCountShow = (attachTotalCount/1024).toFixed(1) + 'M';
       }
       else{
         attachTotalCountShow = attachTotalCount + 'K';
       }
       $('attachTotal').innerHTML = attachTotalCountShow;
       uploadNum++;
       $('showAttTable').style.display = "";
       $('attachTotalDiv').style.display = "";
    }
    $('fsUploadArea').style.display='none';
}

//删除回调函数
function deleteAttachBackHand(attachName,attachId,attrchIndex,j){
  var attachNameOld = $('attachmentName').value;
  var attachIdOld =  $('attachmentId').value;
  var attachNameArrays = attachNameOld.split("*");
  var attachIdArrays = attachIdOld.split(",");
  var attaName = "";
  var attaName2 = "";
  var attaId = "";
  $('ensize').value = '0';
  for(var i = 0 ; i < attachNameArrays.length ; i++){
    if(!attachIdArrays[i] || attachIdArrays[i] == attachId){
      continue;
    }
    attaName += attachNameArrays[i] + "*";
    attaName2 += attachNameArrays[i] + ",";
    attaId += attachIdArrays[i] + ",";
  }
  
  $('attachmentId').value = attaId;
  $('attachmentName').value = attaName;
  $('attachMainShow').innerHTML = attaName2;
  
  $('attachment_tr_'+j).remove();
  
  //计算附件大小
  var attachTotalCount = 0;
  for(var i = 0; i < uploadNum; i++){
    if($('attach_Size_'+i)){
      attachTotalCount = parseInt(attachTotalCount) + parseInt($('attach_Size_'+i).value);
    }
  }
  $('ensize').value = attachTotalCount;
  attachTotalCount = (attachTotalCount/1024).toFixed(0);
  if(attachTotalCount > uploadSize){
    $('attachTotalDiv').style.color = 'red';
  }
  else{
    $('attachTotalDiv').style.color = 'green';
  }
  
  var attachTotalCountShow = '';
  if(attachTotalCount > 1024){
    attachTotalCountShow = (attachTotalCount/1024).toFixed(1) + 'M';
  }
  else{
    attachTotalCountShow = attachTotalCount + 'K';
  }
  $('attachTotal').innerHTML = attachTotalCountShow;
  return true;
}

function delDocAction(seqId) {
  var param = 'seqId=' + seqId;
  var url = contextPath + "/yh/subsys/jtgwjh/ntko/act/YHDocAct/delDoc.act";
  var json = getJsonRs(url , param);
  if(json.rtState == '0'){
    $('mainDocId').value = '';
    $('mainDocName').value = '';
    $('wordDocIframe').style.display = 'none';
    $('tableWordTable').style.display = '';
  }
}

function $C(tag){
  return document.createElement(tag);
}