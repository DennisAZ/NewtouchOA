<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*,yh.core.funcs.calendar.act.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  Date date = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat  dateFormat2 = new SimpleDateFormat("yyyy年-MM月-w周-dd日");
  SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormat4 = new SimpleDateFormat("MM/dd");
  SimpleDateFormat dateFormatWeek = new SimpleDateFormat("E");
  Calendar c = Calendar.getInstance();
  String week = dateFormatWeek.format(date);
  String dateStr = dateFormat.format(date);
  int year = Integer.parseInt(dateStr.substring(0,4));
  int month = Integer.parseInt(dateStr.substring(5,7));
  int day = Integer.parseInt(dateStr.substring(8,10));
  //从日跳过来的
  String status = "0";
  String yearStr = request.getParameter("year");
  String monthStr = request.getParameter("month");
  String dayStr = request.getParameter("day");
  String statusStr = request.getParameter("statusStr");
  String maxDayStr = request.getParameter("maxDay");
  if(yearStr!=null){
    year = Integer.parseInt(yearStr);
    date =dateFormat1.parse(yearStr+"-"+monthStr+"-"+dayStr);
  }
  if(monthStr!=null){
    month = Integer.parseInt(monthStr);
    date =dateFormat1.parse(yearStr+"-"+monthStr+"-"+dayStr);
  }
  if(dayStr!=null){
    day = Integer.parseInt(dayStr);
    date =dateFormat1.parse(yearStr+"-"+monthStr+"-"+dayStr);
  }
  if(statusStr!=null){
    status = statusStr;
  }
  c.setTime(date);
  c.add(Calendar.DATE,-1) ;
  int weekthInt = c.get(Calendar.WEEK_OF_YEAR);
  c.add(Calendar.DATE,+1) ;

  //本身跳过来的
  String yearW = request.getParameter("yearW");
  String weekW = request.getParameter("weekW");
  String statusW = request.getParameter("statusW");
  if(yearW!=null){
    year = Integer.parseInt(yearW);
  }
  if(weekW!=null){
    weekthInt = Integer.parseInt(weekW);
  }
  if(statusW!=null){
    status = statusW;
  }
  //判断月份和星期几
  YHCalendarAct tca = new YHCalendarAct();
  Date beginDate ;
  Date endDate ;
  Calendar[] darr = tca.getStartEnd(year,weekthInt);
  beginDate = dateFormat1.parse(tca.getFullTimeStr(darr[0]));
  endDate = dateFormat1.parse(tca.getFullTimeStr(darr[1]));
  Calendar calendar = new GregorianCalendar();    
  calendar.setTime(beginDate);
  calendar.add(Calendar.DATE,+1) ;
  Date dateTemp2 = calendar.getTime();
  String date1 = dateFormat3.format(beginDate);
  String dateTempStr1 = dateFormat4.format(beginDate);
  String date2 = dateFormat3.format(dateTemp2);
  String dateTempStr2 = dateFormat4.format(dateTemp2);
  calendar.setTime(beginDate);
  calendar.add(Calendar.DATE,+2) ;
  Date dateTemp3 = calendar.getTime();
  String date3 = dateFormat3.format(dateTemp3);
  String dateTempStr3 = dateFormat4.format(dateTemp3);
  calendar.setTime(beginDate);
  calendar.add(Calendar.DATE,+3) ;
  Date dateTemp4 = calendar.getTime();
  String date4= dateFormat3.format(dateTemp4);
  String dateTempStr4 = dateFormat4.format(dateTemp4);
  calendar.setTime(beginDate);
  calendar.add(Calendar.DATE,+4) ;
  Date dateTemp5 = calendar.getTime();
  String date5= dateFormat3.format(dateTemp5);
  String dateTempStr5= dateFormat4.format(dateTemp5);
  calendar.setTime(beginDate);
  calendar.add(Calendar.DATE,+5) ;
  Date dateTemp6 = calendar.getTime();
  String date6= dateFormat3.format(dateTemp6);
  String dateTempStr6 = dateFormat4.format(dateTemp6);
  calendar.setTime(beginDate);
  calendar.add(Calendar.DATE,+6) ;
  Date dateTemp7 = calendar.getTime();
  String date7= dateFormat3.format(dateTemp7);
  String dateTempStr7 = dateFormat4.format(dateTemp7);
  //得到最大周
  int maxWeek = 53;//YHCalendarAct.getMaxWeekNumOfYear(year);
  //out.print(maxWeek+""+weekthInt);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>今天</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<style>
.DayCalendar{background:#D6E4EF;border:1px #E4ECF3 solid;position:relative;margin: 1px 0px;padding:0px 3px;}
           html {
                   overflow:auto;  /*这个可以去掉IE6,7的滚动*/
                   _overflow-x:hidden;/*去掉IE6横向滚动*/
                }

</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript">
var menuData1 = [{ name:'<div style="padding-top:5px;margin-left:10px">周事务<div>',action:setAction,extData:'1'}
,{ name:'<div style="padding-top:5px;margin-left:10px">撰写日志<div>',action:setAction,extData:'2'}
]
var menuData2 = [{ name:'<div style="padding-top:5px;margin-left:10px">全部<div>',action:set_status,extData:'0'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">未开始<div>',action:set_status,extData:'1'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">进行中<div>',action:set_status,extData:'2'}
,{ name:'<div style="color:#FF0000;padding-top:5px;margin-left:10px">已超时<div>',action:set_status,extData:'3'}
,{ name:'<div style="color:#00AA00;padding-top:5px;margin-left:10px">已完成<div>',action:set_status,extData:'4'}
]
var menuData3 = [{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看<div>',action:set_work,extData:'0'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">完成<div>',action:set_work,extData:'1'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">修改<div>',action:set_work,extData:'2'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">删除<div>',action:set_work,extData:'3'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看日志<div>',action:set_work,extData:'5'}
]
var menuData4 = [{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看<div>',action:set_work,extData:'0'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">未完成<div>',action:set_work,extData:'1'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">修改<div>',action:set_work,extData:'2'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">删除<div>',action:set_work,extData:'3'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看日志<div>',action:set_work,extData:'5'}
]
var menuData5 = [{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看<div>',action:set_work,extData:'0'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">完成<div>',action:set_work,extData:'1'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">回复微讯<div>',action:set_work_note,extData:'2'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看日志<div>',action:set_work,extData:'5'}
]
var menuData6 = [{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看<div>',action:set_work,extData:'0'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">未完成<div>',action:set_work,extData:'1'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">回复微讯<div>',action:set_work_note,extData:'2'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看日志<div>',action:set_work,extData:'5'}
]
function setAction(){
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  var status = document.getElementById("status").value;
  if(arguments[2]==1){
    var URL = "<%=contextPath%>/core/funcs/calendar/addcalendar.jsp?ldwmNewStatus=week&newStatus=0&year="+year+"&week="+week+"&status="+status;
    window.open(URL,"calendar","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=220,resizable=yes");
  }else if(arguments[2]==2){
    var date1 = '<%=date1%>';
    var URL = "<%=contextPath%>/core/funcs/calendar/addcalendardiary.jsp?ldwm=week&year="+year+"&date1="+date1;
    window.open(URL,"calendar","height=530,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=100,resizable=yes");
  }
}
function set_status(){
  var status = arguments[2];
  var statusName ;
  var statusNames = ['全部','未开始','进行中','已超时','已完成'];
  for(var i = 0;i<statusNames.length;i++){
    if(i==status){
      statusName = statusNames[i];  
    }
  }
  document.getElementById("status").value=status;
  document.getElementById("name").innerHTML=statusName;
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  window.location="<%=contextPath%>/core/funcs/calendar/week.jsp?yearW="+year+"&weekW="+week+"&statusW="+status;
}
function set_work(){
  var status = arguments[2];
  var seqId_overStatus=  arguments[1];
  var seqId = seqId_overStatus.split(',')[0].substr('cal_'.length);
  var overStatus = seqId_overStatus.split(',')[1];
  var week = document.getElementById("week").value;
  if(status=='0'){
    var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status="+status;
    window.open(URL,"my_note","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=300,resizable=yes");
  }
  if(status=='1'){
    if(overStatus=='0'){
      overStatus='1';
      var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+overStatus+"&ldwm=week";
      var rtJson = getJsonRs(URL);
      if(rtJson.rtState == "1"){
        alert(rtJson.rtMsrg); 
        return ;
      }
      document.location.reload();
      //var URL = "<%=contextPath%>/core/funcs/calendar/updatecalendarsuccess.jsp";
     // window.location.href = URL;
    }else{
      overStatus='0';
      var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+overStatus+"&ldwm=week";
      var rtJson = getJsonRs(URL);
      if(rtJson.rtState == "1"){
        alert(rtJson.rtMsrg); 
        return ;
      }
      document.location.reload();
    }
   
  }
  if(status=='2'){
    var URL = "<%=contextPath%>/core/funcs/calendar/editcalendar.jsp?seqId="+seqId+"&ldwm=week"+"&week"+week;
    window.location.href=URL;
  }
  if(status=='3'){
    var msg = "确认要删除此任务吗？";
    if(window.confirm(msg)){
      var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/deleteCalendarById.act?seqId="+seqId+"&ldwm=week"+"&week="+week;
      var rtJson = getJsonRs(URL);
      if(rtJson.rtState == "1"){
        alert(rtJson.rtMsrg); 
        return ;
      }
      document.location.reload();
    }
  }
  if(status=='4'){
    var URL = "<%=contextPath%>/core/funcs/calendar/addcalendardiary.jsp?seqId="+seqId;
    window.open(URL,"calendar","height=530,width=630,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=100,resizable=yes");
  }
  if(status=='5'){
    var URL = "<%=contextPath%>/core/funcs/calendar/showdiaryBytoday.jsp?seqId="+seqId;
    window.open(URL,"calendar","height=530,width=630,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=100,resizable=yes");
  }
}
function showMenu(event){
  var menu = new Menu({bindTo:$('new') , menuData:menuData1 , attachCtrl:true});
  menu.show(event);
}
function showMenuStatus(event){
  var menu = new Menu({bindTo:'statusName' , menuData:menuData2 , attachCtrl:true});
  menu.show(event);
}
function showMenuWork(event,seqId,overStatus){
  if(overStatus=='0'){
    var menu = new Menu({bindTo:'cal_'+seqId+','+overStatus , menuData:menuData3 , attachCtrl:true});
  }else{
    var menu = new Menu({bindTo:'cal_'+seqId+','+overStatus , menuData:menuData4 , attachCtrl:true});
  }
  menu.show(event,seqId,overStatus);
}

function set_work_note(){
  var seqId_overStatus=  arguments[1];
  var seqId = seqId_overStatus.split(',')[0].substr('cal_'.length);
  var URL = "<%=contextPath%>/core/funcs/calendar/revertnote.jsp?seqId="+seqId;
  window.open(URL,"my_note","height=220,width=450,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=350,top=200,resizable=yes");

}
function showMenuWork(event,seqId,overStatus,plan){
  //overStatus为2 是别人安排的 
  if(plan=='1'){
    if(overStatus=='0'){
      var menu = new Menu({bindTo:'cal_'+seqId+','+overStatus , menuData:menuData5 , attachCtrl:true});
    }else {
      var menu = new Menu({bindTo:'cal_'+seqId+','+overStatus , menuData:menuData6 , attachCtrl:true});
    }  
  }else{
    if(overStatus=='0'){
      var menu = new Menu({bindTo:'cal_'+seqId+','+overStatus , menuData:menuData3 , attachCtrl:true});
    }else{
      var menu = new Menu({bindTo:'cal_'+seqId+','+overStatus , menuData:menuData4 , attachCtrl:true});
    }
  }
  menu.show(event,seqId,overStatus,plan);
}
function r0(date)
{
	if(!date)
	{
		return;
	}
	if(date.substr(0, 1) == '0')
	{
		date = date.substr(1, date.length - 1)
	}
	return date;
}
function setWeekDate(date)
{
	if(!date)
	{
		return;
	}
	date = date.substr(0, 10);
	var ymd = date.split('-');
	var day = new Date();
	day.setFullYear(ymd[0], r0(ymd[1]) - 1, r0(ymd[2]));
	var result = day.getDay();
	if(result == 0)
	{
		result = 7;
	}
	return result;
}
function doOnload(){
  var year = '<%=year%>';
  var weekth = '<%=weekthInt%>';
  var status = '<%=status%>';
  var statusName = "全部";
  var statusNames = ['全部','未开始','进行中','已超时','已完成'];
  var colorTypes = ["","#0000FF","#0000FF","#FF0000","#00AA00"];
  var colorType = colorTypes[status];
  for(var i = 0;i<statusNames.length;i++){
    if(i==status){
      statusName = statusNames[i];  
    }
  }
  document.getElementById("status").value=status;
  document.getElementById("name").innerHTML=statusName;
  document.getElementById("name").style.color=colorType;
  document.getElementById("year").value=year;
  document.getElementById("week").value=weekth;
  //给是本周的标上颜色
  todayWeek();
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/selectCalendarByWeek.act?year="+year+"&weekth="+weekth+"&status="+status;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  //计算周宽度
  var th = $("tbl_header");
  var week_width = 0;
  if(!th){
  	return;
  }
  for(var j = 1; j < th.cells.length; j++){
    week_width += th.cells[j].offsetWidth;
  }
  if(prcs.length>0){

    var calLevelNames = ['未指定','重要/紧急','重要/不紧急','不重要/紧急','不重要/不紧急'];
    //判断是否跨天
    for(var i=0;i<prcs.length;i++){
      var prc = prcs[i];
      var seqId = prc.seqId;
      var overStatus = prc.overStatus;
      var status = prc.status;
      var userId = prc.userId;
      var calLevel = prc.calLevel;
      var calType = prc.calType
      var content = prc.content;
      var managerId = prc.managerId;
      var managerName = prc.managerName;
      var calTime = prc.calTime;
      var endTime = prc.endTime;
	  var start = setWeekDate(calTime);
	  var end = setWeekDate(endTime);
      if(overStatus.trim()==''){
        overStatus = 0;
      }
      if(calLevel.trim()==''){
        calLevel = 0;
      }
	  if(managerName!=''){
        managerName = "\n安排人："+managerName;
	  }
      var calLevelName = "未指定"
      var calTypeName = "类型: 工作事务";
	  var statusName = "状态: 进行中";
	  var statusStyle = "color:#0000FF";
	  var date1 = '<%=date1%>';
	  var date7 = '<%=date7%>';
	  if(calTime.substr(0,10)<=date1){
        start = 1;
	  }
	  if(endTime.substr(0,10)>=date7){
        end = 7;
      }
	  //计算偏移量和长度
	  var left = width = 0;
      for(var j = 1; j < start; j++){
        left += th.cells[j].offsetWidth;
      }
      for(var j = start; j <= end; j++){
        width += th.cells[j].clientWidth;
      }
      if(left + width > week_width - 6){
        width = week_width - left - 6;
      }
       // alert(left+":"+width);
      if(status=='1'){
        statusName = "状态: 未开始";
      }
      if(status=='2'){
        statusName = "状态: 已超时";
        statusStyle = "color:#FF0000";
      }
      if(overStatus=='1'){
        statusName = "状态: 已完成";
        statusStyle = "color:#00AA00";
      }
     
      for(var j = 0;j<calLevelNames.length;j++){
        if(calLevel==j){
          calLevelName = calLevelNames[j];
        }
      }
      if(calType=='2'){
        calTypeName = "个人事务";
      }
      var dayStatus = prc.dayStatus;
      if(managerId.trim()==''||userId==managerId.trim()){
        if(dayStatus=='1'){
          document.getElementById("spanweek").style.display="block";
          var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName +"\n" +  statusName + managerName+"'><a href='javascript:set_week(-1);' title='上一周'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");' style='"+statusStyle+";'>"+content+"</a> </div>"
          );
          $("spanWeekCalendar").appendChild(div);
        }
        if(dayStatus=='2'){
          document.getElementById("spanweek").style.display="block";
          var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName +"\n" + statusName +managerName+ "'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"+ calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_week(1);' title='下一周'><span style='font-family:Webdings'>4</span></a> </div>"
          );
          $("spanWeekCalendar").appendChild(div);
         }
        if(dayStatus=='3'){
          document.getElementById("spanweek").style.display="block";
          var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName +"\n" + statusName + managerName+"'><a href='javascript:set_week(-1);' title='上一周'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_week(1);' title='下一周'><span style='font-family:Webdings'>4</span></a> </div>"
          );
          $("spanWeekCalendar").appendChild(div);
         }
        if(dayStatus=='4'){
          document.getElementById("spanweek").style.display="block";
          var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName +"\n" + statusName + managerName+"'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"+ calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");' style='"+statusStyle+";'>"+content+"</a> </div>"
          );
          $("spanWeekCalendar").appendChild(div);
         }
      
      }else{

        if(dayStatus=='1'){
          document.getElementById("spanweek").style.display="block";
          var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName +"\n" +  statusName + managerName+"'><a href='javascript:set_week(-1);' title='上一周'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ",1);' style='"+statusStyle+";'>"+content+"</a> </div>"
          );
          $("spanWeekCalendar").appendChild(div);
        }
        if(dayStatus=='2'){
          document.getElementById("spanweek").style.display="block";
          var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName +"\n" + statusName +managerName+ "'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"+ calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ",1);' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_week(1);' title='下一周'><span style='font-family:Webdings'>4</span></a> </div>"
          );
          $("spanWeekCalendar").appendChild(div);
         }
        if(dayStatus=='3'){
          document.getElementById("spanweek").style.display="block";
          var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName +"\n" + statusName + managerName+"'><a href='javascript:set_week(-1);' title='上一周'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ",1);' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_week(1);' title='下一周'><span style='font-family:Webdings'>4</span></a> </div>"
          );
          $("spanWeekCalendar").appendChild(div);
         }
        if(dayStatus=='4'){
          document.getElementById("spanweek").style.display="block";
          var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName +"\n" + statusName + managerName+"'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"+ calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ",1);' style='"+statusStyle+";'>"+content+"</a> </div>"
          );
          $("spanWeekCalendar").appendChild(div);
         }
      
      }
      
   //没跨天
      if(dayStatus=='0'){
        //alert(calTime.substr(11,5));
        // alert(calTime.substr(0,10));
         var calTimeInt = str_int(calTime.substr(11,5));  
         var xingqiji = getDayOfWeek(calTime.substr(0,10));
         if(xingqiji==0){
           xingqiji = 7;
         }
        if(managerId.trim()==''||userId==managerId.trim()){

          for(var j=0;j<24;j++){
            //判断是否在00:00-07:00之间
            if(j<7){
              if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
                document.getElementById("front").style.display = '';
                var div = new Element('div').update("<div title='"
                     +calTypeName + "\n" + statusName + managerName+"'><span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>"
                     + calTime.substr(11,5) + " - "
                     + endTime.substr(11,5)+ "</span> "
                     + "<br><a id='cal_"
                     +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
                     + "onmouseover='showMenuWork(event," + seqId + ","+ overStatus+");' style='"+statusStyle+";'>"+content+"</a>"
                     +"</div>");
                $("td_0"+j+xingqiji).appendChild(div);
              }
            }
            //判断是否在07:00-10:00之间
            if(j>=7&&j<10){
              if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
                var div = new Element('div').update("<div title='"
                    +calTypeName + "\n" + statusName +managerName+ "'><span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>"
                    + calTime.substr(11,5) + " - "
                    + endTime.substr(11,5)+ "</span> "
                    + "<br><a id='cal_"
                    +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
                    + "onmouseover='showMenuWork(event," + seqId + ","+ overStatus+");' style='"+statusStyle+";'>"+content+"</a>"
                    +"</div>");
               $("td_0"+j+xingqiji).appendChild(div);
               }
            }
            //判断是否在10:00-24:00之间
            if(j>=10){
              if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
                var div = new Element('div').update("<div title='"
                    +calTypeName + "\n" + statusName +managerName+ "'><span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>"
                    + calTime.substr(11,5) + " - "
                    + endTime.substr(11,5)+ "</span> "
                    + "<br><a id='cal_"
                    +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
                    + "onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");' style='"+statusStyle+";'>" + content+"</a>"
                    + "</div>");
                $("td_"+j+xingqiji).appendChild(div);
              }
            }
          }
        }else{
          for(var j=0;j<24;j++){
            //判断是否在00:00-07:00之间
            if(j<7){
              if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
                document.getElementById("front").style.display = '';
                var div = new Element('div').update("<div title='"
                     +calTypeName + "\n" + statusName + managerName+"'><span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>"
                     + calTime.substr(11,5) + " - "
                     + endTime.substr(11,5)+ "</span> "
                     + "<br><a id='cal_"
                     +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
                     + "onmouseover='showMenuWork(event," + seqId + ","+ overStatus+",1);' style='"+statusStyle+";'>"+content+"</a>"
                     +"</div>");
                $("td_0"+j+xingqiji).appendChild(div);
              }
            }
            //判断是否在07:00-10:00之间
            if(j>=7&&j<10){
              if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
                var div = new Element('div').update("<div title='"
                    +calTypeName + "\n" + statusName +managerName+ "'><span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>"
                    + calTime.substr(11,5) + " - "
                    + endTime.substr(11,5)+ "</span> "
                    + "<br><a id='cal_"
                    +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
                    + "onmouseover='showMenuWork(event," + seqId + ","+ overStatus+",1);' style='"+statusStyle+";'>"+content+"</a>"
                    +"</div>");
               $("td_0"+j+xingqiji).appendChild(div);
               }
            }
            //判断是否在10:00-24:00之间
            if(j>=10){
              if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
                var div = new Element('div').update("<div title='"
                    +calTypeName + "\n" + statusName +managerName+ "'><span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>"
                    + calTime.substr(11,5) + " - "
                    + endTime.substr(11,5)+ "</span> "
                    + "<br><a id='cal_"
                    +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
                    + "onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ",1);' style='"+statusStyle+";'>" + content+"</a>"
                    + "</div>");
                $("td_"+j+xingqiji).appendChild(div);
              }
            }
          }
        }         
      } 
    }
  }
//周期性事物
  var URl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/selectAffairByWeek.act?year="+year+"&week="+weekth;
  var rtJsons = getJsonRs(URl);
  if(rtJsons.rtState == "1"){
    alert(rtJsons.rtMsrg); 
    return ;
    }
  var prcsData = rtJsons.rtData;
  if(prcsData.length>0){
    var typeNames = ["每日","每周","每月","每年"];
    var weekNames = ["一","二","三","四","五","六","日"];
    for(var i= 0;i<prcsData.length;i++){
       var prcAffair = prcsData[i];
       var seqId = prcAffair.seqId;
       var beginTime = prcAffair.beginTime;
       var lastRemind = prcAffair.lastRemind;
       var remindTime = prcAffair.remindTime; 
       var remindDate = prcAffair.remindDate;
       var endTime = prcAffair.endTime;
       var type = prcAffair.type;
       var content = prcAffair.content;
       var isWeekend = prcAffair.isWeekend;
       var week_day_month='';
       if(type=='3'){
         week_day_month = weekNames[parseInt(remindDate)-1];  
       }
       if(type=='4'){
         week_day_month= remindDate+'日';
       }
       if(type=='5'){
         week_day_month = remindDate.split('-')[0]+'月'+remindDate.split('-')[1]+'日';
       }
       var remindTimeInt = str_int(remindTime.substr(0,5)); 
       //
       var weekDates = ['<%=date1%>','<%=date2%>','<%=date3%>','<%=date4%>','<%=date5%>','<%=date6%>','<%=date7%>'];
       var weekDatesIds = '';
       //判断结束时间是否为空
       if(endTime==''){
         for(var j=0;j<weekDates.length;j++){
           if(weekDates[j]>=beginTime.substr(0,10)){
             weekDatesIds = weekDatesIds+(j+1)+",";
           }
         }
       }else{
         for(var j=0;j<weekDates.length;j++){
           if(weekDates[j]>=beginTime.substr(0,10)&&weekDates[j]<=endTime.substr(0,10)){
             weekDatesIds = weekDatesIds+(j+1)+",";
           }
         }
       }
       //得到星期几的Ids
       if(weekDatesIds!=''){
         weekDatesIds = weekDatesIds.substr(0,weekDatesIds.length-1);
       }
       var idLength ='';
       if(weekDatesIds!=''){
         idLength  = weekDatesIds.split(",");
       }
       if(type=='2'){
         for(var k=0;k<idLength.length;k++){
           if(isWeekend=='1'){//判断是否排除了周末
             var day_of_week = getDayOfWeek(weekDates[parseInt(idLength[k],10)-1]);
             if(day_of_week!=0&&day_of_week!=6){
               addDiv(remindTimeInt,idLength,k,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content); 
             }
           }else{
             addDiv(remindTimeInt,idLength,k,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content); 
           }
        }
       }else if(type=='3'){ 
         for(var k=0;k<idLength.length;k++){
           if(remindDate==idLength[k]){
             addDiv(remindTimeInt,idLength,k,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content);  
           }
         }
       }else if(type=='4'){
         for(var k=0;k<idLength.length;k++){
           if(remindDate==parseInt(weekDates[parseInt(idLength[k])-1].substr(8,2),10)){
             addDiv(remindTimeInt,idLength,k,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content);  
           }
         }
       }else if(type=='5'){
         var m = remindDate.split("-")[0];
         var d = remindDate.split("-")[1];
         if(m.length==1){
           m = "0"+m;
         }
         if(d.length==1){
           d = "0"+d;
         }
         for(var k=0;k<idLength.length;k++){
           if((m+"-"+d)==weekDates[parseInt(idLength[k])-1].substr(5,5)){
             addDiv(remindTimeInt,idLength,k,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content);  
           }
         }
       }
      
    }
  }
}
function addDiv(remindTimeInt,idLength,k,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content){
  for(var j=0;j<24;j++){
    //判断是否在00:00-07:00之间
    if(j<7){
      if(remindTimeInt>=(j*3600)&&remindTimeInt<((j+1)*3600)){
        document.getElementById("front").style.display = '';
        var div = new Element('div').update(""
            + remindTime.substr(0,5)+"<br><a href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] + week_day_month +" " +remindTime.substr(0,5)
            + "\n上次提醒："+lastRemind
            + "\n起始时间："+beginTime+"'>"+content+"</a>" );
        $("td_0"+j+idLength[k]).appendChild(div);
      }
    }
    //判断是否在07:00-10:00之间
    if(j>=7&&j<10){
      if(remindTimeInt>=(j*3600)&&remindTimeInt<((j+1)*3600)){
        var div = new Element('div').update(""
            + remindTime.substr(0,5)+" <br><a href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] +week_day_month+ ""+remindTime.substr(0,5)
            + "\n上次提醒："+lastRemind
            + "\n起始时间："+beginTime+"'>"+content+"</a>" );
       $("td_0"+j+idLength[k]).appendChild(div);
       }
    }
    //判断是否在10:00-24:00之间
    if(j>=10){
      if(remindTimeInt>=(j*3600)&&remindTimeInt<((j+1)*3600)){ 
        var div = new Element('div').update(""
            + remindTime.substr(0,5)+"<br> <a href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] +week_day_month+ ""+remindTime.substr(0,5)
            + "\n上次提醒："+lastRemind
            + "\n起始时间："+beginTime+"'>"+content+"</a>" );
       $("td_"+j+idLength[k]).appendChild(div);
      }
    } 
  } 
}
function myAffair(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/affairnote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}
function set_view(temp){
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  var status = document.getElementById("status").value;
  var date = '<%=date1%>';
  if(temp=='list'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/list.jsp?statusTemp="+status;
  }
  if(temp=='day'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/day.jsp?date="+date+"&statusStr="+status;
  }
  if(temp=='week'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/week.jsp?yearW="+year+"&weekW="+week+"&statusW="+status;
  }
  if(temp=='month'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/month.jsp?year="+year+"&status="+status+"&date="+date;
  }
  setCookie("calendarType",temp);
}
function set_year(index){
  var yearW = document.getElementById("year").value;
  var weekW = document.getElementById("week").value;
  var statusW = document.getElementById("status").value;
  if(parseInt(yearW)<=2000){
    yearW = parseInt(yearW);
  }else if(parseInt(yearW)>=2049){
    yearW = parseInt(yearW);
  }else{
    yearW = parseInt(yearW)+index;
  }
  window.location="<%=contextPath%>/core/funcs/calendar/week.jsp?yearW="+yearW+"&weekW="+weekW+"&statusW="+statusW;
}
function set_week(index){
  var yearW = document.getElementById("year").value;
  var weekW = document.getElementById("week").value;
  var statusW = document.getElementById("status").value;
  var maxWeek = '<%=maxWeek%>';

  if(parseInt(weekW,10)+index>maxWeek){
    yearW = parseInt(yearW)+1;
    weekW = 1;
  }else if(parseInt(weekW,10)+index<1){
    yearW = parseInt(yearW)-1;
    weekW = '<%=YHCalendarAct.getMaxWeekNumOfYear(year-1)%>';
  }else{
    var weekW = parseInt(weekW)+index;
  }
  window.location="<%=contextPath%>/core/funcs/calendar/week.jsp?yearW="+yearW+"&weekW="+weekW+"&statusW="+statusW;
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
function display_front(){
  if(document.getElementById("front").style.display == "none"){
    document.getElementById("front").style.display = '';
  }else{
    document.getElementById("front").style.display = 'none';
  }
}
function today(){
  window.location = "<%=contextPath%>/core/funcs/calendar/week.jsp?";
}
function newCalendar(index,date){
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  var URL = "<%=contextPath%>/core/funcs/calendar/addcalendar.jsp?ldwmNewStatus=week&index="+index+"&year="+year+"&date="+date;
  window.open(URL,"calendar","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=220,resizable=yes");
}
function newCalendarByDate(date){
  var URL = "<%=contextPath%>/core/funcs/calendar/addcalendar.jsp?date="+date+"&temp=week";
  window.open(URL,"calendar","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=220,resizable=yes");
  
}
function selectCalendarByDay(date){
  var status = document.getElementById("status").value;
  var URL = "<%=contextPath%>/core/funcs/calendar/day.jsp?date="+date+"&statusStr="+status;
  window.location = URL;
}
function getDayOfWeek(dateStr){
  var day = new Date(Date.parse(dateStr.replace(/-/g, '/'))); //将日期值格式化
  //day.getDay();根据Date返一个星期中的某其中0为星期日
  return day.getDay(); 
}
function My_Submit(){
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  var status = document.getElementById("status").value;
  window.location="<%=contextPath%>/core/funcs/calendar/week.jsp?yearW="+year+"&weekW="+week+"&statusW="+status+"&ldwm=week";
}
function myNote(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status="+status;
  window.open(URL,"my_note","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=300,resizable=yes");
}
//判断今天是星期几
function todayWeek(){
  var curDate = new Date();
  var week = curDate.getDay();
  if(week ==0){
    week = 7;
  }
  for(var i = 0;i<24;i++){
    if(i<10){
      document.getElementById("td_0"+i+week).style.backgroundColor="#E4ECF3";  // TableContent
      //document.getElementById("td_0"+i+week).className = "TableContent";
    }else{
      document.getElementById("td_"+i+week).style.backgroundColor="#E4ECF3";   
      //document.getElementById("td_"+i+week).className="TableContent"; 
    }
  }
}
/**
 * 设置cookie
 */
function setCookie(name,value){
  var Days = 30;
  var exp  = new Date();
  exp.setTime(exp.getTime() + Days*24*60*60*1000);
  document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}
</script>
<body class="" topmargin="5" onload="doOnload();"  style="margin-right:18px;">
<div class="PageHeader">
	<div class="left">
	  <form name="form1" action="/general/calendar/arrange/index.php" style="margin-bottom:5px;">
	  <input type="hidden" value="" name="BTN_OP">
	  <input type="hidden" value="" name="OVER_STATUS">
	  <input type="hidden" value="02" name="MONTH">
	  <input type="hidden" value="25" name="DAY">
	  <a href="javascript: void(0)" onclick="today()" class="ToolBtn"><span>今天</span></a>
	  <!-- 年  -->
	  <a href="javascript:set_year(-1)";  title="上一年"><img src="<%=imgPath%>/prevpreviouspage.png"></img></a>
	  <a href="javascript:set_week(-1);" class="ArrowButtonR" title="上一周"><img src="<%=imgPath%>/previouspage.gif"></img></a>
	  <select id="year" name="year"      onchange="My_Submit();">
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
	  </select>
	  <!-- 周 -->
	  <select id="week" name="week" onchange="My_Submit();;">
	    <%
	      for(int i=1;i<=maxWeek;i++){
	        if(i==weekthInt){
	          %>
	          <option value="<%=i %>" selected="selected">第<%=i %>周</option>
	            <%}else{ %>
	          <option value="<%=i %>">第<%=i %>周</option>
	            <%
	                }
	       }
	      %>
	  </select>
	  <a href="javascript:set_week(1);" class="ArrowButtonR" title="下一周"><img src="<%=imgPath%>/nextpage.gif"></img></a>
	  <a href="javascript:set_year(1);" class="ArrowButtonRR" title="下一年"><img src="<%=imgPath%>/nextnextpage.png"></a>&nbsp;
	  <a id="statusName" href="javascript:;" class="dropdown" onclick="showMenuStatus(event);" hidefocus="true"><span id="name">全部</span></a>&nbsp;
	     <input type="hidden" id="status" name="status" value="0"></input>
	</div>
	<div class="right">
	  <a class="ToolBtn" href="<%=contextPath%>/core/funcs/calendar/selectcalendar.jsp"><span>查询</span></a>
	  <a id="new" href="javascript:void(0);" class="dropdown" onclick="showMenu(event);" hidefocus="true"><span>新建</span></a>&nbsp;
	  <input type="hidden" id="statu" name="status" value="0"></input>
	  <a class="calendar-view list-view" href="javascript:set_view('list');" title="列表视图"></a>
    <a class="calendar-view day-view" href="javascript:set_view('day');" title="日视图"></a>
    <a class="calendar-view week-view" href="javascript:set_view('week');" title="周视图"></a>
    <a class="calendar-view month-view" href="javascript:set_view('month');" title="月视图"></a>
	  </form>
	</div>
  <div class="clear"></div>
</div>
  <table id="cal_table" class="TableBlock" width="100%" align="center">
    <tr id="tbl_header" align="center" class="TableHeader">
      <td width="9%"><a href="javascript:display_front();">0-6点</a></td>
      <td width="13%" id="th_20100222" ondblclick="newCalendarByDate('<%=date1 %>');" title="双击建立日事务" ><b><a href="#" onclick="selectCalendarByDay('<%=date1 %>');"  style="color:#003366;font-size:9pt"  ><%=dateTempStr1 %>(星期一)</a></b></td>
      <td width="13%" id="th_20100223" ondblclick="newCalendarByDate('<%=date2 %>');" title="双击建立日事务"><b><a href="#" onclick="selectCalendarByDay('<%=date2 %>');"  style="color:#003366;font-size:9pt" ><%=dateTempStr2 %>(星期二)</a></b></td>
      <td width="13%" id="th_20100224" ondblclick="newCalendarByDate('<%=date3 %>');" title="双击建立日事务"><b><a href="#" onclick="selectCalendarByDay('<%=date3 %>');"  style="color:#003366;font-size:9pt" ><%=dateTempStr3 %>(星期三)</a></b></td>
      <td width="13%" id="th_20100225" ondblclick="newCalendarByDate('<%=date4 %>');" title="双击建立日事务"><b><a href="#" onclick="selectCalendarByDay('<%=date4 %>');"  style="color:#003366;font-size:9pt" ><%=dateTempStr4 %>(星期四)</a></b></td>
      <td width="13%" id="th_20100226" ondblclick="newCalendarByDate('<%=date5 %>');" title="双击建立日事务"><b><a href="#" onclick="selectCalendarByDay('<%=date5 %>');"  style="color:#003366;font-size:9pt" ><%=dateTempStr5 %>(星期五)</a></b></td>
      <td width="13%" id="th_20100227" ondblclick="newCalendarByDate('<%=date6 %>');" title="双击建立日事务"><b><a href="#" onclick="selectCalendarByDay('<%=date6 %>');"  style="color:#003366;font-size:9pt" ><%=dateTempStr6 %>(星期六)</a></b></td>
      <td width="13%" id="th_20100228" ondblclick="newCalendarByDate('<%=date7 %>');" title="双击建立日事务"><b><a href="#" onclick="selectCalendarByDay('<%=date7 %>');"  style="color:#003366;font-size:9pt" ><%=dateTempStr7 %>(星期日)</a></b></td>
         </tr>
    <tr id="spanweek" class="TableData" style="display:none">
      <td class="TableContent" align="center">跨天</td>
      <td id="spanWeekCalendar" colspan="7">
     </td>
    </tr>

    <tbody id="front" style="display:none">
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">00:00</td>
      <td id="td_001" width="13%"  ondblclick="newCalendar('00','<%=date1 %>')"></td>
      <td id="td_002" width="13%" ondblclick="newCalendar('00','<%=date2 %>')"></td>
      <td id="td_003" width="13%" ondblclick="newCalendar('00','<%=date3 %>')"></td>
      <td id="td_004" width="13%" ondblclick="newCalendar('00','<%=date4 %>')"></td>
      <td id="td_005" width="13%" ondblclick="newCalendar('00','<%=date5 %>')"></td>
      <td id="td_006" width="13%" ondblclick="newCalendar('00','<%=date6 %>')"></td>
      <td id="td_007" width="13%" ondblclick="newCalendar('00','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">01:00</td>
      <td id="td_011" width="13%" ondblclick="newCalendar('01','<%=date1 %>')" ></td>
      <td id="td_012" width="13%" ondblclick="newCalendar('01','<%=date2 %>')"></td>
      <td id="td_013" width="13%" ondblclick="newCalendar('01','<%=date3 %>')"></td>
      <td id="td_014" width="13%" ondblclick="newCalendar('01','<%=date4 %>')" ></td>
      <td id="td_015" width="13%" ondblclick="newCalendar('01','<%=date5 %>')"></td>
      <td id="td_016" width="13%" ondblclick="newCalendar('01','<%=date6 %>')"></td>
      <td id="td_017" width="13%" ondblclick="newCalendar('01','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">02:00</td>
      <td id="td_021" width="13%" ondblclick="newCalendar('02','<%=date1 %>')"></td>
      <td id="td_022" width="13%" ondblclick="newCalendar('02','<%=date2 %>')"></td>
      <td id="td_023" width="13%" ondblclick="newCalendar('02','<%=date3 %>')"></td>
      <td id="td_024" width="13%" ondblclick="newCalendar('02','<%=date4 %>')" ></td>
      <td id="td_025" width="13%" ondblclick="newCalendar('02','<%=date5 %>')"></td>
      <td id="td_026" width="13%" ondblclick="newCalendar('02','<%=date6 %>')"></td>
      <td id="td_027" width="13%" ondblclick="newCalendar('02','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">03:00</td>
      <td id="td_031" width="13%" ondblclick="newCalendar('03','<%=date1 %>')"></td>
      <td id="td_032" width="13%" ondblclick="newCalendar('03','<%=date2 %>')"></td>
      <td id="td_033" width="13%" ondblclick="newCalendar('03','<%=date3 %>')"></td>
      <td id="td_034" width="13%" ondblclick="newCalendar('03','<%=date4 %>')"></td>
      <td id="td_035" width="13%" ondblclick="newCalendar('03','<%=date5 %>')"></td>
      <td id="td_036" width="13%" ondblclick="newCalendar('03','<%=date6 %>')"></td>
      <td id="td_037" width="13%" ondblclick="newCalendar('03','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">04:00</td>
      <td id="td_041" width="13%" ondblclick="newCalendar('04','<%=date1 %>')"></td>
      <td id="td_042" width="13%" ondblclick="newCalendar('04','<%=date2 %>')"></td>
      <td id="td_043" width="13%" ondblclick="newCalendar('04','<%=date3 %>')"></td>
      <td id="td_044" width="13%" ondblclick="newCalendar('04','<%=date4 %>')" ></td>
      <td id="td_045" width="13%" ondblclick="newCalendar('04','<%=date5 %>')"></td>
      <td id="td_046" width="13%" ondblclick="newCalendar('04','<%=date6 %>')"></td>
      <td id="td_047" width="13%" ondblclick="newCalendar('04','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">05:00</td>
      <td id="td_051" width="13%" ondblclick="newCalendar('05','<%=date1 %>')"></td>
      <td id="td_052" width="13%" ondblclick="newCalendar('05','<%=date2 %>')"></td>
      <td id="td_053" width="13%" ondblclick="newCalendar('05','<%=date3 %>')"></td>
      <td id="td_054" width="13%" ondblclick="newCalendar('05','<%=date4 %>')"></td>
      <td id="td_055" width="13%" ondblclick="newCalendar('05','<%=date5 %>')"></td>
      <td id="td_056" width="13%" ondblclick="newCalendar('05','<%=date6 %>')"></td>
      <td id="td_057" width="13%" ondblclick="newCalendar('05','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">06:00</td>
      <td id="td_061" width="13%" ondblclick="newCalendar('06','<%=date1 %>')"></td>
      <td id="td_062" width="13%" ondblclick="newCalendar('06','<%=date2 %>')"></td>
      <td id="td_063" width="13%" ondblclick="newCalendar('06','<%=date3 %>')"></td>
      <td id="td_064" width="13%" ondblclick="newCalendar('06','<%=date4 %>')"></td>
      <td id="td_065" width="13%" ondblclick="newCalendar('06','<%=date5 %>')"></td>
      <td id="td_066" width="13%" ondblclick="newCalendar('06','<%=date6 %>')"></td>
      <td id="td_067" width="13%" ondblclick="newCalendar('06','<%=date7 %>')"></td>
    </tr>
    </tbody>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%" >07:00</td>
      <td id="td_071" width="13%" ondblclick="newCalendar('07','<%=date1 %>')"></td>
      <td id="td_072" width="13%" ondblclick="newCalendar('07','<%=date2 %>')"></td>
      <td id="td_073" width="13%" ondblclick="newCalendar('07','<%=date3 %>')"></td>
      <td id="td_074" width="13%" ondblclick="newCalendar('07','<%=date4 %>')"></td>
      <td id="td_075" width="13%" ondblclick="newCalendar('07','<%=date5 %>')"></td>
      <td id="td_076" width="13%" ondblclick="newCalendar('07','<%=date6 %>')"></td>
      <td id="td_077" width="13%" ondblclick="newCalendar('07','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">08:00</td>
      <td id="td_081" width="13%" ondblclick="newCalendar('08','<%=date1 %>')"></td>
      <td id="td_082" width="13%" ondblclick="newCalendar('08','<%=date2 %>')"></td>
      <td id="td_083" width="13%" ondblclick="newCalendar('08','<%=date3 %>')"></td>
      <td id="td_084" width="13%" ondblclick="newCalendar('08','<%=date4 %>')" ></td>
      <td id="td_085" width="13%" ondblclick="newCalendar('08','<%=date5 %>')"></td>
      <td id="td_086" width="13%" ondblclick="newCalendar('08','<%=date6 %>')"></td>
      <td id="td_087" width="13%" ondblclick="newCalendar('08','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">09:00</td>
      <td id="td_091" width="13%" ondblclick="newCalendar('09','<%=date1 %>')">
      <td id="td_092" width="13%" ondblclick="newCalendar('09','<%=date2 %>')"></td>
      <td id="td_093" width="13%" ondblclick="newCalendar('09','<%=date3 %>')"></td>
      <td id="td_094" width="13%" ondblclick="newCalendar('09','<%=date4 %>')"></td>
      <td id="td_095" width="13%" ondblclick="newCalendar('09','<%=date5 %>')"></td>
      <td id="td_096" width="13%" ondblclick="newCalendar('09','<%=date6 %>')"></td>
      <td id="td_097" width="13%" ondblclick="newCalendar('09','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">10:00</td>
      <td id="td_101" width="13%" ondblclick="newCalendar('10','<%=date1 %>')"></td>
      <td id="td_102" width="13%" ondblclick="newCalendar('10','<%=date2 %>')"></td>
      <td id="td_103" width="13%" ondblclick="newCalendar('10','<%=date3 %>')"></td>
      <td id="td_104" width="13%" ondblclick="newCalendar('10','<%=date4 %>')"></td>
      <td id="td_105" width="13%" ondblclick="newCalendar('10','<%=date5 %>')"></td>
      <td id="td_106" width="13%" ondblclick="newCalendar('10','<%=date6 %>')"></td>
      <td id="td_107" width="13%" ondblclick="newCalendar('10','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">11:00</td>
      <td id="td_111" width="13%" ondblclick="newCalendar('11','<%=date1 %>')"></td>
      <td id="td_112" width="13%" ondblclick="newCalendar('11','<%=date2 %>')"></td>
      <td id="td_113" width="13%" ondblclick="newCalendar('11','<%=date3 %>')"></td>
      <td id="td_114" width="13%" ondblclick="newCalendar('11','<%=date4 %>')"></td>
      <td id="td_115" width="13%" ondblclick="newCalendar('11','<%=date5 %>')"></td>
      <td id="td_116" width="13%" ondblclick="newCalendar('11','<%=date6 %>')"></td>
      <td id="td_117" width="13%" ondblclick="newCalendar('11','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">12:00</td>
      <td id="td_121" width="13%" ondblclick="newCalendar('12','<%=date1 %>')"></td>
      <td id="td_122" width="13%" ondblclick="newCalendar('12','<%=date2 %>')"></td>
      <td id="td_123" width="13%" ondblclick="newCalendar('12','<%=date3 %>')"></td>
      <td id="td_124" width="13%" ondblclick="newCalendar('12','<%=date4 %>')"></td>
      <td id="td_125" width="13%" ondblclick="newCalendar('12','<%=date5 %>')"></td>
      <td id="td_126" width="13%" ondblclick="newCalendar('12','<%=date6 %>')"></td>
      <td id="td_127" width="13%" ondblclick="newCalendar('12','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">13:00</td>
      <td id="td_131" width="13%" ondblclick="newCalendar('13','<%=date1 %>')"></td>
      <td id="td_132" width="13%" ondblclick="newCalendar('13','<%=date2 %>')"></td>
      <td id="td_133" width="13%" ondblclick="newCalendar('13','<%=date3 %>')"></td>
      <td id="td_134" width="13%" ondblclick="newCalendar('13','<%=date4 %>')"></td>
      <td id="td_135" width="13%" ondblclick="newCalendar('13','<%=date5 %>')"></td>
      <td id="td_136" width="13%" ondblclick="newCalendar('13','<%=date6 %>')"></td>
      <td id="td_137" width="13%" ondblclick="newCalendar('13','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">14:00</td>
      <td id="td_141" width="13%" ondblclick="newCalendar('14','<%=date1 %>')"></td>
      <td id="td_142" width="13%" ondblclick="newCalendar('14','<%=date2 %>')"></td>
      <td id="td_143" width="13%" ondblclick="newCalendar('14','<%=date3 %>')"></td>
      <td id="td_144" width="13%" ondblclick="newCalendar('14','<%=date4 %>')"></td>
      <td id="td_145" width="13%" ondblclick="newCalendar('14','<%=date5 %>')"></td>
      <td id="td_146" width="13%" ondblclick="newCalendar('14','<%=date6 %>')"></td>
      <td id="td_147" width="13%" ondblclick="newCalendar('14','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">15:00</td>
      <td id="td_151" width="13%" ondblclick="newCalendar('15','<%=date1 %>')"></td>
      <td id="td_152" width="13%" ondblclick="newCalendar('15','<%=date2 %>')"></td>
      <td id="td_153" width="13%" ondblclick="newCalendar('15','<%=date3 %>')"></td>
      <td id="td_154" width="13%" ondblclick="newCalendar('15','<%=date4 %>')"></td>
      <td id="td_155" width="13%" ondblclick="newCalendar('15','<%=date5 %>')"></td>
      <td id="td_156" width="13%" ondblclick="newCalendar('15','<%=date6 %>')"></td>
      <td id="td_157" width="13%" ondblclick="newCalendar('15','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">16:00</td>
      <td id="td_161" width="13%" ondblclick="newCalendar('16','<%=date1 %>')"></td>
      <td id="td_162" width="13%" ondblclick="newCalendar('16','<%=date2 %>')"></td>
      <td id="td_163" width="13%" ondblclick="newCalendar('16','<%=date3 %>')"></td>
      <td id="td_164" width="13%" ondblclick="newCalendar('16','<%=date4 %>')" ></td>
      <td id="td_165" width="13%" ondblclick="newCalendar('16','<%=date5 %>')"></td>
      <td id="td_166" width="13%" ondblclick="newCalendar('16','<%=date6 %>')"></td>
      <td id="td_167" width="13%" ondblclick="newCalendar('16','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">17:00</td>
      <td id="td_171" width="13%" ondblclick="newCalendar('17','<%=date1 %>')"></td>
      <td id="td_172" width="13%" ondblclick="newCalendar('17','<%=date2 %>')"></td>
      <td id="td_173" width="13%" ondblclick="newCalendar('17','<%=date3 %>')"></td>
      <td id="td_174" width="13%" ondblclick="newCalendar('17','<%=date4 %>')"></td>
      <td id="td_175" width="13%" ondblclick="newCalendar('17','<%=date5 %>')"></td>
      <td id="td_176" width="13%" ondblclick="newCalendar('17','<%=date6 %>')"></td>
      <td id="td_177" width="13%" ondblclick="newCalendar('17','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">18:00</td>
      <td id="td_181" width="13%" ondblclick="newCalendar('18','<%=date1 %>')"></td>
      <td id="td_182" width="13%" ondblclick="newCalendar('18','<%=date2 %>')"></td>
      <td id="td_183"  width="13%" ondblclick="newCalendar('18','<%=date3 %>')"></td>
      <td id="td_184" width="13%" ondblclick="newCalendar('18','<%=date4 %>')"></td>
      <td id="td_185" width="13%" ondblclick="newCalendar('18','<%=date5 %>')"></td>
      <td id="td_186" width="13%" ondblclick="newCalendar('18','<%=date6 %>')"></td>
      <td id="td_187" width="13%" ondblclick="newCalendar('18','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">19:00</td>
      <td id="td_191" width="13%" ondblclick="newCalendar('19','<%=date1 %>')"></td>
      <td id="td_192" width="13%" ondblclick="newCalendar('19','<%=date2 %>')"></td>
      <td id="td_193" width="13%" ondblclick="newCalendar('19','<%=date3 %>')"></td>
      <td id="td_194" width="13%" ondblclick="newCalendar('19','<%=date4 %>')"></td>
      <td id="td_195" width="13%" ondblclick="newCalendar('19','<%=date5 %>')"></td>
      <td id="td_196" width="13%" ondblclick="newCalendar('19','<%=date6 %>')"></td>
      <td id="td_197" width="13%" ondblclick="newCalendar('19','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">20:00</td>
      <td id="td_201" width="13%" ondblclick="newCalendar('20','<%=date1 %>')" ></td>
      <td id="td_202" width="13%" ondblclick="newCalendar('20','<%=date2 %>')"></td>
      <td id="td_203" width="13%" ondblclick="newCalendar('20','<%=date3 %>')"></td>
      <td id="td_204" width="13%" ondblclick="newCalendar('20','<%=date4 %>')"></td>
      <td id="td_205" width="13%" ondblclick="newCalendar('20','<%=date5 %>')"></td>
      <td id="td_206" width="13%" ondblclick="newCalendar('20','<%=date6 %>')"></td>
      <td id="td_207" width="13%" ondblclick="newCalendar('20','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">21:00</td>
      <td id="td_211" width="13%" ondblclick="newCalendar('21','<%=date1 %>')"></td>
      <td id="td_212" width="13%" ondblclick="newCalendar('21','<%=date2 %>')"></td>
      <td id="td_213" width="13%" ondblclick="newCalendar('21','<%=date3 %>')"></td>
      <td id="td_214" width="13%" ondblclick="newCalendar('21','<%=date4 %>')"></td>
      <td id="td_215" width="13%" ondblclick="newCalendar('21','<%=date5 %>')"></td>
      <td id="td_216" width="13%" ondblclick="newCalendar('21','<%=date6 %>')"></td>
      <td id="td_217" width="13%" ondblclick="newCalendar('21','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">22:00</td>
      <td id="td_221" width="13%" ondblclick="newCalendar('22','<%=date1 %>')"></td>
      <td id="td_222" width="13%" ondblclick="newCalendar('22','<%=date2 %>')"></td>
      <td id="td_223" width="13%" ondblclick="newCalendar('22','<%=date3 %>')"></td>
      <td id="td_224" width="13%" ondblclick="newCalendar('22','<%=date4 %>')"></td>
      <td id="td_225" width="13%" ondblclick="newCalendar('22','<%=date5 %>')"></td>
      <td id="td_226" width="13%" ondblclick="newCalendar('22','<%=date6 %>')"></td>
      <td id="td_227" width="13%" ondblclick="newCalendar('22','<%=date7 %>')"></td>
    </tr>
    <tr class="TableData" valign="top" height="40">
      <td align="center" class="TableContent" width="9%">23:00</td>
      <td id="td_231" width="13%" ondblclick="newCalendar('23','<%=date1 %>')"></td>
      <td id="td_232" width="13%" ondblclick="newCalendar('23','<%=date2 %>')"></td>
      <td id="td_233" width="13%" ondblclick="newCalendar('23','<%=date3 %>')"></td>
      <td id="td_234" width="13%" ondblclick="newCalendar('23','<%=date4 %>')"></td>
      <td id="td_235" width="13%" ondblclick="newCalendar('23','<%=date5 %>')"></td>
      <td id="td_236" width="13%" ondblclick="newCalendar('23','<%=date6 %>')"></td>
      <td id="td_237" width="13%" ondblclick="newCalendar('23','<%=date7 %>')"></td>
    </tr>
  </table>
</body>
</html>

