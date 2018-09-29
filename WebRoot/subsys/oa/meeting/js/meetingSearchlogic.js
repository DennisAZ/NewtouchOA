

/**
 * 字段居中显示
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function meetingCenterFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

/**
 * 会议纪要操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var recorder = this.getCellData(recordIndex,"recorder");
  var mProposer = this.getCellData(recordIndex,"mProposer");
  
  var manager = this.getCellData(recordIndex,"manager");
  var str = "<center>";
  //if (manager != loginUserId) {
   // str += "<a href=\"javascript:showSummary("+seqId+");\">查看纪要</a>&nbsp;"
    if(loginUserId == recorder || loginUserId == mProposer){
      str += "<a href=\"javascript:meetingSummaryFunc("+seqId+");\">添加</a>&nbsp;";
    }
 // }
  str += "</center>";
  
  return str;
}

/**
 * 添加会议纪要
 * @param TO_ID
 * @param TO_NAME
 * @return
 */
function meetingSummaryFunc(seqId){
 　var URL = contextPath + "/subsys/oa/meeting/summary/summary.jsp?seqId=" + seqId;
 	newWindow(URL,'950', '540');
 
}

/**
 * 查看纪要
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function showSummaryFunc(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
  return "<center><a href=\"javascript:showSummary("+seqId+");\">查看纪要</a>&nbsp;</center>";
}
/**
 * 转到查看纪要页面
 * @param seqId
 * @return
 */
function showSummary(seqId){
	var URL = contextPath + "/subsys/oa/meeting/apply/review.jsp?seqId=" + seqId;
 	newWindow(URL,'740', '540');
}



/**
 * 打开新窗口  
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow(url,width,height){
	var locX=(screen.width-width)/2;
	var locY=(screen.height-height)/2;
	window.open(url, "meeting", 
			"height=" + height + ",width=" + width + ",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top="+ locY + ", left=" + locX + ", resizable=yes");
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
 * 会议名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function meetingRoomNameFunc(cellData, recordIndex, columIndex){
  var mRoom = this.getCellData(recordIndex,"mRoom");
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/getMeetingRoomName.act";
  var rtJson = getJsonRs(url, "seqId=" + mRoom);
  if (rtJson.rtState == "0") {
    return "<center>" + rtJson.rtData + "</center>";
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function mStartFunc(cellData, recordIndex, columIndex){
  var mStartData = "";
  var mStart = this.getCellData(recordIndex,"mStart");
  if(mStart){
    mStartData = mStart.substr(0, mStart.length - 2);
  }
  return "<center>" + mStartData + "</center>";
}

/**
 * 会议出席人员
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function mAttendeeFunc(cellData, recordIndex, columIndex){
  var mAttendee = this.getCellData(recordIndex,"mAttendee");
  var aAttendeeOut = this.getCellData(recordIndex,"aAttendeeOut");

  $("mAttendee").value = mAttendee;
  if($("mAttendee") && $("mAttendee").value.trim()){
    bindDesc([{cntrlId:"mAttendee", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    return "内部："+$("mAttendeeDesc").value+"<br>外部："+aAttendeeOut+"";
  }else{
    return "内部："+mAttendee+"<br>外部："+aAttendeeOut+"";
  }
}

/**
 * 会议状态
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function mStatusFunc(cellData, recordIndex, columIndex){
  var status = "待批";
  var mStatus = this.getCellData(recordIndex,"mStatus");
  if(mStatus == '0'){
    status = "待批";
    return "<center>" + status + "</center>";
  }else if(mStatus == '1'){
   status = "已批准";
   return "<center>" + status + "</center>";
 }else if(mStatus == '2'){
   status = "进行中";
   return "<center>" + status + "</center>";
 }else if(mStatus == '3'){
   status = "未批准";
   return "<center>" + status + "</center>";
 }else if(mStatus == '4'){
   status = "已结束";
   return "<center>" + status + "</center>";
 }else{
   return "<center>" + status + "</center>";
 }
}

function detailFunc(cellData, recordIndex, columIndex){
  var detail = "详细信息";
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<center><a href=\"javascript:doDetailFunc("+seqId+");\">" + detail + "</a></center>";
}

function doDetailFunc(seqId){
  var URL = contextPath + "/subsys/oa/meeting/query/meetingdetail.jsp?seqId=" + seqId;
  //openDialogResize(URL,'820', '500');
  newWindow(URL,'820', '500');
}

