
/**
 * 字段居中显示
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function trainingCenterFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

function tChannelFunc(cellData, recordIndex, columIndex){
  var tChannel = this.getCellData(recordIndex,"tChannel");
  if(tChannel == "0"){
    return "<center>内部培训</center>";
  }else{
    return "<center>渠道培训</center>";
  }
}

function tCourseTypesFunc(cellData, recordIndex, columIndex){
  var str = "";
  if(cellData == "1"){
    str = "面授";
  }
  if(cellData == "2"){
    str = "函授";
  }
  if(cellData == "3"){
    str = "其它";
  }
  
  return "<center>" + str  + "</center>"; 
}

/**
 * 详细信息
 * @param seqId
 * @return
 */
function trainingDetailFunc(seqId){
  var URL = contextPath + "/subsys/oa/training/plan/plandetail.jsp?seqId=" + seqId;
  //openDialogResize(URL,'820', '500');
  newTrainingWindow(URL,'800','600');
}

/**
 * 打开新窗口  
 * @param url
 * @param width
 * @param height
 * @return
 */
function newTrainingWindow(url,width,height){
  var myleft = (screen.availWidth - width)/2;
  var mytop = (screen.availHeight - height)/2;
  window.open(url, "meeting", 
      "height=600,width=800,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" + mytop + ", left=" + myleft + ", resizable=yes");
}

function checkupFunc(seqId, assessingStatus){
  var msg = "";
  if(assessingStatus == "1"){
    msg = '确认要审批通过此计划申请吗？请填写审批意见：';
  }else{
    msg = '确认要驳回此计划申请吗？请填写驳回理由：';
  }
  $("confirm").innerHTML = "<font color=red>" + msg + "</font>";
  $("seqId").value = seqId;
  $("assessingStatus").value = assessingStatus;
  ShowDialog();
    
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
  var mStatus = this.getCellData(recordIndex,"assessingStatus");
  if(mStatus == "0") {
    return "<center><a href=javascript:trainingDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
         + "<a href=javascript:checkupFunc(" + seqId + ",1)>批准</a>&nbsp;"
         + "<a href=javascript:checkupFunc(" + seqId + ",2)>拒绝</a></center>";
  }
  if(mStatus == "1") {
    return "<center><a href=javascript:trainingDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
         + "<a href=javascript:checkupFunc(" + seqId + ",2)>拒绝</a></center>";
  }
  if(mStatus == "2") {
    return "<center><a href=javascript:trainingDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
         + "<a href=javascript:checkupFunc(" + seqId + ",1)>批准</a></center>";
  }
}

function assessingStatusFunc(cellData, recordIndex, columIndex){
  var assessingStatus = this.getCellData(recordIndex,"assessingStatus");
  if(assessingStatus == "0"){
    return "<center>待审批</center>";
  }else if(assessingStatus == "1"){
    return "<center><font color=green>已批准</font></center>";
  }else if(assessingStatus == "2"){
    return "<center><font color=red>未通过</font></center>";
  }
}


/**
 * 打开新窗口  
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow(url,width,height){
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  window.open(url, "meeting", 
      "height=540,width=740,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
      + locY + ", left=" + locX + ", resizable=yes");
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
  
  if(leaveDate1.value){
    if(leaveDate1Array.length != 2){ 
      alert("错误,起始时间格式不对，应形如 2010-01-01 14:55:20"); 
      leaveDate1.focus(); 
      leaveDate1.select(); 
      return false; 
    }else{ 
      if(!isValidDateStr(leaveDate1Array[0]) || leaveDate1Array[1].match(re1) == null || leaveDate1Array[1].match(re2) != null){ 
        alert("错误,起始时间格式不对，应形如 2010-01-01 14:55:20"); 
        leaveDate1.focus(); 
        leaveDate1.select(); 
        return false; 
      } 
    } 
  }
  if(leaveDate2.value){
    if(leaveDate2Array.length != 2){ 
      alert("错误,截止时间格式不对，应形如 2010-01-01 14:55:20"); 
      leaveDate2.focus(); 
      leaveDate2.select(); 
      return false; 
    }else{ 
      if(!isValidDateStr(leaveDate2Array[0])||leaveDate2Array[1].match(re1) == null || leaveDate2Array[1].match(re2) != null){ 
        alert("错误,截止时间格式不对，应形如 2010-01-01 14:55:20"); 
        leaveDate2.focus(); 
        leaveDate2.select(); 
        return false; 
      } 
    } 
  }
  if(leaveDate1.value && leaveDate2.value){
    if(leaveDate1.value > leaveDate2.value){
      alert("错误 起始时间不能大于截至时间！");
      leaveDate1.focus(); 
      leaveDate1.select(); 
      return false;
    }
  }
  return true;
}

function ClearUser(TO_ID, TO_NAME){
  if(TO_ID == "" || TO_ID == "undefined" || TO_ID == null){
    TO_ID = "TO_ID";
  TO_NAME = "TO_NAME";
  }
  document.getElementsByName(TO_ID)[0].value = "";
  document.getElementsByName(TO_NAME)[0].value = "";
  $("tInstitutionName").value = "";
}

