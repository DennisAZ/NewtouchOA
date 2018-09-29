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
  int weekthInt = c.get(Calendar.WEEK_OF_YEAR);

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
    var activeTimeStr = activeTime.substr(11,8);
    var date = activeTime.substr(0,10);
    var weekTemp = getWeekInt(dateWeek,date);
    var activeTimeInt = str_int(activeTimeStr);
    var dayTemp = getTimeOfDay(activeTimeInt);
    var content = new Element('div',{"style":"float:left;width:200px;height:auto;white-space:normal;word-break:break-all"}).update(""
       // +"<div style='float:left;border-right:1px solid #000;width:200px;height:auto;'>"
    +activeTime.substr(11,5)+"&nbsp"+activeContent
    //+"<div style='float:left;border-right:1px solid #000;width:200px;height:auto;'>"

    );
    //var contentStr = "<div title="+avtiveContent+"--"+opUserIdName+"> "</div>";
    var userDesc = new Element('div').update(""+activeUserName);
   // var userDescStr = "<div>"+activeUserName+"</div>";
    $("td_content_"+weekTemp+"_"+dayTemp).appendChild(content);
    $("td_userDesc_"+weekTemp+"_"+dayTemp).appendChild(userDesc);
  
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
  window.location = "<%=contextPath%>/subsys/oa/active/print.jsp?year="+year+"&week="+week;

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

  window.location = "<%=contextPath%>/subsys/oa/active/print.jsp?year="+yearW+"&week="+weekW;
}
function My_Submit(){
  var year = document.getElementById("year").value;
  var week = document.getElementById("week").value;
  window.location = "<%=contextPath%>/subsys/oa/active/print.jsp?year="+year+"&week="+week;
}
function today(){
  window.location = "<%=contextPath%>/subsys/oa/active/print.jsp?";
}
</script>
 
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
     <font color='red'>右键->打印预览->页面设置->页眉页脚全设置为空（此行文字不会显示）</font>
   
 </div>
</div>
<br><br>

<div align="center" style="font-size:26px;font-family:'楷体';"><span ><%=year %></span>年第<span ><%=weekthInt %></span>周活动安排表</div>
<br><br>
<table id="cal_table" class="TableBlock" style="border:1px #000 solid;line-height:20px;font-size:9pt;border-collapse:collapse;padding:3px;font-size:12px;font-family:'宋体';" width="1000" align="center">
<tr>
	<td rowspan="2" class="TableData" align="center" nowrap>日（星期）</td>
	<td class="TableData" align="center" colspan="2" nowrap>上午</td>
	<td class="TableData" align="center" colspan="2" nowrap>下午</td>
	<td class="TableData" align="center" colspan="2" nowrap>晚上</td>
</tr>
<tr>
	<td class="TableData" align="center">内容</td>
	<td class="TableData" align="center">出席</td>
  <td class="TableData" align="center">内容</td>
	<td class="TableData" align="center">出席</td>
	<td class="TableData" align="center">内容</td>
	<td class="TableData" align="center">出席</td>
</tr>
     <tr id="tbl_header" align="center" class="">
      <td id="td_week_1"  class="TableData"  nowrap>
     <%=dateTempStr1 %>(星期一)
      </td>
   <td valign=top nowrap id="td_content_1_1" class="TableData" align="left"  width="100" height="30">      
      </td>
      <td valign=top id="td_userDesc_1_1" class="TableData" align="left" width="100" height="30">  </td>	
      <td valign=top nowrap id="td_content_1_2" class="TableData" align="left" width="100"  height="30">      
      </td>
      <td valign=top id="td_userDesc_1_2" class="TableData" align="left" width="100" height="30">
      </td>	
      <td valign=top nowrap id="td_content_1_3" class="TableData" align="left" width="100" height="30" >     
      </td>
      <td valign=top id="td_userDesc_1_3" class="TableData" align="left" width="100" height="30" >
     </td>	
     </tr> 
     
     
     <tr id="tbl_header" align="center" >
      <td id="td_week_2"  class="TableData"  nowrap>
       <%=dateTempStr2 %>(星期二)
      </td>
      <td valign=top nowrap id="td_content_2_1" class="TableData" align="left" width="15%" height="30" >
    </td>
      <td valign=top id="td_userDesc_2_1" class="TableData" align="left" width="15%" height="30" >
       </td>	
      <td valign=top nowrap id="td_content_2_2" class="TableData" align="left" width="15%" height="30" >
    </td>
      <td valign=top id="td_userDesc_2_2" class="TableData" align="left" width="15%" height="30" >
      </td>	
      <td valign=top nowrap id="td_content_2_3" class="TableData" align="left" width="15%" height="30" >
    </td>
      <td valign=top id="td_userDesc_2_3" class="TableData" align="left" width="15%" height="30" >
     </td>	
     </tr> 
     <tr id="tbl_header" align="center">
      <td id="td_week_3"  class="TableData"  nowrap>
       <%=dateTempStr3 %>(星期三)
      </td>
      <td valign=top nowrap id="td_content_3_1" class="TableData" align="left" width="15%" height="30" >
                
      </td>
      <td valign=top id="td_userDesc_3_1" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
      <td valign=top nowrap id="td_content_3_2" class="TableData" align="left" width="15%" height="30" >
                
      </td>
      <td valign=top id="td_userDesc_3_2" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
      <td valign=top nowrap id="td_content_3_3" class="TableData" align="left" width="15%" height="30" >
                
      </td>
      <td valign=top id="td_userDesc_3_3" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
     </tr> 
     
     <tr id="tbl_header" align="center">
      <td id="td_week_4"  class="TableData"  nowrap>
    <%=dateTempStr4 %>(星期四)
     </td>
      <td valign=top nowrap id="td_content_4_1" class="TableData" align="left" width="15%" height="30" >
                
      </td>
      <td valign=top id="td_userDesc_4_1" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
      <td valign=top nowrap id="td_content_4_2" class="TableData" align="left" width="15%" height="30" >
                
      </td>
      <td valign=top id="td_userDesc_4_2" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
      <td valign=top nowrap id="td_content_4_3" class="TableData" align="left" width="15%" height="30" >
                
      </td>
      <td valign=top id="td_userDesc_4_3" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
     </tr> 
     
     
     <tr id="tbl_header" align="center">
      <td id="td_week_5"  class="TableData" nowrap>
     <%=dateTempStr5 %>(星期五)
    </td>
      <td valign=top nowrap id="td_content_5_1" class="TableData" align="left" width="15%" height="30" >
                
      </td>
      <td valign=top id="td_userDesc_5_1" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
      <td valign=top nowrap id="td_content_5_2" class="TableData" align="left" width="15%" height="30" >
                
      </td>
      <td valign=top id="td_userDesc_5_2" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
      <td valign=top nowrap id="td_content_5_3" class="TableData" align="left" width="15%" height="30">
                
      </td>
      <td valign=top id="td_userDesc_5_3" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
     </tr> 
     
     <tr id="tbl_header" align="center">
      <td id="td_week_6"  class="TableData"  nowrap>
  <%=dateTempStr6 %>(星期六)
  </td>
      <td valign=top nowrap id="td_content_6_1" class="TableData" align="left" width="15%" height="30">
                
      </td>
      <td valign=top id="td_userDesc_6_1" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
      <td valign=top nowrap id="td_content_6_2" class="TableData" align="left" width="15%" height="30" >
                
      </td>
      <td valign=top id="td_userDesc_6_2" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
      <td valign=top nowrap id="td_content_6_3" class="TableData" align="left" width="15%" height="30" >
                
      </td>
      <td valign=top id="td_userDesc_6_3" class="TableData" align="left" width="15%" height="30" >
     </td>	
     </tr> 
     
     <tr id="tbl_header" align="center">
      <td id="td_week_7"  class="TableData"  nowrap>
    <%=dateTempStr7 %>(星期日)
    </td>
      <td valign=top nowrap id="td_content_7_1" class="TableData" align="left" width="15%" height="30" >
                
      </td>
      <td valign=top id="td_userDesc_7_1" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
      <td valign=top nowrap id="td_content_7_2" class="TableData" align="left" width="15%" height="30" >
                
      </td>
      <td valign=top id="td_userDesc_7_2" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
      <td valign=top nowrap id="td_content_7_3" class="TableData" align="left" width="15%" height="30" >
                
      </td>
      <td valign=top id="td_userDesc_7_3" class="TableData" align="left" width="15%" height="30" >
      	      </td>	
     </tr> 
  </table>

<br>
</body>
</html>