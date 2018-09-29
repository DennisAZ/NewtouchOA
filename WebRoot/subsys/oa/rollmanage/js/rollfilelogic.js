/**
 * 获取下拉框选项
 * getSecretFlag("RMS_SECRET","secret");
 * @param parentNo	代码编号
 * @param optionType	绑定的div
 * @param extValue  要选中的值
 
 * @return
 */
function getSecretFlag(parentNo,optionType,extValue){
	var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
	var url = requestURLStr + "/getSelectOption.act?parentNo=" + parentNo;
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ;
	}
	
	var prcs = rtJson.rtData;
	var selects = document.getElementById(optionType);
	for(var i=0;i<prcs.length;i++){
	  var prc = prcs[i];
	  var option = document.createElement("option"); 
	  option.value = prc.value; 
	  option.innerHTML = prc.text; 
	  selects.appendChild(option);
	  
	  if(extValue && (extValue == prc.value)){
      option.selected = true;
    }
	}
}

// 获取案卷下拉列表值

function getRmsRollSelect(optionType,extValue){
	var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
	var url = requestURLStr + "/getRmsRollSelectOption.act";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ;
	}
	
	var prcs = rtJson.rtData;
	var selects = document.getElementById(optionType);
	for(var i=0;i<prcs.length;i++){
	  var prc = prcs[i];
	  var option = document.createElement("option"); 
	  option.value = prc.rollId; 
	  option.innerHTML = prc.rollCode + " - " + prc.rollName; 
	  selects.appendChild(option);
	  
	  if(extValue && (extValue == prc.rollId)){
      option.selected = true;
    }
	  
	}
}
function getRmsRollSelect2(optionType,extValue){
  var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
  var url = requestURLStr + "/getRmsRollSelectOption2.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  
  var prcs = rtJson.rtData;
  var selects = document.getElementById(optionType);
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.rollId; 
    option.innerHTML = prc.rollCode + " - " + prc.rollName; 
    selects.appendChild(option);
    
    if(extValue && (extValue == prc.rollId)){
      option.selected = true;
    }
    
  }
}
//获取案卷下拉列表值

function checkSelectBox(extValue){
  var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getRmsRollSelect.act";
  var rtJson = getJsonRs(url);
  var selectObj =  $('rollId');
  //selectObj.length = 2;
  if(rtJson.rtState == "0"){
   for(var i = 0; i < rtJson.rtData.length; i++){
     var option = document.createElement("option");
     option.value = rtJson.rtData[i].seqId;
     option.innerHTML = rtJson.rtData[i].rollCode + " - " + rtJson.rtData[i].rollName;
     selectObj.appendChild(option);
     
     if(extValue && (extValue == rtJson.rtData[i].seqId)){
       option.selected = true;
     }
     
   }
  }
}
//获取归档期限下拉列表
function checkSelectBox3(extValue){
  var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getDeadlineRollSelect.act";
  var rtJson = getJsonRs(url);
  var selectObj =  $('deadline');
  if(rtJson.rtState == "0"){
   for(var i = 0; i < rtJson.rtData.length; i++){
     var option = document.createElement("option");
     option.value = rtJson.rtData[i].seqId;
     option.innerHTML = rtJson.rtData[i].classCode + " - " + rtJson.rtData[i].classDesc;
     selectObj.appendChild(option);
     if(extValue && (extValue == rtJson.rtData[i].seqId)){
       option.selected = true;
     }
     
   }
  }
}
function checkSelectBox2(extValue){
  var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getRmsRollSelect2.act";
  var rtJson = getJsonRs(url);
  var selectObj =  $('rollId');
  if(rtJson.rtState == "0"){
   for(var i = 0; i < rtJson.rtData.length; i++){
     var option = document.createElement("option");
     option.value = rtJson.rtData[i].seqId;
     option.innerHTML = rtJson.rtData[i].rollCode + " - " + rtJson.rtData[i].rollName;
     selectObj.appendChild(option);
     
     if(extValue && (extValue == rtJson.rtData[i].seqId)){
       option.selected = true;
     }
   }
  }
}
/**
 * 获取小编码表内容
 * @param classNo 如："RMS_URGENCY"
 * @param classCode	如:"2"
 * @return
 */
function getCodeName(classNo,classCode){
	var str = "";
  var urls = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getCodeName.act";
  var rtJsons = getJsonRs(urls , "classCode=" + classCode + "&classNo=" + classNo);
  if(rtJsons.rtState == '0'){
  	str = rtJsons.rtData;
  }else{
    alert(rtJson.rtMsrg);
  }
  return str;
}

//处理密级
function secretFunc(cellData, recordIndex, columIndex){
	var str = getCodeName("RMS_SECRET",cellData);
	return "<center>"+str+"</center>";
}
function fileWordFunc(cellData, recordIndex, columIndex){
  //var str = getCodeName("FILE_WORD",cellData);
  return "<center>"+cellData+"</center>";
}
function fileYearFunc(cellData, recordIndex, columIndex){
  var str = getCodeName("FILE_YEAR",cellData);
  return "<center>"+str+"</center>";
}
function issueNumFunc(cellData, recordIndex, columIndex){
  //var str = getCodeName("ISSUE_NUM",cellData);
  return "<center>"+cellData+"</center>";
}
//截取日期
function sendDateFunc(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
	var str = cellData.substr(0, 10);
	return "<center>"+ str +"</center>";
}

//居中对齐
function rollFileFunc(cellData, recordIndex, columIndex){
  return "<center>"+cellData+"</center>";
}
//归档期限
function deadlineFunc(cellData, recordIndex, columIndex){
	var str = "";
	var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct/getdeadline.act";
  	var rtJson = getJsonRs(url);
  	if(rtJson.rtState == "0")
  	{
	   for(var i = 0; i < rtJson.rtData.length; i++)
	   {
	     if(cellData == rtJson.rtData[i].seqId)
	     {
	       str = rtJson.rtData[i].class_desc;
	     }
	   }
	  }
	return "<center>"+str+"</center>";
}

//所属案卷function rmsRollNameFunc(cellData, recordIndex, columIndex){
	var str = "";
	var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getRmsRollDetail.act";
  var rtJson = getJsonRs(url, "seqId=" + cellData);
  if (rtJson.rtState == "0") {
  	var data = rtJson.rtData;
  	var rollName = data.rollName;
  	if(rollName){
  		str = rollName;
  	}
  }
	return "<center>"+str+"</center>";
}

/**
 * 紧急等级 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function urgencyFunc(cellData, recordIndex, columIndex){
	var str = "";
	if(cellData){
		str = getCodeName("RMS_URGENCY",cellData);
	}	
	return "<center>"+str+"</center>";
}

/**
 * 文件号 
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function rmsFileCodeFunc(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<center><a href=\"javascript:readRmsFileFunc("+seqId+");\">" + cellData + "</a></center>";
}
 function rmsFileCodeFunc1(cellData, recordIndex, columIndex){
   var seqId = this.getCellData(recordIndex,"seqId");
   return "<center><a href=\"javascript:readRmsFileFunc1("+seqId+",1);\">" + cellData + "</a></center>";
 }
/**
 * 查看文件号 * @param seqId
 * @return
 */
function readRmsFileFunc(seqId , flag){
 var URL = contextPath + "/subsys/oa/rollmanage/readFile.jsp?seqId=" + seqId;
   if (flag) {
     URL += "&flag=" + flag;
   }
  //openDialog(URL,'500', '400');
 	newWindow(URL,'500', '400')
}
 function readRmsFileFunc1(seqId , flag){
   var URL = contextPath + "/subsys/oa/rollmanage/rolllend/readFile.jsp?seqId=" + seqId;
    //openDialog(URL,'500', '400');
    newWindow(URL,'500', '400')
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


//销毁文件
function destroySingleFile(seqId){
	//deleteSingle
	var msg='确认要销毁该项文件么？';
	if(window.confirm(msg)){
		var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
		var url = requestURLStr + "/destroySingleFile.act";
		var rtJson = getJsonRs(url, "seqId=" + seqId );
		if(rtJson.rtState == "0"){		  
			location.reload();
		}else{
			alert(rtJson.rtMsrg); 
		  return ;
		}
	}
}

//选择框
function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
  var rollId = this.getCellData(recordIndex,"rollName");
	var str = "";
	var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getRmsRollDetail.act";
  var rtJson = getJsonRs(url, "seqId=" + rollId);
  if (rtJson.rtState == "0") {
  	var data = rtJson.rtData;
  	var status = data.status;  
  	if(status){
  		str = status;
  	}
  }
  
  if(str == 1){
  	return "";
  }
  
  //return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\"></center>";
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




/**
 * 组卷至 * @return
 */
function change_roll(){
	var idStrs = checkMags('deleteFlag');
	var rollId=document.getElementById("rollId").value;
	if(!idStrs) {
	  alert("要组卷文件，请至少选择其中一个。");
	  document.form1.reset();
	  return;
	}
	
  var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
	var url = requestURLStr + "/changeRoll.act";
  var rtJson = getJsonRs(url, "seqIdStr=" + idStrs + "&rollId=" + rollId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
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

//全部删除提示
function confirmDelAll() {
  if(confirm("确认要销毁已选中的文件吗？")) {
    return true;
  } else {
    return false;
  }
}


//批量销毁文件
function destroyAll() {
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要销毁文件，请至少选择其中一个。");
    return;
  }
  if(!confirmDelAll()) {
    return ;
  }  
  var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
	var url = requestURLStr + "/destroySingleFile.act";
  var rtJson = getJsonRs(url, "seqId=" + idStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 批量导出文件
 * @return
 */
function export_all(){
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要导出文件，请至少选择其中一个。");
    return;
  }
  var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
	var url = requestURLStr + "/exportFileToCsv.act";
  location =url +"?seqIdStr=" + idStrs;
}

/**
 * 文件管理中的操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function optsFile(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
	var rollId = this.getCellData(recordIndex,"rollName");
	var str = "";
	var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getRmsRollDetail.act";
  var rtJson = getJsonRs(url, "seqId=" + rollId);
  if (rtJson.rtState == "0") {
  	var data = rtJson.rtData;
  	var status = data.status;  
  	if(status){
  		str = status;
  	}
  }
  if(str == 1){
  	return "<center>案卷已封 </center>";
  }
  return "<center><a href=\"javascript:readRmsFileFunc("+seqId+");\">查看</a>&nbsp;&nbsp;<a href='fileModify.jsp?seqId="+seqId+"'>修改</a>&nbsp;&nbsp;<a href=\"javascript:destroySingleFile("+seqId+");\">销毁</a></center>";
}

/**
 * 档案查询中的操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function optsFile2(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
	var rollId = this.getCellData(recordIndex,"rollName");
	var str = "";
	var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getRmsRollDetail.act";
  var rtJson = getJsonRs(url, "seqId=" + rollId);
  if (rtJson.rtState == "0") {
  	var data = rtJson.rtData;
  	var status = data.status;  
  	if(status){
  		str = status;
  	}
  }
  if(str == 1){
  	return "<center>案卷已封 </center>";
  }
  return "<center><a href=\"javascript:readRmsFileFunc("+seqId+");\">查看</a></center>";
}

/**
 * 销毁档案中的操作 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function destroyOptsFile(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
	return "<center><a href='javascript:recoverFile("+ seqId + ")'>还原</a>&nbsp;&nbsp;<a href=\"javascript:delete_file("+seqId+");\">删除</a></center>";
}
/**
 * 借阅查询中的操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function lendFileopts(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
	var rollId = this.getCellData(recordIndex,"rollId");
	
	return "<center><a href='javascript:lendFile(\""+ seqId + "\",\"" + rollId  + "\" )'>借阅</a></center>";
}

/**
 * 还原单个文件
 * @param fileId
 * @return
 */
function recoverFile(fileId){
 var msg='确认要还原该项文件么？';
 if(window.confirm(msg)) {
	 
	 var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
	 var url = requestURLStr + "/recoverFile.act";
	 var rtJson = getJsonRs(url, "seqId=" + fileId );
	 if (rtJson.rtState == "0") {
	   window.location.reload();
	 }else {
	   alert(rtJson.rtMsrg); 
	 }
 }
}


/**
 * 批量还原文件
 * @return
 */
function recover_all(){
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要还原文件，请至少选择其中一个。");
    return;
  }
  if(!window.confirm("确认要还原已选中的文件吗？")) {
    return ;
  }  
  var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
	var url = requestURLStr + "/recoverFile.act";
	var rtJson = getJsonRs(url, "seqId=" + idStrs );
	if (rtJson.rtState == "0") {
	  window.location.reload();
	}else {
	   alert(rtJson.rtMsrg); 
	}
	
}

/**
 * 删除单个文件
 * @param fileId
 * @return
 */
function delete_file(fileId){
 var msg='确认要删除该项文件么？';
 if(window.confirm(msg)) {
	 var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
	 var url = requestURLStr + "/deleteFile.act";
	 var rtJson = getJsonRs(url, "seqId=" + fileId );
	 if (rtJson.rtState == "0") {
		 window.location.reload();
	 }else {
		 alert(rtJson.rtMsrg); 
	 }
	 
 }
 
}

/**
 * 删除多个文件
 * @return
 */
function delete_all(){
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要删除文件，请至少选择其中一个。");
    return;
  }
  if(!window.confirm("确认要删除已选中的文件吗？")) {
    return ;
  } 
	var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
	var url = requestURLStr + "/deleteFile.act";
	var rtJson = getJsonRs(url, "seqId=" + idStrs );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
	 alert(rtJson.rtMsrg); 
	}
}


/**
 * 借阅
 * @param seqId
 * @param rollId
 * @return
 */
function lendFile(seqId,rollId){
	var msg='确认要借阅该项文件么？';
	if(window.confirm(msg)) {
		var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsLendAct";
	  var url = requestURLStr + "/rmsLendRoll.act";
	  var rtJson = getJsonRs(url, "fileId=" + seqId + "&rollId=" + rollId);
	  if (rtJson.rtState == "0") {
		  //window.location.reload();
	  	$("showTitleDiv").hide();
	  	$("listContainer").hide();
	  	$("delOpt").hide();
	  	$("lendFileResultDiv").show();
	  	
		}else {
		   alert(rtJson.rtMsrg); 
		}
	}	
	
}

function showAllInfo(){
	$("lendFileResultDiv").hide();
	$("showTitleDiv").show();
	$("listContainer").show();
	$("delOpt").show();
}








