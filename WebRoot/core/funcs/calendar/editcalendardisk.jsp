<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑事务</title>
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
    document.getElementById("content").focus();
    document.getElementById("content").select();
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
  var type2 = "^\:[0-5]?[0-9](\:[0-5]?[0-9])?$";
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
  var seqId = '<%=request.getParameter("seqId")%>'; 
  var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/selectCalendarById.act?seqId="+seqId;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  var seqId = prc.seqId;
  var userId = prc.userId;
  var calType = prc.calType
  var calLevel = prc.calLevel;
  var content = prc.content;
  var managerId = prc.managerId;
  var calTime = prc.calTime;
  var endTime = prc.endTime;
  var overStatus = prc.overStatus; 
  var valLevelName ; 
  document.getElementById("calTime").value=calTime.substr(0,16);
  document.getElementById("endTime").value=endTime.substr(0,16);
  document.getElementById("seqId").value=seqId;
  document.getElementById("content").value=content;
  document.getElementById("calType").value=calType;
  document.getElementById("calLevel").value=calLevel;
  document.getElementById("calLevelName").className = "CalLevel"+calLevel;
  if(calLevel=='0'){
    valLevelName = "未指定";
  }
  if(calLevel=='1'){
    valLevelName = "重要/紧急";
  }
  if(calLevel=='2'){
    valLevelName = "重要/不紧急";
  }
  if(calLevel=='3'){
    valLevelName = "不重要/紧急";
  }
  if(calLevel=='4'){
    valLevelName = "不重要/不紧急";
  }
  document.getElementById("calLevelName").innerHTML=valLevelName;
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
  var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/updateCalendarById.act";
  var rtJson = getJsonRs(URL,mergeQueryString($("form1")));
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
 // history.go(-1);
  top.reloadDesktop();
  top.dispDesk();
}
function getBack(){
  top.reloadDesktop();
  top.dispDesk();
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/calendar.gif" align="absMiddle" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;编辑事务</span>
    </td>
  </tr>
</table>
<form action="<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/updateCalendar.act"  method="post" id="form1" name="form1" onsubmit="return CheckForm();">
 <table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData"> 优先级：</td>
     <td class="TableData">
        <a id="calLevelName" href="javascript:;" class="CalLevel" onclick="showMenu(event);" hidefocus="true">未指定ddd</a>&nbsp;
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
        <select id="calType" name="calType" class="BigSelect">
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
        <input type="text" id="beforeDay" name="beforeDay" size="2" class="BigInput" value=""> 天
        <input type="text" id="beforeHour" name="beforeHour" size="2" class="BigInput" value=""> 小时
        <input type="text" id="beforeMin" name="beforeMin" size="2" class="BigInput" value="10"> 分钟提醒
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 提醒：</td>
      <td class="TableData">
      <input type="checkbox" id="smsRemind" name="smsRemind"  checked><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" id="seqId" name="seqId" value="<%=request.getParameter("seqId")%>"></input>
        <input type="button" value="确定" class="BigButton" onclick="Init()">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="getBack();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>