<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
</head>
<body>
<div class="module_body">
<div class="moduleTypeLink">
<a href="javascript:getCalendar(1);" id="todayCalendar">今日日程</a> | <a href="javascript:getCalendar(2);" id="groundCalendar">近日日程</a> | <a href="javascript:task();" id="taskPlan">日程任务</a>
</div>
<div id="calendar" class="" style="position:relative;overflow:hidden;width:100%;">

	<div id="calendar_ul" class="module_div" style="width:100%;position:relative;">
	
		<ul id="calendar_li" style="float:left;text-align:left;width:100%;position:relative;" type="disc">
		</ul>

	  <div style="clear:both;"></div>
	</div>
</div>
</div>
  </body>
  <script type="text/javascript" >
//日程
  
window.getCalendar = function (index){
  var currDate = new Date();
  var currDay = currDate.getDate();  
  var currMonth = currDate.getMonth();
  var currMonth = currMonth+1;
  var currYear = currDate.getFullYear(); 
  if(currMonth<10){
    currMonth = "0"+currMonth;
  }
  if(currDay<10){
    currDay = "0"+currDay;
  }
  var date = currYear+"-"+currMonth+"-"+currDay;
  var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/selectCalendarToDisk.act?date=" + date + "&index=" + index ;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  //alert(rsText);
  var calendarStr = "";
    for(var i = 0; i<prcs.length;i++){
      var prc = prcs[i];
      var seqId = prc.seqId;
      var calTime = prc.calTime;
      var endTime = prc.endTime;
      var content = prc.content;
      var overStatus = prc.overStatus;
      var overStatusName = "<a href='javascript:updateStatus("+seqId+",2)'>完成</a> &nbsp;";
      var updateCalendarStr = "<a href='javascript:updateCalendar("+seqId+")'>修改</a> &nbsp;";
      var status = "";
      if(calTime.substr(0,10)==endTime.substr(0,10)&&calTime.substr(0,10)==date){
        calTime  = calTime.substr(11,5),
        endTime = endTime.substr(11,5);
      }
      if(overStatus==2){
        overStatusName = "<a href='javascript:updateStatus("+seqId+",1)'>未完成</a>&nbsp;";
        status = "<span style='color:#00AA00'>已完成</span>";
      }
      calendarStr = calendarStr +  "<li onmouseover='showCalendar("+seqId+");' onmouseout='hideCalendar("+seqId+")'>" 
           + calTime + "-" + endTime
           + "&nbsp;<a href='javascript:myNote("+seqId+")'><span style='color:#0000FF'>" + content
           + "</span></a>&nbsp;"+status+" &nbsp;<span id='"+seqId+"' style='display:none'>"+overStatusName+updateCalendarStr+"</span></li>";
    }
  var affairList = getAffair(index,1);
  var affairStr = "";
  for(var i =0;i<affairList.length;i++){
     var affair = affairList[i];
     var seqId = affair.seqId;
     var content = affair.content;
     var remindTime = affair.remindTime;
     affairStr = affairStr + "<li>"+remindTime+"&nbsp;<a href='javascript:myAffair("+seqId+")'>"+ content + "</a></li>";
  }
  $("calendar_li").update(calendarStr+affairStr);
  if(((calendarStr+affairStr)=='')&&(index=='1')){
    $("calendar_li").update("<li>今日暂无日程安排<li>");
  }
  if(((calendarStr+affairStr)=='')&&(index=='2')){
    $("calendar_li").update("<li>近日暂无日程安排<li>");
  }
  if(index=='1'){
    $("todayCalendar").style.color="red";
    $("groundCalendar").style.color="";
    $("taskPlan").style.color="";
  }
  if(index=='2'){
    $("todayCalendar").style.color="";
    $("groundCalendar").style.color="red";
    $("taskPlan").style.color="";
  }
  doInitPlan(prcs.length + affairList.length);
}
window.myNote = function (seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status="+status;
  window.open(URL,"my_note","height=200,width=200,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=600,top=300,resizable=yes");
}
window.showCalendar = function (seqId){

  $(""+seqId).style.display = '';
}
window.hideCalendar = function (seqId){
   $(""+seqId).style.display = 'none';
}
window.updateStatus = function (seqId,status){
  var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+status;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  window.location.reload();
}
window.updateCalendar = function (seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/editcalendardisk.jsp?seqId="+seqId;
  top.dispParts(URL);
}

  //得到当天周期性事物

window.getAffair = function (index,calendarRecord){
  var currDate = new Date();
  var currDay = currDate.getDate();  
  var currMonth = currDate.getMonth();
  var currMonth = currMonth+1;
  var currYear = currDate.getFullYear(); 
  var URl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/selectAffairToDisk.act?year="+currYear+"&month="+currMonth+"&day="+currDay+"&index="+index+"&calendarRecord="+calendarRecord;
  var rtJsons = getJsonRs(URl);
  if(rtJsons.rtState == "1"){
    alert(rtJsons.rtMsrg); 
    return ;
    }
  var prcsData = rtJsons.rtData;
  return prcsData;
}
window.myAffair = function (seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/affairnote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=180,width=170,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}

//近日日程
window.getRoundCalendar = function (){
  alert("开发中");
}
//任务
window.task = function (){
  var URl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHTaskAct/selectTask.act";
  var rtJsons = getJsonRs(URl);
  if(rtJsons.rtState == "1"){
    alert(rtJsons.rtMsrg); 
    return ;
    }
  var prcsData = rtJsons.rtData;
  var taskStr = "";
  if(prcsData.length>0){
    for(var i = 0;i<prcsData.length;i++){
      var task = prcsData[i];
      var seqId = task.seqId;
      var subject = task.subject;
      var taskType = task.taskType;
      var taskTypeName = "工作";
      if(taskType=='2'){
        taskTypeName = "个人"; 
      }
      taskStr = taskStr + "<li>[" + taskTypeName + "]&nbsp;<a href='javascript:taskNote("+seqId + ")'>"+ subject + "</a>";
    }
    $("calendar_li").update(taskStr);
  }else{
    $("calendar_li").update("<li>暂无日程安排<li>")
  }
  $("todayCalendar").style.color="";
  $("groundCalendar").style.color="";
  $("taskPlan").style.color="red";
  doInitPlan(prcsData.length);
} 
window.taskNote = function (seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/tasknote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=180,width=170,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}

window.doInitPlan = function (records){
  //设置
  var lines = <%=request.getParameter("lines")%>;

  $('calendar').setStyle({height:20 * lines + 'px'});
  $('calendar_ul').setStyle({position: 'relative'});

  $('calendar_ul').setStyle({'top': '0px'});

  cfgModule({
      records: records,
      lines: lines,
      name: '日程安排',
      showPage:  function(i){
        $('calendar_ul').setStyle({'top': (- i * lines * 20) + 'px'});
      }
   });
}
getCalendar(1);
var scroll = <%=request.getParameter("scroll")%>;
if (scroll){
  Marquee('calendar_li',80,1);
}
</script>
  </html>