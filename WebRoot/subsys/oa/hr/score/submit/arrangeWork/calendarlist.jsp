<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月dd日");

  String userId = request.getParameter("userId") ==null ? "" : request.getParameter("userId");
  String beginDate = request.getParameter("beginDate") ==null ? "" : request.getParameter("beginDate");
  String endDate = request.getParameter("endDate") ==null ? "" : request.getParameter("endDate");
  //String beginTimeStr = dateFormat2.format(dateFormat1.parse(beginDate));
  //String endTimeStr = dateFormat2.format(dateFormat1.parse(endDate));
  //相隔多少天
  long daySpace = 0;
  if(!YHUtility.isNullorEmpty(beginDate) && !YHUtility.isNullorEmpty(endDate)){
    daySpace = YHUtility.getDaySpan(dateFormat1.parse(beginDate),dateFormat1.parse(endDate))+1;
  }

  //得到到之间的天数数组
  List daysList = new ArrayList();
  String days = "";
  Calendar calendar = new GregorianCalendar();
  for(int i = 0;i<daySpace;i++){
    calendar.setTime(dateFormat1.parse(beginDate));
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
<title>工作日程安排</title>

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

function doOnload(){
  var userId = '<%=userId%>';
  var beginDate = '<%=beginDate%>';
  var endDate = '<%=endDate%>';
  var daySpace = '<%=daySpace%>';
  var days = '<%=days%>';
  if(beginDate!= '' && endDate != ''){
    //新建table
    var user = getUserName(userId);
    var loginUserId = user.loginUserId;
    var table = new Element('table',{"class":"TableBlock"}).update("<tbody id='tbody'>"
        +"<tr id='headerTr' class='TableHeader'><td>姓名</td></tr>"
        +"<tr id='calendarTr'><td>"+user.userName+"</td></tr>"
        +"</tbody>");
    $("calendarList").appendChild(table);
    var dayArray = days.split(",");
    for(var i = 0; i <dayArray.length ; i++){
      var td = new Element('td',{"align":"center"});
      $("headerTr").appendChild(td);
      td.update(dayArray[i]);
      
      var cTd = new Element('td',{"align":"center"});
      $("calendarTr").appendChild(cTd);
      var prcs = getCalendar(userId,dayArray[i]);

      var divStr = "";
      for(var j = 0; j <prcs.length ; j++){
        var prc = prcs[j];
        var seqId = prc.seqId;
        var content = prc.content;
        var managerId = prc.managerId;
        var managerName = prc.managerName;
        var overStatus = prc.overStatus;
        var calTime = prc.calTime;
        var endTime = prc.endTime;
        if(managerName!=''){
          managerName = "<br>安排人："+ managerName;
        }
        if(toDateTime(calTime,endTime)){
          calTime = calTime.substr(11,5);
          endTime = endTime.substr(11,5);
        }
        var overStatus1 = "";
        if(overStatus=="" || overStatus=="1"){
          overStatus1 = "<font color='#00AA00'><b>已结束</b></font>";
        }

        if(managerId == loginUserId){
          divStr = divStr + "<div>"+calTime+ "-" + endTime + "<br><a href='javascript:updateCalendar("+seqId+");'>" + content + "</a>&nbsp; "+overStatus1+" &nbsp;<a href='javascript:delCalendar("+seqId+");'>删除</a>"  + managerName+"</div>";
        }else{
          divStr = divStr + "<div>"+calTime+ "-" + endTime + "<br>" + content + overStatus1  + managerName+"</div>";
        }
      }
      cTd.update(divStr);
    }
  }  
}
function getCalendar(userId,date){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreDataAct/getCalendar.act?userId="+userId + "&date=" + date ; 
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
  	alert(rtJson.rtMsrg); 
  	return ; 
  }
  var prcs = rtJson.rtData;
  return prcs;
}
function getUserName(userId){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreDataAct/getUserName.act?userId="+userId  ; 
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
  	alert(rtJson.rtMsrg); 
  	return ; 
  }
  var prc = rtJson.rtData;
  return prc;
}
function toDateTime(beginTime,endTime){
  if(beginTime != '' && endTime != ''){
    beginTime = beginTime.substr(0,10);
    endTime = endTime.substr(0,10);
    if(beginTime = endTime){
      return true;
    }
  }
  return false;
}
function delCalendar(seqId){
  var msg = "确认要删除此任务吗？";
  if(window.confirm(msg)){
    var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/deleteCalendarById.act?seqId="+seqId+"&ldwm=month";
    var rtJson = getJsonRs(URL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.location.reload();
  }
}
function updateCalendar(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/info/editcalendar.jsp?seqId="+seqId+"&ldwm=day";
  window.open(URL,"my_note","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=200,resizable=yes");

}
</script>
</head>

<body class="" onload="doOnload();" topmargin="5">
<%if(YHUtility.isNullorEmpty(beginDate)){ %>
<table class="MessageBox" align="center" width="500">
   <tr>
     <td class='msg info'><div class='content' style='font-size:12pt'>请点击日历上的日期或周次进行查询或安排工作!</div>
     </td>
     </tr>

</table>

<%}else{ %>
 <div id="calendarList" align='center'>
 
 </div>

<%} %>

  
 
</body>
</html>
