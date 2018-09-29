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

  if(yearStr!=null&&monthStr!=null){
    year = Integer.parseInt(yearStr);
    month = Integer.parseInt(monthStr);
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

  String seqId = request.getParameter("seqId") == null ? "" : request.getParameter("seqId");
  String projId = request.getParameter("projId") == null ? "" : request.getParameter("projId");

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
var projId = '<%=seqId%>';
  var menuData1 = [{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看<div>',action:set_work,extData:'0'}
  //,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">完成<div>',action:set_work,extData:'1'}
  ]
  var menuData2 = [{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看<div>',action:set_work,extData:'0'}
  //,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">未完成<div>',action:set_work,extData:'1'}

  ]
function set_work(){
  var status= arguments[2];
  var seqId_overStatus=  arguments[1];
  var seqId = seqId_overStatus.split(',')[0].substr('cal_'.length);
  var overStatus = seqId_overStatus.split(',')[1];
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  if(status=='0'){
    var URL = "<%=contextPath%>/subsys/oa/profsys/out/baseinfo/news/calendar/calendarNote.jsp?seqId="+seqId;
    window.open(URL,"my_note","height=350,width=350,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=350,top=200,resizable=yes");
  }
  if(status=='1'){
    if(overStatus=='0'){
      overStatus='1';
    }else{
      overStatus='0';
    }
    var URL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+overStatus;
    var rtJson = getJsonRs(URL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    document.location.reload();
  }
  if(status=='2'){
    var URL = "<%=contextPath%>/subsys/oa/profsys/out/baseinfo/news/calendar/editCalendar.jsp?seqId="+seqId;
    window.location.href=URL;
  }
  if(status=='3'){
    var msg = "确认要删除此任务吗？";
    if(window.confirm(msg)){
      var URL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectCalendarAct/deleteCalendarById.act?seqId="+seqId;
      var rtJson = getJsonRs(URL);
      if(rtJson.rtState == "1"){
        alert(rtJson.rtMsrg); 
        return ;
      }
      window.location.reload();
    }
  }
}
function showMenuWork(event,seqId,overStatus){
  if(overStatus=='0'){
    var menu = new Menu({bindTo:'cal_'+seqId+','+overStatus , menuData:menuData1 , attachCtrl:true});
  }else {
    var menu = new Menu({bindTo:'cal_'+seqId+','+overStatus , menuData:menuData2 , attachCtrl:true});
  } 
  menu.show(event,seqId,overStatus);
}
function my_note(seqId){
  var URL = "<%=contextPath%>/subsys/oa/profsys/out/baseinfo/showdetail/calendar.jsp?seqId="+seqId;
  window.open(URL,"my_note","height=200,width=200,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=600,top=300,resizable=yes");
}
function DayNumOfMonth(Year,Month) {
  var d = new Date(Year,Month,0);   
  return d.getDate(); 
}
function set_year(index){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  if (parseInt(year) <= 2000){
    year = parseInt(year);
  }else if(parseInt(year) >= 2049){
    year = parseInt(year);
  }else {
    year = parseInt(year)+ parseInt(index);
  }
  var maxDay = DayNumOfMonth(year,month);
  window.location = "<%=contextPath%>/subsys/oa/profsys/out/baseinfo/showdetail/calendar.jsp?year="+year+"&month="+month;
}
function set_month(index) {
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  if(parseInt(month,10)+index <= 0){
    year = parseInt(year)-1;
    month = 12;
  }else if(parseInt(month,10) + index > 12){
    year = parseInt(year)+1;
    month = 1;
  }else{
    month = parseInt(month,10) + index;
  }
  var maxDay = DayNumOfMonth(year,month);
  window.location = "<%=contextPath%>/subsys/oa/profsys/out/baseinfo/showdetail/calendar.jsp?year="+year+"&month="+month;
}
function today(){
  window.location.href = "<%=contextPath%>/subsys/oa/profsys/out/baseinfo/showdetail/calendar.jsp";
}
function doOnload(){
  var projId = parent.seqId;
  //alert(projId);
  if(projId > 0){
    $("projId").value = projId;
  }else {
    $("projId").value = '<%=seqId%>';
  }
  var year = '<%=year%>';
  var month = '<%=month%>';
  var beginWeek = '<%=beginWeek%>';
  var endWeek = '<%=endWeek%>';
  var beginWeekth = '<%=beginWeekth%>';
  var endWeekth = '<%=endWeekth%>';
  var colorTypes = ["","#0000FF","#0000FF","#FF0000","#00AA00"];
  var colorType = colorTypes[status];
  if(beginWeek == 1){
    beginWeek = 7;  
  }else{
    beginWeek = beginWeek - 1;
  }
  if(endWeek == 1){
    endWeek = 7;  
  }else{
    endWeek = endWeek - 1;
  }
  //建表
  newTable(beginWeekth,endWeekth,beginWeek,endWeek,year,month);


  var statusName = "全部";
  var statusNames = ['全部','未开始','进行中','已超时','已完成'];
  for(var i = 0;i<statusNames.length;i++){
    if(i==status){
      statusName = statusNames[i];  
    }
  }
  document.getElementById("year").value = year;
  document.getElementById("month").value = month;
  var projId = $("projId").value;
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectCalendarAct/selectCalendarByMonth.act?year="+year+"&month="+month + "&projId="+projId;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  if(prcs.length > 0){
    var calLevelName = "未指定"
    //判断是否跨天
    for(var i=0;i<prcs.length;i++){
      var prc = prcs[i];
      var seqId = prc.seqId;
      var userId = prc.userId;
      var overStatus = prc.overStatus;
      var status = prc.status;
      var content = prc.activeContent;
      var startTime = prc.startTime;
      var endTime = prc.endTime;
      if(overStatus.trim()==''){
        overStatus = "0";
      }
      var statusName = "状态: 进行中";
      var statusStyle = "color:#0000FF";
      if(status=='1'){
        statusName = "状态: 未开始";
      }
      if(status=='2'){
        statusName = "状态: 已超时";
        statusStyle = "color:#FF0000"
      }
      if(overStatus=='1'){
        statusName = "状态: 已完成";
        statusStyle = "color:#00AA00";
      }  
      var dayStatus = prc.dayStatus;
      if(true){
        if(dayStatus=='1'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"  +statusName + "'><a href='javascript:set_month(-1);' title='上一月'><span style='font-family:Webdings'>3</span></a> <span  title='" + calLevelName+ "'>" + startTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ","+overStatus+"' href='javascript:my_note(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");' style='"+statusStyle+";'>"+content+"</a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
        }
        if(dayStatus=='2'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='" + statusName + "'><span  title='" + calLevelName+ "'>" + startTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ","+overStatus+"' href='javascript:my_note(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");;' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_month(1);' title='下一月'><span style='font-family:Webdings'>4</span></a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
         }
        if(dayStatus=='3'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div title='"+ statusName + "'><a href='javascript:set_month(-1);' title='上一月'><span style='font-family:Webdings'>3</span></a> <span  title='" + calLevelName+ "'>"  + startTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span><a id='cal_" + seqId + ","+overStatus+"' href='javascript:my_note(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");' style='"+statusStyle+";'>"+content+"</a> <a href='javascript:set_month(1);' title='下一月'><span style='font-family:Webdings'>4</span></a> </div>"
          );
          $("spanMonthCalendar").appendChild(div);
         }
        if(dayStatus=='4'){
          document.getElementById("spanMonth").style.display="block";
          var div = new Element('div').update("<div  title='"+ statusName + "'><span >"  + startTime.substr(0,16) + " - " +endTime.substr(0,16)+ "</span> <a id='cal_" + seqId + ","+overStatus+"' href='javascript:my_note(" + seqId + ");' onmouseover='showMenuWork(event," + seqId +","+ overStatus+ ");' style='"+statusStyle+";'>"+content+"</a></div>"
          );
          $("spanMonthCalendar").appendChild(div);
         }
      }

    //没跨天 
      var dayStatus = prc.dayStatus;
      if(dayStatus == '0'){
        var startTimeSubStr = startTime.substr(8,2);
        var ctssi = parseInt(startTimeSubStr,10);
        if(true){
      
          var div = new Element('div').update("<div title='"
              + statusName +"'> <span >"
              + startTime.substr(11,5) + " - "
              + endTime.substr(11,5)+ "</span> "
              + "<br><a id='cal_"
              + seqId + "," + overStatus + "' href='javascript:myNote(" + seqId + ");' "
              + " onmouseover='showMenuWork(event," + seqId + "," + overStatus + ");' style='"+statusStyle+"'>"+content+"</a>"
              + "</div>");         
          $("td_"+ctssi).appendChild(div);
        }

      }
    }
  }

}
function newTable(beginWeekth,endWeekth,beginWeek,endWeek,year,month){
  var table = new Element('table',{"id":"cal_table","class":"TableBlock","width":"100%","align":"center"}).update("<tbody id = 'tboday'><tr align='center' class='TableHeader'>"
      +"<td width='6%'><b>周数</b></td>"
      +"<td width='14%'><b>星期一</b></td>"
      +"<td width='14%'><b>星期二</b></td>"
      +"<td width='14%'><b>星期三</b></td>"
      +"<td width='14%'><b>星期四</b></td>"
      +"<td width='14%'><b>星期五</b></td>"
      +"<td width='12%'><b>星期六</b></td>"
      +"<td width='12%'><b>星期日</b></td>"
      +"</tr></tbody>");
  $('listDiv').appendChild(table);
  //跨天的tr
  var tr = new Element('tr',{"id":"spanMonth","class":"TableData","align":"left","style":"display:none"}).update("<td class='TableContent' align='center'>跨天</td>"
    +"<td id='spanMonthCalendar' colspan='7'></td>");
  $('tboday').appendChild(tr);
  var monththInt ;
  for(var i=parseInt(beginWeekth,10);i<=parseInt(endWeekth,10);i++){

    var trStr = "";
    if(i==parseInt(beginWeekth)){
      //var tr = new Element('tr',{"class":"TableData","height":"80"});
      var tdStr = "<td id='tw_' class='TableContent' align='center' >第"+i+"周</td>";
      for(var j=1;j<=7;j++){
        if(j>=parseInt(beginWeek,10)){
          monththInt = (j-parseInt(beginWeek,10)+1);
          tdStr =  tdStr+"<td id='td_"+monththInt+"' valign='top' ><div align='right' class='TableContent'title='双击建立日事务' style='cursor:pointer;width: 100%;'><font color='blue'  align='right'><b id='"+monththInt+"'>"+monththInt+"</b></font></div></td>"
        }else{
          tdStr =  tdStr+"<td id='td_' align='top'></td>"
         }
      }
      //tr.update(tdStr);
      trStr = trStr + "<tr class='TableData' height='80'>" + tdStr + "</tr>";
      //$('tboday').appendChild(tr); 
    }else if(i==parseInt(endWeekth)){
      //var tr = new Element('tr',{"class":"TableData", "height":"80"});
      var tdStr = "<td id='tw_' class='TableContent' align='center' >第"+i+"周</td>";
      for(var j=1;j<=7;j++){
        if(j<=parseInt(endWeek,10)){
          monththInt = monththInt+1;
          tdStr =  tdStr+"<td id='td_"+monththInt+"' valign='top' ><div align='right' class='TableContent'  title='双击建立日事务'  style='cursor:pointer;width: 100%;'><font color='blue'  align='right'><b id='"+monththInt+"'>"+monththInt+"</b></font></div></td>"
        }else{
          tdStr =  tdStr+"<td id='td_' valign='top'></td>"
        }
      }
      //tr.update(tdStr);
      trStr = trStr + "<tr class='TableData' height='80'>" + tdStr + "</tr>";
      //$('tboday').appendChild(tr); 
    }else{
      //var tr = new Element('tr',{"class":"TableData", "height":"80"});
      var tdStr = "<td id='tw_' class='TableContent' align='center' >第"+i+"周</td>";
          for(var j=1;j<=7;j++){
           monththInt = monththInt+1;
           tdStr =  tdStr+"<td id='td_"+monththInt+"' valign='top' ><div align='right' class='TableContent'  title='双击建立日事务'  style='cursor:pointer;width: 100%;'><font color='blue'  align='right'><b id='"+monththInt+"'>"+monththInt+"</b></font></div></td>"
          }
      // tr.update(tdStr);
       trStr = trStr + "<tr class='TableData' height='80'>" + tdStr + "</tr>";
      //$('tboday').appendChild(tr); 
    }
    trStr =  $('tboday').innerHTML + trStr ;
    $('tboday').update(trStr);
  } 
  //是当天的TD加颜色，如果当天的（31日）大于指定月的最大天数 ，则默认为最后一天 
  var curDate = new Date();
  var day = curDate.getDate();
  var maxday = '<%=maxDay%>';
  if(day>maxday){
    day = maxday;
  }
  $("td_"+day).className = "TableRed";
  $("td_"+day).getElementsByTagName("div")[0].className = "TableRed";
}


function getDayOfWeek(dateStr){
  var day = new Date(Date.parse(dateStr.replace(/-/g, '/'))); //将日期值格式化
  //day.getDay();根据Date返一个星期中的某其中0为星期日
  return day.getDay(); 
}
function myAffair(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/affairnote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}
function newCalendar(year,month,day){
  //alert(year+","+month+","+day);
  projId = $("projId").value;
  var URL = "<%=contextPath%>/subsys/oa/profsys/out/baseinfo/news/calendar/newCalendar.jsp?&year="+year+"&month="+month+"&day="+day+"&projId="+projId;
  window.open(URL,"calendar","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=220,resizable=yes");
}
function My_Submit(){
  var year = document.getElementById("year").value;
  var month = document.getElementById("month").value;
  var maxDay = DayNumOfMonth(year,month);
  window.location="<%=contextPath%>/subsys/oa/profsys/out/baseinfo/showdetail/calendar.jsp?year="+year+"&month="+month;
}
function myNote(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status="+status;
  window.open(URL,"my_note","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=300,resizable=yes");
}
</script>
<body class="" topmargin="5" onload="doOnload();"  style="margin-right:18px;">
 <form name="form1" action="/general/calendar/arrange/month.php" style="margin-bottom:5px;">
<div class="small" style="clear:both;">
 <div style="float:left;">
   <input type="hidden" value="" name="BTN_OP">
   <input type="hidden" value="3" name="OVER_STATUS">
   <input type="hidden" value="26" name="DAY">
   <input type="button" value="今天" class="SmallButton1" title="今天" onclick="today();">
   <!-- 年 -->
<a href="javascript:set_year(-1)";  title="上一年"><img src="<%=imgPath%>/prevpreviouspage.png"></img></a>
   <a href="javascript:set_month(-1);" class="ArrowButtonR" title="上一周"><img src="<%=imgPath%>/previouspage.gif"></img></a>
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
 </div>
   <input type="hidden" name="projId" id="projId" value="">
 </div>
 </form>
<div id="listDiv"></div>
</body>
</html>