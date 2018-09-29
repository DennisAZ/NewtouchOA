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
	var oaName = this.getCellData(recordIndex, "oaName");
	var expertId = this.getCellData(recordIndex, "expertId");
	//alert(oaName + ">>" + expertId);
	var counter = 0;
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/recruit/recruitment/act/YHHrRecruitRecruitmentAct";
	var url = requestURLStr + "/getHrStaffInfoCount.act";
	var rtJson = getJsonRs(url, "oaName=" + encodeURIComponent(oaName));
	if (rtJson.rtState == "0") {
		counter = rtJson.rtData.counter;
	}else {
		alert(rtJson.rtMsrg); 
	}
	var newStaffInfoStr = "";
	if(counter == "0"){
		newStaffInfoStr = "<a href=javascript:newStaffInfo('" + expertId + "')>建人事档案</a>&nbsp;&nbsp;";
	}
	var optStr = "<a href=javascript:hrRecruitDetail('" + seqId+ "')>详细信息</a>&nbsp;&nbsp;"
				+ newStaffInfoStr
				+"<a href=javascript:editRecruitment('" + seqId + "')>编辑</a>&nbsp;&nbsp;"
				+"<a href=javascript:deleteInfo('" + seqId + "')>删除</a>&nbsp;&nbsp;";
	return "<center>" + optStr + "</center>";
}
/**
 * 操作
 * 
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function optsList1(cellData, recordIndex, columIndex) {
	var seqId = this.getCellData(recordIndex, "seqId");
	var optStr = "<a href=javascript:hrRecruitDetail('" + seqId+ "')>详细信息</a>&nbsp;&nbsp;"
				+"<a href=javascript:editRecruitment('" + seqId + "')>编辑</a>&nbsp;&nbsp;"
				+"<a href=javascript:deleteInfo('" + seqId + "')>删除</a>&nbsp;&nbsp;";
	return "<center>" + optStr + "</center>";
}


/**
* 详细信息
* @param seqId
* @return
*/
function hrRecruitDetail(seqId){
	var URL = contextPath + "/subsys/oa/hr/recruit/recruitment/hrRecruitDetail.jsp?seqId=" + seqId;
	newWindow(URL,'820', '500');
}
/**
 * 新建档案
 * @param expertId
 * @return
 */
function newStaffInfo(expertId){
	//alert(expertId);
	var url = contextPath + "/subsys/oa/hr/recruit/recruitment/newStaff.jsp?expertId=" + expertId;
	newWindow(url,'820','500');
}
function deleteInfo(seqId){
	if(!window.confirm("确定要删除该招聘录用信息吗？删除后将不可恢复")) {
		return ;
	} 
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/recruit/recruitment/act/YHHrRecruitRecruitmentAct";
	var url = requestURLStr + "/deleteHrRecruitInfo.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
	 alert(rtJson.rtMsrg); 
	}
}
function editRecruitment(seqId){
	var url = contextPath + "/subsys/oa/hr/recruit/recruitment/editRecruitment.jsp?seqId=" + seqId;
	location.href = url;
}

function delete_all(){
	var idStrs = checkMags('deleteFlag');
	//alert(idStrs);
	if(!idStrs) {
		alert("要删除招聘录用信息，请至少选择其中一条。");
		return;
	}
	if(!window.confirm("确认要删除所选招聘录用信息吗？")) {
		return ;
	} 
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/recruit/recruitment/act/YHHrRecruitRecruitmentAct";
	var url = requestURLStr + "/deleteHrRecruitInfo.act";
	var rtJson = getJsonRs(url, "seqId=" + idStrs );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
		alert(rtJson.rtMsrg); 
	}
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

/**
 * 获取下拉框选项 getSecretFlag("T_COURSE_TYPE","tCourseTypes");
 * 
 * @param parentNo
 *          代码编号
 * @param optionType
 *          绑定的div
 * @param extValue
 *          要选中的值

 * 
 * @return
 */
function getSelectedCode(parentNo, optionType, extValue) {
	var requestURLStr = contextPath
			+ "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectChildCode.act?parentNo="
			+ parentNo;
	var rtJson = getJsonRs(requestURLStr);
	// alert(rsText);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	var selects = document.getElementById(optionType);
	for ( var i = 0; i < prcs.length; i++) {
		var prc = prcs[i];
		var option = document.createElement("option");
		var codeNo = prc.codeNo;
		option.value = prc.seqId;
		option.innerHTML = prc.codeName;
		selects.appendChild(option);
		if (extValue && (extValue == codeNo)) {
			option.selected = true;
		}
	}
}
/**
 * 获取代码名称
 * @param seqId
 * @return
 */
function selectCodeById(seqId) {
	var str = "";
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectCodeById.act?seqId=" + seqId;
	var rtJson = getJsonRs(requestURLStr);
	//alert(rsText);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	if(prcs.codeName){
		str = prcs.codeName;
	}
	return str;
}

/**
 * 打开新窗口  newWindow(URL,'740', '540');
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow(url,width,height,windowName){
	var locX=(screen.width-width)/2;
	var locY=(screen.height-height)/2;
	var nameStr = "default";
	if(windowName){
		nameStr = windowName;
	}
	window.open(url, nameStr, 
			"height=" +height + ",width=" + width +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
			+ locY + ", left=" + locX + ", resizable=yes");
}

/**
 * 获取人员名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function getPersonNameFunc(cellData,recordIndex,columIndex){
	var str = "";
	if(cellData){
		str = getPersonName(cellData);
	}
	return "<center>" + str + "</center>";
}
function getPersonName(seqId){
	var str = "";
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/recruit/recruitment/act/YHHrRecruitRecruitmentAct";
	var url = requestURLStr + "/getPersonName.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
		str = rtJson.rtData.userName;
	}else {
		alert(rtJson.rtMsrg); 
	}
	return str;
}







