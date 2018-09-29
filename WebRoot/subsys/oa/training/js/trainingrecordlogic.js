
/**
 * 字段居中显示
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function recordCenterFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
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
      return "<center><a href=javascript:recordDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
      + "<a href=javascript:doEdit(" + seqId + ")>编辑</a>&nbsp;"
      + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";

}

function doEdit(seqId){
  location = contextPath + "/subsys/oa/training/record/modify.jsp?seqId="+seqId;
}

/**
 * 详细信息
 * @param seqId
 * @return
 */
function recordDetailFunc(seqId){
  var URL = contextPath + "/subsys/oa/training/record/recorddetail.jsp?seqId=" + seqId;
  //openDialogResize(URL,'820', '500');
  newRecordWindow(URL,'800','600');
}

/**
 * 打开新窗口  
 * @param url
 * @param width
 * @param height
 * @return
 */
function newRecordWindow(url,width,height){
  var myleft = (screen.availWidth - width)/2;
  var mytop = (screen.availHeight - height)/2;
  window.open(url, "meeting", 
      "height=600,width=800,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" + mytop + ", left=" + myleft + ", resizable=yes");
}

/**
 * 受训人
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffUserIdFunc(cellData, recordIndex, columIndex){
  var staffUserId = this.getCellData(recordIndex,"staffUserId");
  var url = contextPath + "/yh/subsys/oa/training/act/YHTrainingRecordAct/getUserName.act";
  var rtJson = getJsonRs(url, "userId=" + staffUserId);
  if (rtJson.rtState == "0") {
    return "<center>" + rtJson.rtData + "</center>";
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function trainingCostFunc(cellData, recordIndex, columIndex){
  var trainingCost = this.getCellData(recordIndex,"trainningCost");
  return "<center>" + insertKiloSplit(trainingCost,2) + "</center>";
  //return "<center>" + trainingCost + "</center>";
  
}

function confirmDel() {
  if(confirm("确定要删除该培训记录吗？删除后将不可恢复")) {
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
  var url = contextPath + "/yh/subsys/oa/training/act/YHTrainingRecordAct/deleteSingle.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
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

function confirmDelAll() {
  if(confirm("确认要删除所选培训记录吗？")) {
    return true;
  } else {
    return false;
  }
}

/**
 * 批量删除
 * @return
 */
function deleteAll() {
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要删除培训记录，请至少选择其中一条。");
    return;
  }
  if(!confirmDelAll()) {
    return ;
  }  
  var url = contextPath + "/yh/subsys/oa/training/act/YHTrainingRecordAct/deleteAll.act";
  var rtJson = getJsonRs(url, "sumStrs=" + idStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
    return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" ></center>";
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
