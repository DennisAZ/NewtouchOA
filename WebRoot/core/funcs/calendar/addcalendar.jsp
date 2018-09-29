<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.act.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  Date date = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  String dateStr = dateFormat.format(date);
  String ldwmNewStatus =request.getParameter("ldwmNewStatus");
  String newStatus = request.getParameter("newStatus");
  String year = request.getParameter("year");
  String week = request.getParameter("week");
  YHCalendarAct tca = new YHCalendarAct();
  String beginDate = "";
  String endDate = "";
  if(week!=null){
    Calendar[] darr = tca.getStartEnd(Integer.parseInt(year),Integer.parseInt(week));
    beginDate = tca.getFullTimeStr(darr[0]);
    endDate = tca.getFullTimeStr(darr[1]);
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建事务</title>
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
  var calTime = document.getElementById("calTime");
  var endTime = document.getElementById("endTime");
  if(calTime.value==''){
    alert("开始时间不能为空！");
    calTime.focus();
    calTime.select();
    return false;
  }
  if(endTime.value==''){
    alert("结束时间不能为空！");
    endTime.focus();
    endTime.select();
    return false;
  }
  var calTimeArray  =calTime.value.trim().split(" ");
  var endTimeArray  = endTime.value.trim().split(" ");
  var type1 =   "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9](\:[0-5]?[0-9])?$"　; 
  var type2 = "^\:[0-5]?[0-9](\:[0-5]?[0-9])?$"　;
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2);
  if(calTime.value!=""&&calTimeArray.length!=2){
    alert("开始时间应为日期时间型，如：1999-01-01 10:20");
    calTime.focus();
    calTime.select();
    return false;
  }else{
    if(calTime.value!=""&&(!isValidDateStr(calTimeArray[0])||calTimeArray[1].match(re1) == null || calTimeArray[1].match(re2) != null)){
      alert("开始时间应为日期时间型，如：1999-01-01 10:20");
      calTime.focus();
      calTime.select();
      return false;
    }
  }
  if(endTime.value!=""&&endTimeArray.length!=2){
    alert("结束时间应为日期时间型，如：1999-01-01 10:20");
    endTime.focus();
    endTime.select();
    return false;
  }else{
    if(endTime.value!=""&&(!isValidDateStr(endTimeArray[0])||endTimeArray[1].match(re1) == null || endTimeArray[1].match(re2) != null)){
      alert("结束时间应为日期时间型，如：1999-01-01 10:20");
      endTime.focus();
      endTime.select();
      return false;
    }
  }
  if(calTime.value!=""&&endTime.value!=""&&calTime.value >= endTime.value){ 
    alert("结束时间要晚于开始时间！");
    endTime.focus();
    endTime.select();
    return (false);
  }
  var IsInt = "^[0-9]*[1-9][0-9]*$";//正整数　
  var IsInt1 = "^[0-9][0-9]*$";//0和正整数　
  var IsInt2 =   "^-?\\d+$";//整数"^\\d+$"　
  var IsInt3 = "^[0-9]*\\.?[0-9]*$";//0以上的数
  var re = new RegExp(IsInt1);
  if($("beforeDay").value!=''&&$("beforeDay").value.match(re)==null){
     alert("提前的天数应为整数！");
     $("beforeDay").focus();
     $("beforeDay").select();
     return false;
  }
  if($("beforeHour").value!=''&&$("beforeHour").value.match(re)==null){
    alert("提前的小时应为整数！");
    $("beforeHour").focus();
    $("beforeHour").select();
    return false;
  }
  if($("beforeMin").value!=''&&$("beforeMin").value.match(re)==null){
    alert("提前的分钟应为整数！");
    $("beforeMin").focus();
    $("beforeMin").select();
    return false;
  }
  return (true);
}
function doOnload(){
  var ldwmNewStatus = "<%=ldwmNewStatus%>";
  var temp = '<%=request.getParameter("temp")%>';
  var newStatus = "<%=newStatus%>";
  var caltimestr;
  var endtimestr;
  var index = '<%=request.getParameter("index")%>';
  var year = '<%=request.getParameter("year")%>';
  var month = '<%=request.getParameter("month")%>';
  var day = '<%=request.getParameter("day")%>';
  if(ldwmNewStatus=='day'){
    caltimestr = year+"-"+month+"-"+day+" "+index+":00";
    endtimestr = year+"-"+month+"-"+day+" "+index+":59";
    if(newStatus=='0'||'<%=request.getParameter("db")%>'=='db'){
      caltimestr = year+"-"+month+"-"+day+" 00:00";
      endtimestr = year+"-"+month+"-"+day+" 23:59";
    }
  }else if(ldwmNewStatus=='week'){
     if(newStatus=='0'){
     var beginDate = '<%=beginDate%>';
     var endDate = '<%=endDate%>';
     caltimestr = beginDate+" 00:00";
     endtimestr = endDate+" 23:59";
    }else{
      caltimestr = '<%=request.getParameter("date")%>'+" " +'<%=request.getParameter("index")%>' +":00";
      endtimestr = '<%=request.getParameter("date")%>'+" " +'<%=request.getParameter("index")%>' +":59";
    }
  }else if(ldwmNewStatus=='month'){
    if(month.length==1){
      month = '0'+month;
    }
    if(<%=request.getParameter("maxDay")%>){
      caltimestr = year+"-" + month+"-01 00:00";
      endtimestr = year+"-"+ month+"-"+'<%=request.getParameter("maxDay")%>'+" 23:59";
    }else{
      if(day.length==1){
        day = "0"+day;
      }
      caltimestr = year+"-" + month+"-"+day+" 00:00";
      endtimestr = year+"-" + month+"-"+day+" 23:59";
    }
  }else if(temp=='week'){
    caltimestr = '<%=request.getParameter("date")%>'+" 00:00";
    endtimestr = '<%=request.getParameter("date")%>'+" 23:59";
  }else{
    var datestr = '<%=dateStr%>';
    var caltimestr = datestr.substr(0,11)+"00:00";
    var endtimestr =  datestr.substr(0,11)+"23:59";      
  }
  document.getElementById("calTime").value=caltimestr;
  document.getElementById("endTime").value=endtimestr;

  var date1Parameters = {
      inputId:'calTime',
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
  //短信
  getSysRemind();
//手机
 getMoblieSmsRemind("moblieSmsRemindDiv","moblieSmsRemind");
}
var menuData1 = [{ name:'<a id="CalLevel_" href="#" class="CalLevel" style="padding-top:5px;margin-left:5px">未指定</a>',action:set_status,extData:'0'}
,{ name:'<a id="CalLevel_1" href="#" class="CalLevel1" style="padding-top:5px;margin-left:5px">重要/紧急</a>',action:set_status,extData:'1'}
,{ name:'<a id="CalLevel_2" href="#" class="CalLevel2" style="padding-top:5px;margin-left:5px">重要/不紧急</a>',action:set_status,extData:'2'}
,{ name:'<a id="CalLevel_3" href="#" class="CalLevel3" style="padding-top:5px;margin-left:5px">不重要/紧急</a>',action:set_status,extData:'3'}
,{ name:'<a id="CalLevel_4" href="#" class="CalLevel4" style="padding-top:5px;margin-left:5px">不重要/不紧急</a>',action:set_status,extData:'4'}
]
function set_status(){
  var status = arguments[2];
  var statusName;
  if(status=='0'){
    statusName = "未指定";
  }
  if(status=='1'){
    statusName = "重要/紧急";
  }
  if(status=='2'){
    statusName = "重要/不紧急";
  }
  if(status=='3'){
    statusName = "不重要/紧急";
  }
  if(status=='4'){
    statusName = "不重要/不紧急";
  }
  document.getElementById("calLevelName").innerHTML=statusName;
  document.getElementById("calLevel").value=status;
  document.getElementById("calLevelName").className = "CalLevel"+status;
}
function showMenu(event){
var menu = new Menu({bindTo:$('calLevelName') , menuData:menuData1 , attachCtrl:true});
menu.show(event);
}
function Init(){
  if(CheckForm()){
    var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/addCalendar.act";
    var rtJson = getJsonRs(requestUrl,mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.opener.location.reload();
    if(rtJson.rtState=='0'){
      document.getElementById("form1").style.display="none";
      document.getElementById("cuccessDiv").style.display="block";
    }
  }
}
function newCalendar(){
  document.location.reload();
  //document.getElementById("form1").style.display="block";
  //document.getElementById("cuccessDiv").style.display="none";
}
//判断是否要显示短信提醒
function getSysRemind(){
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=5";
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  var allowRemind = prc.allowRemind;;//是否允许显示
  var defaultRemind = prc.defaultRemind;;//是否默认选中
  var mobileRemind = prc.mobileRemind;//手机默认选中
  if(allowRemind=='2'){
    $("smsRemindDiv").style.display = 'none';
  }else{
    if(defaultRemind=='1'){
      $("smsRemind").checked = true;
    }
  }
  //return prc;
}
/** 
*js代码 
*是否显示手机短信提醒 
*/ 
function getMoblieSmsRemind(remidDiv,remind){ 
var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=5"; 
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
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/calendar.gif" align="absMiddle" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;新建事务</span>
    </td>
  </tr>
</table>
<form action="<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/addCalendar.act"  method="post" id="form1" name="form1" onsubmit="return CheckForm();">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.calendar.data.YHCalendar"/>
 <table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData"> 优先级：</td>
      <td class="TableData">
        <a id="calLevelName" href="javascript:;" class="CalLevel" onclick="showMenu(event);" hidefocus="true">未指定</a>&nbsp;
        <input type="hidden" id="calLevel" name="calLevel" value="0">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 起始时间：</td>
      <td class="TableData">
        <input type="text" class="BigInput" id="calTime" name="calTime" value="" size="19" maxlength="19">
        <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer"  align="absMiddle">
     </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 结束时间：</td>
      <td class="TableData">
        <input type="text" class="BigInput" id="endTime" name="endTime" value="" size="19" maxlength="19">
        <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer"  align="absMiddle">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 事务类型：</td>
      <td class="TableData">
        <select id="calType" name="calType">
          <option value="1">工作事务</option>
          <option value="2">个人事务</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 事务内容：</td>
      <td class="TableData">
        <textarea id="content" name="content" cols="45" rows="5" class="BigInput"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 提醒时间：</td>
      <td class="TableData">提前
        <input type="text"id="beforeDay" name="beforeDay" size="2" class="BigInput" value=""> 天
        <input type="text" id="beforeHour" name="beforeHour" size="2" class="BigInput" value=""> 小时
        <input type="text" id="beforeMin" name="beforeMin" size="2" class="BigInput" value="10"> 分钟提醒
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 提醒：</td>
      <td class="TableData">
     <span id="smsRemindDiv"><input type="checkbox" id="smsRemind" name="smsRemind" ><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>    <span id="moblieSmsRemindDiv"><input type="checkbox" id="moblieSmsRemind" name="moblieSmsRemind" ><label for="smsRemind">使用手机短信提醒</label>&nbsp;&nbsp;</span>    </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" id="ldwmNewStatus" name="ldwmNewStatus" value="day"></input>
        <input type="button" value="确定" class="BigButton" onclick="Init()">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onclick="window.close();">
      </td>
    </tr>
  </table>
</form>
<div id="cuccessDiv" style="display:none">
<table class="MessageBox" align="center" width="220">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">保存成功</div>
    </td>
  </tr>
</table>
<br><center>
<input type="button" class="BigButton" value="继续新建" onclick="newCalendar();">&nbsp;&nbsp;
<input type="button" class="BigButton" value="关闭" onclick="window.close();">
</center>
</div>
</body>
</html>