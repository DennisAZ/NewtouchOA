/**
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件

 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"280\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}

function WarningMsrgLong(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"410\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=" <h4 class=\"title\">信息</h4>"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}

/**
 * 隐藏显示控件
 * 
 * @param cntrlId
 * @return
 */
function showCntrl(cntrlId) {
  if ($(cntrlId)) {
    if ($(cntrlId).style.display) {
      $(cntrlId).style.display = '';
    } else {
      $(cntrlId).style.display = 'none';
    }
  }
}



/**
 * 用户名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function getUserName(userId){
  var url = contextPath + "/yh/subsys/oa/hr/moneyMonth/act/YHHrMoneyMonthAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + userId);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function My_Submit(){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  window.location= contextPath + "/subsys/oa/hr/moneyMonth/manage.jsp?year="+year+"&month="+month+"&seqId="+seqIdStr;
}

function set_month(index){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  if(parseInt(month,10)+index<=0){
    year = parseInt(year)-1;
    month = 12;
  }else if(parseInt(month,10)+index>12){
    year = parseInt(year)+1;
    month = 1;
  }else{
    month = parseInt(month,10)+index;
  }
  window.location=contextPath + "/subsys/oa/hr/moneyMonth/manage.jsp?year="+year+"&month="+month+"&seqId="+seqIdStr;
}

function set_year(index){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  if(parseInt(year)<=2000){
    year = parseInt(year);
  }else if(parseInt(year)>=2049){
    year = parseInt(year);
  }else{
    year = parseInt(year)+parseInt(index);
  }
  window.location=contextPath + "/subsys/oa/hr/moneyMonth/manage.jsp?year="+year+"&month="+month+"&seqId="+seqIdStr;
}

function getPersonal(){
  var array = new Array("加班总时长","值班次数","外出次数","请假总时长","出差总时长","年休假");
  var flag = 0;
  var str = "";
    var table=new Element('table',{ "width":"50%","class":"TableBlock","align":"center"})
    .update("<tbody id='tbody'><tr class='TableHeader'>"
      + "<td nowrap width='30%' align='center'>个人考勤统计</td>"
      + "<td nowrap width='20%' align='center'>操作</td></tr><tbody>");
    $('listDiv').appendChild(table);
    var strs = "";
    var showStr = "";
    var flag = "";
    var tdFlag = "0";
    for(var i = 0; i < array.length; i++){
     if(i == 0){
       flag = exitsMoneyMonth(userIdStr);
       tdFlag = "1";
       valueStr = getOverTime(userIdStr) + " 小时";
     }
     if(i == 1){
       tdFlag = "0";
       valueStr = getDuty(userIdStr) + " 次";
     }
     if(i == 2){
       tdFlag = "0";
       valueStr = getAttendOut(userIdStr) + " 次";
     }
     if(i == 3){
       tdFlag = "0";
       if(flag == "0"){
         valueStr = getAttendLeaveHour() + " 小时";
       }else{
         valueStr = getAttendLeaveHour()-getOverTimeMoney() + " 小时";
       }
     }
     if(i == 4){
       tdFlag = "0";
       valueStr = getAttendEvectionHour(userIdStr) + " 小时";
     }
     if(i == 5){
       tdFlag = "0";
       valueStr = "请年休假" + getAnnualLeaveDays(userIdStr)+ "天,年休假剩余" + getAnnualOverplus(userIdStr) + "天";
     }
     var trColor = (i % 2 == 0) ? "TableLine1" : "TableLine2";
     var tr = new Element('tr',{'class':trColor});   
      table.firstChild.appendChild(tr);
          str = "<td align='center'>" + array[i] + "</td><td align='center'><a href=" 
          + "javascript:checkScore('" + i + "');"
          + ">" + valueStr + "</a>&nbsp;&nbsp;"
          if(tdFlag == "1"){
            if(flag == "0"){
              str += "<a href=" 
                + "javascript:leaveConvert();"
                + ">请假折抵</a>&nbsp;";
              str += "<a href=" 
                + "javascript:overtimeMoney();"
                + ">申请加班费</a>&nbsp;&nbsp;";
            }else{
              var status = getMoneyMonthStatus(userIdStr);
              if(status == "0"){
                str += "<font color='red'>已请假折抵</font>&nbsp;";
              }else{
                str += "<font color='red'>已申请加班费</font>&nbsp;";
              }
            }
           }
          str += "</td>";
      tr.update(str);
   }
}

function leaveConvert(userIdStr){
  var overtimeHour = getOverTime(userIdStr);  //加班总时长
  var leaveHour =  getAttendLeaveHour(userIdStr);//请假总时长
  var url = contextPath + "/yh/subsys/oa/hr/moneyMonth/act/YHHrMoneyMonthAct/addMoneyMonthLeave.act";
  var rtJson = getJsonRs(url, "leaveHour="+leaveHour+"&overtimeHour="+overtimeHour+"&year="+yearStr+"&month="+monthStr+"&userIdStr="+userIdStr);
  if(rtJson.rtState == "0"){
    window.location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function overtimeMoney(userIdStr){
  var overtimeMoney = getOverTimeMoney(userIdStr); //加班工资数
  var dutyMoney = getDutyMoney(userIdStr);         //值班工资数
  var url = contextPath + "/yh/subsys/oa/hr/moneyMonth/act/YHHrMoneyMonthAct/addMoneyMonth.act";
  var rtJson = getJsonRs(url, "overtimeMoney="+overtimeMoney+"&dutyMoney="+dutyMoney+"&year="+yearStr+"&month="+monthStr+"&userIdStr="+userIdStr);
  if(rtJson.rtState == "0"){
    window.location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
  
}

function getMoneyMonthStatus(userIdStr){
  var url = contextPath + "/yh/subsys/oa/hr/moneyMonth/act/YHHrMoneyMonthAct/getMoneyMonthStatus.act";
  var rtJson = getJsonRs(url, "year="+yearStr+"&month="+monthStr+"&userIdStr="+userIdStr);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function exitsMoneyMonth(userIdStr){
  var url = contextPath + "/yh/subsys/oa/hr/moneyMonth/act/YHHrMoneyMonthAct/exitsMoneyMonth.act";
  var rtJson = getJsonRs(url, "year="+yearStr+"&month="+monthStr+"&userIdStr="+userIdStr);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getOverTimeMoney(){
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendLeaveAct/getOverTimeHour.act";
  var rtJson = getJsonRs(url, "year="+yearStr+"&month="+monthStr+"&userIdStr="+userIdStr);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return insertKiloSplit(data, 2);
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getOverTime(userIdStr){
  var url = contextPath + "/yh/custom/attendance/act/YHOvertimeRecordAct/getOverTimeHour.act";
  var rtJson = getJsonRs(url, "year="+yearStr+"&month="+monthStr+"&userIdStr="+userIdStr);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return insertKiloSplit(data, 2);
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getDuty(userIdStr){
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/getAttendDutyCount.act";
  var rtJson = getJsonRs(url, "year="+yearStr+"&month="+monthStr+"&userIdStr="+userIdStr);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getDutyMoney(userIdStr){
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/getAttendDutyMoney.act";
  var rtJson = getJsonRs(url, "year="+yearStr+"&month="+monthStr+"&userIdStr="+userIdStr);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return insertKiloSplit(data, 2);
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getAttendOut(userIdStr){
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendOutAct/getAttendOutCount.act";
  var rtJson = getJsonRs(url, "year="+yearStr+"&month="+monthStr+"&userIdStr="+userIdStr);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getAttendLeaveHour(userIdStr){
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendLeaveAct/getAttendLeaveHour.act";
  var rtJson = getJsonRs(url, "year="+yearStr+"&month="+monthStr+"&userIdStr="+userIdStr);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getAttendEvectionHour(userIdStr){
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendEvectionAct/getAttendEvectionHour.act";
  var rtJson = getJsonRs(url, "year="+yearStr+"&month="+monthStr+"&userIdStr="+userIdStr);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getAnnualOverplus(userIdStr){
  var requestUrl = contextPath + "/yh/custom/attendance/act/YHAnnualLeaveAct/getAnnualOverplus.act";
  var rtJson = getJsonRs(requestUrl, "userIdStr="+userIdStr);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData.overplusDays;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getAnnualLeaveDays(userIdStr){
  var requestUrl = contextPath + "/yh/custom/attendance/act/YHAnnualLeaveAct/getAnnualOverplus.act";
  var rtJson = getJsonRs(requestUrl, "userIdStr="+userIdStr);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData.leaveDays;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

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

function registerTypeFunc(cellData, recordIndex, columIndex){
  var registerType =  this.getCellData(recordIndex,"registerType");
  if(registerType == "1"){
    return "<center>第1次登记</center>";
  }
  if(registerType == "2"){
    return "<center>第2次登记</center>";
  }
  if(registerType == "3"){
    return "<center>第3次登记</center>";
  }
  if(registerType == "4"){
    return "<center>第4次登记</center>";
  }
  if(registerType == "5"){
    return "<center>第5次登记</center>";
  }
  if(registerType == "6"){
    return "<center>第6次登记</center>";
  }
  return "";
}

function fillTimeFunc(cellData, recordIndex,columInde) {
  var fillTime =  this.getCellData(recordIndex,"fillTime");
  return "<center>" +fillTime.substr(0,10)+ "</center>";
}
function assessingTimeFunc(cellData, recordIndex,columInde) {
  var str = "";
  if(cellData){
    str = cellData.substr(0,10);
  }
  return "<center>" + str + "</center>";
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
 * 申请人

 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function proposerFunc(cellData, recordIndex, columIndex){
  var str = "";
  var mProposer = this.getCellData(recordIndex,"proposer");
  var url = contextPath + "/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/getUserName.act";
  var rtJson = getJsonRs(url, "userId=" + mProposer);
  if (rtJson.rtState == "0") {
    str = rtJson.rtData
    return "<center>" + str + "</center>";
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 审批人

 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function assessingOfficerFunc(cellData, recordIndex, columIndex){
  var mProposer = this.getCellData(recordIndex,"assessingOfficer");
  var url = contextPath + "/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/getUserName.act";
  var rtJson = getJsonRs(url, "userId=" + mProposer);
  if (rtJson.rtState == "0") {
    if(rtJson.rtData == ""){
      return "<center><font color=green>批量审批</font></center>";
    }else{
      return "<center>" + rtJson.rtData + "</center>";
    }
    
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 详细信息
 * @param seqId
 * @return
 */
function approvalDetailFunc(seqId){
  var URL = contextPath + "/subsys/oa/fillRegister/approval/approvalDetail.jsp?seqId=" + seqId;
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
    msg = '确认要审批通过此补登记申请吗？请填写审批意见：';
  }else{
    msg = '确认要驳回此补登记申请吗？请填写驳回理由：';
  }
  $("confirm").innerHTML = "<font color=red>" + msg + "</font>";
  $("seqId").value = seqId;
  $("assessingStatus").value = assessingStatus;
  ShowDialog();
    
}

function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
    return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" ></center>";
}

function checkSelf(){
  var allCheck = $('checkAlls');
  if(allCheck.checked){
    allCheck.checked = false;
  }
}

function confirmDel() {
  if(confirm("确认要删除该补登记申请吗？")) {
    return true;
  } else {
    return false;
  }
}

function confirmDelAll() {
  if(confirm("确认要删除已选中的补登记申请吗？")) {
    return true;
  } else {
    return false;
  }
}

function deleteSingle(seqId){
  if(!confirmDel()) {
   return ;
  }  
  var url = contextPath + "/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/deleteSingle.act";
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

/**
 * 批量删除
 * @return
 */
function deleteAll() {
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要删除补登记申请，请至少选择其中一个。");
    return;
  }
  if(!confirmDelAll()) {
    return ;
  }  
  var url = contextPath + "/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/deleteAll.act";
  var rtJson = getJsonRs(url, "sumStrs=" + idStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
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
  var mStatus = this.getCellData(recordIndex,"assessingStatus");
  if(mStatus == "0") {
    return "<center><a href=javascript:approvalDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
         + "<a href=javascript:checkupFunc(" + seqId + ",1)>批准</a>&nbsp;"
         + "<a href=javascript:checkupFunc(" + seqId + ",2)>拒绝</a>&nbsp;"
         + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
  }
  if(mStatus == "1") {
    return "<center><a href=javascript:approvalDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
         + "<a href=javascript:checkupFunc(" + seqId + ",2)>拒绝</a>&nbsp;"
         + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
  }
  if(mStatus == "2") {
    return "<center><a href=javascript:approvalDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
         + "<a href=javascript:checkupFunc(" + seqId + ",1)>批准</a>&nbsp;"
         + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
  }
}