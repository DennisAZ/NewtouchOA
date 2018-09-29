function confirmDel() {
  if(confirm("确认要删除该会议室吗？")) {
    return true;
  } else {
    return false;
  }
}

function confirmDelAll() {
  if(confirm("确认要删除所有会议室吗？")) {
    return true;
  } else {
    return false;
  }
}

/**
 * 字段居中显示
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function meetingRoomCenterFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

/**
 * 字符切断
 * @param str
 * @param length
 * @return
 */
function strCut(str,length){
  if(str.length >= length){
    var temp = str.substring(0,length);
    if(temp){
      str = str.substring(0,length) + "......";
    }
  }
  return str;
}

/**
 * 案卷号
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function rollCodeFunc(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<center><a href=\"javascript:readRollFunc("+seqId+");\">" + cellData + "</a></center>";
}



function mrDescFunc(cellData, recordIndex, columIndex){
  var mrDesc = this.getCellData(recordIndex,"mrDesc");
  if(mrDesc && mrDesc.length >= 6){
    mrDesc = strCut(mrDesc,6);
  }
  return "<center>" + mrDesc + "</center>";
}

function operatorFunc(cellData, recordIndex, columIndex){
  var operator = this.getCellData(recordIndex,"operator");
  $("operator").value = operator;
  if($("operator") && $("operator").value.trim()){
    bindDesc([{cntrlId:"operator", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    return "<center>" + $("operatorDesc").value + "</center>";
  }
  return "<center>" + operator + "</center>";
}

function toIdFunc(cellData, recordIndex, columIndex){
  var toId = this.getCellData(recordIndex,"toId");
  $("toId").value = toId;
  if($("toId") && $("toId").value.trim() && $("toId").value != "0" && $("toId").value != "ALL_DEPT"){
    bindDesc([{cntrlId:"toId", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
    return "<center>" + $("toIdDesc").value + "</center>";
  }else if($("toId") && ($("toId").value == "0" || $("toId").value == "ALL_DEPT")){
    $("toId").value = "0";
    $("toIdDesc").value = "全体部门";
    return "<center>" + $("toIdDesc").value + "</center>";
  }
  return "<center>" + toId + "</center>";
}

function secretToIdFunc(cellData, recordIndex, columIndex){
  var secretToId = this.getCellData(recordIndex,"secretToId");
  $("secretToId").value = secretToId;
  if($("secretToId") && $("secretToId").value.trim()){
    bindDesc([{cntrlId:"secretToId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    return "<center>" + $("secretToIdDesc").value + "</center>";
  }
  return "<center>" + secretToId + "</center>";
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
  var statusFlag = this.getCellData(recordIndex,"status");
  if(statusFlag == ""){
    statusFlag = 0;
  }
  return "<center><a href='modify.jsp?seqId="+seqId+"'>修改</a>&nbsp;<a href=\"javascript:deleteSingle("+seqId+");\">删除</a></center>";
}

/**
 * 删除单个会议室记录
 * @param seqId
 * @return
 */
function deleteSingle(seqId){
  if(!confirmDel()) {
   return ;
  }  
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingRoomAct/deleteSingle.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 全部删除
 * @return
 */
function deleteAll(){
  if(!confirmDelAll()) {
    return ;
   }  
   var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingRoomAct/deleteAll.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     window.location.reload();
   } else {
     alert(rtJson.rtMsrg); 
   }
}
