<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/core/inc/header.jsp" %>
<%
  Date date = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormatWeek = new SimpleDateFormat("E");
  SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
  String week = dateFormatWeek.format(date);
  String dateStr = dateFormat.format(date);
  int year = Integer.parseInt(dateStr.substring(0,4));
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
  String yearStr = request.getParameter("year");
  String monthStr = request.getParameter("month");
  String dayStr = request.getParameter("day");
  String statusStr = request.getParameter("statusStr");
  String maxDayStr = request.getParameter("maxDay");
  if(yearStr!=null){
    year = Integer.parseInt(yearStr);
    week = dateFormatWeek.format(dateFormat1.parse(yearStr+"-"+monthStr+"-"+dayStr));
  }
  if(monthStr!=null){
    month = Integer.parseInt(monthStr);
    week = dateFormatWeek.format(dateFormat1.parse(yearStr+"-"+monthStr+"-"+dayStr));
  }
  if(dayStr!=null){
    day = Integer.parseInt(dayStr);
    week = dateFormatWeek.format(dateFormat1.parse(yearStr+"-"+monthStr+"-"+dayStr));
  }
  if(statusStr!=null){
    status = statusStr;
  }
  if(maxDayStr!=null){
    maxDay = Integer.parseInt(maxDayStr);
  }
  String weekToDate = request.getParameter("date");
  if(weekToDate!=null){
    year = Integer.parseInt(weekToDate.substring(0,4));
    month = Integer.parseInt(weekToDate.substring(5,7));
    day = Integer.parseInt(weekToDate.substring(8,10));
  }
  if(day>maxDay){
    day = maxDay;
  }

  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>今天</title>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<style type="text/css">
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
var menuData1 = [{ name:'<div style="padding-top:5px;margin-left:10px">日事务<div>',action:setAction,extData:'1'}
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
  var month = document.getElementById("month").value;
  var day = document.getElementById("day").value;
  if(arguments[2]==1){
    var URL = "<%=contextPath%>/core/funcs/calendar/addcalendar.jsp?ldwmNewStatus=day&newStatus=0&year="+year+"&month="+month+"&day="+day;
    window.open(URL,"calendar","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=220,resizable=yes");
  }else if(arguments[2]==2){
    var URL = "<%=contextPath%>/core/funcs/calendar/addcalendardiary.jsp?ldwm=day&year="+year+"&month="+month+"&day="+day;
    window.open(URL,"calendar","height=530,width=630,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=100,resizable=yes");
  }
}
function set_status(){
  var status = arguments[2];
  var statusName = "全部";
  if(status=='1'){
    statusName = "未开始";
  }
  if(status=='2'){
    statusName = "进行中";
  }
  if(status=='3'){
    statusName = "已超时";
  }
  if(status=='4'){
    statusName = "已完成";
  }
  var colorTypes = ["","#0000FF","#0000FF","#FF0000","#00AA00"];
  document.getElementById("status").value=status;
  document.getElementById("name").innerHTML=statusName;
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var day = document.getElementById("day").value;
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/core/funcs/calendar/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&maxDay="+maxDay;
}
function set_work(){
  var status = arguments[2];
  var seqId_overStatus=  arguments[1];
  var seqId = seqId_overStatus.split(',')[0].substr('cal_'.length);
  var overStatus = seqId_overStatus.split(',')[1];
  if(status=='0'){
    var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status="+status;
    window.open(URL,"my_note","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=300,resizable=yes");
  }
  if(status=='1'){
    if(overStatus=='0'){
      overStatus='1';
      var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+overStatus+"&ldwm=day";
      var rtJson = getJsonRs(URL);
      if(rtJson.rtState == "1"){
        alert(rtJson.rtMsrg); 
        return ;
      }
     // var URL = "<%=contextPath%>/core/funcs/calendar/updatecalendarsuccess.jsp";
     // window.location.href = URL;
      document.location.reload();
        
    }else{
      overStatus='0';
      var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+overStatus+"&ldwm=day";
      var rtJson = getJsonRs(URL);
      if(rtJson.rtState == "1"){
        alert(rtJson.rtMsrg); 
        return ;
      }
      document.location.reload();
    }  
  
  }
  if(status=='2'){
    var URL = "<%=contextPath%>/core/funcs/calendar/editcalendar.jsp?seqId="+seqId+"&ldwm=day";
    window.location.href=URL;
  }
  if(status=='3'){
    var msg = "确认要删除此任务吗？";
    if(window.confirm(msg)){
      var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/deleteCalendarById.act?seqId="+seqId+"&ldwm=day";
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
function set_work_note(){
  var seqId_overStatus=  arguments[1];
  var seqId = seqId_overStatus.split(',')[0].substr('cal_'.length);
  var URL = "<%=contextPath%>/core/funcs/calendar/revertnote.jsp?seqId="+seqId;
  window.open(URL,"my_note","height=220,width=450,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=350,top=200,resizable=yes");
}
function showMenu(event){
  var menu = new Menu({bindTo:$('new') , menuData:menuData1 , attachCtrl:true});
  menu.show(event);
}
function showMenuStatus(event){
  var menu = new Menu({bindTo:'statusName' , menuData:menuData2 , attachCtrl:true});
  menu.show(event);
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
function display_front(){
  if(document.getElementById("front").style.display == "none"){
    document.getElementById("front").style.display = '';
  }else{
    document.getElementById("front").style.display = 'none';
  }
}
//function today(){
//  document.location.href = "<%=contextPath%>/yh/core/funcs/calendar";
//}
function doOnload(){
  var status = '<%=status%>';
  var year = '<%=year%>';
  var month = '<%=month%>';
  var day = '<%=day%>';
  var colorTypes = ["","#0000FF","#0000FF","#FF0000","#00AA00"];
  var colorType = colorTypes[status];
  if(month.length==1){
    month = "0"+month;
  }
  if(day.length==1){
    day = "0"+day;
  }
  var statusName = "全部";
  if(status=='1'){
    statusName = "未开始";
  }
  if(status=='2'){
    statusName = "进行中";
  }
  if(status=='3'){
    statusName = "已超时";
  }
  if(status=='4'){
    statusName = "已完成";
  }
  document.getElementById("status").value=status;
  document.getElementById("name").innerHTML=statusName;
  document.getElementById("name").style.color=colorType;
  document.getElementById("year").value=year;
  document.getElementById("month").value=month;
  document.getElementById("day").value=day;
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/selectCalendarByDay.act?year="+year+"&month="+month+"&day="+day+"&status="+status;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prcs = rtJson.rtData;
  if(prcs.length>0){
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
        managerName = " (安排人："+managerName+")";
      }
      var statusName = "状态 : 进行中";
      var calLevelName = "未指定"
      var calTypeName = "工作事务";
      var statusStyle = "color:#0000FF";
      if(status=='1'){
        statusName = "状态 : 未开始";
      }
      if(status=='2'){
        statusName = "状态 : 已超时";
        statusStyle = "color:#FF0000";
      }
      if(overStatus=='1'){
        statusName = "状态 : 已完成";
        statusStyle = "color:#00AA00";
      }
      if(calLevel=='1'){
        calLevelName = "重要/紧急";
      }
      if(calLevel=='2'){
        calLevelName = "重要/不紧急";
      }
      if(calLevel=='3'){
        calLevelName = "不重要/紧急";
      }
      if(calLevel=='4'){
        calLevelName = "不重要/不紧急";
      }
      if(calType=='2'){
        calTypeName = "个人事务";
      }
      var dayStatus = prc.dayStatus;
      if(managerId.trim()==''||userId==managerId.trim()){
        if(dayStatus=='1'){
          document.getElementById("spanDay").style.display="block";
          var div = new Element('div').update("<div title='" + statusName + "'><a href='javascript:set_day(-1);' title='上一天'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> " + calTypeName + "：<a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");' style='"+statusStyle+"'>"+content+"</a>"+managerName+" </div>"
          );
          $("spanDayCalendar").appendChild(div);
        }
        if(dayStatus=='2'){
          document.getElementById("spanDay").style.display="block";
          var div = new Element('div').update("<div title='" + statusName + "'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> " + calTypeName + "：<a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");;' style='"+statusStyle+";'>"+content+"</a> "+managerName+" <a href='javascript:set_day(1);' title='下一天'><span style='font-family:Webdings'>4</span></a></div>"
          );
          $("spanDayCalendar").appendChild(div);
         }
        if(dayStatus=='3'){
          document.getElementById("spanDay").style.display="block";
          var div = new Element('div').update("<div title='" + statusName + "'><a href='javascript:set_day(-1);' title='上一天'><span style='font-family:Webdings'>3</span></a>  <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> " + calTypeName + "：<a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");' style='"+statusStyle+";'>"+content+"</a> "+managerName+"<a href='javascript:set_day(1);' title='下一天'><span style='font-family:Webdings'>4</span></a></div>"
          );
          $("spanDayCalendar").appendChild(div);
         }
      }else{//别人安排的
        if(dayStatus=='1'){
          document.getElementById("spanDay").style.display="block";
          var div = new Element('div').update("<div title='" + statusName + "'><a href='javascript:set_day(-1);' title='上一天'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> " + calTypeName + "：<a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ",1);' style='"+statusStyle+"'>"+content+"</a>"+managerName+" </div>"
          );
          $("spanDayCalendar").appendChild(div);
        }
        if(dayStatus=='2'){
          document.getElementById("spanDay").style.display="block";
          var div = new Element('div').update("<div title='" + statusName + "'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> " + calTypeName + "：<a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ",1);;' style='"+statusStyle+";'>"+content+"</a> "+managerName+" <a href='javascript:set_day(1);' title='下一天'><span style='font-family:Webdings'>4</span></a></div>"
          );
          $("spanDayCalendar").appendChild(div);
         }
        if(dayStatus=='3'){
          document.getElementById("spanDay").style.display="block";
          var div = new Element('div').update("<div title='" + statusName + "'><a href='javascript:set_day(-1);' title='上一天'><span style='font-family:Webdings'>3</span></a>  <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> " + calTypeName + "：<a id='cal_" + seqId + ","+overStatus+"' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ",1);' style='"+statusStyle+";'>"+content+"</a> "+managerName+"<a href='javascript:set_day(1);' title='下一天'><span style='font-family:Webdings'>4</span></a></div>"
          );
          $("spanDayCalendar").appendChild(div);
         }
      }
      
    }
    // 没跨天的
    for(var i=0;i<prcs.length;i++){
      var prc = prcs[i];
      var seqId = prc.seqId;
      var userId = prc.userId;
      var calType = prc.calType
      var calLevel = prc.calLevel;
      var content = prc.content;
      var status = prc.status;
      var managerId = prc.managerId;
      var managerName = prc.managerName;
      var overStatus = prc.overStatus;
      var calTime = prc.calTime;
      var endTime = prc.endTime;
      if(overStatus.trim()==''){
        overStatus = 0;
      }
      var statusName = "状态 : 进行中";
      var statusStyle = "color:#0000FF";
      if(managerName!=''){
        managerName = " (安排人："+managerName+")";
      }
      var calLevelName = "未指定"
      var calTypeName = "工作事务"; 
      if(status=='1'){
        statusName = "状态 : 未开始";
      }
      if(status=='2'){
        statusName = "状态 : 已超时";
        statusStyle = "color:#FF0000";
      }
      if(overStatus=='1'){
        statusName = "状态 : 已完成";
        statusStyle = "color:#00AA00";
      }
      if(calLevel=='1'){
        calLevelName = "重要/紧急";
      }
      if(calLevel=='2'){
        calLevelName = "重要/不紧急";
      }
      if(calLevel=='3'){
        calLevelName = "不重要/紧急";
      }
      if(calLevel=='4'){
        calLevelName = "不重要/不紧急";
      }
      if(calType=='2'){
        calTypeName = "个人事务";
      }
      var dayStatus = prc.dayStatus;
      
      if(dayStatus=='0'){  
        var calTimeInt = str_int(calTime.substr(11,5)); 
        if(managerId.trim()==''||userId==managerId.trim()){
          for(var j=0;j<24;j++){
            //判断是否在00:00-07:00之间
            if(j<7){
              if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
                document.getElementById("front").style.display = '';
                var div = new Element('div').update("<div title='"
                     + statusName + "'><span class='CalLevel"+calLevel+"' title='"+calLevelName+"'>"
                     + calTime.substr(11,5) + " - "
                     + endTime.substr(11,5)+ "</span> "
                     + calTypeName + "：<a id='cal_"
                     +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
                     + "onmouseover='showMenuWork(event," + seqId + ","+ overStatus+");' style='"+statusStyle+";'>"+content+"</a>"
                     +managerName+"</div>");
                $("td_0"+j).appendChild(div);
              }
            }
            //判断是否在07:00-10:00之间
            if(j>=7&&j<10){
              if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
                var div = new Element('div').update("<div title='"
                    + statusName + "'><span class='CalLevel"+calLevel+"' title='"+calLevelName+"'>"
                    + calTime.substr(11,5) + " - "
                    + endTime.substr(11,5)+ "</span> "
                    + calTypeName + "：<a id='cal_"
                    +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
                    + "onmouseover='showMenuWork(event," + seqId + ","+ overStatus+");' style='"+statusStyle+";'>"+content+"</a>"
                    +managerName+" </div>");
               $("td_0"+j).appendChild(div);
               }
            }
            //判断是否在10:00-24:00之间
            if(j>=10){
              if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
                var div = new Element('div').update("<div title='"
                    + statusName + "'><span class='CalLevel"+calLevel+"' title='"+calLevelName+"'>"
                    + calTime.substr(11,5) + " - "
                    + endTime.substr(11,5)+ "</span> "
                    + calTypeName + "：<a id='cal_"
                    +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
                    + "onmouseover='showMenuWork(event," + seqId + ","+ overStatus+");' style='"+statusStyle+";'>"+content+"</a>"
                    +managerName+" </div>");
               $("td_"+j).appendChild(div);
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
                     + statusName + "'><span class='CalLevel"+calLevel+"' title='"+calLevelName+"'>"
                     + calTime.substr(11,5) + " - "
                     + endTime.substr(11,5)+ "</span> "
                     + calTypeName + "：<a id='cal_"
                     +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
                     + "onmouseover='showMenuWork(event," + seqId + ","+ overStatus+",1);' style='"+statusStyle+";'>"+content+"</a>"
                     +managerName+"</div>");
                $("td_0"+j).appendChild(div);
              }
            }
            //判断是否在07:00-10:00之间
            if(j>=7&&j<10){
              if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
                var div = new Element('div').update("<div title='"
                    + statusName + "'><span class='CalLevel"+calLevel+"' title='"+calLevelName+"'>"
                    + calTime.substr(11,5) + " - "
                    + endTime.substr(11,5)+ "</span> "
                    + calTypeName + "：<a id='cal_"
                    +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
                    + "onmouseover='showMenuWork(event," + seqId + ","+ overStatus+",1);' style='"+statusStyle+";'>"+content+"</a>"
                    +managerName+" </div>");
               $("td_0"+j).appendChild(div);
               }
            }
            //判断是否在10:00-24:00之间
            if(j>=10){
              if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
                var div = new Element('div').update("<div title='"
                    + statusName + "'><span class='CalLevel"+calLevel+"' title='"+calLevelName+"'>"
                    + calTime.substr(11,5) + " - "
                    + endTime.substr(11,5)+ "</span> "
                    + calTypeName + "：<a id='cal_"
                    +seqId+","+overStatus+"' href='javascript:myNote("+seqId+");'"
                    + "onmouseover='showMenuWork(event," + seqId + ","+ overStatus+",1);' style='"+statusStyle+";'>"+content+"</a>"
                    +managerName+" </div>");
               $("td_"+j).appendChild(div);
              }
            } 
          }      
        }      
      }
    }
  }
  //周期性事物
  var URl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/selectAffairByDay.act?year="+year+"&month="+month+"&day="+day;
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
       var idWeekend = prcAffair.idWeekend;
       var type = prcAffair.type;
       var content = prcAffair.content;
       if(idWeekend=='1'){
         
       }
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
       var calTimeInt = str_int(remindTime.substr(0,5)); 
       for(var j=0;j<24;j++){
         //判断是否在00:00-07:00之间
         if(j<7){
           if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
             document.getElementById("front").style.display = '';
             var div = new Element('div').update(""
                 + remindTime.substr(0,5)+" <a href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] + week_day_month +" " +remindTime.substr(0,5)
                 + "\n上次提醒："+lastRemind
                 + "\n起始时间："+beginTime+"'>"+content+"</a>" );
             $("td_0"+j).appendChild(div);
           }
         }
         //判断是否在07:00-10:00之间
         if(j>=7&&j<10){
           if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
             var div = new Element('div').update(""
                 + remindTime.substr(0,5)+" <a href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] +week_day_month+ ""+remindTime.substr(0,5)
                 + "\n上次提醒："+lastRemind
                 + "\n起始时间："+beginTime+"'>"+content+"</a>" );
            $("td_0"+j).appendChild(div);
            }
         }
         //判断是否在10:00-24:00之间
         if(j>=10){;
           if(calTimeInt>=(j*3600)&&calTimeInt<((j+1)*3600)){
             var div = new Element('div').update(""
                 + remindTime.substr(0,5)+" <a href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] +week_day_month+ ""+remindTime.substr(0,5)
                 + "\n上次提醒："+lastRemind
                 + "\n起始时间："+beginTime+"'>"+content+"</a>" );
            $("td_"+j).appendChild(div);
           }
         } 
       } 

    }
  }
 
}
function date_day(){
  var year = document.getElementById("year");
  var month = document.getElementById("month");
}
function set_view(temp){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var day = document.getElementById("day").value;
  var statusStr = document.getElementById("status").value;
  if(temp=='list'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/list.jsp?statusTemp="+statusStr;
  }
  if(temp=='day'){  

    var maxDay = DayNumOfMonth(year,month);
    document.location.href = "<%=contextPath%>/core/funcs/calendar/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+statusStr+"&maxDay="+maxDay;
  }
  if(temp=='week'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/week.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+statusStr;
  }
  if(temp=='month'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/month.jsp?year="+year+"&month="+month+"&day="+day+"&status="+statusStr+"&ldwm=month";
  }
  setCookie("calendarType",temp);
}
function My_Submit(){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var day = document.getElementById("day").value;
  var status = document.getElementById("status").value;
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/core/funcs/calendar/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&maxDay="+maxDay;
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
function today(){
  var date = new Date();                     //Create Date object.
  var month = (date.getMonth() + 1);        //Get month
  var day = date.getDate() ;                   //Get day
  var year = date.getFullYear();
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/core/funcs/calendar/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr=0&maxDay="+maxDay;
}
function set_year(index){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var day = document.getElementById("day").value;
  var status = document.getElementById("status").value;
  if(parseInt(year)<=2000){
    year = parseInt(year);
  }else if(parseInt(year)>=2049){
    year = parseInt(year);
  }else{
    year = parseInt(year)+parseInt(index);
  }
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/core/funcs/calendar/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&maxDay="+maxDay;
}
function set_month(index){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var day = document.getElementById("day").value;
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
  window.location="<%=contextPath%>/core/funcs/calendar/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&maxDay="+maxDay;
}
function set_day(index){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var day = document.getElementById("day").value;
  var status = document.getElementById("status").value;
  var maxDay = DayNumOfMonth(year,month);
  if(parseInt(day,10)+parseInt(index,10)<=0){
    if(parseInt(month,10)-1<=0){
       year = parseInt(year)-1;
       month = 12 ;
       maxDay = DayNumOfMonth(year,month);
    }else{
      month = parseInt(month,10)-1;
      maxDay = DayNumOfMonth(year,month);
    } 
    day = maxDay;
  }else if(parseInt(day,10)+parseInt(index)>maxDay){
    if(parseInt(month)+1>12){
      year = parseInt(year)+1;
      month = 1;
      maxDay = DayNumOfMonth(year,month);
    }else{
      month = parseInt(month,10)+1;
      maxDay = DayNumOfMonth(year,month)
    }
    day = 1;
  }else{
    day = parseInt(day,10)+parseInt(index);
  }
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/core/funcs/calendar/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&maxDay="+maxDay;
}
function DayNumOfMonth(Year,Month) {
  var d = new Date(Year,Month,0);   
  return d.getDate(); 
}
function newCalendar(index){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var day = document.getElementById("day").value;
  var URL = "<%=contextPath%>/core/funcs/calendar/addcalendar.jsp?ldwmNewStatus=day&index="+index+"&year="+year+"&month="+month+"&day="+day;
  window.open(URL,"calendar","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=220,resizable=yes");
}
function  new_cal(){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var day = document.getElementById("day").value;
  var URL = "<%=contextPath%>/core/funcs/calendar/addcalendar.jsp?ldwmNewStatus=day&year="+year+"&month="+month+"&day="+day+"&db=db";
  window.open(URL,"calendar","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=220,resizable=yes");
}
function myNote(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status="+status;
  window.open(URL,"my_note","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=300,resizable=yes");
}
function myAffair(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/affairnote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
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
<div id="div1" class="PageHeader" class="small">
 <div id="div2" class="left">
   <input type="hidden" value="" name="BTN_OP">
   <input type="hidden" value="4" name="OVER_STATUS">
   <a href="javascript: void(0)" onclick="today()" class="ToolBtn"><span>今天</span></a>
<!-- 年  -->
 <a href="javascript:set_year(-1)";  title="上一年"><img  src="<%=imgPath%>/previouspage.gif"></img></a>
   <select id="year" name="year" onchange="My_Submit();">
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
   <a href="javascript:set_month(-1);" class="ArrowButtonL" title="上一月"><img src="<%=imgPath%>/previouspage.gif"></img></a><select id="month"    name="month" onchange="My_Submit();">
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
   </select><a href="javascript:set_month(1);" class="ArrowButtonR" title="下一月"><img src="<%=imgPath%>/nextpage.gif"></img></a>
<!-- 日 -->
	<a href="javascript:set_day(-1);" class="ArrowButtonL" title="上一天"><img src="<%=imgPath%>/previouspage.gif"></img></a><select id="day" name="day" onchange="My_Submit();">
     <%
       for(int i=1;i<=maxDay;i++){
         String dayS = "";
         String dayDesc = "";
         String selected = "";
         if(i==month){
           selected = "selected";
         }
         if (i > 9) {
           dayDesc = String.valueOf(i);
           dayS = dayDesc;
         }else {
           dayDesc = "0" + i;
           dayS = dayDesc;
         }
     %>
     <option value="<%=dayS %>" selected="<%=selected %>"><%=dayDesc %>日</option>    
       <%
        
    }
       %>
   </select><a href="javascript:set_day(1);" class="ArrowButtonR" title="下一天"><img src="<%=imgPath%>/nextpage.gif"></img></a>
   <a id="statusName" href="javascript:;" class="dropdown" onclick="showMenuStatus(event);" hidefocus="true"><span id="name">全部</span></a>&nbsp;
   <input type="hidden" id="status" name="status" value="0"></input>
 </div>
 <div id="div3" class="right">
 	 <a href="<%=contextPath%>/core/funcs/calendar/selectcalendar.jsp" class="ToolBtn"><span>查询</span></a>
   <a id="new" href="javascript:;" class="dropdown" onclick="showMenu(event);" hidefocus="true" class="ToolBtn"><span>新建</span></a>&nbsp;
   <a class="calendar-view list-view" href="javascript:set_view('list');" title="列表视图"></a>
   <a class="calendar-view day-view" href="javascript:set_view('day');" title="日视图"></a>
   <a class="calendar-view week-view" href="javascript:set_view('week');" title="周视图"></a>
   <a class="calendar-view month-view" href="javascript:set_view('month');" title="月视图"></a>
 </div>
 <div class="clear"></div>
</div>

<div id="div4"></div>
  <table class="TableBlock" width="100%" align="center">
    <tr align="center" class="TableHeader" style="width:30px;height:20px;font-size:10pt;" >
      <td width="9%"><a href="javascript:display_front();" style>0-6点</a></td>
      <td ondblclick="new_cal();" title="双击建立日事务" ><%=year %>年<%=month %>月<%=day %>日(<%=week %>)</td>
    </tr>
     <tr id="spanDay" class="TableData" style="display:none">
      <td class="TableContent" align="center">跨天</td>
      <td id="spanDayCalendar">
     </td>
     </tr>
   </table>
   <table id="front" style="display: none" class="TableBlock no-top-border" width="100%" align="center">
     <tr id="tr_00" class="TableData" height="30" ondblclick="newCalendar('00');">
       <td class="TableContent" align="center" >00:00</td>
       <td id="td_00"></td>
     </tr>
     <tr id="tr_01" class="TableData" height="30" ondblclick="newCalendar('01');">
       <td class="TableContent" align="center" >01:00</td>
       <td id="td_01"></td>
     </tr>
     <tr id="tr_02" class="TableData" height="30" ondblclick="newCalendar('02');">
       <td width="9%" class="TableContent" align="center" >02:00</td>
       <td id="td_02"></td>
     </tr>
     <tr id="tr_03" class="TableData" height="30" ondblclick="newCalendar('03');">
       <td class="TableContent" align="center">03:00</td>
       <td id="td_03"></td>
     </tr>
     <tr id="tr_04" class="TableData" height="30" ondblclick="newCalendar('04');">
       <td class="TableContent" align="center" >04:00</td>
       <td id="td_04"></td>
     </tr>
     <tr id="tr_05" class="TableData" height="30" ondblclick="newCalendar('05');">
       <td class="TableContent" align="center" width="9%">05:00</td>
       <td id="td_05"></td>
     </tr>
      <tr id="tr_06" class="TableData" height="30" ondblclick="newCalendar('06');">
       <td class="TableContent" align="center">06:00</td>
       <td id="td_06"></td>
     </tr>
   </table>
   <table class="TableBlock no-top-border" width="100%" align="center">
     <tr id="tr_07" class="TableData" height="30" ondblclick="newCalendar('07');">
        <td width="9%" class="TableContent" align="center">07:00</td>
        <td id="td_07"></td>
      </tr>
      <tr id="tr_08" class="TableData" height="30" ondblclick="newCalendar('08');">
        <td class="TableContent" align="center" >08:00</td>
        <td id="td_08"></td>
      </tr>
      <tr id="tr_09" class="TableData" height="30" ondblclick="newCalendar('09');">
        <td class="TableContent" align="center" >09:00</td>
        <td id="td_09"></td>
      </tr>
      <tr id="tr_10" class="TableData" height="30" ondblclick="newCalendar('10');">
        <td class="TableContent" align="center" >10:00</td>
        <td id="td_10"></td>
      </tr>
      <tr id="tr_11" class="TableData" height="30" ondblclick="newCalendar(11);">
        <td class="TableContent" align="center">11:00</td>
        <td id="td_11"></td>
      </tr>
      <tr id="tr_12" class="TableData" height="30" ondblclick="newCalendar(12);">
        <td class="TableContent" align="center" >12:00</td>
        <td id="td_12"></td>
      </tr>
       <tr id="tr_13" class="TableData" height="30" ondblclick="newCalendar(13);">
        <td class="TableContent" align="center" >13:00</td>
        <td id="td_13"></td>
      </tr> 
     <tr id="tr_14" class="TableData" height="30" ondblclick="newCalendar(14);">
        <td class="TableContent" align="center" >14:00</td>
        <td id="td_14"></td>
      </tr>
      <tr id="tr_15" class="TableData" height="30" ondblclick="newCalendar(15);">
        <td class="TableContent" align="center" >15:00</td>
        <td id="td_15"></td>
      </tr>
      <tr id="tr_16" class="TableData" height="30" ondblclick="newCalendar(16);">
        <td class="TableContent" align="center" >16:00</td>
        <td id="td_16"></td>
      </tr>
      <tr id="tr_17" class="TableData" height="30" ondblclick="newCalendar(17);">
        <td class="TableContent" align="center" >17:00</td>
        <td id="td_17"></td>
      </tr>
      <tr id="tr_18" class="TableData" height="30" ondblclick="newCalendar(18);">
        <td class="TableContent" align="center" >18:00</td>
        <td id="td_18"></td>
      </tr>
      <tr id="tr_19" class="TableData" height="30" ondblclick="newCalendar(19);">
        <td class="TableContent" align="center">19:00</td>
        <td id="td_19"></td>
      </tr>
      <tr id="tr_20" class="TableData" height="30" ondblclick="newCalendar(20);">
        <td class="TableContent" align="center" >20:00</td>
        <td id="td_20"></td>
      </tr>
      <tr id="tr_21" class="TableData" height="30" ondblclick="newCalendar(21);">
        <td class="TableContent" align="center" >21:00</td>
        <td id="td_21"></td>
      </tr>
      <tr id="tr_22" class="TableData" height="30" ondblclick="newCalendar(22);">
        <td class="TableContent" align="center" >22:00</td>
        <td id="td_22"></td>
      </tr>
      <tr id="tr_23" class="TableData" height="30" ondblclick="newCalendar(23);">
        <td class="TableContent" align="center" >23:00</td>
        <td id="td_23"></td>
      </tr>
  </table>
</body>
</html>
  