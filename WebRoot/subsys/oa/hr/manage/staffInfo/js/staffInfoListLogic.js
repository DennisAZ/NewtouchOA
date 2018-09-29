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
 * 出生年月
 * 
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffBirthFunc(cellData, recordIndex, columIndex) {
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
function opts(cellData, recordIndex, columIndex) {
	var seqId = this.getCellData(recordIndex, "seqId");
	return "<center>" + "<a href=javascript:doEdit('" + seqId + "')>编辑</a>&nbsp;"
			+ "</center>";
}
function doEdit(seqId) {
	location.href = contextPath
			+ "/subsys/oa/hr/manage/staffInfo/staffInfo.jsp?userId="
			+ encodeURIComponent(seqId);
}
function checkBoxRender(cellData, recordIndex, columIndex) {
	var diaId = this.getCellData(recordIndex, "seqId");
	return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\""
			+ diaId + "\" onclick=\"checkSelf()\" ></center>";
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

function deleteInfo() {
	var idStrs = checkMags('deleteFlag');
	if (!idStrs) {
		alert("要删除人事档案，请至少选择其中一个。");
		return;
	}
	if (!window.confirm("确认要删除所选中的人事档案 ？")) {
		return;
	}
	var requestURLStr = contextPath+ "/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct";
	var url = requestURLStr + "/delHrStaffInfo.act";
	var rtJson = getJsonRs(url, "seqId=" + idStrs);
	if (rtJson.rtState == "0") {
		window.location.reload();
	} else {
		alert(rtJson.rtMsrg);
	}
}
function deleteAll(deptId) {
	var msg = "确定要删除该部门所有用户档案吗？\n删除后将不可恢复，确认删除请输入大写字母“OK”";
	if (window.prompt(msg,"") == "OK") {
		var requestURLStr = contextPath+ "/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct";
		var url = requestURLStr + "/delHrStaffInfo.act";
		var rtJson = getJsonRs(url, "deptId=" + deptId);
		if (rtJson.rtState == "0") {
			window.location.reload();
		} else {
			alert(rtJson.rtMsrg);
		}
	}
	
}

/**
 * 详细操作
 * 
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function detailOpts(cellData, recordIndex, columIndex) {
	var seqId = this.getCellData(recordIndex, "seqId");
	return "<center>" + "<a href=javascript:detail1('" + seqId + "')>详细信息 </a>&nbsp;"
			+ "</center>";
}

function detail(seqId){
	var url = contextPath + "/subsys/oa/hr/manage/staffInfo/detail.jsp?seqId=" + seqId;
	newWindow(url,'820','500');
}
function detail1(seqId){
	var url = contextPath + "/subsys/oa/hr/manage/staffInfo/detail.jsp?seqId=" + seqId;
	newWindow1(url,'820','500',"retireName");
	
}

/**
 * 打开新窗口  newWindow(URL,'740', '540');
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow1(url,width,height,nameStr){
	var locX=(screen.width-width)/2;
	var locY=(screen.height-height)/2;
	window.open(url, nameStr, 
			"height=" +height + ",width=" + width +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
			+ locY + ", left=" + locX + ", resizable=yes");
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
/**
 * 部门名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function deptIdFunc(cellData, recordIndex, columIndex) {
	var str = "";
	if(cellData){
		var requestURLStr = contextPath+ "/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct";
		var url = requestURLStr + "/getDeptIdName.act";
		var rtJson = getJsonRs(url, "deptId=" + cellData);
		if (rtJson.rtState == "0") {
			var prcs = rtJson.rtData;
			if(prcs.deptName){
				str = prcs.deptName;
			}else{
				str = "未记录";
			}
		} else {
			alert(rtJson.rtMsrg);
		}
	}
	return "<center>" + str + "</center>";
}
function staffSexFunc(cellData, recordIndex, columIndex) {
	var str = "";
	if(cellData == "0"){
		str = "男";
	}else{
		str = "女";
	}
	return "<center>" + str + "</center>";
}

/**
 * 离职原因
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function dimissionCause(cellData, recordIndex, columIndex){
	var str = "";
	if(cellData){
		var requestURLStr = contextPath+ "/yh/subsys/oa/hr/manage/staffInfo/act/YHHrDimissionAct";
		var url = requestURLStr + "/getDimissionCause.act";
		var rtJson = getJsonRs(url, "userId=" + cellData);
		if (rtJson.rtState == "0") {
			var prcs = rtJson.rtData;
			str = prcs.causeStr;
		} else {
			alert(rtJson.rtMsrg);
		}
	}
	return "<center>" + str + "</center>";
	
}
/**
 * 年龄
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffAgeFunc(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex, "seqId");
	var str = "";
	if(cellData){
		var requestURLStr = contextPath+ "/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct";
		var url = requestURLStr + "/getStaffAge.act";
		var rtJson = getJsonRs(url, "seqId=" + seqId);
		if (rtJson.rtState == "0") {
			var prcs = rtJson.rtData;
			str = prcs.staffAge;
		} else {
			alert(rtJson.rtMsrg);
		}
	}
	return "<center>" + str + "</center>";
}


/**
 * 查询结果操作
 * 
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function queryResultOpts(cellData, recordIndex, columIndex) {
	var seqId = this.getCellData(recordIndex, "seqId");
	return "<center>" + "<a href=javascript:detail('" + seqId + "')>详细信息 </a>&nbsp;"
			+ "<a href=javascript:doEdit('" + seqId + "')>编辑</a>&nbsp;"
			+ "<a href=javascript:delUserInfo('" + seqId + "')>删除</a>&nbsp;"
			+ "</center>";
}

function delUserInfo(seqId) {
	if (!window.confirm("确定要删除该人事档案吗？删除后将不可恢复")) {
		return;
	}
	var requestURLStr = contextPath+ "/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct";
	var url = requestURLStr + "/delHrStaffInfo.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId);
	if (rtJson.rtState == "0") {
		window.location.reload();
	} else {
		alert(rtJson.rtMsrg);
	}
}

/**
 * 查询结果操作
 * 
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function retireeResultOpts(cellData, recordIndex, columIndex) {
	//var seqId = this.getCellData(recordIndex, "seqId");
	var personId = this.getCellData(recordIndex, "userId");
	return "<center>" + "<a href=javascript:showStaffInfoDetail('" + personId + "')>详情 </a>&nbsp;"
			+ "</center>";
}
function showStaffInfoDetail(personId){
	location.href = contextPath + "/subsys/oa/hr/manage/query/showStaffInfo.jsp?personId=" + personId;
	
}

/**
 * 籍贯
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffNativePlaceFunc(cellData, recordIndex, columIndex) {
	var personId = this.getCellData(recordIndex, "userId");
	var str = "";
	if(cellData){
		str = getSelectedCode("AREA", cellData);
	}
	return "<center>" + str + "</center>";
}
/**
 * 政治面貌
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffPoliticalStatusFunc(cellData, recordIndex, columIndex) {
	var personId = this.getCellData(recordIndex, "userId");
	var str = "";
	if(cellData){
		str = getSelectedCode("STAFF_POLITICAL_STATUS", cellData);
	}
	return "<center>" + str + "</center>";
}
/**
 * 获取下拉框选项 getSecretFlag("T_COURSE_TYPE","tCourseTypes");
 * @param parentNo
 *          代码编号
 * @param optionType
 *          绑定的div
 * @param extValue
 *          要选中的值
 * @return
 */
function getSelectedCode(parentNo, extValue) {
	var str = "";
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectChildCode.act?parentNo=" + parentNo;
	var rtJson = getJsonRs(requestURLStr);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	for ( var i = 0; i < prcs.length; i++) {
		var prc = prcs[i];
		var option = document.createElement("option");
		if (extValue && (extValue == prc.seqId)) {
			str = prc.codeName;
			break;
		}
	}
	return str;
}



