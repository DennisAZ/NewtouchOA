
/**
 * 获取办公用品库根据部门DEPT_ID(返回库字段OFFICE_TYPE_ID)
 * @param selectId
 * @return
 */
function getOfficeDepositoryNames(selectId,extData){
	//alert("extData>>"+extData);
	var urlStr = contextPath + "/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
	var url = urlStr + "/getOfficeDepositoryNames.act";
	var rtJson = getJsonRs(url);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	var selects = document.getElementById(selectId);
	for ( var i = 0; i < prcs.length; i++) {
		var prc = prcs[i];
		var option = document.createElement("option");
		option.value = prc.typeId;
		option.innerHTML = prc.name;
		selects.appendChild(option);
		//alert("prc.typeId>>"+ prc.typeId + "  prc.name>>"+prc.name + "  extData>>" +extData );
		if (extData && (extData == prc.typeId)) {
			option.selected = true;
		}
	}
}
/**
 * 获取办公用品库(返回库字段OFFICE_TYPE_ID)产品编辑页面
 * @param selectId
 * @param extData
 * @return
 */
function getproEditDepositoryNames(selectId,extData){
	//alert("extData>>"+extData);
	var urlStr = contextPath + "/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
	var url = urlStr + "/getproEditDepositoryNames.act";
	var rtJson = getJsonRs(url,"extData="+extData);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	var selects = document.getElementById(selectId);
	for ( var i = 0; i < prcs.length; i++) {
		var prc = prcs[i];
		var option = document.createElement("option");
		option.value = prc.typeId;
		option.innerHTML = prc.name;
		selects.appendChild(option);
		var selectFlag = prc.selectFlag
		if(selectFlag == "1"){
			option.selected = true;
		}
		
		//alert("prc.typeId>>"+ prc.typeId + "  prc.name>>"+prc.name + "  extData>>" +extData );
	}
}
/**
 * 获取办公用品库(返回库seq_id)
 * @param selectId
 * @return
 */
function getOfficeDepositoryName(selectId,extData){
	var urlStr = contextPath + "/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
	var url = urlStr + "/getOfficeDepositoryName.act";
	var rtJson = getJsonRs(url);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	var selects = document.getElementById(selectId);
	for ( var i = 0; i < prcs.length; i++) {
		var prc = prcs[i];
		var option = document.createElement("option");
		option.value = prc.typeId;
		option.innerHTML = prc.name;
		selects.appendChild(option);
		//alert("typeId>>"+ prc.typeId + "  extData>>"+extData);
		if (extData && (extData == prc.typeId)) {
			option.selected = true;
		}
	}
}
/**
 * 获取办公用品类别
 * @param typeId
 * @return
 */
function depositoryOfType(typeId,extData){
	//alert(extData);
	var selects = document.getElementById("officeProtype");
	selects.length =1;
	
	if(typeId){
		var urlStr = contextPath + "/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
		var url = urlStr + "/getOfficeTypeNamesById.act";
		var rtJson = getJsonRs(url,"typeId=" + typeId);
		if (rtJson.rtState == "1") {
			alert(rtJson.rtMsrg);
			return;
		}
		var prcs = rtJson.rtData;
		for ( var i = 0; i < prcs.length; i++) {
			var prc = prcs[i];
			var option = document.createElement("option");
			option.value = prc.typeId;
			option.innerHTML = prc.name;
			selects.appendChild(option);
			if (extData && (extData == prc.typeId)) {
				option.selected = true;
			}
		}
	}else{
		//selects.length =1;
	}
	
}

/**
 * 获取办公用品类别(根据库的id)
 * @param typeId
 * @return
 */
function getTypeNamesByStoreId(storeId,extData){
	//alert(storeId);
	var selects = document.getElementById("officeProtype");
	selects.length =1;
	if(storeId){
		var urlStr = contextPath + "/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
		var url = urlStr + "/getTypeNamesByStoreId.act";
		var rtJson = getJsonRs(url,"storeId=" + storeId);
		//alert(rsText);
		if (rtJson.rtState == "1") {
			alert(rtJson.rtMsrg);
			return;
		}
		var prcs = rtJson.rtData;
		for ( var i = 0; i < prcs.length; i++) {
			var prc = prcs[i];
			var option = document.createElement("option");
			option.value = prc.typeId;
			option.innerHTML = prc.name;
			selects.appendChild(option);
			if (extData && (extData == prc.typeId)) {
				option.selected = true;
			}
		}
	}else{
		//selects.length =1;
	}
}









