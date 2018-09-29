/**
 * 获取下拉框选项 getSecretFlag("T_COURSE_TYPE","tCourseTypes");
 * 
 * @param parentNo
 *          代码编号
 * @param optDiv
 *          绑定的div
 * @param extValue
 *          要选中的值
 * @return
 */
function getSecretFlag(parentNo, optDiv, extValue) {
	var requestURLStr = contextPath
			+ "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectChildCode.act?parentNo="
			+ parentNo;
	var rtJson = getJsonRs(requestURLStr);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	var selects = document.getElementById(optDiv);
	for ( var i = 0; i < prcs.length; i++) {
		var prc = prcs[i];
		var option = document.createElement("option");
		option.value = prc.seqId;
		option.innerHTML = prc.codeName;
		selects.appendChild(option);
		if (extValue && (extValue == prc.value)) {
			option.selected = true;
		}
	}
}

/**
 * 截取时间
 * 
 * @param cellData
 * @param recordIndex
 * @param columInde
 * @return
 */
function splitDateFunc(cellData, recordIndex, columInde) {
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
	var issend = this.getCellData(recordIndex, "issend");
	var issendStr = "";
	var temp = '终止';
	var flag = 1;
	if (issend == 1) {
		temp = '启用';
		flag = 0;
		issendStr = "<a href=javascript:sendEmail(" + seqId + "," + recordIndex + ")>发送EMAIL工资条 </a><br>"
				  + "<a href=javascript:sendMobile(" + seqId + "," + recordIndex + ")>发送手机工资条 </a><br>";
	}
	return "<center>" + "<a href=javascript:importFun(" + seqId + ")>导入工资数据</a><br>" 
					  + "<a href=javascript:exportFun(" + seqId + ")>导出工资报表</a><br>" 
					  + issendStr 
					  + "<a href=javascript:doIssend(" + seqId + "," + flag + ")>" + temp + "</a>&nbsp;" 
					  + "<a href=javascript:doEdit(" + seqId + ")>编辑</a>&nbsp;" 
					  + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>" + "</center>";
}

function importFun(seqId) {
	location.href = contextPath	+ "/subsys/oa/hr/salary/manage/salFlow/import.jsp?seqId=" + seqId;
}
function exportFun(seqId) {
	location.href = contextPath	+ "/subsys/oa/hr/salary/manage/salFlow/reportManager/index.jsp?seqId=" + seqId;
}
function sendEmail(seqId,recordIndex) {
	var content = pageMgr.getCellData(recordIndex, "content");
	location.href = contextPath	+ "/subsys/oa/hr/salary/manage/salFlow/sendEmail.jsp?seqId=" + seqId + "&content=" + encodeURIComponent(content);
}
function sendMobile(seqId,recordIndex) {
	var content = pageMgr.getCellData(recordIndex, "content");
	location.href = contextPath	+ "/subsys/oa/hr/salary/manage/salFlow/sendMobile.jsp?seqId=" + seqId + "&content=" + encodeURIComponent(content);
}

function doIssend(seqId, flag) {
	var requestURLStr = contextPath
			+ "/yh/subsys/oa/hr/salary/salFlow/act/YHHrSalFlowAct";
	var url = requestURLStr + "/doIssend.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId + "&flag=" + flag);
	if (rtJson.rtState == "0") {
		window.location.reload();
	} else {
		alert(rtJson.rtMsrg);
	}
}
function doEdit(seqId) {
	location.href = contextPath
			+ "/subsys/oa/hr/salary/manage/salFlow/modify.jsp?seqId=" + seqId;
}
/**
 * 详细信息
 * 
 * @param seqId
 * @return
 */
function detail(seqId) {
	var URL = contextPath
			+ "/subsys/oa/hr/recruit/requirements/detail.jsp?seqId=" + seqId;
	newWindow(URL, '820', '500');
}
/**
 * 打开新窗口 newWindow(URL,'740', '540');
 * 
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow(url, width, height) {
	var locX = (screen.width - width) / 2;
	var locY = (screen.height - height) / 2;
	window.open(url, "meeting", "height=" + height + ",width=" + width
			+ ",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top="
			+ locY + ", left=" + locX + ", resizable=yes");
}
function deleteSingle(seqId) {
	if (!window.confirm("确认要删除该流程吗 ？通过该流程上报的工资数据将被删除切不可恢复！")) {
		return;
	}
	var requestURLStr = contextPath
			+ "/yh/subsys/oa/hr/salary/salFlow/act/YHHrSalFlowAct";
	var url = requestURLStr + "/deleteFile.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId);
	if (rtJson.rtState == "0") {
		window.location.reload();
	} else {
		alert(rtJson.rtMsrg);
	}
}

/**
 * 判断小数位后２位
 * 
 * @param aValue
 * @return
 */
function isNumbers(aValue) {
	var digitSrc = "0123456789";
	aValue = "" + aValue;
	if (aValue.substr(0, 1) == "-") {
		aValue = aValue.substr(1, aValue.length - 1);
	}
	var strArray = aValue.split(".");
	// 含有多个“.”
	if (strArray.length > 2) {
		return false;
	}
	var tmpStr = "";
	for ( var i = 0; i < strArray.length; i++) {
		tmpStr += strArray[i];
	}
	for ( var i = 0; i < tmpStr.length; i++) {
		var tmpIndex = digitSrc.indexOf(tmpStr.charAt(i));
		if (tmpIndex < 0) {
			// 有字符不是数字
			return false;
		}
	}
	if (aValue.indexOf(".") != -1) {
		var str = aValue.substr(aValue.indexOf(".") + 1, aValue.length - 1);
		if (str.length > 2) {
			return false;
		}
		if (str.length == 0) {
			return false;
		}
	}
	return true;
}
