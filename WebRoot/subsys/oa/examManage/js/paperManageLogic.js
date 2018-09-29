
/**
 * 字段居中显示
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function paperCenterFunc(cellData, recordIndex, columIndex){
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
  //$("roomId").value = this.getCellData(recordIndex,"roomId");
  //$("questionsCount").value = this.getCellData(recordIndex,"questionsCount");
  //$("paperGrade").value = this.getCellData(recordIndex,"paperGrade");
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

/**
 * 获取题型难度
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function questionsRankFunc(cellData, recordIndex, columIndex){
  var str = "";
  if(cellData == "0"){
    str = "低";
  }
  if(cellData == "1"){
    str = "中";
  }
  if(cellData == "2"){
    str = "高";
  }
  return "<center>" + str  + "</center>"; 
}

/**
 * 自动选题
 * @return
 */
function autoTopics(){
  var roomId = $("roomId").value;
  var paperSeqId = $("paperSeqId").value;
  var questionsRank = $("questionsRank").value;
  var questionsType = $("questionsType").value;
  var url = contextPath + "/yh/subsys/oa/examManage/act/YHExamPaperAct/getAutoTopics.act";
  var rtJson = getJsonRs(url, "roomId="+roomId+"&paperSeqId="+paperSeqId+"&questionsRank="+questionsRank+"&questionsType="+questionsType);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    if(data == "0"){
      window.location.reload();
    }else{
      location = contextPath + "/subsys/oa/examManage/paperManage/updateQuiz.jsp?paperSeqId="+paperSeqId;
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 手动选题
 * @return
 */
function handTopics(){
  var roomId = $("roomId").value;
  var paperSeqId = $("paperSeqId").value;
  var questionsCount = $("questionsCount").value;
  var questionsRank = $("questionsRank").value;
  var questionsType = $("questionsType").value;
  var URL = contextPath + "/subsys/oa/examManage/paperManage/selectManual.jsp?roomId="+roomId+"&paperSeqId="+paperSeqId+"&questionsCount="+questionsCount+"&questionsRank="+questionsRank+"&questionsType="+questionsType;
  newRecordWindow(URL,'800','500');
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
  return "<center><a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;"
      + "<a href=javascript:doTopics(" + seqId + ")>选题</a>&nbsp;"
      + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
}

/**
 * 修改
 * @param seqId
 * @return
 */
function doEdit(seqId){
  var url = contextPath + "/yh/subsys/oa/examManage/act/YHExamPaperAct/useredByPaper.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    if(data == "1"){
      location = contextPath + "/subsys/oa/examManage/paperManage/update.jsp";
    }else{
      location = contextPath + "/subsys/oa/examManage/paperManage/modify.jsp?seqId="+seqId;
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 判断所选试题数量是否溢出--cc
 * @param roomId
 * @param questionsCount
 * @return
 */
function isCount(roomId, questionsCount){
  var url = contextPath + "/yh/subsys/oa/examManage/act/YHExamPaperAct/isCount.act";
  var rtJson = getJsonRs(url, "roomId=" + roomId + "&questionsCount="+questionsCount);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    return data;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 选题
 * @param seqId
 * @return
 */
function doTopics(seqId){
  var url = contextPath + "/yh/subsys/oa/examManage/act/YHExamPaperAct/useredByPaper.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    if(data == "1"){
      location = contextPath + "/subsys/oa/examManage/paperManage/update.jsp";
    }else{
      location = contextPath + "/subsys/oa/examManage/paperManage/paperDetails.jsp?paperSeqId="+seqId;
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function confirmDel() {
  if(confirm("确定要删除该试卷吗？")) {
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
  var url = contextPath + "/yh/subsys/oa/examManage/act/YHExamPaperAct/deleteSingle.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
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

/**
 * 出题时间
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function sendDateFunc(cellData, recordIndex, columIndex){
  var mStartData = "";
  var sendDate = this.getCellData(recordIndex,"sendDate");
  if(sendDate){
    mStartData = sendDate.substr(0, 10);
  }
  return "<center>" + mStartData + "</center>";
}

