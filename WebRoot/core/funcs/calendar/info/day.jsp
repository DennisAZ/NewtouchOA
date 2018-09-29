<%@ page language="java" import="java.util.*,java.sql.Connection,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*,yh.core.data.YHRequestDbConn,yh.core.global.YHBeanKeys,yh.core.funcs.dept.logic.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
  String deptId = request.getParameter("deptId");
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
  if(statusStr!=null&&!statusStr.equals("null")){
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
  if(deptId==null){
   deptId = "0";
  }
  if(day>maxDay){
    day = maxDay;
  }
  //out.print(status+":"+statusStr!=null);
  //out.print(deptId);
  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>今天</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
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
function set_status(){
  var status = arguments[2];
  var statusNames = ['全部','未开始','进行中','已超时','已完成'];
  document.getElementById("status").value=status;
  document.getElementById("name").innerHTML=statusNames[status];
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var day = document.getElementById("day").value;
  var deptId = document.getElementById("deptId").value;
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/core/funcs/calendar/info/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&maxDay="+maxDay+"&deptId="+deptId;
}
function set_work(){
  var status = arguments[2];
  var seqId_CAT =  arguments[1];
  var seqId = seqId_CAT.split(',')[0].substr('cal_'.length);
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

function showMenuStatus(event){
  var menu = new Menu({bindTo:'statusName' , menuData:menuData2 , attachCtrl:true});
  menu.show(event);
}
function showMenuWork(event,seqId,CAT){
  var menu = new Menu({bindTo:'cal_'+seqId+","+CAT, menuData:menuData3 , attachCtrl:true});
  menu.show(event,seqId,CAT);
}
function doOnload(){
  var status = '<%=status%>';
  var year = '<%=year%>';
  var month = '<%=month%>';
  var day = '<%=day%>';
  var week = '<%=week%>';
  var deptId = '<%=deptId%>';
  var colorTypes = ["","#0000FF","#0000FF","#FF0000","#00AA00"];
  var statusNames = ['全部','未开始','进行中','已超时','已完成'];
  var colorType = colorTypes[status];
  if(month.length==1){
    month = "0"+month;
  }
  if(day.length==1){
    day = "0"+day;
  }
  document.getElementById("status").value=status;
  document.getElementById("name").innerHTML=statusNames[status];
  document.getElementById("name").style.color=colorType;
  document.getElementById("year").value=year;
  document.getElementById("month").value=month;
  document.getElementById("day").value=day;
  //部门
  var userDeptId = dept(deptId);
  //alert()
  //判断是否指定部门是否为空
  if(userDeptId.split(",").length==2){
    return;
  }
  if(deptId!='0'){
    document.getElementById("deptId").value=deptId;
  }
  deptId = document.getElementById("deptId").value;
  var userIds = getUser(deptId);
  var table = new Element('table',{"class":"TableBlock" ,"width":"100%" ,"align":"center"}).update( "<tbody id='tbody'><tr align='center' class='TableHeader' title='双击为该部门所有用户建立日事务' ondblclick='newCalendarByDept("+year+","+month+","+day+",\""+userIds+"\")'>"
      +"<td width='80'>姓名</td>"
      +"<td>"+year+"年"+month+"月"+day+"日("+week+")</td>"
      +"</tr></tbody>");
 $("div4List").appendChild(table);
 //得到所有人员


 //alert(userIds);
  //日程安排
  calendar(year,month,day,status,userIds,deptId);
  //周期性事物

  affair(year,month,day,userIds);
  //任务
 task(year,month,day,userIds);
}
//查询部门
function dept(deptId){
  var deptIdTemp = document.getElementById("deptId").value;
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectDeptByParentId.act?deptId="+deptIdTemp;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userDeptId = rtJson.rtMsrg;
  $("myDeptId").value = userDeptId;
  var prcs = rtJson.rtData;
  var   selectObj = document.getElementById("deptId");
  if(prcs.length>0){
    for(var i=0;i<prcs.length;i++){
      var prc = prcs[i];
      var myOption = document.createElement("option");
      myOption.value = prc.value;
      myOption.text = prc.text;
      selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
      if(deptIdTemp==prc.value){
        myOption.selected = true;
      }else{
        if(userDeptId==prc.value){
          myOption.selected = true;
        }
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
//查询日程安排
function calendar(year,month,day,status,userIds,deptId){
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectCalendarByDept.act?year="+year+"&month="+month+"&day="+day+"&status="+status+"&userIds="+userIds;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  //得到所有人员的Id和Name
  var prcsJson = rtJson.rtData;
  var users = prcsJson.users;
  for(var i = 0;i<users.length;i++){
    var user = users[i];
    var tr = new Element('tr',{"id":"tr_"+user.value,"class":"TableData","title":"双击为 "+user.name+" 建立日事务" });
    $("tbody").appendChild(tr);
    tr.update("<td align='center' ondblclick='newCalendar("+user.value+","+year+","+month+","+day+")'>"+user.name+"</td>"
        +"<td id='td_"+user.value+"'  ondblclick='newCalendar("+user.value+","+year+","+month+","+day+")'></td>");

  }
  var userSeqId = rtJson.rtMsrg; 
  var prcs = prcsJson.data;
  //跨天
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
    var userName = prc.userName.substr(0,prc.userName.length-1);
    var deptName = prc.deptName;
    if(calLevel.trim()==''){
      calLevel = 0;
    }
    if(managerId.trim()!=''){
      managerName = "(安排人:"+managerName+")";
    }
    var statusName = "状态 : 进行中";
    var calLevelName = "未指定"
    var calTypeName = "类型: 工作事务";
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
    if(calType=='2'){
      calTypeName = "类型: 个人事务";
    }
    var calLevelNames = ['未指定','重要/紧急','重要/不紧急','不重要/紧急','不重要/不紧急'];
    var dayStatus = prc.dayStatus;
    if(dayStatus!='0'){
      calendarDiv(dayStatus,statusName,userId,seqId,userSeqId,managerId,calLevel,calLevelNames,calTime,endTime,seqId,statusStyle,overStatus,content,calTypeName,managerName);
    }
    //没跨天的
    if(dayStatus=='0'){
      calendarDiv2(dayStatus,statusName,calTypeName,userId,seqId,userSeqId,managerId,calLevel,calLevelNames,calTime,endTime,seqId,statusStyle,overStatus,content,calTypeName,managerName);
    }  
  }
}
//跨天的DIV
function  calendarDiv(dayStatus,statusName,userId,seqId,userSeqId,managerId,calLevel,calLevelNames,calTime,endTime,seqId,statusStyle,overStatus,content,calTypeName,managerName){
  if((managerId.trim()==''&&userId==userSeqId)||userSeqId==managerId.trim()){
    if(dayStatus=='1'){
      var div = new Element('div').update("<div title='" + statusName +"\n" +  calTypeName +"'><a href='javascript:set_day(-1);' title='上一天'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a id='cal_" + seqId + ",1' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +",1);' style='"+statusStyle+"'>"+content+"</a> "+managerName+"</div>"
      );
      $("td_"+userId).appendChild(div);
    }
    if(dayStatus=='2'){
      var div = new Element('div').update("<div title='" + statusName +"\n" +  calTypeName +  "'><span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ",1' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId + ",1);;' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_day(1);' title='下一天'><span style='font-family:Webdings'>4</span></a> "+managerName+"</div>"
      );
      $("td_"+userId).appendChild(div);
     }

    if(dayStatus=='3'){
     
      var div = new Element('div').update("<div title='" + statusName + "\n" + calTypeName +  "'><a href='javascript:set_day(-1);' title='上一天'><span style='font-family:Webdings'>3</span></a>  <span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ",1' href='javascript:myNote(" + seqId + ");' onmouseover='showMenuWork(event," + seqId + ",1);' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_day(1);' title='下一天'><span style='font-family:Webdings'>4</span></a> "+managerName+"</div>"
      );
      $("td_"+userId).appendChild(div);
     }
   }else{
     if(dayStatus=='1'){
       var div = new Element('div').update("<div title='" + statusName + "\n" + calTypeName +"'><a href='javascript:set_day(-1);' title='上一天'><span style='font-family:Webdings'>3</span></a> <span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a id='cal_" + seqId + ",1' href='javascript:myNote(" + seqId + ");'  style='"+statusStyle+"'>"+content+"</a> </div>"
       );
       $("td_"+userId).appendChild(div);
     }
     if(dayStatus=='2'){
       var div = new Element('div').update("<div title='" + statusName + "\n" + calTypeName +  "'><span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ",1' href='javascript:myNote(" + seqId + ");' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_day(1);' title='下一天'><span style='font-family:Webdings'>4</span></a> </div>"
       );
       $("td_"+userId).appendChild(div);
      }
     if(dayStatus=='3'){
       var div = new Element('div').update("<div title='" + statusName + "\n" + calTypeName +  "'><a href='javascript:set_day(-1);' title='上一天'><span style='font-family:Webdings'>3</span></a>  <span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel]+ "'>" + calTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ",1' href='javascript:myNote(" + seqId + ");' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_day(1);' title='下一天'><span style='font-family:Webdings'>4</span></a></div>"
       );
       $("td_"+userId).appendChild(div);
      }
    
   }


}
//不跨天DIV
function calendarDiv2(dayStatus,statusName,calTypeName,userId,seqId,userSeqId,managerId,calLevel,calLevelNames,calTime,endTime,seqId,statusStyle,overStatus,content,calTypeName,managerName){
  if((managerId.trim()==''&&userId==userSeqId)||userSeqId==managerId.trim()){
     if(dayStatus=='0'){
       var div = new Element('div').update("<div title='"
           + statusName + "\n" + calTypeName + "'><span class='CalLevel"+calLevel+"' title='"+calLevelNames[calLevel]+"'>"
           + calTime.substr(11,5) + " - "
           + endTime.substr(11,5)+ "</span> "
           + "<a id='cal_"
           +seqId+",1' href='javascript:myNote("+seqId+");'"
           + "onmouseover='showMenuWork(event," + seqId + ",1);' style='"+statusStyle+";'>"+content+"</a> "
           +managerName+"</div>");
       $("td_"+userId).appendChild(div);
     }     
   }else{
     if(dayStatus=='0'){
       var div = new Element('div').update("<div title='"
           + statusName + "\n" +  calTypeName + "'><span class='CalLevel"+calLevel+"' title='"+calLevelNames[calLevel]+"'>"
           + calTime.substr(11,5) + " - "
           + endTime.substr(11,5)+ "</span> "
           + "<a id='cal_"
           +seqId+",1' href='javascript:myNote("+seqId+");'"
           + " style='"+statusStyle+";'>"+content+"</a>"
           +"</div>");
       $("td_"+userId).appendChild(div);
     }     
   }  
}
//查询事务
function affair(year,month,day,userIds){
  var URl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectAffairByDeptDay.act?year="+year+"&month="+month+"&day="+day+"&userIds="+userIds;
  var rtJsons = getJsonRs(URl);
  if(rtJsons.rtState == "1"){
    alert(rtJsons.rtMsrg); 
    return ;
    }
  var userSeqId = rtJsons.rtMsrg;
  var prcsData = rtJsons.rtData;
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
       var managerName = prcAffair.managerName;
       var type = prcAffair.type;
       var content = prcAffair.content;
       if(managerName.trim()!=''){
         managerName = " (安排人："+managerName+")";
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
       affairDiv(userId,remindTime,seqId,userSeqId,typeNames,type,week_day_month,lastRemind,beginTime,content,managerName,managerId);
    }
  }
}
function affairDiv(userId,remindTime,seqId,userSeqId,typeNames,type,week_day_month,lastRemind,beginTime,content,managerName,managerId){
  if((managerId.trim()==''&&userId==userSeqId)||userSeqId==managerId.trim()){
    var div = new Element('div').update(""
        + remindTime.substr(0,8)+" <a id='cal_"+seqId+",2' href='javascript:myAffair("+seqId+");' onmouseover='showMenuWork(event," + seqId + ",2);' title='提醒时间："+  typeNames[parseInt(type)-2] + week_day_month +" " +remindTime.substr(0,5)
        +"\n上次提醒："+lastRemind
        +"\n起始时间："+beginTime+"'>"+content+"</a>"+ managerName);
      $("td_"+userId).appendChild(div);
  }else{
    var div = new Element('div').update(""
        + remindTime.substr(0,8)+" <a id='cal_"+seqId+",2' href='javascript:myAffair("+seqId+");' title='提醒时间："+  typeNames[parseInt(type)-2] + week_day_month +" " +remindTime.substr(0,5)
        + "\n上次提醒："+lastRemind
        + "\n 起始时间："+beginTime+"'>"+content+"</a>  "+managerName );
      $("td_"+userId).appendChild(div)
  }
}
//查询任务 
function task(year,month,day,userIds){
  var URl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectTaskByDeptDay.act?year="+year+"&month="+month+"&day="+day+"&userIds="+userIds;
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
    if(managerName!=''){
      managerName = " (安排人："+managerName+")";
    }
    if(important.trim()==''){
      important = 0;
    }
    if(beginDate==''&&endDate==''){
      taskDiv(userId,seqId,subject,userSeqId);
    }else if(beginDate==''&&endDate!=''){
      if((year+"-"+month+"-"+day)<=endDate.substr(0,10)){
        taskDiv(userId,seqId,subject,userSeqId,managerName,managerId);
      }
    }else if(beginDate!=''&&endDate==''){
      if((year+"-"+month+"-"+day)>=beginDate.substr(0,10)){
        taskDiv(userId,seqId,subject,userSeqId,managerName,managerId);
      }
    }else {
      if((year+"-"+month+"-"+day)>=beginDate.substr(0,10)&&(year+"-"+month+"-"+day)<=endDate.substr(0,10)){
        taskDiv(userId,seqId,subject,userSeqId,managerName,managerId);
      }
    }
  }
}
//任务Div
function taskDiv(userId,seqId,subject,userSeqId,managerName,managerId){
  if((managerId.trim()==''&&userId==userSeqId)||userSeqId==managerId.trim()){
    var div = new Element('div').update(" 任务：<a id='cal_"+seqId+",3' href='javascript:taskNote("+seqId+")'  onmouseover='showMenuWork(event," + seqId + ",3);'style='color:blue'>"+subject+"</a>"+managerName);
    $("td_"+userId).appendChild(div);
  }else{
    var div = new Element('div').update(" 任务：<a id='task_"+seqId+"' href='javascript:taskNote("+seqId+")'  style='color:blue'>"+subject+"</a>"+managerName);
    $("td_"+userId).appendChild(div);
  }
}
function myDeptOnchange(){
  var deptId = document.getElementById("deptId").value;
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var day = document.getElementById("day").value;
  var status = document.getElementById("status").value;
  window.location = "<%=contextPath%>/core/funcs/calendar/info/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&deptId="+deptId
}
function set_view(temp){
  var deptId = document.getElementById("deptId").value;
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var day = document.getElementById("day").value;
  var statusStr = document.getElementById("status").value;
  if(temp=='day'){  
   // var maxDay = DayNumOfMonth(year,month);
    //document.location.href = "<%=contextPath%>/core/funcs/calendar/info/.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+statusStr+"&maxDay="+maxDay+"&deptId="+deptId;
     document.location.reload();
  }
  if(temp=='week'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/info/week.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+statusStr+"&deptId="+deptId;
  }
  if(temp=='month'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/info/month.jsp?year="+year+"&month="+month+"&day="+day+"&status="+statusStr+"&ldwm=month"+"&deptId="+deptId;
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
function My_Submit(){
  var deptId = document.getElementById("deptId").value;
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var day = document.getElementById("day").value;
  var status = document.getElementById("status").value;
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/core/funcs/calendar/info/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&maxDay="+maxDay+"&deptId="+deptId;
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
  var myDeptId = $("myDeptId").value ;
  window.location="<%=contextPath%>/core/funcs/calendar/info/day.jsp?deptId="+myDeptId;
}
function set_year(index){
  var deptId = document.getElementById("deptId").value;
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
  window.location = "<%=contextPath%>/core/funcs/calendar/info/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&maxDay="+maxDay+"&deptId="+deptId;

}
function set_month(index){
  var deptId = document.getElementById("deptId").value;
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
  window.location = "<%=contextPath%>/core/funcs/calendar/info/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&maxDay="+maxDay+"&deptId="+deptId;
}
function set_day(index){
  var deptId = document.getElementById("deptId").value;
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
  window.location = "<%=contextPath%>/core/funcs/calendar/info/day.jsp?year="+year+"&month="+month+"&day="+day+"&statusStr="+status+"&maxDay="+maxDay+"&deptId="+deptId;
}
function DayNumOfMonth(Year,Month) {
  var d = new Date(Year,Month,0);   
  return d.getDate(); 
}
function myNote(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status="+status;
  window.open(URL,"my_note","height=200,width=200,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=600,top=300,resizable=yes");
}
function myAffair(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/affairnote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=180,width=170,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}
function taskNote(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/tasknote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=180,width=170,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}
function selectCalendar(){
  var deptId = document.getElementById("deptId").value;
  window.location.href = "<%=contextPath%>/core/funcs/calendar/info/selectcalendarByTerm.jsp?deptId="+deptId;
}
function newCalendar(userId,year,month,day,deptId){
  var URL = "<%=contextPath%>/core/funcs/calendar/info/index2.jsp?userId="+userId+"&year="+year+"&month="+month+"&day="+day+"&deptId="+deptId+"&dwm=day";
  window.open(URL,"calendar","height=450,width=550,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=200,resizable=yes");     
}
function newCalendarByDept(year,month,day,userIds){
  var URL = "<%=contextPath%>/core/funcs/calendar/info/index2.jsp?year="+year+"&month="+month+"&day="+day+"&userIds="+userIds+"&dwm=dayDept";
  window.open(URL,"calendar","height=450,width=550,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=200,resizable=yes");    

}

</script>
<body class="" topmargin="5" onload="doOnload();">
<div id="all">
<form name="form1" action="#" style="margin-bottom:5px;">
<div id="div1" class="PageHeader">
	<div id="div2" class="left">
	  <input type="hidden" value="" name="BTN_OP">
	  <input type="hidden" value="4" name="OVER_STATUS">
	  <a href="javascript: void(0)" onclick="today()" class="ToolBtn"><span>今天</span></a>
	  <a href="javascript:set_year(-1)";  title="上一年"><img src="<%=imgPath%>/previouspage.gif"></img></a><select id="year" name="year"    onchange="My_Submit();">
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
	
	
	  <a href="javascript:set_month(-1);" class="ArrowButtonL" title="上一月"><img src="<%=imgPath%>/previouspage.gif"></img></a><select id="month" name="month"  onchange="My_Submit();">
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
	
	  <a href="javascript:set_day(-1);" class="ArrowButtonL" title="上一天"><img src="<%=imgPath%>/previouspage.gif"></img></a><select id="day" name="day"   onchange="My_Submit();">
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
	  <a id="statusName" href="javascript:;" class="dropdown" onclick="showMenuStatus(event);" hidefocus="true"><span id="name" style="">全部</span></a>&nbsp;
	  <input type="hidden" id="status" name="status" value="0"></input>
	</div>
	
	<div id="div3" class="right">
	
	  <input type="button" value="查询" class="SmallButton" title="查询" onclick="selectCalendar();"/>
	  <select id="deptId" name="deptId"  onchange="myDeptOnchange();" >
	  </select>&nbsp;
	  <a class="calendar-view day-view" href="javascript:set_view('day');" title="日视图"></a>
    <a class="calendar-view week-view" href="javascript:set_view('week');" title="周视图"></a>
    <a class="calendar-view month-view" href="javascript:set_view('month');" title="月视图"></a>
	</div>
	<div class="clear"></div>
 <input type="hidden" id="myDeptId" name="myDeptId" value=""></input>
</div>
</form>
<div id="div4List"></div>
<div align=right style="display:none">
<span class="small" style="cursor:pointer;" title="点击打开操作提示与技巧窗口" onclick="javascript:window.alert('开发中...')">&nbsp;<img src="<%=imgPath%>/iask_small.gif" align="absmiddle"></span></div>
</div>
</body>
</html>