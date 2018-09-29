<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
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
  int day = Integer.parseInt(dateStr.substring(8,10));
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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>个人考勤统计</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var year = "<%=year%>";
var month = "<%=month%>";
var day = "<%=day%>";

function doInit(){
  var array = new Array("加班总时长","值班次数","外出次数","请假总时长","出差总时长","年休假");
  //var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getManageGroup.act";
  var flag = 0;
  var str = "";
    var table=new Element('table',{ "width":"80%","class":"TableBlock","align":"center"})
    .update("<tbody id='tbody'><tr class='TableHeader'>"
      + "<td nowrap width='30%' align='center'>个人考勤统计</td>"
      + "<td nowrap width='20%' align='center'>操作</td></tr><tbody>");
    $('listDiv').appendChild(table);
    var strs = "";
    var showStr = "";
    var flag = "";
    var tdFlag = "0";
    for(var i = 0; i < array.length; i++){
     if(i == 0){
      // flag = exitsMoneyMonth();
       tdFlag = "1";
       valueStr = getOverTime() + " 小时";
     }
     if(i == 1){
       tdFlag = "0";
       valueStr = getDuty() + " 次";
     }
     if(i == 2){
       tdFlag = "0";
       valueStr = getAttendOut() + " 次";
     }
     if(i == 3){
       tdFlag = "0";
       //if(flag == "0"){
         valueStr = getAttendLeaveHour() + " 小时";
       //}else{
       //  valueStr = getAttendLeaveHour() + " 小时"; //-getOverTimeMoney()
      // }
     }
     if(i == 4){
       tdFlag = "0";
       valueStr = getAttendEvectionHour() + " 小时";
     }
     if(i == 5){
       tdFlag = "0";
       valueStr = "请年休假" + getAnnualLeaveDays()+ "天,年休假剩余" + getAnnualOverplus() + "天";
     }
     var trColor = (i % 2 == 0) ? "TableLine1" : "TableLine2";
     var tr = new Element('tr',{'class':trColor});   
      table.firstChild.appendChild(tr);
          str = "<td align='center'>" + array[i] + "</td><td align='center'><a href=" 
          + "javascript:checkScore('" + i + "');"
          + ">" + valueStr + "</a>&nbsp;";
          /*
          if(tdFlag == "1"){
            if(flag == "0"){
              str += "<a href=" 
                + "javascript:leaveConvert();"
                + ">请假折抵</a>&nbsp;";
              str += "<a href=" 
                + "javascript:overtimeMoney();"
                + ">申请加班费</a>&nbsp;&nbsp;";
            }else{
              var status = getMoneyMonthStatus();
              if(status == "0"){
                str += "<font color='red'>已请假折抵</font>&nbsp;";
              }else{
                str += "<font color='red'>已申请加班费</font>&nbsp;";
              }
            }
           }*/
          str += "</td>";
      tr.update(str);
   }
}

function confirmConvert() {
  if(confirm("确定要请假折抵吗？折抵后将不可恢复")) {
    return true;
  } else {
    return false;
  }
}

function confirmMoney() {
  if(confirm("确定要申请加班费吗？申请后将不可恢复")) {
    return true;
  } else {
    return false;
  }
}

function getMoneyMonthStatus(){
  var url = contextPath + "/yh/subsys/oa/hr/moneyMonth/act/YHHrMoneyMonthAct/getMoneyMonthStatus.act";
  var rtJson = getJsonRs(url, "year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function leaveConvert(){
  if(!confirmConvert()) {
    return ;
   }  
  var overtimeHour = getOverTime();  //加班总时长
  var leaveHour =  getAttendLeaveHour();//请假总时长
  var url = contextPath + "/yh/subsys/oa/hr/moneyMonth/act/YHHrMoneyMonthAct/addMoneyMonthLeave.act";
  var rtJson = getJsonRs(url, "leaveHour="+leaveHour+"&overtimeHour="+overtimeHour+"&year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    window.location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function overtimeMoney(){
  if(!confirmMoney()) {
    return ;
   }  
  var overtimeMoney = getOverTimeMoney(); //加班工资数
  var dutyMoney = getDutyMoney();         //值班工资数
  var url = contextPath + "/yh/subsys/oa/hr/moneyMonth/act/YHHrMoneyMonthAct/addMoneyMonth.act";
  var rtJson = getJsonRs(url, "overtimeMoney="+overtimeMoney+"&dutyMoney="+dutyMoney+"&year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    window.location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
  
}

function getOverTimeHour(){
  var url = contextPath + "/yh/subsys/oa/hr/moneyMonth/act/YHHrMoneyMonthAct/exitsMoneyMonth.act";
  var rtJson = getJsonRs(url, "year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function exitsMoneyMonth(){
  var url = contextPath + "/yh/subsys/oa/hr/moneyMonth/act/YHHrMoneyMonthAct/exitsMoneyMonth.act";
  var rtJson = getJsonRs(url, "year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getOverTimeMoney(){
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendLeaveAct/getOverTimeHour.act";
  var rtJson = getJsonRs(url, "year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return insertKiloSplit(data, 2);
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getOverTime(){
  var url = contextPath + "/yh/custom/attendance/act/YHOvertimeRecordAct/getOverTimeHour.act";
  var rtJson = getJsonRs(url, "year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return insertKiloSplit(data, 1);
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getDuty(){
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/getAttendDutyCount.act";
  var rtJson = getJsonRs(url, "year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getDutyMoney(){
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/getAttendDutyMoney.act";
  var rtJson = getJsonRs(url, "year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return insertKiloSplit(data, 2);
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getAttendOut(){
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendOutAct/getAttendOutCount.act";
  var rtJson = getJsonRs(url, "year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getAttendLeaveHour(){
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendLeaveAct/getAttendLeaveHour.act";
  var rtJson = getJsonRs(url, "year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getAttendEvectionHour(){
  var url = contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendEvectionAct/getAttendEvectionHour.act";
  var rtJson = getJsonRs(url, "year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getAnnualOverplus(){
  var requestUrl = "<%=contextPath%>/yh/custom/attendance/act/YHAnnualLeaveAct/getAnnualOverplus.act";
  var rtJson = getJsonRs(requestUrl, "year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData.overplusDays;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getAnnualLeaveDays(){
  var requestUrl = "<%=contextPath%>/yh/custom/attendance/act/YHAnnualLeaveAct/getAnnualOverplus.act";
  var rtJson = getJsonRs(requestUrl, "year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData.leaveDays;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function My_Submit(){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  window.location="<%=contextPath%>/core/funcs/attendance/personal/manage.jsp?year="+year+"&month="+month;
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
  window.location="<%=contextPath%>/core/funcs/attendance/personal/manage.jsp?year="+year+"&month="+month;
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
  window.location="<%=contextPath%>/core/funcs/attendance/personal/manage.jsp?year="+year+"&month="+month;
}

/**
 * 打开新窗口  
 * @param url
 * @param width
 * @param height
 * @return
 */
function newRecordWindow(url,width,height){
  var myleft = (screen.availWidth - width)/2;
  var mytop = (screen.availHeight - height)/2;
  window.open(url, "meeting", 
      "height=600,width=800,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" + mytop + ", left=" + myleft + ", resizable=yes");
}

function checkScore(valueStr){
  if(valueStr == 0){
    var URL = "<%=contextPath%>/custom/attendance/personal/overtimetotal/index.jsp?year="+year+"&month="+month;
    //openDialogResize(URL,'820', '500');
    newRecordWindow(URL,'800','600');
  }
  if(valueStr == 1){
    var URL  = "<%=contextPath%>/custom/attendance/personal/duty/detailDuty.jsp?year="+year+"&month="+month;
    //openDialogResize(URL,'820', '500');
    newRecordWindow(URL,'800','600');
  }
  if(valueStr == 2){
    var URL = "<%=contextPath%>/core/funcs/attendance/personal/detailOut.jsp?year="+year+"&month="+month;
    //openDialogResize(URL,'820', '500');
    newRecordWindow(URL,'800','600');
  }
  if(valueStr == 3){
    var URL = "<%=contextPath%>/core/funcs/attendance/personal/detailLeave.jsp?year="+year+"&month="+month;
    //openDialogResize(URL,'820', '500');
    newRecordWindow(URL,'800','600');
  }
  if(valueStr == 4){
    var URL = "<%=contextPath%>/core/funcs/attendance/personal/detailEvection.jsp?year="+year+"&month="+month;
    //openDialogResize(URL,'820', '500');
    newRecordWindow(URL,'800','600');
  }
  if(valueStr == 5){
    var URL = "<%=contextPath%>/custom/attendance/personal/annualleave/detailLeave.jsp?year="+year+"&month="+month;
    //openDialogResize(URL,'820', '500');
    newRecordWindow(URL,'800','600');
  }
}

/**
 * 详细信息
 * @param seqId
 * @return
 */
function recordDetailFunc(seqId){
  var URL = contextPath + "/subsys/oa/training/record/recorddetail.jsp?seqId=" + seqId;
  //openDialogResize(URL,'820', '500');
  newRecordWindow(URL,'800','600');
}

/**
 * 打开新窗口  
 * @param url
 * @param width
 * @param height
 * @return
 */
function newRecordWindow(url,width,height){
  var myleft = (screen.availWidth - width)/2;
  var mytop = (screen.availHeight - height)/2;
  window.open(url, "meeting", 
      "height=600,width=800,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" + mytop + ", left=" + myleft + ", resizable=yes");
}

function support(groupNames, seqId){
  var URL = "/yh/core/funcs/system/address/manage/support.jsp?seqId="+seqId+"&groupName="+encodeURIComponent(groupNames);
  openDialog(URL,'480', '280');
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" align="absmiddle"><span class="big3">&nbsp;个人考勤统计</span><br>
    </td>
  </tr>
</table>

<br>
<a href="javascript:set_year(-1)";  title="上一年"><img  src="<%=imgPath%>/previouspage.gif"></img></a>
   <select id="year" name="year" style="height:22px;FONT-SIZE: 11pt;" onchange="My_Submit();">
     <%
       for(int i = 2000; i < 2050; i++){
         if(i == year){
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
       for(int i = 1; i < 13; i++){
         if(i >= 10){
          if(i == month){
     %>
     <option value="<%=i %>" selected="selected"><%=i %>月</option>
        <%}else{ %>
     <option value="<%=i %>"><%=i %>月</option>
       <%
          }    
        }else{
          if(i == month){
       %>
       <option value="0<%=i %>" selected="selected">0<%=i %>月</option>
        <%}else{ %>
     <option value="0<%=i %>">0<%=i %>月</option>
       <%
        }
      }
    }
       %>
   </select><a href="javascript:set_month(1);" class="ArrowButtonR" title="下一月"><img src="<%=imgPath%>/nextpage.gif"></img></a><span class="big3"></span>
<br>
<br>
<div id="listDiv" align="center"></div>
</body>
</html>