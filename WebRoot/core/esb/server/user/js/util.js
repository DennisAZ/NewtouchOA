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
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"290\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}

function checkBoxRender(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + seqId + "\" onclick=\"checkSelf()\" ></center>";
}

function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<center>"
        + "<a href=javascript:detail(" + seqId + ")>详细信息</a>&nbsp;"
        + "<a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;"
        + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>"
        + "</center>";
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
//取得选中选项
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
 * 打开新窗口  newWindow(URL,'740', '540');
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow(url,width,height){
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  window.open(url, "meeting", 
      "height=" +height + ",width=" + width +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
      + locY + ", left=" + locX + ", resizable=yes");
}

/**
 * 单位员工名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function userNameFunc(cellData, recordIndex, columIndex){
  if(cellData == 'ALL_USERS'){
    return '所有用户';
  }
  if(cellData == 'OTHER_USERS'){
    return '所有用户（不包括发送者）';
  }
  var url = contextPath + "/yh/core/esb/server/user/act/TdUserAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return "<center>" + rtJson.rtData + "</center>";
  } else {
    alert(rtJson.rtMsrg); 
  }
}