<%@ page language="java"  import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.person.data.YHPerson,yh.core.funcs.calendar.data.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  Date date = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormatWeek = new SimpleDateFormat("E");
  SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
  date=dateFormat1.parse(request.getParameter("startTime"));
  String week = dateFormatWeek.format(date);
  String dateStr = dateFormat.format(date);
  int year = Integer.parseInt(dateStr.substring(0,4));
  int year1 = Integer.parseInt(dateStr.substring(0,4));
  int month = Integer.parseInt(dateStr.substring(5,7));
  int day = Integer.parseInt(dateStr.substring(8,10));
  Calendar time=Calendar.getInstance(); 
  time.clear(); 
  time.set(Calendar.YEAR,year); //year 为 int 
  time.set(Calendar.MONTH,month-1);//注意,Calendar对象默认一月为0           
  int maxDay = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数 
  
  //计算最后一天和第一天
  String start=year+"年"+month+"月"+"1日";
  String end=year+"年"+month+"月"+maxDay+"日";
  
  List<YHCalendar> calendarList = new ArrayList<YHCalendar>();
  calendarList= (List<YHCalendar>)request.getAttribute("calendarList");
  String status = "0";
  String yearOnly = request.getParameter("yearOnly");
  String yearStr = request.getParameter("year");
  String monthStr = request.getParameter("month");
  if(yearOnly!=null){
    year1 = Integer.parseInt(yearOnly);
  }
  if(yearStr!=null){
    year = Integer.parseInt(yearStr);
  }
  if(monthStr!=null){
    month = Integer.parseInt(monthStr);
  }
  String weekToDate = request.getParameter("date");
  if(weekToDate!=null){
    year = Integer.parseInt(weekToDate.substring(0,4));
    month = Integer.parseInt(weekToDate.substring(5,7));
  }
  String yearMonth = String.valueOf(year) + "-" + String.valueOf(month);
  String startDate=request.getParameter("startTime");
  String endDate=request.getParameter("endTime");
  startDate=startDate.substring(0,10);
  endDate=endDate.substring(0,10);
   
  //获取userId
  String userid=request.getParameter("userId");
  int userId = Integer.parseInt(userid);
  
  
  long daySpace = YHUtility.getDaySpan(dateFormat1.parse(request.getParameter("startTime")),dateFormat1.parse(request.getParameter("endTime")))+1;
  //得到到之间的天数数组
  List daysList = new ArrayList();
  String days = "";
  Calendar calendar = new GregorianCalendar();
  for(int i = 0;i<daySpace;i++){
    calendar.setTime(dateFormat1.parse(request.getParameter("startTime")));
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
<title>上下班考勤记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var year = "<%=year%>";
var month = "<%=month%>";
var days = '<%=days%>';
var userId = '<%=userId%>';

function doOnload(){
  configFunc(userId,days);
  doUserNameFunc();
}

//获取员工姓名
function doUserNameFunc(){ 
  var name='';
  var url = contextPath + "/yh/subsys/oa/hr/manage/act/YHHrStaffIncentiveAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=<%=userId%>");

  if (rtJson.rtState == "0") {
     name= rtJson.rtData ;
	
  } else {
    alert(rtJson.rtMsrg); 
  }
  
  $('user').innerHTML=name+",<%=startDate%> 至   <%=endDate%> 共<%=daySpace%>天";

}




//得到排班类型
function configFunc(userId,days){
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
    var URL = "<%=contextPath%>/yh/subsys/oa/hr/salary/report/act/YHHrReportAct/getUserDutyInfo.act?userId="+userId+"&days="+days;
    var rtJson = getJsonRs(URL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    var prcs= rtJson.rtData;
    table.update(trTempTitle+prcs.trTemp);
  }
}




function My_Submit(){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  window.location="selectduty.jsp?startTime="+year+"-"+month+"-01&userId=<%=userId%>";
}


</script>
</head>
<body class="" topmargin="5" onLoad="doOnload();">
<table id="selectTable" border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;上下班考勤统计(<span id="user"></span>)</span><br>
    </td>
  </tr>
</table>
<table id="resultTable" border="0" width="100%" cellspacing="0" cellpadding="3" class="small" style="display:none">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"/><span class="big3">&nbsp;上下班查询结果 [<span id="returndate1"></span> 至<span id="returndate2"></span> 共<span id="daySpace"></span>天]</span><br>
    </td>
  </tr>
</table>
<!--
<div id="selectDiv" align="center" class="big1">

<form action="#" id="form1" name="form1" onsubmit="return CheckForm();">
<b>
    起始日期: &nbsp;<input type="text" id="dutyDate1" name="dutyDate1" class="BigInput" size="10" maxlength="10" value="">
          <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
 &nbsp;
   截止日期: &nbsp;<input id="dutyDate2" type="text" name="dutyDate2" class="BigInput" size="10" maxlength="10" value="">
          <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
&nbsp;
</b>
  <input type="button" value="查询" onclick="Init();" class="BigButton" title="上下班记录查询">
</form>
</div>
-->

<br>
<div id="dutyTable"></div>
<br>
<div id="listDiv" align="center" style="display:none">
</div>
<div id="returnDiv" align="center" style="display:none">
<br></br>
  <input type="button" value="返回" class="BigButton" onClick="window.location.reload();"></input>
</div>
</body>
</html>