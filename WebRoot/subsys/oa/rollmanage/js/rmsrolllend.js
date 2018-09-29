/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function optsLend(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var statusFlag = this.getCellData(recordIndex,"status");
  if(statusFlag == ""){
    statusFlag = 0;
  }
  return "<center><a href='rollfile.jsp?seqId="+seqId+"'>查看文件</a></center>";
}

/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function optsLendFile(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var rollId = this.getCellData(recordIndex,"rollId");
  return "<center><a href=\"javascript:selectManage("+rollId+","+seqId+");\">借阅</a></center>";
}

function confirmLend() {
  if(confirm("确认要借阅该文件吗？")) {
    return true;
  } else {
    return false;
  }
}

function confirmLendAll() {
  if(confirm("确认要借阅已选中的文件吗？")) {
    return true;
  } else {
    return false;
  }
}



function checkAll(field) {
  var deleteFlags = document.getElementsByName("deleteFlag");
  for(var i = 0; i < deleteFlags.length; i++) {
    deleteFlags[i].checked = field.checked;
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
 * 批量借阅
 * @return
 */
function lendAll(seqId) {
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要借阅文件，请至少选择其中一个。");
    return;
  }
  if(!confirmLendAll()) {
    return ;
  }  
  var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsLendAct/rmsLendAllRoll.act";
  var rtJson = getJsonRs(url, "rollIdStr=" + idStrs);
  if (rtJson.rtState == "0") {
    location = contextPath + "/subsys/oa/rollmanage/rolllend/lend.jsp?seqId="+seqId;
  } else {
    alert(rtJson.rtMsrg); 
  }
}