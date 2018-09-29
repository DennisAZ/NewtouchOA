<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.act.*"  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
//日安排
  String userId = request.getParameter("userId");
  String year = request.getParameter("year");
  String month = request.getParameter("month");
  String day = request.getParameter("day");
  String userIds = request.getParameter("userIds");
  String dwm = request.getParameter("dwm");  
  //周安排查询  year userId deptId dwm
  String date1 = request.getParameter("date1");
  String date7 = request.getParameter("date7");
  String weekth = request.getParameter("week");
  String date = request.getParameter("date");
  
  //月查询得到周的转换
  YHCalendarAct tca = new YHCalendarAct();
  String beginDate = "";
  String endDate = "";
  if(!weekth.equals("null")){
    Calendar[] darr = tca.getStartEnd(Integer.parseInt(year),Integer.parseInt(weekth));
    beginDate = tca.getFullTimeStr(darr[0]);
    endDate = tca.getFullTimeStr(darr[1]);
  }
  
  Date curDate = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormatWeek = new SimpleDateFormat("E");
  Calendar c = Calendar.getInstance();
  String curDateStr = dateFormat.format(curDate);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>任务设置</title>
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
var menuData1 = [{ name:'<a id="CalLevel_" href="#" class="CalLevel" style="padding-top:5px;margin-left:5px">未指定</a>',action:set_status,extData:'0'}
,{ name:'<a id="CalLevel_1" href="#" class="CalLevel1" style="padding-top:5px;margin-left:5px">重要/紧急</a>',action:set_status,extData:'1'}
,{ name:'<a id="CalLevel_2" href="#" class="CalLevel2" style="padding-top:5px;margin-left:5px">重要/不紧急</a>',action:set_status,extData:'2'}
,{ name:'<a id="CalLevel_3" href="#" class="CalLevel3" style="padding-top:5px;margin-left:5px">不重要/紧急</a>',action:set_status,extData:'3'}
,{ name:'<a id="CalLevel_4" href="#" class="CalLevel4" style="padding-top:5px;margin-left:5px">不重要/不紧急</a>',action:set_status,extData:'4'}
]
var menuData2 = [{ name:'<a id="CalColor_" href="#" class="CalColor" style="padding-top:8px;margin-left:5px">未指定</a>',action:set_color,extData:'0'}
,{ name:'<a id="CalColor_1" href="#" class="CalColor1" style="padding-top:8px;margin-left:5px">红色类别</a>',action:set_color,extData:'1'}
,{ name:'<a id="CalColor_2" href="#" class="CalColor2" style="padding-top:8px;margin-left:5px">黄色类别</a>',action:set_color,extData:'2'}
,{ name:'<a id="CalColor_3" href="#" class="CalColor3" style="padding-top:8px;margin-left:5px">绿色类别</a>',action:set_color,extData:'3'}
,{ name:'<a id="CalColor_4" href="#" class="CalColor4" style="padding-top:8px;margin-left:5px">橙色类别</a>',action:set_color,extData:'4'}
,{ name:'<a id="CalColor_5" href="#" class="CalColor5" style="padding-top:8px;margin-left:5px">蓝色类别</a>',action:set_color,extData:'5'}
,{ name:'<a id="CalColor_6" href="#" class="CalColor6" style="padding-top:8px;margin-left:5px">紫色类别</a>',action:set_color,extData:'6'}
]
function set_status(){
  var important = arguments[2];
  var importantName;
  var importantNames = ["未指定","重要/紧急","重要/不紧急","不重要/紧急","不重要/不紧急"];
  if(important==''){
    important= 0;
  }
  importantName = importantNames[important];
  document.getElementById("impName").innerHTML=importantName;
  document.getElementById("important").value=important;
  document.getElementById("importantName").className = "CalLevel"+important;
 // 怎么超连接的class属性
}
function compareDate(beginDate , endDate) {
  d1Arr=beginDate.value.split('-');
  d2Arr=endDate.value.split('-');
  v1=new Date(d1Arr[0],d1Arr[1],d1Arr[2]);
  v2=new Date(d2Arr[0],d2Arr[1],d2Arr[2]);
  return v1>v2;
}
function set_color(){
  var color = arguments[2];
  var colorName;
  if(color==''){
    color = 0;
  }
  var colorNames = ["未指定","红色类别","黄色类别","绿色类别","橙色类别","蓝色类别","紫色类别"];
  colorName = colorNames[color];
  document.getElementById("colName").innerHTML=colorName;
  document.getElementById("color").value=color;
  document.getElementById("colorName").className = "CalColor"+color;
}
function showMenu(event){
  var menu = new Menu({bindTo:$('importantName') , menuData:menuData1 , attachCtrl:true});
  menu.show(event);
}
function showMenuColor(event){
  var menu = new Menu({bindTo:'colorName' , menuData:menuData2 , attachCtrl:true});
  menu.show(event);
}
function CheckForm(){
  if(document.getElementById("subject").value==""){
   	alert("任务标题不能为空！");
    return (false);
  }
  if(document.getElementById("user").value==""){
    alert("请选择人员!");
    return (false);
  }
  var beginDate = document.getElementById("beginDate");
  var endDate = document.getElementById("endDate");
  var IsInt = "^[0-9]*[1-9][0-9]*$";//正整数　
  var IsInt1 = "^[0-9][0-9]*$";//0和正整数　
  var IsInt2 =   "^-?\\d+$";//整数"^\\d+$"　
  var IsInt3 = "^[0-9]*\\.?[0-9]*$";//0以上的数
  var re = new RegExp(IsInt1);
  var re3 = new RegExp(IsInt3);
  if(document.getElementById("taskNo").value!=""&&document.getElementById("taskNo").value.match(re) == null ){
    alert("排序号应为整数!");
    document.getElementById("taskNo").focus();
    document.getElementById("taskNo").select();
    return false;
  }
  if(beginDate.value!=""&&!isValidDateStr(beginDate.value)){
    alert("起始日期格式不对,应形如 2010-02-01");
    beginDate.focus();
    beginDate.select();
    return false;
  }
  if(endDate.value!=""&&!isValidDateStr(endDate.value)){
    alert("结束日期格式不对,应形如 2010-02-01");
    endDate.focus();
    endDate.select();
    return false;
  }
  if(beginDate.value!=""&&endDate.value!=""){
    if(compareDate(beginDate , endDate)){
      alert("起始日期不能大于结束日期!");
      endDate.focus();
      endDate.select();
      return false;
    } 
  }
  var remindTime = document.getElementById("remindTime");
  var finishTime = document.getElementById("finishTime");
  var remindTimeArray  = remindTime.value.trim().split(" ");
  var finishTimeArray  = finishTime.value.trim().split(" ");
  var type1 =   "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　;
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2);
  //if(remindTime.value==''){
   // alert("请指定提醒时间！");
    //remindTime.focus();
   // remindTime.select();
   // return false;
//  }
  if(remindTime.value!=""&&remindTimeArray.length!=2){
    alert("提醒时间应为日期时间型，如：1999-01-01 10:20:10");
    remindTime.focus();
    remindTime.select();
    return false;
  }else{
    if(remindTime.value!=""&&(!isValidDateStr(remindTimeArray[0])||remindTimeArray[1].match(re1) == null ||remindTimeArray[1].match(re2) != null)){
      alert("提醒时间应为日期时间型，如：1999-01-01 10:20:10");
      remindTime.focus();
      remindTime.select();
      return false;
    }
  }
  if(document.getElementById("rate").value!=""&&document.getElementById("rate").value.match(re3)==null){
    alert("完成率不是有效的数字！");
    document.getElementById("rate").focus();
    document.getElementById("rate").select();
    return false;
  }
  if(document.getElementById("rate").value!=""&&parseFloat(document.getElementById("rate").value)>100){
    alert("完成率不能超过100！");
    document.getElementById("rate").focus();
    document.getElementById("rate").select()
    return false;
  }
  if(finishTime.value!=""&&finishTimeArray.length!=2){
    alert("完成时间应为日期时间型，如：1999-01-01 10:20:10");
    finishTime.focus();
    finishTime.select();
    return false;
  }else{
    if(finishTime.value!=""&&(!isValidDateStr(finishTimeArray[0])||finishTimeArray[1].match(re1) == null ||finishTimeArray[1].match(re2) != null)){
      alert("完成时间应为日期时间型，如：1999-01-01 10:20:10");
      finishTime.focus();
      finishTime.select();
      return false;
    }
  }
  if(document.getElementById("totalTime").value!=""&&document.getElementById("totalTime").value.match(re) == null ){
    alert("工作总量应为整数!");
    document.getElementById("totalTime").focus();
    document.getElementById("totalTime").select();
    return false;
  }
  if(document.getElementById("useTime").value!=""&&document.getElementById("useTime").value.match(re) == null ){
    alert("实际工作量应为整数!");
    document.getElementById("useTime").focus();
    document.getElementById("useTime").select();
    return false;
  }
   return (true);
   return (true);
}
function doOnload(){
  var userId = '<%=userId%>';
  var year = '<%=year%>';
  var month = '<%=month%>';
  var day = '<%=day%>';
  var userIds = '<%=userIds%>';
  var dwm = '<%=dwm%>';
  var date1 = '<%=date1%>';
  var date7 = '<%=date7%>';
  var date = '<%=date%>';
  var caltimestr;
  var endtimestr;
  if(dwm=='day'){
    selectUserName(userId);
    caltimestr = year+"-"+month+"-"+day;
  }
  if(dwm=='dayDept'){
    //selectUserNamesByDept(deptId);
     selectUserName(userIds);
    caltimestr = year+"-"+month+"-"+day;
  }
  if(dwm=='week'){
    selectUserName(userId);
    caltimestr = date1;
  }
  if(dwm=='weekIndex'){
    selectUserName(userId);
    caltimestr = date;
  }
  if(dwm=='weekIndexDept'){
    //selectUserNamesByDept(deptId);
     selectUserName(userIds);
    caltimestr = date;
  }
  if(dwm=='month'){
    selectUserName(userId);
    caltimestr = year+"-"+month+"-"+day;
  }
  if(dwm=='monthWeek'){
    selectUserName(userId);
    caltimestr = '<%=beginDate%>';
  }
  document.getElementById("beginDate").value=caltimestr;
//初始化时间
  var date1Parameters = {
      inputId:'beginDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date1Img'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'endDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date2Img'
  };
  new Calendar(date2Parameters);
  var date3Parameters = {
      inputId:'remindTime',
      property:{isHaveTime:true}
      ,bindToBtn:'date3Img'
  };
  new Calendar(date3Parameters);
  var date4Parameters = {
      inputId:'finishTime',
      property:{isHaveTime:true}
      ,bindToBtn:'date4Img'
  };
  new Calendar(date4Parameters);
  getSysRemind();
//手机
  getMoblieSmsRemind("moblieSmsRemindDiv","moblieSmsRemind");
}
function Init(){
  if(CheckForm()){
    var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/addTask.act";
    var rtJson = getJsonRs(requestUrl,mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    parent.opener.location.reload();
    document.getElementById("listDiv").style.display="none";
    document.getElementById("returnDiv").style.display="block";
  }
}
function selectUserName(userId){
  var requestURL = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectUserNames.act?userId="+userId; 
  var json = getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prc = json.rtData;
  document.getElementById("user").value = userId;
  document.getElementById("userDesc").innerHTML = prc.userName;
}
function selectUserNamesByDept(deptId){
  var requestURL = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectUserNamesByDept.act?deptId="+deptId; 
  var json = getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prc = json.rtData;
  document.getElementById("user").value = prc.userIds.substr(0,prc.userIds.length-1);
  document.getElementById("userDesc").innerHTML = prc.userNames;
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
  var allowRemind = prc.allowRemind;
  var defaultRemind = prc.defaultRemind;
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
<div id="listDiv">
<table border="0" width="300" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/calendar.gif" align="absMiddle" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;新建任务-<b id="userDesc"></b></span>
    </td>
  </tr>
</table>
<form action="<%=contextPath%>/yh/core/funcs/calendar/act/YHTaskAct/addTask.act" id="form1" method="post" name="form1" onsubmit="return CheckForm();">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.calendar.data.YHTask"/>
      <input type="hidden" name="user" id="user" value=""  />
 <table class="TableBlock" width="300" align="center" height="300">
    <tr>
      <td nowrap class="TableContent">排序号：</td>
      <td class="TableData" colspan=3>
        <INPUT type="text" id="taskNo" name="taskNo" class="BigInput" size="4" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 类型：</td>
      <td class="TableData" width=100>
        <select id="taskType" name="taskType" class="BigSelect">
          <option value="1" >工作</option>
          <option value="2" >个人</option>
        </select>
      </td>
      <td nowrap class="TableContent" width=40> 状态：</td>
      <td class="TableData" width=150>
        <select id="taskStatus" name="taskStatus" class="BigSelect">
          <option value="1" >未开始</option>
          <option value="2" >进行中</option>
          <option value="3" >已完成</option>
          <option value="4" >等待其他人</option>
          <option value="5" >已推迟</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">颜色：</td>
      <td class="TableData">
       <a id="colorName" href="javascript:;" class="CalColor" onclick="showMenuColor(event);" hidefocus="true"><span id="colName">未指定</span><span style="font-family:Webdings">6</span></a><input type="hidden" id="color" name="color" value="0"/>
      </td>
      <td nowrap class="TableContent">优先级：</td>
      <td class="TableData">
       <a id="importantName" href="javascript:;" class="CalLevel" onclick="showMenu(event);" hidefocus="true"><span id="impName">未指定</span><span style="font-family:Webdings">6</span></a><input type="hidden" id="important" name="important" value="0"/>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">任务标题：</td>
      <td class="TableData" colspan=3>
        <input type="text" id="subject" name="subject" class=BigInput size="50" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">起止日期：</td>
      <td class="TableData" colspan=3>
      	开始于
        <input type="text" id="beginDate" name="beginDate" class=BigInput size="10" value="<%=curDateStr %>">
        <img id="date1Img" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
        结束于
        <input type="text" id="endDate" name="endDate" class=BigInput size="10" value="">
        <img id="date2Img" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 任务详细：</td>
      <td class="TableData" colspan=3>
        <textarea id="content" name="content" cols="40" rows="3" class="BigInput"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">事务提醒：</td>
      <td class="TableData" colspan="3">
    <span id="smsRemindDiv"><input type="checkbox" id="smsRemind" name="smsRemind" ><label for="smsRemind">使用事务提醒</label>&nbsp;&nbsp;</span>    <span id="moblieSmsRemindDiv"><input type="checkbox" id="moblieSmsRemind" name="moblieSmsRemind" ><label for="smsRemind">使用手机短信提醒</label>&nbsp;&nbsp;</span>      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">提醒时间：</td>
      <td class="TableData" colspan=3>
        <input id="remindTime" name="remindTime" size="20" class="BigInput" value="">
        <img id="date3Img" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
     为空则不提醒
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">完成情况：</td>
      <td class="TableData" colspan=3>
        完成率 <INPUT id="rate" type="text"name="rate" class=BigInput size="3" value=""> %&nbsp;&nbsp;
        完成时间 <input id="finishTime" name="finishTime" size="20" class="BigInput" value="">
        <img id="date4Img" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
    </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">工作量：</td>
      <td class="TableData" colspan=3>
        工作总量 <INPUT type="text" id="totalTime" name="totalTime" class=BigInput size="4" value=""> 小时，
        实际工作 <input type="text" id="useTime" name="useTime" size="4" class="BigInput" value=""> 小时
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
      	<INPUT type="hidden" name="PAGE_START" value="0">
      	<INPUT type="hidden" name="seqId" value="">
           <input type="button" value="确定" class="BigButton" onclick="Init();">&nbsp;&nbsp;
         <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
           <input type="button" value="关闭" class="BigButton" onclick="parent.close();">
      </td>
    </tr>
  </table>
</form>
</div>
<div id="returnDiv" style="display:none">
<table class="MessageBox" align="center" width="220">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">保存成功</div>
    </td>
  </tr>
</table>
<br><center>
<input type="button" class="BigButton" value="继续新建" onclick="window.location.reload();">&nbsp;&nbsp;
<input type="button" class="BigButton" value="关闭" onclick="parent.close();">
</center>
</div>
</body>
</html>