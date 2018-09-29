<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String userId = request.getParameter("userId");
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM");
  Date curDate = new Date();
  String dateStr = dateFormat.format(curDate);
  String dateStr1 = dateFormat1.format(curDate);
  String dateStr2 = dateFormat2.format(curDate);
  dateStr2 = dateStr2 + "-01";
  
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  boolean isAdmin = loginUser.isAdmin();
  
  Date date = new Date();
  SimpleDateFormat dateFormatWeek = new SimpleDateFormat("E");
  SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
  String week = dateFormatWeek.format(date);
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
<html class="hiddenRoll">
<head>
<title>人员考勤记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/attendance/manage/js/statisticsQueryLogic.js"></script>
<script type="text/javascript">
var yearStr = "<%=year%>";
var monthStr = "<%=month%>";
var userIdStr = '<%=userId%>';

function CheckForm(){
   var beginTime = document.getElementById("beginTime");
   var endTime = document.getElementById("endTime");
   if(beginTime.value==""){ 
     alert("起始日期不能为空！");
     return (false);
   }
   if(endTime.value==""){ 
     alert("截止日期不能为空！");
     return (false);
   }
   if(!isValidDateStr(beginTime.value)){
     alert("起始日期格式不对,应形如 2010-02-01");
     beginTime.focus();
     beginTime.select();
     return false;
   }
   if(!isValidDateStr(endTime.value)){
     alert("截止日期格式不对,应形如 2010-02-01");
     endTime.focus();
     endTime.select();
     return false;
   }
   var beginInt;
   var endInt;
   var beginArray = beginTime.value.split("-");
   var endArray = endTime.value.split("-");
   for(var i = 0 ; i<beginArray.length; i++){
     beginInt = parseInt(" " + beginArray[i]+ "",10);  
     endInt = parseInt(" " + endArray[i]+ "",10);
     if((beginInt - endInt) > 0){
       alert("起始日期不能大于截止日期!");
       endTime.focus();
       endTime.select();
       return false;
     }else if(beginInt - endInt<0){
       return true;
     }  
   }
   return (true);
}

function CheckForm2(){
  var time = document.getElementById("time");
   if(time.value==""){ 
     alert("查询日期不能为空！");
     return (false);
   }
   if(!isValidDateStr(time.value)){
     alert("起始日期格式不对,应形如 2010-02-01");
     time.focus();
     time.select();
     return false;
   }
   return (true);
}
function doOnload(){
  var userId = '<%=userId%>';
  if(userId=='null'){
    document.getElementById("nullList").style.display = "";
    document.getElementById("attendanDiv").style.display = "none";
  }else{
    //得到用户的信息
    selectUserInfo(userId);
    //建表
    newTable();
    //得到上下班类型    getPersonal();
    //config(userId);
    //
    generalIs('2-2');
  }
  //初始化时间
  var date1Parameters = {
      inputId:'beginTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'endTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
  var date3Parameters = {
      inputId:'time',
      property:{isHaveTime:false}
      ,bindToBtn:'date3'
  };
  new Calendar(date3Parameters);
}



function selectUserInfo(userId){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectUserInfo.act?userId="+userId;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  var userName = prc.userName;
  var annualLeave = prc.annualLeave;
  //document.getElementById("userName").innerHTML = userName;
 // document.getElementById("annualLeave").innerHTML = annualLeave; 
  if(prc.leaveDaysTotal!=''){
    //document.getElementById("leaveDaysTotal").innerHTML = prc.leaveDaysTotal;
  }
}
function newTable(){
  var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tboday'><tr class='TableHeader'><td nowrap align='center'>登记次序</td>"
      + "<td nowrap align='center'>登记类型</td>"
      + "<td nowrap align='center'>规定时间</td>"
      + "<td nowrap align='center'>登记时间</td>"
      + "<td nowrap align='center'>登记IP</td></tr></tbody>");
    //$('dutyTable').appendChild(table); 
}
function config(userId){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectDutyByUserIdName.act?userId="+userId;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs= rtJson.rtData;
  if(prcs.length>0){
    var prcsJson = prcs[0];
    if(prcsJson.seqId){
      //document.getElementById("dutyName").innerHTML =prcsJson.dutyName ;
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
      var registerStatus = "未登记";
      var holidayStr = "";
      if(evection(userId)){
        registerStatus = "<font color='#008000'>出差</font>";
        holidayStr = "1";
      }
      if(generalIs(general)){
        registerStatus = "<font color='#008000'>公休日</font>";
        holidayStr = "1";
      }
      if(holiday()){
        registerStatus = "<font color='#008000'>节假日</font>";
        holidayStr = "1";
      }
      //第一次登记是否存在

      if(dutyTime1.trim() != ""){ 
        var tr = new Element('tr',{"class":"TableData"});
        //$('tboday').appendChild(tr);
        var registerTime;
        var registerType;
        var seqId;
        var userId ;
        var registerIp;
        var remark;
        for(var i = 1; i< prcs.length; i++){
          var duty = prcs[i];
          registerTime = duty.registerTime;
          registerType = duty.registerType;
          seqId = duty.seqId;
          userId = duty.userId;
          registerIp = duty.registerIp;
          remark = duty.remark;
          if(registerType =="1"){break;}
        }
        if(registerType =="1"){
          var ZC = "";
          var update = "";
          var dutyTime1Int = strInt(dutyTime1);
          var  registerTimeInt = strInt(registerTime.substr(11,19));

          if(dutyType1=="1"){
            if(dutyTime1Int<registerTimeInt&&holidayStr != "1"){
              ZC = "<span class=big4>迟到</span><br>";
            }
          }else{
            if(dutyTime1Int>registerTimeInt&&holidayStr != "1"){
              ZC = "<span class=big4>早退</span>";
            }
          }
        
          if(remark!=""){
            update = "<br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
          }
         tr.update("<td nowrap align='center'>第1次登记</td>"
            + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType1)-1]+"</td>"
            + " <td nowrap align='center'>" + dutyTime1 +"</td> "
            + " <td nowrap align='center'>" + registerTime.substr(11,19) + ZC + update+"</td>"
            + " <td nowrap align='center'>"+registerIp+"</td>");
        }else{
          tr.update("<td nowrap align='center'>第1次登记</td>"
              + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType1)-1]+"</td>"
              + " <td nowrap align='center'>" + dutyTime1 +"</td> "
              + "  <td nowrap align='center'>" + registerStatus + "</td>"
              + " <td nowrap align='center'></td>");
        }
      }
    //第二次登记是否存在

      if(dutyTime2.trim() != ""){ 
        var tr = new Element('tr',{"class":"TableData"});
        //$('tboday').appendChild(tr);
        var registerTime;
        var registerType;
        var seqId;
        var userId ;
        var registerIp;
        var remark;
        for(var i = 1; i< prcs.length; i++){
          var duty = prcs[i];
          registerTime = duty.registerTime;
          registerType = duty.registerType;
          seqId = duty.seqId;
          userId = duty.userId;
          registerIp = duty.registerIp;
          remark = duty.remark;
          if(registerType =="2"){break;}
        }

        if(registerType =="2"){
          var ZC = "";
          var update = "";
          var dutyTime2Int = strInt(dutyTime2);
          var  registerTimeInt = strInt(registerTime.substr(11,19));
          if(dutyType2=="1"){
            if(dutyTime2Int<registerTimeInt&&holidayStr != "1"){
              ZC = "<span class=big4>迟到</span><br>";
            }
          }else{
            if(dutyTime2Int>registerTimeInt&&holidayStr != "1"){
              ZC = "<span class=big4>早退</span>";
            }
          }
          if(remark!=""){
            update = "<br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
          }
         tr.update("<td nowrap align='center'>第2次登记</td>"
            + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType2)-1]+"</td>"
            + " <td nowrap align='center'>" + dutyTime2 +"</td> "
            + " <td nowrap align='center'>" + registerTime.substr(11,19) + ZC + update+"</td>"
            + " <td nowrap align='center'>"+registerIp+"</td>");
        }else{
          tr.update("<td nowrap align='center'>第2次登记</td>"
              + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType2)-1]+"</td>"
              + " <td nowrap align='center'>" + dutyTime2 +"</td> "
              + "  <td nowrap align='center'>" + registerStatus + "</td>"
              + " <td nowrap align='center'></td>");
        }
      }
    //第三次登记是否存在

      if(dutyTime3.trim() != ""){ 
        var tr = new Element('tr',{"class":"TableData"});
        //$('tboday').appendChild(tr);
        var registerTime;
        var registerType;
        var seqId;
        var userId ;
        var registerIp;
        var remark;
        for(var i = 1; i< prcs.length; i++){
          var duty = prcs[i];
          registerTime = duty.registerTime;
          registerType = duty.registerType;
          seqId = duty.seqId;
          userId = duty.userId;
          registerIp = duty.registerIp;
          remark = duty.remark;
          if(registerType =="3"){break;}
        }
        if(registerType =="3"){
          var ZC = "";
          var update = "";
          var dutyTime3Int = strInt(dutyTime3);
          var  registerTimeInt = strInt(registerTime.substr(11,19));
          if(dutyType3=="1"){
            if(dutyTime3Int<registerTimeInt&&holidayStr != "1"){
              ZC = "<span class=big4> 迟到</span><br>";
            }
          }else{
            if(dutyTime3Int>registerTimeInt&&holidayStr != "1"){
              ZC = "<span class=big4> 早退</span>";
            }
          }
          if(remark!=""){
            update = "<br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
          }
         tr.update("<td nowrap align='center'>第3次登记</td>"
            + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType3)-1]+"</td>"
            + " <td nowrap align='center'>" + dutyTime3 +"</td> "
            + " <td nowrap align='center'>" + registerTime.substr(11,19) + ZC + update+"</td>"
            + " <td nowrap align='center'>"+registerIp+"</td>");
        }else{
          tr.update("<td nowrap align='center'>第3次登记</td>"
              + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType3)-1]+"</td>"
              + " <td nowrap align='center'>" + dutyTime3 +"</td> "
              + "  <td nowrap align='center'>" + registerStatus + "</td>"
              + " <td nowrap align='center'></td>");
        }
      }
    //第四次登记是否存在

      if(dutyTime4.trim() != ""){ 
        var tr = new Element('tr',{"class":"TableData"});
        //$('tboday').appendChild(tr);
        var registerTime;
        var registerType;
        var seqId;
        var userId ;
        var registerIp;
        var remark;
        for(var i = 1; i< prcs.length; i++){
          var duty = prcs[i];
          registerTime = duty.registerTime;
          registerType = duty.registerType;
          seqId = duty.seqId;
          userId = duty.userId;
          registerIp = duty.registerIp;
          remark = duty.remark;
          if(registerType =="4"){break;}
        }
        if(registerType =="4"){
          var ZC = "";
          var update = "";
          var dutyTime4Int = strInt(dutyTime4);
          var  registerTimeInt = strInt(registerTime.substr(11,19));
          if(dutyType4=="1"){
            if(dutyTime4Int<registerTimeInt&&holidayStr != "1"){
              ZC = "<span class=big4>迟到</span><br>";
            }
          }else{
            if(dutyTime4Int>registerTimeInt&&holidayStr != "1"){
              ZC = "<span class=big4>早退</span>";
            }
          }
          if(remark!=""){
            update = "<br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
          }
         tr.update("<td nowrap align='center'>第4次登记</td>"
            + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType4)-1]+"</td>"
            + " <td nowrap align='center'>" + dutyTime4 +"</td> "
            + " <td nowrap align='center'>" + registerTime.substr(11,19) + ZC + update+"</td>"
            + " <td nowrap align='center'>"+registerIp+"</td>");
        }else{
          tr.update("<td nowrap align='center'>第4次登记</td>"
              + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType4)-1]+"</td>"
              + " <td nowrap align='center'>" + dutyTime4 +"</td> "
              + "  <td nowrap align='center'>" + registerStatus + "</td>"
              + " <td nowrap align='center'></td>");
        }
      }
    //第五次登记是否存在

      if(dutyTime5.trim() != ""){ 
        var tr = new Element('tr',{"class":"TableData"});
        //$('tboday').appendChild(tr);
        var registerTime;
        var registerType;
        var seqId;
        var userId ;
        var registerIp;
        var remark;
        for(var i = 1; i< prcs.length; i++){
          var duty = prcs[i];
          registerTime = duty.registerTime;
          registerType = duty.registerType;
          seqId = duty.seqId;
          userId = duty.userId;
          registerIp = duty.registerIp;
          remark = duty.remark;
          if(registerType =="5"){break;}
        }
        if(registerType =="5"){
          var ZC = "";
          var update = "";
          var dutyTime5Int = strInt(dutyTime5);
          var  registerTimeInt = strInt(registerTime.substr(11,19));
          if(dutyType5=="1"){
            if(dutyTime5Int<registerTimeInt&&holidayStr != "1"){
              ZC = "<span class=big4>迟到</span><br>";
            }
          }else{
            if(dutyTime5Int>registerTimeInt&&holidayStr != "1"){
              ZC = "<span class=big4>早退</span>";
            }
          }
          if(remark!=""){
            update = "<br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
          }
         tr.update("<td nowrap align='center'>第5次登记</td>"
            + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType5)-1]+"</td>"
            + " <td nowrap align='center'>" + dutyTime5 +"</td> "
            + " <td nowrap align='center'>" + registerTime.substr(11,19) + ZC + update+"</td>"
            + " <td nowrap align='center'>"+registerIp+"</td>");
        }else{
          tr.update("<td nowrap align='center'>第5次登记</td>"
              + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType5)-1]+"</td>"
              + " <td nowrap align='center'>" + dutyTime5 +"</td> "
              + "  <td nowrap align='center'>" + registerStatus + "</td>"
              + " <td nowrap align='center'></td>");
        }
      }
    //第六次登记是否存在

      if(dutyTime6.trim() != ""){ 
        var tr = new Element('tr',{"class":"TableData"});
        //$('tboday').appendChild(tr);
        var registerTime;
        var registerType;
        var seqId;
        var userId ;
        var registerIp;
        var remark;
        for(var i = 1; i< prcs.length; i++){
          var duty = prcs[i];
          registerTime = duty.registerTime;
          registerType = duty.registerType;
          seqId = duty.seqId;
          userId = duty.userId;
          registerIp = duty.registerIp;
          remark = duty.remark;
          if(registerType =="6"){break;}
        }
        if(registerType =="6"){
          var ZC = "";
          var update = "";
          var dutyTime6Int = strInt(dutyTime6);
          var  registerTimeInt = strInt(registerTime.substr(11,19));
          if(dutyType6=="1"){
            if(dutyTime6Int<registerTimeInt&&holidayStr != "1"){
              ZC = "<span class=big4>迟到</span><br>";
            }
          }else{
            if(dutyTime6Int>registerTimeInt&&holidayStr != "1"){
              ZC = "<span class=big4>早退</span>";
            }
          }
          if(remark!=""){
            update = "<br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
          }
         tr.update("<td nowrap align='center'>第6次登记</td>"
            + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType6)-1]+"</td>"
            + " <td nowrap align='center'>" + dutyTime6 +"</td> "
            + " <td nowrap align='center'>" + registerTime.substr(11,19) + ZC + update+"</td>"
            + " <td nowrap align='center'>"+registerIp+"</td>");
        }else{
          tr.update("<td nowrap align='center'>第6次登记</td>"
              + " <td nowrap align='center'>"+dutyTypeNames[parseInt(dutyType6)-1]+"</td>"
              + " <td nowrap align='center'>" + dutyTime6 +"</td> "
              + "  <td nowrap align='center'>" + registerStatus + "</td>"
              + " <td nowrap align='center'></td>");
        }
      }
    }
    
   }
 
  //mergeQueryString($("form1"))
}
//判断是否是节假日
function holiday(){
  var timestr = new Date().toLocaleString();
  var requestURLHoliday = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/checkHoliday.act?date="+timestr;
  var rtJson = getJsonRs(requestURLHoliday);
  var holidayJson = rtJson.rtData;
  var holiday = holidayJson.status;//0为公假日
  if(holiday=="1"){
    return false;
  }
  return true;
}
//判断是否为公休日
function generalIs(general){
  var generals = '';
  if(general!=''){
    generals = general.split(',');
  }
  var d=new Date();   
  var week = d.getDay();
  if(week==0){
    week = 7;
  }
  for(var i = 0;i<generals.length;i++){
    if(week==generals[i]){
      return true;
    }
  }
  return false; 
}
//判断是否为出差时间
function evection(userId){
  var date = '<%=dateStr%>';
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/isEvection.act?date="+date;
  var rtJson = getJsonRs(requestURL);
  var data = rtJson.rtData;
  if(data.evection=="1"){//0为出差日
    return false;
  }
  return true;
}
//得到整数时间秒HH:mm:ss
function strInt(str){
  var min = 0;
  var max = 24*3600;
  var strInt;
  var strInt1;
  var strInt2;
  var strArray = str.split(":");
  for(var i = 0 ; i<strArray.length; i++){
    if(i==0){
      strInt1 = parseInt(strArray[i]*3600,10);
    }else if(i==1){
      strInt2 = parseInt(strArray[i]*60,10); 
    }  
  }
  strInt = strInt1+strInt2+parseInt(strArray[2]);
  return strInt;
}
function remark(seqId){
  var URL="<%=contextPath%>/core/funcs/attendance/personal/dutyRemark.jsp?seqId=" + seqId ;
  myleft=(screen.availWidth-650)/2;
  window.open(URL,"formul_edit","height=250,width=450,status=0,toolbar=no,menubar=no,location=no,scrollbars=no,top=150,left="+myleft+",resizable=yes");
}
function Init(){
  if(CheckForm()){
    var userId = '<%=userId%>';
    var beginTime = document.getElementById("beginTime").value;
    var endTime = document.getElementById("endTime").value;
    var requestURL = "<%=contextPath%>/core/funcs/attendance/manage/search.jsp?userId="+userId+"&beginTime="+beginTime+"&endTime="+endTime;
    window.location.href = requestURL;
  }
}
function Init2(){
  if(CheckForm2()){
  var userId = '<%=userId%>';
  var date = document.getElementById("time").value;
  var requestURL = "<%=contextPath%>/core/funcs/attendance/manage/selectdutyByuserId.jsp?userId="+userId+"&date="+date;
  window.location.href = requestURL;
  }
}  

function checkScore(valueStr){
  if(valueStr == 0){
    location = "<%=contextPath%>/custom/attendance/personal/overtimetotal/index.jsp";
    //openDialogResize(URL,'820', '500');
  }
  if(valueStr == 1){
    var URL  = "<%=contextPath%>/custom/attendance/personal/duty/detailDuty.jsp";
    openDialogResize(URL,'820', '500');
  }
  if(valueStr == 2){
    var URL = "<%=contextPath%>/core/funcs/attendance/personal/detailOut.jsp";
    openDialogResize(URL,'820', '500');
  }
  if(valueStr == 3){
    var URL = "<%=contextPath%>/core/funcs/attendance/personal/detailLeave.jsp";
    openDialogResize(URL,'820', '500');
  }
  if(valueStr == 4){
    var URL = "<%=contextPath%>/core/funcs/attendance/personal/detailEvection.jsp";
    openDialogResize(URL,'820', '500');
  }
  if(valueStr == 5){
    var URL = "<%=contextPath%>/custom/attendance/personal/annualleave/detailLeave.jsp";
    openDialogResize(URL,'820', '500');
  }
}

</script>
</head>

<body class="" topmargin="5" onload="doOnload();">
<div id="attendanDiv">
<!--
<table class="MessageBox" align="center" width="500">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt"><span id="userName"></span>本年度已请假<font color="red" id="leaveDaysTotal">0</font>天</div>
    </td>
  </tr>
</table>
-->
<!----  上下班登记
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 今日上下班登记 (<span id="dutyName"></span>)</span><br>
    </td>
  </tr>
</table>
<div id="dutyTable"></div>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>
 ---->
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" align="absmiddle"><span class="big3">&nbsp;人事薪资统计</span><br>
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
<div id="listDiv" align="center"></div>

<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 考勤查询与统计</span><br>
    </td>
  </tr>
</table>

<form action="#" id="form1" name="form1" onsubmit="return CheckForm1();">
<table align="center" class="TableList" width=450>
  <tr class=TableHeader >
    <td colspan=2>考勤统计
    </td>
  </tr>
  <tr>
     <td class=TableData>
    起始日期：<input type="text" id="beginTime" name="beginTime" class="BigInput" size="10" maxlength="10" value="<%=dateStr2 %>">
          <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
      &nbsp; 
  截止日期：<input type="text" id="endTime" name="endTime" class="BigInput" size="10" maxlength="10" value="<%=dateStr1 %>">
          <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
     </td>
     <td class=TableData width=40>
       <input type="button" value="统计" class="BigButton" onclick="Init();">
     </td>
   </tr>
</table>
</form>
<br>

<form action="#" name="form2" onsubmit="return CheckForm2();">
<table align="center" class="TableList" width=450>
<tr class=TableHeader >
<td colspan=2>
上下班登记查询
</td>
</tr>
<tr>
<td class=TableData>

查询日期：<input type="text" id="time" name="time" class="BigInput" size="10" maxlength="10" value="<%=dateStr1 %>">
          <img id="date3" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
</td>
<td class=TableData width=40>
<input type="hidden" id="userId" name="userId" value="admin">
<input type="button" value="查询" class="BigButton" title="上下班登记查询" onclick="Init2();">
</td>
</tr>

</table>
</form>
</div>
<div id="nullList" style="display:none">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/hrms.gif" HEIGHT="20"><span class="big3"> 人员考勤记录</span>
    </td>
  </tr>
</table>
<br>

<table class="MessageBox" align="center" width="240">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">请选择人员</div>
    </td>
  </tr>
</table>
</div>
</body>
</html>

