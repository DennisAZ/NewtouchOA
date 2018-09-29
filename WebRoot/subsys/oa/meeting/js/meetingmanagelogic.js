
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
 * 申请人
 * @param cellData
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

function mrNameFunc(cellData, recordIndex, columIndex){
  return cellData;
}

function mrName2Func(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

function mStartFunc(cellData, recordIndex, columIndex){
  var mStartData = "";
  var mStart = this.getCellData(recordIndex,"mStart");
  if(mStart){
    mStartData = mStart.substr(0, mStart.length - 2);
  }
  return "<center>" + mStartData + "</center>";
}

function mEndFunc(cellData, recordIndex, columIndex){
  var mEndData = "";
  var mEnd = this.getCellData(recordIndex,"mEnd");
  if(mEnd){
    mEndData = mEnd.substr(0, mEnd.length - 2);
  }
  return "<center>" + mEndData + "</center>";
}

/**
 * 详细信息
 * @param seqId
 * @return
 */
function meetingDetailFunc(seqId){
  var URL = contextPath + "/subsys/oa/meeting/query/meetingdetail.jsp?seqId=" + seqId;
  //openDialogResize(URL,'820', '500');
  newWindowss(URL,'820','500');
}

/**
 * 打开新窗口  
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindowss(url,width,height){
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  window.open(url, "meeting", 
      "height=500,width=820,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
      + locY + ", left=" + locX + ", resizable=yes");
}

function confirmDel() {
  if(confirm("确认要删除该会议吗？")) {
    return true;
  } else {
    return false;
  }
}

/**
 * 删除单个会议记录
 * @param seqId
 * @return
 */
function deleteSingle(seqId){
  if(!confirmDel()) {
   return ;
  }  
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/deleteSingle.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
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
      if(rtJson.rtData.path != null){
    	window.location = rtJson.rtData.path;
      }
      else{
        window.location.reload();
      }
    } else {
      alert(rtJson.rtMsrg); 
    }
  }
}

function mStatusFunc(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/checkRoom.act?seqId=" + seqId;
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    if(data == "0"){
      return "<center>无冲突</center>";
    }else{
      return "<center><a href=javascript:conflictFunc("+data+")><font color='red'>预约冲突</font></a></center>";
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function conflictFunc(seqId){
 　var URL = contextPath + "/subsys/oa/meeting/manage/conflictdetail.jsp?seqId=" + seqId;
 openDialogResize(URL,'450', '350');
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
  var mStatus = this.getCellData(recordIndex,"mStatus");
  var flowId = this.getCellData(recordIndex,"flowId");
  var runId = this.getCellData(recordIndex,"runId");
  if(mStatus == "0") {
    if(false){
      return "<center><a href=javascript:meetingDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
      + "<a href=javascript:openPrearrange()>预约情况</a>&nbsp;"
      + "<a href=javascript:formViewFunc(" + seqId + ")>查看流程</a>&nbsp;"
      + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
    }else{
      var ss = "<center><a href=javascript:meetingDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
      + "<a href=javascript:openPrearrange()>预约情况</a>&nbsp;";
      if (runId == 0) {
        ss += "<a href=javascript:modefyMeeting(" + seqId + ")>修改</a>&nbsp;"
          + "<a href=javascript:checkupFunc(" + seqId + ",1)>批准</a>&nbsp;"
          + "<a href=javascript:checkupFunc(" + seqId + ",3)>不准</a>&nbsp;";
      } else {
        ss += "<a href='javascript:formView(" + runId + " , "+flowId+")'>查看流程</a>&nbsp;";
      }
      
      ss += "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
      
      
      return ss;
      
    }
  }
  if(mStatus == "1") {
    var ss = "<center><a href=javascript:meetingDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
      + "<a href=javascript:openPrearrange()>预约情况</a>&nbsp;";
    if (runId == 0) {
      ss += "<a href=javascript:modefyMeeting(" + seqId + ")>修改</a>&nbsp;";
    } else {
      ss += "<a href='javascript:formView(" + runId + " , "+flowId+")'>查看流程</a>&nbsp;";
    }
    ss += "<a href=javascript:checkupFunc(" + seqId + ",0)>撤销 </a>&nbsp;"
    + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
    return ss;
  }
  if(mStatus == "2") {
    var ss = "<center><a href=javascript:meetingDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
    + "<a href=javascript:openPrearrange()>预约情况</a>&nbsp;";
    if (runId == 0) {
      ss += "<a href=javascript:modefyMeeting(" + seqId + ")>修改</a>&nbsp;"
    } else {
      ss += "<a href='javascript:formView(" + runId + " , "+flowId+")'>查看流程</a>&nbsp;";
    }
    ss += "<a href=javascript:meetingSummaryFunc(" + seqId + ")>会议纪要</a>&nbsp;"
      + "<a href=javascript:checkupFunc(" + seqId + ",4)>结束</a>&nbsp;"
      + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
    return ss;
  }
  if(mStatus == "3") {
    var ss = "<center><a href=javascript:meetingDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
      + "<a href=javascript:openPrearrange()>预约情况</a>&nbsp;";
    if (runId == 0) {
      ss += "<a href=javascript:checkupFunc(" + seqId + ",1)>批准</a>&nbsp;"
    } else {
      ss += "<a href='javascript:formView(" + runId + " , "+flowId+")'>查看流程</a>&nbsp;";
    }
    ss += "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
    return ss
  }
  if(mStatus == "4") {
    var ss = "<center><a href=javascript:meetingDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
    + "<a href=javascript:openPrearrange()>预约情况</a>&nbsp;"
    + "<a href=javascript:meetingSummaryFunc(" + seqId + ")>会议纪要</a>&nbsp;"
    + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
    return ss;
  }
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
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow(url,width,height){
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  window.open(url, "meeting", 
      "height=540,width=740,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
      + locY + ", left=" + locX + ", resizable=yes");
}


//预约情况
function openPrearrange() {
  window.open( contextPath + '/subsys/oa/meeting/apply/prearrange.jsp','','height=500,width=820,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=100,resizable=yes');
}

function optsList(cellData, recordIndex, columIndex){
  var cycleNo = this.getCellData(recordIndex,"cycleNo");
  var mStatus = this.getCellData(recordIndex,"mStatus");
  return "<center><a href='managecycle.jsp?cycleNo="+cycleNo+"&mStatus="+mStatus+"'>周期性会议审批</a></center>";
}

function optsCycle(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var cycleNo = this.getCellData(recordIndex,"cycleNo");
  return "<center><a href=javascript:meetingDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
  + "<a href=javascript:openPrearrange()>预约情况</a>&nbsp;"
  + "<a href=javascript:modefyMeeting(" + seqId + ")>修改</a>&nbsp;"
  + "<a href=javascript:checkupFunc(" + seqId + ",1)>批准</a>&nbsp;"
  + "<a href=javascript:checkupFunc(" + seqId + ",3)>不准</a>&nbsp;"
  + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
}

function checkBoxRender(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/checkRoom.act?seqId=" + seqId;
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    if(data == "0"){
      return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + seqId + "\" onclick=\"checkSelf()\" ></center>";
    }else{
      return "";
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 是否选中
 * @param cntrlId
 * @return
 */
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

function confirmCheckAll() {
  if(confirm("确认要批量批准待批会议吗？")) {
    return true;
  } else {
    return false;
  }
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
 * 批量批准待批会议
 * @return
 */
function checkUpAll() {
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要批量批准待批会议，请至少选择其中一个。");
    return;
  }
  if(!confirmCheckAll()) {
    return ;
  }  
  
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/checkUpAllCycle.act";
  var rtJson = getJsonRs(url, "idStrs=" + idStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
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

/**
 * 自动开始和自动结束
 * @return
 */
function autoBeginEnd(){
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/getAutoBeginEnd.act";
  var json=getJsonRs(url);
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  }
}