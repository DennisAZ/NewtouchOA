
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
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件

 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"250\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
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
      alert("错误,起始时间格式不对，应形如 1999-01-02 14:55:20"); 
      leaveDate1.focus(); 
      leaveDate1.select(); 
      return false; 
    }else{ 
      if(!isValidDateStr(leaveDate1Array[0]) || leaveDate1Array[1].match(re1) == null || leaveDate1Array[1].match(re2) != null){ 
        alert("错误,起始时间格式不对，应形如 1999-01-02 14:55:20"); 
        leaveDate1.focus(); 
        leaveDate1.select(); 
        return false; 
      } 
    } 
  }
  if(leaveDate2.value){
    if(leaveDate2Array.length != 2){ 
      alert("错误,截止时间格式不对，应形如 1999-01-02 14:55:20"); 
      leaveDate2.focus(); 
      leaveDate2.select(); 
      return false; 
    }else{ 
      if(!isValidDateStr(leaveDate2Array[0])||leaveDate2Array[1].match(re1) == null || leaveDate2Array[1].match(re2) != null){ 
        alert("错误,截止时间格式不对，应形如 1999-01-02 14:55:20"); 
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

function confirmDel() {
  if(confirm("确认要删除该手机短信吗？")) {
    return true;
  }else {
    return false;
  }
}

function confirmDelAll() {
  if(confirm("确认要删除所选手机短信吗？")) {
    return true;
  }else {
    return false;
  }
}

function confirmDelSign() {
  if(confirm("确认要删除指定范围内的手机短信记录吗？")) {
    return true;
  }else {
    return false;
  }
}

function confirmSendSign() {
  if(confirm("确认要删除该用户的发送记录吗？")) {
    return true;
  }else {
    return false;
  }
}

function sendTimeStyle(cellData, recordIndex, columIndex){
  return "<div align=\"center\">" + cellData + "</div>";
}

/**
 * 发信人
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function sendName(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var phone = sms3Phone(seqId);
  var userName = personMobilNo(phone);
  var psnName = addressMobilNo(phone);
  if(userName == ""){
    if(psnName == ""){
      return "<center>未知</center>";
    }else{
      return "<center>" + psnName + "</center>";
    }
  }else{
    return "<center>"+userName+"</center>";
  }
}

function personMobilNo(phone){
  var url = contextPath +"/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getPersonPhone.act";
  var rtJson = getJsonRs(url, "mobilNo="+phone);
  if (rtJson.rtState == "0") {
    var userName = rtJson.rtData.userName;
    var mobileNoHidden = rtJson.rtData.mobileNoHidden;
    return userName;
  }
}

function addressMobilNo(phone){
  var url = contextPath +"/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getAddressPhone.act";
  var rtJson = getJsonRs(url, "mobilNo="+phone);
  if (rtJson.rtState == "0") {
    var psnName = rtJson.rtData.psnName;
    return psnName;
  }
}

function mobilNo(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var phone = sms3Phone(seqId);
  var url = contextPath + "/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getPersonPhone.act";
  var rtJson = getJsonRs(url, "mobilNo="+phone);
  if (rtJson.rtState == "0") {
    var userName = rtJson.rtData.userName;
    var mobilNoHidden = rtJson.rtData.mobilNoHidden;
    if(userName != "" && mobilNoHidden == "1"){
      return "<center>不公开</center>";
    }else{
      var phone = sms3Phone(seqId);
      return "<center>" + phone + "</center>";
    }
  }
}

function sms3Phone(seqId){
  var url = contextPath + "/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getSms3MobilNo.act";
  var rtJson = getJsonRs(url, "seqId="+seqId);
  if (rtJson.rtState == "0") {
    var phone = rtJson.rtData.phone;
      return phone;
  }
}

/**
 * subject 描画事件
 */
function bindsubjectRenderAction(cellData, recordIndex, columIndex) {
  var cntrl = $("sub_" + recordIndex + "_" + columIndex);
  var diaId = this.getCellData(recordIndex,"seqId");
  var userId = this.getCellData(recordIndex,"userId");
  cntrl.observe("click", commentEdit.bind(this,diaId,userId));
}

function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
  return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" ></center>";
}

function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<center><a href=\"javascript:doDelete("+seqId+");\">删除</a></center>";
}

/**
 * 手机短信接收记录
 * 
 * @return
 */
function exportDept(){
  location = contextPath + "/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/exportToExcel.act";
}