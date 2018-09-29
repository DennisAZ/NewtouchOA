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
 * 申请日期
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
	if(isNaN(cellData)){
		str = "-";
	}
	else if (cellData) {
		str = Math.abs(cellData);
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
	if (cellData == "1") {
		str = "领用";
	}
	if (cellData == "2") {
		str = "借用";
	}
	if (cellData == "3") {
		str = "归还";
	}
	return "<center>" + str + "</center>";
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
	var proName = this.getCellData(recordIndex, "proName");
	var cycleNo = this.getCellData(recordIndex, "cycleNo");
	var flowId = this.getCellData(recordIndex, "flowId");
	var runId  = this.getCellData(recordIndex, "runId");
	var optStr = "";
	if (runId == 0) {
	  if(transFlag == "3"){
		if(proName == "批量申请"){
		  optStr = "<a href=javascript:transManyReturn('" + cycleNo + "')>批量处理</a>&nbsp;";
		}
		else{
	      optStr = "<a href=javascript:transDetailReturn('" + seqId + "')>处理</a>&nbsp;";
		}  
	  }else{
		if(proName == "批量申请"){
		  optStr = "<a href=javascript:transMany('" + cycleNo + "')>批量处理</a>&nbsp;";
		}
		else{
	      optStr = "<a href=javascript:transDetail('" + seqId + "')>处理</a>&nbsp;";
		}
	  }
	} else {
	  optStr = "<a href='javascript:formView("+runId+" , "+flowId+")'>查看流程<a>&nbsp;";
	}
	return "<center>" +optStr + "</center>";
}

function transDetailReturn(tranId){
	location.href = contextPath + "/subsys/oa/officeProduct/manage/transDetailReturn.jsp?tranSeqId=" + tranId;
}
function transDetail(tranId){
	location.href = contextPath + "/subsys/oa/officeProduct/manage/transDetail.jsp?tranSeqId=" + tranId;
}
function transManyReturn(cycleNo){
	location.href = contextPath + "/subsys/oa/officeProduct/manage/transDetailCycleReturn.jsp?cycleNo=" + cycleNo;
}
function transMany(cycleNo){
	location.href = contextPath + "/subsys/oa/officeProduct/manage/transDetailCycle.jsp?cycleNo=" + cycleNo;
}


/**
 * 获取办公用品库(返回库字段OFFICE_TYPE_ID)产品编辑页面
 * @param selectId
 * @param extData
 * @return
 */
function getproEditDepositoryNames(selectId,extData){
	//alert("extData>>"+extData);
	var urlStr = contextPath + "/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct";
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
		if(prc.typeId){
			option.value = prc.typeId;
		}else{
			option.value = "0";
		}
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
 * 获取办公用品类别(点击用品库 库存登记)
 * @param typeId
 * @return
 */
function depositoryOfType(typeId,extData){
	//alert(typeId);
	var selects = document.getElementById("officeProtype");
	if(typeId){
		selects.length =1;
		var urlStr = contextPath + "/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
		var url = urlStr + "/getOfficeTypeNamesById.act";
		var rtJson = getJsonRs(url,"typeId=" + typeId);
		//alert(rsText)
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
 * 获取办公用品(点击用品类别 库存登记)
 * @param typeId
 * @return
 */
function depositoryOfProducts(typeId){
	//alert(typeId);
	var selects = document.getElementById("officePro");
	if(selects.disabled){
		//alert(selects.disabled);
		selects.disabled = false;
	}
	if(typeId){
		if(selects.length>1){
			selects.length =1;
		}
		var urlStr = contextPath + "/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct";
		var url = urlStr + "/getProductsNamesById.act";
		var rtJson = getJsonRs(url,"idStr=" + typeId);
		//alert(rsText)
		if (rtJson.rtState == "1") {
			alert(rtJson.rtMsrg);
			return;
		}
		var prcs = rtJson.rtData;
		for ( var i = 0; i < prcs.length; i++) {
			var prc = prcs[i];
			var option = document.createElement("option");
			option.value = prc.proId;
			option.innerHTML = prc.proName + "/库存" + prc.proStock;
			selects.appendChild(option);
		}
	}else{
		//selects.length =1;
	}
}
/**
 * 获取办公用品(点击办公用品 库存登记)不需id
 * @param typeId
 * @return
 */
function getProductsNames(){
	var selects = document.getElementById("officePro");
	selects.length =1;
	var urlStr = contextPath + "/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct";
	var url = urlStr + "/getProductsNames.act";
	var rtJson = getJsonRs(url );
	//alert(rsText)
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	for ( var i = 0; i < prcs.length; i++) {
		var prc = prcs[i];
		var option = document.createElement("option");
		option.value = prc.proId;
		option.innerHTML = prc.proName + "/库存" + prc.proStock + " " + prc.proUnit;
		selects.appendChild(option);
	}
}


function transFlag0(){
	if(document.all("BORROWER1").style.display=="")
	   document.all("BORROWER1").style.display="none";
	if(document.all("REMEND").style.display=="")
	   document.all("REMEND").style.display="none";
	if(document.all("REPAIR").style.display=="")
	   document.all("REPAIR").style.display="none";
	if(document.all("RE_REMEND").style.display=="")
	   document.all("RE_REMEND").style.display="none";
	if(document.all("REM_TIME1").style.display=="")
	   document.all("REM_TIME1").style.display="none";
	if(document.all("PCONTENT2").style.display=="")
	   document.all("PCONTENT2").style.display="none";
	   document.all("PCONTENT1").style.display="";
	document.all("COST").style.display="";
	document.all("COMP").style.display="";
	document.all("BANDER").style.display="";
	document.all("PCOUNT").style.display="";
}
function transFlag1(){
	if(document.all("COST").style.display=="")
	   document.all("COST").style.display="none";  
	if(document.all("COMP").style.display=="")
	   document.all("COMP").style.display="none"; 
	if(document.all("BANDER").style.display=="")
	   document.all("BANDER").style.display="none"; 
 if(document.all("REPAIR").style.display=="")
    document.all("REPAIR").style.display="none";
 if(document.all("RE_REMEND").style.display=="")
    document.all("RE_REMEND").style.display="none";
 if(document.all("REM_TIME1").style.display=="")
    document.all("REM_TIME1").style.display="none";
if(document.all("PCONTENT2").style.display=="")
  document.all("PCONTENT2").style.display="none";
  document.all("PCONTENT1").style.display="";
 document.all("BORROWER1").style.display="";
 document.all("REMEND").style.display="";
 document.all.t1.style.display='';
 document.all.t2.style.display='none';
 document.all.t3.style.display='none';
 document.all("PCOUNT").style.display="";
}
function transFlag2(){
	if(document.all("COST").style.display=="")
	  document.all("COST").style.display="none";  
	if(document.all("COMP").style.display=="")
	   document.all("COMP").style.display="none"; 
	if(document.all("BANDER").style.display=="")
	   document.all("BANDER").style.display="none";   
  if(document.all("REPAIR").style.display=="")
     document.all("REPAIR").style.display="none";
  if(document.all("RE_REMEND").style.display=="")
     document.all("RE_REMEND").style.display="none";
  if(document.all("REM_TIME1").style.display=="")
     document.all("REM_TIME1").style.display="none";
if(document.all("PCONTENT2").style.display=="")
   document.all("PCONTENT2").style.display="none";
   document.all("PCONTENT1").style.display="";
  document.all("BORROWER1").style.display="";
  document.all("REMEND").style.display="";
  document.all.t2.style.display='';
  document.all.t1.style.display='none';
  document.all.t3.style.display='none';
  document.all("PCOUNT").style.display="";
}
function transFlag3(){
	if(document.all("COST").style.display=="")
	  document.all("COST").style.display="none";   
	if(document.all("COMP").style.display=="")
	   document.all("COMP").style.display="none"; 
	if(document.all("BANDER").style.display=="")
	   document.all("BANDER").style.display="none"; 
  if(document.all("REMEND").style.display=="")
    document.all("REMEND").style.display="none"; 
  if(document.all("REPAIR").style.display=="")
     document.all("REPAIR").style.display="none";
  if(document.all("RE_REMEND").style.display=="")
     document.all("RE_REMEND").style.display="none";
  if(document.all("REM_TIME1").style.display=="")
     document.all("REM_TIME1").style.display="none";
if(document.all("PCONTENT2").style.display=="")
   document.all("PCONTENT2").style.display="none";
   document.all("PCONTENT1").style.display="";
  document.all("BORROWER1").style.display="";
  document.all.t3.style.display='';
  document.all.t1.style.display='none';
  document.all.t2.style.display='none';
  document.all("PCOUNT").style.display="";
}
function transFlag4(){
	if(document.all("BORROWER1").style.display=="")
	   document.all("BORROWER1").style.display="none";
	if(document.all("COMP").style.display=="")
	   document.all("COMP").style.display="none"; 
	if(document.all("BANDER").style.display=="")
	   document.all("BANDER").style.display="none"; 
	if(document.all("COST").style.display=="")
	   document.all("COST").style.display="none";     
 if(document.all("REMEND").style.display=="")
    document.all("REMEND").style.display="none";  
 if(document.all("REPAIR").style.display=="")
    document.all("REPAIR").style.display="none"; 
 if(document.all("RE_REMEND").style.display=="")
    document.all("RE_REMEND").style.display="none";
 if(document.all("REM_TIME1").style.display=="")
    document.all("REM_TIME1").style.display="none";
if(document.all("PCONTENT2").style.display=="")
  document.all("PCONTENT2").style.display="none";
  document.all("PCONTENT1").style.display="";
	document.all.t3.style.display='none';
 document.all.t1.style.display='none';
 document.all.t2.style.display='none';
 document.all("PCOUNT").style.display="";
}
function transFlag5(){
	if(document.all("BORROWER1").style.display=="")
	   document.all("BORROWER1").style.display="none";
	if(document.all("COMP").style.display=="")
	   document.all("COMP").style.display="none"; 
	if(document.all("BANDER").style.display=="")
	   document.all("BANDER").style.display="none"; 
	if(document.all("COST").style.display=="")
	   document.all("COST").style.display="none";     
 if(document.all("REMEND").style.display=="")
    document.all("REMEND").style.display="none";             
 if(document.all("PCOUNT").style.display=="")
    document.all("PCOUNT").style.display="none";
 if(document.all("PCONTENT1").style.display=="")
    document.all("PCONTENT1").style.display="none";
    document.all("PCONTENT2").style.display="";
 document.all("REPAIR").style.display="";
 document.all("RE_REMEND").style.display="";
 document.all("REM_TIME1").style.display="";
	document.all.t3.style.display='none';
 document.all.t1.style.display='none';
 document.all.t2.style.display='none';
}
//-1
function transFlag11(){
	if(document.all("BORROWER1").style.display=="")
    document.all("BORROWER1").style.display="none";
 if(document.all("COST").style.display=="")
    document.all("COST").style.display="none";
 if(document.all("COMP").style.display=="")
    document.all("COMP").style.display="none";
 if(document.all("BANDER").style.display=="")
    document.all("BANDER").style.display="none";
 if(document.all("REMEND").style.display=="")
    document.all("REMEND").style.display="none";
 if(document.all("REPAIR").style.display=="")
    document.all("REPAIR").style.display="none";
 if(document.all("RE_REMEND").style.display=="")
    document.all("RE_REMEND").style.display="none";
 if(document.all("PCONTENT2").style.display=="")
    document.all("PCONTENT2").style.display="none";
    document.all("PCONTENT1").style.display="";
}





