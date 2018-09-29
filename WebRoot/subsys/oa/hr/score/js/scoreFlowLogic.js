
function doTitle(){
  var mgrSec = new SelectMgr();
  mgrSec.addSelect({cntrlId: "groupId"
    , tableName: "oa_score_item"
      , codeField: "SEQ_ID"
        , nameField: "ITEM_NAME"
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

/** 
 *js代码 
 *是否显示手机短信提醒 
 */
function moblieSmsRemind(remidDiv,remind) {
  var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=15";
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
  var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=15"; 
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
    alert("考核任务标题不能为空！");
    $("flowTitle").select();
    $("flowTitle").focus();
    return false;
  }
  if ($("rankmanDesc").value == "") {
    alert("考核人不能空！！");
    $("rankmanDesc").focus();
    return false
  }
  if ($("participantDesc").value == "") {
    alert("被考核人不能空！！");
    $("participantDesc").focus();
    return false;
  }
  if ($("beginDate").value != "" && $("endDate").value != "" && ($("beginDate").value >= $("endDate").value)) {
    alert("生效日期不能大于等于终止日期！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  if ($("endDate").value != "" && ($("endDate").value <= $("beginDate").value)) {
    alert("终止时间不能小于等于当天时间！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  return true;
}

/**
 * 管理已发布的考核任务列表操作
 * @param cellData
 * @param recordIndex
 * @param columInde
 * @return
 */
function optsIndex1(cellData,recordIndex,columInde) {
  var seqId = this.getCellData(recordIndex,"seqId");
  var tdName = "";
  if(voteStatus == "1"){
    return "<a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;"
    +"<a href=javascript:deleteSingle(" + seqId + ")>删除</a>&nbsp;"
    +"<a href=javascript:updateBeginDate('" + seqId + "','BEGIN_DATE','" + dayTime +"')>立即生效</a>";
  }
  if(voteStatus == "2"){
    return "<a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;"
    + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>&nbsp;"
    + "<a href=javascript:updateBeginDate('" + seqId + "','END_DATE','" + dayTime + "')>立即终止</a>";
  }
  if (voteStatus == "3") {
    return "<a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;"
    + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>&nbsp;"
    + "<a href=javascript:updateBeginDate('" + seqId + "','END_DATE','')>恢复生效</a>";
  }

} 

/**
 * 修改考核任务
 * @param seqId
 * @return
 */
function doEdit(seqId){
  location = contextPath + "/subsys/oa/hr/score/flow/modify.jsp?seqId="+seqId;
}

function confirmDel() {
  if(confirm("确认要删除该考核任务？考核数据将被删除且不可恢复！！")) {
    return true;
  } else {
    return false;
  }
}

/**
 * 删除考核任务一条记录
 * @param seqId
 * @return
 */
function deleteSingle(seqId){
  if(!confirmDel()) {
    return ;
  }  
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreFlowAct/deleteSingle.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

//查询试卷标题
function doTitle(){
  var mgrSec = new SelectMgr();
  mgrSec.addSelect({cntrlId: "groupId"
    , tableName: "oa_score_team"
      , codeField: "SEQ_ID"
        , nameField: "GROUP_NAME"
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

function doTitle1(){
  var mgrSec = new SelectMgr();
  mgrSec.addSelect({cntrlId: "groupId"
    , tableName: "oa_score_team"
      , codeField: "SEQ_ID"
        , nameField: "GROUP_NAME"
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
//返回生效时间
function toBeginDate(cellData, recordIndex,columInde) {
  var beginDate =  this.getCellData(recordIndex,"beginDate");
  return beginDate.substr(0,10);
}
//返回结束时间
function toEndDate(cellData, recordIndex,columInde) {
  var endDate =  this.getCellData(recordIndex,"endDate");
  return endDate.substr(0,10);
}
//返回操作项

function toCaoZuo(cellData,recordIndex,columInde) {
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href='#'>考核</a>";
}

//操作
function toCao(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var groupId = this.getCellData(recordIndex,"seqIds");
  var anonmity = this.getCellData(recordIndex,"anonmity");
  return "<a href=javascript:showReader('" + seqId +"','"+groupId+"','"+anonmity+"');>查阅 </a>";
}
//导出
function toDaoChu(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var groupId = this.getCellData(recordIndex,"seqIds");
  var rankman = this.getCellData(recordIndex,"rankman");
  var flowTitle = this.getCellData(recordIndex,"flowTitle");
  var participant = this.getCellData(recordIndex,"participant");
  return "<a href=javascript:excelReport('" + seqId +"','"+groupId+"');>总分 </a>"
  + "&nbsp;<a href=javascript:excelDetail('" + seqId +"','"+groupId+"');>分数明细 </a>";
}
//导出总分
function excelReport(seqId,groupId){
  var param = "seqId=" + seqId + "&groupId=" + groupId;
  window.location.href = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreFlowAct/excelExport.act?" + param;
}

//导出分数明细
function excelDetail(seqId,groupId){
  var param = "seqId=" + seqId + "&groupId=" + groupId;
  window.location.href = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreFlowAct/excelDetail.act?" + param;
}

//查阅
function showReader(seqId,groupId,anonmity){
  var param = "flowId=" + seqId + "&groupId=" + groupId + "&anonmity=" + anonmity;
  var myleft = (screen.availWidth-800)/2;
  window.open(contextPath + "/subsys/oa/hr/score/flow/scoreData/query.jsp?" + param,"","height=500,width=500,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=" + myleft + ",resizable=yes");
  //window.location.href = contextPath + "/subsys/oa/hr/score/flow/scoreData/query.jsp?" + param;
}

//返回状态function toFlow(cellData,recordIndex,columInde) {
  var seqId = this.getCellData(recordIndex,"seqId");
  var endDate =  this.getCellData(recordIndex,"endDate");
  var beginDate =  this.getCellData(recordIndex,"beginDate");
  //voteStatus = this.getCellData(recordIndex,"voteStatus");
  endDate = endDate.substr(0,10);
  beginDate = beginDate.substr(0,10);
  if (beginDate > dayTime) {
    voteStatus = "1";
    return "<font color='#00AA00'><b>待生效</b></font>";
  }
  if (beginDate <= dayTime && (endDate > dayTime || endDate == "")) {
    voteStatus = "2";
    return "<font color='#00AA00'><b>生效</b></font>"
  }
  if (endDate <= dayTime) {
    voteStatus = "3";
    return "<font color='#FF0000'><b>终止</b></font>"
  }
}
//返回匿名
function toAnonmity(cellData,recordIndex,columInde) {
  var anonmity = this.getCellData(recordIndex,"anonmity");
  if (anonmity == "0") {
    return "不允许";
  } else {
    return "允许";
  }
}
//返回考核集

function toGroupId(cellData,recordIndex,columInde) {
  var groupId = this.getCellData(recordIndex,"groupId");
  var seqId = this.getCellData(recordIndex,"seqIds");
  var groupFlag = this.getCellData(recordIndex,"groupFlag");
  return "<a href=javascript:selectGroup('" + seqId + "','" + groupFlag + "')>" + groupId + "</a>";
}
//返回考核人员名

function toRankman(cellData,recordIndex,columInde) {
  var rankman = this.getCellData(recordIndex,"rankman");
  if (rankman != "") {
    var str = "?seqId=" + rankman + "&tableName=PERSON&tdName=USER_NAME";
    var requestUrl = contextPath + "/yh/subsys/oa/vote/act/YHVoteTitleAct/strString.act" + str;
    var rtJson = getJsonRs(requestUrl);
    var userList = rtJson.rtData;
    return userList;
  } else {
    return "";
  }
}

//返回考核人员名--串

function toRankmanStr(cellData,recordIndex,columInde) {
  var rankman = this.getCellData(recordIndex,"rankman");
  if (rankman != "") {
    var str = "?seqId=" + rankman + "&tableName=PERSON&tdName=USER_NAME";
    var requestUrl = contextPath + "/yh/subsys/oa/vote/act/YHVoteTitleAct/strString.act" + str;
    var rtJson = getJsonRs(requestUrl);
    var userList = rtJson.rtData;
    return "<a href=javascript:userList('" + rankman + "'); title='点击查看所有信息'>" + userList.split(",")[0] + "....</a>";
  } else {
    return "";
  }
}

//返回被考核人员名--串

function toParticipantStr(cellData,recordIndex,columInde) {
  var participant = this.getCellData(recordIndex,"participant");
  if (participant != "") {
    var str = "?seqId=" + participant + "&tableName=PERSON&tdName=USER_NAME";
    var requestUrl = contextPath + "/yh/subsys/oa/vote/act/YHVoteTitleAct/strString.act" + str;
    var rtJson = getJsonRs(requestUrl);
    var userList = rtJson.rtData;
    return "<a href=javascript:userList('" + participant + "'); title='点击查看所有信息'>" + userList.split(",")[0] + "....</a>";
  } else {
    return "";
  }
}

function toRankmanStrFunc(cellData,recordIndex,columInde) {
  var seqId = this.getCellData(recordIndex,"seqId");
  var rankman = this.getCellData(recordIndex,"rankman");
  if (rankman != "") {
    var str = "?seqId=" + rankman + "&tableName=PERSON&tdName=USER_NAME";
    var requestUrl = contextPath + "/yh/subsys/oa/vote/act/YHVoteTitleAct/strString.act" + str;
    var rtJson = getJsonRs(requestUrl);
    var userList = rtJson.rtData;
    return "<a href=javascript:showManCheck('" + rankman + "','" + seqId + "'); title='点击查看所有信息'>" + userList.split(",")[0] + "....</a>";
  } else {
    return "";
  }
}

function toParticipantStrFunc(cellData,recordIndex,columInde) {
  var seqId = this.getCellData(recordIndex,"seqId");
  var participant = this.getCellData(recordIndex,"participant");
  if (participant != "") {
    var str = "?seqId=" + participant + "&tableName=PERSON&tdName=USER_NAME";
    var requestUrl = contextPath + "/yh/subsys/oa/vote/act/YHVoteTitleAct/strString.act" + str;
    var rtJson = getJsonRs(requestUrl);
    var userList = rtJson.rtData;
    return "<a href=javascript:showManChecked('" + participant + "'); title='点击查看所有信息'>" + userList.split(",")[0] + "....</a>";
  } else {
    return "";
  }
}

//查看人员
function userList(participant) {
  var myleft = (screen.availWidth-800)/2;
  window.open(contextPath + "/subsys/oa/hr/score/flow/scoreData/showMan.jsp?participant=" + participant,"","height=500,width=500,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=" + myleft + ",resizable=yes");
}

function showManCheck(participant,seqId) {
  var myleft = (screen.availWidth-800)/2;
  window.open(contextPath + "/subsys/oa/hr/score/flow/showManCheck.jsp?participant=" + participant + "&flowId=" + seqId,"","height=500,width=500,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=" + myleft + ",resizable=yes");
}

function showManChecked(participant) {
  var myleft = (screen.availWidth-800)/2;
  window.open(contextPath + "/subsys/oa/hr/score/flow/showManChecked.jsp?participant=" + participant,"","height=500,width=500,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=" + myleft + ",resizable=yes");
}

function selectGroup(seqId,groupFlag) {
  var myleft = (screen.availWidth-800)/2;
  window.open(contextPath + "/subsys/oa/hr/score/flow/scoreData/showReader.jsp?seqId=" + seqId + "&groupFlag=" + groupFlag,"","height=500,width=500,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=" + myleft + ",resizable=yes");
  
}

//立即终止

function toPublish(cellData, recordIndex,columInde){ 
  var endDate = this.getCellData(recordIndex,"endDate"); 
  var beginDate = this.getCellData(recordIndex,"beginDate"); 
  endDate = endDate.substr(0,10); 
  beginDate = beginDate.substr(0,10); 
  //if (publish == "1") { 
  if (beginDate > dayTime) { 
    voteStatus = 1; 
    return "<font color='#00AA00'><b>待生效</b></font>"; 
  } 
  if (beginDate <= dayTime && (endDate > dayTime || endDate == "")) { 
    voteStatus = 2; 
    return "<font color='#00AA00'><b>生效</b></font>" 
  } 
  if (endDate <= dayTime) { 
    voteStatus = 3; 
    return "<font color='#FF0000'><b>终止</b></font>" 
  } 
  // } 
}

//立即生效,立即终止,恢复终止
function updateBeginDate(seqId,tdName,dayTime) {
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreFlowAct/updateBeginDate.act?seqId=" + seqId + "&tdName=" + tdName + "&dayTime=" + dayTime;
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == '1') { 
    alert(rtJson.rtMsrg); 
    return ; 
  } else {
    window.location.reload();
  }
}

function getGroupName(seqId){
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreFlowAct/getGroupName.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    return  rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 获取考核任务标题
 * @param paperSeqId
 * @return
 */
function getFlowTitleName(seqId){
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreFlowAct/getFlowTitleName.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    return  rtJson.rtData ;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 获取考核指标集标题
 * @param paperSeqId
 * @return
 */
function getGroupName(seqId){
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreShowAct/getScoreGroupName.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    return  rtJson.rtData ;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function mouseOverHander(show1td){
  show1td.onmouseover = function(){
    show1td.style.backgroundColor = "#E0E0E0";
  }
  show1td.onmouseout = function(){
    show1td.style.backgroundColor = "#FFFFFF";
  }
}

/**
 * 鼠标点击事件
 */
function clickPriv(field){
  var seqId = field.id;
  var userId = field.userId;
  var parent = window.parent.hrms;
  var groupId = $("groupId").value;
  var checkFlag = $("checkFlag").value;
  if(groupFlag == "0"){
    //parent.location = contextPath + "/subsys/oa/hr/score/submit/scoreIndex.jsp?userId="+seqId+"&flowId="+flowId+"&groupFlag="+groupFlag+"&currPage=1";
    parent.location = contextPath + "/subsys/oa/hr/score/submit/scoreData.jsp?userId="+seqId+"&flowId="+flowId+"&groupFlag="+groupFlag+"&groupId="+groupId+"&checkFlag="+checkFlag+"&currPage=1";
  }else{
    parent.location = contextPath + "/subsys/oa/hr/score/submit/scoreIndex.jsp?userId="+seqId+"&flowId="+flowId+"&groupFlag="+groupFlag+"&checkFlag="+checkFlag+"&currPage=1";
  }
}

