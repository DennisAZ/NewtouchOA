/**
 * 字段居中显示
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function scoreCenterFunc(cellData, recordIndex, columIndex){
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
  return "<center><a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;"
      + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
}

function newItemWindow(url,width,height){
  var myleft = (screen.availWidth - width)/2;
  var mytop = (screen.availHeight - height)/2;
  window.open(url, "examManage", 
      "height=500,width=800,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" + mytop + ", left=" + myleft + ", resizable=yes");
}

/**
 * 修改
 * @param seqId
 * @return
 */
function doEdit(seqId){
  location = contextPath + "/subsys/oa/hr/score/group/modify.jsp?seqId="+seqId;
}

function confirmDel() {
  if(confirm("确认要删除该考核指标集明细 ？")) {
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
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreAnswerAct/deleteSingle.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function returnIndex(){
  var seqId = $("groupId").value;
  var groupId = "1";
  location = contextPath + "/subsys/oa/hr/score/group/fillItem.jsp?seqId="+seqId+"&groupId="+groupId;
}