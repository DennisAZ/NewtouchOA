
/**
 * 获取下拉框选项
 * @param classNo  如：MEETING_EQUIPMENT	表中的 class_no
 * @param optionType 如：fileKind  为组件的id
 * @param extValue
 * @return
 */
function getSecretFlag(classNo,optionType,extValue){
	var requestURLStr = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingEquipmentAct";
	var url = requestURLStr + "/getSelectOption.act?parentNo=" + classNo;
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ;
	}
	
	var prcs = rtJson.rtData;
	var selects = document.getElementById(optionType);
	for(var i=0;i<prcs.length;i++){
	  var prc = prcs[i];
	  var option = document.createElement("option"); 
	  option.value = prc.value; 
	  option.innerHTML = prc.text; 
	  selects.appendChild(option);
	  
	  if(extValue && (extValue == prc.value)){
      option.selected = true;
    }
	}
}


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
 * 待批”操作“
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function optsList(cellData, recordIndex, columIndex){
	 var seqId = this.getCellData(recordIndex,"seqId");
	 var mStatus = this.getCellData(recordIndex,"mStatus");
	 var runId = this.getCellData(recordIndex,"runId");
	 var flowId = this.getCellData(recordIndex,"flowId");
	 var showInfoStr = "";
	 if(mStatus == "1"){
	   if (runId == 0) {
	     showInfoStr = "<center><a href=javascript:meetingDetailFunc(" + seqId + ")>详细信息</a></center>";
	   } else {
	     showInfoStr = "<center><a href='javascript:formView(" + runId + " , "+flowId+")'>查看流程</a></center>" ;
	   }
	 }else if(mStatus == "2"){
	   if (runId == 0) {
	     showInfoStr = "<center><a href=javascript:meetingDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
       + "<a href=javascript:checkupFunc(" + seqId + ",4)>结束</a>&nbsp;";
	     showInfoStr += "<a href=javascript:modefyMeeting(" + seqId + ")>修改</a>&nbsp;"
     } else {
       showInfoStr = "<center><a href='javascript:formView(" + runId + " , "+flowId+")'>查看流程</a></center>" 
           + "<a href=javascript:checkupFunc(" + seqId + ",4)>结束</a>&nbsp;";
     }
	   showInfoStr += "<a href=javascript:meetingSummaryFunc(" + seqId + ")>会议纪要</a></center>";
	 }else{
	   if (runId == 0) {
	     showInfoStr = "<center><a href=javascript:meetingDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
	     + "<a href=javascript:modefyMeeting(" + seqId + ")>修改</a>&nbsp;"
	     + "<a href=javascript:deleteMeeting('" + seqId + "','" + mStatus + "')>删除</a></center>";
     } else {
       showInfoStr = "<center><a href='javascript:formView(" + runId + " , "+flowId+")'>查看流程</a></center>" 
     }
	 }
	 return showInfoStr;
}

/**
 * 会议纪要
 * @param seqId
 * @return
 */
function meetingSummaryFunc(seqId){
 　var URL = contextPath + "/subsys/oa/meeting/summary/summary.jsp?seqId=" + seqId;
  newWindow(URL,'740', '540');
}

/**
 * 打开新窗口  
 * newWindow(URL,'740', '540');
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow(url,width,height){
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  window.open(url, "meeting", 
      "height=" + height + " ,width=" + width + " ,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
      + locY + ", left=" + locX + ", resizable=yes");
}

function checkupFunc(seqId, mStatus){
  var msg = "";
  if(mStatus == "4"){
    msg = '确认要结束该会议么？';
  }else if(mStatus == "1"){
    msg = '确认要批准该会议吗？';
  }else if(mStatus == "3"){
    msg = '确认不批准该会议吗？';
  }else if(mStatus == "0"){
    msg = '确认撤销该会议吗？';
  }
  if (window.confirm(msg)) {
    var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/updateStatus.act?seqId=" + seqId + "&mStatus=" + mStatus;
    var rtJson = getJsonRs(url);
    if (rtJson.rtState == "0") {
      window.location.reload();
    } else {
      alert(rtJson.rtMsrg); 
    }
  }
}


/**
 * 处理日期
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function mDateFunc(cellData, recordIndex, columIndex){
  var mStartData = "";
  var mStart = cellData;
  if(mStart){
    mStartData = mStart.substr(0, mStart.length - 2);
  }
  return "<center>" + mStartData + "</center>";
}

/**
 * 申请人 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function mProposerFunc(cellData, recordIndex, columIndex){
  var mProposer = this.getCellData(recordIndex,"mProposer");
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/getUserName.act";
  var rtJson = getJsonRs(url, "userId=" + mProposer);
  if (rtJson.rtState == "0") {
    return "<center>" + rtJson.rtData + "</center>";
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 出席人员
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function mAttendeeFunc(cellData, recordIndex, columIndex){
	var meetingId = this.getCellData(recordIndex,"seqId");
	var url =contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/getAttendeeName.act";
	var rtJson = getJsonRs(url, "seqId=" + meetingId);
  if (rtJson.rtState == "0") {
    return "<left>" + rtJson.rtData + "</left>";
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 会议室名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function mRoomFunc(cellData, recordIndex, columIndex){
  var mRoom = this.getCellData(recordIndex,"mRoom");
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/getMeetingRoomName.act";
  var rtJson = getJsonRs(url, "seqId=" + mRoom);
  if (rtJson.rtState == "0") {
    return "<center>" + rtJson.rtData + "</center>";
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 会议详细信息
 * @param seqId
 * @return
 */
function meetingDetailFunc(seqId){
  var URL = contextPath + "/subsys/oa/meeting/query/meetingdetail.jsp?seqId=" + seqId;
  //openDialogResize(URL,'820', '500');
  newWindow(URL,'820', '500');
}

/**
 * 修改会议信息
 * @param seqId
 * @return
 */
function modefyMeeting(seqId){
	window.location.href = contextPath + "/subsys/oa/meeting/apply/modifyMeeting.jsp?seqId=" + seqId;
}

/**
 * 删除会议信息
 * @param seqId
 * @param mStatus
 * @return
 */
function deleteMeeting(seqId,mStatus){
	if(window.confirm("确认要删除该会议么？")){
		var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/delMeetingInfo.act";
		var rtJson = getJsonRs(url, "seqId=" + seqId );
		if (rtJson.rtState == "0") {
		  location.reload();
		} else {
		  alert(rtJson.rtMsrg); 
		}
	}
}


