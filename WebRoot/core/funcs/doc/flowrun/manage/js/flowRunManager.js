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
/**
 * 委托
 * @return
 */
function trustAction(flowType,sPar){
  var page = "others";
  if (flowType == '2') {
    page="othersfree";
  }
  var url = contextPath + moduleContextPath + "/flowrun/list/others/"+page+".jsp?"+ sPar + "&isManage=true";
  myleft=(screen.availWidth-700)/2;
  mytop=(screen.availHeight-450)/2;
  window.open(url,"others","status=0,toolbar=no,menubar=no,width=700,height=450,location=no,scrollbars=yes,resizable=no,left="+myleft+",top="+mytop);
}
/**
 * 流程流水号描画器
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */

function flowRunIdRender(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  return "<div  align=\"center\" style=\"height:100%\"><b>" + cellData + "</b></div>";
}

/**
 * 流程名称描画器
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function flowRunNameRender(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  var flowId = this.getCellData(recordIndex,"flowId");
  var runName = this.getCellData(recordIndex,"runName");
  if(cellData.length>18){
    cellData= cellData.substring(0,13);
     cellData=cellData+"...";
   }
  var html = "<div title=\"" + runName + "\"><a href='javascript:;' onclick='formView(" + runId + "," + flowId + ")'>" + cellData + "</a></div>";
  return html;
}
/**
 * 流程步骤描画器
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function flowRunPrcsRender(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  var flowId = this.getCellData(recordIndex,"flowId");
  if(cellData.length>5){
    cellData= cellData.substring(0,5);
     cellData=cellData+"...";
   }
  var html = "<div title=\"" + cellData + "\"><a href='javascript:;' onclick=\" flowView(" + runId + "," + flowId + ",'" + cellData + "','"+sortId+"','"+skin+"')\">" + cellData + "</a></div>";
  return html;
}
/**
 * 流程名称描画器
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function flowRunUserRender(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  var userId = this.getCellData(recordIndex,"userId");
  var html = "<div title=\"" + cellData + "\">" + cellData + "</div>";
  return html;
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
  var flowType = this.getCellData(recordIndex,"flowType");
  var title = "";
  var flowTypedesc = "";
  if(flowType == "1"){
    flowTypedesc = "固定流程";
  } else{
    flowTypedesc = "自由流程";
  }
  if(cellData.length>10){
   cellData= cellData.substring(0,10);
    cellData=cellData+"...";
  }
  
  title += "流程类型: " + flowTypedesc + " \n" + "流程名称: "  + cellData;
  var html = "<div title=\"" + title + "\" ><a href='javascript:;' onclick=\"viewGraph('" + flowId + "')\">" + cellData + "</a></div>";
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
  var prcsFlag = this.getCellData(recordIndex,"prcsFlag");
  var html = "";
  if(prcsFlag == "1"){
    html = "<center><img src=\'"+ imgPath +"/email_close.gif\' alt=\'未接收\'></center>";
  }else if(prcsFlag == "2"){
   html = "<center><img src=\'"+ imgPath +"/email_open.gif\' alt=\'已接收\'></center>";
  }else{
    html = "<center><img src=\'"+ imgPath +"/flow_next.gif\' alt=\'已办结\'></center>";
  }
  return html;
}
/**
 * 办理时间描画器
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */

function flowTimeRender(cellData, recordIndex, columIndex){
  var prcsFlag = this.getCellData(recordIndex,"prcsFlag");
  var createTime = this.getCellData(recordIndex,"createTime");
  var prcsTime = this.getCellData(recordIndex,"prcsTime");
  var deliverTime = this.getCellData(recordIndex,"deliverTime");
  var timeOut = this.getCellData(recordIndex,"timeOut");
  var timeOutType = this.getCellData(recordIndex,"timeOutType");
  var html = "";
  var nowDate = new Date();
  var prcsBeginTime = null ;
  var timeStr = "";
  if(prcsTime){
    prcsBeginTime = getDate(prcsTime,"yyyy-MM-dd HH:mm:ss");
  } else{
    prcsBeginTime = getDate(createTime,"yyyy-MM-dd HH:mm:ss");
  }
  if(!prcsBeginTime){
    prcsBeginTime = nowDate;
  }
  var timeUsed = nowDate.getTime() - prcsBeginTime.getTime();
  var day = Math.floor(timeUsed/(24*60*60*1000)); 
  var hour =  Math.floor((timeUsed/(60*60*1000)-day*24)); 
  var min = Math.floor((timeUsed/(60*1000))-day*24*60-hour*60);
  var sec = Math.floor(timeUsed/1000-day*24*60*60-hour*60*60-min*60);
  if(day > 0)
    timeStr += day + "天";
  if(hour > 0)
    timeStr += hour + "时";
  if(min > 0)
    timeStr += min + "分";
  if(sec > 0)
    timeStr += sec + "秒";

  if(prcsFlag == 1 ){
    timeStr = "接收延迟" + timeStr;
  } else{
    timeStr = "用时" + timeStr; 
  }
  
  if(day > 30){
    day = 30;
  }
  width = (day + 1) * 4;
  var timeOutFlag = 0;
  if((prcsFlag == "2" || prcsFlag == "1") && timeOut != "") {  
    if(timeOutType == 0) {
      prcsBeginTime = getDate(prcsTime,"yyyy-MM-dd HH:mm:ss");
      if(!prcsTime)
        prcsBeginTime =  getDate(createTime,"yyyy-MM-dd HH:mm:ss");
    } else{
      prcsBeginTime =  getDate(createTime,"yyyy-MM-dd HH:mm:ss");
    }
    if(!prcsBeginTime){
      prcsBeginTime = nowDate;
    }
    
    var timeUserStr = getTimeOut(timeOut,prcsBeginTime,nowDate,"11");
    if(timeUserStr){
       timeOutFlag = 1;
       timeStr = "时限" + timeOut + "小时," + timeStr + "," + timeUserStr;
    }
  }
  if(!prcsTime){
    if(prcsFlag == "1"){
      html += "<div title=\"" + timeStr + "\"><img src='"+ imgPath +"/email_close.gif' alt='未接收'></div>"  
     }else{
       html += "<div title=\"" + timeStr + "\">" + createTime.substring(0, 19)  +"</div>"
     }
   }else{ 
    //strDate.substring(0, 10); prcsTime =  prcsTime.substr(prcsTime, prcsTime.length-10);
     var prcsTimeStr = prcsTime.substring(0, 19);
     if (prcsTimeStr) {
       html +=  prcsTimeStr +"<br>" ; 
     }
   }
  if(timeOutFlag){
    html +="<font color=red>" + timeUserStr + "</font><br>";
  }
  html += "<img src="+ imgPath +"/flow_time1.gif width='"+ width +"' height=6>" 
  return html;
  
  
}

/**
 * 流程操作描画器
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function flowOpRender(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  var flowId = this.getCellData(recordIndex,"flowId");
  var flowName = this.getCellData(recordIndex,"flowName");
  var runName = this.getCellData(recordIndex,"runName");
  var freeOther = this.getCellData(recordIndex,"freeOther");
  var prcsId = this.getCellData(recordIndex,"prcsId");
  var flowPrcs = this.getCellData(recordIndex,"flowPrcs");
  var flowType = this.getCellData(recordIndex,"flowType");
  var endTime = this.getCellData(recordIndex,"endTime");
  var par = "runId=" + runId + "&flowId=" + flowId + "&prcsId=" + prcsId + "&flowPrcs=" + flowPrcs  + "&sortId=" + sortId + "&skin=" + skin;
  var html = "";
  if (flowType == 1) {
    html = "<a href='../list/turn/turnnext.jsp?isManage=true&" + par + "' title=转交下一步骤><img src='" + imgPath + "/flow_next.gif' border=0>转交</a>";
  } else {
    html = "<a href='../list/turn/turnnextfree.jsp?isManage=true&" + par + "' title=转交下一步骤><img src='" + imgPath + "/flow_next.gif' border=0>转交</a>";
  }
  
  if(freeOther != 0 && !endTime){
    //html += "&nbsp;<a href = \"javascript:trustAction('" + flowType + "','" + par + "');\" title=将工作委托其他人办理>委托</a>";
  }
  html += "&nbsp;" 
    + "<input type=\"hidden\" id=\"menu_" + recordIndex + "_runId\" value=\"" + runId + "\">" 
    + "&nbsp;<a id=\"menu_" + recordIndex + "\" href='javascript:;' onmouseover=\"showMenu(\'menu_" + recordIndex + "\')\">更多<img align='absMiddle' src='"+ imgPath +"/menu_arrow_down.gif'/></a>";
  return html;
}
function showMenu(cntrlId){
  var menuItems = [{ name:'<div style="padding-top:5px;margin-left:5px">结束<div>',action:endWorkFlowById,extData:'1'}
  ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:5px">删除<div>',action:delWorkFlowById,extData:'2'}
]
  var menu = new Menu({bindTo: cntrlId , menuData:menuItems , attachCtrl: true});
  menu.show();
}
function delWorkFlowById(dom,cntrl,extDat) {
  cntrlId = $(cntrl).id;
  var runIdCntrl = $(cntrlId + "_runId");
  var runId = runIdCntrl.value;
  if(!confirm(tooltipMsg2)) {
    return ;
  }
  var url = contextPath +moduleSrcPath+"/act/YHFlowManageAct/delWorkFlow.act";
  var json = getJsonRs(url, "runIdStr=" + runId) ;
  if (json.rtState == '0') {
    alert(json.rtMsrg); 
    pageMgr.search();
  }
}
function endWorkFlowById(dom,cntrl,extDat) {
  cntrlId = $(cntrl).id;
  var runIdCntrl = $(cntrlId + "_runId");
  var runId = runIdCntrl.value;
  if(!confirm(tooltipMsg3)) {
    return ;
  }
 var url = contextPath +moduleSrcPath+"/act/YHFlowManageAct/endWorkFlow.act?isManage=true";
 var json = getJsonRs(url, "runIdStr=" + runId) ;
 if (json.rtState == '0') {
   alert(json.rtMsrg); 
   pageMgr.search();
 }
}
/*
 * 获取步骤办理超时时间，
 * time_begin $time_end时间段
 * format 返回格式形如 "dhms"为xx天xx小时xx分钟xx秒
 * except 是否排除双修日，两位代码 00 不排除 10代表排除周六，01代表排除周日，11代表排除周六日
 */
function getTimeOut(timeOut , timeBegin , timeEnd , except){
  var timeOutDesc = "";

  if (timeEnd == ""){
    timeEnd = time();
  }
   //-- 已用时间 --
  var timeUsed = "";
   //排除公休日
  timeUsed =  WorkingDay(timeBegin,timeEnd,except)/1000;
   //-- 超时计算 --
   if(timeOut != "" && timeUsed > timeOut * 3600) {
     timeUsed = timeUsed - timeOut * 3600;
      var day = Math.floor(timeUsed / 86400);
      var hour = Math.floor ((timeUsed % 86400) / 3600);
      var min =  Math.floor((timeUsed % 3600)/60);
      var sec =  Math.floor(timeUsed % 60);
      
      if(day > 0)
        timeOutDesc += day  +  "天";
      if(hour > 0)
        timeOutDesc += hour  +  "时";
      if(min > 0)
        timeOutDesc += min  +  "分";
      if(sec > 0)
        timeOutDesc += sec  +  "秒";
      timeOutDesc = "超时"  + timeOutDesc;
   }

  return timeOutDesc;
}
/**
 * 取得工作日毫秒数
 * @param dayStar 开始日期
 * @param dayEnd  结束日期
 * @param except 是否排除双修日，两位代码 00 不排除 10代表排除周六，01代表排除周日，11代表排除周六日
 * @return
 */
function   WorkingDay(dayStar,dayEnd,except){   
  var   weekEnd,weekStar,days,weeks,remain,workdays,workTimes;   
  var times = dayEnd.getTime() - dayStar.getTime();
  days = times/(24*60*60*1000) + 1;  //有多少个工作日 
  weeks = Math.floor(days/7);   //有多少周
  remain = days%7 ;   //不到一周的天数
  var saturday = parseInt(except.substr(0,1)); //周六
  var sunday = parseInt(except.substr(1,1)); //周日
  weekStar = dayStar.getDay();   //开始时间的是星期几
  weekEnd = dayEnd.getDay();   //结束时间的是星期几
  if(saturday && sunday){
    return times;
  }
  if((weekEnd == 0 && sunday) || (weekEnd == 6  && saturday)) {
    weekEnd = 7 - sunday - saturday;   
  }
  if((weekStar == 0 && sunday) || ( weekStar == 6 && saturday)) {
    weekStar = 1;   
  }
  if(weekEnd < weekStar){
    weekEnd = weekEnd + 7 - sunday - saturday; //表示
  }
  if(weekEnd == (7 - sunday - saturday) && weekStar == 1){
    weekEnd = weekStar - 1; //weekEnd == 0;
  }
  remain = weekEnd - weekStar + 1;   
  workdays = weeks*(7 - sunday - saturday) + remain; //工作日
  workTimes = times - (days - workdays)*(24*60*60*1000)
  return workTimes;
}   

var regexp;
function getDate(dateString, formatString){
  
  dateString = dateString.substring(0,19);
  if(validateDate(dateString, formatString)) {
    var now = new Date();
    var vals = regexp.exec(dateString);
    var index = validateIndex(formatString);
    var year = index[0] >= 0 ? vals[index[0] + 1] : now.getFullYear();
     var month = index[1] >= 0 ? (vals[index[1] + 1] - 1) : now.getMonth();
    var day = index[2] >= 0 ? vals[index[2] + 1] : now.getDate();
    var hour = index[3] >= 0 ? vals[index[3] + 1] : "";
    var minute = index[4] >= 0 ? vals[index[4] + 1] : "";
    var second = index[5] >= 0 ? vals[index[5] + 1] : "";
    var validate;
    if (hour == ""){
     validate = new Date(year, month, day);
    } else{
      validate = new Date(year, month, day, hour, minute, second);
    }
   if(validate.getDate() == day){
     return validate;
   }
  }
 // alert("日期验证失败");
}
 
 
 function validateDate(dateString, formatString){
   /** year : /yyyy/ */
   var y4 = "([0-9]{4})";
    /** year : /yy/ */
   var y2 = "([0-9]{2})";
   /** month : /MM/ */
   var M2 = "(0[1-9]|1[0-2])";
   /** month : /M/ */
   var M1 = "([1-9]|1[0-2])";
   /** day : /dd/ */
   var d2 = "(0[1-9]|[1-2][0-9]|30|31)";
   /** day : /d/ */
   var d1 = "([1-9]|[1-2][0-9]|30|31)";
   /** hour : /HH/ */
   var H2 = "([0-1][0-9]|20|21|22|23)";
   /** hour : /H/ */
   var H1 = "([0-9]|1[0-9]|20|21|22|23)";
   /** minute : /mm/ */
   var m2 = "([0-5][0-9])";
   /** minute : /m/ */
   var m1 = "([0-9]|[1-5][0-9])";
   /** second : /ss/ */
   var s2 = "([0-5][0-9])";
   /** second : /s/ */
   var s1 = "([0-9]|[1-5][0-9])";
   
   var dateString = trim(dateString);
  
   if(dateString=="") {
     return;
   }
   var reg = formatString;
   reg = reg.replace(/yyyy/, y4);
   reg = reg.replace(/yy/, y2);
   reg = reg.replace(/MM/, M2);
   reg = reg.replace(/M/, M1);
   reg = reg.replace(/dd/, d2);
   reg = reg.replace(/d/, d1);
   reg = reg.replace(/HH/, H2);
   reg = reg.replace(/H/, H1);
   reg = reg.replace(/mm/, m2);
   reg = reg.replace(/m/, m1);
   reg = reg.replace(/ss/, s2);
   reg = reg.replace(/s/, s1);
   reg = new RegExp("^"+reg+"$");
   regexp = reg;
   return reg.test(dateString);
 }

 function validateIndex(formatString){
   /** index year */
   var yi = -1;
   /** index month */
   var Mi = -1;
   /** index day */
   var di = -1;
   /** index hour */
   var Hi = -1;
   /** index minute */
   var mi = -1;
   /** index month */
   var si = -1;
   
   var ia = new Array();
   var i = 0;
   yi = formatString.search(/yyyy/);
   if ( yi < 0 ) {
     yi = formatString.search(/yy/);
   }
  if (yi >= 0) {
     ia[i] = yi;
     i++;
   }
   Mi = formatString.search(/MM/);
   if ( Mi < 0 ) {
     Mi = formatString.search(/M/);
   }
   if (Mi >= 0) {
     ia[i] = Mi;
     i++;
   }
   di = formatString.search(/dd/);
   if ( di < 0 ) {
     di = formatString.search(/d/);
   }
   if (di >= 0) {
     ia[i] = di;
     i++;
   }
   Hi = formatString.search(/HH/);
   if ( Hi < 0 ){
     Hi = formatString.search(/H/);
   }
   if (Hi >= 0) {
     ia[i] = Hi;
     i++;
   }
   mi = formatString.search(/mm/);
   if ( mi < 0 ) {
     mi = formatString.search(/m/);
   }
   if (mi >= 0) {
     ia[i] = mi;
     i++;
   }
   si = formatString.search(/ss/);
   if ( si < 0 ) {
     si = formatString.search(/s/);
   }
   if (si >= 0) {
     ia[i] = si;
     i++;
   }
   var ia2 = new Array(yi, Mi, di, Hi, mi, si);
   for(i = 0; i < ia.length - 1; i++) {
     for(j = 0;j < ia.length - 1 - i; j++) {
       if(ia[j]>ia[j+1]) {
         temp = ia[j]; 
         ia[j] = ia[j+1]; 
         ia[j+1] = temp;
       }
     }
   }
   for (i = 0; i < ia.length ; i++){
     for (j = 0; j < ia2.length; j++){
       if(ia[i] == ia2[j]) {
       ia2[j] = i;
       }
     }
   }
   return ia2;
 }

 function trim(str){
   return str.replace(/(^\s*)|(\s*$)/g, "");
 }
 