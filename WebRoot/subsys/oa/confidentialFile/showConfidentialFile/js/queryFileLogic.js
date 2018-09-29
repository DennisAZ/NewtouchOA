/**
 * 数据对齐
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function alignFunc(cellData, recordIndex, columIndex){
	return "<center>" + cellData + "</center>";
}

/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function optsList(cellData, recordIndex, columIndex){
	var contentId = this.getCellData(recordIndex,"seqId");
	var sortId = this.getCellData(recordIndex,"sortId");
	var showInfoStr = "<a href=javascript:modefyFile('" + contentId + "','" + sortId + "')>编辑</a>&nbsp;"
	return "<center>"+ showInfoStr + "</center>";
}

/**
 * 截取日期
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function sendDateFunc(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
	var str = "";
	var sendDate = cellData;
	if(sendDate){
		str = sendDate.substr(0,sendDate.length - 2);
	}
	return "<center>"+ str +"</center>";
}

/**
 * 获取目录路径
 * @param seqId
 * @return
 */
function getFolderPathById(seqId){
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct";
	var url = requestURLStr + "/getSortNameById.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
  	var data = rtJson.rtData;
  	$("folderPath").innerHTML = data.folderPath;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 获取文件夹名
 * @param seqId
 * @return
 */
function getFolderName(seqId){
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct";
	var getFolderNameUrl = requestURLStr + "/getFolderName.act?seqId=" + seqId;
	var json = getJsonRs(getFolderNameUrl);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson = json.rtData;	
	$("folderName").innerHTML = prcsJson.folderName;
}
/**
 * 报送状态
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function fileSendFunc(cellData, recordIndex, columIndex){
	var str = "未报送";
	if(cellData == "1"){
		str = "已报送";
	}
	 return "<center>"+ str + "</center>";
}
/**
 * 编辑文件
 * @param contentId
 * @param folderId
 * @return
 */
function modefyFile(contentId,folderId){
	location.href = contextPath + "/subsys/oa/confidentialFile/showConfidentialFile/queryFile/edit.jsp?seqId=" + folderId + "&contentId=" + contentId;
}
/**
 * 报送文件下的选择框
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
  return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"selectedSelf()\" ></center>";
}

/**
 * 全选
 * @param field
 * @return
 */
function selectedkAll(field) {
  var deleteFlags = document.getElementsByName("deleteFlag");
  for(var i = 0; i < deleteFlags.length; i++) {
    deleteFlags[i].checked = field.checked;
  }
}
function selectedSelf(){
	var allCheck = $('checkAlls');
  if(allCheck.checked){
    allCheck.checked = false;
  }
}
/**
 * 取得选中选项
 * @param cntrlId
 * @return
 */
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
 * 文件报送 * @return
 */
function fileSend(){
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct";
	var idStrs = checkMags('deleteFlag');
	var idStrArry = idStrs.split(",");
	var count = idStrArry.length;
  if(!idStrs) {
    alert("请至少选择其中一个。");
    return;
  }
	var url= requestURLStr + "/fileSend.act?contentId=" + idStrs;
	var json = getJsonRs(url);
	if(json.rtState == '0'){
		alert("文件已报送");
  	window.location.reload(); 	
  }else{
		alert(json.rtMsrg);
  }
}

/**
 * 文件撤回
 * @return
 */
function getBack(){
  var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct";
  var idStrs = checkMags('deleteFlag');
  var idStrArry = idStrs.split(",");
  var count = idStrArry.length;
  if(!idStrs) {
    alert("请至少选择其中一个。");
    return;
  }
  var url= requestURLStr + "/getBack.act?contentId=" + idStrs;
  var json = getJsonRs(url);
  if(json.rtState == '0'){
    alert("文件已撤回");
    window.location.reload(); 	
  }else{
    alert(json.rtMsrg);
  }
}
/**
 * 删除文件
 * @param contentId
 * @param folderId
 * @return
 */
function deleFile(contentId){
	var msg="确定要删除选择文件吗？这将不可恢复！";
	if(window.confirm(msg)){
		var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct";
		var url = requestURLStr + "/delCheckedFile.act";
	  var rtJson = getJsonRs(url, "seqIdStr=" + contentId);
	  if (rtJson.rtState == "0") {
	    window.location.reload();
	  } else {
	    alert(rtJson.rtMsrg); 
	  }
	}
}
/**
 * 操作(查询)
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function searchOptsList(cellData, recordIndex, columIndex){
	var contentId = this.getCellData(recordIndex,"seqId");
	var sortId = this.getCellData(recordIndex,"sortId");
	var showInfoStr = "<a href=javascript:modefyFile('" + contentId + "','" + sortId + "')>编辑</a>&nbsp;"
			showInfoStr += "<a href=javascript:deleFile('" + contentId + "')>删除</a>&nbsp;"
	return "<center>"+ showInfoStr + "</center>";
}

/**
 * 操作(全局搜索)
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function globalSearchOptsList(cellData, recordIndex, columIndex){
	var contentId = this.getCellData(recordIndex,"seqId");
	var sortId = this.getCellData(recordIndex,"sortId");
	var visiPriv;
	var newPriv;
	var downPriv;
	var ownerPriv;
	var dbSortParent;
	var dbSeqId;
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct";
	var url = requestURLStr + "/getAllPrivteById.act?seqId=" + sortId;
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == '0'){
		var prcs = rtJson.rtData;
		managePriv = prcs.managePriv;
		newPriv = prcs.newPriv;
		downPriv = prcs.downPriv;
	}
	var showInfoStr = "";
	if(managePriv == "1"){
		 showInfoStr = "<a href=javascript:modefyFile('" + contentId + "','" + sortId + "')>编辑</a>&nbsp;"
			 						+ "<a href=javascript:deleFile('" + contentId + "')>删除</a>&nbsp;"
	}
	return "<center>"+ showInfoStr + "</center>";
}

/**
 * 显示文件夹路径
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function shwoFolderFunc(cellData, recordIndex, columIndex){
	var sortId = this.getCellData(recordIndex,"sortId");
	var str = "";
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct";
	var url = requestURLStr + "/getSortNameById.act";
  var rtJson = getJsonRs(url, "seqId=" + sortId);
  if (rtJson.rtState == "0") {
  	var data = rtJson.rtData;
  	str = "/" + data.folderPath;
  } else {
    alert(rtJson.rtMsrg); 
  }
  return str
}










