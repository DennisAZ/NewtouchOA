function doInit(){
  var url = contextPath + "/yh/subsys/oa/examManage/act/YHExamQuizAct/selectQuiz.act";
   cfgs = {
    dataAction: url,
    container: "giftList",
    paramFunc: getParam,
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"hidden", name:"roomId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"roomName", text:"所属题库", width: "6%",align:"center"},
       {type:"text", name:"type", text:"题型", width: "6%",align:"center",render:toType},
       {type:"text", name:"rank", text:"难度", width: "6%",align:"center",render:toRank},
       {type:"text", name:"title", text:"题目", width: "6%",align:"left"},
       {type:"text", name:"caoZuo", text:"操作", width: "10%",align:"center",render:toCaoZuo}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"250" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>尚未定义试题!</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table); 
  } else {
    $("toDiv").update("共<span class='big4'>&nbsp;" + total + "</span>&nbsp;条")
  }
  doInitFlow();
}
//查询
function queryGift(){
  $("roomId").value = document.getElementById("roomId2").value;
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('giftList').style.display="";
    $('returnNull').style.display="none";
    $("toDiv").update("共<span class='big4'>&nbsp;" + total + "</span>&nbsp;条")
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件试题！ </div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').style.display = ""; 
    $('returnNull').update(table);
    $("toDiv").update("")
  }
}

function getParam(){
  queryParam = $("form1").serialize();
  return queryParam;
}
//下拉框
function doInitFlow(){
  var mgrSec = new SelectMgr();
  mgrSec.addSelect({cntrlId: "roomId2"
              , tableName: "EXAM_QUIZ_SET"
              , codeField: "SEQ_ID"
              , nameField: "ROOM_NAME"
              , value: "0", isMustFill: "1"
              , filterField: ""
              , filterValue: '1'
              , order: ""
              , reloadBy: ""
              , actionUrl: ""
              ,extData:[new CodeRecord("0","所有试题")]         
              });
  mgrSec.loadData();
  mgrSec.bindData2Cntrl();
}
//操作
function toCaoZuo(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href='javascript:update(" + seqId + ");'>修改</a>"
    + "&nbsp;&nbsp;<a href='javascript:deleteQuiz(" + seqId + ");'>删除</a>"
}
//题型
function toType(cellData, recordIndex, columInde){
  var type = this.getCellData(recordIndex,"type");
  if (type == "0") {
    return "单选";
  } else { 
    return "多选";
  }
}

//难度
function toRank(cellData, recordIndex, columInde){
  var rank = this.getCellData(recordIndex,"rank");
  if (rank == "0") {
    return "低";
  }
  if (rank == "1") {
    return "中";
  } else {
    return "高";
  }
}
//修改
function update (seqId) {
  window.location.href = contextPath + "/subsys/oa/examManage/quizManage/updateQuiz.jsp?seqId=" + seqId;
}
//删除
function deleteQuiz(seqId) {
  var msg ="确认要删除该试题吗？试题可能正被使用。";
  if (window.confirm(msg)) {
    var requestUrl = contextPath + "/yh/subsys/oa/examManage/act/YHExamQuizAct/deleteQuiz.act?seqId=" + seqId;
    var rtJson = getJsonRs(requestUrl);
    window.location.reload();
  }
}