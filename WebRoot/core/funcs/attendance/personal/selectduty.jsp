<%@ page language="java"  import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.person.data.YHPerson,yh.core.funcs.calendar.data.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
  int year1 = Integer.parseInt(dateStr.substring(0,4));
  int month = Integer.parseInt(dateStr.substring(5,7));
  int day = Integer.parseInt(dateStr.substring(8,10));
  Calendar time=Calendar.getInstance(); 
  time.clear(); 
  time.set(Calendar.YEAR,year); //year 为 int 
  time.set(Calendar.MONTH,month-1);//注意,Calendar对象默认一月为0           
  int maxDay = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数 
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
  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
  int userId = user.getSeqId();
  
  long daySpace = YHUtility.getDaySpan(dateFormat1.parse(yearMonth + "-01"),dateFormat1.parse(yearMonth + "-" +maxDay))+1;
  //得到到之间的天数数组
  List daysList = new ArrayList();
  String days = "";
  Calendar calendar = new GregorianCalendar();
  for(int i = 0;i<daySpace;i++){
    calendar.setTime(dateFormat1.parse(yearMonth + "-01"));
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
<title>上下班记录查询</title>
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
    var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/getUserDutyInfo.act?userId="+userId+"&days="+days;
    var rtJson = getJsonRs(URL);
    if(rtJson.rtState == "1"){
      //alert(rtJson.rtMsrg); 
      return ;
    }
    var prcs= rtJson.rtData;
    table.update(trTempTitle+prcs.trTemp);
  }
}

function Init(){
    var requestURLDuty = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/selectdutyByDate.act"; 
    var rtjson = getJsonRs(requestURLDuty,mergeQueryString($("form1"))); 
    //alert(rsText);
    if(rtjson.rtState == '1'){ 
      alert(rtjson.rtMsrg); 
      return ; 
    }
    if(rtjson.rtState == '0'){ 
      var date = rtjson.rtMsrg;
      var dates = date.split(",");
      document.getElementById("selectDiv").style.display = 'none'; 
      document.getElementById("selectTable").style.display = 'none'; 
      document.getElementById("resultTable").style.display = 'block'; 
      document.getElementById("listDiv").style.display = 'block'; 
      document.getElementById("returnDiv").style.display = 'block';
      document.getElementById("returndate1").innerHTML = dates[0];
      document.getElementById("returndate2").innerHTML = dates[1];
      document.getElementById("daySpace").innerHTML = dates[2];
    }
    var dutyJson = rtjson.rtData;
    var requestURLConfig = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/selectConfigById.act"; 
    var json = getJsonRs(requestURLConfig); 
    //alert(rsText);
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    } 
    //alert(rsText);
    var prcs = json.rtData;
    if(prcs.seqId){
      var seqId = prcs.seqId;
      var dutyName = prcs.dutyName;
      var dutyTime1 = prcs.dutyTime1; 
      var dutyTime2 = prcs.dutyTime2; 
      var dutyTime3 = prcs.dutyTime3; 
      var dutyTime4 = prcs.dutyTime4; 
      var dutyTime5 = prcs.dutyTime5; 
      var dutyTime6 = prcs.dutyTime6;
      var dutyType1 = prcs.dutyType1; 
      var dutyType2 = prcs.dutyType2; 
      var dutyType3 = prcs.dutyType3; 
      var dutyType4 = prcs.dutyType4; 
      var dutyType5 = prcs.dutyType5; 
      var dutyType6 = prcs.dutyType6;  
      if(dutyType1 == "1"){
        dutyType1 = "上班"
      }else{
        dutyType1 = "下班"
       }
      if(dutyType2 == "1"){
        dutyType2 = "上班"
      }else{
        dutyType2 = "下班"
       }
      if(dutyType3 == "1"){
        dutyType3 = "上班"
      }else{
        dutyType3 = "下班"
       }
      if(dutyType4 == "1"){
        dutyType4 = "上班"
      }else{
        dutyType4 = "下班"
       }
      if(dutyType5 == "1"){
         dutyType5 = "上班"
       }else{
         dutyType5 = "下班"
        }
      if(dutyType6 == "1"){
        dutyType6 = "上班"
      }else{
        dutyType6 = "下班"
       }
      if(prcs != "undefined"){
        var table = new Element('table',{"class":"TableList" ,"width":"100%"}).update("<tbody id = 'tboday'></tbody>");
        $('listDiv').appendChild(table); 
        var tr = new Element('tr',{"class":"TableHeader"});
        $('tboday').appendChild(tr);
        var tds = ''; 
        tds = "<td align='center' nowrap>日期</td>";
        if(dutyTime1.trim() != ""){
          tds = tds + "<td  align='center' nowrap>"+dutyType1+"(" + dutyTime1 + ")</td>";
        }
        if(dutyTime2.trim() != ""){
          tds = tds + "<td  align='center' nowrap>"+dutyType2+"(" + dutyTime2 + ")</td>";
        }
        if(dutyTime3.trim() != ""){
          tds = tds + "<td  align='center' nowrap>"+dutyType3+"(" + dutyTime3 + ")</td>";
        }
        if(dutyTime4.trim() != ""){
          tds = tds + "<td  align='center' nowrap>"+dutyType4+"(" + dutyTime4 + ")</td>";
        }
        if(dutyTime5.trim() != ""){
          tds = tds + "<td  align='center' nowrap>"+dutyType5+"(" + dutyTime5 + ")</td>";
        }
        if(dutyTime6.trim() != ""){
          tds = tds + "<td  align='center' nowrap>"+dutyType6+"(" + dutyTime6 + ")</td>";
        }
        tr.update(tds);
        for(var i = 0; i< dutyJson.length; i++){
          var duty = dutyJson[i]; 
          if(duty.generalStatus =="1"||holiday(duty.today.substr(0,10))){
            var tr = new Element('tr',{"id":"tr" ,"class":"TableContent"});
            $('tboday').appendChild(tr); 
          }else{
            var tr = new Element('tr',{"id":"tr" ,"class":"TableData"});
            $('tboday').appendChild(tr); 
          }
          //var td = new Element('td',{"align":"center","nowrap":true});
         // tr.appendChild(td); 
         // td.update(duty.today);
          var td = "<td align='center' nowrap>" + duty.today + "</td>";
          tr.update(td);
          if(dutyTime1.trim() != "undefined"){
            if(duty.registerTime1){   
              var c_zStatus1 = ""        
              if(duty.c_zStatus1 =="1"&&!(duty.generalStatus == "1")&&!holiday(duty.today.substr(0,10))){c_zStatus1 =  " 迟到";}
              if(duty.c_zStatus1 =="2"&&!(duty.generalStatus == "1")&&!holiday(duty.today.substr(0,10))){c_zStatus1 =  " 早退";}
              if(duty.remark1 != ""){
                var td = new Element('td',{"align":"center"});
                tr.appendChild(td); 
                td.update(duty.registerTime1.substr(11,8) + " " + c_zStatus1 + "<br> 备注：" + duty.remark1);
              }else{
                var td = new Element('td',{"align":"center"});
                tr.appendChild(td); 
                td.update(duty.registerTime1.substr(11,8) + " " + c_zStatus1);
              }         
            }else{
              var td = new Element('td',{"align":"center"});
              tr.appendChild(td); 
              if(holiday(duty.today.substr(0,10))){
                td.update("<font color='#008000'>节假日</font>")
              }else if(duty.generalStatus == "1"){
                td.update("<font color='#008000'>公休日</font>");
              }else{
                td.update("未登记");
              }
            }            
          }//第2次
          if(dutyTime2.trim() != ""){
            var registerTime2 = duty.registerTime2;
            var c_zStatus2 = ""
            if(registerTime2){           
              if(duty.c_zStatus2 =="1"&&!(duty.generalStatus == "1")&&!holiday(duty.today.substr(0,10))){c_zStatus2 =  " 迟到";}
              if(duty.c_zStatus2 =="2"&&!(duty.generalStatus == "1")&&!holiday(duty.today.substr(0,10))){c_zStatus2 =  " 早退";}
              if(duty.remark2 != ""){
                var td = new Element('td',{"align":"center"});
                tr.appendChild(td); 
                td.update(duty.registerTime2.substr(11,8) + " " + c_zStatus2 + "<br> 备注：" + duty.remark2);
              }else{
                var td = new Element('td',{"align":"center"});
                tr.appendChild(td); 
                td.update(duty.registerTime2.substr(11,8) + " " + c_zStatus2);
              }         
            }else{  
              var td = new Element('td',{"align":"center"});
              tr.appendChild(td); 
              if(holiday(duty.today.substr(0,10))){
                
                td.update("<font color='#008000'>节假日</font>");
              }else  if(duty.generalStatus == "1"){   
                td.update("<font color='#008000'>公休日</font>");
              }else{
                td.update("未登记");
                //alert(duty.generalStatus);
             }
           }            
          }
          //第3次
          if(dutyTime3.trim() != ""){
            var registerTime3 = duty.registerTime3;
            var c_zStatus3 = "";
            if(registerTime3){      
              if(duty.c_zStatus3 =="1"&&!(duty.generalStatus == "1")&&!holiday(duty.today.substr(0,10))){c_zStatus3 = "迟到";}
              if(duty.c_zStatus3 =="2"&&!(duty.generalStatus == "1")&&!holiday(duty.today.substr(0,10))){c_zStatus3 = "早退";}
                if(duty.remark3 != ""){
                var td = new Element('td',{"align":"center"});
                tr.appendChild(td); 
                td.update(duty.registerTime3.substr(11,8) + " " + c_zStatus3 + "<br> 备注：" + duty.remark3);
              }else{
                var td = new Element('td',{"align":"center"});
                tr.appendChild(td); 
                td.update(duty.registerTime3.substr(11,8) + " " + c_zStatus3);
              }         
            }else{
              var td = new Element('td',{"align":"center"});
              tr.appendChild(td);
              if(holiday(duty.today.substr(0,10))){
                td.update("<font color='#008000'>节假日</font>");
              }else if(duty.generalStatus == "1"){
                td.update("<font color='#008000'>公休日</font>");
              }else{
                td.update("未登记");
                //alert(duty.generalStatus);
             }
           }            
          }
          //第4次
          if(dutyTime4.trim() != ""){
            var registerTime4 = duty.registerTime4;
            var c_zStatus4 = "";
            if(registerTime4){         
              if((duty.c_zStatus4 =="1")&&!(duty.generalStatus == "1")&&!holiday(duty.today.substr(0,10))){c_zStatus4 = "迟到";}
              if((duty.c_zStatus4 =="2")&&!(duty.generalStatus == "1")&&!holiday(duty.today.substr(0,10))){c_zStatus4 = "早退";}
              if(duty.remark4 != ""){
                var td = new Element('td',{"align":"center"});
                tr.appendChild(td); 
                td.update(duty.registerTime4.substr(11,8) + " " + c_zStatus4 + "<br> 备注：" + duty.remark4);
              }else{
                var td = new Element('td',{"align":"center"});
                tr.appendChild(td); 
                td.update(duty.registerTime4.substr(11,8) + " " + c_zStatus4);
              }         
            }else{
              var td = new Element('td',{"align":"center"});
              tr.appendChild(td); 
              if(holiday(duty.today.substr(0,10))){
                td.update("<font color='#008000'>节假日</font>");
              }else if(duty.generalStatus == "1"){  
                td.update("<font color='#008000'>公休日</font>");
              }else{          
                td.update("未登记");
                //alert(duty.generalStatus);
             }
           }            
          }
          //第5次
          if(dutyTime5.trim() != ""){
            var registerTime5 = duty.registerTime5;
            var c_zStatus5 = "";
            if(registerTime5){           
              if(duty.c_zStatus5 =="1"&&!(duty.generalStatus == "1")&&!holiday(duty.today.substr(0,10))){c_zStatus5 = "迟到";}
              if(duty.c_zStatus5 =="2"&&!(duty.generalStatus == "1")&&!holiday(duty.today.substr(0,10))){c_zStatus5 = "早退";}
              if(duty.remark5 != ""){
                var td = new Element('td',{"align":"center"});
                tr.appendChild(td); 
                td.update(duty.registerTime5.substr(11,8) + " " + c_zStatus5 + "<br> 备注：" + duty.remark5);
              }else{
                var td = new Element('td',{"align":"center"});
                tr.appendChild(td); 
                td.update(duty.registerTime5.substr(11,8) + " " + c_zStatus5);
              }         
            }else{
              var td = new Element('td',{"align":"center"});
              tr.appendChild(td); 
              if(holiday(duty.today.substr(0,10))){
                td.update("<font color='#008000'>节假日</font>");
              }else if(duty.generalStatus == "1"){
                td.update("<font color='#008000'>公休日</font>");
              }else{
                td.update("未登记");
                //alert(duty.generalStatus);
              }
            }            
          }
          //第6次
          if(dutyTime6.trim() != ""){
            var registerTime6 = duty.registerTime6;
            var c_zStatus6 = "";
            if(registerTime6){           
              if(duty.c_zStatus6 =="1"&&!(duty.generalStatus == "1")&&!holiday(duty.today.substr(0,10))){c_zStatus6 = "迟到";}
              if(duty.c_zStatus6 =="2"&&!(duty.generalStatus == "1")&&!holiday(duty.today.substr(0,10))){c_zStatus6 = "早退";}
              if(duty.remark6 != ""){
                var td = new Element('td',{"align":"center"});
                tr.appendChild(td); 
                td.update(duty.registerTime6.substr(11,8) + " " + c_zStatus6 + "<br> 备注:" + duty.remark6);
              }else{
                var td = new Element('td',{"align":"center"});
                tr.appendChild(td); 
                td.update(duty.registerTime6.substr(11,8) + " " + c_zStatus6);
              }         
            }else{
              var td = new Element('td',{"align":"center"});
              tr.appendChild(td); 
              if(holiday(duty.today.substr(0,10))){
                td.update("<font color='#008000'>公休日</font>");
              }else if(duty.generalStatus == "1"){
                td.update("<font color='#008000'>公休日</font>");
              }else{
                td.update("未登记");
                //alert(duty.generalStatus);
             }
           }            
          }               
        }
      }
    }
}
//判断是否是节假日
function holiday(date){
  var userId = '<%=userId%>';
  var requestURLHoliday = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/checkHoliday.act?date="+date;
  var rtJson = getJsonRs(requestURLHoliday);
  var holidayJson = rtJson.rtData;
  var holiday = holidayJson.status;//0为公假日
  if(holiday=="1"){
    return false;
  }
  return true;
}

function My_Submit(){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  window.location="<%=contextPath%>/core/funcs/attendance/personal/selectduty.jsp?year="+year+"&month="+month;
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
  window.location="<%=contextPath%>/core/funcs/attendance/personal/selectduty.jsp?year="+year+"&month="+month;
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
  window.location="<%=contextPath%>/core/funcs/attendance/personal/selectduty.jsp?year="+year+"&month="+month;
}
function remark(seqId){
  var URL="/yh/core/funcs/attendance/personal/dutyRemark.jsp?seqId=" + seqId ;
  myleft=(screen.availWidth-650)/2;
  window.open(URL,"formul_edit","height=250,width=450,status=0,toolbar=no,menubar=no,location=no,scrollbars=no,top=150,left="+myleft+",resizable=yes");
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table id="selectTable" border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;上下班记录查询</span><br>
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
<div id="dutyTable"></div>
<br>
<div id="listDiv" align="center" style="display:none">
</div>
<div id="returnDiv" align="center" style="display:none">
<br></br>
  <input type="button" value="返回" class="BigButton" onclick="window.location.reload();"></input>
</div>
</body>
</html>