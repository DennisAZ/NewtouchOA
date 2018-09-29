var loginUserId = null;
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
function getParam(){
  queryParam = $("queryForm").serialize();
  return queryParam;
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
  return "<input type=checkbox name=run_select value='"+ runId +"'>";
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
 * 流程名称描画器
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function flowNameRender(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  var flowId = this.getCellData(recordIndex,"flowId");
  var runName = this.getCellData(recordIndex,"runName");
  if(cellData.length>30){
    cellData = cellData.substring(0,14);
    cellData =cellData+"...";
  }
  var html = "<a href='javascript:;' title="+runName+" onclick='formView2(" + runId + "," + flowId + ")'>" + cellData + "</a>";
  return html;
}
/**
 * 流程状态描画器
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function flowStautsRender(cellData, recordIndex, columIndex){
  var endTime = this.getCellData(recordIndex,"endTime");
  var html = "";
  if(!endTime){
    html = "<center><font color=red>执行中</font></center>";
  }else{
    html = "<center><font>已结束</font></center>";
  }
  return html;
}


function openCommentWindow(runId , flowId) {
  var url = contextPath + moduleContextPath + "/flowrun/list/comment.jsp?runId=" + runId + "&flowId=" + flowId;
  openDialog(url,  600, 400);
}



function findIdIsIn(id , strs){
  var exts = strs.split(",");
 
  for(var i = 0 ;i < exts.length ; i++){
    if(!exts[i]){
      continue;
    }
    var tmp = exts[i];
    if(tmp == id){
      return true;
    }
 
  return false;
}
  }
function set_stuats(){
  //alert("set_stuats");
};

//清空时间组件
function empty_date(){
  $("startTime").value="";
  $("endTime").value="";
}
function isDate(dataValue){
  var year = "";
  var month = "";
  var day = "";
  var offset = 0;
  var len = dataValue.length;
  var i = dataValue.indexOf("-");
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
//全选实现功能function checkAll(field) {
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

function doFlowTypeJson(json) {
  var rtData = json.rtData;   
  for(var i = 0 ;i < rtData.length ; i ++) {      
    var opt = document.createElement("option") ;      
    opt.value = rtData[i].seqId ;      
    opt.innerHTML = rtData[i].flowName ;      
    $('flowList').appendChild(opt) ;                        
  }  
}
//加载流程
function loadFlowType(){
  var url = contextPath+moduleSrcPath+'/act/YHWorkQueryAct/getFlowTypeJson.act?sortId=' + sortId;
  var json = getJsonRsAsyn(url , "", doFlowTypeJson);
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
/**
 * 委托
 * @return
 */
function trustAction(){
  var par = arguments[2];
  var aPar =  par.split(":");
  var flowType = aPar[0];
  var sPar = aPar[1];
  var page = "others";
  if (flowType == '2') {
    page="othersfree";
  }
  var url = contextPath + moduleContextPath + "/flowrunRec/list/others/"+page+".jsp?"+ sPar;
  myleft=(screen.availWidth-700)/2;
  mytop=(screen.availHeight-450)/2;
  window.open(url,"others","status=0,toolbar=no,menubar=no,width=700,height=450,location=no,scrollbars=yes,resizable=no,left="+myleft+",top="+mytop);
}
function restore() {
  var par = arguments[2];
  var url = contextPath +moduleSrcPath+"/act/YHFlowManageAct/restore.act";
  var json = getJsonRs(url , par);
  if (json.rtState == '0') {
    alert(json.rtMsrg);
    pageMgr.search();
  }
}
/**
 * 
 * @param par
 * @return
 */
function callBack(){ 
  var par = arguments[2];
  msg="下一步骤尚未接收时可收回至本步骤重新办理，确认要收回吗？"; 
  if(window.confirm(msg)) { 
    var url = contextPath+moduleSrcPath+'/act/YHMyWorkAct/callBack.act'; 
    var json = getJsonRs(url , par); 
    if (json.rtState == '0') { 
      alert(json.rtMsrg); 
      pageMgr.search();
    } else { 
      alert(json.rtMsrg); 
    } 
  } 
}
function handleWork(par){
  var url = contextPath + moduleContextPath + "/flowrunRec/list/inputform/index.jsp?" + par;
  location =  url;
}
function edit(){
  var par = arguments[2];
  var url = contextPath + moduleContextPath + "/flowrunRec/list/inputform/edit.jsp?" + par;
  myleft=(screen.availWidth-800)/2;
  window.open(url ,"edit_run","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=600,left="+myleft+",top=50");
}
function exportZip() {
  var runIds = "";
  var deleteFlags = document.getElementsByName("run_select") ;
  for(var i = 0 ; i < deleteFlags.length ; i++) {
    if(deleteFlags[i].checked){
      if(runIds){
        runIds += ",";
      }
      runIds += deleteFlags[i].value;
    }
  }
  if (!runIds) {
    alert(tooltipMsg8) 
    return ;
  }
  var url = contextPath +moduleSrcPath+"/act/YHFlowExportAct/exportFlowZip.act?runIdStr=" + runIds;
  window.open(url);
}
function delWorkFlow() {
  var runIds = "";
  var deleteFlags = document.getElementsByName("run_select") ;
  for(var i = 0 ; i < deleteFlags.length ; i++) {
    if(deleteFlags[i].checked){
      if(runIds){
        runIds += ",";
      }
      runIds += deleteFlags[i].value;
    }
  }
  if (!runIds) {
    alert(tooltipMsg2) 
    return ;
  }
  if(!confirm(tooltipMsg3)) {
    return ;
  }
  var url = contextPath +moduleSrcPath+"/act/YHFlowManageAct/delWorkFlow.act";
  var json = getJsonRs(url, "runIdStr=" + runIds) ;
  if (json.rtState == '0') {
    alert(json.rtMsrg);
    pageMgr.search();
  }
}
function endWorkFlow() {
  var runIds = "";
  var deleteFlags = document.getElementsByName("run_select") ;
  for(var i = 0 ; i < deleteFlags.length ; i++) {
    if(deleteFlags[i].checked){
      if(runIds){
        runIds += ",";
      }
      runIds += deleteFlags[i].value;
    }
  }
  if (!runIds) {
    alert(tooltipMsg4) 
    return ;
  }
  if(!confirm(tooltipMsg5)) {
    return ;
  }
 var url = contextPath +moduleSrcPath+"/act/YHFlowManageAct/endWorkFlow.act";
 var json = getJsonRs(url, "runIdStr=" + runIds) ;
 if (json.rtState == '0') {
   alert(json.rtMsrg); 
   pageMgr.search();
 }
}
function focus(event , ee, par) {
  if(window.confirm(tooltipMsg6)) {
    var url = contextPath + "/yh/core/funcs/doc/flowrunRec/act/YHFlowManageAct/focus.act";
    var json = getJsonRs(url, par) ;
    if (json.rtState == '0') {
      alert(json.rtMsrg); 
      pageMgr.search();
    }
  }
}
function calFocus(event , ee, par) {
  if(window.confirm(tooltipMsg7)) {
    var url = contextPath +moduleSrcPath+"/act/YHFlowManageAct/calFocus.act";
    var json = getJsonRs(url, par) ;
    if (json.rtState == '0') {
      alert(json.rtMsrg); 
      pageMgr.search();
    }
  }
}

/**
 * 创建菜单
 * @param event
 * @param seqId
 * @param flowType
 * @param hasEntrust 是否有委托项
 * @return
 */
function createDiv(event , seqId , flowType , runId , hasEntrust , hasDelete , par){
  var menuData = new Array();
  if (hasEntrust) {
    menuData.push({ name:'<div  style="padding-top:5px;margin-left:10px">委托<div>',action:trustAction ,extData:flowType + ":" + par});
  }
  menuData.push({ name:'<div style="padding-top:5px;margin-left:10px">导出<div>',action:exportAction,extData:par});
  if (hasDelete) {
    menuData.push({ name:'<div  style="padding-top:5px;margin-left:10px">删除<div>',action:delAction,extData:runId});
  }
  var divStyle = {border:'1px solid #69F',width:'90px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:$('more-' + seqId) , menuData:menuData , attachCtrl:true},divStyle);
  menu.show(event);
}
function attachRender(cellData, recordIndex, columIndex) {
  var attachStr = this.getCellData(recordIndex,"attach2");
  if (attachStr) {
    var attach = eval(attachStr);
    var str = "";
    if (attach.length > 0) {
      for (var i = 0 ;i < attach.length ;i ++) {
        var attachment = attach[i];
        str += addAttachment(attachment);
      }
      return str;
    }
  } 
  return '无'; 
}
/**
 * 流程操作描画器

 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function opRender(cellData, recordIndex, columIndex){
  var opStr = "";
  var runId = this.getCellData(recordIndex,"runId");
  var flowId = this.getCellData(recordIndex,"flowId");
  var flowType = this.getCellData(recordIndex,"flowType");
  var flowName = this.getCellData(recordIndex,"flowName");
  var runName = this.getCellData(recordIndex,"runName");
  var prcsId = this.getCellData(recordIndex,"prcsId");
  var flowPrcs = this.getCellData(recordIndex,"flowPrcs");
  var hasComPriv = this.getCellData(recordIndex , "hasComPriv");
  var hasHandler = this.getCellData(recordIndex , "hasHandler");
  var hasOther = this.getCellData(recordIndex , "hasOther");
  
  var hasFocus = this.getCellData(recordIndex , "hasFocus");
  var hasCalFocus = this.getCellData(recordIndex , "hasCalFocus");
  var hasEdit = this.getCellData(recordIndex , "hasEdit");
  var hasCallback = this.getCellData(recordIndex , "hasCallback");
  var endTime = this.getCellData(recordIndex,"endTime");
  var hasRestore = 0;
  if(endTime && isOaAdmin){
    hasRestore = 1;
  }
  var par = "runId=" + runId + "&flowId=" + flowId + "&prcsId=" + prcsId + "&flowPrcs=" + flowPrcs + "&sortId="+ sortId + "&skin=" + skin;
  opStr = "<a href='javascript:;' onclick='"
    + "flowView("+ runId +"," + flowId +",\"\",\"" + sortId +"\",\"" + skin +"\" , 1)"
    +"'>流程图</a>&nbsp;";
  if (hasComPriv == 1) {
    opStr += "<a href='javascript:void(0)' title=\"添加点评意见\" onclick='openCommentWindow("+runId+" , "+flowId+")'>点评</a>&nbsp;";
  }
  if (hasHandler == 1) {
    opStr += "<a  href=\"javascript:handleWork('" + par + "');\" >办理</a>&nbsp;";
  }
  var menuItemsStr = "";
  if (hasFocus == 1) {
    menuItemsStr += "attention|" ;
  }
  if (hasCalFocus == 1) {
    menuItemsStr += "unattention|" ;
  }
  if (hasOther == 1) {
    menuItemsStr += "delegate|" ;
  }
  if (hasRestore == 1) {
    menuItemsStr += "revert|" ;
  }
  if (hasCallback == 1) {
    menuItemsStr += "reclaim|" ;
  }
  if (hasEdit == 1) {
    menuItemsStr += "eidt|" ;
  }
  if (menuItemsStr) {
    opStr += "<a id=\"menu_" + recordIndex + "\" href='javascript:;' onmouseover=\"showMenuDiv(\'menu_" + recordIndex + "\',\'" + menuItemsStr + "\',\'" + flowType + "\',\'" + runId + "\',\'" + par + "\')\">更多<img align='absMiddle' src='"+ imgPath +"/menu_arrow_down.gif'/></a>";
  }
  return opStr;
}
/**
 * 显示更多菜单
 * @param cntrlId
 * @param menuItemsStr
 * @param flowType
 * @param runId
 * @param par
 * @return
 */
function showMenuDiv(cntrlId,menuItemsStr, flowType , runId ,par){
  var menuItems ={
    delegate:{ name:'<div style=\"padding-top:5px;margin-left:5px;height:16px\">委托<div>',action:trustAction ,extData:flowType + ":" + par}
    ,reclaim:{ name:'<div style=\"padding-top:5px;margin-left:5px;height:16px\">收回<div>',action:callBack,extData:par}
    ,revert:{ name:'<div style=\"padding-top:5px;margin-left:5px;height:16px\">恢复执行<div>',action:restore,extData:par}
    ,eidt:{ name:'<div style=\"padding-top:5px;margin-left:5px;height:16px\">编辑<div>',action:edit,extData:par}
   ,attention:{ name:'<div style=\"padding-top:5px;margin-left:5px;height:16px\">关注<div>',action:focus,extData:par}
   ,unattention:{ name:'<div style=\"padding-top:5px;margin-left:5px;height:16px\">取消关注<div>',action:calFocus,extData:par}
  };
  var array = new Array();
  var menuNames = menuItemsStr.split("|");
  for(var i = 0 ,j = 0; i < menuNames.length ; i ++ ){
    var name = menuNames[i];
    if(name){
      array[j] = menuItems[name];
      j++;
    }
  }
  var menu = new Menu({bindTo: cntrlId , menuData:array , attachCtrl: true});
  menu.show();
}
/**
 * 添加附件
 * attachment:{attachmentName:'', attachmentId:'',ext:''}
 */
function addAttachment(attachment){
  var imgSrc = "";
  var ext = attachment.ext;
  var imgStr = getAttachImage(ext);
  var priv = attachment.priv;
  var tmp = attachment.attachmentName;
  if (tmp.length > 10){
    tmp = tmp.substring(0 , 10);
    tmp += "...";
  }
  var str = "<a title='"+attachment.attachmentName+"' href='javascript:' onmouseover='createDiv(event  ,this , \""+ attachment.attachmentId +"\" , \""+ attachment.attachmentName +"\" , \""+ attachment.ext +"\", \""+priv+"\")'><img src='" + imgStr + "'/>&nbsp;" + tmp + "</a><br>";
  return str;
}
/**
 * 创建右建菜单
 * 
 */
function createDiv(event , node , attachId , attachName , ext , priv){
  var down = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">下载<div>',action:downAction ,extData: ""};
  var save = { attachmentId:attachId , attachmentName: attachName ,name:'<div style="padding-top:5px;margin-left:10px">转存<div>',action:saveAction,extData: ""};
  var read = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">阅读<div>',action:readAction,extData: ""};
  var print = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">阅读<div>',action:printAction,extData: ""};
  var preview = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">预览<div>',action:previewAction,extData: ""};
  var play = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">播放<div>',action:playAction,extData: ""};
  var menuD = [];
  var pr = priv.split(",");
  if ( isMedia(attachName) || isVideo(attachName)){
    menuD.push( play );
    menuD.push( down );
    menuD.push( save );
  }else if( findIsIn(docEnd , ext ) ){
    if (pr[0] == '1') {
      menuD.push( down );
      menuD.push( save );
    }
    if (pr[1] == '1' ) {
      menuD.push( print );
    } else {
      menuD.push( read );
    }
   } else {
     menuD.push( down );
     menuD.push( save );
   }
  var divStyle = {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:node , menuData:menuD , attachCtrl:true},divStyle);
  menu.show(event);
}
/**
 * 查看后缀ext是否在exts中
 */
function findIsIn(exts , ext){
  for(var i = 0 ;i < exts.length ; i++){
    var tmp = exts[i];
    if(tmp == ext){
      return true;
    }
  }
  return false;
}

