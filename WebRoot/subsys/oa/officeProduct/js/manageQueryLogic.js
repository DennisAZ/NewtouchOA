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
function transDateFunc(cellData, recordIndex, columIndex) {
	var str = "";
	if (cellData) {
		str = cellData.substr(0, 10);
	}
	return "<center>" + str + "</center>";
}
/**
 * 数量
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function transQtyFunc(cellData, recordIndex, columIndex) {
	var str = "";
	if (cellData) {
		str = Math.abs(cellData);
	}
	var transFlag = this.getCellData(recordIndex, "transFlag");
	if(transFlag == "5"){
		str = "-";
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
	var transFlag = this.getCellData(recordIndex, "transFlag");
	var optStr = "";
	/*if(transFlag == "3"){
		optStr = "<a href=javascript:transDetailReturn('" + seqId + "')>处理</a>&nbsp;";
	}else{
		optStr = "<a href=javascript:transDetail('" + seqId + "')>处理</a>&nbsp;";
	}
	*/
	optStr = "<a href=javascript:deleteDetailReturn('" + seqId + "')>删除</a>&nbsp;&nbsp;";
					//+ "<a href=javascript:editDetailReturn('" + seqId + "')>编辑</a>&nbsp;";
	return "<center>" +optStr + "</center>";
}
function editDetailReturn(transId){
	window.location.href = contextPath + "/subsys/oa/officeProduct/manage/query/editRecord.jsp?transId=" + transId;
}
function deleteDetailReturn(seqId){
	if(window.confirm("确认要删除办公用品登记信息？")) {
		var url = contextPath + "/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct/deleteOfficeTranshistory.act?seqId=" +seqId;
		var rtJson = getJsonRs(url);
		if (rtJson.rtState == "0") {
			window.location.reload();
		} else {
			alert(rtJson.rtMsrg); 
		}
	} 
}
var artt = "";
function arttFunc(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex, "seqId");
	if(seqId){
	   artt += seqId+",";
	}
	alert(artt);
}

/**
 * 申请人
 * 
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function borrowerFunc(cellData, recordIndex, columIndex) {
	var str = "";
	if (cellData) {
		var url = contextPath
				+ "/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct/getUserName.act";
		var rtJson = getJsonRs(url, "userIdStr=" + cellData);
		if (rtJson.rtState == "0") {
			str = rtJson.rtData.userName;
		} else {
			alert(rtJson.rtMsrg);
		}
	}
	return "<center>" + str + "</center>";
}
/**
 * 登记类型
 * 
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function transFlagFunc(cellData, recordIndex, columIndex) {
	var str = "";
	if (cellData == "0") {
		str = "采购入库";
	}
	if (cellData == "1") {
		str = "领用";
	}
	if (cellData == "2") {
		str = "借用";
	}
	if (cellData == "3") {
		str = "归还";
	}
	if (cellData == "4") {
		str = "报废";
	}
	if (cellData == "5") {
		str = "维护";
	}
	return "<center>" + str + "</center>";
}


