/**
 * 清除类容
 * 
 * @param ctrlId
 * @param ctrlIdDesc
 * @return
 */
function Clear() {
  var args = $A(arguments);
  for ( var i = 0; i < args.length; i++) {
    var cntrl = $(args[i]);
    if (cntrl) {
      if (cntrl.tagName.toLowerCase() == "td"
          || cntrl.tagName.toLowerCase() == "div"
          || cntrl.tagName.toLowerCase() == "span") {
        cntrl.innerHTML = '';
      } else {
        cntrl.value = '';
      }
    }
  }
}
/**
 * 加载流程
 * @return
 */
function loadFlowPrcs(cntrlId){
  if(!cntrlId){
    return;
  }
  var url = contextPath+moduleSrcPath+'/act/YHWorkDestroyAct/getFlow.act?sortId=' + sortId;
  var rtJson = getJsonRs(url);
  var selectObj  = $(cntrlId);
  if(rtJson.rtState == "0"){
    for(var i = 0 ; i < rtJson.rtData.length ; i ++){
      var obj = rtJson.rtData[i];
      var optStr = "<option value=" + obj.flowId + ">" + obj.flowName + "</option>";
      selectObj.insert(optStr,'bottom');
    }
    if(selectObj.options[0]){
      selectObj.options[0].selected = true;
    }
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 工作查询
 * @return
 */
function doQuery(cntrlId){
  var form = $(cntrlId);
  if( check(form)){
    return;
  }
  var param = form.serialize();
  location = contextPath + moduleContextPath + "/flowrun/log/search.jsp?" + param;
}
function exportExecl(cntrlId){
  var form = $(cntrlId);
  if( check(form)){
    return;
  }
  var param = form.serialize();
  location = contextPath +moduleSrcPath+"/act/YHWorkLogAct/export.act?" + param;
}
/**
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件
 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ,w) {
 
  var width = "";
  if(w){
    width = w;
  }else{
    width = "300";
  }
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"" + width + "\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}
function getParam(){
  queryParam = $("queryForm").serialize();
  return queryParam;
}

/**
 * 取得用户姓名
 * @return
 */
function getUserNameById(userId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/getUserName.act";
  var rtJson = getJsonRs(url , "userId=" +  userId);
  if(rtJson.rtState == '0'){
    return rtJson.rtData;
  }
}
/**
 * 取得PrcsName
 * @return
 */
function getPrcsName(flowId,flowPrcs){
  var url = contextPath+moduleSrcPath+'/act/YHWorkLogAct/getPrcsName.act';
  var rtJson = getJsonRs(url , "flowId=" +  flowId + "&flowPrcs=" + flowPrcs);
  if(rtJson.rtState == '0'){
    return rtJson.rtData;
  }else{
    return "-";
  }
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
function checkAll(field) {
  var deleteFlags = document.getElementsByName("run_select") ;
  for(var i = 0 ; i < deleteFlags.length ; i++) {
    deleteFlags[i].checked = field.checked ;
  }
}
/**
 * 删除日志
 * @return
 */
function doDelete(){
  var ids = "";
  var deleteFlags = document.getElementsByName("run_select") ;
  for(var i = 0 ; i < deleteFlags.length ; i++) {
    if(deleteFlags[i].checked){
      if(ids){
        ids += ",";
      }
      ids += deleteFlags[i].value;
    }
  }
  if(!ids){
    alert("请选择一条要删除的日志！");
    return;
  }
  if(!confirm("确认要删除所选日志！")) {
    return ;
  }
  var url = contextPath+moduleSrcPath+'/act/YHWorkLogAct/deleteLog.act?seqId=' + ids;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    alert("日志删除成功！");
  }else{
    alert("日志删除失败：" + rtJson.rtMsrg);
  }
  location.reload();
}
function checkNum(str){
  var re=/\D/;
  return str.match(re);
}
function check(form){
  if(checkNum($(form).runId.value)){
    alert("流水号必须为数值！");
    $(form).runId.focus();
    $(form).runId.select();
    return true;
  }
  var ipAddrss = $F("ipAddrss");
  if (ipAddrss) {
    var reg = new RegExp("^([1-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.){2}([1-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])", "g");
    if (!reg.test(ipAddrss)) {
      alert("ip地址不合法!");
      $('ipAddrss').focus();
      $('ipAddrss').select();
      return true;
    }
  }
  return false;
}
function doDeleteBySearch(cntrlId){
  var form = $(cntrlId);
  if( check(form)){
    return;
  }
  var param = form.serialize();
  var url = contextPath+moduleSrcPath+'/act/YHWorkLogAct/deleteLogBySearch.act?' + param;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var count = rtJson.rtData;
    location = contextPath + moduleContextPath + "/flowrun/log/deleteMsrg.jsp?count=" + count;
  }else{
    alert("删除失败：" + rtJson.rtMsrg);
  }
}
//加载流程
function loadFlowType(){
  var url = contextPath+moduleSrcPath+'/act/YHFlowTypeAct/getFlowTypeJson.act?checkType=1&sortId=' + sortId;
  var json = getJsonRsAsyn(url , "", doFlowTypeJson);
} 
function doFlowTypeJson(json) {
  var rtData = json.rtData;  
  for(var i = 0 ;i < rtData.length ; i ++) {      
    var opt = document.createElement("option") ;      
    opt.value = rtData[i].seqId ;      
    opt.innerHTML = rtData[i].flowName ;      
    $("flowId").appendChild(opt) ;                        
  }    
}
/**
 * 单选按钮
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function checkBoxRender(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "&nbsp;&nbsp;<input type=checkbox name=run_select value='"+ seqId +"'>";
}
/**
 * 用户处理
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function userRender(cellData, recordIndex, columIndex){
  var userId = this.getCellData(recordIndex,"userName");
  var userName = getUserNameById(userId);
  return "<div align=\"center\">" + userName + "</div>";
}
/**
 * 用户处理
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function prcsNameRender(cellData, recordIndex, columIndex){
  var flowPrcsNo = this.getCellData(recordIndex,"flowPrcsNo");
  var flowId = this.getCellData(recordIndex,"flowId");
  var prcsName = "";
  if(flowPrcsNo == 0){
    prcsName = "-";
  }else{
    prcsName = getPrcsName(flowId,flowPrcsNo);
  }
  return "<div align=\"center\">" + prcsName + "</div>";
}