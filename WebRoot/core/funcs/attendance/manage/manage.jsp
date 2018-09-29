<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="hiddenRoll">
<head>
<title>外出登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/attendance/manage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/attendance/manage/js/registerApprovalLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
window.setTimeout('refash();',60000);
function outOverTime(seqId,userId,allow){
  var checkOut = 2;
  if($("moblieSmsRemind_out_"+seqId)){
    if($("moblieSmsRemind_out_"+seqId).checked){
      checkOut = 1;
    }
  }
  if(allow == "2"){
    var URL="<%=contextPath%>/custom/attendance/attendmanage/leadermanage/notovertime.jsp?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkOut="+checkOut;
    window.open(URL,"reason","height=500,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=60,resizable=yes");
  }else{
    //window.location.href = "<%=contextPath%>/yh/custom/attendance/act/YHOvertimeRecordAct/updateStatus.act?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkOut="+checkOut;
    var url = "<%=contextPath%>/yh/custom/attendance/act/YHOvertimeRecordAct/updateStatus.act?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkOut="+checkOut;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      //var date = rtJson.rtData;
      refash();
    }else{
      alert(rtJson.rtMsrg); 
    }
  }
}

function out_confirm(seqId,userId,allow){
  var checkOut = 2;
  if($("moblieSmsRemind_out_"+seqId)){
    if($("moblieSmsRemind_out_"+seqId).checked){
      checkOut = 1;
    }
  }
  if(allow == "2"){
    var URL="<%=contextPath%>/core/funcs/attendance/manage/notout.jsp?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkOut="+checkOut;
    window.open(URL,"reason","height=500,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=60,resizable=yes");
  }else{
    window.location.href = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageOutAct/updateStatus.act?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkOut="+checkOut;
  }
}
function DateCompare(asStartDate,asEndDate){   
  var miStart=Date.parse(asStartDate.replace(/-/g,'/'));   
  var miEnd=Date.parse(asEndDate.replace(/-/g,'/')); 
  var d = (miEnd-miStart)/(1000*24*3600);
  return d.toFixed(2);
} 
function dutyConfirm(seqId, userId, allow){
  var checkDuty = 2;
  if($("moblieSmsRemind_out_"+seqId)){
    if($("moblieSmsRemind_out_"+seqId).checked){
      checkDuty = 1;
    }
  }
  if(allow == "2"){
    var URL="<%=contextPath%>/core/funcs/attendance/manage/notduty.jsp?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkDuty="+checkDuty;
    window.open(URL,"reason","height=500,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=60,resizable=yes");
  }else{
    var url = "<%=contextPath%>/yh/custom/attendance/act/YHDutyAct/updateDutyStatus.act?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkDuty="+checkDuty;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      refash();
    }else{
      alert(rtJson.rtMsrg); 
    }
  }
}
function refash() {
  location.href = contextPath + "/core/funcs/attendance/manage/manage.jsp";
}
function leave_confirm(seqId,userId,allow){
  var checkLeave = 2;
  if($("moblieSmsRemind_leave_"+seqId)){
    if($("moblieSmsRemind_leave_"+seqId).checked){
      checkLeave = 1;
    }
  }
  if(allow=="2"){
    var URL="<%=contextPath%>/core/funcs/attendance/manage/notleave.jsp?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkLeave="+checkLeave;
    window.open(URL,"reason","height=500,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=60,resizable=yes");
  }else{
    window.location.href = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageLeaveAct/updateStatus.act?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkLeave="+checkLeave;
  }
}
function leave_destroy_confirm(seqId,userId,status){
  var checkLeave = 2;
  if($("moblieSmsRemind_leave_"+seqId)){
    if($("moblieSmsRemind_leave_"+seqId).checked){
      checkLeave = 1;
    }
  }
  window.location.href = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageLeaveAct/updateDestroyStatus.act?seqId=" + seqId + "&status=" + status+"&userId="+userId+"&checkLeave="+checkLeave;
}

//出差
function annual_confirm(seqId,userId,allow){ 
  var checkEvection = 2; 
  if($("moblieSmsRemind_evection_"+seqId)){ 
    if($("moblieSmsRemind_evection_"+seqId).checked){ 
      checkEvection = 1; 
    } 
  } 
  if(allow=="2"){ 
    var URL="<%=contextPath%>/core/funcs/attendance/manage/notevection.jsp?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkEvection="+checkEvection;
    window.open(URL,"reason","height=500,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=60,resizable=yes"); 
  }else{ 
  window.location.href = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageEvectionAct/updateStatus.act?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkEvection="+checkEvection; 
  } 
}

//年休假
function evection_confirm(seqId,userId,allow){
  var checkEvection = 2;
  if($("moblieSmsRemind_evection_"+seqId)){
    if($("moblieSmsRemind_evection_"+seqId).checked){
      checkEvection = 1;
    }
  }
  if(allow=="2"){
    var URL="<%=contextPath%>/custom/attendance/attendmanage/leadermanage/notannualleave.jsp?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkEvection="+checkEvection;
    window.open(URL,"reason","height=500,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=60,resizable=yes");
  }else{
    window.location.href = "<%=contextPath%>/yh/custom/attendance/act/YHAnnualLeaveAct/updateAllow.act?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkEvection="+checkEvection;
  }
}

//计算时长天——时——分
function getStr(str){
  var day = parseInt(str/(24*60),10);
  var hour = parseInt((str-(day*24*60))/60,10);
  var minute = parseInt((str -(day*24*60) - (hour*60)),10);
  var str = "";
  if(day>0){
    str = str + day + "天";
  }
  if(day>0&&minute>0&&hour==0){
    str = str + hour+"小时";
  }
  if(hour>0){
    str = str + hour+"小时";
  }
  if(minute>0){
    str =  str + minute + "分";
  }
  return str;
 }

//根据传过来的日期字符串判断是星期几

function getDayOfWeek(dateStr){
  var date = new Date(Date.parse(dateStr.replace(/-/g, '/'))); //将日期值格式化
  //day.getDay();根据Date返一个星期中的某其中0为星期日
  var week = date.getDay();
  var weekArray = ["周日","周一","周二","周三","周四","周五","周 六"];
  return weekArray[week]; 
}

function doOnload(){
 //手机短信显示
 var moblieRemindFlag = getMoblieSmsRemind();
  doOnloadOut(moblieRemindFlag);
  doOnloadLeave(moblieRemindFlag);
  doOnloadEvection(moblieRemindFlag);
  doAttendLeader(moblieRemindFlag);
  doFillRegister();
  //doOnloadDuty(moblieRemindFlag);
}

function doAttendLeader(moblieRemindFlag){
  var requestUrlOut = "<%=contextPath%>/yh/custom/attendance/act/YHAttendManageAct/selectAttendLeader.act";
  var rtJsonOut = getJsonRs(requestUrlOut);
  if(rtJsonOut.rtState == "1"){
    alert(rtJsonOut.rtMsrg); 
    return ;
    }
  var prcsJson = rtJsonOut.rtData;
  //加班审批
  var overtimeJson = prcsJson.overtimeJson;
  if(overtimeJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'overtimebody'><tr class='TableHeader'><td nowrap align='center'>部门</td>"
        + "<td nowrap align='center'>姓名</td>"
        + "<td nowrap align='center'>申请时间</td>"
        + "<td nowrap align='center'>开始时间</td>"
        + "<td nowrap align='center'>结束时间</td>"
        + "<td nowrap align='center'>加班时长</td>"
        + "</tr></tbody>");
    $("listOvertime").appendChild(table);
    for(var i = 0; i< overtimeJson.length;i++){
      var tr = new Element('tr',{"class":"TableData"});
      $('overtimebody').appendChild(tr);
      var prc = overtimeJson[i];
      var seqId = prc.seqId;
      var userId = prc.userId;
      var leaderId = prc.leaderId;
      var applyName = prc.applyName;
      var deptName = prc.deptName;

      var isHookRun = prc.isHookRun;
      var flowId = prc.flowId;
      
      var beginTime =  prc.beginTime.substr(0, 10) + " " + prc.beginDate;
      var endTime = prc.beginTime.substr(0, 10) + " " + prc.endDate;
      var overtimeChar = prc.overtimeChar;
      var overtimeCharStr = getStr(overtimeChar);
      var moblieRemindStr = "";
      if(moblieRemindFlag==2){
        moblieRemindStr = " <span><input type='checkbox' id='moblieSmsRemind_out_"+seqId+"' name='moblieSmsRemind_out_"+seqId+"' checked ><label for='moblieSmsRemind_out_"+seqId+"'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      if(moblieRemindFlag==1){
        moblieRemindStr = " <span><input type='checkbox' id='moblieSmsRemind_out_"+seqId+"' name='moblieSmsRemind_out_"+seqId+"'  ><label for='moblieSmsRemind_out_"+seqId+"'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      tr.update("<td nowrap align='center'><b>" + deptName + "</b></td>"
          + "<td nowrap align='center'><b>" +applyName + "</b></td>"
          + "<td nowrap align='center'>" + prc.overtimeTime.substr(0,19) + "</td>"
          + "<td nowrap align='center'>" + beginTime + "</td>"
          + "<td nowrap align='center'>"  + endTime  + "</td>"
          + "<td nowrap align='center'>" + overtimeCharStr + "</td>"
      );
      $('overtimebody').appendChild(tr);
      var tr1 = new Element('tr',{"class":"TableLine1"});
      var ss="<td  colspan=6 align='left'>"    + beginTime +"(" + getDayOfWeek(beginTime.substring(0,10))+ ") &nbsp;至 &nbsp;"+ endTime + "(" + getDayOfWeek(endTime.substring(0,10))+")<br><br>"
        + "加班原因 :&nbsp;&nbsp;" +  prc.overtimeDesc + "<br>"
        + "<div align='center'> "+moblieRemindStr;
        if(isHookRun!="0"){
        	 ss += "<a href='javascript:void(0)' onclick='formView("+isHookRun+" , "+ flowId +")'>查看流程</a>";
        }else{
        	 ss+= "<input type='button' class='SmallButton'  onclick='outOverTime(" + seqId +","+userId +",1);' value='批准'>&nbsp;&nbsp;"
             + "<input type='button'  class='SmallButton' onclick='outOverTime(" + seqId + ","+userId+",2);' value='不批准'>&nbsp;"
            }

        ss+="<br><br></div></td>"
      tr1.update(ss);
      $('overtimebody').appendChild(tr1);  
   }
  }else{
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"280"}).update("<tr>"
        + "<td class='msg info'>"
        + "<div class='content' style='font-size:12pt'>无加班审批申请</div>"
        + "</td></tr>"
         );
     $('listOvertime').appendChild(table);
  }

  //值班审批
  var dutyJson = prcsJson.dutyJson;
  if(dutyJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'dutybody'><tr class='TableHeader'><td nowrap align='center'>部门</td>"
        + "<td nowrap align='center'>姓名</td>"
        + "<td nowrap align='center'>申请时间</td>"
        + "<td nowrap align='center'>开始时间</td>"
        + "<td nowrap align='center'>结束时间</td>"
        + "<td nowrap align='center'>值班时长</td>"
        + "</tr></tbody>");
    $("listDutyLeave").appendChild(table);
    for(var i = 0; i< dutyJson.length;i++){
      var tr = new Element('tr',{"class":"TableData"});
      $('dutybody').appendChild(tr);
      var prc = dutyJson[i];
      var seqId = prc.seqId;
      var userId = prc.userId;
      var leaderId = prc.leaderId;
      var applyName = prc.applyName;
      var deptName = prc.deptName;
      var beginTime =  prc.dutyTime.substr(0, 10) + " " + prc.beginDate;
      var endTime = prc.dutyTime.substr(0, 10) + " " + prc.endDate;
      var overtimeChar = prc.overtimeChar;
      var overtimeCharStr = getStr(overtimeChar);
      var moblieRemindStr = "";
      if(moblieRemindFlag==2){
        moblieRemindStr = " <span><input type='checkbox' id='moblieSmsRemind_out_"+seqId+"' name='moblieSmsRemind_out_"+seqId+"' checked ><label for='moblieSmsRemind_out_"+seqId+"'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      if(moblieRemindFlag==1){
        moblieRemindStr = " <span><input type='checkbox' id='moblieSmsRemind_out_"+seqId+"' name='moblieSmsRemind_out_"+seqId+"'  ><label for='moblieSmsRemind_out_"+seqId+"'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      tr.update("<td nowrap align='center'><b>" + deptName + "</b></td>"
          + "<td nowrap align='center'><b>" +applyName + "</b></td>"
          + "<td nowrap align='center'>" + prc.dutyTime.substr(0,10) + "</td>"
          + "<td nowrap align='center'>" + beginTime + "</td>"
          + "<td nowrap align='center'>"  + endTime  + "</td>"
          + "<td nowrap align='center'>" + overtimeCharStr + "</td>"
      );
      $('dutybody').appendChild(tr);
      var tr1 = new Element('tr',{"class":"TableLine1"});
      tr1.update("<td  colspan=6 align='left'>"    + beginTime +"(" + getDayOfWeek(beginTime.substring(0,10))+ ") &nbsp;至 &nbsp;"+ endTime + "(" + getDayOfWeek(endTime.substring(0,10))+")<br><br>"
        + "值班原因 :&nbsp;&nbsp;" +  prc.dutyDesc + "<br>"
        + "<div align='center'> "+moblieRemindStr
        
        + "<input type='button' class='SmallButton'  onclick='dutyConfirm(" + seqId +","+userId +",1);' value='批准'>&nbsp;&nbsp;"
        + "<input type='button'  class='SmallButton' onclick='dutyConfirm(" + seqId + ","+userId+",2);' value='不批准'>&nbsp;"
        +"<br><br></div></td>"
      );  
      $('dutybody').appendChild(tr1);  
   }
  }else{
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"280"}).update("<tr>"
        + "<td class='msg info'>"
        + "<div class='content' style='font-size:12pt'>无值班审批申请</div>"
        + "</td></tr>"
         );
     $('listDutyLeave').appendChild(table);
  }

  //年休假审批 
  var annualJson = prcsJson.annualJson;
  if(annualJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tbodayAnnual'><tr class='TableHeader'>"
        +"<td nowrap align='center'>部门</td>"
        + "<td nowrap align='center'>姓名</td>"
        + "<td nowrap align='center'>申请时间</td>"
        + "<td nowrap align='center'>开始时间</td>"
        + "<td nowrap align='center'>结束时间</td>"
        + "<td nowrap align='center'>天数</td>"
        + "<td nowrap align='center'>累计天数 </td>"
        + "<td nowrap align='center'>年休假天数</td>"
        + "<td nowrap align='center'>剩余天数</td>"
        + "</tr></tbody>");
      $('listAnnualLeave').appendChild(table); 
      for(var i = 0; i< annualJson.length ; i++){
        var tr = new Element('tr',{"class":"TableData"});
        $('tbodayAnnual').appendChild(tr);
        var prc = annualJson[i];
        var seqId = prc.seqId;
        var userId = prc.userId;
        var leaderId = prc.leaderId;
        var applyName = prc.applyName;
        var leaveType = prc.leaveType;
        var destroyTime = prc.destroyTime;
        var allow = prc.allow;
        var leaveDate1 = prc.leaveDate1;
        var leaveDate2 = prc.leaveDate2;
        var allow = prc.allow;
        var status = prc.status;
        var reason = prc.reason;
        var leaveTotal =prc.leaveTotal;
        var annualDays = prc.annualDays;
        var overPlusDays = prc.overPlusDays;
        var moblieRemindStr = "";
        if(moblieRemindFlag==2){
          moblieRemindStr = " <span id='moblieSmsRemindDiv'><input type='checkbox' id='moblieSmsRemind_evection_"+seqId+"' name='moblieSmsRemind_evection_"+seqId+"' checked ><label for='moblieSmsRemind_evection_"+seqId+"'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
         }
        if(moblieRemindFlag==1){
          moblieRemindStr = " <span id='moblieSmsRemindDiv'><input type='checkbox' id='moblieSmsRemind_evection_"+seqId+"' name='moblieSmsRemind_evection_"+seqId+"'  ><label for='moblieSmsRemind_evection_"+seqId+"'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
         }
        tr.update("<td nowrap align='center'><b>" + prc.deptName + "</b></td>"
          + "<td nowrap align='center'><b>" +applyName + "</b></td>"
          + "<td nowrap align='center'>" + prc.applyTime.substring(0,19) + "</td>"
          + "<td nowrap align='center'>" + leaveDate1 + "</td>"
          + "<td nowrap align='center'>" + leaveDate2 + "</td>"
          + "<td nowrap align='center'>" + prc.leaveDays + "</td>"
          + "<td nowrap align='center'>" + leaveTotal + "</td>"
          + "<td nowrap align='center'>" + annualDays + "</td>"
          + "<td nowrap align='center'>" + overPlusDays + "</td>"
        );
        $("tbodayAnnual").appendChild(tr);  
        var tr = new Element('tr',{"class":"TableLine1"});
        $('tbodayAnnual').appendChild(tr);
        tr.update("<td colspan='9' align='left'>"   + leaveDate1 +"(" + getDayOfWeek(leaveDate1.substring(0,10))+ ")&nbsp;至&nbsp;"+leaveDate2+"(" + getDayOfWeek(leaveDate2.substring(0,10))+")<br><br>"
          + "<div align='center'>"+moblieRemindStr
          + "<input type='button' class='SmallButton' onclick='evection_confirm(" + seqId +","+userId+ ",1);' value='批准'>&nbsp;&nbsp;"
          + "<input type='button' class='SmallButton' onclick='evection_confirm(" + seqId +","+userId +",2);' value='不批准'>&nbsp;"
          +"<br><br></div></td>"
        ); 
    
     }
  }else{
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"280"}).update("<tr>"
        + "<td class='msg info'>"
        + "<div class='content' style='font-size:12pt'>无年休假审批申请</div>"
        + "</td></tr>"
         );
     $('listAnnualLeave').appendChild(table);
  }
}


//得到外出审批
function doOnloadOut(moblieRemindFlag){
  var requestUrlOut = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageOutAct/selectOutManage.act";
  var rtJsonOut = getJsonRs(requestUrlOut);
  if(rtJsonOut.rtState == "1"){
    alert(rtJsonOut.rtMsrg); 
    return ;
    }
  var prcsJson = rtJsonOut.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tboday'><tr class='TableHeader'><td nowrap align='center'>部门</td>"
      + "<td nowrap align='center'>姓名</td>"
      + "<td nowrap align='center'>申请时间</td>"
      + "<td nowrap align='center'>登记IP</td>"
      + "</tr></tbody>");
    $('listOut').appendChild(table); 
    for(var i =0;i< prcsJson.length;i++){
      var tr = new Element('tr',{"class":"TableData"});
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var leaderId = prcs.leaderId;
      var applyName = prcs.applyName;
      var createDate = prcs.createDate;
      var registerIp = prcs.registerIp;
      var outType = prcs.outType;
      var submitTime = prcs.submitTime;
      var week = prcs.week;
      var outTime2 = prcs.outTime2;
      var deptName = prcs.deptName;
       var isHookRun=prcs.isHookRun;
       var flowId=prcs.flowId;

      
      var moblieRemindStr = "";
      if(moblieRemindFlag==2){
        moblieRemindStr = " <span><input type='checkbox' id='moblieSmsRemind_out_"+seqId+"' name='moblieSmsRemind_out_"+seqId+"' checked ><label for='smsRemind'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      if(moblieRemindFlag==1){
        moblieRemindStr = " <span><input type='checkbox' id='moblieSmsRemind_out_"+seqId+"' name='moblieSmsRemind_out_"+seqId+"'  ><label for='smsRemind'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      tr.update("<td nowrap align='center'><b>" + deptName + "</b></td>"
        + "<td nowrap align='center'><b>" +applyName + "</b></td>"
        + "<td nowrap align='center'>" + createDate + "</td>"
        + "<td nowrap align='center'>" + registerIp + "</td>"
      );
      $('tboday').appendChild(tr);
      var tr1 = new Element('tr',{"class":"TableLine1"});
      var ss="";
      ss="<td  colspan=4 align='left'>"   + submitTime.substr(0,16) +"(" + week+ ") &nbsp;至 &nbsp;"+ submitTime.substr(0,10)+ " " + outTime2 + "(" + week+")<br><br>"
        + "外出原因 :&nbsp;&nbsp;" +  outType + "<br>"
        + "<div align='center'> "+moblieRemindStr
        if(isHookRun!=0){

        	 ss += "<a href='javascript:void(0)' onclick='formView("+isHookRun+" , "+ flowId +")'>查看流程</a>";
            }else{

         ss+=  "<input type='button' class='SmallButton'  onclick='out_confirm(" + seqId +","+userId +",1);' value='批准'>&nbsp;&nbsp;"
         + "<input type='button'  class='SmallButton' onclick='out_confirm(" + seqId + ","+userId+",2);' value='不批准'>&nbsp;"
         }
       
       ss +="<br><br></div></td>"
       tr1.update(ss);
      $('tboday').appendChild(tr1);   
    }
  }else{
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"280"}).update("<tr>"
      + "<td class='msg info'>"
      + "<div class='content' style='font-size:12pt'>无外出审批申请</div>"
      + "</td></tr>"
       );
   $('listOut').appendChild(table);
  } 
}
//得到请假要审批的
function doOnloadLeave(moblieRemindFlag){
  var requestUrlLeave = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageLeaveAct/selectLeaveManage.act";
  var rtJsonLeave = getJsonRs(requestUrlLeave);
  if(rtJsonLeave == "1"){
    alert(rtJsonOut.rtMsrg); 
    return ;
    }
  var prcsJson =rtJsonLeave.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tbodayleave'><tr class='TableHeader'><td nowrap align='center'>部门</td>"
      + "<td nowrap align='center'>姓名</td>"
      + "<td nowrap align='center'>开始时间</td>"
      + "<td nowrap align='center'>申请类型</td>"
      + "<td nowrap align='center'>销假申请时间</td>"   
      + "<td nowrap align='center'>登记IP</td>"
      + "</tr></tbody>");
    $('listLeave').appendChild(table); 
    for(var i =0;i< prcsJson.length;i++){
      var tr = new Element('tr',{"class":"TableData"});
      $('tbodayleave').appendChild(tr);
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var leaderId = prcs.leaderId;
      var applyName = prcs.applyName;
      var leaveType = prcs.leaveType;
      var registerIp = prcs.registerIp;
      var destroyTime = prcs.destroyTime;
      var week1 = prcs.week1;
      var week2 = prcs.week2;
      var allow = prcs.allow;
      var leaveDate1 = prcs.leaveDate1;
      var leaveDate2 = prcs.leaveDate2;
      var reason = prcs.reason;
      var status = prcs.status;
      var annualLeave = prcs.annualLeave;
      var deptName = prcs.deptName;
      var isHookRun = prcs.isHookRun;
      var flowId = prcs.flowId;
      
      var moblieRemindStr = "";
      if(moblieRemindFlag==2){
        moblieRemindStr = " <span ><input type='checkbox' id='moblieSmsRemind_leave_"+seqId+"' name='moblieSmsRemind_leave_"+seqId+"' checked ><label for='smsRemind'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      if(moblieRemindFlag==1){
        moblieRemindStr = " <span id='moblieSmsRemindDiv'><input type='checkbox' id='moblieSmsRemind_leave_"+seqId+"' name='moblieSmsRemind_leave_"+seqId+"'  ><label for='smsRemind'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      tr.update("<td nowrap align='center'><b>" + deptName + "</b></td>"
        + "<td nowrap align='center'><b>" +applyName + "</b></td>"
        + "<td nowrap align='center'>" + leaveDate1 + "</td>"
        + "<td nowrap align='center'>请假申请</td>"
        + "<td nowrap align='center'>" + destroyTime + "</td>"
        + "<td nowrap align='center'>" + registerIp + "</td>"
      );
      var tr = new Element('tr',{"class":"TableLine1"});
      $('tbodayleave').appendChild(tr);
      if(allow =="0"){
        var ss = "<td colspan='6' align='left'>"   +leaveDate1 +"(" + week1+ ")&nbsp;至&nbsp;"+ leaveDate2+ "(" + week2+ ")，一共："+DateCompare(leaveDate1,leaveDate2)+"天<br><br>"
        + "请假原因:&nbsp;" + leaveType + "<br>"
        + "<div align='center'>"+moblieRemindStr
        if (isHookRun != '0') {
          ss += "<a href='javascript:void(0)' onclick='formView("+isHookRun+" , "+ flowId +")'>查看流程</a>";
        } else {
          ss += "<input type='button'  class='SmallButton' onclick='leave_confirm(" + seqId + ","+userId+",1);' value='批准'>&nbsp;&nbsp;"
             + "<input type='button'  class='SmallButton' onclick='leave_confirm(" + seqId +","+userId+ ",2);' value='不批准'>&nbsp;";
        }
        ss += "<br><br></div></td>";
        tr.update(ss); 
      }else{
        tr.update("<td colspan='7' align='left'>"   +leaveDate1 +"(" + week1+ ")至"+ leaveDate2+ "(" + week2+ ")，一共："+DateCompare(leaveDate1,leaveDate2)+"天<br><br>"
            +   leaveType + "<br>"
            + "<div align='center'>"+moblieRemindStr
            + "<input type='button' class='SmallButton' onclick='leave_destroy_confirm(" + seqId +","+userId+ ",2);' value='销假'>&nbsp;"
            +"<br><br></div></td>"
          ); 
      } 
    }
  }else{
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"280"}).update("<tr>"
      + "<td class='msg info'>"
      + "<div class='content' style='font-size:12pt'>无请假审批申请</div>"
      + "</td></tr>"
       );
   $('listLeave').appendChild(table);
  } 
}

//得到出差审批
function doOnloadEvection(moblieRemindFlag){
  var requestUrlEvection = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageEvectionAct/selectEvectionManage.act";
  var rtJsonEvection = getJsonRs(requestUrlEvection);
  if(rtJsonEvection.rtState == "1"){
    alert(rtJsonEvection.rtMsrg); 
    return ;
    }
  var prcsJson = rtJsonEvection.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tbodayOut'><tr class='TableHeader'><td nowrap align='center'>部门</td>"
      + "<td nowrap align='center'>姓名</td>"
      + "<td nowrap align='center'>出差地点</td>"
      + "<td nowrap align='center'>开始时间</td>"
      + "<td nowrap align='center'>登记IP</td>"
      + "</tr></tbody>");
    $('listEvection').appendChild(table); 
    for(var i =0;i< prcsJson.length;i++){
      var tr = new Element('tr',{"class":"TableData"});
      $('tbodayOut').appendChild(tr);
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var leaderId = prcs.leaderId;
      var deptName = prcs.deptName;
      var applyName = prcs.applyName;
      var registerIp = prcs.registerIp;
      var evectionDest= prcs.evectionDest;
      var evectionDate1 = prcs.evectionDate1;
      var evectionDate2 = prcs.evectionDate2;
      var allow = prcs.allow;
      var status = prcs.status;
      var reason = prcs.reason;
      var week1 = prcs.week1;
      var week2 = prcs.week2;
      var isHookRun=prcs.isHookRun;
      var flowId=prcs.flowId;
      var moblieRemindStr = "";
      if(moblieRemindFlag==2){
        moblieRemindStr = " <span id='moblieSmsRemindDiv'><input type='checkbox' id='moblieSmsRemind_evection_"+seqId+"' name='moblieSmsRemind_evection_"+seqId+"' checked ><label for='smsRemind'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      if(moblieRemindFlag==1){
        moblieRemindStr = " <span id='moblieSmsRemindDiv'><input type='checkbox' id='moblieSmsRemind_evection_"+seqId+"' name='moblieSmsRemind_evection_"+seqId+"'  ><label for='smsRemind'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      tr.update("<td nowrap align='center'><b>" + deptName + "</b></td>"
        + "<td nowrap align='center'><b>" +applyName + "</b></td>"
        + "<td nowrap align='center'>" + evectionDest + "</td>"
        + "<td nowrap align='center'>" + evectionDate1.substr(0,10) + "</td>"
        + "<td nowrap align='center'>" + registerIp + "</td>"
      );
      var tr = new Element('tr',{"class":"TableLine1"});
      $('tbodayOut').appendChild(tr);
      var ss="";
      ss="<td colspan='5' align='left'>"   + evectionDate1.substr(0,10) +"(" + week1+ ")&nbsp;至&nbsp;"+ evectionDate2.substr(0,10)+"(" + week2+")<br><br>"
        + "事由:&nbsp;" + reason + "<br>"
        + "<div align='center'>"+moblieRemindStr;
        if (isHookRun != '0') {
            ss += "<a href='javascript:void(0)' onclick='formView("+isHookRun+" , "+ flowId +")'>查看流程</a>";
        } else{
	          ss += "<input type='button' class='SmallButton' onclick='annual_confirm(" + seqId +","+userId+ ",1);' value='批准'>&nbsp;&nbsp;"
	           + "<input type='button' class='SmallButton' onclick='annual_confirm(" + seqId +","+userId +",2);' value='不批准'>&nbsp;"
        }
       ss +="<br><br></div></td>"
        tr.update(ss);
    }
  }else{
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"280"}).update("<tr>"
      + "<td class='msg info'>"
      + "<div class='content' style='font-size:12pt'>无出差审批申请</div>"
      + "</td></tr>"
       );
   $('listEvection').appendChild(table);
  } 
}

//补登记审批
var pageMgr = null;
function doFillRegister(){
  var assessingStatus = "0";
  var url = "<%=contextPath%>/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/getRegisterApprovalListJson.act?assessingStatus="+assessingStatus;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"hidden", name:"assessingStatus", text:"顺序号", dataType:"int"},
         {type:"data", name:"proposer", width: '20%', text:"申请人", render:proposerFunc},
         {type:"data", name:"registerType", width: '20%', text:"登记次序", render:registerTypeFunc},
         {type:"data", name:"fillTime", width: '15%', text:"补登记日期", render:fillTimeFunc},
         {type:"data", name:"assessingOfficer",  width: '20%', text:"审批人", render:assessingOfficerFunc},       
         {type:"selfdef", text:"操作", width: '20%',render:opts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无补登记审批记录', 'msrg');
    }
}
/** 
*js代码 
*是否显示手机短信提醒 
*/ 
function getMoblieSmsRemind(){ 
var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=6"; 
var rtJson = getJsonRs(requestUrl); 
if(rtJson.rtState == "1"){ 
alert(rtJson.rtMsrg); 
return ; 
} 
var prc = rtJson.rtData; 
var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
return moblieRemindFlag;
if(moblieRemindFlag == '2'){ //显示并选中
//$(remidDiv).style.display = ''; 
//$(remind).checked = true; 
}else if(moblieRemindFlag == '1'){ //显示不选中
//$(remidDiv).style.display = ''; 
//$(remind).checked = false; 
}else{ 
//$(remidDiv).style.display = 'none'; //直接不显示
} 
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<!-- 今日外出审批 -->
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 加班审批</span><br>
    </td>
  </tr>
</table>
<br>
<div id="listOvertime" ></div>
<br>

<!--值班审批 -->

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 值班审批</span><br>
    </td>
  </tr>
</table>
<br>
<div id="listDutyLeave" ></div>
<br>
<!-- 今日外出审批 -->
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 外出审批</span><br>
    </td>
  </tr>
</table>
<br>
<div id="listOut" ></div>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>

<!-- 请假审批 -->

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 请假审批</span><br>
    </td>
  </tr>
</table>
<br>
<div id="listLeave"></div>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>

<!-- 出差审批 -->

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 出差审批</span><br>
    </td>
  </tr>
</table>
<br>
<div id="listEvection" ></div>
<!--年休假审批 -->

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 年休假审批</span><br>
    </td>
  </tr>
</table>
<br>
<div id="listAnnualLeave" ></div>
<!--补登记审批 -->
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 补登记审批</span><br>
    </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:95%;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="95%">
<tr class="TableControl">
<td colspan="9">
  &nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
    <input type="button"  value="批量删除" class="BigButton" onClick="deleteAll()" title="删除所有卷库">
</td>
</tr>
</table>

</div>
<div id="overlay" style="width: 1238px; height: 610px; display: '';"></div>
<div id="apply" class="ModalDialog" style="display:none;width:400px;">
  <div class="header"><span id="title" class="title">审批意见</span><a class="operation" href="javascript:HideDialog();"><img src="<%=imgPath%>/close.png"/></a></div>
  <form name="form1" id="form1" method="post" action="">
  <div id="detail_body" class="body">
  <span id="confirm"></span>
  <textarea id="assessingView" name="assessingView" cols="45" rows="5" style="overflow-y:auto;" class="BigInput" wrap="yes"></textarea>
  </div>
  <input type="hidden" name="seqId" id="seqId">
  <input type="hidden" name="assessingStatus" id="assessingStatus">
  <div id="footer" class="footer">
    <input class="BigButton" type="button" value="确定" onclick="doSubmit();"/>
    <input class="BigButton" onclick="HideDialog()" type="button" value="关闭"/>
  </div>
  </form>
</div>
<div id="msrg">
</div>
</body>
</html>