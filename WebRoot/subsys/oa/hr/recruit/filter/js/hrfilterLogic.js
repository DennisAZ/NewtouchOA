var personId = "";
/**
 * 内容居中
 * 
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function infoCenterFunc(cellData, recordIndex, columIndex) {
	return "<center>" + cellData + "</center>";
}
/**
 * 日期
 * 
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function splitDateFunc(cellData, recordIndex, columIndex) {
	var str = "";
	if (cellData) {
		str = cellData.substr(0, 10);
	}
	return "<center>" + str + "</center>";
}
/**
 * 操作
 * 
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function optsList(cellData, recordIndex, columIndex) {
	var seqId = this.getCellData(recordIndex, "seqId");
	var endFlag = this.getCellData(recordIndex, "endFlag");
	//alert(endFlag);
	var optStr = "";
	var infoStr = "";
	if(endFlag != "2" && endFlag !="1"){
	  var stepFlag = this.getCellData(recordIndex, "stepFlag");
	  var nextTransaStep = "nextTransaStep";
	  if(stepFlag == 1){
	  }
	  else{
	    nextTransaStep += stepFlag*1 - 1;
	  }
	  var nextTransaStepId = this.getCellData(recordIndex, nextTransaStep);
	  if(personId == nextTransaStepId){
		  infoStr = "&nbsp;&nbsp;<a href=javascript:dealWith('" + seqId + "')>处理</a>";
	  }
	}
	optStr = "<a href=javascript:hrFilterDetail('" + seqId+ "')>详细信息</a>"
	+ infoStr
	+ "&nbsp;&nbsp;<a href=javascript:editHrFilter('" + seqId + "')>修改</a>"
	+ "&nbsp;&nbsp;<a href=javascript:deleteInfo('" + seqId + "')>删除</a>";
	return "<center>" + optStr + "</center>";
}

/**
* 详细信息
* @param seqId
* @return
*/
function hrFilterDetail(seqId){
 var URL = contextPath + "/subsys/oa/hr/recruit/filter/filterDetail.jsp?seqId=" + seqId;
 newWindow(URL,'820', '500');
}
/**
 * 处理
 * @param seqId
 * @return
 */
function dealWith(seqId){
	var url = contextPath + "/subsys/oa/hr/recruit/filter/dealWith.jsp?seqId=" + seqId;
	location.href = url;
}
/**
 * 编辑
 * @param seqId
 * @return
 */
function editHrFilter(seqId){
	var url = contextPath + "/subsys/oa/hr/recruit/filter/editHrFilter.jsp?seqId=" + seqId;
	location.href = url;
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



function deleteInfo(seqId){
	if(!window.confirm("确认要删除该记录吗？")) {
		return ;
	} 
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct";
	var url = requestURLStr + "/deleteHrFilter.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
	 alert(rtJson.rtMsrg); 
	}
}
function delete_all(){
	var idStrs = checkMags('deleteFlag');
	//alert(idStrs);
	if(!idStrs) {
		alert("要删除招聘筛选信息，请至少选择其中一条。");
		return;
	}
	if(!window.confirm("确认要删除所选记录吗？")) {
		return ;
	} 
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct";
	var url = requestURLStr + "/deleteHrFilter.act";
	var rtJson = getJsonRs(url, "seqId=" + idStrs );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
		alert(rtJson.rtMsrg); 
	}
}
function getPersonName(seqId){
	var str = "";
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct";
	var url = requestURLStr + "/getPersonName.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
		str = rtJson.rtData.userName;
	}else {
		alert(rtJson.rtMsrg); 
	}
	return str;
}
/**
 * 发起人
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function transactorStepFunc(cellData,recordIndex,columIndex){
	var str = "";
	if(cellData){
		str = getPersonName(cellData);
	}
	return "<center>" + str + "</center>";
}
/**
 * 应聘者姓名
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function employeeNameFunc(cellData,recordIndex,columIndex){
	var str = "";
	var strInfo = "";
	var seqId = this.getCellData(recordIndex, "seqId");
	var titleStatus = passORNot(seqId);
	if(cellData){
		str = cellData + "<span nowrap style='color:red' style='display:inline;'>(" + titleStatus + ")</span>";
	}
	return "<center>" + str + "</center>";
}

function passORNot(seqId){
	var str = "";
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct";
	var url = requestURLStr + "/passORNot.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
		str = rtJson.rtData.titleStatus;
	}else {
		alert(rtJson.rtMsrg); 
		return;
	}
	return str;
}




//选择框
function checkBoxRender(cellData, recordIndex, columIndex) {
	var diaId = this.getCellData(recordIndex, "seqId");
	return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" ></center>";
}
/**
 * 全选
 * 
 * @param field
 * @return
 */
function checkAll(field) {
	var deleteFlags = document.getElementsByName("deleteFlag");
	for ( var i = 0; i < deleteFlags.length; i++) {
		deleteFlags[i].checked = field.checked;
	}
}

function checkSelf() {
	var allCheck = $('checkAlls');
	if (allCheck.checked) {
		allCheck.checked = false;
	}
}

// 取得选中选项
function checkMags(cntrlId) {
	var ids = ""
	var checkArray = $$('input');
	for ( var i = 0; i < checkArray.length; i++) {
		if (checkArray[i].name == cntrlId && checkArray[i].checked) {
			if (ids != "") {
				ids += ",";
			}
			ids += checkArray[i].value;
		}
	}
	return ids;
}




