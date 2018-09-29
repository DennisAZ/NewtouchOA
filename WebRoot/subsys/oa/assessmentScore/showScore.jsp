<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
   seqId = "";
  }
  String deptId = request.getParameter("deptId");
  if (deptId == null) {
    deptId = "";
  }
  String userName = request.getParameter("userName");
  if (userName == null) {
    userName = "";
  }
  String userId = request.getParameter("userId");
  if (userId == null) {
    userId = "";
  }
  
  String bindUserOther = YHSysProps.getString("BIND_USERS_OTHERS");
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  boolean isAdmin = loginUser.isAdmin();
  
  Date date = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormatWeek = new SimpleDateFormat("E");
  SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
  String week = dateFormatWeek.format(date);
  String dateStr = dateFormat.format(date);
  int year = Integer.parseInt(dateStr.substring(0,4));
  int year1 = Integer.parseInt(dateStr.substring(0,4));
  int month = Integer.parseInt(dateStr.substring(5,7));
  Calendar time=Calendar.getInstance(); 
  time.clear(); 
  time.set(Calendar.YEAR,year); //year 为 int 
  time.set(Calendar.MONTH,month-1);//注意,Calendar对象默认一月为0           
  int maxDay=time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数 
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
%>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  XHTML  1.0  Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var year = "<%=year%>";
var year1 = "<%=year1%>";
var month = "<%=month%>";
var seqId = "<%=seqId%>";
var userName = "<%=userName%>";
function My_Submit(){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  window.location="<%=contextPath%>/subsys/oa/assessmentScore/showScore.jsp?year="+year+"&month="+month+"&seqId="+seqId+"&userName="+userName;
}

function My_Submit2(){
  var yearOnly = document.getElementById("yearOnly").value;
  var month = document.getElementById("month").value;
  window.location="<%=contextPath%>/subsys/oa/assessmentScore/showScore.jsp?yearOnly="+yearOnly+"&month="+month+"&seqId="+seqId+"&userName="+userName;
}

function str_int(str){
  var min = 0;
  var max = 24*3600;
  var strInt;
  var strInt1;
  var strInt2;
  var strArray = str.split(":");
  for(var i = 0 ; i<strArray.length; i++){
    if(i==0){
      strInt1 = parseInt(strArray[i]*3600,10);
    }else{
      strInt2 = parseInt(strArray[i]*60,10); 
    }  
  }
  strInt = strInt1+strInt2;
  return strInt;
}
function set_year(index){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  if(parseInt(year)<=2000){
    year = parseInt(year);
  }else if(parseInt(year)>=2049){
    year = parseInt(year);
  }else{
    year = parseInt(year)+parseInt(index);
  }
  window.location="<%=contextPath%>/subsys/oa/assessmentScore/showScore.jsp?year="+year+"&month="+month+"&seqId="+seqId+"&userName="+userName;
}

function setOnlyYear(index){
  var year = document.getElementById("yearOnly").value;
  var month = document.getElementById("month").value;
  if(parseInt(year)<=2000){
    year = parseInt(year);
  }else if(parseInt(year)>=2049){
    year = parseInt(year);
  }else{
    year = parseInt(year)+parseInt(index);
  }
  window.location="<%=contextPath%>/subsys/oa/assessmentScore/showScore.jsp?yearOnly="+year+"&month="+month+"&seqId="+seqId+"&userName="+userName;
}

function set_month(index){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  if(parseInt(month,10)+index<=0){
    year = parseInt(year)-1;
    month = 12;
  }else if(parseInt(month,10)+index>12){
    year = parseInt(year)+1;
    month = 1;
  }else{
    month = parseInt(month,10)+index;
  }
  window.location="<%=contextPath%>/subsys/oa/assessmentScore/showScore.jsp?year="+year+"&month="+month+"&seqId="+seqId+"&userName="+userName;
}

function DayNumOfMonth(Year,Month) {
  var d = new Date(Year,Month,0);   
  return d.getDate(); 
}

function doInit(){
  $("userName").innerHTML = userName;
  var url = "<%=contextPath%>/yh/subsys/oa/assessmentScore/act/YHAssessmentScoreAct/getMonthScore.act";
  var rtJson = getJsonRs(url, "year="+year+"&month="+month+"&userId="+seqId);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    $("monthScore").innerHTML = insertKiloSplit(data.monthScore,2);
    $("directorScore").innerHTML = insertKiloSplit(data.directorScore,2);
    $("attendScore").innerHTML = insertKiloSplit(data.attendScore,2);
    $("staffScore").innerHTML = insertKiloSplit(data.staffScore,2);
   
  }else{
    alert(rtJson.rtMsrg); 
  }
  getYearScore();
}

function getYearScore(){
  var url = "<%=contextPath%>/yh/subsys/oa/assessmentScore/act/YHAssessmentScoreAct/getYearScore.act";
  var rtJson = getJsonRs(url, "year="+year1+"&userId="+seqId);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    $("yearScore").innerHTML = insertKiloSplit(data.yearScore,2);
    $("monthScoreAvg").innerHTML = insertKiloSplit(data.monthScoreAvg,2);
    $("directorYearScore").innerHTML = insertKiloSplit(data.directorScore,2);
    $("staffYearScore").innerHTML = insertKiloSplit(data.staffScore,2);
  }else{
    alert(rtJson.rtMsrg); 
  }
}
</script>
<title>考核数据</title>
</head>

<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/score.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;考核分数统计(<span id="userName"></span>)</span>
    </td>
  </tr>
</table>
<br>
<a href="javascript:setOnlyYear(-1)";  title="上一年"><img  src="<%=imgPath%>/previouspage.gif"></img></a>
   <select id="yearOnly" name="yearOnly" style="height:22px;FONT-SIZE: 11pt;" onchange="My_Submit2();">
     <%
       for(int i = 2000; i < 2050; i++){
         if(i == year1){
     %>
     <option value="<%=i %>" selected="selected"><%=i %>年</option>
       <%}else{ %>
     <option value="<%=i %>"><%=i %>年</option>
       <%
           }
        }
       %>
   </select><a href="javascript:setOnlyYear(1);" class="ArrowButtonR" title="下一年"><img src="<%=imgPath%>/nextpage.gif"></img></a><span class="big3">年终考核分</span>
   <br>
   <br>
      <table class="TableBlock" width="95%">
    <tr class="TableHeader">
      <td nowrap align="center">年终考核总分</td>
      <td nowrap align="center">月考核平均分</td>
      <td nowrap align="center">年终考核分</td>
      <td nowrap align="center">年奖惩分</td>
    </tr>
    <tr >
      <td nowrap align="center" id="yearScore"></td>
      <td nowrap align="center" id="monthScoreAvg"></td>
      <td nowrap align="center" id="directorYearScore"></td>
      <td nowrap align="center" id="staffYearScore"></td>
    </tr>
</table>
   <br>
   <br>
<a href="javascript:set_year(-1)";  title="上一年"><img  src="<%=imgPath%>/previouspage.gif"></img></a>
   <select id="year" name="year" style="height:22px;FONT-SIZE: 11pt;" onchange="My_Submit();">
     <%
       for(int i=2000;i<2050;i++){
         if(i==year){
     %>
     <option value="<%=i %>" selected="selected"><%=i %>年</option>
       <%}else{ %>
     <option value="<%=i %>"><%=i %>年</option>
       <%
           }
        }
       %>
   </select><a href="javascript:set_year(1);" class="ArrowButtonR" title="下一年"><img src="<%=imgPath%>/nextpage.gif"></img></a>
<!-- 月  -->
   <a href="javascript:set_month(-1);" class="ArrowButtonL" title="上一月"><img src="<%=imgPath%>/previouspage.gif"></img></a><select id="month"  style="height:22px;FONT-SIZE: 11pt;"  name="month" onchange="My_Submit();">
     <%
       for(int i=1;i<13;i++){
         if(i>=10){
          if(i==month){
     %>
     <option value="<%=i %>" selected="selected"><%=i %>月</option>
        <%}else{ %>
     <option value="<%=i %>"><%=i %>月</option>
       <%
          }    
        }else{
          if(i==month){
       %>
       <option value="0<%=i %>" selected="selected">0<%=i %>月</option>
        <%}else{ %>
     <option value="0<%=i %>">0<%=i %>月</option>
       <%
        }
      }
    }
       %>
   </select><a href="javascript:set_month(1);" class="ArrowButtonR" title="下一月"><img src="<%=imgPath%>/nextpage.gif"></img></a><span class="big3">月考核分</span>
   <br>
   <br>
   <table class="TableBlock" width="95%">
    <tr class="TableHeader">
      <td nowrap align="center">月考核分</td>
      <td nowrap align="center">处长主观分</td>
      <td nowrap align="center">考勤分</td>
      <td nowrap align="center">奖惩分</td>
    </tr>
    <tr >
      <td nowrap align="center" id="monthScore"></td>
      <td nowrap align="center" id="directorScore"></td>
      <td nowrap align="center" id="attendScore"></td>
      <td nowrap align="center" id="staffScore"></td>
    </tr>
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="window.location.href='<%=contextPath %>/subsys/oa/fillRegister/attendScore/newRegister.jsp';"></center></body>
</html>