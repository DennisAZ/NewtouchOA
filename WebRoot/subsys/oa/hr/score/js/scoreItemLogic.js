
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
 * 获取最新插入EXAM_PAPER表中的seqId
 * @return
 */
function getPaperSeqId(){
  var url = contextPath + "/yh/subsys/oa/examManage/act/YHExamPaperAct/getPaperSeqId.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 修改
 * @param seqId
 * @return
 */
function doEdit(seqId){
  location = contextPath + "/subsys/oa/hr/score/modify.jsp?seqId="+seqId;
}

/**
 * 修改考核指标集明细-项目
 * @param seqId
 * @return
 */
function doEditItem(seqId,groupId){
  var itemName = $("itemName_"+seqId).value;
  var min = $("min_"+seqId).value;
  var max = $("max_"+seqId).value;
  if(itemName.trim() == ""){ 
    alert("考核项目不能为空！！！");
    $("itemName_"+seqId).focus();
    $("itemName_"+seqId).select();
    return;
  }
  if(min.trim() == ""){ 
    alert("最小值不能为空！！！");
    $("min_"+seqId).focus();
    $("min_"+seqId).select();
    return;
  }
  if(max.trim() == ""){ 
    alert("最大值不能为空！！！");
    $("max_"+seqId).focus();
    $("max_"+seqId).select();
    return;
  }
  if(min){
    if(!isNumbers(min)){
     alert("分值范围,应形如  100.00");
     $("min_"+seqId).focus();
     $("min_"+seqId).select();  
     return;
    }
  }
  if(max){
    if(!isNumbers(max)){
     alert("分值范围,应形如  100.00");
     $("max_"+seqId).focus();
     $("max_"+seqId).select();  
     return;
    }
  }
  if(min >= max){
    alert("分值范围输入不正确！！！");
    $("min_"+seqId).focus();
    $("min_"+seqId).select(); 
    return;
  }
  //var pars = Form.serialize($('form1'));
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreItemAct/updateScoreItem.act?seqId="+seqId;
  var rtJson = getJsonRs(url,"min="+min+"&max="+max+"&itemName="+encodeURIComponent(itemName)+"&groupId="+groupId);
  if(rtJson.rtState == "0"){
    window.location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
}


function confirmDel() {
  if(confirm("您真的要删除该条选项吗？注意：该操作不可恢复!")) {
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
function deleteItem(seqId){
  if(!confirmDel()) {
   return ;
  }  
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreItemAct/deleteItem.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}


function newItemWindow(url,width,height){
  var myleft = (screen.availWidth - width)/2;
  var mytop = (screen.availHeight - height)/2;
  window.open(url, "examManage", 
      "height=500,width=800,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" + mytop + ", left=" + myleft + ", resizable=yes");
}


