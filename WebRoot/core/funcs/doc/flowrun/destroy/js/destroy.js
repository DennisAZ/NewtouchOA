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
  var url = contextPath+moduleSrcPath+'/act/YHWorkDestroyAct/getFlow.act';
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
  return false;
}
//加载流程
function loadFlowType(cntrlId){
  var url = contextPath+moduleSrcPath+'/act/YHFlowTypeAct/getFlowTypeJsonByManager.act?checkType=1&sortId=' + sortId;
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
 * 工作查询
 * @return
 */
function doQuery(cntrlId){
  var form = $(cntrlId);
  if(check(form)){
    return;
  }
  var param = form.serialize();
  location = contextPath + moduleContextPath + "/flowrun/destroy/search.jsp?" + param;
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
 * 流程流水号描画器
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function flowRunIdRender(cellData, recordIndex, columIndex){
  return "<div align=\"center\"><b>" + cellData + "</b></div>";
}
/**
 * 单选按钮
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function checkBoxRender(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  return "&nbsp;&nbsp;<input type=checkbox name=run_select value='"+ runId +"'>";
}
/**
 * 流程名称描画器
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function flowNameRender(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  var flowId = this.getCellData(recordIndex,"flowId");
  var html = "<a href=\"javascript:;\" onclick=\"formView(" + runId + "," + flowId + ")\">" + cellData + "</a>";
  return html;
}
/**
 * 流程名称描画器
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function flowOpRender(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  var flowId = this.getCellData(recordIndex,"flowId");
  var title = this.getCellData(recordIndex, "flowRunName");
  title = title.replace(/\"/g,"\\\"");
  var html = "<a href='javascript:;' onclick=\"flowView(" + runId + "," + flowId + " , '"+ title +"' , '"+ sortId +"' , '"+ skin +"')\">流程图 </a>&nbsp;&nbsp;"
    + "<a href='javascript:;' onclick=\"recoverById(\'" + runId + "\')\">还原</a>&nbsp;&nbsp;"
    + "<a href='javascript:;' onclick=\"destroyById(\'" + runId + "\')\">销毁</a>&nbsp;&nbsp;";
  return html;
}
/**
 * 直接销毁
 * @param cntrlId
 * @return
 */
function destroyBySearch(cntrlId){
  var form = $(cntrlId);
  if( check(form)){
    return;
  }
  if(window.prompt(tooltipMsg1,"") != "OK"){
    return ;
  }
  var param = form.serialize();
  var url = contextPath+moduleSrcPath+'/act/YHWorkDestroyAct/destroyBysearch.act?' + param;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var count = rtJson.rtData;
    location = contextPath + moduleContextPath + "/flowrun/destroy/deleteMsrg.jsp?skin="+skin+"&sortId="+ sortId +"&count=" + count;
  }else{
    alert(tooltipMsg2 + rtJson.rtMsrg);
  }
} 
/**
 * 直接销毁
 * @param cntrlId
 * @return
 */
function recoverBysearch(cntrlId){
  var form = $(cntrlId);
  if(window.prompt(tooltipMsg3,"") != "OK"){
    return ;
  }
  var param = form.serialize();
  var url = contextPath+moduleSrcPath+'/act/YHWorkDestroyAct/recoverBysearch.act?' + param;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var count = rtJson.rtData;
    location = contextPath + moduleContextPath + "/flowrun/destroy/recoverMsrg.jsp?skin="+skin+"&sortId="+ sortId +"&count=" + count;
  }else{
    alert(tooltipMsg4+ rtJson.rtMsrg);
  }
}
/**
 * 销毁工作
 * @return
 */
function destroy(){
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
    alert(tooltipMsg5);
    return;
  }
  if(!confirm(tooltipMsg6)) {
    return ;
  }
  var url = contextPath+moduleSrcPath+'/act/YHWorkDestroyAct/destroy.act?runId=' + ids;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    alert(tooltipMsg7);
  }else{
    alert(tooltipMsg8 + rtJson.rtMsrg);
  }
  location.reload();
}
/**
 * 销毁工作
 * @return
 */
function destroyById(runId){
  if(!confirm(tooltipMsg9)) {
    return ;
  }
  var url = contextPath+moduleSrcPath+'/act/YHWorkDestroyAct/destroy.act?runId=' + runId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    location.reload();
  }else{
    alert(tooltipMsg10 + rtJson.rtMsrg);
  }
}
/**
 * 还原工作
 * @return
 */
function recover(){
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
    alert(tooltipMsg11);
    return;
  }
  if(!confirm(tooltipMsg12)) {
    return ;
  }
  var url = contextPath+moduleSrcPath+'/act/YHWorkDestroyAct/recover.act?runId=' + ids;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    alert("还原成功！");
  }else{
    alert("还原失败：" + rtJson.rtMsrg);
  }
  location.reload();
}
/**
 * 还原工作
 * @return
 */
function recoverById(runId){
  if(!confirm(tooltipMsg13)) {
    return ;
  }
  var url = contextPath+moduleSrcPath+'/act/YHWorkDestroyAct/recover.act?runId=' + runId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    location.reload();
  }else{
    alert("还原失败：" + rtJson.rtMsrg);
  }
}