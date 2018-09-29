
/**
 * 字段居中显示
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function equipmentCenterFunc(cellData, recordIndex, columIndex){
  return cellData;
}

function confirmDel() {
  if(confirm("确认要删除该设备么？")) {
    return true;
  } else {
    return false;
  }
}

/**
 * 删除单个设备记录
 * @param seqId
 * @return
 */
function deleteSingle(seqId){
  if(!confirmDel()) {
   return ;
  }  
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingEquipmentAct/deleteSingle.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 设备状态
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function equipmentStatusFunc(cellData, recordIndex, columIndex){
  var equipmentStatus = this.getCellData(recordIndex,"equipmentStatus");
  if(equipmentStatus == "1"){
    return "可用";
  }else{
    return "不可用";
  }
}

/**
 * 同类设备名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function groupYnFunc(cellData, recordIndex, columIndex){
  var groupYn = this.getCellData(recordIndex,"groupYn");
  var groupNo = this.getCellData(recordIndex,"groupNo");
  if(groupYn == "1"){
  	return getGroupYnEquipmentFunc(groupNo);
   
  }else{
  	return "";
  }
}

/**
 * 获取小编码表内容--同类设备名称
 * @param groupNo
 * @return
 */
function getGroupYnEquipmentFunc(groupNo){
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingEquipmentAct/getCodeName.act";
  var rtJson = getJsonRs(url, "classCode=" + groupNo);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 会议室名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function mRoomFunc(cellData, recordIndex, columIndex){
  var mrId = this.getCellData(recordIndex,"mrId");
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/getMeetingRoomName.act";
  var rtJson = getJsonRs(url, "seqId=" + mrId);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
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
  return "<a href=javascript:editEquipmentFunc(" + seqId + ")>编辑</a>&nbsp;"
  + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>";
}
/**
 * 编辑
 * @param seqId
 * @return
 */
function editEquipmentFunc(seqId){
	window.location.href = contextPath + "/subsys/oa/meeting/equipment/editEquipment.jsp?seqId=" + seqId;
}


