function confirmDel() {
  if(confirm("确认要删除该项案卷吗？")) {
    return true;
  } else {
    return false;
  }
}

function confirmDelAll() {
  if(confirm("确认要删除已选中的案卷吗？")) {
    return true;
  } else {
    return false;
  }
}
function Test(leaveDate1,leaveDate2){ 
//var leaveDate1 = document.getElementById("leaveDate1"); 
//var leaveDate2 = document.getElementById("leaveDate2"); 
  var leaveDate1Array = leaveDate1.value.trim().split(" "); 
  var leaveDate2Array = leaveDate2.value.trim().split(" "); 
  var type1 = "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2); 
  
  if(leaveDate1.value && leaveDate2.value){
    if(leaveDate1.value > leaveDate2.value){
      alert("错误 起始时间不能大于截至时间！");
      leaveDate1.focus(); 
      leaveDate1.select(); 
      return false;
    }
  }
  if(leaveDate1.value){
    if(leaveDate1Array.length != 2){ 
      alert("错误,起始日期格式不对，应形如 1999-01-02"); 
      leaveDate1.focus(); 
      leaveDate1.select(); 
      return false; 
    }else{ 
      if(!isValidDateStr(leaveDate1Array[0]) || leaveDate1Array[1].match(re1) == null || leaveDate1Array[1].match(re2) != null){ 
        alert("错误,起始日期格式不对，应形如 1999-01-02"); 
        leaveDate1.focus(); 
        leaveDate1.select(); 
      return false; 
      } 
    } 
  }
  if(leaveDate2.value){
    if(leaveDate2Array.length != 2){ 
      alert("错误,终止日期格式不对，应形如 1999-01-02"); 
      leaveDate2.focus(); 
      leaveDate2.select(); 
      return false; 
    }else{ 
      if(!isValidDateStr(leaveDate2Array[0])||leaveDate2Array[1].match(re1) == null || leaveDate2Array[1].match(re2) != null){ 
      alert("错误,终止日期格式不对，应形如 1999-01-02"); 
      leaveDate2.focus(); 
      leaveDate2.select(); 
      return false; 
      } 
    } 
  }
  return true;
}

/**
 * 字段居中显示
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function rollCenterFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
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

/**
 * 查看案卷号
 * @param TO_ID
 * @param TO_NAME
 * @return
 */
function readRollFunc(seqId){
 　var URL = contextPath + "/subsys/oa/rollmanage/readroll.jsp?seqId=" + seqId;
 openDialogResize(URL,'500', '400');
}

function rollStatusFunc(cellData, recordIndex, columIndex){
  if(cellData == 1){
    return "<center>已封卷</center>";
  }else if(cellData == 0){
    return "<center>未封卷</center>";
  }else{
    return "<center>未封卷</center>";
  }
}

function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
    return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" ></center>";
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
  return "<center><a href='rollfile.jsp?seqId="+seqId+"'>查看文件</a>&nbsp;<a href=\"javascript:statusFunc("+seqId+","+statusFlag+");\">拆卷/封卷</a>&nbsp;<a href='modify.jsp?seqId="+seqId+"'>修改</a>&nbsp;<a href=\"javascript:deleteSingle("+seqId+");\">删除</a></center>";
}

function optsFunc(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var statusFlag = this.getCellData(recordIndex,"status");
  if(statusFlag == ""){
    statusFlag = 0;
  }
  return "<center><a href='rollfile.jsp?seqId="+seqId+"'>查看文件</a>&nbsp;<a href=\"javascript:statusOtherFunc("+seqId+","+statusFlag+");\">拆卷/封卷</a>&nbsp;<a href='modify.jsp?seqId="+seqId+"'>修改</a>&nbsp;<a href=\"javascript:deleteSingle("+seqId+");\">删除</a></center>";
}

/**
 * 操作文件
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function optsFile(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<center><a href=\"javascript:destroySingleFile("+seqId+");\">销毁</a></center>";
}

/**
 * 销毁文件
 * @param seqId
 * @return
 */
function destroySingleFile(seqId){
  var msg='确认要销毁该项文件么？';
  if(window.confirm(msg)){
    var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
    var url = requestURLStr + "/destroySingleFile.act";
    var rtJson = getJsonRs(url, "seqId=" + seqId );
    if(rtJson.rtState == "0"){      
      window.location.reload();
    }else{
      alert(rtJson.rtMsrg); 
      return ;
    }
  }
}

function confirmDesDelAll() {
  if(confirm("确认要销毁已选中的文件吗？")) {
    return true;
  } else {
    return false;
  }
}

/**
 * 批量销毁
 * @return
 */
function destroyAll() {
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要销毁文件，请至少选择其中一个。");
    return;
  }
  if(!confirmDesDelAll()) {
    return ;
  }  
  var requestURLStr = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct";
  var url = requestURLStr + "/destroySingleFile.act";
  var rtJson = getJsonRs(url, "seqId=" + idStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 删除单个案卷
 * @param seqId
 * @return
 */
function deleteSingle(seqId){
  if(!confirmDel()) {
   return ;
  }  
  var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/deleteSingle.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 案卷状态
 * @param seqId
 * @param statusFlag
 * @return
 */
function statusFunc(seqId,statusFlag){
  var urls = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/updateStatusFlag.act";
  var rtJsons = getJsonRs(urls , "seqId=" + seqId + "&statusFlag=" + statusFlag);
  if(rtJsons.rtState == '0'){
    pageMgr.refreshAll();
  }
}

function statusOtherFunc(seqId,statusFlag){
  var urls = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/updateStatusFlag.act";
  var rtJsons = getJsonRs(urls , "seqId=" + seqId + "&statusFlag=" + statusFlag);
  if(rtJsons.rtState == '0'){
    //location = contextPath + contextPath + "/subsys/oa/rollmanage/manage.jsp";
    pageMgr.refreshAll();
  }
}

function getRmsRollRoomNameFunc(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var urls = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getRmsRollRoomName.act";
  var rtJsons = getJsonRs(urls , "seqId=" + cellData);
  if(rtJsons.rtState == '0'){
    return "<center>" + rtJsons.rtData + "<center>";
  }else{
    alert(rtJson.rtMsrg);
  }
}

/**
 * 凭证类别
 * @return
 */
function getCodeNameKindFunc(cellData, recordIndex, columIndex){
  var classCode = this.getCellData(recordIndex,"certificateKind");
  var classNo = "RMS_CERTIFICATE_KIND";
  var urls = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getCodeName.act";
  var rtJsons = getJsonRs(urls , "classCode=" + classCode + "&classNo=" + classNo);
  if(rtJsons.rtState == '0'){
    return "<center>" + rtJsons.rtData + "<center>";
  }else{
    alert(rtJson.rtMsrg);
  }
}

/**
 * 案卷密级
 * @param seqId
 * @return
 */
function getCodeNameSecretFunc(cellData, recordIndex, columIndex){
  var classCode = this.getCellData(recordIndex,"secret");
  var classNo = "RMS_SECRET";
  var urls = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getCodeName.act";
  var rtJsons = getJsonRs(urls , "classCode=" + classCode + "&classNo=" + classNo);
  if(rtJsons.rtState == '0'){
    return "<center>" + rtJsons.rtData + "<center>";
  }else{
    alert(rtJson.rtMsrg);
  }
}

/**
 * 紧急等级
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function getUrgencyFunc(cellData, recordIndex, columIndex){
  var classCode = this.getCellData(recordIndex,"urgency");
  var classNo = "RMS_URGENCY";
  var urls = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getCodeName.act";
  var rtJsons = getJsonRs(urls , "classCode=" + classCode + "&classNo=" + classNo);
  if(rtJsons.rtState == '0'){
    return "<center>" + rtJsons.rtData + "<center>";
  }else{
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

/**
 * 批量删除
 * @return
 */
function deleteAll() {
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要删除案卷，请至少选择其中一个。");
    return;
  }
  if(!confirmDelAll()) {
    return ;
  }  
  var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/deleteContact.act";
  var rtJson = getJsonRs(url, "sumStrs=" + idStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function getChecked() {
  var ids= ""
  var checkArray = $$('input');
  for(var i = 0 ; i < checkArray.length ; i++){
    if(checkArray[i].name == "deleteFlag" && checkArray[i].checked ){
      if(ids != ""){
        ids += ",";
      }
      ids += checkArray[i].value;
    }
  }
   return ids;
 }

/**
 * 获取ID串
 * @return
 */
function getSeqIdStr() {
  var ids= ""
  var checkArray = $$('input');
  for(var i = 0 ; i < checkArray.length ; i++){
    if(checkArray[i].name == "deleteFlag"){
      if(ids != ""){
        ids += ",";
      }
      ids += checkArray[i].value;
    }
  }
   return ids;
 }

/**
 * 导出文件档案
 * 
 * @return
 */
function exportRmsFile(){
  var seqIdStr = getSeqIdStr();
  location = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/exportToExcel.act?seqIdStr="+seqIdStr;
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
