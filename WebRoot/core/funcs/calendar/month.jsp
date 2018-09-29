<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  Date date = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormatWeek = new SimpleDateFormat("E");
  SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
  Calendar c = Calendar.getInstance();
  String week = dateFormatWeek.format(date);
  String dateStr = dateFormat.format(date);
  int year = Integer.parseInt(dateStr.substring(0,4));
  int month = Integer.parseInt(dateStr.substring(5,7));
  int day = Integer.parseInt(dateStr.substring(8,10));
  //本页面
  String yearStr = request.getParameter("year");
  String monthStr = request.getParameter("month");
  String status = "0";
  String statusStr = request.getParameter("status");
  String ldwmStr = request.getParameter("ldwm");
  String dateWeekStr = request.getParameter("date");
  if(statusStr!=null){
    status = statusStr;
  }
  if(yearStr!=null&&ldwmStr!=null&&ldwmStr.equals("month")){
    year = Integer.parseInt(yearStr);
    month = Integer.parseInt(monthStr);
  }
  //从周页面跳转过来的
  if(dateWeekStr!=null){
    year = Integer.parseInt(dateWeekStr.substring(0,4));
    month = Integer.parseInt(dateWeekStr.substring(5,7));
  }
  
  //判断月初是第几周
  c.set(year,month-1,1);
  int beginWeekth = c.get(Calendar.WEEK_OF_YEAR);
  //判断这个月1号是星期几
  int beginWeek = c.get(Calendar.DAY_OF_WEEK);
  int maxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数 
  
  //判断这个月最后一天是星期几
  c.set(year,month-1,maxDay);
  int endWeek = c.get(Calendar.DAY_OF_WEEK);
  //判断这个月最后一天是第几周
  int endWeekth = c.get(Calendar.WEEK_OF_YEAR);
  //如果这个月的最后一天是星期天的话，那么最未周-1
  if(endWeek==1){
    endWeekth = endWeekth-1;
  }
  //如果这个月的第一天是星期天的话，那么起试周-1；
  if(beginWeek==1){
    beginWeekth = beginWeekth-1;
  }
  if(month==12){
    endWeekth =53;
  }
  //out.print(beginWeek+":"+beginWeekth+":"+endWeek+":"+endWeekth+":"+maxDay);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>今天</title>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<style>
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
var menuData1 = [{ name:'<div style="padding-top:5px;margin-left:10px">月事务<div>',action:setAction,extData:'1'}
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
  ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">回复短信<div>',action:set_work_note,extData:'2'}
  ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看日志<div>',action:set_work,extData:'5'}
  ]
  var menuData6 = [{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看<div>',action:set_work,extData:'0'}
  ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">未完成<div>',action:set_work,extData:'1'}
  ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">回复短信<div>',action:set_work_note,extData:'2'}
  ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看日志<div>',action:set_work,extData:'5'}
  ]
function setAction(){
  var a = arguments[2];
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var maxDay = DayNumOfMonth(year,month);
  if(arguments[2]==1){
    var URL = "<%=contextPath%>/core/funcs/calendar/addcalendar.jsp?ldwmNewStatus=month&year="+year+"&month="+month+"&maxDay="+maxDay;
    window.open(URL,"calendar","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=220,resizable=yes");
  }else if(arguments[2]==2){
    var URL = "<%=contextPath%>/core/funcs/calendar/addcalendardiary.jsp?ldwm=month&year="+year+"&month="+month;
    window.open(URL,"calendar","height=530,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=100,resizable=yes");
 }
}
function set_status(){
  var status= arguments[2];
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var URL = "<%=contextPath%>/core/funcs/calendar/month.jsp?status="+status+"&year="+year+"&month="+month+"&ldwm=month";
  window.location = URL;
}
function set_work(){
  var status= arguments[2];
  var seqId_overStatus=  arguments[1];
  var seqId = seqId_overStatus.split(',')[0].substr('cal_'.length);
  var overStatus = seqId_overStatus.split(',')[1];
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var statusS = document.getElementById("status").value;
  if(status=='0'){
    var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status="+status;
    window.open(URL,"my_note","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=300,resizable=yes");
  }
  if(status=='1'){
    if(overStatus=='0'){
      overStatus='1';
      var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+overStatus+"&ldwm=month";
      var rtJson = getJsonRs(URL);
      if(rtJson.rtState == "1"){
        alert(rtJson.rtMsrg); 
        return ;
      }
      document.location.reload();
     // var URL = "<%=contextPath%>/core/funcs/calendar/updatecalendarsuccess.jsp";
     // window.location.href = URL;
    }else{
      overStatus='0';
      var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+overStatus+"&ldwm=month";
      var rtJson = getJsonRs(URL);
      if(rtJson.rtState == "1"){
        alert(rtJson.rtMsrg); 
        return ;
      }
      document.location.reload();
    }

  }
  if(status=='2'){
    var URL = "<%=contextPath%>/core/funcs/calendar/editcalendar.jsp?seqId="+seqId+"&ldwm=month";
    window.location.href=URL;
  }
  if(status=='3'){
    var msg = "确认要删除此任务吗？";
    if(window.confirm(msg)){
      var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/deleteCalendarById.act?seqId="+seqId+"&ldwm=month";
      var rtJson = getJsonRs(URL);
      if(rtJson.rtState == "1"){
        alert(rtJson.rtMsrg); 
        return ;
      }
      window.location.reload();
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
  var menu = new Menu({bindTo:$('statusName') , menuData:menuData2 , attachCtrl:true});
  menu.show(event);
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
function my_note(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId;
  window.open(URL,"my_note","height=200,width=200,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=600,top=300,resizable=yes");
}
function set_view(temp){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var statusStr = document.getElementById("status").value;
  if(temp=='list'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/list.jsp?statusTemp="+statusStr;
  }
  if(temp=='day'){  
    var maxDay = DayNumOfMonth(year,month);
    document.location.href = "<%=contextPath%>/core/funcs/calendar/day.jsp?year="+year+"&month="+month+"&day="+1+"&statusStr="+statusStr+"&maxDay="+maxDay;
  }
  if(temp=='week'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/week.jsp?year="+year+"&month="+month+"&day="+1+"&statusStr="+statusStr;
  }
  if(temp=='month'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/month.jsp?year="+year+"&month="+month+"&day="+1+"&status="+statusStr;
  }
  setCookie("calendarType",temp);
}
function DayNumOfMonth(Year,Month) {
  var d = new Date(Year,Month,0);   
  return d.getDate(); 
}
function set_year(index){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var status = document.getElementById("status").value;
  if(parseInt(year)<=2000){
    year = parseInt(year);
  }else if(parseInt(year)>=2049){
    year = parseInt(year);
  }else{
    year = parseInt(year)+parseInt(index);
  }
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/core/funcs/calendar/month.jsp?year="+year+"&month="+month+"&status="+status+"&ldwm=month";
}
function set_month(index){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var status = document.getElementById("status").value;
  if(parseInt(month,10)+index<=0){
    year = parseInt(year)-1;
    month = 12;
  }else if(parseInt(month,10)+index>12){
    year = parseInt(year)+1;
    month = 1;
  }else{
    month = parseInt(month,10)+index;
  }
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/core/funcs/calendar/month.jsp?year="+year+"&month="+month+"&status="+status+"&ldwm=month";
}
function today(){
  window.location = "<%=contextPath%>/core/funcs/calendar/month.jsp?";
}
function doOnload(){
  var year = '<%=year%>';
  var month = '<%=month%>';
  var status = '<%=status%>';
  var beginWeek = '<%=beginWeek%>';
  var endWeek = '<%=endWeek%>';
  var beginWeekth = '<%=beginWeekth%>';
  var endWeekth = '<%=endWeekth%>';
  var colorTypes = ["","#0000FF","#0000FF","#FF0000","#00AA00"];
  var colorType = colorTypes[status];
  if(beginWeek==1){
    beginWeek = 7;  
  }else{
    beginWeek = beginWeek - 1;
  }
  if(endWeek==1){
    endWeek = 7;  
  }else{
    endWeek = endWeek - 1;
  }
  //建表
  newTable(beginWeekth,endWeekth,beginWeek,endWeek,year,month);
  var statusName = "全部";
  var statusNames = ['全部','未开始','进行中','已超时','已完成'];
  for(var i = 0;i<statusNames.length;i++){
    if(i==status){
      statusName = statusNames[i];  
    }
  }
  document.getElementById("status").value=status;
  document.getElementById("name").innerHTML=statusName;
  document.getElementById("name").style.color=colorType;
  document.getElementById("year").value=year;
  document.getElementById("month").value=month;
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/selectCalendarByMonth.act?year="+year+"&month="+month+"&status="+status;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  if(prcs.length>0){
    var calLevelName = "未指定"
    var calLevelNames = ['未指定','重要/紧急','重要/不紧急','不重要/紧急','不重要/不紧急'];
    //判断是否跨天
    for(var i=0;i<prcs.length;i++){
      var prc = prcs[i];
      var seqId = prc.seqId;
      var userId = prc.userId;
      var overStatus = prc.overStatus;
      var status = prc.status;
      var calLevel = prc.calLevel;
      var calType = prc.calType
      var content = prc.content;
      var managerId = prc.managerId;
      var managerName = prc.managerName;
      var calTime = prc.calTime;
      var endTime = prc.endTime;
      if(overStatus.trim()==''){
        overStatus = 0;
      }
      if(calLevel.trim()==''){
        calLevel = 0;
      }
      if(managerName!=''){
        managerName = "\n安排人："+ managerName;
      }
      var calTypeName = "工作事务";
      var statusName = "状态: 进行中";
      var statusStyle = "color:#0000FF";
      if(status=='1'){
        statusName = "状态: 未开始";
      }
      if(status=='2'){
        statusName = "状态: 已超时";
        statusStyle = "color:#FF0000"
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
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"  +calTypeName +"\n"+ statusName +managerName + "'><a href='javascript:set_month(-1);' title='上一月'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ","+overStatus+"' href='javascript:my_note(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");' style='"+statusStyle+";'>"+content+"</a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
        }
        if(dayStatus=='2'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='" +calTypeName +"\n"+ statusName+managerName  + "'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ","+overStatus+"' href='javascript:my_note(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");;' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_month(1);' title='下一月'><span style='font-family:Webdings'>4</span></a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
         }
        if(dayStatus=='3'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"  +calTypeName +"\n"+ statusName +managerName + "'><a href='javascript:set_month(-1);' title='上一月'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"  + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a id='cal_" + seqId + ","+overStatus+"' href='javascript:my_note(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_month(1);' title='下一月'><span style='font-family:Webdings'>4</span></a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
         }
        if(dayStatus=='4'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"  +calTypeName +"\n"+ statusName +managerName + "'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"  + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ","+overStatus+"' href='javascript:my_note(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");' style='"+statusStyle+";'>"+content+"</a></div>"
          );
          $("spanMonthCalendar").appendChild(div);
         }
      }else{
        if(dayStatus=='1'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"  +calTypeName +"\n"+ statusName +managerName + "'><a href='javascript:set_month(-1);' title='上一月'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ","+overStatus+"' href='javascript:my_note(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ",1);' style='"+statusStyle+";'>"+content+"</a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
        }
        if(dayStatus=='2'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='" +calTypeName +"\n"+ statusName+managerName  + "'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ","+overStatus+"' href='javascript:my_note(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ",1);;' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_month(1);' title='下一月'><span style='font-family:Webdings'>4</span></a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
         }
        if(dayStatus=='3'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"  +calTypeName +"\n"+ statusName +managerName + "'><a href='javascript:set_month(-1);' title='上一月'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"  + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a id='cal_" + seqId + ","+overStatus+"' href='javascript:my_note(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ",1);' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_month(1);' title='下一月'><span style='font-family:Webdings'>4</span></a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
         }
        if(dayStatus=='4'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"  +calTypeName +"\n"+ statusName +managerName + "'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"  + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ","+overStatus+"' href='javascript:my_note(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ",1);' style='"+statusStyle+";'>"+content+"</a></div>"
          );
          $("spanMonthCalendar").appendChild(div);
         }
      }
      
    //没跨天 
      for(var j = 0;j<calLevelNames.length;j++){
        if(calLevel==j){
          calLevelName = calLevelNames[j];
        }
      }
      if(calType=='2'){
        calTypeName = "个人事务";
      }
      var dayStatus = prc.dayStatus;
      if(dayStatus=='0'){
        
        var calTimeSubStr = calTime.substr(8,2);
        var ctssi = parseInt(calTimeSubStr,10);
        if(managerId.trim()==''||userId==managerId.trim()){
          var div = new Element('div').update("<div title='"
              +calTypeName +"\n"+ statusName + managerName+"'> <span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>"
              + calTime.substr(11,5) + " - "
              + endTime.substr(11,5)+ "</span> "
              +"<br><a id='cal_"
              +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
              + "onmouseover='showMenuWork(event," + seqId + ","+ overStatus+");' style='"+statusStyle+";'>"+content+"</a>"
              +"</div>");
        }else{
          var div = new Element('div').update("<div title='"
              +calTypeName +"\n"+ statusName + managerName+"'> <span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>"
              + calTime.substr(11,5) + " - "
              + endTime.substr(11,5)+ "</span> "
              +"<br><a id='cal_"
              +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
              + "onmouseover='showMenuWork(event," + seqId + ","+ overStatus+",1);' style='"+statusStyle+";'>"+content+"</a>"
              +"</div>");
        }
     
       $("td_"+ctssi).appendChild(div);
      }
    }
  }
  //生日
  checkBirthday();
  //周期性事务
  addAffair(year,month);
}
function newTable(beginWeekth,endWeekth,beginWeek,endWeek,year,month){
  var table = new Element('table',{"id":"cal_table","class":"TableBlock","width":"100%","align":"center"}).update("<tbody id = 'tboday'><tr align='center' class='TableHeader'>"
      +"<td width='6%'><b>周数</b></td>"
      +"<td width='14%'><b>星期一</b></td>"
      +"<td width='14%'><b>星期二</b></td>"
      +"<td width='14%'><b>星期三</b></td>"
      +"<td width='14%'><b>星期四</b></td>"
      +"<td width='14%'><b>星期五</b></td>"
      +"<td width='12%'><b>星期六</b></td>"
      +"<td width='12%'><b>星期日</b></td>"
      +"</tr></tbody>");
  $('listDiv').appendChild(table);
  //跨天的tr
  var tr = new Element('tr',{"id":"spanMonth","class":"TableData","align":"left","style":"display:none"}).update("<td class='TableContent' align='center'>跨天</td>"
    +"<td id='spanMonthCalendar' colspan='7'></td>");
  $('tboday').appendChild(tr);
  var monththInt ;
  for(var i=parseInt(beginWeekth,10);i<=parseInt(endWeekth,10);i++){

    var trStr = "";
    if(i==parseInt(beginWeekth)){
      //var tr = new Element('tr',{"class":"TableData","height":"80"});
      var tdStr = "<td id='tw_' class='TableContent' align='center' title='双击建立周事务' ondblclick='newCalendarWeek("+year+","+month+","+i+")'>第"+i+"周</td>";
      for(var j=1;j<=7;j++){
        if(j>=parseInt(beginWeek,10)){
          monththInt = (j-parseInt(beginWeek,10)+1);
          tdStr =  tdStr+"<td id='td_"+monththInt+"' valign='top' title='双击建立日事务' ondblclick='newCalendar("+year+","+month+","+monththInt+");'><div align='right' onclick='toDay("+year+","+month+","+monththInt+");' title='转到该日查看' style='cursor:pointer;width: 100%;'><font color='blue'  align='right'><b id='"+monththInt+"'>"+monththInt+"</b></font></div></td>"
        }else{
          tdStr =  tdStr+"<td id='td_' align='top'></td>"
         }
      }
      //tr.update(tdStr);
      trStr = trStr + "<tr class='TableData' height='80'>" + tdStr + "</tr>";
      //$('tboday').appendChild(tr); 
    }else if(i==parseInt(endWeekth)){
      //var tr = new Element('tr',{"class":"TableData", "height":"80"});
      var tdStr = "<td id='tw_' class='TableContent' align='center' title='双击建立周事务' ondblclick='newCalendarWeek("+year+","+month+","+i+")'>第"+i+"周</td>";
      for(var j=1;j<=7;j++){
        if(j<=parseInt(endWeek,10)){
          monththInt = monththInt+1;
          tdStr =  tdStr+"<td id='td_"+monththInt+"' valign='top' title='双击建立日事务' ondblclick='newCalendar("+year+","+month+","+monththInt+");'><div align='right' onclick='toDay("+year+","+month+","+monththInt+");' title='转到该日查看' style='cursor:pointer;width: 100%;'><font color='blue'  align='right'><b id='"+monththInt+"'>"+monththInt+"</b></font></div></td>"
        }else{
          tdStr =  tdStr+"<td id='td_' valign='top'></td>"
        }
      }
      //tr.update(tdStr);
      trStr = trStr + "<tr class='TableData' height='80'>" + tdStr + "</tr>";
      //$('tboday').appendChild(tr); 
    }else{
      //var tr = new Element('tr',{"class":"TableData", "height":"80"});
      var tdStr = "<td id='tw_' class='TableContent' align='center' title='双击建立周事务' ondblclick='newCalendarWeek("+year+","+month+","+i+")'>第"+i+"周</td>";
          for(var j=1;j<=7;j++){
           monththInt = monththInt+1;
           tdStr =  tdStr+"<td id='td_"+monththInt+"' valign='top' title='双击建立日事务' ondblclick='newCalendar("+year+","+month+","+monththInt+");'><div align='right' onclick='toDay("+year+","+month+","+monththInt+");' title='转到该日查看' style='cursor:pointer;width: 100%;'><font color='blue'  align='right'><b id='"+monththInt+"'>"+monththInt+"</b></font></div></td>"
          }
      // tr.update(tdStr);
       trStr = trStr + "<tr class='TableData' height='80'>" + tdStr + "</tr>";
      //$('tboday').appendChild(tr); 
    }
    trStr =  $('tboday').innerHTML + trStr ;
    $('tboday').update(trStr);
  } 
  //是当天的TD加颜色，如果当天的（31日）大于指定月的最大天数 ，则默认为最后一天 
  var curDate = new Date();
  var day = curDate.getDate();
  var maxday = '<%=maxDay%>';
  if(day>maxday){
    day = maxday;
  }
  $("td_"+day).className = "TableRed";
  $("td_"+day).getElementsByTagName("div")[0].className = "TableRed";
}
//查询有没有本月生日
function checkBirthday(){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var requestURL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/selectBirthday.act?year="+year+"&month="+month;
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  if(prcs.length>0){
     var tdTmp = "<td colspan='7' id='birthday'>";
     for(var i = 0;i<prcs.length;i++){
       var prc = prcs[i];
       var userName = prc.userName;
       var birthday = prc.birthday;
       tdTmp = tdTmp + userName+"("+birthday+")&nbsp&nbsp&nbsp&nbsp";
     }
     var tr = new Element('tr',{"class":"TableData"}).update("<td style='color:#46A718' align='center'><b>生日：</b></td>"+tdTmp);
     var trStr = "<tr class='TableData' >" + "<td style='color:#46A718' align='center'><b>生日：</b></td>"+tdTmp + "</tr>";
     // $('tboday').appendChild(tr);
     trStr =  $('tboday').innerHTML + trStr;
     $('tboday').update(trStr);
  }
}
//转到按日显示
function toDay(year,month,day){
  var status = document.getElementById("status").value;
  var maxDay = DayNumOfMonth(year,month);
  document.location.href = "<%=contextPath%>/core/funcs/calendar/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&maxDay="+maxDay;
}
function addAffair(year,month){
//周期性事物
  var URl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/selectAffairByMonth.act?year="+year+"&month="+month;
  var rtJson = getJsonRs(URl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var maxDay = rtJson.rtMsrg;
  var prcsData = rtJson.rtData;
  if(prcsData.length>0){
    var typeNames = ["每日","每周","每月","每年"];
    var weekNames = ["一","二","三","四","五","六","日"];
    var days = "";
    if(month.length==1){
      month = "0"+month;
    }
    for(var j=1;j<=parseInt(maxDay,10);j++){
      if(j<10){
        days = days+year+"-"+month+"-"+"0"+j+",";
      }else{
        days = days+year+"-"+month+"-"+j+",";
      }
    }
    if(days!=''){
      days = days.substr(0,days.length-1);
    }
    days = days.split(",");
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
       //
       var dayIds = "";
       if(endTime==''){
         for(var j=0;j<days.length;j++){
           if(days[j]>=beginTime.substr(0,10)){
             dayIds = dayIds+(j+1)+",";
           }
         }
       }else{
         for(var j=0;j<days.length;j++){
           if(days[j]>=beginTime.substr(0,10)&&days[j]<=endTime.substr(0,10)){
             dayIds = dayIds+(j+1)+",";
           }
         }
       }
       var dayId = '';
       if(dayIds!=''){
         dayIds = dayIds.substr(0,dayIds.length-1);
         dayId = dayIds.split(",");
       }
       if(type=='2'){
          for(var j=0;j<dayId.length;j++){
            if(isWeekend=='1'){//判断时候是排除周末的
              var s = days[parseInt(dayId[j],10)-1];
              var day_of_week = getDayOfWeek(s);
              if(day_of_week!=0&&day_of_week!=6){
                var div = new Element('div').update(""
                    + remindTime.substr(0,5)+"<br> <a href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] + week_day_month +" " +remindTime.substr(0,5)
                    + "\n上次提醒："+lastRemind
                    + "\n起始时间："+beginTime+"'>"+content+"</a>" );
                $("td_"+dayId[j]).appendChild(div);
              }
            }else{
              var div = new Element('div').update(""
                  + remindTime.substr(0,5)+"<br> <a href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] + week_day_month +" " +remindTime.substr(0,5)
                  + "\n上次提醒："+lastRemind
                  + "\n起始时间："+beginTime+"'>"+content+"</a>" );
              $("td_"+dayId[j]).appendChild(div);
            }
          }
       }else if(type=='3'){ 
         for(var j=0;j<dayId.length;j++){
           var weekId = getDayOfWeek(days[(parseInt(dayId[j],10)-1)].substr(0,10));
           if(weekId==0){
             weekId = 7;
           }
           if(weekId==remindDate){
             var div = new Element('div').update(""
                 + remindTime.substr(0,5)+" <br><a href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] + week_day_month +" " +remindTime.substr(0,5)
                 + "\n上次提醒："+lastRemind
                 + "\n起始时间："+beginTime+"'>"+content+"</a>" );
             $("td_"+dayId[j]).appendChild(div);
           }
         }
       }else if(type=='4'){
         for(var j=0;j<dayId.length;j++){
           if(remindDate==parseInt(dayId[j],10)){
             var div = new Element('div').update(""
                 + remindTime.substr(0,5)+" <br><a href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] + week_day_month +" " +remindTime.substr(0,5)
                 + "\n上次提醒："+lastRemind
                 + "\n起始时间："+beginTime+"'>"+content+"</a>" );
             $("td_"+dayId[j]).appendChild(div);
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
         for(var j=0;j<dayId.length;j++){
           if(year+"-"+m+"-"+d ==days[parseInt(dayId[j],10)-1]){
             var div = new Element('div').update(""
                 + remindTime.substr(0,5)+" <br><a href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] + week_day_month +" " +remindTime.substr(0,5)
                 + "\n上次提醒："+lastRemind
                 + "\n起始时间："+beginTime+"'>"+content+"</a>" );
             $("td_"+dayId[j]).appendChild(div);
           }
         }
       }
      
    }
  }
}
function getDayOfWeek(dateStr){
  var day = new Date(Date.parse(dateStr.replace(/-/g, '/'))); //将日期值格式化
  //day.getDay();根据Date返一个星期中的某其中0为星期日
  return day.getDay(); 
}
function myAffair(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/affairnote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}
function newCalendar(year,month,day){
  //alert(year+","+month+","+day);
  var URL = "<%=contextPath%>/core/funcs/calendar/addcalendar.jsp?ldwmNewStatus=month&year="+year+"&month="+month+"&day="+day;
  window.open(URL,"calendar","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=220,resizable=yes");
}
function newCalendarWeek(year,month,weekth){
  var URL = "<%=contextPath%>/core/funcs/calendar/addcalendar.jsp?ldwmNewStatus=week&year="+year+"&week="+weekth+"&newStatus=0";
  window.open(URL,"calendar","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=220,resizable=yes"); 
}
function My_Submit(){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var status = document.getElementById("status").value;
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/core/funcs/calendar/month.jsp?year="+year+"&month="+month+"&status="+status+"&maxDay="+maxDay+"&ldwm=month";
}
function myNote(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status="+status;
  window.open(URL,"my_note","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=300,resizable=yes");
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
<form name="form1" action="/general/calendar/arrange/month.php" style="margin-bottom:5px;">
<div class="PageHeader">
	<div class="left">
	  <input type="hidden" value="" name="BTN_OP">
	  <input type="hidden" value="3" name="OVER_STATUS">
	  <input type="hidden" value="26" name="DAY">
	  <a href="javascript: void(0)" onclick="today()" class="ToolBtn"><span>今天</span></a>
	  <!-- 年 -->
	<a href="javascript:set_year(-1)";  title="上一年"><img src="<%=imgPath%>/prevpreviouspage.png"></img></a>
	  <a href="javascript:set_month(-1);" class="ArrowButtonR" title="上一周"><img src="<%=imgPath%>/previouspage.gif"></img></a>
	  <select id="year" name="year"     onchange="My_Submit();">
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
	<!-- 月 -->
	<select id="month" name="month"    onchange="My_Submit();">
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
	      <option value="<%=i %>" selected="selected">0<%=i %>月</option>
	       <%}else{ %>
	    <option value="0<%=i %>">0<%=i %>月</option>
	      <%
	       }
	     }
	   }
	      %>
	  </select>
	  <a href="javascript:set_month(1);" class="ArrowButtonR" title="下一月"><img src="<%=imgPath%>/nextpage.gif"></img></a>
	  <a href="javascript:set_year(1);" class="ArrowButtonRR" title="下一年"><img src="<%=imgPath%>/nextnextpage.png"></a>&nbsp;
	  <a id="statusName" href="javascript:;" class="dropdown" onclick="showMenuStatus(event);" hidefocus="true"><span id="name">全部</span></a>&nbsp;
	     <input type="hidden" id="status" name="status" value="0"></input>
	</div>
	<div class="right">
		<a class="ToolBtn" href="<%=contextPath%>/core/funcs/calendar/selectcalendar.jsp"><span>查询</span></a>
	  <a id="new" href="javascript:void(0);" class="dropdown" onclick="showMenu(event);" hidefocus="true"><span>新建</span></a>&nbsp;
    <a class="calendar-view list-view" href="javascript:set_view('list');" title="列表视图"></a>
    <a class="calendar-view day-view" href="javascript:set_view('day');" title="日视图"></a>
    <a class="calendar-view week-view" href="javascript:set_view('week');" title="周视图"></a>
    <a class="calendar-view month-view" href="javascript:set_view('month');" title="月视图"></a>
	</div>
	<div class="clear"></div>
</div>
</form>
<div id="listDiv"></div>
</body>
</html>

