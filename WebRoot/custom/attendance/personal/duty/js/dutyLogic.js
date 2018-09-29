
function confirmDel() {
  if(confirm("确认要删除该考核任务？考核数据将被删除且不可恢复！！")) {
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
  var url = contextPath + "/yh/custom/attendance/act/YHDutyAct/deleteSingle.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function checkForm(){
  var type1 = "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$";
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2);
  var beginDate = $("beginDate").value.trim();
  var endDate = $("endDate").value.trim();
  if($("dutyDesc").value.trim()==""){ 
    alert("值班原因不能为空！");
    $("dutyDesc").focus();
    $("dutyDesc").select();
    return (false);
  }
  if($("dutyTime") == ""){
    alert("值班时间不能为空！");
    $("dutyTime").focus();
    $("dutyTime").select();
    return false;
  }
  if(beginDate == ""){
    alert("值班开始时间不能为空！");
    $("beginDate").focus();
    $("beginDate").select();
    return false;
  }
  if(endDate == ""){
    alert("值班结束时间不能为空！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  if($("dutyTime").value){
    if(!isValidDateStr($("dutyTime").value)){
      alert("值班时间日期格式不对,应形如 2011-01-01");
      $("dutyTime").focus();
      $("dutyTime").select();
      return false;
    }
  }
  if(beginDate.match(re1) == null || beginDate.match(re2) != null){
    alert("值班开始时间格式不对，应形如 12:12:12");
    $("beginDate").focus();
    $("beginDate").select();
    return false;
  }

  if(endDate.match(re1) == null || endDate.match(re2) != null){
    alert("值班结束时间格式不对，应形如 12:12:12");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  if($("beginDate").value.trim() > $("endDate").value.trim()){
    alert("值班开始时间大于结束时间！");
    $("beginDate").focus();
    $("beginDate").select();
    return false;
  }

  if($("leaderId").value == ''){
    alert("审批人员不能为空！");
    $("leaderId").focus();
    $("leaderId").select();
    return false;
  }  
  //return chekDate(document.getElementById("beginTime"));
   return (true);
}

function attendDuty(){
  if($("beginDate").value && $("endDate").value){
    if($("beginDate").value <= $("endDate").value){
      $("attendDuty").style.display = '';
      getAttendDuty();
    }
  }
}

function getAttendDuty(){
  var beginDate = $("beginDate").value;
  var endDate = $("endDate").value;
  var beginTime = $("dutyTime").value;
  var url = contextPath + "/yh/custom/attendance/act/YHOvertimeRecordAct/getOverTimeInfo.act";
  var rtJson = getJsonRs(url, "beginTime="+beginTime+"&beginDate="+beginDate+"&endDate="+endDate);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    $("dutyType").value = data.type;
    $("hour").value = data.diff;
    $("dutyMoney").value = data.money;
    if(data.type == "1"){
      overtimeType = "平时";
    }
    if(data.type == "2"){
      overtimeType = "周末";
    }
    if(data.type == "3"){
      overtimeType = "节假日";
    }
    var hour = "";//"&nbsp;加班小时：" + data.diff;
    var overtimeTypeStr = "&nbsp;值班类型：" + overtimeType;
    var money = "";//"&nbsp;总加班费:" + data.money; 
    $("attendDuty").innerHTML = "<font style='color:red'>" + hour + money + overtimeTypeStr +"</font>";
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function showTimeBegin(){
  showTime("beginDate", false, attendDuty);
}

function showTimeEnd(){
  showTime("endDate", false, attendDuty);
}
