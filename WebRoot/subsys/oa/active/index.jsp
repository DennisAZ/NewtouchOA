<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*,yh.core.funcs.calendar.act.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  Date date = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat  dateFormat2 = new SimpleDateFormat("yyyy年-MM月-w周-dd日");
  SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormat4 = new SimpleDateFormat("MM/dd");
  SimpleDateFormat dateFormatWeek = new SimpleDateFormat("E");
  Calendar c = Calendar.getInstance();
  String week = dateFormatWeek.format(date);
  String dateStr = dateFormat.format(date);
  int year = Integer.parseInt(dateStr.substring(0,4));
  int month = Integer.parseInt(dateStr.substring(5,7));
  int day = Integer.parseInt(dateStr.substring(8,10));
  c.setTime(date);
  c.add(Calendar.DATE,-1) ;
  int weekthInt = c.get(Calendar.WEEK_OF_YEAR);
  c.add(Calendar.DATE,+1) ;
  //本身跳过来的
  String yearStr= request.getParameter("year");
  String weekStr = request.getParameter("week");
  if(yearStr!=null){
    year = Integer.parseInt(yearStr);
  }
  if(weekStr!=null){
    weekthInt = Integer.parseInt(weekStr);
  }
  //判断月份和星期几
  YHCalendarAct tca = new YHCalendarAct();
  Date beginDate ;
  Date endDate ;
  Calendar[] darr = tca.getStartEnd(year,weekthInt);
  beginDate = dateFormat1.parse(tca.getFullTimeStr(darr[0]));
  endDate = dateFormat1.parse(tca.getFullTimeStr(darr[1]));
  Calendar calendar = new GregorianCalendar();    
  calendar.setTime(beginDate);
  calendar.add(Calendar.DATE,+1) ;
  Date dateTemp2 = calendar.getTime();
  String date1 = dateFormat3.format(beginDate);
  String dateTempStr1 = dateFormat4.format(beginDate);
  String date2 = dateFormat3.format(dateTemp2);
  String dateTempStr2 = dateFormat4.format(dateTemp2);
  calendar.setTime(beginDate);
  calendar.add(Calendar.DATE,+2) ;
  Date dateTemp3 = calendar.getTime();
  String date3 = dateFormat3.format(dateTemp3);
  String dateTempStr3 = dateFormat4.format(dateTemp3);
  calendar.setTime(beginDate);
  calendar.add(Calendar.DATE,+3) ;
  Date dateTemp4 = calendar.getTime();
  String date4= dateFormat3.format(dateTemp4);
  String dateTempStr4 = dateFormat4.format(dateTemp4);
  calendar.setTime(beginDate);
  calendar.add(Calendar.DATE,+4) ;
  Date dateTemp5 = calendar.getTime();
  String date5= dateFormat3.format(dateTemp5);
  String dateTempStr5 = dateFormat4.format(dateTemp5);
  calendar.setTime(beginDate);
  calendar.add(Calendar.DATE,+5) ;
  Date dateTemp6 = calendar.getTime();
  String date6= dateFormat3.format(dateTemp6);
  String dateTempStr6 = dateFormat4.format(dateTemp6);
  calendar.setTime(beginDate);
  calendar.add(Calendar.DATE,+6) ;
  Date dateTemp7 = calendar.getTime();
  String date7= dateFormat3.format(dateTemp7);
  String dateTempStr7 = dateFormat4.format(dateTemp7);
  int maxWeek = 53;//YHCalendarAct.getMaxWeekNumOfYear(year);
 //out.print(date1+""+weekthInt);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>今天</title>
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
<script type="text/javascript">
var menuData1 = [{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">查看<div>',action:set_status,extData:'0'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">修改<div>',action:set_status,extData:'1'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">删除<div>',action:set_status,extData:'2'}
]
function set_status(){
  var status = arguments[2];
  var seqId =  arguments[1];
  if(status=='0'){
    activeNote(seqId);
  }
  if(status=='1'){
    var requestURL = "<%=contextPath%>/subsys/oa/active/editactive.jsp?seqId="+seqId;
    window.open(requestURL,'oa_sub_window','height=400,width=500,status=0,toolbar=no,menubar=no,location=no,left=300,top=200,scrollbars=yes,resizable=yes')  
  }
  if(status=='2'){
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/active/act/YHActiveAct/delActiveById.act?seqId="+seqId;
    var rtJson = getJsonRs(requestUrl);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.location.reload();
  }
}
function showMenu(event,seqId){
  var menu = new Menu({bindTo:''+seqId , menuData:menuData1 , attachCtrl:true});
  menu.show(event,seqId);
}
function print(){
  //alert("开发中...");
  myleft=(screen.availWidth-800)/2;
  window.open("print.jsp","","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=850,height=570,left="+myleft+",top=50");
}
var date1 = '<%=date1%>';
var date2 = '<%=date2%>';
var date3 = '<%=date3%>';
var date4 = '<%=date4%>';
var date5 = '<%=date5%>';
var date6= '<%=date6%>';
var date7 = '<%=date7%>';
var dateWeek = [date1,date2,date3,date4,date5,date6,date7];
function doOnload(){
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/active/act/YHActiveAct/selectActiveByWeek.act?beginDate="+date1+"&endDate="+date7;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  for(var i = 0;i<prcs.length;i++){
    var prc = prcs[i];
    var seqId = prc.seqId;
    var activeUser = prc.activeUser;
    var activeUserName = prc.activeUserName;
    var opUserId = prc.opUserId;
    var opUserIdName = prc.opUserIdName;
    var opDatetime = prc.opDatetime;
    var activeTime = prc.activeTime;
    var activeContent = prc.activeContent;
    var activeContentTitle = prc.activeContent;
    if(activeContent.length>10){
      activeContent = activeContent.substr(0,10) + "...";
    }
  
    var activeTimeStr = activeTime.substr(11,8);
    var date = activeTime.substr(0,10);
    var weekTemp = getWeekInt(dateWeek,date);
    var activeTimeInt = str_int(activeTimeStr);
    var dayTemp = getTimeOfDay(activeTimeInt);
    var content = new Element('div',{"title":""+activeContentTitle+"--"+opUserIdName}).update(""
         +activeTime.substr(11,5)+"&nbsp<a id='"+seqId+"' href='javascript:activeNote("
         +seqId    +")' onmouseover='showMenu(event,"+seqId+");' style='color:blue'>"
         +activeContent+"</a>");
    //var contentStr = "<div title="+avtiveContent+"--"+opUserIdName+"> "</div>";
    var userDesc = new Element('div','').update(""+activeUserName);
   // var userDescStr = "<div>"+activeUserName+"</div>";
    $("td_content_"+weekTemp+"_"+dayTemp).appendChild(content);
    $("td_userDesc_"+weekTemp+"_"+dayTemp).appendChild(userDesc);
  
  }
  //判断今天是星期几，并且加样式
  var curDate = new Date();
  var week = curDate.getDay();
  if(week ==0){
    week = 7;
  }
  for(var i = 1; i<4;i++){
    $("td_content_"+week+"_"+i).className = "TableContent";
    $("td_userDesc_"+week+"_"+i).className = "TableContent";
  }
}
function getWeekInt(week,date){
  var temp = 1;
  for(var i=0;i<week.length;i++){
    if(date==week[i]){
      temp = i+1;
    }
  }
  return temp;
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
function set_year(index){
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  if(parseInt(year)<=2000){
    year = parseInt(year);
  }else if(parseInt(year)>=2049){
    year = parseInt(year);
  }else{
    year = parseInt(year)+index;
  }
  window.location = "<%=contextPath%>/subsys/oa/active/index.jsp?year="+year+"&week="+week;
}
function set_week(index){
  var yearW = document.getElementById("year").value;
  var weekW = document.getElementById("week").value;
  var maxWeek = '<%=maxWeek%>';
  if(parseInt(weekW,10)+index>maxWeek){
    yearW = parseInt(yearW)+1;
    weekW = 1;
  }else if(parseInt(weekW,10)+index<1){
    yearW = parseInt(yearW)-1;
    weekW = '<%=YHCalendarAct.getMaxWeekNumOfYear(year-1)%>';
  }else{
    var weekW = parseInt(weekW)+index;
  }
  window.location = "<%=contextPath%>/subsys/oa/active/index.jsp?year="+yearW+"&week="+weekW;
}
function My_Submit(){
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  window.location = "<%=contextPath%>/subsys/oa/active/index.jsp?year="+year+"&week="+week;
}
function today(){
  window.location = "<%=contextPath%>/subsys/oa/active/index.jsp?";
}
function newActive(index){
  var date = dateWeek[index];
  var requestURL = "<%=contextPath%>/subsys/oa/active/addactive.jsp?date="+date;
  window.open(requestURL,'oa_sub_window','height=350,width=520,status=0,toolbar=no,menubar=no,location=no,left=300,top=200,scrollbars=yes,resizable=yes')
}
function activeNote(seqId){
  var requestURL = "<%=contextPath%>/subsys/oa/active/activenote.jsp?seqId="+seqId;
  window.open(requestURL,'oa_sub_window','height=250,width=280,status=0,toolbar=no,menubar=no,location=no,left=300,top=200,scrollbars=yes,resizable=yes')  

}
</script>
</head>
 
<body class="" topmargin="5" onload="doOnload();">
<div class="small" style="clear:both;">
 <div style="float:left;">
   <form name="form1" id="form1" action="#" style="margin-bottom:5px;">
   <input type="hidden" value="" name="BTN_OP">
   <input type="hidden" value="" name="OVER_STATUS">
   <input type="hidden" value="02" name="MONTH">
   <input type="hidden" value="25" name="DAY">
   <input type="button" value="今天" class="SmallButton1" title="今天" onclick="today();">
<!-- 年 -->
   <a href="javascript:set_year(-1)";  title="上一年"><img src="<%=imgPath%>/prevpreviouspage.png"></img></a>
   <a href="javascript:set_week(-1);" class="ArrowButtonR" title="上一周"><img src="<%=imgPath%>/previouspage.gif"></img></a>
   <select id="year" name="year"   style="height:22px;FONT-SIZE: 11pt;"  onchange="My_Submit();">
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
<!-- 周 -->
   <select id="week" name="week"  style="height:22px;FONT-SIZE: 11pt;"  onchange="My_Submit();;">
     <%
       for(int i=1;i<=maxWeek;i++){
         if(i==weekthInt){
           %>
           <option value="<%=i %>" selected="selected">第<%=i %>周</option>
             <%}else{ %>
           <option value="<%=i %>">第<%=i %>周</option>
             <%
                 }
        }
       %>
   </select>
   <a href="javascript:set_week(1);" class="ArrowButtonR" title="下一周"><img src="<%=imgPath%>/nextpage.gif"></img></a>
   <a href="javascript:set_year(1);" class="ArrowButtonRR" title="下一年"><img src="<%=imgPath%>/nextnextpage.png"></a>&nbsp;
      <input type="button" value="打印" class="SmallButton" title="打印单" onclick="print();">
 </div>
</div>
<br><br>
<table id="cal_table" class="TableBlock" width="100%" align="center">
<tr>
	<td rowspan="2" class="TableContent" align="center" nowrap>日（星期）</td>
	<td class="TableContent" align="center" colspan="2" nowrap>上午</td>
	<td class="TableContent" align="center" colspan="2" nowrap>下午</td>
	<td class="TableContent" align="center" colspan="2" nowrap>晚上</td>
</tr>
<tr>
	<td class="TableContent" align="center">内容</td>
	<td class="TableContent" align="center">出席</td>
  <td class="TableContent" align="center">内容</td>
	<td class="TableContent" align="center">出席</td>
	<td class="TableContent" align="center">内容</td>
	<td class="TableContent" align="center">出席</td>
</tr>
     <tr id="tbl_header" align="center" class="">
      <td id="td_week_1"  class="TableContent" nowrap>
          <a href="#" onclick="newActive(0);"><%=dateTempStr1 %>(星期一)</a>
      </td>
   <td valign=top nowrap id="td_content_1_1" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(0);">      
      </td>
      <td valign=top id="td_userDesc_1_1" class="TableData" align="left" width="15%" height="30"  title="双击新建日事务" ondblclick="newActive(0);">  </td>	
      <td valign=top nowrap id="td_content_1_2" class="TableData" align="left" width="15%" height="30"  title="双击新建日事务" ondblclick="newActive(0);">      
      </td>
      <td valign=top id="td_userDesc_1_2" class="TableData" align="left" width="15%" height="30"  title="双击新建日事务" ondblclick="newActive(0);">
      </td>	
      <td valign=top nowrap id="td_content_1_3" class="TableData" align="left" width="15%" height="30"  title="双击新建日事务"ondblclick="newActive(0);">     
      </td>
      <td valign=top id="td_userDesc_1_3" class="TableData" align="left" width="15%" height="30"  title="双击新建日事务" ondblclick="newActive(0);">
     </td>	
     </tr> 
     
     
     <tr id="tbl_header" align="center" >
      <td id="td_week_2"  class="TableContent" nowrap>
          <a href="#" onclick="newActive(1);"><%=dateTempStr2 %>(星期二)</a>
      </td>
      <td valign=top nowrap id="td_content_2_1" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(1);">
    </td>
      <td valign=top id="td_userDesc_2_1" class="TableData" align="left" width="15%" height="30"  title="双击新建日事务" ondblclick="newActive(1);">
       </td>	
      <td valign=top nowrap id="td_content_2_2" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(1);">
    </td>
      <td valign=top id="td_userDesc_2_2" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(1);">
      </td>	
      <td valign=top nowrap id="td_content_2_3" class="TableData" align="left" width="15%" height="30" ondblclick="newActive(1);" title="双击新建日事务">
    </td>
      <td valign=top id="td_userDesc_2_3" class="TableData" align="left" width="15%" title="双击新建日事务" height="30" ondblclick="newActive(1);">
     </td>	
     </tr> 
     <tr id="tbl_header" align="center">
      <td id="td_week_3"  class="TableContent" nowrap>
          <a href="#" onclick="newActive(2);"><%=dateTempStr3 %>(星期三)</a>
      </td>
      <td valign=top nowrap id="td_content_3_1" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(2);">
                
      </td>
      <td valign=top id="td_userDesc_3_1" class="TableData" align="left" width="15%" height="30"  title="双击新建日事务" ondblclick="newActive(2);">
      	      </td>	
      <td valign=top nowrap id="td_content_3_2" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(2);">
                
      </td>
      <td valign=top id="td_userDesc_3_2" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(2);">
      	      </td>	
      <td valign=top nowrap id="td_content_3_3" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(2);">
                
      </td>
      <td valign=top id="td_userDesc_3_3" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(2);">
      	      </td>	
     </tr> 
     
     <tr id="tbl_header" align="center">
      <td id="td_week_4"  class="TableContent" nowrap>
      <a href="#" onclick="newActive(3);"><%=dateTempStr4 %>(星期四)</a>
     </td>
      <td valign=top nowrap id="td_content_4_1" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(3);">
                
      </td>
      <td valign=top id="td_userDesc_4_1" class="TableData" align="left" width="15%" height="30"  title="双击新建日事务" ondblclick="newActive(3);">
      	      </td>	
      <td valign=top nowrap id="td_content_4_2" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(3);">
                
      </td>
      <td valign=top id="td_userDesc_4_2" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(3);">
      	      </td>	
      <td valign=top nowrap id="td_content_4_3" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(3);">
                
      </td>
      <td valign=top id="td_userDesc_4_3" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(3);">
      	      </td>	
     </tr> 
     
     
     <tr id="tbl_header" align="center">
      <td id="td_week_5"  class="TableContent" nowrap>
      <a href="#" onclick="newActive(4);"><%=dateTempStr5 %>(星期五)</a>
    </td>
      <td valign=top nowrap id="td_content_5_1" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(4);">
                
      </td>
      <td valign=top id="td_userDesc_5_1" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(4);">
      	      </td>	
      <td valign=top nowrap id="td_content_5_2" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(4);">
                
      </td>
      <td valign=top id="td_userDesc_5_2" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(4);">
      	      </td>	
      <td valign=top nowrap id="td_content_5_3" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(4);">
                
      </td>
      <td valign=top id="td_userDesc_5_3" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(4);">
      	      </td>	
     </tr> 
     
     <tr id="tbl_header" align="center">
      <td id="td_week_6"  class="TableContent" nowrap>
      <a href="#" onclick="newActive(5);"><%=dateTempStr6 %>(星期六)</a>
  </td>
      <td valign=top nowrap id="td_content_6_1" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(5);">
                
      </td>
      <td valign=top id="td_userDesc_6_1" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(5);">
      	      </td>	
      <td valign=top nowrap id="td_content_6_2" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(5);">
                
      </td>
      <td valign=top id="td_userDesc_6_2" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(5);">
      	      </td>	
      <td valign=top nowrap id="td_content_6_3" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(5);">
                
      </td>
      <td valign=top id="td_userDesc_6_3" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(5);">
     </td>	
     </tr> 
     
     <tr id="tbl_header" align="center">
      <td id="td_week_7"  class="TableContent" nowrap>
      <a href="#" onclick="newActive(6);"><%=dateTempStr7 %>(星期日)</a>
    </td>
      <td valign=top nowrap id="td_content_7_1" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(6);">
                
      </td>
      <td valign=top id="td_userDesc_7_1" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(6);">
      	      </td>	
      <td valign=top nowrap id="td_content_7_2" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(6);">
                
      </td>
      <td valign=top id="td_userDesc_7_2" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(6);">
      	      </td>	
      <td valign=top nowrap id="td_content_7_3" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(6);">
                
      </td>
      <td valign=top id="td_userDesc_7_3" class="TableData" align="left" width="15%" height="30" title="双击新建日事务" ondblclick="newActive(6);">
      	      </td>	
     </tr> 
  </table>

<br>
</body>
</html>
