<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String userIds = request.getParameter("userIds");
  String beginTime = request.getParameter("beginTime");
  String endTime = request.getParameter("endTime");
  String dutyType = request.getParameter("dutyType");
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月dd日");
  String beginTimeStr = dateFormat2.format(dateFormat1.parse(beginTime));
  String endTimeStr = dateFormat2.format(dateFormat1.parse(endTime));
  //相隔多少天
  long daySpace = YHUtility.getDaySpan(dateFormat1.parse(beginTime),dateFormat1.parse(endTime))+1;
  //得到到之间的天数数组
  List daysList = new ArrayList();
  String days = "";
  Calendar calendar = new GregorianCalendar();
  for(int i = 0;i<daySpace;i++){
    calendar.setTime(dateFormat1.parse(beginTime));
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
<html>
<head>
<title>上下班登记修改</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" >
function doOnload(){
  var userIds = '<%=userIds%>';
  var beginTime = '<%=beginTime%>';
  var endTime = '<%=endTime%>';
  var days = '<%=days%>';  
  var dutyType = '<%=dutyType%>';
  days = days.split(",");
  userIds = userIds.split(",");
  //所有的排版类型；
  allConfig(userIds,dutyType);
  //根据排版类型得到是此排版类型的所有用户
  var dutyType = document.getElementById("dutyType").value;
  var userInfo = getUser(userIds,dutyType);
 
    //得到排版类型
  config(userInfo,beginTime,endTime,days,dutyType);
}
//根据排版类型得到是此排版类型的所有用户的信息
function getUser(userIds,dutyType){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectUserBydutyType.act?userId="+userIds+"&dutyType="+dutyType;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  return prcs;
}
//得到排班类型
function config(userInfo,beginTime,endTime,days,dutyType){
  requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendConfigAct/selectConfigById.act?seqId=" + dutyType; 
  var json = getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }

  prcs = json.rtData;
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
    var general = prcs.general;
    var dutyTypeNames = ["上班登记","下班登记"];
    //建表
    var table = new Element('table',{"class":"TableList" , "width":"100%" ,"align":"center"});
    $("dutyTable").appendChild(table);
    var trTemp = "<tbody id='tbody'><tr id='tr_title'  class='TableHeader' align='center'><td nowrap align='center'>日期</td>"
    +"<td nowrap align='center'>姓名</td>"
    +"<td nowrap align='center'>部门</td>";
    for(var i = 1;i<=6;i++){
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
      trTemp = trTemp + "<td align='center' nowrap>" +dutyTypeNames[parseInt(dutyType)-1]+"("+dutyTime+")</td>";
    }
    trTemp = trTemp + "</tr></tbody>";
    //对用户进行循环
    for(var k = 0;k<userInfo.length;k++){
      var user = userInfo[k];
      var userSeqId = user.seqId;
      var deptName = user.deptName;
      var userName = user.userName;
      var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/getAllUserDutyInfo.act?userId="+userSeqId+"&days="+days;
      var rtJson = getJsonRs(URL);
      if(rtJson.rtState == "1"){
        alert(rtJson.rtMsrg); 
        return ;
      }
      //trTemp = trTemp + "<td align='center'>"+deptName + "</td>"
     
      var prcs= rtJson.rtData;
      //alert(prcs.trTemp);
      trTemp = trTemp + prcs.trTemp;
    }  

    table.update(trTemp);
  }
  
  
}
function getDayOfWeek(dateStr){
  var day = new Date(Date.parse(dateStr.replace(/-/g, '/'))); //将日期值格式化
  //day.getDay();根据Date返一个星期中的某其中0为星期日
  return day.getDay(); 
}
//得到上下班情况
function attendDuty(userId,date,registerType){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectDutyByDay.act?userId="+userId+"&date="+date+"&registerType="+registerType;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs= rtJson.rtData;
  return prcs;
}
//判断是否是节假日
function holiday(userId,date){
  var requestURLHoliday = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/checkHoliday.act?date="+date;
  var rtJson = getJsonRs(requestURLHoliday);
  var holidayJson = rtJson.rtData;
  var holiday = holidayJson.status;//0为公假日
  if(holiday=="1"){
    return false;
  }
  return true;
}
//判断是否为公休日
function generalIs(general,date){
  var generals = '';
  if(general!=''){
    generals = general.split(',');
  }
  var date = date.substr(5,2)+"/"+date.substr(8,2)+"/"+date.substr(0,4);
  var d=new Date(date);   
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
function evection(userId,date){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/isEvection.act?date="+date+"&userId="+userId;
  var rtJson = getJsonRs(requestURL);
  var data = rtJson.rtData;
  if(data.evection=="1"){//0为出差日
    return false;
  }
  return true;
}
//判断是否为请假时间段
function leave(userId,date){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/isLeave.act?date="+date+"&userId="+userId;
  var rtJson = getJsonRs(requestURL);
  var data = rtJson.rtData;
  if(data.evection=="1"){//0为请假
    return false;
  }
  return true;
}
//判断是否在外出时间段
function out(userId,date){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/isOut.act?date="+date+"&userId="+userId;
  var rtJson = getJsonRs(requestURL);
  var data = rtJson.rtData;
  if(data.out=="1"){//0为请假
    return false;
  }
  return true;
}
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
//排版类型
function allConfig(userId,dutyType){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectAllConfig.act?userId="+userId+"&dutyType="+dutyType;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }

  var prcs = rtJson.rtData;
  var selects = document.getElementById("dutyType");
  for(var i = 0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.seqId; 
    option.innerHTML = prc.dutyName; 
    selects.appendChild(option);
    if(dutyType==prc.seqId){
      option.selected = true;
    }
  }
}
function onChange(){
  var dutyType = document.getElementById("dutyType").value;
  var beginTime = '<%=beginTime%>';
  var endTime = '<%=endTime%>';
  var userIds = '<%=userIds%>';
  window.location.href = "<%=contextPath%>/core/funcs/attendance/manage/alluserdutylist.jsp?userIds="+userIds+"&beginTime="+beginTime+"&endTime="+endTime+"&dutyType="+dutyType;
}
function returnBefore(){
  var userId = "";
  var beginTime = '<%=beginTime%>';
  var endTime = '<%=endTime%>';
  window.location.href = "<%=contextPath%>/core/funcs/attendance/manage/statisticsearch.jsp?userId="+userId+"&beginTime="+beginTime+"&endTime="+endTime+"&deptId="+deptId+"&dutyType="+dutyType;
}
</script>
</head>

<body class="" topmargin="5" onload="doOnload();">

<table border="0" width="900" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" HEIGHT="20" align="absmiddle">
    <span class="big3"> 上下班查询结果 - [ <%=beginTimeStr %> 至 <%=endTimeStr %>共 <%=daySpace %> 天]</span><br>
    </td>
    <td class="Big" HEIGHT="20">
   <span class="big3">排班类型</span>
    </td>
    <td class="Big" HEIGHT="20">
       <select id="dutyType" name="dutyType" class="BigSelect" onchange="onChange();">
       </select>
    </td>
  </tr>
</table>
<br>
<div id="dutyTable"></div>
<center>
<input type="button" class="BigButton" value="返回" onclick="history.go(-1);"></center>
</body>
</html>