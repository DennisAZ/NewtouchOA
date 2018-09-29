 <%@ page language="java" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*,yh.core.funcs.calendar.act.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId==null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑活动安排 </title>
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
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function doOnload(){
  var seqId = '<%=seqId%>';
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/active/act/YHActiveAct/selectActiveById.act?seqId="+seqId;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  if(prc.seqId){
    var seqId = prc.seqId;
    var activeUser = prc.activeUser;
    var activeUserName = prc.activeUserName;
    var opUserId = prc.opUserId;
    var opUserIdName = prc.opUserIdName;
    var opDatetime = prc.opDatetime;
    var activeTime = prc.activeTime;
    var activeContent = prc.activeContent;
    var activeTimeStr = activeTime.substr(11,8);
    var date = activeTime.substr(0,10);
    var activeTimeInt = str_int(activeTimeStr);
    var dayTemp = getTimeOfDay(activeTimeInt);
    var TimeOfDayDesc = "上午";
    if(dayTemp==1){
      TimeOfDayDesc = "上午";
    }
    if(dayTemp == 2){
      TimeOfDayDesc = "下午";
    }
    if(dayTemp==3){
      TimeOfDayDesc = "晚上";
    }
    var bodyContentStr = "" + activeTime.substr(0,16) + "&nbsp" + TimeOfDayDesc + "<br></br>"+activeContent+"<br></br>"+ activeUserName;
    $("bodyContent").innerHTML = bodyContentStr;
  }
}
function getTimeOfDay(timeInt){
  var temp = 1 ;
  if(timeInt>43200&&timeInt<=64800){
     temp = 2;
  }
  if(timeInt>64800){
    temp = 3;
  }
  return temp;
}
function str_int(str){
  var min = 0;
  var max = 24*3600;
  var strInt;
  var strInt1;
  var strInt2;
  var strInt3;
  var strArray = str.split(":");
  for(var i = 0 ; i<strArray.length; i++){
    if(i==0){
      strInt1 = parseInt(strArray[i]*3600,10);
    }else if(i==1){
      strInt2 = parseInt(strArray[i]*60,10); 
    }else{
      strInt3 = parseInt(strArray[i],10);
    }  
  }
  strInt = strInt1+strInt2+strInt3;
  return strInt;
}
</script>
<body class="bodyClass" onload="doOnload();">
 
<div class="small" id="bodyContent">
</div>
</body>
</html>
