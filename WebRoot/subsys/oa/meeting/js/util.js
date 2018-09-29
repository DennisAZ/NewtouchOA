/**
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件

 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"290\">";
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
 * 申请人
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function mProposerFunc(mProposer){
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/getUserName.act";
  var rtJson = getJsonRs(url, "userId=" + mProposer);
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
function meetingRoomNameFunc(mRoom){
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/getMeetingRoomName.act";
  var rtJson = getJsonRs(url, "seqId=" + mRoom);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 会议状态
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function mStatusFunc(mStatus){
  var status = "待批";
  if(mStatus == '0'){
    status = "待批";
    return status;
  }else if(mStatus == '1'){
   status = "已批准";
   return status;
 }else if(mStatus == '2'){
   status = "进行中";
   return status;
 }else if(mStatus == '3'){
   status = "未批准";
   return status;
 }else if(mStatus == '4'){
   status = "已结束";
   return status;
 }else{
   return status;
 }
}
