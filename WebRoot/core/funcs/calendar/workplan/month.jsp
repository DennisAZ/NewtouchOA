<%@ page language="java"
	import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormatWeek = new SimpleDateFormat("E");
			SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
			Calendar c = Calendar.getInstance();
			String week = dateFormatWeek.format(date);
			String dateStr = dateFormat.format(date);
			int year = Integer.parseInt(dateStr.substring(0, 4));
			int month = Integer.parseInt(dateStr.substring(5, 7));
			int day = Integer.parseInt(dateStr.substring(8, 10));
			//本页面

			String yearStr = request.getParameter("year");
			String monthStr = request.getParameter("month");
			String status = "0";
			String statusStr = request.getParameter("status");
			String ldwmStr = request.getParameter("ldwm");
			String dateWeekStr = request.getParameter("date");
			if (statusStr != null) {
				status = statusStr;
			}
			if (ldwmStr != null && ldwmStr.equals("month")) {
				year = Integer.parseInt(yearStr);
				month = Integer.parseInt(monthStr);
			}
			if (dateWeekStr != null) {
				year = Integer.parseInt(dateWeekStr.substring(0, 4));
				month = Integer.parseInt(dateWeekStr.substring(5, 7));
			}
			//从周页面跳转过来的

			//判断月初是第几周
			c.set(year, month - 1, 1);
			int beginWeekth = c.get(Calendar.WEEK_OF_YEAR);
			//判断这个月1号是星期几

			int beginWeek = c.get(Calendar.DAY_OF_WEEK);
			int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数 
			//判断这个月最后一天是星期几

			c.set(year, month - 1, maxDay);
			int endWeek = c.get(Calendar.DAY_OF_WEEK);
			//判断这个月最后一天是第几周

			int endWeekth = c.get(Calendar.WEEK_OF_YEAR);
			//如果这个月的最后一天是星期天的话，那么最未周-1
			if (endWeek == 1) {
				endWeekth = endWeekth - 1;
			}
			//如果这个月的第一天是星期天的话，那么起试周-1；

			if (beginWeek == 1) {
				beginWeekth = beginWeekth - 1;
			}
			if (month == 12) {
				endWeekth = 53;
			}
			String deptId = request.getParameter("deptId");
			String userId = request.getParameter("userId");
			if (deptId == null) {
				deptId = "";
			}
			if (status == null) {
				status = "0";
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
	overflow: auto; /*这个可以去掉IE6,7的滚动*/
	_overflow-x: hidden; /*去掉IE6横向滚动*/
}
</style>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript"
	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript">
var menuData1 = [{ name:'月事务',action:setAction,extData:'1'}
  ,{ name:'今日日志',action:setAction,extData:'1'}
  ]
  var menuData2 = [{ name:'<div style="padding-top:5px;margin-left:10px">全部<div>',action:set_status,extData:'0'}
  ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">未开始<div>',action:set_status,extData:'1'}
  ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">进行中<div>',action:set_status,extData:'2'}
  ,{ name:'<div style="color:#FF0000;padding-top:5px;margin-left:10px">已超时<div>',action:set_status,extData:'3'}
  ,{ name:'<div style="color:#00AA00;padding-top:5px;margin-left:10px">已完成<div>',action:set_status,extData:'4'}
  ]
  var menuData3 = [{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看<div>',action:set_work,extData:'0'}
  ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">修改<div>',action:set_work,extData:'1'}
  ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">删除<div>',action:set_work,extData:'2'}
  ]
function setAction(){
  var a = arguments[2];
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var deptId = document.getElementById("deptId").value;
  var maxDay = DayNumOfMonth(year,month);
  var URL = "<%=contextPath%>/core/funcs/calendar/addcalendar.jsp?ldwmNewStatus=month&year="+year+"&month="+month+"&maxDay="+maxDay+"&deptId="+deptId;
  window.open(URL,"calendar","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=220,resizable=yes");
}
function set_status(){
  var status= arguments[2];
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var deptId = document.getElementById("deptId").value;
  var userId = document.getElementById("userId").value;
  var URL = "<%=contextPath%>/core/funcs/calendar/workplan/month.jsp?status="+status+"&year="+year+"&month="+month+"&ldwm=month"+"&deptId="+deptId+"&userId="+userId;
  window.location = URL;
}
function set_work(){
  var status = arguments[2];
  var seqId_CAT =  arguments[1];
  var seqId = seqId_CAT.split(',')[0].substr('CAT_'.length);
  var CAT = seqId_CAT.split(',')[1];
  if(CAT=='1'){
    if(status=='0'){
      var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status="+status;
      window.open(URL,"my_note","height=200,width=200,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=600,top=300,resizable=yes");
    }
    if(status=='1'){
      var URL = "<%=contextPath%>/core/funcs/calendar/info/editcalendar.jsp?seqId="+seqId+"&ldwm=day";
      window.open(URL,"my_note","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=200,resizable=yes");
     }
    if(status=='2'){
      var msg = "确认要删除此任务吗？";
      if(window.confirm(msg)){
        var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/deleteCalendarById.act?seqId="+seqId+"&ldwm=day";
        var rtJson = getJsonRs(URL);
        if(rtJson.rtState == "1"){
          alert(rtJson.rtMsrg); 
          return ;
        }
        window.location.reload();
      }
    }
  }
  if(CAT=='2'){
    if(status=='0'){
      var URL = "<%=contextPath%>/core/funcs/calendar/affairnote.jsp?seqId="+seqId;
      window.open(URL,"calendar","height=180,width=170,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
    }
    if(status=='1'){
      var URL = "<%=contextPath%>/core/funcs/calendar/info/editaffair.jsp?seqId="+seqId;
      window.open(URL,"my_note","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=200,resizable=yes");   
      }
    if(status=='2'){
      var msg='确认要删除该事务吗？';
      if(window.confirm(msg)) {
        var requestURL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/deleteAffairById.act?seqId="+seqId;
        var rtJson = getJsonRs(requestURL);
        if(rtJson.rtState == "1"){
          alert(rtJson.rtMsrg); 
          return ;
        }
        window.location.reload();
      }
    }
  }
  if(CAT=='3'){
    if(status=='0'){
      var URL = "<%=contextPath%>/core/funcs/calendar/tasknote.jsp?seqId="+seqId;
      window.open(URL,"calendar","height=180,width=170,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
  }
    if(status=='1'){
      URL="<%=contextPath%>/core/funcs/calendar/info/edittask.jsp?seqId="+seqId+"&tempStatus=0";
      window.open(URL,"my_note","height=480,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=200,resizable=yes");   
      }
    if(status=='2'){
      msg='确认要删除该任务吗？';
      if(window.confirm(msg)){
        var requestURL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHTaskAct/deleteTaskById.act?seqId="+seqId;
        var rtJson = getJsonRs(requestURL);
        if(rtJson.rtState == "1"){
          alert(rtJson.rtMsrg); 
          return ;
        }
        window.location.reload();
      }
    }
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
function showMenuWork(event,seqId,CAT,index){
  var menu = new Menu({bindTo:'CAT_'+seqId+","+CAT+","+index, menuData:menuData3 , attachCtrl:true});
  menu.show(event,seqId,CAT,index);
}
function my_note(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId;
  window.open(URL,"my_note","height=200,width=200,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=600,top=300,resizable=yes");
}
function set_view(temp){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var deptId = document.getElementById("deptId").value;
  var statusStr = document.getElementById("status").value;
  var userId = $("userId").value;
  var userIdTemp = $("userIdTemp").value;
  if(temp=='day'){  
    var maxDay = DayNumOfMonth(year,month);
    document.location.href = "<%=contextPath%>/core/funcs/calendar/workplan/index.jsp?year="+year+"&month="+month+"&day="+1+"&statusStr="+statusStr+"&maxDay="+maxDay+"&deptId="+deptId+"&userId="+userId+"&userIdTemp="+userIdTemp;
  }
  if(temp=='week'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/workplan/week.jsp?year="+year+"&month="+month+"&day="+1+"&statusStr="+statusStr+"&deptId="+deptId+"&userId="+userId+"&userIdTemp="+userIdTemp;
  }
  if(temp=='month'){  
    document.location.reload() ;
  }
}
function DayNumOfMonth(Year,Month) {
  var d = new Date(Year,Month,0);   
  return d.getDate(); 
}
function set_year(index){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var status = document.getElementById("status").value;
  var deptId = document.getElementById("deptId").value;
  var userId = document.getElementById("userId").value
  if(parseInt(year)<=2000){
    year = parseInt(year);
  }else if(parseInt(year)>=2049){
    year = parseInt(year);
  }else{
    year = parseInt(year)+parseInt(index);
  }
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/core/funcs/calendar/workplan/month.jsp?year="+year+"&month="+month+"&status="+status+"&ldwm=month"+"&deptId="+deptId+"&userId="+userId;
}
function set_month(index){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var status = document.getElementById("status").value;
  var deptId = document.getElementById("deptId").value;
  var userId = document.getElementById("userId").value
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
  window.location="<%=contextPath%>/core/funcs/calendar/workplan/month.jsp?year="+year+"&month="+month+"&status="+status+"&ldwm=month"+"&deptId="+deptId+"&userId="+userId;
}
function today(){
  var userId = $("userIdTemp").value;
  window.location = "<%=contextPath%>/core/funcs/calendar/workplan/month.jsp?userId="+userId;
}
function doOnload(){
  var year = '<%=year%>';
  var month = '<%=month%>';
  var status = '<%=status%>';
  var maxDay = '<%=maxDay%>';
  var beginWeek = '<%=beginWeek%>';
  var endWeek = '<%=endWeek%>';
  var beginWeekth = '<%=beginWeekth%>';
  var endWeekth = '<%=endWeekth%>';
  var deptId = '<%=deptId%>';
  var userId = '<%=userId%>';
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
  //部门
  var userDeptId = dept(deptId,userId);
  //判断是否指定部门是否为空
  if(userDeptId.split(",").length==2){
    return;
  }
  deptId = $("deptId").value;
  var userIds = getUser(deptId);
  deptId = document.getElementById("deptId").value;
  //由次部门得到所有人员
  var personURL = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectPersonByDept.act?userIds="+userIds;
  var rtJson = getJsonRs(personURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userSeqId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selectObj = $("userId");
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var myOption = document.createElement("option");
    myOption.value = prc.value;
    myOption.text = prc.name;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
    if(userId==prc.value){
      myOption.selected=true;
    }
  }
  if(userId=='0'){
    document.getElementById("userId").value=userSeqId;
  }
  var userId = document.getElementById("userId").value;
  //日程
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectCalendarByDeptMonth.act?year="+year+"&month="+month+"&status="+status+"&userId="+userId;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userSeqId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  if(prcs.length>0){
    var calLevelName = "未指定"
    var calLevelNames = ['未指定','重要/紧急','重要/不紧急','不重要/紧急','不重要/不紧急'];
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
      if(calLevel.trim()==''){
        calLevel = 0;
      }
      if(managerName!=''){
        managerName = "\n安排人："+managerName;
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
      //判断是否为自己的日程查询和是否自己安排的;
      if((managerId.trim()==''&&userId==userSeqId)||userSeqId==managerId.trim()){
        //判断是否跨天
        if(dayStatus=='1'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"  +calTypeName +"\n"+ statusName +managerName + "'><a href='javascript:set_month(-1);' title='上一月'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='CAT_" + seqId + ",1,0' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +",1,0);' style='"+statusStyle+";'>"+content+"</a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
        }
        if(dayStatus=='2'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='" +calTypeName +"\n"+ statusName +managerName + "'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='CAT_" + seqId + ",1,0' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +",1,0);;' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_month(1);' title='下一月'><span style='font-family:Webdings'>4</span></a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
         }
        if(dayStatus=='3'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"  +calTypeName +"\n"+ statusName +managerName + "'><a href='javascript:set_month(-1);' title='上一月'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"  + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a id='CAT_" + seqId + ",1,0' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +",1,0);' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_month(1);' title='下一月'><span style='font-family:Webdings'>4</span></a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
         }
        if(dayStatus=='4'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"  +calTypeName +"\n"+ statusName +managerName + "'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"  + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='CAT_" + seqId + ",1,0' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +",1,0);' style='"+statusStyle+";'>"+content+"</a></div>"
          );
          $("spanMonthCalendar").appendChild(div);
        }
        //没跨天
        if(dayStatus=='0'){
          var calTimeSubStr = calTime.substr(8,2);
          var ctssi = parseInt(calTimeSubStr,10);
          var div = new Element('div').update("<div title='"
              +calTypeName +"\n"+ statusName +managerName+ "'> <span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>"
              + calTime.substr(11,5) + " - "
              + endTime.substr(11,5)+ "</span> "
              +"<br><a id='CAT_"
              +seqId+",1,"+ctssi+"' href='javascript:myNote("+seqId+");'"
              + "onmouseover='showMenuWork(event," + seqId + ",1,"+ctssi+");' style='"+statusStyle+";'>"+content+"</a>"
              +"</div>");
         $("td_"+ctssi).appendChild(div);
        }
      }else{
        if(dayStatus=='1'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"  +calTypeName +"\n"+ statusName +managerName + "'><a href='javascript:set_month(-1);' title='上一月'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a  href='javascript:myNote(" + seqId + ");'  style='"+statusStyle+";'>"+content+"</a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
        }
        if(dayStatus=='2'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='" +calTypeName +"\n"+ statusName+managerName  + "'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a href='javascript:myNote(" + seqId + ");'  style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_month(1);' title='下一月'><span style='font-family:Webdings'>4</span></a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
         }
        if(dayStatus=='3'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"  +calTypeName +"\n"+ statusName+managerName  + "'><a href='javascript:set_month(-1);' title='上一月'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"  + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a  href='javascript:myNote(" + seqId + ");'  style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_month(1);' title='下一月'><span style='font-family:Webdings'>4</span></a> </div>"
          )
          $("spanMonthCalendar").appendChild(div);
         }
        if(dayStatus=='4'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"  +calTypeName +"\n"+ statusName +managerName + "'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"  + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a href='javascript:myNote(" + seqId + ");'  style='"+statusStyle+";'>"+content+"</a></div>"
          );
          $("spanMonthCalendar").appendChild(div);
        }
        if(dayStatus=='0'){
          var calTimeSubStr = calTime.substr(8,2);
          var ctssi = parseInt(calTimeSubStr,10);
          var div = new Element('div').update("<div title='"
              +calTypeName +"\n"+ statusName + "'> <span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>"
              + calTime.substr(11,5) + " - "
              + endTime.substr(11,5)+ "</span> "
              +"<br><a  href='javascript:myNote("+seqId+");'"
              + " style='"+statusStyle+";'>"+content+"</a>"
              +"</div>");
         $("td_"+ctssi).appendChild(div);
        }
      }    
    }
  }
  //本月生日的
  checkBirthday();
  //周期性事务

  addAffair(year,month,userId,userSeqId);
  //任务
   task(userId,year,month,maxDay);
}
//查询部门
function dept(deptId,userId){
  var deptIdTemp = document.getElementById("deptId").value;
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectDeptToPlan.act?deptId="+deptIdTemp+"&userId="+userId;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userDeptId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  if(prcs.length>0){
    var selectObj = $("deptId");
    for(var i=0;i<prcs.length;i++){
      var prc = prcs[i];
      var myOption = document.createElement("option");
      myOption.value = prc.value;
      myOption.text = prc.text;
      selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
      if(userDeptId==prc.value){
        myOption.selected = true;
      }
      //判断模块权限是指定人员的
      if(prc.value==''){
        document.getElementById("deptId").style.display = 'none';
        break;
      }
    }
  }else{
    userDeptId = userDeptId + ",1";
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"360"}).update("<tr>"
      +"<td class='msg forbidden'>"
        +"<h4 class='title'>禁止</h4>"
        +"<div class='content' style='font-size:12pt'>您没有该部门的查看权限</div>"
       + "</td></tr>");
    $("all").update(table);
  }
  return userDeptId ;
}
//得到所有人员
function getUser(deptId){
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/getUser.act?deptId="+deptId;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
   }
  var prcs = rtJson.rtData;
  return prcs.userId;
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
    if(i==parseInt(beginWeekth)){
      var tr = new Element('tr',{"class":"TableData","height":"80"});
      var tdStr = "<td id='tw_' class='TableContent' align='center' title='双击建立周事务'  ondblclick='newCalendarWeek("+year+","+month+","+i+")'>第"+i+"周</td>";
      for(var j=1;j<=7;j++){
        if(j>=parseInt(beginWeek,10)){
          monththInt = (j-parseInt(beginWeek,10)+1);
          tdStr =  tdStr+"<td id='td_"+monththInt+"' valign='top'  title='双击建立日事务' ondblclick='newCalendar("+year+","+month+","+monththInt+");'><div align='right' class='TableContent' onclick='toDay("+year+","+month+","+monththInt+");' title='转到该日查看' style='cursor:pointer;width: 100%;'><font color='blue'  align='right'><b id='"+monththInt+"'>"+monththInt+"</b></font></div></td>"
        }else{
          tdStr =  tdStr+"<td id='td_' align='top'></td>"
         }
      }
      $('tboday').appendChild(tr); 
      tr.update(tdStr);
    }else if(i==parseInt(endWeekth)){
      var tr = new Element('tr',{"class":"TableData", "height":"80"});
      var tdStr = "<td id='tw_' class='TableContent' align='center' title='双击建立周事务'  ondblclick='newCalendarWeek("+year+","+month+","+i+")'>第"+i+"周</td>";
      for(var j=1;j<=7;j++){
        if(j<=parseInt(endWeek,10)){
          monththInt = monththInt+1;
          tdStr =  tdStr+"<td id='td_"+monththInt+"' valign='top' title='双击建立日事务' ondblclick='newCalendar("+year+","+month+","+monththInt+");'><div align='right' class='TableContent' onclick='toDay("+year+","+month+","+monththInt+");' title='转到该日查看' style='cursor:pointer;width: 100%;'><font color='blue'  align='right'><b id='"+monththInt+"'>"+monththInt+"</b></font></div></td>"
        }else{
          tdStr =  tdStr+"<td id='td_' valign='top'></td>"
        }
      }
      $('tboday').appendChild(tr); 
      tr.update(tdStr);
    }else{
      var tr = new Element('tr',{"class":"TableData", "height":"80"});
      var tdStr = "<td id='tw_' class='TableContent' align='center' title='双击建立周事务'  ondblclick='newCalendarWeek("+year+","+month+","+i+")'>第"+i+"周</td>";
          for(var j=1;j<=7;j++){
           monththInt = monththInt+1;
           tdStr =  tdStr+"<td id='td_"+monththInt+"' valign='top' title='双击建立日事务'  ondblclick='newCalendar("+year+","+month+","+monththInt+");'><div align='right' class='TableContent' onclick='toDay("+year+","+month+","+monththInt+");' title='转到该日查看' style='cursor:pointer;width: 100%;'><font color='blue'  align='right'><b id='"+monththInt+"'>"+monththInt+"</b></font></div></td>"
          }
       $('tboday').appendChild(tr); 
       tr.update(tdStr);
    }
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
       tdTmp = tdTmp +"<img src='<%=imgPath%>/cake.png' align='absMiddle'>" +userName+"("+birthday+")&nbsp&nbsp&nbsp&nbsp";
     }
     var tr = new Element('tr',{"class":"TableData"});
     $('tboday').appendChild(tr);
     tr.update("<td style='color:#46A718' align='center'><b>本月生日：</b></td>"+tdTmp);
  }
}
//转到按日显示
function toDay(year,month,day){
  var status = document.getElementById("status").value;
  var deptId = document.getElementById("deptId").value;
  var maxDay = DayNumOfMonth(year,month);

  
  document.location.href = "<%=contextPath%>/core/funcs/calendar/workplan/index.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&maxDay="+maxDay+"&deptId="+deptId;
}
function addAffair(year,month,userId,userSeqId){
//周期性事物
  var URl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectAffairByDeptMonth.act?year="+year+"&month="+month+"&userId="+userId;
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
       var userId = prcAffair.userId;
       var beginTime = prcAffair.beginTime;
       var lastRemind = prcAffair.lastRemind;
       var remindTime = prcAffair.remindTime; 
       var remindDate = prcAffair.remindDate;
       var managerId = prcAffair.managerId;
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
       if((managerId.trim()==''&&userId==userSeqId)||userSeqId==managerId.trim()){
         if(type=='2'){
           for(var j=0;j<dayId.length;j++){
             var day_of_week = getDayOfWeek(days[parseInt(dayId[j],10)-1]);//判断排除周末
             if(isWeekend=='1'){
               if(day_of_week!=0&&day_of_week!=6){   
                 affairDiv(seqId,dayId,j,remindTime,typeNames,type,week_day_month,lastRemind,beginTime,content);
               }
             }else{ 
               affairDiv(seqId,dayId,j,remindTime,typeNames,type,week_day_month,lastRemind,beginTime,content);
             }
           }
        }else if(type=='3'){ 
          for(var j=0;j<dayId.length;j++){
            var weekId = getDayOfWeek(days[(parseInt(dayId[j],10)-1)].substr(0,10));
            if(weekId==0){
              weekId = 7;
            }
            if(weekId==remindDate){
              affairDiv(seqId,dayId,j,remindTime,typeNames,type,week_day_month,lastRemind,beginTime,content);
            }
          }
        }else if(type=='4'){
          for(var j=0;j<dayId.length;j++){
            if(remindDate==parseInt(dayId[j],10)){
              affairDiv(seqId,dayId,j,remindTime,typeNames,type,week_day_month,lastRemind,beginTime,content);
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
              affairDiv(seqId,dayId,j,remindTime,typeNames,type,week_day_month,lastRemind,beginTime,content);
            }
          }
        }
       }else{
         if(type=='2'){
           for(var j=0;j<dayId.length;j++){
             var day_of_week = getDayOfWeek(days[parseInt(dayId[j],10)-1]);//判断排除周末
             if(isWeekend=='1'){
               if(day_of_week!=0&&day_of_week!=6){   
                 affairDiv2(seqId,dayId,j,remindTime,typeNames,type,week_day_month,lastRemind,beginTime,content);
               }
             }else{ 
               affairDiv2(seqId,dayId,j,remindTime,typeNames,type,week_day_month,lastRemind,beginTime,content);
             }

           }
        }else if(type=='3'){ 
          for(var j=0;j<dayId.length;j++){
            var weekId = getDayOfWeek(days[(parseInt(dayId[j],10)-1)].substr(0,10));
            if(weekId==0){
              weekId = 7;
            }
            if(weekId==remindDate){
              affairDiv2(seqId,dayId,j,remindTime,typeNames,type,week_day_month,lastRemind,beginTime,content);
            }
          }
        }else if(type=='4'){
          for(var j=0;j<dayId.length;j++){
            if(remindDate==parseInt(dayId[j],10)){
              affairDiv2(seqId,dayId,j,remindTime,typeNames,type,week_day_month,lastRemind,beginTime,content);
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
              affairDiv2(seqId,dayId,j,remindTime,typeNames,type,week_day_month,lastRemind,beginTime,content);
            }
          }
        }
     }
       
      
    }
  }
}
//周期性事务div,本人或安排人 
function affairDiv(seqId,dayId,j,remindTime,typeNames,type,week_day_month,lastRemind,beginTime,content){
  var div = new Element('div').update(""
      + remindTime.substr(0,8)+"<br> <a id='CAT_"+seqId+",2,"+dayId[j]+"' href='javascript:myAffair("+seqId+");'  onmouseover='showMenuWork(event," + seqId +",2,"+dayId[j]+")' title='提醒时间："+  typeNames[parseInt(type)-2] + week_day_month +" " +remindTime.substr(0,5)
      + "\n上次提醒："+lastRemind
      + "\n起始时间："+beginTime+"'>"+content+"</a>" );
  $("td_"+dayId[j]).appendChild(div);
}
function affairDiv2(seqId,dayId,j,remindTime,typeNames,type,week_day_month,lastRemind,beginTime,content){
  var div = new Element('div').update(""
      + remindTime.substr(0,8)+"<br> <a  href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] + week_day_month +" " +remindTime.substr(0,5)
      + "\n上次提醒："+lastRemind
      + "\n起始时间："+beginTime+"'>"+content+"</a>" );
  $("td_"+dayId[j]).appendChild(div);
}
//任务
function task(userId,year,month,maxDay){
  var URl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectTaskByUserId.act?userId="+userId;
  var rtJsons = getJsonRs(URl);
  if(rtJsons.rtState == "1"){
    alert(rtJsons.rtMsrg); 
    return ;
    }
  var userSeqId = rtJsons.rtMsrg;
  var prcs = rtJsons.rtData;
  //得到本月的所有日期为了做比较
  var monthDates = '' ;
  if(month.length==1){
    month = "0"+month;
  }
  for(var i=1;i<=parseInt(maxDay,10);i++){
    if(i>=10){
      monthDates = monthDates+year+"-"+month+"-"+i+",";
    }else{
      monthDates = monthDates+year+"-"+month+"-0"+i+",";
    }   
  }
  monthDates = monthDates.substr(0,monthDates.length-1);
  monthDates = monthDates.split(",");
  for(var i= 0;i<prcs.length;i++){
    var prc = prcs[i];
    var seqId = prc.seqId;
    var userId = prc.userId;
    var taskNo = prc.taskNo;
    var subject = prc.subject;
    var taskStatus = prc.taskStatus;
    var managerId = prc.managerId;
    var important = prc.important;
    var color = prc.color;
    var taskType = prc.taskType;
    var rate = prc.rate;
    var beginDate = prc.beginDate;
    var endDate = prc.endDate;
    var managerName = prc.managerName;
    if(managerName!=''){
      managerName = " (安排人："+managerName+")";
    }
    if(beginDate==''&&endDate==''){
      for(var j = 0;j<monthDates.length;j++){
        taskDiv(userId,seqId,subject,userSeqId,j+1,managerId,managerName);
      }
    }else if(beginDate==''&&endDate!=''){
      for(var j = 0;j<monthDates.length;j++){
        if(monthDates[j]<=endDate.substr(0,10)){
          taskDiv(userId,seqId,subject,userSeqId,j+1,managerId,managerName);
        }
      } 
    }else if(beginDate!=''&&endDate==''){
      for(var j = 0;j<monthDates.length;j++){
       if(monthDates[j]>=beginDate.substr(0,10)){
          taskDiv(userId,seqId,subject,userSeqId,j+1,managerId,managerName);
        }
      }
    }else {
      for(var j = 0;j<monthDates.length;j++){
        if(monthDates[j]>=beginDate.substr(0,10)&&monthDates[j]<=endDate.substr(0,10)){
         taskDiv(userId,seqId,subject,userSeqId,j+1,managerId,managerName);
        }
      }
    }
  }
}
//任务Div
function taskDiv(userId,seqId,subject,userSeqId,index,managerId,managerName){
  var div ;
  if((managerId.trim()==''&&userId==userSeqId)||userSeqId==managerId.trim()){
    div = new Element('div').update(" 任务：<a id='CAT_"+seqId+",3,"+index+"' href='javascript:taskNote("+seqId+")'  onmouseover='showMenuWork(event," + seqId + ",3,"+index+");' style='color:blue'>"+subject+"</a>"+managerName);
  }else{
    div = new Element('div').update(" 任务：<a href='javascript:taskNote("+seqId+")'  style='color:blue'>"+subject+"</a>"+managerName);
  }
  $("td_"+index).appendChild(div);
}
function getDayOfWeek(dateStr){
  var day = new Date(Date.parse(dateStr.replace(/-/g, '/'))); //将日期值格式化
  //day.getDay();根据Date返一个星期中的某其中0为星期日
  return day.getDay(); 
}
function myAffair(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/affairnote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=180,width=170,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}
function newCalendar(year,month,day){
  //alert(year+","+month+","+day);
  var userId = document.getElementById("userId").value;
  var URL = "<%=contextPath%>/core/funcs/calendar/info/index2.jsp?userId="+userId+"&year="+year+"&month="+month+"&day="+day+"&userId="+userId+"&dwm=month";
  window.open(URL,"calendar","height=450,width=550,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=200,resizable=yes");     
}
function newCalendarWeek(year,month,weekth){
  var userId = document.getElementById("userId").value;
  var URL = "<%=contextPath%>/core/funcs/calendar/info/index2.jsp?dwm=monthWeek&year="+year+"&week="+weekth+"&userId="+userId;
  window.open(URL,"calendar","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=220,resizable=yes"); 
}
function My_Submit(){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var status = document.getElementById("status").value;
  var deptId = document.getElementById("deptId").value;
  var userId = document.getElementById("userId").value;
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/core/funcs/calendar/info/month.jsp?year="+year+"&month="+month+"&status="+status+"&maxDay="+maxDay+"&ldwm=month"+"&deptId="+deptId+"&userId="+userId;
}
function My_UserSubmit(){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var status = document.getElementById("status").value;
  var deptId = document.getElementById("deptId").value;
  var userId = document.getElementById("userId").value;
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/core/funcs/calendar/info/month.jsp?year="+year+"&month="+month+"&status="+status+"&maxDay="+maxDay+"&ldwm=month"+"&deptId="+deptId+"&userId="+userId;
}
function myNote(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status="+status;
  window.open(URL,"my_note","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=300,resizable=yes");
}
function myAffair(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/affairnote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}
function taskNote(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/tasknote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}
function myDeptOnchange(){
  var deptId = document.getElementById("deptId").value;
  var userId = document.getElementById("userId").value;
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var status = document.getElementById("status").value;
  var maxDay = DayNumOfMonth(year,month);
  window.location = "<%=contextPath%>/core/funcs/calendar/info/month.jsp?year="+year+"&month="+month+"&status="+status+"&deptId="+deptId+"&userId="+userId+"&maxDay="+maxDay+"&ldwm=month";
}
function selectCalendar(){
  var deptId = document.getElementById("deptId").value;
  window.location.href = "<%=contextPath%>/core/funcs/calendar/info/selectcalendarByTerm.jsp?deptId="+deptId;
}
</script>
<body class="" topmargin="5" onload="doOnload();"
	style="margin-right: 18px">
<div id="all">
<div class="small" style="clear: both;">
<div style="float: left;">
<form name="form1" action="/general/calendar/arrange/month.php"
	style="margin-bottom: 5px;"><input type="hidden" value=""
	name="BTN_OP"> <input type="hidden" value="3"
	name="OVER_STATUS"> <input type="hidden" value="26" name="DAY">
<input type="button" value="今天" class="SmallButton1" title="今天"
	onclick="today();">
	 <!-- 年 --> <a
	href="javascript:set_year(-1)" ;  title="上一年"><img
	src="<%=imgPath%>/prevpreviouspage.png"></img></a> <a
	href="javascript:set_month(-1);" class="ArrowButtonR" title="上一周"><img
	src="<%=imgPath%>/previouspage.gif"></img></a> <select
	id="year" name="year" style="height: 22px; FONT-SIZE: 11pt;"
	onchange="My_Submit();">
	<%
	  for (int i = 2000; i < 2050; i++) {
					if (i == year) {
	%>
	<option value="<%=i%>" selected="selected"><%=i%>年</option>
	<%
	  } else {
	%>
	<option value="<%=i%>"><%=i%>年</option>
	<%
	  }
				}
	%>
</select>

 <!-- 月--> <select id="month" name="month"
	style="height: 22px; FONT-SIZE: 11pt;" onchange="My_Submit();">
	<%
	  for (int i = 1; i < 13; i++) {
					if (i >= 10) {
						if (i == month) {
	%>
	<option value="<%=i%>" selected="selected"><%=i%>月</option>
	<%
	  } else {
	%>
	<option value="<%=i%>"><%=i%>月</option>
	<%
	  }
					} else {
						if (i == month) {
	%>
	<option value="<%=i%>" selected="selected">0<%=i%>月</option>
	<%
	  } else {
	%>
	<option value="0<%=i%>">0<%=i%>月</option>
	<%
	  }
					}
				}
	%>
</select> <a href="javascript:set_month(1);" class="ArrowButtonR" title="下一月"><img
	src="<%=imgPath%>/nextpage.gif"></img></a> <a
	href="javascript:set_year(1);" class="ArrowButtonRR" title="下一年"><img
	src="<%=imgPath%>/nextnextpage.png"></a>&nbsp; <a
	id="statusName" href="javascript:;" class="dropdown"
	onclick="showMenuStatus(event);" hidefocus="true"><span
	id="name">全部</span></a>&nbsp; <input
	type="hidden" id="status" name="status" value="0"></input></form>
</div>
<div style="float: right;"><input type="button" value="查询"
	class="SmallButton" title="查询" onclick="selectCalendar();"> <select
	id="deptId" name="deptId" onchange="myDeptOnchange();">
</select> <select id="userId" name="userId" onchange="My_UserSubmit();">

</select> <span class=""> <a href="javascript:set_view('day');"
	title="日视图"><span><img
	src="<%=imgPath%>/cal_day.gif" align="absMiddle">日</span></a>
<a href="javascript:set_view('week');" title="周视图"><span><img
	src="<%=imgPath%>/cal_week.gif" align="absMiddle">周</span></a>
<a href="javascript:set_view('month');" title="月视图"><span><img
	src="<%=imgPath%>/cal_month.gif" align="absMiddle">月</span></a>
</span></div>
</div>
<input type="hidden" id="userIdTemp" name="userIdTemp"
	value="<%=userId%>"></input>
<div id="listDiv"></div>
</div>
</body>
</html>

