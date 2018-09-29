<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  Date curDate = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormatWeek = new SimpleDateFormat("E");
  Calendar c = Calendar.getInstance();
  String curDateStr = dateFormat.format(curDate);
  int month = Integer.parseInt(curDateStr.substring(5,7));
  int day = Integer.parseInt(curDateStr.substring(8,10));
  String time = curDateStr.substring(11,19);
  c.setTime(curDate);
  int week = c.get(Calendar.DAY_OF_WEEK);
  if(week==1){
    week = 7;  
  }else{
    week = week - 1;
  }
  String weeks[] = {"星期一","星期二","星期三","星期四","星期五","星期六","星期天"};
  //out.print(month+":"+day+":"+week);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑周期性事务</title>
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
function CheckForm(){
  if(document.getElementById("content").value==""){ 
    alert("事务内容不能为空！");
    return (false);
  }

  var beginTime = document.getElementById("beginTime");
  var endTime = document.getElementById("endTime");
  var beginTimeArray  = beginTime.value.trim().split(" ");
  var endTimeArray  = endTime.value.trim().split(" ");
  var type1 =   "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　;
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2);
  if(beginTime.value!=""&&beginTimeArray.length!=2){
    alert("开始时间应为日期时间型，如：1999-01-01 10:20:10");
    beginTime.focus();
    beginTime.select();
    return false;
  }else{
    if(beginTime.value!=""&&(!isValidDateStr(beginTimeArray[0])||beginTimeArray[1].match(re1) == null || beginTimeArray[1].match(re2) != null)){
      alert("开始时间应为日期时间型，如：1999-01-01 10:20:10");
      beginTime.focus();
      beginTime.select();
      return false;
    }
  }
  if(endTime.value!=""&&endTimeArray.length!=2){
    alert("结束时间应为日期时间型，如：1999-01-01 10:20:10");
    endTime.focus();
    endTime.select();
    return false;
  }else{
    if(endTime.value!=""&&(!isValidDateStr(endTimeArray[0])||endTimeArray[1].match(re1) == null || endTimeArray[1].match(re2) != null)){
      alert("结束时间应为日期时间型，如：1999-01-01 10:20:10");
      endTime.focus();
      endTime.select();
      return false;
    }
  }
  if(beginTime.value!=""&&endTime.value!=""&&beginTime.value >= endTime.value){ 
    alert("结束时间要晚于开始时间！");
    return (false);
  }
  var type = document.getElementById("type").value;
  var remindTime = document.getElementById("remindTime");
  var remindTime3 = document.getElementById("remindTime3");
  var remindTime4 = document.getElementById("remindTime4");
  var remindTime5 = document.getElementById("remindTime5");
  if(type=='2'&&remindTime.value!=''&&(remindTime.value.match(re1) == null || remindTime.value.match(re2) != null)){
    alert("提醒时间应为时间型，如：10:20:10");
    remindTime.focus();
    remindTime.select();
    return false;
  }
  if(type==3&&remindTime3.value!=''&&(remindTime3.value.match(re1) == null || remindTime3.value.match(re2) != null)){
    alert("提醒时间应为时间型，如：10:20:10");
    remindTime3.focus();
    remindTime3.select();
    return false;
  }
  if(type==4&&remindTime4.value!=''&&(remindTime4.value.match(re1) == null || remindTime4.value.match(re2) != null)){
    alert("提醒时间应为时间型，如：10:20:10");
    remindTime4.focus();
    remindTime4.select();
    return false;
  }
  if(type==5&&remindTime5.value!=''&&(remindTime5.value.match(re1) == null || remindTime5.value.match(re2) != null)){
    alert("提醒时间应为时间型，如：10:20:10");
    remindTime5.focus();
    remindTime5.select();
    return false;
  }
  return (true);
}
function sel_change(){
   var aff_type="day";
   if(form1.type.value=="2"){
     document.getElementById("week").style.display="none";
     document.getElementById("mon").style.display="none";
     document.getElementById("years").style.display="none";
     aff_type="day";
     $("dayShow").style.display = '';
   }  
   if(form1.type.value=="3"){
     $("dayShow").style.display = 'none';
     document.getElementById("day").style.display="none";
     document.getElementById("mon").style.display="none";
     document.getElementById("years").style.display="none";
     aff_type="week";
   }
   if(form1.type.value=="4"){
     $("dayShow").style.display = 'none';
     document.getElementById("day").style.display="none";
     document.getElementById("week").style.display="none";
     document.getElementById("years").style.display="none";
     aff_type="mon";
   }
   if(form1.type.value=="5"){
     $("dayShow").style.display = 'none';
     document.getElementById("day").style.display="none";
     document.getElementById("week").style.display="none";
     document.getElementById("mon").style.display="none";
     aff_type="years";
   }
   document.getElementById(aff_type).style.display="";
}
function doOnload(){
  var month = '<%=month%>';
  var day = '<%=day%>';
  var week = '<%=week%>';
  var time = '<%=time%>';
  var seqId = '<%=request.getParameter("seqId")%>';
  var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/selectAffairById.act?seqId="+seqId;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  var userId = prc.userId;
  var beginTime = prc.beginTime;
  var endTime = prc.endTime;
  var type = prc.type;
  var remindDate = prc.remindDate;
  var remindTime = prc.remindTime;
  var content = prc.content;
  var isWeekend = prc.isWeekend;
  document.getElementById("seqId").value=seqId;
  document.getElementById("beginTime").value=beginTime;
  document.getElementById("endTime").value= endTime
  document.getElementById("content").value=content;
  document.getElementById("type").value=type;
  if(type=='2'){
    document.getElementById("remindTime").value=remindTime;
    $("dayShow").style.display = '';
    if(isWeekend=='1'){
      $("isWeekend").checked = true;
      }
  }
  if(type=='3'){
    document.getElementById("day").style.display = "none";
    document.getElementById("week").style.display = "";

    document.getElementById("remindDate3").value=remindDate;
    document.getElementById("remindTime3").value=remindTime;
    $("dayShow").style.display = 'none';
  }
  if(type=='4'){
    document.getElementById("day").style.display = "none";
    document.getElementById("mon").style.display = "";
    document.getElementById("remindDate4").value=remindDate;
    document.getElementById("remindTime4").value=remindTime;
    $("dayShow").style.display = 'none';
  }
  if(type=='5'){
    $("dayShow").style.display = 'none';
    document.getElementById("day").style.display = "none";
    document.getElementById("years").style.display = "";
    var dates = remindDate.split('-');
    month = dates[0];
    day = dates[1]
    document.getElementById("remindDate5Mon").value=month;
    document.getElementById("remindDate5Day").value=day;
    document.getElementById("remindTime5").value=remindTime;
  }
  
  var date1Parameters = {
      inputId:'beginTime',
      property:{isHaveTime:true}
      ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'endTime',
      property:{isHaveTime:true}
      ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
//手机
  getMoblieSmsRemind("moblieSmsRemindDiv","moblieSmsRemind");
  
}
/** 
*js代码 
*是否显示手机短信提醒 
*/ 
function getMoblieSmsRemind(remidDiv,remind){ 
var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=45"; 
var rtJson = getJsonRs(requestUrl); 
if(rtJson.rtState == "1"){ 
alert(rtJson.rtMsrg); 
return ; 
} 
var prc = rtJson.rtData; 
var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
if(moblieRemindFlag == '2'){ 
$(remidDiv).style.display = ''; 
$(remind).checked = true; 
}else if(moblieRemindFlag == '1'){ 
$(remidDiv).style.display = ''; 
$(remind).checked = false; 
}else{ 
$(remidDiv).style.display = 'none'; 
} 
}
function Init(){
  if(CheckForm()){
    var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/updateAffair.act";
    var rtJson = getJsonRs(URL,mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.location.href = "<%=contextPath%>/core/funcs/calendar/Cycaffair.jsp";
  }
}
function Test(){
  var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/updateAffairRemindTime.act";
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
}
function returnPre(){
  window.location.href = "<%=contextPath%>/core/funcs/calendar/Cycaffair.jsp";
}
</script>
</head>

<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" align="absMiddle" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;编辑周期性事务</span>
    </td>
  </tr>
</table>

<br>
<form action="<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/updateAffair.act"  method="post" id="form1" name="form1" onsubmit="return CheckForm();">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.calendar.data.YHAffair"/>
 <table class="TableBlock" width="500" align="center">
    <tr>
      <td nowrap class="TableData" width=100>开始时间：</td>
      <td class="TableData">
        <INPUT type="text" id="beginTime" name="beginTime" class=BigInput size="20" value="<%=curDateStr %>">
        <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
       &nbsp;&nbsp;为空为当前时间
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">结束时间：</td>
      <td class="TableData">
        <INPUT type="text" id="endTime" name="endTime" class=BigInput size="20" value="">
        <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
    &nbsp;&nbsp;为空则不结束
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 提醒类型：</td>
      <td class="TableData">
        <select id="type" name="type" class="BigSelect" onchange="sel_change()">
          <option value="2">按日提醒</option>
          <option value="3">按周提醒</option>
          <option value="4">按月提醒</option>
          <option value="5">按年提醒</option>
        </select>
         <span id="dayShow" style="display:none"> <input type="checkbox" name="isWeekend" id="isWeekend" ></input>&nbsp;选中为排除周六、日</span>
      </td>
    </tr>
    <tr id="day">
      <td nowrap class="TableData"> 提醒时间：</td>
      <td class="TableData">
        <input id="remindTime" name="remindTime" size="10" class="BigInput" value="<%=time %>">
        <img src="<%=imgPath%>/clock.gif" align="absMiddle" border="0" style="cursor:pointer" onclick="showTime('remindTime')">
        &nbsp;&nbsp;为空为当前时间
      </td>
    </tr>
    <tr id="week" style="display:none">
      <td nowrap class="TableData"> 提醒时间：</td>
      <td class="TableData">
        <select id="remindDate3" name="remindDate3" class="BigSelect">
        <%for(int i=1;i<=7;i++){ 
            if(i==week){
         %>
         <option value="<%=i %>" selected="selected" ><%=weeks[i-1] %></option>
         <%     
            }else{
        %>
          <option value="<%=i %>" ><%=weeks[i-1] %></option>
        <%}
         }
        %>
        </select>&nbsp;&nbsp;
        <input id="remindTime3" name="remindTime3" size="10" class="BigInput" value="<%=time %>">
        <img src="<%=imgPath%>/clock.gif" align="absMiddle" border="0" style="cursor:pointer" onclick="showTime('remindTime3')" >
        &nbsp;&nbsp;为空为当前时间
      </td>
    </tr>
    <tr id="mon" style="display:none">
      <td nowrap class="TableData"> 提醒时间：</td>
      <td class="TableData">
        <select id="remindDate4" name="remindDate4" class="BigSelect">
        <%for(int i=1;i<=31;i++){ 
            if(i==day){
         %>
         <option value="<%=i %>" selected="selected"><%=i %>日</option>
        <%
            }else{
        %>
          <option value="<%=i %>"><%=i %>日</option>
        <%} }%>
        </select>&nbsp;&nbsp;
        <input id="remindTime4" name="remindTime4" size="10" class="BigInput" value="<%=time %>">
        <img src="<%=imgPath%>/clock.gif" align="absMiddle" border="0" style="cursor:pointer" onclick="showTime('remindTime4')">
        &nbsp;&nbsp;为空为当前时间
      </td>
    </tr>
    <tr id="years" style="display:none">
      <td nowrap class="TableData"> 提醒时间：</td>
      <td class="TableData">
        <select id="remindDate5Mon" name="remindDate5Mon" class="BigSelect">
         <%for(int i=1;i<=12;i++){ 
            if(i==month){
         %>
            <option value="<%=i %>" selected="selected"><%=i %>月</option>  
        <%
            }else{
        %>
          <option value="<%=i %>" ><%=i %>月</option>
        <%}} %>
        </select>&nbsp;&nbsp;
        <select id="remindDate5Day" name="remindDate5Day" class="BigSelect">
        <%for(int i=1;i<=31;i++){ 
            if(i==day){
         %>
           <option value="<%=i %>" selected="selected"><%=i %>日</option>
         <%
            }else{
        %>
          <option value="<%=i %>"><%=i %>日</option>
        <%}} %>
        </select>&nbsp;&nbsp;
        <input id="remindTime5" name="remindTime5" size="10" class="BigInput" value="<%=time %>">
        <img src="<%=imgPath%>/clock.gif" align="absMiddle" border="0" style="cursor:pointer" onclick="showTime('remindTime5')">
        &nbsp;&nbsp;为空为当前时间
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 事务内容：</td>
      <td class="TableData">
        <textarea id="content" name="content" cols="45" rows="5" class="BigInput"></textarea>
      </td>
    </tr>
     <tr class="TableData" id="moblieSmsRemindDiv">  
    <td>手机短信提醒</td>
     <td nowrap align="left">
    <input type="checkbox" id="moblieSmsRemind" name="moblieSmsRemind" ><label for="smsRemind">使用手机短信提醒</label>&nbsp;&nbsp;
    </td></tr>
        <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" id="seqId" name="seqId" value=""></input>
        <input type="button" value="确定" class="BigButton" onclick="Init();">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="returnPre();">
      </td>
    </tr>
  </table>
</form>

</body>
</html>