
function confirmDel() {
  if(confirm("确定要删除该出国记录吗？删除后将不可恢复")) {
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
  var url = contextPath + "/yh/subsys/oa/abroad/act/YHHrAbroadRecordAct/deleteSingle.act";
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
  if(confirm("确认要删除所选出国记录吗？")) {
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
  alert(idStrs);
  if(!idStrs) {
    alert("要删除出国记录，请至少选择其中一条。");
    return;
  }
  //if(!confirmDelAll()) {
  //  return ;
  //}  
  //var url = contextPath + "/yh/subsys/oa/abroad/act/YHHrAbroadRecordAct/deleteAll.act";
  //var rtJson = getJsonRs(url, "sumStrs=" + idStrs);
  //if (rtJson.rtState == "0") {
  //  window.location.reload();
 // } else {
  //  alert(rtJson.rtMsrg); 
  //}
}

function deleteAll() {
  var idStrs = checkMags('deleteFlag');
  alert(idStrs);
  if(!idStrs) {
    alert("要删除出国记录，请至少选择其中一条。");
    return;
  }
  //if(!confirmDelAll()) {
  //  return ;
  //}  
  //var url = contextPath + "/yh/subsys/oa/abroad/act/YHHrAbroadRecordAct/deleteAll.act";
  //var rtJson = getJsonRs(url, "sumStrs=" + idStrs);
  //if (rtJson.rtState == "0") {
  //  window.location.reload();
 // } else {
  //  alert(rtJson.rtMsrg); 
  //}
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

//平时加班总时长
function getNormalAdd(userId){
  var beginDate = $("beginDate").value;
  var endDate = $("endDate").value;
  var url = contextPath + "/yh/custom/attendance/act/YHOvertimeRecordAct/getTotalAddStat.act";
  var rtJson = getJsonRs(url, "overtimeType=1&beginDate="+beginDate+"&endDate="+endDate+"&userId="+userId);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    if(data > 0){
      $("normalSpan").innerHTML = "(加班总时长"+data+"小时)";
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

//周末加班总时长
function getWeekAdd(userId){
  var beginDate = $("beginDate").value;
  var endDate = $("endDate").value;
  var url = contextPath + "/yh/custom/attendance/act/YHOvertimeRecordAct/getTotalAddStat.act";
  var rtJson = getJsonRs(url, "overtimeType=2&beginDate="+beginDate+"&endDate="+endDate+"&userId="+userId);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    if(data > 0){
      $("weekSpan").innerHTML = "(加班总时长"+data+"小时)";
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

//节假日加班总时长
function getFestivalAdd(userId){
  var beginDate = $("beginDate").value;
  var endDate = $("endDate").value;
  var url = contextPath + "/yh/custom/attendance/act/YHOvertimeRecordAct/getTotalAddStat.act";
  var rtJson = getJsonRs(url, "overtimeType=3&beginDate="+beginDate+"&endDate="+endDate+"&userId="+userId);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    if(data > 0){
      $("festivalSpan").innerHTML = "(加班总时长"+data+"小时)";
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

//平时加班列表
function normal(userId){
  var beginDate = $("beginDate").value;
  var endDate = $("endDate").value;
  var requestURL = contextPath + "/yh/custom/attendance/act/YHOvertimeRecordAct/getOverTimeStat.act";
  var rtJson = getJsonRs(requestURL, "overtimeType=1&beginDate="+beginDate+"&endDate="+endDate+"&userId="+userId);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcsJson = rtJson.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"100%"}).update("<tbody id = 'normal'><tr class='TableHeader'>"
      +"<td nowrap width='40' align='center'>选择</td>"
      +"<td nowrap align='center'>申请时间</td>"
      + "<td nowrap align='center'>审批人员</td>"
      + "<td nowrap align='center'>加班原因</td>"
      + "<td nowrap align='center'>开始时间</td>"
      + "<td nowrap align='center'>结束时间</td>"
      + "<td nowrap align='center'>加班时长(小时)</td></tr></tbody>");
    $('normalDiv').appendChild(table); 
    for(var i = 0; i < prcsJson.length; i++){
      var tr = new Element('tr');
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var leaderId = prcs.leaderId;
      var leaderName = prcs.leaderName;
      var hour = prcs.hour;
      var reason = prcs.reason;
      var statusOptDesc = ""
      var beginDate = prcs.beginTime.substr(0,10) + " " + prcs.beginDate;
      var endDate = prcs.beginTime.substr(0,10) + " " + prcs.endDate;

      if(seqId){  
          tr.update("<td align='center'><input type='checkbox'id='deleteFlag' name='deleteFlag' value="+seqId+"></td>"
              + "<td nowrap align='center'>" + prcs.overtimeTime + "</td>"
              + "<td nowrap align='center'>" + leaderName + "</td>"
              + "<td  align='center'>" + prcs.overtimeDesc + "</td>"
              + "<td nowrap align='center'>" + beginDate + "</td>"
              + "<td nowrap align='center'>" + endDate + "</td>"
              + "<td nowrap align='center'>" + hour + "</td>");
        }
      $('normal').appendChild(tr);
    }
  }else{
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"280"}).update("<tr>"
        + "<td class='msg info'>"
        + "<div class='content' style='font-size:12pt'>无平时时长统计</div>"
        + "</td></tr>"
         );
     $('normalDiv').appendChild(table);
  }
}

//节假日加班列表
function festival(userId){
  var beginDate = $("beginDate").value;
  var endDate = $("endDate").value;
  var requestURL = contextPath + "/yh/custom/attendance/act/YHOvertimeRecordAct/getOverTimeStat.act";
  var rtJson = getJsonRs(requestURL, "overtimeType=3&beginDate="+beginDate+"&endDate="+endDate+"&userId="+userId);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcsJson = rtJson.rtData;
  if(prcsJson.length > 0){
    var table = new Element('table',{"class":"TableList" ,"width":"100%"}).update("<tbody id = 'festival'><tr class='TableHeader'>"
        +"<td nowrap width='40' align='center'>选择</td>"
        +"<td nowrap align='center'>申请时间</td>"
      + "<td nowrap align='center'>审批人员</td>"
      + "<td nowrap align='center'>加班原因</td>"
      + "<td nowrap align='center'>开始时间</td>"
      + "<td nowrap align='center'>结束时间</td>"
      + "<td nowrap align='center'>加班时长(小时)</td></tr></tbody>");
    $('festivalDiv').appendChild(table); 
    for(var i = 0; i < prcsJson.length; i++){
      var tr = new Element('tr');
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var leaderId = prcs.leaderId;
      var leaderName = prcs.leaderName;
      var hour = prcs.hour;
      var reason = prcs.reason;
      var statusOptDesc = ""
      var beginDate = prcs.beginTime.substr(0,10) + " " + prcs.beginDate;
      var endDate = prcs.beginTime.substr(0,10) + " " + prcs.endDate;
      if(seqId){  
          tr.update("<td align='center'><input type='checkbox' id='deleteFlag' name='deleteFlag' value="+seqId+"></td>"
              +"<td nowrap align='center'>" + prcs.overtimeTime + "</td>"
              + "<td nowrap align='center'>" + leaderName + "</td>"
              + "<td nowrap align='center'>" + prcs.overtimeDesc + "</td>"
              + "<td nowrap align='center'>" + beginDate + "</td>"
              + "<td nowrap align='center'>" + endDate + "</td>"
              + "<td nowrap align='center'>" + hour + "</td>");
        }
      $('festival').appendChild(tr);
    }
  }else{
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"280"}).update("<tr>"
        + "<td class='msg info'>"
        + "<div class='content' style='font-size:12pt'>无节假日时长统计</div>"
        + "</td></tr>"
         );
     $('festivalDiv').appendChild(table);
  }
}

//周末加班时长列表
function week(userId){
  var beginDate = $("beginDate").value;
  var endDate = $("endDate").value;
  var requestURL = contextPath + "/yh/custom/attendance/act/YHOvertimeRecordAct/getOverTimeStat.act";
  var rtJson = getJsonRs(requestURL, "overtimeType=2&beginDate="+beginDate+"&endDate="+endDate+"&userId="+userId);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcsJson = rtJson.rtData;
  if(prcsJson.length > 0){
    var table = new Element('table',{"class":"TableList" ,"width":"100%"}).update("<tbody id = 'week'><tr class='TableHeader'>"
        +"<td nowrap width='40' align='center'>选择</td>"
        +"<td nowrap align='center'>申请时间</td>"
      + "<td nowrap align='center'>审批人员</td>"
      + "<td nowrap align='center'>加班原因</td>"
      + "<td nowrap align='center'>开始时间</td>"
      + "<td nowrap align='center'>结束时间</td>"
      + "<td nowrap align='center'>加班时长(小时)</td></tr></tbody>");
    $('weekDiv').appendChild(table); 
    for(var i = 0;i < prcsJson.length; i++){
      var tr = new Element('tr');
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var leaderId = prcs.leaderId;
      var leaderName = prcs.leaderName;
      var hour = prcs.hour;
      var reason = prcs.reason;
      var statusOptDesc = ""
      var beginDate = prcs.beginTime.substr(0,10) + " " + prcs.beginDate;
      var endDate = prcs.beginTime.substr(0,10) + " " + prcs.endDate;

      if(seqId){  
          tr.update("<td align='center'><input type='checkbox'id='deleteFlag' name='deleteFlag' value="+seqId+"></td>"
              +"<td nowrap align='center'>" + prcs.overtimeTime + "</td>"
              + "<td nowrap align='center'>" + leaderName + "</td>"
              + "<td  align='center'>" + prcs.overtimeDesc + "</td>"
              + "<td nowrap align='center'>" + beginDate + "</td>"
              + "<td nowrap align='center'>" + endDate + "</td>"
              + "<td nowrap align='center'>" + hour + "</td>");
        }
      $('week').appendChild(tr);
    }
  }else{
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"280"}).update("<tr>"
        + "<td class='msg info'>"
        + "<div class='content' style='font-size:12pt'>无周末时长统计</div>"
        + "</td></tr>"
         );
     $('weekDiv').appendChild(table);
  }
}

function getUserName(userId){
  var url = contextPath + "/yh/custom/attendance/act/YHOvertimeRecordAct/getUserName.act";
  var rtJson = getJsonRs(url, "userId=" + userId);
  if (rtJson.rtState == "0") {
    $("userName").innerHTML = rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}
