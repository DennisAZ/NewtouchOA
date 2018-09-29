
/**
 * 字段居中显示
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function scoreCenterFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

/**
 * 获取所属题库名称
 * @param paperSeqId
 * @return
 */
function getRoomName(paperSeqId){
  var url = contextPath + "/yh/subsys/oa/examManage/act/YHExamPaperAct/getRoomName.act";
  var rtJson = getJsonRs(url, "paperSeqId=" + paperSeqId);
  if (rtJson.rtState == "0") {
    return  rtJson.rtData ;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 获取roomId
 * @param paperSeqId
 * @return
 */
function getRoomId(paperSeqId){
  var url = contextPath + "/yh/subsys/oa/examManage/act/YHExamPaperAct/getRoomId.act";
  var rtJson = getJsonRs(url, "paperSeqId=" + paperSeqId);
  if (rtJson.rtState == "0") {
    return  rtJson.rtData ;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 题库名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function roomNameFunc(cellData, recordIndex, columIndex){
  var roomId = this.getCellData(recordIndex,"roomId");
  var url = contextPath + "/yh/subsys/oa/examManage/act/YHExamPaperAct/getRoomNameSingle.act";
  var rtJson = getJsonRs(url, "roomId=" + roomId);
  if (rtJson.rtState == "0") {
    return "<center>" + rtJson.rtData + "</center>";
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 获取最新插入EXAM_PAPER表中的seqId
 * @return
 */
function getPaperSeqId(){
  var url = contextPath + "/yh/subsys/oa/examManage/act/YHExamPaperAct/getPaperSeqId.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 获取试题数量
 * @param paperSeqId
 * @return
 */
function getQuestionsCount(paperSeqId){
  var url = contextPath + "/yh/subsys/oa/examManage/act/YHExamPaperAct/getQuestionsCount.act";
  var rtJson = getJsonRs(url, "paperSeqId="+paperSeqId);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 获取选择题ID串
 * @param paperSeqId
 * @return
 */
function getQuestionsList(paperSeqId){
  var url = contextPath + "/yh/subsys/oa/examManage/act/YHExamPaperAct/getQuestionsList.act";
  var rtJson = getJsonRs(url, "paperSeqId="+paperSeqId);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 获取题型
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function questionsTypeFunc(cellData, recordIndex, columIndex){
  var str = "";
  if(cellData == "0"){
    str = "单选";
  }
  if(cellData == "1"){
    str = "多选";
  }
  return "<center>" + str  + "</center>"; 
}

function newRecordWindow(url,width,height){
  var myleft = (screen.availWidth - width)/2;
  var mytop = (screen.availHeight - height)/2;
  window.open(url, "examManage", 
      "height=500,width=800,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" + mytop + ", left=" + myleft + ", resizable=yes");
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
  var groupFlag = this.getCellData(recordIndex,"groupFlag");
  return "<center><a href=javascript:doDetail(" + seqId + "," + groupFlag + ")>指标集明细</a>&nbsp;"
      + "<a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;"
      + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
}

/**
 * 修改
 * @param seqId
 * @return
 */
function doEdit(seqId){
  location = contextPath + "/subsys/oa/hr/score/modify.jsp?seqId="+seqId;
}

/**
 * 选题
 * @param seqId
 * @return
 */
function doDetail(seqId, groupFlag){
  if(groupFlag == "0"){
    location = contextPath + "/subsys/oa/hr/score/group/index.jsp?seqId="+seqId;
  }else{
    location = contextPath + "/subsys/oa/hr/score/group/fillItem.jsp?seqId="+seqId+"&groupId="+groupFlag;
  }
}

function confirmDel() {
  if(confirm("确认要删除该指标集？")) {
    return true;
  } else {
    return false;
  }
}

/**
 * 删除一条记录
 * @param seqId
 * @return
 */
function deleteSingle(seqId){
  if(!confirmDel()) {
   return ;
  }  
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreGroupAct/deleteSingle.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function checkDiary() {
  if ($("diaryId").checked) {
    $("diaryId").value = "DIARY";
  }else {
    $("diaryId").value = "";
  }
}
function checkCalendar() {
  if ($("calendarId").checked) {
    $("calendarId").value = "CALENDAR";
  }else {
    $("calendarId").value = "";
  }
}
