
/**
 * 日期操作函数
 */
//--返回当前日期 格式为'2009-12-28'
function curdate(date){
  var ds ="";
  ds += date.getFullYear(); 
  ds += "-" ; 
  ds += (date.getMonth() + 1) ;
  ds += "-" ;
  ds += date.getDate(); 
  return ds;
}
//--返回当前日期,形如 2009年1月1日
function formatDate(date){
  var ds ="";
  ds += date.getFullYear() + ""; 
  ds += "年" ; 
  ds += (date.getMonth() + 1) ;
  ds += "月" ;
  ds += date.getDate(); 
  ds += "日" ;
  return ds;
}
/**
 * 返回日期所代表的星期数，如 六
 */
function getWeek(date){ 
  var day = date.getDay();
  switch(day){
    case 0:
    return "日";
    case 1:
    return "一";
    case 2:
    return "二";
    case 3:
    return "三";
    case 4:
    return "四";
    case 5:
    return "五";
    case 6:
    return "六";
  }
}

//-- 返回形如 1999年2月
function formatDateShort1(date){
  var ds ="";
  ds += date.getFullYear(); 
  ds += "年" ; 
  ds += (date.getMonth() + 1) ;
  ds += "月" ;
  return ds;
}
//-- 返回形如 2月1日
function formatDateShort2(date){
  var ds ="";
  ds += (date.getMonth() + 1) ;
  ds += "月" ;
  ds += date.getDate(); 
  ds += "日" ;
  return ds;
}
//-- 返回形如 2008年
function formatDateShort3(date) {
  var ds ="";
  ds += date.getFullYear(); 
  ds += "年" ; 
  return ds;
}
//-- 返回形如 2008
function formatDateShort4(date) {
  var ds ="";
  ds += date.getFullYear(); 
  return ds;
}
//--当前时间 10:09:21
function sysTime(date){
  var ds = "";
  var flex = ":";
  ds += date.getHours() + flex ; 
  ds += date.getMinutes() + flex ; 
  ds += date.getSeconds(); 
  return ds;
}
function getIp(){
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getIpAdd.act"; 
  var rtJson = getJsonRs(url); 
  return rtJson.rtData.ip;
}
function runName(){ //工作名称/文号
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getRunName.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    return rtJson.rtData[0].runName; 
  }else{
    return "";
  }
}  
function getUser(){//当前用户姓名
  var url = contextPath + "/yh/core/funcs/person/act/YHPersonAct/getLoginUser.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    return rtJson.rtData;
  }else{
    return "";
  }
  //YHPerson person = (YHPerson) request.getSession().getAttribute("LONGIN_USER");
}
function beginTime(){ //流程开始日期
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getBeginTime.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    return rtJson.rtData[0].beginTime; 
  }else{
    return "";
  }
} 
function runId(){ //流水号
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getRunId.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
      if(rtJson.rtData[0].runId == "null"){
        return "";
      }else{
        return rtJson.rtData[0].runId; 
      }
  }else{
    return ""; 
  }
} 

function endTime(){ //流程开始日期
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getEndTime.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
      if(rtJson.rtData[0].endTime == "null"){
       return "";
      }else{
        return rtJson.rtData[0].endTime; 
      }
  }else{
    return "";
  }
} 

function deptNameLong(){//当前用户部门（长名称）
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getDeptNameLong.act?";
  var rtJson = getJsonRs(url);
  var result = "";
  if(rtJson.rtState == "0"){
    for(var i = 0; i < rtJson.rtData.length; i++){
      if(result != "")
        result += "/";
      result += rtJson.rtData[i].deptName; 
    }
  }
  return result;
}

function deptNameShort(){//当前用户部门（短名称）
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getDeptNameShort.act?";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    return rtJson.rtData.deptName;
  }else{
    return "";
  }
}
//-- DATE1=DATE2 返回0,DATE1>DATE2 返回1,DATE1<DATE2 返回-1
function compareDate(dateStr1,dateStr2) {
  var strs1 = dateStr1.split("-");
  var strs2 = dateStr2.split("-");

  var y1 = strs1[0] ;//年1
  var m1 = strs1[1] ;//月1
  var d1 = strs1[2] ;//日1
  
  var y2 = strs2[0] ;//年2
  var m2 = strs2[1] ;//月2
  var d2 = strs2[2] ;//日2

  if(y1 > y2)
     return 1;
  else if(y1 < y2)
     return -1;
  else
  {
    if(m1 > m2)
       return 1;
    else if(m1 < m2)
       return -1;
    else
    {
      if(d1 > d2)
         return 1;
      else if(d1 < d2)
         return -1;
      else
         return 0;
    }
  }
}
//-- time1=time2 返回0,time1>time2 返回1,time1<time2 返回-1
function compareTime(time1,time2){
  var times1 = time1.split(":");
  var hour1 = times1[0];
  var min1 = times1[1];
  var sec1 = times1[2];
  
  var times2 = time2.split(":");
  var hour2 = times2[0];
  var min2 = times2[1];
  var sec2 = times2[2];

  if(hour1 > hour2)
     return 1;
  else if(hour1 < hour2)
     return -1;
  else
  {
    if(min1 > min2)
       return 1;
    else if(min1 < min2)
       return -1;
    else
    {
      if(sec1 > sec2)
         return 1;
      else if(sec1 < sec2)
         return -1;
      else
         return 0;
    }
  }
}
//-- dateTime1=dateTime2 返回0,dateTime1>dateTime2 返回1,dateTime1<dateTime2 返回-1
function compareDateTime(dateTime1,dateTime2){
    if(dateTime1 == null || dateTime1.length == 0 || dateTime2 == null || dateTime2.length == 0 )
       return -1;
    var dateTime1Arry = dateTime1.split(" ");
    var dateTime2Arry = dateTime2.split(" ");
    
    if(compareDate(dateTime1Arry[0],dateTime2Arry[0])==1)
       return 1;
    else if(compareDate(dateTime1Arry[0],dateTime2Arry[0])==0)
    {
        if(compareTime(dateTime1Arry[1],dateTime2Arry[1])==1)
           return 1;
        else if(compareTime(dateTime1Arry[1],dateTime2Arry[1])==0)
           return 0;
        else
           return -1;
    }
    else
       return -1;
}
function getUserPriv(domId){    //权限列表
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getSelectData.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
  var select = document.getElementById(domId);
  var len = select.options.length;
  for ( var i = 0; i < len; i++) {
    select.remove(0);
  }
  select.value = "0";
  for(var i = 0; i < rtJson.rtData.length; i++) {
    var option = document.createElement("option");
    option.value = rtJson.rtData[i].seqId;
    option.innerHTML = rtJson.rtData[i].privName;
    select.appendChild(option);
  }
  }else{
  alert(rtJson.rtMsrg); 
  }
}

function getPerson(domId){    //人员列表
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getPerson.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
  var select = document.getElementById(domId);
  var len = select.options.length;
  for ( var i = 0; i < len; i++) {
    select.remove(0);
  }
  select.value = "0";
  for(var i = 0; i < rtJson.rtData.length; i++) {
    var option = document.createElement("option");
    //alert(rtJson.rtData[i].seqId);
    option.value = rtJson.rtData[i].seqId;
    option.innerHTML = rtJson.rtData[i].userName;
    select.appendChild(option);
  }
  }else{
  alert(rtJson.rtMsrg); 
  }
}
function getDepartment(domId){    //部门列表
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getDepartment.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
  var select = document.getElementById(domId);
  var len = select.options.length;
  for ( var i = 0; i < len; i++) {
    select.remove(0);
  }
  select.value = "0";
  for(var i = 0; i < rtJson.rtData.length; i++) {
    var option = document.createElement("option");
    option.value = rtJson.rtData[i].seqId;
    option.innerHTML = rtJson.rtData[i].deptName;
    select.appendChild(option);
  }
  }else{
  alert(rtJson.rtMsrg); 
  }
}

function deptParent(domId){   //部门主管（上级部门）
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getDeptParent.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
  var select = document.getElementById(domId);
  var len = select.options.length;
  for ( var i = 0; i < len; i++) {
    select.remove(0);
  }
  select.value = "0";
  for(var i = 0; i < rtJson.rtData.length; i++) {
    var option = document.createElement("option");
    //alert(rtJson.rtData[i].seqId);
    option.value = rtJson.rtData[i].seqId;
    option.innerHTML = rtJson.rtData[i].userName;
    select.appendChild(option);
  }
  }else{
  alert(rtJson.rtMsrg); 
  }
}
function deptLocalInput(){ //部门主管（本部门 input）
  var url =contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getDeptLocalInput.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    return rtJson.rtData[0].userName; 
  }else{
    return "";
  }
}
function deptParentInput(){//部门主管（上级部门  input）
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getDeptParentInput.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    return rtJson.rtData[0].userName; 
  }else{
    return "";
  }
}
function deptFirstInput(){ //部门主管（ 一级部门 input）
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getDeptFirstInput.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    return rtJson.rtData[0].userName; 
  }else{
    return "";
  }
}  
function deptFirst(domId){   //部门主管（一级部门）
  var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getDeptFirst.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
  var select = document.getElementById(domId);
  var len = select.options.length;
  for ( var i = 0; i < len; i++) {
    select.remove(0);
  }
  select.value = "0";
  for(var i = 0; i < rtJson.rtData.length; i++) {
    var option = document.createElement("option");
    option.value = rtJson.rtData[i].seqId;
    option.innerHTML = rtJson.rtData[i].userName;
    select.appendChild(option);
  }
  }else{
  alert(rtJson.rtMsrg); 
  }
}
function deptLocal(domId){ //本部门主管

  // var url = "/yh/core/funcs/person/act/YHUserPrivAct/getAutoData.act?seqId=" + seqId;
   var url = contextPath + moduleSrcPath + "/util/YHFlowFormUtilAct/getDeptLocal.act";
   var rtJson = getJsonRs(url);
   if(rtJson.rtState == "0"){
     //bindJson2Cntrl(rtJson.rtData);
     //alert(rsText);
   var select = document.getElementById(domId);
   var len = select.options.length;
   for ( var i = 0; i < len; i++) {
     select.remove(0);
   }
   select.value = "0";
   for(var i = 0; i < rtJson.rtData.length; i++){
     var option = document.createElement("option");
     //option.no = rtJson.rtData[i].privNo;
     option.value = rtJson.rtData[i].seqId;
     option.innerHTML = rtJson.rtData[i].userName;
     select.appendChild(option);
   }
   }else{
   alert(rtJson.rtMsrg); 
   }
 }

function getFormJsonRs(url,seqId){
  var rtJson = getJsonRs(url,"seqId=" + seqId);
  if(rtJson.rtState == "0"){
    //alert(rtJson.rtData);
    return rtJson.rtData;
  }else{
      alert(rtJson.rtMsrg); 
      return null;
  }
}
function data_picker(obj,item_str)
{
  var id = obj.getAttribute("ID");
  var URL=contextPath + "/core/funcs/workflow/flowrun/list/inputform/selectData/index.jsp?ctrl=" + id + "&itemStr=" + item_str;
  var openWidth = 800;
  var openHeight = 500;
  openDialog(URL,  openWidth, openHeight);
}
var wordRetNameArray = null
function selectWord(title  , splitStr) {
  wordRetNameArray = $(title);
  if (wordRetNameArray) {
    var url = contextPath + "/core/funcs/doc/docword/wordSelect/MultiWordSelect.jsp?splitStr=" + encodeURIComponent(splitStr);
    openDialogResize(url,  520, 400);
  }
}
function data_fetch(obj,run_id,item_str){
  var dataSrc=obj.getAttribute("DATA_TABLE");
  var dataField=obj.getAttribute("DATA_FIELD");
  var URL= contextPath + moduleSrcPath + "/act/YHFormDataFetch/dataFetch.act";
  var args="dataSrc="+dataSrc+"&runId="+run_id+"&dataField="+dataField+"&itemStr="+item_str;
  var json = getJsonRs(URL,args);
  if (json.rtState == "1") {
     //回填数据
    if(json.rtMsrg.substring(0,6)=="error:"){
      alert(json.rtMsrg.substring(6,json.rtMsrg.length)); return;
    }
    return ;
  }
  var value_array = json.rtData;
  if(!value_array) return;
  var item_array=item_str.split(",");
  for(i=0;i<item_array.length-1;i++){      
    if(item_array[i]=="")
       continue;

    var x= $(item_array[i]);
    if (!x) {
      continue;
    }
    switch(x.tagName){
      case "INPUT":
        if(x.type=="text"){
          x.value=value_array[item_array[i]];
        }else if(x.type=="checkbox"){
          if(value_array[item_array[i]]=="on")
              x.checked=true;
          else
            x.checked=false;
        }       
        break;
      case "SELECT":
        for(k=0;k<x.length;k++)
          if(x.options[k].value == value_array[item_array[i]])
            break;
        if(k!=x.length)
          x.selectedIndex=k;
        break;
      case "TEXTAREA":        
          x.innerText=value_array[item_array[i]].replace(/<br>/ig,"\r\n");
          break;
      case "BUTTON":
        break;
    }
  }
}