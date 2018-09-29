<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  Date date = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormatWeek = new SimpleDateFormat("E");
  SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
  Calendar c = Calendar.getInstance();
  String week = dateFormatWeek.format(date);
  String dateStr = dateFormat.format(date);
  int year = Integer.parseInt(dateStr.substring(0,4));
  int month = Integer.parseInt(dateStr.substring(5,7));
  int day = Integer.parseInt(dateStr.substring(8,10));
  //本页面

  String yearStr = request.getParameter("year");
  String monthStr = request.getParameter("month");
  String dateWeekStr = request.getParameter("date");
  String userId = request.getParameter("userId") ==null ? "" : request.getParameter("userId");
  if(yearStr!=null){
    year = Integer.parseInt(yearStr);
    month = Integer.parseInt(monthStr);
  }
  //从周页面跳转过来的

  if(dateWeekStr!=null){
    year = Integer.parseInt(dateWeekStr.substring(0,4));
    month = Integer.parseInt(dateWeekStr.substring(5,7));
  }
  
  //判断月初是第几周
  c.set(year,month-1,1);
  int beginWeekth = c.get(Calendar.WEEK_OF_YEAR);
  //判断这个月1号是星期几

  int beginWeek = c.get(Calendar.DAY_OF_WEEK);
  int maxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数 
  
  //判断这个月最后一天是星期几

  c.set(year,month-1,maxDay);
  int endWeek = c.get(Calendar.DAY_OF_WEEK);
  //判断这个月最后一天是第几周

  int endWeekth = c.get(Calendar.WEEK_OF_YEAR);
  //如果这个月的最后一天是星期天的话，那么最未周-1
  if(endWeek==1){
    endWeekth = endWeekth-1;
  }
  //如果这个月的第一天是星期天的话，那么起试周-1；

  if(beginWeek==1){
    beginWeekth = beginWeekth-1;
  }
  if(month==12){
    endWeekth =53;
  }
  //out.print(beginWeek+":"+beginWeekth+":"+endWeek+":"+endWeekth+":"+maxDay);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>今天</title>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<style>
      html {
                   overflow:auto;  /*这个可以去掉IE6,7的滚动*/
                   _overflow-x:hidden;/*去掉IE6横向滚动*/
                }
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript">
var userId = '<%=userId%>';
function my_note(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId;
  window.open(URL,"my_note","height=200,width=200,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=600,top=300,resizable=yes");
}
function DayNumOfMonth(Year,Month) {
  var d = new Date(Year,Month,0);   
  return d.getDate(); 
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
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/subsys/oa/hr/score/submit/arrangeWork/calendar.jsp?userId="+userId+"&year="+year+"&month="+month+"&ldwm=month";
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
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/subsys/oa/hr/score/submit/arrangeWork/calendar.jsp?userId="+userId+"&year="+year+"&month="+month+"&ldwm=month";
}
function today(){
  window.location = "<%=contextPath%>/subsys/oa/hr/score/submit/arrangeWork/calendar.jsp?userId=" + userId;
}
function doOnload(){
  var year = '<%=year%>';
  var month = '<%=month%>';
  var beginWeek = '<%=beginWeek%>';
  var endWeek = '<%=endWeek%>';
  var beginWeekth = '<%=beginWeekth%>';
  var endWeekth = '<%=endWeekth%>';
  if(beginWeek==1){
    beginWeek = 7;  
  }else{
    beginWeek = beginWeek - 1;
  }
  if(endWeek==1){
    endWeek = 7;  
  }else{
    endWeek = endWeek - 1;
  }
  //建表
  newTable(beginWeekth,endWeekth,beginWeek,endWeek,year,month);

}
function newTable(beginWeekth,endWeekth,beginWeek,endWeek,year,month){
  var table = new Element('table',{"id":"cal_table","class":"TableBlock","width":"70%","align":"center"}).update("<tbody id = 'tboday'><tr align='center' class='TableHeader'>"
 
      +"<td ><b>一</b></td>"
      +"<td ><b>二</b></td>"
      +"<td><b>三</b></td>"
      +"<td ><b>四</b></td>"
      +"<td ><b>五</b></td>"
      +"<td ><b>六</b></td>"
      +"<td ><b bgcolor='#FFCCFF'>日</b></td>"
      +"<td><b>周次</b></td>"
      +"</tr></tbody>");
  $('listDiv').appendChild(table);
  var monththInt = 1 ;
  var tempMonth = 0;
  for(var i=parseInt(beginWeekth,10);i<=parseInt(endWeekth,10);i++){

    var trStr = "";
    tempMonth++;

    if(i==parseInt(beginWeekth)){
      //var tr = new Element('tr',{"class":"TableData","height":"80"});
      var tdStr = "";
      var beginDate = year + "-" + month + "-" + toStr(monththInt + "");
      var endDate = year + "-" + month + "-" ;
      for(var j=1;j<=7;j++){
        if(j>=parseInt(beginWeek,10)){
          monththInt = (j-parseInt(beginWeek,10)+1);
          tdStr =  tdStr+"<td id='td_"+monththInt+"' align='center' ><a href='#' onclick='toCalenarList("+year+","+month+","+monththInt+");'>"+monththInt+"</a></td>";
        }else{
          tdStr =  tdStr+"<td id='td_' align='top'></td>"
         }
      }
      endDate = endDate + toStr(monththInt+ "");
      tdStr = tdStr +   "<td id='tw_' class='TableContent' align='center' ><a href=\"javascript:toCalenarWeek('"+beginDate+"', '" + endDate + "');\">第"+tempMonth+"周</a></td>";
      trStr = trStr + "<tr class='TableData' >" + tdStr + "</tr>";
      //$('tboday').appendChild(tr); 
    }else if(i==parseInt(endWeekth)){
      //var tr = new Element('tr',{"class":"TableData", "height":"80"});
      var tdStr = ""; 
      var beginDate = year + "-" + month + "-" + toStr((monththInt + 1)+ "");
      var endDate = year + "-" + month + "-" ;
      for(var j=1;j<=7;j++){
        if(j<=parseInt(endWeek,10)){
          monththInt = monththInt+1;
          tdStr =  tdStr+"<td id='td_"+monththInt+"' align='center' ><a href='#' onclick='toCalenarList("+year+","+month+","+monththInt+");'>"+monththInt+"</a></td>"
        }else{
          tdStr =  tdStr+"<td id='td_' valign='top'></td>"
        }
      }
      endDate = endDate + toStr(monththInt + "");
      tdStr = tdStr + "<td id='tw_' class='TableContent' align='center' ><a href=\"javascript:toCalenarWeek('"+beginDate+"', '" + endDate + "');\">第"+tempMonth+"周</a></td>";
      
      //tr.update(tdStr);
      trStr = trStr + "<tr class='TableData'>" + tdStr + "</tr>";
      //$('tboday').appendChild(tr); 
    }else{
      //var tr = new Element('tr',{"class":"TableData", "height":"80"});
      var tdStr = "";
      var beginDate = year + "-" + month + "-" + toStr((monththInt + 1)+ "");
      var endDate = year + "-" + month + "-" ;
      for(var j=1;j<=7;j++){
        monththInt = monththInt+1;
        tdStr =  tdStr+"<td id='td_"+monththInt+"' align='center'><a href='#' onclick='toCalenarList("+year+","+month+","+monththInt+");'>"+monththInt+"</a></td>"
      }
      // tr.update(tdStr);
       endDate = endDate + toStr(monththInt + "");
       tdStr = tdStr + "<td id='tw_' class='TableContent' align='center''><a href=\"javascript:toCalenarWeek('"+beginDate+"', '" + endDate + "');\">第"+tempMonth+"周</a></td>";
       trStr = trStr + "<tr class='TableData'>" + tdStr + "</tr>";
      //$('tboday').appendChild(tr); 
    }
    trStr =  $('tboday').innerHTML + trStr ;
    $('tboday').update(trStr);
  } 
  
}
function toStr(str){
  if(str.length <2){
    str = "0" + str;
  }
  return str;
}
function getDayOfWeek(dateStr){
  var day = new Date(Date.parse(dateStr.replace(/-/g, '/'))); //将日期值格式化
  //day.getDay();根据Date返一个星期中的某其中0为星期日
  return day.getDay(); 
}

function My_Submit(){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/subsys/oa/hr/score/submit/arrangeWork/calendar.jsp?userId="+userId+"&year="+year+"&month="+month+"&maxDay="+maxDay+"&ldwm=month";
}
function myNote(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status="+status;
  window.open(URL,"my_note","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=300,resizable=yes");
}
function toCalenarList(year,month,day){
  //alert(day);
  var beginDate = year + "-" +toStr(month) + "-" + toStr(day);
  var endDate = year + "-" +toStr(month) + "-" + toStr(day);
  parent.calendarlist.location = "<%=contextPath%>/subsys/oa/hr/score/submit/arrangeWork/calendarlist.jsp?userId=" + userId + "&beginDate=" + beginDate + "&endDate=" + endDate; 
 
}
function toCalenarWeek(beginDate,endDate){
  //alert(beginDate+":" + endDate);
  parent.calendarlist.location = "<%=contextPath%>/subsys/oa/hr/score/submit/arrangeWork/calendarlist.jsp?userId=" + userId + "&beginDate=" + beginDate + "&endDate=" + endDate; 
  
}
</script>
<body class="" topmargin="5" onload="doOnload();"  style="margin-right:18px;">
<div class="small" style="clear:both;">
 <div style="float:left;">
   <form name="form1" action="/general/calendar/arrange/month.php" style="margin-bottom:5px;">
   <input type="hidden" value="" name="BTN_OP">
   <input type="hidden" value="3" name="OVER_STATUS">
   <input type="hidden" value="26" name="DAY">
   <img src="<%=imgPath %>/calendar.gif" align="absMiddle" WIDTH="22" HEIGHT="20" align="absmiddle"> 工作安排查询
   

   <!-- 年 -->
<a href="javascript:set_year(-1)";  title="上一年"><img src="<%=imgPath%>/prevpreviouspage.png"></img></a>
   <a href="javascript:set_month(-1);" class="ArrowButtonR" title="上一月"><img src="<%=imgPath%>/previouspage.gif"></img></a>
   <select id="year" name="year"  style="height:22px;FONT-SIZE: 11pt;"   onchange="My_Submit();">
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
   </select>
<!-- 月 -->
<select id="month" name="month"  style="height:22px;FONT-SIZE: 11pt;"  onchange="My_Submit();">
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
       <option value="<%=i %>" selected="selected">0<%=i %>月</option>
        <%}else{ %>
     <option value="0<%=i %>">0<%=i %>月</option>
       <%
        }
      }
    }
       %>
   </select>
   <a href="javascript:set_month(1);" class="ArrowButtonR" title="下一月"><img src="<%=imgPath%>/nextpage.gif"></img></a>
   <a href="javascript:set_year(1);" class="ArrowButtonRR" title="下一年"><img src="<%=imgPath%>/nextnextpage.png"></a>&nbsp;
   
      <input type="button" value="本月" class="SmallButton1" title="本月" onclick="today();">
 </div>
</div>
<br></br>
<div id="listDiv"></div>
</body>
</html>

