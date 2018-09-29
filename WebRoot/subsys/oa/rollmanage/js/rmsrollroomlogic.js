
function getDeptNameFunc(cellData, recordIndex, columIndex){
  var deptId = this.getCellData(recordIndex,"deptId");
    var urls = contextPath + "/yh/core/funcs/person/act/YHPersonAct/getDeptName.act";
    var rtJsons = getJsonRs(urls , "deptId=" +  cellData);
    if(rtJsons.rtState == '0'){
      return "<center>"+rtJsons.rtData+"<center>";
    }
}

function roomCodeFunc(cellData, recordIndex, columIndex){
  return "<center>"+cellData+"</center>";
}

function roomNameFunc(cellData, recordIndex, columIndex){
  return "<center>"+cellData+"</center>";
}

function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<center><a href='modify.jsp?seqId="+seqId+"'>编辑</a>&nbsp;&nbsp;<a href=\"javascript:deleteSingle("+seqId+");\">删除</a></center>";
}

function deleteSingle(seqId){
  if(!confirmDel()) {
   return ;
  }  
  var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollRoomAct/deleteSingle.act";
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
   var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollRoomAct/deleteAll.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     window.location.reload();
   } else {
     alert(rtJson.rtMsrg); 
   }
}

function confirmDel() {
  if(confirm("确认要删除该项卷库吗？")) {
    return true;
  } else {
    return false;
  }
}

function confirmDelAll() {
  if(confirm("确认要删除所有卷库吗？")) {
    return true;
  } else {
    return false;
  }
}