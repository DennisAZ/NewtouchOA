
/**
 * 获取下拉框选项
 * getSecretFlag("T_COURSE_TYPE","tCourseTypes");
 * @param parentNo	代码编号
 * @param optionType	绑定的div
 * @param extValue  要选中的值
 * @return
 */
function getSecretFlag(parentNo,optionType,extValue){
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectChildCode.act?parentNo=" + parentNo;
	var rtJson = getJsonRs(requestURLStr);
	if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ;
	}
	var prcs = rtJson.rtData;
	var selects = document.getElementById(optionType);
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
 * 字段居中显示
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function trainingCenterFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

//选择框
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
 * 删除多个文件
 * @return
 */
function delete_all(){
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要删除培训计划，请至少选择其中一个。");
    return;
  }
  if(!window.confirm("确认要删除已选中的培训计划吗？")) {
    return ;
  } 
	var requestURLStr = contextPath + "/yh/subsys/oa/training/act/YHHrTrainingPlanAct";
	var url = requestURLStr + "/deleteFile.act";
	var rtJson = getJsonRs(url, "seqId=" + idStrs );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
	 alert(rtJson.rtMsrg); 
	}
}

/**
 * 培训渠道
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function tChannelFunc(cellData, recordIndex, columIndex){
	var str = "";
	if(cellData == "0"){
		str = "内部培训";
	}
	if(cellData == "1"){
		str = "渠道培训";
	}
	 return "<center>" + str  + "</center>"; 
}

/**
 * 培训形式
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function tCourseTypesFunc(cellData, recordIndex, columIndex){
  var classCode = this.getCellData(recordIndex,"tCourseTypes");
  var classNo = "T_COURSE_TYPE";
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
 * 计划状态
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function assessingStatusFunc(cellData, recordIndex, columIndex){
	var str = "";
	if(cellData == "0"){
		str = "待审批";
	}
	if(cellData == "1"){
		str = "<font color=green>已批准</font>";
	}
	if(cellData == "2"){
		str = "<font color=red>未批准</font>";
	}
	return "<center>" + str  + "</center>"; 
}


/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function optsList(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var tStatus = this.getCellData(recordIndex,"assessingStatus");
  var showEditStr = "";
  if(tStatus != "1"){
  	showEditStr = "<a href=javascript:modefyTrainingPlan(" + seqId + ")>修改</a>&nbsp;";
  }
  
  var  showInfoStr = "<center><a href=javascript:trainingPlanDetail(" + seqId + ")>详细信息</a>&nbsp;"
  + showEditStr
  + "<a href=javascript:deleteTrainingPaln('" + seqId + "')>删除</a></center>";
  
  return showInfoStr; 
}

/**
 * 删除多个文件
 * @return
 */
function deleteTrainingPaln(seqId){
  if(!window.confirm("确认要删除该项培训计划吗？")) {
    return ;
  } 
	var requestURLStr = contextPath + "/yh/subsys/oa/training/act/YHHrTrainingPlanAct";
	var url = requestURLStr + "/deleteFile.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
	 alert(rtJson.rtMsrg); 
	}
}
/**
 * 详细信息
 * @param seqId
 * @return
 */
function trainingPlanDetail(seqId){
  var URL = contextPath + "/subsys/oa/training/plan/plandetail.jsp?seqId=" + seqId;
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

/**
 * 修改培训计划信息
 * @param seqId
 * @return
 */
function modefyTrainingPlan(seqId){
	window.location.href = contextPath + "/subsys/oa/training/plan/modifyTrainingPlan.jsp?seqId=" + seqId;
}

//检查数据库是否已经有该值function checkPlanNo(tPlanNo,seqId){
	if(!tPlanNo){
		return ;
	}
	$("plan_no_msg").innerHTML = "<img src='" +imgPath + "/loading_16.gif' align='absMiddle'> 检查中，请稍候……";
	var requestURLStr = contextPath + "/yh/subsys/oa/training/act/YHHrTrainingPlanAct";
	var url = requestURLStr + "/checkPlanNo.act";
	var rtJson = getJsonRs(url, "tPlanNo=" + encodeURIComponent(tPlanNo) + "&seqId=" + seqId );
	if (rtJson.rtState == "0") {
		var isHave = rtJson.rtData.isHave;
		if(isHave == 0){
			$("plan_no_msg").innerHTML = "<img src='" +imgPath + "/correct.gif' align='absMiddle'> ";
		}else{
			$("plan_no_msg").innerHTML = "<img src='" +imgPath + "/error.gif' align='absMiddle'>该编号已存在";
			$("tPlanNo").focus();
			$("tPlanNo").select();
		}
	}else {
	 //alert(rtJson.rtMsrg); 
	}
}

//判断是否要显示短信提醒 
function getSysRemind(remidDiv,remind){ 
  var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=61"; 
  var rtJson = getJsonRs(requestUrl); 
    if(rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  } 
  var prc = rtJson.rtData; 
  //alert(rsText);
  var allowRemind = prc.allowRemind;;//是否允许显示 
  var defaultRemind = prc.defaultRemind;//是否默认选中 
  var mobileRemind = prc.mobileRemind;//手机默认选中 
  if(allowRemind=='2'){ 
    $(remidDiv).style.display = 'none'; 
  }else{ 
    if(defaultRemind=='1'){ 
      $(remind).checked = true; 
    } 
  } 
  //return prc; 
}

//判断是否要显示手机短信提醒 
function moblieSmsRemind(remidDiv,remind){ 
  var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=61"; 
  var rtJson = getJsonRs(requestUrl); 
    if(rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  } 
  var prc = rtJson.rtData; 
  //alert(rsText);
  var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
  if(moblieRemindFlag == '2'){ 
    $(remidDiv).style.display = '';
    $(remind).checked = true;
  }else if(moblieRemindFlag == '1'){ 
    $(remidDiv).style.display = '';
    $(remind).checked = false;
  }else{
    $(remidDiv).style.display = 'none'; 
  }
}



