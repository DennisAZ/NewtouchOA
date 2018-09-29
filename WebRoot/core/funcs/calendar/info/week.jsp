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
  String deptId = request.getParameter("deptId");
  if(deptId==null){
    deptId = "0";
  }
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
  String dateTempStr5 = dateFormat4.format(dateTemp5);
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
  int maxWeek = 53;//YHCalendarAct.getMaxWeekNumOfYear(year);
 // out.print(date1+""+weekthInt);
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
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript">
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
var menuData4 = [{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看<div>',action:set_work2,extData:'0'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">修改<div>',action:set_work2,extData:'1'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">删除<div>',action:set_work2,extData:'2'}
]
function set_status(){
  var status = arguments[2];
  var statusNames = ['全部','未开始','进行中','已超时','已完成'];
  document.getElementById("status").value=status;
  document.getElementById("name").innerHTML=statusName;
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  var deptId = document.getElementById("deptId").value;
  window.location="<%=contextPath%>/core/funcs/calendar/info/week.jsp?yearW="+year+"&weekW="+week+"&statusW="+status+"&deptId="+deptId;
}
function set_work(){
  var status = arguments[2];
  var seqId =  arguments[1].substr('cal_'.length);
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
function set_work2(){
  var status = arguments[2];
  var seqId_AT =  arguments[1];
  var seqId = seqId_AT.split(',')[0].substr('AT_'.length);
  var AT = seqId_AT.split(',')[1];
  if(AT=='2'){
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
  if(AT=='3'){
    if(status=='0'){
      var URL = "<%=contextPath%>/core/funcs/calendar/tasknote.jsp?seqId="+seqId;
      window.open(URL,"calendar","height=180,width=170,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
  }
    if(status=='1'){
      URL="<%=contextPath%>/core/funcs/calendar/info/edittask.jsp?seqId="+seqId+"&tempStatus=0";
      window.open(URL,"my_note","height=450,width=580,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=200,resizable=yes");   
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

function showMenuStatus(event){
  var menu = new Menu({bindTo:'statusName' , menuData:menuData2 , attachCtrl:true});
  menu.show(event);
}
function showMenuWork(event,seqId){
  var menu = new Menu({bindTo:'cal_'+seqId, menuData:menuData3 , attachCtrl:true});
  menu.show(event,seqId);
}
function showMenuWork2(event,seqId,AT,index){
  var menu = new Menu({bindTo:'AT_'+seqId+","+AT+","+index, menuData:menuData4 , attachCtrl:true});
  menu.show(event,seqId,AT,index);
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
  var deptId = '<%=deptId%>';
  var status = '<%=status%>';
  var statusName = "全部";
  var colorTypes = ["","#0000FF","#0000FF","#FF0000","#00AA00"];
  var colorType = colorTypes[status];
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
  document.getElementById("week").value=weekth;

  //部门 
  var userDeptId = dept(deptId);
  //判断是否指定部门是否为空
  if(userDeptId.split(",").length==2){
    return;
  }
  if(deptId!='0'){
    document.getElementById("deptId").value=deptId;
  }

  deptId = document.getElementById("deptId").value;
  var userIds = getUser(deptId);

  //日程安排
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectCalendarByDeptWeek.act?year="+year+"&weekth="+weekth+"&status="+status+"&userIds="+userIds;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcsJson = rtJson.rtData;
  var users = prcsJson.users;
  var userSeqId = rtJson.rtMsrg; 
  var prcs = prcsJson.data;
  //计算周宽度

  var th = $("tbl_header");
  var week_width = 0;
  if(!th){
  	return;
  }
  for(var j = 1; j < th.cells.length; j++){
    week_width += th.cells[j].offsetWidth;
  }
  //列出本部门所有的人员
  for(var i = 0;i<users.length;i++){
    var user = users[i];
    var value = user.value;
    var name = user.name;
    if(prcs.length>0){
      var calLevelNames = ['未指定','重要/紧急','重要/不紧急','不重要/紧急','不重要/不紧急'];
      //判断是否跨天
      for(var k=0;k<prcs.length;k++){
        var prc = prcs[k];
        var seqId = prc.seqId;
        var userId = prc.userId;
        var userName = prc.userName;
        var managerName = prc.managerName;
        var overStatus = prc.overStatus;
        var status = prc.status;
        var calLevel = prc.calLevel;
        var calType = prc.calType
        var content = prc.content;
        var managerId = prc.managerId;
        var managerName = prc.managerName;
        var calTime = prc.calTime;
        var endTime = prc.endTime;
  	    var start = setWeekDate(calTime);
  	    var end = setWeekDate(endTime);
  	    if(calLevel.trim()==''){
  	      calLevel = 0;
  	    }
  	    if(managerName!=''){
          managerName = "安排人："+managerName;
    	}
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
        var statusName = "状态: 进行中";
        var calLevelName = "未指定"
        var calTypeName = "类型: 工作事务";
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
          calTypeName = "类型: 个人事务";
        }
        var dayStatus = prc.dayStatus;
        if(userId==value){
          if((managerId.trim()==''&&userId==userSeqId)||userSeqId==managerId.trim()){
          
            if(dayStatus=='1'){
              var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName + "\n"+statusName + "'><a href='javascript:set_week(-1);' title='上一周'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + "' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +");' style='"+statusStyle+";'>"+content+"</a> "+managerName+"</div>"
              );
              if(document.getElementById("td_"+userId+"_name")){
                $("td_"+userId+"_spanWeek").appendChild(div);
              }else{
                calendarTr(userId,userName,div);
              }
            }
            if(dayStatus=='2'){
              var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" +calTypeName + "\n"+ statusName +"'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"+ calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + "' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +");' style='"+statusStyle+";'>"+content+ "</a>"+managerName+" <a href='javascript:set_week(1);' title='下一周'><span style='font-family:Webdings'>4</span></a> </div>"
              );
              if(document.getElementById("td_"+userId+"_name")){
                $("td_"+userId+"_spanWeek").appendChild(div);
              }else{
                calendarTr(userId,userName,div);
              }
             }
            if(dayStatus=='3'){
              var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName + "\n"+statusName+"'><a href='javascript:set_week(-1);' title='上一周'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + "' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +");' style='"+statusStyle+";'>"+content+ "</a>"+managerName+" <a href='javascript:set_week(1);' title='下一周'><span style='font-family:Webdings'>4</span></a> </div>"
              );
              if(document.getElementById("td_"+userId+"_name")){
                $("td_"+userId+"_spanWeek").appendChild(div);
              }else{
                calendarTr(userId,userName,div);
              }
             }
            if(dayStatus=='4'){    
              var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName + "\n"+statusName + "'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"+ calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + "' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +");' style='"+statusStyle+";'>"+content+ "</a>"+managerName+" </div>"
              );
              if(document.getElementById("td_"+userId+"_name")){
                $("td_"+userId+"_spanWeek").appendChild(div);
              }else{
               calendarTr(userId,userName,div);
              }
            }
          }else{
            if(dayStatus=='1'){
              var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName + "\n"+ statusName+ "'><a href='javascript:set_week(-1);' title='上一周'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ",1' href='javascript:myNote(" + seqId + ");'  style='"+statusStyle+";'>"+content+ "</a> "+managerName+"</div>"
              );
              if(document.getElementById("td_"+userId+"_name")){
                $("td_"+userId+"_spanWeek").appendChild(div);
              }else{
                calendarTr(userId,userName,div);
              }
            }
            if(dayStatus=='2'){
              var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" +  calTypeName + "\n"+ statusName +"'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"+ calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a id='cal_" + seqId + ",1' href='javascript:myNote(" + seqId + ");' style='"+statusStyle+";'>"+content+ "</a> "+managerName+"<a href='javascript:set_week(1);' title='下一周'><span style='font-family:Webdings'>4</span></a> </div>"
              );
              if(document.getElementById("td_"+userId+"_name")){
                $("td_"+userId+"_spanWeek").appendChild(div);
              }else{
                calendarTr(userId,userName,div);
              }
             }
            if(dayStatus=='3'){
              var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName + "\n"+ statusName+ "'><a href='javascript:set_week(-1);' title='上一周'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a id='cal_" + seqId + ",1' href='javascript:myNote(" + seqId + ");'style='"+statusStyle+";'>"+content+ "</a> "+managerName+"<a href='javascript:set_week(1);' title='下一周'><span style='font-family:Webdings'>4</span></a> </div>"
              );
              if(document.getElementById("td_"+userId+"_name")){
                $("td_"+userId+"_spanWeek").appendChild(div);
              }else{
                calendarTr(userId,userName,div);
              }
             }
            if(dayStatus=='4'){    
              var div = new Element('div').update("<div style=\"left:" + left + "px;width:" + width + "px\" class=\"DayCalendar\" title='" + calTypeName + "\n"+ statusName +"'><span class='CalLevel"+calLevel+"' title='" + calLevelName+ "'>"+ calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a id='cal_" + seqId + ",1' href='javascript:myNote(" + seqId + ");' style='"+statusStyle+";'>"+content+ "</a>"+managerName+" </div>"
              );
              if(document.getElementById("td_"+userId+"_name")){
                $("td_"+userId+"_spanWeek").appendChild(div);
              }else{
               calendarTr(userId,userName,div);
              }
            }
          }   
        }
      }
   
     //没跨天
      for(var k=0;k<prcs.length;k++){
        var prc = prcs[k];
        var seqId = prc.seqId;
        var userId = prc.userId;
        var userName = prc.userName;
        var managerName = prc.managerName;
        var overStatus = prc.overStatus;
        var status = prc.status;
        var calLevel = prc.calLevel;
        var calType = prc.calType
        var content = prc.content;
        var managerId = prc.managerId;
        var calTime = prc.calTime;
        var endTime = prc.endTime;
  	    var start = setWeekDate(calTime);
  	    var end = setWeekDate(endTime);
        var dayStatus = prc.dayStatus;
        var statusName = "状态: 进行中";
        var calLevelName = "未指定"
        var calTypeName = "类型: 工作事务";
        if(managerName!=''){
          managerName = "\n安排人 ："+managerName;
        }
        if(calLevel.trim()==''){
          calLevel = 0;
        }
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
        if(calType=='2'){
          calTypeName = "类型: 个人事务";
        }
        if(userId==value){
          if(dayStatus=='0'){
            var xingqiji = getDayOfWeek(calTime.substr(0,10));
            if(xingqiji==0){
              xingqiji = 7;
            }
            var div = new Element('div').update("<div title='"
                +calTypeName+"\n"+ statusName + managerName+"'><span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>"
                + calTime.substr(11,5) + " - "
                + endTime.substr(11,5)+ "</span> "
                + "<br><a id='cal_"
                +seqId+"' href='javascript:myNote("+seqId+");'"
                + "onmouseover='showMenuWork(event," + seqId + ");' style='"+statusStyle+";'>"+content+"</a>"
                +"</div>");
           var div2 = new Element('div').update("<div title='"
               +calTypeName+"\n"+ statusName +managerName+ "'><span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>"
               + calTime.substr(11,5) + " - "
               + endTime.substr(11,5)+ "</span> "
               + "<br><a id='cal_"
               +seqId+"' href='javascript:myNote("+seqId+");'"
               + " style='"+statusStyle+";'>"+content+"</a>"
               +"</div>");
            if((managerId.trim()==''&&userId==userSeqId)||userSeqId==managerId.trim()){
              if(document.getElementById("td_"+userId+"_name")){
                $("td_"+userId+"_"+xingqiji).appendChild(div);
              }else{
               calendarTr2(userId,userName,xingqiji,div);   
              }
            }else{
              if(document.getElementById("td_"+userId+"_name")){
                $("td_"+userId+"_"+xingqiji).appendChild(div2);
              }else{
               calendarTr2(userId,userName,xingqiji,div2);     
              }
            }
          } 
        } 
      }
    }
    if($("td_"+value+"_name")){
      
    }else{
      calendarTr3(value,name);
    }
  }
  //事务
  affair(year,weekth,userIds,userSeqId);
  //任务
 task(userIds,userSeqId);
}
//查询部门
function dept(deptId){
  var deptIdTemp = document.getElementById("deptId").value;
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectDeptByParentId.act?deptId="+deptIdTemp
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  $("myDeptId").value = userId;
  var prcs = rtJson.rtData;
  var selectObj = document.getElementById("deptId");

  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var myOption = document.createElement("option");
    myOption.value = prc.value;
    myOption.text = prc.text;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
    if(deptId==prc.value){
      myOption.selected = true;
    }else{
       if(userId==prc.value){
         myOption.selected = true;
       }
    }
    
    //判断模块权限是指定人员的
    if(prc.value==''){
      document.getElementById("deptId").style.display = 'none';
      break;
    }
  }
  if(prcs.length==0){
    userId = userId + ",1";
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"360"}).update("<tr>"
      +"<td class='msg forbidden'>"
        +"<h4 class='title'>禁止</h4>"
        +"<div class='content' style='font-size:12pt'>您没有该部门的查看权限</div>"
       + "</td></tr>");
    $("all").update(table);
  }
  return userId ;
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
  document.getElementById("userIds").value = prcs.userId;
  return prcs.userId;
}
//日程跨天TableContent
function calendarTr(userId,userName,div){
  var tmp = "<td id='td_"+userId+"_name' width='80' align='center' rowspan=2 title='双击建立周事务' ondblclick='newCalendar("+userId+")'>"+userName+"</td>"
  +" <td colspan=7 id='td_"+userId+"_spanWeek'>"
  +"</td>";
  var tr1 = new Element('tr',{ "class":"TableData" ,"height":"30"});
  $("tbody").appendChild(tr1);
  tr1.update(tmp);
  //判断今天是星期几
  var curDate = new Date();
  var week = curDate.getDay();
  if(week ==0){
    week = 7;
  }
  var tmp1 = "";
  for(var i = 1;i<8;i++){
    var tdClass="TableData";
    if(week==i){
      tdClass = "TableContent";
    }
   tmp1 = tmp1 + "<td id='td_"+userId+"_"+i+"' class='"+tdClass+"'  title='双击建立日事务'  ondblclick='newCalendarIndex("+userId+","+(i-1)+")'></td>"
  }
 var tr = new Element('tr',{ "class":"TableData" ,"height":"30"});
 $("tbody").appendChild(tr);
 tr.update(tmp1) ;
 $("td_"+userId+"_spanWeek").appendChild(div);
}
//日程不跨天
function calendarTr2(userId,userName,xingqiji,div){
  //判断今天是星期几
  var curDate = new Date();
  var week = curDate.getDay();
  if(week ==0){
    week = 7;
  }
  var tmp = "<td id='td_"+userId+"_name' width='80' align='center'  title='双击建立周事务'  ondblclick='newCalendar("+userId+")'>"+userName+"</td>";
  for(var i = 1;i<8;i++){
    var tdClass="TableData";
    if(week==i){
      tdClass = "TableContent";
    }
   tmp = tmp  +"<td id='td_"+userId+"_"+i+"' class='"+tdClass+"' title='双击建立日事务' ondblclick='newCalendarIndex("+userId+","+(i-1)+")'></td>"
  }
   var tr = new Element('tr',{"class":"TableData" ,"height":"30"});
   $(tbody).appendChild(tr);
   tr.update(tmp);
   $("td_"+userId+"_"+xingqiji).appendChild(div);
}
//空日程
function calendarTr3(value,name){
  //判断今天是星期几
  var curDate = new Date();
  var week = curDate.getDay();
  if(week ==0){
    week = 7;
  }
  var tmp = "<td id='td_"+value+"_name' width='80' align='center'  title='双击建立周事务'  ondblclick='newCalendar("+value+")'>"+name+"</td>";
  for(var i = 1;i<8;i++){
    var tdClass="TableData";
    if(week==i){
      tdClass = "TableContent";
    }
   tmp = tmp  +"<td id='td_"+value+"_"+i+"' class='"+tdClass+"' title='双击建立日事务' ondblclick='newCalendarIndex("+value+","+(i-1)+")'></td>"
  }
  var tr = new Element('tr',{"class":"TableData" ,"height":"30"});
  $("tbody").appendChild(tr); 
  tr.update(tmp);
}
//事务
function affair(year,weekth,userIds,userSeqId){
  var URl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectAffairByDeptWeek.act?year="+year+"&week="+weekth+"&userIds="+userIds;
  var rtJsons = getJsonRs(URl);
  if(rtJsons.rtState == "1"){
    alert(rtJsons.rtMsrg); 
    return ;
    }
  var prcsData = rtJsons.rtData;
  //alert(prcsData.length);
  if(prcsData.length>0){
    var typeNames = ["每日","每周","每月","每年"];
    var weekNames = ["一","二","三","四","五","六","日"];
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
       var weekDates = ['<%=date1%>','<%=date2%>','<%=date3%>','<%=date4%>','<%=date5%>','<%=date6%>','<%=date7%>'];
       //判断结束时间是否为空
       if(endTime==''){
         for(var j=0;j<weekDates.length;j++){
           if(weekDates[j]>=beginTime.substr(0,10)){
             if(type=='2'){
               var day_of_week = getDayOfWeek(weekDates[j]);//判断排除周末
               if(isWeekend=='1'){
                 if(day_of_week!=0&&day_of_week!=6){
                   addDiv(userSeqId,userId,j+1,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content,managerId);
                 }
               }else{
                 addDiv(userSeqId,userId,j+1,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content,managerId);
               }
             }else if(type=='3'){ 
               if(remindDate==j+1){
                 addDiv(userSeqId,userId,j+1,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content,managerId); 
               }
             }else if(type=='4'){
               if(remindDate==parseInt(weekDates[j].substr(8,2),10)){
                 addDiv(userSeqId,userId,j+1,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content,managerId);
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
               if((m+"-"+d)==weekDates[j].substr(5,5)){
                 addDiv(userSeqId,userId,j+1,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content,managerId);
               }
             }
           }
         }
       }else{
         for(var j=0;j<weekDates.length;j++){
           if(weekDates[j]>=beginTime.substr(0,10)&&weekDates[j]<=endTime.substr(0,10)){
             if(type=='2'){
               var day_of_week = getDayOfWeek(weekDates[j]);//判断排除周末
               if(isWeekend=='1'){
                 if(day_of_week!=0&&day_of_week!=6){
                   addDiv(userSeqId,userId,j+1,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content,managerId);
                 }
               }else{
                 addDiv(userSeqId,userId,j+1,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content,managerId); 
               }
           }else if(type=='3'){ 
               if(remindDate==j+1){
                 addDiv(userSeqId,userId,j+1,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content,managerId) ;
               }
             }else if(type=='4'){
               if(remindDate==parseInt(weekDates[j].substr(8,2),10)){
                 addDiv(userSeqId,userId,j+1,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content,managerId);
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
               if((m+"-"+d)==weekDates[j].substr(5,5)){
                 addDiv(userSeqId,userId,j+1,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content,managerId);
               }
             }
           }
         }
       } 
    }
  }
}
//事务DIV
function addDiv(userSeqId,userId,index,remindTime,seqId,typeNames,type,week_day_month,lastRemind,beginTime,content,managerId){
  if((managerId.trim()==''&&userId==userSeqId)||userSeqId==managerId.trim()){
    var div = new Element('div').update(""
        + remindTime.substr(0,8)+" <br><a href='javascript:myAffair("+seqId+");' id='AT_"+seqId+",2,"+index+ "' onmouseover='showMenuWork2(event," + seqId + ",2,"+index+");' title='提醒时间："+  typeNames[parseInt(type)-2] + week_day_month +" " +remindTime.substr(0,5)
        + "\n上次提醒："+lastRemind
        + "\n起始时间："+beginTime+"'>"+content+"</a>" );
    $("td_"+userId+"_"+index).appendChild(div);
  }else{
    var div = new Element('div').update(""
        + remindTime.substr(0,8)+" <br><a href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] + week_day_month +" " +remindTime.substr(0,5)
        + "\n上次提醒："+lastRemind
        + "\n起始时间："+beginTime+"'>"+content+"</a>" );
    $("td_"+userId+"_"+index).appendChild(div);
  }
}
//任务
function task(userIds,userSeqId){
  var URl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectTaskByDeptDay.act?userIds="+userIds;
  var rtJsons = getJsonRs(URl);
  if(rtJsons.rtState == "1"){
    alert(rtJsons.rtMsrg); 
    return ;
    }
  var userSeqId = rtJsons.rtMsrg;
  var prcs = rtJsons.rtData;
  for(var i= 0;i<prcs.length;i++){
    var prc = prcs[i];
    var seqId = prc.seqId;
    var userId = prc.userId;
    var taskNo = prc.taskNo;
    var subject = prc.subject;
    var taskStatus = prc.taskStatus;
    var important = prc.important;
    var color = prc.color;
    var taskType = prc.taskType;
    var rate = prc.rate;
    var beginDate = prc.beginDate;
    var endDate = prc.endDate;
    var managerId = prc.managerId;
    var managerName = prc.managerName;
    if(important.trim()==''){
      important = 0;
    }
    if(managerName!=''){
      managerName = " (安排人："+managerName+")";
    }
    var weekDates = ['<%=date1%>','<%=date2%>','<%=date3%>','<%=date4%>','<%=date5%>','<%=date6%>','<%=date7%>'];
    if(beginDate==''&&endDate==''){
      for(var j = 1;j<7;j++){
        taskDiv(userId,seqId,subject,userSeqId,j+1,managerId,managerName);
      }
    }else if(beginDate==''&&endDate!=''){
      for(var j = 0;j<7;j++){
        if(weekDates[j]<=endDate.substr(0,10)){
          taskDiv(userId,seqId,subject,userSeqId,j+1,managerId,managerName);
        }
      } 
    }else if(beginDate!=''&&endDate==''){
      for(var j = 0;j<7;j++){
        if(weekDates[j]>=beginDate.substr(0,10)){
          taskDiv(userId,seqId,subject,userSeqId,j+1,managerId,managerName);
        }
      }
    }else {
      for(var j = 0;j<7;j++){
        if(weekDates[j]>=beginDate.substr(0,10)&&weekDates[j]<=endDate.substr(0,10)){
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
    div = new Element('div').update(" 任务：<a id='AT_"+seqId+",3,"+index+"' href='javascript:taskNote("+seqId+")'  onmouseover='showMenuWork2(event," + seqId + ",3,"+index+");'style='color:blue'>"+subject+"</a>"+managerName);
  }else{
    div = new Element('div').update(" 任务：<a href='javascript:taskNote("+seqId+")'  style='color:blue'>"+subject+"</a>"+managerName);
  }
  $("td_"+userId+"_"+index).appendChild(div);
}
function myAffair(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/affairnote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=180,width=170,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}
function set_view(temp){
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  var deptId = document.getElementById("deptId").value;
  var status = document.getElementById("status").value;
  var date = '<%=date1%>';
  if(temp=='day'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/info/day.jsp?date="+date+"&statusStr="+status+"&deptId="+deptId;
  }
  if(temp=='week'){  
    //document.location.href = "<%=contextPath%>/core/funcs/calendar/info/week.jsp?yearW="+year+"&weekW="+week+"&statusW="+status+"&deptId="+deptId;
    window.location.reload();
  }
  if(temp=='month'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/info/month.jsp?year="+year+"&status="+status+"&date="+date+"&deptId="+deptId;
  }
  setCookie("calendarQueryType",temp);
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
function set_year(index){
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  var status = document.getElementById("status").value;
  if(parseInt(year)<=2000){
    year = parseInt(year);
  }else if(parseInt(year)>=2049){
    year = parseInt(year);
  }else{
    year = parseInt(year)+index;
  }
  var deptId = document.getElementById("deptId").value;
  window.location = "<%=contextPath%>/core/funcs/calendar/info/week.jsp?yearW="+year+"&weekW="+week+"&statusW="+status+"&deptId="+deptId;
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
  var deptId = document.getElementById("deptId").value;

  window.location = "<%=contextPath%>/core/funcs/calendar/info/week.jsp?yearW="+yearW+"&weekW="+weekW+"&statusW="+statusW+"&deptId="+deptId;
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
    document.getElementById("front").style.display = 'block';
  }else{
    document.getElementById("front").style.display = 'none';
  }
}
function today(){
  var myDeptId = $("myDeptId").value;
  window.location = "<%=contextPath%>/core/funcs/calendar/info/week.jsp?deptId="+myDeptId;
}
function newCalendar(userId){
  var date1 = '<%=date1%>';
  var date7 = '<%=date7%>';
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  var URL = "<%=contextPath%>/core/funcs/calendar/info/index2.jsp?userId="+userId+"&year="+year+"&week="+week+"&date1="+date1+"&date7="+date7+"&dwm=week";
  window.open(URL,"calendar","height=450,width=550,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=200,resizable=yes");     
}
function newCalendarIndex(userId,index){
  var weekDates = ['<%=date1%>','<%=date2%>','<%=date3%>','<%=date4%>','<%=date5%>','<%=date6%>','<%=date7%>'];
  var date = weekDates[index];
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  var URL = "<%=contextPath%>/core/funcs/calendar/info/index2.jsp?userId="+userId+"&year="+year+"&week="+week+"&date="+date+"&dwm=weekIndex";
  window.open(URL,"calendar","height=450,width=550,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=200,resizable=yes");     
}
function newCalendarIndexDept(index){
  var deptId = document.getElementById("deptId").value;
  var userIds = document.getElementById("userIds").value;
  var weekDates = ['<%=date1%>','<%=date2%>','<%=date3%>','<%=date4%>','<%=date5%>','<%=date6%>','<%=date7%>'];
  var date = weekDates[index];
  var URL = "<%=contextPath%>/core/funcs/calendar/info/index2.jsp?date="+date+"&deptId="+deptId+"&dwm=weekIndexDept"+"&userIds="+userIds;
  window.open(URL,"calendar","height=450,width=550,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=200,resizable=yes");    
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
  var deptId = document.getElementById("deptId").value;
  window.location = "<%=contextPath%>/core/funcs/calendar/info/week.jsp?yearW="+year+"&weekW="+week+"&statusW="+status+"&deptId="+deptId;
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
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  var status = document.getElementById("status").value;
  window.location = "<%=contextPath%>/core/funcs/calendar/info/week.jsp?yearW="+year+"&weekW="+week+"&statusW="+status+"&deptId="+deptId
}
function selectCalendar(){
  var deptId = document.getElementById("deptId").value;
  window.location.href = "<%=contextPath%>/core/funcs/calendar/info/selectcalendarByTerm.jsp?deptId="+deptId;
}
function toDay(index){
  var weekDates = ['<%=date1%>','<%=date2%>','<%=date3%>','<%=date4%>','<%=date5%>','<%=date6%>','<%=date7%>'];
  var date = weekDates[index];
  var year = document.getElementById("year").value;
  var month = date.substr(5,2);
  var day = date.substr(8,2);
  var maxDay = DayNumOfMonth(year,month);
  var status = document.getElementById("status").value;
  var deptId = document.getElementById("deptId").value;
  window.location.href = "<%=contextPath%>/core/funcs/calendar/info/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&maxDay="+maxDay+"&deptId="+deptId;
}
function DayNumOfMonth(Year,Month) {
  var d = new Date(Year,Month,0);   
  return d.getDate(); 
}
</script>
<body class="" topmargin="5" onload="doOnload();">
<div class="PageHeader">
	<div class="left">
	  <form name="form1" action="/general/calendar/arrange/index.php" style="margin-bottom:5px;">
	  <input type="hidden" value="" name="BTN_OP">
	  <input type="hidden" value="" name="OVER_STATUS">
	  <input type="hidden" value="02" name="MONTH">
	  <input type="hidden" value="25" name="DAY">
	  <a href="javascript: void(0)" onclick="today()" class="ToolBtn"><span>今天</span></a>
	<!-- 年 -->
	  <a href="javascript:set_year(-1)";  title="上一年"><img src="<%=imgPath%>/prevpreviouspage.png"></img></a>
	  <a href="javascript:set_week(-1);" class="ArrowButtonR" title="上一周"><img src="<%=imgPath%>/previouspage.gif"></img></a>
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
	<!-- 周 -->
	  <select id="week" name="week"    onchange="My_Submit();;">
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
		   <input type="button" value="查询" class="SmallButton" title="查询" onclick="selectCalendar();">
	  <select id="deptId" name="deptId" onchange="myDeptOnchange();">  
	</select>
	
	   <a class="calendar-view day-view" href="javascript:set_view('day');" title="日视图"></a>
	   <a class="calendar-view week-view" href="javascript:set_view('week');" title="周视图"></a>
	   <a class="calendar-view month-view" href="javascript:set_view('month');" title="月视图"></a>
	  <input type="hidden" id="myDeptId" name="myDeptId" value=""></input>
	  </form>
	</div>
	<div class="clear"></div>
</div>
<div id="divList"></div>
    <input type="hidden" name="userIds" id="userIds" value="">
<table id="cal_table" class="TableBlock" width="100%" align="center"  >
  <tbody id="tbody">
    <tr id="tbl_header" align="center" class="TableHeader">
      <td width="9%" >姓名</td>
      <td id="th_1267977600" width="13%"  ondblclick="newCalendarIndexDept(0)"  title="双击为下边所有人员建立日事务">
          <a href="#" onclick="toDay(0)" style="color:#003366;font-size:9pt"><%=dateTempStr1 %>(星期一)</a>
      </td>
      <td id="th_1268064000" width="13%"  ondblclick="newCalendarIndexDept(1)" title="双击为下边所有人员建立日事务">
          <a href="#" onclick="toDay(1)"  style="color:#003366;font-size:9pt" ><%=dateTempStr2 %>(星期二)</a>
      </td>
      <td id="th_1268150400" width="13%"  ondblclick="newCalendarIndexDept(2)" title="双击为下边所有人员建立日事务">
          <a href="#" onclick="toDay(2)"  style="color:#003366;font-size:9pt"><%=dateTempStr3 %>(星期三)</a>
      </td>
      <td id="th_1268236800" width="13%"  ondblclick="newCalendarIndexDept(3)" title="双击为下边所有人员建立日事务">
          <a href="#" onclick="toDay(3)" style="color:#003366;font-size:9pt" ><%=dateTempStr4 %>(星期四)</a>
      </td>
      <td id="th_1268323200" width="13%"   ondblclick="newCalendarIndexDept(4)" title="双击为下边所有人员建立日事务">
          <a href="#" onclick="toDay(4)" style="color:#003366;font-size:9pt" ><%=dateTempStr5 %>(星期五)</a>
      </td>
      <td id="th_1268409600" width="13%"  ondblclick="newCalendarIndexDept(5)" title="双击为下边所有人员建立日事务">
          <a href="#" onclick="toDay(5)" style="color:#003366;font-size:9pt" ><%=dateTempStr6 %>(星期六)</a>
      </td>
      <td id="th_1268496000" width="13%" ondblclick="newCalendarIndexDept(6)" title="双击为下边所有人员建立日事务">
          <a href="#" onclick="toDay(6)" style="color:#003366;font-size:9pt" ><%=dateTempStr7 %>(星期日)</a>
      </td>
    </tr>
  </tbody>
</table>
</body>
</html>

