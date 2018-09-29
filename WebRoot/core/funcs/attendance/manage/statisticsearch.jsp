<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String userId = request.getParameter("userId");
  String deptId = request.getParameter("deptId");
  String beginTime = request.getParameter("beginTime");
  String endTime = request.getParameter("endTime");
  String dutyType = request.getParameter("dutyType");
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月dd日");
  String beginTimeStr = dateFormat2.format(dateFormat1.parse(beginTime));
  String endTimeStr = dateFormat2.format(dateFormat1.parse(endTime));
  //相隔多少天
  long daySpace = YHUtility.getDaySpan(dateFormat1.parse(beginTime),dateFormat1.parse(endTime))+1;
  //得到到之间的天数数组
  List daysList = new ArrayList();
  String days = "";
  Calendar calendar = new GregorianCalendar();
  for(int i = 0;i<daySpace;i++){
    calendar.setTime(dateFormat1.parse(beginTime));
    calendar.add(Calendar.DATE,+i) ;
    Date dateTemp = calendar.getTime();
    String dateTempStr = dateFormat1.format(dateTemp);
    daysList.add(dateTempStr);
    days = days + dateTempStr + ",";
  }
  if(daySpace>0){
    days = days.substring(0,days.length()-1);
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="hiddenRoll">
<head>
<title>上下班登记修改</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" >
function doOnload(){
  var userId = '<%=userId%>';
  var deptId = '<%=deptId%>';
  var beginTime = '<%=beginTime%>';
  var endTime = '<%=endTime%>';
  var dutyType = '<%=dutyType%>';
  var days = '<%=days%>';
  days = days.split(",");
  //得到部门的Id 可能还有子部门的所有 人员
  var userIds = selectUserIds(userId,deptId);
  //上下班记录情况
  dutyInfo(userIds,beginTime,endTime,dutyType,days);
  //得到外出记录
  out(userIds,beginTime,endTime);
  //得到请假记录
  leave(userIds,beginTime,endTime);
  //得到出差记录
  evection(userIds,beginTime,endTime);
}
//得到部门的Id 可能还有子部门的所有 人员
function selectUserIds(userId,deptId){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectUserIds.act?userId="+userId+"&deptId="+deptId;
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;

  return prc.userIds;
}
//上下班记录 根据排版类型 重新得到 userId ,在后台去掉免签人员
function selectuserIdByDutyType(userIds,dutyType){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectuserIdByDutyType.act?userId="+userIds+"&dutyType="+dutyType;
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  $("userIds").value = prc.userIds;
  return prc.userIds;
}

//得到上下班情况
function dutyInfo(userIds,beginTime,endTime,dutyType,days){
  var userId = selectuserIdByDutyType(userIds,dutyType);
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/allUserDutyInfo.act?userId="+userId+"&dutyType="+dutyType+"&days="+days;
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  //alert(rsText);
  for(var i = 0; i<prcs.length;i++){
    var prc = prcs[i];
    var deptName = prc.deptName;
    var userName = prc.userName;
    var perfectCount = prc.perfectCount;
    var hourTotal = prc.hourTotal;
    var lateCount = prc.lateCount;
    var dutyOnTotal = prc.dutyOnTotal;
    var earlyCount = prc.earlyCount;
    var dutyOffTotal = prc.dutyOffTotal;
    var addDutyOnCount = prc.addDutyOnCount;
    var addDutyOffCount = prc.addDutyOffCount;
    var userSeqId = prc.userSeqId;
    hourTotal = getStr2(hourTotal);
    newTr(deptName,userName,perfectCount,hourTotal,lateCount,dutyOnTotal,earlyCount,dutyOffTotal,addDutyOnCount,addDutyOffCount,userSeqId);
  }
 
}
function newTr(deptName,userName,perfectCount,hourTotal,lateCount,dutyOnTotal,earlyCount,dutyOffTotal,addDutyOnCount,addDutyOffCount,userSeqId){
  var tr = new Element('tr',{"class":"TableLine1"}).update("<td nowrap align='center'>"+deptName+"</td>"
    +"<td nowrap align='center' >"+userName+"</td>"
    +"<td nowrap align='center'>"+perfectCount+"</td>"
    +"<td nowrap align='center'>"+hourTotal+"</td>"
    +"<td nowrap align='center'>"+lateCount+"</td>"
    +"<td nowrap align='center'>"+dutyOnTotal+"</td>"
    +"<td nowrap align='center'>"+earlyCount+"</td>"
    +"<td nowrap align='center'>"+dutyOffTotal+"</td>"
    +"<td nowrap align='center'>"+addDutyOnCount+"</td>"
    +"<td nowrap align='center'>"+addDutyOffCount+"</td>"
    +"<td nowrap align='center'><a href='#' onclick='ToUser("+userSeqId+")'>详细记录</a></td>");

  $("userTable").appendChild(tr);
}
//进入单个用户信息
function ToUser(userSeqId){
  var beginTime = '<%=beginTime%>';
  var endTime = '<%=endTime%>';
  window.location.href="<%=contextPath%>/core/funcs/attendance/manage/dutyliststatisc.jsp?userId="+userSeqId+"&beginTime="+beginTime+"&endTime="+endTime;

}
//根据人员的ids得到所有外出记录
function out(userIds,beginTime,endTime){
  var requestUrlOut = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageOutAct/selectOut.act?userId="+userIds+"&beginTime="+beginTime+"&endTime="+endTime;
  var rtJsonOut = getJsonRs(requestUrlOut);
  if(rtJsonOut.rtState == "1"){
    alert(rtJsonOut.rtMsrg); 
    return ;
    }
  var prcsJson = rtJsonOut.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tbody'><thead class='TableHeader'>"
       	+"<td nowrap align='center'>部门</td>"
        +"<td nowrap align='center'>申请人</td>"
      	+"<td nowrap align='center'>申请时间</td>"
        +"<td nowrap align='center'>外出原因</td>"
        +"<td nowrap align='center'>登记IP</td>"
        +"<td nowrap align='center'>外出日期</td>"
        +"<td nowrap align='center'>外出时间</td>"
        +"<td nowrap align='center'>归来时间</td>"
        +"<td nowrap align='center'>时长</td>"
        +"<td nowrap align='center'>审批人员</td>"
        +"<td nowrap align='center'>状态</td>"
        +"<td nowrap align='center'>操作</td>"
        +"</thead></tbody>");
    $('listOut').appendChild(table); 
    for(var i =0;i< prcsJson.length;i++){
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var userName = prcs.userName;
      var deptName = prcs.deptName;
      var leaderId = prcs.leaderId;
      var leaderName = prcs.leaderName;
      var createDate = prcs.createDate;
      var registerIp = prcs.registerIp;
      var outType = prcs.outType;
      var submitTime = prcs.submitTime;
      var outTime1 = prcs.outTime1;
      var outTime2 = prcs.outTime2;
      var deptName = prcs.deptName;
      var allow = prcs.allow;
      var status = prcs.status;
      var outStatus = "待批";
      if(allow=='1'&&status=='0'){
        outStatus = "审批";
      }
      if(allow=='2'&&status=='0'){
        outStatus = "未批";
      }
      if(allow=='1'&&status=='1'){
        outStatus = "已归来";
      }
      var str =  getStr(outTime1,outTime2);
      var tr = new Element('tr',{"class":"TableLine1"});
      $('tbody').appendChild(tr);
      tr.update("<td nowrap  align='center'>" + deptName + "</td>"
          +"<td nowrap align='center'>" + userName + "</td>"
        +"<td nowrap  align='center'>" + createDate + "</td>"
        + "<td align='center'>" +outType + "</td>"
        + "<td nowrap align='center'>" + registerIp + "</td>"
        + "<td nowrap align='center'>" + submitTime.substr(0,10) + "</td>"
        + "<td nowrap align='center'>" + outTime1 + "</td>"
        + "<td nowrap align='center'>" + outTime2 + "</td>"
        + "<td nowrap align='center'>" + str + "</td>"
        + "<td nowrap align='center'>" + leaderName + "</td>"
        + "<td nowrap align='center'>" + outStatus + "</td>"
        + "<td nowrap align='center'>"
      	+"<a href='javascript:deleteOut("+seqId+");'>删除</a>"
        +"</td>"
      );  
    }
  }else{
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"240"}).update("<tr>"
      + "<td class='msg info'>"
      + "<div class='content' style='font-size:12pt'>无外出记录</div>"
      + "</td></tr>"
       );
   $('listOut').appendChild(table);
  } 
}
function deleteOut(seqId){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendOutAct/deleteOutById.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  window.location.reload();
}
//计算时长
function getStr(outTime1,outTime2){
  var outTime1Int = strInt2(outTime1);
  var outTime2Int = strInt2(outTime2);
  var outTimeInt = outTime2Int-outTime1Int;
  var hour = parseInt(outTimeInt/60);
  var minute = (outTimeInt - (hour*60));
  var str = "";
  if(hour>0){
    str = hour+"小时";
  }else{
    str = "";
  }
  if(minute>0){
    minute = minute + "分";
  }else{
    minute = "";
  }
  str = str + minute;
  return str;
}
//计算时长
function getStr2(str){
  var hour = parseInt(str/3600,10);
  var minute = parseInt((str - (hour*3600))/60,10);
  var str = "";
  if(hour>0){
    str = hour+"小时";
  }else{
    str = "";
  }
  if(minute>0){
    minute = minute + "分";
  }else{
    minute = "";
  }
  str = str + minute;
  return str;
}
//计算时长两个时间段相差多少秒
function getStrChaInt(registerTime1,registerTime2){
  var Time1Int = strInt(registerTime1);
  var Time2Int = strInt(registerTime2);
  var TimeInt = Time2Int-Time1Int;
  return TimeInt;
}
//请假记录
function leave(userId,beginTime,endTime){
  var requestUrlLeave = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageLeaveAct/selectLeave.act?userId="+userId+"&beginTime="+beginTime+"&endTime="+endTime;
  var rtJsonLeave = getJsonRs(requestUrlLeave);
  if(rtJsonLeave == "1"){
    alert(rtJsonOut.rtMsrg); 
    return ;
    }
  var prcsJson =rtJsonLeave.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tbodyleave'>"
     +"<thead class='TableHeader'>"
     +"<td nowrap align='center'>部门</td>"
     +"<td nowrap align='center'>申请人</td>"
     +"<td nowrap align='center'>请假原因</td>"
     +"<td nowrap align='center'>登记IP</td>"
     +"<td nowrap align='center'>开始日期</td>"
     +"<td nowrap align='center'>结束日期</td>"
     +"<td nowrap align='center'>审批人员</td>"
     +"<td nowrap align='center'>状态</td>"
     +"<td nowrap align='center'>操作</td>"
     +"</thead></tbody>");
    $('listLeave').appendChild(table); 
    for(var i =0;i< prcsJson.length;i++){
      var tr = new Element('tr',{"class":"TableData"});
      $('tbodyleave').appendChild(tr);
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var userName = prcs.userName;
      var deptName = prcs.deptName;
      var leaderId = prcs.leaderId;
      var leaderName = prcs.leaderName;
      var leaveType = prcs.leaveType;
      var registerIp = prcs.registerIp;
      var destroyTime = prcs.destroyTime;
      var allow = prcs.allow;
      var leaveDate1 = prcs.leaveDate1;
      var leaveDate2 = prcs.leaveDate2;
      var reason = prcs.reason;
      var status = prcs.status;
      var annualLeave = prcs.annualLeave;
      var deptName = prcs.deptName;
      var leaveStatus = "待批";
      if(status=='1'&&allow=='1'){
        leaveStatus = "现行";
      }
      if(status=='1'&&allow=='2'){
        leaveStatus = "未批";
      }
      if(status=='1'&&allow=='3'){
        leaveStatus = "现行";
      }
      if(status=='2'&&allow=='3'){
        leaveStatus = "已销毁";
      }
      tr.update("<td nowrap align='center'>" + deptName + "</td>"
        + "<td nowrap align='center'>" + userName + "</td>"
        +"<td  align='center'>" + leaveType + "</td>"
        + "<td nowrap align='center'>" + registerIp + "</td>"
        + "<td nowrap align='center'>" + leaveDate1 + "</td>"
        + "<td nowrap align='center'>" + leaveDate2 + "</td>"
        + "<td nowrap align='center'>" + leaderName + "</td>"
        + "<td nowrap align='center'>" + leaveStatus + "</td>"
        + "<td nowrap align='center'>"
      	+"<a href='javascript:deleteLeave("+seqId+");'>删除</a>"
        +"</td>" );
    } 
  }else{
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"240"}).update("<tr>"
      + "<td class='msg info'>"
      + "<div class='content' style='font-size:12pt'>无请假记录</div>"
      + "</td></tr>"
       );
   $('listLeave').appendChild(table);
  }  
}
function deleteLeave(seqId){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendLeaveAct/deleteLeaveById.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  window.location.reload(); 
}
//出差
function evection(userId,beginTime,endTime){
  var requestUrlEvection = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageEvectionAct/selectEvection.act?userId="+userId+"&beginTime="+beginTime+"&endTime="+endTime;
  var rtJsonLeave = getJsonRs(requestUrlEvection);
  if(rtJsonLeave == "1"){
    alert(rtJsonOut.rtMsrg); 
    return ;
    }
  var prcsJson =rtJsonLeave.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tbodyEvection'><thead class='TableHeader'>"
        +"<td nowrap align='center'>部门</td>"
        +"<td nowrap align='center'>申请人</td>"
        +"<td nowrap align='center'>出差地点</td>"
        +"<td nowrap align='center'>登记IP</td>"
        +"<td nowrap align='center'>开始日期</td>"
        +"<td nowrap align='center'>结束日期</td>"
        +"<td nowrap align='center'>审批人员</td>"
        +"<td nowrap align='center'>状态</td>"
        +"<td nowrap align='center'>操作</td>"
     +"</thead></tbody>");
    $('listEvection').appendChild(table); 
    for(var i =0;i< prcsJson.length;i++){
      var tr = new Element('tr',{"class":"TableData"});
      $('tbodyEvection').appendChild(tr);
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var userName = prcs.userName;
      var deptName = prcs.deptName;
      var leaderId = prcs.leaderId;
      var leaderName = prcs.leaderName;
      var evectionDate1 = prcs.evectionDate1;
      var evectionDate2 = prcs.evectionDate2;
      var registerIp = prcs.registerIp;
      var evectionDest = prcs.evectionDest;
      var allow =prcs.allow;
      var status = prcs.status;
      var reason = prcs.reason;
      var evectionStatus = "在外";
      if(status=='1'&&allow=='0'){
        evectionStatus = "待批";
      }
      if(status=='1'&&allow=='1'){
        evectionStatus = "现行";
      }
      if(status=='2'&&allow=='1'){
        evectionStatus = "归来";
      }
      tr.update("<td nowrap align='center'>" + deptName + "</td>"
          + "<td nowrap align='center'>" + userName + "</td>"
          +"<td align='center'>" + evectionDest + "</td>"
          + "<td nowrap align='center'>" + registerIp + "</td>"
          + "<td nowrap align='center'>" + evectionDate1.substr(0,10) + "</td>"
          + "<td nowrap align='center'>" + evectionDate2.substr(0,10) + "</td>"
          + "<td nowrap align='center'>" + leaderName + "</td>"
          + "<td nowrap align='center'>" + evectionStatus + "</td>"
          + "<td nowrap align='center'>"
        	+"<a href='javascript:deleteEvection("+seqId+");'>删除</a>"
          +"</td>" );
    }
  }else{
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"240"}).update("<tr>"
        + "<td class='msg info'>"
        + "<div class='content' style='font-size:12pt'>无出差记录</div>"
        + "</td></tr>"
         );
     $('listEvection').appendChild(table);
  }
}
function deleteEvection(seqId){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendEvectionAct/deleteEvectionById.act?seqId=" + seqId;
   var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  window.location.reload();
}
function strInt(str){
  var min = 0;
  var max = 24*3600;
  var strInt;
  var strInt1;
  var strInt2;
  var strArray = str.split(":");
  for(var i = 0 ; i<strArray.length; i++){
    if(i==0){
      strInt1 = parseInt(strArray[i]*3600,10);
    }else if(i==1){
      strInt2 = parseInt(strArray[i]*60,10); 
    }  
  }
  strInt = strInt1+strInt2+parseInt(strArray[2]);
  return strInt;
}
function strInt2(str){
  var strInt;
  var strInt1;
  var strInt2;
  var strArray = str.split(":");
  for(var i = 0 ; i<strArray.length; i++){
    if(i==0){
      strInt1 = parseInt(strArray[i]*60,10);
    }else if(i==1){
      strInt2 = parseInt(strArray[i],10); 
    }  
  }
  
  strInt = strInt1+strInt2;
  return strInt;
}
function ToAllUser(){
  var beginTime = '<%=beginTime%>';
  var endTime = '<%=endTime%>';
  var userId = '<%=userId%>';
  var dutyType = '<%=dutyType%>';
  var deptId = '<%=deptId%>';
  var userIds = selectUserIds(userId,deptId);
  var userSeqId = selectuserIdByDutyType(userIds,dutyType);
  window.location.href = "<%=contextPath%>/core/funcs/attendance/manage/alluserdutylist.jsp?userIds="+userSeqId+"&beginTime="+beginTime+"&endTime="+endTime+"&dutyType="+dutyType;
}
function returnBefore(){
  window.location.href = "<%=contextPath%>/core/funcs/attendance/manage/statisticindex.jsp";
}
function exprot(expType){
  var beginTime = '<%=beginTime%>';
  var endTime = '<%=endTime%>'; 
  var userIds = $("userIds").value;
  window.location.href = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/exportCVS.act?expType="+expType+"&userId="+userIds+"&beginTime="+beginTime+"&endTime="+endTime;;
}
function exprotAttend(){
  var beginTime = '<%=beginTime%>';
  var endTime = '<%=endTime%>'; 
  var userIds = $("userIds").value;
  var dutyType = '<%=dutyType%>';
  var days = '<%=days%>';
  window.location.href = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/exprotAttendCVS.act?dutyType="+dutyType+"&userId="+userIds+"&beginTime="+beginTime+"&endTime="+endTime+"&days="+days;
}
</script>
</head>

<body class="" topmargin="5" onload="doOnload();">


<!-- 上下班 -->
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 上下班统计（从<%=beginTimeStr %>至<%=endTimeStr %>共<%=daySpace %>天）
    </span>
    <input type="button" value="导出Excel" class="BigButtonW" onclick="exprotAttend();"  title="导出下班统计信息" name="button">
    &nbsp;<input type="button" value="所有人详细记录" class="BigButtonC" onclick="ToAllUser();" title="所有人详细记录" name="button">
    </td>
  </tr>
</table>

<table class="TableList" width="95%"  >
 <tbody id="userTable">
  <tr class="TableHeader">
    <td nowrap align="center">部门</td>
    <td nowrap align="center" >姓名</td>
    <td nowrap align="center">全勤(天)</td>
    <td nowrap align="center">时长</td>
    <td nowrap align="center">迟到</td>
    <td nowrap align="center">上班未登记</td>
    <td nowrap align="center">早退</td>
    <td nowrap align="center">下班未登记</td>
    <td nowrap align="center">加班上班登记</td>
    <td nowrap align="center">加班下班登记</td>
    <td nowrap align="center">操作</td>
  </tr>
  </tbody>
</table>

<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
<!-- 外出记录 -->


<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 外出记录</span>
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="导出" class="BigButton" onclick="exprot(1);" title="导出外出记录" name="button">
    </td>
  </tr>
</table>
<div id="listOut"></div>
<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
<!-- 请假记录 -->


<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 请假记录</span>
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="导出" class="BigButton" onclick="exprot(2);" title="导出请假记录" name="button">
    </td>
  </tr>
</table>
<div id="listLeave"></div>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>


<!-- 出差记录 -->


<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 出差记录</span>
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="导出" class="BigButton" onclick="exprot(3);" title="导出出差记录" name="button">
    </td>
  </tr>
</table>
<div id="listEvection" ></div>
<br>

<div align="center">
  <input type="hidden" id="userIds" name = "userIds" value=""></input>
  <input type="button"  value="返回" class="BigButton" onClick="returnBefore();">
</div>

</body>
</html>