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
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function optsList(cellData, recordIndex, columIndex){
	var contentId = this.getCellData(recordIndex,"seqId");
	var sortId = this.getCellData(recordIndex,"sortId");
	//alert(contentId + " >>" + sortId);
	var showInfoStr = "<a href=javascript:modefyFile('" + contentId + "','" + sortId + "')>编辑</a>&nbsp;&nbsp;"
    + "<a href=javascript:showFile('" + sortId + "')>查看文件</a>";
	return "<center>"+ showInfoStr + "</center>";
}
/**
 * 编辑文件
 * @param contentId
 * @param folderId
 * @return
 */
function modefyFile(contentId,folderId){
	location.href = contextPath + "/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/edit.jsp?seqId=" + folderId + "&contentId=" + contentId;
}
/**
 * 查看文件
 * @param sortId
 * @return
 */
function showFile(sortId){
	location.href = contextPath + "/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/index.jsp?sortId=" + sortId;
}

/**
 * 报送文件下的选择框
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function selectedRender(cellData, recordIndex, columIndex){
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
 * 文件报送
 * @return
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


