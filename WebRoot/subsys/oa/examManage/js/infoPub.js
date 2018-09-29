function doInit() {
  //时间
  var parameters = {
      inputId:'beginDate',
      property:{isHaveTime:false}
  ,bindToBtn:'date1'
  };
  new Calendar(parameters);

  var parameters = {
      inputId:'endDate',
      property:{isHaveTime:false}
  ,bindToBtn:'date2'
  };
  new Calendar(parameters);
  moblieSmsRemind('sms2Remind3','sms2Check');
  getSysRemind();
  doTitle();
}
//查询试卷标题
function doTitle(){
  var mgrSec = new SelectMgr();
  mgrSec.addSelect({cntrlId: "paperId"
              , tableName: "EXAM_PAPER"
              , codeField: "SEQ_ID"
              , nameField: "PAPER_TITLE"
              , value: "0", isMustFill: "1"
              , filterField: " "
              , filterValue: ''
              , order: ""
              , reloadBy: ""
              , actionUrl: ""
              });
  mgrSec.loadData();
  mgrSec.bindData2Cntrl();
}

//查询试卷标题
function doTitle2(){
  var mgrSec = new SelectMgr();
  mgrSec.addSelect({cntrlId: "paperId"
              , tableName: "EXAM_PAPER"
              , codeField: "SEQ_ID"
              , nameField: "PAPER_TITLE"
              , value: "0", isMustFill: "0"
              , filterField: " "
              , filterValue: ''
              , order: ""
              , reloadBy: ""
              , actionUrl: ""
              });
  mgrSec.loadData();
  mgrSec.bindData2Cntrl();
}
/** 
 *js代码 
 *是否显示手机短信提醒 
 */
function moblieSmsRemind(remidDiv,remind) {
  var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=36";
  var rtJson = getJsonRs(requestUrl); 
  if (rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  }
  var prc = rtJson.rtData;
  var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
  if (moblieRemindFlag == '2') {
    $(remidDiv).style.display = ''; 
    $(remind).checked = true;
    document.getElementById("smsSJ").value = "1";
  } else if(moblieRemindFlag == '1') { 
    $(remidDiv).style.display = ''; 
    $(remind).checked = false; 
  } else {
    $(remidDiv).style.display = 'none'; 
  }
}

//判断是否要显示短信提醒 
function getSysRemind(){ 
  var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=36"; 
  var rtJson = getJsonRs(requestUrl); 
  if (rtJson.rtState == "1") { 
    alert(rtJson.rtMsrg); 
    return ; 
  }
  var prc = rtJson.rtData; 
  var allowRemind = prc.allowRemind; 
  var defaultRemind = prc.defaultRemind; 
  var mobileRemind = prc.mobileRemind;
  if (allowRemind == '2') {
    $("smsRemindDiv").style.display = 'none';
  }else{ 
    if (defaultRemind == '1') { 
      $("smsflag2").checked = true;
      document.getElementById("smsflag").value = "1";
    }
  }
}

//选择发送消息
function checkBox2() {
  if (document.getElementById("smsflag2").checked) {
    document.getElementById("smsflag").value = "1";
  }else {
    document.getElementById("smsflag").value = "0";
  }
}
function checkBox3() {
  if (document.getElementById("sms2Check").checked) {
    document.getElementById("smsSJ").value = "1";
  }else {
    document.getElementById("smsSJ").value = "0";
  }
}
//表单验证
function checkForm() {
  if ($("flowTitle").value.trim() == "") {
    alert("考试名称不能为空！");
    $("flowTitle").select();
    $("flowTitle").focus();
    return false;
  }
  if ($("participantDesc").value == "") {
    alert("参加考试人员必填！");
    $("participantDesc").select();
    $("participantDesc").focus();
    return false
  }
  if ($("paperId").value == "") {
    alert("试卷不能为空！");
    $("paperId").select();
    $("paperId").focus();
    return false;
  }
  if ($("beginDate").value != "" && $("endDate").value != "" && ($("beginDate").value >= $("endDate").value)) {
    alert("生效日期不能大于等于终止日期！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  if ($("endDate").value != "" && ($("endDate").value <= beginEnd)) {
    alert("终止日期不能小于等于当天时间！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  return true;
}
//提交数据
function checkForm2() {
  if (checkForm()) {
    $("sendTime").value = beginEnd;
    var pars = $('form1').serialize() ;
    var requestURL = contextPath + "/yh/subsys/oa/examManage/act/YHExamFlowAct/add.act";
    var json = getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      window.location.href = contextPath + "/subsys/oa/examManage/infoPub/news.jsp";
    }
  }
}
//修改数据
function checkForm3() {
  if (checkForm()) {
    var pars = $('form1').serialize() ;
    var requestURL = contextPath + "/yh/subsys/oa/examManage/act/YHExamFlowAct/updateFlow.act";
    var json = getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      window.location.href = contextPath + "/subsys/oa/examManage/infoPub/news.jsp";
    }
  }
}
//修改数据
function update(seqId) {
  window.location.href = contextPath +  "/subsys/oa/examManage/infoPub/updateFlow.jsp?seqId=" + seqId;
}
//删除
function deleteVote(seqId) {
  var msg ="确认要删除该考试信息？考试信息数据将被删除且不可恢复！！";
  if (window.confirm(msg)) {
    var requestUrl = contextPath + "/yh/subsys/oa/examManage/act/YHExamFlowAct/deleteFlow.act?seqId=" + seqId;
    var rtJson = getJsonRs(requestUrl);
    window.location.reload();
  }
}
//考试结果统计
function showDesc(paperId,flowId) {
  var myleft = (screen.availWidth-800)/2;
  var url = contextPath + "/subsys/oa/examManage/infoPub/showDesc.jsp?paperId=" + paperId + "&flowId=" + flowId;
  window.open(url,"","height=550,width=920,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=" + myleft + ",resizable=yes");
}
//导出分数
function excelReport(seqId,paperId) {
  window.location.href = contextPath + "/yh/subsys/oa/examManage/act/YHExamFlowAct/excelReport.act?seqId=" + seqId + "&paperId=" + paperId;
}
//查卷
function showReader(seqId,paperId) {
  var myleft = (screen.availWidth-800)/2;
  var url = contextPath + "/subsys/oa/examManage/infoPub/scoreManage/query.jsp?seqId=" + seqId + "&paperId=" + paperId;
  window.open(url,"","height=550,width=800,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=" + myleft + ",resizable=yes");
}
//立即终止
function updateStatus(seqId) {
  var msg ="确认要终止该考试信息！";
  if (window.confirm(msg)) {
    var requestUrl = contextPath + "/yh/subsys/oa/examManage/act/YHExamFlowAct/updateStatus.act?seqId=" + seqId;
    var rtJson = getJsonRs(requestUrl);
    window.location.reload();
  }
}
//查看人员
function userList(participant) {
  var myleft = (screen.availWidth-800)/2;
  window.open(contextPath + "/subsys/oa/examManage/infoPub/showMan.jsp?participant=" + participant,"","height=500,width=500,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=" + myleft + ",resizable=yes");
}
//返回起始时间
function toBenginDate(cellData, recordIndex,columInde){
  var beginDate =  this.getCellData(recordIndex,"beginDate");
  return beginDate.substr(0,10);
}
//返回终止时间
function toEndDate(cellData, recordIndex,columInde){
  var endDate =  this.getCellData(recordIndex,"endDate");
  if (endDate != "") {
    return endDate.substr(0,10);
  } else {
    return "";
  } 
}
//操作
function toCaoZuo(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var paperId = this.getCellData(recordIndex,"paperId");
  var participant = this.getCellData(recordIndex,"participant");
  return "<a href='javascript:update(" + seqId + ");'>修改</a>"
    + "&nbsp;&nbsp;<a href='javascript:deleteVote(" + seqId + ");'>删除</a>"
    + "&nbsp;&nbsp;<a href=javascript:updateStatus(" + seqId + ");>立即终止</a><br>"
    + "&nbsp;&nbsp;<a href=javascript:showReader(" + seqId + "," + paperId + ");>查卷</a>"
    + "&nbsp;&nbsp;<a href=javascript:excelReport(" + seqId + "," + paperId + ");>导出分数</a>"
    + "&nbsp;&nbsp;<a href=javascript:showDesc(" + paperId + "," + seqId + ");>考试结果统计</a>";
}

//参加考试人员
function toStr(cellData,recordIndex,columIndex){
  var participant = this.getCellData(recordIndex,"participant");
  if (participant != "") {
    var str = "?seqId=" + participant + "&tableName=PERSON&tdName=USER_NAME";
    var requestUrl = contextPath + "/yh/subsys/oa/vote/act/YHVoteTitleAct/strString.act" + str;
    var rtJson = getJsonRs(requestUrl);
    var userList = rtJson.rtData;
    return "<a href=javascript:userList('" + participant + "'); title='点击查看所有成员'>" + userList.split(",")[0] + "....</a>";
  } else {
    return "";
  }
}