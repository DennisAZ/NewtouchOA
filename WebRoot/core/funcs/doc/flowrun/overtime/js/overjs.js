var pageCount = 1 ;
var defaultShowLen = 10;
var queryParam = "";
var cfgs = null;
var pageMgr = null;
/**
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件

 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"260\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}
function queryData(){
  var beginDate = document.getElementById("statrTime");
  var endDate = document.getElementById("endTime");
  //alert(beginDate+"::"+endDate);
  var beginInt;
  var endInt;
  var beginArray = beginDate.value.split("-");
  var endArray = endDate.value.split("-");
  for(var i = 0 ; i<beginArray.length; i++){
   // alert(beginArray[i]);
    beginInt = parseInt(" " + beginArray[i]+ "",10);  
    endInt = parseInt(" " + endArray[i]+ "",10);
    if((beginInt - endInt) > 0){
      alert("起始日期不能大于结束日期!");
      endDate.focus();
      endDate.select();
      return false;
    }else if(beginInt - endInt<0){
      break;
    }  
  }
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('listContainer').style.display = "";
    if( $('flowRunOpTab')){
       $('flowRunOpTab').style.display = "";
     }
    $('msrg').style.display = "none";
  }else{
    WarningMsrg('没有检索到数据!', 'msrg','info');
    $('msrg').style.display = "";
    if( $('flowRunOpTab')){
      $('flowRunOpTab').style.display = "none";
    }
    $('listContainer').style.display = "none";
  }
}
/**
 * 流程名称描画器

 * @param cellData  代表前面的数字（流水号）不能为空 
 * @param recordIndex (流水号)这行数据 必须有
 * @param columIndex // 这列数据 必须有
 * @return
 */
function flowNameRender(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  var flowId = this.getCellData(recordIndex,"flowId");
  var html = "<center><a href='javascript:;' onclick='formView(" + runId + ","+flowId+")'>"+ cellData+"</a></center>";
  return html;
}
/**
 * 步骤名称描画器
 */
function flowRunPrcsRender(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  var flowId = this.getCellData(recordIndex,"flowId");
  var prcsName = this.getCellData(recordIndex,"prcsName");
  var runName = this.getCellData(recordIndex,"runName");
  var html = "";
  var titleTmp = prcsName + "(" + runId + ")" + runName;
  var html = "<center><a href='javascript:;' onclick='flowView("+ runId +"," +flowId +",\"" + titleTmp +"\",\""+sortId+"\" ,\""+skin+"\")'>" + cellData + "</a><center>";
  return html;
}
/**
 * 流程号居中
 */
function flowNumber(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  var html = "";
  html = "<center>"+runId+"</center>";
  return html;
}
/**
 * 主办人状态
 */
function personStatusRender(cellData, recordIndex, columIndex){
  var nowState = this.getCellData(recordIndex,"nowState");
  var prcsFlag = this.getCellData(recordIndex,"prcsFlag");
  var html = "";
  html ="<center><span class='color"+prcsFlag+"'>■</span>"+nowState+"</center>";
  return html;
}
hostPersonRender
/**
 * 主办人
 */
function hostPersonRender(cellData, recordIndex, columIndex){
  var userName = this.getCellData(recordIndex,"userName");
  var html = "";
  html ="<center>"+userName+"</center>";
  return html;
}
/**
 * 办理时限
 */
function handleTimeRender(cellData, recordIndex, columIndex){
  var timeOut = this.getCellData(recordIndex,"timeOut");
  var html = "";
  html ="<center>"+timeOut+"</center>";
  return html;
}

/**
 * 超时时间
 */
function overTimeRender(cellData, recordIndex, columIndex){
  var timeExcept = this.getCellData(recordIndex,"timeExcept");
  var html = "";
  if (!timeExcept) {
    timeExcept = '';
  }
  html ="<center>"+ timeExcept.replace('null','0') + "</center>";
  return html;
}
function doInit(flag){
  if (flag == 1) {
    skinObjectToSpan(flowrun_overtime_overtotal);
  } else {
    skinObjectToSpan(flowrun_overtime_overquery);
  }
  loadFlowType() ;
  var url = contextPath+moduleSrcPath+'/act/YHWorkOverTimeAct/getWorkOverTimeList.act';
  cfgs = {
      dataAction: url,
      container: "listContainer",
      paramFunc: getParam,
      moduleName:"doc",
      showRecordCnt: true,
      colums: [
         {type:"data", name:"runId", text:"流水号",width: "7%",render:flowNumber},
         {type:"data", name:"runName", text:"工作名称/文号", width: "25%", render:flowNameRender},
         {type:"data", name:"prcsName", text:"步骤名称", width:"25%", render:flowRunPrcsRender},
         {type:"data", name:"nowState", text:"主办人状态", width: "8%",render:personStatusRender},
         {type:"data", name:"userName", text:"主办人", width: "8%",render:hostPersonRender},
         {type:"data", name:"timeOut", text:"办理时限",width:"7%",render:handleTimeRender},
         {type:"data", name:"timeExcept", text:"超时时间",width:"20%",render:overTimeRender},
         {type:"hidden", name:"opFlag"},
         {type:"hidden", name:"prcsTime",dataType:"dateTime"},
         {type:"hidden", name:"prcsFlag"}, // 主办人状态
         {type:"hidden", name:"deliverTime",dataType:"dateTime"},
         {type:"hidden", name:"createTime",dataType:"dateTime"},
         {type:"hidden", name:"flowId"}
        ]
    };
  
  
  var beginParameters = {
      inputId:'statrTime',
      property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endTime',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);
  
}
//工作监控查询 
function getParam(){
  var queryParam = $("queryForm").serialize();
  return queryParam;
}
//加载流程
function loadFlowType(){
  var url = contextPath+moduleSrcPath+'/act/YHFlowTypeAct/getFlowTypeJson.act?sortId=' + sortId;
  var json = getJsonRsAsyn(url , "", doFlowTypeJson);
} 
function doFlowTypeJson(json) {
  var rtData = json.rtData;   
  for(var i = 0 ;i < rtData.length ; i ++) {      
    var opt = document.createElement("option") ;      
    opt.value = rtData[i].seqId ;      
    opt.innerHTML = rtData[i].flowName ;      
    $('flowList').appendChild(opt) ;                        
  }    
}
function removeAllChildren(parentNode){
  parentNode = $(parentNode);
  while(parentNode.firstChild){
    var oldNode = parentNode.removeChild(parentNode.firstChild);
    oldNode = null;
  }
}


//清空时间组件
function empty_date(){
  $("statrTime").value="";
  $("endTime").value="";
}
function isDate(dataValue){
  var year = "";
  var month = "";
  var day = "";
  var offset = 0;
  var len = dataValue.length;
  var i = dataValue.indexOf("-");
  year = dataValue.substr(offset, (i-offset));
  offset = i + 1;
  if (offset > len){
    return false;
  }
  if (i){
    i = dataValue.indexOf("-",offset);
    day = dataValue.substr(sum,(len-offset));
  }
  if(year == ""||month == ""||day == ""){
    return false;
  }
  return true;
}
//全选实现功能
function checkAll(field) {
  var deleteFlags = document.getElementsByName("run_select") ;
  for(var i = 0 ; i < deleteFlags.length ; i++) {
  deleteFlags[i].checked = field.checked ;
  }
}
//显示单选框提示信息
function check_select(){
  var select_check = document.getElementsByName("run_select") ;
  var select_str = "";
  for(var i = 0; i < select_check.length ; i ++){
  if(select_check[i].checked){
    select_str += select_check[i].value + "," ; 
    }
  }
}

function selectLen(selectValue){
  $("pageIndex").value = "1";
  loadData(1, selectValue , queryParam);
}
function setUserChange(value) {
  if (value == '5') {
    $('setUser').show();
  } else {
    $('setUser').hide();
  }
  
}
function exportCsv() {
  var url = contextPath+moduleSrcPath+'/act/YHWorkOverTimeAct/exportCsv.act?' + getParam();
  window.open(url);
}