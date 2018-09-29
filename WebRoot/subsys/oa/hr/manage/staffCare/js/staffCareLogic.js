/**
 * 获取下拉框选项
 * getSecretFlag("T_COURSE_TYPE","tCourseTypes");
 * @param parentNo	代码编号
 * @param optDiv	绑定的div
 * @param extValue  要选中的值

 * @return
 */
function getSecretFlag(parentNo,optDiv,extValue){
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectChildCode.act?parentNo=" + parentNo;
	var rtJson = getJsonRs(requestURLStr);
	if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ;
	}
	var prcs = rtJson.rtData;
	var selects = document.getElementById(optDiv);
	for(var i=0;i<prcs.length;i++){
	  var prc = prcs[i];
	  var option = document.createElement("option"); 
	  option.value = prc.seqId; 
	  option.innerHTML = prc.codeName; 
	  selects.appendChild(option);
	  if(extValue && (extValue == prc.value)){
		  option.selected = true;
	  }
	}
}
/**
 * 与关怀类型
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function careItemFunc(cellData, recordIndex, columIndex){
  var classCode = this.getCellData(recordIndex,"careType");
  var classNo = "HR_STAFF_TRANSFER1";
  var urls = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectCodeById.act?seqId="+classCode;
  var rtJsons = getJsonRs(urls);
  var prc = rtJsons.rtData;
  if(rtJsons.rtState == '0'){
    if(prc.codeName){
      return "<center>" + prc.codeName + "<center>";
    }
    return  "<center><center>";
  }else{
    alert(rtJson.rtMsrg);
  }
}

/**
 * 截取时间
 * @param cellData
 * @param recordIndex
 * @param columInde
 * @return
 */
function splitDateFunc(cellData, recordIndex,columInde) {
	var str = "";
	if(cellData){
		str = cellData.substr(0,10);
	}
  return "<center>" + str + "</center>";
}

/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function opts(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
	return "<center>"
				+ "<a href=javascript:detail(" + seqId + ")>详细信息</a>&nbsp;"
				+ "<a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;"
				+ "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>"
				+ "</center>";
}
function doEdit(seqId){
	location.href = contextPath + "/subsys/oa/hr/manage/staffCare/modify.jsp?seqId=" + seqId;
}
/**
 * 详细信息
 * @param seqId
 * @return
 */
function detail(seqId){
  var URL = contextPath + "/subsys/oa/hr/manage/staffCare/detail.jsp?seqId=" + seqId;
  newWindow(URL,'820', '500');
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
function deleteSingle(seqId){
	if(!window.confirm("确认要删除该员工关怀信息 ？")){
		return ;
	}
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/manage/care/act/YHHrStaffCareAct";
	var url = requestURLStr + "/deleteFile.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
	 alert(rtJson.rtMsrg); 
	}
}
/**
 * 删除多个文件
 * @return
 */
function deleteAll(){
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要删除员工关怀信息，请至少选择其中一个。");
    return;
  }
  if(!window.confirm("确认要删除已选中的员工关怀信息 ？")) {
    return ;
  } 
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/manage/care/act/YHHrStaffCareAct";
	var url = requestURLStr + "/deleteFile.act";
	var rtJson = getJsonRs(url, "seqId=" + idStrs );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
	 alert(rtJson.rtMsrg); 
	}
}
function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
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
/**
 * 判断小数位后２位
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
  for (var i = 0; i < strArray.length; i++) { 
    tmpStr += strArray[i]; 
  } 
  for (var i = 0; i < tmpStr.length; i++) { 
    var tmpIndex = digitSrc.indexOf(tmpStr.charAt(i)); 
    if (tmpIndex < 0) { 
  // 有字符不是数字 
      return false; 
    } 
  } 
  if(aValue.indexOf(".") != -1){
    var str = aValue.substr(aValue.indexOf(".")+1, aValue.length-1);
    if(str.length > 2){
      return false;
    }
    if(str.length == 0){
      return false;
    }
  }
  return true;
}

/**
 * 单位员工名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffNameFunc(cellData, recordIndex, columIndex){
  var url = contextPath + "/yh/subsys/oa/hr/manage/act/YHHrStaffIncentiveAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return "<center>" + rtJson.rtData + "</center>";
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function careFeesRMB(cellData, recordIndex, columIndex){
	var str = "0.00";
	if(cellData){
		str = insertKiloSplit(cellData,2);
	}
	return str+"(元)";
}

function checkBoxRenderCare(cellData, recordIndex, columIndex){
  var staffMobile = this.getCellData(recordIndex,"mobilNo");
  if(staffMobile && staffMobile.trim() != ""){
	  return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + staffMobile + "\" onclick=\"checkSelf()\" ></center>";
  }
  return "<center><input disabled type=\"checkbox\" name=\"deleteFlag\" value=\"" + staffMobile + "\" onclick=\"checkSelf()\" ></center>";
}

function sendInfo(){
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("请至少选择一个客户");
    return;
  }
  location.href = contextPath + "/core/funcs/mobilesms/new/index.jsp?outUser=" + idStrs;	
}

function sexFunc(cellData, recordIndex, columIndex){
  var sex = this.getCellData(recordIndex,"sex");
  if(sex){
    if(sex == 0)
      return "男";
    if(sex == 1)
      return "女";
  }
  return "";
}
