<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="hiddenRoll">
<head>
<title>外出登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
window.setTimeout('this.location.reload();',60000);
function out_confirm(seqId,userId,allow){
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
    window.location.href = "<%=contextPath%>/yh/custom/attendance/act/YHOvertimeRecordAct/updateStatus.act?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkOut="+checkOut;
  }
}
function leave_confirm(seqId,userId,allow){
  var checkLeave = 2;
  if($("moblieSmsRemind_leave_"+seqId)){
    if($("moblieSmsRemind_leave_"+seqId).checked){
      checkLeave = 1;
    }
  }
  if(allow=="2"){
    var URL="<%=contextPath%>/custom/attendance/attendmanage/leadermanage/notleave.jsp?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkLeave="+checkLeave;
    window.open(URL,"reason","height=500,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=60,resizable=yes");
  }else{
    window.location.href = "<%=contextPath%>/yh/custom/attendance/act/YHPersonalLeaveAct/updateAllowReasonOn.act?seqId=" + seqId + "&allow=" + allow+"&userId="+userId+"&checkLeave="+checkLeave;
  }
}
function leave_destroy_confirm(seqId,userId,status){
  var checkLeave = 2;
  if($("moblieSmsRemind_leave_"+seqId)){
    if($("moblieSmsRemind_leave_"+seqId).checked){
      checkLeave = 1;
    }
  }
  window.location.href = "<%=contextPath%>/yh/custom/attendance/act/YHPersonalLeaveAct/updateDestroyStatus.act?seqId=" + seqId + "&status=" + status+"&userId="+userId+"&checkLeave="+checkLeave;
}
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
function doOnload(){
 //手机短信显示
  var moblieRemindFlag = getMoblieSmsRemind();
  doAttendLeader(moblieRemindFlag);
  //doOnloadOut(moblieRemindFlag);
  //doOnloadLeave(moblieRemindFlag);
  //doOnloadEvection(moblieRemindFlag);
}

//得到加班审批+请假审批 + 年休假审批
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
      var beginTime =  prc.beginTime;
      var endTime = prc.endTime;
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
      tr1.update("<td  colspan=6 align='left'>"    + beginTime +"(" + getDayOfWeek(beginTime.substring(0,10))+ ") &nbsp;至 &nbsp;"+ endTime + "(" + getDayOfWeek(endTime.substring(0,10))+")<br><br>"
        + "加班原因 :&nbsp;&nbsp;" +  prc.overtimeDesc + "<br>"
        + "<div align='center'> "+moblieRemindStr
        
        + "<input type='button' class='SmallButton1'  onclick='out_confirm(" + seqId +","+userId +",1);' value='批准'>&nbsp;&nbsp;"
        + "<input type='button'  class='SmallButton1' onclick='out_confirm(" + seqId + ","+userId+",2);' value='不批准'>&nbsp;"
        +"<br><br></div></td>"
      );  
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


  //请假审批
  var leaveJson = prcsJson.leaveJson;
  if(leaveJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tbodayleave'><tr class='TableHeader'>"
        +"<td nowrap align='center'>部门</td>"
        + "<td nowrap align='center'>姓名</td>"
        + "<td nowrap align='center'>申请时间</td>"
        + "<td nowrap align='center'>开始时间</td>"
        + "<td nowrap align='center'>结束时间</td>" 
        + "<td nowrap align='center'>请假时长</td>" 
        + "</tr></tbody>");
    $('listLeave').appendChild(table);
    for(var i = 0; i<leaveJson.length;i++){
      var tr = new Element('tr',{"class":"TableData"});
      $('tbodayleave').appendChild(tr);
      var prc = leaveJson[i];
      var seqId = prc.seqId;
      var userId = prc.userId;
      var leaderId = prc.leaderId;
      var applyName = prc.applyName;
      var leaveType = prc.leaveType;
      var destroyTime = prc.destroyTime;
      var allow = prc.allow;
      var leaveDate1 = prc.leaveDate1;
      var leaveDate2 = prc.leaveDate2;
      var leaveChar = prc.leaveChar;
      var leaveCharStr = getStr(leaveChar);
      var reason = prc.reason;
      var deptName = prc.deptName;
      var moblieRemindStr = "";
      if(moblieRemindFlag==2){
        moblieRemindStr = " <span ><input type='checkbox' id='moblieSmsRemind_leave_"+seqId+"' name='moblieSmsRemind_leave_"+seqId+"' checked ><label for='moblieSmsRemind_leave_"+seqId+"'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      if(moblieRemindFlag==1){
        moblieRemindStr = " <span id='moblieSmsRemindDiv'><input type='checkbox' id='moblieSmsRemind_leave_"+seqId+"' name='moblieSmsRemind_leave_"+seqId+"'  ><label for='moblieSmsRemind_leave_"+seqId+"'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      tr.update("<td nowrap align='center'><b>" + deptName + "</b></td>"
        + "<td nowrap align='center'><b>" +applyName + "</b></td>"
        + "<td nowrap align='center'>" + prc.applyTime.substring(0,19) + "</td>"
        + "<td nowrap align='center'>" + leaveDate1 + "</td>"
        + "<td nowrap align='center'>"+leaveDate2+"</td>"
        + "<td nowrap align='center'>" +leaveCharStr + "</td>"
      );
      var tr = new Element('tr',{"class":"TableLine1"});
      $('tbodayleave').appendChild(tr);
      if(allow =="0"){
        tr.update("<td colspan='6' align='left'>"   +leaveDate1 +"(" + getDayOfWeek(leaveDate1.substring(0,10))+ ")&nbsp;至&nbsp;"+ leaveDate2+ "(" + getDayOfWeek(leaveDate2.substring(0,10))+ ")<br><br>"
            + "请假原因:&nbsp;" + leaveType + "<br>"
            + "<div align='center'>"+moblieRemindStr
            + "<input type='button'  class='SmallButton1' onclick='leave_confirm(" + seqId + ","+userId+",1);' value='批准'>&nbsp;&nbsp;"
            + "<input type='button'  class='SmallButton1' onclick='leave_confirm(" + seqId +","+userId+ ",2);' value='不批准'>&nbsp;"
            +"<br><br></div></td>"
          ); 
      }else{
        tr.update("<td colspan='6' align='left'>"   +leaveDate1 +"(" + week1+ ")至"+ leaveDate2+ "(" + week2+ ")<br><br>"
            +   leaveType + "<br>"
            + "<div align='center'>"+moblieRemindStr
            + "<input type='button' class='SmallButton1' onclick='leave_destroy_confirm(" + seqId +","+userId+ ",2);' value='销假'>&nbsp;"
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
        tr.update("<td colspan='9' align='left'>"   + leaveDate1 +"(" + getDayOfWeek(leaveDate1.substring(0,10))+ ")&nbsp;至&nbsp;"+leaveDate1+"(" + getDayOfWeek(leaveDate2.substring(0,10))+")<br><br>"
          + "<div align='center'>"+moblieRemindStr
          + "<input type='button' class='SmallButton1' onclick='evection_confirm(" + seqId +","+userId+ ",1);' value='批准'>&nbsp;&nbsp;"
          + "<input type='button' class='SmallButton1' onclick='evection_confirm(" + seqId +","+userId +",2);' value='不批准'>&nbsp;"
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
//alert(moblieRemindFlag)
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
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath %>/dian1.gif" width="100%"></td>
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

<!--年休假审批 -->

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 年休假审批</span><br>
    </td>
  </tr>
</table>
<br>
<div id="listAnnualLeave" ></div>
</body>
</html>