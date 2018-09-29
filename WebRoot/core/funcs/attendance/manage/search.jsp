<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String userId = request.getParameter("userId");
  String beginTime = request.getParameter("beginTime");
  String endTime = request.getParameter("endTime");
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
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/attendance/manage/js/statisticsQueryLogic.js"></script>
<script type="text/javascript">
var userId = '<%=userId%>';
var beginTime = '<%=beginTime%>';
var endTime = '<%=endTime%>';
var days = '<%=days%>';
function doOnload(){
  days = days.split(",");
  //得到排版类型
  configFunc(userId,beginTime,endTime,days);
  selectUserInfo(userId);
  //上下班登记情况
  //config(userId);
  //外出
  out(userId);
  //请假
  leave(userId);
  //出差
  evection(userId);
  fillRegister();
}

//得到排班类型
function configFunc(userId,beginTime,endTime,days){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectDutyByUserIdName.act?userId="+userId;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs= rtJson.rtData;
  if(prcs.length){
    var prcsJson = prcs[0];
    var seqId = prcsJson.seqId;
    var dutyName = prcsJson.dutyName;
    var dutyTime1 = prcsJson.dutyTime1;
    var dutyTime2 = prcsJson.dutyTime2;
    var dutyTime3 = prcsJson.dutyTime3;
    var dutyTime4 = prcsJson.dutyTime4;
    var dutyTime5 = prcsJson.dutyTime5;
    var dutyTime6 = prcsJson.dutyTime6;
    var dutyType1 = prcsJson.dutyType1;
    var dutyType2 = prcsJson.dutyType2;
    var dutyType3 = prcsJson.dutyType3;
    var dutyType4 = prcsJson.dutyType4;
    var dutyType5 = prcsJson.dutyType5;
    var dutyType6 = prcsJson.dutyType6;
    var dutyStatus1 = prcsJson.dutyStatus1;
    var dutyStatus2 = prcsJson.dutyStatus2;
    var dutyStatus3 = prcsJson.dutyStatus3;
    var dutyStatus4 = prcsJson.dutyStatus4;
    var dutyStatus5 = prcsJson.dutyStatus5;
    var dutyStatus6 = prcsJson.dutyStatus6;
    var general = prcsJson.general;
    var dutyTypeNames = ["上班登记","下班登记"];
    //建表
    var table = new Element('table',{"class":"TableList" , "width":"100%" ,"align":"center"});
    $("dutyTable").appendChild(table);
    var trTempTitle = "<tbody id='tbody'><tr id='tr_title'  class='TableHeader' align='center'><td nowrap align='center'>日期</td>";
    for(var i = 1; i <= 6; i++){
      if(i==1){
        dutyTime = dutyTime1;
        dutyType = dutyType1;
      }
      if(i==2){
        dutyTime = dutyTime2;
        dutyType = dutyType2;
      }
      if(i==3){
        dutyTime = dutyTime3;
        dutyType = dutyType3;
      }
      if(i==4){
        dutyTime = dutyTime4;
        dutyType = dutyType4
      }
      if(i==5){
        dutyTime = dutyTime5;
        dutyType = dutyType5;
      }
      if(i==6){
        dutyTime = dutyTime6;
        dutyType = dutyType6;
      }
      if(dutyTime.trim()==''){ continue;}
      trTempTitle = trTempTitle + "<td  nowrap align='center'>" +dutyTypeNames[parseInt(dutyType)-1]+"("+dutyTime+")</td>";
    }
    trTempTitle = trTempTitle + "</tr></tbody>";
    var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/getUserDutyInfo.act?userId="+userId+"&days="+days;
    var rtJson = getJsonRs(URL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    var prcs= rtJson.rtData;
    table.update(trTempTitle+prcs.trTemp);
  }
}


function getDayOfWeek(dateStr){
  var day = new Date(Date.parse(dateStr.replace(/-/g, '/'))); //将日期值格式化
  //day.getDay();根据Date返一个星期中的某其中0为星期日
  return day.getDay(); 
}
//得到上下班情况

function attendDuty(userId,date,registerType){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectDutyByDay.act?userId="+userId+"&date="+date+"&registerType="+registerType;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs= rtJson.rtData;
  return prcs;
}
//判断是否是节假日
function holiday(userId,date){
  var requestURLHoliday = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/checkHoliday.act?date="+date;
  var rtJson = getJsonRs(requestURLHoliday);
  var holidayJson = rtJson.rtData;
  var holiday = holidayJson.status;//0为公假日
  if(holiday=="1"){
    return false;
  }
  return true;
}
//判断是否为公休日
function generalIs(general,date){
  var generals = '';
  if(general!=''){
    generals = general.split(',');
  }
  var date = date.substr(5,2)+"/"+date.substr(8,2)+"/"+date.substr(0,4);
  var d=new Date(date);   
  var week = d.getDay();
  if(week==0){
    week = 7;
  }
  for(var i = 0;i<generals.length;i++){
    if(week==generals[i]){
      return true;
    }
  }
  return false; 
}
//判断是否为出差时间

function evection(userId,date){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/isEvection.act?date="+date+"&userId="+userId;
  var rtJson = getJsonRs(requestURL);
  var data = rtJson.rtData;
  if(data.evection=="1"){//0为出差日
    return false;
  }
  return true;
}
//判断是否为请假时间段
function leave(userId,date){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/isLeave.act?date="+date+"&userId="+userId;
  var rtJson = getJsonRs(requestURL);
  var data = rtJson.rtData;
  if(data.evection=="1"){//0为请假

    return false;
  }
  return true;
}
//判断是否在外出时间段
function out(userId,date){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/isOut.act?date="+date+"&userId="+userId;
  var rtJson = getJsonRs(requestURL);
  var data = rtJson.rtData;
  if(data.out=="1"){//0为请假

    return false;
  }
  return true;
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
function remark(seqId){
  var URL="<%=contextPath%>/core/funcs/attendance/personal/dutyRemark.jsp?seqId=" + seqId ;
  myleft=(screen.availWidth-650)/2;
  window.open(URL,"formul_edit","height=250,width=450,status=0,toolbar=no,menubar=no,location=no,scrollbars=no,top=150,left="+myleft+",resizable=yes");
}

function selectUserInfo(userId){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectUserInfo.act?userId="+userId;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  var userName = prc.userName;
  document.getElementById("userName").innerHTML = userName;
}
//得到排班类型
function config(userId){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectDutyByUserIdNameTemp.act?userId="+userId+"&days="+days;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs= rtJson.rtData;
  if(prcs.dutyOnTotal){
    var perfectCount = prcs.perfectCount;
    var dutyOnTotal = prcs.dutyOnTotal;
    var dutyOffTotal = prcs.dutyOffTotal;
    var lateCount = prcs.lateCount;
    var earlyCount = prcs.earlyCount;
    var addDutyOnCount = prcs.addDutyOnCount;
    var addDutyOffCount = prcs.addDutyOffCount;
 
    document.getElementById("perfectCount").innerHTML = perfectCount;
    document.getElementById("dutyOnTotal").innerHTML = dutyOnTotal;
    document.getElementById("dutyOffTotal").innerHTML = dutyOffTotal;
    document.getElementById("lateCount").innerHTML = lateCount;
    document.getElementById("earlyCount").innerHTML = earlyCount;
    document.getElementById("addDutyOnCount").innerHTML = addDutyOnCount;
    document.getElementById("addDutyOffCount").innerHTML = addDutyOffCount;
  }

}
//得到外出审批
function out(userId){
  var requestUrlOut = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageOutAct/selectOut.act?userId="+userId+"&beginTime="+beginTime+"&endTime="+endTime;
  var rtJsonOut = getJsonRs(requestUrlOut);
  if(rtJsonOut.rtState == "1"){
    alert(rtJsonOut.rtMsrg); 
    return ;
    }
  var prcsJson = rtJsonOut.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"100%"}).update("<tbody id = 'tbodyOut'><thead class='TableHeader'>"
      	+"<td nowrap align='center'>申请时间</td>"
        +"<td nowrap align='center'>外出原因</td>"
        +"<td nowrap align='center'>登记IP</td>"
        +"<td nowrap align='center'>外出日期</td>"
        +"<td nowrap align='center'>外出时间</td>"
        +"<td nowrap align='center'>归来时间</td>"
        +"<td nowrap align='center'>审批人员</td>"
        +"<td nowrap align='center'>状态</td>"
        +"<td nowrap align='center'>操作</td>"
        +"</thead></tbody>");
    $('listOut').appendChild(table); 
    for(var i =0;i< prcsJson.length;i++){
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
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
      var tr = new Element('tr',{"class":"TableLine1"});
      $('tbodyOut').appendChild(tr);
      tr.update("<td align='center'>" + createDate + "</td>"
        + "<td  align='center'>" +outType + "</td>"
        + "<td nowrap align='center'>" + registerIp + "</td>"
        + "<td nowrap align='center'>" + submitTime.substr(0,10) + "</td>"
        + "<td nowrap align='center'>" + outTime1 + "</td>"
        + "<td nowrap align='center'>" + outTime2 + "</td>"
        + "<td nowrap align='center'>" + leaderName + "</td>"
        + "<td nowrap align='center'>" + outStatus + "</td>"
        + "<td nowrap align='center'>"
        +"<a href='javascript:editOut("+seqId+");' title='仅OA管理员可以修改'>修改 </a>&nbsp;"
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
function editOut(seqId){
  URL="<%=contextPath%>/core/funcs/attendance/manage/editout.jsp?seqId="+seqId+"&userId="+userId;
  myleft=(screen.availWidth-780)/2;
  mytop=100;
  mywidth=650;
  myheight=400;
  window.open(URL,"out_edit","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}
//得到请假记录
function leave(userId){
  var requestUrlLeave = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageLeaveAct/selectLeave.act?userId="+userId+"&beginTime="+beginTime+"&endTime="+endTime;
  var rtJsonLeave = getJsonRs(requestUrlLeave);
  if(rtJsonLeave == "1"){
    alert(rtJsonOut.rtMsrg); 
    return ;
    }
  var prcsJson =rtJsonLeave.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"100%"}).update("<tbody id = 'tbodyleave'>"
     +"<thead class='TableHeader'>"
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
      tr.update("<td align='center'>" + leaveType + "</td>"
        + "<td nowrap align='center'>" + registerIp + "</td>"
        + "<td nowrap align='center'>" + leaveDate1 + "</td>"
        + "<td nowrap align='center'>" + leaveDate2 + "</td>"
        + "<td nowrap align='center'>" + leaderName + "</td>"
        + "<td nowrap align='center'>" + leaveStatus + "</td>"
        + "<td nowrap align='center'>"
        +"<a href='javascript:editLeave("+seqId+");' title='仅OA管理员可以修改'>修改 </a>&nbsp;"
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
function editLeave(seqId){
  URL="<%=contextPath%>/core/funcs/attendance/manage/editleave.jsp?seqId="+seqId;
  myleft=(screen.availWidth-780)/2;
  mytop=100;
  mywidth=650;
  myheight=400;
  window.open(URL,"leave_edit","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}
//出差
function evection(userId){
  var requestUrlEvection = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageEvectionAct/selectEvection.act?userId="+userId+"&beginTime="+beginTime+"&endTime="+endTime;
  var rtJsonLeave = getJsonRs(requestUrlEvection);
  if(rtJsonLeave == "1"){
    alert(rtJsonOut.rtMsrg); 
    return ;
    }
  var prcsJson =rtJsonLeave.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"100%"}).update("<tbody id = 'tbodyEvection'><thead class='TableHeader'>"
        +"<td nowrap align='center'>出差地点</td>"
        +"<td nowrap align='center'>出差原因</td>"
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
      if(status=='2'&&allow=='1'){
        evectionStatus = "归来";
      }
      tr.update("<td  align='center'>" + evectionDest + "</td>"
          + "<td align='center'>" + reason + "</td>"
          + "<td nowrap align='center'>" + registerIp + "</td>"
          + "<td nowrap align='center'>" + evectionDate1.substr(0,10) + "</td>"
          + "<td nowrap align='center'>" + evectionDate2.substr(0,10) + "</td>"
          + "<td nowrap align='center'>" + leaderName + "</td>"
          + "<td nowrap align='center'>" + evectionStatus + "</td>"
          + "<td nowrap align='center'>"
          +"<a href='javascript:editEvection("+seqId+");' title='仅OA管理员可以修改'>修改 </a>&nbsp;"
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

function fillRegister(){
  var assessingStatus = "1";
  var url = "<%=contextPath%>/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/getRegisterApprovalPassJson.act?assessingStatus="+assessingStatus+"&beginTime="+beginTime+"&endTime="+endTime;;
  var cfgs = {
      dataAction: url,
      container: "fillRegister",
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
      showCntrl('fillRegister');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无补登记记录', 'msrg');
    }
}

/**
 * 查看页面格局css操作
 * @return
 */
function ShowDialog(){
  $("apply").style.left = (parseInt(document.body.clientWidth) - parseInt($("apply").style.width))/2;
  $("apply").style.top = 150;
  $("overlay").style.width = document.body.clientWidth;
  if(parseInt(document.body.scrollHeight) < parseInt(document.body.clientHeight)){
     $("overlay").style.height = document.body.clientHeight;
  }else{
   //$("overlay").style.height = document.body.scrollHeight;
   $("overlay").style.display = 'block';
   $("apply").style.display = 'block';
  }
  window.scroll(0,0);
}

function HideDialog(){
  var div = $('apply');
  div.style.display = "none";
  var overlay = $('overlay');
  overlay.style.display = "none";
}

function doSubmit(){
  if($("assessingView").value == ""){ 
    alert("请填写审批意见！");
    $("assessingView").focus();
 return false;
  }
  var pars = Form.serialize($('form1'));
  var url = contextPath + "/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/updateStatus.act";
  var rtJson = getJsonRs(url, pars);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    location = "<%=contextPath %>/subsys/oa/fillRegister/approval/approval.jsp?data="+data;
  } else {
    alert(rtJson.rtMsrg); 
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
function editEvection(seqId){
  URL="<%=contextPath%>/core/funcs/attendance/manage/editevection.jsp?seqId="+seqId;
  myleft=(screen.availWidth-780)/2;
  mytop=100;
  mywidth=650;
  myheight=400;
  window.open(URL,"evection_edit","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
 }
function look(){
  var userId  = '<%=userId%>';
  var beginTime = '<%=beginTime%>';
  var endTime = '<%=endTime%>';
  window.location.href="<%=contextPath%>/core/funcs/attendance/manage/dutylist.jsp?userId="+userId+"&beginTime="+beginTime+"&endTime="+endTime;
}
</script>
</head>

<body class="" topmargin="5" onload="doOnload();">


<!------------------------------------- 上下班 ------------------------------->
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 上下班统计
    （<span id="userName"></span>,从<%=beginTimeStr %>至<%=endTimeStr %>共<%=daySpace %>天）
    </span><br>
    </td>
  </tr>
</table>

<br>
<div id="dutyTable"></div>
<br>

<!--  
<table class="TableList"  width="95%">
  <tr class="TableHeader">
    <td nowrap align="center">全勤(天)</td>
    <td nowrap align="center">迟到</td>
    <td nowrap align="center">上班未登记</td>
    <td nowrap align="center">早退</td>
    <td nowrap align="center">下班未登记</td>
    <td nowrap align="center">加班上班登记</td>
    <td nowrap align="center">加班下班登记</td>
  </tr>
  <tr class="TableData">
    <td nowrap align="center" id="perfectCount"></td>
    <td nowrap align="center" id="lateCount"></td>
    <td nowrap align="center" id="dutyOnTotal"></td>
    <td nowrap align="center" id="earlyCount"></td>
    <td nowrap align="center" id="dutyOffTotal"></td>
    <td nowrap align="center" id="addDutyOnCount"></td>
    <td nowrap align="center" id="addDutyOffCount"></td>
  </tr> 
  <tr class="TableControl">
    <td align="center" colspan=7>
    	<input type="button" value="查看上下班登记详情" class="BigButtonC"  onClick="look();">
    </td>
  </tr>
</table>
-->
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>

<!------------------------------------- 外出记录 ------------------------------->

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 外出记录</span><br>
    </td>
  </tr>
</table>
<div id="listOut" ></div>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
<!------------------------------------- 请假记录 ------------------------------->


<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 请假记录</span><br>
    </td>
  </tr>
</table>
<div id="listLeave"  ></div>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>


<!------------------------------------- 出差记录 ------------------------------->


<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 出差记录</span><br>
    </td>
  </tr>
</table>


<div id="listEvection" ></div>

<br>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 补登记记录</span><br>
    </td>
  </tr>
</table>


<div id="fillRegister"  style="display:none;width:100;"></div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
<td colspan="9">
  &nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
    <input type="button"  value="批量删除" class="BigButton" onClick="deleteAll()" title="删除所有卷库">
</td>
</tr>
</table>

</div>
<br>
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
<div align="center">
  <input type="button"  value="返回" class="BigButton" onClick="history.go(-1);">
</div>

</body>
</html>