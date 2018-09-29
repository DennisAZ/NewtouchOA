
function doEdit(seqId){
	location.href = contextPath + "/subsys/jtgwjh/sendDoc/modify.jsp?seqId=" + seqId;
}
/**
 * 详细信息
 * @param seqId
 * @return
 */
function detail(seqId){
  var URL = contextPath + "/subsys/jtgwjh/sendDoc/detail.jsp?seqId=" + seqId;
  newWindow(URL,'820', '500');
}
/**
 * 打开新窗口  newWindow(URL,'740', '540');
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow(url,width,height){
	var locX=(screen.width-width)/2;
	var locY=(screen.height-height)/2;
	window.open(url, "meeting", 
			"height=" +height + ",width=" + width +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
			+ locY + ", left=" + locX + ", resizable=yes");
}
function deleteSingle(seqId){
	if(!window.confirm("确认要删除该发文登记信息吗？")){
		return ;
	}
	var requestURLStr = contextPath + "/yh/subsys/jtgwjh/docSend/act/YHDocSendAct";
	var url = requestURLStr + "/deleteFile.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
	 alert(rtJson.rtMsrg); 
	}
}
/**
 * 删除多个文件
 * @return
 */
function deleteAll(){
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要删除发文登记信息，请至少选择其中一个。");
    return;
  }
  if(!window.confirm("确认要删除已选中的发文登记信息 ？")) {
    return ;
  } 
	var requestURLStr = contextPath + "/yh/subsys/jtgwjh/docSend/act/YHDocSendAct";
	var url = requestURLStr + "/deleteFile.act";
	var rtJson = getJsonRs(url, "seqId=" + idStrs );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
	 alert(rtJson.rtMsrg); 
	}
}
function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
  return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" ></center>";
}

/**
 * 全选
 * @param field
 * @return
 */
function checkAll(field) {
  var deleteFlags = document.getElementsByName("deleteFlag");
  for(var i = 0; i < deleteFlags.length; i++) {
    deleteFlags[i].checked = field.checked;
  }
}

function checkSelf(){
  var allCheck = $('checkAlls');
  if(allCheck.checked){
    allCheck.checked = false;
  }
}

//取得选中选项
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

function renderDocType(cellData, recordIndex, columIndex){
  if(cellData == 0){
    return "普通";
  }
  else if(cellData == 1){
    return "公文";
  }
}

function renderDocKind(cellData, recordIndex, columIndex){
  if(cellData == 0){
    return "普通";
  }
  else if(cellData == 1){
    return "通知";
  }
}

function renderUrgentType(cellData, recordIndex, columIndex){
  if(cellData == 0){
    return "一般";
  }
  else if(cellData == 1){
    return "紧急";
  }
  else if(cellData == 2){
    return "特急";
  }
}

function renderSecurityLevel(cellData, recordIndex, columIndex){
  if(cellData == 0){
    return "非密";
  }
  else if(cellData == 1){
    return "秘密";
  }
  else if(cellData == 2){
    return "机密";
  }
}

function renderCreateDatetime(cellData, recordIndex, columIndex){
    return cellData.substring(0, 19);
}

/**
 * 激活盖章
 */
function startStamp(seqId){
  var requestURLStr = contextPath + "/yh/subsys/jtgwjh/docSend/act/YHDocSendStampAct";
  var url = requestURLStr + "/startStamp.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId );
  if (rtJson.rtState == "0") {
    alert("发文进入盖章流程！");
    window.location.reload();
  }else {
   alert(rtJson.rtMsrg); 
  }
}

/**
 * 盖章
 */
function toStamp(seqId, flag){
  var width = "800";
  var height = "600";
  var url = contextPath + "/subsys/jtgwjh/sendDoc/stamp/stamp.jsp?seqId="+seqId+"&flag="+flag;
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  window.open(url, "stamp", 
      "height=" +height + ",width=" + width +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
      + locY + ", left=" + locX + ", resizable=yes");
}

/**
 * 发送
 */
function sendSingle(docsendInfoId,flag){
//alert(mainDocId);
 // var  mainDocId  = this.getCellData(recordIndex,"mainDocId");
 // var  mainDocName  = this.getCellData(recordIndex,"mainDocName");
  
  var doc  = getSendDoc(docsendInfoId);
  if(!doc.seqId){
    return;
  }
  var mainDocId = doc.mainDocId;
  var mainDocName = doc.mainDocName;
  if(mainDocId && mainDocName){
    var fileRePath = "";
    if(flag){//盖章集权
      fileRePath = $("filePath").value;
    }else{
      fileRePath = loadWordToAIP(mainDocId,mainDocName,attachPath,serviceName,port,$("AIPDIV"));
    }
   addAipNote();
   var uploadAIP = updateAipToService(fileRePath,port,serviceName);
   if(uploadAIP){
     var requestURLStr = contextPath + "/yh/subsys/jtgwjh/docSend/act/YHDocSendAct";
     var url = requestURLStr + "/sendDocInfo.act";
     var rtJson = getJsonRs(url, "docsendInfoId=" + docsendInfoId );
     if (rtJson.rtState == "0") {
       alert("发文登记信息已加入发送队列！");
       if(flag){
         location.href = contextPath + '/subsys/jtgwjh/sendDoc/new.jsp';
       }
       else{
        window.location.reload();
       }
     }else {
      alert(rtJson.rtMsrg); 
     }
   }
  }else{
    var requestURLStr = contextPath + "/yh/subsys/jtgwjh/docSend/act/YHDocSendAct";
    var url = requestURLStr + "/sendDocInfo.act";
    var rtJson = getJsonRs(url, "docsendInfoId=" + docsendInfoId );
    if (rtJson.rtState == "0") {
      alert("发文登记信息已加入发送队列！");
      if(flag){
        location.href = contextPath + '/subsys/jtgwjh/sendDoc/new.jsp';
      }
      else{
       window.location.reload();
      }
    }else {
     alert(rtJson.rtMsrg); 
    }
  }
  //return;
 
}
function getSendDoc(seqId){
  var url = contextPath + "/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/getDetail.act?seqId=" + seqId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg);
  }
}


/*
 * 把aip文件上传到服务器去
 */
function updateAipToService(filePath,port,serviceName) {
  var  attachmentName = "";
  try{   
    var webObj=document.getElementById("HWPostil1");
        webObj.HttpInit();
        webObj.HttpAddPostCurrFile("FileBlody");  
        returnValue = webObj.HttpPost("http://"+serviceName+ ":" +port+ "/yh/subsys/jtgwjh/receiveDoc/saveFile/saveAip.jsp?filePath=" + encodeURIComponent(filePath) + "&fileName=" + encodeURIComponent(attachmentName));
        if("ok" == returnValue){
          return true;
          //alert("上传成功！"); 
        }else if("failed" == returnValue){
          alert("上传失败！");
        }
        //window.location.href  = "index.jsp" ;
  }catch(e){
    alert(e);
  }
  return false;
}

function renderDocTitle(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href=javascript:detail(" + seqId + ")>"+cellData+"</a>";
}

function renderStampStatue(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href=javascript:stampStatue(" + seqId + ")>"+cellData+"</a>";
}

/**
 * 详细信息
 * @param seqId
 * @return
 */
function stampStatue(seqId){
  var URL = contextPath + "/subsys/jtgwjh/sendDoc/stamp/stampStatue.jsp?seqId=" + seqId;
  newWindow(URL,'820', '500');
}

function renderReciveDeptDesc(cellData, recordIndex, columIndex){
  if(cellData.length > 50){
    cellData = cellData.substring(0 , 47) + "...";
  }
  return cellData;
}