//查询试卷标题
function doTitle(){
  var mgrSec = new SelectMgr();
  mgrSec.addSelect({cntrlId: "groupId"
    , tableName: "oa_score_item"
      , codeField: "SEQ_ID"
        , nameField: "ITEM_NAME"
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
//返回操作项
function toCaoZuo2(cellData,recordIndex,columInde) {
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href='#'>修改</a>&nbsp;<a href='#'>删除</a>&nbsp;<a href='#'>立即终止</a>";
} 
//操作
function toCao(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href=javascript:showReader();>查阅 </a>"
  + "&nbsp;&nbsp;<a href=javascript:excelReport();>分值统计 </a>"
}
//导出
function toDaoChu(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId"); 
  return "<a href=javascript:showReader();>总分 </a>"
  + "&nbsp;&nbsp;<a href=javascript:excelReport();>分数明细 </a>"
}
//返回状态
function toFlow(cellData,recordIndex,columInde) {
  var seqId = this.getCellData(recordIndex,"seqId");
  var endDate =  this.getCellData(recordIndex,"endDate");
  var beginDate =  this.getCellData(recordIndex,"beginDate");
  endDate = endDate.substr(0,10);
  beginDate = beginDate.substr(0,10);
  if (beginDate > dayTime) {
    return "<font color='#00AA00'><b>待生效</b></font>";
  }
  if (beginDate <= dayTime && (endDate > dayTime || endDate == "")) {
    return "<font color='#00AA00'><b>生效</b></font>"
  }
  if (endDate <= dayTime) {
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
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href=javascript:selectGroup('" + seqId + "')>" + groupId + "</a>";
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
    return "<a href='#' title='点击查看所有信息'>" + userList.split(",")[0] + "....</a>";
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
    return "<a href='#' title='点击查看所有信息'>" + userList.split(",")[0] + "....</a>";
  } else {
    return "";
  }
}
function selectGroup(seqId) {
  return;
}
